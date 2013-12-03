
package com.slsw.fiarworks.firework;

import java.io.ByteArrayOutputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class GLRenderer implements GLSurfaceView.Renderer{
    private static final String TAG = "GLRenderer";
	private Firework mFirework;
    private GLCamera mCamera;
    private float[] mProjMatrix = new float[16];
    private float[] mVMatrix;
    private float[] mMVPMatrix = new float[16];
    private GLBackground mBackground;
    private Bitmap mBackgroundImage =Bitmap.createBitmap(1,1,Bitmap.Config.RGB_565);
    private static final byte[][] initalMask = {{(byte)0xf}}; 
    

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mBackground = new GLBackground();
        mFirework = new Firework();
        mCamera = new GLCamera(10.0f, 5.0f);
    	mBackgroundImage.setPixel(0, 0, 0xffffff);
        mFirework.Launch();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 1.0f, 100.0f);

    }


    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // Draw background color
        //GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        mVMatrix = mCamera.view();

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        mFirework.update();
        mFirework.draw(mMVPMatrix);
        mBackground.draw(mBackgroundImage);


    }
    
    public void setTextures(byte[] image, int width, int height, byte[][] mask){
        //mBackgroundImage.recycle();
        //mBackgroundImage = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        YuvImage yuvImage = new YuvImage(image, ImageFormat.NV21, width, height,
                null);
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 50, out);
        byte[] imageBytes = out.toByteArray();
        mBackgroundImage = BitmapFactory.decodeByteArray(imageBytes, 0,
                imageBytes.length, null);
    	if (mBackgroundImage == null){
    		System.err.println("FAILED");
    	}
    	
    }
    
    public void setChange(float[] rot){
    	
    }

}