package com.kt.moss.opticaltest;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


//==========================================================
// 블루투스 장치의 검색과 연결, 송수신을 담당하는 클래스
// 검색 작업은 시스템에 요청한 후 방송을 수신하고
// 페어링 작업 역시 시스템에 요청후 방송을 수신한다.
// 연결은 장시간 소요되므로 쓰레드에 위임하고 메시지를 받는다.
// 송,수신 역시 쓰레드에서 처리하고 메시지를 받는다.
//==========================================================
public class BluetoothService {

	// 디버그용 태그
	private static final String TAG = "BluetoothService";

	// 메인 액티비티 참조
	private final MainActivity mMainActivity;
	// 블루투스 어댑터
    private final BluetoothAdapter mAdapter;
    // 메시지 핸들러
    private final Handler mHandler;
    // 블루투스 원격장치
    private BluetoothDevice mDevice = null;
    // 블루투스 접속 및 송수신 쓰레드
    BluetoothThread mBluetoothThread = null;
    // 웹 포스팅 쓰레드
    HttpThread mHttpThread = null;

    public enum workStatus {
    	None,
    	PowerOn,
    	Finding_Bond,
    	Finding_UnBond,
    	Find_Complete,
    	Device_Bonding,
    	Device_Bonded,
    	Connect_Start,
    	Connect_Processing,
    	Device_UnBonding,
    	Device_UnBonded,
    	Connect_Complete,
    	Running
    }
    
    // 현재의 서비스 상태
    public workStatus currentStatus = workStatus.None;
    
    // 생성자
    public BluetoothService(MainActivity context, Handler handler) {
    	mMainActivity = context;
        mHandler = handler;
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    
        // 블루투스가 지원되지 않는 장비면 종료한다.
		if (mAdapter == null) {
			Toast.makeText(context, "블루투스가 지원되지 않는 장비입니다.", Toast.LENGTH_LONG).show();
			context.finish();
			return;
		}

        IntentFilter filter;
        // 블루투스 활성화 상태 수신기를 등록한다.
        filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(mReceiver, filter);
		// 장치 검색 수신기를 등록한다.
		filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(mReceiver, filter);
		// 장치의 본딩 상태 변경 수신기를 등록한다.
		filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		context.registerReceiver(mReceiver, filter);
		// 장치 검색 완료 수신기를 등록한다.
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		context.registerReceiver(mReceiver, filter);
		// 블루투스 페어링 수신기를 등록한다.
		filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
		context.registerReceiver(mReceiver, filter);
    }


    // 블루투스 장치의 페어링을 해제한다.
//    public void unBondDevice() {
//    }
    
