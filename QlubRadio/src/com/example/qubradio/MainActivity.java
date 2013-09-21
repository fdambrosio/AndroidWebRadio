package com.example.qubradio;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	MediaPlayer	mp;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		final ImageButton play = (ImageButton) findViewById(R.id.playButton);
		final ImageButton pause = (ImageButton) findViewById(R.id.pauseButton);
		
		
		
		Uri myUri = Uri.parse("http://shoutcast.rtl.it:3010/"); //url di Qubradio: http://79.59.211.91:8080
		mp = new MediaPlayer();
		
		play.setBackgroundResource(android.R.drawable.alert_dark_frame);
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		
		try {
			mp.setDataSource(this, myUri);
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
		
		
		
		
		play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			if(!mp.isPlaying()){
				mp.start();
				play.setBackgroundResource(android.R.drawable.alert_dark_frame);
				pause.setBackgroundResource(android.R.drawable.btn_default);
				Toast toast= Toast.makeText(getApplicationContext(), "in ascolto", 500);
                toast.show();
				
				}	
			}
		});
		
		
		pause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mp.isPlaying()){
					mp.pause();
					Toast toast= Toast.makeText(getApplicationContext(), "in pausa", 500);
                    toast.show();
					play.setBackgroundResource(android.R.drawable.btn_default);
					pause.setBackgroundResource(android.R.drawable.alert_dark_frame);
				}
			}
		});
		
		
		
		
		
		
		
		mp.setOnPreparedListener(new OnPreparedListener(){
            public void onPrepared(MediaPlayer mp) {
                     mp.start();
                     Toast toast= Toast.makeText(getApplicationContext(), "in ascolto", 1500);
                     toast.show();
            } 
		});
		
		
	}
		

	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    mp.stop();
	}
	
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
