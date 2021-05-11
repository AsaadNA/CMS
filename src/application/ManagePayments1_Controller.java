package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

class CustomComponentManageTicketOne extends AnchorPane {
	
	public String movietitle,block,clientfirstname,clientlastname,clienphone,clientemail;
	public int timestamp,paymentamount,ticketcount;	
		
	private Label createLabel(String text , int x , int y) {
		Label result = new Label(text);
		result.setTextFill(Color.WHITE);
		result.setFont(new Font("System",17));
		result.setLayoutX(x);
		result.setLayoutY(y);	
		return result;
	}
	
	public CustomComponentManageTicketOne(CashierController ccInst,String clientfirstname , String clientlastname , String clientphone ,String block, String movietitle , int timestamp,int paymentamount,int ticketcount,String clientemail) {
		
		this.block = block;
		this.movietitle = movietitle;
		this.timestamp = timestamp;
		this.paymentamount = paymentamount;
		this.clientfirstname = clientlastname;
		this.clienphone = clientphone;
		this.clientemail = clientemail;
		this.ticketcount = ticketcount;
		
		setStyle("-fx-background-color: #292929");
		setPrefSize(450, 150);
				
		Label blockLabel = createLabel("Block : " + block,42,26);
		Label clientlabel = createLabel("Client: " + clientfirstname + " " + clientlastname,42,31+28-2);
		Label clientemaillabel = createLabel("Email: " + clientemail,42,31+28+28+2-2);
		Label clientphonelabel = createLabel("Phone: " + clientphone,42,31+28+28+2+28-2);
		
		Label paymentamountlabel = createLabel("Payment amount: " + paymentamount,400,26);
		
		getChildren().add(blockLabel);
		getChildren().add(clientlabel);
		getChildren().add(clientemaillabel);
		getChildren().add(clientphonelabel);
		
		getChildren().add(paymentamountlabel);
	}
}


public class ManagePayments1_Controller implements Initializable {

	@FXML 
	AnchorPane pane;
	
	public CashierController ccInst; //instance pass via manually in CashierController

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		VBox content = new VBox(2);
		content.setStyle("-fx-background-color: #292929");
		content.setSpacing(6);
		ScrollPane scroller = new ScrollPane(content);
		scroller.setFitToWidth(true);
		scroller.setLayoutX(66);
		scroller.setLayoutY(38);
		scroller.setStyle("-fx-background-color: #292929");
		scroller.setPrefSize(768, 513);
		scroller.setVbarPolicy(ScrollBarPolicy.NEVER);
		
		for(int i = 0; i <= 4; i++) {
			content.getChildren().add(new CustomComponentManageTicketOne(ccInst,"Asaad","Noman","03222666528","A","Tenet",630,4000,4,"asaad.abbasi@gmail.com"));
		} pane.getChildren().add(scroller);
	}

}
