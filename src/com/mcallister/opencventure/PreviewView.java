package com.mcallister.opencventure;
import java.io.IOException;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class PreviewView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    
    private String TAG = "PreviewView";

    public PreviewView(Context context, Camera camera) {
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
	    Log.i(TAG, "Surface Changed");

    	try {        	
        	// create preview
	        mCamera.setPreviewDisplay(holder);
	        mCamera.startPreview();
	    } catch (IOException e) {
	        Log.e(TAG, "Error setting camera preview: " + e.getMessage());
	        e.printStackTrace();
	    }               
    	
    	// set up preview callback
	    mCamera.setPreviewCallback(new PreviewCallback(){
	        public void onPreviewFrame(byte[] data, Camera camera){
	            //Log.i(TAG, "onPreviewFrame called!");
	        	Size previewSize = camera.getParameters().getPreviewSize();
	            
	        	// construct matrix from YUV420 data and convert to RGB
	            Mat yuvMat = new Mat(previewSize.height + previewSize.height / 2, previewSize.width, CvType.CV_8UC1);
	            yuvMat.put(0, 0, data);
	            Mat rgbaMat = new Mat();
	            Imgproc.cvtColor(yuvMat, rgbaMat, Imgproc.COLOR_YUV420sp2RGB, 4);
	            CameraUIActivity.sharedMat = rgbaMat;
	        }
	    });
	}

    /** Tells the camera where to draw the preview when created */
    public void surfaceCreated(SurfaceHolder holder) {              
	    Log.i(TAG, "Surface Created");
    }

    /** Stop preview and release camera */
    public void surfaceDestroyed(SurfaceHolder holder) {
	    Log.i(TAG, "Surface Destroyed");
    }
}