package com.mcallister.opencventure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingView extends SurfaceView implements Runnable{
	public PreviewView mPreviewView;
	private SurfaceHolder mHolder;
	private Canvas mCanvas;
	private Paint mPaint;
	
	private String TAG = "DrawingView";
	
	public DrawingView(Context context) {
		super(context);
		
		mHolder = getHolder();
		mHolder.setFormat(PixelFormat.TRANSLUCENT);
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
		
		(new Thread(this)).start();
	}
	
	public void run() {
		Log.i(TAG, "Starting processing thread");
		int i=0;
		while( this.isShown() ) {
			try {
				mCanvas = mHolder.lockCanvas();
				mCanvas.drawCircle(i, i, 10, mPaint);
				mHolder.unlockCanvasAndPost(mCanvas);
				i++;
			} catch ( Exception e ) {
				Log.i(TAG, "mPreviewView.sharedBmp == null");
			}
		}
	}
}