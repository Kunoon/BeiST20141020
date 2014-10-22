package com.bei.smartravel.version.down;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.bei.smartravel.R;
import com.bei.smartravel.chen.configer;
import com.bei.smartravel.macro.Macro;
import com.bei.smartravel.version.xml.MySAXMLService;
import com.bei.smartravel.version.xml.VersionXml;

import android.content.Context;
import android.os.Handler;
/**
 * 下载版本控制XML文件
 * @author Bei_YK
 */
public class DownloadVersionFile {
	private Context mContext;
	private final Handler handler;
	private static final String xmlSavePath = "/mnt/sdcard/";	// 下载包安装路径
	public static final String xmlSaveFileName = xmlSavePath + "version.xml";
	private String apkUrl = "http://" + configer.getConfiger().getonfValue(configer.CMD_CONF_IP, configer.IP);
    private boolean interceptFlag = false;
    
	public DownloadVersionFile(Context context, Handler handler) {
		if(Macro.ACTION_XIAOBING) {
			apkUrl += "/swap/4android/beiST3/20140507/version.xml";	// 返回的安装包url
		} else if(Macro.ACTION_XIAOBING_NOVICE) {
			apkUrl += "/swap/4android/beiST3_noVoid/20140507/version.xml";
		} else if(Macro.ACTION_SHENSI) {
			apkUrl += "/swap/4android/handheld/20140507/versionS.xml";
		} else if(Macro.ACTION_SHENSI_PRINT) {
			apkUrl += "/swap/4android/handheld/versionS.xml";
		} else if(Macro.ACTION_ZHIGULIAN) {
			apkUrl += "/swap/4android/handheld/versionS.xml";
		}
		this.mContext = context;
		this.handler = handler;
	}
	
	/**
	 * 外部接口让主Activity调用
	 */
	public void checkUpdateInfo(){
		new Thread(mdownApkRunnable).start();
	}
	
	private Runnable mdownApkRunnable = new Runnable() {	
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestMethod("POST");
				conn.setConnectTimeout(20000);
				conn.setReadTimeout(20000);
				conn.setUseCaches(false);
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				
				File file = new File(xmlSavePath);
				if(!file.exists()){
					file.mkdir();
				}
				String apkFile = xmlSaveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				int count = 0;
				byte buf[] = new byte[1024];
				do{   		   		
		    		int numread = is.read(buf);
		    	    if( (numread <= 0)||(count == length) ) {
		    	    	interceptFlag = true;
		    	    } else {
		    	    	fos.write(buf,0,numread);
		    	    }
		    	    count += numread;
		    	}while(!interceptFlag);//点击取消就停止下载.
				fos.close();
				is.close();
				try {
					ArrayList<VersionXml> tmpArrayList = MySAXMLService.readXML(xmlSaveFileName);
					String sVersion = "";
					if(Macro.ACTION_XIAOBING) {
						sVersion = mContext.getString(R.string.system_version);
					} else if(Macro.ACTION_XIAOBING_NOVICE) {
						sVersion = mContext.getString(R.string.system_version) + "NV";
					} else if(Macro.ACTION_SHENSI) {
						sVersion = mContext.getString(R.string.system_version) + "S";
					}
					if( !tmpArrayList.get(0).getVersion().equalsIgnoreCase(sVersion)
							&& (tmpArrayList.get(0).getUpDevices().equalsIgnoreCase("all")
									|| (tmpArrayList.get(0).getUpDevices().indexOf(
											configer.getConfiger().getonfValue(configer.CMD_CONF_ID, "")) > -1))
							&& (tmpArrayList.get(0).getNUpDevices().indexOf(
											configer.getConfiger().getonfValue(configer.CMD_CONF_ID, "")) < 0) ) {
						handler.obtainMessage(0).sendToTarget();
					} else {
						handler.obtainMessage(1).sendToTarget();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	};
}