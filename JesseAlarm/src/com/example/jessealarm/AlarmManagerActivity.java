package com.example.jessealarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AlarmManagerActivity extends Activity {

	private Button mStartBtn1, mStartBtn2, mStopBtn;
	private EditText mTxtSeconds;
	private Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_manager);

		//get the three buttons used for setting a single alarm, setting a repeating alarm, and stopping repeating
		mStartBtn1 = (Button) findViewById(R.id.set_alarm);
		mStartBtn2 = (Button) findViewById(R.id.start_repeating);
		mStopBtn = (Button) findViewById(R.id.stop_repeating);
		
		//get the EditText field used for user input
		mTxtSeconds = (EditText) findViewById(R.id.txtSeconds);

		//create an onClickListener to check when set alarm button is clicked
		mStartBtn1.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				try {
					//get input from user's text field
					int i = Integer.parseInt(mTxtSeconds.getText().toString());
					//create new intent to be used in pendingIntent
					Intent intent = new Intent(AlarmManagerActivity.this,
							AlarmReceiverActivity.class);
					PendingIntent pendingIntent = PendingIntent.getActivity(
							AlarmManagerActivity.this, 2, intent,
							PendingIntent.FLAG_CANCEL_CURRENT);
					//start the Alarm Service
					AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
					//set the alarm to i (user's input) seconds away from current time
					am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
							+ (i * 1000), pendingIntent);
					//if message is being displayed, cancel that message
					if (mToast != null) {
						mToast.cancel();
					}
					//create message to show to user
					mToast = Toast.makeText(getApplicationContext(),
							"Alarm has been set for: " + i + " seconds.",
							Toast.LENGTH_LONG);
					//show the message
					mToast.show();
					//if they entered a letter somehow, show error
				} catch (NumberFormatException e) {
					if (mToast != null) {
						mToast.cancel();
					}
					mToast = Toast.makeText(AlarmManagerActivity.this,
							"Please enter a number", Toast.LENGTH_LONG);
					mToast.show();
					Log.i("AlarmManagerActivity", "Number format exception");
				}
			}

		});

		//create an onClickListener to check when set repeating alarm button is clicked
		mStartBtn2.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				try {
					//get input from text field
					int i = Integer.parseInt(mTxtSeconds.getText().toString());
					//create another intent
					Intent intent = new Intent(AlarmManagerActivity.this,
							RepeatingAlarmReceiverActivity.class);
					PendingIntent pendingIntent = PendingIntent.getActivity(
							AlarmManagerActivity.this, 3, intent,
							PendingIntent.FLAG_CANCEL_CURRENT);
					//start alarm service
					AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
					//set the alarm to go off in i seconds, and keep repeating each i * 2 seconds
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							System.currentTimeMillis() + (i * 1000),
							i * 2 * 1000, pendingIntent);
					//cancel current message
					if (mToast != null) {
						mToast.cancel();
					}
					//make a new message
					mToast = Toast.makeText(getApplicationContext(),
							"Repeating Alarm has been set for " + i
									+ " seconds and repeats every " + i * 2
									+ " seconds after that", Toast.LENGTH_LONG);
					mToast.show();
					//catch non-number input
				} catch (NumberFormatException e) {
					if (mToast != null) {
						mToast.cancel();
					}
					mToast = Toast.makeText(AlarmManagerActivity.this,
							"Please enter a number", Toast.LENGTH_LONG);
					mToast.show();
					Log.i("AlarmManagerActivity", "Number format exception");
				}
			}

		});

		//create an onClickListener to check when stop repeating alarm button is clicked
		mStopBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				//create new intent
				Intent intent = new Intent(AlarmManagerActivity.this,
						RepeatingAlarmReceiverActivity.class);
				//duplicate pendingIntent from repeating alarm so we can stop it
				PendingIntent pendingIntent = PendingIntent.getActivity(
						AlarmManagerActivity.this, 3, intent, 0);
				
				AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
				//stop the repeating alarm
				am.cancel(pendingIntent);
				
				if (mToast != null) {
					mToast.cancel();
				}
				//show user our message
				mToast = Toast.makeText(getApplicationContext(),
						"Repeating has been cancelled!", Toast.LENGTH_LONG);
				mToast.show();

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm_manager, menu);
		return true;
	}

}
