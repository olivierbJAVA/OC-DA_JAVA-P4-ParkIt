package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Class managing the connection and the closures with the database 
 */
public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");

    /**
     * Get a connection to the DataBase
     * 
     * @param inputListSymptoms
     * An ArrayList of Strings containing the list of symptoms not sorted and possibly with duplications
     * 
     * @return A Connection to the DataBase
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/prod","root","rootroot");
    }

    /**
     * Close the connection to the DataBase
     * 
     * @param connection
     * A connection to the DataBase
     */
    public void closeConnection(Connection connection){
        if(connection!=null){
            try {
                connection.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection",e);
            }
        }
    }

    /**
     * Close the preparedStatement
     * 
     * @param preparedStatement
     * A preparedStatement for the DataBase
     */
    public void closePreparedStatement(PreparedStatement preparedStatement) {
        if(preparedStatement!=null){
            try {
                preparedStatement.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement",e);
            }
        }
    }

    /**
     * Close the resultSet
     * 
     * @param resultSet
     * A resultSet for the DataBase
     */
    public void closeResultSet(ResultSet resultSet) {
        if(resultSet!=null){
            try {
                resultSet.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set",e);
            }
        }
    }
}
