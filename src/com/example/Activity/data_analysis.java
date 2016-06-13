package com.example.Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.xsctcshi.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
public class data_analysis extends Activity {
	String a1;
	String a2;
	String a3;
	String a4;
	String a5;
	String a6;
	String a7;
	float tem_res;
	float hum_res;
	float O1=0;
	float O2=0;
	static float[] xmin = {(float) 8.35,(float) 35.45,0,0,(float) 16.9,(float) 42.8};
	static float[] xmax = {31,(float) 99.35,(float) 1110.7,(float) 5.9,(float) 26.1,(float) 44.1};
	// String jsonData ;
	String a;
	public static final String GET_NEWS_URL = "http://10.108.244.197/111.php";
	private TextView data_read;
	private TextView data_read2;
	private TextView tem_result;
	private TextView hum_result;
	private int m_year, m_month, m_day, m_hour, m_min;
	private EditText DateEdit;
	private EditText TimeEdit;
//	private EditText tem;
//	private EditText hum;
	private EditText tem1;
	private EditText tem2;
	private EditText tem3;
	private EditText tem4;
	private EditText tem5;
	private EditText tem6;
	private EditText tem7;
	private EditText tem8;
	private EditText tem9;
	private EditText tem10;
	private EditText hum1;
	private EditText hum2;
	private EditText hum3;
	private EditText hum4;
	private EditText hum5;
	private EditText hum6;
	private EditText hum7;
	private EditText hum8;
	private EditText hum9;
	private EditText hum10;
	private Button DateBtn;
	private Button TimeBtn;
	private Button send;
	private Button show;
	private Calendar c;
	private Handler getNewsHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String jsonData = (String) msg.obj;
			System.out.println(jsonData);

			try {
				JSONObject jsonobject = new JSONObject(jsonData);
				a1 = jsonobject.getString("title");
				a2 = jsonobject.getString("desc");
				data_read.setText(a1);
				data_read2.setText(a2);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_analysis);
		data_read = (TextView) findViewById(R.id.data_read);
		data_read2 = (TextView) findViewById(R.id.data_read2);
		send = (Button) findViewById(R.id.anal_send);
		show = (Button) findViewById(R.id.anal_show);
		DateBtn = (Button) findViewById(R.id.anal_DateBtn);
		DateEdit = (EditText) findViewById(R.id.anal_DateEdit);
		TimeBtn = (Button) findViewById(R.id.anal_TimeBtn);
		TimeEdit = (EditText) findViewById(R.id.anal_TimeEdit);
		tem1= (EditText) findViewById(R.id.anal_tem1);
		tem2= (EditText) findViewById(R.id.anal_tem2);
		tem3= (EditText) findViewById(R.id.anal_tem3);
		tem4= (EditText) findViewById(R.id.anal_tem4);
		tem5= (EditText) findViewById(R.id.anal_tem5);
		tem6= (EditText) findViewById(R.id.anal_tem6);
		tem7= (EditText) findViewById(R.id.anal_tem7);
		tem8= (EditText) findViewById(R.id.anal_tem8);
		tem9= (EditText) findViewById(R.id.anal_tem9);
		tem10= (EditText) findViewById(R.id.anal_tem10);
		tem_result=(TextView) findViewById(R.id.tem_result);
		hum1 = (EditText) findViewById(R.id.anal_hum1);
		hum2 = (EditText) findViewById(R.id.anal_hum2);
		hum3 = (EditText) findViewById(R.id.anal_hum3);
		hum4 = (EditText) findViewById(R.id.anal_hum4);
		hum5 = (EditText) findViewById(R.id.anal_hum5);
		hum6 = (EditText) findViewById(R.id.anal_hum6);
		hum7 = (EditText) findViewById(R.id.anal_hum7);
		hum8 = (EditText) findViewById(R.id.anal_hum8);
		hum9 = (EditText) findViewById(R.id.anal_hum9);
		hum10 = (EditText) findViewById(R.id.anal_hum10);
		hum_result=(TextView) findViewById(R.id.hum_result);
		DateBtn.setOnClickListener(new DateClick());
		TimeBtn.setOnClickListener(new TimeClick());
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Thread t = new Thread() {
					public void run() {
						UpLoad();
						}
					
				};
				t.start();
				
			}
		};
		
		OnClickListener listener1 = new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Thread e=new Thread(){
					public void run()
					{
						Calculate();
					}
				};
				e.start();
				
		}
		};
		// data_read2.setText(title);
		c = Calendar.getInstance();// 获取日历的实例
		m_year = c.get(Calendar.YEAR);// 年
		m_month = c.get(Calendar.MONTH);// 月
		m_day = c.get(Calendar.DAY_OF_MONTH);// 日
		m_hour = c.get(Calendar.HOUR_OF_DAY);
		m_min = c.get(Calendar.MINUTE);
		send.setOnClickListener(listener);
		show.setOnClickListener(listener1);
		// HttpUtils.getNewJson(GET_NEWS_URL, getNewsHandler);
	}

	class DateClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			showDialog(0);
		}

	}

	class TimeClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			showDialog(1);
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 0) {
			return new DatePickerDialog(this, l1, m_year, m_month, m_day);
		} else if (id == 1) {
			return new TimePickerDialog(this, l2, m_hour, m_min, true);
		} else
			return null;
	}

	private OnDateSetListener l1 = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			m_year = year;
			m_month = monthOfYear;
			m_day = dayOfMonth;
			DateEdit.setText(m_year + "-" +(m_month+1)  + "-" + m_day);

		}
	};
	private OnTimeSetListener l2 = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hour, int min) {
			m_hour = hour;
			m_min = min;
			TimeEdit.setText(m_hour + "时" + m_min + "分");

		}

	};
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			data_read.setText(a6);
			data_read2.setText(a7);
			// Log.v("ls_LOG_TAG", "tem");
			// dis.setText(smsg); //显示数据
			// sv.scrollTo(0,dis.getMeasuredHeight()); //跳至数据最后一页
		}
	};
	Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tem_result.setText(String.valueOf(tem_res));
			hum_result.setText(String.valueOf(hum_res));
