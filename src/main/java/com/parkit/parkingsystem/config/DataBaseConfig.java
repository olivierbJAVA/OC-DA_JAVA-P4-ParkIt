package com.parkit.parkingsystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class managing the connection and the closures with the database.
 */
public class DataBaseConfig {

	private static final Logger logger = LogManager.getLogger("DataBaseConfig");

	/**
	 * Get a connection to the DataBase.
	 * 
	 * @return A Connection to the DataBase
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		logger.info("Create DB connection");
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/prod", "root", "rootroot");
	}

	/**
	 * Close the connection to the DataBase.
	 * 
	 * @param connection A connection to the DataBase
	 */
	public void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
				logger.info("Closing DB connection");
			} catch (SQLException e) {
				logger.error("Error while closing connection", e);
			}
		}
	}

	/**
	 * Close the prepared statement.
	 * 
	 * @param preparedStatement A preparedStatement for the DataBase
	 */
	public void closePreparedStatement(PreparedStatement preparedStatement) {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
				logger.info("Closing Prepared Statement");
			} catch (SQLException e) {
				logger.error("Error while closing prepared statement", e);
			}
		}
	}

	/**
	 * Close the result set.
	 * 
	 * @param resultSet A resultSet for the DataBase
	 */
	public void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
				logger.info("Closing Result Set");
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
		}
	}
}
