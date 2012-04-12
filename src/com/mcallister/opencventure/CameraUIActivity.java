package com.mcallister.opencventure;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class CameraUIActivity extends Activity{
	private DrawingView mDrawingView;
	
	private String TAG = "CameraUIActivity";
	
	/** Displays camera_ui layout when activity is created */
    @Override
	public void onCreate( Bundle savedInstanceState ){
    	super.onCreate( savedInstanceState );
        setContentView(R.layout.camera_ui);
    	Log.i(TAG, "Created");
    	
    	// create preview
    	mDrawingView = new DrawingView(this);
    	FrameLayout preview = (FrameLayout) findViewById(R.id.preview_frame);
    	preview.addView(mDrawingView);
    }
    
    public void onStop(){
    	super.onStop();
    	Log.i(TAG, "Stopping");
    }
}