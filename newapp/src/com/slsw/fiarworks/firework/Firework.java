
package com.slsw.fiarworks.firework;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.Random;


public class Firework
{
	private ArrayList<Spark> sparks;
	static private GLFirework glfw;
	private GLCamera cam;
	private static Random r = new Random();
	public Firework(Context c, GLCamera camera)
	{
		glfw = new GLFirework(c);
		sparks = new ArrayList<Spark>();
		cam = camera;
	}
	public void Launch(float angle, float depth)
	{
		// System.out.println("Launching");
		// Vec3 start_pos = new Vec3(depth * (float) Math.cos(angle), depth * (float) Math.sin(angle), 0.0f);
		// System.out.println("X: " + start_pos.x + " Y: " + start_pos.y + "Z: " + start_pos.z);
		//possible issues:
		//radians or degrees?
		//cos and sin mixed up?

		// Vec3 start_pos = new Vec3(0.0f, -100.0f, 0.0f);
		float throw_dist = 60.0f;
		Vec3 start_pos = new Vec3(cam.facing[0]*throw_dist,cam.facing[1]*throw_dist,0.0f);
		Vec3 start_vel = new Vec3(0.0f, 0.0f, 0.3f);
		start_vel.add(Vec3.random_velocity(0.08f));
		Vec3 color = new Vec3(Vec3.random_velocity(1.0f));
		Spark start_spark = new Spark(start_pos, start_vel, color, 0);
		sparks.add(start_spark);
	}

	public void update()
	{
		ArrayList<Spark> new_sparks = new ArrayList<Spark>();
		for(int i = 0; i < sparks.size(); i++)
		{
			if(sparks.size() <= 800)
			{
				new_sparks.addAll(sparks.get(i).update());
			}
			else
			{
				if(r.nextFloat() >= 0.05)
				{
					new_sparks.addAll(sparks.get(i).update());
				}
			}
		}
		sparks = new_sparks;

		return;
	}
	public void draw(float[] MVPMatrix, Bitmap mask)
	{
		int size = sparks.size();
		float[] draw_buffer = new float[3*6*size];
		float[] tex_coord_buffer = new float[2*6*size];
		float[] color_buffer = new float[3*6*size];
		
		for(int i = 0; i < size; i++)
		{
			// generate new quad from current spark
			sparks.get(i).set_quad(draw_buffer, i*3*6, tex_coord_buffer, i*2*6, color_buffer);
		}
		glfw.updateFireworkAndDraw(draw_buffer, tex_coord_buffer, color_buffer, MVPMatrix, mask);
	}}

