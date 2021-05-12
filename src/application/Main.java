package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//1. Shifted over to DATETIME instead of INTEGER TIMESLOT
//2. TimeStamp log during payment
//3. After purchase it goes back to display all the booking
//4. [Bug fix] double info message displayed
//5. Manage Payments Implemented
//6. Refunding Payments Implemented
//7. Search payments by phone number implemented
//8. Search movie seats by movies in bookTickets_1 implemented
//9. Generate QR code and save path to ticket 
//10. Generate and save ticket to localfile with the qr code attached

public class Main {

	private static Stage stage;
	
	public static Stage getStage() { return stage;}  
	
	public static void close() {
		stage.close();
	}
	
	//Instantiating the stage
	public Main() {
		
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		} catch (Exception e) {
			System.out.println("[ERROR @ Main (loading FXML)] : " + e);
		}
		
		Scene mainScene = new Scene(root);
        mainScene.setCursor(Cursor.HAND);
		stage = new Stage();
		stage.setScene(mainScene);
		stage.setTitle("CMS");
		stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);        
		stage.show();
	}
}
