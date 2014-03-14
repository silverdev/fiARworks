
package com.slsw.fiarworks.firework;
import java.util.ArrayList;

/*
 * Types:
 * 0 - Rocket
 * 1 - Shrapnel
 *
 *
 *
 *
 *
 *
*/

public class Spark
{
	private Vec3 p1; 
	private Vec3 p2;
	private Vec3 p3;
	private Vec3 p4;
	public static final Vec3 worldx = new Vec3(1,0,0);
	public static final Vec3 worldz = new Vec3(0,0,1);
	protected Vec3 position;
	private Vec3 velocity;
	private int type;
	private int age;
	private float size;
	Spark(Vec3 pos, Vec3 vel, int t)
	{
		position = pos;
		velocity = vel;
		type = t;
		if(type == 0)
			size = 0.2f;
		else if(type == 1)
			size = 0.2f;
		age = 0;
		p1 = new Vec3();
		p2 = new Vec3();
		p3 = new Vec3();
		p4 = new Vec3();
	}
	ArrayList<Spark> update()
	{
		ArrayList<Spark> return_list = new ArrayList<Spark>();
		position.add(velocity);
		age++;
		// type 0 can spawn new sparks
		if(type == 0)
		{
			if(age >= 60) //explode after 60 frames
			{
				// System.out.println("Exploded!");
				//Explode
				for(int i = 0; i < 100; i++)
				{
					// make type 1 sparks
					Spark new_spark = new Spark(new Vec3(position), Vec3.random_velocity(0.06f), 1);
					// Spark new_spark = new Spark(new Vec3(position), Vec3.negz_velocity(0.04f), 1);

					return_list.add(new_spark);
				}
			}
			else
			{
				return_list.add(this);
			}
		}
		// type 1 can't spawn any more sparks
		else if(type == 1)
		{
			size = size * 0.99f;
			if(size > 0.02f)
				return_list.add(this);
		}
		return return_list;
	}

	public void set_quad(float[] buffer, int offset, float[] tex_coord_buffer, int tex_coord_offset)
	{
		// spark center pos in world coords
		Vec3 p_ = position;

		boolean useBillboarding = true;

		if (!useBillboarding)
		{
			// NON-ROTATED quad coordinates in XZ plane, Z being up.
			// Assumes points are set to p_ first!
			// IF THE BILLBOARDING DOESN'T WORK, SWITCH TO THIS FOR NOW. 
			p1.set(p_.x, p_.y, p_.z);
			p2.set(p_.x, p_.y, p_.z);
			p3.set(p_.x, p_.y, p_.z);
			p4.set(p_.x, p_.y, p_.z);
			// bottom-left
			p1.add(-size, -size, 0.0f);
			// bottom-right		
			p2.add(size, -size, 0.0f);
			// top-left
			p3.add(-size, size, 0.0f);
			// top-right		
			p4.add( size, size, 0.0f);
		}

		else
		{
			// ROTATED billboard coordinates. These should face the origin.
			// Not tested as of 6AM on Dec 5.
			
			// the objy axis points out the back of the quad
			Vec3 objy = new Vec3(p_);
			objy.unitize();
			
			// objx is generally the horizontal axis of the quad
			Vec3 objx;
			// if the particle is almost directly above or below the viewer
			// then we need to do something different to avoid numerical problems
			if (objy.isAlmostVertical()) {
				objx = objy.cross(worldx);
				
				// should be the same but faster maybe?
				//objx = new Vec3(0, objy.z, -objy.y); 
			}
			// ordinary situation
			else {
				objx = objy.cross(worldz);
				
				// should be the same but faster maybe?
				//objx = new Vec3(objy.y, -objy.x, 0);
			}
			objx.unitize();
			
			// objz is generally the vertical axis of the quad
			Vec3 objz = objx.cross(objy);
			objz.unitize();
			
			// half width of quad in objx dimension
			Vec3 hobjx = new Vec3(objx);
			hobjx.mult(size);
			
			// half width of quad in objz dimension
			Vec3 hobjz = new Vec3(objz);
			hobjz.mult(size);

			// start with points centered on the spark
			p1 = new Vec3(p_);
			p2 = new Vec3(p_);
			p3 = new Vec3(p_);
			p4 = new Vec3(p_);
			// shift points into position based on calculated half-width vectors
			// bottom-left
			p1.sub(hobjx);
			p1.sub(hobjz);
			// bottom-right		
			p2.add(hobjx);
			p2.sub(hobjz);
			// top-left
			p3.sub(hobjx);
			p3.add(hobjz);
			// top-right		
			p4.add(hobjx);
			p4.add(hobjz);

		}

		set_vertex(buffer, offset + 0 , p1);
		set_vertex(buffer, offset + 3 , p3);
		set_vertex(buffer, offset + 6, p2);
		set_vertex(buffer, offset + 9, p4);
		set_vertex(buffer, offset + 12, p3);
		set_vertex(buffer, offset + 15, p2);

		set_texture(tex_coord_buffer, tex_coord_offset + 0,  0.0f, 0.0f);
		set_texture(tex_coord_buffer, tex_coord_offset + 2,  0.0f, 1.0f);
		set_texture(tex_coord_buffer, tex_coord_offset + 4,  1.0f, 0.0f);
		set_texture(tex_coord_buffer, tex_coord_offset + 6,  1.0f, 1.0f);
		set_texture(tex_coord_buffer, tex_coord_offset + 8,  0.0f, 1.0f);
		set_texture(tex_coord_buffer, tex_coord_offset + 10, 1.0f, 0.0f);
	}



	private void set_vertex(float[] buffer, int offset, Vec3 p)
	{
		buffer[offset + 0] = p.x;
		buffer[offset + 1] = p.y;
		buffer[offset + 2] = p.z;
	}

	private void set_texture(float[] buffer, int offset, float a, float b)
	{
		buffer[offset + 0] = a;
		buffer[offset + 1] = b;
	}
}