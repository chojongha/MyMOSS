package com.kt.moss.qtest.data;

import com.google.gson.annotations.SerializedName;

/**
 * 인터넷전화 개통 테스트 결과
 * @author Administrator
 *
 */
public class InternetPhoneResultData {

	
		
	// 일반/LOOPBACK 여부
	@SerializedName("TESTTYPE")
	public String testType = "";
	
	// 만족/미달 여부
	@SerializedName("TESTQUALITY")
	public String testQuality = "";
	
	// 측정일시
	@SerializedName("TESTDATE")
	public String testDate = "";
	
	// 발신번호
	@SerializedName("CALLER")
	public String caller = "";
	
	// 테스트 고객번호
	@SerializedName("CALLEE")
	public String callee = "";
	
	// 콜방향
	@SerializedName("CALLDIRECTION")
	public String callDirection = "";
	
	// 미디어 방향
	@SerializedName("MEDIADIRECTION")
	public String mediaDirection = "";
	
	// 실제통화에 쓰이는 codec 정보
	@SerializedName("CODECTYPE")
	public String codecType = "";
	
	// 원격 RTP 아이피
	@SerializedName("REMOTERTPIP")
	public String remoterTpIp = "";
	
	// 원격 RTP 포트
	@SerializedName("REMOTERTPPORT")
	public String remoterTpPort = "";
	
	// 테스트 시작시각
	@SerializedName("CALLTIME")
	public String callTime = "";
	
	// 테스트 종료시각
	@SerializedName("ENDTIME")
	public String endTime = "";
	
	// 코드(-1=성공)
	@SerializedName("ERRCAUSE")
	public String errCause = "";
	
	// 수신패킷수
	@SerializedName("RXRTP")
	public String rxRtp = "";
	
	// 송신패킷수
	@SerializedName("TXRTP")
	public String txRtp = "";
	
	// 손실패킷수
	@SerializedName("RTPLOSS")
	public String rtpLoss = "";
	
	// 지터손실
	@SerializedName("JITTERLOSS")
	public String jitterLoss = "";
	
	// 손실률(평균)
	@SerializedName("LOSSAVG")
	public String lossAvg = "";
	
	// 손실률(최대)
	@SerializedName("LOSSMAX")
	public String lossMax = "";
	
	// 지터(평균)
	@SerializedName("JITTERAVG")
	public Double jitterAvg = 0.0;
	
	// 지터(최대)
	@SerializedName("JITTERMAX")
	public Double jitterMax = 0.0;
	
	// 지연(평균)
	@SerializedName("DELAYAVG")
	public String delayAvg = "";
	
	// 지연(최대)
	@SerializedName("DELAYMAX")
	public String delayMax = "";
	
	// R값(평균)
	@SerializedName("CQRAVG")
	public String cqrAvg = "";
	
	// R값(최소)
	@SerializedName("CQRMIN")
	public String cqrMin = "";
	
	// MOS(평균)
	@SerializedName("CQMOSAVG")
	public Double cqmosAvg = 0.0;
	
	// MOS(최소)
	@SerializedName("CQMOSMIN")
	public Double cqmosMin = 0.0;
	
	// PESQ(평균)
	@SerializedName("PESQAVG")
	public Double pesqAvg = 0.0;
	
	// PESQ(최소)
	@SerializedName("PESQMIN")
	public Double pesqMin = 0.0;

	@Override
	public String toString() {
		return "InternetPhoneResultData [TESTTYPE=" + testType
				+ ", TESTQUALITY=" + testQuality + ", TESTDATE=" + testDate
				+ ", CALLER=" + caller + ", CALLEE=" + callee
				+ ", CALLDIRECTION=" + callDirection + ", MEDIADIRECTION="
				+ mediaDirection + ", CODECTYPE=" + codecType
				+ ", REMOTERTPIP=" + remoterTpIp + ", REMOTERTPPORT="
				+ remoterTpPort + ", CALLTIME=" + callTime + ", ENDTIME="
				+ endTime + ", ERRCAUSE=" + errCause + ", RXRTP=" + rxRtp
				+ ", TXRTP=" + txRtp + ", RTPLOSS=" + rtpLoss + ", JITTERLOSS="
				+ jitterLoss + ", LOSSAVG=" + lossAvg + ", LOSSMAX=" + lossMax
				+ ", JITTERAVG=" + jitterAvg + ", JITTERMAX=" + jitterMax
				+ ", DELAYAVG=" + delayAvg + ", DELAYMAX=" + delayMax
				+ ", CQRAVG=" + cqrAvg + ", CQRMIN=" + cqrMin + ", CQMOSAVG="
				+ cqmosAvg + ", CQMOSMIN=" + cqmosMin + ", PESQAVG=" + pesqAvg
				+ ", PESQMIN=" + pesqMin + "]";
	}



	

	
	
	
}
