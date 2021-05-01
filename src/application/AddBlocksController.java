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
	ComboBox<String> movieBox;
	@FXML
	ComboBox<String> blockBox;
	@FXML
	Button addButton;
	
	private HashMap<String,Integer> movie_hashMap = new HashMap<String,Integer>();
	
	//Our passed instances
	public Stage stageInstance;
	public ManageBlocksController mbcInst;
	
	public void onAddClick(ActionEvent e) {
		String block = blockBox.getValue().toString();
		String movie = movieBox.getValue().toString();
		
		int movieid = movie_hashMap.get(movie); 
		LoginController.getSQL().executeQuery("INSERT INTO MOVIEBLOCK (movieid,block) values "
					+ "(" + movieid + ",'" + block + "')");
		
		//Reload the grid using the passed instance
		try {
			mbcInst.loadGridPane();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		stageInstance.close(); //close the Add blocks stage
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
		
		result = LoginController.getSQL().executeQuery("SELECT block from MOVIEBLOCK");
		try {
			while(result.next()) {
				resultList.remove(result.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}  blockBox.setItems(FXCollections.observableArrayList(resultList));
		
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