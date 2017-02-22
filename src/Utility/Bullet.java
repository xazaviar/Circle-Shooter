package Utility;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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
	 * p is true if the player fired the shot
	 */
	public Bullet( int x, int y, double t){
		xPos = x;
		yPos = y;
		theta = t;
		speed = 5;
		alive = true;
		img = ImageLoader.loadImage("resources/Images/Your_Ship_Bullet_N.png");
	}
	
	public void update(){
		xPos = (int) (xPos + speed * Math.cos(theta - Math.toRadians(180)));
		yPos = (int) (yPos + speed * Math.sin(theta - Math.toRadians(180)));
		dist += speed;
		if(dist > MAX_DIST) alive = false;
	}
	
	public void draw(Graphics2D g){
		g.drawImage(img, getRotation(), xPos - (img.getWidth()/2), yPos - (img.getHeight()/2));
	}
	
	private AffineTransformOp getRotation(){
		double rotationRequired = theta - Math.toRadians(90);
		double locationX = img.getWidth() / 2;
		double locationY = img.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op;
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
