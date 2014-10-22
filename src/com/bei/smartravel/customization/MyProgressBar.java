package com.bei.smartravel.customization;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * 自定义ProgressBar，可以通过setText方法设置显示文字
 * @author YongKun
 *
 */
public class MyProgressBar extends ProgressBar {
	String myString;
	Paint myPaint;

	public MyProgressBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initText();
	}

	public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initText();
	}

	public MyProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initText();
	}

	@Override
	public synchronized void setProgress(int progress) {
		// TODO Auto-generated method stub
		setText(progress, "");
		super.setProgress(progress);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// this.setText();
		Rect rect = new Rect();
		this.myPaint.getTextBounds(this.myString, 0, this.myString.length(), rect);
		int x = (getWidth() / 2) - rect.centerX();
		int y = (getHeight() / 2) - rect.centerY();
		canvas.drawText(this.myString, x, y, this.myPaint);
	}

	/**
	 * 初始化，画笔
	 */
	private void initText() {
		this.myPaint = new Paint();
		this.myPaint.setColor(Color.RED);
	}

	/**
	 * 设置文字内容
	 * @param progress
	 */
	private void setText(int progress, String string) {
//		int i = (progress * 100) / this.getMax();
		this.myString = string;
	}
	
	public void setText(String timeString) {
		setText(this.getProgress(), timeString);
	}
}