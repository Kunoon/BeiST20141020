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
		isExec = true;//�������¿�ʼִ�б�־
        mReader = CardReader.getInstance();	// ��ȡ���֤ʶ��������ʵ��
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
			intent.setAction("android.intent.action.test");// action���������ͬ
			sendBroadcast(intent);
		}
		
		private boolean initID() {
			AraError error = new AraError();
	        // ����ϵ硢��ʼ������
	        if (!mReader.powerOn(error)) {// �ϵ�����Ҫ2sʱ�䣬�˴���ʹ����ʾ���߳�
	        	sendMsg("fmsg_���֤ʶ�����ϵ�ʧ�ܣ�");
	        	return false;
	        } else if (!mReader.open(error)) {
	        	sendMsg("fmsg_���֤ʶ������ʧ�ܣ�");
	        	return false;
	        } else {
	            // ��ʼ����ɣ�������ť����
	        	sendMsg("smsg_���֤ʶ������ʼ���ɹ���");
	        	return true;
	        }
		}
		
		private void readID() {
            // �����֤����Լ��Ҫ1s
            AraError error = new AraError();
            IDCard card = mReader.read(error);
            if (card != null) {
            	sendMsg("id_" + card.getNumber());
            } else if (error.mErrorCode == AraError.CARD_NO_CARD) {
//                hd.obtainMessage(1, "�����·ſ�����ȷ�Ͽ�Ƭ�Ƿ���ڣ�").sendToTarget();
            } else if(error.mErrorCode == AraError.CARD_VALIDATE_FAIL){
                sendMsg("msg_����ʧ�ܣ������룺" + error.mErrorCode);
            }
		}
		
		private void closeID() {
	        AraError error = new AraError();
	        // ��������ϵ����
	        if (!mReader.close(error)) {
	            sendMsg("fmsg_���֤ʶ�����ر�ʧ�ܣ�");
	        } else if (!mReader.powerOff(error)) {
	            sendMsg("fmsg_���֤ʶ�����ϵ�ʧ�ܣ�");
	        } else {
	        	sendMsg("smsg_���֤ʶ�����رճɹ���");
	        }
		}
	}
}
////////////////////////////*************** shenSi End ***************//////////////////////////////
