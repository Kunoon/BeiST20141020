package com.bei.smartravel.chen;
//	bit_state门票状态：
//	0 表示已经更新上传
// 	1 刚下载的门票
//	2 已检但未上传
//	3 已经退的门票但还未上传
//	4 已经退的票已上传
////////////////////////////////////////////////////
////////////////////////////////////////////////////
//	pay_state付款状态：
//	0为已付，1为未付
////////////////////////////////////////////////////
////////////////////////////////////////////////////
//	cat_id门票类型：
//	1为普通票，8为年票
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import android.os.Handler;
import android.util.*;

import java.util.regex.*;
import java.util.*;

import com.bei.smartravel.customization.MD5Encryption;
import com.bei.smartravel.customization.MyFormatDateTime;

public class TicketMger extends Thread {
	/**
	 * 构造函数
	 */
	private TicketMger(){
		httpUrlConMy = new HttpUrlConMy();
		myDatabase = Database.getDataBase();
		parser = new DateParser();
		myTicketMgerStartBool = true;
		myCommunicateTime = 12;
	}
	
	/**
	 * 启动通信线程
	 */
	public synchronized void start(){
		myTicketMgerStartBool = true;
		thisThread = new Thread(this);
		thisThread.start();
	}
	
	/**
	 * 获得TicketMger对象
	 * @return TicketMger对象
	 */
	public synchronized static TicketMger getTicketMger(){
		return tmger;
	}
	
	public synchronized static TicketMger getTicketMger(Handler handler){
		myHandler = handler;
		return tmger;
	}
	
	/**
	 * 重启通信线程
	 */
	public synchronized void restart(){
		cancel();
		start();
	}
	
	public void run() {
		for (;myTicketMgerStartBool;) {
			try {
				doWork();
				Thread.sleep(myCommunicateTime * 1000);
			}
			catch (InterruptedException e) {
				Log.e("debug", e.toString());
			}
		}
		myHandler.obtainMessage(1).sendToTarget();
	}
	
	public synchronized void cancel(){
		myTicketMgerStartBool = false;
//		try {
//			thisThread.join();
//		} 
//		catch (InterruptedException e) {
//			Log.e("debug", e.toString());
//		}
	}
	
	private synchronized void doWork(){
		RefreshTicketData();
		uploadChenkedTics();
	}
	
	/**
	 * 用于更新票务数据
	 */
	private synchronized void RefreshTicketData(){
		String cont = encodeParams4SToC();
		Log.e("DDD", "####### DOWN SEND START #######\n" + cont + "\n####### DOWN SEND END #######");
		List<String> tmpList = httpUrlConMy.getResult(getURLStr("/if/mac/stoc_new"), cont);
		String data = tmpList.get(1);
		Log.e("DDD", "####### DOWN RECV START ####### " + tmpList.get(0) + "\n" + data + "\n####### DOWN RECV END ####### ");
		if( data.length() == 0 ) {
			myHandler.obtainMessage(0, "断").sendToTarget();
		} else {
			Log.e("DDD", "####### DOWN RECV START #####--> " + data.length() + " <--##### DOWN RECV END ####### ");
			if( !(tmpList.get(0).length() == 0) && !tmpList.get(0).equals("0") ) {
				if(!MyFormatDateTime.getMyFormatDateTime().GetDate(null).equalsIgnoreCase(
						MyFormatDateTime.getMyFormatDateTime().GetDate(tmpList.get(0)))) {
					myHandler.obtainMessage(2).sendToTarget();
				}
			}
			myHandler.obtainMessage(0, "通").sendToTarget();
			if(data.contains(String.format("[pack_type]<101>[cid]<%s>[cpasswd]<123456>[rc]"
					, configer.getConfiger().getonfValue(configer.CMD_CONF_ID, ""))) || data.contains(String.format("[specialid]")) ) {
				parser.reset(data, myDatabase.rawQuery("SELECT action  FROM 'task' WHERE order_id = 1;").getData(0, "action"));
				ITable oredr = parser.parse();
				dbAction(oredr);
			}
		}
	}

