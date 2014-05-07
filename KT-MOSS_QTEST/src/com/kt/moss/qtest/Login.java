package com.kt.moss.qtest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kt.moss.qtest.data.LoginBodyData;
import com.kt.moss.qtest.data.LoginData;
import com.kt.moss.qtest.util.AES256Cipher;
import com.kt.moss.qtest.util.MossDef;




public class Login extends Activity {


	private InputMethodManager imm;
	private EditText id_et;
	private EditText password_et;
	private SharedPreferences sharedPreference;


	LoginData loginData = null;

	private Button login_btn;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);




		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.login);



		imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);


		sharedPreference = getSharedPreferences(MossDef.PREFS_NAME, Context.MODE_PRIVATE);

		Init();



		String userID =	sharedPreference.getString(MossDef.USER_ID, "");

		if (!userID.equals("")) {

			id_et.setText(userID);
		}


	}

	private void saveUserID() {

		Editor editor = sharedPreference.edit();

		String idValue = 	id_et.getText().toString();

		if (!idValue.equals("")) {
			editor.putString(MossDef.USER_ID, idValue);
		}

		editor.commit();


	}



	/**
	 * 초기화...
	 */
	private void Init() {

		id_et = (EditText) findViewById(R.id.id_et);
		password_et = (EditText) findViewById(R.id.password_et);	
		login_btn  = (Button) findViewById(R.id.login_btn);

		login_btn.setEnabled(true); //사용가능 여부
	}



	@Override
	public void onResume() {

		super.onResume();					
		//키보드 감추기
		imm.hideSoftInputFromWindow(id_et.getWindowToken(), 0);			

	}


	/**
	 * 로그인 버튼 이벤트
	 * 
	 * @param v
	 */
	@SuppressWarnings("deprecation")
	public void clickLoginBtn(View v) {
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

		// 1. 키보드 닫기

		// 2. 아이디 체크

		// 3. 비밀번호 체크


		// 4. 네트워크 상태확인
		// 4.1 ok => 다음화면
		// 4.2 fail => 에러처리


		String id = id_et.getText().toString();
		String password = password_et.getText().toString();

		// 사용자 정보 에러

		if(id.equals("")){
			Toast.makeText(this, R.string.login_id_missingvalue, Toast.LENGTH_SHORT).show();
			return;
		}


		if(password.equals("")){
			Toast.makeText(this, R.string.login_password_missingvalue, Toast.LENGTH_SHORT).show();
			return;
		}


		// 네트워크 연결 에러	

		if (checkNetworkInfo()) {

			StringBuffer sbUrl = new StringBuffer();
			sbUrl.append(MossDef.LOGIN_URL);
			sbUrl.append(id);
			sbUrl.append("&userPW=");

			String encodeText = null;

			try {
				encodeText = AES256Cipher.AES_Encode(password, MossDef.SEED_PASSWORD);
			} catch (Exception e) {
				e.printStackTrace();
			}

			sbUrl.append(URLEncoder.encode(encodeText));




            //로그인 인증
			login_btn.setEnabled(false);
						
			new LoginAsyncTask().execute(sbUrl.toString());



            //로그인 미인증
//			saveUserID();
//
//			Intent in = new Intent(Login.this, OrderList.class);            
//			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			//스택에 기존에 사용하던 Activity가 있다면 그 위의 스택을 전부 제거해 주고 호출한다.
//			startActivity(in); 
//			finish();



		}

	}



	private class LoginAsyncTask extends AsyncTask<String, Void, Void> {



		private final HttpClient httpclient = new DefaultHttpClient();
		final HttpParams params = httpclient.getParams();
		private boolean error = false;
		private InputStream is = null;
		//InoutStream:바이트단위로 외부클래스를 읽어드림
		protected Void doInBackground(String... urls) {

			String URL = null;
			try {
				// URL passed to the AsyncTask
				URL = urls[0];
				HttpConnectionParams.setConnectionTimeout(params,MossDef.REGISTRATION_TIMEOUT);

				HttpConnectionParams.setSoTimeout(params, MossDef.WAIT_TIMEOUT);

				ConnManagerParams.setTimeout(params, MossDef.WAIT_TIMEOUT);


				HttpPost httpPost = new HttpPost(URL);
				// Response from the Http Request
				HttpResponse response = httpclient.execute(httpPost);

				// Check the Http Request for success
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {


					HttpEntity httpEntity = response.getEntity();
					is = httpEntity.getContent();									

					Gson gson = new Gson(); 
					//gson java object를 json표현식으로 변환하는 api
					Reader reader = new InputStreamReader(is); 
					//문자기반으로 읽어들이는 기능
					loginData = gson.fromJson(reader, LoginData.class); 					
				} else {
					// Closes the connection.
					//	Log.w(LOG_TAG, statusLine.getReasonPhrase());
					response.getEntity().getContent().close();
					Toast.makeText(Login.this, "로그인 실패 " + statusLine.getStatusCode(), Toast.LENGTH_SHORT).show();

					//throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {
				Log.w("LoginAsyncTask", e);
				error = true;
				cancel(true);
			}
			return null;
		}

		protected void onCancelled() {
			login_btn.setEnabled(true);

			//	Log.e(LOG_TAG, "login fail onCancelled");
			//	progressLayout.setVisibility(LinearLayout.GONE);
			Toast.makeText(Login.this, "로그인 실패 : 서버 연결 실패", Toast.LENGTH_SHORT).show();
		}

		protected void onPostExecute(Void unused) {


			login_btn.setEnabled(true);

			if (error) {
				//Log.e(LOG_TAG, "login fail onPostExecute");
				//	progressLayout.setVisibility(LinearLayout.GONE);
				Toast.makeText(Login.this, "로그인 실패", Toast.LENGTH_SHORT).show();
			} else {				
				loginResult();
			}
		}
	}


	private void loginResult() {



		String authResult =	loginData.getHeader().result;
		String errMsg =	loginData.getHeader().errMsg;

		if ("0".equals(authResult)) {


			saveUserID();


			Intent in = new Intent(Login.this, OrderList.class);            

			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(in); 

			finish();


		} else {


			AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("로그인 실패");
			builder.setMessage(errMsg);
			builder.setNeutralButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {


					return;
				}
			});
			builder.create().show();
		}


	}






	private boolean checkNetworkInfo() { // network 연결 상태 확인


		boolean isEnable = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isWifiAvail = ni.isAvailable();
		boolean isWifiConn = ni.isConnected();
		ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobileAvail = ni.isAvailable();
		boolean isMobileConn = ni.isConnected();


		if (!isWifiConn && !isMobileConn) {           

			isEnable = false;
			alertShow("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.\n네트워크 확인후 다시 접속해 주세요!");        


		}  else {

			isEnable = true;
		}

		return isEnable;

	}

	private void alertShow(String msg) {
		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(this);
		alert_internet_status.setTitle("네트워크 연결 오류");
		alert_internet_status.setMessage(msg);
		alert_internet_status.setPositiveButton("닫기",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); // 닫기
				return;
			}
		});
		alert_internet_status.show();

	}	 




	@Override
	protected void onDestroy() {

		super.onDestroy();
	}



}