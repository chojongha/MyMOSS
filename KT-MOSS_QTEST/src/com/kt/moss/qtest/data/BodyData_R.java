package com.kt.moss.qtest.data;

import com.google.gson.annotations.SerializedName;

/**
 * 인터넷 수리
 * @author Administrator
 *
 */
public class BodyData_R {

	
	// 작업의뢰번호
	@SerializedName("WORKODRNUM")
	public String workOdrNum = "";
	
	// 수용국
	@SerializedName("OFFICENAME")
	public String officename = "";
	
	// 수용국 표준 코드
	@SerializedName("OFFICESCODE")
	public String officescode = "";

	// 고장접수번호
	@SerializedName("TTID")
	public String ttID = "";
	
	// 수리의뢰일련번호
	@SerializedName("REQSTSEQNUM")
	public String reqstSeqNum = "";
	
	// 상품종류
	@SerializedName("SVCTYPE")
	public String svctype = "";
	
	// 가설 개통일시
	@SerializedName("INSTALLDT")
	public String installDT = "";
	
	// 고객 접수시간
	@SerializedName("RECEIPTDT")
	public String receiptDT = "";
	
	// 방문 예정시간
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
	
	// 코넷아이디
	@SerializedName("KORNETID")
	public String kornetId = "";
	
	// 고장서비스/시설
	@SerializedName("SVCFACILITY")
	public String svcFacility = "";
		
	
	// 신고자 의견
	@SerializedName("REPORTEROPINION")
	public String reporterOpinion = "";
		
	// 수리의뢰자1(F/M)
	@SerializedName("REQSTNAME")
	public String reqstName = "";
	
	// 수리의뢰자1(F/M)
	@SerializedName("REQSTTELNUM")
	public String reqstTelNum = "";
		
	
	// 수리의뢰자 의견
	@SerializedName("REQSTOPINION")
	public String reqstOpinion = "";
	
	// 독촉성여부
	@SerializedName("URGENTYN")
	public String urgentYN = "";
	
	// 당일중복고장횟수
	@SerializedName("DAYREPEATREPORTCNT")
	public String dayRepeatReportCnt = "";
	
	// 당일중복고장횟수
	@SerializedName("PROCESSINGREPORTCNT")
	public String processIngreportCnt = "";
	
	// 30일이내고장여부
	@SerializedName("INSTALLAFTERMONTHYN")
	public String installAfterMonthYN = "";
	
	// 작업자명
	@SerializedName("WORKERNAME")
	public String workerName = "";
	
	// 포트정보
	@SerializedName("FACINFO")
	public String facInfo = "";
		
	// 포트정보2
	@SerializedName("FACDIV")
	public String facDiv = "";		
	
	// 심선번호
	@SerializedName("CABLENUM")
	public String cableNum = "";
	
	// LLU 사업자
	@SerializedName("LLUTELCO")
	public String lluTelCo = "";
	
	// LLU 사업자
	@SerializedName("LLUTELNUM")
	public String lluTelNum = "";
	
	// SAID
	@SerializedName("SCN")
	public String scn = "";
	
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
		return "BodyData_R [workOdrNum=" + workOdrNum + ", officename="
				+ officename + ", officescode=" + officescode + ", ttID="
				+ ttID + ", reqstSeqNum=" + reqstSeqNum + ", svctype="
				+ svctype + ", installDT=" + installDT + ", receiptDT="
				+ receiptDT + ", rsvDT=" + rsvDT + ", custName=" + custName
				+ ", telNum=" + telNum + ", contactTelNum1=" + contactTelNum1
				+ ", contactTelNum2=" + contactTelNum2 + ", address=" + address
				+ ", kornetId=" + kornetId + ", svcFacility=" + svcFacility
				+ ", reporterOpinion=" + reporterOpinion + ", reqstName="
				+ reqstName + ", reqstTelNum=" + reqstTelNum
				+ ", reqstOpinion=" + reqstOpinion + ", urgentYN=" + urgentYN
				+ ", dayRepeatReportCnt=" + dayRepeatReportCnt
				+ ", processIngreportCnt=" + processIngreportCnt
				+ ", installAfterMonthYN=" + installAfterMonthYN
				+ ", workerName=" + workerName + ", facInfo=" + facInfo
				+ ", facDiv=" + facDiv + ", cableNum=" + cableNum
				+ ", lluTelCo=" + lluTelCo + ", lluTelNum=" + lluTelNum
				+ ", scn=" + scn + ", authcompld=" + authcompld + ", FTTHFlag="
				+ FTTHFlag + ", authcompld_nm=" + authcompld_nm + ", sla_down="
				+ sla_down + ", sla_up=" + sla_up + ", sla_udp=" + sla_udp
				+ ", sla_ping=" + sla_ping + "]";
	}
	
	
	
	

	
	
	
	
	
	
}
