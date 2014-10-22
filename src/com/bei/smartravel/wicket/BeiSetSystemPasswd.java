package com.bei.smartravel.wicket;

import com.bei.smartravel.R;
import com.bei.smartravel.chen.configer;
import com.bei.smartravel.customization.MyEditText4Point;
import com.bei.smartravel.customization.MyToast;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
/**
 * @author YongKun
 *
 */
public class BeiSetSystemPasswd extends Activity{

	private EditText myEditTextOldPassword, myEditTextNewPasswd, myEditTextConfirmNewPassword;
	private Button myButtonPasswdOK, myBackImageButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_set_passwd);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		myEditTextOldPassword = (EditText)findViewById(R.id.myEditText_OldPassword);
		myEditTextNewPasswd = (EditText)findViewById(R.id.myEditText_NewPasswd);
		myEditTextConfirmNewPassword = (EditText)findViewById(R.id.myEditText_ConfirmNewPassword);
		myButtonPasswdOK = (Button)findViewById(R.id.myButtonPasswdOK);
		myBackImageButton = (Button)findViewById(R.id.setPasswdImageButton);
		
		MyEditText4Point.getMyEditText4Point().EditTextGetFocus(myEditTextOldPassword, "请输入原密码");
		MyEditText4Point.getMyEditText4Point().EditTextGetFocus(myEditTextNewPasswd, "请输入新密码");
		MyEditText4Point.getMyEditText4Point().EditTextGetFocus(myEditTextConfirmNewPassword, "请输入确认密码");
		
		myButtonPasswdOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					if( (0 != myEditTextOldPassword.getText().toString().length())
							&& myEditTextOldPassword.getText().toString().
							equalsIgnoreCase(configer.getConfiger().getonfValue(configer.CMD_CONF_PW, ""))) {
						if(0 != myEditTextNewPasswd.getText().toString().length()) {
							if(0 != myEditTextConfirmNewPassword.getText().toString().length()) {
								if(myEditTextNewPasswd.getText().toString().
										equalsIgnoreCase(myEditTextConfirmNewPassword.getText().toString())) {
									configer.getConfiger().config(configer.CMD_CONF_PW, myEditTextConfirmNewPassword.getText().toString());
									MyToast.getMyToast().WordToast(getApplicationContext(), "密码修改成功！");
									finish();
								} else {
									MyToast.getMyToast().WordToast(getApplicationContext(), "密码不一致，请重新输入！");
								}
							} else {
								MyToast.getMyToast().WordToast(getApplicationContext(), "确认密码不能为空！");
							}
						} else {
							MyToast.getMyToast().WordToast(getApplicationContext(), "新密码不能为空！");
						}
					} else {
						MyToast.getMyToast().WordToast(getApplicationContext(), "原始密码错误，请重新输入！");
					}
			}
		});
		
		myBackImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
