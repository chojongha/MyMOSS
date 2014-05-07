/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.BluetoothChat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the main Activity that displays the current chat session.
 */
@TargetApi(5)
public class BluetoothChat extends Activity {
    // Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_KORNET_CONNECTED = 6;
    public static final int MESSAGE_KORNET_FAIL = 7;
    public static final int VISIBLE = 0;
    public static final int GONE = 8;


    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOKORNET_SUCCESS = "toKornetSucess";
    public static final String TOKORNET_FAIL = "toKornetFail";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    
    // SEED
    public static final String SEED_PASSWORD = "1234567890abcdef";
    
    // Layout Views
    //private ListView mConversationView;
    private TextView mConversationView;
    private EditText mOutEditText;
    //private Button mSendButton;
    private ScrollView scroll;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    
    private String mToKornetConn = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter  = null;
    // Member object for the chat services
    private BluetoothChatService mChatService = null;

    private InputMethodManager imm;
    
	private Dialog customDialogInstance;
		
	public static String serverIP="";
	public static int servicePort=0;
	public static String usrID="";
	public static String usrpass="";
	
	public TextView BlueToothStatus;
	public TextView ServerStatus;
	
    @TargetApi(5)
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        // Set up the window layout
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }  
        
        BlueToothStatus = (TextView) findViewById(R.id.title_left_text);
        ServerStatus = (TextView) findViewById(R.id.title_right_text);

        BlueToothStatus.setText(" ");
        ServerStatus.setText(" ");
        
        /*Intent intent = new Intent(BluetoothChatService.ACTION_FOREGROUND);
        intent.setClass(BluetoothChat.this, BluetoothChatService.class);
        startService(intent);*/        
        /*Intent intent = new Intent(this,BluetoothChat.class);
        PendingIntent pendingintent = PendingIntent.getActivity(this,0,intent,0);
        String msg1 = "foreground activate..";
        Notification mNotification = new Notification(R.drawable.app_icon,msg1,System.currentTimeMillis());
        mNotification.flags = mNotification.flags|Notification.FLAG_ONGOING_EVENT;
        startForeground(1000,mNotification);*/
        /*Intent intent = new Intent(this,BluetoothChat.class);
        PendingIntent pendingintent = PendingIntent.getActivity(this,0,intent,0);
        String msg1 = "foreground activate..";
        Notification mNotification = new Notification(R.drawable.app_icon,msg1,System.currentTimeMillis());
        //mNotification.flags = mNotification.flags|Notification.FLAG_ONGOING_EVENT;
        mNotification.setLatestEventInfo(this, msg1, msg1+"aa", pendingintent);
        StartForeground(1000,mNotification);*/
        
        /*Notification notification = new Notification(R.drawable.app_icon, "111",System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, BluetoothChat.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, "222","333", pendingIntent);
        startForeground(Notification.FLAG_ONGOING_EVENT, notification);*/ 
        /*Intent intent = new Intent(BluetoothChatService.ACTION_FOREGROUND);
        intent.setClass(BluetoothChat.this, BluetoothChatService.class);
        startService(intent);*/
        startService(new Intent("com.example.android.BluetoothChat.FOREGROUND"));
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        dialog = getCustomDialog2();
        customDialogInstance = dialog;
        return dialog;
    }
    private Dialog getCustomDialog2() {
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        
        Context mContext = BluetoothChat.this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_dialog2,(ViewGroup) findViewById(R.id.layout_root));
        
        final EditText DserverIP = (EditText)layout.findViewById(R.id.serverIP);
        final EditText DservicePort = (EditText)layout.findViewById(R.id.servicePort);
        final EditText DuserID = (EditText)layout.findViewById(R.id.userID);
        final EditText DuserPasswd = (EditText)layout.findViewById(R.id.userPasswd);
        
        DserverIP.setText("14.63.129.156");
        DservicePort.setText("10035");
