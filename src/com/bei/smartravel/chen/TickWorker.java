package com.bei.smartravel.chen;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.*;

import com.bei.smartravel.customization.MD5Encryption;
import com.bei.smartravel.customization.MyFormatDateTime;

//import com.bei.smartravel.wicket.configer;

public class TickWorker {
	/**
	 * ���ݶ����Źؼ��ʼ�Ʊ
	 * @param ���ڴ洢�����źͼ�Ʊ������List
	 */
	public synchronized void check(List<HashMap<String, Object> > orderInfo){
		long times = (new Date()).getTime()/1000;
		for (int i = 0; i < orderInfo.size(); i++){
			String sql = String.format("UPDATE 'order' SET bit_state='2', pay_state='0', " +
					"change_num='%s' , plantime='%d' " + "WHERE (order_id = %s)", 
					orderInfo.get(i).get(orderInfo.get(i).keySet().toArray()[0]).toString(), 
					times,
					orderInfo.get(i).keySet().toArray()[0]);
			db.execSQL(sql);
			
			db.execSQL(String.format("UPDATE 'order' SET remain_num='0'" + "WHERE (remain_num = %s)", 
					orderInfo.get(i).keySet().toArray()[0]));
		}
	}
	
	public ITable salesQuery(long startTime, long endTime) {
		String sql = String.format("SELECT `goods_name`, "
				+ "COUNT( `order_id` ) AS order_num, "
				+ "SUM( `goods_number` ) AS people_num "
				+ "FROM `order` "
				+ "WHERE `bit_state`=0 AND `pay_state`=0 AND "
				+ "`plantime` BETWEEN %s AND %s "
				+ "GROUP BY `goods_name`", Long.toString(startTime/1000), Long.toString(endTime/1000));
		ITable re = db.rawQuery(sql);
		return re;
	}
	
