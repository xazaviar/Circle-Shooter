package Enemy;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import Utility.ImageLoader;

public class Asteroid extends Enemy{

	public Asteroid(int x, int y){
		super.name = "Asteroid";
		super.images[0] = ImageLoader.loadImage("resources/Enemy/asteroidTemp.jpg");
		super.images[1] = ImageLoader.loadImage("resources/Enemy/asteroidDamagedTemp.jpg");
		super.x = x;
		super.y = y;
		super.width = images[0].getWidth();
		super.height = images[0].getHeight();
		super.alive = true;
		super.hp = 2;

		// Random direction and speed
		Random rand = new Random();
		do {
			super.dx = rand.nextInt(3) * ((rand.nextBoolean() == true) ? 1 : -1);
			super.dy = rand.nextInt(3) * ((rand.nextBoolean() == true) ? 1 : -1);
		} while (dx == 0 && dy == 0);
	}
	
	@Override
	public void move() {
		super.move();
		
		if (super.width + super.x > super.game.WIDTH) {
			dx *= -1;
			damage(2);
		}
		if (super.x < 0) {
			dx *= -1;
			damage(2);
		}
		if (super.height + super.y > super.game.HEIGHT) {
			dy *= -1;
			damage(2);
		}
		if (super.y < 0) {
			dy *= -1;
			damage(2);
		}
	}
}
