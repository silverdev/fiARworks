package com.slsw.fiarworks.masker;

import java.util.Arrays;

import android.graphics.Bitmap;



public class AlphaMake {
public static final byte BACKROUND = (byte)0xf;
public static final byte OPAQUE = (byte)0x0;	
	public static byte[][] makeSimpleMask(Bitmap image, int height, 
				int width, float[] currRot){
	 byte[][] mask = new byte[height][];
	 for (int i = 0; i < mask.length; i++){
		 byte[] lmask = new byte[width];
		 if (i < height/2){
			 Arrays.fill(lmask, BACKROUND);
		 }
		 else {
			 Arrays.fill(lmask, OPAQUE);
		 	}
		 mask[i] = lmask;
	 }
	return mask;		
	}
	
}
