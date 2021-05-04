package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class CashierController implements Initializable {

	@FXML 
	BorderPane borderPane;
	
	private double xOffset = 0 , yOffset = 0; //For borderless window movment

	
	//Helper function to load pane fxml
		public Pane getPane(String path) throws IOException {
			Pane result = FXMLLoader.load(getClass().getResource(path));
			return result;
		}
		
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			
			/*
			 *  These pieces of code basically some up the movement of
			 *  the borderless window
			 */
			borderPane.setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent e) {
					xOffset = Cashier.getStage().getX() - e.getScreenX();
	                yOffset = Cashier.getStage().getY() - e.getScreenY();
				}
				
			});	
			
			borderPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	            	Cashier.getStage().setX(event.getScreenX() + xOffset);
	            	Cashier.getStage().setY(event.getScreenY() + yOffset);
	            }
	        });
		}
	
}
