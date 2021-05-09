package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class CashierController implements Initializable {

	@FXML 
	BorderPane borderPane;
	@FXML
	ImageView closeButton;
	@FXML
	Label usernameLabel_cashier;
	
	private double xOffset = 0 , yOffset = 0; //For borderless window movment
	
	//On close click
	public void onCloseClick(MouseEvent e) {
		Platform.exit();
		System.exit(1);
	}
	
	//This is the book tickets sections
	//this will load the first section of the book tickets
	//other sections are opened chronologically witin each controller
	public void onBookTicketsClick(ActionEvent e) throws IOException {
		FXMLLoader fxmlLoader = null;
		fxmlLoader = new FXMLLoader(getClass().getResource("bookTickets_1.fxml"));
		bookTickets_1Controller controller = new bookTickets_1Controller();
		fxmlLoader.setController(controller); 
		controller.ccInst = this; //this should come first before setting borderpane
		borderPane.setCenter(fxmlLoader.load());
	}

	//On logout click
	public void onLogoutClick(ActionEvent e) {
		Login.getStage().show();
		Cashier.getStage().hide();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//Setting the username label
		usernameLabel_cashier.setText(LoginController.getUser());
		
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
