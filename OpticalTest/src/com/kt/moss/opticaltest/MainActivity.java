package com.kt.moss.opticaltest;

import com.kt.moss.opticaltest.BluetoothService.workStatus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DiscretePathEffect;
import android.util.Log;
import android.util.Property;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//====================================================
// 옵티컬 파워 측정용 액티비티
//
// 본 액티비티는 스스로 기동 할 수도 있지만,
// 상위의 어플리케이션에서 호출되면 불루투스 장치를 통해 광파워를 측정하고
// 측정 결과를 서버에 전송하는 것을 목적으로 한다.
// (사실 상위에 앱이 존재 하므로 측정값만 넘겨 받아서 스스로 보고 하는것이
//  편할텐데 왜 이리 설계 했는지 의심스럽지만)
// 또 블루투스 장치의 스트림 오픈과정에서 장치의 상태에 따라
// 적지 않은 시간이 소요되기도 하고, 연결된 장치가
// 거리나 여러 이유로 영구적인 연결 상태를 보장하지 않으므로
// 액티비티가 활성화 상태일 onResume 이후에 주요한 활동을 처리하고
// 백그라운드로 내려가는 onPause 상태에서 스스로 종료한다.
// 따라서 측정 중에 전화, 문자 등의 여러 이유로 다른 액티비티가 활성화 되면
// 본 액티비티는 종료 되므로 새롭게 시작하여야 한다.
//
// 2013-11-09 최종 수정 by
//   SOLME Programmer 김영일 (mademlm@gmail.com) 
//====================================================
public class MainActivity extends Activity implements OnClickListener {

	// 디버그용 태그
	private static final String TAG = "OpticalTest";
	
	// 스크린 객체 참조 (TextView)
	private TextView mTextTitle;
	private TextView mTextMessage;
	private ImageView mImageBattery;
	private TextView mText_WorkOrderNum;
	private TextView mText_CustName;
	private TextView mText_TelNum;
	private TextView mText_Lambda;
	private TextView mText_Power;
	
	// 스크린 객체 참조 (Button)
	private Button mButton_Exit;
	private ImageView mButton_Power;
	private ImageView mButton_Lambda;
	private ImageView mButton_Select;
	private Button mButton_Send;
	
	// 내부 사용 변수
	private String mWorkOdrNum;
	private String mCustName;
	private String mTelNum;
	private static final String mPostAddress = "http://moss.kt.com/moss/OpticTestResult";
	
    // 블루투스 서비스 클래스
    private BluetoothService mBluetoothService = null;
    
    // 백그라운드 전환시 종료 플래그
    private boolean mExitActivity = true;
    