	/**
	 * ��ѯƱ��Ϣ
	 * @param sfz�����֤�ţ����ô�null;
	 * @param orderID�������Ų�ѯ�����ô�null;
	 * @param tickName��Ʊ�������ô�null;
	 * @param tickType��Ʊ��ʹ��״̬���������֣�UNUSED_TYPEδ�죬USED_TYPE�Ѽ죬ALL_TYPEȫ��
	 * @param tickStat������״̬���������֣�UNPAY_STATδ����PAY_STAT�Ѹ�
	 * @param beginTime����ʼʱ�䣬���ô�null��
	 * @param endTime������ʱ�䣬���ô�null��
	 * @param otherID������������
	 * @param isWicket���Ƿ����ڼ�Ʊ��ѯ
	 * @return ��ѯ������������ͣ�������ID��PayState��usedStat,���֤��sfz����Ϣ
	 */
	public synchronized ITable query(String sfz, String orderID, String tickName, int tickType, int tickStat
			, String beginTime, String endTime, String otherID){

		String whereCont = "where goods_number > '0' ";
		String nameCont = "";
		String timeCont = "";
		String sfzCont = "";
		String bitStatCont = "";
		
		if ((beginTime != null) && (endTime != null)){
			String[] date = beginTime.split("-");
			int yearA = Integer.valueOf(date[0]);
			int monthA = Integer.valueOf(date[1]) - 1;
			int dayA = Integer.valueOf(date[2]);
			
			date = endTime.split("-");
			int yearB = Integer.valueOf(date[0]);
			int monthB = Integer.valueOf(date[1]) - 1;
			int dayB = Integer.valueOf(date[2])+1;
			long timeA = (new GregorianCalendar(yearA, monthA, dayA)).getTime().getTime() / 1000;
			long timeB = (new GregorianCalendar(yearB, monthB, dayB)).getTime().getTime() / 1000;
			
			timeCont = String.format("plantime between '%d' AND '%d'", timeA - 1, timeB - 1);
			
			whereCont += " AND " + timeCont;
		}
		
		if (sfz != null){
			if(sfz.length() < 18) {
				sfzCont = String.format("(sfz = '%s' OR sfz = '%s')"
						, MD5Encryption.getMD5Encryption().encryption(sfz), sfz);
			} else {
				sfzCont = "(sfz like '%" + MD5Encryption.getMD5Encryption().encryption(sfz) + "%'"
						+ "OR sfz like '%" + sfz + "%')";
			}
			whereCont += " AND " + sfzCont;
		}
		
		if (orderID != null){
			sfzCont = String.format("order_id = '%s'", orderID);
			whereCont += " AND " + sfzCont;
		}
		
		if( !((sfz!=null)||(orderID!=null)) ) {
			whereCont += " AND goods_id<>2566 AND goods_id<>2628";
		}
		
		if (tickName != null){
			String goodsName = "";
			try{
				goodsName = URLEncoder.encode(tickName, "GBK");
			}
			catch (Exception e) {
				// TODO: handle exception
				log.logIns.logD("����Ʊ���ͳ���");
			}
			
			nameCont = String.format("goods_name = '%s'", goodsName);
			whereCont += " AND " + nameCont;
		}
		
		if(otherID != null) {
			sfzCont = String.format("useindex = '%s'", otherID);
			whereCont += " AND " + sfzCont;
		}
		
		String usedStat = ", CASE WHEN bit_state = '1' THEN 'δ��' ELSE '�Ѽ�' END AS 'usedStat'";
		if (UNPAY_STAT == tickStat){
//			bitStatCont = String.format("bit_state ='1' AND pay_state = '1'");
			bitStatCont = String.format("pay_state = '1'");
		} else if (PAY_STAT == tickStat) {
			switch (tickType) {	
			case USED_AND_UNUSED_TYPE:
				bitStatCont = String.format("pay_state = '0'");
				break;
		    case UNUSED_TYPE:
		    	bitStatCont = String.format("bit_state ='1' AND pay_state = '0'");
				break;
		    case USED_TYPE:
		    	bitStatCont = String.format("bit_state !='1' AND pay_state = '0'");
				break;
			default:
				break;
			}
		}
		
		if (bitStatCont != ""){
			whereCont += " AND " + bitStatCont;
		}
		
		// acs������		desc������
		String sql = String.format("SELECT order_id, goods_id, goods_number, sfz, goods_name, " +
								   "remain_num, change_num, usetime, bit_state, cat_id, plantime, " +
								   "price, pay_state, useindex, pay_num, usetime, pname" +
								   ", old_order_id %s FROM 'order' %s order by order_id asc", 
								   usedStat, whereCont);
		ITable re = db.rawQuery(sql);
		Vector<Map<String, String>> ticksInfo = new Vector<Map<String, String>>();
		int rowNum4Check = 0;
		while (re.moveNext()) {
			// log.logIns.logD("########cat_id is: " + re.getData("cat_id"));
			Map<String, String> ticks = new HashMap<String, String>();
			String goods_name = "";

			try {
				goods_name = URLDecoder.decode(re.getData("goods_name"), "GBK");

				ticks.put("����", goods_name);
//				ticks.put("����", re.getData("bit_state"));
				if(re.getData("bit_state").equalsIgnoreCase("1"))
					ticks.put("����", re.getData("goods_number"));
				else
					ticks.put("����", re.getData("change_num"));
				ticks.put("fatherID", re.getData("remain_num"));
				ticks.put("ID", re.getData("order_id"));
				ticks.put("GID", re.getData("goods_id"));
				ticks.put("sfz", re.getData("sfz"));
				ticks.put("plantime", re.getData("plantime"));
				ticks.put("price", re.getData("price"));
				ticks.put("isYearTick", isyearTick(re.getInt("cat_id")).toString());
				ticks.put("useindex", re.getData("useindex"));
				ticks.put("usedStat", re.getData("usedStat"));
				ticks.put("startime", re.getData("pay_num"));
				ticks.put("endtime", re.getData("usetime"));
				ticks.put("pname", re.getData("pname"));
				ticks.put("PayState", ((re.getData("pay_state")).equalsIgnoreCase("1")?"δ��":"�Ѹ�"));
				if( (re.getData("old_order_id") == null) 
						|| re.getData("old_order_id").equalsIgnoreCase("") ) {
					ticks.put("week", "0");
				} else {
					ticks.put("week", re.getData("old_order_id"));
				}
				ticks.put("valid", "true");
				
				if(isyearTick(re.getInt("cat_id"))) {	// ��Ʊ
					if(MyFormatDateTime.getMyFormatDateTime().GetYear(null)		// ��Ч
							== MyFormatDateTime.getMyFormatDateTime().GetYear(re.getData("plantime"))) {
						ticksInfo.insertElementAt(ticks, 0);
					} else if(MyFormatDateTime.getMyFormatDateTime().GetYear(null)		// ����
							>= MyFormatDateTime.getMyFormatDateTime().GetYear(re.getData("plantime"))) {
						ticks.put("PayState", "����");
						ticks.put("valid", "false");
						ticksInfo.add(ticks);
					} else if(MyFormatDateTime.getMyFormatDateTime().GetYear(null)		// δ����
							<= MyFormatDateTime.getMyFormatDateTime().GetYear(re.getData("plantime"))) {
						ticks.put("PayState", "δ����");
						ticks.put("valid", "false");
						ticksInfo.add(ticks);
					}
					rowNum4Check++;
				} else {	// ����Ʊ��
	////////////////================*************====================//////////////////
//					(re.getData("usetime") == null) || re.getData("usetime").equalsIgnoreCase("") || 
					// �ж���ʼʱ���Ƿ���ڣ�����ֹʱ�䣩
					if( (re.getData("pay_num") == null) || re.getData("pay_num").equalsIgnoreCase("")
							|| re.getData("pay_num").equalsIgnoreCase("0") ) {
						if( MyFormatDateTime.getMyFormatDateTime().CompareTime(null, re.getData("plantime"), false) ) {
							if( (re.getData("old_order_id") == null)
									|| re.getData("old_order_id").equalsIgnoreCase("")
									|| re.getData("old_order_id").equalsIgnoreCase("0")
									|| re.getData("old_order_id").equalsIgnoreCase(JudgeWeek()) ) {
								if((re.getData("remain_num") != null) 
										&& !re.getData("remain_num").equalsIgnoreCase("")
										&& !re.getData("remain_num").equalsIgnoreCase("0")) {
									ticks.put("PayState", "����");
									ticks.put("valid", "false");
								}
								ticksInfo.insertElementAt(ticks, 0);
							} else if(!re.getData("old_order_id").equalsIgnoreCase(JudgeWeek())) {
								ticks.put("valid", "false");
								ticksInfo.add(ticks);
							}
						} else {
							ticks.put("PayState", "����");
							ticks.put("valid", "false");
							ticksInfo.add(ticks);
						}
//						rowNum4Check++;
					} else {	// ������ֹʱ��
//						(re.getData("pay_num") != null) && !re.getData("pay_num").equalsIgnoreCase("") &&
						if( MyFormatDateTime.getMyFormatDateTime().CompareTime(null, re.getData("usetime"), true) ) {
							if( MyFormatDateTime.getMyFormatDateTime().CompareTimeLess(null
									, Long.toString( Long.valueOf(re.getData("pay_num")) )) ) {
								if( (re.getData("old_order_id") == null)
										|| re.getData("old_order_id").equalsIgnoreCase("")
										|| re.getData("old_order_id").equalsIgnoreCase("0")
										|| re.getData("old_order_id").equalsIgnoreCase(JudgeWeek()) ) {
									if((re.getData("remain_num") != null) 
											&& !re.getData("remain_num").equalsIgnoreCase("")
											&& !re.getData("remain_num").equalsIgnoreCase("0")) {
										ticks.put("PayState", "����");
										ticks.put("valid", "false");
									}
									ticksInfo.insertElementAt(ticks, 0);
								} else if( !re.getData("old_order_id").equalsIgnoreCase(JudgeWeek()) ) {
									ticks.put("valid", "false");
									ticksInfo.add(ticks);
								}
							} else {
								ticks.put("PayState", "δ����");
								ticks.put("valid", "false");
								ticksInfo.add(ticks);
							}
						} else {
							ticks.put("PayState", "����");
							ticks.put("valid", "false");
							ticksInfo.add(ticks);
						}
//						rowNum4Check++;
					}
					rowNum4Check++;
	////////////////================*************====================//////////////////
				}
			} catch (Exception e) {
				log.logIns.logD("����Ʊ���ͳ���");
			}
		}
		return new result(2, rowNum4Check, ticksInfo);
	}
	
