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
// ��Ƽ�� �Ŀ� ������ ��Ƽ��Ƽ
//
// �� ��Ƽ��Ƽ�� ������ �⵿ �� ���� ������,
// ������ ���ø����̼ǿ��� ȣ��Ǹ� �ҷ����� ��ġ�� ���� ���Ŀ��� �����ϰ�
// ���� ��� ������ ����ϴ� ���� �������� �Ѵ�.
// (��� ������ ���� ���� �ϹǷ� �������� �Ѱ� �޾Ƽ� ������ ���� �ϴ°���
//  �����ٵ� �� �̸� ���� �ߴ��� �ǽɽ�������)
// �� ������� ��ġ�� ��Ʈ�� ���°������� ��ġ�� ���¿� ���
// ���� ���� �ð��� �ҿ�Ǳ⵵ �ϰ�, ����� ��ġ��
// �Ÿ��� ���� ������ �������� ���� ���¸� �������� �����Ƿ�
// ��Ƽ��Ƽ�� Ȱ��ȭ ������ onResume ���Ŀ� �ֿ��� Ȱ���� ó���ϰ�
// ��׶���� �������� onPause ���¿��� ������ �����Ѵ�.
// ��� ���� �߿� ��ȭ, ���� ���� ���� ������ �ٸ� ��Ƽ��Ƽ�� Ȱ��ȭ �Ǹ�
// �� ��Ƽ��Ƽ�� ���� �ǹǷ� ���Ӱ� �����Ͽ��� �Ѵ�.
//
// 2013-11-09 ���� ���� by
//   SOLME Programmer �迵�� (mademlm@gmail.com) 
//====================================================
public class MainActivity extends Activity implements OnClickListener {

	// ����׿� �±�
	private static final String TAG = "OpticalTest";
	
	// ��ũ�� ��ü ���� (TextView)
	private TextView mTextTitle;
	private TextView mTextMessage;
	private ImageView mImageBattery;
	private TextView mText_WorkOrderNum;
	private TextView mText_CustName;
	private TextView mText_TelNum;
	private TextView mText_Lambda;
	private TextView mText_Power;
	
	// ��ũ�� ��ü ���� (Button)
	private Button mButton_Exit;
	private ImageView mButton_Power;
	private ImageView mButton_Lambda;
	private ImageView mButton_Select;
	private Button mButton_Send;
	
	// ���� ��� ����
	private String mWorkOdrNum;
	private String mCustName;
	private String mTelNum;
	private static final String mPostAddress = "http://moss.kt.com/moss/OpticTestResult";
	
    // ������� ���� Ŭ����
    private BluetoothService mBluetoothService = null;
    
    // ��׶��� ��ȯ�� ���� �÷���
    private boolean mExitActivity = true;
    
