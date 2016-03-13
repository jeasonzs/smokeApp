package com.example.smokeapp;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;

public class utils {
    private final static float[] HIGH_LIGHT = new float[] {       
    	2, 0, 0, 0, 2,    
        0, 2, 0, 0, 2,    
        0, 0, 2, 0, 2,    
        0, 0, 0, 1, 0 };     
              
    private final static float[] NORMAL = new float[] {       
           1, 0, 0, 0, 0,       
           0, 1, 0, 0, 0,       
           0, 0, 1, 0, 0,       
           0, 0, 0, 1, 0 };  
	public final static ColorFilter highLightColorFilter() {
		return new ColorMatrixColorFilter(HIGH_LIGHT);
	}
	
	public final static ColorFilter normalColorFilter() {
		return new ColorMatrixColorFilter(NORMAL);
	}

}
