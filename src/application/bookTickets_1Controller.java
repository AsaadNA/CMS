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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * 
 * 
 * 
 * @author Asaad
 *
 *	Now this view will basically display all the movieblocktime so we can reserve the desired seats of the desired
 *	MovieID , BlockID , TimeSlotID
 */

public class bookTickets_1Controller implements Initializable {

	@FXML
	TableView<bookTickets_1_TableModel> table;
	@FXML
	TableColumn<bookTickets_1_TableModel,String> movie_column;
	@FXML
	TableColumn<bookTickets_1_TableModel,String> block_column;
	@FXML
	TableColumn<bookTickets_1_TableModel,Float> rating_column;
	@FXML
	TableColumn<bookTickets_1_TableModel,Integer> duration_column;
	@FXML
	TableColumn<bookTickets_1_TableModel,Integer> timeslot_column;
	@FXML
	TableColumn<bookTickets_1_TableModel,Integer> seatsLeft_column;
	
	ObservableList<bookTickets_1_TableModel> obList = FXCollections.observableArrayList();
		
	public static CashierController ccInst; //instance pass via manually in CashierController
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		System.out.println("Intialzie Running");
		
		//Handling double click on a selected row here
		//After a movie , timeslot and the block is selected
		//we will go to the seats scene where the user will choose the seat
		table.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
		        	
		        	bookTickets_2Controller controller = new bookTickets_2Controller();
		    		
		        	//Passing these variable to other scene via the controller;
		    		controller.block = table.getSelectionModel().getSelectedItem().block;
		    		controller.movie = table.getSelectionModel().getSelectedItem().movie;
		    		controller.timeslot = table.getSelectionModel().getSelectedItem().timeslot;	    		
		            
		            FXMLLoader fxmlLoader = null;
		    		fxmlLoader = new FXMLLoader(getClass().getResource("bookTickets_2.fxml"));
		    		fxmlLoader.setController(controller); 
		    		try {
						ccInst.borderPane.setCenter(fxmlLoader.load());
					} catch (IOException e) {
						System.out.println("Couuld not load fxml BOOK TICKETS_1 Controller");
						e.printStackTrace();
					}	
		        }
		    }
		});
		
		block_column.setCellValueFactory(new PropertyValueFactory<>("block"));
		movie_column.setCellValueFactory(new PropertyValueFactory<>("movie"));
		rating_column.setCellValueFactory(new PropertyValueFactory<>("rating"));
		duration_column.setCellValueFactory(new PropertyValueFactory<>("duration"));
		timeslot_column.setCellValueFactory(new PropertyValueFactory<>("timeslot"));
		seatsLeft_column.setCellValueFactory(new PropertyValueFactory<>("seatsLeft"));

		//Load the movieblocktime data into the table
		ResultSet result = LoginController.getSQL().executeQuery("select block,movietitle,movierating,movieduration,timestamp from movie,movieblock,timestamps,movieblocktime where movieblocktime.blockid = movieblock.blockid and movieblocktime.timestampid = timestamps.timestampid and movieblocktime.movieid = movie.movieid order by block");
		try {
			while(result.next()) {
				obList.add(new bookTickets_1_TableModel(result.getString(1),result.getString(2),result.getFloat(3),result.getInt(4),result.getInt(5),0));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//We need to get the seats left list for all the movieblocktime
		for(int i = 0; i <= obList.size()-1; i++) {
			
			//get ID of movieblocktime with ID of block,movie,timeslot
			int MovieBlockTimeID = 0;
			int blockID = 0;
			int movieID = 0;
			int timestampID = 0;
			
			try {
				
				String block = obList.get(i).block;
				String movie = obList.get(i).movie;
				int timeslot = obList.get(i).timeslot;
				
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
					obList.get(i).seatsLeft = result.getInt(1);
				}

			} catch (SQLException e5) {
				// TODO Auto-generated catch block
				e5.printStackTrace();
			}
		}
		
		table.setItems(obList);		
	}
}
