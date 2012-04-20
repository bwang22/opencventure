package com.mcallister.opencventure;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;

public class CameraUIActivity extends Activity{
	private DrawingView mDrawingView;
	private PreviewView mPreviewView;
	private Camera mCamera;
	public static volatile Mat sharedMat;
	public static volatile List<Rect> faces;
	
	private String TAG = "CameraUIActivity";
	
	/** Displays camera_ui layout when activity is created */
    @Override
	public void onCreate( Bundle savedInstanceState ){
    	super.onCreate( savedInstanceState );
    	//Remove title bar
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);

    	//Remove notification bar
    	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
        setContentView(R.layout.camera_ui);
    	Log.i(TAG, "Created");
        
    	getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
    	
    	// check for camera initialization errors
    	if ( !initCamera() ) {
        	Log.e(TAG, "Failed to init camera");
        	System.exit(0);
        }    	
    	
    	// create preview
    	mPreviewView = new PreviewView(this, mCamera);
    	mDrawingView = new DrawingView(this);
    	FrameLayout frame = (FrameLayout) findViewById(R.id.preview_frame);
    	frame.addView(mPreviewView);
    	frame.addView(mDrawingView);
    }
    
    public void onStop(){
    	super.onStop();
    	Log.i(TAG, "Stopping");

    	try {
    		mCamera.setPreviewDisplay(null);
		    mCamera.stopPreview();
	    	mCamera.release();
    	} catch (Exception e ) {
    		Log.e(TAG, "Destroy failed: " + e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    /** initializes camera and camera parameters */
    private boolean initCamera(){
    	try{
    		mCamera = Camera.open();
    		
    		Camera.Parameters params = mCamera.getParameters();
    		params.setPreviewSize(854, 480);
    		mCamera.setParameters(params);
    		Log.i(TAG, "preview format: " + Integer.toHexString(params.getPreviewFormat()));
    		
    		
    	} catch ( Exception e ) {
    		Log.e(TAG, "Failed to init cam: " + e.getMessage());
    		e.printStackTrace();
    		return false;
    	}
    	
    	return true;
    }
}