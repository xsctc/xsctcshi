package com.example.Activity;

import java.util.ArrayList;
import java.util.List;

import com.example.xsctcshi.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends Activity implements OnPageChangeListener {
	private LinearLayout pointllayout;
	private int count;
	private ImageView[] imgs;
	private int currentItem;
	private ViewPager mViewPager;
	private View view1, view2, view3;
	private List<View> list;
	public Boolean flag = false;
	private Button btn_start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		pointllayout = (LinearLayout) findViewById(R.id.llayout);
		count = pointllayout.getChildCount();
		imgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			imgs[i] = (ImageView) pointllayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setOnPageChangeListener(this);
		LayoutInflater inflater = LayoutInflater.from(GuideActivity.this);
		list = new ArrayList<View>();
		SharedPreferences sp = getSharedPreferences("loding", 0);
		flag = sp.getBoolean("loding_flag", false);
		initpage(inflater);
	}

	private void initpage(LayoutInflater inflater) {
		view1 = inflater.inflate(R.layout.activity_guide_loading1, null);
		view2 = inflater.inflate(R.layout.activity_guide_loading2, null);
		view3 = inflater.inflate(R.layout.activity_guide_loading3, null);

		list.add(view1);
		list.add(view2);
		list.add(view3);
		btn_start = (Button) view3.findViewById(R.id.btn_start);
		btn_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(GuideActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
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
