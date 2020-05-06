package com.parkit.parkingsystem.integration.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class DataBasePrepareServiceTestsTicketDAO {

	public static final String SAVE_TICKET_TEST = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
	public static final String UPDATE_TICKET_TEST = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
	public static final String GET_TICKET_TEST = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME DESC limit 1";

	private DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

	// save a test ticket in the database
	public Ticket ticketDAOTest_SaveATestTicketInDB() {

		Ticket ticketTest = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		double price = 0.0;
		LocalDateTime inTime = LocalDateTime.of(2019, 4, 26, 3, 6, 9);
		LocalDateTime outTime = null;
		ticketTest.setParkingSpot(parkingSpot);
		ticketTest.setId(1);
		ticketTest.setVehicleRegNumber("TEST");
		ticketTest.setPrice(price);
		ticketTest.setInTime(inTime);
		ticketTest.setOutTime(outTime);

		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dataBaseTestConfig.getConnection();

			ps = connection.prepareStatement(SAVE_TICKET_TEST);
			// PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME
			ps.setInt(1, ticketTest.getParkingSpot().getNumber());
			ps.setString(2, ticketTest.getVehicleRegNumber());
			ps.setDouble(3, ticketTest.getPrice());
			ps.setTimestamp(4, Timestamp.valueOf(ticketTest.getInTime()));
			ps.setTimestamp(5, ticketTest.getOutTime() == null ? null : Timestamp.valueOf(ticketTest.getOutTime()));

			ps.execute();

		} catch (SQLException | ClassNotFoundException | NullPointerException e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closePreparedStatement(ps);
			dataBaseTestConfig.closeConnection(connection);
		}
		return ticketTest;
	}

	// get a ticket from the database
	public Ticket ticketDAOTest_GetATicketFromDB(String vehicleRegNumber) {

		Ticket ticket = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = dataBaseTestConfig.getConnection();

			ps = connection.prepareStatement(GET_TICKET_TEST);
			// PARKING_NUMBER, ID, PRICE, IN_TIME, OUT_TIME, TYPE
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(rs.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(rs.getDouble(3));
				ticket.setInTime(rs.getTimestamp(4).toLocalDateTime());
				ticket.setOutTime(rs.getTimestamp(5) == null ? null : rs.getTimestamp(5).toLocalDateTime());
			}

		} catch (SQLException | ClassNotFoundException | NullPointerException e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closePreparedStatement(ps);
			dataBaseTestConfig.closeResultSet(rs);
			dataBaseTestConfig.closeConnection(connection);
		}
		return ticket;
	}

	// save a ticket in the database
	public boolean ticketDAOTest_SaveATicketInDB(Ticket ticket) {

		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dataBaseTestConfig.getConnection();

			ps = connection.prepareStatement(SAVE_TICKET_TEST);
			// PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME
			ps.setInt(1, ticket.getParkingSpot().getNumber());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setDouble(3, ticket.getPrice());
			ps.setTimestamp(4, Timestamp.valueOf(ticket.getInTime()));
			ps.setTimestamp(5, ticket.getOutTime() == null ? null : Timestamp.valueOf(ticket.getOutTime()));

			ps.execute();

		} catch (SQLException | ClassNotFoundException | NullPointerException e) {
			e.printStackTrace();
			return false;
		} finally {
			dataBaseTestConfig.closePreparedStatement(ps);
			dataBaseTestConfig.closeConnection(connection);
		}
		return true;
	}

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
