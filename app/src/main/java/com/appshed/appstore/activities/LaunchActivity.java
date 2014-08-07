package com.appshed.appstore.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.appshed.appstore.R;

/**
 * Created by Anton Maniskevich on 8/7/14.
 */
public class LaunchActivity extends Activity {

	private static final String TAG = LaunchActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		if (getIntent().getExtras() != null) {
			String text = getIntent().getData().toString();
			((TextView)findViewById(R.id.txt_text)).setText(text);
		}
	}
}
