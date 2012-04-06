package com.mcallister.opencventure;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class CameraUIActivity extends Activity{
	private Camera mCamera;
	private CameraPreview mPreview;
	
	private String TAG = "CameraUIActivity";
	
	/** Displays camera_ui layout when activity is created */
    @Override
	public void onCreate( Bundle savedInstanceState ){
    	super.onCreate( savedInstanceState );
        setContentView(R.layout.camera_ui);
    	Log.i(TAG, "Created");
    	
    	// acquire camera and initialize params
    	if ( !safeCameraOpen() ){
    		Log.e(TAG, "Failed to acquire camera");
    		System.exit(0); 
    	}  	
    	
    	initCamera();  
    	
    	// create preview
    	mPreview = new CameraPreview(this, mCamera);
    	FrameLayout preview = (FrameLayout) findViewById(R.id.preview_frame);
    	preview.addView(mPreview);
    }
    
    public void onStop(){
    	super.onStop();
    	Log.i(TAG, "Stopping");
    	
    	// safely release camera
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
    }
    
    public boolean onTouchEvent(MotionEvent event){
    	Log.i(TAG, "Touch Coords: (" + event.getX() + ", " + event.getY() + ")");
    	
    	return true;
    }
    
    /** safely access an instance of the camera */
    private boolean safeCameraOpen() {
        boolean qOpened = false;
      
        try {
            mCamera = Camera.open();
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;    
    }
    
    /** Initialize camera and begin recording */
    public void initCamera( ){
    	Parameters camParams = mCamera.getParameters();
    	
    	camParams.setRotation(90);
    	mCamera.setParameters(camParams);
    	
    	Log.i(TAG, "Camera initialized");
    }
}