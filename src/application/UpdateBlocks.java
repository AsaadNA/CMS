package application;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UpdateBlocks {
		
	private static Stage stage;
	private static BlockDS updateBlockDS; //access for controller
	
	private static ManageBlocksController mbcInst;
	
	public static Stage getStage() { return stage; }
	public static BlockDS getDataStructure() { return updateBlockDS; }
	
	public static void refreshGridPane() {
		try {
			mbcInst.loadGridPane();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public UpdateBlocks(BlockDS ds , ManageBlocksController mbcInst) {
		
		UpdateBlocks.mbcInst = mbcInst;
		updateBlockDS = ds; //takes in info
		
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UpdateBlocks.fxml"));
		    Parent root1 = (Parent) fxmlLoader.load();
		    stage = new Stage();
		    stage.setScene(new Scene(root1));
		    //This allows only one instance to be created until the current is not closed
		    stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
		    stage.show();
		} catch (IOException e1) {
			System.out.println("[ERROR @ Update Blocks: (loading update blocks fxml] " + e1);
		}			
	}
}
