package com.bei.smartravel.taobao.xml;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
/**
 * 
 * @author Bei_YK
 */
public class TBValidData {
	public String getTBValidData(String xmlStr) {
		DefaultHandler4TBXML myDefaultHandler4TBXML = null;
		ParsedTBXMLDataSet myParsedTBXMLDataSet = null;
		try {
			StringReader read = new StringReader(xmlStr);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp;
			sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			myDefaultHandler4TBXML = new DefaultHandler4TBXML();
			xr.setContentHandler(myDefaultHandler4TBXML);
			xr.parse(source);
			myParsedTBXMLDataSet = myDefaultHandler4TBXML.getParsedData();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  myParsedTBXMLDataSet.getTBDataStr();
	}
}
