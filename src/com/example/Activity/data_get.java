package com.example.Activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import com.example.xsctcshi.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class data_get extends Activity{
	private final static int REQUEST_CONNECT_DEVICE = 1; // �궨���ѯ�豸���

	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB"; // SPP����UUID��

	private InputStream is; // ������������������������
	private TextView dis; // ����������ʾ���
	private ScrollView sv; // ��ҳ���
	private String smsg1 = ""; // ��ʾ�����ݻ����¶�
	private String smsg2 = ""; // ��ʾ�����ݻ���ʪ��
	private String fmsg = ""; // ���������ݻ���
	public OutputStream os;

	public String filename = ""; // ��������洢���ļ���
	BluetoothDevice _device = null; // �����豸
	BluetoothSocket _socket = null; // ����ͨ��socket
	boolean _discoveryFinished = false;
	boolean bRun = true;
	boolean bThread = false;
	private float tem;
	float hum;

	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter(); // ��ȡ�����������������������豸

	private List<String> soil_list = new ArrayList<String>();
	private List<String> crons_list = new ArrayList<String>();
	private List<String> depth_list = new ArrayList<String>();

	private Spinner soil_Spinner;
	private Spinner crons_Spinner;
	private Spinner depth_Spinner;
	private ArrayAdapter<String> soil_adapter;
	private ArrayAdapter<String> crons_adapter;
	private ArrayAdapter<String> depth_adapter;
	private Button DateBtn;
	private Button TimeBtn;
	private Button GPSBtn;
	private EditText DateEdit;
	private EditText TimeEdit;
	private EditText shujucaiji_tem;
	private EditText shujucaiji_hum;
	private EditText GPS_lon;
	private EditText GPS_lat;
	private int m_year, m_month, m_day, m_hour, m_min;
	private Calendar c;

	public LocationClient mLocClient = null;
	double locData_latitude;// ��λ��������
	double locData_longitude;
	StringBuffer sb = new StringBuffer(256);
	boolean isRequest = false;// �Ƿ��ֶ���������λ
	public MyLocationListenner myListener = new MyLocationListenner();
//	Db db =new Db(this);


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_get); // ���û���Ϊ������ main.xml

		soil_list.add("ɳ��");
		soil_list.add("ճ��");
		soil_list.add("����");
		crons_list.add("С��");
		crons_list.add("����");
		depth_list.add("10 cm");
		depth_list.add("20 cm");
		depth_list.add("30 cm");
		depth_list.add("40 cm");
		depth_list.add("50 cm");
		soil_Spinner = (Spinner) findViewById(R.id.spinner1);
		crons_Spinner = (Spinner) findViewById(R.id.spinner2);
		depth_Spinner = (Spinner) findViewById(R.id.spinner3);
		DateBtn = (Button) findViewById(R.id.DateBtn);
		DateEdit = (EditText) findViewById(R.id.DateEdit);
		TimeBtn = (Button) findViewById(R.id.TimeBtn);
		TimeEdit = (EditText) findViewById(R.id.TimeEdit);
		GPSBtn = (Button) findViewById(R.id.GPSBtn);
		shujucaiji_tem = (EditText) findViewById(R.id.shujucaiji_tem);
		shujucaiji_hum = (EditText) findViewById(R.id.shujucaiji_hum);
		GPS_lon = (EditText) findViewById(R.id.GPS_log);
		GPS_lat = (EditText) findViewById(R.id.GPS_lat);
		soil_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, soil_list);
		soil_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		crons_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, crons_list);
		crons_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		depth_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, depth_list);
		depth_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		soil_Spinner.setAdapter(soil_adapter);
		crons_Spinner.setAdapter(crons_adapter);
		depth_Spinner.setAdapter(depth_adapter);
		DateBtn.setOnClickListener(new DateClick());
		TimeBtn.setOnClickListener(new TimeClick());
		GPSBtn.setOnClickListener(new GPSClick());
		c = Calendar.getInstance();// ��ȡ������ʵ��
		m_year = c.get(Calendar.YEAR);// ��
		m_month = c.get(Calendar.MONTH);// ��
		m_day = c.get(Calendar.DAY_OF_MONTH);// ��
		m_hour = c.get(Calendar.HOUR_OF_DAY);
		m_min = c.get(Calendar.MINUTE);

		
		soil_Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		crons_Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		depth_Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setOpenGps(true);// ��gps
		mLocClient.setLocOption(option);
		mLocClient.start();
		// ///////////////////////////////////////////////////////////////////////////
		sv = (ScrollView) findViewById(R.id.ScrollView01); // �õ���ҳ���
