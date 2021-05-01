package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Login extends Application {
	
	public static Stage stage;
		
	public static void main(String args[]) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {	
		Login.stage = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene loginScene = new Scene(root);
		primaryStage.setScene(loginScene);
		primaryStage.setTitle("CMS");
		primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);        
		primaryStage.show();
	}

}
