package Enemy;

import java.awt.Graphics2D;
import java.util.Random;

import Utility.Bullet;
import Utility.ImageLoader;
import arcadia.Game;

/**
 * Ship class that handles all Ship functions.
 * @author Josh Fosdick
 *
 */
public class Ship extends Enemy{

//	private final int turn = 5;
	private final int shotDelay = 37;
	
	private int targetX, targetY, radius, centerX, centerY;
	private int delay = shotDelay;
	private int speed = 2;
	private double thetaShip, thetaOrbit;
	
	public Ship(int x, int y, int points){
		super.name = "Ship";
		super.x = x;
		super.y = y;
		super.px = x;
		super.py = y;
		super.dx = 0;
		super.dy = 0;
		//super.images = new BufferedImage[2];
		super.images[0] = ImageLoader.loadImage("resources/Images/Resized_Resources/Resized_Ship_Enemy.png");
		//super.images[1] = ImageLoader.loadImage("resources/Images/Enemy_Ship_STILL.png");
		super.hp = 2;
		super.alive = true;
		super.size = images[0].getWidth();
		
		super.points = points;
		
		targetX = 0;
		targetY = 0;
		thetaShip = 0;
		
		// Random direction
		Random rand = new Random();
		speed *= ((rand.nextBoolean() == true) ? 1 : -1);

		centerX = Game.WIDTH/2-size/2;
		centerY = Game.HEIGHT/2-size/2;
		radius = (int) Math.sqrt((centerX - (super.x + super.images[0].getWidth()/2)) *
				(centerX - (super.x + super.images[0].getWidth()/2)) +
				(centerY - (super.y + super.images[0].getHeight()/2)) *
				(centerY - (super.y + super.images[0].getHeight()/2))) + 50;
		thetaOrbit = Math.atan2(centerY - super.y + super.images[0].getHeight()/2, centerX - super.y + super.images[0].getWidth()/2);
	}
	
	
	/**
	 * Updates the Ship's deltas
	 */
	@Override
	public void update() {
		super.update();
		
		// Get next deltas
		thetaOrbit += Math.toRadians(speed);
		double toX = centerX + radius * Math.cos(thetaOrbit);
		double toY = centerY + radius * Math.sin(thetaOrbit);
		
		
		dx = (int) toX - super.x;
		dy = (int) toY - super.y;
		//System.out.println(dx);
	}
	
	
	/**
	 * Spawns an Enemy Bullet
	 * @return		A Bullet object
	 */
	public Bullet fire() {
		Bullet shot = null;
		if (delay > 0) {
			delay--;
		}
		else {
			delay = shotDelay;
//			int myX = (int) (super.x + super.images[0].getWidth()/2 + (50 * Math.sin(thetaShip)));
//			int myY = (int) (super.y + super.images[0].getHeight()/2 + (50 * Math.cos(thetaShip)));
			shot = new Bullet(/*myX/*/super.x + super.images[0].getWidth()/2/**/,
					/*myY/*/super.y + super.images[0].getHeight()/2/**/, thetaShip, false);
			//System.out.println(Math.toDegrees(thetaShip);
		}
		//System.out.println(Math.toDegrees(thetaS));
		return shot;
	}
	
	
	/**
	 * Updates the position of the Player for the Ship to fire at
	 * @param tx		Target x position
	 * @param ty		Target y position
	 */
	public void getTarget(int tx, int ty) {
		targetX = tx;
		targetY = ty;
		double angle = Math.atan2(super.y - targetY, super.x - targetX);
		thetaShip = angle;
		//System.out.println(Math.toDegrees(thetaS));
		//if (theta > angle) { theta = theta + Math.toRadians(-turn); }
		//else if (theta < angle) { theta = theta + Math.toRadians(turn); }
	}
	
	
	/**
	 * Draws image to Graphics object
	 * 
	 * @param g		Graphics object to draw on
	 */
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(images[0], ImageLoader.getRotation(thetaShip, images[0]), x, y);
	}
}
