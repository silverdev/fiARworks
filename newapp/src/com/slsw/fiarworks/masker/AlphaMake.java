package com.slsw.fiarworks.masker;

import java.util.Arrays;
import com.slsw.fiarworks.bitmapTools.PixelTools;
import com.slsw.fiarworks.bitmapTools.BlockDCT;
import android.graphics.Bitmap;

public class AlphaMake {
	public enum SkyPos{
		up(),
		down(),
		left(),
		right();
	}
	
	public enum PhonePos{
		sky(),
		center(),
		down(),
	}
	
	public static final int BACKROUND =  0xffffffff;
	public static final int OPAQUE = 0;
	public static final int CUTOFF = 64;

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
	
	public static Bitmap coolMask(byte[] image, int width,int height,
			float[] currRot) {
		int[] mask = new int[height*width];
		
		int[] luma = PixelTools.YUBtoLuma(image, width, height);
		
		double[] norms = BlockDCT.computeNorms(luma, width, height, BlockDCT.BlockSize._8x8, BlockDCT.Norm.L1);
		
		for (int i = 0; i<norms.length; i++){
			if (norms[i] < CUTOFF)
			{
				mask[i] = BACKROUND;
			}
			else {
				mask[i] = OPAQUE;
			}
		}
		
		return Bitmap.createBitmap(mask, width, height, Bitmap.Config.ARGB_8888);
	}
	
	public static Bitmap SkyFillMask(byte[] image, int width,int height,
			float[] currRot) {
		int[] mask = new int[height*width];
		
		int[] luma = PixelTools.YUBtoLuma(image, width, height);
		
		double[] norms = BlockDCT.computeNorms(luma, width, height, BlockDCT.BlockSize._8x8, BlockDCT.Norm.L1);
		
		for (int i = 0; i<norms.length; i++){
			if (norms[i] < CUTOFF)
			{
				mask[i] = BACKROUND;
			}
			else {
				mask[i] = OPAQUE;
			}
			
		}
		
		
		
		return Bitmap.createBitmap(mask, width, height, Bitmap.Config.ARGB_8888);
	}
	
	private SkyPos getSkyDir(float[] currRot){
		return null;
	}
	private PhonePos getPhoneDir(float[] currRot){
		return null;
	}

}
