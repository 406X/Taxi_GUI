
package taxi_gui;

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
	// [ 3 ][ 0 ] Time passenger called
	// [ 3 ][ 1 ] Time passenger picked up
	
	public PassengerList(int maxPassenger){
		this.maxPassenger = maxPassenger;
		passenger = new int[maxPassenger][4][2];
	}
	
	public void addPassenger(int s_x , int s_y , int d_x , int d_y ){
		System.out.println(numPassenger);
		if(numPassenger<maxPassenger && empty(passenger[numPassenger])){
			numPassenger++;
			passenger[numPassenger-1][0][0] = s_x;
			passenger[numPassenger-1][0][1] = s_y;
			passenger[numPassenger-1][1][0] = d_x;
			passenger[numPassenger-1][1][1] = d_y;
			passenger[numPassenger-1][2][0] = s_x;
			passenger[numPassenger-1][2][1] = s_y;
			passenger[numPassenger-1][3][0] = time;
		}
	}
	
	public boolean empty(int[][] passenger){
		if( passenger[0][0] == 0 && passenger[0][1] == 0 && passenger[1][1] == 0 && passenger[1][0] == 0 && passenger[2][1] == 0 && passenger[2][0] == 0)
			return true;
		else
			return false;
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
		int[][] closestPassenger =  passenger[closestIndex].clone();
		
		//remove from closest passenger from the list
		passenger[closestIndex] = new int[4][2];
		for(int count = closestIndex ; count < maxPassenger - 1 ; count++){
			passenger[count] = passenger[count+1];
			passenger[count+1] = new int[4][2];
		}
		numPassenger--;
		return closestPassenger.clone();
	}
	
	public int[][][] getPassengers(){
		int[][][] temp = new int[numPassenger][4][2];
		for(int count = 0 ; count < numPassenger ; count++)
			temp[count] = passenger[count].clone();
	
		return temp.clone();
	}
	
	public int getNumPassenger(){
		return numPassenger;
	}
	
	public void setTime(int time){
		this.time = time;
	}
	
}
