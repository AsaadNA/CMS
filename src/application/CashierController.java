package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
	
	//This is the book tickets sections
	//this will load the first section of the book tickets
	//other sections are opened chronologically witin each controller
	public void onBookTicketsClick(ActionEvent e) throws IOException {
		FXMLLoader fxmlLoader = null;
		fxmlLoader = new FXMLLoader(getClass().getResource("bookTickets_1.fxml"));
		bookTickets_1Controller controller = new bookTickets_1Controller();
		fxmlLoader.setController(controller); 
		borderPane.setCenter(fxmlLoader.load());
		controller.ccInst = this;
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
