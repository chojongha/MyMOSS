package com.kt.moss;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.apache.cordova.DroidGap;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.DownloadListener;
import android.widget.Toast;

public class MainActivity extends DroidGap {
	
	private DownloadManager downloadManager;
	private static Intent fileIntent;
	private static File file;
	static final String TAG = "MainActivity";
	private String fileName = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        // SharedPreferences로 전달 받은 디바이스 아이디를 웹뷰에 GetParameter로 전달한다.
		super.loadUrl("file:///android_asset/www/client/login.html?getRegId="+ getPreferences());
		
		Log.i(TAG, "============================ get Registration id ==============================");
		Log.i(TAG, getPreferences());
		Log.i(TAG, "===============================================================================");
		
		downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE); 
		// 웹뷰 파일 다운로드 시 파일다운로더와 인텐트를 이용한 파일 처리
		super.appView.setDownloadListener(new DownloadListener() {
			
			@Override
		    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
				
				Log.d("MAIL", "=============== onDownloadStart() ===============");
				Log.d("MAIL", "url : " + url);
				Log.d("MAIL", "userAgent : " + userAgent);
				Log.d("MAIL", "contentDisposition : " + contentDisposition);
				Log.d("MAIL", "mimeType : " + mimeType);
				Log.d("MAIL", "contentLength : " + contentLength);
				 
				Uri uri = Uri.parse(url);
				fileName = contentDisposition.substring(contentDisposition.lastIndexOf("=") + 1, contentDisposition.length());
				
				// 한글파일명 처리
				try {
					fileName = new String(fileName.getBytes("8859_1"), "euc-kr");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
		        
				Request downloadRequest = new DownloadManager.Request(uri);
				downloadRequest = new DownloadManager.Request(uri);
				downloadRequest.setMimeType(mimeType);
				downloadRequest.setTitle("Downloader");
				downloadRequest.setDescription("Downloding...");
				downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
				downloadRequest.addRequestHeader("Connection", "close");
				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
				downloadManager.enqueue(downloadRequest);
		    }
		});
	}
	
	@Override
	protected void onResume() {
		//	앱이 실행되면 리시버 등록
		IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(completeReceiver, completeFilter);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//	앱이 중단 되면 리시버 등록 해제
		unregisterReceiver(completeReceiver);
	}
	
	//	다운로드 완료 리시버
	private BroadcastReceiver completeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 다운로드 완료 토스트 출력
			Toast.makeText(context, "다운로드 완료", Toast.LENGTH_SHORT).show();
			//	다운로드 완료 후 파일의 타입에 따른 파일뷰어 호출
			file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName);
			//	다운로드된 파일의 유효성 체크 (확장자가 없는 파일 예외처리)
			if ( file.getAbsolutePath().lastIndexOf(".") >= 0 ) {
				String fileExtend = getExtension(file.getAbsolutePath());
				
				Log.i(TAG, Environment.DIRECTORY_DOWNLOADS);
				Log.i(TAG, file.getAbsolutePath());
				
				fileIntent = new Intent();
				//	파일의 확장자를 확인하여 해당 파일형식을 지원하는 뷰어를 호출한다.
				getMimeType(fileExtend);
				startActivity(fileIntent);
			} else {
				//	파일의 형식이 잘못되었을 경우 경고 발생
				showDownloadAlert();
			}
		}
	};
	
	//	확장자 추출
	public static String getExtension(String fileStr) {
		return fileStr.substring(fileStr.lastIndexOf(".") + 1, fileStr.length());
	}
	//	파일형식에 따른 mineType 설정
	public static String getMimeType(String fileExtend) {
		// 파일 확장자 별로 mime type 지정해 준다.
		if (fileExtend.equalsIgnoreCase("jpg") || fileExtend.equalsIgnoreCase("jpeg") 
			|| fileExtend.equalsIgnoreCase("gif") || fileExtend.equalsIgnoreCase("png") 
			|| fileExtend.equalsIgnoreCase("bmp")) {
			fileIntent.setDataAndType(Uri.fromFile(file), "image/*");
		} 
		else if (fileExtend.equalsIgnoreCase("txt")) {
			fileIntent.setDataAndType(Uri.fromFile(file), "text/*");
		} 
		else if (fileExtend.equalsIgnoreCase("doc") || fileExtend.equalsIgnoreCase("docx")) {
			fileIntent.setDataAndType(Uri.fromFile(file), "application/msword");
		} 
		else if (fileExtend.equalsIgnoreCase("xls") || fileExtend.equalsIgnoreCase("xlsx")) {
			fileIntent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
		} 
		else if (fileExtend.equalsIgnoreCase("ppt") || fileExtend.equalsIgnoreCase("pptx")) {
			fileIntent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-powerpoint");
		} 
		else if (fileExtend.equalsIgnoreCase("pdf")) {
			fileIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
		} 
		else if (fileExtend.equalsIgnoreCase("hwp")) {
			fileIntent.setDataAndType(Uri.fromFile(file), "application/haansofthwp");
		}				
		return fileExtend;
	}
	
	 // 값 불러오기
    private String getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString("regId", "");
    }
    
    private void showDownloadAlert() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
	       
        dialogBuilder.setTitle("다운로드");
        dialogBuilder.setMessage("파일의 형식이 잘못되었습니다.");
        dialogBuilder.setPositiveButton("확인", null);
        dialogBuilder.show();
    }
}
