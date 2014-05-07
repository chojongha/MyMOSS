package com.kt.moss.qtest.data;

import java.util.ArrayList;


/**
 * 인터넷TV 수리 데이터 스트럭처
 * @author Administrator
 *
 */

public class InternetTvData_R {

	
	
	HeaderData header;	
	ArrayList<BodyData_R> body = new ArrayList<BodyData_R>();
	ArrayList<InternetTvResultData> result = new ArrayList<InternetTvResultData>();
	
	
	public HeaderData getHeader() {
		return header;
	}
	public void setHeader(HeaderData header) {
		this.header = header;
	}
	public ArrayList<BodyData_R> getBody() {
		return body;
	}
	public void setBody(ArrayList<BodyData_R> body) {
		this.body = body;
	}
	public ArrayList<InternetTvResultData> getResult() {
		return result;
	}
	public void setResult(ArrayList<InternetTvResultData> result) {
		this.result = result;
	}
	
	
	@Override
	public String toString() {
		return "InternetData_R [header=" + header + ", body=" + body
				+ ", result=" + result + "]";
	}
	
	
	
	
}
