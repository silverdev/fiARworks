/*
 * Sets up the camera,
 * then starts camera preview and overlay
 */
package com.slsw.fiarworks;


import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class CameraActivity extends Activity implements OnTouchListener {
	private Camera mCamera = null;
	private MyGLSurfaceView mView;
	private CameraPreview mPrev;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("ONCREATE");
		if(mCamera == null)
			mCamera = getCameraInstance();
		mView = new MyGLSurfaceView(this);
		mPrev = new CameraPreview(this.getBaseContext(), mCamera);
		if(mPrev == null) System.out.println("Oh noes");
		mCamera.setPreviewCallback(mPrev);
		mCamera.startPreview();
		setContentView(mPrev);
		addContentView(mView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		mView.setOnTouchListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		System.out.println("ONCREATEOPTIONSMENU");
		return true;
	}

	@Override
	protected void onPause() {
		releaseCamera();
		super.onPause();
		//releaseCamera();
		System.out.println("ONPAUSE");
	}

	@Override
	protected void onResume() {
		if(mCamera == null)
			mCamera = getCameraInstance();
		mPrev = new CameraPreview(getBaseContext(), mCamera);
		if(mPrev == null) System.out.println("Oh noes");
		mCamera.setPreviewCallback(mPrev);
		mCamera.startPreview();
		setContentView(mPrev);
		addContentView(mView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		mView.start();
		mView.setOnTouchListener(this);
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
	Overlay mOverlay = null;
	private Thread mThread;
    public MyGLSurfaceView(CameraActivity context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        
        //Should make background visible
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderMediaOverlay(true);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mOverlay = new Overlay(context));
        

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    public void start(){
        this.mThread = new Thread(){
        	public void run(){
        		while (true){
        			try {
						this.sleep(1000);
						requestRender();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        	}
        };
        mThread.start();
    }
    
	public void launchFirework(double x, double y, double d) {
		mOverlay.launchFirework(x, y, d);
		
	}
}
