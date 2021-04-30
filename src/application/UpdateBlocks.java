package application;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

//THIS IS AN ADT TO CONVEY THE INFO TO THE 
//CONTROLLER OF UPDATE BLOCKS
//This DATA STRUCTURE USED FOR BLOCK BUTTONS AND ALSO FOR GIVING THE INFO TO UPDATE BLOCKS WINDOW
class BlockDS {
	
	public String block;
	public String movie_name;
	public int capacity;
	public int time;
	
	public GridPane pane;
	
	public BlockDS(GridPane pane,String block , String movie_name , int capacity , int time) {
		this.block = block;
		this.movie_name = movie_name;
		this.capacity = capacity;
		this.time = time;
		this.pane = pane;
	}
}

public class UpdateBlocks {
		
	private static Stage stage;
	private static BlockDS updateBlockDS; //access for controller
	
	public static Stage getStage() { return stage; }
	public static BlockDS getDataStructure() { return updateBlockDS; }
	
	public UpdateBlocks(BlockDS ds) {
		
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
