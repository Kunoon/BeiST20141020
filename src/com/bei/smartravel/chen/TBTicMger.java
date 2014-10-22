package com.bei.smartravel.chen;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import android.os.Handler;
/**
 * 与淘宝网站交互，通过用户输入的验证码获得淘宝订单信息
 * @author Bei_YK
 */
public class TBTicMger {
	
	public static TBTicMger getTBTicMger(Handler handler){
		myHandler = handler;
		if (tBTicMger == null)
			tBTicMger = new TBTicMger();
		return tBTicMger;
	}
	
	public TBTicMger() {
		// TODO Auto-generated constructor stub
		myHttpUrlConMy = new HttpUrlConMy();
	}
	
	/**
	 * 获得淘宝订单号
	 * @param tbCode 淘宝验证码
	 * @return 淘宝订单号
	 */
	public void getTBOrderID(final String tbCode) {
    	new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String tmpStrID = myHttpUrlConMy.getResult(TBUrl, TBUserParams(tbCode)).get(1);
				if( tmpStrID.length() != 0 ) {
					myHandler.obtainMessage(0, tmpStrID).sendToTarget();
				} else {
					myHandler.obtainMessage(1, "网络不可用").sendToTarget();
				}
			}
		}).start();
	}
	
    /**
    * 新的md5签名，首尾放secret。
    * @param secret 分配给您的APP_SECRET
    */
    private String md5Signature(TreeMap<String, String> params, String secret) {
    	String result = null;
    	StringBuffer orgin = getBeforeSign(params, new StringBuffer(secret));
    	if (orgin == null)
    		return result;
    	orgin.append(secret);
    	try {
    		MessageDigest md = MessageDigest.getInstance("MD5");
    		result = byte2hex(md.digest(orgin.toString().getBytes("utf-8")));
    	} catch (Exception e) {
    		throw new java.lang.RuntimeException("sign error !");
    	}
    	return result;
	}
    
    /**
    * 二行制转字符串
    */
    private String byte2hex(byte[] b) {
    	StringBuffer hs = new StringBuffer();
    	String stmp = "";
    	for (int n = 0; n < b.length; n++) {
    		stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
    		if (stmp.length() == 1)
    			hs.append("0").append(stmp);
    		else
    			hs.append(stmp);
    	}
    	return hs.toString().toUpperCase();
	}
    
    /**
    * 添加参数的封装方法
    */
    private StringBuffer getBeforeSign(TreeMap<String, String> params, StringBuffer orgin) {
    	if (params == null)
    		return null;
    	Map<String, String> treeMap = new TreeMap<String, String>();
    	treeMap.putAll(params);
    	Iterator<String> iter = treeMap.keySet().iterator();
    	while (iter.hasNext()) {
    		String name = (String) iter.next();
    		orgin.append(name).append(params.get(name));
    	}
    	return orgin;
	}
    
    /**
     * 打包网络通信参数
     * @return
     */
    private String TBUserParams(String tbCode) {
        TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
        apiparamsMap.put("method", "taobao.vmarket.eticket.auth.beforeconsume");
        apiparamsMap.put("session", "6101d02ee51c72c33804f46079d7eeecabb3f3261b0d2671719190675");
        String timestamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        apiparamsMap.put("timestamp",timestamp);
        apiparamsMap.put("format", "xml");
        apiparamsMap.put("app_key","21539199");
        apiparamsMap.put("v", "2.0");
        apiparamsMap.put("sign_method", "md5");
        apiparamsMap.put("operatorid", "50701");
        apiparamsMap.put("verify_code", tbCode);
        //生成签名"30217087"
        String sign = md5Signature(apiparamsMap,secret);
        apiparamsMap.put("sign", sign);
        StringBuilder param = new StringBuilder();
        for (Iterator<Map.Entry<String, String>> it = apiparamsMap.entrySet()
        .iterator(); it.hasNext();) {
            Map.Entry<String, String> e = it.next();
            param.append("&").append(e.getKey()).append("=").append(e.getValue());
        }
        return param.toString().substring(1);
    }

	private static TBTicMger tBTicMger = null;
	private static Handler myHandler;
	private HttpUrlConMy myHttpUrlConMy;
	
    private String TBUrl = "http://gw.api.taobao.com/router/rest";
    private String secret = "153e4672165a909cd82bd2176fdb6690";
}
