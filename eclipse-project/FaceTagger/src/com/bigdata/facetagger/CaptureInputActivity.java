package com.bigdata.facetagger;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.FaceDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



public class CaptureInputActivity extends SurfaceView{

	

public CaptureInputActivity(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

private Camera mCamera;
private SurfaceHolder mHolder;
private FaceDetector mFaceDetector;
private static final int NUM_FACES = 5; // max is 64

private Bitmap mWorkBitmap;

	
	public void setCamera(Camera camera){
    	mCamera = camera;
    }
    
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {      
        	if (mCamera != null){
        		setWillNotDraw(false);
            	mCamera.setPreviewDisplay(holder);
        	}
        } catch (IOException e) {
           // Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            cameraReleased();
        }
       
    }
    
   public void getCameraInstance(){
        try {
            mCamera = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        	//Log.e(TAG, "Erreur " + e.getMessage());
        }
    }
    
    public void cameraReleased(){
   	 	if(mCamera != null) {
   	 		mCamera.stopPreview();
   	 		mCamera.setPreviewCallbackWithBuffer(null);
            mCamera.release();	
            mCamera = null;
        }
   	 	setWillNotDraw(true);
    }
    
    public void surfaceDestroyed(SurfaceHolder holder) {
    	cameraReleased();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Make sure to stop the preview before resizing or reformatting it.
    	//Log.d(TAG, String.format("surfaceChanged: format=%d, w=%d, h=%d", format, w, h));
        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }
        
        try {
            // stop preview before making changes
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        	 cameraReleased();
        }

       // Log.d(TAG, "Width: " + w + " Height: " + h);
        
        try {
        	// set preview size and make any resize, rotate or
            // reformatting changes here
        	Camera.Parameters parameters = mCamera.getParameters();
            List<Size> sizes = parameters.getSupportedPreviewSizes();
            Size optimalSize = getOptimalPreviewSize(sizes, w, h);
            parameters.setPreviewFpsRange(30000, 30000);
            parameters.setPreviewSize(optimalSize.width, optimalSize.height);
            // Now that the size is known, set up the camera parameters and begin
            // the preview.
            mCamera.setParameters(parameters);
            
            mCamera.setPreviewDisplay(mHolder);
            // start preview with new settings
            mCamera.startPreview();
            
            //mCamera.startFaceDetection();
            // Setup the objects for the face detection
            mWorkBitmap = Bitmap.createBitmap(optimalSize.width,  optimalSize.height, Bitmap.Config.RGB_565);
            mFaceDetector = new FaceDetector(optimalSize.width,  optimalSize.height, NUM_FACES);

            int bufSize = optimalSize.width * optimalSize.height *
                 ImageFormat.getBitsPerPixel(parameters.getPreviewFormat()) / 8;
            byte[] cbBuffer = new byte[bufSize];
            mCamera.setPreviewCallbackWithBuffer((PreviewCallback) this);
            //mCamera.setPreviewCallback(this);
            mCamera.addCallbackBuffer(cbBuffer);

        } catch (Exception e){
            //Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            cameraReleased();
        }
    }
    
    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1	;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
        	//Log.d(TAG, "Cool size : " + size.width + " " + size.height);
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    


}
