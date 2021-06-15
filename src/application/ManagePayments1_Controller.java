package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

class CustomComponentManageTicketOne extends AnchorPane {
	
	private String movietitle,block,clientfirstname,clientlastname,clienphone,clientemail,moviedatetime,paymenttimestamp;
	private  int paymentamount,ticketcount;	
	
	public int paymentID = 0;
		
	private Label createLabel(String text , int x , int y) {
		Label result = new Label(text);
		result.setTextFill(Color.WHITE);
		result.setFont(new Font("System",17));
		result.setLayoutX(x);
		result.setLayoutY(y);	
		return result;
	}
	
	public CustomComponentManageTicketOne(CashierController ccInst,int paymentID,String block,String clientfirstname,String clientlastname,String clientphone,String clientemail,int ticketcount,int paymentamount,String movietitle,String moviedatetime,String paymenttimestamp )  {
		
		this.block = block;
		this.movietitle = movietitle;
		this.moviedatetime = moviedatetime;
		this.paymentamount = paymentamount;
		this.clientfirstname = clientlastname;
		this.clienphone = clientphone;
		this.clientemail = clientemail;
		this.ticketcount = ticketcount;
		this.paymenttimestamp = paymenttimestamp;
		
		this.paymentID = paymentID;
		
		setStyle("-fx-background-color: #292929");
		setPrefSize(500, 150);
		
		int xx = 12;
		Label blockLabel = createLabel("Block : " + block,42,26);
		Label clientlabel = createLabel("Client: " + clientfirstname + " " + clientlastname,42,31+28-2);
		Label clientemaillabel = createLabel("Email: " + clientemail,42,31+28+28+2-2);
		Label clientphonelabel = createLabel("Phone: " + clientphone,42,31+28+28+2+28-2);
		Label paymenttimestamplabel = createLabel("Payment timestamp: " + paymenttimestamp,42,31+28+28+2+28-2+28+xx);
		
		Label paymentamountlabel = createLabel("Payment amount: " + paymentamount,400,26+xx);
		Label ticketcountlabel = createLabel("Tickets: " + ticketcount,400,31+28-2+xx);
		Label movietitlelabel = createLabel("Movie: " + movietitle , 400,31+28+28+2-2+xx);
		Label moviedatetimelabel = createLabel("Movie Timestamp: " + moviedatetime,400,31+28+28+2+28-2+xx);
		
		Button moreInfoButton = new Button("More Info");
		moreInfoButton.setStyle("-fx-background-color: #88e327");
		moreInfoButton.setFont(new Font("System",15));
		moreInfoButton.setLayoutX(400);
		moreInfoButton.setLayoutY(180);
		
		Button refundPaymentButton = new Button("Refund Payment");
		refundPaymentButton.setStyle("-fx-background-color: #88e327");
		refundPaymentButton.setFont(new Font("System",15));
		refundPaymentButton.setLayoutX(525);
		refundPaymentButton.setLayoutY(180);
		
		getChildren().add(blockLabel);
		getChildren().add(clientlabel);
		getChildren().add(clientemaillabel);
		getChildren().add(clientphonelabel);
		
		getChildren().add(paymentamountlabel);
		getChildren().add(ticketcountlabel);
		getChildren().add(movietitlelabel);
		getChildren().add(moviedatetimelabel);
		getChildren().add(paymenttimestamplabel);
		
		getChildren().add(moreInfoButton);
		getChildren().add(refundPaymentButton);
		
		//So when we click the button we want to get all the tickets  where the payment id is of the given payment block
		
		moreInfoButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				
				//pass the data to ManagePayments2Controller
								
				ManagePayments2_Controller controller = new ManagePayments2_Controller();
	    		
	        	//Passing these variable to other scene via the controller;
	    		controller.paymentID = paymentID;
	    		//controller.ccInst = ccInst;
	            
	            FXMLLoader fxmlLoader = null;
	    		fxmlLoader = new FXMLLoader(getClass().getResource("ManagePayments_2.fxml"));
	    		fxmlLoader.setController(controller); 
	    		try {
					ccInst.borderPane.setCenter(fxmlLoader.load());
				} catch (IOException e1) {
					System.out.println("Couuld not load fxml ManagePayments2 Controller");
					e1.printStackTrace();
				}	
			}
			
		}); 
		
		//Refund payment button
		refundPaymentButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				
				ArrayList<String> seatIDs = new ArrayList<String>();
				ResultSet result = LoginController.getSQL().executeQuery("SELECT seatid from ticket where paymentid="+paymentID);
				try {
					while(result.next()) {
						seatIDs.add(result.getString(1));
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
						
				//first delete tickets
				LoginController.getSQL().executeQuery("DELETE FROM ticket where paymentid="+paymentID);
				
				//second delete seats
				for(int i = 0; i <= seatIDs.size()-1; i++) { 
					LoginController.getSQL().executeQuery("DELETE FROM seat where seatid=" + seatIDs.get(i));
					
					//Physical storage of the barcode in the localstorage
			        String QRpath = "localstorage/" + paymentID + "-" + seatIDs.get(i) + ".png";
					File myObj = new File(QRpath); 
				    if (myObj.delete()) { 
				      //System.out.println("Deleted the file: " + myObj.getName());
				    } else {
				      //System.out.println("Failed to delete the file.");
				    } 
					
				} seatIDs.clear();
				
				//third the payment
				LoginController.getSQL().executeQuery("DELETE FROM PAYMENT where paymentId="+paymentID);
								
				Alert alter = new Alert(AlertType.INFORMATION,"Payment refuned !");
				alter.showAndWait();
				
				try {
					ManagePayments1_Controller.reload();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
}


public class ManagePayments1_Controller implements Initializable {

	@FXML 
	AnchorPane pane;
	@FXML
	TextField searchTextfield;
	
	public static VBox content = new VBox(2);

	public static CashierController ccInst; //instance pass via manually in CashierController
	
	//On search click
	public void onSearchButtonClick(ActionEvent e) throws SQLException {
		String clientphoneText = searchTextfield.getText();
		
		int cashierID = -1;
		String cashierUsername = LoginController.getUser();
		
		String query = "SELECT cashierID from CASHIER WHERE cashierUsername='"+cashierUsername+"'";
		ResultSet result = LoginController.getSQL().executeQuery(query);
		while(result.next()) {
			cashierID = result.getInt(1);
		}
		
		query = "select block,clientfirstname,clientlastname,clientphone,clientemail,paymentticketcount,paymentamount,movietitle,to_char(timestampdatetime,'yyyy/mm/dd hh12:mi'),to_char(paymenttimestamp,'yyyy/mm/dd hh12:mi'),paymentid from client inner join payment on payment.clientid = client.clientid inner join movieblocktime on payment.movieblocktimeid = movieblocktime.movieblocktimeid inner join movie on movie.movieid = movieblocktime.movieid inner join timestamps on timestamps.timestampid = movieblocktime.timestampid inner join movieblock on movieblocktime.blockid = movieblock.blockid where client.clientphone="+clientphoneText+" and payment.cashierid="+cashierID;
		result = LoginController.getSQL().executeQuery(query);
		if(result.next()) { 
			content.getChildren().clear();
			String block = result.getString(1);
			String clientfirstname = result.getString(2);
			String clientlastname = result.getString(3);
			String clientphone = result.getString(4);
			String clientemail = result.getString(5);
			int ticketcount = result.getInt(6);
			int paymentamount = result.getInt(7);
			String movietitle = result.getString(8);
			String moviedatetime = result.getString(9);
			String paymenttimestamp = result.getString(10);

			content.getChildren().add(new CustomComponentManageTicketOne(ccInst,result.getInt(11),block,clientfirstname,clientlastname,clientphone,clientemail,ticketcount,paymentamount,movietitle,moviedatetime,paymenttimestamp));
			try {
				while(result.next()) { content.getChildren().add(new CustomComponentManageTicketOne(ccInst,result.getInt(11),block,clientfirstname,clientlastname,clientphone,clientemail,ticketcount,paymentamount,movietitle,moviedatetime,paymenttimestamp));}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else {
			Alert alter = new Alert(AlertType.ERROR,"Payment not found !");
			alter.showAndWait();
		}
	}
	
	//reload the data
	public static void reload() throws SQLException {
		content.getChildren().clear();
		
		int cashierID = -1;
		String cashierUsername = LoginController.getUser();
		
		String query = "SELECT cashierID from CASHIER WHERE cashierUsername='"+cashierUsername+"'";
		ResultSet result = LoginController.getSQL().executeQuery(query);
		while(result.next()) {
			cashierID = result.getInt(1);
		}
		
		query = "select block,clientfirstname,clientlastname,clientphone,clientemail,paymentticketcount,paymentamount,movietitle,to_char(timestampdatetime,'yyyy/mm/dd hh12:mi'),to_char(paymenttimestamp,'yyyy/mm/dd hh12:mi'),paymentid from client inner join payment on payment.clientid = client.clientid inner join movieblocktime on payment.movieblocktimeid = movieblocktime.movieblocktimeid inner join movie on movie.movieid = movieblocktime.movieid inner join timestamps on timestamps.timestampid = movieblocktime.timestampid inner join movieblock on movieblocktime.blockid = movieblock.blockid inner join cashier on payment.cashierid = cashier.cashierid where payment.cashierid="+cashierID;
		result = LoginController.getSQL().executeQuery(query);
		
		try {
			while(result.next()) {
				
				String block = result.getString(1);
				String clientfirstname = result.getString(2);
				String clientlastname = result.getString(3);
				String clientphone = result.getString(4);
				String clientemail = result.getString(5);
				int ticketcount = result.getInt(6);
				int paymentamount = result.getInt(7);
				String movietitle = result.getString(8);
				String moviedatetime = result.getString(9);
				String paymenttimestamp = result.getString(10);
				
				content.getChildren().add(new CustomComponentManageTicketOne(ccInst,result.getInt(11),block,clientfirstname,clientlastname,clientphone,clientemail,ticketcount,paymentamount,movietitle,moviedatetime,paymenttimestamp));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		content.setStyle("-fx-background-color: #292929");
		content.setSpacing(6);
		ScrollPane scroller = new ScrollPane(content);
		scroller.setLayoutX(66);
		scroller.setLayoutY(86);
		
		scroller.setStyle("-fx-background: rgb(41,41,41); -fx-padding:0;");
		scroller.setFitToHeight(true);
		scroller.setFitToWidth(true);
		
		scroller.setPrefSize(768, 513);
		scroller.setVbarPolicy(ScrollBarPolicy.NEVER);
		
		try {
			reload();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pane.getChildren().add(scroller);
	}

}
