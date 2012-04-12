package com.mcallister.opencventure;

import java.util.List;

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
			
			Canvas canvas = mHolder.lockCanvas();
			Paint paint = new Paint();
			paint.setColor(Color.GREEN);
			canvas.drawCircle(100, 100, 10, paint);
			mHolder.unlockCanvasAndPost(canvas);
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

	/** IGNORED. Activity will handle cleanup */
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "surfaceDestroyed");
        if (mVideoCapture != null) {
            mVideoCapture.release();
            mVideoCapture = null;
        }
	}
	
	protected abstract Bitmap processFrame(byte[] data);
	
	public void run() {
		Log.i(TAG, "Starting processing thread");
	}
}