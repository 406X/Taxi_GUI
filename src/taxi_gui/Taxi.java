
package taxi_gui;

//Taxi Simulator Backend

import java.util.Arrays;

public class Taxi {
	
	private int numPassenger = 0;
	private int[][][] passenger;
	private int numTaxi = 0;
	private int maxTaxi = 1;
	private int maxPassenger = 10;
	private int taxiSize = 2;
	private int boxes =8;
	private int[] passengerColor = new int[maxPassenger];
	private int[] taxiColor = new int[maxTaxi];
	private int[][][] list = new int[maxPassenger][3][2];
	private int[][] taxiCoords = new int[maxTaxi][2];
	private int[][] taxiPassenger = new int[maxTaxi][taxiSize];
	private int[] taxiNumPassenger = new int[maxTaxi];
	private int[][] blockWeight = new int[boxes+1][boxes+1];
	private int[] passengerStatus = new int[maxPassenger];
	private int[] taxiWeight = new int[maxTaxi];
	private int[] taxiFetching = new int[maxTaxi];
	/*
	Passenger Status
	0 - No Taxi Assigned
	1 - Taxi is Assigned
	
	TaxiFetching
	-1 - Taxi is not getting anyone
	Other Values[X} - Taxi is fetching passenger no.X
	*/
	
	
	/*
	passengerCoords[ID][0] = Source
	passengerCoords[ID][1] = Destination
	passengerCoords[ID][2] = Current
	*/
	
	public Taxi(int maxPassenger, int taxiSize, int boxes){
		this.maxPassenger = maxPassenger;
		this.taxiSize = taxiSize;
		this.boxes = boxes;
		
		
		Arrays.fill(taxiFetching, -1);
		taxiPassenger = new int[maxTaxi][taxiSize];
		
		for(int count = 0 ; count<maxTaxi ; count++)
			Arrays.fill( taxiPassenger[count], -1);
		
		passenger = new int[this.maxPassenger][3][2];
	}
	
	
	public void add(int s_x ,int s_y ,int d_x ,int d_y){
		if(numPassenger<maxPassenger){
			numPassenger++;
			System.out.println(numPassenger);
			passenger[numPassenger-1][0][0] = s_x;
			passenger[numPassenger-1][0][1] = s_y;
			passenger[numPassenger-1][1][0] = d_x;
			passenger[numPassenger-1][1][1] = d_y;
			passenger[numPassenger-1][2][0] = s_x;
			passenger[numPassenger-1][2][1] = s_y;
		}
	}
	
	public void addTaxi(){
		numTaxi++;
		taxiCoords[numTaxi-1][0] = 1;
		taxiCoords[numTaxi-1][1] = 1;
	}
	
	public void dropOff(int taxiIndex , int passengerIndex){
		remove(passengerIndex);
		taxiPassenger[taxiIndex][0]=-1;	
		taxiNumPassenger[taxiIndex]--;
	}
	
	public void remove(int index){
		
		int[][][] templist = new int[maxPassenger][3][2];
		
		for(int count = 0 ,count2 = 0; count < numPassenger ; count++){
			if(count==index)
				continue;
			
			templist[count2] = passenger[count].clone();
			count2++;
		}
		passenger = templist.clone();
		numPassenger--;
	}
	
	public int[][][] getPassengerCoords(){
		return passenger;
	}
	
	public int[][] getTaxiCoords(){
		return taxiCoords;
	}
	
	public int[] getTaxiCoords(int index){
		return taxiCoords[index];
	}
	
	public int getClosestPassenger(int index){
		int[] distance = new int[numPassenger];
		int closestIndex = 0;
		
		for(int count = 0 ; count < numPassenger ; count++){
			distance[count] =  ( taxiCoords[index][0] - passenger[count][0][0] ) + taxiCoords[index][1] - passenger[count][0][1];
			
			if(distance[count]<0)
				distance[count]*=-1;
			
			if( distance[count] < distance[closestIndex] && passengerStatus[count]==0)
				closestIndex = count;
			
		}
		return closestIndex;
	}
	
