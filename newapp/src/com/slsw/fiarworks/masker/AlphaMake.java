package com.slsw.fiarworks.masker;

import java.util.Arrays;

import android.graphics.Bitmap;

public class AlphaMake {
	public static final int BACKROUND =  0xffffffff;
	public static final int OPAQUE = 0;

	public static Bitmap makeSimpleMask(byte[] image, int width,int height,
			float[] currRot) {
		int[] mask = new int[height*width];
		for (int i = 0; i < mask.length; i++) {
			
			if (i < height * width / 2) {
				mask[i] = BACKROUND;
			} else {
				mask[i] = OPAQUE;
			}
		}
		return Bitmap.createBitmap(mask, width, height, Bitmap.Config.ARGB_8888);
	}

}
