package com.bei.smartravel.customization;

import com.bei.smartravel.R;
import com.bei.smartravel.macro.Macro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * 身份证输入窗体
 * @author YongKun
 */
public class MyIDTextViewActivity extends Activity {
	
	private MyKeyboard4IDNum myKeyboard;	// 声明自定义键盘
	private EditText myEditText_ID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(Macro.ACTION_SHENSI) {
			setContentView(R.layout.activity_id_text_ss);
		} else {
			setContentView(R.layout.activity_id_text);
		}
		setResult(Activity.RESULT_CANCELED);
		
		myEditText_ID = (EditText)findViewById(R.id.tv);
		myKeyboard = new MyKeyboard4IDNum(this, this, myEditText_ID);
		myKeyboard.showKeyboard();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
            intent.putExtra("AAA", myEditText_ID.getText().toString());
            // 设置返回数据，并结束Activity
            setResult(Activity.RESULT_OK, intent);
			finish();
            return true;
        }
		return super.onKeyDown(keyCode, event);
	}
}
