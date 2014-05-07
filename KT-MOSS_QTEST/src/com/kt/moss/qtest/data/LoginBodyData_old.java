package com.kt.moss.qtest.data;

import com.google.gson.annotations.SerializedName;



/**
 * 로그인 바디
 * @author Administrator
 *
 */
public class LoginBodyData_old {

	
	
	@SerializedName("result")
	public String result = "";
	
	@SerializedName("errMsg")
	public String errMsg = "";

	
	@Override
	public String toString() {
		return "LoginBodyData [result=" + result + ", errMsg=" + errMsg + "]";
	}



	
}
