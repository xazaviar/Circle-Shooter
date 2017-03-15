package Utility;

import java.awt.Graphics2D;

public interface Weapon {
	
	public void update();
	
	public void draw(Graphics2D g);
	
	public int getSize();
	
	public boolean getAlive();
	
	public int getX();
	public int getY();
	
}
