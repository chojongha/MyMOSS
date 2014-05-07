package com.example.android.BluetoothChat;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class ServiceClass extends Service {

	private MediaPlayer mPlayer = null;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@TargetApi(5)
	public void onStart(Intent intent1, int startId) {
	
		Intent intent = new Intent(this,BluetoothChat.class);
		Toast.makeText(this, "start service", Toast.LENGTH_SHORT).show();
		//Intent intent = new Intent(this,StopBluetoothService.class);
		
        PendingIntent pendingintent = PendingIntent.getActivity(this,0,intent,0);
       
        String msg1 = "foreground activate..";
       
        Notification mNotification = new Notification(R.drawable.app_icon,msg1,System.currentTimeMillis());
        
        mNotification.setLatestEventInfo(this, msg1, msg1+"aa", pendingintent);
      
        /*mNotification.flags = mNotification.flags|Notification.FLAG_ONGOING_EVENT;
        
        mNotification.flags = mNotification.flags|Notification.FLAG_AUTO_CANCEL;*/
        
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        //startForeground(1002,mNotification);
        
        startForeground(startId,mNotification);
        
		super.onStart(intent, startId);
		
		//mPlayer = MediaPlayer.create(this, R.raw.goaway);
		//mPlayer.start();
	}
	public void onDestroy() {
		
		//mPlayer.stop();
		Toast.makeText(this, "stop service", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

}
 