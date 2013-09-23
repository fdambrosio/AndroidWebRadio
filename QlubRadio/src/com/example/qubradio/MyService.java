package com.example.qubradio;

import java.io.IOException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.IBinder;

public class MyService extends Service {

	Uri myUri;
	MediaPlayer mp;
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		mp = new MediaPlayer();

		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		myUri = Uri.parse("http://shoutcast.rtl.it:3010");
		
		try {
			
			mp.setDataSource(getApplicationContext(), myUri);
			mp.prepareAsync();
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mp.setOnPreparedListener(new OnPreparedListener(){
	        public void onPrepared(MediaPlayer mp) {
	                 mp.start();
	               
	        } 
		});
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.addAction(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		
		registerReceiver(receiver, filter);
		
		return Service.START_NOT_STICKY;
	  }
	
	public void onDestroy() {
	    if (mp.isPlaying()) {
	      mp.stop();
	      mp.release();
	    }
	    unregisterReceiver(receiver);
	  }
	
	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		   @Override
		   public void onReceive(Context context, Intent intent) {
		      String action = intent.getAction();
		      
		       if(action.equals(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
		           //action for phone state changed
		    	   if (mp.isPlaying()) {
		    		   mp.stop();
		    		   mp.reset();
		    	   }else { 
		    			
		    			try {
		    				mp.setDataSource(getApplicationContext(), myUri);
		    				mp.prepareAsync();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    			
		    			mp.setOnPreparedListener(new OnPreparedListener(){
		    		        public void onPrepared(MediaPlayer mp) {
		    		                 mp.start();
		    		               
		    		        } 
		    			});
		    			
		    		}
		      }     
		   }
		};
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
