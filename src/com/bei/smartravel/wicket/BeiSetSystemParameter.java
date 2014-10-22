package com.bei.smartravel.wicket;

import com.bei.smartravel.R;
import com.bei.smartravel.chen.configer;
import com.bei.smartravel.customization.MyToast;
import com.bei.smartravel.macro.Macro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * @author YongKun
 *
 */
public class BeiSetSystemParameter extends Activity {

	private TextView myVersionTextView, myTextView_ScenicName, myTextView_DeviceID, 
					 myTextView_ServerAdd, myTextView_ServerPort, myTextView_AutoWicket, 
					 myTextView_ToPay, myTextView_ModifyOrder, myTextView_CommunicateTime, 
					 myTextView_ishowPayTitle;
	private Button myButton, mySetParameterImageButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_set_system_parameter);
		
		myButton = (Button)findViewById(R.id.myButton);
		mySetParameterImageButton = (Button)findViewById(R.id.setParameterImageButton);
		myVersionTextView = (TextView)findViewById(R.id.myVersionTextView);
		myTextView_ScenicName = (TextView)findViewById(R.id.myTextView_ScenicName);
		myTextView_DeviceID = (TextView)findViewById(R.id.myTextView_DeviceID);
		myTextView_ServerAdd = (TextView)findViewById(R.id.myTextView_ServerAdd);
		myTextView_ServerPort = (TextView)findViewById(R.id.myTextView_ServerPort);
		myTextView_AutoWicket = (TextView)findViewById(R.id.myTextView_AutoWicket);
		myTextView_ToPay = (TextView)findViewById(R.id.myTextView_ToPay);
		myTextView_ModifyOrder = (TextView)findViewById(R.id.myTextView_ishowPay);
		myTextView_ishowPayTitle = (TextView)findViewById(R.id.ishowPayTitleTextView);
		myTextView_CommunicateTime = (TextView)findViewById(R.id.myTextView_WaitTime);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(Macro.ACTION_XIAOBING) {
			myVersionTextView.setText(getString(R.string.system_version));
		} else if(Macro.ACTION_XIAOBING_NOVICE) {
			myVersionTextView.setText(getString(R.string.system_version) + "NV");
		} else if(Macro.ACTION_SHENSI) {
			myVersionTextView.setText(getString(R.string.system_version) + "S");
		}
		myTextView_ScenicName.setText(configer.getConfiger().getonfValue(configer.CMD_CONF_NAME, ""));
		myTextView_DeviceID.setText(configer.getConfiger().getonfValue(configer.CMD_CONF_ID, ""));
		myTextView_ServerAdd.setText(configer.getConfiger().getonfValue(configer.CMD_CONF_IP, configer.IP));
		myTextView_ServerPort.setText(configer.getConfiger().getonfValue(configer.CMD_CONF_IP, configer.PORT));
		myTextView_AutoWicket.setText((configer.getConfiger().getonfValue(configer.CMD_CONF_AU, "").equalsIgnoreCase("1")) ? "是" : "否");
		myTextView_ToPay.setText((configer.getConfiger().getonfValue(configer.CMD_CONF_CR, "").equalsIgnoreCase("1")) ? "是" : "否");
		if(myTextView_ToPay.getText().toString().equalsIgnoreCase("是")) {
			myTextView_ishowPayTitle.setTextColor(Color.BLACK);
			myTextView_ModifyOrder.setTextColor(Color.BLACK);
			myTextView_ModifyOrder.setText(
					(configer.getConfiger().getonfValue(configer.CMD_CONF_MD, "").equalsIgnoreCase("1")) ? "全部订单" : "未付订单");
		} else {
			myTextView_ishowPayTitle.setTextColor(Color.GRAY);
			myTextView_ModifyOrder.setTextColor(Color.GRAY);
			myTextView_ModifyOrder.setText("该功能仅在开启到付时使用");
		}
		myTextView_CommunicateTime.setText(configer.getConfiger().getonfValue(configer.CMD_CONF_TM, "") + "s");
		
		myButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyPasswdDialog();
			}
		});
		
		mySetParameterImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private void MyPasswdDialog() {
		LayoutInflater myLayoutInflater = getLayoutInflater();
		View myLayout = myLayoutInflater.inflate(R.layout.sys_para_dialog_input, 
				(ViewGroup) findViewById(R.id.mySystemParaInputDialog));
		final EditText myPasswdEditText = (EditText)myLayout.findViewById(R.id.myPasswdEditText);
		
		final AlertDialog.Builder builder = new Builder(BeiSetSystemParameter.this);
		builder.setCancelable(false);
		builder.setTitle("请输入管理员密码");
		builder.setMessage("请输入管理员密码");
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setView(myLayout);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(myPasswdEditText.getText().toString().
						equalsIgnoreCase(configer.getConfiger().getonfValue(configer.CMD_CONF_PW, ""))) {
					startActivity(new Intent(getApplicationContext(), BeiSystemSetting.class));
				} else {
					MyToast.getMyToast().WordToast(getApplicationContext(), "密码输入错误！");
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
	
}
