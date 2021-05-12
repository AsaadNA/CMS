package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
	public int seatsLeft;	
	private Label seatsLeftLabel;
	
	public String moviedatetime;
		
	public void setSeatsLeftLabel(int seatsLeft) {
		this.seatsLeftLabel.setText("Seats Left: " + Integer.toString(seatsLeft));
	}
	
	private Label createLabel(String text , int x , int y) {
		Label result = new Label(text);
		result.setTextFill(Color.WHITE);
		result.setFont(new Font("System",17));
		result.setLayoutX(x);
		result.setLayoutY(y);
		
		return result;
	}
	
	public CustomComponentBookTicketOne(CashierController ccInst,String block , String movietitle , String moviedatetime , int seatsLeft) {
		this.block = block;
		this.movietitle = movietitle;
		this.moviedatetime = moviedatetime;
		this.seatsLeft = seatsLeft;
				
		setStyle("-fx-background-color: #292929");
		setPrefSize(450, 150);
		
		Label blockLabel = createLabel("Block : " + block,42,26);
		Label movieLabel = createLabel("Movie : " + movietitle,42,31+28);
		Label timeslotLabel = createLabel("Timeslot : " + moviedatetime,42,26+28+5+30+3);
		seatsLeftLabel = createLabel("Seats Left : " + Integer.toString(seatsLeft),300,26+28+5+30+3);
		
		Button button = new Button("Purchase Tickets");
		button.setStyle("-fx-background-color: #88e327");
		button.setFont(new Font("System",15));
		button.setLayoutX(539);
		button.setLayoutY(62);


		button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				bookTickets_2Controller controller = new bookTickets_2Controller();
	    		
	        	//Passing these variable to other scene via the controller;
	    		controller.block = block;
	    		controller.movie = movietitle;
	    		controller.moviedatetime = moviedatetime;
	    		controller.ccInst = ccInst;
	            
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
	@FXML
	ComboBox<String> movieBox;
	
	public VBox content = new VBox(5);
	public CashierController ccInst; //instance pass via manually in CashierController
	
	public void onSearchButtonClick(ActionEvent e) throws SQLException {
		//
		String selectedmovie = movieBox.getValue();
		ResultSet result = LoginController.getSQL().executeQuery("select block,movietitle,movierating,movieduration,to_char(timestampdatetime,'yyyy/mm/dd hh12:mi') from movie,movieblock,timestamps,movieblocktime where movieblocktime.blockid = movieblock.blockid and movieblocktime.timestampid = timestamps.timestampid and movieblocktime.movieid = movie.movieid and movie.movietitle='"+selectedmovie+"'");
		content.getChildren().clear(); //clear here
		while(result.next()) {
			content.getChildren().add(new CustomComponentBookTicketOne(ccInst,result.getString(1), result.getString(2), result.getString(5), 0));
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
						String moviedatetime = comp.moviedatetime;

						//This all is used to return the id of the movieblocktime using movieid,blockid and timestampid
						result = LoginController.getSQL().executeQuery("SELECT blockid from movieblock where block='"+block+"'");
						while(result.next()) { blockID = result.getInt(1); }
						result = LoginController.getSQL().executeQuery("SELECT movieid from movie where movietitle='"+movie+"'");
						while(result.next()) { movieID = result.getInt(1); }
						//SELECT timestampid from timestamps where timestampdatetime=TO_DATE('2021/06/05 9:30', 'yyyy/mm/dd hh12:mi')
						//String q = "SELECT timestampid from timestamps where timestampdatetime=TO_DATE('"+moviedatetime+"','"+"yyyy/mm/dd hh12:mi')";
						//	System.out.println(q);
						result = LoginController.getSQL().executeQuery("SELECT timestampid from timestamps where timestampdatetime=TO_DATE('"+moviedatetime+"','"+"yyyy/mm/dd hh12:mi')");
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
				}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//Loading moviebox with movies
		ArrayList<String> movielist = new ArrayList<String>();
		ResultSet result = LoginController.getSQL().executeQuery("SELECT movietitle from movie");
		try {
			while(result.next()) {
				movielist.add(result.getString(1));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} movieBox.setItems(FXCollections.observableArrayList(movielist));
		
		content.setStyle("-fx-background-color: #292929");
		content.setSpacing(5);
		ScrollPane scroller = new ScrollPane(content);
		scroller.setFitToWidth(true);
		scroller.setLayoutX(66);
		scroller.setLayoutY(82);
		scroller.setStyle("-fx-background-color: #292929");
		scroller.setPrefSize(768, 513);
		scroller.setVbarPolicy(ScrollBarPolicy.NEVER);

		// Load the movieblocktime data
		result = LoginController.getSQL().executeQuery(
				"select block,movietitle,movierating,movieduration,"+ "to_char(timestampdatetime," + "'" + "yyyy/mm/dd hh12:mi')" +" from movie,movieblock,timestamps,movieblocktime where movieblocktime.blockid = movieblock.blockid and movieblocktime.timestampid = timestamps.timestampid and movieblocktime.movieid = movie.movieid order by block");
		try {
			while (result.next()) {
				content.getChildren().add(new CustomComponentBookTicketOne(ccInst,result.getString(1), result.getString(2), result.getString(5), 0));
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
				String moviedatetime = comp.moviedatetime;

				//This all is used to return the id of the movieblocktime using movieid,blockid and timestampid
				result = LoginController.getSQL().executeQuery("SELECT blockid from movieblock where block='"+block+"'");
				while(result.next()) { blockID = result.getInt(1); }
				result = LoginController.getSQL().executeQuery("SELECT movieid from movie where movietitle='"+movie+"'");
				while(result.next()) { movieID = result.getInt(1); }
				//SELECT timestampid from timestamps where timestampdatetime=TO_DATE('2021/06/05 9:30', 'yyyy/mm/dd hh12:mi')
				//String q = "SELECT timestampid from timestamps where timestampdatetime=TO_DATE('"+moviedatetime+"','"+"yyyy/mm/dd hh12:mi')";
				//	System.out.println(q);
				result = LoginController.getSQL().executeQuery("SELECT timestampid from timestamps where timestampdatetime=TO_DATE('"+moviedatetime+"','"+"yyyy/mm/dd hh12:mi')");
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
