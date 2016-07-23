package com.yashketkar.attendancemanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class SplashScreen extends FragmentActivity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 1000;
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity

				SharedPreferences mPreferences = getSharedPreferences(
						PREFS_NAME, 0);
				boolean hasRun = mPreferences.getBoolean("hasRun", false);

				if (hasRun) {
					Intent i = new Intent(SplashScreen.this, MainActivity.class);
					startActivity(i);
					finish();
				} else {
					Intent i = new Intent(SplashScreen.this, Tour.class);
					startActivity(i);
					finish();
				}
				// close this activity

			}
		}, SPLASH_TIME_OUT);

	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
