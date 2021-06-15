package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
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

class CustomDialog {
	
	//This is for the Add Movie Button
	public CustomDialog() {
		Dialog<Results> dialog = new Dialog<>();
        dialog.setTitle("Add New Movie");
        dialog.setHeaderText("Insert new movie in database");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        TextField movietitlefield = new TextField("Enter movie title");
        TextField moviedurationfield = new TextField("Enter movie duration");
        TextField movieratingfield = new TextField("Enter movie rating");
    	DatePicker moviereleasedatefield = new DatePicker(LocalDate.now());
    	TextField moviedirectorfield = new TextField("Enter movie director");
    	
        dialogPane.setContent(new VBox(8, movietitlefield,moviedurationfield,movieratingfield,moviereleasedatefield,moviedirectorfield));
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new Results(movietitlefield.getText(),moviedurationfield.getText(),movieratingfield.getText(),moviereleasedatefield.getValue(),moviedirectorfield.getText());
            }
            return null;
        });
        
        //This is where SQL Statement gets executed
        Optional<Results> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Results results) -> {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			System.out.println("ADDING: " + results.movietitle + " " + results.movieduration + " " + results.movierating + " " + dtf.format(results.moviereleasedate) + " " + results.moviedirector);
            
            int movieduration = Integer.parseInt(results.movieduration);
            float movierating = Float.parseFloat(results.movierating);
            
            String query = "INSERT INTO MOVIE(movietitle,movieduration,movierating,moviereleasedate,moviedirector) values ('"+results.movietitle+"',"+movieduration+","+movierating+",TO_DATE('"+dtf.format(results.moviereleasedate)+"','yyyy/mm/dd'),'"+results.moviedirector+"')";
            LoginController.getSQL().executeQuery(query);
            ManageMoviesController.reload(); //reload to display the updated data
            
        });
	}
	
	//This is for the update button
	public CustomDialog(CMMC_Struct data) {
		
		Dialog<Results> dialog = new Dialog<>();
        dialog.setTitle("Updating Movie Info");
        dialog.setHeaderText("Update Relevant Info");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        TextField movietitlefield = new TextField(data.movietitle);
        movietitlefield.setEditable(false);
        TextField moviedurationfield = new TextField(Integer.toString(data.movieduration));
        TextField movieratingfield = new TextField(Float.toString(data.movierating));
        
        //Converting the String to date time for CMCC_Struct passed
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        
        DatePicker moviereleasedatefield;
        String date = data.moviereleasedate;
        if(date != null) {
        	date = date.replace(" 00:00:00", "");
        	date = date.replace("-","/");
        	LocalDate localDate = LocalDate.parse(date, formatter);
        	moviereleasedatefield = new DatePicker(localDate);
        } else {
        	moviereleasedatefield = new DatePicker();
        }
     
        TextField moviedirectorfield = new TextField(data.moviedirector);
        
        dialogPane.setContent(new VBox(8, movietitlefield,moviedurationfield,movieratingfield,moviereleasedatefield,moviedirectorfield));
        
        Platform.runLater(movietitlefield::requestFocus);
        
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new Results(movietitlefield.getText(),moviedurationfield.getText(),movieratingfield.getText(),moviereleasedatefield.getValue(),moviedirectorfield.getText());
            }
            return null;
        });
        
        //This is where SQL Statement gets executed
        Optional<Results> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Results results) -> {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			//System.out.println(results.movietitle + " " + results.movieduration + " " + results.movierating + " " + dtf.format(results.moviereleasedate) + " " + results.moviedirector);
            
            //if its an update dialog .. execute command related to updating movie
			int movieduration = Integer.parseInt(results.movieduration);
            float movierating = Float.parseFloat(results.movierating);
            String query = "UPDATE MOVIE SET movieduration=" + movieduration + ",movierating=" + movierating + ",moviereleasedate=TO_DATE('"+dtf.format(results.moviereleasedate)+"','yyyy/mm/dd'),moviedirector='"+results.moviedirector+"' where movietitle='" + results.movietitle + "'";
            LoginController.getSQL().executeQuery(query);
            ManageMoviesController.reload(); //reload to display the updated data
            
        });
	}

	private static class Results {

        String movietitle;
        String movieduration;
        String movierating;
        LocalDate moviereleasedate;
        String moviedirector;
        
        public Results(String movietitle,String movieduration,String movierating,LocalDate moviereleasedate,String moviedirector) {
            this.movietitle= movietitle;
            this.movieduration = movieduration;
            this.movierating = movierating;
            this.moviereleasedate = moviereleasedate;
            this.moviedirector = moviedirector;
        }
    }
}

