package Utility;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Small Explosion animation.
 * Implements Particle.
 * 
 * @author Mathonwy Dean-Hall
 *
 */
public class ExplosionSmall implements Particle{

	private int xPos;
	private int yPos;
	private double theta;
	
	private BufferedImage[] img;
	
	private int animState;
	
	/**
	 * ExplosionSmall Constructor
	 * 
	 * @param x
	 * 		int X-Coordinate of the origin
	 * @param y
	 * 		int Y-Coordinate of the origin
	 */
	public ExplosionSmall(int x, int y){
		xPos = x;
		yPos = y;
		
		theta = Math.toRadians(Math.random() * 360);
		
		img = new BufferedImage[2];
		img[0] = ImageLoader.loadImage("resources/Images/Coll1.png");
		img[1] = ImageLoader.loadImage("resources/Images/Coll2.png");

		animState = 0;
	}

	/**
	 * Get whether the Explosion is still active
	 * 
	 * @return
	 * 		boolean True if active, False otherwise
	 */
	public boolean isAlive() {
		return animState < 2;
	}

	/**
	 * Draw the Explosion's current animation state and increment the state
	 */
	public void draw(Graphics2D g) {
		g.drawImage(img[animState], ImageLoader.getRotation(theta, img[animState]), xPos - (img[animState].getWidth()/2), yPos - (img[animState].getHeight()/2));
		animState++;
	}

}
