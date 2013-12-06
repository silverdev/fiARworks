package com.slsw.fiarworks.masker;

import java.util.Arrays;



public class AlphaMake {
public static final byte BACKROUND = (byte)0xf;
public static final byte OPAQUE = (byte)0x0;	
	public static byte[][] makeSimpleMask(byte[] rgb565, int hight, 
				int width, float[] currRot){
	 byte[][] mask = new byte[hight][];
	 for (int i = 0; i < mask.length; i++){
		 byte[] lmask = new byte[width];
		 if (i < hight/2){
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
