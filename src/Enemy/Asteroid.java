package Enemy;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import Utility.ImageLoader;

public class Asteroid extends Enemy{

	private double theta;
	
	public Asteroid(int x, int y, int points){
		super.name = "Asteroid";
		super.images[0] = ImageLoader.loadImage("resources/Images/Resized_Resources/Rock_3.png");
		super.images[1] = ImageLoader.loadImage("resources/Images/Enemy/asteroidDamagedTemp.jpg");
		super.x = x;
		super.y = y;
		super.width = images[0].getWidth();
		super.height = images[0].getHeight();
		super.alive = true;
		super.hp = 2;
		super.size = width;
		
		super.points = points;

		// Random direction and speed
		Random rand = new Random();
		do {
			super.dx = rand.nextInt(3) * ((rand.nextBoolean() == true) ? 1 : -1);
			super.dy = rand.nextInt(3) * ((rand.nextBoolean() == true) ? 1 : -1);
		} while (dx == 0 && dy == 0);
		
		theta = 0;
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
	
	/*
	 * Creates a rotational matrix which rotates the
	 * player sprite. Code found at
	 * http://stackoverflow.com/questions/8639567/java-rotating-images
	 */
	public AffineTransformOp getRotation(){
		double rotationRequired = theta - Math.toRadians(0);
		double locationX = images[0].getWidth() / 2;
		double locationY = images[0].getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(images[0], getRotation(), x, y);
	}
}
