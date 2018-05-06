
package taxi_gui;


public class TaxiObject {
	
	private int maxPassenger = 1;
	private int numPassenger = 0;
	private static Log log = new Log();
	private static int time = 0;
	private int ID;
	private int[][][] passenger = new int[maxPassenger][4][2];
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
	
	public TaxiObject(int maxPassenger, int x , int y, PassengerList pList,Log log,int id){
		this.log = log;
		this.ID = id;
		log.writelog("["+ time + "]" + " Taxi " + ID +  " started");
		passengerList = pList;
		this.maxPassenger = maxPassenger;
		passenger = new int[maxPassenger][4][2];
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
				log.writelog("["+ time + "]" + " Taxi " + ID +  " going to pick-up passenger at(" + passenger[0][0][0] + "," + passenger[0][0][1] +")" );
				move();
			}
		}
		else{
			if(passengerStatus[0] == 0){	
				if( x < passenger[0][2][0] ){
					x += 1;
					log.writelog("["+ time + "]" + " Taxi " + ID +  " moving right");
				}
				else if( x > passenger[0][2][0] ){
					x -= 1;
					log.writelog("["+ time + "]" + " Taxi " + ID +  " moving left");
				}
				else if( y < passenger[0][2][1] ){
					y += 1;
					log.writelog("["+ time + "]" + " Taxi " + ID +  " moving down");
				}
				else if( y > passenger[0][2][1] ){
					y -= 1;
					log.writelog("["+ time + "]" + " Taxi " + ID +  " moving up");
				}
				else if( x == passenger[0][2][0] && y ==   passenger[0][2][1]){
					passenger[0][3][0] = time - passenger[0][3][0]; // Calculate time waited for taxi to arrive
					passenger[0][3][1] = time; // For calculating riding time
					passengerStatus[0] = 1;
					log.writelog("["+ time + "]" + " Taxi " + ID +  " picked up passenger, heading to destination");
					move();
				}
			}
			else{
				if( x < passenger[0][1][0] ){
					x += 1;
					passenger[0][2][0]+=1;
					log.writelog("["+ time + "]" + " Taxi " + ID +  " moving right");
				}
				else if( x > passenger[0][1][0] ){
					x -= 1;
					passenger[0][2][0]-=1;
					log.writelog("["+ time + "]" + " Taxi " + ID +  " moving left");
				}
				else if( y > passenger[0][1][1] ){
					y -= 1;
					passenger[0][2][1]-=1;
					log.writelog("["+ time + "]" + " Taxi " + ID +  " moving up");
				}
				else if( y < passenger[0][1][1] ){
					y += 1;
					passenger[0][2][1]+=1;
					log.writelog("["+ time + "]" + " Taxi " + ID +  " moving down");
				}
				else if( x == passenger[0][1][0] && y ==   passenger[0][1][1]){
					passenger[0][3][1] = time - passenger[0][3][1];
					log.writelog("["+ time + "]" + " Taxi " + ID +  " arrived at destination, dropping off passenger");
					log.writelog("["+ time + "]" + " Passenger waited for " + passenger[0][3][0] +" minutes and rode for " + passenger[0][3][1] + " minutes");
					dropPassenger(0);
					move();
				}
			}
		}
	}
	
	public int[][][] getPassenger(){
		int[][][] temp = new int[numPassenger][4][2];
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
	
	public void setTime(int num){
		time = num;
	}
}
