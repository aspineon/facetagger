package com.bigdata.facetagger;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


import android.app.Activity;
import android.content.Context;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;



public class FrameCam extends Activity {

    private static final String    TAG                 = "OCVSample::Activity";
   
    private File                   mCascadeFile;
   
    private float                  mRelativeFaceSize   = 0.2f;
    private int                    mAbsoluteFaceSize   = 0;

    //private CameraControl mOpenCvCameraView;

    private GestureDetector mGestureDetector;    
    private Camera mCamera;
    //private SurfaceHolder surfaceHolder;
    //private boolean mCameraConfigured = false;
    private boolean bAfterInit = false;
    private static final int SWIPE_MIN_DISTANCE = 100;
    private static final int SWIPE_THRESHOLD_VELOCITY = 1000;
    private Bitmap bmp;
   
    
    /**/
    //////////////////////////////////////////////////////
//    private boolean safeCameraOpen(int id) {
//        try {
//            releaseCameraAndPreview();
//            mCamera = Camera.open(id);
//            qOpened = (mCamera != null);
//        } catch (Exception e) {
//            Log.e(TAG, "failed to open Camera");
//            e.printStackTrace();
//        }
//
//        return qOpened;    
//    }
//    
//    private void releaseCameraAndPreview() {
//        //mPreview.setCamera(null);
//        if (mCamera != null) {
//            mCamera.release();
//            mCamera = null;
//        }
//    }
    //////////////////////////////////////////////

    

    
    @Override
    public void onPause()
    {
    	Log.i(TAG, "+++++++++++++++ onPause called +++++++++++++++");
        
    	super.onPause();
    	//releaseCameraAndPreview();
    }	

    private void setMinFaceSize(float faceSize) {
        mRelativeFaceSize = faceSize;
        mAbsoluteFaceSize = 0;
        
        Toast.makeText(this, String.format("Face size: %.0f%%", mRelativeFaceSize*100.0f), Toast.LENGTH_SHORT).show();
    }
    

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

	

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) 
	{
		
		
		float size = mRelativeFaceSize;
		try {
            float totalXTraveled = e2.getX() - e1.getX();
            float totalYTraveled = e2.getY() - e1.getY();
            if (Math.abs(totalXTraveled) > Math.abs(totalYTraveled)) {
                if (Math.abs(totalXTraveled) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (totalXTraveled > 10) {
                        Log.d("Event", "On Fling Forward ----");
                        size -= 0.2f;
            			if ( size < 0.2f )
            				size = 0.2f;
                        //
                    } else {
                        Log.d("Event", "On Fling Backward +++");
                        //
                        size += 0.2f;
            			if ( size > 0.8f )
            				size = 0.8f;
                    }
                }
            } else {
                if (Math.abs(totalYTraveled) > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    if(totalYTraveled > 0) {
                    	// to leave app safely
                        Log.d("Event", "On Fling Down");
                        finish(); 
                    } else {
                        Log.d("Event", "On Fling Up");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		setMinFaceSize(size);
        return false;
	}
}