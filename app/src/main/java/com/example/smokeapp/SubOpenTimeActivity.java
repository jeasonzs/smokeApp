package com.example.smokeapp;
import java.util.prefs.Preferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class SubOpenTimeActivity extends Activity implements View.OnTouchListener {
	
	public static final String PREF_KEY_NEXT_OPEN_TIME= "prefKeyNextOpenTime";
	private static String strTime = "";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be full screen*/
        setContentView(R.layout.activity_sub_open_time);
        ((ImageButton)findViewById(R.id.imageButtonSubOpenTimeReturn)).setOnTouchListener(this); 
//        String time = utils.getPrefString(PREF_KEY_NEXT_OPEN_TIME);
//        Log.v("smoke","time="+time); 
        
    }
    
    @Override
    protected void onResume() {
    	utils.checkAndOpenBlueTooth(this);
    	((TextView)findViewById(R.id.textViewSubOpenTimeDataContent)).setText(strTime);
    	// TODO Auto-generated method stub
    	BlueTooth.getInstance().getNextOpenTime(new Handler() {
        	@Override
        	public void handleMessage(Message msg) {
        		// TODO Auto-generated method stub
        		int time = msg.arg1&0xff;
        		strTime = String.valueOf(time);
        		((TextView)findViewById(R.id.textViewSubOpenTimeDataContent)).setText(strTime);
//        		utils.setPrefString(PREF_KEY_NEXT_OPEN_TIME, strTime);
        	}
        });
    	super.onResume();
    }
    
    public boolean onTouch(View v, MotionEvent event) {   
    	
    	if(event.getAction()==MotionEvent.ACTION_DOWN){ 
    		v.getBackground().setColorFilter(utils.highLightColorFilter()); 
    	}
    	else if(event.getAction()==MotionEvent.ACTION_UP){  
    		v.getBackground().setColorFilter(utils.normalColorFilter()); 
	        switch (v.getId()) {  
	        	case R.id.imageButtonSubOpenTimeReturn:
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
