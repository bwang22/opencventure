package com.mcallister.opencventure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View{
	private boolean mTouched;
    private float mTouchX, mTouchY;
	
	private String TAG = "DrawingView";
	
	
	public DrawingView(Context context) {
		super(context);
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
    		Paint paint = new Paint(Color.RED);
    		canvas.drawCircle(mTouchX, mTouchY, 50, paint);
    	}
    }
    
}