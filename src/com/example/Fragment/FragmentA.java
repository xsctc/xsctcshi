package com.example.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.Activity.data_analysis;
import com.example.Activity.data_get;
import com.example.Activity.data_search;
import com.example.adapter.Fraga_Adapter;
import com.example.xsctcshi.MyActivity;
import com.example.xsctcshi.R;
import com.example.xsctcshi.welcome;





import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentA extends Fragment implements OnPageChangeListener {
	private LinearLayout pointllayout;
	private int count;
	private ImageView[] imgs;
	private int currentItem;
	private ViewPager mViewPager;
	private View view1, view2, view3, view4, view5;
	private List<View> list;
	public Boolean flag = false;
	private ListView listView;
	private Fraga_Adapter adapter;
	private Button button_fraga1,button_fraga2,button_fraga3,button_fraga4;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_a, container, false);
		pointllayout = (LinearLayout) view.findViewById(R.id.llayout_fraga);
		count = pointllayout.getChildCount();
		imgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			imgs[i] = (ImageView) pointllayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		button_fraga1=(Button) view.findViewById(R.id.button_fraga1);
		button_fraga2=(Button) view.findViewById(R.id.button_fraga2);
		button_fraga3=(Button) view.findViewById(R.id.button_fraga3);
		button_fraga1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(getActivity(),data_get.class);
				startActivity(i);

			}
		});
		button_fraga2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(getActivity(),data_search.class);
				startActivity(i);

			}
		});
		button_fraga3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(getActivity(),data_analysis.class);
				startActivity(i);

			}
		});
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager_fragmenta);
		mViewPager.setOnPageChangeListener(this);
		list = new ArrayList<View>();
		initpage(inflater);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		listView=(ListView) view.findViewById(R.id.fraga_listview);
		adapter = new Fraga_Adapter(getActivity());
		listView.setAdapter(adapter);

	}

	private void initpage(LayoutInflater inflater) {
		view1 = inflater.inflate(R.layout.loading1, null);
		view2 = inflater.inflate(R.layout.loading2, null);
		view3 = inflater.inflate(R.layout.loading3, null);
		view4 = inflater.inflate(R.layout.loading3, null);
		view5 = inflater.inflate(R.layout.loading3, null);
		list.add(view1);
		list.add(view2);
		list.add(view3);
		list.add(view4);
		list.add(view5);

		mViewPager.setAdapter(pager);
	}

	PagerAdapter pager = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(list.get(position));

			return list.get(position);
		}
	};

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		setcurrentpoint(arg0);
	}

	private void setcurrentpoint(int position) {
		if (position < 0 || position > count - 1 || position == currentItem)
			return;
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;
	}

}