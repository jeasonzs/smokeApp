package com.example.smokeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Sub24Activity extends Activity implements View.OnTouchListener  {
	private TimeTableView timeTableView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be full screen*/
        setContentView(R.layout.activity_sub_24);
        ((ImageButton)findViewById(R.id.imageButtonSub24Return)).setOnTouchListener(this);    
        FrameLayout layout = (FrameLayout)findViewById(R.id.frameLayoutSub24DataContent);       
        timeTableView = new TimeTableView(this);
        layout.addView(timeTableView);
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	BlueTooth.getInstance().getOpenLog(new Handler() {
        	@Override
        	public void handleMessage(Message msg) {
        		// TODO Auto-generated method stub
        		OpenLog openLog = (OpenLog)msg.obj;
            	float[] data = openLog.get24();
            	timeTableView.setData(40, data);
        	}
        });

//    	float[] data = {0,0.1f,0.2f,0.3f,0.4f,0.5f,0.7f,0.9f,0.95f,1};
//    	timeTableView.setData(100, data);
    	super.onResume();
    }
    
    public boolean onTouch(View v, MotionEvent event) {   
    	
    	if(event.getAction()==MotionEvent.ACTION_DOWN){ 
    		v.getBackground().setColorFilter(utils.highLightColorFilter()); 
    	}
    	else if(event.getAction()==MotionEvent.ACTION_UP){  
    		v.getBackground().setColorFilter(utils.normalColorFilter()); 
	        switch (v.getId()) {  
	        	case R.id.imageButtonSub24Return:
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
