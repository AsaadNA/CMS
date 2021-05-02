package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddBlocksController implements Initializable {
	
	@FXML
	AnchorPane pane;
	@FXML
	ComboBox<String> blockBox;
	@FXML
	ComboBox<String> timeBox;
	@FXML 
	ComboBox<String> movieBox;
	@FXML
	Button addSlot;
	@FXML
	Button doneButton;
	
	private HashMap<String,Integer> block_hashMap = new HashMap<String,Integer>(); //block, blockid
	private HashMap<String,Integer> movie_hashMap = new HashMap<String,Integer>(); //movietitle,movieid
	private HashMap<Integer,Integer> timestamp_hashMap = new HashMap<Integer,Integer>(); //timestamp,timestampID

	ArrayList<String> resultListTIME = new ArrayList<String>();

	//Our passed instances
	public Stage stageInstance; //These are passed while instantiating the ADD BLOCK CONTROLLER
	public ManageBlocksController mbcInst; //These are passed while instantiating the ADD BLOCK CONTROLLER
	
	//When done is clicked .. it will reload the girdPane for the updated data
	public void onDoneClick(ActionEvent e) {
		//Reload the grid using the passed instance
		try {
			mbcInst.loadGridPane();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		stageInstance.close(); //close the Add blocks stage
	}
	
	//This will update time slot combo box with unslotted time for a specific block from the movieblocktime table
	private void updateTimeComboBox(String block) throws SQLException {
		String query = "select timestamp from movie,movieblock,timestamps,movieblocktime where movieblocktime.blockid = movieblock.blockid and movieblocktime.timestampid = timestamps.timestampid and movieblocktime.movieid = movie.movieid and movieblock.block ="+"'" + block + "'";
		ResultSet result = LoginController.getSQL().executeQuery(query);
		while(result.next()) {
			resultListTIME.remove(Integer.toString(result.getInt(1)));
		} timeBox.setItems(FXCollections.observableArrayList(resultListTIME));
	}
	
	public void onAddSlotClick(ActionEvent e) throws SQLException {	
		
		String time = timeBox.getValue().toString();
		String movie = movieBox.getValue().toString();
		String block = blockBox.getValue().toString();
		
		Integer timeID = timestamp_hashMap.get(Integer.parseInt(time));
		Integer movieID = movie_hashMap.get(movie);
		Integer blockID = block_hashMap.get(block);
		
		ResultSet result = LoginController.getSQL().executeQuery("SELECT block from MOVIEBLOCK where block='"+block+"'");
		if(!result.next()) {
			
			//1. Insert new block to the movieblock table
			LoginController.getSQL().executeQuery("INSERT INTO MOVIEBLOCK (block) values ('"+block+"')");
			
			//2. Retrieve the id of the block
			result = LoginController.getSQL().executeQuery("SELECT blockid from MOVIEBLOCK where block='"+block+"'");
			while(result.next()) { blockID = result.getInt(1); }
			
			//2.5 put the key,value pair to block
			block_hashMap.put(block, blockID);
					
			//3. Insert the slot given into the table
			LoginController.getSQL().executeQuery("INSERT INTO MOVIEBLOCKTIME (timeStampID,blockID,movieID) VALUES ("+ timeID + ","+blockID+","+movieID+")");
			
			//4. Update the time combo box
			updateTimeComboBox(block);
					
			blockBox.setDisable(true);
			
		} else {
			//3. Insert the slot given into the table
			LoginController.getSQL().executeQuery("INSERT INTO MOVIEBLOCKTIME (timeStampID,blockID,movieID) VALUES ("+ timeID + ","+blockID+","+movieID+")");
			//4. Update the time combo box
			updateTimeComboBox(block);
		}
	}
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		ResultSet result;
				
		ArrayList<String> resultList = new ArrayList<String>();
		resultList.add("A");
		resultList.add("B");
		resultList.add("C");
		resultList.add("D");
		resultList.add("E");
		resultList.add("F");
		resultList.add("G");
		resultList.add("H");
		resultList.add("J");
		resultList.add("K");
		resultList.add("L");
		
		/* POPULATING BLOCKS WHICH ARE NOT USED ALREADY */
		
		result = LoginController.getSQL().executeQuery("SELECT blockid, block from MOVIEBLOCK");
		try {
			while(result.next()) {
				resultList.remove(result.getString(2));
				block_hashMap.put(result.getString(2),result.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}  blockBox.setItems(FXCollections.observableArrayList(resultList));
		
		/* POPULATING TIME */
		
		result = LoginController.getSQL().executeQuery("SELECT timestampid, timestamp from TIMESTAMPS");
		try {
			while(result.next()) {
				resultListTIME.add(Integer.toString(result.getInt(2)));
				timestamp_hashMap.put(result.getInt(2),result.getInt(1));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} timeBox.setItems(FXCollections.observableArrayList(resultListTIME));
		
		/* POPULATING MOVIES */
		
		result = LoginController.getSQL().executeQuery("SELECT movieid,movietitle from MOVIE");
		resultList.clear(); //clear the old junk
		try {
			while(result.next()) {
				resultList.add(result.getString(2));
				movie_hashMap.put(result.getString(2),result.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} movieBox.setItems(FXCollections.observableArrayList(resultList));
		
	}
}