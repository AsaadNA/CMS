package application;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class BlockButtons extends Button {
	
	public BlockButtons(BlockDS info , ManageBlocksController mbcInst) {
		
		//Some properties of the button
		this.setText(info.block);
	    this.setMaxWidth(Double.MAX_VALUE);
	    this.setMaxHeight(Double.MAX_VALUE);
		//this.setMaxSize(180, 151);
		this.setFont(new Font(57));
		this.setTextFill(Color.WHITE);
		this.setStyle("fx-background-radius: 15px; -fx-background-color:#303030;");
		
		//generating the custom tool-tip
		Tooltip tt = new Tooltip();
		tt.setText("Block: " + getText() + "\n" + "Movie: " + "SpiderMan: Homecoming\nDuration: 90 mins\nRating: 9.0");
		tt.setStyle("-fx-font: normal bold 20 Langdon; "
		    + "-fx-background-color: #292929; "
		    + "-fx-text-fill: white;");

		this.setTooltip(tt);
		tt.setShowDelay(Duration.seconds(0));
		
		//This handles what to do with the buttons
		//depending on remove toggle or updateToggle
		//it has the ManagerBlocksController's Instance to 
		//check status of the toggles
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if(mbcInst.getRemoveToggleStatus()) {
					info.pane.getChildren().remove((Button)e.getSource());
				} if (mbcInst.getUpdateToggleStatus()) {
					//Create a new instantiation of update blocks to give info
					UpdateBlocks ubInst = new UpdateBlocks(info);
				}
			} 
		});
	}
}
