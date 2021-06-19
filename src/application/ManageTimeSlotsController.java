package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

class CustomManageTimeDialog {
	
	public CustomManageTimeDialog() {
		Dialog<Results> dialog = new Dialog<>();
        dialog.setTitle("Add A New Timeslot");
        dialog.setHeaderText("Insert new timeslot in database");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
    	DatePicker timestamp_dateField= new DatePicker(LocalDate.now());
    	TextField timestamp_timeField = new TextField("Enter time here");
    	
        dialogPane.setContent(new VBox(8, timestamp_dateField,timestamp_timeField));
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new Results(timestamp_dateField.getValue(),timestamp_timeField.getText());
                
            }
            return null;
        });
        
        //This is where SQL Statement gets executed
        Optional<Results> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Results results) -> {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			String query = "INSERT INTO TIMESTAMPS (timestampdatetime) values (TO_DATE('" + dtf.format(results.timestamp_date) + " " + results.timestamp_time + "','yyyy/mm/dd hh12:mi'))";
			//System.out.println(query);
			LoginController.getSQL().executeQuery(query);
			ManageTimeSlotsController.reload();
        });
	}
	
	private static class Results {
		
        LocalDate timestamp_date;
        String timestamp_time;
        
        public Results(LocalDate timestamp_date , String timestamp_time) {
          this.timestamp_date = timestamp_date;
          this.timestamp_time = timestamp_time;
        }
    }
	
}

class CustomManageTimeslotsComponent extends AnchorPane {
	
	String timeslot;
	private Label createLabel(String text , int x , int y) {
		Label result = new Label(text);
		result.setTextFill(Color.WHITE);
		result.setFont(new Font("System",20));
		result.setLayoutX(x);
		result.setLayoutY(y);	
		return result;
	}
	
	public CustomManageTimeslotsComponent() {
		setStyle("-fx-background-color: #292929");
		
		Button AddTimeSlot = new Button("Add New Time Slot");
		AddTimeSlot.setLayoutX(500/2 + 20);
		AddTimeSlot.setLayoutY(150/2 - 70);
		AddTimeSlot.setStyle("-fx-foreground-color: #FFFFFF");
		AddTimeSlot.setFont(new Font("System",12));
		
		getChildren().add(AddTimeSlot);		
		
		AddTimeSlot.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				CustomManageTimeDialog dialog = new CustomManageTimeDialog();
			}
		});
	}
	
	public CustomManageTimeslotsComponent(String timeslot) {
		this.timeslot = timeslot;
		setStyle("-fx-background-color: #292929");
		setPrefSize(600, 100);

		Label timeslotlabel = createLabel("Timeslot : " + timeslot,42,26);
		
		Button Remove = new Button("Remove");
		Remove.setStyle("-fx-background-color: #88e327");
		Remove.setFont(new Font("System",15));
		Remove.setLayoutX(500);
		Remove.setLayoutY(26);
				
		getChildren().add(timeslotlabel);
		getChildren().add(Remove);
		
		//Deleting the timestampdatetime here
		Remove.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				String query = "DELETE FROM TIMESTAMPS where timestampdatetime=to_date('" + timeslot +"', 'yyyy/mm/dd hh12:mi')";
				LoginController.getSQL().executeQuery(query);
				ManageTimeSlotsController.reload();
			}
		});
	}
}

public class ManageTimeSlotsController implements Initializable {
	
	@FXML
	AnchorPane pane;
	
	public static VBox content = new VBox(2);

	public static void reload() {
		content.getChildren().clear();
		String query = "select to_char(timestampdatetime,'yyyy/mm/dd hh12:mi') from timestamps";
		ResultSet result = LoginController.getSQL().executeQuery(query);
		
		//this is the add movie button
		content.getChildren().add(new CustomManageTimeslotsComponent());
		try {
			while(result.next()) {
				content.getChildren().add(new CustomManageTimeslotsComponent(result.getString(1)));
			}
		} catch(SQLException e) {
			System.out.println("SQL ERRROR @ : ManageMoviesController inside Main class");
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		content.setStyle("-fx-background-color: #292929");
		content.setSpacing(0);
		ScrollPane scroller = new ScrollPane(content);
		scroller.setLayoutX(66);
		scroller.setLayoutY(86);
		
		scroller.setStyle("-fx-background: rgb(41,41,41); -fx-padding:0;");
		scroller.setFitToHeight(true);
		scroller.setFitToWidth(true);
		
		scroller.setPrefSize(768, 513);
		scroller.setVbarPolicy(ScrollBarPolicy.NEVER);
		
		reload();
		
		pane.getChildren().add(scroller);
	}

}