class CMMC_Struct {
	public String movietitle,moviereleasedate,moviedirector;
	public int movieduration;
	public float movierating;
	public CMMC_Struct(String movietitle,int movieduration,float movierating,String moviereleasedate,String moviedirector) {
		this.movietitle = movietitle;
		this.movieduration = movieduration;
		this.movierating = movierating;
		this.moviereleasedate = moviereleasedate;
		this.moviedirector = moviedirector;
	}
}

class CustomManageMoviesComponent extends AnchorPane {
	
	public CMMC_Struct data;
		
	private Label createLabel(String text , int x , int y) {
		Label result = new Label(text);
		result.setTextFill(Color.WHITE);
		result.setFont(new Font("System",17));
		result.setLayoutX(x);
		result.setLayoutY(y);	
		return result;
	}
	
	public CustomManageMoviesComponent() {
		setStyle("-fx-background-color: #292929");
		//setPrefSize(500, 150);
		
		Button AddMovie = new Button("Add New Movie");
		AddMovie.setLayoutX(500/2 + 20);
		AddMovie.setLayoutY(150/2 - 70);
		AddMovie.setStyle("-fx-foreground-color: #FFFFFF");
		AddMovie.setFont(new Font("System",15));
		
		getChildren().add(AddMovie);
		
		AddMovie.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				CustomDialog addDialog = new CustomDialog();
			}
		});
		
	}
	
	public CustomManageMoviesComponent(CMMC_Struct data) {
		this.data = data;
		
		setStyle("-fx-background-color: #292929");
		setPrefSize(500, 150);

		Label titlelabel = createLabel("Title : " + data.movietitle,42,26);
		Label durationlabel = createLabel("Duration: " + data.movieduration + " Minutes",42,31+28-2);
		Label releasedatelabel = createLabel("Release Date: " + data.moviereleasedate,42,31+28+28+2-2);
		Label ratinglabel = createLabel("Rating: " + data.movierating,42,31+28+28+2+28-2);
		Label directorlabel = createLabel("Director: " + data.moviedirector,42,31+28+28+2+28-2+28);
		
		Button Remove = new Button("Remove");
		Remove.setStyle("-fx-background-color: #88e327");
		Remove.setFont(new Font("System",15));
		Remove.setLayoutX(400);
		Remove.setLayoutY(31+28+28+2+28-2+28);
		
		Button Update = new Button("Update");
		Update.setStyle("-fx-background-color: #88e327");
		Update.setFont(new Font("System",15));
		Update.setLayoutX(400+90);
		Update.setLayoutY(31+28+28+2+28-2+28);
		
		getChildren().add(titlelabel);
		getChildren().add(durationlabel);
		getChildren().add(releasedatelabel);
		getChildren().add(ratinglabel);
		getChildren().add(directorlabel);
		
		getChildren().add(Remove);
		getChildren().add(Update);
		
		///Button actions
		
		Remove.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				LoginController.getSQL().executeQuery("DELETE FROM MOVIE WHERE movietitle='" + data.movietitle + "'");
				ManageMoviesController.reload();
			}
		});
		
		Update.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				CustomDialog updateDialog = new CustomDialog(data);
			}
		});
	}
}

public class ManageMoviesController implements Initializable {

	@FXML
	AnchorPane pane;
	
	public static VBox content = new VBox(2);

	public static void reload() {
		content.getChildren().clear();
		String query = "Select * from movie";
		ResultSet result = LoginController.getSQL().executeQuery(query);
		
		//this is the add movie button
		content.getChildren().add(new CustomManageMoviesComponent());
		try {
			while(result.next()) {
				CMMC_Struct data = new CMMC_Struct(result.getString(2),result.getInt(3),result.getFloat(4),result.getString(5),result.getString(6));
				content.getChildren().add(new CustomManageMoviesComponent(data));
			}
		} catch(SQLException e) {
			System.out.println("SQL ERRROR @ : ManageMoviesController inside Main class");
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
		
		reload();
		
		pane.getChildren().add(scroller);
	}
}
