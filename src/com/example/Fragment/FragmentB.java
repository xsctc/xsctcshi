package com.example.Fragment;


import com.example.Activity.paizhao;
import com.example.Activity.photo_search;
import com.example.xsctcshi.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FragmentB extends Fragment {
	private Button button_fragb1;
	private Button button_fragb2;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_b, container, false);
		button_fragb1=(Button) view.findViewById(R.id.button_fragb1);
		button_fragb1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(getActivity(),paizhao.class);
				startActivity(i);

			}
		});
		button_fragb2=(Button) view.findViewById(R.id.button_fragb2);
		button_fragb2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(getActivity(),photo_search.class);
				startActivity(i);

			}
		});
		return view;
	}}