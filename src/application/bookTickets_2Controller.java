package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

//TODO: Payment System

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
		}
	}
	
	//Purchase anchorpane submit button
	public void onPurchaseSubmit(ActionEvent e) throws SQLException {
		
		//get ID of movieblocktime with ID of block,movie,timeslot
		int MovieBlockTimeID = 0;
		int blockID = 0;
		int movieID = 0;
		int timestampID = 0;
		int clientID = 0;

		//SQL stuff here
		if(userCheckBox.isSelected()) {
			
			/* If it's an old user use the first name and phone number to get the client id */
			
			String firstName = firstName_textField.getText();
			long phoneNumber = Integer.parseInt(phoneNumber_textField.getText());

			//Get the clientID
			ResultSet result = LoginController.getSQL().executeQuery("SELECT clientid from client where clientfirstname='"+firstName+"'" +  " and clientPhone="+phoneNumber);
			while(result.next()) { clientID = result.getInt(1); }

		} else {
			
			/* If it's a new user insert the data to the client table and get the client id */
			
			String firstName = firstName_textField.getText();
			String lastName = lastName_textField.getText();
			long phoneNumber = Integer.parseInt(phoneNumber_textField.getText());
			String email = email_textField.getText();


			//1. Insert into client the new user
			String query = "INSERT INTO CLIENT (clientfirstname,clientlastname,clientphone,clientemail) values "
					+ "('"+firstName+"','"+lastName+"',"+phoneNumber+",'"+email+"')";
			LoginController.getSQL().executeQuery(query);

			//Get the clientID
			ResultSet result = LoginController.getSQL().executeQuery("SELECT clientid from client where clientfirstname='"+firstName+"'" +  " and clientPhone="+phoneNumber);
			while(result.next()) { clientID = result.getInt(1); }
		}

		//This all is used to return the id of the movieblocktime using movieid,blockid and timestampid
		ResultSet result = LoginController.getSQL().executeQuery("SELECT blockid from movieblock where block='"+block+"'");
		while(result.next()) { blockID = result.getInt(1); }
		result = LoginController.getSQL().executeQuery("SELECT movieid from movie where movietitle='"+movie+"'");
		while(result.next()) { movieID = result.getInt(1); }
		result = LoginController.getSQL().executeQuery("SELECT timestampid from timestamps where timestamp="+timeslot);
		while(result.next()) { timestampID = result.getInt(1); }
		result = LoginController.getSQL().executeQuery("SELECT movieblocktimeid from movieblocktime where blockid="+blockID+" and movieid=" + movieID + " and timestampid="+timestampID);
		while(result.next()) { MovieBlockTimeID = result.getInt(1); }

		//2. Get the arraylist of seats .. traverse through it and insert the seat in the seat table
		for(int i = 0; i <= selected_seats.size()-1; i++) {
			SeatButton seat = selected_seats.get(i);
			int row = seat.row;
			int column = seat.column;
			int seatTypeID = 0;

			String seatype = seat.seatType; //find the id of the seatype before inserting
			result = LoginController.getSQL().executeQuery("SELECT typeID from seatType where seatType='"+seatype+"'");
			while(result.next()) { seatTypeID = result.getInt(1); }

			String query = "INSERT INTO SEAT (seatrow,seatcolumn,typeid,movieblocktimeid,clientid) values (" 
					+ row + "," + column + "," + seatTypeID + "," + MovieBlockTimeID + "," + clientID + ")";
			
			LoginController.getSQL().executeQuery(query);
		}
		
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
		System.out.println(query);
		LoginController.getSQL().executeQuery(query);
		
		///////////////////////////////////  TICKETS  //////////////////////////////////
		
		//We need to get the payment id of the current transaction
		//int paymentID = 0;
		
		int paymentID = 0;
		
		query = "SELECT PAYMENTID FROM PAYMENT WHERE CLIENTID=" + clientID + " and CASHIERID=" + cashierID + " and MOVIEBLOCKTIMEID=" + MovieBlockTimeID;
		result = LoginController.getSQL().executeQuery(query);
		while(result.next()) { paymentID = result.getInt(1); }
		
		//We need to get the ID's of all the selected seats that are pushed in the SEAT Table above
		
		ArrayList<Integer> seatIDs = new ArrayList<Integer>();
		query = "SELECT seatid from seat where MOVIEBLOCKTIMEID=" + MovieBlockTimeID + " and clientID=" + clientID;
		result = LoginController.getSQL().executeQuery(query);
		while(result.next()) {
			seatIDs.add(result.getInt(1));
		}
		
		//Now we insert the tickets for each seat 
		for(int i = 0; i <= seatIDs.size()-1; i++) {
			query = "INSERT INTO ticket (clientID,paymentID,cashierID,movieblocktimeid,seatID) values (" + 
					clientID + "," + paymentID + "," + cashierID + "," +MovieBlockTimeID + "," + seatIDs.get(i) + ")";
			LoginController.getSQL().executeQuery(query);
		}
		
		//// now everything is said and done exit from here

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		anchorPane_purchase.setVisible(false);
		
		block_label.setText(block);
		movie_label.setText(movie);
		timeslot_label.setText(Integer.toString(timeslot));
		
		//get ID of movieblocktime with ID of block,movie,timeslot
		int MovieBlockTimeID = 0;
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
		
		//1. Get row and column of the  seats that are already booked for this movie and timeslot
		ArrayList<SeatButton> bookedSeats = new ArrayList<SeatButton>();
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
					anchorPane_seats.getChildren().remove((SeatButton)seats.get(j));
					seats.remove(j);
				}
			}
		}
	}
}