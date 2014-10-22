package com.bei.smartravel.wicket;

import com.bei.smartravel.R;
import com.bei.smartravel.chen.configer;
import com.bei.smartravel.customization.MyEditText4Point;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
/**
 * @author YongKun
 *
 */
public class BeiSystemSetting extends Activity{
	
	private static final String[] myData4Spinner = {"否", "是"};
	private static final String[] myData4ModifySpinner = {"未付订单", "全部订单"};
	private static final String[] myNoData4ModifySpinner = {"该功能仅在开启到付时使用"};
	private ArrayAdapter<String> myAdapter4Spinner, myAdapter4ModifySpinner, myAdapter4NoDataModifySpinner;
	
	private TextView isShowPayTextView = null;
	private EditText myEditTextSettingID = null, myEditTextSettingAdd = null, 
			myEditTextSettingPort = null, myEditTextSettingCommunicateTime = null, 
			myEditTextScenicName = null;
	private Spinner myAutoSpinner = null, myToPaySpinner = null, myModifyOrderSpinner = null;
	private Button myButtonSettingOK = null, mySettingImageButton = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_system_parameter);
		
		isShowPayTextView = (TextView) findViewById(R.id.isShowPayTextView);
		myEditTextScenicName = (EditText)findViewById(R.id.myEditTextScenicName);
		myEditTextSettingID = (EditText)findViewById(R.id.myEditTextSettingID);
		myEditTextSettingAdd = (EditText)findViewById(R.id.myEditTextSettingAdd);
		myEditTextSettingPort = (EditText)findViewById(R.id.myEditTextSettingPort);
		myEditTextSettingCommunicateTime = (EditText)findViewById(R.id.myEditTextSettingWaitTime);
		myAutoSpinner = (Spinner)findViewById(R.id.autoWicketSpinner);
		myToPaySpinner = (Spinner)findViewById(R.id.toPaySpinner);
		myModifyOrderSpinner = (Spinner)findViewById(R.id.modifyOrderSpinner);
		myButtonSettingOK = (Button)findViewById(R.id.myBottonSettingOK);
		mySettingImageButton = (Button)findViewById(R.id.settingImageButton);
		
		myEditTextScenicName.setHint(configer.getConfiger().getonfValue(configer.CMD_CONF_NAME, ""));
		myEditTextSettingID.setHint(configer.getConfiger().getonfValue(configer.CMD_CONF_ID, ""));
		myEditTextSettingAdd.setHint(configer.getConfiger().getonfValue(configer.CMD_CONF_IP, configer.IP));
		myEditTextSettingPort.setHint(configer.getConfiger().getonfValue(configer.CMD_CONF_IP, configer.PORT));
		myEditTextSettingCommunicateTime.setHint(configer.getConfiger().getonfValue(configer.CMD_CONF_TM, "") + "s");
		
		MyEditText4Point.getMyEditText4Point().EditTextGetFocus(myEditTextScenicName, 
				configer.getConfiger().getonfValue(configer.CMD_CONF_NAME, ""));
		MyEditText4Point.getMyEditText4Point().EditTextGetFocus(myEditTextSettingID, 
				configer.getConfiger().getonfValue(configer.CMD_CONF_ID, ""));
		MyEditText4Point.getMyEditText4Point().EditTextGetFocus(myEditTextSettingAdd, 
				configer.getConfiger().getonfValue(configer.CMD_CONF_IP, configer.IP));
		MyEditText4Point.getMyEditText4Point().EditTextGetFocus(myEditTextSettingPort, 
				configer.getConfiger().getonfValue(configer.CMD_CONF_IP, configer.PORT));
		MyEditText4Point.getMyEditText4Point().EditTextGetFocus(myEditTextSettingCommunicateTime, 
				configer.getConfiger().getonfValue(configer.CMD_CONF_TM, "") + "s");
		
		// 将可选内容与ArrayAdapter连接起来
		myAdapter4Spinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myData4Spinner);
		myAdapter4ModifySpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myData4ModifySpinner);
		myAdapter4NoDataModifySpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myNoData4ModifySpinner);
		// 设置下拉列表的风格
		myAdapter4Spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		myAdapter4ModifySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		myAutoSpinner.setAdapter(myAdapter4Spinner);
		myToPaySpinner.setAdapter(myAdapter4Spinner);
		
		myAutoSpinner.setSelection(Integer.valueOf(configer.getConfiger().getonfValue(configer.CMD_CONF_AU, "")));
		myToPaySpinner.setSelection(Integer.valueOf(configer.getConfiger().getonfValue(configer.CMD_CONF_CR, "")));
		
		myToPaySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					myModifyOrderSpinner.setAdapter(myAdapter4NoDataModifySpinner);
					myModifyOrderSpinner.setClickable(false);
					isShowPayTextView.setTextColor(Color.GRAY);
					break;
				case 1:
					myModifyOrderSpinner.setAdapter(myAdapter4ModifySpinner);
					myModifyOrderSpinner.setSelection(Integer.valueOf
							(configer.getConfiger().getonfValue(configer.CMD_CONF_MD, "")));
					myModifyOrderSpinner.setClickable(true);
					isShowPayTextView.setTextColor(Color.BLACK);
					break;
				default:
					break;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		myButtonSettingOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SetSystemParameter(myEditTextScenicName.getText().toString(),
						myEditTextSettingID.getText().toString(), 
						myEditTextSettingAdd.getText().toString(), 
						myEditTextSettingPort.getText().toString(), 
						myEditTextSettingCommunicateTime.getText().toString(), 
						myAutoSpinner.getSelectedItem().toString(), 
						myToPaySpinner.getSelectedItem().toString(), 
						myModifyOrderSpinner.getSelectedItem().toString());
				BeiSTWicket.GetSystemConfiger(configer.getConfiger().getonfValue(configer.CMD_CONF_AU, "").equalsIgnoreCase("1")?true:false, 
						Integer.valueOf(configer.getConfiger().getonfValue(configer.CMD_CONF_TM, "")));
				finish();
			}
		});
		mySettingImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	/**
	 * 更新系统设置到数据库
	 * @param uID	景区ID
	 * @param uAdd	服务器地址
	 * @param uPort	服务器端口
	 * @param communicateTimeStr	通信间隔时间
	 * @param autoWicket	自动检票开关
	 * @param toPay	到付
	 * @param modifyOrder	修改订单
	 */
	private void SetSystemParameter(String uName, String uID, String uAdd, String uPort, String communicateTimeStr,
			String autoWicket, String toPay, String modifyOrder) {
		configer.getConfiger().config(configer.CMD_CONF_NAME, uName);
		configer.getConfiger().config(configer.CMD_CONF_ID, uID);
		configer.getConfiger().config(configer.CMD_CONF_IP, uAdd, uPort);
		configer.getConfiger().config(configer.CMD_CONF_TM, communicateTimeStr);
		configer.getConfiger().config(configer.CMD_CONF_AU, autoWicket.equalsIgnoreCase("是")?"1":"0");
		configer.getConfiger().config(configer.CMD_CONF_CR, toPay.equalsIgnoreCase("是")?"1":"0");
		if(myToPaySpinner.getSelectedItem().toString().equalsIgnoreCase("是")) {
			configer.getConfiger().config(configer.CMD_CONF_MD, modifyOrder.equalsIgnoreCase("全部订单")?"1":"0");
		}
	}
}