//        DuserID.setText("91038947");
//        DuserPasswd.setText("ktmoss!@34");
        //	아이디 입력란 포커스, 비밀번호 입력란 별표(*) 처리
        DuserID.requestFocus();
        DuserPasswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        
        final Button okButton = (Button)layout.findViewById(R.id.button1);
        okButton.setOnClickListener(new OnClickListener() {
            
            //@Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(DserverIP.getText()) || TextUtils.isEmpty(DservicePort.getText())) {
                    Toast.makeText(getApplicationContext(), "IP or PORT is empty", Toast.LENGTH_SHORT).show();
                } else {
                	serverIP = DserverIP.getText().toString();
                	servicePort = Integer.parseInt(DservicePort.getText().toString());
                	usrID = DuserID.getText().toString();
//                	usrpass = DuserPasswd.getText().toString();
                	// Pw 암호화
                	
                	try {
						usrpass = AES256Cipher.AES_Encode(DuserPasswd.getText().toString(), SEED_PASSWORD);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	
                	mChatService.toKornetConnect();
                    customDialogInstance.dismiss();                	
                }
            }
        });
        
        final Button cancelButton = (Button)layout.findViewById(R.id.button2);
        cancelButton.setOnClickListener(new OnClickListener() {
            
            //@Override
            public void onClick(View v) {            	
                customDialogInstance.dismiss();                 
                //android.os.Process.killProcess(android.os.Process.myPid());                
            }
        });
        
        builder = new AlertDialog.Builder(mContext);
        builder.setView(layout);
        alertDialog = builder.create();
                
        return alertDialog;
    }
    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
        	Log.e(TAG, "++ mblue s ++");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (mChatService == null) {Log.e(TAG, "++ if s ++"); setupChat();
            /*Intent intent = new Intent(BluetoothChatService.ACTION_FOREGROUND);
            intent.setClass(BluetoothChat.this, BluetoothChatService.class);
            startService(intent);*/
            }
        }
    }

    @Override
    public synchronized void onResume() {
    //public void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
              // Start the Bluetooth chat services
              mChatService.start();
            }
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        mConversationView = (TextView) findViewById(R.id.in);
        mConversationView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	//mSendButton.setVisibility(0);
            	//mOutEditText.setVisibility(VISIBLE);
            	//if (!imm.isActive()) imm.showSoftInput(mOutEditText, 0);
            	imm.toggleSoftInput(0, 0);
            	scroll.postDelayed( new Runnable(){  
            		public void run() {  
            			//scroll.smoothScrollBy(0, 500);
            			//scroll.smoothScrollTo(20,20);
            			scroll.fullScroll(ScrollView.FOCUS_DOWN);
            		}}, 100); 
            }
        });
        //mConversationView = getViewById(R.id.in);
        //mConversationView.setAdapter(mConversationArrayAdapter);
        //mConversationView.setAdapter(mConversationArrayAdapter);      

        // Initialize the compose field with a listener for the return key
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);

        //mOutEditText.setVisibility(View.INVISIBLE);
        
        mOutEditText.setOnEditorActionListener(mWriteListener);

        scroll = (ScrollView)findViewById(R.id.widget35);

               
        // Initialize the send button with a listener that for click events
        /*mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                TextView view = (TextView) findViewById(R.id.edit_text_out);
                String message = view.getText().toString();
                sendMessage(message);
            }
        });*/

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    @Override
    public synchronized void onPause() {
    //public  void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        stopService(new Intent("com.example.android.BluetoothChat.FOREGROUND"));
        if(D) Log.e(TAG, "--- ON DESTROY ---");
        
    }

    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
    	
    	/*mSendButton.setVisibility(4);
    	mOutEditText.setVisibility(4);
    	imm.hideSoftInputFromWindow(mOutEditText.getWindowToken(), 0);*/
    	
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            BlueToothStatus.setText("BlueToothNotConnected");
            return;
        }

      
        // Check that there's actually something to send
        //message = message + "\r";
        if (message.length() > 0) { 
            // Get the message bytes and tell the BluetoothChatService to write
        	//message = message + "\r";
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
        	int i =0;
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                //mConversationView.requestFocus();
                //mOutEditText.setVisibility(GONE);
                message = message + "\r";
                sendMessage(message);
            }
            if(D) Log.i(TAG, "END onEditorAction");
            return true;
        }
    };

    private final void setStatus(int resId) {
        //final ActionBar actionBar = getActionBar();
        //actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        //actionBar.setDisplayOptions(DISPLAY_SHOW_HOME, DISPLAY_SHOW_HOME | DISPLAY_USE_LOGO);
        //actionBar.setSubtitle(resId);
    }

    private final void setStatus(CharSequence subTitle) {
        //final ActionBar actionBar = getActionBar();
        //actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        //actionBar.setSubtitle(subTitle);
    }

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                    setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                    mConversationArrayAdapter.clear();
                    BlueToothStatus.setText("BlueToothConnected");
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    setStatus(R.string.title_connecting);
                    BlueToothStatus.setText("BlueToothConnecting");
                    break;
                case BluetoothChatService.STATE_LISTEN:
                	break;
                case BluetoothChatService.STATE_NONE:
                    setStatus(R.string.title_not_connected);
                    BlueToothStatus.setText("BlueToothNotConnected");
                    break;
                case BluetoothChatService.KORNET_CONNECTED:
                    setStatus(R.string.title_not_connected);
                    //ServerStatus.setText("Server Connected");
                    break; 
                case BluetoothChatService.NOT_KORNET_CONNECTED:
                    setStatus(R.string.title_not_connected);
                    ServerStatus.setText("Server Not Connected");
                    break; 
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                writeMessage = writeMessage + "\n";
                //mConversationArrayAdapter.add("Me:  " + writeMessage);
                mConversationView.append(new String(writeMessage));
                //mConversationView.setSelected(true);
                //mConversationView.setFocusable(true);
                //mConversationView.requestFocus();
                scroll.post(new Runnable()                
                {
                	//@Override
                    public void run()
                    {
                    	scroll.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });                 
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                //mConversationArrayAdapter.add(readMessage);
                mConversationView.append(new String(readMessage));
                //mConversationView.setSelected(true);
                //mConversationView.setFocusable(true);
                //mConversationView.requestFocus();
                scroll.post(new Runnable()                
                {
                	//@Override
                    public void run()
                    {
                    	scroll.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });    
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                //ServerStatus.setText("Server Not Connected");
                break;
            case MESSAGE_KORNET_CONNECTED:
            	mToKornetConn = msg.getData().getString(TOKORNET_SUCCESS);
                Toast.makeText(getApplicationContext(), mToKornetConn, Toast.LENGTH_SHORT).show();
                ServerStatus.setText("Server Connected");
                break;
            case MESSAGE_KORNET_FAIL:
            	mToKornetConn = msg.getData().getString(TOKORNET_FAIL);
                Toast.makeText(getApplicationContext(), mToKornetConn, Toast.LENGTH_SHORT).show();
                ServerStatus.setText("Server NOT Connected");
                break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE_SECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, true);
            }
            break;
        case REQUEST_CONNECT_DEVICE_INSECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, false);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                setupChat();
            } else {
                // User did not enable Bluetooth or an error occurred
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        switch (item.getItemId()) {
        case R.id.insecure_connect_scan:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
            return true;
        case R.id.connect_server:
            // connect server
        	showDialog(1);
            return true;
        }
        return false;
    }



}