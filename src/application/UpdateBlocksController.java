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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class UpdateBlocksController implements Initializable {

	@FXML
	ComboBox<String> movieBox;
	@FXML
	AnchorPane anchor;
	@FXML
	Button updateButton;
	
	private double xOffset = 0 , yOffset = 0; //For borderless window movment
		
	private HashMap<String,Integer> movie_hashMap = new HashMap<String,Integer>();

	//Here we handle update window closing
	public void onUpdateClick(ActionEvent e) {
		
		//String block = UpdateBlocks.getDataStructure().block;
		//String movie = movieBox.getValue().toString();
		//int movieid = movie_hashMap.get(movie);
		
		//LoginController.getSQL().executeQuery("UPDATE movieblock set movieid="+movieid+" where block='"+block+"'");
		
		//refresh the gridpane
		UpdateBlocks.refreshGridPane();
		UpdateBlocks.getStage().close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		ResultSet result = LoginController.getSQL().executeQuery("SELECT movietitle,movieid from MOVIE");
		ArrayList<String> resultList = new ArrayList<String>(); //clear the old junk
		try {
			while(result.next()) {
				resultList.add(result.getString(1));
				movie_hashMap.put(result.getString(1),result.getInt(2));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} movieBox.setItems(FXCollections.observableArrayList(resultList));
		
		/*
		 *  These pieces of code basically some up the movement of
		 *  the borderless window
		 */
		
		//TODO: Clean up redundant borderless window code
		//TODO: Connect manage movies with database
		
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
