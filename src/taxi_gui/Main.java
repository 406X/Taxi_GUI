package taxi_gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
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
public class Main extends Application {
	
	
	
	private int winWidth = 450;
	private int winHeight = 800;
	private int canvasWidth = winWidth - 50;
	private int canvasHeight = winHeight/2;
	private int boxes = 8;
	private int maxBlocks =4;
	private int numTaxi = 0;
	private int maxTaxi = 4;
	private double sqrWidth = canvasWidth/boxes;
	private double sqrHeight = canvasHeight/boxes;
	private int numPassenger = 0;
	private int maxPassenger = 10;
	private int[][][] list = new int[maxPassenger][3][2];
	private int[][] Taxi = new int[maxTaxi][2];
	
	Color[] colorPassenger = new Color[10];
	mainCanvas canvas = new mainCanvas(canvasWidth,canvasHeight);
	GraphicsContext gc_box = canvas.getGraphicsContext2D();
	GraphicsContext gc_blocks = canvas.getGraphicsContext2D();
	int box_lineWidth = 4;
	GraphicsContext gc_taxi = canvas.getGraphicsContext2D();
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Taxii Simulator!");
		
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		
		primaryStage.setHeight(winHeight);
		primaryStage.setWidth(winWidth);
		primaryStage.setResizable(false);
		
		root.getChildren().add(canvas);
		canvas.setTranslateX(20);
		canvas.setTranslateY(25);
		
		//Texts and Textboxes
		int x_srcx = 15;
		int y_srcx = 520;
		int tf_width = 40;
		TextField tf_srcx = new NumberTextField(x_srcx+25,y_srcx-20);
		tf_srcx.setMaxWidth(tf_width );
		root.getChildren().add(tf_srcx);
		
		TextField tf_srcy = new NumberTextField(x_srcx+110,y_srcx-20);
		tf_srcy.setMaxWidth(tf_width );
		root.getChildren().add(tf_srcy);
		
		Text t_srcx = new defaultText("X",x_srcx,y_srcx);
		root.getChildren().add(t_srcx);
		
		Text t_srcy = new defaultText("Y",x_srcx+85,y_srcx);
		root.getChildren().add(t_srcy);
		
		Text t_dstx = new defaultText("X",x_srcx+240,y_srcx);
		root.getChildren().add(t_dstx);
		
		Text t_dsty = new defaultText("Y",x_srcx+325,y_srcx);
		root.getChildren().add(t_dsty);
		
		TextField tf_dstx = new NumberTextField(x_srcx+240+25,y_srcx-20);
		tf_dstx.setMaxWidth(tf_width );
		root.getChildren().add(tf_dstx);
		
		TextField tf_dsty = new NumberTextField(x_srcx+240+25+85,y_srcx-20);
		tf_dsty.setMaxWidth(tf_width );
		root.getChildren().add(tf_dsty);
		
		Text t_src = new defaultText("Source:",x_srcx,y_srcx-35);
		root.getChildren().add(t_src);
		
		Text t_dst = new defaultText("Destination:",x_srcx+240,y_srcx-35);
		root.getChildren().add(t_dst);
		
		//Test Variables
		numPassenger=2;
		list[0][0][0]=1;
		list[0][0][1]=1;
		list[0][1][0]=5;
		list[0][1][1]=5;
		list[0][2][0]=1;
		list[0][2][1]=1;
		
		list[1][0][0]=2;
		list[1][0][1]=2;
		list[1][1][0]=5;
		list[1][1][1]=5;
		list[1][2][0]=2;
		list[1][2][1]=2;
		
		numTaxi=1;
		Taxi[0][0] = 2;
		Taxi[0][1] = 3;
		
		drawBoxes();
		drawBlocks();
		drawTaxi();
		primaryStage.show();
	}//End of start
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void drawBoxes(){
		for(int count = 0 , x = 0 , y = 0 ; count<boxes ; count++, y+=sqrHeight, x = 0){
			for(int count2 = 0 ; count2 < boxes ; count2++, x+=sqrWidth){
				gc_box.strokeRect(x, y, sqrWidth, sqrHeight);
			}
		}
		gc_box.setLineWidth(box_lineWidth);
		gc_box.strokeRect(0, 0, canvasWidth, canvasHeight);
	}
	
	public void move(int index, int c_x , int c_y){
		
	}
	
	public void drawBlocks(){
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
			gc_taxi.strokeRect(Taxi[count][0]*sqrWidth + box_lineWidth/2 , Taxi[count][0]*sqrHeight + box_lineWidth/2, sqrWidth - box_lineWidth, sqrHeight - box_lineWidth);
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
