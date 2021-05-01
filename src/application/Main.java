package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main {

	private static Stage stage;
	
	public static Stage getStage() { return stage;}  
	
	//Instantiating the stage
	public Main() {
				
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		} catch (IOException e) {
			System.out.println("[ERROR @ Main (loading FXML)] : " + e);
		}
		
		Scene mainScene = new Scene(root);
		stage = new Stage();
		stage.setScene(mainScene);
		stage.setTitle("CMS");
		stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);        
		stage.show();
	}
}
