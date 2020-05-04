package com.parkit.parkingsystem.integration.config;

import com.parkit.parkingsystem.config.DataBaseConfig;

import java.sql.*;

public class DataBaseTestConfigReturnNullConnection extends DataBaseConfig {

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        return null;
    }

}
