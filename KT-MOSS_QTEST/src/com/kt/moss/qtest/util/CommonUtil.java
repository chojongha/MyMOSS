package com.kt.moss.qtest.util;

public class CommonUtil {

	public static String nullCheck(String data){
//		System.out.println("DATA :::"+ data);
		
		if(data == null || "null".equalsIgnoreCase(data) || data.isEmpty()){
			return "";
		}
		
		return data;
	}
	
	public static String measured(String data){
		
		double d = Double.parseDouble(data);
		if(d >= 1000){
		              d = d/1000;
			          String num = String.format("%.2f", d);
			          
			return (num + " Mbps");
		}
	return (data + "Kbps");	
	}

public static String changeint2000(String data1){
	String a1;
	int d1 = Integer.parseInt(data1);
	
	
	if(d1 >= 2000){
		 a1 = "품질만족";
	}else{
	     a1 = "품질미달";
	}
	
    return a1;	
}
public static String changeint0(String data1){
	String a1;
	int d1 = Integer.parseInt(data1);
	
	
	if(d1 >= 0){
		 a1 = "품질만족";
	}else{
	     a1 = "품질미달";
	}
	
    return a1;	
}

}