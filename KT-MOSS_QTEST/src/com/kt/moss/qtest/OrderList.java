package com.kt.moss.qtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kt.moss.qtest.data.WorkOrderBodyData;
import com.kt.moss.qtest.data.WorkOrderData;
import com.kt.moss.qtest.util.MossDef;

public class OrderList extends Activity implements OnCheckedChangeListener {


	private ArrayList<WorkOrderBodyData> orderList = new ArrayList<WorkOrderBodyData>();	

	private WorkOrderData workOrderData = new WorkOrderData();

	private MyCustomAdapter dataAdapter;	

	private ArrayAdapter<CharSequence> adspin;

	private boolean mInitSpinner;

	private Spinner combo_spin;

	private String selectedOrderKind = "전체";	


	private ArrayAdapter<CharSequence> adspin_type;

	private boolean mInitSpinner_type;

	private Spinner combo_spin_type;

	private String selectedType = "전체";	



	private ListView listView = null;	 	

	private RadioGroup radioGroup;

	String condition = MossDef.CONDITION_P;

	SharedPreferences settings;     
	private String user_id = "";

	static final int DIALOG_EXIT = 0;

	private TextView title;

	ProgressDialog  mProgressDialog  = null;

	RadioButton p_btn = null;
	RadioButton c_btn = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);		

		setContentView(R.layout.order_list);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,	R.layout.custom_title_bar);
		title = (TextView) findViewById(R.id.title_name);		
		title.setText("품질측정");	

		p_btn  = (RadioButton) findViewById(R.id.p_btn);
		c_btn  = (RadioButton) findViewById(R.id.c_btn);

		radioGroup = (RadioGroup) findViewById(R.id.group_grp);
		radioGroup.setOnCheckedChangeListener(this);		 

		Intent intent = getIntent();

		String conditionValue = intent.getStringExtra("condition");

		if (conditionValue == null) {

			condition = MossDef.CONDITION_P;

		} else {

			condition = conditionValue;

			if (condition.equals(MossDef.CONDITION_P)) {
				p_btn.setChecked(true);
			}else {
				c_btn.setChecked(true);
			}
		}

		//개통 수리에 관한 스피너
		combo_spin = (Spinner) findViewById(R.id.state_combo_spin_list);	 

		adspin = ArrayAdapter.createFromResource(this, R.array.state_array_input, R.layout.spinner_item_fillter);
		adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);				

		combo_spin.setAdapter(adspin);		
		combo_spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

				if (mInitSpinner == false) {
					mInitSpinner = true;
					return;
				}

				selectedOrderKind = adspin.getItem(position).toString();

				getWorkList(condition, selectedOrderKind, selectedType);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}					

		});


		//종류에 따른 스피너	
		combo_spin_type = (Spinner) findViewById(R.id.type_combo_spin_list);	 

		adspin_type = ArrayAdapter.createFromResource(this, R.array.type_array_input, R.layout.spinner_item_fillter);
		adspin_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);				

		combo_spin_type.setAdapter(adspin_type);		
		combo_spin_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

				if (mInitSpinner_type == false) {
					mInitSpinner_type = true;
					return;
				}

				selectedType = adspin_type.getItem(position).toString();

				getWorkList(condition, selectedOrderKind, selectedType);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}					

		});


		dataAdapter = new MyCustomAdapter(this, R.layout.orderlist_row,	orderList);	
		listView = (ListView) findViewById(R.id.orderlistView);		
		listView.setAdapter(dataAdapter);						

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {

				String workOdrKind = ((TextView) view.findViewById(R.id.workOdrKind)).getText().toString();  
				String odrType = ((TextView) view.findViewById(R.id.odrType)).getText().toString(); 
				String telNum = ((TextView) view.findViewById(R.id.telNum)).getText().toString(); 
				String workOrderNumber  = orderList.get(position).workOdrNum;
				String custName = orderList.get(position).custName;

				String telNum_phone = orderList.get(position).telNum;

				Intent in = null;

				if (odrType.equals(MossDef.ORDERTYPE_I_STR)) {

					if (workOdrKind.equals(MossDef.WORKORDERKIND_I_STR)) { // 개통
						in = new Intent(OrderList.this, OrderDetailInternet_I.class);   
					} else { // 수리
						in = new Intent(OrderList.this, OrderDetailInternet_R.class);   
					}									         

					in.putExtra("custName", custName);  // 고객명
					in.putExtra("telNum", telNum);  // 전화번호
					in.putExtra("telNum_original", telNum_phone);  // 필터링 전 전화번호
				}

				else if (odrType.equals(MossDef.ORDERTYPE_P_STR)) {

					if (workOdrKind.equals(MossDef.WORKORDERKIND_I_STR)) {
						in = new Intent(OrderList.this, OrderDetailInternetPhone_I.class);   
					} else { // 수리
						in = new Intent(OrderList.this, OrderDetailInternetPhone_R.class);   
					}					

					in.putExtra("telNum", telNum_phone);  // 전화번호

				} else  { 

					if (workOdrKind.equals(MossDef.WORKORDERKIND_I_STR)) {
						in = new Intent(OrderList.this, OrderDetailInternetTV_I.class);   
					} else {
						in = new Intent(OrderList.this, OrderDetailInternetTV_R.class);   
					}					
				}

				in.putExtra("workOdrNum", workOrderNumber);        
				in.putExtra("workOdrKind", workOdrKind); 
				in.putExtra("odrType", odrType); 			
				in.putExtra("condition", condition);  // 진행, 완료 여부

				startActivity(in); 

			}
		});

	}


	@Override//재시작
	public void onResume() {

		super.onResume();

		settings = getSharedPreferences(MossDef.PREFS_NAME, Context.MODE_PRIVATE);   		
		user_id = settings.getString(MossDef.USER_ID, ""); 	

		if (mProgressDialog != null) {
			mProgressDialog.dismiss();						
		}

		mProgressDialog = ProgressDialog.show(OrderList.this,  "", "목록 조회중입니다.");

		new OrderListAsyncTask().execute(MossDef.LIST_INFO_URL + user_id);	
		//객체 = (클래스캐스팅)new 클래스 생성자().execute(첫 제네릭);
	}

	//리셋
	public void clickReset(View v) {


		settings = getSharedPreferences(MossDef.PREFS_NAME, Context.MODE_PRIVATE);   		
		user_id = settings.getString(MossDef.USER_ID, ""); 

		if (mProgressDialog != null) {
			mProgressDialog.dismiss();						
		}
		mProgressDialog = ProgressDialog.show(OrderList.this,  "", "목록 조회중입니다.");

		new OrderListAsyncTask().execute(MossDef.LIST_INFO_URL + user_id);				

	}


	/**
	 * 	if (orderData.odrType.equals(MossDef.ORDERTYPE_I_STR)) {
				holder.icon_image.setImageResource(R.drawable.icon_internet);
			} else if (orderData.odrType.equals(MossDef.ORDERTYPE_P_STR)) {
				holder.icon_image.setImageResource(R.drawable.icon_phone);
			} else {
				holder.icon_image.setImageResource(R.drawable.icon_tv);
			}
	 * @param condition
	 * @param workOdrKind
	 * @param odrType
	 */

	private void getWorkList(String condition, String workOdrKind, String odrType) {
		//                           (진행/완료)         (전체/개통/수리)        (전체/인터넷/전화/Tv)


		orderList.clear();
		ArrayList<WorkOrderBodyData> list = workOrderData.getBody();

		ArrayList<WorkOrderBodyData> returnValue = new ArrayList<WorkOrderBodyData> ();

		for (WorkOrderBodyData workOrderBodyData : list) {

			if (workOdrKind.equals("전체")) {					


				if (odrType.equals("전체")) {
					//inprogress ="상태값"(진행/완료)
					if (workOrderBodyData.inprogress.equals(condition) ) {						
						returnValue.add(workOrderBodyData);
					}
				} else {


					if (workOrderBodyData.odrType.equals(odrType)  && workOrderBodyData.inprogress.equals(condition)) {						
						returnValue.add(workOrderBodyData);
					}	


				}




			} else {					

				if (odrType.equals("전체")) {
					if (workOrderBodyData.inprogress.equals(condition) &&  workOrderBodyData.workOdrKind.trim().equals(workOdrKind)) {	

						returnValue.add(workOrderBodyData);
					}

				} else {

					if (workOrderBodyData.inprogress.equals(condition) 
							&&  workOrderBodyData.workOdrKind.trim().equals(workOdrKind) 
							&& 	workOrderBodyData.odrType.equals(odrType)) {						
						returnValue.add(workOrderBodyData);
					}

				}


			}

		}		

		orderList.addAll(returnValue);				


		if (dataAdapter != null) {
			// 리스트의 데이터가 변경이 완료 되는 시점
			dataAdapter.notifyDataSetChanged();		

			if (orderList.size() == 0) {				
				alertShow("조회결과가 없습니다.");   				  
			}

		}


	}






	@Override//키보드자판을 누르는 순간에 발생하는 이벤트
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(DIALOG_EXIT);
			return true;	        
		}else {
			return super.onKeyDown(keyCode, event);
		}	
	}


	protected Dialog onCreateDialog(int id) { 
		AlertDialog.Builder builder; 
		AlertDialog alertDialog; 


		switch(id) { 


		case DIALOG_EXIT: 

			builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle("Exit");	    	

			builder.setMessage(R.string.dialog_exit);
			builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					finish();
					moveTaskToBack(true);
					android.os.Process.killProcess(android.os.Process.myPid() ); 
				}
			});
			builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});

			alertDialog = builder.create();
			break;

		default: 
			alertDialog = null; 
		} 	    

		return alertDialog; 
	}


	private void alertShow(String msg) {
		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(this);
		alert_internet_status.setTitle("알림");
		alert_internet_status.setMessage(msg);
		alert_internet_status.setPositiveButton("닫기",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); // 닫기
				//팝업창 생성시 사라지면 login화면으로 돌아가기위해 삽입(140213)
				//						Intent intn = new Intent(OrderList.this,Login.class);
				//						startActivity(intn);
				return ;
			}
		});
		alert_internet_status.show();

	}	 

	@Override	
	public void onCheckedChanged(RadioGroup group, int checkedId) {       
		// TODO Auto-generated method stub

		switch(checkedId){       

		case R.id.p_btn://진행버튼			
			condition = MossDef.CONDITION_P;			
			getWorkList(condition, selectedOrderKind, selectedType);
			break;

		case R.id.c_btn://완료버튼			
			condition = MossDef.CONDITION_C;
			getWorkList(condition, selectedOrderKind, selectedType);
			break;

		default:				
			break;
		}

	}



	private class MyCustomAdapter extends ArrayAdapter<WorkOrderBodyData> {

		public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<WorkOrderBodyData> orderList) {

			super(context, textViewResourceId, orderList);
		}


		private class ViewHolder {			
			TextView telNum;
			TextView workOdrKind;
			TextView odrType;
			TextView addr;	
			TextView custName;
			TextView rsvDT;		
			ImageView icon_image;
		}



		@Override
		public View getView(int position, View convertView, ViewGroup parent) {



			ViewHolder holder = null;

			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = vi.inflate(R.layout.orderlist_row, null);

				holder = new ViewHolder();

				holder.telNum = (TextView) convertView.findViewById(R.id.telNum);
				holder.workOdrKind = (TextView) convertView.findViewById(R.id.workOdrKind);				
				holder.odrType = (TextView) convertView.findViewById(R.id.odrType);
				holder.addr = (TextView) convertView.findViewById(R.id.addr);				
				//	holder.addrEtc = (TextView) convertView.findViewById(R.id.addrEtc);
				holder.custName = (TextView) convertView.findViewById(R.id.custName);
				holder.rsvDT = (TextView) convertView.findViewById(R.id.rsvDT);

				holder.icon_image = (ImageView) convertView.findViewById(R.id.icon_image);

				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();

			}


			WorkOrderBodyData orderData = orderList.get(position);		


			if (orderData.workOdrKind.trim().equals("개통")) {				
				holder.workOdrKind.setText(Html.fromHtml("<font color='#0000ff'>["+orderData.workOdrKind.trim()+"]</font>"));	            	
			} else {
				holder.workOdrKind.setText(Html.fromHtml("<font color='#ff0000'>["+orderData.workOdrKind.trim()+"]</font>"));
			}

			holder.telNum.setText(MossDef.makePhoneNumber(orderData.telNum.trim()));		
			holder.odrType.setText(orderData.odrType.trim());		

			if (orderData.odrType.equals(MossDef.ORDERTYPE_I_STR)) {
				holder.icon_image.setImageResource(R.drawable.icon_internet);
			} else if (orderData.odrType.equals(MossDef.ORDERTYPE_P_STR)) {
				holder.icon_image.setImageResource(R.drawable.icon_phone);
			} else {
				holder.icon_image.setImageResource(R.drawable.icon_tv);
			}

			holder.addr.setText(MossDef.getNullCheckString2(orderData.addr) + MossDef.getNullCheckString2(orderData.addrEtc));		
			holder.custName.setText(orderData.custName);
			holder.rsvDT.setText(orderData.rsvDT.substring(0, 16));
			//날짜 시간의 데이터 길이가 16자리 이하일시 exception예외처리(140213)
			//			try{
			//				if(orderData.rsvDT.length() > 16 )
			//					holder.rsvDT.setText(orderData.rsvDT.substring(0, 16));
			//				else throw new  StringIndexOutOfBoundsException("에러");
			//			} catch(StringIndexOutOfBoundsException e) {
			//
			//				alertShow("데이터오류.");
			//			}
			return convertView;

		}
	}


	@Override
	public void onDestroy()  {
		super.onDestroy();
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}

	}

	//ui에 관한 작업에 대해 쉽게 멀티스레딩이 가능하게 해줌
	private class OrderListAsyncTask extends AsyncTask<String, Void, Void> {



		private final HttpClient httpclient = new DefaultHttpClient();
		final HttpParams params = httpclient.getParams();
		private boolean error = false;


		// 백그라운드 쓰레드에서 실행,inputparameter가 이 메소드로 전달(계산 작업을 다룸)
		protected Void doInBackground(String... urls) {



			String URL = null;
			InputStream is = null;
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
					System.out.println(reader.toString());
					workOrderData = gson.fromJson(reader, WorkOrderData.class); 					

					orderList.clear();
					orderList.addAll(workOrderData.getBody());

				} else {

					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				error = true;
				cancel(true);  //멈춤
			}
			return null;
		}





		protected void onCancelled() {



			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}


			Toast.makeText(OrderList.this, "데이터 수신 실패", Toast.LENGTH_SHORT).show();
		}

		protected void onPostExecute(Void unused) {


			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}


			if (error) {

				Toast.makeText(OrderList.this, "데이터 수신 실패", Toast.LENGTH_SHORT).show();
			} else {				

				getWorkList(condition, selectedOrderKind, selectedType);
			}
		}


	}
}
