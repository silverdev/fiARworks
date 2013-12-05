
package com.slsw.fiarworks.firework;

import java.util.Random;

public class Vec3
{
	public float x;
	public float y;
	public float z;
	private static Random r = new Random();

	public Vec3()
	{
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

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

	void add(Vec3 B)
	{
//		Changed to modify existing object instead of returning a new one.
//		return new Vec3(this.x+B.x, this.y+B.y, this.z+B.z);
		this.x += B.x;
		this.y += B.y;
		this.z += B.z;
	}

	void add(float xdiff, float ydiff, float zdiff)
	{
		this.x += xdiff;
		this.y += ydiff;
		this.z += zdiff;		
	}
	
	static Vec3 random_velocity(float speed)
	{
		float x = (r.nextFloat() - 0.5f) * speed;
		float y = (r.nextFloat() - 0.5f) * speed;
		float z = (r.nextFloat() - 0.5f) * speed;

		return new Vec3(x, y, z);
	}
}
