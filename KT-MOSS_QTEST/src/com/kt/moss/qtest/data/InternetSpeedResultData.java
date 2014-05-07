package com.kt.moss.qtest.data;

import com.google.gson.annotations.SerializedName;

/**
 * 인터넷 개통 속도 측정 결과
 * @author Administrator
 *
 */
public class InternetSpeedResultData {

	
		
	//수용국표준코드
	@SerializedName("OFFICESCODE")
	public String officescode = "";
		
	//측정일시
	@SerializedName("TESTDATE")
	public String testdate = "";
		
	//측정서버코드
	@SerializedName("SERVERINFO")
	public String serverinfo = "";
		
    //평균UDP지터지연시간
	@SerializedName("JITTERAVG")
	public String jitteravg = "";
	
	//웹브라우저
	@SerializedName("BROWSER")
	public String browser = "";
 
	//최대패킷지연시간
	@SerializedName("PACKDELAYTIMEMAX")
	public String packdelaytimemax = "";

	//오더구분(1:개통오더, 2:수리오더)
	@SerializedName("WORKODRKIND")
	public String workodrkind  = "";

	//최소패킷지연시간
	@SerializedName("PACKDELAYTIMEMIN")
	public String packdelaytimemin = "";

	//CPU부하율
	@SerializedName("CPURATE")
	public String cpurate = "";

	//평균업로드속도
	@SerializedName("UPLOADSPEEDAVG")
	public String uploadspeedavg = "";

	//최대다운로드속도
	@SerializedName("DOWNLOADSPEEDMAX")
	public String downloadspeedmax = "";

	//UDP지연시간표준편차(1ms*1000)
	@SerializedName("UDPDELAYTIMESTD")
	public String udpdelaytimestd = "";

	//광 측정 품질
	@SerializedName("OPTICTESTQUALITY")
	public String optictestquality = "";
	
	//패킷손실률
	@SerializedName("PACKLOSSRATE")
	public String packlossrate = "";

	//메모리사용율
	@SerializedName("MEMORYRATE")
	public String memoryrate = "";
	
	//UDP패킷손실율
	@SerializedName("UDPLOSSRATE")
	public String udplossrate = ""; 
	
	//UDP지터지연시간편차(1ms*1000)
	@SerializedName("JITTERSTD")
	public String jitterstd = "";
	
	//최소UDP지터지연시간
	@SerializedName("JITTERMIN")
	public String jittermin = "";
	
	//측정IP(Client)
	@SerializedName("IP")
	public String ip = "";
	
	//전화번호
	@SerializedName("TEL_NO")
	public String tel_no= "";
	
	//운영체제코드
	@SerializedName("OS")
	public String os = "";
	
	//홉수 
	@SerializedName("HOPCOUNT")
	public String hopcount = "";
	
	//코넷 아이디
	@SerializedName("KORNETID")
	public String kornetid = "";
	
	//서비스 계약 아이디
	@SerializedName("SCN")
	public String scn = "";
	
	//평균다운로드속도
	@SerializedName("DOWNLOADSPEEDAVG")
	public String downloadspeedavg = "";
	
	//CPU종류
	@SerializedName("CPUTYPE")
	public String cputype = "";
	
	//GATEWAY
	@SerializedName("GATEWAY")
	public String gateway = "";
	
	//최대UDP지터지연시간
	@SerializedName("JITTERMAX")
	public String jittermax = "";
	
	//최대업로드속도
	@SerializedName("UPLOADSPEEDMAX")
	public String uploadspeedmax= "";
	
	//평균UDP지연시간
	@SerializedName("UDPDELAYTIMEAVG")
	public String udpdelaytimeavg = "";
	
	//최소업로드속도
	@SerializedName("UPLOADSPEEDMIN")
	public String uploadspeedmin = "";

	//작업자 아이디
	@SerializedName("WORKERID")
	public String workerid = "";
	
	//최소다운로드속도
	@SerializedName("DOWNLOADSPEEDMIN")
	public String downloadspeedmin = "";
	
	//오더번호
	@SerializedName("WORKODRNUM")
	public String workodrnum = "";
	
	//패킷지연시간편차(1ms*1000)
	@SerializedName("PACKDELAYTIMESTD")
	public String packdelaytimestd = "";
	
	//최대UDP지연시간
	@SerializedName("UDPDELAYTIMEMAX")
	public String udpdelaytimemax = "";
	
	//측정 품질(0:만족, 1:미달)
	@SerializedName("TESTQUALITY")
	public String testquality = "";
	
	//측정MAC(Client)
	@SerializedName("MAC")
	public String mac = "";
	
	//최소UDP지연시간
    @SerializedName("UDPDELAYTIMEMIN")
	public String udpdelaytimemin = "";
	
    //NIC종류
	@SerializedName("NIC")
	public String nic = "";
    
    //메모리사이즈
    @SerializedName("MEMORYSIZE")
	public String memorysize = "";
    
    //평균패킷지연시간
    @SerializedName("PACKDELAYTIMEAVG")
	public String packdelayimeavg = "";

	@Override
	public String toString() {
		return "InternetSpeedResultData [officescode=" + officescode
				+ ", testdate=" + testdate + ", serverinfo=" + serverinfo
				+ ", jitteravg=" + jitteravg + ", browser=" + browser
				+ ", packdelaytimemax=" + packdelaytimemax + ", workodrkind="
				+ workodrkind + ", packdelaytimemin=" + packdelaytimemin
				+ ", cpurate=" + cpurate + ", uploadspeedavg=" + uploadspeedavg
				+ ", downloadspeedmax=" + downloadspeedmax
				+ ", udpdelaytimestd=" + udpdelaytimestd
				+ ", optictestquality=" + optictestquality + ", packlossrate="
				+ packlossrate + ", memoryrate=" + memoryrate
				+ ", udplossrate=" + udplossrate + ", jitterstd=" + jitterstd
				+ ", jittermin=" + jittermin + ", ip=" + ip + ", tel_no="
				+ tel_no + ", OS=" + os + ", hopcount=" + hopcount
				+ ", kornetid=" + kornetid + ", scn=" + scn
				+ ", downloadspeedavg=" + downloadspeedavg + ", cputype="
				+ cputype + ", gateway=" + gateway + ", jittermax=" + jittermax
				+ ", uploadspeedmax=" + uploadspeedmax + ", udpdelaytimeavg="
				+ udpdelaytimeavg + ", uploadspeedmin=" + uploadspeedmin
				+ ", workerid=" + workerid + ", downloadspeedmin="
				+ downloadspeedmin + ", workodrnum=" + workodrnum
				+ ", packdelaytimestd=" + packdelaytimestd
				+ ", udpdelaytimemax=" + udpdelaytimemax + ", testquality="
				+ testquality + ", mac=" + mac + ", udpdelaytimemin="
				+ udpdelaytimemin + ", nic=" + nic + ", memorysize="
				+ memorysize + ", packdelayimeavg=" + packdelayimeavg + "]";
	}
	

	
	

	

	
	
	
	
	
	
	
	
	
}
