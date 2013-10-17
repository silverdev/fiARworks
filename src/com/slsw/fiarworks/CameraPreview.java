/*
 * Draw camera preview to the screen
 */
package com.slsw.fiarworks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback, PreviewCallback {
	private SurfaceHolder mHolder;
	private Camera mCamera;

	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (mCamera != null) {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			}
		} catch (IOException e) {
			Log.d(VIEW_LOG_TAG,
					"Error setting camera preview: " + e.getMessage());
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.stopPreview();
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
	public void onPreviewFrame(byte[] data, Camera c) {
        Camera.Parameters p = c.getParameters();
        int width = p.getPreviewSize().width;
        int height = p.getPreviewSize().height;
        System.out.println("Width=" + width + "\nHeight=" + height);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, width, height,
                null);
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 50, out);
        byte[] imageBytes = out.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0,
                imageBytes.length, null);
        bmp = bmp.copy(Bitmap.Config.ARGB_8888, true);

        Overlay.preview.set(bmp);
	}
}
