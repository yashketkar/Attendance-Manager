package com.attendance.manager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
  
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    public static final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
 
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            	
            	
            	SharedPreferences mPreferences = getSharedPreferences(PREFS_NAME, 0);
                boolean hasRun = mPreferences.getBoolean("hasRun", false);
                
            	if(hasRun)
            	{
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            	}
            	else
            	{
            	Intent i = new Intent(SplashScreen.this, Tour.class);
                startActivity(i);
                finish();
            	}	
                // close this activity
                
            }
        }, SPLASH_TIME_OUT);
    }
 
}