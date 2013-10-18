package com.slsw.fiarworks.firework;

public class Quad
{
	Vec3 p1;
	Vec3 p2;
	Vec3 p3;
	Vec3 p4;
	Quad(Spark s)
	{
		float size = 0.5f;
		Vec3 p_ = s.position;
		p1 = p_.add(new Vec3(0.0f, -size, -size));
		p2 = p_.add(new Vec3(0.0f,  size, -size));
		p3 = p_.add(new Vec3(0.0f, -size,  size));
		p4 = p_.add(new Vec3(0.0f,  size,  size));
	}

	public void set_quad(float[] buffer, int offset)
	{
		set_vertex(buffer, offset + 0 , p1);
		set_vertex(buffer, offset + 8 , p3);
		set_vertex(buffer, offset + 16, p2);
		set_vertex(buffer, offset + 24, p4);
		set_vertex(buffer, offset + 32, p3);
		set_vertex(buffer, offset + 40, p2);
	}

	private void set_vertex(float[] buffer, int offset, Vec3 p)
	{
		buffer[offset + 0] = p.x;
		buffer[offset + 1] = p.y;
		buffer[offset + 2] = p.z;
	}
}
