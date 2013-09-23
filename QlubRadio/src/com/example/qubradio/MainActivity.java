package com.example.qubradio;

import java.io.IOException;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
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
	
	private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null; 
    private ProgressBar pb ;
    private TextView onair ;
    Uri myUri;
    Intent intent;
    
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		final ImageButton play = (ImageButton) findViewById(R.id.playButton);
		final ImageButton pause = (ImageButton) findViewById(R.id.pauseButton);
		pb = (ProgressBar) findViewById(R.id.streaming);
		onair = (TextView) findViewById(R.id.onair);
	
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		
		initControls();//inizializzo volume  default
		
		
		// il receiver riceve i messaggi sul cambio della connettività
		IntentFilter filter = new IntentFilter();
		filter.addAction("aandroid.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(isConnected, filter); //
		
		
	
		//inizializza l'intent per il service e lo lancia
		intent = new Intent(getApplicationContext(), MyService.class);
		startService(intent);
	
	
		
		play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(!isMyServiceRunning()){ //fa partire il service se inattivo
					startService(intent);
					pb.setVisibility(View.VISIBLE);
					onair.setVisibility(View.VISIBLE);
					
				}
			
			
			}
		});
		
		
		pause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isMyServiceRunning()){ //se il service è attivo lo stoppa
					stopService(intent);
					pb.setVisibility(View.INVISIBLE);
					onair.setVisibility(View.INVISIBLE);
					
				}
				
				
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
		
	
	//l'app viene distrutta
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    
	    //stoppa il Service e cancella il receiver
	    unregisterReceiver(isConnected); 
	    stopService(intent);
	
	}
	
	
	
	//viene premuto il tasto indietro
	@Override
	public void onBackPressed() {
		
		if(isMyServiceRunning()){
			
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    
	    builder.setTitle("Vuoi tenere la radio accesa ?");
	    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                // torna indietro e non stoppa la radio: onPause()
	            	
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
	    
		}else finish();
	    
	}
    
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(!isMyServiceRunning()){
			startService(intent);
			pb.setVisibility(View.VISIBLE);
			onair.setVisibility(View.VISIBLE);
			
		}
		
		
		
	}
	
	
	//receiver per i problemi di rete NON FUNZIONA
	private final BroadcastReceiver isConnected = new BroadcastReceiver() {
		   @Override
		   public void onReceive(Context context, Intent intent) {
			   ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			     NetworkInfo netInfo = cm.getActiveNetworkInfo();
			   boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting()&& cm.getActiveNetworkInfo().isAvailable()&& cm.getActiveNetworkInfo().isConnected();  

			   if (!isConnected){
				   Toast toast = Toast.makeText(getApplicationContext(), "Errore di rete", 1000);
			    	 toast.show();
				    pb.setVisibility(View.INVISIBLE);
					onair.setVisibility(View.INVISIBLE);
			   		}
				 

		   }
		};
	
	  
	  
	 private boolean isMyServiceRunning() {
		    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
		        if (MyService.class.getName().equals(service.service.getClassName())) {
		            return true;
		        }
		    }
		    return false;
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
