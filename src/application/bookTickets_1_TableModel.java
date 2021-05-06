package application;

public class bookTickets_1_TableModel {
	public String block,movie;
	public float rating;
	public int duration,timeslot,seatsLeft;
	
	public bookTickets_1_TableModel(String block,String movie,float rating,int duration,int timeslot,int seatsLeft) {
		this.block = block;
		this.movie = movie;
		this.rating = rating;
		this.duration = duration;
		this.timeslot = timeslot;
		this.seatsLeft = seatsLeft;
	}

	public int getSeatsLeft() {
		return seatsLeft;
	}

	public void setSeatsLeft(int seatsLeft) {
		this.seatsLeft = seatsLeft;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getMovie() {
		return movie;
	}

	public void setMovie(String movie) {
		this.movie = movie;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(int timeslot) {
		this.timeslot = timeslot;
	}
	
	
}
