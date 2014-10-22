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
 * �Զ������֤�����������
 * @author YongKun
 */
public class MyKeyboard4IDNum {
	private Context myContext;
	private Activity myActivity;
	private KeyboardView myKeyboardView;
	private Keyboard keyboardSymbols;	// ���ּ���, ��ĸ����
	private EditText myEditText;
	public boolean isNum = false;	// �Ƿ����ݼ���
	public boolean isCapital = false;	// �Ƿ��д
	private SoundPool soundPool;	// ����һ��SoundPool  
	private int music;	// ����һ��������load������������suondID  

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
		// ��һ������Ϊͬʱ���������������������ڶ����������ͣ�����Ϊ��������
		soundPool= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		//����������زķŵ�res/raw���2��������Ϊ��Դ�ļ�����3��Ϊ���ֵ����ȼ�
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
			// ���Ű�����Ч
			soundPool.play(music, 1, 1, 0, 0, 1);
			Editable editable = myEditText.getText();
			int start = myEditText.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {	// ���
				Intent intent = new Intent();
	            intent.putExtra("AAA", editable.toString());
	            // Set result and finish this Activity
	            myActivity.setResult(Activity.RESULT_OK, intent);
				myActivity.finish();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {	// ����
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
	 * ��ʾ����
	 */
	public void showKeyboard() {
		int myVisibility = myKeyboardView.getVisibility();
		if (myVisibility == View.GONE || myVisibility == View.INVISIBLE) {
			myKeyboardView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * ���ؼ���
	 */
	public void hideKeyboard() {
		int myVisibility = myKeyboardView.getVisibility();
		if (myVisibility == View.VISIBLE) {
			myKeyboardView.setVisibility(View.INVISIBLE);
		}
	}
}