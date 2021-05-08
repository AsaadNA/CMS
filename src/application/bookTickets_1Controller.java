package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *  
 * @author Asaad
 *
 *	Now this view will basically display all the movieblocktime so we can reserve the desired seats of the desired
 *	MovieID , BlockID , TimeSlotID
 */

class CustomComponentBookTicketOne extends AnchorPane {
	
	public String block,movietitle;
	public int timestamp,seatsLeft;	
	private Label seatsLeftLabel;
	
	public void setSeatsLeftLabel(int seatsLeft) {
		this.seatsLeftLabel.setText("Seats Left: " + Integer.toString(seatsLeft));
	}
	
	private Label createLabel(String text , int x , int y) {
		Label result = new Label(text);
		result.setTextFill(Color.WHITE);
		result.setFont(new Font("System",24));
		result.setLayoutX(x);
		result.setLayoutY(y);
		
		return result;
	}
	
	public CustomComponentBookTicketOne(CashierController ccInst,String block , String movietitle , int timestamp , int seatsLeft) {
		this.block = block;
		this.movietitle = movietitle;
		this.timestamp = timestamp;
		this.seatsLeft = seatsLeft;
				
		setStyle("-fx-background-color: #303030");
		setPrefSize(450, 150);
		
		Label blockLabel = createLabel("Block : " + block,42,26);
		Label movieLabel = createLabel("Movie : " + movietitle,42,31+28);
		Label timeslotLabel = createLabel("Timeslot : " + Integer.toString(timestamp),42,26+28+5+30+3);
		seatsLeftLabel = createLabel("Seats Left : " + Integer.toString(seatsLeft),200,26+28+5+30+3);
		
		Button button = new Button("Purchase Tickets");
		button.setStyle("-fx-background-color: #88e327");
		button.setFont(new Font("System",17));
		button.setLayoutX(539);
		button.setLayoutY(62);
		
		button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				bookTickets_2Controller controller = new bookTickets_2Controller();
	    		
	        	//Passing these variable to other scene via the controller;
	    		controller.block = block;
	    		controller.movie = movietitle;
	    		controller.timeslot = timestamp;	    		
	            
	            FXMLLoader fxmlLoader = null;
	    		fxmlLoader = new FXMLLoader(getClass().getResource("bookTickets_2.fxml"));
	    		fxmlLoader.setController(controller); 
	    		try {
					ccInst.borderPane.setCenter(fxmlLoader.load());
				} catch (IOException e1) {
					System.out.println("Couuld not load fxml BOOK TICKETS_1 Controller");
					e1.printStackTrace();
				}	
			}
			
		});
		
		getChildren().add(blockLabel);
		getChildren().add(movieLabel);
		getChildren().add(timeslotLabel);
		getChildren().add(seatsLeftLabel);
		getChildren().add(button);
	}
}

public class bookTickets_1Controller implements Initializable {

	@FXML
	AnchorPane anchorPane;
	
	public CashierController ccInst; //instance pass via manually in CashierController
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		VBox content = new VBox(5);
		content.setStyle("-fx-background-color: #292929");
		content.setSpacing(10);
		ScrollPane scroller = new ScrollPane(content);
		scroller.setFitToWidth(true);
		scroller.setLayoutX(66);
		scroller.setLayoutY(38);
		scroller.setStyle("-fx-background-color: #292929");
		scroller.setPrefSize(768, 513);
		scroller.setVbarPolicy(ScrollBarPolicy.NEVER);

		// Load the movieblocktime data
		ResultSet result = LoginController.getSQL().executeQuery(
				"select block,movietitle,movierating,movieduration,timestamp from movie,movieblock,timestamps,movieblocktime where movieblocktime.blockid = movieblock.blockid and movieblocktime.timestampid = timestamps.timestampid and movieblocktime.movieid = movie.movieid order by block");
		try {
			while (result.next()) {
				content.getChildren().add(new CustomComponentBookTicketOne(ccInst,result.getString(1), result.getString(2), result.getInt(5), 0));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//We need to get the seats left list for all the movieblocktime
		for(int i = 0; i <= content.getChildren().size()-1; i++) {

			//get ID of movieblocktime with ID of block,movie,timeslot
			int MovieBlockTimeID = 0;
			int blockID = 0;
			int movieID = 0;
			int timestampID = 0;

			try {

				CustomComponentBookTicketOne comp = (CustomComponentBookTicketOne) content.getChildren().get(i);
				String block = comp.block;
				String movie = comp.movietitle;
				int timeslot = comp.timestamp;

				//This all is used to return the id of the movieblocktime using movieid,blockid and timestampid
				result = LoginController.getSQL().executeQuery("SELECT blockid from movieblock where block='"+block+"'");
				while(result.next()) { blockID = result.getInt(1); }
				result = LoginController.getSQL().executeQuery("SELECT movieid from movie where movietitle='"+movie+"'");
				while(result.next()) { movieID = result.getInt(1); }
				result = LoginController.getSQL().executeQuery("SELECT timestampid from timestamps where timestamp="+timeslot);
				while(result.next()) { timestampID = result.getInt(1); }
				result = LoginController.getSQL().executeQuery("SELECT movieblocktimeid from movieblocktime where blockid="+blockID+" and movieid=" + movieID + " and timestampid="+timestampID);
				while(result.next()) { MovieBlockTimeID = result.getInt(1); }

				//108 is the number of seats in the block
				String query = "SELECT 108-count(seatid) as seatsLeft from seat where movieblocktimeid=" + MovieBlockTimeID;
				result = LoginController.getSQL().executeQuery(query);
				while(result.next()) {
					comp.setSeatsLeftLabel(result.getInt(1));
				}

			} catch (SQLException e5) {
				e5.printStackTrace();
			}
		} anchorPane.getChildren().add(scroller);
	}
}
