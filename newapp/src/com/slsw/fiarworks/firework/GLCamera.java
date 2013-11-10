
package com.example.android.opengl;

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
		Matrix.setLookAtM(mViewMatrix, 0, 0.0f, mHeight, -mDistance, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		// System.out.println("camera: ");
		// for (float item : mViewMatrix) { System.out.print(item + " "); }
		// System.out.println("");
		return mViewMatrix;
	}
}