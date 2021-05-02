package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//TODO: Instead of creating a new window for the add blocks etc why dont we just change the scene and add a cancel button to it
//TODO: ADD an AM and PM Column in TimeStamp table

public class ManageBlocksController implements Initializable {
	
	@FXML
	ToggleButton removeToggle;
	@FXML
	ToggleButton updatseToggle;
	@FXML 
	GridPane gridPane;
	
	private ArrayList<BlockDS> data = new ArrayList<BlockDS>();
		
	//This will return the remove toggle status to BLOCK_BUTTONS
	public boolean getRemoveToggleStatus() { return removeToggle.isSelected();}
	//This will return the update toggle status to BLOCK_BUTTONS
	public boolean getUpdateToggleStatus() { return updatseToggle.isSelected();}
	
	//Highlights toggled button .. depending on the source of the event
	public void onToggleClick(ActionEvent e) {
		ToggleButton toggledButton = ((ToggleButton)e.getSource());
		if(toggledButton.isSelected()) {
			toggledButton.setStyle("-fx-background-color: green");
		} else {
			toggledButton.setStyle("-fx-background-color: #303030");
		}
	}
	
	public void loadGridPane() throws SQLException {	
		
		gridPane.getChildren().clear();
		data.clear();
		
		SQLController sql = LoginController.getSQL();
		String query1 = "select block from movieblock order by block";
		ResultSet result1 = sql.executeQuery(query1);
		
		while(result1.next()) {
			
			ArrayList<MovieDS> movieList = new ArrayList<MovieDS>();
			BlockDS newData = new BlockDS();
			String query2 = "select movietitle,movierating,movieduration,timestamp from movie,movieblock,timestamps,movieblocktime where movieblocktime.blockid = movieblock.blockid and movieblocktime.timestampid = timestamps.timestampid and movieblocktime.movieid = movie.movieid and movieblock.block ="+"'" + result1.getString(1) + "' order by timestamp";
			ResultSet result2 = sql.executeQuery(query2);
			while(result2.next()) {
				
				String movieTitle = result2.getString(1);
				float movieRating = result2.getFloat(2);
				int movieduration = result2.getInt(3);
				int timestamp = result2.getInt(4);
				
				movieList.add(new MovieDS(movieTitle,movieRating,movieduration,timestamp));
			} 
			
			newData.block = result1.getString(1);
			newData.scheduledMovies = movieList;
			newData.pane = gridPane;
			data.add(newData);
		}
				
		int r = 0 , c = 0;
		for(int i = 0; i <= data.size()-1; i++) {
			if(c <= 4) {
				gridPane.add(new BlockButtons(data.get(i),this), c, r);
				c++;
			} if (c==4) { 
				c = 0; 
				r++; 
			}
		} 
	}
	
	//Adding shit
	public void onAddClick(ActionEvent e) throws SQLException {
		try {
			//Passing our stageInstnace to our controller
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddBlocks.fxml"));
			AddBlocksController controller = new AddBlocksController();
			fxmlLoader.setController(controller);
		    Parent root1 = (Parent) fxmlLoader.load();
		    Stage stage = new Stage();
		    stage.setScene(new Scene(root1));
		    
		    controller.stageInstance = stage; //Passing our instance to the controller
		    controller.mbcInst = this; //Passing our controller instance
		    
		    //This allows only one instance to be created until the current is not closed
		    stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
		    stage.show();	
		} catch (IOException e1) {
			System.out.println("[ERROR @ Update Blocks: (loading update blocks fxml] " + e1);
		}
	}
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		try {
			loadGridPane();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}