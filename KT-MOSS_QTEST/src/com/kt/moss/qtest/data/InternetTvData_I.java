package com.kt.moss.qtest.data;

import java.util.ArrayList;

/**
 * 인터넷TV 개통 데이터 스트럭처
 * @author Administrator
 *
 */
public class InternetTvData_I {

	
	
	HeaderData header;	
	ArrayList<BodyData_I> body = new ArrayList<BodyData_I>();
	ArrayList<InternetTvResultData> result = new ArrayList<InternetTvResultData>();
	
	
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
	public ArrayList<InternetTvResultData> getResult() {
		return result;
	}
	public void setResult(ArrayList<InternetTvResultData> result) {
		this.result = result;
	}
	
	
	@Override
	public String toString() {
		return "InternetData_I [header=" + header + ", body=" + body
				+ ", result=" + result + "]";
	}
	

	
	
	
}
