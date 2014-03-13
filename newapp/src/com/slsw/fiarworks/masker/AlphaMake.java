package com.slsw.fiarworks.masker;

import java.util.Arrays;

import android.graphics.Bitmap;

import com.slsw.fiarworks.bitmapTools.BlockDCT;
import com.slsw.fiarworks.bitmapTools.PixelTools;

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
	
	public static final int SKY =  0xffffffff;
	public static final int MABY =  0x00000001;
	public static final int NOTSKY = 0;
	public static final int CUTOFF = 121;
	private static BlockDCT.BlockSize bsize = BlockDCT.BlockSize._8x8;
	
	public static Bitmap makeSimpleMask(byte[] image, int width,int height,
			float[] currRot) {
		int[] mask = new int[height*width];
		for (int i = 0; i < mask.length; i++) {
			
			if (i < height * width / 2) {
				mask[i] = SKY;
			} else {
				mask[i] = NOTSKY;
			}
		}
		return Bitmap.createBitmap(mask, width, height, Bitmap.Config.ARGB_8888);
	}

	
	
	public static Bitmap coolMask(byte[] image, int width,int height,
			float[] currRot) {
		
		int n=1<<bsize.getLog();
		int mx=width/n;
		int my=height/n;
		int[] mask = new int[height*width];
		
		int[] luma = PixelTools.YUBtoLuma(image, width, height);
		
		double[] norms = BlockDCT.computeNorms(luma, width, height, bsize, BlockDCT.Norm.L1);
		
		for (int y = 0; y<height; y++){
			for (int x = 0; x<width; x++){
			int bx=x/n;
			int by=y/n;
			if (norms[by * mx + bx] < CUTOFF)
			{
				mask[y * width + x] = SKY;
			}
			else {
				mask[y * width + x] = NOTSKY;
			}
			}
		}
		
		return Bitmap.createBitmap(mask, width, height, Bitmap.Config.ARGB_8888);
	}
	
	public static Bitmap skyFillMask(byte[] image, int width, int height,
			float[] currRot) {
		
		int n=1<<bsize.getLog();
		int mx=width/n;
		int my=height/n;
		
		int[] luma = PixelTools.YUBtoLuma(image, width, height);

		int[] mask = null;
		switch (getPhoneDir(currRot)) {
		case center:
			mask = makeDCTMask(width, height, luma);
			switch (getSkyDir(currRot)) {
			
			case down:
				for (int i = 0; i < mx; i++){
					if (mask[(my - 1) * mx + i] == MABY)
					PixelTools.floodFill(SKY, mask, i, my - 1 , mx, my);
				}
				break;
			case left:
				for (int i = 0; i < my; i++){
					if (mask[i * mx] == MABY)
					PixelTools.floodFill(SKY, mask, 0, i, mx, my);
					
				}
				break;
			case right:
				for (int i = 0; i < my; i++){
					if (mask[i * mx + (mx - 1)] == MABY){
					PixelTools.floodFill(SKY, mask, mx - 1 , i, mx, my);
					}
				}
				break;
			case up:
				for (int i = 0; i < mx; i++){
					if (mask[i] == MABY){
						PixelTools.floodFill(SKY, mask, i, 0, mx, my);
					}
				}
				break;
			default:
				break;

			}
			break;
		case down:
			
			return Bitmap
			.createBitmap(new int[height * width], width, height, Bitmap.Config.ARGB_8888);
		case sky:
			mask = new int [mx * my];
			Arrays.fill(mask, SKY);
			break;
		default:
			break;
		}
		
		for (int y = 0; y<height; y++){
			for (int x = 0; x<width; x++){
			int bx=x/n;
			int by=y/n;
			
			luma[y * width + x] = mask[by * mx + bx];
			
			
			}
		}
		

		return Bitmap
				.createBitmap(luma, width, height, Bitmap.Config.ARGB_8888);
	}



	private static int[] makeDCTMask(int width, int height, int[] luma) {
		double[] norms = BlockDCT.computeNorms(luma, width, height,
				BlockDCT.BlockSize._8x8, BlockDCT.Norm.L1);
		int[] mask = new int[norms.length];
		
		for (int i = 0; i < norms.length; i++) {
			if (norms[i] < CUTOFF) {
				mask[i] = MABY;
			} else {
				mask[i] = NOTSKY;
			}
		}
		return mask;
	}
	

	private static SkyPos getSkyDir(float[] currRot){
		float newY = currRot[7];
		float newX = currRot[6];
		if(Math.abs(newY)<Math.abs(newX)){
			if(newX>0){
				return SkyPos.up;
			} else{
				return SkyPos.down;
			}
		} else{
			if(newY>0){
				return SkyPos.left;
			} else{
				return SkyPos.right;
			}
		}
	}

	private static PhonePos getPhoneDir(float[] currRot){
		float newZ = currRot[8];
		if(newZ<-.75) return PhonePos.sky;
		if(newZ>.5) return PhonePos.down;
		return PhonePos.center;
	}
}
