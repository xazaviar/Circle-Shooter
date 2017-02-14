package Utility;

import java.awt.Point;
import java.util.Random;

public abstract class Calc {

	public static int randInt(int min, int max) {
	    Random r = new Random();
	    return r.nextInt((max - min) + 1) + min;
	}
	
	/**
	 * This checks if two objects have collided by 
	 * checking the circles of the given point and
	 * radius
	 * @param p1
	 * 			Point of object 1
	 * @param s1
	 * 			size (diameter) of object 1
	 * @param p2
	 * 			Point of object 2
	 * @param s2
	 * 			size (diameter) of object 2
	 * @return
	 * 			If the two objects have collided
	 */
	public static boolean collide(Point p1, int s1, Point p2, int s2){
		return Math.pow(p2.x-p1.x,2)+Math.pow(p1.y-p2.y,2) <= Math.pow(s1/2+s2/2,2);
	}
}
