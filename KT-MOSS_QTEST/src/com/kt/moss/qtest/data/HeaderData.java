package com.kt.moss.qtest.data;

import com.google.gson.annotations.SerializedName;

/**
 * JSON 헤더 데이터
 * 
 * @author Administrator
 * 
 */
public class HeaderData {

	@SerializedName("result")
	public String result = "";

	@SerializedName("errMsg")
	public String errMsg = "";

	@Override
	public String toString() {
		return "HeaderData [result=" + result + ", errMsg=" + errMsg + "]";
	}
}
