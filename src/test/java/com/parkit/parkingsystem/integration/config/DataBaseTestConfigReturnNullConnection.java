package com.parkit.parkingsystem.integration.config;

import com.parkit.parkingsystem.config.DataBaseConfig;

import java.sql.*;

public class DataBaseTestConfigReturnNullConnection extends DataBaseConfig {

	//return a null connection to the database for test purposes
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        return null;
    }

}
