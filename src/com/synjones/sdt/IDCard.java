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
			"½âÂë´í",			// 00
			"ºº",			// 01
			"ÃÉ¹Å",			// 02
			"»Ø",			// 03
			"²Ø",			// 04
			"Î¬Îá¶û",			// 05
			"Ãç",			// 06
			"ÒÍ",			// 07
			"×³",			// 08
			"²¼ÒÀ",			// 09
			"³¯ÏÊ",			// 10
			"Âú",			// 11
			"¶±",			// 12
			"Ñþ",			// 13
			"°×",			// 14
			"ÍÁ¼Ò",			// 15
			"¹þÄá",			// 16
			"¹þÈø¿Ë",			// 17
			"´ö",			// 18
			"Àè",			// 19
			"ÀüËÛ",			// 20
			"Øô",			// 21
			"î´",			// 22
			"¸ßÉ½",			// 23
			"À­ìï",			// 24
			"Ë®",			// 25
			"¶«Ïç",			// 26
			"ÄÉÎ÷",			// 27
			"¾°ÆÄ",			// 28
			"¿Â¶û¿Ë×Î",		// 29
			"ÍÁ",			// 30
			"´ïÎÓ¶û",			// 31
			"ØïÀÐ",			// 32
			"Ç¼",			// 33
			"²¼ÀÊ",			// 34
			"ÈöÀ­",			// 35
			"Ã«ÄÏ",			// 36
			"ØîÀÐ",			// 37
			"Îý²®",			// 38
			"°¢²ý",			// 39
			"ÆÕÃ×",			// 40
			"Ëþ¼ª¿Ë",			// 41
			"Å­",			// 42
			"ÎÚ×Î±ð¿Ë",		// 43
			"¶íÂÞË¹",			// 44
			"¶õÎÂ¿Ë",			// 45
			"µÂêÄ",			// 46
			"±£°²",			// 47
			"Ô£¹Ì",			// 48
			"¾©",			// 49
			"ËþËþ¶û",			// 50
			"¶ÀÁú",			// 51
			"¶õÂ×´º",			// 52
			"ºÕÕÜ",			// 53
			"ÃÅ°Í",			// 54
			"çó°Í",			// 55
			"»ùÅµ",			// 56
			"±àÂë´í",			// 57
			"ÆäËû",			// 97
			"Íâ¹úÑªÍ³"			// 98
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
			this.nation = "ÆäËû";
		} else if (nationcode == 98) {
			this.nation = "Íâ¹úÑªÍ³";
		} else {
			this.nation = "±àÂë´í";
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
