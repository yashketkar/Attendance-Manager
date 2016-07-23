package com.yashketkar.attendancemanager;

import android.app.AlertDialog;
import android.app.Activity;
import android.app.backup.BackupManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class AboutActivity extends Activity implements OnItemClickListener {

	public static final String[] titles = new String[] { "Backup", "Restore",
			"Set Attendance Limit", "Help", "Feedback", "Rate This App",
			"Share This App", "Recommended Apps", "Watch Free Live TV" };

	public static final Integer[] images = { R.drawable.ic_action_upload,
			R.drawable.ic_action_download, R.drawable.ic_action_edit,
			R.drawable.ic_action_help, R.drawable.ic_action_email,
			R.drawable.ic_action_star, R.drawable.ic_action_share_dark,
			R.drawable.ic_action_heart, R.drawable.ic_live_tv };

	public static final String PREFS_NAME = "MyPrefsFile";
	ListView listView;
	List<RowItem> rowItems;

	private AdView adView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		rowItems = new ArrayList<RowItem>();
		for (int i = 0; i < titles.length; i++) {
			RowItem item = new RowItem(images[i], titles[i]);
			rowItems.add(item);
		}

		listView = (ListView) findViewById(R.id.aboutlist);
		AboutCustomAdapter adapter = new AboutCustomAdapter(this,
				R.layout.about_row, rowItems);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

		// Create an ad.
		adView = (AdView) this.findViewById(R.id.adView2);

		// Create an ad request. Check logcat output for the hashed device ID to
		// get test ads on a physical device.
		AdRequest adRequest = new AdRequest.Builder().build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		BackupManager bm = new BackupManager(this);
		switch (position) {
		case 0:
			bm.dataChanged();

			AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
			alert1.setTitle("Confirm Backup");
			alert1.setMessage("Are you sure you want to save records to : \n\n"
					+ Environment.getExternalStorageDirectory().getPath()
					+ "/attendance.am");
			alert1.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							try {
								File src = new File(getApplicationContext()
										.getFilesDir().getPath()
										+ "/attendance");
								File dst = new File(Environment
										.getExternalStorageDirectory()
										.getPath()
										+ "/attendance.am");
								if (!dst.exists()) {
									dst.createNewFile();
								}
								InputStream in = new FileInputStream(src);
								OutputStream out = new FileOutputStream(dst);

								// Transfer bytes from in to out
								byte[] buf = new byte[1024];
								int len;
								while ((len = in.read(buf)) > 0) {
									out.write(buf, 0, len);
								}
								in.close();
								out.close();

								Toast.makeText(
										AboutActivity.this,
										"Records saved at : "
												+ Environment
														.getExternalStorageDirectory()
														.getPath()
												+ "/attendance.am",
										Toast.LENGTH_SHORT).show();

							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Toast.makeText(AboutActivity.this,
										"File Not Found", Toast.LENGTH_SHORT)
										.show();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Toast.makeText(AboutActivity.this, "Failed",
										Toast.LENGTH_SHORT).show();
							}

						}
					});
			alert1.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});
			alert1.show();

			break;
		case 1:
			// bm.requestRestore(new RestoreObserver() {
			// });

			AlertDialog.Builder alert2 = new AlertDialog.Builder(this);
			alert2.setTitle("Confirm Restore");
			alert2.setMessage("Are you sure you want to restore records from : \n\n"
					+ Environment.getExternalStorageDirectory().getPath()
					+ "/attendance.am");
			alert2.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							try {
								File src = new File(Environment
										.getExternalStorageDirectory()
										.getPath()
										+ "/attendance.am");
								File dst = new File(getApplicationContext()
										.getFilesDir().getPath()
										+ "/attendance");
								if (!dst.exists()) {
									dst.createNewFile();
								}
								InputStream in = new FileInputStream(src);
								OutputStream out = new FileOutputStream(dst);

								// Transfer bytes from in to out
								byte[] buf = new byte[1024];
								int len;
								while ((len = in.read(buf)) > 0) {
									out.write(buf, 0, len);
								}
								in.close();
								out.close();

								Toast.makeText(
										AboutActivity.this,
										"Records restored from : "
												+ Environment
														.getExternalStorageDirectory()
														.getPath()
												+ "/attendance.am",
										Toast.LENGTH_SHORT).show();

							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Toast.makeText(AboutActivity.this,
										"File Not Found", Toast.LENGTH_SHORT)
										.show();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Toast.makeText(AboutActivity.this, "Failed",
										Toast.LENGTH_SHORT).show();
							} finally {
								// System.exit(0);
								finish();
							}
						}
					});
			alert2.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});
			alert2.show();
			break;
		case 2:
			AlertDialog.Builder alert3 = new AlertDialog.Builder(this);
			alert3.setTitle("Set Attendance Limit");
			final EditText input = new EditText(this);
			input.setSingleLine();
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
			input.setText("" + 75);
			alert3.setView(input);
			alert3.setMessage("Edit your attendance limit here.\nEg.Write 75 for 75% attendance limit.");
			alert3.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							SharedPreferences mPreferences = getSharedPreferences(
									PREFS_NAME, 0);
							SharedPreferences.Editor editor = mPreferences
									.edit();
							editor.putInt("limit", Integer.parseInt(input
									.getText().toString()));
							editor.commit();
						}
					});
			alert3.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});
			alert3.show();
			break;
		case 3:
			String videoId = "Y4lXDMUDvuA";
			try {
				Intent vintent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("vnd.youtube:" + videoId));
				vintent.putExtra("VIDEO_ID", videoId);
				startActivity(vintent);
			} catch (ActivityNotFoundException ex) {
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://www.youtube.com/watch?v=" + videoId));
				startActivity(intent);
			}
			break;
		case 4:
			Intent browserIntent2 = new Intent(Intent.ACTION_SENDTO,
					Uri.fromParts("mailto", "yashketkar93@gmail.com", null));
			browserIntent2.putExtra(Intent.EXTRA_SUBJECT,
					"Attendance Manager Feedback");

			String debug_info = " Device information \n -------------------------------";
			try {
				debug_info += "\n App version: "
						+ getPackageManager().getPackageInfo(getPackageName(),
								0).versionName;
			} catch (PackageManager.NameNotFoundException nne) {
				Log.e("About", "Name not found exception");
			}
			debug_info += "\n Android Version: " + Build.VERSION.RELEASE
					+ " (API " + Build.VERSION.SDK_INT
					+ ") \n Model (and product): " + android.os.Build.MODEL
					+ " (" + android.os.Build.PRODUCT + ") \n Device: "
					+ android.os.Build.DEVICE + "\n Manufacturer: "
					+ android.os.Build.MANUFACTURER
					+ "\n\n Please write your feedback from here.\n\n";

			browserIntent2.putExtra(Intent.EXTRA_TEXT, debug_info);

			startActivity(Intent.createChooser(browserIntent2, "Send e-mail"));
			break;
		case 5:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			if (isAppInstalled(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE)) {
				intent.setData(Uri
						.parse("market://details?id=com.yashketkar.attendancemanager"));
			} else {
				intent.setData(Uri
						.parse("http://play.google.com/store/apps/details?id=com.yashketkar.attendancemanager"));
			}
			startActivity(intent);

			break;

		case 6:
			Toast.makeText(AboutActivity.this, "You're awesome! :D",
					Toast.LENGTH_SHORT).show();
			String value = "http://play.google.com/store/apps/details?id=com.yashketkar.attendancemanager \n\nNever fall short of attendance again. Install Attendance Manager and bunk your way to glory! (P.S. Not for geeks.)";
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, value);
			startActivity(Intent.createChooser(shareIntent, "Share via..."));
			break;
		case 7:
			Intent intent2 = new Intent(Intent.ACTION_VIEW);

			if (isAppInstalled(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE)) {
				intent2.setData(Uri.parse("market://search?q=pub:Yash Ketkar"));
			} else {
				intent2.setData(Uri
						.parse("http://play.google.com/store/search?q=pub%3AYash%20Ketkar"));
			}

			startActivity(intent2);
			break;
		case 8:
			String url = "http://www.getlivetvapp.co.vu/download.html";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
			break;
		}

	}

	private boolean isAppInstalled(String packageName) {
		PackageManager pm = getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}
}