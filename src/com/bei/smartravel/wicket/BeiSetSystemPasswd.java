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
		
		MyEditText4Point.getMyEditText4Point().EditTextGetFocus(myEditTextOldPassword, "������ԭ����");
		MyEditText4Point.getMyEditText4Point().EditTextGetFocus(myEditTextNewPasswd, "������������");
		MyEditText4Point.getMyEditText4Point().EditTextGetFocus(myEditTextConfirmNewPassword, "������ȷ������");
		
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
									MyToast.getMyToast().WordToast(getApplicationContext(), "�����޸ĳɹ���");
									finish();
								} else {
									MyToast.getMyToast().WordToast(getApplicationContext(), "���벻һ�£����������룡");
								}
							} else {
								MyToast.getMyToast().WordToast(getApplicationContext(), "ȷ�����벻��Ϊ�գ�");
							}
						} else {
							MyToast.getMyToast().WordToast(getApplicationContext(), "�����벻��Ϊ�գ�");
						}
					} else {
						MyToast.getMyToast().WordToast(getApplicationContext(), "ԭʼ����������������룡");
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
