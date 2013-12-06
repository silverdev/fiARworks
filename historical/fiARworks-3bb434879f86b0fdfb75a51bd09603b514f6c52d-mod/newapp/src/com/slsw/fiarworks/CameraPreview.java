/*
 * Draw camera preview to the screen
 */
package com.slsw.fiarworks;

import java.io.IOException;
import java.util.Arrays;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.slsw.fiarworks.firework.GLRenderer;
import com.slsw.fiarworks.masker.AlphaMake;

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback, PreviewCallback, SensorEventListener {
	private SurfaceHolder mHolder;
	public volatile Camera mCamera;
	private float[] mRotVec;
	private float[] mRotOld;
	public volatile boolean sendBitmap;
	private byte[][] mask = null;
	public GLRenderer mRenderer;

	public CameraPreview(Context context, Camera camera, GLRenderer renderer) {
		super(context);
		mCamera = camera;
		mHolder = getHolder();
		mHolder.addCallback(this);
		sendBitmap=true;
		mRenderer=renderer;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (mCamera != null) {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			}else { System.out.println("CAMERA NULL IN SURFACECREATED"); }
		} catch (IOException e) {
			Log.d(VIEW_LOG_TAG,
					"Error setting camera preview: " + e.getMessage());
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			//mCamera.stopPreview();
			//mCamera.release();
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Rotation not enabled, possibly will be in the future

		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// camera does not exist or is not previewing
		}

		// Once rotation is enabled, code for that will go here

		// start preview with new settings
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();

		} catch (Exception e) {
			Log.d(VIEW_LOG_TAG,
					"Error starting camera preview: " + e.getMessage());
		}
	}

	/*
	 * Surface or settings changed
	 */
	public void setCamera(Camera c) {
		mCamera = c;
	}

	/*
	 * This function is called for every new preview frame. This is the data that we are receiving from camera!
	 * Pass this along to the Overlay, but first compress it down to a reasonable size and convert it into a bitmap.
	 */
	@Override
	public void onPreviewFrame(byte[] data, Camera c) { 
		if(sendBitmap){
	        Camera.Parameters p = c.getParameters();
	        int width = p.getPreviewSize().width;
	        int height = p.getPreviewSize().height;
	        
	        sendBitmap=true;
	        
	        mask = AlphaMake.makeSimpleMask(data, height, width, mRotVec);
	        mRotOld=mRotVec.clone();
	        mRenderer.setTextures(data, mask);
	        mRenderer.setChange(changeInRot());
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		//System.err.println(Arrays.toString(event.values));
		if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
			//Update rotation and position vector
			mRotVec = event.values;
			mRenderer.setChange(changeInRot());
		}
	}
	
	public float[] changeInRot(){
		if (mRotOld == null) {
			mRotOld = mRotVec.clone();
		}
		float[] angleChange = new float[]{0f,0f,0f};
		//SensorManager.getAngleChange (angleChange, mRotVec, mRotOld);
		return angleChange;
		
	}
	
	
}
