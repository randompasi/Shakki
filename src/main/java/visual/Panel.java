package visual;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import Pelimekaniikat.ChessBoard;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.stage.WindowEvent;
 
public class Panel extends Application  {
	Stage window;
	Scene start, chess, choose;
	
	private GridPane menuGrid = new GridPane();
	private GridPane menuGrid2 = new GridPane();
	
	private Graphics graphics;

	
	public static void main(String[] args){
		launch(args);
	}
	
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		
	//--------------------creating Starting Panel------------------------
		
		menuGrid.setPadding(new Insets(20, 20, 20, 20));
		menuGrid.setVgap(50);
		menuGrid.setHgap(10);
		start = new Scene(menuGrid, 300, 200);
		
		
		
		 
		//--------------Creating Buttons and adding them to Starting panel -------------------
		
		Button gameButton = new Button("New Game");
		GridPane.setConstraints(gameButton, 10, 0);
		gameButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				try {
					graphics = new Graphics();
				} catch (RuntimeException | IOException e  ) {
					e.printStackTrace();
				}
				window.setScene(chess= new Scene(graphics.getChessBoard(), 600, 600));
			}
			
		});
		
		Button loadButton = new Button("Load Game");
		GridPane.setConstraints(loadButton, 10, 1);
		loadButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				try {
					ObjectInputStream in = new ObjectInputStream(new FileInputStream("SavedGame.bin"));
					ChessBoard Saved = (ChessBoard) in.readObject();
					
					graphics = new Graphics(Saved);
					window.setScene(chess= new Scene(graphics.getChessBoard(), 600, 600));
					in.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					
					e.printStackTrace();
				}
				
				
			}
		});
		
		menuGrid.getChildren().addAll(gameButton, loadButton);
	
	//------------------------Setting scene-------------------------
		
		window.setScene(start);
		window.setTitle("Chess Titan");
		window.show();
		
		 window.setOnCloseRequest( new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	           String filename =  "SavedGame.bin";
	           try {
				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
				ChessBoard as = graphics.getCB();
				os.writeObject(as);
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	          }
	      });
	
	
		 
	
	}
	
	
}
	
    