    private final String INIT_LAMBDA = "---- nm";
    private final String INIT_POWER = "--.-- dBm";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "OnCreate");
		
		// Ÿ��Ʋ ������ ����� �����Ѵ�.
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cust_title);

		// ȭ�鿡 ���� ��� ��ü ����
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

		// �ܺο��� ȣ���Ҷ� �Ѱ��� �Ķ���� Ȯ��
		Intent mIntent = getIntent();
		mWorkOdrNum = mIntent.getStringExtra("workOdrNum");
		mCustName = mIntent.getStringExtra("custName");
		mTelNum = mIntent.getStringExtra("telNum");
		//mPostAddress = mIntent.getStringExtra("postAddress");
		Log.d(TAG, "����ּ�:" + mPostAddress);

		// â ���� ǥ��
		mTextTitle.setText(R.string.win_title);
		
		// ������ȣ, �?��Ī, ��ȭ��ȣ ǥ��
		mText_WorkOrderNum.setText(mWorkOdrNum == null ? "��������" : mWorkOdrNum);
		mText_CustName.setText(mCustName == null ? "����" : mCustName);
		mText_TelNum.setText(mTelNum == null ? "����" : mTelNum);

		// ������ �ʱ�ȭ
		mText_Lambda.setText(INIT_LAMBDA);
		mText_Power.setText(INIT_POWER);

		// ������� ���� ��
		mBluetoothService = new BluetoothService(this, mHandler);
	}

	// ȭ���� ���� ��ư ��ü�� Ȱ��ȭ �Ǵ� ��Ȱ��ȭ �Ѵ�.
	private void screenEnable(boolean enable) {
		mButton_Power.setEnabled(enable);
		mButton_Lambda.setEnabled(enable);
		mButton_Select.setEnabled(enable);
		// ��ü �׽����� ��� ���(Send) ��ư�� �� Ȱ��ȭ �Ѵ�.
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
		// ȭ���� ��ư�� ��Ȱ��ȭ ��Ų��.
		screenEnable(false);
		// ������� ���񽺸� �����Ѵ�.
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
			Log.d(TAG, "���� ��ư ����");
			checkExit();
			break;
		case R.id.button_Power:
			Log.d(TAG, "Power ��ư ����");
			mPowerFlag = !mPowerFlag;
			mButton_Power.setImageResource(mPowerFlag ? R.drawable.button_power_on : R.drawable.button_power_off);
			mBluetoothService.sendSerial(mPowerFlag ? 'o' : 'x');
			break;
		case R.id.button_Lambda:
			Log.d(TAG, "Lambda ��ư ����");
			mBluetoothService.sendSerial('l');
			break;
		case R.id.button_Select:
			Log.d(TAG, "Select ��ư ����");
			mBluetoothService.sendSerial('r');
			break;
		case R.id.button_Send:
			Log.d(TAG, "Send ��ư ����");
			String mLambda = mText_Lambda.getText().toString();
			String mPower = mText_Power.getText().toString();
			if (mLambda.equals(INIT_LAMBDA)) {
				ShowMessage("���尪�� �������� �ʾҽ��ϴ�.");
			} else if (mPower.equals(INIT_POWER)) {
				ShowMessage("�Ŀ����� �������� �ʾҽ��ϴ�.");				
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

	// �佺Ʈ �޽����� ���̾ƿ��� ����� �����Ͽ� �����ش�.
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

	
	// ���񽺿��� ���� ���� ����
	private void report(CharSequence msg) {
		mTextMessage.setText(msg);
	}
	
	// BACK ��ư�̳�, ���� ��ư�� ���� ���� Ȯ��
	private void checkExit() {
		AlertDialog dialog = new AlertDialog.Builder(this)
		.setTitle("�ȳ�")
		.setMessage("�����Ͻðڽ��ϱ�?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mBluetoothService != null) {
					mBluetoothService.stop();
				}
				finish();
			}})
		.setNegativeButton("�ƴϿ�", mClick).create();
		dialog.show();
	}
	
	// ������ ������ ġ������ ����� �޽��� Ȯ�� �� ����
	private void errorExit(CharSequence msg) {
		AlertDialog dialog = new AlertDialog.Builder(this)
			.setTitle("�ȳ�")
			.setMessage(msg)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mBluetoothService.stop();
					finish();
				}})
			.create();
		dialog.show();
	}
	
	// ��ġ ã�� ���н� ��õ� ���� Ȯ��
	private void findErrorRetryCheck() {
		AlertDialog dialog = new AlertDialog.Builder(this)
		.setTitle("�ȳ�")
		.setMessage("�Ŀ����͸� ã�� �� ����ϴ�.\n�ٽ� �˻� �Ͻðڽ��ϱ�?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("��õ�", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mBluetoothService.currentStatus = workStatus.None;
				mBluetoothService.startWork();
			}
		})
		.setNegativeButton("����", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mBluetoothService.stop();
				finish();
			}
		})
		.create();
		dialog.show();
	}

	// ���� ���� ���� ��õ� ���� Ȯ��
	private void connectErrorRetryCheck() {
		AlertDialog dialog = new AlertDialog.Builder(this)
		.setTitle("�ȳ�")
		.setMessage("�Ŀ����Ϳ� ���ῡ ������ �ֽ��ϴ�.\n�ٽ� �õ� �Ͻðڽ��ϱ�?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("��õ�", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mBluetoothService.currentStatus = workStatus.None;
				mBluetoothService.startWork();
			}
		})
		.setNegativeButton("����", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mBluetoothService.stop();
				finish();
			}
		})
		.create();
		dialog.show();
	}

