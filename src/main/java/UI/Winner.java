package UI;
import javafx.stage.Modality;

import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class Winner {
	

		
		public static void display(String voittaja){
			Stage window = new Stage();
			window.initModality(Modality.APPLICATION_MODAL);
			window.setTitle("Chess Titan");
			window.setMinWidth(250);
			window.setMinHeight(100);
			Label label = new Label();
			label.setText("Shakkimatti"+ "  "+voittaja+ " ovat voittaneet ");
			
			
					
					VBox layout = new VBox(100);
					layout.getChildren().addAll(label);
					layout.setAlignment(Pos.CENTER);
					window.setScene(new Scene(layout));
					window.showAndWait();
		}
	}
