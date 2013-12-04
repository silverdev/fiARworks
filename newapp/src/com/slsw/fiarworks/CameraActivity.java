/*
 * Sets up the camera,
 * then starts camera preview and overlay
 */
package com.slsw.fiarworks;

import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import com.slsw.fiarworks.firework.GLRenderer;
import com.slsw.fiarworks.masker.AlphaMake;

public class CameraActivity extends Activity implements Camera.PreviewCallback, OnTouchListener {

    private Camera mCamera;
    private CameraPreview mPreview;
	private SensorManager mSensorManager;
    private Sensor mRotation;
	private MyGLSurfaceView mView;
    private GLRenderer mRenderer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.main_layout);
	    
	
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mRotation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		
	
		mRenderer=new GLRenderer();
		mView = new MyGLSurfaceView(this, mRenderer);
	
	    // Create an instance of Camera
	    mCamera = getCameraInstance();
	    Camera.Parameters parameters = mCamera.getParameters();
	    List<String> focusModes = parameters.getSupportedFocusModes();
	    if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
	    {
	        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
	    }
	    mCamera.setParameters(parameters);
	
	    // Create our Preview view and set it as the content of our activity
	    mPreview = new CameraPreview(this, mCamera, mRenderer);
	    setContentView(mView);
	    addContentView(mPreview, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    mSensorManager.registerListener((SensorEventListener)mPreview, mRotation, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        super.onPause();
        
        mSensorManager.unregisterListener(this.mPreview);

        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();
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
	    System.out.println("PreviewFrame");
	    Camera.Parameters p = mCamera.getParameters();
	    int width = p.getPreviewSize().width;
	    int height = p.getPreviewSize().height;
	    mRenderer.setTextures(data, width, height, mPreview);
    }
    
    /*
	 * Called whenever a touch is detected.
	 */
	public boolean onTouch(View v, MotionEvent event) {
		System.out.println("ONTOUCH");
		double x = event.getX(); 
		double y = event.getY();
		y = (y/v.getHeight()-.5)*2;
		x= (x/v.getWidth()-.5)*2;
		mView.launchFirework(x,y, 0.0);
		
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
		
	}
}
