package Utility;

import java.awt.Graphics2D;

/**
 * Interface for Weapons such as Bullets and Bombs so that
 * they can be stored together and decrease number
 * of data structures and collision types.
 * 
 * @author Mathonwy Dean-Hall
 *
 */
public interface Weapon {
	
	/**
	 * How the weapon changes between frames
	 */
	public void update();
	
	/**
	 * Draw the weapon
	 * 
	 * @param g
	 * 		Graphics2D object we're drawing to
	 */
	public void draw(Graphics2D g);
	
	/**
	 * Get size of the weapon for collision
	 * 
	 * @return
	 * 		int representing the diameter of the hitbox of the object
	 */
	public int getSize();
	
	/**
	 * Get whether the weapon is still active
	 * 
	 * @return
	 * 		boolean true if alive, false if not
	 */
	public boolean getAlive();
	
	/**
	 * Get X-Coordinate of the weapon
	 * 
	 * @return
	 * 		int of X-Coordinate
	 */
	public int getX();
	
	/**
	 * Get Y-Coordinate of the weapon
	 * 
	 * @return
	 * 		int of Y-Coordinate
	 */
	public int getY();
	
}