	/**
	 * 整理并上传检票数据
	 */
	private synchronized void uploadChenkedTics(){
		String Tics = encode4CToS();
		if ("" == Tics && special_id < (tmpSid+1) ) {
//			log.logIns.logD("没有要上传的票");
			return;
		}
		Log.e("DDD", "####### UP SEND START #######\n" + Tics + "\n####### UP SEND END #######");
		String data = httpUrlConMy.getResult(getURLStr("/if/mac/ctos_new"), Tics).get(1);
		Log.e("DDD", "####### UP DOWN RECV START #######\n" + data + "\n####### UP RECV EN ####### ");
//		[pack_type]<102>[mac_id]<67701>[mac_pw]<123456>[rc]<0>0
		Log.e("UUU", "\n@@@@@@@ UP DOWN RECV START @@@@@--> " + data.length() + " <--@@@@@ UP RECV END @@@@@@@");
		if(data.contains(String.format("[pack_type]<102>[mac_id]<%s>[mac_pw]<123456>[rc]<0>"
				, configer.getConfiger().getonfValue(configer.CMD_CONF_ID, "")))) {
	        db_update_bit(Tics);
		}
	}

	/**
	 * 更新上传后修改本地门票状态
	 */
	private void db_update_bit(String str){
		String [] str0 = GetOrderId4Update(str);
		String sql_sp = null;
		
		if ( special_id > tmpSid ) {
			sql_sp = String.format("UPDATE 'order' SET bit_state='0', pay_state='0', change_num=goods_number  WHERE order_id = %d ", special_id );
			myDatabase.execSQL(sql_sp);
			sql_sp = String.format("UPDATE 'order' SET remain_num='0'" + "WHERE (remain_num = %s)", special_id);
			myDatabase.execSQL(sql_sp);
			special_id = tmpSid;
		}
		for(int i = 1; i < str0.length; i++) {
			myDatabase.execSQL(String.format("UPDATE 'order' SET bit_state='0' WHERE bit_state='2' AND order_id = %d", Integer.valueOf(str0[i]) ));
		}
	}
	
	private String [] GetOrderId4Update(String str) {
		String [] strA = str.split("&id%5B%5D=");
		for(int i = 1; i < strA.length; i++) {
			strA[i] = strA[i].split("&ut%5B%5D=")[0];
		}
		return strA;
	}
	
	/**
	 * 编码硬件终端信息
	 * @return String
	 */
	private String encodeMac(){
		ITable retState, retOrder;
		retState = myDatabase.rawQuery("select mac_id, mac_pw, mac_type, mac_var, user_pw, order_id," +
						"special_id, use_index, commu_time from state limit 1");
		if (retState.isEmpty()){
			return "";
		}
		
//		retOrder = myDatabase.rawQuery("SELECT order_id  FROM 'order' ORDER BY order_id DESC LIMIT 1");
		retOrder = myDatabase.rawQuery("SELECT action  FROM 'task' WHERE order_id = 1;");
		String order_id;
		
//		order_id = retOrder.isEmpty()? "1" : retOrder.getData(0, "order_id");
		order_id = retOrder.isEmpty()? "1" : retOrder.getData(0, "action");
		System.out.println("###@@@### ==>　" + order_id);
		
		String temp = String.format("&mac_id=%s&mac_pw=%s&mac_type=%s&mac_ver=%s" + 
									"&order_id=%s&special_id=%d&commu_time=%s&user_pw=%s&use_index=%s",
									retState.getData(0, "mac_id"),
									retState.getData(0, "user_pw"),	// mac_pw  user_pw
									retState.getData(0, "mac_type"),
									retState.getData(0, "mac_var"),
									order_id,
									special_id,	
									retState.getData(0, "commu_time"),
									retState.getData(0, "user_pw"),
									retState.getData(0, "use_index"));
		return temp + "&adv_id=1000000";
	}

	private String getURLStr(String url) {
		ITable result = myDatabase.rawQuery("select serv_ip from state");
		String tmpStr = "http://" + result.getData(0, "serv_ip") + url;
		return tmpStr;
	}
	
