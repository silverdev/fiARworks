package com.slsw.fiarworks.firework;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Calendar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.slsw.fiarworks.CameraPreview;
import com.slsw.fiarworks.bitmapTools.PixelTools;
import com.slsw.fiarworks.masker.AlphaMake;

public class GLRenderer implements GLSurfaceView.Renderer {
	private static final String TAG = "GLRenderer";
	private Firework mFirework;

	private GLCamera mCamera;
	private float[] mProjMatrix = new float[16];
	private float[] mVMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];
	private GLBackground mBackground;
	private Bitmap mBackgroundImage = Bitmap.createBitmap(1, 1,
			Bitmap.Config.RGB_565);
	private static Bitmap myMask = Bitmap.createBitmap(1, 1,
			Bitmap.Config.RGB_565);
	private Bitmap[] imgs = new Bitmap[8];
	private Context context;
	private int captured = 0;
	private int wait = 0;

	public GLRenderer(Context c){
		super();
		context=c;
	}
	
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        // GLES20.glDepthFunc( GLES20.GL_LEQUAL );
        // GLES20.glDepthMask( true );
		mBackground = new GLBackground();
		mFirework = new Firework(context);
		mCamera = new GLCamera();
		mBackgroundImage = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
		mBackgroundImage.setPixel(0, 0, 0xffffff);
		myMask = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
		myMask.setPixel(0, 0, 0xffffff);
		mFirework.Launch(0.0f, 10.0f);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// Adjust the viewport based on geometry changes,
		// such as screen rotation
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;

		// this projection matrix is applied to object coordinates
		// in the onDrawFrame() method
        Matrix.setIdentityM(mProjMatrix, 0);
		Matrix.perspectiveM (mProjMatrix, 0, 45.0f, ratio, 0.1f, 100000.0f);
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearDepthf(1.0f);

		// Draw background color
		// GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		// Set the camera position (View matrix)

        // Calculate the projection and view transformation
        //mBackground.draw_background(mBackgroundImage, myMask);
        mVMatrix = mCamera.view();
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        mFirework.update();
        mFirework.draw(mMVPMatrix);
        // mBackground.draw_foreground(mBackgroundImage, myMask);

	}

	public void setTextures(byte[] image, int width, int height,
			CameraPreview prev) {


		//Bitmap BackgroundImage = PixelTools.makeBlackAndWhiteBitmap(image,
		//		width, height);
		 Bitmap BackgroundImage = PixelTools.makeColorBitmap(image, width,
		 height);
		if (BackgroundImage == null) {
			System.err.println("FAILED to make Bitmap");
			
		} else {
			 //saveImages(BackgroundImage);
			//myMask = BackgroundImage;
			myMask = AlphaMake.skyFillMask(image, width, height, prev.mRotVec);
			mBackgroundImage = BackgroundImage;
		}
		
		
		mCamera.updateView(prev.mRotVec);
	}

	private void saveImages(Bitmap b) {
		if (wait < 60) {
			wait++;
		} else if (captured < imgs.length) {
			imgs[captured] = b;
			captured++;
		} else if (captured == imgs.length) {
			// Save images to file
			Calendar c = Calendar.getInstance();
			int seconds = c.get(Calendar.SECOND);
			int min = c.get(Calendar.MINUTE);
			String seq = min + "_" + seconds;
			for (int i = 0; i < captured; i++) {
				Bitmap bmp = imgs[i];
				try {
					FileOutputStream o = new FileOutputStream(
							"storage/sdcard0/testimg" + seq + "_" + i + ".png");
					bmp.compress(Bitmap.CompressFormat.PNG, 100, o);
					o.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			captured++;
		}
	}

	public void setChange(float[] rot) {
		System.err.println(Arrays.toString(rot));
	}
    //x,y are screen coords
    //we need to get the y-axis angle in world coords
	public void launchFirework(float x, float y) {
        System.out.println("Launching in GLRenderer");
        float angle = (float)Math.atan2(mCamera.pointing[1], mCamera.pointing[0]);
        mFirework.Launch(angle, 10.0f);
	}

}