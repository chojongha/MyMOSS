package com.kt.moss.qtest.data;

import com.google.gson.annotations.SerializedName;

public class WorkOrderBodyData   {

	
	// 개통오더번호
	@SerializedName("WORKODRNUM")
	public String workOdrNum = "";	
	
	// 개통(I), 수리(R) 여부
	@SerializedName("WORKODRKIND")
	public String workOdrKind = "";
	
	// 오더타입 인터넷(I), 인터넷TV(T), 인터넷전화(P)
	@SerializedName("ODRTYPE")
	public String odrType = "";
	
	// 고객명
	@SerializedName("CUSTNAME")
	public String custName = "";
	
	// 주소
	@SerializedName("ADDR")
	public String addr = "";
	
	// 기타주소
	@SerializedName("ADDRETC")
	public String addrEtc = "";
	
	// 전화번호
	@SerializedName("TELNUM")
	public String telNum = "";
	
	
	
	// 예약 일시
	@SerializedName("RSVDT")
	public String rsvDT = "";

	// 상태값 (P: 진행중, C:측정완료)
	@SerializedName("INPROGRESS")
	public String inprogress = "";

	
	@Override
	public String toString() {
		return "WorkOrderBodyData [WORKODRNUM=" + workOdrNum + ", WORKODRKIND="
				+ workOdrKind + ",  ODRTYPE=" + odrType + ", CUSTNAME="
				+ custName + ", ADDR=" + addr + ", ADDRETC=" + addrEtc
				+ ", TELNUM=" + telNum + ", RSVDT=" + rsvDT + ", INPROGRESS="
				+ inprogress + "]";
	}

	


	
	
	
	
	
	
	


	
	
}
