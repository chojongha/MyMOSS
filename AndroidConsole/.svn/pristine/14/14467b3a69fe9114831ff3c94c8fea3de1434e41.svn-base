/*
 * Copyright (C) 2009 The Android Open Source Projects
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
@TargetApi(10)
public class BluetoothChatService {
    // Debugging
    private static final String TAG = "BluetoothChatService";
    private static final boolean D = true;

    // Name for the SDP record when creating server socket
    private static final String NAME_SECURE = "BluetoothChatSecure";
    private static final String NAME_INSECURE = "BluetoothChatInsecure";

    // Unique UUID for this application
    //private static final UUID MY_UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    //private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //00001110-0000-1000-8000-00805F9B34FB
    //private static final UUID MY_UUID_SECURE = UUID.fromString("00001110-0000-1000-8000-00805F9B34FB");
    //private static final UUID MY_UUID_INSECURE = UUID.fromString("00001110-0000-1000-8000-00805F9B34FB");
    


    // Member fields
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private AcceptThread mSecureAcceptThread;
    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private ToKornetThread kornetThread;
    private int mState;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    public static final int KORNET_CONNECTED = 4;
    public static final int NOT_KORNET_CONNECTED = 5;

	private Socket toKornetSocket;  
	private InputStream networkReader; 
	private OutputStream networkWriter;
	private String ip;  
	private int port; 
	public static String delimeter = "a^b!c^d!";
	public boolean toKornetSocketReady = false;
	
	static final String ACTION_FOREGROUND = "com.example.android.BluetoothChat.FOREGROUND";
    
    /**
     * Constructor. Prepares a new BluetoothChat session.
     * @param context  The UI Activity Context
     * @param handler  A Handler to send messages back to the UI Activity
     */
    public BluetoothChatService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mHandler = handler;   
        
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
        startForeground(1000,mNotification);*/
        
        /*Notification notification = new Notification(R.drawable.app_icon, "111",System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, BluetoothChat.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, "222","333", pendingIntent);
        startForeground(Notification.FLAG_ONGOING_EVENT, notification);*/ 
        
        /*Intent intent = new Intent(this,BluetoothChat.class);
        PendingIntent pendingintent = PendingIntent.getActivity(this,0,intent,0);
        String msg1 = "foreground activate..";
        Notification mNotification = new Notification(R.drawable.app_icon,msg1,System.currentTimeMillis());
        mNotification.setLatestEventInfo(this, msg1, msg1+"aa", pendingintent);
        mNotification.flags = mNotification.flags|Notification.FLAG_ONGOING_EVENT;
        startForeground(1000,mNotification);*/
        
    } 
    /*@Override
    public void onCreate() {}
    @Override
    public void onStart(Intent intentarg, int startID) {
    	Log.e("tttt", "ooooooooooookkkkkkkkkkkkkkkkkkkk");
    	super.onStart(intentarg, startID);
    	Intent intent = new Intent(this,BluetoothChat.class);
        PendingIntent pendingintent = PendingIntent.getActivity(this,0,intent,0);
        String msg1 = "foreground activate..";
        Notification mNotification = new Notification(R.drawable.app_icon,msg1,System.currentTimeMillis());
        mNotification.setLatestEventInfo(this, msg1, msg1+"aa", pendingintent);
        mNotification.flags = mNotification.flags|Notification.FLAG_ONGOING_EVENT;
        startForeground(1000,mNotification);
    }*/
    /**
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     */         
    private synchronized void setState(int state) {
        if (D) Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(BluetoothChat.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     * Return the current connection state. */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume() */
    public synchronized void start() {
        if (D) Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        setState(STATE_LISTEN);

        // Start the thread to listen on a BluetoothServerSocket
        if (mSecureAcceptThread == null) {
            mSecureAcceptThread = new AcceptThread(true);
            mSecureAcceptThread.start();
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread(false);
            mInsecureAcceptThread.start();
        }
    }        
    
    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    public synchronized void connect(BluetoothDevice device, boolean secure) {
        if (D) Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device, secure);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }
    
    public void toKornetConnect(){
    	Log.e("9999", "aaaa");
    	if(kornetThread != null && kornetThread.isAlive()) {
    		Log.e("9999", "bbbb");
    		kornetThread.cancel();
    		Log.e("9999", "cccc");
    		//kornetThread.interrupt();
    		//Log.e("9999", "dddd");
    	}  
    	Log.e("9999", "eeee");
    	kornetThread = new ToKornetThread();
    	Log.e("9999", "ffff");
    	kornetThread.start(); 
    	Log.e("9999", "gggg");  
    	toKornetSocketReady = false;
    }   
    
    public synchronized void ConnectedToKornet(String str) {
    	Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_KORNET_CONNECTED);
        Bundle bundle = new Bundle();
        bundle.putString(BluetoothChat.TOKORNET_SUCCESS, str);
        msg.setData(bundle);
        mHandler.sendMessage(msg);  
    }
    
    public synchronized void KonetFail(String str) {
    	Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_KORNET_FAIL);
        Bundle bundle = new Bundle();
        bundle.putString(BluetoothChat.TOKORNET_FAIL, str);
        msg.setData(bundle);
        mHandler.sendMessage(msg);  
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device, final String socketType) {
        if (D) Log.d(TAG, "connected, Socket Type:" + socketType);

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Cancel the accept thread because we only want to connect to one device
        if (mSecureAcceptThread != null) {
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }
        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread.cancel();
            mInsecureAcceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket, socketType);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(BluetoothChat.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (D) Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mSecureAcceptThread != null) {
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }

        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread.cancel();
            mInsecureAcceptThread = null;
        }
        if (kornetThread != null) {
        	kornetThread.cancel();
        	kornetThread = null;
        }
        setState(STATE_NONE);
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
         r.writeComport(out);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed(String str) {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(BluetoothChat.TOAST, str);
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        // Start the service over to restart listening mode
        BluetoothChatService.this.start();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost(String str) {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(BluetoothChat.TOAST, str);
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        // Start the service over to restart listening mode
        BluetoothChatService.this.start();
    }
    
    private void connectionLostKornet(String str) {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(BluetoothChat.TOAST, str);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        private String mSocketType;

        public AcceptThread(boolean secure) {
            BluetoothServerSocket tmp = null;
            mSocketType = secure ? "Secure":"Insecure";

            // Create a new listening server socket
            try {
                if (secure) {
                    tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_SECURE);
                } else {
                    tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME_INSECURE, MY_UUID_INSECURE);
                	//tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(MY_UUID_INSECURE);
                }
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            if (D) Log.d(TAG, "Socket Type: " + mSocketType + "BEGIN mAcceptThread" + this);
            setName("AcceptThread" + mSocketType);

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket Type: " + mSocketType + "accept() failed", e);
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothChatService.this) {
                        switch (mState) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            // Situation normal. Start the connected thread.
                            connected(socket, socket.getRemoteDevice(),
                                    mSocketType);
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Could not close unwanted socket", e);
                            }
                            break;
                        }
                    }
                }
            }
            if (D) Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);

        }

        public void cancel() {
            if (D) Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e);
            }
        }
    }


    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
                    //BluetoothSocket tmp1 = null;
                    //tmp1 = device.createRfcommSocketToServiceRecord(new UUID("12"));
                } else {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE);
                }
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
            }
            mmSocket = tmp;            
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
                
                //String uu;
                //uu = mHandler.obtainMessage(BluetoothChat.serverIP);
                
                /*try {
                	toKornetSocket = new Socket(ip, port);            
                    networkWriter = toKornetSocket.getOutputStream(); 
                    networkReader = toKornetSocket.getInputStream();
                } catch (IOException e) {  
                	try {
                		toKornetSocket.close();
                		networkWriter.close();
                        networkReader.close();                       
                    } catch (IOException e2) {                    	
                    }
                }*/
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " + mSocketType + " socket during connection failure", e2);
                }
                connectionFailed("Unable to connect device");
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothChatService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice, mSocketType);
            
        }

        public void cancel() {
            try {
                mmSocket.close();
                           
                /*networkWriter.close();
                networkReader.close();
                toKornetSocket.close();*/
            } catch (IOException e) {
                Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            Log.d(TAG, "create ConnectedThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;                      
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                	Log.e("888", "aaaaaaaaaaaaaaaaaaaaaa");
                    // Read from the InputStream
                	buffer = new byte[1024];
                    bytes = mmInStream.read(buffer);
                    Log.e("888", "bbbbbbbbbbbbbbbbbbbbbb");

                    // Send the obtained bytes to the UI Activity
                    String rm = new String(buffer, 0, bytes);
                    Log.e("888",rm);
                    Log.e("888", "cccccccccccccccccccccc");
                    mHandler.obtainMessage(BluetoothChat.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    Log.e("888", "dddddddddddddddddddddd");
                    try {                    	             
                        if (toKornetSocket != null && toKornetSocket.isConnected() && toKornetSocketReady) {
                        	Log.e("888", "eeeeeeeeeeeeeeeeeeeeeee");
                        	//networkWriter.write(buffer);
                        	networkWriter.write(buffer, 0, bytes);
                        	Log.e("888", "fffffffffffffffffffffff");
                    	    networkWriter.flush();
                    	    Log.e("888", "ggggggggggggggggggggggg");
                        }
                    } catch (IOException e) {                    	
                    }                    
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost("Device connection was lost");
                    // Start the service over to restart listening mode
                    try {
						toKornetSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    BluetoothChatService.this.start();
                    Log.e("888", "hhhhhhhhhhhhhhhhhhhhhhh");
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void writeComport(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                //mHandler.obtainMessage(BluetoothChat.MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
    
    private class ToKornetThread extends Thread {
    	public ToKornetThread() {  
    		toKornetSocketReady = false;
    	}
    	public void run() {    		
    		try {
    			/*try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
    			Log.e("9999", "hhhh");
    			toKornetSocket = new Socket(BluetoothChat.serverIP , BluetoothChat.servicePort );   
    			//toKornetSocket.close();
    			Log.e("9999", "iiii");
                networkWriter = toKornetSocket.getOutputStream(); 
                Log.e("9999", "jjjj");
                networkReader = toKornetSocket.getInputStream(); 
                Log.e("9999", "kkkk");
                ConnectedToKornet("Success Kornet Connection");
                Log.e("9999", "mmmm");               
                //////////usrID usrpass
                String sendIDPASS = BluetoothChat.usrID + delimeter + BluetoothChat.usrpass + delimeter + "\n";
                byte[] sendByte = sendIDPASS.getBytes();                
                networkWriter.write(sendByte, 0, sendByte.length);                  
            	Log.e("888", "fffffffffffffffffffffff");
        	    networkWriter.flush();        	    
        	    byte[] r = new byte[1024];
		        int size=0;
		        Log.e("9999", "pppp");
				size = networkReader.read(r);
				Log.e("9999", "qqqq");
				String imsiStr = new String(r);
				String resultString = imsiStr.substring(0, size);
				if (resultString.indexOf("accepted") >= 0) {
				   ConnectedToKornet("Success Login !!!!!!"); 
				   //setState(KORNET_CONNECTED);
				   toKornetSocketReady = true;}
				else {ConnectedToKornet("Login Fail"); cancel(); return;}    
            } catch (IOException e) {
            	Log.e("9999", "nnnn");
            	KonetFail("Fail To Kornet");
            	Log.e("9999", "oooo");
            	setState(NOT_KORNET_CONNECTED);
            	return;
            }
    		while (true) {
    			//receive
    			try {
    				byte[] r = new byte[1024];
    		        int size=0;
    		        Log.e("9999", "pppp");
					size = networkReader.read(r);
					Log.e("9999", "qqqq");
					String imsiStr = new String(r);
					Log.e("9999", "rrrr");
					String resultString = imsiStr.substring(0, size);
					Log.e("resultString", resultString);
					byte[] imsiByte = resultString.getBytes();
					Log.e("imsiByte", ""+imsiByte);
					write(imsiByte);
					Log.e("9999", "uuuu");
    			}
    			catch (IOException e) {    			
    				// Close the socket
                    try {
                    	Log.e("9999", "vvvv");
                    	toKornetSocket.close();
                    	//networkWriter.close();
                    	//networkReader.close();
                    	Log.e("9999", "wwww");
                    } catch (IOException e2) {
                    	Log.e("9999", "xxxx");
                        Log.e(TAG, "toKornet connection closed", e2);
                    }
                    Log.e("9999", "yyyy");
                    KonetFail("Kornet connection lost");
                    //setState(NOT_KORNET_CONNECTED);
                    toKornetSocketReady = false;
                    Log.e("9999", "zzzz");
                    return;
    			}
    		}    	
    	}
    	public void cancel() {
            if (D) Log.d(TAG, "toKornet Connection cancel " + this);
            try {
            	Log.e("9999","11");
            	toKornetSocket.close();
            	Log.e("9999","22");
            	//networkWriter.close();
            	//networkReader.close();
            } catch (IOException e) {
            	Log.e("9999","33");
                Log.e(TAG, "toKornet connection close Fail", e);
            }
            toKornetSocketReady = false;
        }
    }


}
