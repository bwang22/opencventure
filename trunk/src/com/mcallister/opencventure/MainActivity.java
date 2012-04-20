package com.mcallister.opencventure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        
        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
    }
    
    /** Called when click detected on startButton */
    public void onClick(View view){
    	System.out.println("Start Button pressed");
    	
    	Intent intent = new Intent(this, CameraUIActivity.class);
		startActivity(intent);
    }
}