package Utility;
import java.awt.Color;

public class Player {
	private int xPos;
	private int yPos;

	private Color color;

	private int circleDiameter;
	private int temp; //this is to simulate moving around the circle

	public Player(int x, int y, int d){
		xPos = x;
		yPos = y;
		color = Color.green;
		circleDiameter = d;
		temp = 1;
		//System.out.println("xPos = " + xPos + " | yPos = " + yPos);
	}

	public int getX(){
		return xPos;
	}

	public int getY(){
		return yPos;
	}

	public Color getColor(){
		return color;
	}

	//changes position of the player to simulate movement
	public void updatePos(){
		temp++;
		if(temp > 4) temp = 1;
		switch (temp) {
		case 1: xPos = xPos + circleDiameter /2;
				yPos = yPos + circleDiameter /2;
				break;
		case 2: xPos = xPos + circleDiameter /2;
				yPos = yPos - circleDiameter /2;
				break;
		case 3: xPos = xPos - circleDiameter /2;
				yPos = yPos - circleDiameter /2;
				break;
		case 4: xPos = xPos - circleDiameter /2;
				yPos = yPos + circleDiameter /2;
				break;
		}
	}

}
