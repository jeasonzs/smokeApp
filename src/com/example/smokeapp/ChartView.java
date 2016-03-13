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

public class ChartView extends ImageView {
	protected float spaceRatio = 0;
	float[] data;
	int datNum = 0;
	public ChartView(Context context) {
		super(context);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		// TODO Auto-generated constructor stub
	}
	
	public void setData(float spaceRatio,float[] data) {
		this.spaceRatio = spaceRatio;
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
			
			float per = width/(datNum+(datNum-1)*spaceRatio);

			Log.v("smoke","width="+String.valueOf(width)); 
			Log.v("smoke","per="+String.valueOf(per)); 
			int x = 0;
			for(int i =0;i<datNum;i++) {
				Log.v("smoke","x="+String.valueOf(x)); 
				int y = (int) ((1.0f-data[i])*height);
				canvas.drawRect(x, y, x+per, height, paint);
				x += per*spaceRatio+per;
			}
		}
	}

}
