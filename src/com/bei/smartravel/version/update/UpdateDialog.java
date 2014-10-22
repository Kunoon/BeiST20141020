package com.bei.smartravel.version.update;

import java.io.File;
import java.util.ArrayList;

import com.bei.smartravel.R;
import com.bei.smartravel.chen.configer;
import com.bei.smartravel.version.down.DownloadVersionFile;
import com.bei.smartravel.version.xml.MySAXMLService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/**
 * @author YongKun
 *
 */
public class UpdateDialog extends Activity{

	private TextView msgTextView;
	private Button positiveButton, negativeButton;
	private String apkUrl = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_dialog);
		
		apkUrl = "http://" + configer.getConfiger().getonfValue(configer.CMD_CONF_IP, configer.IP);
		msgTextView = (TextView)findViewById(R.id.msgTextView);
		positiveButton = (Button)findViewById(R.id.buttonPositive);
		negativeButton = (Button)findViewById(R.id.buttonNegative);
		
		try {
			ArrayList<String> tmpList = MySAXMLService.readXML(DownloadVersionFile.xmlSaveFileName).get(0).getUpFunction();
			msgTextView.setText("");
			for(String tmp : tmpList) {
				msgTextView.append(tmp + "\n");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		positiveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
		        	File file = new File("/mnt/sdcard/updatedemo/UpdateDemoRelease.apk");
					if(file.exists()){
						file.delete();
					}
					UpdateManager mUpdateManager = new UpdateManager(UpdateDialog.this);
					mUpdateManager.checkUpdateInfo( apkUrl +
							MySAXMLService.readXML(DownloadVersionFile.xmlSaveFileName).get(0).getURL() );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		negativeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File file = new File("/mnt/sdcard/updatedemo/UpdateDemoRelease.apk");
				if(file.exists()){
					file.delete();
				}
				finish();
			}
		});
	}

}
