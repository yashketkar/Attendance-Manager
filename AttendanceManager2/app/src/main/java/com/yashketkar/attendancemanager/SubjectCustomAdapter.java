package com.yashketkar.attendancemanager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


public class SubjectCustomAdapter extends ArrayAdapter<Subject> {
	Context context;
	int layoutResourceId;
	ArrayList<Subject> data = new ArrayList<Subject>();
	MainActivity act;

	public static final String PREFS_NAME = "MyPrefsFile";

	public SubjectCustomAdapter(Context context, int layoutResourceId,
			ArrayList<Subject> data, MainActivity act) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		this.act = act;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		SubjectHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new SubjectHolder();
			holder.textSubject = (TextView) row.findViewById(R.id.textSubject);
			holder.buttonAttend = (Button) row.findViewById(R.id.buttonAttend);
			holder.buttonBunk = (Button) row.findViewById(R.id.buttonBunk);
			row.setTag(holder);
		} else {
			holder = (SubjectHolder) row.getTag();
		}
		holder.buttonAttend.setTag(Integer.valueOf(position));
		holder.buttonBunk.setTag(Integer.valueOf(position));
		Subject subject = data.get(position);
		double a = (double) subject.getAttended();
		double t = (double) subject.getTotal();
		double percent = 100 * a / t;
		percent = percent * 100;
		percent = java.lang.Math.round(percent);
		percent = percent / 100;

		if (a == 0 && t == 0) {
			percent = 100;
		}
		SharedPreferences mPreferences = context.getSharedPreferences(
				PREFS_NAME, 0);

		int limit = mPreferences.getInt("limit", 75);
		if (percent < limit) {
			holder.textSubject.setTextColor(Color.RED);
		} else {
			holder.textSubject.setTextColor(Color.BLACK);
		}

		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"RobotoCondensed-Regular.ttf");
		holder.textSubject.setTypeface(face);
		holder.textSubject.setText(subject.getName()+" : " + percent + "%"
			    + "\nAttendance : " + subject.getAttended() + "/"
				+ subject.getTotal());
        //holder.textSubject.setText(Html.fromHtml("<h3>"+subject.getName()+" : "+
        //        percent+"%</h3>"+"<p>Attendance:"+subject.getAttended()+"/"+subject.getTotal()+"</p>"));
		holder.buttonAttend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Integer pos;
				pos = (Integer) v.getTag();
				Subject subject = data.get(pos);
				subject.setAttended(subject.getAttended() + 1);
				subject.setTotal(subject.getTotal() + 1);
				act.increase();
				// Toast.makeText(context, "Attend button No. " + pos
				// +" Clicked",
				// Toast.LENGTH_SHORT).show();
			}
		}

		);

		holder.buttonBunk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Integer pos;
				pos = (Integer) v.getTag();
				Subject subject = data.get(pos);
				subject.setTotal(subject.getTotal() + 1);
				act.increase();
				// Toast.makeText(context, "Bunk button No. " + pos +" Clicked",
				// Toast.LENGTH_SHORT).show();
			}
		});

		/*
		 * Animation animation = AnimationUtils.loadAnimation(context,
		 * R.anim.subject_tour_flipper_enter); animation.setDuration(5000);
		 * row.startAnimation(animation); animation = null;
		 * Toast.makeText(context, "Done",Toast.LENGTH_SHORT).show();
		 */
		return row;
	}

	static class SubjectHolder {
		TextView textSubject;
		Button buttonAttend;
		Button buttonBunk;
	}
}