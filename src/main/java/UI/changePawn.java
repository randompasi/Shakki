package UI;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import javafx.geometry.*;
public class changePawn {
	private static int palautus;
	private static Stage window; 
	
	public static int display(){
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Chess Titan");
		window.setMinWidth(250);
		Label label = new Label();
		label.setText("Choose piece");
		Button queen = new Button("queen");
				queen.setOnAction(e -> queen());
			Button bishop = new Button("bishop");
				bishop.setOnAction(e -> bishop());
			Button knight = new Button("knight");
				knight.setOnAction(e -> knight());
			Button rook = new Button("rook");
				rook.setOnAction(e -> rook());
		
				
				VBox layout = new VBox(10);
				layout.getChildren().addAll(label, queen, knight, bishop, rook);
				layout.setAlignment(Pos.CENTER);
				window.setScene(new Scene(layout));
				window.showAndWait();
				return palautus;
				
	}
	
	private static void queen(){
		palautus = 1;
		window.close();
	}
	private static void bishop(){
		palautus = 2;
		window.close();
	}
	private static void knight(){
		palautus = 3;
		window.close();
	}
	private static void rook(){
		palautus = 4;
		window.close();
	}
}
