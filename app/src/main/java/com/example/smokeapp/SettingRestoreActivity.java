package com.example.smokeapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.os.Vibrator;
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

public class SettingRestoreActivity extends Activity implements View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be full screen*/
        setContentView(R.layout.activity_setting_restore);
        ((ImageButton)findViewById(R.id.imageButtonSettingRestoreConfirm)).setOnTouchListener(this);
        ((ImageButton)findViewById(R.id.imageButtonSettingRestoreReturn)).setOnTouchListener(this);
    }
    
    public boolean onTouch(View v, MotionEvent event) {   
    	
    	if(event.getAction()==MotionEvent.ACTION_DOWN){ 
    		v.getBackground().setColorFilter(utils.highLightColorFilter()); 
    	}
    	else if(event.getAction()==MotionEvent.ACTION_UP){  
    		v.getBackground().setColorFilter(utils.normalColorFilter()); 
	        switch (v.getId()) {  
	        	case R.id.imageButtonSettingRestoreConfirm:
	        		finish();
	        		break;
	        	case R.id.imageButtonSettingRestoreReturn:
	        		finish();
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
