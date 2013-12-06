package com.slsw.fiarworks;

import java.lang.ref.SoftReference;

import android.graphics.Bitmap;

public class SynchBitmap {
	private Bitmap preview;

	public SynchBitmap(Bitmap p) {
		preview = p;
	}

	public synchronized Bitmap get() {
		return preview;
	}

	public synchronized void set(Bitmap p) {
		preview = p;
	}
}
