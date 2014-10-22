package com.bei.smartravel.version.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX XML�ļ�����
 * @author Yongkun
 */
public class MyDefaultHandler extends DefaultHandler {
	private ArrayList<VersionXml> versionXmlList = new ArrayList<VersionXml>();
	private VersionXml versionXml = null;
	private String currentElementName = "";
	
	private final String UPDATE_BST = "update";
	private final String VERSION_BST = "version";
	private final String UPDEVICE_BST = "upd";
	private final String NUPDEVICE_BST = "nupd";
	private final String URL_BST = "url";
	private final String FUNCTION_BST = "function";

	public ArrayList<VersionXml> getBeautyDate() {
		return versionXmlList;
	}
	
	/**
	 * ��ʼ����xmlʱ����
	 */
	@Override
	public void startDocument() throws SAXException {
		
	}
	
	/**
	 * uri�������ռ�
	 * localName�����������ռ�ǰ׺�ı�ǩ��
	 * qName���������ռ�ǰ׺�ı�ǩ��
	 * attributes�����Լ���
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentElementName = localName;
		if(localName.equals(UPDATE_BST)){
			versionXml = new VersionXml();
		}
	}
	
	/**
	 * ch������
	 * start����ʼλ��
	 * length������
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String textContent = new String(ch, start, length);
		if(currentElementName.equals(VERSION_BST)&&textContent!=null&&!textContent.trim().equals("")){
			versionXml.setVersion(textContent);
		}
		if(currentElementName.equals(UPDEVICE_BST)&&textContent!=null&&!textContent.trim().equals("")){
			versionXml.setUpDevices(textContent);
		}
		if(currentElementName.equals(NUPDEVICE_BST)&&textContent!=null&&!textContent.trim().equals("")){
			versionXml.setNUpDevices(textContent);
		}
		if(currentElementName.equals(URL_BST)&&textContent!=null&&!textContent.trim().equals("")){
			versionXml.setURL(textContent);
		}
		if(currentElementName.equals(FUNCTION_BST)&&textContent!=null&&!textContent.trim().equals("")){
			versionXml.setUpFunction(textContent);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(localName.equalsIgnoreCase(UPDATE_BST)){
			versionXmlList.add(versionXml);
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		
	}
}