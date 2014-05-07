package com.kt.moss.qtest;


import com.kt.moss.qtest.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class Moss extends Activity {
	private boolean close = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.moss);
		
		/** set time to splash out */
		
		// 딜레이 시간 3초로 설정
		final int welcomeScreenDisplay = 2000;
		
		/** create a thread to show splash up to splash time */
		Thread welcomeThread = new Thread() {
			
			int wait = 0;
			
			@Override			
			public void run() {
				try {
					super.run();
					
					
					while (wait < welcomeScreenDisplay) {
						sleep(100);
						wait += 100;
					}
				} catch (Exception e) {
			
				} finally {
					/**
					 * Called after splash times up. Do some action after splash
					 * times up. Here we moved to another main activity class
					 */
					startnext();
										
					finish();
				}
			}
		};
		welcomeThread.start();
	}
	@Override
	protected void onDestroy() {
		close = true;
		super.onDestroy();
	}
	private void startnext() {
		// TODO Auto-generated method stub
		if (close == false) {
			
			startActivity(new Intent(Moss.this, Login.class));
		
			//startActivity(new Intent(Moss.this, JqueryTest.class));
		}
	}
}