package com.kt.moss.qtest.data;

import java.util.ArrayList;

public class VcTestData {

	
	
	HeaderData header;	
	ArrayList<VcTestBodyData> body = new ArrayList<VcTestBodyData>();
	
				
	
	public HeaderData getHeader() {
		return header;
	}
	
	public void setHeader(HeaderData header) {
		this.header = header;
	}

	public ArrayList<VcTestBodyData> getBody() {
		return body;
	}

	public void setBody(ArrayList<VcTestBodyData> body) {
		this.body = body;
	}

	
	@Override
	public String toString() {
		return "VcTestData [header=" + header + ", body=" + body + "]";
	}
	


	
}
