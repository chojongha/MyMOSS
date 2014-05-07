package com.kt.moss.qtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

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
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kt.moss.qtest.data.BodyData_R;
import com.kt.moss.qtest.data.InternetData_R;
import com.kt.moss.qtest.data.InternetResultData;
import com.kt.moss.qtest.data.InternetSpeedResultData;
import com.kt.moss.qtest.speedtest.LineTest;
import com.kt.moss.qtest.speedtest.SpeedTest;
import com.kt.moss.qtest.util.CommonUtil;
import com.kt.moss.qtest.util.MossDef;



/**
 * 인터넷 수리 (진행 및 완료) 정보
 * 
 * @author jhkim
 *
 */
public class OrderDetailInternet_R extends Activity implements OnCheckedChangeListener{



	private RadioGroup group_grp;


	private LinearLayout p_layout;
	private LinearLayout c_layout;

	private int checkedItem = 0;

	private LinearLayout summaryInfo_layout;
	private LinearLayout customerInfo_layout;
	private LinearLayout fmInfo_layout;
	private LinearLayout testInfo_layout;
    private LinearLayout sheenlevel_layout;
	private LinearLayout speedresult_layout;
	
	private String workOdrNum = "";
	private String workOdrKind = "";
	private String odrType = "";
	private String condition = "";
	private String custName = "";
	private String telNum = "";
	private String telNum_original = "";

	private InternetData_R	data;



	private TextView r_summary_01_tv; // 작업의뢰번호
	private TextView r_summary_02_tv; // 진행상태
	private TextView r_summary_03_tv; // 수용국
	private TextView r_summary_04_tv; // 오더내역
	private TextView r_summary_05_tv; // 상품종류
	private TextView r_summary_06_tv; // 고장접수번호
	private TextView r_summary_07_tv; // 수리의뢰일련번호
	private TextView r_summary_08_tv; // 가설 개통일시
	private TextView r_summary_09_tv; // 고객 접수시간
	private TextView r_summary_10_tv; // 방문 예정시간
	private TextView r_summary_11_tv; // 고객명
	private TextView r_summary_12_tv; // 전화번호
	private TextView r_summary_13_tv; // 주소

	private TextView r_customer_01_tv; // 고객명
	private TextView r_customer_02_tv; // 코넷아이디
	private TextView r_customer_03_tv; // 전화번호
	private TextView r_customer_04_tv; // 전화번호
	private TextView r_customer_05_tv; // 전화번호
	private TextView r_customer_06_tv; // 주소
	private TextView r_customer_07_tv; // 고장서비스/시설
	private TextView r_customer_08_tv; // 고객의견
	private TextView r_customer_09_tv; // 수리의뢰자(F/M) 1
	private TextView r_customer_10_tv; // 수리의뢰자(F/M) 2
	private TextView r_customer_11_tv; // 수리의뢰자 의견
	private TextView r_customer_12_tv; // 독촉성 여부
	private TextView r_customer_13_tv; // 당일 중복고장 횟수
	private TextView r_customer_14_tv; // 처리중 신고횟수
	private TextView r_customer_15_tv; // 30일 이내 고장여부
	private TextView r_customer_16_tv; // 작업자

	private TextView r_facilities_01_tv; // 포트정보
	private TextView r_facilities_02_tv; // 포트정보
	private TextView r_facilities_03_tv; // 심선번호
	private TextView r_facilities_04_tv; // LLU 사업자
	private TextView r_facilities_05_tv; // LLU KT 전화번호


