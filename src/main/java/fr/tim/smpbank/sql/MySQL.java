package fr.tim.smpbank.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

	private static String host = "localhost";
	private static String port = "3306";
	private static String database = "smpbank";
	private static String username = "root";
	private static String password = "";
	
	
	private static Connection connection;
	
	
	
	//V�rifie si connexion
	public static boolean isConnected() {
		return (connection == null ? false : true);
	}
	
	
	public static void connect() throws ClassNotFoundException, SQLException {
		if (!isConnected()) {
			connection = DriverManager.getConnection("jdbc:mysql://"+
					host + ":"+ port + "/"+ database + "?useSSL=false",
					username,password);
		}
	}
	
	public static void disconnect() {
		if (isConnected()) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Connection getConnection() {
		return connection;
	}
	
	
}
