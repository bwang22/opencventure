package com.mcallister.opencventure;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class DrawingView extends PreviewBase{
	private boolean mTouched;
    private float mTouchX, mTouchY;
    
    private Mat mRGBa;
	
	private static String TAG = "DrawingView";
	
	
	public DrawingView(Context context) {
		super(context);
	}
	
	@Override
	public void surfaceChanged( SurfaceHolder _holder, int format, int width, int height ) {
        super.surfaceChanged(_holder, format, width, height);
        
        synchronized (this) {
        	mRGBa = new Mat();
        }
	}
	
	@Override
	protected Bitmap processFrame( VideoCapture capture ){
		Log.i(TAG, "processFrame()");
		
		// load captured frame into matrix and return as bitmap
		capture.retrieve( mRGBa, Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGBA );
		Bitmap bmp = Bitmap.createBitmap( mRGBa.cols(), mRGBa.rows(), Bitmap.Config.ARGB_8888 );
		
		if ( Utils.matToBitmap( mRGBa, bmp ) )
			return bmp;
		
		bmp.recycle();
		return null;
	}
	
    public boolean onTouchEvent(MotionEvent event){
    	mTouched = true;
    	mTouchX = event.getX();
    	mTouchY = event.getY();
    	Log.i(TAG, "Touch Coords: (" + mTouchX + ", " + mTouchY + ")");
    	
    	postInvalidate();
    	
    	return true;
    }    
	

    protected void onDraw(Canvas canvas){
    	Log.i(TAG, "onDraw called");
    	
    	if( mTouched ){
    		Paint paint = new Paint();
    		paint.setColor(Color.GREEN);
    		canvas.drawCircle(mTouchX, mTouchY, 50, paint);
    	}
    }    
}