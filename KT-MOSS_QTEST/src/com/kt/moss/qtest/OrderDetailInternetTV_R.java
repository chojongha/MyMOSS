package com.kt.moss.qtest;

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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
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
import com.kt.moss.qtest.data.BodyData_R;
import com.kt.moss.qtest.data.InternetTvData_R;
import com.kt.moss.qtest.data.InternetTvResultData;
import com.kt.moss.qtest.util.MossDef;



/**
 * 인터넷TV 수리 (진행 및 완료) 정보
 * 
 * @author jhkim
 *
 */
public class OrderDetailInternetTV_R extends Activity implements OnItemSelectedListener, OnCheckedChangeListener {

	
	
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
 	
 	
 	private InternetTvData_R data;
 	
 	
 	
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
 
 	
 	private TextView t_test_01_tv; // 지연시간 최대
 	private TextView t_test_02_tv; // 지연시간 최소
 	private TextView t_test_03_tv; // 지연시간 평균
 	private TextView t_test_04_tv; // 손실율
	private TextView t_test_05_tv; // 다운로드속도(평균)
 	private TextView t_test_06_tv; // 다운로드속도(최대)
 	private TextView t_test_07_tv; // 다운로드속도(최소)
 	private StringBuffer detailUrl = null;
 	
 	
	private ArrayList<String> arraylist = null;
 	private ArrayAdapter<String> adapter = null;  
 	private Spinner sp;
	ProgressDialog  mProgressDialog  = null;
 	
 	// http://192.168.0.9:8080/moss/WorkOdrDetail?workOdrNum=1O2011090512020&workOdrKind=I&odrType=I
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		

