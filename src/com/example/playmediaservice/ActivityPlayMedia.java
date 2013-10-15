package com.example.playmediaservice;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ActivityPlayMedia extends Activity implements OnClickListener{

	// VARIABLES
	public static String TAG = "ActivityPlayMedia";
	
	private BroadcastReceiver receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {

			Bundle extra = intent.getExtras();
			if(extra != null){
				String message = extra.getString("Message");
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			}
		}
		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_play_media);
		
		Button btn_startService = (Button) findViewById(R.id.btn_startService);
		Button btn_stopService = (Button) findViewById(R.id.btn_stopService);
		
		btn_startService.setOnClickListener(this);
		btn_stopService.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_play_media, menu);
		return true;
	}

	@Override
	public void onResume(){
		super.onResume();
		registerReceiver(receiver, new IntentFilter("abc"));
	}
	
	@Override 
	public void onPause(){
		super.onPause();
		unregisterReceiver(receiver);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btn_startService){
			startService(new Intent(ActivityPlayMedia.this, PlayMediaService.class));			
		}
		else{
			stopService(new Intent(ActivityPlayMedia.this, PlayMediaService.class));
		}
	}

}
