package com.bei.smartravel.chen;

import java.util.Map;
import java.util.Vector;
/**
 * 用于存储订单数据的键值对可变数组接口
 * @author YongKun
 */
public interface ITable{
	//String getData(int iRow, int iColumn);
	String getData(int iRow, String columnName);
	String getData(String columnName);
	int getInt(String columnName);
	int getColNum();
	int getRowNum();
	boolean moveFirst();
	boolean moveNext();
	boolean moveLast();
	boolean isEmpty();
}
/**
 * 对 存储订单数据的键值对可变数组接口的 实现
 * @author YongKun
 */
abstract class MapTable implements ITable{
	public String getData(String columnName){
		return table.elementAt(cur).get(columnName);
	}
	
	public String getData(int iRow, String columnName){
		return table.elementAt(iRow).get(columnName);
	}
	
	public int getInt(String columnName){
		return Integer.valueOf(table.elementAt(cur).get(columnName));
	}
	
	public int getColNum(){
		return colNum;
	}
	
	public int getRowNum(){
		return rowNum;
	}
	
	public boolean moveFirst(){
		if (isEmpty()){
			return false;
		}
		
		cur = 0;
		return true;
	}
	
	public boolean moveNext(){
		if (cur >= rowNum - 1){
			return false;
		}
		
		cur++;
		return true;
	}
	
	public boolean moveLast(){
		if (isEmpty()){
			return false;
		}
		
		cur = rowNum - 1;
		return true;
	}
	
	public boolean isEmpty(){
		if (0 != rowNum){
			return false;
		}
		return true;
	}
	
	protected MapTable(int colNum, int rowNum, Vector<Map<String, String>> table){
		this.cur = -1;
		this.colNum = colNum;
		this.rowNum = rowNum;
		this.table = table;
	}
	
	protected Vector<Map<String, String>> table;
	protected int cur, colNum, rowNum;
}
