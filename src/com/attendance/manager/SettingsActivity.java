package com.attendance.manager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;
import android.app.Dialog;

public class SettingsActivity extends Activity {
	SampleAlarmReceiver alarm = new SampleAlarmReceiver();
	ToggleButton toggle;
	RelativeLayout r;
	TextView tt;
	TextView ap;
	
	SharedPreferences mPreferences;
	boolean notification;

	static final int TIME_DIALOG_ID = 999;
	public int hour;
	public int min;

	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		toggle = (ToggleButton) findViewById(R.id.togglebutton);
		r = (RelativeLayout) findViewById(R.id.notification_time_group);
		tt= (TextView) findViewById(R.id.notification_time);
		ap= (TextView) findViewById(R.id.notification_time_meridiem);

		mPreferences = getSharedPreferences(PREFS_NAME, 0);
		hour = mPreferences.getInt("hour", 5);
		min = mPreferences.getInt("min", 30);

		tt.setText("" + hour + ":" + min);
		
		ap.setText("PM");
		
		notification = mPreferences.getBoolean("notification", false);

		if (notification) {
			r.setVisibility(View.VISIBLE);
			toggle.setChecked(true);
			alarm.setAlarm(this);
		} else {
			r.setVisibility(View.GONE);
			toggle.setChecked(false);
		}

	}

	public void onToggleClicked(View view) {
		// Is the toggle on?
		boolean on = ((ToggleButton) view).isChecked();

		if (on) {
			r.setVisibility(View.VISIBLE);
			SharedPreferences.Editor editor = mPreferences.edit();
			editor.putBoolean("notification", true);
			editor.commit();
			alarm.setAlarm(this);
			Toast.makeText(SettingsActivity.this, "Alarm Set",
					Toast.LENGTH_LONG).show();
		} else {
			r.setVisibility(View.GONE);
			SharedPreferences.Editor editor = mPreferences.edit();
			editor.putBoolean("notification", false);
			editor.commit();
			alarm.cancelAlarm(this);

		}
	}

	public void notificationTimePicker(View v) {
		showDialog(TIME_DIALOG_ID);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, hour, min,
					false);

		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			min = selectedMinute;
			
			SharedPreferences.Editor editor = mPreferences.edit();
			editor.putInt("hour", hour);
			editor.putInt("min", min);
			editor.commit();

		}
	};

}