	private TextView r_test_01_tv; // 측정일시
	private TextView r_test_02_tv; // 파장
	private TextView r_test_03_tv; // 파워
	private TextView r_test_04_tv; // Reference
	private TextView r_test_05_tv; // 회선인증결과
	private TextView r_test_06_tv; // FTTH-Real
	private TextView r_test_07_tv; // 다운로드(최대)
	private TextView r_test_07_1_tv; // 다운로드(평균)
	private TextView r_test_07_2_tv; // 다운로드(최소)
	private TextView r_test_08_tv; // 업로드(최대)
	private TextView r_test_08_1_tv; // 업로드(평균)
	private TextView r_test_08_2_tv; // 업로드(최소)
	private TextView r_test_09_tv; // ping 지연(최소)
	private TextView r_test_10_tv; // ping 지연(표준편차)
	private TextView r_test_11_tv; // ping 지연(최대)
	private TextView r_test_12_tv; // 손실율
	private TextView r_test_13_tv; // ping 지연(평균)
	private TextView r_test_14_tv; // ping 지연(최소)
	private TextView r_test_15_tv; // ping 지연(표준편차)
	private TextView r_test_16_tv; // ping 지연(최대)
	private TextView r_test_17_tv; // 손실율
	private TextView r_test_18_tv; // ping 지연(평균)
	private TextView r_test_19_tv; // CPU부하율
	private TextView r_test_20_tv; // 메모리점유율
	private TextView r_test_21_tv; // 사용자 ip
	private TextView r_test_22_tv; // Momory
	private TextView r_test_23_tv; // 운영체제
	private TextView r_test_24_tv; // 브라우저
	private TextView r_test_25_tv; // CPU
	private TextView r_test_31_tv; // 다운로드 속도 품질 기준(2000 kbps 이상)
	private TextView r_test_32_tv; // 업로드 속도 품질 기준(0kbps 이상
	private TextView r_test_33_tv; // udp 품질 기준(0 ms 이하)
	private TextView r_test_34_tv; // ping 품질 (0 ms 이하)

	ProgressDialog  mProgressDialog  = null;
	private StringBuffer detailUrl = null;
	private ArrayList<String> arraylist = null;
	private ArrayList<String> arraylist_speed = null;
	private ArrayAdapter<String> adapter = null;
	private ArrayAdapter<String> adapter_speed =null;
	private Spinner sp;
	private Spinner sp_speed;
	SharedPreferences settings;     
	private String user_id = "";

