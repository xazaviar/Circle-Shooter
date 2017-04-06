package Utility;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import arcadia.Input;
import arcadia.Button;

public class Player {
	private int xPos;
	private int yPos;
	private int xOrigin;
	private int yOrigin;

	private double theta;

	private int radius;

	private int speed;
	private final int MAX_SPEED = 4;

	private final int size = 20;

	private BufferedImage[] img;	//Array of player sprites
	private int animState;			//Which sprite to use

	private int shootDelay;
	private int bombs;
	private int bombDelay;

	private int lives;
	
	final int MAX_INV_TIME = 120;	//Max time the player is invincible after death (in frames)
	int invTime = 0;

	//x and y are the center of the circle player travels in
	//r is the radius of the circle
	public Player(int x, int y, int r){
		
		xOrigin = x;
		yOrigin = y;

		img = new BufferedImage[6];
		img[0] = ImageLoader.loadImage("resources/Images/Still1.png");
		img[1] = ImageLoader.loadImage("resources/Images/Still2.png");
		img[2] = ImageLoader.loadImage("resources/Images/Left1.png");
		img[3] = ImageLoader.loadImage("resources/Images/Left2.png");
		img[4] = ImageLoader.loadImage("resources/Images/Right1.png");
		img[5] = ImageLoader.loadImage("resources/Images/Right2.png");
		
		radius = r;
		theta = Math.PI / 2;
		xPos = (int)(xOrigin + r * Math.cos(theta));
		yPos = (int)(yOrigin + r * Math.sin(theta));

		speed = 0;
		animState = 0;
		shootDelay = 0;
		bombs = 3;
		bombDelay = 30;
		lives = 3;
		//System.out.println("xPos = " + xPos + " | yPos = " + yPos);
	}

	public int getX(){
		return xPos;
	}

	public int getY(){
		return yPos;
	}

	public BufferedImage getSprite(){
		return img[animState];
	}

	public int getSize(){
		return this.size;
	}

	public int getBombs(){
		return bombs;
	}

	public int getLives(){
		if(lives<0) lives = 0;
		return lives;
	}

	public void loseLife(){
		if( lives > 0) lives--;
	}

	public void respawn(){
		theta = Math.PI / 2;
		xPos = (int)(xOrigin + radius * Math.cos(theta));
		yPos = (int)(yOrigin + radius * Math.sin(theta));
		animState = 0;
		invTime = MAX_INV_TIME;
	}
	
	public int getInvTime(){
		return invTime;
	}

	/*
	 * Toggles between engine sprites
	 */
	private int toggleState(){
		if( animState % 2 == 0 ){
			animState += 1;
			return animState;
		} else {
			animState -= 1;
			return animState;
		}
	}

	private void shipRightOnRing(int seg, Ring ring){
		seg = Calc.shipOnRing(new Point(this.xPos,this.yPos), this.size, ring);
		if(ring.ring[seg].health==0){
			for(int i = (seg-1<0?ring.ring.length-1:seg-1); i != seg; seg =(seg-1<0?ring.ring.length-1:seg-1)){
				if(ring.ring[i].health > 0){
					xPos = ring.ring[i].p2.x;
					yPos = ring.ring[i].p2.y;
					speed = 0;
				}
			}
		}
	}

	private void shipLeftOnRing(int seg, Ring ring){
		seg = Calc.shipOnRing(new Point(this.xPos,this.yPos), this.size, ring);
		if(ring.ring[seg].health==0){
			for(int i = (seg+1)%ring.ring.length; i != seg; seg=(seg+1)%ring.ring.length){
				if(ring.ring[i].health > 0){
					xPos = ring.ring[i].p1.x;
					yPos = ring.ring[i].p1.y;
					speed = 0;
				}
			}
		}
	}

