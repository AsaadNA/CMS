package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/*
 * This is basically the scene where u book the seats
 * 
 */

public class bookTickets_2Controller implements Initializable {

	@FXML
	Label block_label;
	@FXML
	Label movie_label;
	@FXML
	Label timeslot_label;
	@FXML
	GridPane gridPane;
	@FXML
	Button bookNowButton;
	@FXML
	AnchorPane anchorPane_seats; //This is the seat anchorpane
	@FXML
	AnchorPane anchorPane_purchase; //This is the purchase anchorpane
	@FXML
	TextField firstName_textField;
	@FXML
	TextField lastName_textField;
	@FXML
	TextField phoneNumber_textField;
	@FXML
	TextField email_textField;
	@FXML
	Button purchase_submit_button;
	@FXML
	CheckBox userCheckBox;
	
	//Passed via bookTickets_1Controller parameter
	public String block,movie;
	public int timeslot;
	
	//
	private ArrayList<SeatButton> seats = new ArrayList<SeatButton>();
	public ArrayList<SeatButton> selected_seats = new ArrayList<SeatButton>(); //accesed by SeatButton statically
	
	int MovieBlockTimeID = 0;

	//System.out.println("Status: " + seat.isSelected + " | " +"R: " + seat.row  + " | C: " + seat.column + " | Seat Type : " + seat.seatType);
	//On book now ask for the client info and search his/her credentials using db
	public void onBookNowClick(ActionEvent e) {
		
		//We wanna book only if the seats are selected
		if(selected_seats.size() != 0) {

			//This time what we do is we dont change the fxml
			//but we have 2 anchorpanes with different components 
			//first anchorpane has the seat selection
			//second anchorpane has the purchasing stuff

			anchorPane_seats.setVisible(false);
			bookNowButton.setVisible(false);

			anchorPane_purchase.setVisible(true);
		} else {
			//System.out.println("Select a seat");
		}
	}
	
	public void alreadyClientClick(ActionEvent e) {
		if(userCheckBox.isSelected()) {
			lastName_textField.setDisable(true);
			email_textField.setDisable(true);
		} else {
			lastName_textField.setDisable(false);
			email_textField.setDisable(false);
		}
	}
	
