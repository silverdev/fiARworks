
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
		Vec3 start_pos = new Vec3(depth * (float) Math.cos(angle), depth * (float) Math.sin(angle), 0.0f);
		// System.out.println("X: " + start_pos.x + " Y: " + start_pos.y + "Z: " + start_pos.z);
		//possible issues:
		//radians or degrees?
		//cos and sin mixed up?

		// Vec3 start_pos = new Vec3(0.0f, 100.0f, 0.0f);
		Vec3 start_vel = new Vec3(0.0f, 0.0f, 0.2f);
		// Vec3 start_pos = new Vec3(0.0f, -100.0f, 0.0f);
<<<<<<< HEAD:newapp/src/com/slsw/fiarworks/firework/Firework.java
		float throw_dist = 100.0f;
		// Vec3 start_pos = new Vec3(GLCamera.dangerous_pointing[0]*throw_dist,GLCamera.dangerous_pointing[1]*throw_dist,GLCamera.dangerous_pointing[2]*throw_dist);
		// Vec3 start_vel = new Vec3(0.0f, 0.0f, 0.01f);
		Vec3 start_pos = new Vec3(0.0f, 0.0f, 20.0f);
		Vec3 start_vel = new Vec3(0.0f, 0.0f, 0.0f);
=======
		// float throw_dist = 100.0f;
		// Vec3 start_pos = new Vec3(GLCamera.dangerous_pointing[0]*throw_dist,GLCamera.dangerous_pointing[1]*throw_dist,GLCamera.dangerous_pointing[2]*throw_dist);
		// Vec3 start_vel = new Vec3(0.0f, 0.0f, 0.01f);
>>>>>>> 1f58e7643637ff32b8f64584501cd873c89623a7:src/com/slsw/fiarworks/firework/Firework.java
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
<<<<<<< HEAD:newapp/src/com/slsw/fiarworks/firework/Firework.java
		int size = sparks.size();
		float[] draw_buffer = new float[3*6*size];
		float[] tex_coord_buffer = new float[2*6*size];
		
		for(int i = 0; i < size; i++)
=======
		float[] draw_buffer = new float[3*6*sparks.size()];
		float[] tex_buffer = new float[2*6*sparks.size()];
		//bug with size of buffers and stuff with multiple fireworks
		for(int i = 0; i < sparks.size(); i++)
>>>>>>> 1f58e7643637ff32b8f64584501cd873c89623a7:src/com/slsw/fiarworks/firework/Firework.java
		{
			// generate new quad from current spark

			Quad q = new Quad(sparks.get(i));
<<<<<<< HEAD:newapp/src/com/slsw/fiarworks/firework/Firework.java
			q.set_quad(draw_buffer, i*3*6, tex_coord_buffer, i*2*6);
		}
		glfw.updateFireworkAndDraw(draw_buffer, tex_coord_buffer, MVPMatrix, mask);
=======
			q.set_quad(draw_buffer, i*3*6, tex_buffer, i*2*6);
		}
		glfw.updateFireworkAndDraw(draw_buffer, tex_buffer, MVPMatrix);
>>>>>>> 1f58e7643637ff32b8f64584501cd873c89623a7:src/com/slsw/fiarworks/firework/Firework.java
	}
}

