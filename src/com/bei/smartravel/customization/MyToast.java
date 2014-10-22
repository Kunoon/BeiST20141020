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
 * 自定义Tosat类
 * @author YongKun
 */
public class MyToast {
	private static MyToast myToast = null;
	
	/**
	 * 获得MyToast对象
	 * @return
	 */
	public static MyToast getMyToast() {
		if (myToast == null)
			myToast = new MyToast();
		return myToast;
	}
	
	/**
	 * 文本提示框，该提示框局域屏幕中央，黑色透明背景，白色前景字
	 * @param context	需要显示的Context
	 * @param str	需要显示的字符串
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
	 * 文本提示框，该提示框局域屏幕中央，黑色背景，白色前景字
	 * @param context	需要显示的Context
	 * @param str	需要显示的字符串
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
	 * 未购门票Image Toast
	 * @param context	需要显示的Context
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
