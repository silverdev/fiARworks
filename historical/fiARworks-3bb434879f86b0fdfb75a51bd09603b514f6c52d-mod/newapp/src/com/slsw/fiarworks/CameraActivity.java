/*
 * Sets up the camera,
 * then starts camera preview and overlay
 */
package com.slsw.fiarworks;


import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.slsw.fiarworks.firework.GLRenderer;

public class CameraActivity extends Activity implements OnTouchListener {
	private Camera mCamera = null;
	private MyGLSurfaceView mView;
	private CameraPreview mPrev;
	private SensorManager mSensorManager;
    private Sensor mRotation;
    private GLRenderer mRenderer;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("ONCREATE");
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mRotation = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		if(mCamera == null)
			mCamera = getCameraInstance();
		mRenderer=new GLRenderer();
		mView = new MyGLSurfaceView(this, mRenderer);
		mPrev = new CameraPreview(this.getBaseContext(), mCamera, mRenderer);
		if(mPrev == null) System.out.println("Oh noes");
		mCamera.getParameters().setPreviewFormat(ImageFormat.RGB_565);
		mCamera.setPreviewCallback(mPrev);
		mCamera.startPreview();
		setContentView(mView);
		mView.setOnTouchListener(this);
		System.err.println("hello");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		System.out.println("ONCREATEOPTIONSMENU");
		return true;
	}

	@Override
	protected void onPause() {
		releaseCamera();
		mSensorManager.unregisterListener(this.mPrev);
		super.onPause();
		//releaseCamera();
		System.out.println("ONPAUSE");
		mView.onPause();
	}

	@Override
	protected void onResume() {
		if(mCamera == null)
			mCamera = getCameraInstance();
		mPrev = new CameraPreview(getBaseContext(), mCamera, mRenderer);
		if(mPrev == null) System.out.println("Oh noes");
		mCamera.getParameters().setPreviewFormat(ImageFormat.RGB_565);
		mCamera.setPreviewCallback(mPrev);
		mCamera.startPreview();
		setContentView(mView);
		mView.onResume();
		mView.setOnTouchListener(this);
		mSensorManager.registerListener(this.mPrev, mRotation, SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	/*
	 * Release the camera so that other applications can use it. Called when the
	 * program is not in the forefront
	 */
	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release(); // release the camera for other applications
			mCamera = null;
			mPrev.mCamera=null;
		}
	}

	/*
	 * This function tries to grab the first available camera
	 * 
	 * returns null if unavailable
	 */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
			// Camera is not available
			System.out.println("CAMERA NOT AVAILABLE");
		}
		return c;
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
