package com.kt.moss.qtest.util;

import java.text.DecimalFormat;

import android.util.Log;

public class MossDef {	
	
	
	// 서버 주소 - 테스트 서버(노트북 서버)
	// public static final String SERVER_IP = "http://192.168.123.145:8080/moss/";
	//public static final String SERVER_IP = "http://10.217.76.186:8081/moss/";
	// 서버 주소 - 개인 서버  
	// public static final String SERVER_IP = "http://192.168.25.7:8080/moss/";
	// 서버 주소 - 테스트 서버(통합 테스트 서버)
	public static final String SERVER_IP = "http://moss.kt.com/moss/";
	
	// 로그인
	public static final String LOGIN_URL =  SERVER_IP + "OpticTestLogin?userID=";
	// 리스트
	public static final String LIST_INFO_URL =  SERVER_IP + "WorkOdrList?workerID=";
	// 상세정보
	public static final String DETAIL_INFO_URL =  SERVER_IP + "WorkOdrDetail?workOdrNum=";
	// VC 테스트
	public static final String VC_TEST_URL =  SERVER_IP + "SoipTest?workOdrNum=";
	// 광레벨 측정앱 주소
	public static final String OPTICAL_TEST_APP_PATH = "com.kt.moss.opticaltest";
	//속도측정,회선인증 경로 설정 14.02.14
	public static final String SPEED_TEST_APP_PATH = "com.kt.moss.speedtest";
	
	public static final String AUTHCOMPID_APP_PATH = "com.kt.moss.authcompid";
	
	
	public static final int REGISTRATION_TIMEOUT = 5000;
	public static final int WAIT_TIMEOUT = 15000;
		
	public static final String PREFS_NAME = "MOSSF_Preferences";	 
	public static final String USER_ID = "USER_ID";

	public static final String CONDITION_P = "P";
	public static final String CONDITION_C = "C";
	public static final String CONDITION_P_STR = "진행";
	public static final String CONDITION_C_STR = "완료";
		
	public static final String ORDERTYPE_I = "I";
	public static final String ORDERTYPE_P = "P";
	public static final String ORDERTYPE_T = "T";
	
	public static final String ORDERTYPE_I_STR = "인터넷";
	public static final String ORDERTYPE_P_STR = "인터넷전화";
	public static final String ORDERTYPE_T_STR = "TV";
		
	public static final String WORKORDERKIND_I = "I";
	public static final String WORKORDERKIND_R = "R";	

	public static final String WORKORDERKIND_I_STR = "[개통]";
	public static final String WORKORDERKIND_R_STR = "[수리]";
	
	public static final int WORKORDERKIND_I_CODE = 1;
	public static final int WORKORDERKIND_R_CODE = 2;
		
	public static final String VCTEST_STATE_READY = "측정대기";
	public static final String VCTEST_STATE_RUN = "측정중";
	public static final String VCTEST_STATE_FINISH = "측정완료";
	public static final String VCTEST_STATE_SUCCESS = "측정성공";
	public static final String VCTEST_STATE_FAIL = "측정실패";
	
	
	public static final String VCTEST_TYPE_NORMAL = "0";
	public static final String VCTEST_TYPE_LOOPBACK = "2";
	
	public static final String SEED_PASSWORD = "1234567890abcdef";
	 
	
	public static String getConditionStr(String code) {
		
		
		String result = "";
		
		if (code.equals(CONDITION_P)) {			
			result =  CONDITION_P_STR;
		}  else {
			result =  CONDITION_C_STR;
			
		}
					
		return result;
		
	}
	
	
	public static String getOrderTypeStr(String code) {
		
		
		String result = "";
		
		if (code.equals(ORDERTYPE_I)) {			
			result =  ORDERTYPE_I_STR;
		}  else if(code.equals(ORDERTYPE_P)){
			result =  ORDERTYPE_P_STR;			
		} else {
			result =  ORDERTYPE_T_STR;
		}
					
		return result;
		
	}
	
	
public static String getOrderTypeCode(String orderType) {
		
		
		String code = "";
		
		if (orderType.equals(ORDERTYPE_I_STR)) {			
			code =  ORDERTYPE_I;
		}  else if(orderType.equals(ORDERTYPE_P_STR)){
			code =  ORDERTYPE_P;			
		} else {
			code =  ORDERTYPE_T;
		}
					
		return code;
		
	}
	
	
	
