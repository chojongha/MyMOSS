package com.kt.moss.qtest.data;

import com.google.gson.annotations.SerializedName;

public class VcTestBodyData {
	
	
	// VC 측정 성공 여부(0:성공, 0이외의 값: 실패)
	@SerializedName("result")
	public  String result = "";

	
	// 품질측정 결과
	@SerializedName("quality")
	public  String quality = "";

	
	// 에러 내용.
	@SerializedName("errMsg")
	public  String errMsg = "";

	
	// 에러 이유
	@SerializedName("errCause")
	public  String errCause = "";


	@Override
	public String toString() {
		return "VcTestBodyData [result=" + result + ", quality=" + quality
				+ ", errMsg=" + errMsg + ", errCause=" + errCause + "]";
	}

	
	
	
	
	
}
