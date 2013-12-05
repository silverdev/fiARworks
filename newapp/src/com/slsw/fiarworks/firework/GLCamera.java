
package com.slsw.fiarworks.firework;

import android.opengl.Matrix;

public class GLCamera
{
	private float[] mViewMatrix = new float[16];

	public GLCamera()
	{
		Matrix.setIdentityM(mViewMatrix, 0);
	}
	//returns view matrix
	public float[] view()
	{

		// Matrix.setIdentityM(mViewMatrix, 0);
		// Matrix.setLookAtM(mViewMatrix, 0, 0.0f, mHeight, -mDistance, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		return mViewMatrix;
	}

	public void updateView(float[] dir)
	{
		mViewMatrix[0] = dir[0];
		mViewMatrix[1] = dir[1];
		mViewMatrix[2] = dir[2];
		mViewMatrix[4] = dir[3];
		mViewMatrix[5] = dir[4];
		mViewMatrix[6] = dir[5];
		mViewMatrix[8] = dir[6];
		mViewMatrix[9] = dir[7];
		mViewMatrix[10] = dir[8];
		mViewMatrix[15] = 1.0f;
	}

}