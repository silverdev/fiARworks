package com.slsw.fiarworks.firework;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ByteOrder;

public class Model
{
	public FloatBuffer geometryBuffer;
	public int num_geometry;

	Model(float[] geometry)
	{
		num_geometry = geometry.length;
		ByteBuffer bb = ByteBuffer.allocateDirect(geometry.length * 4);	//4 bytes per float
		bb.order(ByteOrder.nativeOrder());
		geometryBuffer = bb.asFloatBuffer();
		geometryBuffer.put(geometry);
		geometryBuffer.position(0);
	}
}