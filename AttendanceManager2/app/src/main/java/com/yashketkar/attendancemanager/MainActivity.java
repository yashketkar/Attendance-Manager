package com.yashketkar.attendancemanager;

import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.widget.ListView;
import android.view.View;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.android.gms.ads.*;

public class MainActivity extends AppCompatActivity {

    public static SubjectCustomAdapter subjectAdapter;
    public static ArrayList<Subject> subjectArray = new ArrayList<Subject>();
    private ListView lv;
    private AdView adView;

    private InterstitialAd interstitial;

    // Object for intrinsic lock
    static final Object sDataLock = new Object();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_list);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.ic_launcher);
        //getSupportActionBar().setIcon(R.drawable.ic_launcher);

        subjectAdapter = new SubjectCustomAdapter(MainActivity.this,
                R.layout.row, subjectArray, MainActivity.this);
        subjectAdapter.setNotifyOnChange(true);
        // setListAdapter(subjectAdapter);

        lv = (ListView) findViewById(android.R.id.list);
        lv.setAdapter(subjectAdapter);
        registerForContextMenu(lv);
        lv.setEmptyView(findViewById(R.id.emptyview));
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                // Toast.makeText(getBaseContext(), "",
                // Toast.LENGTH_LONG).show();
            }
        });
        readstring();
        increase();

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-2265268935639337/6064921007");
        AdRequest adRequest2 = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest2);

        // Create an ad.
        adView = (AdView) this.findViewById(R.id.adView1);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder().build();
        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_add:
                AlertDialog.Builder alert2 = new AlertDialog.Builder(this);
                alert2.setTitle("Enter the subject name:");
                // alert2.setMessage("Enter the subject name:");
                final EditText input2 = new EditText(this);
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    // only for gingerbread and newer versions
                    input2.setTextColor(Color.WHITE);
                }
                input2.setSingleLine();
                alert2.setView(input2);
                alert2.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                String value = input2.getText().toString()
                                        .replaceAll("[\\t\\n\\r]", " ");
                                subjectArray.add(new Subject(value, 0, 0));
                                increase();
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
            case R.id.action_info:
                displayInterstitial();
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    startnext();
                } else {
                    Intent ii = new Intent(MainActivity.this, InfoActivity.class);
                    startActivity(ii);
                }
                break;
            case R.id.action_deleteall:
                if (subjectArray.size() == 0) {
                    Toast.makeText(this, "Add a subject first!", Toast.LENGTH_SHORT)
                            .show();
                } else {

                    AlertDialog.Builder alert3 = new AlertDialog.Builder(this);
                    alert3.setTitle("Delete All Subjects?");
                    alert3.setMessage("Do you really want to delete all the subjects?");
                    alert3.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {

                                    subjectArray.removeAll(subjectArray);
                                    increase();
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
                    // increase();

                }
                break;

            case R.id.action_bunkall:
                if (subjectArray.size() == 0) {
                    Toast.makeText(this, "Add a subject first!", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
                    alert1.setTitle("Confirm Bunks?");
                    alert1.setMessage("Are you sure you want to Bunk all subjects?");
                    alert1.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    for (int i = 0; i < subjectArray.size(); i++) {
                                        subjectArray.get(i).setTotal(
                                                subjectArray.get(i).getTotal() + 1);
                                    }
                                    increase();

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
                }
                break;

            case R.id.action_settings:
                displayInterstitial();
                Intent is = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(is);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        readstring();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();

    }

    @SuppressLint("NewApi")
    public void startnext() {
        Intent ii = new Intent(MainActivity.this, InfoActivity.class);
        View view = getWindow().getDecorView().findViewById(
                android.R.id.content);
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(view, 0,
                0, view.getWidth(), view.getHeight());
        startActivity(ii, options.toBundle());
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        // This isn't ever called!
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
                // DO SOMETHING

                AlertDialog.Builder alert4 = new AlertDialog.Builder(this);
                alert4.setTitle("Edit the subject details:");

                final int subjectnumber = (int) info.id;
                Subject es = subjectArray.get(subjectnumber);

                final EditText input4 = new EditText(this);
                final EditText input5 = new EditText(this);

                input4.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                input5.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

                final TextView texta = new TextView(this);
                final TextView textt = new TextView(this);

                texta.setText("    Attended:   ");
                textt.setText("    Total:          ");

                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    // only for gingerbread and newer versions
                    texta.setTextColor(Color.WHITE);
                    textt.setTextColor(Color.WHITE);
                    input4.setTextColor(Color.WHITE);
                    input5.setTextColor(Color.WHITE);
                }

                texta.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                textt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                input4.setInputType(InputType.TYPE_CLASS_NUMBER);
                input4.setText("" + es.a);

                input5.setInputType(InputType.TYPE_CLASS_NUMBER);
                input5.setText("" + es.t);

                LinearLayout l1 = new LinearLayout(this);
                l1.setOrientation(LinearLayout.HORIZONTAL);
                l1.addView(texta);
                l1.addView(input4);

                LinearLayout l2 = new LinearLayout(this);
                l2.setOrientation(LinearLayout.HORIZONTAL);
                l2.addView(textt);
                l2.addView(input5);

                LinearLayout l4 = new LinearLayout(this);
                l4.setOrientation(LinearLayout.VERTICAL);
                l4.addView(l1);
                l4.addView(l2);

                alert4.setView(l4);

			/*
             * RelativeLayout esrela =
			 * (RelativeLayout)findViewById(R.layout.editsubject); (ViewGroup)
			 * getCurrentFocus() LayoutInflater inflater = getLayoutInflater();
			 * View dialoglayout = inflater.inflate(R.layout.editsubject, null)
			 * alert4.setView(dialoglayout); EditText
			 * es=(EditText)findViewById(R.id.edita); es.setFocusable(true);
			 * es.setClickable(true); es.setEnabled(true);
			 */

                alert4.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                Subject es = subjectArray.get(subjectnumber);

                                try {
                                    int esa = Integer.parseInt(input4.getText()
                                            .toString());
                                    int est = Integer.parseInt(input5.getText()
                                            .toString());
                                    if (esa <= est) {
                                        es.a = esa;
                                        es.t = est;
                                        increase();
                                    } else {
                                        Toast.makeText(MainActivity.this,
                                                "That doesn't seem right!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } catch (NumberFormatException ne) {
                                    Toast.makeText(MainActivity.this,
                                            "Please enter numbers in both fields.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                alert4.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // Canceled.
                            }
                        });
                alert4.show();
                // increase();

			/*
			 * Toast.makeText(MainActivity.this, "EDIT CLICKED",
			 * Toast.LENGTH_LONG) .show();
			 */

                return true;
            case R.id.delete:
			/*
			 * Toast.makeText(MainActivity.this, "DELETE CLICKED on " + (int)
			 * info.id, Toast.LENGTH_LONG) .show();
			 */
                subjectArray.remove((int) info.id);
                increase();
                return true;
            case R.id.editname:
                AlertDialog.Builder alert5 = new AlertDialog.Builder(this);
                alert5.setTitle("Edit new subject name:");

                final EditText input6 = new EditText(this);

                input6.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

                final TextView texts = new TextView(this);

                texts.setText(" Subject Name:");

                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    // only for gingerbread and newer versions
                    texts.setTextColor(Color.WHITE);
                    input6.setTextColor(Color.WHITE);
                }

                texts.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                final int subjectnumber2 = (int) info.id;
                Subject es2 = subjectArray.get(subjectnumber2);

                input6.setInputType(InputType.TYPE_CLASS_TEXT);
                input6.setText("" + es2.n);

                LinearLayout l3 = new LinearLayout(this);
                l3.setOrientation(LinearLayout.HORIZONTAL);
                l3.addView(texts);
                l3.addView(input6);

                LinearLayout l5 = new LinearLayout(this);
                l5.setOrientation(LinearLayout.VERTICAL);
                l5.addView(l3);

                alert5.setView(l5);

			/*
			 * RelativeLayout esrela =
			 * (RelativeLayout)findViewById(R.layout.editsubject); (ViewGroup)
			 * getCurrentFocus() LayoutInflater inflater = getLayoutInflater();
			 * View dialoglayout = inflater.inflate(R.layout.editsubject, null)
			 * alert4.setView(dialoglayout); EditText
			 * es=(EditText)findViewById(R.id.edita); es.setFocusable(true);
			 * es.setClickable(true); es.setEnabled(true);
			 */

                alert5.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                Subject es2 = subjectArray.get(subjectnumber2);
                                es2.setName(input6.getText().toString());
                                increase();
                            }
                        });
                alert5.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // Canceled.
                            }
                        });
                alert5.show();
                increase();

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void increase() {
        createstring();
        subjectAdapter.notifyDataSetChanged();
        BackupManager bm = new BackupManager(this);
        bm.dataChanged();
    }

    public void createstring() {
        String builder = null;
        for (int j = 0; j < subjectArray.size(); j++) {
            Subject s = subjectArray.get(j);
            if (builder != null)
                builder = builder + s.getName() + "\n" + s.getAttended() + "\n"
                        + s.getTotal() + "\n";
            if (builder == null)
                builder = s.getName() + "\n" + s.getAttended() + "\n"
                        + s.getTotal() + "\n";
        }
        try {
            synchronized (MainActivity.sDataLock) {
                String filename = "attendance";
                FileOutputStream outputStream;
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(builder.getBytes());
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Toast.makeText(MainActivity.this,builder, Toast.LENGTH_SHORT).show();
    }

    public void readstring() {
        subjectArray.clear();
        try {
            synchronized (MainActivity.sDataLock) {
                String filename = "attendance";
                FileInputStream fis = openFileInput(filename);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                int i = 0, a = 0, t = 0;
                String sname = null;
                while ((line = bufferedReader.readLine()) != null) {
                    // Toast.makeText(MainActivity.this,line,
                    // Toast.LENGTH_LONG).show();
                    if (i % 3 == 0)
                        sname = line;
                    if (i % 3 == 1)
                        a = Integer.parseInt(line);
                    if (i % 3 == 2) {
                        t = Integer.parseInt(line);
                        subjectArray.add(new Subject(sname, a, t));
                    }
                    sb.append(line);
                    i++;
                }
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
        } finally {
        }
        increase();
    }
}