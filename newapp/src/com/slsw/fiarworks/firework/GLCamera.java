
package com.slsw.fiarworks.firework;

import android.opengl.Matrix;

public class GLCamera
{
	public float[] mViewMatrix = new float[16];

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
		// copy 3x3 matrix elements into 4x4 matrix format
		// 0  1  2  3
		// 4  5  6  7
		// 8  9  10 11
		// 12 13 14 15
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

		Matrix.invertM(mViewMatrix, 0, mViewMatrix, 0);

<<<<<<< HEAD
		// float[] downVector = {0.0f, 0.0f, 1.0f, 1.0f};
		// float[] pointing = {0.0f, 0.0f, 0.0f, 0.0f};

		// Matrix.multiplyMV(pointing, 0, mViewMatrix, 0, downVector, 0);
		// System.out.println("Camera is pointing at: ");
		// System.out.println("X: " + pointing[0] + " Y: " + pointing[1] + "Z: " + pointing[2] + " W: " + pointing[3]);
=======
		Matrix.multiplyMV(pointing, 0, mViewMatrix, 0, downVector, 0);
		System.out.println("Camera is pointing at: ");
		System.out.println("X: " + pointing[0] + " Y: " + pointing[1] + "Z: " + pointing[2] + " W: " + pointing[3]);
		System.out.println("["+dir[0]+"]["+dir[1]+"]["+dir[2]+"]");
		System.out.println("["+dir[3]+"]["+dir[4]+"]["+dir[5]+"]");
		System.out.println("["+dir[6]+"]["+dir[7]+"]["+dir[8]+"]");
		System.out.println("---");
>>>>>>> 581f296789008181c345aee0dff23d459a3b4eaa
	}

}