package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class UpdateBlocksController implements Initializable {

	@FXML
	AnchorPane anchor;
	@FXML
	Label blockLabel;
	@FXML
	ComboBox timeBox_addSlot;
	@FXML
	ComboBox movieBox_addSlot;
	@FXML
	ComboBox timeBox_otherSlot;
	@FXML 
	ComboBox movieBox_otherSlot;
	
	private String block;
	private double xOffset = 0 , yOffset = 0; //For borderless window movment	
		
	private HashMap<String,Integer> movie_hashMap = new HashMap<String,Integer>(); //movietitle,movieid
	private HashMap<String,Integer> timestamp_hashMap = new HashMap<String,Integer>(); //timestamp,timestampID
	
	public void onAddSlotClick(ActionEvent e) throws SQLException {
		
		String time = timeBox_addSlot.getValue().toString();
		String movie = movieBox_addSlot.getValue().toString();
		
		Integer timeID = timestamp_hashMap.get(time);
		Integer movieID = movie_hashMap.get(movie);
		Integer blockID = 0;
		
		//0. get block id
		ResultSet result = LoginController.getSQL().executeQuery("SELECT blockid from MOVIEBLOCK where block='"+block+"'");
		while(result.next()) { blockID = result.getInt(1); }
		
		//1. Insert the slot given into the table
		LoginController.getSQL().executeQuery("INSERT INTO MOVIEBLOCKTIME (timeStampID,blockID,movieID) VALUES ("+ timeID + ","+blockID+","+movieID+")");
		
		//2. Update the time combo boxs
		updateTimeBox_ADD(block);
		updateTimeBox_OTHER(block);
	}
	
	public void onDeleteSlotClick(ActionEvent e) throws SQLException {
		String time = timeBox_otherSlot.getValue().toString();
		Integer timeID = timestamp_hashMap.get(time);
		Integer blockID = 0;
		
		//0. get block id
		ResultSet result = LoginController.getSQL().executeQuery("SELECT blockid from MOVIEBLOCK where block='"+block+"'");
		while(result.next()) { blockID = result.getInt(1); }
		
		LoginController.getSQL().executeQuery("DELETE FROM MOVIEBLOCKTIME where blockID="+blockID+" and timestampid="+timeID);
		
		//2. Update the time combo boxs
		updateTimeBox_ADD(block);
		updateTimeBox_OTHER(block);
	}
	
	private void updateTimeBox_ADD(String block) {
		
		//1. Getting our all timestamps list
		ArrayList<String> timeSlotList = new ArrayList<String>();
		
		ResultSet result = LoginController.getSQL().executeQuery("SELECT timestampid," + "to_char(timestampdatetime," + "'" + "yyyy/mm/dd hh12:mi')" +  " from TIMESTAMPS");
		try {
			while(result.next()) {
				timestamp_hashMap.put(result.getString(2),result.getInt(1));
				timeSlotList.add(result.getString(2));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		//2. Updating timeBox of add slot
		String query = "select " + "to_char(timestampdatetime," + "'" + "yyyy/mm/dd hh12:mi')" + " from movie,movieblock,timestamps,movieblocktime where movieblocktime.blockid = movieblock.blockid and movieblocktime.timestampid = timestamps.timestampid and movieblocktime.movieid = movie.movieid and movieblock.block ="+"'" + block + "'";

		result = LoginController.getSQL().executeQuery(query);
		try {
			while(result.next()) {
				timeSlotList.remove(result.getString(1));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} timeBox_addSlot.setItems(FXCollections.observableArrayList(timeSlotList));
	}
	
	private void updateTimeBox_OTHER (String block) {
		ArrayList<String> tempList = new ArrayList<String>();
		String query = "select " + "to_char(timestampdatetime," + "'" + "yyyy/mm/dd hh12:mi')" + " from movie,movieblock,timestamps,movieblocktime where movieblocktime.blockid = movieblock.blockid and movieblocktime.timestampid = timestamps.timestampid and movieblocktime.movieid = movie.movieid and movieblock.block ="+"'" + block + "'";
		ResultSet result = LoginController.getSQL().executeQuery(query);
		try {
			while(result.next()) { tempList.add(result.getString(1)); }
		} catch(SQLException e) {
			e.printStackTrace();
		} timeBox_otherSlot.setItems(FXCollections.observableArrayList(tempList));
	}
	
	public void onUpdateMovieClick(ActionEvent e) throws SQLException {
		String time = timeBox_otherSlot.getValue().toString();
		String movie = movieBox_otherSlot.getValue().toString();
		
		Integer timeID = timestamp_hashMap.get(time);
		Integer movieID = movie_hashMap.get(movie);
		Integer blockID = 0;
		
		//0. get block id
		ResultSet result = LoginController.getSQL().executeQuery("SELECT blockid from MOVIEBLOCK where block='"+block+"'");
		while(result.next()) { blockID = result.getInt(1); }
		
		LoginController.getSQL().executeQuery("UPDATE MOVIEBLOCKTIME SET movieid="+movieID+" where timestampid="+timeID + " and blockid="+blockID);
	}
	
	//Here we handle update window closing
	public void onDoneClick(ActionEvent e) {	
		UpdateBlocks.refreshGridPane();
		UpdateBlocks.getStage().close();
	}
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		block = UpdateBlocks.getDataStructure().block;
		blockLabel.setText(block);
		
		updateTimeBox_ADD(block);
		updateTimeBox_OTHER(block);
		
		
							/* POPULATING MOVIES BOTH */
		
		ArrayList<String> resultList = new ArrayList<String>();
		ResultSet result = LoginController.getSQL().executeQuery("SELECT movieid,movietitle from MOVIE");
		resultList.clear(); //clear the old junk
		try {
			while(result.next()) {
				resultList.add(result.getString(2));
				movie_hashMap.put(result.getString(2),result.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		movieBox_addSlot.setItems(FXCollections.observableArrayList(resultList));
		movieBox_otherSlot.setItems(FXCollections.observableArrayList(resultList));
			
		///////////////////////////////////////////////////////////////////////////
		
		/*
		 *  These pieces of code basically some up the movement of
		 *  the borderless window
		 */
		
		anchor.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				xOffset = UpdateBlocks.getStage().getX() - e.getScreenX();
                yOffset = UpdateBlocks.getStage().getY() - e.getScreenY();
			}
			
		});	
		
		anchor.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	UpdateBlocks.getStage().setX(event.getScreenX() + xOffset);
            	UpdateBlocks.getStage().setY(event.getScreenY() + yOffset);
            }
        });
	}	
}
