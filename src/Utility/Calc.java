package Utility;

import java.util.Random;

public abstract class Calc {

	public static int randInt(int min, int max) {
	    Random r = new Random();
	    return r.nextInt((max - min) + 1) + min;
	}
}
