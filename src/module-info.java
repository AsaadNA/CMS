
module CMS {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.desktop;
	requires javafx.base;
	requires java.sql;
	requires com.google.zxing;
	requires com.google.zxing.javase;

	opens application to javafx.graphics, javafx.fxml,javafx.base;
}
