package Enemy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.ArrayList;

import Utility.ImageLoader;
import arcadia.Game;

public class Asteroid extends Enemy{

	private int type;
	private double theta;
	private double speeds [] = {1,1.25,1.5,2};
	private int hps[] = {2,2,2,1};
	private double spin;
	private boolean breakup = true;
	
	public Asteroid(int x, int y, int points){
		super.name = "Asteroid";
		super.images[0] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock_1_Largest.png");
		super.images[1] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock_3.png");
		super.images[2] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock4.png");
		super.images[3] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock_2_Smallest.png");
		super.x = x;
		super.y = y;
		super.px = x;
		super.py = y;
		super.alive = true;
		super.hp = 2;
		
		super.points = points;

		theta = 0;
		type = 0;
		
		// Random direction and speed
		Random rand = new Random();
		do {
			super.dx = rand.nextDouble() * ((rand.nextBoolean() == true) ? 1 : -1);
			super.dy = rand.nextDouble() * ((rand.nextBoolean() == true) ? 1 : -1);
		} while (dx < 0.5 && dy < 0.5);
		
		spin = 0.115 * ((rand.nextBoolean() == true) ? 1 : -1);
		
		super.width = images[type].getWidth();
		super.height = images[type].getHeight();
		super.size = width;
	}
	
	// New constructor
	public Asteroid(int x, int y, int points, int t, boolean b){
		super.name = "Asteroid";
		super.images[0] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock_1_Largest.png");
		super.images[1] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock_3.png");
		super.images[2] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock4.png");
		super.images[3] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock_2_Smallest.png");
		

		theta = 0;
		type = t;
		breakup = b;
		super.width = images[type].getWidth();
		super.height = images[type].getHeight();
		super.size = width;
		super.hp = hps[type];
		super.x = x - size/2;
		super.y = y - size/2;
		super.px = super.x;
		super.py = super.y;
		super.alive = true;
		
		super.points = points;
		
		// Random direction and speed
		Random rand = new Random();
		do {
			super.dx = rand.nextDouble() * speeds[type] * ((rand.nextBoolean() == true) ? 1 : -1);
			super.dy = rand.nextDouble() * speeds[type] * ((rand.nextBoolean() == true) ? 1 : -1);
		} while (dx < 0.5 && dy < 0.5);
		
		spin = 0.115 * ((rand.nextBoolean() == true) ? 1 : -1);
	}
	
	/**
	 * Updates the x,y of the Asteroid, rotates the image
	 * and sets to remove if it leaves the screen. 
	 */
	@Override
	public void move() {
		super.move();
		theta += spin;
		
		if (this.x > Game.WIDTH) {
			alive = false;
		}
		if (this.width + this.x < 0) {
			alive = false;
		}
		if (this.y > Game.HEIGHT) {
			alive = false;
		}
		if (this.height + this.y < 0) {
			alive = false;
		}
	}
	
	/**
	 * Draws image to Graphics object
	 * 
	 * @param g		Graphics object to draw on
	 */
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(images[type], ImageLoader.getRotation(theta, images[type]), x, y);
		//g.setColor(Color.green);
		//g.drawOval(x, y, size, size);
		//g.setColor(Color.yellow);
		//g.drawOval(x-size/2, y-size/2, size, size);
	}
	
	
	/**
	 * Sets the Asteroid as dead and to be removed
	 * @return		A list of new Enemies
	 */
	@Override
	public ArrayList<Enemy> die() {
		super.alive = false;
		ArrayList<Enemy> spawn = new ArrayList<Enemy>();
		
		if (type < 3 && breakup) {
			spawn.add(new Asteroid(x, y, points, type+1, true));
			spawn.add(new Asteroid(x, y, points, type+1, true));
		}
		
		return spawn;
	}
}
