package com.kt.moss.qtest.data;

import com.google.gson.annotations.SerializedName;

public class InternetTvResultData {

	
	
	// 테스트 날짜
	@SerializedName("TESTDATE")
	public String testDate = "";
	
	// 핑스피드최소값
	@SerializedName("PINGSPEEDMIN")
	public String pingSpeedMin = "";
			
	// 핑스피드평균값
	@SerializedName("PINGSPEEDAVG")
	public String pingSpeedAvg = "";
	
	// 핑스피드최대값
	@SerializedName("PINGSPEEDMAX")
	public String pingSpeedMax = "";
		
	// 로스
	@SerializedName("PINGPACKETLOSSCNT")
	public String pingPacketLossCnt = "";
		
	// FTP다운로드속도최소값
	@SerializedName("FTPDOWNSPEEDMIN")
	public String ftpDownSpeedMin = "";
	
	// FTP다운로드속도평균값
	@SerializedName("FTPDOWNSPEEDAVG")
	public String ftpDownSpeedAvg = "";
	
	// FTP다운로드속도최대값
	@SerializedName("FTPDOWNSPEEDMAX")
	public String ftpDownSpeedMax = "";

	@Override
	public String toString() {
		return "InternetTvResultData [TESTDATE=" + testDate + ", PINGSPEEDMIN="
				+ pingSpeedMin + ", PINGSPEEDAVG=" + pingSpeedAvg
				+ ", PINGSPEEDMAX=" + pingSpeedMax + ", PINGPACKETLOSSCNT="
				+ pingPacketLossCnt + ", FTPDOWNSPEEDMIN=" + ftpDownSpeedMin
				+ ", FTPDOWNSPEEDAVG=" + ftpDownSpeedAvg + ", FTPDOWNSPEEDMAX="
				+ ftpDownSpeedMax + "]";
	}

	

	
	
	
	
}