//	// ��ġ�� ���� �Ϸ��
//	public synchronized void onBondedDevice() {
//		Log.d(TAG, "onBondedDevice");
//		mExitActivity = true;
//		mBluetoothService.connect();
//	}
//	
//	// ��ġ�� ���� ������
//	public synchronized void onUnbondDevice() {
//		Log.d(TAG, "onUnbondDevice");
//		setupMeasure();
//	}
	
	
	// ������� Ŭ������ ���
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
	// ����� Ŭ������ ���
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
				report("���� �Ŀ����� �˻���.");
				break;
			case MESSAGE_BOND_DEVICE_FINDED:
				Log.d(TAG, "MESSAGE_BOND_DEVICE_FINDED");
				report("���� �Ŀ����͸� ã�ҽ��ϴ�.");
				mIsBond = true;
				break;
			case MESSAGE_UNBOND_DEVICE_FINDING:
				Log.d(TAG, "MESSAGE_UNBOND_DEVICE_FINDING");
				report("������ ���� �Ŀ����� �˻���");
				mBluetoothService.startWork();
				break;
			case MESSAGE_UNBOND_DEVICE_FINDED:
				Log.d(TAG, "MESSAGE_UNBOND_DEVICE_FINDED");
				report("������ ���� �Ŀ����͸� ã�ҽ��ϴ�.");
				mExitActivity = false;
				mBluetoothService.startWork();
				break;
			case MESSAGE_FIND_ERROR:
				Log.d(TAG, "MESSAGE_FIND_ERROR");
				report("�Ŀ����� �˻� ����");
				findErrorRetryCheck();
				break;
			case MESSAGE_DEVICE_BONDED:
				Log.d(TAG, "MESSAGE_DEVICE_BONDED");
				report("�Ŀ����͸� �� �߽��ϴ�.");
				mBluetoothService.startWork();
				break;
			case MESSAGE_DEVICE_UNBONDED:
				Log.d(TAG, "MESSAGE_DEVICE_UNBONDED");
				report("�Ŀ����͸� �� ���� �߽��ϴ�.");
				mBluetoothService.startWork();
				break;
			case MESSAGE_CONNECT_START:
				mExitActivity = false;
				report("�Ŀ����Ϳ� ������ �����մϴ�.");
				break;
			case MESSAGE_CONNECT_COMPLETE:
				mExitActivity = true;
				report("�Ŀ����Ϳ� ������ �Ϸ�Ǿ���ϴ�.");
				Toast.makeText(MainActivity.this, "������ �����ϼ���!",  Toast.LENGTH_SHORT).show();
				// ȭ���� Ȱ��ȭ �Ѵ�.
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
				.setTitle("�ȳ�")
				
				.setMessage((mMsg1 == null || mMsg1.isEmpty()) ? "��� ����� �Ϸ� �Ͽ����ϴ�." : "������ ���� \n ����: " + mMsg1)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("Ȯ��", null)
				.create();
				dialog1.show();
				mButton_Send.setEnabled(true);
				System.out.println(mMsg1);
				break;
			case MESSAGE_POST_ERR:
				String mMsg2 = (String) msg.obj;
				AlertDialog dialog2 = new AlertDialog.Builder(MainActivity.this)
				.setTitle("�ȳ�")
				.setMessage("����� ���Ͽ� ��� ��ۿ� ���� �Ͽ����ϴ�.\n" + mMsg2)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("Ȯ��", null)
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
		// ��Ƽ��Ƽ�� ��׶���� �������� ���� ���� ó�� �Ѵ�.
		//exitError(mExitActivity);
		super.onPause();
	}
}
