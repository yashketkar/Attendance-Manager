package com.attendance.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import java.util.Calendar;

public class SampleAlarmReceiver extends WakefulBroadcastReceiver {
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;

	SharedPreferences mPreferences;
	boolean notification;
	
	public int hour;
	public int min;
	
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, SampleSchedulingService.class);
		startWakefulService(context, service);
	}

	public void setAlarm(Context context) {
		alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, SampleAlarmReceiver.class);
		alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(System.currentTimeMillis());

		mPreferences = context.getSharedPreferences(PREFS_NAME, 0);
		hour = mPreferences.getInt("hour", 5);
		min = mPreferences.getInt("min", 30);
		
		calendar.set(Calendar.HOUR_OF_DAY,hour);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, 0);

		alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				alarmIntent);

		ComponentName receiver = new ComponentName(context,
				SampleBootReceiver.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

	public void cancelAlarm(Context context) {
		if (alarmMgr != null) {
			alarmMgr.cancel(alarmIntent);
		}
		ComponentName receiver = new ComponentName(context,
				SampleBootReceiver.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
	}
}