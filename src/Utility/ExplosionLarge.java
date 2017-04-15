package Utility;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ExplosionLarge implements Particle{

	private int xPos;
	private int yPos;
	private double theta;
	
	private BufferedImage[] img;
	
	private int animState;
	
	public ExplosionLarge(int x, int y){
		xPos = x;
		yPos = y;
		
		theta = Math.toRadians(Math.random() * 360);
		
		img = new BufferedImage[8];
		img[0] = ImageLoader.loadImage("resources/Images/c1.png");
		img[1] = ImageLoader.loadImage("resources/Images/c2.png");
		img[2] = ImageLoader.loadImage("resources/Images/c3.png");
		img[3] = ImageLoader.loadImage("resources/Images/c4.png");
		img[4] = ImageLoader.loadImage("resources/Images/c5.png");
		img[5] = ImageLoader.loadImage("resources/Images/c6.png");
		img[6] = ImageLoader.loadImage("resources/Images/c7.png");
		img[7] = ImageLoader.loadImage("resources/Images/c8.png");
		animState = 0;
	}
	
	public boolean isAlive(){
		return animState < 8;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(img[animState], ImageLoader.getRotation(theta, img[animState]), xPos - (img[animState].getWidth()/2), yPos - (img[animState].getHeight()/2));
		animState++;
	}

}
