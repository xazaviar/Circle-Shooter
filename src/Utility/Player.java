package Utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import arcadia.Input;
import arcadia.Button;

public class Player {
	private int xPos;
	private int yPos;
	private int xOrigin;
	private int yOrigin;

	private double theta;

	private Color color;

	private int radius;

	private int speed;

	private final int size = 20;

	private BufferedImage[] img;	//Array of player sprites
	private int animState;			//Which sprite to use
	
	private int shootDelay;

	//x and y are the center of the circle player travels in
	//r is the radius of the circle
	public Player(int x, int y, int r){
		xPos = 0;
		yPos = 0;
		xOrigin = x;
		yOrigin = y;
		theta = Math.PI / 2;
		color = Color.green;
		radius = r;
		speed = 4;
		img = new BufferedImage[5];
		img[0] = ImageLoader.loadImage("resources/Your_Ship_STILL.png");
		img[1] = ImageLoader.loadImage("resources/Your_Ship_LEFT1.png");
		img[2] = ImageLoader.loadImage("resources/Your_Ship_LEFT2.png");
		img[3] = ImageLoader.loadImage("resources/Your_Ship_RIGHT1.png");
		img[4] = ImageLoader.loadImage("resources/Your_Ship_RIGHT2.png");
		animState = 0;
		shootDelay = 0;
		//System.out.println("xPos = " + xPos + " | yPos = " + yPos);
	}

	public int getX(){
		return xPos;
	}

	public int getY(){
		return yPos;
	}

	public Color getColor(){
		return color;
	}

	public BufferedImage getSprite(){
		return img[animState];
	}

	public void setColor( Color c ){
		color = c;
	}

	public int getSize(){
		return this.size;
	}

	/*
	 * Creates a rotational matrix which rotates the
	 * player sprite. Code found at
	 * http://stackoverflow.com/questions/8639567/java-rotating-images
	 */
	public AffineTransformOp getRotation(){
		double rotationRequired = theta - Math.toRadians(90);
		double locationX = img[animState].getWidth() / 2;
		double locationY = img[animState].getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op;
	}

	/*
	 * Toggles between engine sprites
	 */
	private int toggleState(){
		if( animState % 2 == 0 ){
			animState -= 1;
			return animState;
		} else {
			animState += 1;
			return animState;
		}
	}

	/*
	 *	changes position of the player to simulate movement
	 *	movement code courtesy of
	 *	http://stackoverflow.com/questions/16802431/trouble-making-object-move-in-a-circle
	 */
	public Bullet updatePos( Input input, Ring ring ){
		

		//PRESSING LEFT
		if( input.pressed(Button.L)){
			int seg = Calc.shipOnRing(new Point(this.xPos,this.yPos), this.size, ring);
			
			System.out.println("LEFT --> RING: "+ring.ring.length+" || seg: "+seg);
			if(ring.ring[(seg+1)%ring.ring.length].health!=0){
				theta = theta + Math.toRadians(speed);
				xPos = (int)(xOrigin + radius * Math.cos(theta));
				yPos = (int)(yOrigin + radius * Math.sin(theta));
				if( animState != 3 || animState != 4 ){
					animState = 3;
				} else {
					toggleState();
				}
				
				//Adjust if no longer on ring
				seg = Calc.shipOnRing(new Point(this.xPos,this.yPos), this.size, ring);
				if(ring.ring[seg].health==0){
					for(int i = (seg-1<0?ring.ring.length-1:seg-1); i != seg; seg =(seg-1<0?ring.ring.length-1:seg-1)){
						if(ring.ring[i].health > 0){
							xPos = ring.ring[i].p2.x;
							yPos = ring.ring[i].p2.y;
						}
					}
				}
			}
		}
		//PRESSING RIGHT
		else if( input.pressed(Button.R)){
			int seg = Calc.shipOnRing(new Point(this.xPos,this.yPos), this.size, ring);

			System.out.println("RIGHT--> RING: "+ring.ring.length+" || seg: "+seg);
			if(ring.ring[seg].health!=0){
				theta = theta + Math.toRadians(-speed);
				xPos = (int)(xOrigin + radius * Math.cos(theta));
				yPos = (int)(yOrigin + radius * Math.sin(theta));
				if( animState != 1 || animState != 2 ){
					animState = 1;
				} else {
					toggleState();
				}
				
				//Adjust if no longer on ring
				seg = Calc.shipOnRing(new Point(this.xPos,this.yPos), this.size, ring);
				if(ring.ring[seg].health==0){
					for(int i = (seg+1)%ring.ring.length; i != seg; seg=(seg+1)%ring.ring.length){
						if(ring.ring[i].health > 0){
							xPos = ring.ring[i].p1.x;
							yPos = ring.ring[i].p1.y;
						}
					}
				}
			}
		}
		//NO MOVEMENT
		else {
			xPos = (int)(xOrigin + radius * Math.cos(theta));
			yPos = (int)(yOrigin + radius * Math.sin(theta));
			animState = 0;
		}
		//Shoot your gun
		if( input.pressed(Button.A) && shootDelay <= 0){
			shootDelay = 5;
			return new Bullet(xPos, yPos, theta);
		}
		shootDelay--; //possible underflow
		return null;
	}
	
	public void draw(Graphics2D g){
		g.drawImage(img[animState], getRotation(), xPos - (img[animState].getWidth()/2), yPos - (img[animState].getHeight()/2));
	}

}
