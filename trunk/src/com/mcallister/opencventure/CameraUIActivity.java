package com.mcallister.opencventure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CameraUIActivity extends Activity{
	private Camera mCamera = null;
	
	private CameraPreview camPreview = null;
	private FrameLayout previewFrame = null;
	
	private PictureCallback mPicture = null;
	private File mPictureFile = null;
	private String TAG = "CameraUIActvity";
	
	/** Displays camera_ui layout when activity is created */
    @Override
	public void onCreate( Bundle savedInstanceState ){
    	super.onCreate( savedInstanceState );
        setContentView(R.layout.camera_ui);
    	System.out.println("CameraUIActivity.onCreate(): Created");
    }
    
    /** Initializes camera, preview, if necessary, and resumes preview */
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
        
    	// create button and set respective onClickListener
        Button takePictureButton = (Button) findViewById(R.id.takePictureButton);
        takePictureButton.setOnClickListener(
	        new View.OnClickListener() {
	            public void onClick(View v) {
	                // get an image from the camera
	                mCamera.takePicture(null, null, mPicture);
	            }
	        }
	    );
    	
    	// create preview
    	camPreview = new CameraPreview(this, mCamera);
        previewFrame = (FrameLayout) findViewById(R.id.camera_preview);
        previewFrame.addView(camPreview);
            	
    	mPicture = new PictureCallback() {
    		public void onPictureTaken(byte[] imageData, Camera camera) {
    			Log.i(TAG, "onPictureTaken() started");
    	        mPictureFile = ImageProcessing.getImageOutputFile(1); // 1 = MEDIA_TYPE_IMAGE
    	            	        
    	        try{
    	        	mPictureFile.createNewFile();
	    	        FileOutputStream fos = new FileOutputStream(mPictureFile);
	    	        fos.write(imageData);
	    	        fos.close();
	    	        Log.i(TAG, "Picture written to SD");
    	        }
    	        catch( IOException e )
    	        {
    	        	Log.e(TAG, "Failed to write image data to SD");
    	        }
    		}
    	};
    	
    	/*
    	ImageView imageView = (ImageView) findViewById(R.id.imageView);
    	
    	// Capture and process images
		mCamera.takePicture(null, null, mPicture);
    	ImageProcessing.ProcessImage(mPicture);
    	//imageView.setImageURI(Uri.fromFile(mPictureFile));
    	*/
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
    }

    /** Releases camera so other apps can use after being destroyed */
    public void onDestroy(){
    	super.onDestroy();
    	System.out.println("CameraUIActivity.onDestroy(): destroyed, releasing camera, nulling member vars");
	    mCamera.release();
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
    	int JPEG = 0x100;
    	
    	Parameters camParams = cam.getParameters();
    	camParams.setPictureFormat(JPEG);
    	cam.setParameters(camParams);
    	
    	cam.setDisplayOrientation(90); // rotate to portrait mode
    	
    	System.out.println("CameraUIActivity.initCamera(): Camera initialized");
    }
}