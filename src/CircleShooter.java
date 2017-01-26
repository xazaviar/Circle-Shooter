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
	
	int[][] list = {{eType.ASTEROID.ordinal(),1000},
					{eType.SHIP.ordinal(),0}};
	
	Round r1 = new Round(500,0,1500,list);
	//Player player = new Player( WIDTH/2, HEIGHT - HEIGHT/4, HEIGHT/2);
	Player player = new Player( WIDTH/2, HEIGHT/2, HEIGHT/4);
	
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
		
		//Draw the Circle
		g.setColor(Color.white);
		g.drawOval((WIDTH / 2) - (HEIGHT/4), (HEIGHT/2) - (HEIGHT/4), HEIGHT/2, HEIGHT/2);
		
		//Draw the Player
		player.updatePos( input );
		g.setColor(player.getColor());
		g.fillOval(player.getX() - (10/2), player.getY() - (10/2), 10, 10);
		
		//Draw Enemies
		ArrayList<Enemy> dead = new ArrayList<>();
		for(Enemy e: enemies){
			e.setGame(this);
			if(e instanceof Asteroid){
				e.update(g);
			}else if(e instanceof Ship){
				g.setColor(Color.white);
				g.fillRect(e.x, e.y, 5, 5);
			}
			if (!e.alive) {
				dead.add(e);
			}
		}
		
		// Remove dead enemies
		enemies.removeAll(dead);
		
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
