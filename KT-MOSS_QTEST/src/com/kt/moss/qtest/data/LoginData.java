package com.kt.moss.qtest.data;

import java.util.ArrayList;

public class LoginData {

	
	HeaderData header;	
	VersionData version;
	ArrayList<LoginBodyData> body = new ArrayList<LoginBodyData>();
	
	public HeaderData getHeader() {
		return header;
	}
	public void setHeader(HeaderData header) {
		this.header = header;
	}
	/*
	public VersionData getVersion() {
		return version;
	}
	public void setVersion(VersionData version) {
		this.version = version;
	}
	*/
	public ArrayList<LoginBodyData> getBody() {
		return body;
	}
	public void setBody(ArrayList<LoginBodyData> body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "LoginData [header=" + header + ", body=" + body + "]";
	}
}
