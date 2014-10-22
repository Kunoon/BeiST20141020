package com.sdses.idreader;

import java.io.UnsupportedEncodingException;

import com.sdses.tool.Util;

import android.util.Log;

public class ID2Info {

	private String mName;
	private String mGender;
	private String mNational;
	private String mBirthYear;
	private String mBirthMonth;
	private String mBirthDay;
	private String mAddress;
	private String mID2Num;
	private String mIssue;
	private String mBegin;
	private String mEnd;
	private byte[] id2Info = new byte[256];
	
	public ID2Info() {
	}
	public void setID2Info(byte[] id2Info) {
		Util.memcpy(this.id2Info, 0, id2Info, 0, 256);
	}
	public void MakeID2() {
		byte[] name = new byte[30];
		byte[] gender = new byte[2];
		byte[] national = new byte[4];
		byte[] birthday = new byte[16];
		byte[] address = new byte[70];
		byte[] identity = new byte[36];
		byte[] issued = new byte[30];
		byte[] start = new byte[16];
		byte[] end = new byte[16];

		Util.memcpy(name, 0, id2Info, 0, 30);
		Util.memcpy(gender, 0, id2Info, 30, 2);
		Util.memcpy(national, 0, id2Info, 32, 4);
		Util.memcpy(birthday, 0, id2Info, 36, 16);
		Util.memcpy(address, 0, id2Info, 52, 70);
		Util.memcpy(identity, 0, id2Info, 122, 36);
		Util.memcpy(issued, 0, id2Info, 158, 30);
		Util.memcpy(start, 0, id2Info, 188, 16);
		Util.memcpy(end, 0, id2Info, 204, 16);
		
		try {
			mName = new String(name,"UTF-16LE");
			Log.w("ID2Info","mName is:"+mName);
			String sGender = new String(gender, "UTF-16LE");
			mGender = Integer.valueOf(sGender) == 1 ? "�� ": "Ů";

			mNational = GetNationalFromCode(new String(national, "UTF-16LE"));

			String sBirthday = new String(birthday, "UTF-16LE");
			mBirthYear = String.format("%s", sBirthday.substring(0, 4));
			mBirthMonth = String.format("%s", sBirthday.substring(4, 6));
			mBirthDay = String.format("%s", sBirthday.substring(6, 8));

			mAddress = new String(address, "UTF-16LE");
			mID2Num= new String(identity, "UTF-16LE");
			mIssue = new String(issued, "UTF-16LE");
			String sStart = new String(start, "UTF-16LE");
			mBegin = String.format("%s.%s.%s", sStart.substring(0, 4),
					sStart.substring(4, 6), sStart.substring(6, 8));
			String sEnd = new String(end, "UTF-16LE");
			String strTem = sEnd.substring(0,4);
			Log.w("777","strTem is:"+strTem);
			if (strTem.equals("����")) {
				mEnd = strTem;
			}else {
				mEnd = String.format("%s.%s.%s", sEnd.substring(0, 4),
						sEnd.substring(4, 6), sEnd.substring(6, 8));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void Destroy(){
	}

	public String getName(){
		return mName;
	}

	public String getGender() {
		return mGender;
	}

	public String getNational(){
		return mNational;
	}

	public String getBirthYear() {
		return mBirthYear;
	}

	public String getBirthMonth() {
		return mBirthMonth;
	}
	public String getBirthDay() {
		return mBirthDay;
	}

	public String getAddress(){
		return mAddress;
	}
	public String getIndentityCard(){
		return mID2Num;
	}

	public String getIssued() {
		return mIssue;
	}

	public String getBeginTime() {
		return mBegin;
	}
	public String getEndTime() {
		return mEnd;
	}

	private String GetNationalFromCode(String nationalCode) {
		int n = Integer.valueOf(nationalCode);
		switch (n) {
		case 1:
			return "��";
		case 2:
			return "�ɹ�";
		case 3:
			return "��";
		case 4:
			return "��";
		case 5:
			return "ά���";
		case 6:
			return "��";
		case 7:
			return "��";
		case 8:
			return "׳";
		case 9:
			return "����";
		case 10:
			return "����";
		case 11:
			return "��";
		case 12:
			return "��";
		case 13:
			return "��";
		case 14:
			return "��";
		case 15:
			return "����";
		case 16:
			return "����";
		case 17:
			return "������";
		case 18:
			return "��";
		case 19:
			return "��";
		case 20:
			return "����";
		case 21:
			return "��";
		case 22:
			return "�";
		case 23:
			return "��ɽ";
		case 24:
			return "����";
		case 25:
			return "ˮ";
		case 26:
			return "����";
		case 27:
			return "����";
		case 28:
			return "����";
		case 29:
			return "�¶�����";
		case 30:
			return "��";
		case 31:
			return "���Ӷ�";
		case 32:
			return "����";
		case 33:
			return "Ǽ";
		case 34:
			return "����";
		case 35:
			return "����";
		case 36:
			return "ë��";
		case 37:
			return "����";
		case 38:
			return "����";
		case 39:
			return "����";
		case 40:
			return "����";
		case 41:
			return "������";
		case 42:
			return "ŭ";
		case 43:
			return "���α��";
		case 44:
			return "����˹";
		case 45:
			return "���¿�";
		case 46:
			return "����";
		case 47:
			return "����";
		case 48:
			return "ԣ��";
		case 49:
			return "��";
		case 50:
			return "������";
		case 51:
			return "����";
		case 52:
			return "���״�";
		case 53:
			return "����";
		case 54:
			return "�Ű�";
		case 55:
			return "���";
		case 56:
			return "��ŵ";
		default:
			return "����";
		}
	}
}
