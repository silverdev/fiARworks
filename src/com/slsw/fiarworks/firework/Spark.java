
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
	protected Vec3 position;
	private Vec3 velocity;
	private int type;
	private int age;
	Spark(Vec3 pos, Vec3 vel, int t)
	{
		position = pos;
		velocity = vel;
		type = t;
		age = 0;

	}
	ArrayList<Spark> update()
	{
		ArrayList<Spark> return_list = new ArrayList<Spark>();
		position.add(velocity);
		// type 0 can spawn new sparks
		if(type == 0)
		{
<<<<<<< HEAD:newapp/src/com/slsw/fiarworks/firework/Spark.java
			// System.out.println("Starter Position");
			// System.out.println("X: " + position.x + " Y: " + position.y + " Z: " + position.z);
			// if(position.z > 1.0f)
			if(true) //explode right away
=======
			System.out.println("Starter Position");
			System.out.println("X: " + position.x + " Y: " + position.y + " Z: " + position.z);
			if(position.z > 55.0f)
>>>>>>> 1f58e7643637ff32b8f64584501cd873c89623a7:src/com/slsw/fiarworks/firework/Spark.java
			{
				// System.out.println("Exploded!");
				//Explode
				for(int i = 0; i < 100; i++)
				{
					// make type 1 sparks
					Spark new_spark = new Spark(new Vec3(position), Vec3.random_velocity(0.3f), 1);
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
			age++;
			if(age < 180)
				return_list.add(this);
		}
		return return_list;
	}
}