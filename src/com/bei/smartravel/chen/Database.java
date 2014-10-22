package com.bei.smartravel.chen;

import android.database.sqlite.*;
import android.database.*;
import android.content.*;

import java.util.*;

public class Database{		
	public void execSQL(String sql, Object[] bindArgs) throws SQLException {
		mySQLiteDatabase.execSQL(sql, bindArgs);
	}
	
	public void execSQL(String sql) throws SQLException {
		mySQLiteDatabase.execSQL(sql);
	}
	/**
	 * 将检索到的数据以动态数组哈希表的形式保存到内存
	 * @param sql				// SQL语句
	 * @param selectionArgs		// 查找参数
	 * @return
	 * @throws SQLException
	 */
	public ITable  rawQuery (String sql, String[] selectionArgs) throws SQLException {
		int columnNum;	// 用于统计列数
		Cursor myCursor = mySQLiteDatabase.rawQuery(sql, selectionArgs);
		// 用以保存数据的动态数组形式哈希表
		Vector<Map<String, String>> myVectorData = new Vector<Map<String, String>>();
		DatabadeTable retTable = new DatabadeTable(myCursor.getColumnCount(), myCursor.getCount(), myVectorData);
		while (myCursor.moveToNext()) {
			Map<String, String> map = new HashMap<String,String> ();
			columnNum = myCursor.getColumnCount();
			for (int i =0; i < columnNum;i++){
				String coluName = myCursor.getColumnName(i);
				String strValue = myCursor.getString(i);
				map.put(coluName, strValue);
			}
			myVectorData.add(map);
		}
		myCursor.close();
		return retTable;
	}
	
	public ITable  rawQuery (String sql) throws SQLException {
		return rawQuery(sql, null);
	}
	
	private static class helper extends SQLiteOpenHelper{
		
		private boolean isFirst = true;
		
		public helper(Context context, String mbName){
			super(context, mbName, null, 1);
		}
		
		public synchronized void onCreate(SQLiteDatabase db){
			// 用于存储订单信息
			db.execSQL("CREATE TABLE 'order'(order_id INTEGER primary key UNIQUE, goods_id int, goods_number int, change_num int, remain_num int, sfz varchar(32), plantime int, bit_state int, goods_name varchar(12), useindex int, usetime int, cat_id int, price varchar(8), pay_state int, pay_num int, old_order_id int, order_mid int, pname varchar(64) );");
			// 用于存储已检订单
			db.execSQL("CREATE TABLE 'order_used'(order_id int, goods_id int, goods_number int, sfz varchar(32),plantime int, bit_state int, goods_name varchar(12),useindex int, usetime int, cat_id int, barcode varchar(18) );");
			// 终端网络通信信息
			//mac_name 景区名
			//isAutoCheck是否自动检票，1是，0否
			//isCashRegister是否到付，1是，0否
			//isModifiable是否可以改票，1是，0否
			if(isFirst) {
				db.execSQL("CREATE TABLE state(mac_id int, mac_name varchar(64), mac_pw varchar(18), mac_type int , mac_var int, user_pw varchar(20), order_id int, special_id int, use_index int, commu_time int,serv_ip varchar(20),serv_port varchar(20), isAutoCheck int, isCashRegister int, isModifiable int, timeSys int);");
				db.execSQL("insert into state values(00000,'杭州贝竹电子商务有限公司', '123456',1,123,'123456',1512500,58812,2,1322817275,'www.bzezt.com','1', '1', '0', '0', 10);");
			}
			// 终端本地通信信息
			db.execSQL("CREATE TABLE loc_state(mac_id int, mac_pw varchar(18), mac_type int , mac_var int, user_pw varchar(20), order_id int, special_id int, use_index int, commu_time int,serv_ip varchar(20),serv_port varchar(20));");
			db.execSQL("insert into loc_state values(1,'123456',2,122,'123456',54431,58832,2,1322817275,'192.168.1.150','9980');");
			db.execSQL("CREATE TABLE task(order_id int, action int);");
			// 向数据库写入终端信息
		}
		
		public synchronized void onUpgrade(SQLiteDatabase db, int oldV, int newV){
			db.execSQL("DROP TABLE IF EXISTS order_used");
			//db.execSQL("DROP TABLE IF EXISTS state");
			db.execSQL("DROP TABLE IF EXISTS loc_state");
			db.execSQL("DROP TABLE IF EXISTS task");
			db.execSQL("DROP TABLE IF EXISTS 'order'");
			isFirst = false;
			onCreate(db);
			intiTaskTable(1) ;
			isFirst = true;
		}
	}
	
	public static void init(Context context, String dbName) throws SQLException{
		if (null == databaseIns){
			databaseIns = new Database();
			databaseIns.dbHelper = new helper(context, dbName);
			databaseIns.mySQLiteDatabase = databaseIns.dbHelper.getWritableDatabase();
		}
	}
	
	public static void addColumn() {
		try {
			databaseIns.mySQLiteDatabase.execSQL("ALTER TABLE 'order' ADD COLUMN pname varchar(64);");
		} catch (SQLException e) {
			// TODO: handle exception
		}
	}
	
	public static void updateServerIP() {
		try {
			databaseIns.mySQLiteDatabase.execSQL("UPDATE 'state' SET serv_ip = '10.121.1.2' WHERE serv_ip = 'www.bzezt.com';");
		} catch (SQLException e) {
			// TODO: handle exception
		}
	}
	
	public static void intiTaskTable(int iMaxID) {
		try {
			databaseIns.mySQLiteDatabase.execSQL("insert into task values(1, 1);");
			updateSTaskTable(iMaxID);
		} catch (SQLException e) {
			// TODO: handle exception
		}
	}
	
	public static void updateSTaskTable(int iTmp) {
		try {
			databaseIns.mySQLiteDatabase.execSQL(String.format("UPDATE 'task' SET action = '%d' WHERE order_id = 1;", iTmp));
		} catch (SQLException e) {
			// TODO: handle exception
		}
	}
	
	public synchronized void reset(){
		dbHelper.onUpgrade(databaseIns.mySQLiteDatabase, 1, 2);
	}
	
	public synchronized void close(){
		mySQLiteDatabase.close();
	}
	
	public static Database getDataBase() throws SQLException{
		return databaseIns;	
	}
	
	private Database() {
	}
	
	private class DatabadeTable extends MapTable{
		/**
		 * @param colNum	// 列编号
		 * @param rowNum	// 行编号		
		 * @param table		// 键值表
		 */
		private DatabadeTable(int colNum, int rowNum, Vector<Map<String, String>> table){
			super(colNum, rowNum, table);
		}
	}
	
	private SQLiteDatabase mySQLiteDatabase;
	helper dbHelper;
	private static Database databaseIns= null;
}

