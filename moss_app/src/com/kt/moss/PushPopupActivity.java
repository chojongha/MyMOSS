package com.kt.moss;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PushPopupActivity extends Activity {

	private static final String TAG = "PushPopupActivity";
	public static Activity myActivity = null;
	public static boolean popupYN = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i(TAG, "*********************** onCreate()");
		//	다른 액티비에서 현재 액티비티의 상태를 확인한다.
		popupYN = true;
		
		//	현재 액티비티를 다른 액티비티에서 종료하기 위해 static 변수에 액티비티를 담아둔다.
		myActivity = PushPopupActivity.this;
		
		//	화면의 TitleBar, StatusBar 없애기
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//	화면이 잠겨있을 때 보여주기... 키잠금 해제, 화면 켜기
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		setContentView(R.layout.pushpopup);
        
	    // 텍스트뷰에 푸시 알람 내용 출력 : 타이틀
	    TextView tvTital = (TextView)findViewById(R.id.tvTital);
	    String title = getIntent().getStringExtra("pushType");
	    
        // 상황에 따른 타이블 정의
        if("1".equals(title)) {
        	tvTital.setText("고장상황 발생");
        }
        else if("2".equals(title)) {
        	tvTital.setText("고장상황 진행");
        }
        else if("3".equals(title)) {
        	tvTital.setText("고장상황 회복");
        }
        else if("4".equals(title)) {
        	tvTital.setText("고장상황 수정");
        }
        else {
        	tvTital.setText("KT MOSS");
        }
        
        // 텍스트뷰에 푸시 알람 내용 출력 : 내용
	    TextView tvContents = (TextView)findViewById(R.id.tvContents);
	    tvContents.setText(getIntent().getStringExtra("message"));
	    
	    // 보기 이벤트 : 보기 버튼을 터치 하면 상태바의 해당 푸시 메세지를 해제하고 앱을 호출한다.
	    Button btnYes = (Button)findViewById(R.id.btnYes);
	    btnYes.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            // 확인버튼을 누르면 앱의 런처액티비티를 호출한다.
	            Intent intent = new Intent(getBaseContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	            NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	            
	            String msg = getIntent().getStringExtra("message");
	            int notiId = getIntent().getIntExtra("notiId", 0);
	            
	            Log.i(TAG, msg);
	            Log.i(TAG, String.valueOf(notiId));
	            
	            // 현재 도착한 noti를 상태바에서 삭제
	            nm.cancel(notiId);
	            
	            startActivity(intent);
	            popupYN = false;
	            finish();
	        }
	    });
	    
	    // 닫기 이벤트 : 푸시 팝업 액티비티를 종료한다.
	    Button btnNo = (Button)findViewById(R.id.btnNo);
	    btnNo.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	popupYN = false;
	            finish();
	        }
	    });
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "*********************** onResume()");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "*********************** onPause()");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "*********************** onDestroy()");
		
		popupYN = false;
	}
}
