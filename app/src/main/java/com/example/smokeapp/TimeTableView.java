package com.example.smokeapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class TimeTableView extends ImageView {
	protected int totalNum = 0;
	float[] data;
	int datNum = 0;
	public TimeTableView(Context context) {
		super(context);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		// TODO Auto-generated constructor stub
	}
	
	public void setData(int totalNum,float[] data) {
		this.totalNum = totalNum;
		this.data = data;
		datNum = data.length;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(datNum > 0) {
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setAntiAlias(true);
			paint.setColor(0xFF4EB7CD);
			int width = canvas.getWidth();
			int height = canvas.getHeight();
			
			float per = width/(datNum+totalNum-1);

			Log.v("smoke","width="+String.valueOf(width)); 
			Log.v("smoke","per="+String.valueOf(per)); 
			int x = 0;
			int y = 0;
			for(int i =0;i<datNum;i++) {
				Log.v("smoke","x="+String.valueOf(x)); 
				x = (int) (data[i]*(width-per));
				canvas.drawRect(x, y, x+per, height, paint);
			}
		}
	}

}
