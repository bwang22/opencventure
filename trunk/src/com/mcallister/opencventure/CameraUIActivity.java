package com.mcallister.opencventure;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class CameraUIActivity extends Activity implements OnClickListener{
	/** Called when the activity is first created. */
    @Override
	public void onCreate( Bundle savedInstanceState ){
    	super.onCreate( savedInstanceState );
        setContentView(R.layout.camera_ui);
        
        Camera cam = getCameraInstance();
        
        
    }
    
    public void onClick(View view){
    	System.out.println("Click Detected");
    }
    
    /** safely access an instance of the camera */
    public static Camera getCameraInstance(){
    	Camera cam = null;
    	
    	try{
    		cam = Camera.open();
    	}
    	catch ( Exception e ){
    		System.out.println("No camera found");
    	}
    	
    	return cam;
    }
}