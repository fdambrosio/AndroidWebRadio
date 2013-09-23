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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
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
	
	boolean isRing=true; //controllo per le chiamate in arrivo -- da sistemare con Broadcast Receiver
	MediaPlayer	mp;
	private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null; 
    private ProgressBar pb ;
    private TextView onair ;
    Uri myUri;
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
		
		
		myUri = Uri.parse("http://shoutcast.rtl.it:3010"); //url di Qubradio: http://79.59.211.91:8080--/
		mp = new MediaPlayer();
		
		
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		
		
		startPlayer();
	
		
		play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			if(!mp.isPlaying()){
				
				startPlayer();
				

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
					mp.reset();
					
					pb.setVisibility(View.INVISIBLE);
					onair.setVisibility(View.INVISIBLE);
					
				}
			}
		});
		
		
		
		
		
		
		
		mp.setOnPreparedListener(new OnPreparedListener(){
            public void onPrepared(MediaPlayer mp) {
                     mp.start();
                     if (mp.isPlaying()){
             			pb.setVisibility(View.VISIBLE);
             			onair.setVisibility(View.VISIBLE);
             			}
            } 
		});
		
		
	}
		
	public void startPlayer(){
		try {
			mp.setDataSource(this, myUri);
			mp.prepareAsync();
			if (mp.isPlaying()){
			pb.setVisibility(View.VISIBLE);
			onair.setVisibility(View.VISIBLE);
			}
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
		
	
	//l'app viene distrutta
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    
	   mp.stop();
	   mp.release();
	
	}
	
	
	//l'app va in pausa o viene chiamata una nuova app
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if(isRing){
			mp.stop();
			mp.reset();
		}
		
		
		
	}
	
	//viene premuto il tasto indietro
	@Override
	public void onBackPressed() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    
	    builder.setTitle("Vuoi tenere la radio accesa ?");
	    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                // torna indietro e non stoppa la radio: onPause()
	            	isRing=false;
	            	moveTaskToBack (true);
	            }
	        });
	    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                // stoppa la radio
	            	
	            	MainActivity.this.finish();
	            }
	        });
	    
	    AlertDialog dialog = builder.create();
	    dialog.show();
	    
	}
    
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(!isRing) isRing = true;
		
		else{
			startPlayer();
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
	    	  isRing=false;
	    	  Intent case2 = new Intent(getApplicationContext(), Credits.class);
  	    	  startActivity(case2);
	        return true;
	      
	      default:
	        return super.onOptionsItemSelected(item);
	      }

}
}
