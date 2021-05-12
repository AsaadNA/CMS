package application;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

class CustomComponentManageTicketTwo extends AnchorPane {
	
	public String block,movietitle,clientfirstname,clientlastname,seattype;
	public int seatrow,seatcolumn,seatprice;
	public String moviedatetime,ticketpurchasedatetime;
	
	private Label createLabel(String text , int x , int y) {
		Label result = new Label(text);
		result.setTextFill(Color.WHITE);
		result.setFont(new Font("System",17));
		result.setLayoutX(x);
		result.setLayoutY(y);	
		return result;
	}
	
	public CustomComponentManageTicketTwo(int seatID , int paymentID,String block,String movietitle,String moviedatetime,int seatrow,int seatcolumn,String clientfirstname,String clientlastname,int seatprice,String seattype,String ticketpurchasedatetime) {
				
		this.block = block;
		this.movietitle = movietitle;
		this.moviedatetime = moviedatetime;
		this.seatrow = seatrow;
		this.clientfirstname = clientlastname;
		this.clientlastname = clientlastname;
		this.seatcolumn = seatcolumn;
		this.seatprice = seatprice;
		this.ticketpurchasedatetime = ticketpurchasedatetime;
		this.seattype = seattype;
				
		setStyle("-fx-background-color: #292929");
		setPrefSize(500, 150);
		
		int xx = 0;
		Label blockLabel = createLabel("Block : " + block,42,26);
		Label clientlabel = createLabel("Client : " + clientfirstname + " " + clientlastname,42,31+28-2);
		Label movielabel = createLabel("Movie : " + movietitle,42,31+28+28+2-2);
		Label moviedatetimelabel = createLabel("Movie timestamp : " + moviedatetime,42,31+28+28+2+28-2);
		
		Label seatrowlabel = createLabel("Seat row : " + seatrow,400,26+xx);
		Label seatcolumnlabel = createLabel("Seat column : " + seatcolumn,400,31+28-2+xx);
		Label seattypelabel = createLabel("Seat type :  " + seattype, 400,31+28+28+2-2+xx);
		Label seatpricelabel = createLabel("Seat price : " + seatprice,400,31+28+28+2+28-2+xx);
		Label seatpurchasetimestamplabel = createLabel("Seat purchase timestamp : " + ticketpurchasedatetime,400,31+28+28+2+28-2+xx+28);

		Button printTicketButton = new Button("Generate printable ticket");
		printTicketButton.setStyle("-fx-background-color: #88e327");
		printTicketButton.setFont(new Font("System",15));
		printTicketButton.setLayoutX(42);
		printTicketButton.setLayoutY(31+28+28+2+28-2+28+5);
		
		getChildren().add(blockLabel);
		getChildren().add(clientlabel);
		getChildren().add(movielabel);
		getChildren().add(moviedatetimelabel);
		
		getChildren().add(seatrowlabel);
		getChildren().add(seatcolumnlabel);
		getChildren().add(seattypelabel);
		getChildren().add(seatpricelabel);
		getChildren().add(seatpurchasetimestamplabel);
		
		getChildren().add(printTicketButton);
		
		//GENERATE A PRINTABLE TICKET
		printTicketButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				
				String ticketQRpath = "";
				ResultSet result = LoginController.getSQL().executeQuery("SELECT ticketQR from ticket where paymentID="+paymentID+" and seatid=" + seatID);
				try {
					while(result.next()) {
						ticketQRpath = result.getString(1);
					}
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				BufferedImage qrCode = null;
			    try {
					 qrCode = ImageIO.read(new File(ticketQRpath));
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				// load an image
				Image image = null;
				try {
					image = javax.imageio.ImageIO.read(new File("res/template_ticket.png"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// resize it
				image = image.getScaledInstance(754, 247, Image.SCALE_SMOOTH);
				// create a new image to render to
				BufferedImage newimg = new BufferedImage(754,247,BufferedImage.TYPE_INT_ARGB);
				// get graphics to draw..
				Graphics2D graphics =newimg.createGraphics();
				
				//draw the other image on it
				graphics.drawImage(image,0,0,null);
				graphics.drawImage(qrCode,452,50,null); //drawing the qrCode
				
				graphics.setFont(graphics.getFont().deriveFont(20f));
			    graphics.drawString("Seat: " + seatrow + " Row " + seatcolumn + " Column", 71, 98);
			    graphics.drawString("Seat Type: " + seattype, 71, 98+20);
			    graphics.drawString("Movie: " + movietitle,71,98+20+20);
			    graphics.drawString("Datetime: " + moviedatetime, 71, 98+20+20+20);
				
				//export the new image
				try {
					ImageIO.write(newimg,"png",new File("localstorage_printtickets/" + Integer.toString(paymentID) + " (" + seatrow + "," + seatcolumn +").png"));
					//System.out.println("Generated ticket");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Alert alter = new Alert(AlertType.INFORMATION,"Generated ticket !");
				alter.showAndWait();
			}
			
		}); 
		
	}
}


public class ManagePayments2_Controller implements Initializable {
	
	@FXML
	AnchorPane pane;
	
	public int paymentID = 0;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pane.setStyle("-fx-background-color: #292929");
		VBox content = new VBox(2);
		content.setStyle("-fx-background-color: #292929");
		content.setSpacing(6);
		ScrollPane scroller = new ScrollPane(content);
		scroller.setFitToWidth(true);
		scroller.setLayoutX(66);
		scroller.setLayoutY(38);
		scroller.setStyle("-fx-background: rgb(41,41,41); -fx-padding:0;");
		scroller.setPrefSize(768, 513);
		scroller.setVbarPolicy(ScrollBarPolicy.NEVER);
		
		String query = "select clientfirstname,clientlastname,seatrow,seatcolumn,seatprice,seattype,movietitle,block,to_char(timestampdatetime,'yyyy/mm/dd hh12:mi'),to_char(paymenttimestamp,'yyyy/mm/dd hh12:mi'),seat.seatid from client inner join ticket on ticket.clientid = client.clientid inner join seat on ticket.seatID = seat.seatID inner join seattype on seat.typeID = seattype.typeid inner join movieblocktime on ticket.movieblocktimeid = movieblocktime.movieblocktimeid inner join movie on movie.movieid = movieblocktime.movieid inner join timestamps on timestamps.timestampid = movieblocktime.timestampid inner join movieblock on movieblocktime.blockID = movieblock.blockID inner join payment on ticket.paymentid = payment.paymentid where payment.paymentid="+ paymentID;
		ResultSet result = LoginController.getSQL().executeQuery(query);
		try {
			while(result.next()) {
				
				String clientfirstname = result.getString(1);
				String clientlastname = result.getString(2);
				int seatrow = result.getInt(3);
				int seatcolumn = result.getInt(4);
				int seatprice = result.getInt(5);
				String seattype = result.getString(6);
				String movietitle = result.getString(7);
				String block = result.getString(8);
				String moviedatetime = result.getString(9);
				String ticketpurchasedatetime = result.getString(10);
				
				content.getChildren().add(new CustomComponentManageTicketTwo(result.getInt(11),paymentID,block,movietitle,moviedatetime,seatrow,seatcolumn,clientfirstname,clientlastname,seatprice,seattype,ticketpurchasedatetime));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pane.getChildren().add(scroller);
	}

}
