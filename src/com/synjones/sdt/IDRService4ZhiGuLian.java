package com.synjones.sdt;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
* @author YongKun
*
*/
public class IDRService4ZhiGuLian extends Service {

	protected SerialPort mSerialPort;
	private IDCard idcard = null;
	
	public static boolean isIDRServiceRuning = false;
	public static boolean isBeiWicketActivityRuning = false;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		isIDRServiceRuning = true;
		isExec = true;//设置重新开始执行标志
      	try {
			mSerialPort = getSerialPort();
		} catch (InvalidParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	// 获取身份证识读器控制实例
		new ReadIDCardThread().start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isIDRServiceRuning = false;
		isExec = false;
	}
	
	@Override
	public boolean stopService(Intent name) {
		// TODO Auto-generated method stub
		isExec = false;
		return super.stopService(name);
	}
	
	private SerialPort getSerialPort()  throws SecurityException, IOException, InvalidParameterException {
    	if (mSerialPort == null) {
    		mSerialPort = new SerialPort(new File("/dev/ttySAC1"), 115200, 0);
    		mSerialPort.setMaxRFByte((byte)0x50);
    	}
    	return mSerialPort;
    }
	
	public static boolean isExec = true;
	
	private class ReadIDCardThread extends Thread {
		private Intent intent;
		public ReadIDCardThread() {
			// TODO Auto-generated constructor stub
			intent = new Intent();
			isExec = true;
		}

		@Override
		public void run() {
			while(isExec) {
				idcard = mSerialPort.getIDCard();
				if(idcard != null) {
					sendMsg(idcard.getIDCardNo());
				}
				try {
					sleep(700);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		private void sendMsg(String str) {
			if(!isBeiWicketActivityRuning) {
				Intent intent = new Intent();
		        intent.setClassName("com.bei.smartravel", "com.bei.smartravel.wicket.BeiSTWicket");
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(intent);
		        while(!isBeiWicketActivityRuning);
			}
			intent.putExtra("i", str);
			intent.setAction("android.intent.action.test");// action与接收器相同
			sendBroadcast(intent);
		}
	}
}