    private final String INIT_LAMBDA = "---- nm";
    private final String INIT_POWER = "--.-- dBm";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "OnCreate");
		
		// 타이틀 영역을 사용자 정의한다.
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cust_title);

		// 화면에 사용된 모든 객체 참조
		mTextTitle = (TextView) findViewById(R.id.text_title);
		mImageBattery = (ImageView) findViewById(R.id.image_batt);
		mText_WorkOrderNum = (TextView) findViewById(R.id.text_workOrdNum);
		mText_CustName = (TextView) findViewById(R.id.text_custName);
		mText_TelNum = (TextView) findViewById(R.id.text_telNum);
		mText_Lambda = (TextView) findViewById(R.id.text_lambda);
		mText_Power = (TextView) findViewById(R.id.text_power);
		mTextMessage = (TextView) findViewById(R.id.text_Message);
		
		mButton_Exit = (Button) findViewById(R.id.button_exit);
		mButton_Power = (ImageView) findViewById(R.id.button_Power);
		mButton_Lambda = (ImageView) findViewById(R.id.button_Lambda);
		mButton_Select = (ImageView) findViewById(R.id.button_Select);
		mButton_Send = (Button) findViewById(R.id.button_Send);
		
		mButton_Exit.setOnClickListener(this);
		mButton_Power.setOnClickListener(this);
		mButton_Lambda.setOnClickListener(this);
		mButton_Select.setOnClickListener(this);
		mButton_Send.setOnClickListener(this);

		// 외부에서 호출할때 넘겨준 파라미터 확인
		Intent mIntent = getIntent();
		mWorkOdrNum = mIntent.getStringExtra("workOdrNum");
		mCustName = mIntent.getStringExtra("custName");
		mTelNum = mIntent.getStringExtra("telNum");
		//mPostAddress = mIntent.getStringExtra("postAddress");
		Log.d(TAG, "전송주소:" + mPostAddress);

		// 창 제목 표시
		mTextTitle.setText(R.string.win_title);
		
		// 오더번호, 고객명칭, 전화번호 표시
		mText_WorkOrderNum.setText(mWorkOdrNum == null ? "임의측정" : mWorkOdrNum);
		mText_CustName.setText(mCustName == null ? "없음" : mCustName);
		mText_TelNum.setText(mTelNum == null ? "없음" : mTelNum);

		// 측정값 초기화
		mText_Lambda.setText(INIT_LAMBDA);
		mText_Power.setText(INIT_POWER);

		// 블루투스 서비스 생성
		mBluetoothService = new BluetoothService(this, mHandler);
	}

	// 화면의 각종 버튼 객체를 활성화 또는 비활성화 한다.
	private void screenEnable(boolean enable) {
		mButton_Power.setEnabled(enable);
		mButton_Lambda.setEnabled(enable);
		mButton_Select.setEnabled(enable);
		// 자체 테스팅의 경우 전송(Send) 버튼을 비 활성화 한다.
		if (enable && (mWorkOdrNum != null) && (mPostAddress != null)) {
			mButton_Send.setEnabled(true);
		} else {
			mButton_Send.setEnabled(false);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "OnStart");
		// 화면의 버튼을 비활성화 시킨다.
		screenEnable(false);
		// 블루투스 서비스를 시작한다.
		mBluetoothService.startWork();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "OnResume");
		mExitActivity = true;
		if (mBluetoothService.currentStatus == workStatus.Connect_Complete || mBluetoothService.currentStatus == workStatus.Running) {
			screenEnable(true);
		}
		super.onResume();
	}

	private boolean mPowerFlag = false;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_exit:
			Log.d(TAG, "종료 버튼 눌림");
			checkExit();
			break;
		case R.id.button_Power:
			Log.d(TAG, "Power 버튼 눌림");
			mPowerFlag = !mPowerFlag;
			mButton_Power.setImageResource(mPowerFlag ? R.drawable.button_power_on : R.drawable.button_power_off);
			mBluetoothService.sendSerial(mPowerFlag ? 'o' : 'x');
			break;
		case R.id.button_Lambda:
			Log.d(TAG, "Lambda 버튼 눌림");
			mBluetoothService.sendSerial('l');
			break;
		case R.id.button_Select:
			Log.d(TAG, "Select 버튼 눌림");
			mBluetoothService.sendSerial('r');
			break;
		case R.id.button_Send:
			Log.d(TAG, "Send 버튼 눌림");
			String mLambda = mText_Lambda.getText().toString();
			String mPower = mText_Power.getText().toString();
			if (mLambda.equals(INIT_LAMBDA)) {
				ShowMessage("파장값이 측정되지 않았습니다.");
			} else if (mPower.equals(INIT_POWER)) {
				ShowMessage("파워값이 측정되지 않았습니다.");				
			} else {
				mLambda = mLambda.split(" ")[0];
				mPower = mPower.split(" ")[0];
				mButton_Send.setEnabled(false);
				mBluetoothService.sendPost(mPostAddress, mWorkOdrNum, mLambda, mPower, "0");
			}
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		checkExit();
	}

	// 토스트 메시지의 레이아웃을 사용자 정의하여 보여준다.
	private void ShowMessage(CharSequence msg) {
		View layout = getLayoutInflater().inflate(R.layout.toast_border, (ViewGroup) findViewById(R.id.toast_layout_root));
		TextView text = (TextView) layout.findViewById(R.id.text_toast);
		Toast toastView = new Toast(this);
		text.setText(msg);
		toastView.setGravity(Gravity.CENTER, 0, 0);
		toastView.setDuration(Toast.LENGTH_LONG);
		toastView.setView(layout);
		toastView.show();
	}

	
	// 서비스에서 각종 상태 보고
	private void report(CharSequence msg) {
		mTextMessage.setText(msg);
	}
	
	// BACK 버튼이나, 종료 버튼에 의한 종료 확인
	private void checkExit() {
		AlertDialog dialog = new AlertDialog.Builder(this)
		.setTitle("안내")
		.setMessage("종료하시겠습니까?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("예", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mBluetoothService != null) {
					mBluetoothService.stop();
				}
				finish();
			}})
		.setNegativeButton("아니오", mClick).create();
		dialog.show();
	}
	
	// 서버스 진행중 치명적인 오류로 메시지 확인 후 종료
	private void errorExit(CharSequence msg) {
		AlertDialog dialog = new AlertDialog.Builder(this)
			.setTitle("안내")
			.setMessage(msg)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mBluetoothService.stop();
					finish();
				}})
			.create();
		dialog.show();
	}
	
	// 장치 찾기 실패시 재시도 여부 확인
	private void findErrorRetryCheck() {
		AlertDialog dialog = new AlertDialog.Builder(this)
		.setTitle("안내")
		.setMessage("파워미터를 찾을 수 없습니다.\n다시 검색 하시겠습니까?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("재시도", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mBluetoothService.currentStatus = workStatus.None;
				mBluetoothService.startWork();
			}
		})
		.setNegativeButton("종료", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mBluetoothService.stop();
				finish();
			}
		})
		.create();
		dialog.show();
	}

	// 연결 오류에 대한 재시도 여부 확인
	private void connectErrorRetryCheck() {
		AlertDialog dialog = new AlertDialog.Builder(this)
		.setTitle("안내")
		.setMessage("파워미터와 연결에 문제가 있습니다.\n다시 시도 하시겠습니까?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("재시도", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mBluetoothService.currentStatus = workStatus.None;
				mBluetoothService.startWork();
			}
		})
		.setNegativeButton("종료", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mBluetoothService.stop();
				finish();
			}
		})
		.create();
		dialog.show();
	}

