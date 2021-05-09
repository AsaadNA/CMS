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
import javafx.scene.layout.Pane;

public class MainController implements Initializable {
	
	@FXML 
	BorderPane borderPane;
	@FXML
	ImageView closeButton;
	@FXML
	Label usernameLabel;
	
	private double xOffset = 0 , yOffset = 0; //For borderless window movment
	
	//This handles the close button
	public void onCloseClick(MouseEvent e) {
		Platform.exit();
		System.exit(1);
	}
	
	//Helper function to load pane fxml
	public Pane getPane(String path) throws IOException {
		Pane result = FXMLLoader.load(getClass().getResource(path));
		return result;
	}
	
	//This will handle the Manageblocks Button
	public void onManageBlocksClick(ActionEvent e) {
		Pane manageBlocks = null;
		try {
			manageBlocks = getPane("ManageBlocks.fxml");
		} catch (IOException e1) {
			e1.printStackTrace();
		} borderPane.setCenter(manageBlocks);
	}
	
	public void onManageMoviesClick(ActionEvent e) {
		Pane manageMovies = null;
		try {
			manageMovies = getPane("ManageMovies.fxml");
		} catch (IOException e1) {
			e1.printStackTrace();
		} borderPane.setCenter(manageMovies);
	}

	public void onLogoutClick(ActionEvent e) {
		Login.getStage().show();
		Main.getStage().hide();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//Setting the username label here
		usernameLabel.setText(LoginController.getUser());
		
		/*
		 *  These pieces of code basically some up the movement of
		 *  the borderless window
		 */
		borderPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				xOffset = Main.getStage().getX() - e.getScreenX();
                yOffset = Main.getStage().getY() - e.getScreenY();
			}
			
		});	
		
		borderPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	Main.getStage().setX(event.getScreenX() + xOffset);
            	Main.getStage().setY(event.getScreenY() + yOffset);
            }
        });
	}
}
