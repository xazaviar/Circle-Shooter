package Enemy;

import java.awt.Graphics2D;
import java.util.Random;
import java.util.ArrayList;

import Utility.ImageLoader;

public class Asteroid extends Enemy{

	private int type;
	private double theta;
	private double speeds [] = {1,1.25,1.5,2};
	
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
		
		
		super.width = images[type].getWidth();
		super.height = images[type].getHeight();
		super.size = width;
	}
	
	// New constructor
	public Asteroid(int x, int y, int points, int t){
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
		type = t;
		
		// Random direction and speed
		Random rand = new Random();
		do {
			super.dx = rand.nextDouble() * speeds[type] * ((rand.nextBoolean() == true) ? 1 : -1);
			super.dy = rand.nextDouble() * speeds[type] * ((rand.nextBoolean() == true) ? 1 : -1);
		} while (dx < 0.5 && dy < 0.5);
		
		super.width = images[type].getWidth();
		super.height = images[type].getHeight();
		super.size = width;
	}
	
	@Override
	public void move() {
		super.move();
		theta += 0.115;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(images[type], ImageLoader.getRotation(theta, images[type]), x, y);
	}
	
	@Override
	public ArrayList<Enemy> die() {
		super.alive = false;
		ArrayList<Enemy> spawn = new ArrayList<Enemy>();
		
		if (type < 3) {
			spawn.add(new Asteroid(x, y, points, type+1));
			spawn.add(new Asteroid(x, y, points, type+1));
		}
		
		return spawn;
	}
}
