package Utility;
import java.awt.Color;

import arcadia.Input;
import arcadia.Button;

public class Player {
	private int xPos;
	private int yPos;
	private int xOrigin;
	private int yOrigin;

	private double theta;
	
	private Color color;

	private int radius;
	
	private int speed;
	
	private final int size = 20; 

	//x and y are the center of the circle player travels in
	//r is the radius of the circle
	public Player(int x, int y, int r){
		xPos = 0;
		yPos = 0;
		xOrigin = x;
		yOrigin = y;
		theta = Math.PI / 2;
		color = Color.green;
		radius = r;
		speed = 4;
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
	
	public void setColor( Color c ){
		color = c;
	}
	
	public int getSize(){
		return this.size;
	}

	/*
		changes position of the player to simulate movement
		movement code courtesy of
		http://stackoverflow.com/questions/16802431/trouble-making-object-move-in-a-circle
	*/
	public void updatePos( Input input ){
		
		//PRESSING LEFT
		if( input.pressed(Button.L)){
			theta = theta + Math.toRadians(speed);
			xPos = (int)(xOrigin + radius * Math.cos(theta));
			yPos = (int)(yOrigin + radius * Math.sin(theta));
		}
		//PRESSING RIGHT
		else if( input.pressed(Button.R)){
			theta = theta + Math.toRadians(-speed);
			xPos = (int)(xOrigin + radius * Math.cos(theta));
			yPos = (int)(yOrigin + radius * Math.sin(theta));
		}
		//NO MOVEMENT
		else {
			xPos = (int)(xOrigin + radius * Math.cos(theta));
			yPos = (int)(yOrigin + radius * Math.sin(theta));
		}
	}

}
