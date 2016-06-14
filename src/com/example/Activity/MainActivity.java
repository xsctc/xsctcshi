package com.example.Activity;

import com.example.Fragment.FragmentMain;
import com.example.xsctcshi.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

public class MainActivity extends FragmentActivity {
	private FragmentTransaction fragmentTransaction = getSupportFragmentManager()
			.beginTransaction();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		fragmentTransaction.replace(R.id.activity_main, new FragmentMain());
		fragmentTransaction.commit();
	}

}