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
import com.kt.moss.qtest.data.BodyData_I;
import com.kt.moss.qtest.data.InternetTvData_I;
import com.kt.moss.qtest.data.InternetTvResultData;
import com.kt.moss.qtest.util.MossDef;



/**
 * 인터넷TV 개통 (진행 및 완료) 정보
 * 
 */
public class OrderDetailInternetTV_I extends Activity implements OnItemSelectedListener, OnCheckedChangeListener {

	
	
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
 	
 	
 	private InternetTvData_I data; 		
 	
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
 	private TextView i_customer_08_tv; // 휴대전화1
 	private TextView i_customer_09_tv; // 휴대전화2	 	
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
 	
 	private TextView t_test_01_tv; // 지연시간 최대
 	private TextView t_test_02_tv; // 지연시간 최소
 	private TextView t_test_03_tv; // 지연시간 평균
 	private TextView t_test_04_tv; // 손실율
	private TextView t_test_05_tv; // 다운로드속도(평균)
 	private TextView t_test_06_tv; // 다운로드속도(최대)
 	private TextView t_test_07_tv; // 다운로드속도(최소)
 	
 	
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
		setContentView(R.layout.order_detail_internet_tv_i);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,	R.layout.custom_detail_title_bar);
		TextView title = (TextView) findViewById(R.id.title_name);
		title.setText("TV");
		
		TextView title_type = (TextView) findViewById(R.id.title_type);
		title_type.setText("개통");
		
		
		
		
		
		Intent intent = getIntent();

		workOdrNum = intent.getStringExtra("workOdrNum");
		workOdrKind = intent.getStringExtra("workOdrKind");
		odrType = intent.getStringExtra("odrType");
		condition = intent.getStringExtra("condition");
					
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
			 mProgressDialog = ProgressDialog.show(OrderDetailInternetTV_I.this,  "", "TV\n상세정보 조회중입니다.");
			new DetailAsyncTask().execute(detailUrl.toString());
			
	 }
	
	
	
	private void Init() {
		
		
		p_layout = (LinearLayout)findViewById(R.id.p_layout);
		c_layout = (LinearLayout)findViewById(R.id.c_layout);
		
		  summaryInfo_layout = (LinearLayout)findViewById(R.id.summaryInfo_layout);
		  customerInfo_layout = (LinearLayout)findViewById(R.id.customerInfo_layout);
		  fmInfo_layout = (LinearLayout)findViewById(R.id.fmInfo_layout);	
			testInfo_layout = (LinearLayout)findViewById(R.id.testInfo_layout);	
		
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
			 BodyData_I  bodyData =  data.getBody().get(0);
			 
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
			 		 
			else if (value == 3) {
				
				    arraylist = new ArrayList<String>();
				
				for(InternetTvResultData  resultData : data.getResult() ) {
					
					
					arraylist.add(resultData.testDate);
				}
				
				
			
			
				adapter = new ArrayAdapter<String>(this,  R.layout.spinner_item, arraylist);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			 
				
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
			// TODO Auto-generated method stub
			
		
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


        //메소드는 메소드명 그대로 아무것도 선택되지 않았을때 호출되는 메소드입니다. 
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

					data = gson.fromJson(reader, InternetTvData_I.class); 					
					
					
				    
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
			
			Toast.makeText(OrderDetailInternetTV_I.this, "수신 실패", Toast.LENGTH_SHORT).show();
		}

		protected void onPostExecute(Void unused) {
			
			
			
			if (mProgressDialog != null) {
				  mProgressDialog.dismiss();
				}
			
			
			if (error) {
				
				Toast.makeText(OrderDetailInternetTV_I.this, "수신 실패", Toast.LENGTH_SHORT).show();
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
				 
				Intent in = new Intent(OrderDetailInternetTV_I.this, OrderList.class);              
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
		   mProgressDialog = ProgressDialog.show(OrderDetailInternetTV_I.this,  "", "TV\n상세정보 조회중입니다.");
	   	
	   		new DetailAsyncTask().execute(detailUrl.toString());			

	   	}
	   
	
	public void clickBack(View v) {
		if (mProgressDialog != null) {
			  mProgressDialog.dismiss();
			}
		
		Intent in = new Intent(OrderDetailInternetTV_I.this, OrderList.class);              
		 in.putExtra("condition", condition);        
		 in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 
		 			
		 finish();


	}




}