//	// 장치의 페어링이 완료됨
//	public synchronized void onBondedDevice() {
//		Log.d(TAG, "onBondedDevice");
//		mExitActivity = true;
//		mBluetoothService.connect();
//	}
//	
//	// 장치의 페어링이 해제됨
//	public synchronized void onUnbondDevice() {
//		Log.d(TAG, "onUnbondDevice");
//		setupMeasure();
//	}
	
	
	// 블루투스 클래스와 통신
	public static final int MESSAGE_BLUETOOTH_ENABLED = 11;
	public static final int MESSAGE_BOND_DEVICE_FINDING = 13;
	public static final int MESSAGE_BOND_DEVICE_FINDED = 16;
	public static final int MESSAGE_UNBOND_DEVICE_FINDING = 19;
	public static final int MESSAGE_UNBOND_DEVICE_FINDED = 22;
	public static final int MESSAGE_FIND_ERROR = 25;
	public static final int MESSAGE_DEVICE_BONDED = 28;
	public static final int MESSAGE_DEVICE_UNBONDED = 31;
	
	public static final int MESSAGE_CONNECT_START = 41;
	public static final int MESSAGE_CONNECT_COMPLETE = 43;
	public static final int MESSAGE_CONNECT_ERROR = 45;
	
	public static final int MESSAGE_SEND = 61;
	public static final int MESSAGE_RECV = 63;
	
	public static final int MESSAGE_ERROR = 81;
	// 웹전송 클래스와 통신
	public static final int MESSAGE_POST_OK = 91;
	public static final int MESSAGE_POST_ERR = 95;

	private boolean mIsBond = false;

	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_BLUETOOTH_ENABLED:
				Log.d(TAG, "MESSAGE_BLUETOOTH_ENABLED");
				mBluetoothService.startWork();
				break;
			case MESSAGE_BOND_DEVICE_FINDING:
				Log.d(TAG, "MESSAGE_BOND_DEVICE_FINDING");
				report("페어링된 파워미터 검색중.");
				break;
			case MESSAGE_BOND_DEVICE_FINDED:
				Log.d(TAG, "MESSAGE_BOND_DEVICE_FINDED");
				report("페어링된 파워미터를 찾았습니다.");
				mIsBond = true;
				break;
			case MESSAGE_UNBOND_DEVICE_FINDING:
				Log.d(TAG, "MESSAGE_UNBOND_DEVICE_FINDING");
				report("페어링되지 않은 파워미터 검색중");
				mBluetoothService.startWork();
				break;
			case MESSAGE_UNBOND_DEVICE_FINDED:
				Log.d(TAG, "MESSAGE_UNBOND_DEVICE_FINDED");
				report("페어링되지 않은 파워미터를 찾았습니다.");
				mExitActivity = false;
				mBluetoothService.startWork();
				break;
			case MESSAGE_FIND_ERROR:
				Log.d(TAG, "MESSAGE_FIND_ERROR");
				report("파워미터 검색 실패");
				findErrorRetryCheck();
				break;
			case MESSAGE_DEVICE_BONDED:
				Log.d(TAG, "MESSAGE_DEVICE_BONDED");
				report("파워미터를 페어링 했습니다.");
				mBluetoothService.startWork();
				break;
			case MESSAGE_DEVICE_UNBONDED:
				Log.d(TAG, "MESSAGE_DEVICE_UNBONDED");
				report("파워미터를 페어링 해제 했습니다.");
				mBluetoothService.startWork();
				break;
			case MESSAGE_CONNECT_START:
				mExitActivity = false;
				report("파워미터와 연결을 시작합니다.");
				break;
			case MESSAGE_CONNECT_COMPLETE:
				mExitActivity = true;
				report("파워미터와 연결이 완료되었습니다.");
				Toast.makeText(MainActivity.this, "측정을 시작하세요!",  Toast.LENGTH_SHORT).show();
				// 화면을 활성화 한다.
				screenEnable(true);
				break;
			case MESSAGE_CONNECT_ERROR:
				Log.d(TAG, "CONNECT_ERROR");
				if (mIsBond == true) {
					mIsBond = false;
					//mTextMessage.setText(R.string.bluetooth_connect_err);
					//mBluetoothService.stop();
					mBluetoothService.currentStatus = workStatus.Device_UnBonding;
					mBluetoothService.startWork();
				} else {
					connectErrorRetryCheck();
				}
				break;
			case MESSAGE_ERROR:
				String errMsg = (String) msg.obj;
				errorExit(errMsg);
				break;
			case MESSAGE_SEND:
				break;
			case MESSAGE_RECV:
				String recvMsg = (String) msg.obj;
				if (recvMsg.contains("nm")) {
					mText_Lambda.setText(recvMsg);
				} else if (recvMsg.contains("dB")) {
					mText_Power.setTextColor(Color.parseColor("#333399"));
					mText_Power.setText(recvMsg);
					if (recvMsg.contains("dBm")) {
						String mStr = recvMsg.split(" ")[0];
						double mVal = Double.parseDouble(mStr);
						if (mVal <= -24.0) {
							mText_Power.setTextColor(Color.parseColor("#FF0000"));
						}
					}
				} else if (recvMsg.contains("%")) {
					String mStr = recvMsg.split(" ")[0];
					double batVal = Double.parseDouble(mStr); 
					//mTextBattery.setText(recvMsg);
					if (batVal < 10) {
						mImageBattery.setImageResource(R.drawable.battery_0);
					} else if (batVal < 30) {
						mImageBattery.setImageResource(R.drawable.battery_1);						
					} else if (batVal < 60) {
						mImageBattery.setImageResource(R.drawable.battery_2);						
					} else if (batVal < 90) {
						mImageBattery.setImageResource(R.drawable.battery_3);												
					} else {
						mImageBattery.setImageResource(R.drawable.battery_4);																		
					}
				}
				break;
			case MESSAGE_POST_OK:
				String mMsg1 = (String) msg.obj;
				AlertDialog dialog1 = new AlertDialog.Builder(MainActivity.this)
				.setTitle("안내")
				
				.setMessage((mMsg1 == null || mMsg1.isEmpty()) ? "결과 전송을 완료 하였습니다." : "결과전송 실패 \n 사유: " + mMsg1)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("확인", null)
				.create();
				dialog1.show();
				mButton_Send.setEnabled(true);
				System.out.println(mMsg1);
				break;
			case MESSAGE_POST_ERR:
				String mMsg2 = (String) msg.obj;
				AlertDialog dialog2 = new AlertDialog.Builder(MainActivity.this)
				.setTitle("안내")
				.setMessage("오류로 인하여 결과 전송에 실패 하였습니다.\n" + mMsg2)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("확인", null)
				.create();
				dialog2.show();
				mButton_Send.setEnabled(true);
				break;
			default:
				break;
			}
		}
	};

	private DialogInterface.OnClickListener mClick = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
		}
	};
	
	@Override
	protected void onPause() {
		Log.d(TAG, "OnPause");
		// 액티비티가 백그라운드로 내려갈때 영구 종료 처리 한다.
		//exitError(mExitActivity);
		super.onPause();
	}
}
