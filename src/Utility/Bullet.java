package Utility;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

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

	/*
	 * x is the x-axis origin
	 * y is the y-axis origin
	 * t is the angle towards the middle from the origin
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
	
	public void update(){
		xPos = (int) (xPos + speed * Math.cos(theta - Math.toRadians(180)));
		yPos = (int) (yPos + speed * Math.sin(theta - Math.toRadians(180)));
		dist += speed;
		if(dist > MAX_DIST) alive = false;
	}
	
	public void draw(Graphics2D g){
		g.drawImage(img, ImageLoader.getRotation(theta, img), xPos - (img.getWidth()/2), yPos - (img.getHeight()/2));
	}
	
	public int getSize(){
		return size;
	}
	
	public boolean getAlive(){
		return alive;
	}
	
	public int getX(){
		return xPos;
	}
	
	public int getY(){
		return yPos;
	}
}
