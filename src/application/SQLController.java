package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLController {
	
	private Connection connection = null;
	
	//This will instantiate the connection to the schema
	public SQLController() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String dbURL = "jdbc:oracle:thin:@localhost:1521:orcl";
			String username = "cms";
			String password = "1234";
			connection = DriverManager.getConnection(dbURL, username, password);
			System.out.println("[LOG @ SQL CONTROLLER] : Database connected");
		} catch(SQLException | ClassNotFoundException e) {
			System.out.println("[ERROR @ SQL CONTROLLER (sqlConnection) or (class not found) ] : " + e);
			System.exit(1);
		}
	}
	
	//Execute query and return the resultSet
	public ResultSet executeQuery(String query) {
		ResultSet result = null;
		try {
			Statement stmt = connection.createStatement();  
			result = stmt.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("[ERROR @ SQL CONTROLLER (executeQuery) ] : " + e);
			return null;
		}  return result;
	}
	
	//close the connection
	public boolean closeConnection() { 
		try {
			if(!connection.isClosed()) { connection.close(); }
		} catch (SQLException e) {
			System.out.println("[ERROR @ SQL CONTROLLER (closeConnection) ] : " + e);
			return false;
		} return true; 
	}
}
