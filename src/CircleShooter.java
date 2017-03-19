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

	//Round Variables
	int[][][] lists = {
			//Round 1
			{{eType.ASTEROID.ordinal(),10,50}},
			//Round 2
			{{eType.ASTEROID.ordinal(),15,50},
			 {eType.SHIP.ordinal(),2,100}},
			//Round 3
			{{eType.SHIP.ordinal(),20,100}},
			//Round 4
			{{eType.ASTEROID.ordinal(),20,50},
			 {eType.SHIP.ordinal(),10,100}},
			//Round 5
			{{eType.ASTEROID.ordinal(),40,50},
			 {eType.SHIP.ordinal(),20,100}},
			//Round 6
			{{eType.ASTEROID.ordinal(),60,50},
			 {eType.SHIP.ordinal(),25,100}},
			//Round 7
			{{eType.ASTEROID.ordinal(),70,50},
			 {eType.SHIP.ordinal(),30,100}},
			//Round 8
			{{eType.ASTEROID.ordinal(),80,50},
			 {eType.SHIP.ordinal(),35,100}},
			//Round 9
			{{eType.ASTEROID.ordinal(),100,50},
			 {eType.SHIP.ordinal(),50,100}}
	};
					 		//	Score 	spawnRate 	spawnCap 	refresh		list 		center 						 	 range
	Round[] rounds = {new Round(500,	30,			3, 			false,		lists[0], 	new Point((WIDTH/2), (HEIGHT/2)),c_size/4),
					  new Round(1000,	30,			4, 			true,		lists[1], 	new Point((WIDTH/2), (HEIGHT/2)),c_size/4),
					  new Round(2000,	20,			5, 			false,		lists[2], 	new Point((WIDTH/2), (HEIGHT/2)),c_size/4),
					  new Round(4000,	20,			8, 			true,		lists[3], 	new Point((WIDTH/2), (HEIGHT/2)),c_size/4),
					  new Round(8000,	15,			10, 		false,		lists[4], 	new Point((WIDTH/2), (HEIGHT/2)),c_size/4),
					  new Round(10000,	15,			10, 		true,		lists[5], 	new Point((WIDTH/2), (HEIGHT/2)),c_size/4),
					  new Round(20000,	10,			15, 		false,		lists[6], 	new Point((WIDTH/2), (HEIGHT/2)),c_size/4),
					  new Round(30000,	7,			20, 		true,		lists[7], 	new Point((WIDTH/2), (HEIGHT/2)),c_size/4),
					  new Round(50000,	3,			30, 		true,		lists[8], 	new Point((WIDTH/2), (HEIGHT/2)),c_size/4)};
	int roundIndex = 0;
	int roundCount = 1;
	double growth = 1.5;
	
	//Player Variables
	Player player = new Player( (WIDTH / 2), (HEIGHT/2), c_size/2 );
	int lives = 3;
	int score = 0;
	final int MAX_INV_TIME = 60;	//Max time the player is invincible after death (in frames)
	int inv_time = 0;
	
	
	Ring ring = new Ring(45,2,c_size,WIDTH,HEIGHT);
	
	//Game Data
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public ArrayList<Weapon> playerBullets = new ArrayList<Weapon>();
	public ArrayList<Bullet> enemyBullets = new ArrayList<Bullet>();
	boolean roundOver = false;
	boolean gameOver = false;
	final int MAX_roundOverCount = 90;
	int roundOverCount = MAX_roundOverCount;
	
	//Music
	Audio music = new Audio("resources/Audio/BGM.wav");

	/**
	 * Constructor of the game
	 */
	public CircleShooter(){
	}

	@Override
	public void tick(Graphics2D g, Input input, Sound sound) {	
		//******************************************************************
		// Game Calculations
		//******************************************************************
		if(!this.checkEndGame()) movement(input);
		collisionDetection();
		draw(g);
		music();
		gameLogic();
	}
	
	/**
	 * This method handles all player inputs
	 * @param input
	 * 				The Input object
	 */
	private void movement(Input input){
		//Move Player & get new Bullets
		Weapon nullCheck = player.updatePos( input, ring );
		if( nullCheck != null ) playerBullets.add( nullCheck );
	}
	
	/**
	 * This method handles all collision detection
	 */
	private void collisionDetection(){
		
		//Move Player Bullets
		for(int i = 0; i < playerBullets.size(); i++){
			Weapon b = playerBullets.get(i);
			b.update();
			if(!b.getAlive()){
				playerBullets.remove(i);
				i--;
			}
		}
		
		//Move Enemy Bullets
		for (int i = 0; i < enemyBullets.size(); i++) {
			Bullet b = enemyBullets.get(i);
			b.update();
			if (!b.getAlive()) {
				enemyBullets.remove(i);
				i--;
			}
		}
		//Move Enemies
		for(int ee = 0; ee < enemies.size(); ee++){
			Enemy e = enemies.get(ee);
			boolean collide = false;
			
			if(e instanceof Asteroid){
				e.update();
				
				//Check for ring collision
				int rC = Calc.ringCollide(new Point(e.x,e.y), e.getSize(), ring);
				if(rC>-1){
					collide = true;
					enemies.remove(ee);
					ee--;
					this.rounds[this.roundIndex].enemyDied();
					ring.ringSegDamage(rC);
				}

				//Check for collision with player
				if(Calc.collide(new Point(player.getX(),player.getY()), player.getSize(), new Point(e.x,e.y), e.getSize()) && !collide){
					collide = true;
					enemies.remove(ee);
					ee--;
					this.rounds[this.roundIndex].enemyDied();
					collideWithPlayer();
				}
				
			}else if(e instanceof Ship){
				((Ship) e).getTarget(player.getX(), player.getY());
				e.update();
				
				//Shoot a bullet
				Bullet shot = ((Ship) e).fire();
				if (shot != null) {
					enemyBullets.add(shot);
				}
			}
			
			//Check for bullet collision
			if(!collide)
				for(int i = 0; i < playerBullets.size(); i++){
					Weapon b = playerBullets.get(i);
					
					if(Calc.collide(new Point(b.getX(),b.getY()), b.getSize(), new Point(e.x,e.y), e.getSize())){
						enemies.addAll(e.die());
						collide = true;
						enemies.remove(ee);
						ee--;
						this.rounds[this.roundIndex].enemyDied();
						
						this.score += e.getPoints();
						if( b instanceof Bullet){ 
							playerBullets.remove(i);
							i--;
						}
						break;
					}
				}

		}
		
		// Check enemy bullet collisions
		for (int i = 0; i < enemyBullets.size(); i++) {
			Bullet b = enemyBullets.get(i);
			
			// Player collision
			if (Calc.collide(new Point(b.getX(), b.getY()), b.getSize(), new Point(player.getX(), player.getY()), player.getSize())) {
				collideWithPlayer();
				enemyBullets.remove(i);
				i--;
			}
			
			// Ring collision
			int rC = Calc.ringCollide(new Point(b.getX(), b.getY()), b.getSize(), ring);
			if (rC > -1) {
				enemyBullets.remove(i);
				i--;
				ring.ringSegDamage(rC);
			}
			
			// Bullet collision
			for (int j = 0; j < playerBullets.size(); j++) {
				Weapon w = playerBullets.get(i);
				if (Calc.collide(new Point(b.getX(), b.getY()), b.getSize(),
						new Point(w.getX(), w.getY()), w.getSize())) {
					enemyBullets.remove(i);
					if (w instanceof Bullet) playerBullets.remove(j);
				}
			}
		}
	}
	
	/**
	 * This method handles all drawing
	 * @param g
	 * 			The Graphics2D object
	 */
	private void draw(Graphics2D g){
		
			//Clear the screen (Black)  
			g.setColor(Color.black);  
			g.fillRect(0,0,WIDTH,HEIGHT);
	
			//Draw lives
			g.setColor(Color.red);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
			g.drawString("LIVES: "+lives, WIDTH-125, 25);
			g.drawString("SCORE: "+score, 25, 25);
			g.drawString("BOMBS: "+player.getBombs(), WIDTH-125, 50);
	
			//Draw the Ring
			ring.draw(g,WIDTH,HEIGHT);
	
			//Draw the Player
			if(lives>0) player.draw(g);
	
			for(Weapon b: playerBullets){
				b.draw(g);
			}
			
			//Draw the enemies
			for(Enemy e:enemies){
				e.draw(g);
			}
			
			for (Bullet b : enemyBullets) {
				b.draw(g);
			}
			
		if(!this.checkEndGame()){	
			//Draw Round Over
			if(roundOver){
				if(this.roundOverCount==this.MAX_roundOverCount){ 
					this.score += this.rounds[this.roundIndex].score();
					if(this.rounds[this.roundIndex].refreshRing()) this.ring.refreshRing();
				}
				g.setColor(Color.red);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 110));
				g.drawString("END ROUND "+(roundCount), WIDTH/2-370, HEIGHT/2);
				this.roundOverCount--;
			}
		}else{
			g.setColor(Color.red);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 124));
			g.drawString("GAME OVER", WIDTH/2-370, HEIGHT/2);
			//Audio gameover = new Audio("resources/Audio/Game Over.wav");
			//gameover.start();
		}
	}
	
	/**
	 * This method handles all music code
	 */
	private void music(){
		if(!music.getPlaying()){
			if(music.notEOF()){
				music.start();
			} else {
				music = new Audio("resources/Audio/BGM.wav");
				music.start();
			}
		}
	}
	
	/**
	 * This method checks any game logic that is tick dependent
	 */
	private void gameLogic(){
		
		//Check if the round is over
		roundOver = (rounds[roundIndex].checkEndRound() && this.lives > 0);

		//Spawn new enemies
		Enemy temp = rounds[roundIndex].spawnEnemy();
		if(temp!=null){
			enemies.add(temp);
			enemies.get(enemies.size()-1).setGame(this);
		}
		
		//Increment roundOverCount
		if(this.roundOverCount<=0){
			if(this.roundIndex+1 == this.rounds.length){ //Last possible round
				this.rounds[this.roundIndex].resetRound(growth);
			}else{
				this.roundIndex++;
			}
			this.roundCount++;
			this.roundOverCount = this.MAX_roundOverCount;
			this.roundOver = false;
		}
		
		//Decrement invincibility counter
		if(this.inv_time>0){
			this.inv_time--;
		}
	}

	/**
	 * Checks and returns if the game should be ended
	 * @return
	 * 		If the game should be ended
	 */
	public boolean checkEndGame(){
		if(lives<0) lives = 0;
		return lives==0;
	}

	/**
	 * This method respawns the player
	 */
	public void collideWithPlayer(){
		if(this.inv_time==0 && this.lives>0){
			lives--;	
			this.ring.refreshRing();
			this.player.respawn();
			this.inv_time = this.MAX_INV_TIME;
		}
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
