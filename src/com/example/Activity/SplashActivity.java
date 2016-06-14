package com.example.Activity;

import com.example.xsctcshi.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashActivity extends Activity {
	private static final int HOME = 1;
	private static final int LOGIN = 2;
	private Boolean isfirstin = false;
	private static final int TIME = 2000;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HOME:
				gohome();
				break;
			case LOGIN:

				gologin();
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		init();
	}

	protected void gologin() {

		Intent i = new Intent(SplashActivity.this, GuideActivity.class);
		startActivity(i);
		finish();
	}

	protected void gohome() {
		Intent i = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(i);
		finish();

	}

	private void init() {
		SharedPreferences sp = getSharedPreferences("laosi", MODE_PRIVATE);
		isfirstin = sp.getBoolean("isfirstin", true);
		if (!isfirstin) {
			handler.sendEmptyMessageDelayed(HOME, TIME);
		} else {
			handler.sendEmptyMessageDelayed(LOGIN, TIME);
			Editor editor = sp.edit();
			editor.putBoolean("isfirstin", false);
			editor.commit();
		}
	}

}
