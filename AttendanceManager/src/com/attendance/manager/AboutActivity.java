package com.attendance.manager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Button;
import android.util.Log;
import android.view.View;
//import com.google.android.gms.plus.PlusOneButton;
//import android.view.View.OnClickListener;

public class AboutActivity extends Activity {

	ImageButton ig, it;
	Button br, bp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		// mPlusOneButton = (PlusOneButton) findViewById(R.id.plus_one_button);
		br = (Button) findViewById(R.id.button1);
		bp = (Button) findViewById(R.id.button2);
		ig = (ImageButton) findViewById(R.id.imageButton1);
		it = (ImageButton) findViewById(R.id.imageButton2);
		ig.setOnClickListener(listener);
		it.setOnClickListener(listener);
		br.setOnClickListener(listener);
		bp.setOnClickListener(listener);
	}

	private View.OnClickListener listener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case (R.id.button1):
				// Intent intent = new Intent(Intent.ACTION_VIEW);
				// intent.setData(Uri.parse("market://details?id=com."));
				// startActivity(intent);
				break; // add here
			case (R.id.button2):
				Intent browserIntent2 = new Intent(Intent.ACTION_SENDTO,
						Uri.fromParts("mailto", "yashketkar93@gmail.com", null));
				browserIntent2.putExtra(Intent.EXTRA_SUBJECT,
						"Attendance Manager Feedback");

				String debug_info = "\n\n\n Device information \n -------------------------------";
				try {
					debug_info += "\n Attendance Manager version: "
							+ getPackageManager().getPackageInfo(
									getPackageName(), 0).versionName;
				} catch (PackageManager.NameNotFoundException nne) {
					Log.e("About", "Name not found exception");
				}
				debug_info += "\n Android Version: " + Build.VERSION.RELEASE
						+ " (API " + Build.VERSION.SDK_INT
						+ ") \n Model (and product): " + android.os.Build.MODEL
						+ " (" + android.os.Build.PRODUCT + ") \n Device: "
						+ android.os.Build.DEVICE;

				browserIntent2.putExtra(Intent.EXTRA_TEXT, debug_info);

				startActivity(Intent.createChooser(browserIntent2,
						"Send e-mail"));
				break;// add here
			case (R.id.imageButton1):
				Intent browserIntent3 = new Intent(Intent.ACTION_VIEW,
						Uri.parse("https://plus.google.com/+YashKetkar"));
				startActivity(browserIntent3);
				break; // add here
			case (R.id.imageButton2):
				Intent browserIntent4 = new Intent(Intent.ACTION_VIEW,
						Uri.parse("https://www.twitter.com/yashketkar"));
				startActivity(browserIntent4);
				break; // add here
			}
		}
	};
}