package com.example.xsctcshi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

public class MyActivity extends FragmentActivity {
	private FragmentManager fm = getSupportFragmentManager();
	private FragmentTransaction fragmentTransaction = getSupportFragmentManager()
			.beginTransaction();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mymain);
		fragmentTransaction.replace(R.id.fragmentmain, new MainActivity());

		fragmentTransaction.commit();
	}

}