	//This will process the transaction
	public void processTransaction(int clientID) throws SQLException {
		
		if(clientID != -1 || clientID != 0) {
			
			//get ID of movieblocktime with ID of block,movie,timeslot
			int MovieBlockTimeID = 0;
			int blockID = 0;
			int movieID = 0;
			int timestampID = 0;

			//This all is used to return the id of the movieblocktime using movieid,blockid and timestampid
			ResultSet result = LoginController.getSQL().executeQuery("SELECT blockid from movieblock where block='"+block+"'");
			while(result.next()) { blockID = result.getInt(1); }
			result = LoginController.getSQL().executeQuery("SELECT movieid from movie where movietitle='"+movie+"'");
			while(result.next()) { movieID = result.getInt(1); }
			result = LoginController.getSQL().executeQuery("SELECT timestampid from timestamps where timestamp="+timeslot);
			while(result.next()) { timestampID = result.getInt(1); }
			result = LoginController.getSQL().executeQuery("SELECT movieblocktimeid from movieblocktime where blockid="+blockID+" and movieid=" + movieID + " and timestampid="+timestampID);
			while(result.next()) { MovieBlockTimeID = result.getInt(1); }
			
			///////////////////////////////////  PAYMENT //////////////////////////////////

			int paymentAmount = 0; //this is the total amount for the seats
			int ticketCount = 0;
			int cashierID = 0;

			//getting the seat price for every seat selected from the table
			for(int i = 0; i <= selected_seats.size()-1; i++) {
				String seatype = selected_seats.get(i).seatType; //find the id of the seatype before inserting
				result = LoginController.getSQL().executeQuery("SELECT seatPrice from seatType where seatType='"+seatype+"'");
				while(result.next()) { paymentAmount += result.getInt(1); }
				ticketCount++;
			}

			//getting cashierID
			result = LoginController.getSQL().executeQuery("SELECT cashierID from cashier where cashieruserName='"+LoginController.getUser()+"'");
			while(result.next()) { cashierID = result.getInt(1); }

			//Inserting to payments table
			String query = "INSERT INTO PAYMENT (PAYMENTAMOUNT,PAYMENTTICKETCOUNT,CLIENTID,CASHIERID,MOVIEBLOCKTIMEID) VALUES (" +
					paymentAmount + "," + ticketCount + "," + clientID + "," + cashierID + "," + MovieBlockTimeID + ")";
			//System.out.println("Inserted Payment");
			LoginController.getSQL().executeQuery(query);
			
			
			int paymentID = 0;

			//getting paymentID
			query = "SELECT PAYMENTID FROM PAYMENT WHERE CLIENTID=" + clientID + " and CASHIERID=" + cashierID + " and MOVIEBLOCKTIMEID=" + MovieBlockTimeID;
			result = LoginController.getSQL().executeQuery(query);
			while(result.next()) { paymentID = result.getInt(1); }
			
			
			///////////////////			SEAT & TICKET   ////////////////////////////
			
			//after the seat is inserted we are also going to insert the ticket in the same loop

			//Get the arraylist of seats .. traverse through it and insert the seat in the seat table
			for(int i = 0; i <= selected_seats.size()-1; i++) {
				
				SeatButton seat = selected_seats.get(i);
				int row = seat.row;
				int column = seat.column;
				int seatTypeID = 0;

				String seatype = seat.seatType; //find the id of the seatype before inserting
				result = LoginController.getSQL().executeQuery("SELECT typeID from seatType where seatType='"+seatype+"'");
				while(result.next()) { seatTypeID = result.getInt(1); }

				query = "INSERT INTO SEAT (seatrow,seatcolumn,typeid,movieblocktimeid,clientid) values (" 
						+ row + "," + column + "," + seatTypeID + "," + MovieBlockTimeID + "," + clientID + ")";

				LoginController.getSQL().executeQuery(query);
			
				//Insert ticket
				int seatID = 0;
				query = "SELECT seatid from seat where MOVIEBLOCKTIMEID=" + MovieBlockTimeID + " and clientID=" + clientID + " and seatrow=" + seat.row + " and seatcolumn="+seat.column + " and typeid="+seatTypeID;
				result = LoginController.getSQL().executeQuery(query);
				while(result.next()) { seatID = result.getInt(1); }
				
				query = "INSERT INTO ticket (clientID,paymentID,cashierID,movieblocktimeid,seatID) values (" + 
						clientID + "," + paymentID + "," + cashierID + "," +MovieBlockTimeID + "," + seatID + ")";
				LoginController.getSQL().executeQuery(query);

			} 
			
			showInfo("Transaction Completed !");
		} 
		
		else {
			showError("Payment Not Successful Invalid Client or Information");
		}
		
		seats.clear();
		selected_seats.clear();

	}

	private void showError(String text) {
		Alert alert = new Alert(AlertType.ERROR,text);
		alert.showAndWait();
	}
	
	private void showInfo(String text) {
		Alert alter = new Alert(AlertType.INFORMATION,text);
		alter.showAndWait();
	}
	
	//Purchase button
	public void onPurchaseSubmit(ActionEvent e) throws SQLException, IOException {
		
		//NOTE: We can't have duplicate first name and contact in the database for now
		
		int clientID = -1;
		
		//old user
		if(userCheckBox.isSelected()) {
			String clientfirstname = firstName_textField.getText().toString();
			String clientphonenumber = phoneNumber_textField.getText().toString();

			//if it's old it still maybe that user is not there or inserted so we check
			String query = "SELECT clientID from client where (clientfirstname='" + clientfirstname + "' and clientphone=" + clientphonenumber + ") and clientphone=" + clientphonenumber;
			ResultSet result = LoginController.getSQL().executeQuery(query);
			if(!result.next()) {
				showError("User data does not exist");
			} else {
				result = LoginController.getSQL().executeQuery(query); //lifetime runs out that's why we query again
				while(result.next()) { clientID = result.getInt(1);  }
				processTransaction(clientID);
				showInfo("Transaction Completed");
			}				
		} 
		
		//new user
		else {
			
			String clientfirstname = firstName_textField.getText().toString();
			String clientphonenumber = phoneNumber_textField.getText().toString();
			String clientemail = email_textField.getText();

			//if it's old it still maybe that user is not there or inserted so we check
			String query = "SELECT clientID from client where (clientfirstname='" + clientfirstname + "' and clientphone=" + clientphonenumber + ") or clientphone=" + clientphonenumber + " or clientemail='" + clientemail + "'";
			ResultSet result = LoginController.getSQL().executeQuery(query);
			if(result.next()) { //user already exists
				showError("Key Value Already Exists");
			} else {
				
				String clientlastname = lastName_textField.getText();

				query = "INSERT INTO CLIENT (clientfirstname,clientlastname,clientphone,clientemail) values "
						+ "('"+clientfirstname+"','"+clientlastname+"',"+clientphonenumber+",'"+clientemail+"')";
				LoginController.getSQL().executeQuery(query);

				//Get the clientID
				result = LoginController.getSQL().executeQuery("SELECT clientid from client where clientfirstname='"+clientfirstname+"'" +  " and clientPhone="+clientphonenumber);
				while(result.next()) { clientID = result.getInt(1); }
							
				processTransaction(clientID);
			}				
		} 
				
	}
	
