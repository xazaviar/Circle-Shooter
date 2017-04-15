//Java Imports
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Audio.Music;
//Arcadia Imports
import arcadia.Arcadia;
import arcadia.Button;
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
			{{eType.ASTEROID_XL.ordinal(),10,25}},
			//Round 2
			{{eType.ASTEROID_L.ordinal(),12,25},
			 {eType.SHIP.ordinal(),4,75}},
			//Round 3
			{{eType.SHIP.ordinal(),20,100}},
			//Round 4
			{{eType.ASTEROID_L.ordinal(),10,50},
			 {eType.ASTEROID_M_B.ordinal(),10,50},
			 {eType.SHIP.ordinal(),10,100}},
			//Round 5
			{{eType.ASTEROID_M_B.ordinal(),10,50},
		     {eType.ASTEROID_S.ordinal(),30,50},
			 {eType.SHIP.ordinal(),20,100}},
			//Round 6
			{{eType.ASTEROID_L.ordinal(),40,50},
			 {eType.ASTEROID_XL_B.ordinal(),20,50}},
			//Round 7
			{{eType.ASTEROID_M_B.ordinal(),70,50},
			 {eType.ASTEROID_S.ordinal(),30,50},
			 {eType.SHIP.ordinal(),30,100}},
			//Round 8
			{{eType.ASTEROID_XL_B.ordinal(),80,50},
			 {eType.ASTEROID_L.ordinal(),30,50},
			 {eType.SHIP.ordinal(),35,100}},
			//Round 9
			{{eType.ASTEROID_L_B.ordinal(),100,50},
			 {eType.ASTEROID_XL_B.ordinal(),30,50},
			 {eType.SHIP.ordinal(),60,100}}
	};
					 		//	BGM		Score 	spawnRate 	spawnCap 	refresh		list 		center 						 	 range
	Round[] rounds = {new Round("bgm",	500,	30,			3, 			false,		lists[0], 	new Point((WIDTH/2), (HEIGHT/2)),3*c_size/5),
					  new Round("bgm",	1000,	30,			4, 			true,		lists[1], 	new Point((WIDTH/2), (HEIGHT/2)),3*c_size/5),
					  new Round("bgm2",	2000,	30,			4, 			false,		lists[2], 	new Point((WIDTH/2), (HEIGHT/2)),3*c_size/5),
					  new Round("bgm2",	4000,	20,			8, 			true,		lists[3], 	new Point((WIDTH/2), (HEIGHT/2)),3*c_size/5),
					  new Round("bgm",	8000,	15,			10, 		false,		lists[4], 	new Point((WIDTH/2), (HEIGHT/2)),3*c_size/5),
					  new Round("bgm",	10000,	15,			10, 		true,		lists[5], 	new Point((WIDTH/2), (HEIGHT/2)),3*c_size/5),
					  new Round("bgm2",	20000,	10,			15, 		false,		lists[6], 	new Point((WIDTH/2), (HEIGHT/2)),3*c_size/5),
					  new Round("bgm2",	30000,	7,			20, 		true,		lists[7], 	new Point((WIDTH/2), (HEIGHT/2)),3*c_size/5),
					  new Round("swi",	50000,	3,			30, 		true,		lists[8], 	new Point((WIDTH/2), (HEIGHT/2)),3*c_size/5)};
	int roundIndex = 0;
	int roundCount = 1;
	double growth = 1.5;
	
	//Player Variables
	Player player = new Player( (WIDTH / 2), (HEIGHT/2), c_size/2 );
	int score = 0;
	
	
	Ring ring = new Ring(45,2,c_size,WIDTH,HEIGHT);
	
	//Game Data
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public ArrayList<Weapon> playerBullets = new ArrayList<Weapon>();
	public ArrayList<Bullet> enemyBullets = new ArrayList<Bullet>();
	public ArrayList<Particle> particles = new ArrayList<Particle>();
	boolean roundOver = false;
	final int MAX_roundOverCount = 90;
	int roundOverCount = MAX_roundOverCount;
	
	final int MAX_gameOverCount = 30;
	int gameOverCount = MAX_gameOverCount;
	
	//Music
	//Audio music = new Audio("resources/Audio/BGM.wav");
	int bgmPlay = 0;
	boolean start = true;
	boolean gStart = true;
	boolean gEnd = false, canPlayEndG = true;
	boolean rStart = false;
	boolean rEnd = false, canPlayEndR = true;
	boolean pBul = false, pBom = false, pDeath = false;
	boolean sDeath = false, sBul = false;
	boolean imp = false;
	
	Music bgm = new Music("resources/Audio/BGM.wav", true);
	Music bgm2 = new Music("resources/Audio/BGM2.wav", true);
	Music gameStartA = new Music("resources/Audio/Game Start.wav", false);
	Music gameOverA = new Music("resources/Audio/Game Over.wav", false);
	Music roundStartA = new Music("resources/Audio/round start.wav", false);
	Music roundCompleteA = new Music("resources/Audio/round complete.wav", false);
	Music playerBullet = new Music("resources/Audio/Laser9.wav", false);
	Music playerBomb = new Music("resources/Audio/Bomb.wav", false);
	Music playerDeath = new Music("resources/Audio/Ship Explosion.wav", false);
	Music impact = new Music("resources/Audio/Impact.wav", false);
	

	Music[] enemyShot = {new Music("resources/Audio/Laser1.wav", false),
						 new Music("resources/Audio/Laser2.wav", false),
						 new Music("resources/Audio/Laser3.wav", false),
						 new Music("resources/Audio/Laser4.wav", false),
						 new Music("resources/Audio/Laser5.wav", false),
						 new Music("resources/Audio/Laser6.wav", false),
						 new Music("resources/Audio/Laser7.wav", false),
						 new Music("resources/Audio/Laser8.wav", false),
						 new Music("resources/Audio/Laser9.wav", false)};
	
	//Images
	BufferedImage lifeFull = ImageLoader.loadImage("resources/Images/Resized_Resources/Life_Full_Icon.png");
	BufferedImage lifeEmpty = ImageLoader.loadImage("resources/Images/Resized_Resources/Life_Empty_Icon.png");
	BufferedImage bombAmmo = ImageLoader.loadImage("resources/Images/Bomb_Icon.png");
	BufferedImage logo = ImageLoader.loadImage("resources/Images/LogoBG.png");
	
	/**
	 * Constructor of the game
	 */
	public CircleShooter(){
	}

	/**
	 * Manages all actions done in a tick
	 */
	@Override
	public void tick(Graphics2D g, Input input, Sound sound) {	
		//******************************************************************
		// Game Calculations
		//******************************************************************
		if(!this.checkEndGame()) movement(input);
		collisionDetection();
		draw(g);
		music();
		gameLogic(input);
	}
	
	/**
	 * This method handles all player inputs
	 * @param input
	 * 				The Input object
	 */
	private void movement(Input input){
		//Move Player & get new Bullets
		Weapon nullCheck = player.updatePos( input, ring );
		if( nullCheck != null ){ 
			playerBullets.add( nullCheck );
			if(nullCheck instanceof Bomb)
				this.pBom = true;
			else
				this.pBul = true;
		}
		
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

				//Check for collision with player
				if(Calc.collide(new Point(player.getX(),player.getY()), player.getSize(), new Point(e.x,e.y), e.getSize()) && !collide){
					collide = true;
					imp = true;
					enemies.remove(ee);
					ee--;
					this.rounds[this.roundIndex].enemyDied();
					collideWithPlayer();
					
					particles.add(new ExplosionLarge(e.x+e.getSize()/2, e.y+e.getSize()/2));
				}
				
				//Check for ring collision
				int rC = Calc.ringCollide(new Point(e.x,e.y), e.getSize(), ring);
				if(rC>-1 && !collide){
					collide = true;
					imp = true;
					enemies.remove(ee);
					ee--;
					this.rounds[this.roundIndex].enemyDied();
					ring.ringSegDamage(rC);
					
					particles.add(new ExplosionLarge(e.x+e.getSize()/2, e.y+e.getSize()/2));
				}
				
			}else if(e instanceof Ship){
				((Ship) e).getTarget(player.getX(), player.getY());
				e.update();
				
				//Shoot a bullet
				Bullet shot = ((Ship) e).fire();
				if (shot != null) {
					enemyBullets.add(shot);
					this.sBul = true;
				}
			}
			
			//Check for bullet collision
			if(!collide)
				for(int i = 0; i < playerBullets.size(); i++){
					Weapon b = playerBullets.get(i);
					
					if(Calc.collide(new Point(b.getX(),b.getY()), b.getSize(), new Point(e.x,e.y), e.getSize())){
						collide = true;
						imp = true;
						
						// Asteroids split
						ArrayList<Enemy> add = e.damage(1);
						enemies.addAll(add);
						if (e instanceof Asteroid) {
							this.rounds[this.roundIndex].enemyAdded(add.size());
						}else{
							//this.sDeath = true;
						}
						
						if( b instanceof Bullet){ 
							playerBullets.remove(i);
							if (!e.alive) {
								this.score += e.getPoints();
							}
							i--;
						}
						break;
					}
				}

			// Remove offscreen enemies
			if (/*!collide && */!e.alive) {
				enemies.remove(ee);
				ee--;
				this.rounds[this.roundIndex].enemyDied();
				particles.add(new ExplosionLarge((e.x+e.getSize()/2), e.y+e.getSize()/2));
				this.sDeath = true;
			}
		}
		
		// Check enemy bullet collisions
		for (int i = 0; i < enemyBullets.size(); i++) {
			Bullet b = enemyBullets.get(i);
			boolean collide = false;
			
			// Player collision
			if (Calc.collide(new Point(b.getX(), b.getY()), b.getSize(),
					new Point(player.getX(), player.getY()), player.getSize()) && !collide) {
				collideWithPlayer();
				enemyBullets.remove(i);
				i--;
				collide = true;
				
				particles.add(new ExplosionLarge(player.getX()+player.getSize()/2, player.getY()+player.getSize()/2));
			}
			
			// Ring collision
			int rC = Calc.ringCollide(new Point(b.getX(), b.getY()), b.getSize(), ring);
			if (rC > -1 && !collide) {
				enemyBullets.remove(i);
				i--;
				ring.ringSegDamage(rC);
				collide = true;
				imp = true;
				
				particles.add(new ExplosionLarge(b.getX()+b.getSize()/2, b.getY()+b.getSize()/2));
			}

			// Bullet collision
			for (int j = 0; j < playerBullets.size() && !collide; j++) {
				Weapon w = playerBullets.get(j);
				if (Calc.collide(new Point(b.getX(), b.getY()), b.getSize(),
						new Point(w.getX(), w.getY()), w.getSize())) {
					enemyBullets.remove(i);
					i--;
					collide = true;
					
					particles.add(new ExplosionSmall(b.getX()+b.getSize()/2, b.getY()+b.getSize()/2));
					
					if (w instanceof Bullet) {
						playerBullets.remove(j);
						j--;
					}
					break;
				}
			}
		}
		
		//Remove dead particles
		for( int i = 0; i < particles.size(); i++ ){
			if (!particles.get(i).isAlive()){
				particles.remove(i);
				i--;
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
			
			/*
			//Dangerous Logo
			int randX = (int) (Math.random() * WIDTH);
			int randY = (int) (Math.random() * HEIGHT);
			g.drawImage(logo, null, randX - logo.getWidth()/2, randY - logo.getHeight()/2);
			 */
			
			//Game Logo
			g.drawImage(logo, null, WIDTH/2 - logo.getWidth()/2, HEIGHT/2 - logo.getHeight()/2);
	
			//Draw lives, score, and bombs
			g.setColor(Color.red);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
			drawLives(g);
			g.drawString("SCORE: "+score, 25, 25);
			drawBombs(g);
	
			//Draw the Ring
			ring.draw(g,WIDTH,HEIGHT);
	
			//Draw the Player
			if(player.getLives()>0) player.draw(g);
	
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
			
			for (Particle p : particles) {
				p.draw(g);
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
			
			this.gameOverCount--;
			if(this.gameOverCount<=0){
				this.gameOverCount = 0;
				g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
				g.drawString("Press anything to play again.", WIDTH/4, HEIGHT/2+130);
			}
			
			//gameover.start();
		}
	}
	
	/**
	 * This method handles all music code
	 */
	private void music(){
		if(this.start){
			this.start = false;
			try {
				this.bgm.play();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(this.rStart){
			this.rStart = false;
			try {
				this.roundStartA.play2();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//Change BGM
			if(this.rounds[this.roundIndex].bgm().equals("swi") || !this.rounds[this.roundIndex].bgm().equals(this.rounds[this.roundIndex-1].bgm())){
				if(bgmPlay == 0){
					bgmPlay = 1;
					bgm.stop();
					try {
						this.bgm2.play();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					bgmPlay = 0;
					bgm2.stop();
					try {
						this.bgm.play();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(this.rEnd && this.canPlayEndR){
			this.rEnd = false;
			this.canPlayEndR = false;
			try {
				this.roundCompleteA.play2();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(this.gStart){
			this.gStart = false;
			try {
				this.gameStartA.play2();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(this.imp){
			this.imp = false;
			try {
				this.impact.play2();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(this.gEnd && this.canPlayEndG){
			this.gEnd = false;
			this.canPlayEndG = false;
			try {
				if(this.bgmPlay==0) this.bgm.stop();
				if(this.bgmPlay==1) this.bgm2.stop();
				this.gameOverA.play2();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(this.pBul){
			this.pBul = false;
			try {
				this.playerBullet.play2();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		

		if(this.sBul){
			this.sBul = false;
			try {
				int r = (int)((Math.random()*1000)%this.enemyShot.length);
				this.enemyShot[r].play2();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if(this.pBom){
			this.pBom = false;
			try {
				this.playerBomb.play2();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if(this.pDeath || this.sDeath){
			this.pDeath = false;
			this.sDeath = false;
			try {
				this.playerDeath.play2();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method checks any game logic that is tick dependent
	 */
	private void gameLogic(Input input){
		
		//Check if the round is over
		roundOver = (rounds[roundIndex].checkEndRound() && player.getLives() > 0);

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
			this.rStart = true;
			this.rEnd = false;
			this.canPlayEndR = true;
			this.player.speedUpFiring();
		}else if(this.roundOverCount < this.MAX_roundOverCount) this.rEnd = true;
		
		//Check for game Restart
		if(this.gameOverCount<=0 && (input.pressed(Button.R) || input.pressed(Button.L) ||input.pressed(Button.A) ||input.pressed(Button.B) ||input.pressed(Button.C) || input.pressed(Button.D))){
			this.restartGame();
		}
		
	}

	/**
	 * Checks and returns if the game should be ended
	 * @return
	 * 		If the game should be ended
	 */
	public boolean checkEndGame(){
		if(player.getLives() == 0 && this.canPlayEndG) gEnd = true;
		return player.getLives()==0;
	}

	/**
	 * This method respawns the player
	 */
	public void collideWithPlayer(){
		if(player.getInvTime()==0 && player.getLives()>0){
			player.loseLife();	
			playerBullets.add(new Bomb(player.getX(), player.getY()));
			this.ring.refreshRing();
			this.player.respawn();
			this.pDeath = true;
			this.pBom = true;
		}
	}
	
	public void drawLives(Graphics2D g){
		if( player.getLives() > 2 ){
			g.drawImage(lifeFull, null, WIDTH - 60, 15);
		} else {
			g.drawImage(lifeEmpty, null, WIDTH - 60, 15);
		}
		if( player.getLives() > 1 ){
			g.drawImage(lifeFull, null, WIDTH - 110, 15);
		} else {
			g.drawImage(lifeEmpty, null, WIDTH - 110, 15);
		}
		if( player.getLives() > 0 ){
			g.drawImage(lifeFull, null, WIDTH - 160, 15);
		} else {
			g.drawImage(lifeEmpty, null, WIDTH - 160, 15);
		}
	}
	
	public void drawBombs(Graphics2D g){
		if( player.getBombs() > 2 ){
			g.drawImage(bombAmmo, null, WIDTH - 60, 60);
		}
		if( player.getBombs() > 1 ){
			g.drawImage(bombAmmo, null, WIDTH - 110, 60);
		}
		if( player.getBombs() > 0 ){
			g.drawImage(bombAmmo, null, WIDTH - 160, 60);
		}
	}
	
	public void restartGame(){
		Round rE = new Round("swi",50000,3,30,true,lists[8],new Point((WIDTH/2),(HEIGHT/2)),c_size/4);
		
		//Reset Game Vars
		enemies.clear();
		enemies = new ArrayList<Enemy>();
		playerBullets = new ArrayList<Weapon>();
		enemyBullets = new ArrayList<Bullet>();
		particles = new ArrayList<Particle>();
		roundOver = false;
		score = 0;
		roundIndex = 0;
		roundCount = 1;
		gameOverCount = MAX_gameOverCount;
		
		
		//Reset rounds
		for(int r = 0; r < this.rounds.length-1; r++){
			this.rounds[r].resetGameRound();
		}
		this.rounds[this.rounds.length-1] = rE;
		
		//Reset Player and ring
		this.player.resetPlayer();
		this.ring.refreshRing();
		
		//Begin
		bgmPlay = 0;
		start = true;
		gStart = true;
		gEnd = false; canPlayEndG = true;
		rStart = false;
		rEnd = false; canPlayEndR = true;
		pBul = false; pBom = false; pDeath = false;
		sDeath = false; sBul = false;
		
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
