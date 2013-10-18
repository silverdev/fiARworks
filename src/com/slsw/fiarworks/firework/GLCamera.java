package com.slsw.fiarworks.firework;

import android.opengl.Matrix;

public class GLCamera
{
	private float[] mViewMatrix = new float[16];

	private float mDistance;
	private float mHeight;
	public GLCamera(float distance, float height)
	{
		distance = mDistance;
		height = mHeight;
	}
	public void view(MatrixStack stackMV)
	{
		Matrix.setLookAtM(mViewMatrix, 0, mDistance, 0.0f, mHeight, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
		stackMV.glMultMatrixf(mViewMatrix, 0);
	}
}