	/*
	 *	changes position of the player to simulate movement
	 *	movement code courtesy of
	 *	http://stackoverflow.com/questions/16802431/trouble-making-object-move-in-a-circle
	 */
	public Weapon updatePos( Input input, Ring ring ){

		//PRESSING LEFT
		if( input.pressed(Button.L)){
			int seg = Calc.shipOnRing(new Point(this.xPos,this.yPos), this.size, ring);
			if(speed != MAX_SPEED) speed++;
			//System.out.println("LEFT --> RING: "+ring.ring.length+" || seg: "+seg);
			if(ring.ring[(seg+1)%ring.ring.length].health!=0){
				theta = theta + Math.toRadians(speed);
				xPos = (int)(xOrigin + radius * Math.cos(theta));
				yPos = (int)(yOrigin + radius * Math.sin(theta));
				if( animState == 2 || animState == 3 ){
					toggleState();
				} else {
					animState = 2;
				}

				//Adjust if no longer on ring
				shipRightOnRing(seg, ring);
			}
		}
		//PRESSING RIGHT
		else if( input.pressed(Button.R)){
			int seg = Calc.shipOnRing(new Point(this.xPos,this.yPos), this.size, ring);
			if(speed != -MAX_SPEED) speed--;
			//System.out.println("RIGHT--> RING: "+ring.ring.length+" || seg: "+seg);
			if(ring.ring[seg].health!=0){
				theta = theta + Math.toRadians(speed);
				xPos = (int)(xOrigin + radius * Math.cos(theta));
				yPos = (int)(yOrigin + radius * Math.sin(theta));
				if( animState == 4 || animState == 5 ){
					toggleState();
				} else {
					animState = 4;
				}

				//Adjust if no longer on ring
				shipLeftOnRing(seg, ring);
			}
		}
		
		//NO MOVEMENT
		else{
			//Drifting Right
			int seg = Calc.shipOnRing(new Point(this.xPos,this.yPos), this.size, ring);
			if(speed < 0){
				speed++;
				if(ring.ring[seg].health!=0){
					theta = theta + Math.toRadians(speed);
					xPos = (int)(xOrigin + radius * Math.cos(theta));
					yPos = (int)(yOrigin + radius * Math.sin(theta));
					if( animState == 0 || animState == 1 ){
						toggleState();
					} else {
						animState = 0;
					}
					
					shipLeftOnRing(seg, ring);
				}
			}
			//Drifting Left
			else if(speed > 0){
				speed--;
				if(ring.ring[(seg+1)%ring.ring.length].health!=0){
					theta = theta + Math.toRadians(speed);
					xPos = (int)(xOrigin + radius * Math.cos(theta));
					yPos = (int)(yOrigin + radius * Math.sin(theta));
					if( animState == 0 || animState == 1 ){
						toggleState();
					} else {
						animState = 0;
					}
					
					//Adjust if no longer on ring
					shipRightOnRing(seg, ring);
				}
			}
			//No movement
			else {
				xPos = (int)(xOrigin + radius * Math.cos(theta));
				yPos = (int)(yOrigin + radius * Math.sin(theta));
				if( animState == 0 || animState == 1 ){
					toggleState();
				} else {
					animState = 0;
				}
			}
			
		}
		
		//Decrement Invulnerability
		if(invTime > 0) invTime--;
		
		//Fire a bomb
		if( input.pressed(Button.D) && bombDelay <= 0 && bombs > 0){
			shootDelay = 20;
			bombDelay = 30;
			bombs--;
			return new Bomb(xPos, yPos);
		}
		//Shoot your gun
		else if( input.pressed(Button.U) && shootDelay <= 0){
			shootDelay = 5;
			return new Bullet(xPos, yPos, theta);
		}
		if( shootDelay > 0) shootDelay--;
		if( bombDelay > 0) bombDelay--;
		return null;
	}

	public void draw(Graphics2D g){
		if(invTime == 0 || invTime % 2 == 0)
		g.drawImage(img[animState], ImageLoader.getRotation(theta, img[animState]), xPos - (img[animState].getWidth()/2), yPos - (img[animState].getHeight()/2));
		//System.out.println("Current state: " + animState);
	}

}
