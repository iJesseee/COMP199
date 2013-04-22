package com.example.clock;

import java.util.Calendar;


import com.example.clock.RepeatingAlarmReceiverActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView tvDisplayTime;
	private TimePicker timePicker1;
	private Button btnChangeTime;

	private int hour;
	private int minute;

	private MediaPlayer mpButtonClick;
	
	static final int TIME_DIALOG_ID = 999;
	
	private Button mStartBtn1, mStartBtn2, mStopBtn;
	private EditText mTxtSeconds;
	private Toast mToast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//set up button to make a sound when clicked
		mpButtonClick = MediaPlayer.create(this, R.raw.btnclick);

		setCurrentTimeOnView();
		//addListenerOnButton();

		
		//get the three buttons used for setting a single alarm, setting a repeating alarm, and stopping repeating
		mStartBtn1 = (Button) findViewById(R.id.set_alarm);
		mStartBtn2 = (Button) findViewById(R.id.start_repeating);
		mStopBtn = (Button) findViewById(R.id.stop_repeating);
		
		//get the EditText field used for user input
		mTxtSeconds = (EditText) findViewById(R.id.txtSeconds);

		
		mStartBtn1.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				try {
					//get input from user's text field
					int i = Integer.parseInt(mTxtSeconds.getText().toString());
					//create new intent to be used in pendingIntent
					Intent intent = new Intent(MainActivity.this,
							AlarmReceiverActivity.class);
					PendingIntent pendingIntent = PendingIntent.getActivity(
							MainActivity.this, 2, intent,
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
					mToast = Toast.makeText(MainActivity.this,
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
							Intent intent = new Intent(MainActivity.this,
									RepeatingAlarmReceiverActivity.class);
							PendingIntent pendingIntent = PendingIntent.getActivity(
									MainActivity.this, 3, intent,
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
							mToast = Toast.makeText(MainActivity.this,
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
						Intent intent = new Intent(MainActivity.this,
								RepeatingAlarmReceiverActivity.class);
						//duplicate pendingIntent from repeating alarm so we can stop it
						PendingIntent pendingIntent = PendingIntent.getActivity(
								MainActivity.this, 3, intent, 0);
						
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			startActivity(new Intent(this, AboutActivity.class));
			return true;
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// display current time
	public void setCurrentTimeOnView() {

		tvDisplayTime = (TextView) findViewById(R.id.tvTime);
		timePicker1 = (TimePicker) findViewById(R.id.timePicker1);

		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);

		// set current time into textview
		tvDisplayTime.setText(new StringBuilder().append(pad(hour)).append(":")
				.append(pad(minute)));

		// set current time into timepicker
		timePicker1.setCurrentHour(hour);
		timePicker1.setCurrentMinute(minute);

	}

	public void addListenerOnButton() {

		btnChangeTime = (Button) findViewById(R.id.btnChangeTime);

		btnChangeTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDialog(TIME_DIALOG_ID);
				mpButtonClick.start();

			}

		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);

		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;
			String amOrPm = "";

			if (hour > 12) {
				hour -= 12;
				amOrPm = "PM";
			} else if (hour <= 0) {
				hour += 12;
				amOrPm = "AM";
			} else {
				amOrPm = "AM";
			}
			// set current time into textview
			tvDisplayTime
					.setText(new StringBuilder().append(pad(hour)).append(":")
							.append(pad(minute)).append(" ").append(amOrPm));

			// set current time into timepicker
			timePicker1.setCurrentHour(hour);
			timePicker1.setCurrentMinute(minute);

		}
	};

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
}