//////////////////////////////=============== BeiSmart 3 Start ===============//////////////////////////////
package com.bei.smartravel.idreader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import szxb.test.ll.jni.test4052;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android_serialport_api.SerialPort;

import com.alibaba.cn.idcard.IDCardManager;
import com.alibaba.cn.idcard.entity.IDCardInfo;
/**
 * @author YongKun
 *
 */
public class IDRService4XiaoBing extends Service
{
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
		isExec = true;//�������¿�ʼִ�б�־
		new ReadIDCardThread().start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isIDRServiceRuning = false;
	}
	
	@Override
	public boolean stopService(Intent name) {
		// TODO Auto-generated method stub
		return super.stopService(name);
	}
//////////////////////////////////////////////////////////////////////////////////////////////////
	public static List<Runnable> lists = new ArrayList<Runnable>();
	public static boolean isExec = true;
	
	private class ReadIDCardThread extends Thread {
		private Intent intent;
		public ReadIDCardThread() {
			// TODO Auto-generated constructor stub
			intent = new Intent();
			isExec = true;
		}
		private void showInfo(IDCardInfo cardInfo) {
			if(!isBeiWicketActivityRuning) {
				Intent intent = new Intent();
		        intent.setClassName("com.bei.smartravel", "com.bei.smartravel.wicket.BeiSTWicket");
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(intent);
		        while(!isBeiWicketActivityRuning);
			}
			intent.putExtra("i", cardInfo.getIdNumber());
			intent.setAction("android.intent.action.test");// action���������ͬ
			sendBroadcast(intent);
		}
		@Override
		public void run() {
			//���ж�ֻ��֤lists��ֻ��һ���߳���ִ�У�������ڶ����ֱ�ӷ���
			if(lists.size()>1) {
				isExec = false;
				Log.e("List<Runnable> lists", "List<Runnable> lists�д��ڶ��");
				return;
			}
			
			test4052.testopen();
			test4052.testioctl(10);
			
			SerialPort mSerialPort = null;
			IDCardManager idManager = null;
			IDCardInfo cardInfo = null;
			
			try {
				mSerialPort = new SerialPort(new File("/dev/ttySAC0"), 115200, 0);
				idManager = new IDCardManager(mSerialPort.getOutputStream(),mSerialPort.getInputStream());
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			lists.add(this);
			while(isExec) {
				if(idManager.init()) {
					cardInfo = idManager.parseIDCardData();
					if(cardInfo!=null) {
						showInfo(cardInfo);
					} else {
						intent.putExtra("i", "default");
						intent.setAction("android.intent.action.test");// action���������ͬ
						sendBroadcast(intent);
					}
				}
			}
			
			//�������������ͷ���Դ������ӡʹ��
			if(idManager!=null)
				idManager.closeStream();
			if(mSerialPort!=null)
				mSerialPort.close();
			test4052.testrelease();
			
			//������жϱ�֤lists��ֻ��һ�����߳�һ��
			if(lists.contains(this)&&lists.size()==1)
			{
				lists.remove(this);
			}
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////
}
//////////////////////////////=============== BeiSmart 3 End ===============//////////////////////////////
