package com.bei.smartravel.version.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.bei.smartravel.R;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
/**
 * @author YongKun
 *
 */
public class UpdateManager {

	private Context mContext;
	
	private String apkUrl = null;
	private Dialog downloadDialog;
	 /* 下载包安装路径 */
    private static final String apkSavePath = "/mnt/sdcard/updatedemo/";
    private static final String apkSaveFileName = apkSavePath + "UpdateDemoRelease.apk";
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private int progress;
    //下载线程
    private Thread downLoadThread;
    private boolean interceptFlag = false;
    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				installApk();
				break;
			default:
				break;
			}
    	};
    };
    
	public UpdateManager(Context context) {
		this.mContext = context;
	}
	
	//外部接口让主Activity调用
	public void checkUpdateInfo(String path){
		apkUrl = path;
		showDownloadDialog();
	}

	//显示下载框
	private void showDownloadDialog(){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setCancelable(false);
		builder.setTitle("软件版本更新");
		
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.progress);
		
		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {	
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();
		
		downloadApk();
	}
	//下载
	private Runnable mdownApkRunnable = new Runnable() {	
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
				
				File file = new File(apkSavePath);
				if(!file.exists()){
					file.mkdir();
				}
				String apkFile = apkSaveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				
				int count = 0;
				byte buf[] = new byte[1024];
				
				do{   		   		
		    		int numread = is.read(buf);
		    		count += numread;
		    	    progress =(int)(((float)count / length) * 100);
		    	    //更新进度
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){
		    			//下载完成通知安装
		    			mHandler.sendEmptyMessage(DOWN_OVER);
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(!interceptFlag);//点击取消就停止下载.
				
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	};
	
	 /**
     * 下载apk
     * @param url
     */
	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	
	 /**
     * 安装apk
     * @param url
     */
	private void installApk(){
		File apkfile = new File(apkSaveFileName);
        if (!apkfile.exists()) {
            return;
        }
//        BeiSTWicket.instance.finish();
//        BeiSTWicket.instance.StopIDService();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        mContext.startActivity(i);
	}
}
