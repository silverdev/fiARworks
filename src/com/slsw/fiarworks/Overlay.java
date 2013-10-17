/*
 * Overlay draws the laughing man logos
 */
package com.slsw.fiarworks;

import java.io.FileOutputStream;
import java.lang.ref.SoftReference;

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


public class Overlay extends View{
	public static SynchBitmap preview;
	private Paint p;
	private CameraActivity cont;
	private int frame;
	private final int FRAMERATE = 5;

	public Overlay(CameraActivity context) {
		super(context);
		preview=new SynchBitmap(null);
		frame=0;
		p = new Paint();
		cont = context;
	}

	/*
	 * For every callback, draw bitmap onto canvas. To speed this up, only draw on certain frames. Put on fireworks afterwards.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		if(frame==0){
			if (Overlay.preview.get() != null) {
				canvas.drawBitmap(Bitmap.createScaledBitmap(Overlay.preview.get(), canvas.getWidth(), canvas.getHeight(), true), 0, 0, null);
			} else{
				p.setColor(Color.argb(255, 0, 0, 0));
				canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), p);
			}
			super.onDraw(canvas);
			invalidate();
		}
		frame++;
		if(frame>FRAMERATE){
			frame=0;
		}
	}

	/*
	 * Called whenever a picture is taken
	 * 
	 * @see android.hardware.Camera.PictureCallback#onPictureTaken(byte[],
	 * android.hardware.Camera)
	 */
	public void onPictureTaken(byte[] data, Camera camera) {

	}
}
