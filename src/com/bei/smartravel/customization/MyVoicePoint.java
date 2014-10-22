package com.bei.smartravel.customization;

import com.bei.smartravel.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * ������ʾ��
 * @author YongKun
 *
 */
public class MyVoicePoint {
	private static MyVoicePoint myVoicePoint = null;
	private SoundPool soundPool = null;	// ����һ��SoundPool  
	private int musicBrushSuccess, musicBrushFail, musicBrushAgain, musicCheckSuccess
				, musicBrushInvalid, musicBrushToPay, musicChangeSucs, musicChangeFail;	// ����һ��������load������������suondID 
	
	public MyVoicePoint(Context context) {
		// TODO Auto-generated constructor stub
		if(soundPool == null)	// ��һ������Ϊͬʱ���������������������ڶ����������ͣ�����Ϊ��������
			soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		//����������زķŵ�res/raw���2��������Ϊ��Դ�ļ�����3��Ϊ���ֵ����ȼ�
		musicBrushSuccess = soundPool.load(context, R.raw.brush_success, 1);
        musicBrushFail = soundPool.load(context, R.raw.brush_fail, 1);
        musicBrushAgain = soundPool.load(context, R.raw.brush_again, 1);
        musicCheckSuccess = soundPool.load(context, R.raw.check, 1);
        musicBrushInvalid = soundPool.load(context, R.raw.invalid_tic, 1);
        musicBrushToPay = soundPool.load(context, R.raw.brush_topay, 1);
        musicChangeSucs = soundPool.load(context, R.raw.change_success, 1);
        musicChangeFail = soundPool.load(context, R.raw.change_fail, 1);
	}
	
	/**
	 * ���MyVoicePoint����
	 * @param context
	 * @return
	 */
	public static MyVoicePoint getMyVoicePoint(Context context) {
		if (myVoicePoint == null)
			myVoicePoint = new MyVoicePoint(context);
		return myVoicePoint;
	}
	
	/**
	 * ˢƱ�ɹ�
	 */
	public void BrushSuccess() {
		soundPool.play(musicBrushSuccess, 1, 1, 0, 0, 1);
	}
	
	/**
	 * ��Ч������ʾ
	 */
	public void BrushInvalid() {
		soundPool.play(musicBrushInvalid, 1, 1, 0, 0, 1);
	}
	
	/**
	 * ������ʾ
	 */
	public void BrushToPay() {
		soundPool.play(musicBrushToPay, 1, 1, 0, 0, 1);
	}
	
	/**
	 * ˢƱʧ��
	 */
	public void BrushFail() {
		soundPool.play(musicBrushFail, 1, 1, 0, 0, 1);
	}
	
	/**
	 * ����ˢ���֤
	 */
	public void BrushAgain() {
		soundPool.play(musicBrushAgain, 1, 1, 0, 0, 1);
	}
	
	/**
	 * ��Ʊ�ɹ�
	 */
	public void CheckSuccess() {
		soundPool.play(musicCheckSuccess, 1, 1, 0, 0, 1);
	}
	
	/**
	 * ��Ʊ�ɹ�
	 */
	public void ChangeSuccess() {
		soundPool.play(musicChangeSucs, 1, 1, 0, 0, 1);
	}
	
	/**
	 * ��Ʊʧ��
	 */
	public void ChangeFail() {
		soundPool.play(musicChangeFail, 1, 1, 0, 0, 1);
	}
}
