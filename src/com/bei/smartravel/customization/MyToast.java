package com.bei.smartravel.customization;

import com.bei.smartravel.R;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �Զ���Tosat��
 * @author YongKun
 */
public class MyToast {
	private static MyToast myToast = null;
	
	/**
	 * ���MyToast����
	 * @return
	 */
	public static MyToast getMyToast() {
		if (myToast == null)
			myToast = new MyToast();
		return myToast;
	}
	
	/**
	 * �ı���ʾ�򣬸���ʾ�������Ļ���룬��ɫ͸����������ɫǰ����
	 * @param context	��Ҫ��ʾ��Context
	 * @param str	��Ҫ��ʾ���ַ���
	 */
	public void WordToast(Context context, String str) {
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		TextView tmpTextView = new TextView(context);
		tmpTextView.setWidth(300);
		tmpTextView.setHeight(100);
		tmpTextView.setGravity(Gravity.CENTER);
		tmpTextView.setBackgroundColor(1996488704); // #77000000
		tmpTextView.setTextColor(Color.WHITE);
		tmpTextView.setText(str);
		tmpTextView.setTextSize(30);
		toast.setView(tmpTextView);
		toast.show();
	}
	
	/**
	 * �ı���ʾ�򣬸���ʾ�������Ļ���룬��ɫ��������ɫǰ����
	 * @param context	��Ҫ��ʾ��Context
	 * @param str	��Ҫ��ʾ���ַ���
	 */
	public void WordToastLarge(Context context, String str) {
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		TextView tmpTextView = new TextView(context);
		tmpTextView.setWidth(300);
		tmpTextView.setHeight(100);
		tmpTextView.setGravity(Gravity.CENTER);
		tmpTextView.setBackgroundColor(0xFF000000);
		tmpTextView.setTextColor(Color.WHITE);
		tmpTextView.setText(str);
		tmpTextView.setTextSize(60);
		toast.setView(tmpTextView);
		toast.show();
	}
	
	/**
	 * δ����ƱImage Toast
	 * @param context	��Ҫ��ʾ��Context
	 */
	public void NoTickectsImageToast(Context context, int which) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = null;
		switch (which) {	//  
		case 0:
			layout = inflater.inflate(R.layout.custom_image_toast, null);
			break;
		case 1:
			layout = inflater.inflate(R.layout.custom_image_toast_invalid, null);
			break;
		default:
			break;
		}
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}
	
}
