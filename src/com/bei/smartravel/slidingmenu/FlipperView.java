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

	private int distance;// 完全显示菜单需要移动的距离
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

		// 布局菜单和主页面视图
		distance = getWidth() * 7 / 20;
		menu = getChildAt(0);// 获得菜单视图
		menu.setVisibility(VISIBLE);
		menu.measure(  
                MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),  
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
		menu.layout(-getWidth() * 7 / 20, 0, 0, getHeight());

		main = getChildAt(1);// 获得主页面视图
		main.setVisibility(VISIBLE);
		main.measure(  
                MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),  
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
		main.layout(0, 0, getWidth(), getHeight());
	}

	/**
	 * 显示目录菜单
	 */
	public void showMenu() {
		if (!menuVisible) {
			scroller.startScroll(getScrollX(), 0, -distance, 0, 300);
			invalidate();
			menuVisible = true;
		}
	}
	
	/**
	 * 隐藏目录菜单
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
		// 当滚动没有完成
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), 0);
			postInvalidate();
		}
	}

}
