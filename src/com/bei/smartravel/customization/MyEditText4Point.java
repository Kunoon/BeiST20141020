package com.bei.smartravel.customization;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * 自定义EditText类，用于监听EditText在获取焦点、失去焦点时提示信息的处理
 * @author YongKun
 */
public class MyEditText4Point {
	private static MyEditText4Point myEditText4Point = null;
	
	/**
	 * 获得MyEditText4Point对象
	 * @return
	 */
	public static MyEditText4Point getMyEditText4Point() {
		if (myEditText4Point == null)
			myEditText4Point = new MyEditText4Point();
		return myEditText4Point;
	}
	
	/**
	 * 监听带有提示信息的EditText
	 * @param tmpEditText	需要监听的EditText
	 * @param str	提示信息字符串
	 */
	public void EditTextGetFocus(final EditText tmpEditText, final String str) {
		tmpEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
					tmpEditText.setHint("");
				else
					tmpEditText.setHint(str);
			}
		});
	}
	
}
