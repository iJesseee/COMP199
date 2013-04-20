package com.example.jessealarm;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RepeatingAlarmReceiverActivity extends Activity {
	private MediaPlayer mMediaPlayer = null;
	private PowerManager.WakeLock mWakeLock;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

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

	private void testSound() {

		mMediaPlayer.setLooping(true);
		mMediaPlayer.start();

	}

	protected void onStop() {
		super.onStop();
		mWakeLock.release();
	}

}