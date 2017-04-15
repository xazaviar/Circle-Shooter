package Enemy;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import arcadia.Game;

public class Enemy {

	public String name;
	public BufferedImage images[] = new BufferedImage[4];
	public int x, y, width, height;
	protected double dx, dy, px, py;					// Delta x and y
	protected int hp, damage;
	protected static Game game = null;
	public boolean alive;
	
	protected int size;					//The size of the enemy
	protected int points;				//The points for the enemy
	
	public void setGame(Game g) {
		if (game == null)
			game = g;
	}
	/**
	 * Handles all actions for enemy during a tick
	 * 
	 * @param g		Graphics object to draw on
	 */
	public void update() {
		//draw(g);
		move();
	}
	/**
	 * Draws image to Graphics object
	 * 
	 * @param g		Graphics object to draw on
	 */
	public void draw(Graphics2D g) {
		g.drawImage(images[hp%2], x, y, null);
	}
	
	
	/**
	 * Updates the x,y of the Enemy
	 */
	protected void move() {
		px += dx;
		py += dy;
		x = (int) px;
		y = (int) py;
	}
	
	
	/**
	 * Deals damage to the Enemy
	 * @param d		Damage dealt
	 * @return		A list of new Enemies
	 */
	public ArrayList<Enemy> damage(int d) {
		if ((hp -= d) <= 0) {
			return die();
		}
		return new ArrayList<Enemy>();
	}
	
	
	/**
	 * Sets the Enemy as dead and to be removed
	 * @return		A list of new Enemies
	 */
	public ArrayList<Enemy> die() {
		alive = false;
		
		return new ArrayList<Enemy>();
	}
	

	/**
	 * The diameter of the Enemy
	 * @return		Diameter of the Enemy
	 */
	public int getSize(){
		return this.size;
	}
	
	
	/**
	 * Number of points for killing enemy
	 * @return		Point value
	 */
	public int getPoints(){
		return this.points;
	}
}
