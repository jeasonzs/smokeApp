package com.example.smokeapp;

import com.example.smokeapp.DeviceListActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SubSettingActivity extends Activity implements View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be full screen*/
        setContentView(R.layout.activity_sub_setting);
        ((ImageButton)findViewById(R.id.imageButtonSubSettingReturn)).setOnTouchListener(this); 
        ((ImageButton)findViewById(R.id.imageButtonSubSettingItemBlueTooth)).setOnTouchListener(this); 
        ((ImageButton)findViewById(R.id.imageButtonSubSettingItemOpenTime)).setOnTouchListener(this); 
        ((ImageButton)findViewById(R.id.imageButtonSubSettingItemRestore)).setOnTouchListener(this); 
        ((ImageButton)findViewById(R.id.imageButtonSubSettingItemAboutUs)).setOnTouchListener(this); 
    }
    

    
    public boolean onTouch(View v, MotionEvent event) {   
    	
    	if(event.getAction()==MotionEvent.ACTION_DOWN){ 
    		v.getBackground().setColorFilter(utils.highLightColorFilter()); 
    	}
    	else if(event.getAction()==MotionEvent.ACTION_UP){  
    		v.getBackground().setColorFilter(utils.normalColorFilter()); 
	        switch (v.getId()) {  
	        	case R.id.imageButtonSubSettingReturn:
	        		finish();
	        		//BlueTooth.getInstance().getNextOpenTime(null);
	        		break;
	        	case R.id.imageButtonSubSettingItemBlueTooth:
	                startActivityForResult(new Intent(this, DeviceListActivity.class), 2);
		            break;
	        	case R.id.imageButtonSubSettingItemOpenTime:
	                startActivity(new Intent(SubSettingActivity.this,SettingOpenTimeActivity.class));
		            break;
	        	case R.id.imageButtonSubSettingItemRestore:
	                startActivity(new Intent(SubSettingActivity.this,SettingRestoreActivity.class));
		            break;
	        	case R.id.imageButtonSubSettingItemAboutUs:
	                startActivity(new Intent(SubSettingActivity.this,SettingAboutUsActivity.class));
	        		//BlueTooth.getInstance().getOpenLog(null);
		            break;
	        } 
    	}
        return true;
    } 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Log.v("smoke","onCreateOptionsMenu menu 24"); 
        return true;
    }
}
