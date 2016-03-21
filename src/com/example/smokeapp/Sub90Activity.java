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
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class Sub90Activity extends Activity implements View.OnTouchListener {
	private ChartView chartView1_30;
	private ChartView chartView31_60;
	private ChartView chartView61_90;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be full screen*/
        setContentView(R.layout.activity_sub_90);
        ((ImageButton)findViewById(R.id.imageButtonSub90Return)).setOnTouchListener(this);   
        chartView1_30 = new ChartView(this);
        chartView31_60 = new ChartView(this);
        chartView61_90 = new ChartView(this);
        ((FrameLayout)findViewById(R.id.frameLayoutSubData1_30)).addView(chartView1_30);
        ((FrameLayout)findViewById(R.id.frameLayoutSubData31_60)).addView(chartView31_60);
        ((FrameLayout)findViewById(R.id.frameLayoutSubData61_90)).addView(chartView61_90);
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	BlueTooth.getInstance().getOpenLog(new Handler() {
        	@Override
        	public void handleMessage(Message msg) {
        		// TODO Auto-generated method stub
        		OpenLog openLog = (OpenLog)msg.obj;
            	float[] data = openLog.get90();
            	float[][] dataAll = new float[3][30];
            	for (int i =0; i<90; i++) {
            		dataAll[i/30][i%30] = data[i];
            		
            	}
            	chartView1_30.setData(1.0f, dataAll[0]);
            	chartView31_60.setData(1.0f, dataAll[1]);
            	chartView61_90.setData(1.0f, dataAll[2]);
                //chartView.setData(1.0f, data);
        	}
        });
//    	float[] data = {0.1f,0.2f,0.3f,0.4f,0.5f,0.6f,0.7f,0.8f,0.9f,0.9f,0.8f,0.2f,0.3f,0.4f,0.5f,0.6f,0.7f,0.8f,0.9f,0.9f,0.8f,0.2f,0.3f,0.4f,0.5f,0.6f,0.7f,0.8f,0.9f,0.9f,0.8f};
//    	chartView1_30.setData(1.0f, data);
//    	chartView31_60.setData(1.0f, data);
//    	chartView61_90.setData(1.0f, data);
    	super.onResume();
    }
    
    public boolean onTouch(View v, MotionEvent event) {   
    	
    	if(event.getAction()==MotionEvent.ACTION_DOWN){ 
    		v.getBackground().setColorFilter(utils.highLightColorFilter()); 
    	}
    	else if(event.getAction()==MotionEvent.ACTION_UP){  
    		v.getBackground().setColorFilter(utils.normalColorFilter()); 
	        switch (v.getId()) {  
	        	case R.id.imageButtonSub90Return:
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
