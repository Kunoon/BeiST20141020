package com.sdses.idreader;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sdses.service.ReadCardShell;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class IDRService4ShenSiWPt extends Service
{
	public static boolean isIDRServiceRuning = false;
	public static boolean isBeiWicketActivityRuning = false;
	private ReadCardShell rcShell;
	
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
		isExec = true;
		rcShell = new ReadCardShell();
		new Thread(new StartDevThread()).start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private class StartDevThread implements Runnable {
		private Message msg;
		public StartDevThread() {
		}

		public void run() {
			try {
		        rcShell.DevOn("/dev/ttyS5");
		        String samid = rcShell.getSAMID();
		        int i = 0;
		        while((samid.equalsIgnoreCase(""))&&(i<3)){
		        	samid = rcShell.getSAMID();
		        	i++;
		        }
		        msg = handler.obtainMessage(100, samid);
		 		handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

    Handler handler = new Handler() {
    	Bitmap bm = null;
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 100:
				String tsamid = msg.obj.toString();
				System.out.println("Ä£¿éºÅÎª£º"+tsamid);
				new ReadIDCardThread().start();
				break;
			default:
				break;
			}
		}
    };

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
	
	public static boolean isExec = true;
	
	private class ReadIDCardThread extends Thread {
		private HashMap<String, Object> id2Data;
		private byte[] m_bteCardInfo = new byte[256];
		private byte[] wltData = new byte[1024];
		private byte[] allId2Data = new byte[1280];
		private ID2Info m_ID2Info = new ID2Info();
		
		private Intent intent;
		public ReadIDCardThread() {
			// TODO Auto-generated constructor stub
			intent = new Intent();
			isExec = true;
		}

		@Override
		public void run() {
			while(isExec) {
        		if(rcShell.IsFindId2()){
        			id2Data = rcShell.ReadCard();
        			if(id2Data!=null){
						m_bteCardInfo = GetId2TxtDataFromHash(id2Data);
						wltData = GetId2WltDataFromHash(id2Data);
						m_ID2Info.setID2Info(m_bteCardInfo);
						m_ID2Info.MakeID2();
						System.arraycopy(m_bteCardInfo, 0, allId2Data, 0, 256);
						System.arraycopy(wltData, 0, allId2Data, 256, 1024);
						sendMsg(m_ID2Info.getIndentityCard());
        			}
        		}
        		try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
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
			intent.setAction("android.intent.action.test");
			sendBroadcast(intent);
		}
	}

	private byte[] GetId2TxtDataFromHash(HashMap<String, Object> hashData) {
		byte[] tmpData = new byte[256];
		try {
			memcpy(tmpData, 0, (byte[]) hashData.get("txt"), 0, 256);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmpData;
	}
	
	private byte[] GetId2WltDataFromHash(HashMap<String, Object> hashData) {
		byte[] tmpData = new byte[1024];
		try {
			memcpy(tmpData, 0, (byte[]) hashData.get("wlt"), 0, 1024);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmpData;
	}
	
	private static void memcpy(byte[] Dst, int dstStartPos, byte[] Src,
			int srcStartPos, int data_len) {
		int i = 0;
		for (i = 0; i < data_len; i++) {
			Dst[dstStartPos + i] = Src[srcStartPos + i];
		}
	}
	
	public static String replaceBlank(String str) {
	      String dest = "";
		  if (str!=null) {
		       Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	           Matcher m = p.matcher(str);
	           dest = m.replaceAll("");
		  }
	      return dest;
	}
}
