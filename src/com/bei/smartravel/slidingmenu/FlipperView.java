package com.bei.smartravel.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
/**
 * @author YongKun
 *
 */
public class FlipperView extends ViewGroup {

	private int distance;// ��ȫ��ʾ�˵���Ҫ�ƶ��ľ���
	private View menu;
	private View main;
	private Scroller scroller;
	private boolean menuVisible = false;

	public FlipperView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		scroller = new Scroller(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

		// ���ֲ˵�����ҳ����ͼ
		distance = getWidth() * 7 / 20;
		menu = getChildAt(0);// ��ò˵���ͼ
		menu.setVisibility(VISIBLE);
		menu.measure(  
                MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),  
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
		menu.layout(-getWidth() * 7 / 20, 0, 0, getHeight());

		main = getChildAt(1);// �����ҳ����ͼ
		main.setVisibility(VISIBLE);
		main.measure(  
                MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),  
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
		main.layout(0, 0, getWidth(), getHeight());
	}

	/**
	 * ��ʾĿ¼�˵�
	 */
	public void showMenu() {
		if (!menuVisible) {
			scroller.startScroll(getScrollX(), 0, -distance, 0, 300);
			invalidate();
			menuVisible = true;
		}
	}
	
	/**
	 * ����Ŀ¼�˵�
	 */
	public void hideMenu() {
		if (menuVisible) {
			scroller.startScroll(getScrollX(), 0, distance, 0, 300);
			invalidate();
			menuVisible = false;
		}
	}

	public boolean isShowMenu() {
		return menuVisible;
	}
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		// ������û�����
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), 0);
			postInvalidate();
		}
	}

}
