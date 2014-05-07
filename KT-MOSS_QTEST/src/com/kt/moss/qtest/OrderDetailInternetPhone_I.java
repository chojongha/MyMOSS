package com.kt.moss.qtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kt.moss.qtest.data.BodyData_I;
import com.kt.moss.qtest.data.InternetPhoneData_I;
import com.kt.moss.qtest.data.InternetPhoneResultData;
import com.kt.moss.qtest.util.MossDef;



/**
 * 인터넷전화 개통 (진행 및 완료) 정보
 * 
 * @author jhkim
 *
 */
public class OrderDetailInternetPhone_I extends Activity implements  OnItemSelectedListener, OnCheckedChangeListener {

	
	
	private RadioGroup group_grp;


 	
	
 	private LinearLayout p_layout;
 	private LinearLayout c_layout;
 	
 	private int checkedItem = 0;
 	 	
 	private LinearLayout summaryInfo_layout;
 	private LinearLayout customerInfo_layout;
 	private LinearLayout fmInfo_layout;
 	private LinearLayout testInfo_layout;
	 	
 	private String workOdrNum = "";
 	private String workOdrKind = "";
 	private String odrType = "";
 	private String condition = "";
 	private String telNum = "";
 	private String scn="";
 	private String officescode="";
	private String kornetId = "";
 	
 	private InternetPhoneData_I	data;
 	
 	private LinearLayout test_result_layout;
 	
 	private TextView i_summary_01_tv; // 작업의뢰번호
 	private TextView i_summary_02_tv; // 진행상태
 	private TextView i_summary_03_tv; // 수용국
 	private TextView i_summary_04_tv; // 오더내역
 	private TextView i_summary_05_tv; // 상품종류
 	private TextView i_summary_06_tv; // 가설 접수시간
 	private TextView i_summary_07_tv; // 고객 희망시간
 	private TextView i_summary_08_tv; // 방문 예정시간
 	private TextView i_summary_09_tv; // 고객명
 	private TextView i_summary_10_tv; // 전화번호
 	private TextView i_summary_11_tv; // 주소
 	
 	
 	private TextView i_customer_01_tv; // 고객명
 	private TextView i_customer_02_tv; // 코넷아이디
 	private TextView i_customer_03_tv; // 상품종류
 	private TextView i_customer_04_tv; // ADSL모뎀
 	private TextView i_customer_05_tv; // SAID
 	private TextView i_customer_06_tv; // 주소
 	private TextView i_customer_07_tv; // 전화번호
 	private TextView i_customer_08_tv; // 휴대전화
	private TextView i_customer_09_tv; // 고객요구사항
 	private TextView i_customer_10_tv; // 고객요구사항
 	
 	private TextView i_facilities_01_tv; // ADSL/FLC 포트정보
 	private TextView i_facilities_02_tv; // ADSL 타이번호
 	private TextView i_facilities_03_tv; // FLC 타이번호
 	private TextView i_facilities_04_tv; // INET 타이번호
 	private TextView i_facilities_05_tv; // LC 타이번호
 	private TextView i_facilities_06_tv; // 심선번호
 	private TextView i_facilities_07_tv; // MDF LEN
 	private TextView i_facilities_08_tv; // 가설단자함 위치주소
 	private TextView i_facilities_09_tv; // LLU 사업자
 	private TextView i_facilities_10_tv; // LLU KT 전화번호
 	
 	
 	// 하향
 	private TextView p_test_01_tv_d; // 미디어 방향
 	private TextView p_test_02_tv_d; // 콜 방향
 	private TextView p_test_03_tv_d; // 로그시각
 	private TextView p_test_04_tv_d; // 발신번호
 	private TextView p_test_05_tv_d; // 테스트 고객 번호
 	private TextView p_test_06_tv_d; // 코덱 종류
 	private TextView p_test_07_tv_d; // 테스트 시작시간
 	private TextView p_test_08_tv_d; // 테스트 종료시간
 	private TextView p_test_09_tv_d; // 원격 Rtp IP
 	private TextView p_test_10_tv_d; // 원격 Rtp 포트
 	private TextView p_test_11_tv_d; // 송신 패킷수
 	private TextView p_test_12_tv_d; // 수신 패킷수
 	private TextView p_test_13_tv_d; // 손실 패킷수
 	private TextView p_test_14_tv_d; // 손실률(평균)
 	private TextView p_test_15_tv_d; // 손실률(최대)
 	private TextView p_test_16_tv_d; // 에러코드
 	private TextView p_test_17_tv_d; // 지터(최대)
 	private TextView p_test_18_tv_d; // 지터(평균)
 	private TextView p_test_19_tv_d; // 지터 손실
 	private TextView p_test_20_tv_d; // 지연(최대)
 	private TextView p_test_21_tv_d; // 지연(평균)
 	private TextView p_test_22_tv_d; // R값(평균)
 	private TextView p_test_23_tv_d; // R값(최소)
 	private TextView p_test_24_tv_d; // Pseq(평균)
 	private TextView p_test_25_tv_d; // Pseqeq(최소)
 	private TextView p_test_26_tv_d; // Mos값(평균)
 	private TextView p_test_27_tv_d; // Mos(최소)


