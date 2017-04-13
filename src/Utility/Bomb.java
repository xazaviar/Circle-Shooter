package Utility;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bomb implements Weapon{

	private int xPos;
	private int yPos;
	
	private int size;
	private final int maxSize = 500 * 2; //size of ring
	
	private boolean alive;
	
	public Bomb(int x, int y){
		xPos = x;
		yPos = y;
		size = 0;
		alive = true;
	}
	
	public void update(){
		if( size < maxSize ) size += 100;
		else alive = false;
	}
	
	public void draw(Graphics2D g){
		g.setColor(Color.red);
		g.drawOval(xPos - (size / 2) , yPos - (size / 2), size, size);
	}

	public int getSize() {
		return size;
	}

	public boolean getAlive() {
		return alive;
	}
	
	public int getX(){
		return xPos - (size / 2);
	}
	
	public int getY(){
		return yPos - (size / 2);
	}
}
