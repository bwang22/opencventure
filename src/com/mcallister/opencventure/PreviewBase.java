package com.mcallister.opencventure;

import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public abstract class PreviewBase extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    protected SurfaceHolder mHolder;
    private VideoCapture mVideoCapture;
    private List<Size> mSizes;
    private int mFrameWidth;
    private int mFrameHeight;
    
    
    private String TAG = "CameraPreview";

    public PreviewBase(Context context) {
        super(context);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
    }
    
    public int getFrameWidth() {
        return mFrameWidth;
    }

    public int getFrameHeight() {
        return mFrameHeight;
    }

    /** Handles changes to the preview when changed */
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.i(TAG, "SurfaceChanged");
		
		if ( mVideoCapture != null && mVideoCapture.isOpened()) {
			mSizes = mVideoCapture.getSupportedPreviewSizes();
			mFrameWidth = width;
			mFrameHeight = height;
			
			// determine best preview size
			double minDiff = Double.MAX_VALUE;
			for ( Size size : mSizes ) {
				if (Math.abs(size.height - height) < minDiff) {
                    mFrameWidth = (int) size.width;
                    mFrameHeight = (int) size.height;
                    minDiff = Math.abs(size.height - height);
                }
			}
			
			// set preview size
			mVideoCapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, mFrameWidth);
			mVideoCapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, mFrameHeight);
		}
	}

	/** Tells the camera where to draw the preview when created */
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, "SurfaceCreated");
		
		mVideoCapture = new VideoCapture(Highgui.CV_CAP_ANDROID);
		if (mVideoCapture.isOpened()){
			Log.i(TAG, "VideoCapture opened");
			(new Thread(this)).start();
		} else {
			mVideoCapture.release();
            mVideoCapture = null;
            Log.e(TAG, "Failed to open VideoCapture");
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "surfaceDestroyed");
        if (mVideoCapture != null) {
        	synchronized (this) {
	            mVideoCapture.release();
	            mVideoCapture = null;
        	}
        }
	}
	
	protected abstract Bitmap processFrame( VideoCapture capture );
	
	public void run() {
		Log.i(TAG, "Starting processing thread");
		
		Bitmap bmp = null;
		Canvas canvas = null;
		float x = 0;
		float y = 0;
		
		Mat image = new Mat();
		
		while( true ) {
			Log.i( TAG, "Before sync" );
			
			//synchronized (this) {

				Log.i( TAG, "After sync" );
				// check for invalid video
				if ( mVideoCapture == null ){
					Log.e(TAG, "Invalid VideoCapture");
					break;
				}
				Log.i( TAG, "After VC Sanity check" );
				
				mVideoCapture.grab();
				Log.i( TAG, "After capture read" );
				/*
				// grab next frame and check for error
				if ( !mVideoCapture.grab() ) {
					Log.e(TAG, "VideoCapture grab() failed");
					break;
				}
				
				Log.i( TAG, "Before processFrame" );
				bmp = processFrame( mVideoCapture );
				Log.i( TAG, "After processFrame" );
				*/
				Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.ARGB_8888);
				Utils.matToBitmap(image, bmp);
			//}
			
			if ( bmp != null ) {
				canvas = mHolder.lockCanvas();
				
				// check for canvas locking errors
				if ( canvas != null ) {
                    //canvas.drawBitmap(bmp, (canvas.getWidth() - bmp.getWidth()) / 2, (canvas.getHeight() - bmp.getHeight()) / 2, null);
                    Paint paint = new Paint();
                    paint.setColor(Color.GREEN);
                    canvas.drawCircle(x++, y++, 10, paint);
                    mHolder.unlockCanvasAndPost(canvas);
				}
				
				bmp.recycle();
			}
		}
		
		Log.i(TAG, "Exiting processing thread");
	}
}