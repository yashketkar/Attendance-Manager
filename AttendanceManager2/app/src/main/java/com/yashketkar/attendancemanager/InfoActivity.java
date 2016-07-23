package com.yashketkar.attendancemanager;

import java.util.ArrayList;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {
	private TextView textPercentage, textTip;
	private Animation animate;
	private AdView adView;
	private AdRequest adRequest;

	public static final String PREFS_NAME = "MyPrefsFile";
	public static ArrayList<Subject> subjectArray;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_info);
        toolbar.setTitle("Attendance Report");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subjectArray = MainActivity.subjectArray;

		adView = (AdView) InfoActivity.this.findViewById(R.id.adView3);
		adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

		double gt = 0, ga = 0, gp = 0;

		for (int i = 0; i < subjectArray.size(); i++) {
			gt += subjectArray.get(i).getTotal();
			ga += subjectArray.get(i).getAttended();
		}

		gp = 100 * ga / gt;
		gp = gp * 100;
		gp = java.lang.Math.round(gp);
		gp = gp / 100;

		if (ga == 0 && gt == 0) {
			gp = 100;
		}

		SharedPreferences mPreferences = getApplicationContext()
				.getSharedPreferences(PREFS_NAME, 0);

		textPercentage = (TextView) findViewById(R.id.textinfohead);
		textTip = (TextView) findViewById(R.id.textinfotip);
		textPercentage.setTypeface(Typeface.createFromAsset(getAssets(),
				"RobotoCondensed-Regular.ttf"));

		int limit = mPreferences.getInt("limit", 75);
		if (gp < limit) {
			textPercentage.setTextColor(Color.RED);
		} else {
			textPercentage.setTextColor(Color.BLACK);
		}

		textPercentage.setText(gp + "%");

		String first = "<br>You have attended " + "<font color='#EE0000'>"
				+ (int) ga + "</font>" + " classes from a total of "
				+ "<font color='#EE0000'>" + (int) gt + "</font>.";

		int diff = 0;
		while (gp < limit) {
			ga++;
			gt++;
			diff++;
			gp = 100 * ga / gt;
		}

		if (diff > 0) {
			first += "<br><br>You need to attend next "
					+ "<font color='#EE0000'>" + diff + "</font>"
					+ " classes for your attendance to be more than "
					+ "<font color='#EE0000'>" + limit + "</font>" + "%";
		}else if(gp==limit)
			{
			first += "<br><br>Your attendance is equal to "
					+ "<font color='#EE0000'>" + limit + "</font>"
					+ "%<br><br>Congratulations!";			
			}else {
			first += "<br><br>Your attendance is above "
					+ "<font color='#EE0000'>" + limit + "</font>"
					+ "%<br><br>Happy Bunking!";

		}
		textTip.setText(Html.fromHtml(first));

		animate = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.reminder_vibrate);
		textPercentage.startAnimation(animate);

	}
}
