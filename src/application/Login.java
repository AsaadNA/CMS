package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Login extends Application {
	
	private static Stage stage;
	public static Stage getStage() { return stage;}  

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
        loginScene.setCursor(Cursor.HAND);
		primaryStage.show();
	}

}
