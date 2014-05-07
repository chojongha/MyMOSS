package com.kt.moss.qtest.data;

import com.google.gson.annotations.SerializedName;

/**
 * 인터넷 개통 바디
 * @author Administrator
 *
 */
public class BodyData_I {

	// 작업의뢰번호
	@SerializedName("WORKODRNUM")
	public String workOdrNum = "";
	
	// 수용국
	@SerializedName("OFFICENAME")
	public String officename = "";
	
	// 수용국 표준코드
	@SerializedName("OFFICESCODE")
	public String officescode = "";
		
	// 상품종류
	@SerializedName("SVCTYPE")
	public String svctype = "";
	
	// 가설접수시간
	@SerializedName("RECEIPTDT")
	public String receiptDT = "";
	
	// 고객희망시간
	@SerializedName("CONNHOPEDT")
	public String connHopeDT = "";
	
	// 방문예정시간
	@SerializedName("RSVDT")
	public String rsvDT = "";
	
	// 고객명
	@SerializedName("CUSTNAME")
	public String custName = "";
	
	// 전화번호
	@SerializedName("TELNUM")
	public String telNum = "";
	
	// 휴대전화
	@SerializedName("CONTACTTELNUM1")
	public String contactTelNum1 = "";
	
	// 휴대전화
	@SerializedName("CONTACTTELNUM2")
	public String contactTelNum2= "";
		
		
	// 주소
	@SerializedName("ADDRESS")
	public String address = "";
	
	// ADSL/FLC 포트정보
	@SerializedName("ADSLFLCTIEPORT")
	public String adslflcTiePort = "";
	
	// ADSL 타이번호
	@SerializedName("ADSLTIE")
	public String adslTie = "";
	
	// FLC 타이번호
	@SerializedName("FLCPORTALIAS")
	public String flcPortAlias = "";
	
	// INET 타이번호
	@SerializedName("INETTIE")
	public String inetTie = "";
	
	// LC 타이번호
	@SerializedName("LCTIE")
	public String lcTie = "";
	
	// 심선번호
	@SerializedName("CABLENUM")
	public String cableNum = "";
	
	// MDF LEN
	@SerializedName("LEN")
	public String len = "";	
	
	// 가설단자함 위치정보
	@SerializedName("TERMBOXLOC")
	public String termBoxLoc = "";
		
	// LLU 사업자
	@SerializedName("LLUBUSICODE")
	public String lluBusiCode = "";
	
	// LLU KT 전화번호
	@SerializedName("LLUTELNUM")
	public String lluTelnum = "";
		
	// 코넷아이디
	@SerializedName("KORNETID")
	public String kornetId = "";
	
	// ADSL 모뎀
	@SerializedName("ADSLTERM")
	public String adslTerm = "";
	
	// SAID
	@SerializedName("SCN")
	public String scn = "";
	
	// 고객요구사항
	@SerializedName("REMARK")
	public String remark = "";
	
	//회선인증여부(Y/N/null)
	@SerializedName("AUTHCOMPLD")
	public String authcompld = "";
	
	 //FTTH-FLAG
	@SerializedName("FTTHFLAG")
	public String FTTHFlag = "";
		
	//AUTHCOMPLD_NM
    @SerializedName("AUTHCOMPLD_NM")
	public String authcompld_nm = "";
    
    //다운로드 속도 품질 기준(단위:Kbps, 예:2000 Kbps 이상)
    @SerializedName("SLA_DOWN")
	public String sla_down = "";
    
  //업로드 속도 품질 기준(단위:Kbps, 예: 0 Kbps 이상)
    @SerializedName("SLA_UP")
	public String sla_up = "";
    
  //udp 품질 기준(단위:ms, 예:0 ms 이하)
    @SerializedName("SLA_UDP")
	public String sla_udp = "";
    
  //ping 품질 기준(단위:ms, 예:0 ms 이하)
    @SerializedName("SLA_PING")
	public String sla_ping = "";
    
 
    
    
    @Override
	public String toString() {
		return "InternetBodyData_I [WORKODRNUM=" + workOdrNum + ", OFFICENAME="
				+ officename + ", OFFICESCODE="+ officescode + ", SVCTYPE=" + svctype + ", RECEIPTDT="
				+ receiptDT + ", CONNHOPEDT=" + connHopeDT + ", RSVDT=" + rsvDT
				+ ", CUSTNAME=" + custName + ", TELNUM=" + telNum
				+ ", CONTACTTELNUM1=" + contactTelNum1 + ", CONTACTTELNUM2="
				+ contactTelNum2 + ", ADDRESS=" + address + ", ADSLFLCTIEPORT="
				+ adslflcTiePort + ", ADSLTIE=" + adslTie + ", FLCPORTALIAS="
				+ flcPortAlias + ", INETTIE=" + inetTie + ", LCTIE=" + lcTie
				+ ", CABLENUM=" + cableNum + ", LEN=" + len + ", TERMBOXLOC="
				+ termBoxLoc + ", LLUBUSICODE=" + lluBusiCode + ", LLUTELNUM="
				+ lluTelnum + ", KORNETID=" + kornetId + ", ADSLTERM="
				+ adslTerm + ", SCN=" + scn + ", REMARK=" + remark + ",AUTHCOMPLD="
				+ authcompld +",FTTHFLAG="+FTTHFlag+",AUTHCOMPLD_NM= "+authcompld_nm+", sla_down ="
				+ sla_down + ", sla_up ="+ sla_up + ", sla_udp ="+ sla_udp +", sla_ping ="
				+ sla_ping + "]";
	}
		
	
	
	
	
	
	
	
	
	
	
	
	
}
