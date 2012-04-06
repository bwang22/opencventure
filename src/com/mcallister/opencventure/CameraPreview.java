package com.mcallister.opencventure;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
	
    private String TAG = "CameraPreview";

    public CameraPreview(Context context, Camera camera) {
        super(context);

        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /** Handles changes to the preview when changed */
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	// set up preview callback
    	mCamera.setPreviewCallback(new PreviewCallback(){
    	    public void onPreviewFrame(byte[] data, Camera camera){
    	    	Log.i(TAG, "onPreviewFrame called!");
    	    }
    	});
	}

	/** Tells the camera where to draw the preview when created */
	public void surfaceCreated(SurfaceHolder holder) {		
		try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }		
	}

	/** IGNORED. Activity will handle cleanup */
	public void surfaceDestroyed(SurfaceHolder holder) {
		// ignored
	}    
}