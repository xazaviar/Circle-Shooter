//Java Imports
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
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
	
	final int c_size = 500;	//The size of the circle
	int lives = 3;
	
	int[][] list = {{eType.ASTEROID.ordinal(),1000},
					{eType.SHIP.ordinal(),0}};
	
	Round r1 = new Round(500,15,1500,list, new Point((WIDTH/2), (HEIGHT/2)),c_size/2);
	Player player = new Player( (WIDTH / 2), (HEIGHT/2), c_size/2);
	
	//Game Data
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	boolean roundOver = false;
	boolean gameOver = false;
	
	/**
	 * Constructor of the game
	 */
	public CircleShooter(){
	}
	
	@Override
	public void tick(Graphics2D g, Input input, Sound sound) {	
		if(!this.checkEndGame()){
		//******************************************************************
		// Game Calculations
		//******************************************************************

		//Move Player
		player.updatePos( input );
		
		//Draw and move Enemies
		ArrayList<Enemy> dead = new ArrayList<>();
		for(Enemy e: enemies){
			//e.setGame(this);
			if(e instanceof Asteroid){
				e.update(g);
			}else if(e instanceof Ship){
				g.setColor(Color.white);
				g.fillRect(e.x, e.y, 5, 5);
			}
			if (!e.alive) {
				dead.add(e);
			}
			
			//Check for collision
			if(Calc.collide(new Point(player.getX(),player.getY()), player.getSize(), new Point(e.x,e.y), e.getSize())){
				lives--;
				dead.add(e);
				//Player respawn [will need to be discussed]
			}
			
		}
		
		// Remove dead enemies
		enemies.removeAll(dead);
		
		//Check if the round is over
		roundOver = r1.checkEndRound();
		
		//Spawn new enemies
		Enemy temp = r1.spawnEnemy();
		if(temp!=null){
			enemies.add(temp);
			enemies.get(enemies.size()-1).setGame(this);
		}
		
		
		//******************************************************************
		// Drawing
		//******************************************************************
		//Clear the screen (Black)  
		g.setColor(Color.black);  
		g.fillRect(0,0,WIDTH,HEIGHT);
		
		//Draw lives
		g.setColor(Color.red);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
		g.drawString("LIVES: "+lives, WIDTH-125, 25);
		
		//Draw the Circle
		g.setColor(Color.white);
		//g.drawOval(WIDTH-550, 25, 500, 500);
		g.drawOval((WIDTH / 2) - c_size/2, (HEIGHT/2) - c_size/2, c_size, c_size);
		
		//Draw the Player
		g.setColor(player.getColor());
		g.fillOval(player.getX() - (10/2), player.getY() - (10/2), player.getSize(), player.getSize());
		
		//Draw the enemies
		for(Enemy e:enemies){
			e.draw(g);
		}
		
		
		//******************************************************************
		// Sound and Audio
		//******************************************************************
		}else{
			g.setColor(Color.red);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 124));
			g.drawString("GAME OVER", WIDTH/2-370, HEIGHT/2);
		}
	}
	
	/**
	 * Checks and returns if the game should be ended
	 * @return
	 * 		If the game should be ended
	 */
	public boolean checkEndGame(){
		return lives==0;
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
