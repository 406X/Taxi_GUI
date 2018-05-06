
package taxi_gui;

import java.util.Random;

public class PassengerList {	
	
	private int maxPassenger = 10;
	private int numPassenger = 0;
	private int[][][] passenger = new int[maxPassenger][4][2];
	private int time = 0;
	// passenger[ passengerID ]
	// [ 0 ] - Source Coordinate
	// [ 1 ] - Destination Coordinate
	// [ 2 ] - Current Coordinate
	// [ 0/1/2 ][ 0 ] - X coordinate
	// [ 0/1/2 ][ 1 ] - Y coordinate
	// [ 3 ] Time
	// [ 3][ 0 ] Time passenger called
	// [ 3][ 1 ] Time passenger picked up
	
	public PassengerList(int maxPassenger){
		this.maxPassenger = maxPassenger;
		passenger = new int[maxPassenger][4][2];
	}
	
	public void addPassenger(int s_x , int s_y , int d_x , int d_y ){
		if(numPassenger < maxPassenger){
			passenger[numPassenger][0][0] = s_x;
			passenger[numPassenger][0][1] = s_y;
			passenger[numPassenger][1][0] = d_x;
			passenger[numPassenger][1][1] = d_y;
			passenger[numPassenger][2][0] = s_x;
			passenger[numPassenger][2][1] = s_y;
			passenger[numPassenger][3][0] = time;
			numPassenger++;
		}
	}
	
	
	public int[][] getClosestPassenger(int x , int y){
		double[] distance = new double[numPassenger];
		int closestIndex = 0;
		
		for(int count = 0 ; count < numPassenger ; count++){
			
			
			if( passenger[count][0][0] == 0 && passenger[count][0][1] == 0
					&& passenger[count][1][0] == 0 && passenger[count][1][1] == 0
					&& passenger[count][2][0] == 0 && passenger[count][2][1] == 0)
			{
				break;
			}
			
			distance[count] = Math.pow ( (x - passenger[count][0][0] ),2) + Math.pow(y - passenger[count][0][1] , 2);
			distance[count] = Math.sqrt(distance[count]);
			
			if( distance[count] < distance[closestIndex])
				closestIndex = count;
		}
		
		//store closest passenger in temp array
		int[][] closestPassenger =  passenger[closestIndex];
		
		//remove from closest passenger from the list
		for(int count = closestIndex ; count < maxPassenger - 1 ; count++){
			passenger[count] = passenger[count+1];
		}
		numPassenger--;
		
		return closestPassenger;
	}
	
	public int[][][] getPassengers(){
		int[][][] temp = new int[numPassenger][4][2];
		for(int count = 0 ; count < numPassenger ; count++)
			temp[count] = passenger[count].clone();
	
		return temp;
	}
	
	public int getNumPassenger(){
		return numPassenger;
	}
	
	public void setTime(int time){
		this.time = time;
	}
	
}
