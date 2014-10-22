package com.bei.smartravel.chen;

import android.util.*;

public class log {
	public static void init(int level){
		if (logIns != null)
			return;
		logIns = new log();
		logIns.level = level;
	}
	public static log getLogInstance(){
		return logIns;
	}
	
	private final void logM(String message, int level){
		if (level < this.level){
			return;
		}
		
		String tag = null;
		switch(level){
		case DEBUG_LOG:
			tag = "debug";
			break;
		case INFO_LOG:
			tag = "info";
			break;
		case WARNING_LOG:
			tag = "warning";
			break;
		case ERROR_LOG:
			tag = "error";
			break;
		}
		
		Log.i(tag, message);
	}
	
	public void logD(String message){
		logM(message, DEBUG_LOG);
	}
	
	public void logI(String message){
		logM(message, INFO_LOG);
	}
	
	public void logW(String message){
		logM(message, WARNING_LOG);
	}
	
	public void logE(String message){
		logM(message, ERROR_LOG);
	}
	
	public void setLogLevel(int level){
		this.level = level;
	}
	
	private log(){
		level = INFO_LOG;
	}
	
	public static final int ERROR_LOG = 3, WARNING_LOG = 2, INFO_LOG = 1, DEBUG_LOG = 0;
	
	private int level;
    public static log logIns = null;
}
