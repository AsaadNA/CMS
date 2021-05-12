package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

public class LoginController implements Initializable {
	
	private static SQLController sqlController;
	
	@FXML
	TextField userField;
	@FXML
	PasswordField passwordField;
	@FXML 
	AnchorPane pane;
	
	private double xOffset = 0 , yOffset = 0; //Borderless window
	
	private static String user = null;
	
	public static Scene loginScene;
	
	public static String getUser() { return user; }
	public static SQLController getSQL() { return sqlController; }
	
	//This is the login button controller
	//which will instantitate the main stage if the credentials are correct
	public void onLoginClick(ActionEvent e) throws SQLException {
		String query = "SELECT * from admin where username=" + "'" + userField.getText() + "' and password='" + passwordField.getText() + "'"; 
		ResultSet result = sqlController.executeQuery(query);
		if(!result.next()) {
			query = "SELECT * from CASHIER where cashierusername=" + "'" + userField.getText() + "' and cashierpassword='" + passwordField.getText() + "'"; 
			result = sqlController.executeQuery(query);
			if(!result.next()) {
				Alert alert = new Alert(AlertType.ERROR,"Invalid username or password");
				alert.showAndWait();
			} else {
				user = userField.getText();
				System.out.println("[LOG @ LoginController] CASHIER : Login successful");			
				Login.getStage().hide();				
				Cashier cashier = new Cashier();
			}
			
		} else { 
			user = userField.getText();
			System.out.println("[LOG @ LoginController] ADMIN : Login successful");		
            //((Node)(e.getSource())).getScene().getWindow().hide();
			Login.getStage().hide();
			Main main = new Main();
		}
	}

	public void onCloseClick(MouseEvent e) {
		Platform.exit();
		System.exit(1);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		sqlController = new SQLController(); //Intialzing sql connection once
		
		/*
		 *  These pieces of code basically some up the movement of
		 *  the borderless window
		 */
		
		pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				xOffset = Login.getStage().getX() - e.getScreenX();
                yOffset = Login.getStage().getY() - e.getScreenY();
			}
			
		});
		
		pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	Login.getStage().setX(event.getScreenX() + xOffset);
            	Login.getStage().setY(event.getScreenY() + yOffset);
            }
        });
	}
	
}
