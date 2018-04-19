
package taxi_gui;

//Taxi Simulator Backend
public class Taxi {
  
    private int numPassenger = 0;
    private int[][][] passenger;
    private int currentPassenger = -1;
    /*
    passengerCoords[X][0] = Source
    passengerCoords[X][1] = Destination
    passengerCoords[X][2] = Current
    passengerCoords[X][3] = Colour
    */
    int[] taxi = new int[2];
    private int maxPassenger;
    
    public Taxi(int maxPassenger){
        this.maxPassenger = maxPassenger;
    }
    
    
    public void add(int s_x ,int s_y ,int d_x ,int d_y){
        if(numPassenger<maxPassenger){
            numPassenger++;
            int[][][] templist = new int[numPassenger][4][2];
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
    
    public int[] getTaxiCoords(){
        return taxi;
    }
    
    public int getClosestPassenger(){
        int[] distance = new int[numPassenger];
        int closestIndex = 0;
        
        for(int count = 0 ; count < numPassenger ; count++){
            distance[count] =  ( taxi[0] - passenger[count][0][0] ) + taxi[1] - passenger[count][0][1];
            
            if(distance[count]<0)
                distance[count]*=-1;
            
            if( distance[count] < distance[closestIndex])
                closestIndex = count;
                
        }
        
        return closestIndex;
    }
    
    public void move(){
        
        //If no passenger
        if( currentPassenger<0){
        
        
        }
        //If have passenger
        else{
        
            //If at destination
            
            //If not at destination
        
        
        }
    }
    
    public void updateCoords(int index, int c_x , int c_y){
        passenger[index-1][3][0] = c_x;
        passenger[index-1][3][1] = c_y;
    }
    
}
