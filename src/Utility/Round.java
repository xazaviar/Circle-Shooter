package Utility;

import java.awt.Point;

import Enemy.*;

public class Round {
	
	//Received Variables
	int score; 				//The points received for completing the round
	final int spawnRate;	//The rate at which enemies will be spawned in (0 is every tick)
	final int spawnCap;		//The maximum number of enemies allow on screen
	int[] eList;			//The list of enemies to spawn and in what order
	int[][] pointList;		//The Point values associated with each type of enemy
	boolean refresh;		//Determines if a ring refresh can happen
	
	//Predetermined Variables
	final int xMin;			//The minimum x coordinate that enemies can spawn
	final int xMax;			//The maximum x coordinate that enemies can spawn
	final int yMin;			//The minimum y coordinate that enemies can spawn
	final int yMax;			//The maximum y coordinate that enemies can spawn
	int spawned = 0;		//The current count of enemies spawned
	int sIndex = 0;			//The current enemy spawning index
	int spawnRateCount = 0;	//The counter to check when an enemy can be spawned
	
	/**
	 * Initialize a round with the required variables
	 * 
	 * @param score
	 * 			The points awarded for completing the round
	 * @param spawnRate
	 * 			The rate at which enemies are allowed to spawn 
	 * 		    (0 means every tick, 30 means every second)
	 * @param spawnCap
	 * 			The maximum amount of enemies on the screen
	 * @param refresh
	 * 			Denotes if the ring refreshes this round
	 * @param list
	 * 			The list of the different types of enemies 
	 *          allowed to spawn and how many to spawn
	 * @param center
	 * 			The center of the circle
	 * @param radius
	 * 			The radius of the circle
	 */
	public Round(int score, int spawnRate, int spawnCap, boolean refresh, int[][] list, Point center, int radius){
		//Initialize variables
		this.score = score;
		this.spawnRate = spawnRate;
		this.spawnCap = spawnCap;
		this.refresh = refresh;
		
		//Create the correct sizing for the enemy list
		int sum = 0;
		this.pointList = new int[list.length][3];
		for(int i = 0; i < list.length; i++){ 
			sum += list[i][1];
			this.pointList[i][0] = list[i][0];
			this.pointList[i][1] = list[i][2];
			this.pointList[i][2] = list[i][1];
		}
		this.eList = new int[sum];
		
		//Generate the list of enemy spawn order by choosing 
		//randomly from the selected list of options
		for(int i = 0; i < this.eList.length; i++){
			
			//Randomly select
			int r = ((int)(Math.random()*100))%list.length;
			while(list[r][1] == 0)
				r = ((int)(Math.random()*100))%list.length;
			
			this.eList[i] = list[r][0];
			list[r][1]--;
		}
		
		
		//Set the min and max spawning distance
		xMin = center.x - radius/6;
		xMax = center.x + radius/6;
		yMin = center.y - radius/6;
		yMax = center.y + radius/6;
		
	}
	
	/**
	 * Returns the score for completing the round
	 * @return
	 * 		The score for completing the round
	 */
	public int score(){
		return this.score;
	}
	
	/**
	 * Returns if the ring should refresh
	 * @return
	 * 		If the ring refreshes
	 */
	public boolean refreshRing(){
		return this.refresh;
	}
	
	/**
	 * Checks and returns if the round should be ended. It does
	 * so by checking if there are no enemies spawned and if it
	 * has no more enemies to spawn
	 * @return
	 * 		If the round should be ended
	 */
	public boolean checkEndRound(){
		return this.spawned <= 0 && this.sIndex == this.eList.length;
	}
	
	/**
	 * Call this whenever an enemy dies
	 */
	public void enemyDied(){
		this.spawned--;
	}
	
	/**
	 * Call this whenever an asteroid splits
	 */
	public void enemyAdded(int a) {
		this.spawned += a;
	}
	
	/**
	 * This method attempts to spawn an enemy. It will return
	 * the spawned enemy when all are true:
	 * 		1. The spawnRate tick was hit
	 * 		2. The number of enemies currently spawned is less than the cap
	 * 		3. There are enemies in the queue to spawn
	 * @return
	 * 		The new enemy that has been spawned or null if none
	 */
	public Enemy spawnEnemy(){
		Enemy ret = null;
		
		//Spawn the enemy
		if(this.spawnRateCount == this.spawnRate && this.spawned < this.spawnCap && this.sIndex < this.eList.length){
			int x = Calc.randInt(this.xMin, this.xMax);
			int y = Calc.randInt(this.yMin, this.yMax);
			int points = 0;
			
			//Set the points for the spawned enemy
			for(int i = 0; i < this.pointList.length;i++){
				if(this.pointList[i][0] == this.eList[this.sIndex]){
					points = this.pointList[i][1];
					break;
				}
			}
			
			if(this.eList[this.sIndex] == eType.ASTEROID.ordinal())
				ret = new Asteroid(x,y,points);
			else if(this.eList[this.sIndex] == eType.SHIP.ordinal())
				ret = new Ship(x,y,points);
			this.sIndex++;
			this.spawned++;
			this.spawnRateCount = 0;
		//Increment Counter
		}else if(this.spawned < this.spawnCap && this.sIndex < this.eList.length)
			this.spawnRateCount++;
		
		return ret;
	}
	
	/**
	 * This method resets the enemy spawning and spawns more 
	 * enemies based on the given growth
	 * @param growth
	 * 			The number of enemies to grow (as a percentage)
	 * 			<br> <1 = shrink
	 * 			<br>  1 = no change
	 * 			<br> >1 = growth		
	 */
	public void resetRound(double growth){
		//Initialize variables
		double tScore = score*growth;
		this.score = (int)(tScore);
		this.refresh = !this.refresh;
		
		//Create the correct sizing for the enemy list
		int sum = 0;
		int[] list = new int[this.pointList.length];
		for(int i = 0; i < this.pointList.length; i++){
			double temp = this.pointList[i][2]*growth;
			list[i] = (int)temp; 
			sum += (int)temp;
		}
		this.eList = new int[sum];
		
		//Generate the list of enemy spawn order by choosing 
		//randomly from the selected list of options
		for(int i = 0; i < this.eList.length; i++){
			
			//Randomly select
			int r = ((int)(Math.random()*100))%this.pointList.length;
			while(list[r] == 0)
				r = ((int)(Math.random()*100))%this.pointList.length;
			
			this.eList[i] = this.pointList[r][0];
			list[r]--;
		}
		
		this.sIndex = 0;
		//System.out.println("NEW SUM: "+sum+" || NEW POINTS: "+score);
	}
}