	public static String getWorkOrderKindStr(String code) {
		
		
		String result = "";
		
		if (code.equals(WORKORDERKIND_I)) {			
			result =  WORKORDERKIND_I_STR;
		}  else {
			result =  WORKORDERKIND_R_STR;
			
		}
					
		return result;
		
	}
	
	
	public static String getWorkOrderKindCode(String workOrderKind) {
		
		
		String code = "";
		
		if (workOrderKind.equals(WORKORDERKIND_I_STR)) {			
			code =  WORKORDERKIND_I;
		}  else {
			code =  WORKORDERKIND_R;
			
		}
					
		return code;
		
	}
	
	
public static int getWorkOrderKindInt(String workOrderKind) {
		
		
		int code = 1;
		
		if (workOrderKind.equals(WORKORDERKIND_I_STR)) {			
			code =  WORKORDERKIND_I_CODE;
		}  else {
			code =  WORKORDERKIND_R_CODE;
			
		}
					
		return code;
		
	}
	
	
	 public static String getNullCheckString(String input) {
		  	
		  	String returnValue = "-1";
		  	
		  	if (input == null) {
				
		  		return "-1";
		  	
		  	} else if (input.equalsIgnoreCase("null") || input.equals("-")) {
		  	
		  		return "-1";
		  	
		  	} else {
			
		  		returnValue = input;
				
		  	}
		  	
		  	
		  	return returnValue;
		  	
		  }
	 
	 
	 public static String getNullCheckString2(String input) {
		  	
		  	String returnValue = "";
		  	
		  	if (input == null) {
				
		  		return "";
		  	
		  	} else if (input.equalsIgnoreCase("null") || input.equals("-")) {
		  	
		  		return "";
		  	
		  	} else {
			
		  		returnValue = input;
				
		  	}
		  	
		  	
		  	return returnValue;
		  	
		  }
	 
	 

	
	
	public static String makePhoneNumber(String inputValue) {
		
		String phoneNumber = "";		
		//System.out.println(" inputValue ::  " + inputValue);
				
		String	baseString  =  getNullCheckString(inputValue);
		
			
		if (!baseString.equals("-1")) {
			
			int length = baseString.length();
			
			// 12 자리라면

			   if (length == 12) {
				  
				  String startNum = baseString.substring(0, 4);
				  
				  String startNum_new = "";
				  
				  if (startNum.substring(0, 3).equals("000")) {
					  startNum_new = startNum.substring(2, 4);
				  } else {					  
					  startNum_new = startNum.substring(1, 4);
				  } 
				  
				  String middleNum = baseString.substring(4, 8);
				  String endNum = baseString.substring(8, 12);
				  
				  phoneNumber = startNum_new + "-"+ middleNum + "-" + endNum;
				  
			   } 
			   
			   
			   
			   // 11 자리
				  // 5400589416
				   
				   else if(length == 11){
					   
					   
							 
							  String startNum = baseString.substring(0, 3);
							   
							   String middleNum = baseString.substring(3, 7);
							   String endNum = baseString.substring(7);
								  
							   phoneNumber = startNum + "-"+ middleNum + "-" + endNum;
							  
			}
			   
			   
			   // 10 자리
			  // 5400589416
			   
			   else if(length == 10){
				   
				   if( baseString.substring(0, 2).equals("02")){
						 
						  String startNum = baseString.substring(0, 2);
						   
						   String middleNum = baseString.substring(2, 6);
						   String endNum = baseString.substring(6);
							  
						   phoneNumber = startNum + "-"+ middleNum + "-" + endNum;
						   }else{
						   String startNum = baseString.substring(0, 3);
						   
						   String middleNum = baseString.substring(3, 6);
						   String endNum = baseString.substring(6);
							  
						   phoneNumber = startNum + "-"+ middleNum + "-" + endNum;
		                                   }
		}

			   			   
			   else if(length == 9){
				   
				   
				   
				   if (baseString.substring(0, 1).equals("2")) {
					
					   String startNum = baseString.substring(0, 1);
					   String middleNum = baseString.substring(1, 5);
					   String endNum = baseString.substring(5, 9);
						  
					   phoneNumber = "0"+startNum + "-"+ middleNum + "-" + endNum;
					   
					   
				    } else {
				    	   String startNum = baseString.substring(0, 2);
						   String middleNum = baseString.substring(2, 5);
						   String endNum = baseString.substring(5, 9);
							  
						   phoneNumber = "0"+startNum + "-"+ middleNum + "-" + endNum;
				    	
				    }
				
				   
				   
				   
				   
			   }
		}
	 //  System.out.println("phoneNumber :: "  + phoneNumber);
		return phoneNumber;
		
	}
	
	

