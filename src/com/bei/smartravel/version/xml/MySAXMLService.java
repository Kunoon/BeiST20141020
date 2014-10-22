package com.bei.smartravel.version.xml;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
/**
 * @author YongKun
 *
 */
public class MySAXMLService {
	public static ArrayList<VersionXml> readXML(String path) throws Exception {
		FileInputStream fileIS = new FileInputStream(path);
        StringBuffer sb=new StringBuffer();
        BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));
        String readString = new String();
        while((readString = buf.readLine())!= null){
        	sb.append(readString);
        }
		fileIS.close();
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();	// ´´½¨½âÎöÆ÷
		MyDefaultHandler myDefaultHandler = new MyDefaultHandler();
		parser.parse(new InputSource(new StringReader(sb.toString())), myDefaultHandler);
		return myDefaultHandler.getBeautyDate();
	}
}
