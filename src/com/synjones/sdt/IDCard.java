package com.synjones.sdt;

public class IDCard {
	private String name;
	private String sex;
	private String nation;
	private String birthday;
	private String address;
	private String idcardno;
	private String grantdept;
	private String userlifebegin;
	private String userlifeend;
	private byte[] wlt;
	static public byte SW1, SW2, SW3;
	final private String[] nations = {
			"�����",			// 00
			"��",			// 01
			"�ɹ�",			// 02
			"��",			// 03
			"��",			// 04
			"ά���",			// 05
			"��",			// 06
			"��",			// 07
			"׳",			// 08
			"����",			// 09
			"����",			// 10
			"��",			// 11
			"��",			// 12
			"��",			// 13
			"��",			// 14
			"����",			// 15
			"����",			// 16
			"������",			// 17
			"��",			// 18
			"��",			// 19
			"����",			// 20
			"��",			// 21
			"�",			// 22
			"��ɽ",			// 23
			"����",			// 24
			"ˮ",			// 25
			"����",			// 26
			"����",			// 27
			"����",			// 28
			"�¶�����",		// 29
			"��",			// 30
			"���Ӷ�",			// 31
			"����",			// 32
			"Ǽ",			// 33
			"����",			// 34
			"����",			// 35
			"ë��",			// 36
			"����",			// 37
			"����",			// 38
			"����",			// 39
			"����",			// 40
			"������",			// 41
			"ŭ",			// 42
			"���α��",		// 43
			"����˹",			// 44
			"���¿�",			// 45
			"����",			// 46
			"����",			// 47
			"ԣ��",			// 48
			"��",			// 49
			"������",			// 50
			"����",			// 51
			"���״�",			// 52
			"����",			// 53
			"�Ű�",			// 54
			"���",			// 55
			"��ŵ",			// 56
			"�����",			// 57
			"����",			// 97
			"���Ѫͳ"			// 98
	};
	
	public IDCard() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNation() {
		return nation;
	}
	public String getNationName(String nation) {
		int nationcode = Integer.parseInt(nation);
		if (nationcode>=1 && nationcode<=56) {
			this.nation = nations[nationcode];			
		} else if (nationcode == 97) {
			this.nation = "����";
		} else if (nationcode == 98) {
			this.nation = "���Ѫͳ";
		} else {
			this.nation = "�����";
		}
		
		return this.nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIDCardNo() {
		return idcardno;
	}
	public void setIDCardNo(String idcardno) {
		this.idcardno = idcardno;
	}
	public String getGrantDept() {
		return grantdept;
	}
	public void setGrantDept(String grantdept) {
		this.grantdept = grantdept;
	}
	public String getUserLifeBegin() {
		return this.userlifebegin;
	}
	public void setUserLifeBegin(String userlifebegin) {
		this.userlifebegin = userlifebegin;
	}
	public String getUserLifeEnd() {
		return this.userlifeend;
	}
	public void setUserLifeEnd(String userlifeend) {
		this.userlifeend = userlifeend;
	}
	public byte[] getWlt() {
		return this.wlt;
	}
	public void setWlt(byte[] wlt) {
		this.wlt = wlt;
	}
}
