package com.mcallister.opencventure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class DrawingView extends PreviewBase{
	private boolean mTouched;
    private float mTouchX, mTouchY;
	
	private static String TAG = "DrawingView";
	
	
	public DrawingView(Context context) {
		super(context);
	}
	
	@Override
	protected Bitmap processFrame(byte[] data){
		//Log.i(TAG, "processFrame()");
		
		int frameSize = getFrameWidth() * getFrameHeight();
        int[] rgba = new int[frameSize];

       //int view_mode = Sample0Base.viewMode;
       //if (view_mode == Sample0Base.VIEW_MODE_GRAY) {
            for (int i = 0; i < frameSize; i++) {
                int y = (0xff & ((int) data[i]));
                rgba[i] = 0xff000000 + (y << 16) + (y << 8) + y;
            }
        /*} else if (view_mode == Sample0Base.VIEW_MODE_RGBA) {
            for (int i = 0; i < getFrameHeight(); i++)
                for (int j = 0; j < getFrameWidth(); j++) {
                    int y = (0xff & ((int) data[i * getFrameWidth() + j]));
                    
                    Log.i("Sample0View", "VAL: " + (frameSize + (i >> 1) * getFrameWidth() + (j & ~1) + 0) );
                    Log.i("Sample0View", "MAX: " + data.length);
                    
                    int u = (0xff & ((int) data[frameSize + (i >> 1) * getFrameWidth() + (j & ~1) + 0]));
                    int v = (0xff & ((int) data[frameSize + (i >> 1) * getFrameWidth() + (j & ~1) + 1]));
                    y = y < 16 ? 16 : y;

                    int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                    int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                    int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));

                    r = r < 0 ? 0 : (r > 255 ? 255 : r);
                    g = g < 0 ? 0 : (g > 255 ? 255 : g);
                    b = b < 0 ? 0 : (b > 255 ? 255 : b);

                    rgba[i * getFrameWidth() + j] = 0xff000000 + (b << 16) + (g << 8) + r;
                }
        }*/

        Bitmap bmp = Bitmap.createBitmap(getFrameWidth(), getFrameHeight(), Bitmap.Config.ARGB_8888);
        bmp.setPixels(rgba, 0/* offset */, getFrameWidth() /* stride */, 0, 0, getFrameWidth(), getFrameHeight());
        return bmp;
		
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