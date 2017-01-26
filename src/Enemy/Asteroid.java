package Enemy;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Asteroid extends Enemy{

	public Asteroid(int x, int y){
		try {
			super.name = "Asteroid";
			super.images[0] = ImageIO.read(new File("resources/Enemy/asteroidTemp.jpg"));
			super.images[1] = ImageIO.read(new File("resources/Enemy/asteroidDamagedTemp.jpg"));
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
		catch (IOException e) {
			System.out.println("FAIL");
		}
	}
	
	@Override
	public void move() {
		super.move();
		
		if (super.width + super.x > super.game.WIDTH) {
			dx *= -1;
		}
		if (super.x < 0) {
			dx *= -1;
		}
		if (super.height + super.y > super.game.HEIGHT) {
			dy *= -1;
		}
		if (super.y < 0) {
			dy *= -1;
			damage(1);
		}
	}
}
