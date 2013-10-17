/*
 * Sets up the camera,
 * then starts camera preview and overlay
 */
package com.slsw.fiarworks;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class CameraActivity extends Activity implements OnTouchListener {
	private Camera mCamera;
	private Overlay mView;
	private CameraPreview mPrev;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("ONCREATE");
		mCamera = getCameraInstance();
		mView = new Overlay(this);
		mPrev = new CameraPreview(this.getBaseContext(), mCamera);
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
		super.onPause();
		System.out.println("ONPAUSE");
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("ONRESUME");
	}

	/*
	 * Release the camera so that other applications can use it. Called when the
	 * program is not in the forefront
	 */
	private void releaseCamera() {
		if (mCamera != null) {

			mCamera.release(); // release the camera for other applications
			mCamera = null;
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
		return true;
	}
}
