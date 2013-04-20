package com.example.jessealarm;

import java.io.IOException;

import com.example.jessealarm.R;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class AlarmReceiverActivity extends Activity {
	private MediaPlayer mMediaPlayer;
	private PowerManager.WakeLock mWakeLock;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//create a new powermanager to use for a wakelock
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		// create a wakelock to wake device
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Wake Log");
		mWakeLock.acquire();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		setContentView(R.layout.alarm);
		
		Button stopAlarm = (Button) findViewById(R.id.btnStopAlarm);
		stopAlarm.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mMediaPlayer.setLooping(false);
				mMediaPlayer.stop();
				finish();

			}
		});

		
		mMediaPlayer = MediaPlayer.create(this, R.raw.btnclick);
		
		testSound();
	}

	private void testSound(){
		mMediaPlayer.setLooping(true);
		mMediaPlayer.start();
	}
	
	protected void onStop(){
		super.onStop();
		mWakeLock.release();
	}
	
	
	//currently not being used, figure out later
	/*private void playSound(Context context, Uri alert) {
		mMediaPlayer = new MediaPlayer();

		try {
			mMediaPlayer.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			}
		} catch (IOException e) {
			Log.i("AlarmReceiver", "No audio files are found");
		}

	}
	
	private Uri getAlarmUri(){
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		if(alert == null){
			alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if(alert == null){
				alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
				
			}
		}
		return alert;
	}*/

	
	
}
