package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Cashier {

	private static Stage stage;
	
	public static Stage getStage() { return stage;}  
	
	//Instantiating the stage
	public Cashier() {
				
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("CashierMain.fxml"));
		} catch (Exception e) {
			System.out.println("[ERROR @ CASHIER (loading FXML)] : " + e);
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
