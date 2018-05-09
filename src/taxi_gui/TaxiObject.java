package taxi_gui;
import java.util.ArrayList;
import java.util.Arrays;

public class TaxiObject {
	private ArrayList<Integer[]> arrList = new ArrayList();
	private int numTaxi = 0;
	private int maxPassenger = 1;
	private int numPassenger = 0;
	private static Log log = new Log();
	private int numMove=-1;
	private static int time = 0;
	private int weight = 1;
	private int ID;
	private int maxBlocks = 4;
	private int boxes = 0;
	private int[][] bWeight;
	private int[][] obstacle;
	private int[][][] passenger = new int[maxPassenger][4][2];
	private int initialDepth = boxes;
	private static int iteration = 0;
	
	private boolean enableIterativeDeepening = true; // Much more efficient for small search space
	private int deepeneningPerIteration = 2;
	private boolean enableSearchDepth = false;
	private int searchDepth = 40; //Increase this when increasing the number of boxes (At least 2.5*boxes is recommended)
								  //Increasing this will increase the time taken to find a route
	
	private boolean exhaustive = true; //If set to true, the algorithm will search everything and ignore searchDepth and iterationThreshold
									   //Thus increasing search time tremendously
									   //In exchange, the generated path is the best possible
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
	
	public TaxiObject(int maxPassenger, int x , int y,Log log,int id, int boxes, int maxBlocks){
		this.log = log;
		this.ID = id;
		this.boxes = boxes;
		this.maxBlocks = maxBlocks;
		this.initialDepth = boxes;
		log.writelog("["+ time + "]" + " Taxi " + ID +  " started");
		if(id==1){
			passengerList = new PassengerList(maxPassenger);
		}
		this.maxPassenger = maxPassenger;
		passenger = new int[maxPassenger][4][2];
		passengerStatus = new int[maxPassenger];
		this.x = x;
		this.y = y;
		numTaxi++;
		
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
				addPassenger( passengerList.getClosestPassenger(x, y).clone() );
				log.writelog("["+ time + "]" + " Taxi " + ID +  " going to pick-up passenger at(" + passenger[0][0][0] + "," + passenger[0][0][1] +")" );
				numMove = -1;
				move();
			}
		}
		else{
			if(passengerStatus[0] == 0){
				if(numMove==-1){
					if(enableIterativeDeepening)
						searchDepth = initialDepth;
					
					calcPath( passenger[0][0][0] , passenger[0][0][1]);
					
					while(enableIterativeDeepening && numMove==-1){
						searchDepth+=deepeneningPerIteration;
						calcPath( passenger[0][0][0] , passenger[0][0][1]);
					}
					if(!enableIterativeDeepening && numMove==-1)
						System.out.println( "Taxi " + ID + ": No solution found, try increasing search depth.");
					else
						move();
				}
				//At passenger pickup location
				else if( x == passenger[0][0][0] && y ==   passenger[0][0][1]){
					numMove = -1;
					passenger[0][3][0] = time - passenger[0][3][0]; // Calculate time waited for taxi to arrive
					passenger[0][3][1] = time; // For calculating riding time
					passengerStatus[0] = 1;
					log.writelog("["+ time + "]" + " Taxi " + ID +  " picked up passenger, heading to destination("+ passenger[0][1][0] + "," + passenger[0][1][1] +")");
					move();
				}
				else{
					//Move
					//System.out.println( "X:" + x + " Y:" + y);
					if(weight < bWeight[y-1][x-1])
						weight++;
					else{
						weight=1;
						Integer[] move = arrList.remove(0);
						x+=move[0];
						y+=move[1];
						
						
						if(move[0]==1)
							log.writelog("["+ time + "]" + " Taxi " + ID +  " is moving right");
						else if(move[0]==-1)
							log.writelog("["+ time + "]" + " Taxi " + ID +  " is moving left");
						else if(move[1]==1)
							log.writelog("["+ time + "]" + " Taxi " + ID +  " is moving down");
						else if(move[1]==-1)
							log.writelog("["+ time + "]" + " Taxi " + ID +  " is moving up");
					}
					
				}
			}
			else{
				if(numMove==-1){
					if(enableIterativeDeepening)
						searchDepth = initialDepth;
					
					calcPath( passenger[0][1][0] , passenger[0][1][1]);
					
					while(enableIterativeDeepening && numMove==-1){
						searchDepth+=deepeneningPerIteration;
						calcPath( passenger[0][1][0] , passenger[0][1][1]);
					}
					if(!enableIterativeDeepening && numMove==-1)
						System.out.println( "Taxi " + ID + ": No solution found, try increasing search depth.");
					else
						move();
				}
				//At passenger drop-off destination
				else if( x == passenger[0][1][0] && y ==   passenger[0][1][1]){
					passenger[0][3][1] = time - passenger[0][3][1];
					log.writelog("["+ time + "]" + " Taxi " + ID +  " arrived at destination, dropping off passenger");
					log.writelog("["+ time + "]" + " Passenger waited for " + passenger[0][3][0] +" minutes and rode for " + passenger[0][3][1] + " minutes");
					arrList = new ArrayList();
					numMove=-1;
					dropPassenger(0);
					move();
				}
				else{
					//Move
					//System.out.println( "X:" + x + " Y:" + y);
					if(weight < bWeight[y-1][x-1])
						weight++;
					else{
						weight=1;
						Integer[] move = arrList.remove(0);
						passenger[0][2][0]+=move[0];
						passenger[0][2][1]+=move[1];
						x+=move[0];
						y+=move[1];
						
						if(move[0]==1)
							log.writelog("["+ time + "]" + " Taxi " + ID +  " is moving right");
						else if(move[0]==-1)
							log.writelog("["+ time + "]" + " Taxi " + ID +  " is moving left");
						else if(move[1]==1)
							log.writelog("["+ time + "]" + " Taxi " + ID +  " is moving down");
						else if(move[1]==-1)
							log.writelog("["+ time + "]" + " Taxi " + ID +  " is moving up");
					}
				}

			}
		}
	}
	
	public void calcPath(int destX, int destY){
		iteration = 1;
		ArrayList<Integer[]> tempList = new ArrayList();
		ArrayList<Integer[]> visited = new ArrayList();
		ArrayList<Integer[]> queue = new ArrayList();
		int tempNumMove = 0;
		int tempNumMove2;
		int currentX = x;
		int currentY = y;
		int depth = 1;
		int moveX = 0;
		int moveY = 0;
		
		
		
		if( (tempNumMove > numMove && numMove!=-1) )
			return;
		
		if(!exhaustive){
			if(  ( depth > searchDepth && enableSearchDepth) )
				return;
		}
		else if(enableIterativeDeepening){
			if( depth > searchDepth  )
				return;
		}
		
		moveX = 1;
		moveY = 0;
		
		if( currentX + moveX <=boxes  && currentX + moveX >0 && currentY + moveY <=boxes && currentY + moveY >0 && ( obstacle[currentY+moveY-1][currentX+moveX-1]!=1) &&!(isBoxFull(currentX+moveX,currentY+moveY,0)&&numPassenger>0)){
			tempNumMove2 = tempNumMove + bWeight[ currentX + moveX - 1][ currentY + moveY -1];
			if( (currentX + moveX == destX && currentY+moveY == destY && numMove == -1) || ( currentX + moveX == destX && currentY+moveY == destY && tempNumMove2 < numMove)){
				Integer[] move = new Integer[2];
				move[0] = moveX;
				move[1] = moveY;
				tempList.add( move );
				numMove = tempNumMove2;
				arrList = tempList;
				return;
			}
			else{
				ArrayList tempVisited = copyArrList(visited);
				Integer[] currentVisit = new Integer[2];
				currentVisit[0] = currentX;
				currentVisit[1] = currentY;
				Integer[] nextVisit = new Integer[2];
				nextVisit[0] = currentX + moveX;
				nextVisit[1] = currentY + moveY;
				
				
				
				if(!containsArr( visited,nextVisit)){
					Integer[] move = new Integer[2];
					move[0] = moveX;
					move[1] = moveY;
					ArrayList temp = copyArrList(tempList);
					temp.add(move);
					tempVisited.add(currentVisit);
					queue.add(nextVisit);
					calcPath( temp,tempNumMove2,currentX+move[0],currentY+move[1] ,destX , destY,tempVisited,depth);
				}
			}
		}
		
		moveX = 0;
		moveY = 1;
		if( currentX + moveX <=boxes  && currentX + moveX >0 && currentY + moveY <=boxes && currentY + moveY >0  && ( obstacle[currentY+moveY-1][currentX+moveX-1]!=1)&&!(isBoxFull(currentX+moveX,currentY+moveY,0)&&numPassenger>0)){
			tempNumMove2 =tempNumMove + bWeight[ currentX + moveX - 1][ currentY + moveY -1];
			if( (currentX + moveX == destX && currentY+moveY == destY && numMove == -1) || ( currentX + moveX == destX && currentY+moveY == destY && tempNumMove2 < numMove)){
				Integer[] move = new Integer[2];
				move[0] = moveX;
				move[1] = moveY;
				tempList.add( move );
				numMove = tempNumMove2;
				arrList = tempList;
				return;
			}
			else{
				ArrayList<Integer[]> tempVisited = copyArrList(visited);
				Integer[] currentVisit = new Integer[2];
				currentVisit[0] = currentX;
				currentVisit[1] = currentY;
				Integer[] nextVisit = new Integer[2];
				nextVisit[0] = currentX + moveX;
				nextVisit[1] = currentY + moveY;
				
				if(!containsArr( visited,nextVisit)){
					Integer[] move = new Integer[2];
					move[0] = moveX;
					move[1] = moveY;
					ArrayList<Integer[]> temp = copyArrList(tempList);
					temp.add(move);
					tempVisited.add(currentVisit);
					calcPath( temp,tempNumMove2,currentX+move[0],currentY+move[1] ,destX , destY,tempVisited,depth);
				}
			}
		}
		
		moveX = -1;
		moveY = 0;
		if( currentX + moveX <= boxes  && currentX + moveX >0 && currentY + moveY <=boxes && currentY + moveY >0 && ( obstacle[currentY+moveY-1][currentX+moveX-1]!=1)&&!(isBoxFull(currentX+moveX,currentY+moveY,0)&&numPassenger>0)){
			tempNumMove2 = tempNumMove + bWeight[ currentX + moveX - 1][ currentY + moveY - 1];
			if( (currentX + moveX == destX && currentY+moveY == destY && numMove == -1) || ( currentX + moveX == destX && currentY+moveY == destY && tempNumMove2 < numMove)){
				Integer[] move = new Integer[2];
				move[0] = moveX;
				move[1] = moveY;
				tempList.add( move );
				numMove = tempNumMove2;
				arrList = tempList;
				return;
			}
			else{
				ArrayList<Integer[]> tempVisited = copyArrList(visited);
				Integer[] currentVisit = new Integer[2];
				currentVisit[0] = currentX;
				currentVisit[1] = currentY;
				Integer[] nextVisit = new Integer[2];
				nextVisit[0] = currentX + moveX;
				nextVisit[1] = currentY + moveY;
				
				if(!containsArr( visited,nextVisit)){
					Integer[] move = new Integer[2];
					move[0] = moveX;
					move[1] = moveY;
					ArrayList<Integer[]> temp = copyArrList(tempList);
					temp.add(move);
					tempVisited.add(currentVisit);
					calcPath( temp,tempNumMove2,currentX+move[0],currentY+move[1] ,destX , destY,tempVisited,depth);
				}
			}
		}
		
		moveX = 0;
		moveY = -1;
		if( currentX + moveX <=boxes  && currentX + moveX >0 && currentY + moveY <=boxes && currentY + moveY >0 && ( obstacle[currentY+moveY-1][currentX+moveX-1]!=1)&&!(isBoxFull(currentX+moveX,currentY+moveY,0)&&numPassenger>0)){
			tempNumMove2 =tempNumMove + bWeight[ currentX + moveX - 1][ currentY + moveY -1];
			if( (currentX + moveX == destX && currentY+moveY == destY && numMove == -1) || ( currentX + moveX == destX && currentY+moveY == destY && tempNumMove2 < numMove)){
				Integer[] move = new Integer[2];
				move[0] = moveX;
				move[1] = moveY;
				tempList.add( move );
				numMove = tempNumMove2;
				arrList = tempList;
				return;
			}
			else{
				ArrayList<Integer[]> tempVisited = copyArrList(visited);
				Integer[] currentVisit = new Integer[2];
				currentVisit[0] = currentX;
				currentVisit[1] = currentY;
				Integer[] nextVisit = new Integer[2];
				nextVisit[0] = currentX + moveX;
				nextVisit[1] = currentY + moveY;
				
				if(!containsArr( visited,nextVisit)){
					Integer[] move = new Integer[2];
					move[0] = moveX;
					move[1] = moveY;
					ArrayList<Integer[]> temp = copyArrList(tempList);
					temp.add(move);
					tempVisited.add(currentVisit);
					calcPath( temp,tempNumMove2,currentX+move[0],currentY+move[1] ,destX , destY,tempVisited,depth);
				}
				
				
			}
		}
	}

		public boolean isBoxFull(int s_x, int s_y,int offset){
		int countBlock = 0;
			int[][][] list = getPassengerList().getPassengers();
			
			for(int count = 0 ; count < list.length ; count++){
				if( list[count][2][0] == s_x
						&& list[count][2][1] == s_y ){
					countBlock++;
				}
			}
			
			for(int count = 0 ; count < numTaxi ; count++){
				list = getPassenger();
				
				for(int count2 = 0 ; count2 < list.length ; count2++){
					if( list[count2][2][0] == s_x
							&& list[count2][2][1] == s_y ){
						countBlock++;
					}
				}
				
			}
			
			if(	countBlock < maxBlocks+offset)
				return false;
			else
				return true;
	}
	
	public ArrayList copyArrList( ArrayList arrList){
		ArrayList temp = new ArrayList();
		for(int count = 0 ; count<arrList.size() ; count++)
			temp.add( arrList.get(count) );
		
		return temp;
	}
	
	public boolean containsArr(ArrayList<Integer[]> arrList,Integer[] arr){
		for(int count =0 ; count < arrList.size() ; count++){
		Integer[] arr2 = arrList.get(count);
		
		if(Arrays.equals(arr2, arr))
				return true;
		}
		return false;
	}
	
	public void calcPath(ArrayList<Integer[]> tempList , int tempNumMove, int currentX, int currentY , int destX , int destY, ArrayList<Integer[]> visited, int depth){
		//Integer[] lastVisited = visited.get(visited.size()-1);
		//System.out.println( "Last visited: X: " + lastVisited[0] + " Y: " + lastVisited[1]);

		//System.out.println("Iteration: " + iteration + " Depth: "+tempNumMove);
		int moveX = 0;
		int moveY = 0;
		int tempNumMove2;	
		//System.out.println(tempNumMove);
		depth++;
		if( (tempNumMove > numMove && numMove!=-1) )
			return;
		
		iteration++;
		
		if(!exhaustive){
			if(  ( depth > searchDepth && enableSearchDepth) )
				return;
		}
		else if(enableIterativeDeepening){
			if( depth > searchDepth  )
				return;
		}
		
		moveX = 1;
		moveY = 0;
		if( currentX + moveX <=boxes  && currentX + moveX >0 && currentY + moveY <=boxes && currentY + moveY >0 && ( obstacle[currentY+moveY-1][currentX+moveX-1]!=1)&&!(isBoxFull(currentX+moveX,currentY+moveY,0)&&numPassenger>0)){
			tempNumMove2 = tempNumMove + bWeight[ currentX + moveX - 1][ currentY + moveY -1];
			if( (currentX + moveX == destX && currentY+moveY == destY && numMove == -1) || ( currentX + moveX == destX && currentY+moveY == destY && tempNumMove2 < numMove)){
				Integer[] move = new Integer[2];
				move[0] = moveX;
				move[1] = moveY;
				tempList.add( move );
				numMove = tempNumMove2;
				arrList = tempList;
				return;
			}
			else{
				ArrayList<Integer[]> tempVisited = copyArrList(visited);
				Integer[] currentVisit = new Integer[2];
				currentVisit[0] = currentX;
				currentVisit[1] = currentY;
				Integer[] nextVisit = new Integer[2];
				nextVisit[0] = currentX + moveX;
				nextVisit[1] = currentY + moveY;
				
				if(!containsArr( visited,nextVisit)){
					Integer[] move = new Integer[2];
					move[0] = moveX;
					move[1] = moveY;
					ArrayList<Integer[]> temp = copyArrList(tempList);
					temp.add(move);
					tempVisited.add(currentVisit);
					calcPath( temp,tempNumMove2,currentX+move[0],currentY+move[1] ,destX , destY,tempVisited,depth);
				}
			}
		}
		
		moveX = 0;
		moveY = 1;
		if( currentX + moveX <=boxes  && currentX + moveX >0 && currentY + moveY <=boxes && currentY + moveY >0 && ( obstacle[currentY+moveY-1][currentX+moveX-1]!=1)&&!(isBoxFull(currentX+moveX,currentY+moveY,0)&&numPassenger>0)){
			tempNumMove2 =tempNumMove + bWeight[ currentX + moveX - 1][ currentY + moveY -1];
			if( (currentX + moveX == destX && currentY+moveY == destY && numMove == -1) || ( currentX + moveX == destX && currentY+moveY == destY && tempNumMove2 < numMove)){
				Integer[] move = new Integer[2];
				move[0] = moveX;
				move[1] = moveY;
				tempList.add( move );
				numMove = tempNumMove2;
				arrList = copyArrList( tempList);
				return;
			}
			else{
				ArrayList<Integer[]> tempVisited = copyArrList(visited);
				Integer[] currentVisit = new Integer[2];
				currentVisit[0] = currentX;
				currentVisit[1] = currentY;
				Integer[] nextVisit = new Integer[2];
				nextVisit[0] = currentX + moveX;
				nextVisit[1] = currentY + moveY;
				
				if(!containsArr( visited,nextVisit)){
					Integer[] move = new Integer[2];
					move[0] = moveX;
					move[1] = moveY;
					ArrayList<Integer[]> temp = copyArrList(tempList);
					temp.add(move);
					tempVisited.add(currentVisit);
					calcPath( temp,tempNumMove2,currentX+move[0],currentY+move[1] ,destX , destY,tempVisited,depth);
				}
			}
		}
		
		moveX = -1;
		moveY = 0;
		if( currentX + moveX <=boxes  && currentX + moveX >0 && currentY + moveY <=boxes && currentY + moveY >0 && ( obstacle[currentY+moveY-1][currentX+moveX-1]!=1)&&!(isBoxFull(currentX+moveX,currentY+moveY,0)&&numPassenger>0)){
			tempNumMove2 =tempNumMove + bWeight[ currentX + moveX - 1][ currentY + moveY -1];
			if( (currentX + moveX == destX && currentY+moveY == destY && numMove == -1) || ( currentX + moveX == destX && currentY+moveY == destY && tempNumMove2 < numMove)){
				Integer[] move = new Integer[2];
				move[0] = moveX;
				move[1] = moveY;
				tempList.add( move );
				numMove = tempNumMove2;
				arrList = copyArrList( tempList);
				return;
			}
			else{
				ArrayList<Integer[]> tempVisited = copyArrList(visited);
				Integer[] currentVisit = new Integer[2];
				currentVisit[0] = currentX;
				currentVisit[1] = currentY;
				Integer[] nextVisit = new Integer[2];
				nextVisit[0] = currentX + moveX;
				nextVisit[1] = currentY + moveY;
				
				if(!containsArr( visited,nextVisit)){
					Integer[] move = new Integer[2];
					move[0] = moveX;
					move[1] = moveY;
					ArrayList<Integer[]> temp = copyArrList(tempList);
					temp.add(move);
					tempVisited.add(currentVisit);
					calcPath( temp,tempNumMove2,currentX+move[0],currentY+move[1] ,destX , destY,tempVisited,depth);
				}
			}
		}
		
		moveX = 0;
		moveY = -1;
		if( currentX + moveX <=boxes  && currentX + moveX >0 && currentY + moveY <=boxes && currentY + moveY >0 && ( obstacle[currentY+moveY-1][currentX+moveX-1]!=1)&&!(isBoxFull(currentX+moveX,currentY+moveY,0)&&numPassenger>0)){
			tempNumMove2 =tempNumMove + bWeight[ currentX + moveX - 1][ currentY + moveY -1];
			if( (currentX + moveX == destX && currentY+moveY == destY && numMove == -1) || ( currentX + moveX == destX && currentY+moveY == destY && tempNumMove2 < numMove)){
				Integer[] move = new Integer[2];
				move[0] = moveX;
				move[1] = moveY;
				tempList.add( move );
				numMove = tempNumMove2;
				arrList = copyArrList( tempList);
				return;
			}
			else{
				ArrayList<Integer[]> tempVisited = copyArrList(visited);
				Integer[] currentVisit = new Integer[2];
				currentVisit[0] = currentX;
				currentVisit[1] = currentY;
				Integer[] nextVisit = new Integer[2];
				nextVisit[0] = currentX + moveX;
				nextVisit[1] = currentY + moveY;
				
				if(!containsArr( visited,nextVisit)){
					Integer[] move = new Integer[2];
					move[0] = moveX;
					move[1] = moveY;
					ArrayList<Integer[]> temp = copyArrList(tempList);
					temp.add(move);
					tempVisited.add(currentVisit);
					calcPath( temp,tempNumMove2,currentX+move[0],currentY+move[1] ,destX , destY,tempVisited,depth);				}
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
	
	public void setBlockWeight( int[][] bWeight){
		this.bWeight = bWeight;
	}
	
	public void setObstacle( int[][] obstacle){
		this.obstacle = obstacle;
	}
	
	public PassengerList getPassengerList(){
		return passengerList;
	}
}
