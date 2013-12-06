/*
 * Sets up the camera,
 * then starts camera preview and overlay
 */
package com.slsw.fiarworks;

import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.slsw.fiarworks.firework.GLRenderer;

public class CameraActivity extends Activity implements Camera.PreviewCallback, OnTouchListener {

    private Camera mCamera;
    private CameraPreview mPreview;
	private SensorManager mSensorManager;
    private Sensor mRotation;
	private MyGLSurfaceView mView;
    private GLRenderer mRenderer;
	private long meantime = 0;
	private int count =0;
	private boolean allowLaunching = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    // Kirby and Eric are putting wake lock back for a while. If you want to get rid of it,
	    // please comment the next line out instead of deleting it.
	    getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mRotation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		
		mRenderer=new GLRenderer(getBaseContext());
		mView = new MyGLSurfaceView(this, mRenderer);
		mView.setOnTouchListener(this);
    }

    public void onPause() {
        super.onPause();
        System.out.println("PAUSE");
        
        mSensorManager.unregisterListener(this.mPreview);

        if (mCamera != null) {
        	System.out.println("mCamera now null");
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();
            mCamera=null;
            mPreview.setCamera(null);
            mPreview=null;
        }
    }
    
    public void onResume(){
    	super.onResume();
    	System.out.println("RESUME");
    	startCamera();
    	mSensorManager.registerListener((SensorEventListener)mPreview, mRotation, SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    public void startCamera(){
    	System.out.println("mCamera no longer null");
    	if(mCamera==null) {
	    	mCamera = getCameraInstance();
	    	mPreview = new CameraPreview(this, mCamera, mRenderer);
	    	Camera.Parameters parameters = mCamera.getParameters();
		    List<String> focusModes = parameters.getSupportedFocusModes();
		    if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
		    {
		        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		    }
		    List<Size> sizes = parameters.getSupportedPreviewSizes();
		    System.out.println("CALLED");
		    for(Size s : sizes){
		    	System.out.println("Size = "+s.width +", "+ s.height);
		    }
		    mCamera.setParameters(parameters);
		    setContentView(mView);
			addContentView(mPreview, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    	}
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
	    //System.out.println("PreviewFrame");
	    Camera.Parameters p = mCamera.getParameters();
	    int width = p.getPreviewSize().width;
	    int height = p.getPreviewSize().height;
	    
	    final long startTime = System.currentTimeMillis();
	    mRenderer.setTextures(data, width, height, mPreview);
	    final long Endtime = System.currentTimeMillis();
	    meantime += (Endtime - startTime);
	    count += 1;
	    // System.out.println(meantime/count); 
	    
    }
    
    /*
	 * Called whenever a touch is detected.
	 */
	public boolean onTouch(View v, MotionEvent event) {

		// identify touch type
		int actionType = event.getActionMasked();
		String eventTypeString;
		// touch with just one finger
		if (actionType==MotionEvent.ACTION_DOWN) {
			eventTypeString = new String("ACTION_DOWN " + actionType);
			// schedule a launch
			allowLaunching = true;
		}
		// lift the only finger
		else if (actionType==MotionEvent.ACTION_UP) {
			eventTypeString = new String("ACTION_UP " + actionType);
			// don't launch
			allowLaunching = false;
		}
		// finger slide
		else if (actionType==MotionEvent.ACTION_MOVE) {
			eventTypeString = new String("ACTION_MOVE " + actionType);
			// don't launch
			allowLaunching = false;
		}
		// anything else, like touching/lifting an additional finger
		else {
			eventTypeString = new String("dunno, look it up "+actionType);
			// don't launch
			allowLaunching = false;
		}
		System.out.println("ONTOUCH: " + eventTypeString);
		
		if (allowLaunching) {
			double x = event.getX();
			double y = event.getY();
			y = (y/v.getHeight()-.5)*2;
			x= (x/v.getWidth()-.5)*2;
			mView.launchFirework(x,y, 0.0);
		}
		
		return true;
	}

 }

class MyGLSurfaceView extends GLSurfaceView {
	private final GLRenderer mRenderer;

    public MyGLSurfaceView(Context context, GLRenderer rend) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = rend;
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
    
	public void launchFirework(double x, double y, double d) {
		this.mRenderer.launchFirework((float)x, (float)y);
	}
}
