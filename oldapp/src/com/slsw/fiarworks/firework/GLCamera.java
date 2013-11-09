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
	public void view(MatrixStack stackMV)
	{

		Matrix.setIdentityM(mViewMatrix, 0);
		System.out.println("Camera Identity: ");
		for (float item : mViewMatrix) { System.out.print(item + " "); }
		System.out.println("");
		Matrix.setLookAtM(mViewMatrix, 0, mDistance, 0.0f, mHeight, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		System.out.println("camera: ");
		for (float item : mViewMatrix) { System.out.print(item + " "); }
		System.out.println("");
		stackMV.glMultMatrixf(mViewMatrix, 0);
	}
}