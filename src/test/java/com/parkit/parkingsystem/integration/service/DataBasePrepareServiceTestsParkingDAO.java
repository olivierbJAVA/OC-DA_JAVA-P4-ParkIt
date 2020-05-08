package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class including database services for ParkingSpotDAO tests.
 */
public class DataBasePrepareServiceTestsParkingDAO {

	private DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

	// set parking one availability at TRUE
	public void updateParkingSpotDAOTest_SetParkingOneAvailabilityToTrue() {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dataBaseTestConfig.getConnection();

			ps = connection.prepareStatement("update parking set AVAILABLE = true where PARKING_NUMBER = 1");
			ps.execute();

		} catch (SQLException | ClassNotFoundException | NullPointerException e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closePreparedStatement(ps);
			dataBaseTestConfig.closeConnection(connection);
		}
	}

	// set parking one availability at FALSE
	public void updateParkingSpotDAOTest_SetParkingOneAvailabilityToFalse() {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dataBaseTestConfig.getConnection();

			ps = connection.prepareStatement("update parking set AVAILABLE = false where PARKING_NUMBER = 1");
			ps.execute();

		} catch (SQLException | ClassNotFoundException | NullPointerException e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closePreparedStatement(ps);
			dataBaseTestConfig.closeConnection(connection);
		}
	}

	// get availability of parking one
	public boolean getParkingSpotDAOTest_GetAvailabilityParkingOne() {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			connection = dataBaseTestConfig.getConnection();

			ps = connection.prepareStatement("select AVAILABLE from parking where PARKING_NUMBER = 1");
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getBoolean(1);
			}
		} catch (SQLException | ClassNotFoundException | NullPointerException e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closePreparedStatement(ps);
			dataBaseTestConfig.closeResultSet(rs);
			dataBaseTestConfig.closeConnection(connection);
		}
		return result;
	}

	// set all parking availability at TRUE
	public void updateParkingSpotDAOTest_SetAvailabilityAllToTrue() {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dataBaseTestConfig.getConnection();

			ps = connection.prepareStatement("update parking set AVAILABLE = true");
			ps.execute();

		} catch (SQLException | ClassNotFoundException | NullPointerException e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closePreparedStatement(ps);
			dataBaseTestConfig.closeConnection(connection);
		}
	}

	// set all parking availability at FALSE
	public void updateParkingSpotDAOTest_SetAvailabilityAllToFalse() {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dataBaseTestConfig.getConnection();

			ps = connection.prepareStatement("update parking set AVAILABLE = false");
			ps.execute();

		} catch (SQLException | ClassNotFoundException | NullPointerException e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closePreparedStatement(ps);
			dataBaseTestConfig.closeConnection(connection);
		}
	}

	// clear all database entries
	public void clearDataBaseEntries() {
		Connection connection = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		try {
			connection = dataBaseTestConfig.getConnection();

			// set parking entries to available
			ps1 = connection.prepareStatement("update parking set AVAILABLE = true");
			ps1.execute();

			// clear ticket entries;
			ps2 = connection.prepareStatement("truncate table ticket");
			ps2.execute();

		} catch (SQLException | ClassNotFoundException | NullPointerException e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closePreparedStatement(ps1);
			dataBaseTestConfig.closePreparedStatement(ps2);
			dataBaseTestConfig.closeConnection(connection);
		}
	}

}
