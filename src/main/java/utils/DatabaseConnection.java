package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection {
	private static Properties properties = new Properties();
	
	private static String user;
	private static String password;
	private static String driver;
	private static String uri;
	
	static {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		InputStream resourceAsStream = contextClassLoader.getResourceAsStream("db.properties");
		
		try {
			properties.load(resourceAsStream);
			
			driver = properties.getProperty("jdbc.driver");
			uri = properties.getProperty("jdbc.uri");
			user = properties.getProperty("jdbc.user");
			password = properties.getProperty("jdbc.password");
			
			Class.forName(driver);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static Connection getConnection() {
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection(uri, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public static Statement getStatement(Connection connection) {
		Statement statement = null;
		
		try {
			return connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return statement;
	}
	
	public static PreparedStatement getPreparedStatement(Connection connection, String sqlString) {
		try {
			return connection.prepareStatement(sqlString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void close(Connection connection, Statement statement, ResultSet resultSet) {
		try {
			if (null != resultSet) resultSet.close();
			if (null != statement) statement.close();
			if (null != connection) connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
