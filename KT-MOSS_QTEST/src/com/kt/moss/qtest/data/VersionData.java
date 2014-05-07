package com.kt.moss.qtest.data;

import com.google.gson.annotations.SerializedName;


/**
 * JSON 헤더 데이터
 * @author Administrator
 *
 */
public class VersionData {

	
	@SerializedName("SWVERSION")
	public  String ver = "";

	@Override
	public String toString() {
		return "VersionData [SWVERSION=" + ver + "]";
	}
	

	

	
	
}
