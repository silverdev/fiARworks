package com.slsw.fiarworks.firework;

import java.util.Random;

public class Vec3
{
	public float x;
	public float y;
	public float z;
	public Vec3(float x_, float y_, float z_)
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
	
	static Vec3 random_velocity(float speed)
	{
		Random r = new Random();
		r.setSeed(2);
		float x = (r.nextFloat() - 0.5f) * speed;
		float y = (r.nextFloat() - 0.5f) * speed;
		float z = (r.nextFloat() - 0.5f) * speed;

		return new Vec3(x, y, z);
	}
}
