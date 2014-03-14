package com.slsw.fiarworks;

import java.io.FileOutputStream;
import java.lang.ref.SoftReference;

import com.slsw.fiarworks.masker.AlphaMake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Environment;
import android.view.Display;
import android.view.View;

public class Overlay extends View {
	private Paint p;
	private CameraPreview preview;
	private int frame;
	private final int FRAMERATE = 60;
	private SynchBitmap mask;

	public Overlay(Context context, CameraPreview prev) {
		super(context);
		frame = 0;
		p = new Paint();
		preview = prev;
		mask = new SynchBitmap(null);
	}

	/*
	 * For every callback, draw bitmap onto canvas. To speed this up, only draw
	 * on certain frames. Put on fireworks afterwards.
	 */
	@Override
	protected void onDraw(Canvas canvas) { // TODO: This commented out code is
											// useless, but will probably be
											// repurposed later.
	// if(frame==0){
	/*
	 * System.out.println("DRAW"); if (Overlay.preview.get() != null) {
	 * canvas.drawBitmap(Bitmap.createScaledBitmap(Overlay.preview.get(),
	 * canvas.getWidth(), canvas.getHeight(), true), 0, 0, null); } else{
	 * p.setColor(Color.argb(255, 0, 0, 0)); canvas.drawRect(0, 0,
	 * canvas.getWidth(), canvas.getHeight(), p); }
	 */
		Bitmap b = mask.get();
		if(b!=null){
			p.setColor(Color.argb(255, 0, 0, 0)); canvas.drawRect(0, 0,
					  canvas.getWidth(), canvas.getHeight(), p);
			canvas.drawBitmap(Bitmap.createScaledBitmap(b,
					 canvas.getWidth(), canvas.getHeight(), true), 0, 0, null);
		}
		super.onDraw(canvas);
		invalidate();
		// }
		// frame++;
		// if(frame>FRAMERATE){
		// frame=0;
		// }
	}

	/*
	 * Called whenever a picture is taken
	 * 
	 * @see android.hardware.Camera.PictureCallback#onPictureTaken(byte[],
	 * android.hardware.Camera)
	 */
	public void onPictureTaken(byte[] data, Camera camera) {

	}
	
	public void setTextures(byte[] image, int width, int height,
			CameraPreview prev){
		mask.set(AlphaMake.skyFillMask(image, width, height, prev.mRotVec));
	}
}