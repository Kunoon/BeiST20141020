package com.bei.smartravel.chen;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/**
 * 建立HTTP网络连接的基类
 * @author Bei_YK
 */
public class HttpUrlConMy {
	
	private List<String> returnList = null;
	
	public HttpUrlConMy() {
		// TODO Auto-generated constructor stub
		returnList = new ArrayList<String>();
		returnList.add(0, "");
		returnList.add(1, "");
	}
    /**
     * 通过HTTP URL连接到服务器并获取数据
     * @param urlStr 地址
     * @param content 参数
     * @return
     */
    public synchronized List<String> getResult(String urlStr, String content) {
    	returnList.set(0, "");
    	returnList.set(1, "");
    	URL url = null;
    	HttpURLConnection connection = null;
    	try {
    		url = new URL(urlStr);
    		connection = (HttpURLConnection) url.openConnection();
    		connection.setDoOutput(true);
    		connection.setDoInput(true);
    		connection.setRequestMethod("POST");
    		connection.setConnectTimeout(20000);
    		connection.setReadTimeout(20000);
    		connection.setUseCaches(false);
    		connection.connect();
    		
    		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
    		out.write(content.getBytes("utf-8"));
    		out.flush();
    		out.close();
    		
    		BufferedReader reader =
    				new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
    		returnList.set(0, Long.toString( connection.getDate()/1000 ));
    		StringBuffer buffer = new StringBuffer();
    		String line = "";
    		while ((line = reader.readLine()) != null) {
    			buffer.append(line);
    		}
    		reader.close();
    		returnList.set(1, buffer.toString());
    		return returnList;
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		if (connection != null) {
    			connection.disconnect();
    		}
    	}
    	return returnList;
    }
}
