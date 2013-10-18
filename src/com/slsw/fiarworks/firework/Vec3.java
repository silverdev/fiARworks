package com.slsw.fiarworks.firework;

public class Vec3
{
	public double x;
	public double y;
	public double z;
	public Vec3(double x_, double y_, double z_)
	{
		x = x_;
		y = y_;
		z = z_;
	}
	
	public Vec3(Vec3 other)
	{
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}

	Vec3 add(Vec3 B)
	{
		return new Vec3(this.x+B.x, this.y+B.y, this.z+B.z);
	}
	
	static Vec3 random_velocity()
	{
		return new Vec3(0.0, 0.0, 0.0);
	}
}