	/**
	 * 编码贝竹信息请求属性
	 * @return 返回请求信息字符串
	 */
	private String encodeParams4SToC(){
		String temp = "god=girl" + encodeMac();
		return temp;
	}

	/**
	 * 编码网站信息同步属性
	 * @return	返回 上传请求及数据字符串
	 */
	private String encode4CToS(){
		String temp = "";
		// bit_state=2 查找 已检但未上传 门票
		ITable Tics = myDatabase.rawQuery("SELECT order_id,goods_id,plantime" +
				", change_num FROM 'order'" + " WHERE bit_state='2'");
		
		if ( Tics.isEmpty() && special_id < (tmpSid+1) ) {
			return "";
		}
		
		int ticNum = Tics.getRowNum();	// 用于记录上传的门票数量
		while(Tics.moveNext()){
			temp += "&id%5B%5D=" + Tics.getData("order_id") +
					"&ut%5B%5D=" + Tics.getData("plantime") +
					"&ac%5B%5D=8" + "&num%5B%5D=" + Tics.getData("change_num");
		}
		
		String date =  Long.toString((new Date()).getTime());
		// bit_state=3 已经退的门票但还未上传
		Tics = myDatabase.rawQuery("select order_id from 'order' WHERE bit_state = '3' ");
		ticNum = ticNum + Tics.getRowNum();
		while(Tics.moveNext()) {
			temp += "&specialid%5B%5D=" + Tics.getData("order_id") + 
					"&ut%5B%5D=" + date + "&ac%5B%5D=8";
		}
		// bit_state=5终端付款通知网站
		Tics = myDatabase.rawQuery("SELECT order_id, goods_number FROM 'order' WHERE goods_number > '0' " +
				"AND bit_state = '5' AND pay_state = '0'");
		ticNum = ticNum + Tics.getRowNum();
		while(Tics.moveNext()){
			temp += "&id%5B%5D=" + Tics.getData("order_id") +
					"&ut%5B%5D=" + date + "&ac%5B%5D=8" + 
					"&num%5B%5D=" + Tics.getData("goods_number");
		}
		temp += String.format("&rc=%d", ticNum) + encodeMac();
		return temp;
	}
	/**
	 * 将下载到内存的数据订单存入数据库
	 * @param orders
	 */
	private void dbAction(ITable orders){
		long iPlantime = 0;
		String sql = null;
		
		if (orders.isEmpty())
			return;
		
		while(orders.moveNext()){
			// 中央电视塔6类票延期30天
			if( orders.getData("goods_id").equalsIgnoreCase("1461")
					|| orders.getData("goods_id").equalsIgnoreCase("1462")
					|| orders.getData("goods_id").equalsIgnoreCase("1463")
					|| orders.getData("goods_id").equalsIgnoreCase("1464")
					|| orders.getData("goods_id").equalsIgnoreCase("1465")
					|| orders.getData("goods_id").equalsIgnoreCase("1466") )
				iPlantime = Integer.valueOf(orders.getData("plantime")) + 2592000;
			else
				iPlantime = Integer.valueOf(orders.getData("plantime"));

			int pay_state = 0;	// 0为已付，1为未付
			int cat_id = orders.getInt("cat_id");	// 1为普通票，8为年票
			if ((orders.getInt("pay_state")&8) != 0 && cat_id != 8)
				pay_state = 1;
			int oldOrdId = orders.getInt("old_order_id");
			
			String EnID = "";
			String [] tmpStrAr = orders.getData("sfz").split(",");
			for(int i = 0; i < tmpStrAr.length; i++) {
				if(tmpStrAr[i].length() <= 18)
					EnID += MD5Encryption.getMD5Encryption().encryption(tmpStrAr[i]) + ",";
				else
					EnID += tmpStrAr[i] + ",";
			}
			EnID = EnID.substring(0, EnID.length()-1);
			// 处理网站推送订单
			ITable tmpRe = myDatabase.rawQuery(String.format("SELECT bit_state FROM 'order' WHERE (order_id = '%d' );", orders.getInt("order_id")));
			if(tmpRe.getRowNum() != 0) {
				tmpRe = myDatabase.rawQuery(String.format("SELECT bit_state FROM 'order' WHERE (order_id = '%d' );", oldOrdId));
				if( (tmpRe.getRowNum() == 0) || ( (tmpRe.getRowNum() != 0) && !tmpRe.getData("bit_state").equals("1") ) ) {
					oldOrdId = 0;
				}
				String str00 = String.format("UPDATE 'order' SET order_id='%s', goods_id='%s', goods_number='%s', " +
						"sfz='%s', plantime='%d', bit_state=1, goods_name='%s', cat_id='%s', price='%s', pay_state='%s', " +
						"useindex='%s', pay_num='%s', usetime='%s', pname='%s', old_order_id='%s', remain_num='%d' WHERE (order_id = %s);", 
						orders.getData("order_id"), orders.getData("goods_id"), orders.getData("goods_num"),
						EnID, iPlantime, orders.getData("goods_name"), cat_id, orders.getData("price"),
						pay_state, orders.getData("other_id"), orders.getData("startime"),	// 有效起始时间
						orders.getData("endtime").equalsIgnoreCase("0")
								?Long.toString( MyFormatDateTime.getMyFormatDateTime()
										.dateToSecond("yyyyMMddHHmmss", "21000101000000") )
								:orders.getData("endtime"),		// 有效截止时间
						orders.getData("pname"),		// 订单来源
						orders.getData("week"),
						oldOrdId, orders.getData("order_id") );
				myDatabase.execSQL(str00);
				continue;
			}
			// 处理常规订单
			if(oldOrdId > 0) {
				if(orders.getInt("order_mid") == 2) {
					sql = String.format("DELETE FROM 'order' WHERE order_id = '%d' ", oldOrdId);	// 网站改票、退票
					myDatabase.execSQL(sql);
					oldOrdId = 0;
				} else {
					sql = String.format("UPDATE 'order' SET goods_number=goods_number - %d WHERE (order_id = '%d' ) ", 
								orders.getInt("goods_num"), oldOrdId);	// 先撤销老订单（单景区多景点）
					myDatabase.execSQL(sql);
					ITable re = myDatabase.rawQuery(String.format("SELECT bit_state FROM 'order' WHERE (order_id = '%d' );", oldOrdId));
					if(re.getRowNum() == 0 || !re.getData(0, "bit_state").equalsIgnoreCase("1")) {
						oldOrdId = 0;
					}
				}
			}
			
			if(orders.getData("action").equalsIgnoreCase("2")) {
				sql = String.format("insert into 'order' (order_id, goods_id, goods_number, sfz, plantime, " +
						"bit_state, goods_name, cat_id, price, pay_state, " +
						"useindex, pay_num, usetime, pname, old_order_id, remain_num)" +
						"values('%s', '%s', '%s', '%s', '%d', 1, '%s', " +
						"'%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%d')",
					orders.getData("order_id"),
					orders.getData("goods_id"),
					orders.getData("goods_num"),
//					(orders.getData("sfz").length() > 18) ? orders.getData("sfz")	// MD5加密身份证号码
//							: MD5Encryption.getMD5Encryption().encryption(orders.getData("sfz")),
					EnID,
					iPlantime,
					orders.getData("goods_name"),
					cat_id,
					orders.getData("price"),
					pay_state,
					orders.getData("other_id"),
					orders.getData("startime"),	// 有效起始时间
//					orders.getData("endtime").equalsIgnoreCase("0")
//					?Long.toString( MyFormatDateTime.getMyFormatDateTime()
//							.dateToSecond("yyyyMMddHHmmss", "21000101000000") )
//					:orders.getData("endtime"),		// 有效截止时间   86399
					orders.getData("endtime").equalsIgnoreCase("0")
					?Long.toString(iPlantime+86399) : orders.getData("endtime"),		// 有效截止时间
					orders.getData("pname"),		// 订单来源
					orders.getData("week"),
					oldOrdId );
			} else {
				sql = String.format("insert into 'order' (order_id, goods_id, goods_number, sfz, plantime, " +
						"bit_state, goods_name, cat_id, price, pay_state)" +
						"values('%s', '%s', '%s', '%s', '%d', 1, '%s', '%s', '%s', '%s')",
					orders.getData("order_id"),
					orders.getData("goods_id"),
					orders.getData("goods_num"),
//					(orders.getData("sfz").length() > 18) ? orders.getData("sfz")	// MD5加密身份证号码
//							: MD5Encryption.getMD5Encryption().encryption(orders.getData("sfz")),
					EnID,
					iPlantime,
					orders.getData("goods_name"),
					cat_id,
					orders.getData("price"),
					pay_state );
			}
			myDatabase.execSQL(sql);
		}
	}
	
