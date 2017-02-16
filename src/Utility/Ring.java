package Utility;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Ring {

	RingSegment[] ring;
	final int MAX_HEALTH;
	final int DIAMETER;
	
	/**
	 * Initializes the ring with a number of segments
	 * and the amount of health alloted
	 * @param size
	 * 			The number of segments of the ring
	 * @param maxHealth
	 * 			The maximum health of a given segment
	 * @param diameter
	 * 			The diameter of the ring
	 * @param width
	 * 			The width of the screen
	 * @param height
	 * 			The height of the screen
	 */
	public Ring(int size, int maxHealth, int diameter, int width, int height){
		this.MAX_HEALTH = maxHealth;
		this.DIAMETER = diameter;
		
		//Create the ring and initialize all 
		//segments to max health and give
		//them their points
		ring = new RingSegment[size];

		int segLength = 360/size;
        double cx = (width / 2);
        double cy = (height / 2);
        
        int theta = 0;
        Point p1, p2;
        p1 = new Point((int)(cx + diameter/2 * Math.cos(Math.toRadians(theta))), (int)(cy + diameter/2 * Math.sin(Math.toRadians(theta))));
		theta+=segLength;
		
		for(int i = 0; i < ring.length; i++){
			
			//Calculate next point
            p2 = new Point((int)(cx + diameter/2 * Math.cos(Math.toRadians(theta))), (int)(cy + diameter/2 * Math.sin(Math.toRadians(theta))));
            
            //Create the ring segment
            if(i==0)
            	ring[i] = new RingSegment(0, p1, p2);
            else if(i<ring.length-1)
            	ring[i] = new RingSegment(this.MAX_HEALTH, p1, p2);
            else
            	ring[i] = new RingSegment(1, p1, ring[0].p1);
			
			//Shift points
			theta+=segLength;
			p1 = p2;
		}
	}
	
	/**
	 * Refreshes the health of all ring segments
	 */
	public void refreshRing(){
		for(RingSegment r: ring)
			r.health = this.MAX_HEALTH;
	}
	
	/**
	 * Deals damage to a segment of the ring
	 * Assumes all damage is 1
	 * @param seg
	 * 			the segment that was hit
	 */
	public void ringSegDamage(int seg){
		if(ring[seg].health>0)
			ring[seg].health -= 1;
	}
	
	
	/**
	 * Draws the ring
	 * @param g
	 * 			The graphics element to draw with
	 */
	public void draw(Graphics g, int width, int height){
		for(RingSegment r: ring){
			if(r.health==this.MAX_HEALTH)
				g.setColor(Color.white);
			else if(r.health>0)
				g.setColor(Color.red);
			
			if(r.health>0)
				g.drawLine(r.p1.x, r.p1.y, r.p2.x, r.p2.y);
		}
	}
	
	
	/**
	 * This represents a segment of the ring
	 * 
	 * @author Joseph Ryan
	 */
	public class RingSegment{
		int health;
		Point p1, p2;
		
		/**
		 * Initializes the segment
		 * @param health
		 * 			The health of the current segment
		 * @param p1
		 * 			The starting point of the segment
		 * @param p2
		 * 			The end point of the segment
		 */
		public RingSegment(int health, Point p1, Point p2){
			this.health = health;
			this.p1 = p1;
			this.p2 = p2;
		}
	}
}