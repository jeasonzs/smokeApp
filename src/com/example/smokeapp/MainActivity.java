package com.example.smokeapp;


import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.smokeapp.Sub24Activity;


public class MainActivity extends Activity implements View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*set it to be full screen*/
        setContentView(R.layout.activity_main);
        findViewById(R.id.imageButtonMainCancle).setOnTouchListener(this);
        findViewById(R.id.imageButtonMain24).setOnTouchListener(this);
        findViewById(R.id.imageButtonMainOpenTime).setOnTouchListener(this);
        findViewById(R.id.imageButtonMainHelp).setOnTouchListener(this);
        findViewById(R.id.imageButtonMain7).setOnTouchListener(this);
        findViewById(R.id.imageButtonMain90).setOnTouchListener(this);
        findViewById(R.id.imageButtonMainSetting).setOnTouchListener(this);    
    }
    
    
 
    
    @Override  
    public boolean onTouch(View v, MotionEvent event) {   
    	
    	if(event.getAction()==MotionEvent.ACTION_DOWN){ 
    		v.getBackground().setColorFilter(utils.highLightColorFilter()); 
    	}
    	else if(event.getAction()==MotionEvent.ACTION_UP){  
    		v.getBackground().setColorFilter(utils.normalColorFilter()); 
	        switch (v.getId()) {  
	        	case R.id.imageButtonMainCancle:
	        		System.exit(0);
	        		break;
	        	case R.id.imageButtonMain24:
	                startActivity(new Intent(MainActivity.this,Sub24Activity.class));
		            break;
	        	case R.id.imageButtonMainOpenTime:
	                startActivity(new Intent(MainActivity.this,SubOpenTimeActivity.class));
	                break;
	        	case R.id.imageButtonMainHelp:
	                startActivity(new Intent(MainActivity.this,SubHelpActivity.class));
	                break;
	        	case R.id.imageButtonMain7: 
	                startActivity(new Intent(MainActivity.this,Sub7Activity.class));
	                break;
	        	case R.id.imageButtonMain90:
	                startActivity(new Intent(MainActivity.this,Sub90Activity.class));
	                break;
	        	case R.id.imageButtonMainSetting:
	                startActivity(new Intent(MainActivity.this,SubSettingActivity.class));
	                break;
	        } 
    	}
        return true;
    } 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
