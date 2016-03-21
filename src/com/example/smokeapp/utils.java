package com.example.smokeapp;

import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;

public class utils {
	public static final String PREFS_NAME = "SmokeAppPrefsFile";
	private static SharedPreferences pref;
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
	
	public final static void setSharedPreferences(SharedPreferences vpref) {
		pref = vpref;
	}
	public final static void setPrefString(String key,String value) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public final static String getPrefString(String key) {
        return pref.getString(key, "");
	}
	
	
	public final static void setPrefStringSet(String key,Set<String> value) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putStringSet(key, value);
		editor.commit();
	}
	
	public final static Set<String> getPrefStringSet(String key) {
        return pref.getStringSet(key, null);
	}
	
	public final static void checkAndOpenBlueTooth(Context context) {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
        	//提示用户打开；
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(enableIntent);
        	//直接打开；
        //	mBluetoothAdapter.enable();
        //	 setupChat();
        // Otherwise, setup the chat session
        }
	}


}
