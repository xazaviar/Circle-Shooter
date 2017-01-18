package Utility;

import Enemy.*;

public class Round {
	
	//Received Variables
	final int score; 		//The points received for completing the round
	final int spawnRate;	//The rate at which enemies will be spawned in (0 is every tick)
	final int spawnCap;		//The maximum number of enemies allow on screen
	int[] eList;			//The list of enemies to spawn and in what order
	
	//Predetermined Variables
	final int xMin = 200;	//The minimum x coordinate that enemies can spawn
	final int xMax = 400;	//The maximum x coordinate that enemies can spawn
	final int yMin = 200;	//The minimum y coordinate that enemies can spawn
	final int yMax = 400;	//The maximum y coordinate that enemies can spawn
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
	 * 		    (0 means every tick)
	 * @param spawnCap
	 * 			The maximum amount of enemies on the screen
	 * @param list
	 * 			The list of the different types of enemies 
	 *          allowed to spawn and how many to spawn
	 */
	public Round(int score, int spawnRate, int spawnCap, int[][] list){
		//Initialize variables
		this.score = score;
		this.spawnRate = spawnRate;
		this.spawnCap = spawnCap;
		
		//Create the correct sizing for the enemy list
		int sum = 0;
		for(int i = 0; i < list.length; i++) sum += list[i][1];
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
	 * Checks and returns if the round should be ended. It does
	 * so by checking if there are no enemies spawned and if it
	 * has no more enemies to spawn
	 * @return
	 * 		If the round should be ended
	 */
	public boolean checkEndRound(){
		return this.spawned == 0 && this.sIndex == this.eList.length;
	}
	
	/**
	 * Call this whenever an enemy dies
	 */
	public void enemyDied(){
		this.spawned--;
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
			if(this.eList[this.sIndex] == eType.ASTEROID.ordinal())
				ret = new Asteroid(x,y);
			else if(this.eList[this.sIndex] == eType.SHIP.ordinal())
				ret = new Ship(x,y);
			this.sIndex++;
			this.spawned++;
			this.spawnRateCount = 0;
			
		//Increment Counter
		}else if(this.spawned < this.spawnCap && this.sIndex < this.eList.length)
			this.spawnRateCount++;
		
		return ret;
	}
}