	public void move(){
		
		
		for(int count = 0 ; count < numTaxi ; count++){
			if(numPassenger==0)
						continue;
			
			//If no passenger
			if( taxiIsEmpty(count)){
				int fetchID = getClosestPassenger(count);
				
				//If taxi is no fetching anyone
				if(taxiFetching[count]==-1){
					passengerStatus[fetchID]=1;
					taxiFetching[count] = fetchID;
				}
				else if( taxiFetching[count]!=fetchID){
					passengerStatus[taxiFetching[count]]=0;
					taxiFetching[count] = fetchID;
				}
				
				//If taxi is at passenger
				if(taxiCoords[count][0]==passenger[fetchID][2][0] && taxiCoords[count][1]==passenger[fetchID][2][1] ){
					taxiPassenger[count][taxiNumPassenger[count]] = fetchID;
					taxiNumPassenger[count]++;
					taxiFetching[count]=-1;
					
					int[] movement = moveTaxi( taxiCoords[count],passenger[fetchID][1],count);
					taxiCoords[count][0]+= movement[0];
					taxiCoords[count][1]+= movement[1];
					passenger[fetchID][2][0] += movement[0];
					passenger[fetchID][2][1] += movement[1];
					continue;
				}
				else{
					//Taxi Movement
					int[] movement = moveTaxi( taxiCoords[count],passenger[fetchID][2],count);
					taxiCoords[count][0]+= movement[0];
					taxiCoords[count][1]+= movement[1];
				}
			}
			//If have passenger
			else{
				//If at destination
				if(taxiCoords[count][0]==passenger[  taxiPassenger[count][0] ][1][0] && taxiCoords[count][1]==passenger[ taxiPassenger[count][0] ][1][1] ){
					dropOff( count , taxiPassenger[count][0]);
					
					if(numPassenger==0)
						continue;
					
					count--;
					continue;
				}
				//If not at destination
				
				int[] movement = moveTaxi( taxiCoords[count],passenger[ taxiPassenger[count][0] ][1],count);
				taxiCoords[count][0]+= movement[0];
				taxiCoords[count][1]+= movement[1];
				passenger[  taxiPassenger[count][0] ][2][0] += movement[0];
				passenger[  taxiPassenger[count][0] ][2][1] += movement[1];
				
			}
		}
	}
	
	public int getNumPassenger(){
		return numPassenger;
	}
	
	
	public int[] moveTaxi( int[] src , int[] dest, int taxiIndex){
		int[] movement = new int[2];
		
		//Movement Algo goes here
		if(blockWeight[ src[1] ][ src[0]] >1 && taxiWeight[taxiIndex] < blockWeight[ src[1] ][ src[0]])
			return movement;
		
		int x_diff = dest[0] - src[0];
		int y_diff = dest[1] - src[1];
		
		int x_diff_abs = x_diff;
		int y_diff_abs = y_diff;
		
		if(x_diff_abs<0)
			x_diff_abs*=-1;
		
		if(y_diff_abs<0)
			y_diff_abs*=-1;
		
		if(x_diff_abs>=y_diff_abs){
			if( x_diff > 0)
				movement[0] = 1;
			else 
				movement[0] = -1;
		
		}
		else{
			if( y_diff > 0)
				movement[1] = 1;
			else 
				movement[1] = -1;
		}
		return movement;
	}
	
	public boolean taxiIsEmpty(int index){
		for(int count = 0 ; count < taxiSize ; count++)
			if(taxiPassenger[index][count]>=0)
				return false;
		return true;
	}
	
	//For Passenger
	public void updateCoords(int index, int c_x , int c_y){
		passenger[index-1][3][0] = c_x;
		passenger[index-1][3][1] = c_y;
	}
	
}