	/**
	 * �жϼ���Ʊ��Ǽ���Ʊ
	 * 1 ƽ��Ʊ
	 * 2 ����Ʊ
	 * @return
	 */
	private String JudgeWeek() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
		String tmpStr = dateFormat.format(new Date());
		if(tmpStr.equalsIgnoreCase("������") || tmpStr.equalsIgnoreCase("������")) {
			return "2";
		}
		return "1";
	}
	
	private class result extends MapTable{		
		private result(int colNum, int rowNum, Vector<Map<String, String>> table){
			super(colNum, rowNum, table);
		}		
	}
	
	public static TickWorker getAction(){
		if (null == worker){
			worker = new TickWorker();
		}
		return worker;
	}
	
	/**
	 * �ж���Ʊ
	 * @param yearValue
	 * @return true����Ʊ    ===  false������Ʊ
	 */
	private Boolean isyearTick(int yearValue){
		if (yearValue != YEAR_TICK_TYPE)
			return false;
		return true;
	}
	
	private TickWorker(){
		db = Database.getDataBase();
	}
	//public final static int UNPAYED_TYPE = 4;
	public final static int USED_TYPE = 2;
	public final static int UNUSED_TYPE = 1;
	public final static int USED_AND_UNUSED_TYPE = 0;
	
	public final static int PAY_STAT = 3;
	public final static int UNPAY_STAT = 4;
	public final static int ALL_STAT = 5;
	public final static int NULL_STAT = 6;
	
	private Database db;
	private static TickWorker worker = null;
	
	private final static int YEAR_TICK_TYPE = 8;
}
