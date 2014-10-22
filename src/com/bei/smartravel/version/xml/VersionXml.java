package com.bei.smartravel.version.xml;

import java.util.ArrayList;

/**
 * @author Yongkun
 */
public class VersionXml {
	private String stVersion, stUpDevices, stNUpDevices, stURL;
	private ArrayList<String> stUpFunctionList = new ArrayList<String>();

	public String getVersion() {
		return stVersion;
	}

	public void setVersion(String stVersion) {
		this.stVersion = stVersion;
	}

	public String getUpDevices() {
		return stUpDevices;
	}
	
	public void setUpDevices(String stUpDevices) {
		this.stUpDevices = stUpDevices;
	}
	
	public String getNUpDevices() {
		return stNUpDevices;
	}
	
	public void setNUpDevices(String stNUpDevices) {
		this.stNUpDevices = stNUpDevices;
	}
	
	public String getURL() {
		return stURL;
	}

	public void setURL(String stURL) {
		this.stURL = stURL;
	}

	public ArrayList<String> getUpFunction() {
		return stUpFunctionList;
	}
	
	public void setUpFunction(String stUpFunction) {
		this.stUpFunctionList.add(stUpFunction);
	}
	
//	@Override
//	public String toString() {
//		return "VERSION = " + getVersion() + "\n" + "NAME = " + getName() + "\n" + "URL = " + getURL();
//	}
}
