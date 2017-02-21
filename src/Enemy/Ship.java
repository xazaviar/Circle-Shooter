package Enemy;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import Utility.Bullet;
import Utility.ImageLoader;

public class Ship extends Enemy{

	private final int turn = 5;
	private final int speed = 2;
	private final int shotDelay = 30;
	
	private int targetX, targetY, radius, centerX, centerY;
	private int delay = 0;
	private double thetaS, thetaO;
	
	public Ship(int x, int y, int points){
		super.name = "Ship";
		super.x = x;
		super.y = y;
		super.dx = 0;
		super.dy = 0;
		super.images = new BufferedImage[2];
		super.images[0] = ImageLoader.loadImage("resources/Images/Enemy_Ship_STILL.png");
		super.images[1] = ImageLoader.loadImage("resources/Images/Enemy_Ship_STILL.png");
		super.hp = 2;
		super.alive = true;
		
		super.points = points;
		
		targetX = 0;
		targetY = 0;
		thetaS = 0;
		

		centerX = super.game.WIDTH/2;
		centerY = super.game.HEIGHT/2;
		radius = (int) Math.sqrt((centerX - (super.x + super.images[0].getWidth()/2)) *
				(centerX - (super.x + super.images[0].getWidth()/2)) +
				(centerY - (super.y + super.images[0].getHeight()/2)) *
				(centerY - (super.y + super.images[0].getHeight()/2))) + 50;
		thetaO = Math.atan2(centerY - super.y + super.images[0].getHeight()/2, centerX - super.y + super.images[0].getWidth()/2);
	}
	
	@Override
	public void update() {
		super.update();
		
		// Get next deltas
		thetaO += Math.toRadians(speed);
		double toX = centerX + radius * Math.cos(thetaO);
		double toY = centerY + radius * Math.sin(thetaO);
		
		
		dx = (int) toX - super.x;
		dy = (int) toY - super.y;
		//System.out.println(dx);
	}
	
	
	public Bullet fire() {
		Bullet shot = null;
		if (delay > 0) {
			delay--;
		}
		else {
			delay = shotDelay;
			shot = new Bullet(super.x + super.images[0].getWidth()/2,
					super.y + super.images[0].getHeight()/2, thetaS + Math.toRadians(90));
		}
		//System.out.println(Math.toDegrees(thetaS));
		return shot;
	}
	
	public void getTarget(int tx, int ty) {
		targetX = tx;
		targetY = ty;
		double angle = Math.atan2(-super.x + targetX, super.y - targetY);
		thetaS = angle;
		//System.out.println(Math.toDegrees(thetaS));
		//if (theta > angle) { theta = theta + Math.toRadians(-turn); }
		//else if (theta < angle) { theta = theta + Math.toRadians(turn); }
	}
	
	
	/*
	 * Creates a rotational matrix which rotates the
	 * player sprite. Code found at
	 * http://stackoverflow.com/questions/8639567/java-rotating-images
	 */
	public AffineTransformOp getRotation(){
		double rotationRequired = thetaS - Math.toRadians(0);
		double locationX = images[1].getWidth() / 2;
		double locationY = images[1].getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op;
	}
	
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(images[1], getRotation(), x, y);
	}
}