 	// 상향
 	private TextView p_test_01_tv_u; // 미디어 방향
 	private TextView p_test_02_tv_u; // 콜 방향
 	private TextView p_test_03_tv_u; // 로그시각
 	private TextView p_test_04_tv_u; // 발신번호
 	private TextView p_test_05_tv_u; // 테스트 고객 번호
 	private TextView p_test_06_tv_u; // 코덱 종류
 	private TextView p_test_07_tv_u; // 테스트 시작시간
 	private TextView p_test_08_tv_u; // 테스트 종료시간
 	private TextView p_test_09_tv_u; // 원격 Rtp IP
 	private TextView p_test_10_tv_u; // 원격 Rtp 포트
 	private TextView p_test_11_tv_u; // 송신 패킷수
 	private TextView p_test_12_tv_u; // 수신 패킷수
 	private TextView p_test_13_tv_u; // 손실 패킷수
 	private TextView p_test_14_tv_u; // 손실률(평균)
 	private TextView p_test_15_tv_u; // 손실률(최대)
 	private TextView p_test_16_tv_u; // 에러코드
 	private TextView p_test_17_tv_u; // 지터(최대)
 	private TextView p_test_18_tv_u; // 지터(평균)
 	private TextView p_test_19_tv_u; // 지터 손실
 	private TextView p_test_20_tv_u; // 지연(최대)
 	private TextView p_test_21_tv_u; // 지연(평균)
 	private TextView p_test_22_tv_u; // R값(평균)
 	private TextView p_test_23_tv_u; // R값(최소)
 	private TextView p_test_24_tv_u; // Pseq(평균)
 	private TextView p_test_25_tv_u; // Pseqeq(최소)
 	private TextView p_test_26_tv_u; // Mos값(평균)
 	private TextView p_test_27_tv_u; // Mos(최소)
 	
 	private ArrayList<String> arraylist = null;
 	private ArrayAdapter<String> adapter = null;
  	private StringBuffer detailUrl = null;
 	private Spinner sp;
	ProgressDialog  mProgressDialog  = null;
	
	
 	// http://192.168.0.9:8080/moss/WorkOdrDetail?workOdrNum=1O2011090512020&workOdrKind=I&odrType=I
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		

		// 화면 구성하기
		setContentView(R.layout.order_detail_internet_phone_i);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,	R.layout.custom_detail_title_bar);
		TextView title = (TextView) findViewById(R.id.title_name);
		title.setText("인터넷 전화");
		
		TextView title_type = (TextView) findViewById(R.id.title_type);
		title_type.setText("개통");		
		
		Intent intent = getIntent();

		workOdrNum = intent.getStringExtra("workOdrNum");
		workOdrKind = intent.getStringExtra("workOdrKind");
		odrType = intent.getStringExtra("odrType");
		condition = intent.getStringExtra("condition");
		telNum = intent.getStringExtra("telNum");
		
		
