package com.slsw.fiarworks.firework;
import java.util.ArrayList;

public class Spark
{
	protected Vec3 position;
	private Vec3 velocity;
	private int type;
	private int age;
	Spark(Vec3 pos, Vec3 vel, int t)
	{
		position = pos;
		velocity = vel;
		type = t;

	}
	ArrayList<Spark> update()
	{
		ArrayList<Spark> return_list = new ArrayList<Spark>();
		position = position.add(velocity);
		if(type == 0)
		{
			if(position.z > 5.0)
			{
				//Explode
				for(int i = 0; i < 100; i++)
				{
					Spark new_spark = new Spark(new Vec3(position), Vec3.random_velocity(), 1);
					return_list.add(new_spark);
				}
			}
			else
			{
				return_list.add(this);
			}
		}
		if(type == 0)
		{
			return_list.add(this);
		}
		return return_list;
	}
}