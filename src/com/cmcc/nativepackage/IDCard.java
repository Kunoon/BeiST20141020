package com.cmcc.nativepackage;

import java.lang.Integer;

public class IDCard {
	
	static 
	{
		//System.loadLibrary("ist_idc");
		System.loadLibrary("CMCC_UNITDEVICE_WEWINS_A8");
	}

	//杩炴帴浜屼唬璇佽韩浠借瘑鍒澶�?	
	public static native int openIDCard(int idCardType,String deviceId,String password);
	
	public static native int closeIDCard();
	
	//鑾峰彇缁勪欢鐗堟湰淇℃伅
	public static native int getIDCardVersion(byte[] version);
	
	//鍒濆鍖栦簩浠ｈ瘉韬唤璇嗗埆璁惧锛屾竻闄や簩浠ｈ瘉韬唤璇嗗埆璁惧鍐呯紦�?樻暟鎹�?
	public static native int initialIDCard ();
	
	//鑾峰緱浜屼唬璇佷俊鎭�?	
	public static native int getIdCardInfo(String[] idCardInfo,byte[] img);

}
