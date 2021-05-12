package application;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

class MovieDS {
	
	String movieTitle;
	float movieRating;
	int movieDuration;
	
	String moviedatetime;
	
	public MovieDS(String movieTitle,float movieRating,int movieDuration,String moviedatetime) {
		this.movieTitle = movieTitle;
		this.movieRating = movieRating;
		this.movieDuration = movieDuration;
		this.moviedatetime = moviedatetime;
	}
}

class BlockDS {
	
	public String block;
	public ArrayList<MovieDS> scheduledMovies = new ArrayList<MovieDS>();
	public GridPane pane;
	
	public BlockDS() {}
	public BlockDS(GridPane pane,String block,ArrayList<MovieDS> scheduledMovies) {
		this.pane = pane;
		this.block = block;
		this.scheduledMovies = scheduledMovies;
	}
}

public class BlockButtons extends Button {
	
	public BlockButtons(BlockDS info , ManageBlocksController mbcInst) {
		
		//Some properties of the button
		this.setText(info.block);
	    this.setMaxWidth(Double.MAX_VALUE);
	    this.setMaxHeight(Double.MAX_VALUE);
		this.setFont(new Font(45));
		this.setTextFill(Color.WHITE);
		this.setStyle("fx-background-radius: 15px; -fx-background-color:#303030;");
		
		//generating the custom tool-tip
		Tooltip tt = new Tooltip();
		
		String toolTipText = "Block: " + info.block + "\n";
		for(int i = 0; i <= info.scheduledMovies.size()-1; i++) {
			toolTipText += "------ " + info.scheduledMovies.get(i).moviedatetime + " ------\n";
			toolTipText += "Movie " + " : " + info.scheduledMovies.get(i).movieTitle + "\n";
		}
		
		tt.setText(toolTipText);
		tt.setStyle("-fx-font: normal bold 17 Langdon; "
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
					LoginController.getSQL().executeQuery("DELETE FROM movieblock where block='"+info.block+"'");
					//Reloading the gridPane with updated shit
					try {
						mbcInst.loadGridPane();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} if (mbcInst.getUpdateToggleStatus()) {
					//Create a new instantiation of update blocks to give info
					@SuppressWarnings("unused")
					UpdateBlocks ubInst = new UpdateBlocks(info,mbcInst);
				}
			} 
		});
	}
}