		// 화면 구성하기
		setContentView(R.layout.order_detail_internet_tv_r);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,	R.layout.custom_detail_title_bar);
		TextView title = (TextView) findViewById(R.id.title_name);
		title.setText("TV");
		
		TextView title_type = (TextView) findViewById(R.id.title_type);
		title_type.setText("수리");
		
		
		
		
		
		Intent intent = getIntent();

		workOdrNum = intent.getStringExtra("workOdrNum");
		workOdrKind = intent.getStringExtra("workOdrKind");
		odrType = intent.getStringExtra("odrType");
		condition = intent.getStringExtra("condition");
			
	}
	
	 @Override
		public void onDestroy()  {
		 super.onDestroy();
			if (mProgressDialog != null) {
				  mProgressDialog.dismiss();
				}
		 
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
				mProgressDialog = ProgressDialog.show(OrderDetailInternetTV_R.this,  "", "TV\n상세정보 조회중입니다.");
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
			
 		
 			sp = (Spinner) this.findViewById(R.id.spinner);
 		 	t_test_01_tv  = (TextView)findViewById(R.id.t_test_01_tv);
 		 	t_test_02_tv  = (TextView)findViewById(R.id.t_test_02_tv);
 		 	t_test_03_tv  = (TextView)findViewById(R.id.t_test_03_tv);
 		 	t_test_04_tv  = (TextView)findViewById(R.id.t_test_04_tv);
 		 	t_test_05_tv  = (TextView)findViewById(R.id.t_test_05_tv);
 		 	t_test_06_tv  = (TextView)findViewById(R.id.t_test_06_tv);
 		 	t_test_07_tv  = (TextView)findViewById(R.id.t_test_07_tv);
			
  		 			
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
			 BodyData_R  bodyData =  data.getBody().get(0);
			 
			 if (value == 0) {			
				 r_summary_01_tv.setText(bodyData.workOdrNum);			 
				 r_summary_02_tv.setText(Html.fromHtml("<font color='#ff0000'>"+MossDef.getConditionStr(condition)+"</font>")); 
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
			 		 
			else if (value == 3) {			
					
				
				
				
				
				for(InternetTvResultData  resultData : data.getResult() ) {
					
					arraylist = new ArrayList<String>();
					arraylist.add(resultData.testDate);
				}
				
				
				
				adapter = new ArrayAdapter<String>(this,  R.layout.spinner_item, arraylist);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				
			
				//adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraylist); 
				
				
				sp.setAdapter(adapter);
				sp.setOnItemSelectedListener(this);
						

				InternetTvResultData  resultData =	data.getResult().get(0);
			
				t_test_01_tv.setText(resultData.pingSpeedMax + " ms");
				t_test_02_tv.setText(resultData.pingSpeedMin + " ms");
				t_test_03_tv.setText(resultData.pingSpeedAvg + " ms");
				t_test_04_tv.setText(resultData.pingPacketLossCnt + " %");
			
				
				t_test_05_tv.setText(MossDef.toCommifyString(resultData.ftpDownSpeedAvg) + " kbps");
				t_test_06_tv.setText(MossDef.toCommifyString(resultData.ftpDownSpeedMax)+ " kbps");
				t_test_07_tv.setText(MossDef.toCommifyString(resultData.ftpDownSpeedMin)+ " kbps");
			}
		}
	
		
	 }
	 
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			//Toast.makeText(this, arraylist.get(arg2), Toast.LENGTH_LONG).show();
			
			InternetTvResultData  resultData =	data.getResult().get(arg2);
			
			t_test_01_tv.setText(resultData.pingSpeedMax + " ms");
			t_test_02_tv.setText(resultData.pingSpeedMin + " ms");
			t_test_03_tv.setText(resultData.pingSpeedAvg + " ms");
			t_test_04_tv.setText(resultData.pingPacketLossCnt + " %");
			t_test_05_tv.setText(MossDef.toCommifyString(resultData.ftpDownSpeedAvg) + " kbps");
			t_test_06_tv.setText(MossDef.toCommifyString(resultData.ftpDownSpeedMax)+ " kbps");
			t_test_07_tv.setText(MossDef.toCommifyString(resultData.ftpDownSpeedMin)+ " kbps");
			
			
		}



		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
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

					data = gson.fromJson(reader, InternetTvData_R.class); 					
					
					
				    
				} else {
					// Closes the connection.
					//Log.w(LOG_TAG, statusLine.getReasonPhrase());
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
			
			Toast.makeText(OrderDetailInternetTV_R.this, "수신 실패", Toast.LENGTH_SHORT).show();
		}

		protected void onPostExecute(Void unused) {
			
			
			if (mProgressDialog != null) {
				  mProgressDialog.dismiss();
				}
			
			if (error) {
				
				Toast.makeText(OrderDetailInternetTV_R.this, "수신 실패", Toast.LENGTH_SHORT).show();
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
			}
		}
	}
	
	

	   @Override
			public boolean onKeyDown(int keyCode, KeyEvent event) {
								
				if(keyCode == KeyEvent.KEYCODE_BACK) {
					if (mProgressDialog != null) {
						  mProgressDialog.dismiss();
						}
					Intent in = new Intent(OrderDetailInternetTV_R.this, OrderList.class);              
					 in.putExtra("condition", condition);        
					 in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);	
					 finish();

			        return true;	        
			    }else {
			        return super.onKeyDown(keyCode, event);
			    }	
			}
	
	
	   public void clickReset(View v) {
	   		
		   if (mProgressDialog != null) {
				  mProgressDialog.dismiss();
				}
	   	 mProgressDialog = ProgressDialog.show(OrderDetailInternetTV_R.this,  "", "TV\n상세정보 조회중입니다.");
	   	
	   		new DetailAsyncTask().execute(detailUrl.toString());			

	   	}
	   
	
	public void clickBack(View v) {
		
		if (mProgressDialog != null) {
			  mProgressDialog.dismiss();
			}
		Intent in = new Intent(OrderDetailInternetTV_R.this, OrderList.class);              
		 in.putExtra("condition", condition);        
		 in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		
		 finish();


	}

	

}
