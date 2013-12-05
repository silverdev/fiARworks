package com.slsw.fiarworks.bitmapTools;

import android.graphics.Bitmap;

public class PixelTools {
	 public static Bitmap makeBlackAndWhiteBitmap(byte[] image, int width, int height){
	    	int[] RGBAImage = new int[width * height];
	    	applyGrayScale(RGBAImage, image, width, height);
			return Bitmap.createBitmap(RGBAImage, width, height, Bitmap.Config.ARGB_8888);
	    	
	    }
	    /**
	     * Converts YUV420 NV21 to Y888 (RGB8888). The grayscale image still holds 3 bytes on the pixel.
	     * 
	     * @param pixels output array with the converted array o grayscale pixels
	     * @param data byte array on YUV420 NV21 format.
	     * @param width pixels width
	     * @param height pixels height
	     */
	    public static void applyGrayScale(int [] pixels, byte [] data, int width, int height) {
	        int p;
	        int size = width*height;
	        for(int i = 0; i < size; i++) {
	            p = data[i] & 0xFF;
	            pixels[i] = 0xff000000 | p<<16 | p<<8 | p;
	        }
	    }

}
