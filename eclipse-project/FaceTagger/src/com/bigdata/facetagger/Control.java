package com.bigdata.facetagger;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;


public class Control  implements SurfaceHolder.Callback, PreviewCallback, PictureCallback {
	
	private static final String TAG = "CameraControl";
	private Camera mCamera;
    
    public Control(Context context, AttributeSet attrs) {
    	
        //
        Log.i(TAG, "CameraControl++++++++++++++++");
    }
    
    public boolean setParam() {
    	
    	
        Log.i(TAG, "set parameter+++++++++++++++");
        if (mCamera == null){
        	//mCamera = Camera.open();
        	return false;
        }
        
        Camera.Parameters camParameters = mCamera.getParameters();
 	        //start glass fix - use 5000 instead of 30000 for better battery performance
	    camParameters.setPreviewFpsRange(30000, 30000);
//	        //end glass fix
	    camParameters.setPreviewSize(1920, 1080);
	    camParameters.setPictureSize(2592, 1944);
	    mCamera.setParameters(camParameters);
	    Log.i(TAG, "set parameter---------------");
	    //mCamera.startPreview();
	    return true;
//	        mCameraConfigured = true;			
//		}
    }

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onPreviewFrame+++++++++++");
		mCamera = camera;
		setParam();
		
	}

//	@Override
//	protected boolean connectCamera(int width, int height) {
//		// TODO Auto-generated method stub
//		Log.i(TAG, "connectCamera+++++++++++");
//		return true;
//	}
//
//	@Override
//	protected void disconnectCamera() {
//		// TODO Auto-generated method stub
//		Log.i(TAG, "disconnectCamera+++++++++++");
//		
//	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

    
   
}