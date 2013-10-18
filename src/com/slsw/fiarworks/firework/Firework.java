package com.slsw.fiarworks.firework;
import java.util.ArrayList;


public class Firework
{
	private ArrayList<Spark> sparks;
	Firework(double x, double y)
	{
		Vec3 start_pos = new Vec3(0.0, 0.0, 0.0);
		Vec3 start_vel = new Vec3(0.0, 0.0, 0.01);
		Spark start_spark = new Spark(start_pos, start_vel, 0);
	}
	void update()
	{
		ArrayList<Spark> new_sparks = new ArrayList<Spark>();
		for(int i = 0; i < sparks.size(); i++)
		{
			new_sparks.addAll(sparks.get(i).update());
		}
		sparks = new_sparks;

		return;
	}
	//returns false if animation is over
	boolean draw()
	{
		if(sparks.size() == 0)
		{
			return false;
		}

		ArrayList<Quad> quads = new ArrayList<Quad>();

		//TODO render these quads

		return true;
	}
}

