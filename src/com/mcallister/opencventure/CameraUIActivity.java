package com.mcallister.opencventure;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class CameraUIActivity extends Activity implements OnClickListener{
	/** Called when the activity is first created. */
    @Override
	public void onCreate( Bundle savedInstanceState ){
    	super.onCreate( savedInstanceState );
        setContentView(R.layout.camera_ui);
    }
    
    public void onClick(View view){
    	System.out.println("Click Detected");
    }
}