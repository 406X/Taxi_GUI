
package taxi_gui;

//Taxi Simulator Backend

import java.util.Arrays;

public class Taxi {
  
    private int numPassenger = 0;
    private int[][][] passenger;
	private int numTaxi = 0;
	private int maxTaxi = 4;
	private int maxPassenger = 10;
	private int taxiSize = 2;
	private int boxes =8;
	private int[] passengerColor = new int[maxPassenger];
	private int[] taxiColor = new int[maxTaxi];
	private int[][][] list = new int[maxPassenger][3][2];
	private int[][] taxiCoords = new int[maxTaxi][2];
	private int[][] taxiPassenger = new int[maxTaxi][taxiSize];
	private int[] taxiNumPassenger = new int[maxTaxi];
	private int[][] blockWeight = new int[boxes][boxes];
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
		for(int count = 0 ; count<maxTaxi ; count++)
			Arrays.fill( taxiPassenger[count], -1);
		
		Arrays.fill(taxiFetching, -1);
    }
    
    
    public void add(int s_x ,int s_y ,int d_x ,int d_y){
        if(numPassenger<maxPassenger){
            numPassenger++;
            int[][][] templist = new int[numPassenger][3][2];
            templist = passenger.clone();
            passenger[numPassenger-1][0][0] = s_x;
            passenger[numPassenger-1][0][1] = s_y;
            passenger[numPassenger-1][1][0] = d_x;
            passenger[numPassenger-1][1][1] = d_y;
            passenger = templist.clone();
        }
    }
    
    public void remove(int index){
        
        int[][][] templist = new int[numPassenger-1][4][2];
        
        for(int count = 0 ,count2 = 0; count < numPassenger ; count++){
            if(count==index-1)
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
			//If no passenger
			if( taxiIsEmpty(count)){
				
				
				int fetchID = getClosestPassenger(count);
				if(taxiFetching[count]==-1){
					passengerStatus[fetchID]=1;
					taxiFetching[count] = fetchID;
				}
				else if( taxiFetching[count]!=fetchID){
					passengerStatus[taxiFetching[count]]=0;
					taxiFetching[count] = fetchID;
				}
				
				if(taxiCoords[count]==passenger[fetchID][2]){
					taxiPassenger[count][taxiNumPassenger[count]] = fetchID;
					taxiNumPassenger[count]++;
					taxiFetching[count]=-1;
				}
				else{
					int[] movement = moveAlgo( taxiCoords[count],passenger[fetchID][2]);
					taxiCoords[count][0]+= movement[0];
					taxiCoords[count][1]+= movement[1];
				}
				
			}
			//If have passenger
			else{
				
				//If at destination
				
				//If not at destination
				
				
			}
		}
    }
	
	public int[] moveAlgo( int[] src , int[] dest){
		int[] movement = new int[2];
	
		//Movement Algo goes here
		
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