//		dis = (TextView) findViewById(R.id.in); // �õ�������ʾ���

		// ����򿪱��������豸���ɹ�����ʾ��Ϣ����������
		if (_bluetooth == null) {
			Toast.makeText(this, "�޷����ֻ���������ȷ���ֻ��Ƿ����������ܣ�", Toast.LENGTH_LONG)
					.show();
			finish();
			return;
		}

		// �����豸���Ա�����
		/*
		 * new Thread(){ public void run(){ if(_bluetooth.isEnabled()==false){
		 * _bluetooth.enable(); } } }.start();
		 */
		Thread t = new Thread() {
			public void run() {
				if (_bluetooth.isEnabled() == false) {
					_bluetooth.enable();
				}
			}
		};
		t.start();

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

	class GPSClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			isRequest = true;
			mLocClient.requestLocation();// ���ö�λ

			GPS_lat.setText(String.valueOf(locData_latitude));
			GPS_lon.setText(String.valueOf(locData_longitude));
			Toast.makeText(data_get.this, "���ڶ�λ����", Toast.LENGTH_SHORT)
					.show();
			// Intent intent=new Intent(shujucaiji.this,GPS.class);
			// startActivity(intent);
		}

	}

	public class MyLocationListenner implements BDLocationListener {
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}

		// ����λ�Ժ󷵻ض�λ���
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			sb.append("latitude : ");
			sb.append(location.getLatitude());
			sb.append("lontitude : ");
			sb.append(location.getLongitude());
			locData_latitude = location.getLatitude();
			locData_longitude = location.getLongitude();
			// �������ʾ��λ����Ȧ����accuracy��ֵΪ0����

			// myLocationOverlay.setData(locData);
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
			DateEdit.setText(m_year + "-" + (m_month + 1) + "-" + m_day);

		}
	};
	private OnTimeSetListener l2 = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hour, int min) {
			m_hour = hour;
			m_month = min;
			TimeEdit.setText(m_hour + "ʱ" + m_min + "��");

		}

	};

	// ���Ͱ�����Ӧ
	public void onSendButtonClicked(View v) {
		try {
			os = _socket.getOutputStream(); // �������������
			byte bos[] = new byte[8];
			bos[0] = 0x01;
			bos[1] = 0x03;
			bos[2] = 0x00;
			bos[3] = 0x00;
			bos[4] = 0x00;
			bos[5] = 0x02;
			bos[6] = -0x3C;
			bos[7] = 0x0B;
			os.write(bos);

		} catch (IOException e) {
		}
	}

	// ���ջ�������ӦstartActivityForResult()
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE: // ���ӽ������DeviceListActivity���÷���
			// ��Ӧ���ؽ��
			if (resultCode == Activity.RESULT_OK) { // ���ӳɹ�����DeviceListActivity���÷���
				// MAC��ַ����DeviceListActivity���÷���
				String address = data.getExtras().getString(
						BluetoothList.EXTRA_DEVICE_ADDRESS);
				// �õ������豸���
				_device = _bluetooth.getRemoteDevice(address);

				// �÷���ŵõ�socket
				try {
					_socket = _device.createRfcommSocketToServiceRecord(UUID
							.fromString(MY_UUID));
				} catch (IOException e) {
					Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				}
				// ����socket
				Button btn = (Button) findViewById(R.id.Button03);
				try {
					_socket.connect();
					Toast.makeText(this, "����" + _device.getName() + "�ɹ���",
							Toast.LENGTH_SHORT).show();
					btn.setText("�Ͽ�");
				} catch (IOException e) {
					try {
						Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT)
								.show();
						_socket.close();
						_socket = null;
					} catch (IOException ee) {
						Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT)
								.show();
					}

					return;
				}

				// �򿪽����߳�
				try {
					is = _socket.getInputStream(); // �õ���������������
				} catch (IOException e) {
					Toast.makeText(this, "��������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
					return;
				}
				if (bThread == false) {
					ReadThread.start();
					bThread = true;
				} else {
					bRun = true;
				}
			}
			break;
		default:
			break;
		}
	}

	// ���������߳�
	Thread ReadThread = new Thread() {

		public void run() {
			byte[] buffer = new byte[1024];
			// float tem;
			// float hum;
			bRun = true;
			// �����߳�
			while (true) {
				try {
					while (is.available() == 0) {
						while (bRun == false) {
						}
					}
					// ������ʾ��Ϣ��������ʾˢ��
					while (true) {
						is.read(buffer);
						float a=buffer[2] * 256 + buffer[3];
						float b=buffer[4] * 256 + buffer[5];
						tem = a/100;
						hum = b/100;
						smsg1=String.valueOf(tem); 
						smsg2=String.valueOf(hum);
						if (is.available() == 0)
							break; // ��ʱ��û�����ݲ�����������ʾ
					}
					handler.sendMessage(handler.obtainMessage());
				} catch (IOException e) {
				}
			}
		}
	};
	// ��Ϣ�������
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			shujucaiji_tem.setText(smsg1 + "C");
			shujucaiji_hum.setText(smsg2 + "%");
