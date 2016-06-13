package com.example.Activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;


import com.example.xsctcshi.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class paizhao extends Activity{
	private Button button;

	private Button button_gallery;
	private Button button_send;
	private ImageView imageView;
	private File mCurrentPhotoFile=null;
	private Bitmap cameraBitmap;
	private static final int RequestCode=1;
	private static final int GALLERY_RequestCode=2;
	private Bitmap send_Bitmap=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paizhao);
		button = (Button) findViewById(R.id.button_image);
		imageView = (ImageView) findViewById(R.id.imageView);

		button_gallery=(Button)findViewById(R.id.button_gallery);
		button_send=(Button)findViewById(R.id.button_send);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageView.setVisibility(View.VISIBLE);
				File dir=new File(Environment.getExternalStorageDirectory(),"pictures");
				if(!dir.exists())
				{
					dir.mkdirs();
				}
				
				mCurrentPhotoFile = new File(dir,System.currentTimeMillis()+".jpg");
				if(!mCurrentPhotoFile.exists())
				{
					try {
						mCurrentPhotoFile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			    Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
//				Intent intent1=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
		        startActivityForResult(
				intent1,RequestCode);
			}

			
		});
		button_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView.setVisibility(View.VISIBLE);
				Intent intent2=new Intent(Intent.ACTION_GET_CONTENT);
				intent2.setType("image/*");
				startActivityForResult(intent2, GALLERY_RequestCode);
			}
			
			
		});
		button_send.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(send_Bitmap==null)
				{
					Toast.makeText(paizhao.this, "«Î—°‘Ò’’∆¨",Toast.LENGTH_LONG).show();
				}
				else
				{
							sendImage(send_Bitmap);
				}
				send_Bitmap=null;
			}
			
		});
	}

			

	private Uri saveBitmap(Bitmap bm)
	{
		return null;
		
	}
	private Uri converturi(Uri uri) {
		InputStream is=null;
		try {
			is=getContentResolver().openInputStream(uri);
			Bitmap bitmap=BitmapFactory.decodeStream(is);
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case 1:
			Uri uri1;
			uri1=Uri.fromFile(mCurrentPhotoFile);
			imageView.setImageURI(uri1);

			Bitmap bm1 = null;
			try {
				bm1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri1);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			send_Bitmap=bm1;
/*			Bundle extras=data.getExtras();
			if(extras!=null)
			{
			Bitmap bm=extras.getParcelable("data");
			imageView.setImageBitmap(bm);
			send_Bitmap=bm;
			}*/

			break;
		case 2:
			if(data==null)
			{
				return;
			}
			Uri uri2;
			uri2=data.getData();
			imageView.setImageURI(uri2);

			Bitmap bm2 = null;
			try {
				bm2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri2);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			send_Bitmap=bm2;
			break;
		case 3:
			if (data != null) {
				cameraBitmap = (Bitmap) data.getExtras().get("data");
				imageView.setImageBitmap(cameraBitmap);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void sendImage(Bitmap bm)
	{
    	ByteArrayOutputStream stream=new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG,40, stream);
		byte[] bytes=stream.toByteArray();
		String img=new String(Base64.encodeToString(bytes, Base64.DEFAULT));
		

		AsyncHttpClient client=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.add("img", img);
		client.post("http://10.108.244.85/tupian.php", params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Toast.makeText(paizhao.this,"Upload Success!",Toast.LENGTH_SHORT).show();				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Toast.makeText(paizhao.this,"Upload Fail!",Toast.LENGTH_SHORT).show();				
			}
		});
	}
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";

	}
}