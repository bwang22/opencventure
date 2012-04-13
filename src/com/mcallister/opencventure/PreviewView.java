package com.mcallister.opencventure;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class PreviewView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private BitmapFactory.Options mBmpOpts;
    
    private String TAG = "CameraPreview";

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
	            
	            // Convert to JPG
	            Size previewSize = camera.getParameters().getPreviewSize(); 
	            YuvImage yuvimage=new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height, null);
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);
	            byte[] jdata = baos.toByteArray();

	            // Convert to Bitmap
	            CameraUIActivity.sharedBmp = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
	        }
	    });
	}

    /** Tells the camera where to draw the preview when created */
    public void surfaceCreated(SurfaceHolder holder) {              
	    Log.i(TAG, "Surface Created");
	    
        // set bitmap options
    	mBmpOpts = new BitmapFactory.Options();
    	mBmpOpts.inDither = true;
    	mBmpOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
    }

    /** Stop preview and release camera */
    public void surfaceDestroyed(SurfaceHolder holder) {
	    Log.i(TAG, "Surface Destroyed");
    }
}