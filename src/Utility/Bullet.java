package Utility;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Bullet object used by both players and enemies.
 * Utilizes the Weapon interface.
 * 
 * @author Mathonwy Dean-Hall
 *
 */
public class Bullet implements Weapon{
	
	private int xPos;
	private int yPos;
	
	private double theta;
	
	private int speed;
	private int dist;
	private final int MAX_DIST = 1500;
	
	private boolean alive;
	
	private final int size = 20;
	
	private BufferedImage img;

	/**
	 * Bullet Constructor
	 * 
	 * @param x
	 * 		int X-Coordinate origin of Bullet
	 * @param y
	 * 		int Y-Coordinate origin of Bullet
	 * @param t
	 * 		double Angle toward the center of circle from origin
	 * @param player
	 * 		boolean True if Player fired the Bullet, False if enemy
	 */
	public Bullet( int x, int y, double t, boolean player){
		xPos = x;
		yPos = y;
		theta = t;
		speed = 5;
		alive = true;
		if (player) {
			img = ImageLoader.loadImage("resources/Images/Your_Ship_Bullet_N.png");
		}
		else {
			img = ImageLoader.loadImage("resources/Images/Enemy_Bullet.png");
		}
	}
	
	/**
	 * Update Bullet position
	 */
	public void update(){
		xPos = (int) (xPos + speed * Math.cos(theta - Math.toRadians(180)));
		yPos = (int) (yPos + speed * Math.sin(theta - Math.toRadians(180)));
		dist += speed;
		if(dist > MAX_DIST) alive = false;
	}
	
	/**
	 * Draw the Bullet
	 * 
	 * @param g
	 * 		Graphics2D object we are drawing to
	 */
	public void draw(Graphics2D g){
		g.drawImage(img, ImageLoader.getRotation(theta, img), xPos - (img.getWidth()/2), yPos - (img.getHeight()/2));
	}
	
	/**
	 * Get the collision size of the Bullet
	 * 
	 * @return
	 * 		int size of Bullet
	 */
	public int getSize(){
		return size;
	}
	
	/**
	 * Get whether the Bullet is still active
	 * 
	 * @return
	 * 		boolean True if Bullet is active, False otherwise
	 */
	public boolean getAlive(){
		return alive;
	}
	
	/**
	 * Get X-Coordinate of the Bullet
	 * 
	 * @return
	 * 		int X-Coordinate of Bullet
	 */
	public int getX(){
		return xPos;
	}
	
	/**
	 * Get Y-Coordinate of the Bullet
	 * 
	 * @return
	 * 		int Y-Coordinate of Bullet
	 */
	public int getY(){
		return yPos;
	}
}
