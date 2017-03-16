package Enemy;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Utility.ImageLoader;

public class Asteroid extends Enemy{

	private int type;
	private double theta;
	
	public Asteroid(int x, int y, int points){
		super.name = "Asteroid";
		super.images[0] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock_1_Largest.png");
		super.images[1] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock_3.png");
		super.images[2] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock4.png");
		super.images[3] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock_2_Smallest.png");
		super.x = x;
		super.y = y;
		super.alive = true;
		super.hp = 2;
		
		super.points = points;

		// Random direction and speed
		Random rand = new Random();
		do {
			super.dx = rand.nextInt(3) * ((rand.nextBoolean() == true) ? 1 : -1);
			super.dy = rand.nextInt(3) * ((rand.nextBoolean() == true) ? 1 : -1);
		} while (dx == 0 && dy == 0);
		
		theta = 0;
		type = 0;

		super.width = images[type].getWidth();
		super.height = images[type].getHeight();
		super.size = width;
	}
	
	// New constructor
	public Asteroid(int x, int y, int points, int t) {
		super.name = "Asteroid";
		super.images[0] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock_1_Largest.png");
		super.images[1] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock_3.png");
		super.images[2] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock4.png");
		super.images[3] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock_2_Smallest.png");
		super.x = x;
		super.y = y;
		super.alive = true;
		super.hp = 2;
		
		super.points = points;

		// Random direction and speed
		Random rand = new Random();
		do {
			super.dx = rand.nextInt(3) * ((rand.nextBoolean() == true) ? 1 : -1);
			super.dy = rand.nextInt(3) * ((rand.nextBoolean() == true) ? 1 : -1);
		} while (dx == 0 && dy == 0);
		
		theta = 0;
		type = t;

		super.width = images[type].getWidth();
		super.height = images[type].getHeight();
		super.size = width;
	}
	
	@Override
	public void move() {
		super.move();
		theta += 0.115;
		
		if (super.width + super.x > super.game.WIDTH) {
			this.alive = false;
		}
		if (super.x < 0) {
			this.alive = false;
		}
		if (super.height + super.y > super.game.HEIGHT) {
			this.alive = false;
		}
		if (super.y < 0) {
			this.alive = false;
		}
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
