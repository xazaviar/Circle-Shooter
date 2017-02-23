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
	int score = 0;

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
	
	
	Player player = new Player( (WIDTH / 2), (HEIGHT/2), c_size/2 );
	Ring ring = new Ring(45,2,c_size,WIDTH,HEIGHT);
	
	//Game Data
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public ArrayList<Weapon> playerBullets = new ArrayList<Weapon>();
	public ArrayList<Bullet> enemyBullets = new ArrayList<Bullet>();
	boolean roundOver = false;
	boolean gameOver = false;
	final int MAX_roundOverCount = 90;
	int roundOverCount = MAX_roundOverCount;

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

			//Move Player & get new Bullets
			Weapon nullCheck = player.updatePos( input, ring );
			if( nullCheck != null ) playerBullets.add( nullCheck );

			ArrayList<Weapon> spent = new ArrayList<>();
			for(Weapon b: playerBullets){
				b.update();
				if(!b.getAlive()){
					spent.add(b);
				}
			}
			
			for (Bullet b : enemyBullets) {
				b.update();
				if (!b.getAlive()) {
					spent.add(b);
				}
			}

			//Draw and move Enemies
			ArrayList<Enemy> dead = new ArrayList<>();
			for(Enemy e: enemies){
				//e.setGame(this);
				if(e instanceof Asteroid){
					e.update();
				}else if(e instanceof Ship){
					((Ship) e).getTarget(player.getX(), player.getY());
					e.update();
					Bullet shot = ((Ship) e).fire();
					if (shot != null) {
						enemyBullets.add(shot);
					}
				}
//				if (!e.alive) {
//					dead.add(e);
//					this.rounds[this.roundIndex].enemyDied();
//				}

				//Check for collision with player
				if(Calc.collide(new Point(player.getX(),player.getY()), player.getSize(), new Point(e.x,e.y), e.getSize())){
					lives--;
					dead.add(e);
					this.rounds[this.roundIndex].enemyDied();
					//Player respawn [will need to be discussed]
				}
				
				//Check for ring collision
				int rC = Calc.ringCollide(new Point(e.x,e.y), e.getSize(), ring);
				if(rC>-1){
					dead.add(e);
					this.rounds[this.roundIndex].enemyDied();
					ring.ringSegDamage(rC);
				}
				
				for( Weapon b: playerBullets ){
					if(Calc.collide(new Point(b.getX(),b.getY()), b.getSize(), new Point(e.x,e.y), e.getSize())){
						dead.add(e);
						this.score += e.getPoints();
						this.rounds[this.roundIndex].enemyDied();
						if( b instanceof Bullet) spent.add(b);
					}
				}

			}
			
			// Check enemy bullet collisions (ONLY FOR PLAYER)
			for (Bullet b : enemyBullets) {
				if (Calc.collide(new Point(b.getX(), b.getY()), b.getSize(), new Point(player.getX(), player.getY()), player.getSize())) {
					lives--;
					spent.add(b);
				}
			}

			// Remove dead bullets
			playerBullets.removeAll(spent);
			enemyBullets.removeAll(spent);
			// Remove dead enemies
			enemies.removeAll(dead);

			//Check if the round is over
			roundOver = rounds[roundIndex].checkEndRound();

			//Spawn new enemies
			Enemy temp = rounds[roundIndex].spawnEnemy();
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
			g.drawString("SCORE: "+score, 25, 25);
			g.drawString("BOMBS: "+player.getBombs(), WIDTH-125, 50);

			//Draw the Ring
			ring.draw(g,WIDTH,HEIGHT);

			//Draw the Player
			player.draw(g);

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
