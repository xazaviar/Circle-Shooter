package Enemy;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import arcadia.Game;

public class Enemy {

	public String name;
	public BufferedImage images[] = new BufferedImage[2];
	public int x, y, width, height;
	protected int dx, dy;					// Delta x and y
	protected int hp, damage;
	protected static Game game = null;
	public boolean alive;
	
	private int size = 14;				//The size of the enemy
	
	public void setGame(Game g) {
		if (game == null)
			game = g;
	}
	/**
	 * Handles all actions for enemy during a tick
	 * 
	 * @param g		Graphics object to draw on
	 */
	public void update(Graphics g) {
		//draw(g);
		move();
	}
	/**
	 * Draws image to Graphics object
	 * 
	 * @param g		Graphics object to draw on
	 */
	public void draw(Graphics g) {
		g.drawImage(images[hp%2], x, y, null);
	}
	
	
	/**
	 * Updates the x,y of the Enemy
	 */
	protected void move() {
		x += dx;
		y += dy;
	}
	
	
	protected void damage(int d) {
		if ((hp -= d) == 0) {
			die();
		}
		
	}
	
	
	protected void die() {
		alive = false;
	}
	

	public int getSize(){
		return this.size;
	}
}
