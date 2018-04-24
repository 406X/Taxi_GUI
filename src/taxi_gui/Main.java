package taxi_gui;


import java.util.Arrays;
import static javafx.animation.Animation.INDEFINITE;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
public class Main extends Application {
	
	
	//Can be changed
	private int boxes = 10;
	private int maxTaxi = 1;
	private int maxPassenger = 10;
	private int winWidth = 450; //Default: 450
	private int winHeight = 800; //Default: 800
	
	//Do not change
	//-------------------------------------------------------
	private int canvasWidth = winWidth - 50;
	private int canvasHeight = winHeight/2;
	private int maxBlocks =4;
	private double sqrWidth = canvasWidth/boxes;
	private double sqrHeight = canvasHeight/boxes;
	private int numTaxi = 0;
	private int numPassenger = 0;
	private int[][][] list;
	private int[][] Taxi;
	private int[][] blockWeight;
	private long time =0;
	
	Taxi Taxii = new Taxi(maxPassenger,1,boxes);
	Color[] colorPassenger = new Color[10];
	mainCanvas canvas = new mainCanvas(canvasWidth,canvasHeight);
	GraphicsContext gc_box = canvas.getGraphicsContext2D();
	GraphicsContext gc_blocks = canvas.getGraphicsContext2D();
	int box_lineWidth = 4;
	GraphicsContext gc_taxi = canvas.getGraphicsContext2D();
	GraphicsContext gc_weight = canvas.getGraphicsContext2D();
	GraphicsContext gc = canvas.getGraphicsContext2D();
	//-------------------------------------------------------
	
	
	@Override
	public void start(Stage primaryStage) {
		//GUI
		primaryStage.setTitle("Taxii Simulator! Time Elapsed: " + time + " seconds");
		
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		
		primaryStage.setHeight(winHeight);
		primaryStage.setWidth(winWidth);
		primaryStage.setResizable(false);
		
		root.getChildren().add(canvas);
		canvas.setTranslateX( winWidth * (20.0/450) );
		canvas.setTranslateY( winHeight * (25.0/800) );
		
		//Texts and Textboxes
		int tf_width = 40;
		TextField tf_srcx = new NumberTextField( (int) ( winWidth * (15+25)/450.0 ),(int) ( winHeight * (520-20.0)/800 ) );
		tf_srcx.setMaxWidth(tf_width );
		root.getChildren().add(tf_srcx);
		
		TextField tf_srcy = new NumberTextField( (int) ( winWidth * (15+110.0)/450 ), (int)( winHeight * (520-20.0)/800 ) );
		tf_srcy.setMaxWidth(tf_width );
		root.getChildren().add(tf_srcy);
		
		Text t_srcx = new defaultText("X",winWidth * (15.0/450) ,winHeight * (520.0/800));
		root.getChildren().add(t_srcx);
		
		Text t_srcy = new defaultText("Y",winWidth * (15+85.0)/450,winHeight * (520.0/800));
		root.getChildren().add(t_srcy);
		
		Text t_dstx = new defaultText("X",winWidth * (15+240.0)/450,winHeight * (520.0/800));
		root.getChildren().add(t_dstx);
		
		Text t_dsty = new defaultText("Y",winWidth * (15+325.0)/450,winHeight * (520.0/800));
		root.getChildren().add(t_dsty);
		
		TextField tf_dstx = new NumberTextField( (int) ( winWidth *(15+240+25.0)/450 ),(int) ( winHeight * (520-20.0)/800 ) );
		tf_dstx.setMaxWidth(tf_width );
		root.getChildren().add(tf_dstx);
		
		TextField tf_dsty = new NumberTextField( (int) ( winWidth*(15+240+25+85.0)/450), (int) (winHeight * (520-20.0)/800));
		tf_dsty.setMaxWidth(tf_width );
		root.getChildren().add(tf_dsty);
		
		Text t_src = new defaultText("Source:",winWidth * (15.0/450),winHeight * (520-35.0)/800);
		root.getChildren().add(t_src);
		
		Text t_dst = new defaultText("Destination:",winWidth * (15+240.0)/450,winHeight * (520-35.0)/800);
		root.getChildren().add(t_dst);
		
		//Button
		Button addPassenger = new Button("Add Passenger");
		addPassenger.setTranslateX(winWidth/2*0.72);
		addPassenger.setTranslateY(winHeight * (520+20.0)/800);
		root.getChildren().add(addPassenger);
		
		Taxii.setBlockWeight( 0, 0, 3);
		blockWeight = Taxii.getBlockWeight();
		
		//Test Variables
		addPassenger(2,2,4,4);
		addPassenger(8,8,3,3);
		addTaxi();
		
		//Fetch data from backend
		list = Taxii.getPassengerCoords();
		Taxi = Taxii.getTaxiCoords();
		
		//Draw 
		primaryStage.show();
		
		drawBlocks();
		drawBoxes();
		drawTaxi();
		drawWeight();
		
		//Loop
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.seconds(1), e -> {
					gc.setFill( Color.WHITE);
					gc.fillRect(0, 0, canvasWidth, canvasHeight);
					Taxii.move();
					numPassenger = Taxii.getNumPassenger();
					list = Taxii.getPassengerCoords();
					Taxi = Taxii.getTaxiCoords();
					
					drawBlocks();
					drawBoxes();
					drawTaxi();
					drawWeight();
					time+=1;
					primaryStage.setTitle("Taxii Simulator! Time Elapsed: " + time +" seconds");
				})
		);
		timeline.setCycleCount(INDEFINITE);
		
		timeline.play();
		
		//Button Action
		addPassenger.setOnAction( 
				new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				int countBlock = 0;
				for(int count = 0 ; count < numPassenger ; count++){
					if( list[count][2][0] == Integer.valueOf( tf_srcx.getText() ) 
						&& list[count][2][1] == Integer.valueOf( tf_srcy.getText() ) ){
						countBlock++;
					}
				}	
				if(	countBlock < maxBlocks  
					&& Integer.valueOf(tf_srcx.getText()) < boxes 
					&& Integer.valueOf(tf_srcy.getText()) < boxes 
					&& Integer.valueOf(tf_dstx.getText()) < boxes 
					&& Integer.valueOf(tf_dsty.getText()) < boxes )
					
					addPassenger(Integer.valueOf(tf_srcx.getText()),Integer.valueOf(tf_srcy.getText()) ,Integer.valueOf(tf_dstx.getText()),Integer.valueOf(tf_dsty.getText()));
			}
		});
		
	}//End of start
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void addPassenger(int x_src,int y_src,int x_dest,int y_dest){
		Taxii.add(x_src, y_src, x_dest, y_dest);
		numPassenger++;
	}
	
	public void addTaxi(){
		Taxii.addTaxi();
		numTaxi++;
	}
	
	public void drawBoxes(){
		gc_box.setStroke(Color.BLACK);
		for(int count = 0 , x = 0 , y = 0 ; count<boxes ; count++, y+=sqrHeight, x = 0){
			for(int count2 = 0 ; count2 < boxes ; count2++, x+=sqrWidth){
				gc_box.strokeRect(x, y, sqrWidth, sqrHeight);
			}
		}
		gc_box.setLineWidth(box_lineWidth);
		gc_box.strokeRect(0, 0, canvasWidth, canvasHeight);
	}
	
	
	public void drawBlocks(){
		gc_blocks.setFill(Color.RED);
		int[][] countBlock = new int[boxes][boxes];
		for(int count = 0 ;count < numPassenger ; count++){
			gc_blocks.fillRect( convertX(list[count][2][0],list[count][2][1],countBlock) , convertY(list[count][2][0],list[count][2][1],countBlock), sqrWidth/2, sqrHeight/2);
			countBlock[list[count][2][0]-1][list[count][2][1]-1]++;
		}
	}
	
	public void drawTaxi(){
		gc_taxi.setLineWidth(box_lineWidth/2);
		gc_taxi.setStroke( Color.AQUA);
		for(int count = 0 ; count < numTaxi ; count++){
			gc_taxi.strokeRect((Taxi[count][0] - 1)*sqrWidth + box_lineWidth/2 , (Taxi[count][1] - 1)*sqrHeight + box_lineWidth/2, sqrWidth - box_lineWidth, sqrHeight - box_lineWidth);
		}
	}
	
	public void drawWeight(){
		gc_weight.setStroke(Color.BLACK);
		gc_weight.setFont(new Font("Verdana", 14));
		gc_weight.setLineWidth(1);
		for(int count = 0 , x = 0 , y = 0 ; count<boxes ; count++, y+=sqrHeight, x = 0){
			for(int count2 = 0 ; count2 < boxes ; count2++, x+=sqrWidth){
				gc_weight.strokeText( String.valueOf(blockWeight[count2][count]), count2*sqrWidth + 0.75*sqrWidth, count*sqrHeight+ 0.85*sqrHeight);
			}
		}
	
	}

	public double convertX(int blockX,int blockY,int[][] countBlock){
		blockX--;
		blockY--;
		if(countBlock[blockX][blockY]==0 || countBlock[blockX][blockY] == 2 )
			return sqrWidth*blockX;
		else
			return sqrWidth*blockX+sqrWidth/2;
	}
	
	public double convertY(int blockX,int blockY,int countBlock[][]){
		blockX--;
		blockY--;
		if(countBlock[blockX][blockY]==0 || countBlock[blockX][blockY] == 1 )
			return sqrHeight*blockY;
		else
			return sqrHeight*blockY+sqrHeight/2;
	}
	
}