    private synchronized void mydelay() {
    	try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    //=================================================
    // 블루투스 장치가 꺼져 있으면 켜고
    // 장치 검색이 시작되지 않았으면 페어링된 장치를 검색하고
    // 페어링된 장치에 
    // 페어링된 장치 검색을 시작한다.
    // 먼저 페어링된 장치를 검색하고 장치가 없을경우
    // 신호가 있는 장치를 검색한다.
    public synchronized void startWork() {
    	Thread mThread = new Thread(new Runnable() {
			@Override
			public void run() {
		    	while (true) {
		        	switch (currentStatus) {
		    		case None:
		    			// 블루투스 장치가 꺼져 있으면 켠다.
		    			if (mAdapter.isEnabled() == false) {
		    				Log.d(TAG, "꺼져있는 블루투스 켬");
		    				mAdapter.enable();
		        			// TODO 일정 시간이 경과 해도 활성화 상태가 바뀌지 않으면 종료 처리
		    				return;
		    			} else {
		    				currentStatus = workStatus.PowerOn;
		    			}
		    			break;
		    		case Device_UnBonded:
		    		case PowerOn:
		    			currentStatus = workStatus.Finding_Bond;
		    	        // 페어링된 장치 목록 가져오기
		    			mHandler.obtainMessage(MainActivity.MESSAGE_BOND_DEVICE_FINDING).sendToTarget();
		    			mydelay();
		    	    	mDevice = null;
		    			Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();
		    			if (pairedDevices.size() > 0) {
		    				for (BluetoothDevice device : pairedDevices) {
		    					String mName = device.getName();
		    					Log.d(TAG, "장치검색: " + mName + " / " + device.getAddress());
		    					if (mName.equals("SOLMEPM3")) {
		    						// 솔메옵티컬파워메터를 찾았음 (페어링 완료 상태)
		    						mDevice = device;
		    						Log.d(TAG, "옵티컬 파워미터 발견 (페어링)");
		    						currentStatus = workStatus.Find_Complete;
		    						// 장치 검색 보고
		    						mHandler.obtainMessage(MainActivity.MESSAGE_BOND_DEVICE_FINDED).sendToTarget();
		    						mydelay();
		    					}
		    				}
		    			}
		    			// 페어링 장치를 찾을 수 없는 경우
		    			if (mDevice == null) {
		    	    		Log.d(TAG, "페어링된 장치 검색 실패");
		    	    		// 일반 장치 검색 시작
		    	    		mHandler.obtainMessage(MainActivity.MESSAGE_UNBOND_DEVICE_FINDING).sendToTarget();
		    	    		mydelay();
		    	    		// 주변 블루투스 장치를 검색한다.
		    	    		if (mAdapter.isDiscovering()) {
		    	    			mAdapter.cancelDiscovery();
		    	    		}
		    	    		currentStatus = workStatus.Finding_UnBond;
		    	    		mAdapter.startDiscovery();
		    	    		return;
		    			}
		    			break;
		    		case Find_Complete:
		    	    	if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
		    	    		try {
		    					mDevice.getClass().getMethod("createBond").invoke(mDevice);
		    					currentStatus = workStatus.Device_Bonding;
		    				} catch (Exception e) {
		    					e.printStackTrace();
		    				}
		    	    	} else {
		    	    		currentStatus = workStatus.Connect_Start;
		    	    	}
		    			break;
		    		case Device_Bonding:
		    			return;
		    		case Device_UnBonding:
		    	    	if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
			        		try {
			    				mDevice.getClass().getMethod("removeBond").invoke(mDevice);
			    				return;
			    			} catch (Exception e) {
			    				e.printStackTrace();
			    			}
		    	    	}
		    			break;
		    		case Device_Bonded:
		    			currentStatus = workStatus.Connect_Start;
		    			break;
		    		case Connect_Start:
		    	    	stop();
		    	    	mBluetoothThread = new BluetoothThread(mDevice);
		    	    	mBluetoothThread.start();
		    	    	currentStatus = workStatus.Connect_Processing;
		    			return;
		    		default:
		    			return;
		    		}
		        	mydelay();
		    	}
			}
		});
    	mThread.start();
    }

    // 실행중인 쓰레드를 중지 시킨다.
    public synchronized void stop() {
    	if (mBluetoothThread != null) {
    		mBluetoothThread.cancel();
    		mBluetoothThread = null;
    	}
    }
    
    // 코멘드를 원격 장치에 전송한다.
    public synchronized void sendSerial(int dat) {
    	if (mBluetoothThread != null) {
    		mBluetoothThread.WriteSerial(dat);
    	}
    }
    
    public synchronized void sendPost(String url,String workOdrNum, String lambda, String power, String reference) {
    	if (mHttpThread != null) {
    		mHttpThread = null;
    	}
    	String data = "workOdrNum=" + workOdrNum;
    	data += "&opticWaveLength=" + lambda;
    	data += "&opticTestQuality=" + power;
    	data += "&opticTestReference=" + reference;
		mHttpThread = new HttpThread(url, data);
		mHttpThread.start();
    }

	// 블루투스 장치 검색 수신기
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			// 블루투스 활성화 상태 변경
			if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
				int stat = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
				if (stat == BluetoothAdapter.STATE_ON) {
					Log.d(TAG, "Broadcast: 블루투스 ON");
					currentStatus = workStatus.PowerOn;
					mHandler.obtainMessage(MainActivity.MESSAGE_BLUETOOTH_ENABLED).sendToTarget();
				}
				return;
			}
			
			// 블루투스 원격 장치 발견
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				int mBond = device.getBondState();
				Log.d(TAG, "Broadcast: 원격장치 발견");
				if (mBond != BluetoothDevice.BOND_BONDED) {
					if (device.getName().equals("SOLMEPM3")) {
						// 장치 검색을 중단한다.
						mAdapter.cancelDiscovery();
						mDevice = device;
						// 장치 검색 보고
						currentStatus = workStatus.Find_Complete;
						mHandler.obtainMessage(MainActivity.MESSAGE_UNBOND_DEVICE_FINDED).sendToTarget();
					}
				}
				return;
			}
			
			// 블루투스 원격 장치 찾기 종료
			if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				Log.d(TAG, "Broadcast: 원격장치 찾기 종료");
				// 검색 종료 이벤트
				if (mDevice == null) {
					// 장치 검색 오류 보고
					mHandler.obtainMessage(MainActivity.MESSAGE_FIND_ERROR).sendToTarget();
				}
				return;
			}
			
			// 블루투스 장치의 본딩 상태 변경
			if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				Log.d(TAG, "Broadcast: 본딩 상태 변경");
				int stat = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);
				if (stat == BluetoothDevice.BOND_BONDED) {
					currentStatus = workStatus.Device_Bonded;
					mHandler.obtainMessage(MainActivity.MESSAGE_DEVICE_BONDED).sendToTarget();
				} else if (stat == BluetoothDevice.BOND_NONE) {
					currentStatus = workStatus.Device_UnBonded;
					mHandler.obtainMessage(MainActivity.MESSAGE_DEVICE_UNBONDED).sendToTarget();
				}
				return;
			}
			
			// 페어링이 시작됨
			if ("android.bluetooth.device.action.PAIRING_REQUEST".equals(action)) {
			    // 페어링 과정에서 요구하는 PIN 번호를 자동으로 입력한다.
				Log.d(TAG, "Broadcast: 페어링 시작");
		    	byte[] pins = { 0x31, 0x32, 0x33, 0x34 };
		    	try {
		    		BluetoothDevice device = intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
		    		device.getClass().getMethod("setPin", byte[].class).invoke(mDevice,  pins);
					device.getClass().getMethod("cancelPairingUserInput", boolean.class).invoke(device);
		    		device.getClass().getMethod("setPairingConfirmation", boolean.class).invoke(device, true);
//					Class<? extends BluetoothDevice> mClass = mDevice.getClass();
//					mClass.getMethod("setPin", byte[].class).invoke(mDevice,  pins);
//					mClass.getMethod("setPairingConfirmation", boolean.class).invoke(mDevice,  true);
					//mClass.getMethod("cancelPairingUserInput", boolean.class).invoke(mDevice);
		    		//mMainActivity.onBondedDevice();
				} catch (Exception e) {
					Log.d(TAG, "자동 페어링 오류: " + e.getMessage());
					e.printStackTrace();
				}
		    	return;
			}
		}
	};
	
	// 블루투스 장치 연결 및 송수신 쓰레드
	private class BluetoothThread extends Thread {
		private final BluetoothSocket mSocket;
		private InputStream mInStream = null;
		private OutputStream mOutStream = null;
		private boolean IsRun = false;

		public BluetoothThread(BluetoothDevice device) {
			Log.d(TAG, "파워미터에 연결을 시작합니다.");
			BluetoothSocket tmp = null;
			try {
				// 원래소스: device.createRfcommSocketToServiceRecord(SERIAL_UUID);
				Method m = mDevice.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
				tmp = (BluetoothSocket) m.invoke(mDevice, 1);
				Log.d(TAG, "소켓 생성 완료");
			} catch (Exception e) {
				Log.d(TAG, "소켓 생성 실패");
			}
			mSocket = tmp;
		}

		// 원격 장치에 접속 하기
		private synchronized boolean Connect() {
			boolean mIsConnect = false;
			try {
				mHandler.obtainMessage(MainActivity.MESSAGE_CONNECT_START).sendToTarget();
				Thread.sleep(20);
				Log.d(TAG, "BluetoothThread:Connect");
				mSocket.connect();
				mIsConnect = true;
				// 소켓 연결 성공 보고
				mHandler.obtainMessage(MainActivity.MESSAGE_CONNECT_COMPLETE).sendToTarget();
				Thread.sleep(20);
			} catch (Exception e) {
				// 오류 보고
				mHandler.obtainMessage(MainActivity.MESSAGE_CONNECT_ERROR, "Connect Error").sendToTarget();
				Log.d(TAG, "연결 오류: " + e.getMessage());
				try {
					mSocket.close();
				} catch (IOException e2) {
					Log.d(TAG, "연결 닫기 오류: " + e.getMessage());
				}
			}
			return mIsConnect;
		}
		
		// 접속된 장치의 입,출력 스트림 얻기
		private synchronized boolean GetStream() {
			boolean mIsGet = false;
            try {
            	mInStream = mSocket.getInputStream();
            	mOutStream = mSocket.getOutputStream();
            	mIsGet = true;
            } catch (IOException e) {
            	// 오류 보고
				mHandler.obtainMessage(MainActivity.MESSAGE_ERROR, "GetStream Error").sendToTarget();
            }
            return mIsGet;
		}
		
		public void run() {
			setName("ConnectThread");
			mAdapter.cancelDiscovery();
			if (mSocket == null) return;
			// 연결을 두번 시도
			if (Connect() == false) {
				if (Connect() == false) {
					return;
				}
			}
			currentStatus = workStatus.Connect_Complete;
			if (GetStream() == false) return;
			currentStatus = currentStatus.Running;

            IsRun = true;
            String mRecvMsg = "";
            while (IsRun == true) {
                try {
                    // 입력 스트림에서 데이터를 읽어 들인다.
                    int dat = mInStream.read();
                    switch (dat) {
					case ':':
                    	mRecvMsg = "";
						break;
					case '\n':
                    	if (mRecvMsg.length() > 0) {
                            // 읽어들인 결과를 메인 UI 엑티비티에 보고한다.
                            mHandler.obtainMessage(MainActivity.MESSAGE_RECV, mRecvMsg).sendToTarget();
                            Log.d(TAG, "문자열 수신 보고: " + mRecvMsg);
                    	}
                    	break;
					case '\r':
						break;
					default:
                    	mRecvMsg += (char) dat;
						break;
					}
                } catch (Exception e) {
                	// 오류 보고
    				//mHandler.obtainMessage(MainActivity.MESSAGE_ERROR, "ReadStream").sendToTarget();
                }
            }
		}

        public void WriteSerial(int cmd) {
        	if (mOutStream == null) {
        		return;
        	}
            try {
                mOutStream.write(cmd);
                // 전송 완료를 메인 액티비티에 보고
                mHandler.obtainMessage(MainActivity.MESSAGE_SEND, cmd).sendToTarget();
            } catch (IOException e) {
				mHandler.obtainMessage(MainActivity.MESSAGE_ERROR, "WriteStream").sendToTarget();
            }
        }

        // 연결된 소켓을 닫는다.
		public void cancel() {
			try {
				IsRun = false;
				this.join(100);
	            mSocket.close();
				Log.d(TAG, "소켓 해지 완료");
				mInStream.close();
				Log.d(TAG, "해지(I)");
	            mOutStream.close();
				Log.d(TAG, "해지(O)");
			} catch (Exception e) {
				Log.d(TAG, "소켓 닫기 및 스레드 종료 오류: " + e.getMessage());
			}
		}
	}
	
	// 웹 POST를 담당하는 쓰레드
	private class HttpThread extends Thread {
		private final URL mHttpUrl;
		private final String mPostData;
		
		public HttpThread(String url, String data) {
			URL tmp = null;
			try {
				tmp = new URL(url);
			} catch (Exception e) {
			}
			mHttpUrl = tmp;
			mPostData = data;
			Log.d(TAG, "전송시작: " + url);
		}

		private String SubmitData() {
			StringBuilder result = new StringBuilder();
			try {
				HttpURLConnection conn = (HttpURLConnection) mHttpUrl.openConnection();
				conn.setConnectTimeout(15000);
				conn.setDefaultUseCaches(false);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				OutputStream ws = conn.getOutputStream();
				ws.write(mPostData.getBytes("UTF-8"));
				ws.flush();
				ws.close();
				//conn.setAllowUserInteraction(false);
//				DataInputStream dis = new DataInputStream(conn.getInputStream());
//				String OneLine = null;
//				while ((OneLine = dis.readUTF()) != null) {
//					result.append(OneLine + "\n");
//				}
//				dis.close();
				
				InputStream is = conn.getInputStream();
		        BufferedReader br = new BufferedReader(new InputStreamReader(is));
		        char[] buff = new char[512];
		        int len = -1;
		        
		        while( (len = br.read(buff)) != -1) {
		        	result.append(new String(buff, 0, len));
		        }
		        
		        br.close();
				// JSON 결과 파싱			
				
				JSONObject json = new JSONObject(result.toString());
				JSONArray mBody = json.getJSONArray("body");
				JSONObject mRec = mBody.getJSONObject(0);
				String mMsg = mRec.getString("errMsg"); 				
				
				mHandler.obtainMessage(MainActivity.MESSAGE_POST_OK, mMsg).sendToTarget();
			} catch (Exception e) {
				mHandler.obtainMessage(MainActivity.MESSAGE_POST_ERR, e.getMessage()).sendToTarget();
				e.printStackTrace();
			}
			return result.toString();
		}

		@Override
		public void run() {
			setName("HttpThread");
			final String result = SubmitData();
		}
	}
}
