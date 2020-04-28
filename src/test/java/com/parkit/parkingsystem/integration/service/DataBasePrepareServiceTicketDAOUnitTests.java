package com.parkit.parkingsystem.integration.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class DataBasePrepareServiceTicketDAOUnitTests {

	public static final String SAVE_TICKET_TEST = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
	public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
	public static final String GET_TICKET_TEST = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME DESC limit 1";

	DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	//DataBaseConfig dataBaseTestConfig = new DataBaseConfig();

	@Test
	public Ticket ticketDAOTest_SaveATicketInDB() {

		Ticket ticket = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		double price = 123.0;
		LocalDateTime inTime = LocalDateTime.of(2019, 4, 26, 3, 6, 9);
		LocalDateTime outTime = LocalDateTime.of(2019, 4, 26, 7, 6, 9);
		ticket.setParkingSpot(parkingSpot);
		// ticket.setId(1);
		ticket.setVehicleRegNumber("TESTABC");
		ticket.setPrice(price);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);

		Connection connection = null;
		try {
			connection = dataBaseTestConfig.getConnection();

			PreparedStatement ps = connection.prepareStatement(SAVE_TICKET_TEST);
			// PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			// values(?,?,?,?,?)";
			ps.setInt(1, ticket.getParkingSpot().getId());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setDouble(3, ticket.getPrice());
			ps.setTimestamp(4, Timestamp.valueOf(ticket.getInTime()));
			ps.setTimestamp(5, Timestamp.valueOf(ticket.getOutTime()));
			ps.execute();

			// clear ticket entries;
			// connection.prepareStatement("truncate table ticket").execute();
			dataBaseTestConfig.closePreparedStatement(ps);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closeConnection(connection);
		}
		return ticket;
	}

	@Test
	public void ticketDAOTest_ClearTicketDB() {

		Connection connection = null;
		try {
			connection = dataBaseTestConfig.getConnection();

			// clear ticket entries
			PreparedStatement ps = connection.prepareStatement("truncate table ticket");
			ps.execute();

			dataBaseTestConfig.closePreparedStatement(ps);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closeConnection(connection);
		}
	}

	@Test
	public Ticket ticketDAOTest_GetATicketFromDB(String vehicleRegNumber) {

		Ticket ticket = null;
		Connection connection = null;

		try {
			connection = dataBaseTestConfig.getConnection();

			PreparedStatement ps = connection.prepareStatement(GET_TICKET_TEST);
			// PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";

			ps.setString(1, vehicleRegNumber);
			ResultSet rs = ps.executeQuery();
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

			dataBaseTestConfig.closePreparedStatement(ps);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closeConnection(connection);
		}
		return ticket;
	}
}
