package com.kt.moss.qtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kt.moss.qtest.data.VcTestBodyData;
import com.kt.moss.qtest.data.VcTestData;
import com.kt.moss.qtest.util.MossDef;

public class VcTest extends Activity {

	private ArrayAdapter<CharSequence> type_adapter; // 주어진 리스트 객체의 내용과 리스트 항목의
														// 레이아웃을 연결시켜준다.
	private boolean mInitSpinner;
	private Spinner vc_test_type_spin;
	private String selectedTypeItem = MossDef.VCTEST_TYPE_NORMAL; // 스피너의 "일반"

	private EditText vc_test_duration;
	private TextView vc_test_state;

	SharedPreferences settings;
	private String user_id = "";

	private String workOdrNum = "";
	private int workOdrKind = 1;
	private String telNum = "";

	private String scn = "";
	private String officescode = "";
	private String kornetId = "";
	private String callee = "";

	private VcTestData data;

	private int duration = 0;

	private static TextView vc_testing_time;

	ThreadTime thread;
	static int checkHandler = 0;
	static long count = 0;

	private Button vctest_start_btn;
	private Button vctest_close_btn;

	private boolean isRunning = false;

	@Override
	// 부모클래스를 상속받는 자식클래스가 부모클래스가 갖고있던 메소드를 "재정의"하는것
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);

		setContentView(R.layout.vc_test);

		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.logo);

		settings = getSharedPreferences(MossDef.PREFS_NAME,
				Context.MODE_PRIVATE);
		user_id = settings.getString(MossDef.USER_ID, "");

		Intent intent = getIntent();

		workOdrNum = intent.getStringExtra("workOdrNum");
		telNum = intent.getStringExtra("telNum");
		scn = intent.getStringExtra("scn");
		officescode = intent.getStringExtra("officesCode");
		kornetId = intent.getStringExtra("kornetId");

		workOdrKind = MossDef.getWorkOrderKindInt(intent
				.getStringExtra("workOdrKind"));

		Init();

		vc_test_state.setText(Html.fromHtml("<font color='#424243'>"
				+ MossDef.VCTEST_STATE_READY + "</font>"));
	}

	private void Init() {

		vc_test_duration = (EditText) findViewById(R.id.vc_test_duration);
		vc_test_state = (TextView) findViewById(R.id.vc_test_state);

		vctest_start_btn = (Button) findViewById(R.id.vctest_start_btn);
		vctest_close_btn = (Button) findViewById(R.id.vctest_close_btn);

		vc_test_type_spin = (Spinner) findViewById(R.id.vc_test_type_spin);
		type_adapter = ArrayAdapter.createFromResource(this, R.array.test_type,
				R.layout.spinner_item_vctest);
		type_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		vc_test_type_spin.setAdapter(type_adapter);
		vc_test_type_spin
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {

						// TODO Auto-generated method stub
						if (mInitSpinner == false) {
							mInitSpinner = true;
							return;
						}

						// selectedTypeItem =
						// type_adapter.getItem(position).toString();

						if (position == 0) {
							selectedTypeItem = MossDef.VCTEST_TYPE_NORMAL;
						} else {
							selectedTypeItem = MossDef.VCTEST_TYPE_LOOPBACK;
						}

						vc_test_state.setText("");

					}

				});

		vc_testing_time = (TextView) findViewById(R.id.vc_testing_time);
		vc_testing_time.setText("");
		thread = new ThreadTime(mHandler);
		thread.start();

	}

	public void clickCloseBtn(View v) {

		if (isRunning) {
			Toast.makeText(VcTest.this, "VC Test 실행중입니다.", Toast.LENGTH_SHORT)
					.show();

		} else {
			thread = null;
			finish();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (isRunning) {
				Toast.makeText(VcTest.this, "VC Test 실행중입니다.",
						Toast.LENGTH_SHORT).show();

			} else {
				thread = null;
				finish();
			}

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void clickStartBtn(View v) {

		try {

			if (vc_test_duration.getText().toString().equals("")) {
				vc_test_duration.setText("10");
				duration = 10 * 1000;
				Toast.makeText(this, "기본10초로 측정됩니다.", Toast.LENGTH_SHORT)
						.show();

				// Toast.makeText(this, "측정시간을 입력하세요. 기본10초로 측정됩니다.",
				// Toast.LENGTH_SHORT).show();
				// return;
				// finish();

			} else {

				duration = Integer.parseInt(vc_test_duration.getText()
						.toString());

				if (duration <= 9 || duration > 101) {

					Toast.makeText(this, "측정시간은 10 ~ 100초 사이값을 입력하세요 ",
							Toast.LENGTH_SHORT).show();
					return;

				}

				duration = duration * 1000;

			}

			System.out.println("telnum :; " + telNum);
			System.out.println("callee :; "
					+ MossDef.makePhoneNumbercallee(telNum));
			System.out.println("scn :; " + scn);
			System.out.println("officescode :; " + officescode);
			System.out.println("kornetId :; " + kornetId);

			StringBuffer vcTestUrl = new StringBuffer();
			vcTestUrl.append(MossDef.VC_TEST_URL);
			vcTestUrl.append(workOdrNum);
			vcTestUrl.append("&workerID=");
			vcTestUrl.append(user_id);
			vcTestUrl.append("&workOdrKind=");
			vcTestUrl.append(workOdrKind);
			vcTestUrl.append("&telNum=");
			vcTestUrl.append(telNum);
			// vcTestUrl.append("07041528270");
			vcTestUrl.append("&type=");
			vcTestUrl.append(selectedTypeItem);
			vcTestUrl.append("&scn=");
			vcTestUrl.append(scn.trim());
			vcTestUrl.append("&officesCode=");
			vcTestUrl.append(officescode);
			vcTestUrl.append("&kornetId=");
			if (kornetId != null) {
				vcTestUrl.append(kornetId);
			}
			vcTestUrl.append("&callee=");
			vcTestUrl.append(MossDef.makePhoneNumbercallee(telNum));
			vcTestUrl.append("&duration=");
			vcTestUrl.append(vc_test_duration.getText().toString());

			System.out.println("VC 테스트 경로 :: " + vcTestUrl.toString());

			vc_testing_time.setText("");

			vc_test_state.setText(Html.fromHtml("<font color='#ff0000'>"
					+ MossDef.VCTEST_STATE_RUN + "</font>"));

			vc_test_duration = (EditText) findViewById(R.id.vc_test_duration);
			vc_test_state = (TextView) findViewById(R.id.vc_test_state);

			vctest_start_btn = (Button) findViewById(R.id.vctest_start_btn);
			vctest_close_btn = (Button) findViewById(R.id.vctest_close_btn);

			vc_test_type_spin = (Spinner) findViewById(R.id.vc_test_type_spin);

			setVisibilityWidget(false);

			thread.onStart();
			new TestRunAsyncTask().execute(vcTestUrl.toString());
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
		}

	}

	private void setVisibilityWidget(boolean visibility) {

		vc_test_duration.setEnabled(visibility);
		vc_test_type_spin.setEnabled(visibility);
		vctest_start_btn.setEnabled(visibility);
		vctest_close_btn.setEnabled(visibility);

	}

	private class TestRunAsyncTask extends AsyncTask<String, Void, Void> {

		private final HttpClient httpclient = new DefaultHttpClient();
		final HttpParams params = httpclient.getParams();
		private boolean error = false;

		protected Void doInBackground(String... urls) {

			isRunning = true;

			String URL = null;
			InputStream is = null;
			try {

				// URL passed to the AsyncTask
				URL = urls[0];
				HttpConnectionParams.setConnectionTimeout(params,
						MossDef.REGISTRATION_TIMEOUT);

				// ## 나중에 합치자...
				if (selectedTypeItem.equals(MossDef.VCTEST_TYPE_NORMAL)) {
					HttpConnectionParams.setSoTimeout(params,
							MossDef.WAIT_TIMEOUT + duration);

					ConnManagerParams.setTimeout(params, MossDef.WAIT_TIMEOUT
							+ duration);
				} else {

					HttpConnectionParams.setSoTimeout(params,
							MossDef.WAIT_TIMEOUT + duration * 2);

					ConnManagerParams.setTimeout(params, MossDef.WAIT_TIMEOUT
							+ duration * 2);
				}

				HttpPost httpPost = new HttpPost(URL);

				// Response from the Http Request
				HttpResponse response = httpclient.execute(httpPost);

				// Check the Http Request for success
				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

					HttpEntity httpEntity = response.getEntity();
					is = httpEntity.getContent();

					Gson gson = new Gson();

					Reader reader = new InputStreamReader(is);

					data = gson.fromJson(reader, VcTestData.class);

				} else {
					// Closes the connection.
					// Log.w(LOG_TAG, statusLine.getReasonPhrase());
					response.getEntity().getContent().close();

					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {
				// Log.w(LOG_TAG, e);
				error = true;
				cancel(true);
			}
			return null;
		}

		protected void onCancelled() {

			isRunning = false;

			thread.onStop();
			count = 0; // 시간값 초기화

			vc_test_state.setText(Html.fromHtml("<font color='#ff0000'>"
					+ MossDef.VCTEST_STATE_FAIL + "</font>"));
			vc_testing_time.setText("");
			Toast.makeText(VcTest.this, "VC테스트 결과 수신 실패", Toast.LENGTH_SHORT)
					.show();
			setVisibilityWidget(true);
		}

		protected void onPostExecute(Void unused) {
			isRunning = false;

			thread.onStop();
			count = 0; // 시간값 초기화
			// vc_testing_time.setText("");

			if (error) {
				setVisibilityWidget(true);
				vc_test_state.setText(Html.fromHtml("<font color='#ff0000'>"
						+ MossDef.VCTEST_STATE_FAIL + "</font>"));
				vc_testing_time.setText("");
				Toast.makeText(VcTest.this, "VC테스트 결과 수신 실패",
						Toast.LENGTH_SHORT).show();
			} else {

				vc_test_state.setText(Html.fromHtml("<font color='#0000ff'>"
						+ MossDef.VCTEST_STATE_FINISH + "</font>"));
				vc_testing_time.setText("");

				setVisibilityWidget(true);
				// 결과 도착
				resultData();
			}
		}
	}

	private void resultData() {
		// 측정완료
		vc_test_state.setText(MossDef.VCTEST_STATE_FINISH);

		if (data != null) {

			VcTestBodyData vcTestData = data.getBody().get(0);

			System.out.println("VC TEST RESULT :: " + vcTestData.toString());

			if (vcTestData.result.equals("0")) {

				// vc_test_state.setText(vcTestData.quality);
				vc_test_state.setText(Html.fromHtml("<font color='#0000ff'>"
						+ vcTestData.quality + "</font>"));

				System.out.println("vcTestData.result :: " + vcTestData.result);
				System.out.println("vcTestData.quality :: "
						+ vcTestData.quality);
				System.out.println("vcTestData.errMsg :: " + vcTestData.errMsg);
				System.out.println("vcTestData.errCause :: "
						+ vcTestData.errCause);

			} else {

				System.out.println("vcTestData.result :: " + vcTestData.result);
				System.out.println("vcTestData.quality :: "
						+ vcTestData.quality);
				System.out.println("vcTestData.errMsg :: " + vcTestData.errMsg);
				System.out.println("vcTestData.errCause :: "
						+ vcTestData.errCause);

				// vc_test_state.setText(Html.fromHtml("<font color='#ff0000'>"+vcTestData.errMsg+"</font>"));
				vc_test_state.setText(Html.fromHtml("<font color='#ff0000'>"
						+ MossDef.VCTEST_STATE_FAIL + "</font>"));

				// vc_test_state.setText(MossDef.VCTEST_STATE_FAIL);

				AlertDialog.Builder builder = new AlertDialog.Builder(
						VcTest.this);
				builder.setIcon(android.R.drawable.ic_dialog_alert);
				builder.setTitle("측정 실패");
				builder.setMessage(vcTestData.errMsg);
				builder.setNeutralButton(R.string.dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								return;
							}
						});
				builder.create().show();
			}

		} else {
			// vc_test_state.setText(MossDef.VCTEST_STATE_FAIL); (측정실패)
			vc_test_state.setText(Html.fromHtml("<font color='#ff0000'>"
					+ MossDef.VCTEST_STATE_FAIL + "</font>"));
		}

	}

	static Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			// Log.v("StopWatch", "Handler" + count);
			count++;
			long second = getSecond(count);
			vc_testing_time.setText(second + "초 ");

		}

	};

	public static long getSecond(long milli) {
		long secondValue = 0;
		secondValue = milli / 10;
		return secondValue;
	}

	class ThreadTime extends Thread {
		Handler mHandler;
		boolean sns = false; // Thread를 통제하기 위한 boolean 값

		public void run() {
			while (true) {
				if (sns) {
					// Log.v("StopWatch", "ThreadTime");
					mHandler.sendEmptyMessage(0);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		}

		// 생성자
		public ThreadTime(Handler handler) {
			mHandler = handler;
		}

		public void onStart() {
			sns = true;
		}

		public void onStop() {
			sns = false;
			vc_testing_time.setText("");
		}

	}

}
