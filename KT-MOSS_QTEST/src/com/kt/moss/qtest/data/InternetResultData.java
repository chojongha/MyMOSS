package com.kt.moss.qtest.data;

import com.google.gson.annotations.SerializedName;

/**
 * 인터넷 개통 결과
 * @author Administrator
 *
 */
public class InternetResultData {

	
		
	// 회선인증 완료여부 ('Y'인경우 광수신품질 UPDATE 금지)
	@SerializedName("AUTHCOMPLD")
	public String authCompld = "";
		
	// 측정일시
	@SerializedName("OPTICTESTDATE")
	public String opticTestDate = "";
		
	// 파장
	@SerializedName("OPTICWAVELENGTH")
	public String opticWaveLength = "";
		
	// 파워
	@SerializedName("OPTICTESTQUALITY")
	public String opticTestQuality = "";
	
	// Reference
	@SerializedName("OPTICTESTREFERENCE")
	public String opticTestReference = "";

	@Override
	public String toString() {
		return "InternetResultData [AUTHCOMPLD=" + authCompld
				+ ", OPTICTESTDATE=" + opticTestDate + ", OPTICWAVELENGTH="
				+ opticWaveLength + ", OPTICTESTQUALITY=" + opticTestQuality
				+ ", OPTICTESTREFERENCE=" + opticTestReference + "]";
	}
	
	

	

	
	
	
	
	
	
	
	
	
}
