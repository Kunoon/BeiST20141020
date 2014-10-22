package com.bei.smartravel.taobao.xml;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * @author Bei_YK
 */
public class DefaultHandler4TBXML extends DefaultHandler{
	private String firstElement = "", validElement = "";
	private boolean in_mytag = false;
	private ParsedTBXMLDataSet myParsedTBXMLDataSet = new ParsedTBXMLDataSet();

	public ParsedTBXMLDataSet getParsedData() {
		return this.myParsedTBXMLDataSet;
	}
	
	public void startDocument() throws SAXException {
		this.myParsedTBXMLDataSet = new ParsedTBXMLDataSet();
	}
	
	public void endDocument() throws SAXException {

	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if(firstElement.length() == 0)
			firstElement = qName;
		if ( ( firstElement.equals("vmarket_eticket_auth_beforeconsume_response")
				|| firstElement.equals("error_response") ) 
				&& ( qName.equals("order_id") || qName.equals("sub_msg") ) ) {
			validElement = qName;
			this.in_mytag = true;
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (firstElement.equals("vmarket_eticket_auth_beforeconsume_response") && qName.equals("order_id")) {
			firstElement = "";
			validElement = "";
			this.in_mytag = false;
		}
	}

	public void characters(char ch[], int start, int length) {
		if(this.in_mytag){
			myParsedTBXMLDataSet.setTBDataStr(validElement + "%" + new String(ch, start, length));
		}
	}
}