
package com.slsw.fiarworks.firework;

import android.opengl.Matrix;

public class GLCamera
{
	private float[] mViewMatrix = new float[16];

	private float mDistance;
	private float mHeight;
	public GLCamera(float distance, float height)
	{
		mDistance = distance;
		mHeight = height;
		Matrix.setIdentityM(mViewMatrix, 0);
	}
	//returns view matrix
	public float[] view()
	{

		Matrix.setIdentityM(mViewMatrix, 0);
		Matrix.setLookAtM(mViewMatrix, 0, 0.0f, 0.0f, -mDistance, 0.0f, mHeight, 0.1f, 0.0f, 1.0f, 0.0f);
		return mViewMatrix;
	}
}