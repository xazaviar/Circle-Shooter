package Utility;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import arcadia.Input;
import arcadia.Button;

/**
 * Player class. Handles all Player input, movement
 * weapons, respawning, and invincibility.
 * 
 * @author Mathonwy Dean-Hall
 *
 */
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
	private final int min_shootDelay = 5;
	private int currentDelay = 10;
	private int bombs;
	private int bombDelay;

	private int lives;
	
	final int MAX_INV_TIME = 120;	//Max time the player is invincible after death (in frames)
	int invTime = 0;

	/**
	 * Player Constructor
	 * 
	 * @param x
	 * 		int X-Coordinate of the origin
	 * @param y
	 * 		int Y-Coordinate of the origin
	 * @param r
	 * 		int radius of the circle
	 */
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
	
	/**
	 * Resets the Player position and stats for new game
	 */
	public void resetPlayer(){
		theta = Math.PI / 2;
		xPos = (int)(xOrigin + radius * Math.cos(theta));
		yPos = (int)(yOrigin + radius * Math.sin(theta));
		speed = 0;
		animState = 0;
		shootDelay = 0;
		bombs = 3;
		bombDelay = 30;
		lives = 3;
		currentDelay = 10;
	}

	/**
	 * Get X-Coordinate of the Player
	 * 
	 * @return
	 * 		int X-Coordinate of Player
	 */
	public int getX(){
		return xPos;
	}

	/**
	 * Get Y-Coordinate of the Player
	 * 
	 * @return
	 * 		int Y-Coordinate of Player
	 */
	public int getY(){
		return yPos;
	}

	/**
	 * Get the current animation state of the Player
	 * 
	 * @return
	 * 		BufferedImage of Player state
	 */
	public BufferedImage getSprite(){
		return img[animState];
	}

	/**
	 * Get size of the Player for collision
	 * 
	 * @return
	 * 		int size of Player
	 */
	public int getSize(){
		return this.size;
	}

	/**
	 * Get number of Bombs Player can use
	 * 
	 * @return
	 * 		int Bombs available to Player
	 */
	public int getBombs(){
		return bombs;
	}

	/**
	 * Get number of lives Player has left
	 * 
	 * @return
	 * 		int number of lives
	 */
	public int getLives(){
		if(lives<0) lives = 0;
		return lives;
	}

	/**
	 * Decrement lives, but not beyond zero
	 */
	public void loseLife(){
		if( lives > 0) lives--;
		speedUpFiring();
	}

	/**
	 * Respawn the Player after dying
	 */
	public void respawn(){
		theta = Math.PI / 2;
		xPos = (int)(xOrigin + radius * Math.cos(theta));
		yPos = (int)(yOrigin + radius * Math.sin(theta));
		animState = 0;
		invTime = MAX_INV_TIME;
	}
	
	/**
	 * Check how much time is left on Player invulnerability from dying
	 * 
	 * @return
	 * 		int time of invulnerability left
	 */
	public int getInvTime(){
		return invTime;
	}

	/**
	 * Increase the fire speed of the Player
	 */
	public void speedUpFiring(){
		if(currentDelay > min_shootDelay)
			currentDelay--;
	}
	
	/*
	 * Toggles between engine sprites
	 */
	/**
	 * Toggles between Player engine animation sprites for left
	 * and right movement.
	 * 
	 * @return
	 * 		int of Player's animation state
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

	/**
	 * Currently Unused.
	 * Calculates if the Player can move right on the Ring
	 * 
	 * @param seg
	 * 		RingSegment Player is on
	 * @param ring
	 * 		Ring object the Player is on
	 */
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

	/**
	 * Currently Unused.
	 * Calculates if the Player can move left on the Ring
	 * 
	 * @param seg
	 * 		RingSegment Player is on
	 * @param ring
	 * 		Ring object the Player is on
	 */
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

	/**
	 * Updates the Player's position based on input and also fires weapons.
	 * 
	 * Movement partially based on code found at:
	 * http://stackoverflow.com/questions/16802431/trouble-making-object-move-in-a-circle
	 * 
	 * @param input
	 * 		Input object sent by Arcadia
	 * @param ring
	 * 		Ring object the Player is on
	 * @return
	 * 		Weapon object that Player may fire
	 */
	public Weapon updatePos( Input input, Ring ring ){

		//PRESSING LEFT
		if( input.pressed(Button.L)){
			if(speed < 0) speed = 0;
			if(speed < MAX_SPEED) speed++;
			double tTest =  theta + Math.toRadians(speed);
			int xTest = (int)(xOrigin + radius * Math.cos(tTest));
			int yTest = (int)(yOrigin + radius * Math.sin(tTest));
			int seg = Calc.shipOnRing(new Point(xTest,yTest), this.size, ring);
			//System.out.println("LEFT --> RING: "+ring.ring.length+" || seg: "+seg);
			if(ring.ring[(seg)%ring.ring.length].health!=0){
				theta = theta + Math.toRadians(speed);
				xPos = (int)(xOrigin + radius * Math.cos(theta));
				yPos = (int)(yOrigin + radius * Math.sin(theta));
				if( animState == 2 || animState == 3 ){
					toggleState();
				} else {
					animState = 2;
				}

				//Adjust if no longer on ring
				//shipRightOnRing(seg, ring);
			}
		}
		//PRESSING RIGHT
		else if( input.pressed(Button.R)){
			if(speed > 0) speed = 0;
			if(speed > -MAX_SPEED) speed--;
			double tTest =  theta + Math.toRadians(speed);
			int xTest = (int)(xOrigin + radius * Math.cos(tTest));
			int yTest = (int)(yOrigin + radius * Math.sin(tTest));
			int seg = Calc.shipOnRing(new Point(xTest,yTest), this.size, ring);
			//System.out.println("RIGHT--> RING: "+ring.ring.length+" || seg: "+seg);
			if(ring.ring[(seg>-1?seg:ring.ring.length-1)].health!=0){
				theta = theta + Math.toRadians(speed);
				xPos = (int)(xOrigin + radius * Math.cos(theta));
				yPos = (int)(yOrigin + radius * Math.sin(theta));
				if( animState == 4 || animState == 5 ){
					toggleState();
				} else {
					animState = 4;
				}

				//Adjust if no longer on ring
				//shipLeftOnRing(seg, ring);
			}
		}
		
		//NO MOVEMENT
		else{
//			//Drifting Right
//			if(speed < 0){
//				speed++;
//				double tTest =  theta + Math.toRadians(speed);
//				int xTest = (int)(xOrigin + radius * Math.cos(tTest));
//				int yTest = (int)(yOrigin + radius * Math.sin(tTest));
//				int seg = Calc.shipOnRing(new Point(this.xPos,this.yPos), this.size, ring);
//				if(ring.ring[seg].health!=0){
//					theta = theta + Math.toRadians(speed);
//					xPos = (int)(xOrigin + radius * Math.cos(theta));
//					yPos = (int)(yOrigin + radius * Math.sin(theta));
//					if( animState == 0 || animState == 1 ){
//						toggleState();
//					} else {
//						animState = 0;
//					}
//					
//					//shipLeftOnRing(seg, ring);
//				}
//			}
//			//Drifting Left
//			else if(speed > 0){
//				speed--;
//				double tTest =  theta + Math.toRadians(speed);
//				int xTest = (int)(xOrigin + radius * Math.cos(tTest));
//				int yTest = (int)(yOrigin + radius * Math.sin(tTest));
//				int seg = Calc.shipOnRing(new Point(this.xPos,this.yPos), this.size, ring);
//				if(ring.ring[(seg+1)%ring.ring.length].health!=0){
//					theta = theta + Math.toRadians(speed);
//					xPos = (int)(xOrigin + radius * Math.cos(theta));
//					yPos = (int)(yOrigin + radius * Math.sin(theta));
//					if( animState == 0 || animState == 1 ){
//						toggleState();
//					} else {
//						animState = 0;
//					}
//					
//					//Adjust if no longer on ring
//					//shipRightOnRing(seg, ring);
//				}
//			}
//			//No movement
//			else {
				xPos = (int)(xOrigin + radius * Math.cos(theta));
				yPos = (int)(yOrigin + radius * Math.sin(theta));
				if( animState == 0 || animState == 1 ){
					toggleState();
				} else {
					animState = 0;
				}
//			}
			
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
			shootDelay = this.currentDelay;
			return new Bullet(xPos, yPos, theta, true);
		}
		if( shootDelay > 0) shootDelay--;
		if( bombDelay > 0) bombDelay--;
		return null;
	}

	/**
	 * Draw the Player with correct orientation and animation state
	 * 
	 * @param g
	 * 		Graphics2D object we are drawing to
	 */
	public void draw(Graphics2D g){
		if(invTime == 0 || invTime % 2 == 0)
		g.drawImage(img[animState], ImageLoader.getRotation(theta, img[animState]), xPos - (img[animState].getWidth()/2), yPos - (img[animState].getHeight()/2));
		//System.out.println("Current state: " + animState);
	}

}