//			tem.setText(String.valueOf(O1));
//			hum.setText(String.valueOf(O2));
		}
	};
	private void UpLoad() {
		try {
			HttpClient client = new DefaultHttpClient();


			HttpPost post = new HttpPost(GET_NEWS_URL);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("m_year", String.valueOf(m_year)));
			nvps.add(new BasicNameValuePair("m_month", String.valueOf(m_month)));
			nvps.add(new BasicNameValuePair("m_day", String.valueOf(m_day)));
			nvps.add(new BasicNameValuePair("m_hour", String.valueOf(m_hour)));
			nvps.add(new BasicNameValuePair("m_min", String.valueOf(m_min)));
			post.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse httpResponse = client.execute(post);
			HttpEntity entity = httpResponse.getEntity();
			String result = EntityUtils.toString(entity);
			Log.i("result", result);
			try {
				JSONObject jsonobject = new JSONObject(result);
				a1 = jsonobject.getString("1");
				a2 = jsonobject.getString("2");
				a3 = jsonobject.getString("3");
				a4 = jsonobject.getString("4");
				a5 = jsonobject.getString("5");
				a6 = jsonobject.getString("6");
				a7 = jsonobject.getString("7");

				handler.sendMessage(handler.obtainMessage());

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private void Calculate() {
		float[] Xtem={0,0,0,0,0,0,0,0,0,0};
		float[] Xhum={0,0,0,0,0,0,0,0,0,0};
		float alpha=(float) 0.05;
		float[] Stem={0,0,0,0,0,0,0,0,0,0,0};
		float[] Shum={0,0,0,0,0,0,0,0,0,0,0};
		Xtem[0]=Float.parseFloat(tem1.getText().toString());
		Xtem[1]=Float.parseFloat(tem2.getText().toString());
		Xtem[2]=Float.parseFloat(tem3.getText().toString());
		Xtem[3]=Float.parseFloat(tem4.getText().toString());
		Xtem[4]=Float.parseFloat(tem5.getText().toString());
		Xtem[5]=Float.parseFloat(tem6.getText().toString());
		Xtem[6]=Float.parseFloat(tem7.getText().toString());
		Xtem[7]=Float.parseFloat(tem8.getText().toString());
		Xtem[8]=Float.parseFloat(tem9.getText().toString());
		Xtem[9]=Float.parseFloat(tem10.getText().toString());
		Stem[0]=Xtem[0];
		for(int i=1;i<=10;i++){
			Stem[i]=(float)(alpha*Xtem[i-1]+(1-alpha)*Stem[i-1]);
		}
		Xhum[0]=Float.parseFloat(hum1.getText().toString());
		Xhum[1]=Float.parseFloat(hum2.getText().toString());
		Xhum[2]=Float.parseFloat(hum3.getText().toString());
		Xhum[3]=Float.parseFloat(hum4.getText().toString());
		Xhum[4]=Float.parseFloat(hum5.getText().toString());
		Xhum[5]=Float.parseFloat(hum6.getText().toString());
		Xhum[6]=Float.parseFloat(hum7.getText().toString());
		Xhum[7]=Float.parseFloat(hum8.getText().toString());
		Xhum[8]=Float.parseFloat(hum9.getText().toString());
		Xhum[9]=Float.parseFloat(hum10.getText().toString());
		Shum[0]=Xhum[0];
		for(int i=1;i<=10;i++){
			Shum[i]=(float)(alpha*Xhum[i-1]+(1-alpha)*Shum[i-1]);
		}
		tem_res=Stem[10];
		hum_res=Shum[10];		
		handler1.sendMessage(handler1.obtainMessage());
	}

}