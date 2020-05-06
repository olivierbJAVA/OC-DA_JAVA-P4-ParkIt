package com.parkit.parkingsystem.integration.config;

import java.sql.Connection;
import java.sql.SQLException;

import com.parkit.parkingsystem.config.DataBaseConfig;

public class DataBaseTestConfigReturnNullConnection extends DataBaseConfig {

	// return a null connection to the database for test purposes
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		return null;
	}

}
