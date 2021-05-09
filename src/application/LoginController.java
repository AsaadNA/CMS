package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
				userField.setText(""); passwordField.setText("");
			} else {
				user = userField.getText();
				System.out.println("[LOG @ LoginController] CASHIER : Login successful");			
	            ((Node)(e.getSource())).getScene().getWindow().hide();
				@SuppressWarnings("unused")
				Cashier cashier = new Cashier();
			}
			
		} else { 
			user = userField.getText();
			System.out.println("[LOG @ LoginController] ADMIN : Login successful");		
            ((Node)(e.getSource())).getScene().getWindow().hide();
			@SuppressWarnings("unused")
			Main main = new Main();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		sqlController = new SQLController(); //Intialzing sql connection
		
		/*
		 *  These pieces of code basically some up the movement of
		 *  the borderless window
		 */
		
		pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				xOffset = Login.stage.getX() - e.getScreenX();
                yOffset = Login.stage.getY() - e.getScreenY();
			}
			
		});
		
		pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	Login.stage.setX(event.getScreenX() + xOffset);
            	Login.stage.setY(event.getScreenY() + yOffset);
            }
        });
	}
	
}
