package com.bei.smartravel.chen;

public class configer {
	private configer(){
		db = Database.getDataBase();
	}
	
	public synchronized void config(int cmdType, String... args){
		switch(cmdType){
		case CMD_CONF_IP:
			confIp(args);
			break;
		case CMD_CONF_ID:
			confID(args[0]);
			break;	
		case CMD_CONF_PW:
			confPw(args[0]);
			break;
		case CMD_CONF_AU:
			confAu(args[0]);
			break;
		case CMD_CONF_MD:
			confMD(args[0]);
			break;
		case CMD_CONF_CR:
			confCR(args[0]);
			break;
		case CMD_CONF_NAME:
			confName(args[0]);
			break;
		case CMD_CONF_TM:
			confTm(args[0]);
			break;
		default:
			break;
		}
	}
	
	public synchronized String getonfValue(int cmdType, String... args){
		String tmpStr = "";
		switch(cmdType){
		case CMD_CONF_IP:
			tmpStr = getIP(args[0]);
			break;
		case CMD_CONF_ID:
			tmpStr = getID();
			break;	
		case CMD_CONF_PW:
			tmpStr = getPW();
			break;
		case CMD_CONF_AU:
			tmpStr = getAUState();
			break;
		case CMD_CONF_MD:
			tmpStr = getMDState();
			break;
		case CMD_CONF_CR:
			tmpStr = getCRState();
			break;
		case CMD_CONF_NAME:
			tmpStr = getName();
			break;
		case CMD_CONF_TM:
			tmpStr = getTm();
			break;
		default:
			break;
		}
		return tmpStr;
	}

	
	public synchronized static configer getConfiger(){
		if (Cfger == null)
			Cfger = new configer();
		return Cfger;
	}
	
	private synchronized void confIp(String... ipInfo){
		String updataStr = "";
		String sep = "";
		if (0 != ipInfo[0].length()){
			updataStr = String.format("%s serv_ip = '%s' ", sep, ipInfo[0]);
			sep = ", ";
		}
		
		if (0 != ipInfo[1].length()){
			updataStr += String.format("%s serv_port = '%s' ", sep, ipInfo[1]);;
		}

		if (0 == updataStr.length())
			return;
		
		String sql = String.format("UPDATE 'state' SET %s ", updataStr);
		db.execSQL(sql);
		TicketMger.getTicketMger().restart();
	}
	
	private synchronized void confID(String ID){
		if (0 == ID.length())
			return;
		String sql = String.format("UPDATE 'state' SET mac_id = '%s' ", ID);
		db.execSQL(sql);
	}
	
	private synchronized void confPw(String pw){
		if (0 == pw.length())
			return;
		String sql = String.format("UPDATE 'state' SET mac_pw = '%s' ", pw);
		db.execSQL(sql);
	}
	
	private synchronized void confAu(String isAuCheck){
		if (0 == isAuCheck.length())
			return;
		
		if ((!isAuCheck.equals("1")) && (!isAuCheck.equals("0"))) 
			return;
		
		String sql = String.format("UPDATE 'state' SET isAutoCheck = '%s' ", isAuCheck);
		db.execSQL(sql);
	}
	
	private synchronized void confCR(String isCR){
		if (0 == isCR.length())
			return;
		
		if ((!isCR.equals("1")) && (!isCR.equals("0"))) 
			return;
		
		String sql = String.format("UPDATE 'state' SET isCashRegister = '%s' ", isCR);
		db.execSQL(sql);
	}
	
	/**
	 * 已付、到付票显示选择
	 * @param isMD 1：显示未付票和已付票	0：仅显示未付票
	 */
	private synchronized void confMD(String isMD){
		if (0 == isMD.length())
			return;
		
		if ((!isMD.equals("2")) && (!isMD.equals("1")) && (!isMD.equals("0"))) 
			return;
		
		String sql = String.format("UPDATE 'state' SET isModifiable = '%s' ", isMD);
		db.execSQL(sql);
	}
	
	private synchronized void confName(String name){
		if (0 == name.length())
			return;
		
		String sql = String.format("UPDATE 'state' SET mac_name = '%s' ", name);
		db.execSQL(sql);
	}
	
	private synchronized void confTm(String tm){
		if (0 == tm.length())
			return;
		
		String sql = String.format("UPDATE 'state' SET timeSys = '%s' ", tm);
		db.execSQL(sql);
		
		TicketMger.getTicketMger().restart();
	}
	
	private synchronized String getName(){
		String sql = String.format("SELECT mac_name  FROM 'state' ");
		ITable re = db.rawQuery(sql);
		re.moveFirst();
		return re.getData("mac_name");
	}
	
	private synchronized String getID(){
		String sql = String.format("SELECT mac_id  FROM 'state' ");
		ITable re = db.rawQuery(sql);
		re.moveFirst();
		return re.getData("mac_id");
	}
	
	private synchronized String getIP(String addr){
		if (addr.equalsIgnoreCase(PORT)){
			String sql1 = String.format("SELECT serv_port  FROM 'state' ");
			ITable reP = db.rawQuery(sql1);
			reP.moveFirst();
			return reP.getData("serv_port");
		}
		
		String sql = String.format("SELECT serv_ip  FROM 'state' ");
		ITable re = db.rawQuery(sql);
		re.moveFirst();
		return re.getData("serv_ip");
	}
	
	private synchronized String getPW(){
		String sql = String.format("SELECT mac_pw  FROM 'state' ");
		ITable re = db.rawQuery(sql);
		re.moveFirst();
		return re.getData("mac_pw");
	}
	
	private synchronized String getAUState(){
		String sql = String.format("SELECT isAutoCheck  FROM 'state' ");
		ITable re = db.rawQuery(sql);
		re.moveFirst();
		return re.getData("isAutoCheck");
	}
	
	private synchronized String getCRState(){
		String sql = String.format("SELECT isCashRegister  FROM 'state' ");
		ITable re = db.rawQuery(sql);
		re.moveFirst();
		return re.getData("isCashRegister");
	}
	
	private synchronized String getMDState(){
		String sql = String.format("SELECT isModifiable  FROM 'state' ");
		ITable re = db.rawQuery(sql);
		re.moveFirst();
		return re.getData("isModifiable");
	}
	
	private synchronized String getTm(){
		String sql = String.format("SELECT timeSys  FROM 'state' ");
		ITable re = db.rawQuery(sql);
		re.moveFirst();
		return re.getData("timeSys");
	}
	
	public final static int CMD_CONF_IP = 0;//服务器IP
	public final static int CMD_CONF_ID = 1;//景区ID
	public final static int CMD_CONF_PW = 2;//密码
	public final static int CMD_CONF_CR = 3;//到付
	public final static int CMD_CONF_AU = 4;//自动检票
	public final static int CMD_CONF_MD = 5;//是否可以改票
	public final static int CMD_CONF_NAME = 6;//景区名称
	public final static int CMD_CONF_TM = 7;//通信间隔时间
	public final static String IP = "ip";
	public final static String PORT = "port";
	
	private Database db;
	
	private static configer Cfger = null;
	
}