//		scn = intent.getStringExtra("scn");
//		officescode = intent.getStringExtra("officesCode");
//		kornetId = intent.getStringExtra("kornetId");
		 
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
			
			 System.out.println(" 인터넷 전화 상세정보 :: " + detailUrl.toString());
			 mProgressDialog = ProgressDialog.show(OrderDetailInternetPhone_I.this,  "", "인터넷전화\n상세정보 조회중입니다.");
			new DetailAsyncTask().execute(detailUrl.toString());
	 }
	 
	
	
	
	
	
	private void Init() {
		
		
		p_layout = (LinearLayout)findViewById(R.id.p_layout);
		c_layout = (LinearLayout)findViewById(R.id.c_layout);
		
		  summaryInfo_layout = (LinearLayout)findViewById(R.id.summaryInfo_layout);
		  customerInfo_layout = (LinearLayout)findViewById(R.id.customerInfo_layout);
		  fmInfo_layout = (LinearLayout)findViewById(R.id.fmInfo_layout);	
		  testInfo_layout = (LinearLayout)findViewById(R.id.testInfo_layout);	
			test_result_layout = (LinearLayout)findViewById(R.id.test_result_layout);	
		
		  	i_summary_01_tv  = (TextView)findViewById(R.id.i_summary_01_tv);	
			i_summary_02_tv  = (TextView)findViewById(R.id.i_summary_02_tv);	
			i_summary_03_tv  = (TextView)findViewById(R.id.i_summary_03_tv);	
			i_summary_04_tv  = (TextView)findViewById(R.id.i_summary_04_tv);	
			i_summary_05_tv  = (TextView)findViewById(R.id.i_summary_05_tv);	
			i_summary_06_tv  = (TextView)findViewById(R.id.i_summary_06_tv);	
			i_summary_07_tv  = (TextView)findViewById(R.id.i_summary_07_tv);	
			i_summary_08_tv  = (TextView)findViewById(R.id.i_summary_08_tv);	
			i_summary_09_tv  = (TextView)findViewById(R.id.i_summary_09_tv);	
			i_summary_10_tv  = (TextView)findViewById(R.id.i_summary_10_tv);	
			i_summary_11_tv  = (TextView)findViewById(R.id.i_summary_11_tv);	
			
			i_customer_01_tv  = (TextView)findViewById(R.id.i_customer_01_tv);	
			i_customer_02_tv  = (TextView)findViewById(R.id.i_customer_02_tv);
			i_customer_03_tv  = (TextView)findViewById(R.id.i_customer_03_tv);
			i_customer_04_tv  = (TextView)findViewById(R.id.i_customer_04_tv);
			i_customer_05_tv  = (TextView)findViewById(R.id.i_customer_05_tv);
			i_customer_06_tv  = (TextView)findViewById(R.id.i_customer_06_tv);
			i_customer_07_tv  = (TextView)findViewById(R.id.i_customer_07_tv);
			i_customer_08_tv  = (TextView)findViewById(R.id.i_customer_08_tv);
			i_customer_09_tv  = (TextView)findViewById(R.id.i_customer_09_tv);
			i_customer_10_tv  = (TextView)findViewById(R.id.i_customer_10_tv);
		  
			i_facilities_01_tv  = (TextView)findViewById(R.id.i_facilities_01_tv);
			i_facilities_02_tv  = (TextView)findViewById(R.id.i_facilities_02_tv);
			i_facilities_03_tv  = (TextView)findViewById(R.id.i_facilities_03_tv);
			i_facilities_04_tv  = (TextView)findViewById(R.id.i_facilities_04_tv);
			i_facilities_05_tv  = (TextView)findViewById(R.id.i_facilities_05_tv);
			i_facilities_06_tv  = (TextView)findViewById(R.id.i_facilities_06_tv);
			i_facilities_07_tv  = (TextView)findViewById(R.id.i_facilities_07_tv);
			i_facilities_08_tv  = (TextView)findViewById(R.id.i_facilities_08_tv);
			i_facilities_09_tv  = (TextView)findViewById(R.id.i_facilities_09_tv);
			i_facilities_10_tv  = (TextView)findViewById(R.id.i_facilities_10_tv);
			
			
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
			
			sp = (Spinner) this.findViewById(R.id.spinner);
 			
 			
 			
 		 	p_test_01_tv_d  = (TextView)findViewById(R.id.p_test_01_tv);
 		 	p_test_02_tv_d  = (TextView)findViewById(R.id.p_test_02_tv);
 		 	p_test_03_tv_d  = (TextView)findViewById(R.id.p_test_03_tv);
 		 	p_test_04_tv_d  = (TextView)findViewById(R.id.p_test_04_tv);
 		 	p_test_05_tv_d  = (TextView)findViewById(R.id.p_test_05_tv);
 		 	p_test_06_tv_d  = (TextView)findViewById(R.id.p_test_06_tv);
 		 	p_test_07_tv_d  = (TextView)findViewById(R.id.p_test_07_tv);
 		 	p_test_08_tv_d  = (TextView)findViewById(R.id.p_test_08_tv);
 		 	p_test_09_tv_d  = (TextView)findViewById(R.id.p_test_09_tv);
 		 	p_test_10_tv_d  = (TextView)findViewById(R.id.p_test_10_tv);
 		 	p_test_11_tv_d  = (TextView)findViewById(R.id.p_test_11_tv);
 		 	p_test_12_tv_d  = (TextView)findViewById(R.id.p_test_12_tv);
 		 	p_test_13_tv_d  = (TextView)findViewById(R.id.p_test_13_tv);
 		 	p_test_14_tv_d  = (TextView)findViewById(R.id.p_test_14_tv);
 		 	p_test_15_tv_d  = (TextView)findViewById(R.id.p_test_15_tv);
 		 	p_test_16_tv_d  = (TextView)findViewById(R.id.p_test_16_tv);
 		 	p_test_17_tv_d  = (TextView)findViewById(R.id.p_test_17_tv);
 		 	p_test_18_tv_d  = (TextView)findViewById(R.id.p_test_18_tv);
 		 	p_test_19_tv_d  = (TextView)findViewById(R.id.p_test_19_tv);
 		 	p_test_20_tv_d  = (TextView)findViewById(R.id.p_test_20_tv);
 		 	p_test_21_tv_d  = (TextView)findViewById(R.id.p_test_21_tv);
 		 	p_test_22_tv_d  = (TextView)findViewById(R.id.p_test_22_tv);
 		 	p_test_23_tv_d  = (TextView)findViewById(R.id.p_test_23_tv);
 		 	p_test_24_tv_d  = (TextView)findViewById(R.id.p_test_24_tv);
 		 	p_test_25_tv_d  = (TextView)findViewById(R.id.p_test_25_tv);
 		 	p_test_26_tv_d  = (TextView)findViewById(R.id.p_test_26_tv);
 		 	p_test_27_tv_d  = (TextView)findViewById(R.id.p_test_27_tv);
 		 	
 		 	
 			p_test_01_tv_u  = (TextView)findViewById(R.id.p_test_01_tv_u);
 		 	p_test_02_tv_u  = (TextView)findViewById(R.id.p_test_02_tv_u);
 		 	p_test_03_tv_u  = (TextView)findViewById(R.id.p_test_03_tv_u);
 		 	p_test_04_tv_u  = (TextView)findViewById(R.id.p_test_04_tv_u);
 		 	p_test_05_tv_u  = (TextView)findViewById(R.id.p_test_05_tv_u);
 		 	p_test_06_tv_u  = (TextView)findViewById(R.id.p_test_06_tv_u);
 		 	p_test_07_tv_u  = (TextView)findViewById(R.id.p_test_07_tv_u);
 		 	p_test_08_tv_u  = (TextView)findViewById(R.id.p_test_08_tv_u);
 		 	p_test_09_tv_u  = (TextView)findViewById(R.id.p_test_09_tv_u);
 		 	p_test_10_tv_u  = (TextView)findViewById(R.id.p_test_10_tv_u);
 		 	p_test_11_tv_u  = (TextView)findViewById(R.id.p_test_11_tv_u);
 		 	p_test_12_tv_u  = (TextView)findViewById(R.id.p_test_12_tv_u);
 		 	p_test_13_tv_u  = (TextView)findViewById(R.id.p_test_13_tv_u);
 		 	p_test_14_tv_u  = (TextView)findViewById(R.id.p_test_14_tv_u);
 		 	p_test_15_tv_u  = (TextView)findViewById(R.id.p_test_15_tv_u);
 		 	p_test_16_tv_u  = (TextView)findViewById(R.id.p_test_16_tv_u);
 		 	p_test_17_tv_u  = (TextView)findViewById(R.id.p_test_17_tv_u);
 		 	p_test_18_tv_u  = (TextView)findViewById(R.id.p_test_18_tv_u);
 		 	p_test_19_tv_u  = (TextView)findViewById(R.id.p_test_19_tv_u);
 		 	p_test_20_tv_u  = (TextView)findViewById(R.id.p_test_20_tv_u);
 		 	p_test_21_tv_u  = (TextView)findViewById(R.id.p_test_21_tv_u);
 		 	p_test_22_tv_u  = (TextView)findViewById(R.id.p_test_22_tv_u);
 		 	p_test_23_tv_u  = (TextView)findViewById(R.id.p_test_23_tv_u);
 		 	p_test_24_tv_u  = (TextView)findViewById(R.id.p_test_24_tv_u);
 		 	p_test_25_tv_u  = (TextView)findViewById(R.id.p_test_25_tv_u);
 		 	p_test_26_tv_u  = (TextView)findViewById(R.id.p_test_26_tv_u);
 		 	p_test_27_tv_u  = (TextView)findViewById(R.id.p_test_27_tv_u);
 		 	
			
  		 			
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
			 BodyData_I  bodyData =  data.getBody().get(0);
			
	     Log.i("Phone_setdata!!", data.toString());
			 if (value == 0) {
				
				 i_summary_01_tv.setText(bodyData.workOdrNum);			 
				 i_summary_02_tv.setText(Html.fromHtml("<font color='#ff0000'>"+MossDef.getConditionStr(condition)+"</font>")); 
				 i_summary_03_tv.setText(bodyData.officename);
				 i_summary_04_tv.setText(odrType);
				 i_summary_05_tv.setText(bodyData.svctype);
				 i_summary_06_tv.setText(bodyData.receiptDT);
				 i_summary_07_tv.setText(bodyData.connHopeDT);
				 i_summary_08_tv.setText(bodyData.rsvDT);
				 i_summary_09_tv.setText(bodyData.custName);
				 i_summary_10_tv.setText(MossDef.makePhoneNumber(bodyData.telNum));
				 i_summary_11_tv.setText(bodyData.address);
				 		 
				 
			} else if(value == 1) {
				i_customer_01_tv.setText(bodyData.custName);	
				i_customer_02_tv.setText(bodyData.kornetId);
				i_customer_03_tv.setText(bodyData.svctype);	
				i_customer_04_tv.setText(bodyData.adslTerm);	
				i_customer_05_tv.setText(bodyData.scn);	
				i_customer_06_tv.setText(bodyData.address);	
				i_customer_07_tv.setText(MossDef.makePhoneNumber(bodyData.telNum));	
				i_customer_08_tv.setText(MossDef.makePhoneNumber(bodyData.contactTelNum1));	
				i_customer_09_tv.setText(MossDef.makePhoneNumber(bodyData.contactTelNum2));	
				i_customer_10_tv.setText(bodyData.remark);	
			} 
			 
			else if (value == 2) {
				i_facilities_01_tv.setText(bodyData.adslflcTiePort);
				i_facilities_02_tv.setText(bodyData.adslTie);
				i_facilities_03_tv.setText(bodyData.flcPortAlias);
				i_facilities_04_tv.setText(bodyData.inetTie);
				i_facilities_05_tv.setText(bodyData.lcTie);
				i_facilities_06_tv.setText(bodyData.cableNum);
				i_facilities_07_tv.setText(bodyData.len);
				i_facilities_08_tv.setText(bodyData.termBoxLoc);
				i_facilities_09_tv.setText(bodyData.lluBusiCode);
				i_facilities_10_tv.setText(MossDef.makePhoneNumber(bodyData.lluTelnum));
				
			}
			//측정결과 		 
			else if (value == 3) {
				
				
								
				
				
				
				ArrayList<String> 	resultList = new ArrayList<String>();
				
				for(InternetPhoneResultData  resultData : data.getResult() ) {				
									
					resultList.add(resultData.testDate);
					
				}
				Log.i("resulttest", data.toString());
			
				arraylist = new ArrayList<String>(); 
				//hashset:중복없이 자료를 저장하고싶을때 사용
				HashSet<String> hs = new HashSet<String>(resultList); 
				         
				Iterator<String> it = hs.iterator(); 
				
				
				while(it.hasNext()){ 
					arraylist.add(it.next()); 
				} 
				
				
				// 내림차순 정렬
				 Collections.sort(arraylist, Collections.reverseOrder());

							
				adapter = new ArrayAdapter<String>(this,  R.layout.spinner_item, arraylist);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			 
				
				sp.setAdapter(adapter);
				sp.setOnItemSelectedListener(this);
				
				String selectedDate = arraylist.get(0);
								
				setTestData(selectedDate);
								
				
			}
		}
	
		
	 }
	 
	 
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub		

			String selectedDate =   arraylist.get(arg2);
			
			setTestData(selectedDate);
			
		}

		
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub			
		}

		
		
		
		private void setTestData(String selectedDate) {
			
			ArrayList<InternetPhoneResultData> viewList = new ArrayList<InternetPhoneResultData>();
			
			for (InternetPhoneResultData  resultData :  data.getResult()) {
								
				if (resultData.testDate.equals(selectedDate)) {					
					viewList.add(resultData);
				}
			}
			Log.i("settestdata!!!", data.toString());
			
			int size = viewList.size();
			
			if (size == 2) {
				
				test_result_layout.setVisibility(LinearLayout.VISIBLE);
				
				InternetPhoneResultData  bodyData_down = viewList.get(0);
				
				if (bodyData_down.mediaDirection.equals("상향")) {
					p_test_01_tv_d.setText(Html.fromHtml("<font color='#0000ff'>"+bodyData_down.mediaDirection+"</font>"));
				} else {
					p_test_01_tv_d.setText(Html.fromHtml("<font color='#ff0000'>"+bodyData_down.mediaDirection+"</font>"));
				}
				
				p_test_02_tv_d.setText(bodyData_down.callDirection);
	 		 	p_test_03_tv_d.setText(bodyData_down.testDate);
	 		 	p_test_04_tv_d.setText(MossDef.makePhoneNumber(bodyData_down.caller));
	 		 	p_test_05_tv_d.setText(MossDef.makePhoneNumber(bodyData_down.callee));
	 		 	p_test_06_tv_d.setText(bodyData_down.codecType);
	 		 	p_test_07_tv_d.setText(bodyData_down.callTime);
	 		 	p_test_08_tv_d.setText(bodyData_down.endTime);
	 		 	p_test_09_tv_d.setText(bodyData_down.remoterTpIp);
	 		 	p_test_10_tv_d.setText(bodyData_down.remoterTpIp);
	 		 	p_test_11_tv_d.setText(bodyData_down.txRtp);
	 		 	p_test_12_tv_d.setText(bodyData_down.rxRtp);
	 		 	p_test_13_tv_d.setText(bodyData_down.rtpLoss);
	 		 	p_test_14_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.lossAvg) + " %");
	 		 	p_test_15_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.lossMax)+ " %");
	 		 	p_test_16_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.errCause));
	 		 	p_test_17_tv_d.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_down.jitterMax)) + " ms");
	 		 	p_test_18_tv_d.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_down.jitterAvg)) + " ms");
	 		 	p_test_19_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.jitterLoss)+ " %");
	 		 	p_test_20_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.delayMax)+ " ms");
	 		 	p_test_21_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.delayAvg)+ " ms");
	 		 	p_test_22_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.cqrAvg)+ " 점");
	 		 	p_test_23_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.cqrMin)+ " 점");
	 		 	p_test_24_tv_d.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_down.pesqAvg))+ " 점");
	 		 	p_test_25_tv_d.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_down.pesqMin))+ " 점");
	 		 	p_test_26_tv_d.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_down.cqmosAvg))+ " 점");
	 		 	p_test_27_tv_d.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_down.cqmosMin))+ " 점");
	 		 		 		 	
	 		 	InternetPhoneResultData  bodyData_up = viewList.get(1);
				
				if (bodyData_up.mediaDirection.equals("상향")) {					
					p_test_01_tv_u.setText(Html.fromHtml("<font color='#0000ff'>"+bodyData_up.mediaDirection+"</font>"));
				} else {					
					p_test_01_tv_u.setText(Html.fromHtml("<font color='#ff0000'>"+bodyData_up.mediaDirection+"</font>"));
				}							
				
	 		 	p_test_02_tv_u.setText(bodyData_up.callDirection);
	 		 	p_test_03_tv_u.setText(bodyData_up.testDate);
	 		 	
	 		 	p_test_04_tv_u.setText(MossDef.makePhoneNumber(bodyData_up.caller));
	 		 	p_test_05_tv_u.setText(MossDef.makePhoneNumber(bodyData_up.callee));
	 		 	
	 		 	p_test_06_tv_u.setText(bodyData_up.codecType);
	 		 	p_test_07_tv_u.setText(bodyData_up.callTime);
	 		 	p_test_08_tv_u.setText(bodyData_up.endTime);
	 		 	p_test_09_tv_u.setText(bodyData_up.remoterTpIp);
	 		 	p_test_10_tv_u.setText(bodyData_up.remoterTpIp);
	 		 	p_test_11_tv_u.setText(bodyData_up.txRtp);
	 		 	p_test_12_tv_u.setText(bodyData_up.rxRtp);
	 		 	p_test_13_tv_u.setText(bodyData_up.rtpLoss);
	 		 	p_test_14_tv_u.setText(MossDef.getNullCheckString2(bodyData_up.lossAvg) + " %");
	 		 	p_test_15_tv_u.setText(MossDef.getNullCheckString2(bodyData_up.lossMax)+ " %");
	 		 	p_test_16_tv_u.setText(MossDef.getNullCheckString2(bodyData_up.errCause));
	 		 	p_test_17_tv_u.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_up.jitterMax)) + " ms");
	 		 	p_test_18_tv_u.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_up.jitterAvg)) + " ms");
	 		 	p_test_19_tv_u.setText(MossDef.getNullCheckString2(bodyData_up.jitterLoss)+ " %");
	 		 	p_test_20_tv_u.setText(MossDef.getNullCheckString2(bodyData_up.delayMax)+ " ms");
	 		 	p_test_21_tv_u.setText(MossDef.getNullCheckString2(bodyData_up.delayAvg)+ " ms");
	 		 	p_test_22_tv_u.setText(MossDef.getNullCheckString2(bodyData_up.cqrAvg)+ " 점");
	 		 	p_test_23_tv_u.setText(MossDef.getNullCheckString2(bodyData_up.cqrMin)+ " 점");
	 		 	p_test_24_tv_u.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_up.pesqAvg))+ " 점");
	 		 	p_test_25_tv_u.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_up.pesqMin))+ " 점");
	 		 	p_test_26_tv_u.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_up.cqmosAvg))+ " 점");
	 		 	p_test_27_tv_u.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_up.cqmosMin))+ " 점");
	 		 	
			} else {
				
				
				test_result_layout.setVisibility(LinearLayout.GONE);					
				
				InternetPhoneResultData  bodyData_down = viewList.get(0);
					
				
				if (bodyData_down.mediaDirection.equals("상향")) {
					
					p_test_01_tv_d.setText(Html.fromHtml("<font color='#0000ff'>"+bodyData_down.mediaDirection+"</font>"));
				} else {
					
					p_test_01_tv_d.setText(Html.fromHtml("<font color='#ff0000'>"+bodyData_down.mediaDirection+"</font>"));
				}
				
	 		 	p_test_02_tv_d.setText(bodyData_down.callDirection);
	 		 	p_test_03_tv_d.setText(bodyData_down.testDate);
	 		
	 		 	p_test_04_tv_d.setText(MossDef.makePhoneNumber(bodyData_down.caller));
	 		 	p_test_05_tv_d.setText(MossDef.makePhoneNumber(bodyData_down.callee));
	 		 	
	 			p_test_06_tv_d.setText(bodyData_down.codecType);
	 		 	p_test_07_tv_d.setText(bodyData_down.callTime);
	 		 	p_test_08_tv_d.setText(bodyData_down.endTime);
	 		 	p_test_09_tv_d.setText(bodyData_down.remoterTpIp);
	 		 	p_test_10_tv_d.setText(bodyData_down.remoterTpIp);
	 		 	p_test_11_tv_d.setText(bodyData_down.txRtp);
	 		 	p_test_12_tv_d.setText(bodyData_down.rxRtp);
	 		 	p_test_13_tv_d.setText(bodyData_down.rtpLoss);
	 		 	p_test_14_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.lossAvg) + " %");
	 		 	p_test_15_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.lossMax)+ " %");
	 		 	p_test_16_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.errCause));
	 		 	p_test_17_tv_d.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_down.jitterMax)) + " ms");
	 		 	p_test_18_tv_d.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_down.jitterAvg)) + " ms");
	 		 	p_test_19_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.jitterLoss)+ " %");
	 		 	p_test_20_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.delayMax)+ " ms");
	 		 	p_test_21_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.delayAvg)+ " ms");
	 		 	p_test_22_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.cqrAvg)+ " 점");
	 		 	p_test_23_tv_d.setText(MossDef.getNullCheckString2(bodyData_down.cqrMin)+ " 점");
	 		 	p_test_24_tv_d.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_down.pesqAvg))+ " 점");
	 		 	p_test_25_tv_d.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_down.pesqMin))+ " 점");
	 		 	p_test_26_tv_d.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_down.cqmosAvg))+ " 점");
	 		 	p_test_27_tv_d.setText(MossDef.getNullCheckString2(String.valueOf(bodyData_down.cqmosMin))+ " 점");
	 		 	 		 	
	 		 	
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
				HttpConnectionParams.setConnectionTimeout(params, MossDef.REGISTRATION_TIMEOUT);
				
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

					data = gson.fromJson(reader, InternetPhoneData_I.class); 					
					
									    
				} else {
					// Closes the connection.
				//	Log.w(LOG_TAG, statusLine.getReasonPhrase());
					response.getEntity().getContent().close();											
					throw new IOException(statusLine.getReasonPhrase());
					
				}
			} catch (Exception e) {
				//Log.w(LOG_TAG, e);
				error = true;
				cancel(true);
			}
			return null;
		}

		protected void onCancelled() {
			
			
			
			if (mProgressDialog != null) {
				  mProgressDialog.dismiss();
				}
		
			Toast.makeText(OrderDetailInternetPhone_I.this, "수신 실패", Toast.LENGTH_SHORT).show();
		}

		protected void onPostExecute(Void unused) {
			
		
			
			if (mProgressDialog != null) {
				  mProgressDialog.dismiss();
				}
			if (error) {
					
				Toast.makeText(OrderDetailInternetPhone_I.this, "수신 실패", Toast.LENGTH_SHORT).show();
			} else {				
				
				
				if (data != null) {
					
					if (data.getResult().size() == 0) {
						condition = MossDef.CONDITION_P;
					} else {
						condition = MossDef.CONDITION_C;
					}
				}
				
				
				showTab();
				changedButton(checkedItem);
				
				
				//setData(checkedItem);
			}
		}
	}
	
	
	 @Override
		public void onDestroy()  {
		 super.onDestroy();
			if (mProgressDialog != null) {
				  mProgressDialog.dismiss();
				}
		 
	 }
