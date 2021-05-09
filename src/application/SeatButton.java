package application;

import java.io.File;
import java.sql.SQLException;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class SeatButton extends ImageView {
	
	public int x,y,row,column;
	public String seatType;
	public boolean isBooked = false;

	//TODO: GOLD COLOR
	//TODO: SELECTED COLOR
	//TODO: BOOKED COLOR
	
	public boolean isSelected = false;
	
	public SeatButton(int row , int column) { this.row = row; this.column = column; }
	public SeatButton(bookTickets_2Controller bt2Inst , int x , int y , int row , int column) throws SQLException {
		this.x = x;
		this.y = y;
		this.row = row;
		this.column = column;
		
		File file = new File("C:/Workspace/Ongoing/CMS/res/seat.png");
		Image normal_seat_image = new Image(file.toURI().toString());
				
		File newFile = new File("C:/Workspace/Ongoing/CMS/res/seat_selected.png");
		Image selected_seat_image = new Image(newFile.toURI().toString());
		
		setFitWidth(46);
		setFitHeight(46);
		
		setX((double)x);
		setY((double)y);

		setImage(normal_seat_image);
		
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				
				//If it's not booked already then we can select
				//or what not retrieved from db
				if(!isBooked) {
					System.out.println("Clicking booked");
					//simple toggle for selected or not selected
					if(isSelected) {
						isSelected = false;
						setImage(normal_seat_image);
						//remove the seat from the arraylist 
						bt2Inst.selected_seats.remove((ImageView)e.getSource());
					} else {
						isSelected = true;
						setImage(selected_seat_image);
						//add to the arraylist
						bt2Inst.selected_seats.add((SeatButton)e.getSource());
					}
				}			
			}
		});
		
	}
	
}
