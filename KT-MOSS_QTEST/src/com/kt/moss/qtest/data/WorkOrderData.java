package com.kt.moss.qtest.data;

import java.util.ArrayList;


/**
 * 리스트 데이터
 * 
 * @author Administrator
 *
 */

public class WorkOrderData {
	

	HeaderData header;	
	ArrayList<WorkOrderBodyData> body = new ArrayList<WorkOrderBodyData>();
	
				
	
	public HeaderData getHeader() {
		return header;
	}
	
	public void setHeader(HeaderData header) {
		this.header = header;
	}
	
	public ArrayList<WorkOrderBodyData> getBody() {
		return body;
	}
	
	public void setBody(ArrayList<WorkOrderBodyData> body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "WorkOrderData [header=" + header + ", body=" + body + "]";
	}
	
	
	
}
