package com.slsw.fiarworks.firework;
import java.util.ArrayList;


public class Firework
{
	private ArrayList<Spark> sparks;
	static private GLFirework glfw;
	public Firework()
	{
		glfw = new GLFirework();
		sparks = new ArrayList<Spark>();
	}
	public void Launch()
	{
		System.out.println("Launching");
		Vec3 start_pos = new Vec3(0.0f, 0.0f, 0.0f);
		Vec3 start_vel = new Vec3(0.0f, 0.0f, 0.01f);
		Spark start_spark = new Spark(start_pos, start_vel, 0);
		sparks.add(start_spark);
	}

	public void update()
	{
		System.out.println("Number of sparks in update: " + sparks.size());
		ArrayList<Spark> new_sparks = new ArrayList<Spark>();
		for(int i = 0; i < sparks.size(); i++)
		{
			new_sparks.addAll(sparks.get(i).update());
		}
		sparks = new_sparks;

		return;
	}
	public void draw(MatrixStack stackMV, float[] projection)
	{
		float[] draw_buffer = new float[48*sparks.size()];
		for(int i = 0; i < sparks.size(); i++)
		{
			Quad q = new Quad(sparks.get(i));
			q.set_quad(draw_buffer, i*48);
		}
		glfw.updateFireworkAndDraw(draw_buffer, stackMV, projection);
	}
}

