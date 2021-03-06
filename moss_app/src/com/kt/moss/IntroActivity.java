package com.kt.moss;

import static com.kt.moss.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.kt.moss.CommonUtilities.EXTRA_MESSAGE;
import static com.kt.moss.CommonUtilities.SENDER_ID;
import static com.kt.moss.CommonUtilities.SERVER_URL;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;



public class IntroActivity extends Activity{
	
	private final int START_PHONE_GAP = 1;
	static final String TAG = "IntroActivity";
	private Timer m_timer;
	private Context m_context;
	private String registration_Id = "";
    AsyncTask<Void, Void, Void> mRegisterTask;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		
        checkNotNull(SERVER_URL, "SERVER_URL");
        checkNotNull(SENDER_ID, "SENDER_ID");
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
        registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
        final String regId = GCMRegistrar.getRegistrationId(this);
        
        if (regId.equals("")) {
            // Automatically registers application on startup.
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            // Device is already registered on GCM, check server.
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
                Log.i(TAG, getString(R.string.already_registered) + "\n");
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
//                        ServerUtilities.register(context, regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
//                mRegisterTask.execute(null, null, null);
            }
        }
        
        // Call MainActivity 
		m_context = this;
		m_timer = new Timer();
		m_timer.schedule(m_timeTasker, 2 * 1000);
	}

	TimerTask m_timeTasker = new TimerTask() {
		
		@Override
		public void run() {
			m_Handler.sendEmptyMessage(START_PHONE_GAP);
		}
	};
	
	Handler.Callback m_hCallback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch(msg.what) {
			case START_PHONE_GAP :
				m_timer.cancel();
				
		        registration_Id = GCMRegistrar.getRegistrationId(m_context);
		        Log.i("Registration Id", registration_Id);
		        // regId를 SharedPreferences 에 저장
				savePreferences();
		        Intent intent = new Intent(m_context, MainActivity.class);
//				intent.putExtra("regId", registration_Id);
				startActivity(intent);
//				finish();
				System.exit(0);
				break;
			}
			
			return false;
		}
		
	};

	Handler m_Handler = new Handler(m_hCallback);

    @Override
    protected void onDestroy() {
    	
    	Log.i(TAG, "================= onDestroy() =================");
    	
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        unregisterReceiver(mHandleMessageReceiver);
        GCMRegistrar.onDestroy(getApplicationContext());
        super.onDestroy();
    }

    private void checkNotNull(Object reference, String name) {
        if (reference == null) {
            throw new NullPointerException(getString(R.string.error_config, name));
        }
    }

    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            Log.i("Intro", newMessage + "\n");
        }
    };
    
    // 웹뷰에 전달하기 위한 regId 값 저장하기
    private void savePreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", registration_Id);
        editor.commit();
    }
}
