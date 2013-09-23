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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
	
	MediaPlayer	mp;
	private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null; 
    private ProgressBar pb ;
    private TextView onair ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		final ImageButton play = (ImageButton) findViewById(R.id.playButton);
		final ImageButton pause = (ImageButton) findViewById(R.id.pauseButton);
		pb = (ProgressBar) findViewById(R.id.streaming);
		onair = (TextView) findViewById(R.id.onair);
		
		pb.setVisibility(View.INVISIBLE);
		onair.setVisibility(View.INVISIBLE);
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		
		initControls();//inizializzo volume  default
		
		
		Uri myUri = Uri.parse("http://shoutcast.rtl.it:3010"); //url di Qubradio: http://79.59.211.91:8080--/
		mp = new MediaPlayer();
		
		
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		
		try {
			mp.setDataSource(this, myUri);
			mp.prepareAsync();
			if (mp.isPlaying()){
			pb.setVisibility(View.VISIBLE);
			onair.setVisibility(View.VISIBLE);}
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
				
				mp.prepareAsync();
				
				/*play.setBackgroundResource(android.R.drawable.);
				pause.setBackgroundResource(android.R.drawable.btn_default);*/
				if (mp.isPlaying()){
					pb.setVisibility(View.VISIBLE);
					onair.setVisibility(View.VISIBLE);
					}
				
				}	
			}
		});
		
		
		pause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mp.isPlaying()){
					mp.stop();
					
					pb.setVisibility(View.INVISIBLE);
					onair.setVisibility(View.INVISIBLE);
					
					/*play.setBackgroundResource(android.R.drawable.btn_default);
					pause.setBackgroundResource(android.R.drawable.alert_dark_frame);*/
				}
			}
		});
		
		
		
		
		
		
		
		mp.setOnPreparedListener(new OnPreparedListener(){
            public void onPrepared(MediaPlayer mp) {
                     mp.start();
                     if (mp.isPlaying()){
             			pb.setVisibility(View.VISIBLE);
             			onair.setVisibility(View.VISIBLE);}
            } 
		});
		
		
	}
		
//-----------------
	
	private void initControls()
    {
        try
        {
            volumeSeekbar = (SeekBar)findViewById(R.id.volumebar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));   


            volumeSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) 
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) 
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) 
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
	
//-----------------
		
	
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    mp.stop(); 
	    pb.setVisibility(View.INVISIBLE);
		onair.setVisibility(View.INVISIBLE);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mp.stop();
		pb.setVisibility(View.INVISIBLE);
		onair.setVisibility(View.INVISIBLE);
		
	}
    
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*mp.prepareAsync();*/
		try {
			mp.prepare();
			mp.start();
			pb.setVisibility(View.VISIBLE);
			
			onair.setVisibility(View.VISIBLE);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	

	//CREAZIONE MENU	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	      case R.id.credits:
	    	  Intent case2 = new Intent(getApplicationContext(), Credits.class);
  	    	  startActivity(case2);
	        return true;
	      
	      default:
	        return super.onOptionsItemSelected(item);
	      }

}
}