public void clickReset(View v) {
		
	if (mProgressDialog != null) {
		mProgressDialog.dismiss();						
	}
		 mProgressDialog = ProgressDialog.show(OrderDetailInternetPhone_I.this,  "", "인터넷전화\n상세정보 조회중입니다.");
		new DetailAsyncTask().execute(detailUrl.toString());			

	}
	
	
   public void runTest(View v){
	   
	   
	    			
			 Intent intent = new Intent(OrderDetailInternetPhone_I.this, VcTest.class);		 
			 intent.putExtra("workOdrNum", workOdrNum);      
			 intent.putExtra("telNum", telNum);   
			 intent.putExtra("workOdrKind", workOdrKind);   
			 intent.putExtra("scn", data.getBody().get(0).scn);
			 intent.putExtra("officesCode", data.getBody().get(0).officescode);
			 intent.putExtra("kornetId", data.getBody().get(0).kornetId);
			 
			 
			
			 
			 startActivity(intent);
			
		     
	   
	   
		
	   
   }
	
	
   @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
						
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if (mProgressDialog != null) {
				  mProgressDialog.dismiss();
				}
			
				Intent in = new Intent(OrderDetailInternetPhone_I.this, OrderList.class);              
				 in.putExtra("condition", condition);        
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
			Intent in = new Intent(OrderDetailInternetPhone_I.this, OrderList.class);              
			 in.putExtra("condition", condition);        
			 in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 
					
			 finish();
		
	}


	

}