	// http://192.168.0.9:8080/moss/WorkOdrDetail?workOdrNum=1O2011090512020&workOdrKind=I&odrType=I
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);


		// 화면 구성하기
		setContentView(R.layout.order_detail_internet_r);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,	R.layout.custom_detail_title_bar);
		TextView title = (TextView) findViewById(R.id.title_name);
		title.setText("인터넷");

		TextView title_type = (TextView) findViewById(R.id.title_type);
		title_type.setText("수리");





		Intent intent = getIntent();

		workOdrNum = intent.getStringExtra("workOdrNum");
		workOdrKind = intent.getStringExtra("workOdrKind");
		odrType = intent.getStringExtra("odrType");
		condition = intent.getStringExtra("condition");
		custName = intent.getStringExtra("custName");
		telNum = intent.getStringExtra("telNum");
		settings = getSharedPreferences(MossDef.PREFS_NAME, Context.MODE_PRIVATE);   		
		user_id = settings.getString(MossDef.USER_ID, ""); 
		telNum_original = intent.getStringExtra("telNum_original");
	}

	@Override
	public void onResume() {

		super.onResume();


		Init();

		detailUrl = new StringBuffer();

		detailUrl.append(MossDef.DETAIL_INFO_URL);
		detailUrl.append(workOdrNum);
		detailUrl.append("&workOdrKind=");
		detailUrl.append(MossDef.getWorkOrderKindCode(workOdrKind));
		detailUrl.append("&odrType=");
		detailUrl.append(MossDef.getOrderTypeCode(odrType));
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();						
		}
		mProgressDialog = ProgressDialog.show(OrderDetailInternet_R.this,  "", "인터넷\n상세정보 조회중입니다.");
		new DetailAsyncTask().execute(detailUrl.toString());		
	}





	private void Init() {


		p_layout = (LinearLayout)findViewById(R.id.p_layout);
		c_layout = (LinearLayout)findViewById(R.id.c_layout);

		summaryInfo_layout = (LinearLayout)findViewById(R.id.summaryInfo_layout);
		customerInfo_layout = (LinearLayout)findViewById(R.id.customerInfo_layout);
		fmInfo_layout = (LinearLayout)findViewById(R.id.fmInfo_layout);	
		testInfo_layout = (LinearLayout)findViewById(R.id.testInfo_layout);	

		r_summary_01_tv  = (TextView)findViewById(R.id.r_summary_01_tv);	
		r_summary_02_tv  = (TextView)findViewById(R.id.r_summary_02_tv);	
		r_summary_03_tv  = (TextView)findViewById(R.id.r_summary_03_tv);	
		r_summary_04_tv  = (TextView)findViewById(R.id.r_summary_04_tv);	
		r_summary_05_tv  = (TextView)findViewById(R.id.r_summary_05_tv);	
		r_summary_06_tv  = (TextView)findViewById(R.id.r_summary_06_tv);	
		r_summary_07_tv  = (TextView)findViewById(R.id.r_summary_07_tv);	
		r_summary_08_tv  = (TextView)findViewById(R.id.r_summary_08_tv);	
		r_summary_09_tv  = (TextView)findViewById(R.id.r_summary_09_tv);	
		r_summary_10_tv  = (TextView)findViewById(R.id.r_summary_10_tv);	
		r_summary_11_tv  = (TextView)findViewById(R.id.r_summary_11_tv);	
		r_summary_12_tv  = (TextView)findViewById(R.id.r_summary_12_tv);
		r_summary_13_tv  = (TextView)findViewById(R.id.r_summary_13_tv);

		r_customer_01_tv  = (TextView)findViewById(R.id.r_customer_01_tv);	
		r_customer_02_tv  = (TextView)findViewById(R.id.r_customer_02_tv);
		r_customer_03_tv  = (TextView)findViewById(R.id.r_customer_03_tv);
		r_customer_04_tv  = (TextView)findViewById(R.id.r_customer_04_tv);
		r_customer_05_tv  = (TextView)findViewById(R.id.r_customer_05_tv);
		r_customer_06_tv  = (TextView)findViewById(R.id.r_customer_06_tv);
		r_customer_07_tv  = (TextView)findViewById(R.id.r_customer_07_tv);
		r_customer_08_tv  = (TextView)findViewById(R.id.r_customer_08_tv);
		r_customer_09_tv  = (TextView)findViewById(R.id.r_customer_09_tv);
		r_customer_10_tv  = (TextView)findViewById(R.id.r_customer_10_tv);
		r_customer_11_tv  = (TextView)findViewById(R.id.r_customer_11_tv);
		r_customer_12_tv  = (TextView)findViewById(R.id.r_customer_12_tv);
		r_customer_13_tv  = (TextView)findViewById(R.id.r_customer_13_tv);
		r_customer_14_tv  = (TextView)findViewById(R.id.r_customer_14_tv);
		r_customer_15_tv  = (TextView)findViewById(R.id.r_customer_15_tv);
		r_customer_16_tv  = (TextView)findViewById(R.id.r_customer_16_tv);

		r_facilities_01_tv  = (TextView)findViewById(R.id.r_facilities_01_tv);
		r_facilities_02_tv  = (TextView)findViewById(R.id.r_facilities_02_tv);
		r_facilities_03_tv  = (TextView)findViewById(R.id.r_facilities_03_tv);
		r_facilities_04_tv  = (TextView)findViewById(R.id.r_facilities_04_tv);
		r_facilities_05_tv  = (TextView)findViewById(R.id.r_facilities_05_tv);


		showTab();




		//  summaryInfo_layout.setVisibility(LinearLayout.VISIBLE);

	}


	private void showTab() {

		if (condition.equals(MossDef.CONDITION_P)) {

			p_layout.setVisibility(LinearLayout.VISIBLE);
			c_layout.setVisibility(LinearLayout.GONE);

			group_grp = (RadioGroup) findViewById(R.id.group_grp_p);
			group_grp.setOnCheckedChangeListener(this);


		} else {

			p_layout.setVisibility(LinearLayout.GONE);
			c_layout.setVisibility(LinearLayout.VISIBLE);

			group_grp = (RadioGroup) findViewById(R.id.group_grp_c);
			group_grp.setOnCheckedChangeListener(this);


			testInfo_layout = (LinearLayout)findViewById(R.id.testInfo_layout);	

			sp = (Spinner) this.findViewById(R.id.spinner);
			r_test_01_tv  = (TextView)findViewById(R.id.r_test_01_tv);
			r_test_02_tv  = (TextView)findViewById(R.id.r_test_02_tv);
			r_test_03_tv  = (TextView)findViewById(R.id.r_test_03_tv);
			r_test_04_tv  = (TextView)findViewById(R.id.r_test_04_tv);

			sp_speed = (Spinner) this.findViewById(R.id.spinner1);
			r_test_05_tv  = (TextView)findViewById(R.id.r_test_05_tv);
			r_test_06_tv  = (TextView)findViewById(R.id.r_test_06_tv);
			r_test_07_tv  = (TextView)findViewById(R.id.r_test_07_tv);
			r_test_07_1_tv  = (TextView)findViewById(R.id.r_test_07_1_tv);
			r_test_07_2_tv  = (TextView)findViewById(R.id.r_test_07_2_tv);
			r_test_08_tv  = (TextView)findViewById(R.id.r_test_08_tv);
			r_test_08_1_tv  = (TextView)findViewById(R.id.r_test_08_1_tv);
			r_test_08_2_tv  = (TextView)findViewById(R.id.r_test_08_2_tv);
			r_test_09_tv  = (TextView)findViewById(R.id.r_test_09_tv);
			r_test_10_tv  = (TextView)findViewById(R.id.r_test_10_tv);
			r_test_11_tv  = (TextView)findViewById(R.id.r_test_11_tv);
			r_test_12_tv  = (TextView)findViewById(R.id.r_test_12_tv);
			r_test_13_tv  = (TextView)findViewById(R.id.r_test_13_tv);
			r_test_14_tv  = (TextView)findViewById(R.id.r_test_14_tv);
			r_test_15_tv  = (TextView)findViewById(R.id.r_test_15_tv);
			r_test_16_tv  = (TextView)findViewById(R.id.r_test_16_tv);
			r_test_17_tv  = (TextView)findViewById(R.id.r_test_17_tv);
			r_test_18_tv  = (TextView)findViewById(R.id.r_test_18_tv);
			r_test_19_tv  = (TextView)findViewById(R.id.r_test_19_tv);
			r_test_20_tv  = (TextView)findViewById(R.id.r_test_20_tv);
			r_test_21_tv  = (TextView)findViewById(R.id.r_test_21_tv);
			r_test_22_tv  = (TextView)findViewById(R.id.r_test_22_tv);
			r_test_23_tv  = (TextView)findViewById(R.id.r_test_23_tv);
			r_test_24_tv  = (TextView)findViewById(R.id.r_test_24_tv);
			r_test_25_tv  = (TextView)findViewById(R.id.r_test_25_tv);
			r_test_31_tv  = (TextView)findViewById(R.id.r_test_31_tv);
			r_test_32_tv  = (TextView)findViewById(R.id.r_test_32_tv);
			r_test_33_tv  = (TextView)findViewById(R.id.r_test_33_tv);
			r_test_34_tv  = (TextView)findViewById(R.id.r_test_34_tv);

		}
	}


	private void changedButton(int value) {

		if (condition.equals(MossDef.CONDITION_P)) {

			if (value == 0) {					
				summaryInfo_layout.setVisibility(LinearLayout.VISIBLE);
				customerInfo_layout.setVisibility(LinearLayout.GONE);
				fmInfo_layout.setVisibility(LinearLayout.GONE);		
			} else if (value == 1) {
				summaryInfo_layout.setVisibility(LinearLayout.GONE);
				customerInfo_layout.setVisibility(LinearLayout.VISIBLE);
				fmInfo_layout.setVisibility(LinearLayout.GONE);					
			} else if (value == 2) {					
				summaryInfo_layout.setVisibility(LinearLayout.GONE);
				customerInfo_layout.setVisibility(LinearLayout.GONE);
				fmInfo_layout.setVisibility(LinearLayout.VISIBLE);
			} 

		} else {


			if (value == 0) {						
				summaryInfo_layout.setVisibility(LinearLayout.VISIBLE);
				customerInfo_layout.setVisibility(LinearLayout.GONE);
				fmInfo_layout.setVisibility(LinearLayout.GONE);
				testInfo_layout.setVisibility(LinearLayout.GONE);
			} else if (value == 1) {
				summaryInfo_layout.setVisibility(LinearLayout.GONE);
				customerInfo_layout.setVisibility(LinearLayout.VISIBLE);
				fmInfo_layout.setVisibility(LinearLayout.GONE);
				testInfo_layout.setVisibility(LinearLayout.GONE);
			} else if (value == 2) {
				summaryInfo_layout.setVisibility(LinearLayout.GONE);
				customerInfo_layout.setVisibility(LinearLayout.GONE);
				fmInfo_layout.setVisibility(LinearLayout.VISIBLE);
				testInfo_layout.setVisibility(LinearLayout.GONE);
			} else if (value == 3) {
				summaryInfo_layout.setVisibility(LinearLayout.GONE);
				customerInfo_layout.setVisibility(LinearLayout.GONE);
				fmInfo_layout.setVisibility(LinearLayout.GONE);
				testInfo_layout.setVisibility(LinearLayout.VISIBLE);
			}

		}
		setData(value);

	}



	private void setData(int value) {



		if (data != null) {
			InternetResultData.class.getName();

			BodyData_R  bodyData =  data.getBody().get(0);

			Button btn_test  = (Button) findViewById(R.id.btn_test);
			Button btn_line  = (Button) findViewById(R.id.btn_line);
			Button btn_speed  = (Button) findViewById(R.id.btn_speed);


			if(bodyData.authcompld != null && bodyData.authcompld.equals("Y")){
				btn_test.setVisibility(Button.VISIBLE);
				btn_line.setVisibility(Button.GONE);
				btn_speed.setVisibility(Button.VISIBLE);
			} else {
				btn_test.setVisibility(Button.VISIBLE);
				btn_line.setVisibility(Button.VISIBLE);
				btn_speed.setVisibility(Button.GONE);

			}


			if (value == 0) {			
				r_summary_01_tv.setText(bodyData.workOdrNum);			 
//				r_summary_02_tv.setText(Html.fromHtml("<font color='#ff0000'>"+MossDef.getConditionStr(condition)+"</font>"));
				r_summary_02_tv.setText(Html.fromHtml("<font color='#ff0000'>진행</font>"));
				if(data.getSpeedResult().size() != 0){
					r_summary_02_tv.setText(Html.fromHtml("<font color='#ff0000'>완료</font>"));
				}
				r_summary_03_tv.setText(bodyData.officename);
				r_summary_04_tv.setText(odrType);
				r_summary_05_tv.setText(bodyData.svctype);			 
				r_summary_06_tv.setText(bodyData.ttID);			 
				r_summary_07_tv.setText(bodyData.reqstSeqNum);			 
				r_summary_08_tv.setText(bodyData.installDT);
				r_summary_09_tv.setText(bodyData.receiptDT);
				r_summary_10_tv.setText(bodyData.rsvDT);
				r_summary_11_tv.setText(bodyData.custName);
				r_summary_12_tv.setText(MossDef.makePhoneNumber(bodyData.telNum));
				r_summary_13_tv.setText(bodyData.address);			 		 			 
			}

			else if(value == 1) {
				r_customer_01_tv.setText(bodyData.custName);	
				r_customer_02_tv.setText(bodyData.kornetId);
				r_customer_03_tv.setText(MossDef.makePhoneNumber(bodyData.telNum));	

				r_customer_04_tv.setText(MossDef.makePhoneNumber(bodyData.contactTelNum1));	
				r_customer_05_tv.setText(MossDef.makePhoneNumber(bodyData.contactTelNum2));	// ?
				r_customer_06_tv.setText(bodyData.address);	
				r_customer_07_tv.setText(bodyData.svcFacility);	// ?

				r_customer_08_tv.setText(bodyData.reporterOpinion);				
				r_customer_09_tv.setText(bodyData.reqstName);	
				r_customer_10_tv.setText(MossDef.makePhoneNumber(bodyData.reqstTelNum));	
				r_customer_11_tv.setText(bodyData.reqstOpinion);	
				r_customer_12_tv.setText(bodyData.urgentYN);	
				r_customer_13_tv.setText(bodyData.dayRepeatReportCnt);	
				r_customer_14_tv.setText(bodyData.processIngreportCnt);	
				r_customer_15_tv.setText(bodyData.installAfterMonthYN);	
				r_customer_16_tv.setText(bodyData.workerName);	

			} 

			else if (value == 2) {
				r_facilities_01_tv.setText(bodyData.facInfo);
				r_facilities_02_tv.setText(bodyData.facDiv);
				r_facilities_03_tv.setText(bodyData.cableNum);
				r_facilities_04_tv.setText(bodyData.lluTelCo);
				r_facilities_05_tv.setText(MossDef.makePhoneNumber(bodyData.lluTelNum));
			}
			//측정결과 		 
			else if (value == 3) {	
				sheenlevel_layout = (LinearLayout)findViewById(R.id.sheenlevel_layout);
				speedresult_layout = (LinearLayout)findViewById(R.id.speedresult_layout);

				if(data.getResult().size() != 0){
					arraylist = new ArrayList<String>();

					for(InternetResultData  resultData :  data.getResult() ) {


						arraylist.add(resultData.opticTestDate);
					}

					adapter = new ArrayAdapter<String>(this,  R.layout.spinner_item, arraylist);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


					sp.setAdapter(adapter);
					sp.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							InternetResultData  resultData =  data.getResult().get(arg2);

							r_test_01_tv.setText(resultData.opticTestDate);
							r_test_02_tv.setText(resultData.opticWaveLength + " nm");
							r_test_03_tv.setText(resultData.opticTestQuality + " dBm");

							if(resultData.opticTestQuality != null && resultData.opticTestQuality.startsWith(".")){
								r_test_03_tv.setText("0"+resultData.opticTestQuality + " dBm");
							}
							else if(resultData.opticTestQuality != null && resultData.opticTestQuality.startsWith("-.")){
								r_test_03_tv.setText(resultData.opticTestQuality.replace("-.", "-0.") + " dBm");
							}
							else{
								r_test_03_tv.setText(resultData.opticTestQuality + " dBm");
							}
							r_test_04_tv.setText(resultData.opticTestReference + " dB");
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}

					});

				}else{
					if(data.getResult() != null ){
						sheenlevel_layout.setVisibility(LinearLayout.GONE);
						}
//					adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,
//							getResources().getStringArray(R.array.datanull));
//					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//					
//					sp.setAdapter(adapter);
//					sp.setSelection(0);
//					sp.setPrompt("광레벨 측정");
				
				}

				r_test_05_tv.setText(bodyData.authcompld_nm);
				r_test_06_tv.setText(bodyData.FTTHFlag);



				if(data.getSpeedResult().size() != 0){
					//speed result 


					arraylist_speed = new ArrayList<String>();

					for(InternetSpeedResultData SpeedresultData :  data.getSpeedResult() ) {
						arraylist_speed.add(SpeedresultData.testdate);
					}


					adapter_speed = new ArrayAdapter<String>(this,  R.layout.spinner_item, arraylist_speed);
					adapter_speed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


					sp_speed.setAdapter(adapter_speed);
					sp_speed.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							InternetSpeedResultData SpeedresultData =  data.getSpeedResult().get(arg2);
							//다운로드
							r_test_07_tv.setText(CommonUtil.measured(SpeedresultData.downloadspeedmax));
							r_test_07_1_tv.setText(CommonUtil.measured(SpeedresultData.downloadspeedavg));
							r_test_07_2_tv.setText(CommonUtil.measured(SpeedresultData.downloadspeedmin));
							//업로드
							r_test_08_tv.setText(CommonUtil.measured(SpeedresultData.uploadspeedmax));
							r_test_08_1_tv.setText(CommonUtil.measured(SpeedresultData.uploadspeedavg));
							r_test_08_2_tv.setText(CommonUtil.measured(SpeedresultData.uploadspeedmin));
							//ping (지연/손실)
							r_test_09_tv.setText(SpeedresultData.packdelaytimemin + " ms");
							r_test_10_tv.setText(SpeedresultData.packdelaytimestd + " ms");
							r_test_11_tv.setText(SpeedresultData.packdelaytimemax + " ms");
							r_test_12_tv.setText(SpeedresultData.packlossrate + " %");
							r_test_13_tv.setText(SpeedresultData.packdelayimeavg + " ms");
							//udp (지연/손실)
							r_test_14_tv.setText(SpeedresultData.udpdelaytimemin + " ms");
							r_test_15_tv.setText(SpeedresultData.udpdelaytimestd + " ms");
							r_test_16_tv.setText(SpeedresultData.udpdelaytimemax + " ms");
							r_test_17_tv.setText(SpeedresultData.udplossrate + " %");
							r_test_18_tv.setText(SpeedresultData.udpdelaytimeavg + " ms");
							//기타
							r_test_19_tv.setText(SpeedresultData.cpurate + " %");
							r_test_20_tv.setText(SpeedresultData.memoryrate + " %");
							//고객정보
							r_test_21_tv.setText(SpeedresultData.ip);
							r_test_22_tv.setText(SpeedresultData.memorysize + " Mbytes");
							r_test_23_tv.setText(SpeedresultData.os);
							r_test_24_tv.setText(CommonUtil.nullCheck(SpeedresultData.browser));
							r_test_25_tv.setText(SpeedresultData.cputype);
							//품질정보
							r_test_31_tv.setText(CommonUtil.changeint2000(SpeedresultData.downloadspeedavg));
							if(CommonUtil.changeint2000(SpeedresultData.downloadspeedavg).equals("품질미달")){
								r_test_31_tv.setTextColor(Color.RED);
							}else{
								r_test_31_tv.setTextColor(Color.BLACK);
							}
							r_test_32_tv.setText(CommonUtil.changeint0(SpeedresultData.uploadspeedavg));
							if(CommonUtil.changeint0(SpeedresultData.uploadspeedavg).equals("품질미달")){
								r_test_32_tv.setTextColor(Color.RED);
							}else{
								r_test_32_tv.setTextColor(Color.BLACK);
							}
							r_test_33_tv.setText(CommonUtil.changeint0(SpeedresultData.packdelayimeavg));
							if(CommonUtil.changeint0(SpeedresultData.packdelayimeavg).equals("품질미달")){
								r_test_33_tv.setTextColor(Color.RED);
							}else{
								r_test_33_tv.setTextColor(Color.BLACK);
							}
							r_test_34_tv.setText(CommonUtil.changeint0(SpeedresultData.udpdelaytimeavg));
							if(CommonUtil.changeint0(SpeedresultData.udpdelaytimeavg).equals("품질미달")){
								r_test_34_tv.setTextColor(Color.RED);
							}else{
								r_test_34_tv.setTextColor(Color.BLACK);
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}

					});



				}else{
					if(data.getSpeedResult() != null ){
						speedresult_layout.setVisibility(LinearLayout.GONE);
						}
//					adapter_speed = new ArrayAdapter<String>(this,R.layout.spinner_item,
//							getResources().getStringArray(R.array.datanull));
//					adapter_speed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//					
//					sp_speed.setAdapter(adapter_speed);
//					sp_speed.setSelection(0);
//					sp_speed.setPrompt("회선 인증 후 인터넷속도측정을 \n해주시기바랍니다.");
				}
			}
		}
	}


	@Override	
	public void onCheckedChanged(RadioGroup group, int checkedId) {       
		// TODO Auto-generated method stub


		switch(checkedId){       


		case R.id.summaryInfo_p:
			checkedItem = 0;
			changedButton(checkedItem);
			break;
		case R.id.customerInfo_p:
			checkedItem = 1; 
			changedButton(checkedItem);
			break;
		case R.id.fmInfo_p:
			checkedItem = 2;
			changedButton(checkedItem);
			break;

		case R.id.summaryInfo_c:
			checkedItem = 0;
			changedButton(checkedItem);
			break;
		case R.id.customerInfo_c:
			checkedItem = 1; 
			changedButton(checkedItem);
			break;
		case R.id.fmInfo_c:
			checkedItem = 2;
			changedButton(checkedItem);
			break;
		case R.id.testInfo_c:
			checkedItem = 3;
			changedButton(checkedItem);
			break;	


		default:

			break;
		}



	}





	private class DetailAsyncTask extends AsyncTask<String, Void, Void> {


		private final HttpClient httpclient = new DefaultHttpClient();
		final HttpParams params = httpclient.getParams();
		private boolean error = false;
		private InputStream is = null;

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

					Reader reader = new InputStreamReader(is); 

					data = gson.fromJson(reader, InternetData_R.class); 					


				} else {
					// Closes the connection.
					//	Log.w(LOG_TAG, statusLine.getReasonPhrase());
					response.getEntity().getContent().close();

					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {
				//	Log.w(LOG_TAG, e);
				error = true;
				cancel(true);
			}
			return null;
		}

		protected void onCancelled() {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}	
			Toast.makeText(OrderDetailInternet_R.this, "수신 실패", Toast.LENGTH_SHORT).show();

		}

		protected void onPostExecute(Void unused) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}


			if (error) {

				Toast.makeText(OrderDetailInternet_R.this, "수신 실패", Toast.LENGTH_SHORT).show();
			} else {				

				if (data != null) {

					if (            (data.getResult().size() == 0 
							|| data.getResult().get(0).opticTestDate == null)
							&&
							(data.getSpeedResult().size() == 0 
							|| data.getSpeedResult().get(0).testdate == null))
					{
						condition = MossDef.CONDITION_P;
					} else {
						condition = MossDef.CONDITION_C;
					}
				}

				showTab();

				changedButton(checkedItem);

			}
		}
	}




	public void clickReset(View v) {

		if (mProgressDialog != null) {
			mProgressDialog.dismiss();						
		}
		mProgressDialog = ProgressDialog.show(OrderDetailInternet_R.this,  "", "인터넷\n상세정보 조회중입니다.");

		new DetailAsyncTask().execute(detailUrl.toString());			

	}

	public void runTest(View v){


		//  Toast.makeText(OrderDetailInternet_R.this, "광레벨 측정앱 실행", Toast.LENGTH_SHORT).show();


		PackageManager packageManager = getPackageManager();

		String packageName = MossDef.OPTICAL_TEST_APP_PATH;


		try {

			Intent intent = packageManager.getLaunchIntentForPackage(packageName);


			if (intent != null) {

				intent.putExtra("workOdrNum", workOdrNum);
				intent.putExtra("custName", custName);
				intent.putExtra("telNum", telNum);
				startActivity(intent);
			}else {

				alertShow("측정앱이 설치되어 있지 않습니다.\n앱 설치 후 실행하세요.");
			}

		} catch (ActivityNotFoundException e) {

			alertShow("측정앱이 설치되어 있지 않습니다.\n앱 설치 후 실행하세요.");


			// TODO: handle exception
		}

	}
	public void LineTest(View v){
		


		try {

			Intent intent = new Intent(OrderDetailInternet_R.this, LineTest.class);


			if (intent != null) {

				intent.putExtra("scn", CommonUtil.nullCheck(data.getBody().get(0).scn ).trim());
				intent.putExtra("workerId", user_id);
				intent.putExtra("workOdrNum", workOdrNum);
				

				startActivity(intent);
			}

		} catch (ActivityNotFoundException e) {
			// TODO: handle exception

			alertShow("측정앱이 설치되어 있지 않습니다.");

		}

	}

	public void speedTest(View v){
		


		try {

			Intent intent = new Intent(OrderDetailInternet_R.this, SpeedTest.class);

			if (intent != null) {

				intent.putExtra("scn", CommonUtil.nullCheck(data.getBody().get(0).scn ));
				intent.putExtra("workOdrNum", workOdrNum);
				intent.putExtra("workerId", user_id);
				intent.putExtra("workOdrKind", "2");
				intent.putExtra("kornetId", CommonUtil.nullCheck(data.getBody().get(0).kornetId));
				intent.putExtra("tel_no", CommonUtil.nullCheck(telNum_original)); 
				intent.putExtra("officesCode",CommonUtil.nullCheck(data.getBody().get(0).officescode));
				intent.putExtra("sla_down", data.getBody().get(0).sla_down);
				intent.putExtra("sla_up", data.getBody().get(0).sla_up);
				intent.putExtra("sla_udp", data.getBody().get(0).sla_udp);
				intent.putExtra("sla_ping", data.getBody().get(0).sla_ping);

                	

				startActivity(intent);
			}

		} catch (ActivityNotFoundException e) {
			// TODO: handle exception

			alertShow("측정앱이 설치되어 있지 않습니다.\n앱 설치 후 실행하세요.");

		}

	}



	@Override
	public void onDestroy()  {
		super.onDestroy();
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode == KeyEvent.KEYCODE_BACK) {

			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			Intent in = new Intent(OrderDetailInternet_R.this, OrderList.class);              
			if(data.getSpeedResult().size() != 0){
				in.putExtra("condition", condition);
			}                  
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			finish();


			return true;	        
		}else {
			return super.onKeyDown(keyCode, event);
		}	
	}


	public void clickBack(View v) {

		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}


		Intent in = new Intent(OrderDetailInternet_R.this, OrderList.class);              
		if(data.getSpeedResult().size() != 0){
			in.putExtra("condition", condition);
		}                 
		in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


		finish();


	}
	private void alertShow(String msg) {
		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(this);
		alert_internet_status.setTitle("연결 오류");
		//			alert_internet_status.setTitle("광레벨 측정앱 연결 오류");
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


}
