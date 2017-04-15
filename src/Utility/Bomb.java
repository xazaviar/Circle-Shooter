package Utility;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Bomb object used by Player.
 * Implements Weapon interface.
 * 
 * @author Mathonwy Dean-Hall
 *
 */
public class Bomb implements Weapon{

	private int xPos;
	private int yPos;
	
	private int size;
	private final int maxSize = 500 * 2; //size of ring
	
	private boolean alive;
	
	/**
	 * Bomb Constructor
	 * 
	 * @param x
	 * 		int X-Coordinate of Bomb origin
	 * @param y
	 * 		int Y-Coordinate of Bomb origin
	 */
	public Bomb(int x, int y){
		xPos = x;
		yPos = y;
		size = 0;
		alive = true;
	}
	
	/**
	 * Update Bomb size
	 */
	public void update(){
		if( size < maxSize ) size += 100;
		else alive = false;
	}
	
	/**
	 * Draw the Bomb
	 * 
	 * @param
	 * 		Graphics2D object we are drawing to
	 */
	public void draw(Graphics2D g){
		g.setColor(Color.red);
		g.drawOval(xPos - (size / 2) , yPos - (size / 2), size, size);
	}

	/**
	 * Get the collision size of the Bomb
	 * 
	 * @return
	 * 		int size of Bomb
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Get whether the Bomb is still active
	 * 
	 * @return
	 * 		boolean True if Bomb is active, False otherwise
	 */
	public boolean getAlive() {
		return alive;
	}
	
	/**
	 * Get X-Coordinate of the Bomb
	 * 
	 * @return
	 * 		int X-Coordinate of Bomb
	 */
	public int getX(){
		return xPos - (size / 2);
	}
	
	/**
	 * Get Y-Coordinate of the Bomb
	 * 
	 * @return
	 * 		int Y-Coordinate of Bomb
	 */
	public int getY(){
		return yPos - (size / 2);
	}
}
