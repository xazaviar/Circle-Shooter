//Java Imports
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

//Arcadia Imports
import arcadia.Arcadia;
import arcadia.Game;
import arcadia.Input;
import arcadia.Sound;

//Circle-shooter Imports
import Enemy.*;
import Utility.*;

public class CircleShooter extends Game{

	//Position of the ball
	int x = 0;
	int y = 0;
	
	int[][] list = {{eType.ASTEROID.ordinal(),1000},
					{eType.SHIP.ordinal(),700}};
	
	Round r1 = new Round(500,0,1500,list);
	
	//Game Data
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	boolean roundOver = false;
	boolean gameOver = false;
	
	@Override
	public void tick(Graphics2D g, Input input, Sound sound) {		
		//******************************************************************
		// Game Calculations
		//******************************************************************
		
		//Check if the round is over
		roundOver = r1.checkEndRound();
		
		//Check if any enemies need to be removed
		
		//Spawn enemies
		Enemy temp = r1.spawnEnemy();
		if(temp!=null)
			enemies.add(temp);
		
		
		//******************************************************************
		// Drawing
		//******************************************************************
		
		//Clear the screen (Black)  
		g.setColor(Color.black);  
		g.fillRect(0,0,WIDTH,HEIGHT);
	    //Draw a ball  
		g.setColor(Color.red);  
		g.fillOval(x, y, 10, 10);
	    //Update the ball's position  
		x += 2;  
		y += 1;
		
		//Draw Enemies
		for(Enemy e: enemies){
			if(e instanceof Asteroid){
				g.setColor(Color.blue);
				g.fillOval(e.x, e.y, 8, 8);
			}else if(e instanceof Ship){
				g.setColor(Color.white);
				g.fillRect(e.x, e.y, 5, 5);
			}
		}
		
		//******************************************************************
		// Sound and Audio
		//******************************************************************
	}
	
	/**
	 * Checks and returns if the game should be ended
	 * @return
	 * 		If the game should be ended
	 */
	public boolean checkEndGame(){
		return false;
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
