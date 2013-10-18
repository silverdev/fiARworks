package com.slsw.fiarworks.firework;

public class Quad
{
	Vec3 p1;
	Vec3 p2;
	Vec3 p3;
	Vec3 p4;
	Quad(Spark s)
	{
		double size = 0.5;
		Vec3 p_ = s.position;
		p1 = p_.add(new Vec3(0.0, -size, -size));
		p2 = p_.add(new Vec3(0.0,  size, -size));
		p3 = p_.add(new Vec3(0.0, -size,  size));
		p4 = p_.add(new Vec3(0.0,  size,  size));
	}
}
