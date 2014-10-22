////////////////////////////*************** shenSi Start ***************//////////////////////////////
package com.bei.smartravel.idreader;

import cn.com.aratek.dev.AraError;
import cn.com.aratek.idcard.CardReader;
import cn.com.aratek.idcard.IDCard;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author YongKun
 *
 */
public class IDRService4ShenSi extends Service
{
	public static boolean isIDRServiceRuning = false;
	public static boolean isBeiWicketActivityRuning = false;
	private CardReader mReader;
	
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
        mReader = CardReader.getInstance();	// 获取身份证识读器控制实例
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
			if (initID()) {
				while(isExec) {
					readID();
				}
			}
			closeID();
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
		
		private boolean initID() {
			AraError error = new AraError();
	        // 完成上电、初始化操作
	        if (!mReader.powerOn(error)) {// 上电大概需要2s时间，此处可使用提示或线程
	        	sendMsg("fmsg_身份证识读器上电失败！");
	        	return false;
	        } else if (!mReader.open(error)) {
	        	sendMsg("fmsg_身份证识读器打开失败！");
	        	return false;
	        } else {
	            // 初始化完成，读卡按钮可用
	        	sendMsg("smsg_身份证识读器初始化成功！");
	        	return true;
	        }
		}
		
		private void readID() {
            // 读身份证，大约需要1s
            AraError error = new AraError();
            IDCard card = mReader.read(error);
            if (card != null) {
            	sendMsg("id_" + card.getNumber());
            } else if (error.mErrorCode == AraError.CARD_NO_CARD) {
//                hd.obtainMessage(1, "请重新放卡或者确认卡片是否存在！").sendToTarget();
            } else if(error.mErrorCode == AraError.CARD_VALIDATE_FAIL){
                sendMsg("msg_读卡失败！错误码：" + error.mErrorCode);
            }
		}
		
		private void closeID() {
	        AraError error = new AraError();
	        // 完成清理、断电操作
	        if (!mReader.close(error)) {
	            sendMsg("fmsg_身份证识读器关闭失败！");
	        } else if (!mReader.powerOff(error)) {
	            sendMsg("fmsg_身份证识读器断电失败！");
	        } else {
	        	sendMsg("smsg_身份证识读器关闭成功！");
	        }
		}
	}
}
////////////////////////////*************** shenSi End ***************//////////////////////////////
