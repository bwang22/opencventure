package com.mcallister.opencventure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingView extends SurfaceView implements Runnable{
	public PreviewView mPreviewView;
	private SurfaceHolder mHolder;
	private Bitmap mBmp;
	private Canvas mCanvas;
	private Paint mPaint;
	private CascadeClassifier mCascade;
	
	private String TAG = "DrawingView";
	
	public DrawingView(Context context) {
		super(context);
		
		mHolder = getHolder();
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
		
		loadCascade( context );
		
		(new Thread(this)).start();
	}
	
	public boolean onTouchEvent( MotionEvent event ) {
		Log.i(TAG, "Touch at: (" + event.getRawX() + ", " + event.getRawY() + ")");
		
		boolean hit = false;
		
		Point touchPoint = new Point();
		touchPoint.x = event.getRawX();
		touchPoint.y = event.getRawY();
		
		if ( CameraUIActivity.faces != null ) {
			for ( Rect r : CameraUIActivity.faces ) {
				Log.i(TAG, "Rect at: " + r.tl().toString() + " " + r.br().toString());
				if ( r.contains(touchPoint) ) {
					Log.i(TAG, "HIT! :D");
					hit = true;
				}
			}
		}
		else
			Log.i(TAG, "No faces found :(");
		
		if ( !hit )
			Log.i(TAG, "Miss :(");
		
		return hit;
	}	
	
	/** load classifier for face detection */
	private boolean loadCascade( Context context ) {
		try {
            InputStream is = context.getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
            File cascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(cascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            mCascade = new CascadeClassifier(cascadeFile.getAbsolutePath());
            if (mCascade.empty()) {
                Log.e(TAG, "Failed to load cascade classifier");
                mCascade = null;
            } else
                Log.i(TAG, "Loaded cascade classifier from " + cascadeFile.getAbsolutePath());

            cascadeFile.delete();
            cascadeDir.delete();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
            return false;
        }
		
		return true;
	}
	
	private Bitmap processFrame(Mat imageMat) {		
		// compare to classifier to find face
        if (mCascade != null) {
            int height = imageMat.rows();
            int faceSize = Math.round(height * 0.2f);
            //CameraUIActivity.faces = new LinkedList<Rect>();
            mCascade.detectMultiScale(imageMat, CameraUIActivity.faces, 1.1, 2, 2 // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                    , new Size(faceSize, faceSize));

            // draw alien face for all faces found
            for (Rect r : CameraUIActivity.faces) {
                // head
            	Core.rectangle(imageMat, r.tl(), r.br(), new Scalar(0, 255, 0, 255), -1);
            	
            	// eyes
            	Point leftEyeTL = new Point();
            	leftEyeTL.x = r.x + (r.width/4);
            	leftEyeTL.y = r.y + (r.height/8);
            	Point leftEyeBR = new Point();
            	leftEyeBR.x = leftEyeTL.x + (r.width/10);
            	leftEyeBR.y = leftEyeTL.y + (r.height/10);
            	Core.rectangle(imageMat, leftEyeTL, leftEyeBR, new Scalar(255, 0, 0, 255), -1);
            	
            	Point rightEyeTL = new Point();
            	rightEyeTL.x = (r.x + r.width) - (r.width/4) - (r.width/10);
            	rightEyeTL.y = r.y + (r.height/8);
            	Point rightEyeBR = new Point();
            	rightEyeBR.x = rightEyeTL.x + (r.width/10);
            	rightEyeBR.y = rightEyeTL.y + (r.height/10);
            	Core.rectangle(imageMat, rightEyeTL, rightEyeBR, new Scalar(255, 0, 0, 255), -1);
            
            	// mouth
            	Point mouthTL = new Point();
            	mouthTL.x = r.x + (r.width/5);
            	mouthTL.y = r.y + 3*(r.height/4);
            	Point mouthBR = new Point();
            	mouthBR.x = r.x + 4*(r.width/5);
            	mouthBR.y = mouthTL.y + (r.height/5);
            	Core.rectangle(imageMat, mouthTL, mouthBR, new Scalar(255, 0, 0, 255), -1);
            }
        }

        Bitmap bmp = Bitmap.createBitmap(imageMat.cols(), imageMat.rows(), Bitmap.Config.ARGB_8888);

        if (Utils.matToBitmap(imageMat, bmp))
            return bmp;

        bmp.recycle();
        return null;
    }

	public void run() {
		Log.i(TAG, "Starting processing thread");

		CameraUIActivity.faces = new LinkedList<Rect>();
		
		while( this.isShown() ) {
			try {
				if ( CameraUIActivity.sharedMat == null )
					continue;
				
				mBmp = processFrame( CameraUIActivity.sharedMat );
				Log.i(TAG, "frame processed");
				
				mCanvas = mHolder.lockCanvas();
				mCanvas.drawBitmap(mBmp, 0, 0, null);
				mHolder.unlockCanvasAndPost(mCanvas);
				
			} catch ( Exception e ) {
				Log.e(TAG, "Exception while processing: " + e.getMessage());
				e.printStackTrace();
				break;
			}
		}
	}
}