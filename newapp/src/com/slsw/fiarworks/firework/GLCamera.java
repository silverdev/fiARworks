
package com.slsw.fiarworks.firework;

import android.opengl.Matrix;

public class GLCamera
{
	public float[] mViewMatrix = new float[16];
	private float[] rotMat = new float[16];
	public float[] pointing = new float[4];

	public GLCamera()
	{
		Matrix.setIdentityM(mViewMatrix, 0);
		Matrix.setIdentityM(rotMat, 0);
	}
	//returns view matrix
	public float[] view()
	{

		// Matrix.setIdentityM(mViewMatrix, 0);
		// Matrix.setLookAtM(mViewMatrix, 0, 0.0f, 0.0f, 0.0f, pointing[0], pointing[1], pointing[2], 0.0f, 1.0f, 0.0f);
		Matrix.setLookAtM(mViewMatrix, 0, 0.0f, 0.0f, 0.0f, 0.0f, 20.0f, 5.0f, 0.0f, 1.0f, 0.0f);

		return mViewMatrix;
	}

	public void updateView(float[] dir)
	{
		// copy 3x3 matrix elements into 4x4 matrix format
		// 0  1  2  3
		// 4  5  6  7
		// 8  9  10 11
		// 12 13 14 15
		rotMat[0] = dir[0];
		rotMat[1] = dir[1];
		rotMat[2] = dir[2];
		rotMat[4] = dir[3];
		rotMat[5] = dir[4];
		rotMat[6] = dir[5];
		rotMat[8] = dir[6];
		rotMat[9] = dir[7];
		rotMat[10] = dir[8];
		rotMat[15] = 1.0f;

		Matrix.invertM(rotMat, 0, rotMat, 0);

		float[] downVector = {0.0f, 0.0f, 1.0f, 1.0f};

		Matrix.multiplyMV(pointing, 0, rotMat, 0, downVector, 0);
		// System.out.println("Camera is pointing at: ");
		// System.out.println("X: " + pointing[0] + " Y: " + pointing[1] + "Z: " + pointing[2] + " W: " + pointing[3]);
		
		/*
		System.out.println("Camera is pointing at: ");
		System.out.println("X: " + pointing[0] + " Y: " + pointing[1] + "Z: " + pointing[2] + " W: " + pointing[3]);
		System.out.println("["+dir[0]+"]["+dir[1]+"]["+dir[2]+"]");
		System.out.println("["+dir[3]+"]["+dir[4]+"]["+dir[5]+"]");
		System.out.println("["+dir[6]+"]["+dir[7]+"]["+dir[8]+"]");
		System.out.println("---");
		*/
	}

}