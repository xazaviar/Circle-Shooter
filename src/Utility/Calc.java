package Utility;

import java.awt.Point;
import java.util.Random;

//import Utility.Ring.RingSegment;

/**
 * Class of static helper methods for Ring calculations regarding
 * collisions and Player movement.
 * 
 * @author Joseph Ryan
 *
 */
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
		return Math.pow((p2.x+s2/2)-(p1.x+s1/2),2)+Math.pow((p2.y+s2/2)-(p1.y+s1/2),2) <= Math.pow(s1/2+s2/2,2);
	}
	
	
	/**
	 * This checks if an object has collided 
	 * with the ring by checking the circle
	 * of the object and the line of a given
	 * ring segment. 
	 * @reference
	 * 		http://stackoverflow.com/questions/1073336/circle-line-segment-collision-detection-algorithm
	 * @param p
	 * 			Point of the object
	 * @param s
	 * 			size (diameter) of the object
	 * @param r
	 * 			The ring
	 * @return
	 * 			the number of the ring segment 
	 *          collided with. -1 if no collision
	 */
	public static int ringCollide(Point p, int s, Ring ring){
		
		for(int i = 0; i < ring.ring.length; i++){
			Point d = new Point(ring.ring[i].p2.x - ring.ring[i].p1.x, ring.ring[i].p2.y - ring.ring[i].p1.y);
			Point f = new Point(ring.ring[i].p1.x - (p.x+s/2), ring.ring[i].p1.y - (p.y+s/2));
			
			double a = dot(d,d);
			double b = 2*dot(f,d);
			double c = dot(f, f) - (s/2)*(s/2);

			double discriminant = b*b-4*a*c;
			if( discriminant >= 0 ) {

				discriminant = Math.sqrt( discriminant );
				double t1 = (-b - discriminant)/(2*a);
				double t2 = (-b + discriminant)/(2*a);

				
				if( ((t1 >= 0 && t1 <= 1) || (t2 >= 0 && t2 <= 1)) && ring.ring[i].health>0) return i;
			}
		}
		
		return -1;
	}
	
	
	/**
	 * This returns what segment of the ring
	 * the player is currently on
	 * @param p
	 * 			The player's point
	 * @param s
	 * 			The player's size
	 * @param ring
	 * 			The ring
	 * @return
	 * 			The segment the player is on. -1 is an error
	 */
	public static int shipOnRing(Point p, int s, Ring ring){
		
		for(int i = 0; i < ring.ring.length; i++){
			Point d = new Point(ring.ring[i].p2.x - ring.ring[i].p1.x, ring.ring[i].p2.y - ring.ring[i].p1.y);
			Point f = new Point(ring.ring[i].p1.x - p.x, ring.ring[i].p1.y - p.y);
			
			double a = dot(d,d);
			double b = 2*dot(f,d);
			double c = dot(f, f) - (s/2)*(s/2);

			double discriminant = b*b-4*a*c;
			if( discriminant >= 0 ) {

				discriminant = Math.sqrt( discriminant );
				double t1 = (-b - discriminant)/(2*a);
				double t2 = (-b + discriminant)/(2*a);

				
				if( ((t1 >= 0 && t1 <= 1) || (t2 >= 0 && t2 <= 1))) return i;
			}
		}
		
		return -1;
	}
	
	
	/**
	 * Does the dot product of two points
	 * @param p1
	 * 			Point 1
	 * @param p2
	 * 			Point 2
	 * @return
	 * 			The dot product of two points
	 */
	private static double dot(Point p1, Point p2){
		return p1.x*p2.x+p1.y*p2.y;
	}
}
