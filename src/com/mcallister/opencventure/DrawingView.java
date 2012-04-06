package com.mcallister.opencventure;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.View;

public class DrawingView extends View{
	private SurfaceHolder mHolder;
    private Camera mCamera;
	
	private String TAG = "DrawingView";
	
	
	public DrawingView(Context context, Camera camera) {
		super(context);
		
		mCamera = camera;
	}	
}