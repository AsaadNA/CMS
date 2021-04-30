package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

public class ManageBlocksController implements Initializable {

	//TODO: Dynamic gridPane with scrollPane
	
	@FXML
	GridPane gridPane;
	@FXML
	ToggleButton removeToggle;
	@FXML
	ToggleButton updatseToggle;
	
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
	
	//TODO: Implement Add
	public void onAddClick(ActionEvent e) {
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
				
		//This is basically intializing the gridPane with the blocks
		String data[] = {"A","B","C","D","E","F","G","H","I","J","K","L"};
		int r = 0 , c = 0;
		for(int i = 0; i <= data.length-1; i++) {
			if(c <= 4) { //inserting buttons to our gridPane with our data
				gridPane.add(new BlockButtons(new BlockDS(gridPane,data[i],"spiderman",90,0),this), c, r);
				c++;
			} if (c==4) { 
				c = 0; 
				r++; 
			}
		}
	}
}
