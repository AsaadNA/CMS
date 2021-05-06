import application.bookTickets_1_TableModel;

module CMS {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.desktop;
	requires javafx.base;
	requires java.sql;

	opens application to javafx.graphics, javafx.fxml,javafx.base;
}
