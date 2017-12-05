package com.darktheme;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.rarepebble.colorpicker.ColorPickerView;
import com.services.SampleOverlayService;

public class ActOverlayMain extends Activity {

	/** code to post/handler request for permission */
	public final static int REQUEST_CODE = -1010101;
	String hexColor = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_demo);
		ComponentName cn = getComponentName();

		final ColorPickerView picker = (ColorPickerView)findViewById(R.id.colorPicker);
		picker.setColor(0xffff0000);

		Button btnDone = (Button)findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				hexColor = String.format("#%08X", (0xFFFFFFFF & picker.getColor()));
			//	Toast.makeText(getApplicationContext(),"Color code  = "+hexColor,Toast.LENGTH_SHORT).show();



				char  code_first_char = hexColor.charAt(1);
				if(isNumeric(""+code_first_char))
				{
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						checkDrawOverlayPermission();;
					}
					else{
						SampleOverlayService.stop();
						stopService(new Intent(ActOverlayMain.this, SampleOverlayService.class));
						Toast.makeText(getApplicationContext(),"Color code =  '"+hexColor+"'  Applied.",Toast.LENGTH_SHORT).show();
						//SampleOverlayService sampleOverlayService = new SampleOverlayService();
						Intent intentService = new Intent(ActOverlayMain.this, SampleOverlayService.class);
						intentService.putExtra("colorCode",hexColor);
						startService(intentService);
					}



				}
				else
				{
					Toast.makeText(getApplicationContext(),"Color code =  '"+hexColor+"'  not applied, Please reduce transparency..",Toast.LENGTH_SHORT).show();
				}
			}
		});


		Button btnStop = (Button) findViewById(R.id.btnStop);
		btnStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SampleOverlayService.stop();
				stopService(new Intent(ActOverlayMain.this, SampleOverlayService.class));
				Toast.makeText(getApplicationContext(),"Stop com.overlay.",Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static boolean isNumeric(String str)
	{
		try
		{
			double d = Double.parseDouble(str);
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}

	public void checkDrawOverlayPermission() {
		// check if we already  have permission to draw over other apps
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (!Settings.canDrawOverlays(ActOverlayMain.this)) {
				// if not construct intent to request permission
				Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
						Uri.parse("package:" + getPackageName()));
				// request permission via start activity for result
				startActivityForResult(intent, REQUEST_CODE);
			}
			else
			{
				App.showLog("========checkDrawOverlayPermission=========else=======");

				SampleOverlayService.stop();
				stopService(new Intent(ActOverlayMain.this, SampleOverlayService.class));
				Toast.makeText(getApplicationContext(),"Color code =  '"+hexColor+"'  Applied.",Toast.LENGTH_SHORT).show();
				//SampleOverlayService sampleOverlayService = new SampleOverlayService();
				Intent intentService = new Intent(ActOverlayMain.this, SampleOverlayService.class);
				intentService.putExtra("colorCode",hexColor);
				startService(intentService);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
		// check if received result code is equal our requested code for draw permission
		if (requestCode == REQUEST_CODE) {
       // if so check once again if we have permission
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (Settings.canDrawOverlays(ActOverlayMain.this)) {
                    // continue here - permission was granted

					App.showLog("========REQUEST_CODE=========granted=======");

					SampleOverlayService.stop();
					stopService(new Intent(ActOverlayMain.this, SampleOverlayService.class));
					Toast.makeText(getApplicationContext(),"Color code =  '"+hexColor+"'  Applied.",Toast.LENGTH_SHORT).show();
					//SampleOverlayService sampleOverlayService = new SampleOverlayService();
					Intent intentService = new Intent(ActOverlayMain.this, SampleOverlayService.class);
					intentService.putExtra("colorCode",hexColor);
					startService(intentService);
                }
                else
				{
					App.showLog("========REQUEST_CODE=========allow permission=======");
				}
			}
		}
	}
}
