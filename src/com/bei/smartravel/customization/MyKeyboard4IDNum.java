package com.bei.smartravel.customization;

import com.bei.smartravel.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.media.AudioManager;
import android.media.SoundPool;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

/**
 * 自定义身份证号码输入键盘
 * @author YongKun
 */
public class MyKeyboard4IDNum {
	private Context myContext;
	private Activity myActivity;
	private KeyboardView myKeyboardView;
	private Keyboard keyboardSymbols;	// 数字键盘, 字母键盘
	private EditText myEditText;
	public boolean isNum = false;	// 是否数据键盘
	public boolean isCapital = false;	// 是否大写
	private SoundPool soundPool;	// 声明一个SoundPool  
	private int music;	// 定义一个整型用load（）；来设置suondID  

	public MyKeyboard4IDNum(Activity activity, Context context, EditText editText) {
		myActivity = activity;
		myContext = context;
		myEditText = editText;
		keyboardSymbols = new Keyboard(myContext, R.layout.keyboard_symbols_id);
		myKeyboardView = (KeyboardView) activity.findViewById(R.id.keyboard_view_id);
		myKeyboardView.setKeyboard(keyboardSymbols);
		myKeyboardView.setEnabled(true);
		myKeyboardView.setPreviewEnabled(true);
		myKeyboardView.setOnKeyboardActionListener(myOnKeyboardActionListener);
		// 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
		soundPool= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		//把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        music = soundPool.load(this.myActivity, R.raw.standard, 1); 
	}

	private OnKeyboardActionListener myOnKeyboardActionListener = new OnKeyboardActionListener() {
		
		@Override
		public void swipeUp() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void swipeRight() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void swipeLeft() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void swipeDown() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onText(CharSequence text) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onRelease(int primaryCode) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onPress(int primaryCode) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			// TODO Auto-generated method stub
			// 播放按键音效
			soundPool.play(music, 1, 1, 0, 0, 1);
			Editable editable = myEditText.getText();
			int start = myEditText.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {	// 完成
				Intent intent = new Intent();
	            intent.putExtra("AAA", editable.toString());
	            // Set result and finish this Activity
	            myActivity.setResult(Activity.RESULT_OK, intent);
				myActivity.finish();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {	// 回退
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			} else {
				editable.insert(start, Character.toString((char) primaryCode));
			}
		}
	};
	
	/**
	 * 显示键盘
	 */
	public void showKeyboard() {
		int myVisibility = myKeyboardView.getVisibility();
		if (myVisibility == View.GONE || myVisibility == View.INVISIBLE) {
			myKeyboardView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 隐藏键盘
	 */
	public void hideKeyboard() {
		int myVisibility = myKeyboardView.getVisibility();
		if (myVisibility == View.VISIBLE) {
			myKeyboardView.setVisibility(View.INVISIBLE);
		}
	}
}