	public int tmpSid = 141020;
	public int special_id = tmpSid;		//特殊订单号
	
	private static Handler myHandler;
	private int myCommunicateTime;	// 通信间隔时间 （单位：S）
	private boolean myTicketMgerStartBool;
	private Database myDatabase;
	private DateParser parser;
	Thread thisThread;
	private static final TicketMger tmger = new TicketMger();
	private HttpUrlConMy httpUrlConMy;
}

///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
/**
 * 通信数据解析器类
 * @author YongKun
 */
class DateParser {
	public DateParser(){
		myMatcher = Pattern.compile("").matcher("");
		initTicketKeyOfAC();
	}
	/**
	 * 将数据传入解析器
	 * @param input 需要解析的数据
	 */
	public void reset(String input, String foID){
		fromOrderID = foID;
		str = input;
		myMatcher.reset(input);
	}
	
	/**
	 * 解析接收到的数据，格式化为所需数据
	 * @return 返回MapTable格式
	 */
	public ITable parse() {
		Vector<Map<String, String>> orderList = new Vector<Map<String, String>>();
		int rowNum = 0;
		int colNum = 16;
		int curRowStart;
		int curRowEnd = str.indexOf("#") + 1;
		int ordersNum = -1;
		if(str.contains(String.format("[specialid]"))) {
			ordersNum = 0;
		} else {
			ordersNum = Integer.valueOf(str.substring(str.indexOf("[cpasswd]<123456>[rc]"), str.length()).split(">")[1].split("<")[1]);
		}
		
		if (curRowEnd < 1) {
			return new OrderTable(0, 0, orderList);
		}
		// 将specialid解析出来
		String specialid_temp = null;
		int index_sp = 0;

		if( (index_sp = str.indexOf( "specialid", curRowEnd )) > 1
				&& str.indexOf( "}", index_sp ) > 1 ) {
			specialid_temp = str.substring( str.indexOf("<",index_sp)+1, str.indexOf(">",index_sp) );
			TicketMger.getTicketMger().special_id = Integer.valueOf(specialid_temp);
		}

		for (;(curRowStart = str.indexOf('{', curRowEnd)) > 0;) {
			curRowEnd = str.indexOf('}', curRowStart);
			if (curRowEnd < 0) {
				break;
			}
			myMatcher.region(curRowStart, curRowEnd);
			
			if (str.startsWith("[specialid]", curRowStart)) {
				if (str.charAt(curRowEnd + 1) != '0')
					continue;
			}
			String action = getValueOfKey("[ac]");
			Map<String, String> acInfo = ticketKeyOfAC.get(action);
			if (acInfo == null) {
				log.logIns.logD("acInfo is null");
				break;
			}
			
			Map<String, String> orderRow = new HashMap<String, String>();
			
			for (String key : acInfo.keySet()) {
				try {
					String value = getValueOfKey(acInfo.get(key));
					if(key.equals("order_id")) {
//						if(Integer.valueOf(value) <= Integer.valueOf(fromOrderID)) {
//							Database.updateSTaskTable(Integer.valueOf(value));
//						}
						Database.updateSTaskTable(Integer.valueOf(value));
					}
//					if ("" == value){
//						orderRow.clear();
//						break;
//					}
					orderRow.put(key, value);
					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("解析异常");
				}
			}
			if (orderRow.isEmpty()) {
				continue;
			}
			orderRow.put("action", action);
			orderList.add(orderRow);
			rowNum++;
		}
		System.out.println("ordersNum is " + ordersNum + " AND rowNum is " + rowNum);
		if(rowNum != ordersNum) {
			Database.updateSTaskTable(Integer.valueOf(fromOrderID));
			return new OrderTable(colNum, 0, orderList);
		}
		return new OrderTable(colNum, rowNum, orderList);
	}
	