//			Log.v("ls_LOG_TAG", "tem");
			// dis.setText(smsg); //��ʾ����
			// sv.scrollTo(0,dis.getMeasuredHeight()); //�����������һҳ
		}
	};

	// �رճ�����ô�����
	public void onDestroy() {
		super.onDestroy();
		if (_socket != null) // �ر�����socket
			try {
				_socket.close();
			} catch (IOException e) {
			}
		// _bluetooth.disable(); //�ر���������
	}

	// ���Ӱ�����Ӧ����
	public void onConnectButtonClicked(View v) {
		if (_bluetooth.isEnabled() == false) { // ����������񲻿�������ʾ
			Toast.makeText(this, " ��������...", Toast.LENGTH_LONG).show();
			return;
		}

		// ��δ�����豸���DeviceListActivity�����豸����
		Button btn = (Button) findViewById(R.id.Button03);
		if (_socket == null) {
			Intent serverIntent = new Intent(this, BluetoothList.class); // ��ת��������
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE); // ���÷��غ궨��
		} else {
			// �ر�����socket
			try {

				is.close();
				_socket.close();
				_socket = null;
				bRun = false;
				btn.setText("����");
			} catch (IOException e) {
			}
		}
		return;
	}

	// ���水����Ӧ����


	public void onUpLoadButtonClicked(View v) {
		UpLoad();
	}

	// ���������Ӧ����
	public void onClearButtonClicked(View v) {
		// smsg="";
		// fmsg="";
		// dis.setText(smsg);
		// return;
	}

	// �˳�������Ӧ����
	public void onQuitButtonClicked(View v) {
		finish();
	}



	private void UpLoad() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.add("tem", String.valueOf(tem));
		params.add("hum", String.valueOf(hum));
		client.post("http://10.108.244.95/pic.php", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Toast.makeText(data_get.this, "Upload Success!",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Toast.makeText(data_get.this, "Upload Fail!",
								Toast.LENGTH_SHORT).show();
					}
		});

	}
}