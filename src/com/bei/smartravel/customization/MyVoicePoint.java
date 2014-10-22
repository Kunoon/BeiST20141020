package com.bei.smartravel.customization;

import com.bei.smartravel.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * 语音提示类
 * @author YongKun
 *
 */
public class MyVoicePoint {
	private static MyVoicePoint myVoicePoint = null;
	private SoundPool soundPool = null;	// 声明一个SoundPool  
	private int musicBrushSuccess, musicBrushFail, musicBrushAgain, musicCheckSuccess
				, musicBrushInvalid, musicBrushToPay, musicChangeSucs, musicChangeFail;	// 定义一个整型用load（）；来设置suondID 
	
	public MyVoicePoint(Context context) {
		// TODO Auto-generated constructor stub
		if(soundPool == null)	// 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
			soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		//把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
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
	 * 获得MyVoicePoint对象
	 * @param context
	 * @return
	 */
	public static MyVoicePoint getMyVoicePoint(Context context) {
		if (myVoicePoint == null)
			myVoicePoint = new MyVoicePoint(context);
		return myVoicePoint;
	}
	
	/**
	 * 刷票成功
	 */
	public void BrushSuccess() {
		soundPool.play(musicBrushSuccess, 1, 1, 0, 0, 1);
	}
	
	/**
	 * 无效订单提示
	 */
	public void BrushInvalid() {
		soundPool.play(musicBrushInvalid, 1, 1, 0, 0, 1);
	}
	
	/**
	 * 付款提示
	 */
	public void BrushToPay() {
		soundPool.play(musicBrushToPay, 1, 1, 0, 0, 1);
	}
	
	/**
	 * 刷票失败
	 */
	public void BrushFail() {
		soundPool.play(musicBrushFail, 1, 1, 0, 0, 1);
	}
	
	/**
	 * 重新刷身份证
	 */
	public void BrushAgain() {
		soundPool.play(musicBrushAgain, 1, 1, 0, 0, 1);
	}
	
	/**
	 * 检票成功
	 */
	public void CheckSuccess() {
		soundPool.play(musicCheckSuccess, 1, 1, 0, 0, 1);
	}
	
	/**
	 * 改票成功
	 */
	public void ChangeSuccess() {
		soundPool.play(musicChangeSucs, 1, 1, 0, 0, 1);
	}
	
	/**
	 * 改票失败
	 */
	public void ChangeFail() {
		soundPool.play(musicChangeFail, 1, 1, 0, 0, 1);
	}
}