	private void loadSeatGrid() {
		
		ArrayList<SeatButton> bookedSeats = new ArrayList<SeatButton>();
		
		seats.clear();
		selected_seats.clear();
		bookedSeats.clear();
					
		//1. Get row and column of the  seats that are already booked for this movie and timeslot
		String query = "SELECT seatrow,seatcolumn from seat where clientid is not NULL and movieblocktimeid="+MovieBlockTimeID;
		ResultSet result = LoginController.getSQL().executeQuery(query);
		try {
			while(result.next()) {
				bookedSeats.add(new SeatButton(result.getInt(1),result.getInt(2)));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/* Intiailzing the seats */
		
		int rows = 5; //6x18 = 108 seats
		int cols = 17;
		
		int xx = 0;
		int yy = 0;
		
		int xPadding = 40;
		int yPadding = 70;
		
		for(int i = 0; i <= rows; i++) {
			for(int j = 0; j <= cols; j++) {
				SeatButton seat = null;
				try {
					seat = new SeatButton(this,xx,yy,i,j);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(j == 9) { xx += 20; } //gives that padding

				if(i == 2 || i == 3) {seat.seatType = "GOLD";} //Telling which rows we want to be gold
				else { seat.seatType = "BRONZE"; } //and which rows we want to be bronze
				
				anchorPane_seats.getChildren().add(seat);
				seats.add(seat);
				xx += xPadding;
			}
			
			xx = 0;
			yy += yPadding;
		}
		
		//2. Hide or delete the already booked seats from the overall data
		//   or use the isBookedFlag to change the image to someting else
		// or setting isBookedFlag from database here
		for(int i = 0; i <= bookedSeats.size()-1; i++) {
			SeatButton bSeat = bookedSeats.get(i);
			for(int j = 0; j <= seats.size()-1; j++) {
				SeatButton seat = seats.get(j);
				if((bSeat.row == seat.row) && (bSeat.column == seat.column)) {
					SeatButton s = (SeatButton) anchorPane_seats.getChildren().get(j);
					s.setBooked(true);
					//seats.get(i).setBooked(true);
					//anchorPane_seats.getChildren().remove((SeatButton)seats.get(j));
					//seats.remove(j);
				}
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		anchorPane_purchase.setVisible(false);
		
		block_label.setText(block);
		movie_label.setText(movie);
		timeslot_label.setText(Integer.toString(timeslot));

		//get ID of movieblocktime with ID of block,movie,timeslot
		int blockID = 0;
		int movieID = 0;
		int timestampID = 0;

		try {

			//This all is used to return the id of the movieblocktime using movieid,blockid and timestampid
			ResultSet result = LoginController.getSQL().executeQuery("SELECT blockid from movieblock where block='"+block+"'");
			while(result.next()) { blockID = result.getInt(1); }
			result = LoginController.getSQL().executeQuery("SELECT movieid from movie where movietitle='"+movie+"'");
			while(result.next()) { movieID = result.getInt(1); }
			result = LoginController.getSQL().executeQuery("SELECT timestampid from timestamps where timestamp="+timeslot);
			while(result.next()) { timestampID = result.getInt(1); }
			result = LoginController.getSQL().executeQuery("SELECT movieblocktimeid from movieblocktime where blockid="+blockID+" and movieid=" + movieID + " and timestampid="+timestampID);
			while(result.next()) { MovieBlockTimeID = result.getInt(1); }

		} catch (SQLException e5) {
			// TODO Auto-generated catch block
			e5.printStackTrace();
		}
		
		loadSeatGrid();
		
	}
}