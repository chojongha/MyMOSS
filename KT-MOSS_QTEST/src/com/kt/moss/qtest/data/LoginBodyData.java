package com.kt.moss.qtest.data;

import com.google.gson.annotations.SerializedName;

public class LoginBodyData {

	
	
//	@SerializedName("result")
//	public String result = "";
//	
//	@SerializedName("errMsg")
//	public String errMsg = "";
	
	// 사번
	@SerializedName("loginId")
	public String loginId = "";
	
	// 사번
	@SerializedName("WORKERID")
	public String workerid = "";
	
	// 이름
	@SerializedName("username")
	public String username = "";
	
	// 부서코드
	@SerializedName("departmentCD")
	public String departmentCD = "";
	
	// 부서명
	@SerializedName("departmentName")
	public String departmentName = "";
	
	// 회사코드
	@SerializedName("companyCD")
	public String companyCD = "";
	
	// 회사명
	@SerializedName("mobilePhoneNum")
	public String mobilePhoneNum = "";
	
	
	// 이메일
	@SerializedName("email")
	public String email = "";


	@Override
	public String toString() {
		return "LoginBodyData [workerid=" + workerid + "]";
	}


	
		
	
	
	
	
}