	public static String makePhoneNumberWidthoutHyphen(String inputValue) {
		
		String phoneNumber = "";		
		
		String	baseString  =  getNullCheckString(inputValue);
		
	
		
		if (!baseString.equals("-1")) {
			
			int length = baseString.length();
			
			// 12 자리라면
			if( length == 12){
					
						String startNum = baseString.substring(0, 4);
						String middleNum = baseString.substring(4, 8);
						String endNum  = baseString.substring(8);
		  
						startNum = startNum.replaceAll("000", "0").replaceAll("00", "0");
						if(middleNum.startsWith("0")){
							middleNum = middleNum.substring(1);
						}else{
						
						phoneNumber = startNum + middleNum + endNum;
						Log.i("phoneNumber?:", phoneNumber);
						}
}         
			   // 10 자리
			  // 5400589416
			   
			   else if(length == 10){
				   
				   String startNum = baseString.substring(0, 2);
				   String middleNum = baseString.substring(2, 6);
				   String endNum = baseString.substring(6, 10);
					  
				   phoneNumber = "0"+startNum + middleNum + endNum;
			   }
			   			   
			   else if(length == 9){
				   
				   String startNum = baseString.substring(0, 2);
				   String middleNum = baseString.substring(2, 5);
				   String endNum = baseString.substring(5, 9);
					  
				   phoneNumber = "0"+startNum + middleNum + endNum;
			   }
		}
	   
		return phoneNumber;
		
	}
	
	
	public static String toCommifyString(String sourceNum) {
		if (sourceNum != null) {
		if (sourceNum.isEmpty())
			return "0";
		}
		if (sourceNum.length() <= 3)
			return sourceNum;

		double num = Double.parseDouble(sourceNum);
		DecimalFormat df = new DecimalFormat("#,###");

		return df.format(num).toString();
	}
	public static String makePhoneNumbercallee(String inputValue) {
		
		String phoneNumber = "";		
		//System.out.println(" inputValue ::  " + inputValue);
				
		String	baseString  =  getNullCheckString(inputValue);
		
			
		if (!baseString.equals("-1")) {
			
			int length = baseString.length();
			
			// 12 자리라면

			   if (length == 12) {
				  
				  String startNum = baseString.substring(0, 4);
				  
				  String startNum_new = "";
				  
				  if (startNum.substring(0, 3).equals("000")) {
					  startNum_new = startNum.substring(2, 4);
				  } else {					  
					  startNum_new = startNum.substring(1, 4);
				  } 
				  
				  String middleNum = baseString.substring(4, 8);
				  String endNum = baseString.substring(8, 12);
				  
				  phoneNumber = startNum_new + middleNum + endNum;
				  
			   } 
			   
			   
			   
			   // 11 자리
				  // 5400589416
				   
				   else if(length == 11){
					   
					   
							 
							  String startNum = baseString.substring(0, 3);
							   
							   String middleNum = baseString.substring(3, 7);
							   String endNum = baseString.substring(7);
								  
							   phoneNumber = startNum + middleNum + endNum;
							  
			}
			   
			   
			   // 10 자리
			  // 5400589416
			   
			   else if(length == 10){
				   
				   if( baseString.substring(0, 2).equals("02")){
						 
						  String startNum = baseString.substring(0, 2);
						   
						   String middleNum = baseString.substring(2, 6);
						   String endNum = baseString.substring(6);
							  
						   phoneNumber = startNum + "-"+ middleNum + "-" + endNum;
						   }else{
						   String startNum = baseString.substring(0, 3);
						   
						   String middleNum = baseString.substring(3, 6);
						   String endNum = baseString.substring(6);
							  
						   phoneNumber = startNum + middleNum + endNum;
		                                   }
		}

			   			   
			   else if(length == 9){
				   
				   
				   
				   if (baseString.substring(0, 1).equals("2")) {
					
					   String startNum = baseString.substring(0, 1);
					   String middleNum = baseString.substring(1, 5);
					   String endNum = baseString.substring(5, 9);
						  
					   phoneNumber = "0"+startNum + "-"+ middleNum + "-" + endNum;
					   
					   
				    } else {
				    	   String startNum = baseString.substring(0, 2);
						   String middleNum = baseString.substring(2, 5);
						   String endNum = baseString.substring(5, 9);
							  
						   phoneNumber = "0"+startNum + middleNum + endNum;
				    	
				    }
				
				   
				   
				   
				   
			   }
		}
	 //  System.out.println("phoneNumber :: "  + phoneNumber);
		return phoneNumber;
		
	}
}