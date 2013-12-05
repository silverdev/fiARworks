
package com.slsw.fiarworks.firework;


public class Quad
{
	Vec3 p1;
	Vec3 p2;
	Vec3 p3;
	Vec3 p4;
	// Spark generated by fireworks routine
	Quad(Spark s)
	{
		float size = 0.1f;
		// spark center pos in world coords
		Vec3 p_ = s.position;
		// size is half-width of the spark
		// bottom-left
		p1 = new Vec3(p_);
		p1.add(-size, -size, 0.0f);
		// bottom-right
		p2 = new Vec3(p_);
		p2.add(size, -size, 0.0f);
		// top-left
		p3 = new Vec3(p_);
		p3.add(-size,  size, 0.0f);
		// top-right
		p4 = new Vec3(p_);
		p4.add( size,  size, 0.0f);
	}

	public void set_quad(float[] buffer, int offset)
	{
		set_vertex(buffer, offset + 0 , p1);
		set_vertex(buffer, offset + 3 , p3);
		set_vertex(buffer, offset + 6, p2);
		set_vertex(buffer, offset + 9, p4);
		set_vertex(buffer, offset + 12, p3);
		set_vertex(buffer, offset + 15, p2);
	}

	private void set_vertex(float[] buffer, int offset, Vec3 p)
	{
		buffer[offset + 0] = p.x;
		buffer[offset + 1] = p.y;
		buffer[offset + 2] = p.z;
	}
}
