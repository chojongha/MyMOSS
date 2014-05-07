package com.kt.moss.qtest.data;

import java.util.ArrayList;

/**
 * 인터넷전화 개통 데이터 스트럭처
 * @author Administrator
 *
 */
public class InternetPhoneData_I {

	
	
	HeaderData header;	
	ArrayList<BodyData_I> body = new ArrayList<BodyData_I>();
	ArrayList<InternetPhoneResultData> result = new ArrayList<InternetPhoneResultData>();
	
	
	public HeaderData getHeader() {
		return header;
	}
	public void setHeader(HeaderData header) {
		this.header = header;
	}
	public ArrayList<BodyData_I> getBody() {
		return body;
	}
	public void setBody(ArrayList<BodyData_I> body) {
		this.body = body;
	}
	
		
	
	
	public ArrayList<InternetPhoneResultData> getResult() {
		return result;
	}
	public void setResult(ArrayList<InternetPhoneResultData> result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "InternetPhoneData_I [header=" + header + ", body=" + body
				+ ", result=" + result + "]";
	}

	
	
	
	
	
	
	
	
}
