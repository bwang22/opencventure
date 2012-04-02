package com.mcallister.opencventure;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class CameraUIActivity extends Activity{
	private Camera mCamera = null;
	private CameraPreview camPreview = null;
	private FrameLayout previewFrame = null;
	
	/** Initializes camera and begins preview */
    @Override
	public void onCreate( Bundle savedInstanceState ){
    	super.onCreate( savedInstanceState );
        setContentView(R.layout.camera_ui);
    	System.out.println("CameraUIActivity.onCreate(): Created");
        
        mCamera = getCameraInstance();
        initCamera( mCamera );
        
        camPreview = new CameraPreview(this, mCamera);
        previewFrame = (FrameLayout) findViewById(R.id.camera_preview);
        previewFrame.addView(camPreview);
    }
    
    /** Re-initializes camera, preview, if necessary, and resumes preview */
    public void onResume( ){
    	super.onResume();    	
    	setContentView(R.layout.camera_ui);
    	System.out.println("CameraUIActivity.onResume(): Resuming");
        
    	if ( null == mCamera ){
            mCamera = getCameraInstance();
            initCamera( mCamera );
    	}
    	else{
    		try{
    			mCamera.reconnect();
    	    	System.out.println("CameraUIActivity.onResume(): Successfully acquired camera");
    		}
    		catch (Exception e){
    			System.err.println("CameraUIActivity.onResume(): Could not access camera. Stack Trace:");
    			e.printStackTrace();
    			System.exit(0);
    		}
    	}
        
    	camPreview = new CameraPreview(this, mCamera);
    	
    	previewFrame = (FrameLayout) findViewById(R.id.camera_preview);
	    previewFrame.addView(camPreview);
    }
    
    /** Re-initializes camera, preview, if necessary, and resumes preview */
    public void onRestart( ){
    	super.onRestart();
    	setContentView(R.layout.camera_ui);
    	System.out.println("CameraUIActivity.onRestart(): Restarting");
        
    	if ( null == mCamera ){
            mCamera = getCameraInstance();
            initCamera( mCamera );
    	}
    	else{
    		try{
    			mCamera.reconnect();
    	    	System.out.println("CameraUIActivity.onRestart(): Successfully acquired camera");
    		}
    		catch (Exception e){
    			System.err.println("CameraUIActivity.onRestart(): Could not access camera. Stack Trace:");
    			e.printStackTrace();
    			System.exit(0);    			
    		}
    	}
        
    	camPreview = new CameraPreview(this, mCamera);
    	
    	previewFrame = (FrameLayout) findViewById(R.id.camera_preview);
	    previewFrame.addView(camPreview);
    }
    
    
    /** Releases camera so other apps can use while paused */
    public void onPause(){
    	super.onPause();
    	System.out.println("CameraUIActivity.onPause(): paused, releasing camera");
    	mCamera.release();
    }
    

    /** Releases camera so other apps can use after stopping */
    public void onStop(){
    	super.onStop();
    	System.out.println("CameraUIActivity.onStop(): stopped, releasing camera, nulling member vars");
    	mCamera.release();
    	mCamera = null;
    	camPreview = null;
    	previewFrame = null;
    }

    /** Releases camera so other apps can use after being destroyed */
    public void onDestroy(){
    	super.onDestroy();
    	System.out.println("CameraUIActivity.onDestroy(): destroyed, releasing camera, nulling member vars");
    	if (mCamera != null ){
	    	mCamera.release();
    	}
    }
    
    public boolean onTouchEvent(MotionEvent event){
    	System.out.println("CameraUIActivity.onTouchEvent: X: " + event.getX() + " Y: " + event.getY());
    	
    	return true;
    }
    
    /** safely access an instance of the camera */
    public static Camera getCameraInstance(){
    	Camera cam = null;
    	
    	try{
    		cam = Camera.open();
    		System.out.println("CameraUIActivity.getCameraInstance(): Camera found, opening");
    	}
    	catch ( Exception e ){
    		System.err.println("CameraUIActivity.getCameraInstance(): No camera found. Stack trace:");
    		e.printStackTrace();
    		System.exit(0);
    	}
    	
    	return cam;
    }
    
    /** Initialize camera and begin recording */
    public void initCamera( Camera cam ){
    	cam.setDisplayOrientation(90); // rotate to portrait mode
    	System.out.println("CameraUIActivity.initCamera(): Camera initialized");
    }
}