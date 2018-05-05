
package taxi_gui;


public class TaxiObject {
	
	private int maxPassenger = 1;
	private int numPassenger = 0;
	
	private int[][][] passenger = new int[maxPassenger][3][2];
	// passenger[ passengerID ]
	// [0] - Source Coordinate
	// [1] - Destination Coordinate
	// [2] - Current Coordinate
	// [ 0/1/2 ][ 0 ] - X coordinate
	// [ 0/1/2 ][ 1 ] - Y coordinate

	
	private int[] passengerStatus = new int[maxPassenger];
	//PassengerStatus[ passengerID ]
	// 0 = Not in Taxi yet
	// 1 = In Taxi
	
	//Taxi current coordinate
	private int x = 0;
	private int y = 0;
	
	private static PassengerList passengerList;
	
	public TaxiObject(int maxPassenger, int x , int y, PassengerList pList){
		passengerList = pList;
		this.maxPassenger = maxPassenger;
		passenger = new int[maxPassenger][3][2];
		passengerStatus = new int[maxPassenger];
		this.x = x;
		this.y = y;
	}
	
	public void addPassenger(int[][] passenger){
		this.passenger[ numPassenger ] = passenger;
		this.passengerStatus[ numPassenger ] = 0;
		numPassenger++;
	}
	
	public void dropPassenger(int index){
		for(int count = index ; count < numPassenger - 1 ; count++){
			passenger[count] = passenger[count+1];
			passengerStatus[count] = passengerStatus[count+1];
		}
		numPassenger--;
	}
	
	public void move(){	
		if(numPassenger == 0){
			if( passengerList.getNumPassenger() > 0){
				addPassenger( passengerList.getClosestPassenger(x, y) );
				move();
			}
		}
		else{
			if(passengerStatus[0] == 0){	
				if( x < passenger[0][2][0] )
					x += 1;
				else if( x > passenger[0][2][0] )
					x -= 1;
				else if( y < passenger[0][2][1] )
					y += 1;
				else if( y > passenger[0][2][1] )
					y -= 1;
				else if( x == passenger[0][2][0] && y ==   passenger[0][2][1]){
					passengerStatus[0] = 1;
					move();
				}
			}
			else{
				if( x < passenger[0][1][0] ){
					x += 1;
					passenger[0][2][0]+=1;
				}
				else if( x > passenger[0][1][0] ){
					x -= 1;
					passenger[0][2][0]-=1;
				}
				else if( y > passenger[0][1][1] ){
					y -= 1;
					passenger[0][2][1]-=1;
				}
				else if( y < passenger[0][1][1] ){
					y += 1;
					passenger[0][2][1]+=1;
				}
				else if( x == passenger[0][1][0] && y ==   passenger[0][1][1]){
					dropPassenger(0);
					move();
				}
			}
		}
	}
	
	public int[][][] getPassenger(){
		int[][][] temp = new int[numPassenger][3][2];
		for(int count = 0 ; count < numPassenger ; count++)
			temp[count] = passenger[count].clone();
	
		return temp;
	}
	
	public int getNumPassenger(){
		return numPassenger;
	}
	
	public int[] getCoords(){
		int[] coords = new int[2];
		coords[0] = x;
		coords[1] = y;
		return coords;
	}
	
	
}
