package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;

public class UpdateBlocksController implements Initializable {

	@FXML
	TextField blocksField;
	@FXML
	TextField movieField;
	@FXML
	AnchorPane anchor;
	@FXML
	Button updateButton;
	
	private double xOffset = 0 , yOffset = 0; //For borderless window movment
	
	//Here we handle update window closing
	public void onUpdateClick(ActionEvent e) {
		
		UpdateBlocks.getStage().close();
		
		//HANDLE THE SQL STUFF HERE
		System.out.println("Block: " + blocksField.getText());
		System.out.println("Movie: " + movieField.getText());
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		blocksField.setText(UpdateBlocks.getDataStructure().block);
		movieField.setText(UpdateBlocks.getDataStructure().movie_name);
			
		
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
