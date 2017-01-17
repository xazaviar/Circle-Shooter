//Java Imports
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

//Arcadia Imports
import arcadia.Arcadia;
import arcadia.Game;
import arcadia.Input;
import arcadia.Sound;

//Circle-shooter Imports
import Enemy.*;


public class CircleShooter extends Game{

	//Position of the ball
	int x = 0;
	int y = 0;
	
	@Override
	public void tick(Graphics2D g, Input input, Sound sound) {
		// TODO Auto-generated method stub
		//Clear the screen (Black)  
		g.setColor(Color.black);  
		g.fillRect(0,0,WIDTH,HEIGHT);
	    //Draw a ball  
		g.setColor(Color.red);  
		g.fillOval(x, y, 10, 10);
	    //Update the ball's position  
		x += 2;  
		y += 1;
	}
	
	public static void main(String[] args){
		Arcadia.display(new Arcadia(new CircleShooter()));
	}

	@Override
	public Image cover() {
		// TODO Auto-generated method stub
		return null;
	}

}