	public String getValueOfKey(String key) {
		String value = "";
		
		String strPattern = "\\" +  key + "<([^\\[\\]\\}\\{]+?)>";
		myMatcher.usePattern(Pattern.compile(strPattern));
		
		if (myMatcher.find()) {
			value = myMatcher.group(1);
//           调试打开	
//			Properties props = System.getProperties();
//			log.logIns.logD(props.getProperty("os.version"));
//			if ("[name]" == key){
//				try {
//					log.logIns.logD(URLDecoder.decode(value, "GBK"));
//				} catch (Exception e){
//					log.logIns.logD("asdfasd");
//				}
//			}
		}
		
		return value;
	}
	
	private class OrderTable extends MapTable {
		/**
		 * 用于存储订单的数据结构
		 * @param colNum 列数
		 * @param rowNum 行数
		 * @param table  订单数据
		 */
		private OrderTable(int colNum, int rowNum, Vector<Map<String, String>> table){
			super(colNum, rowNum, table);
		}
	}
	
	/**
	 * 初始化订单表数据结构
	 */
	private void initTicketKeyOfAC() {
//		Map<String, String> temp1 , temp4, temp0, temp2;
		Map<String, String> temp1, temp2;
		temp1 = new HashMap<String, String>();
		temp1.put("order_id", "[id]");
		temp1.put("goods_num", "[num]");
		temp1.put("goods_id", "[type]");
		temp1.put("goods_name", "[name]");
		temp1.put("sfz", "[sfz]");
		temp1.put("plantime", "[pt]");
		temp1.put("cat_id", "[catid]");
		temp1.put("pay_state", "[bitwan]");
		temp1.put("old_order_id", "[oid]");
		temp1.put("order_mid", "[mid]");
		temp1.put("price", "[price]");
//		temp1.put("other_id", "[other_id]");
//		
//		temp4 = new HashMap<String, String>();
//		temp4.put("order_id", "[id]"); 待检
//		
		temp2 = new HashMap<String, String>();
		temp2.put("order_id", "[id]");
		temp2.put("goods_num", "[num]");
		temp2.put("goods_id", "[type]");
		temp2.put("goods_name", "[name]");
		temp2.put("sfz", "[sfz]");
		temp2.put("plantime", "[pt]");
		temp2.put("cat_id", "[catid]");
		temp2.put("pay_state", "[bitwan]");
		temp2.put("old_order_id", "[oid]");
		temp2.put("order_mid", "[mid]");
		temp2.put("price", "[price]");
		temp2.put("other_id", "[other_id]");
		temp2.put("startime", "[st]");
		temp2.put("endtime", "[et]");
		temp2.put("pname", "[pname]");
		temp2.put("week", "[week]");
//		
//		temp0 = temp4;
		
		ticketKeyOfAC.put("1", temp1);
//		ticketKeyOfAC.put("4", temp4);
//		ticketKeyOfAC.put("0", temp0);
		ticketKeyOfAC.put("2", temp2);
	}
	private String fromOrderID = "";
	/**
	 * 需要解析的数据
	 */
	private String str;
	/**
	 * 用于正则表达式判断的类
	 */
	private Matcher myMatcher;
	/**
	 * 存储数据库字段名，作为数据解析时的KEY
	 */
	static final Map<String, Map<String, String>> ticketKeyOfAC = new HashMap<String, Map<String, String>>();
}