package com.bei.smartravel.customization;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * �Զ���EditText�࣬���ڼ���EditText�ڻ�ȡ���㡢ʧȥ����ʱ��ʾ��Ϣ�Ĵ���
 * @author YongKun
 */
public class MyEditText4Point {
	private static MyEditText4Point myEditText4Point = null;
	
	/**
	 * ���MyEditText4Point����
	 * @return
	 */
	public static MyEditText4Point getMyEditText4Point() {
		if (myEditText4Point == null)
			myEditText4Point = new MyEditText4Point();
		return myEditText4Point;
	}
	
	/**
	 * ����������ʾ��Ϣ��EditText
	 * @param tmpEditText	��Ҫ������EditText
	 * @param str	��ʾ��Ϣ�ַ���
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
