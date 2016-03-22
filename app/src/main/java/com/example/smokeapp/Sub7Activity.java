package com.example.smokeapp;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Sub7Activity extends Activity implements View.OnTouchListener {
	private ChartView chartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be full screen*/
        setContentView(R.layout.activity_sub_7);
        ((ImageButton)findViewById(R.id.imageButtonSub7Return)).setOnTouchListener(this);   
        FrameLayout layout = (FrameLayout)findViewById(R.id.frameLayoutSub7DataContent);
        
        chartView = new ChartView(this);
        
        layout.addView(chartView);
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	BlueTooth.getInstance().getOpenLog(new Handler() {
        	@Override
        	public void handleMessage(Message msg) {
        		// TODO Auto-generated method stub
        		OpenLog openLog = (OpenLog)msg.obj;
            	float[] data = openLog.get7();
                chartView.setData(1.0f, data);
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
	        	case R.id.imageButtonSub7Return:
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
        return true;
    }
}
