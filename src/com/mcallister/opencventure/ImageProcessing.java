package com.mcallister.opencventure;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;


public class ImageProcessing {
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final String TAG = "ImageProcessing";
	

    /** Captures images from camera, and processes with OpenCV library */
    public static void ProcessImage( PictureCallback pic ){
    	Log.i(TAG, "Begin ProcessImage");
    }
    
	
	/** Create a file Uri for saving an image or video */
	public static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getImageOutputFile(type));
	}
	
	/** Create a File for saving an image or video */
	public static File getImageOutputFile(int type){
	    // kill if sd not mounted
		if ( Environment.getExternalStorageState().equals("mounted") == false )
		{
			Log.e(TAG, "SD card not mounted, exiting");
		}
	
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES ), "OpenCVenture");
	
	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d(TAG, "failed to create directory");
	            return null;
	        }
	    }
	
	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }
	
	    return mediaFile;
	}
}