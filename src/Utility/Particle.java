package Utility;

import java.awt.Graphics2D;

/**
 * Interface for Particle effects such as explostions.
 * Allows for all kinds of effects to be stored in the
 * same structure.
 * 
 * @author Mathonwy Dean-Hall
 *
 */
public interface Particle {

	/**
	 * Get whether the Particle is still active
	 * 
	 * @return
	 * 		boolean True if active, False otherwise
	 */
	public boolean isAlive();
	
	/**
	 * Draw the Particle
	 * 
	 * @param g
	 * 		Graphics2D object we are drawing to
	 */
	public void draw(Graphics2D g);
	
}
