package com.kt.moss.qtest.data;

import java.util.ArrayList;


/**
 * 인터넷 수리 데이터 스트럭처
 * @author Administrator
 *
 */

public class InternetData_R {

	
	
	HeaderData header;	
	ArrayList<BodyData_R> body = new ArrayList<BodyData_R>();
	ArrayList<InternetResultData> result = new ArrayList<InternetResultData>();
	ArrayList<InternetSpeedResultData> speedresult = new ArrayList<InternetSpeedResultData>();
	
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
	public ArrayList<InternetResultData> getResult() {
		return result;
	}
	public void setResult(ArrayList<InternetResultData> result) {
		this.result = result;
	}
	public ArrayList<InternetSpeedResultData> getSpeedResult() {
		return speedresult;
	}
	public void setSpeedResult(ArrayList<InternetSpeedResultData> speedresult) {
		this.speedresult = speedresult;
	}
	
	@Override
	public String toString() {
		return "InternetData_R [header=" + header + ", body=" + body
				+ ", result=" + result + ",speedresult="+speedresult+"]";
	}
	
	
	
	
}
