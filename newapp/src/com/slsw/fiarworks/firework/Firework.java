
package com.slsw.fiarworks.firework;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;

public class Firework
{
	private ArrayList<Spark> sparks;
	static private GLFirework glfw;
	public Firework(Context c)
	{
		glfw = new GLFirework(c);
		sparks = new ArrayList<Spark>();
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
		float throw_dist = 100.0f;
		// Vec3 start_pos = new Vec3(GLCamera.dangerous_pointing[0]*throw_dist,GLCamera.dangerous_pointing[1]*throw_dist,GLCamera.dangerous_pointing[2]*throw_dist);
		// Vec3 start_vel = new Vec3(0.0f, 0.0f, 0.01f);
		Vec3 start_pos = new Vec3(0.0f, 0.0f, 20.0f);
		Vec3 start_vel = new Vec3(0.0f, 0.0f, 0.0f);
		Spark start_spark = new Spark(start_pos, start_vel, 0);
		sparks.add(start_spark);
	}

	public void update()
	{
		ArrayList<Spark> new_sparks = new ArrayList<Spark>();
		for(int i = 0; i < sparks.size(); i++)
		{
			new_sparks.addAll(sparks.get(i).update());
		}
		sparks = new_sparks;

		return;
	}
	public void draw(float[] MVPMatrix, Bitmap mask)
	{
		float[] draw_buffer = new float[3*6*sparks.size()+100];
		float[] tex_coord_buffer = new float[2*6*sparks.size()+100];
		for(int i = 0; i < sparks.size(); i++)
		{
			// generate new quad from current spark
			Quad q = new Quad(sparks.get(i));
			q.set_quad(draw_buffer, i*3*6, tex_coord_buffer, i*2*6);
		}
		glfw.updateFireworkAndDraw(draw_buffer, tex_coord_buffer, MVPMatrix, mask);
	}
}

