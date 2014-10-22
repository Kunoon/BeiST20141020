package com.bei.smartravel.customization;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用于MD5加密
 * @author Yongkun
 */
public class MD5Encryption {
	private static MD5Encryption myMD5Encryption = null;
	
	public MD5Encryption() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 获得MyJudgeTime对象
	 * @return
	 */
	public static MD5Encryption getMD5Encryption() {
		if (myMD5Encryption == null)
			myMD5Encryption = new MD5Encryption();
		return myMD5Encryption;
	}
    /**
     * @param plainText 明文
     * @return 32位密文
     */
    public String encryption(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
 
            int i;
 
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
 
            re_md5 = buf.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5.toUpperCase();
    }
}
