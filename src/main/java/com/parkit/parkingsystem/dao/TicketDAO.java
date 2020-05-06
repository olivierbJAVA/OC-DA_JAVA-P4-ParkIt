package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

/**
 * Class managing interactions with the database linked to ticket.
 */
public class TicketDAO {

	private static final Logger logger = LogManager.getLogger("TicketDAO");

	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	/**
	 * Save a ticket in the database when a vehicle enters in the parking.
	 * 
	 * @param ticket The ticket to save
	 * 
	 * @return True if the ticket was saved with success, false if it failed
	 */
	public boolean saveTicket(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.SAVE_TICKET);
			// PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			ps.setInt(1, ticket.getParkingSpot().getNumber());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setDouble(3, ticket.getPrice());
			ps.setTimestamp(4, Timestamp.valueOf(ticket.getInTime()));
			ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (Timestamp.valueOf(ticket.getOutTime())));
			ps.execute();
			return true;
		} catch (SQLException | ClassNotFoundException | NullPointerException ex) {
			logger.error("Error saving ticker", ex);
			return false;
		} finally {
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);
		}
	}

	/**
	 * Get the ticket from the database when a vehicle wants to exit from the
	 * parking.
	 * 
	 * @param vehicleRegNumber The vehicle registration number which wants to exit
	 *                         from the parking
	 * 
	 * @return The ticket
	 */
	public Ticket getTicket(String vehicleRegNumber) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Ticket ticket = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_TICKET);
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				// PARKING_NUMBER, TICKET_ID, PRICE, IN_TIME, OUT_TIME, TYPE
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(rs.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(rs.getDouble(3));
				ticket.setInTime(rs.getTimestamp(4).toLocalDateTime());
				ticket.setOutTime(rs.getTimestamp(5) == null ? null : rs.getTimestamp(5).toLocalDateTime());
			}
		} catch (SQLException | ClassNotFoundException | NullPointerException ex) {
			logger.error("Error getting ticket", ex);
		} finally {
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);
		}
		return ticket;
	}

	/**
	 * Update the ticket when a vehicle wants to exit from the parking.
	 * 
	 * @param ticket The ticket to update
	 * 
	 * @return True if the ticket was updated with success, false if it failed
	 */
	public boolean updateTicket(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataBaseConfig.getConnection();
			// PRICE, OUT_TIME, TICKET_ID
			ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
			ps.setDouble(1, ticket.getPrice());
			ps.setTimestamp(2, Timestamp.valueOf(ticket.getOutTime()));
			ps.setInt(3, ticket.getId());
			ps.execute();
			return true;
		} catch (SQLException | ClassNotFoundException | NullPointerException ex) {
			logger.error("Error updating ticket info", ex);
			return false;
		} finally {
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);
		}
	}

	/**
	 * Indicates if the vehicle registration number already came in the parking.
	 * 
	 * @param vehicleRegNumberUser The vehicle registration number for which we want
	 *                             to know if it already came in the parking
	 * 
	 * @return True if the vehicle already came in the parking, false if it never
	 *         came before
	 */
	public boolean recurringUser(String vehicleRegNumberUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_RECURRING_USER);
			ps.setString(1, vehicleRegNumberUser);
			rs = ps.executeQuery();
			rs.next();
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException | ClassNotFoundException | NullPointerException ex) {
			logger.error("Error fetching recurring user", ex);
		} finally {
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);
		}
		return result;
	}

	/**
	 * Indicates if the vehicle registration number is currently inside the parking.
	 * 
	 * @param vehicleRegNumberUser The vehicle registration number for which we want
	 *                             to know if it is currently inside the parking
	 * 
	 * @return True if the vehicle is currently inside the parking, false if it is
	 *         currently not inside the parking
	 */
	public boolean vehicleInTheParking(String vehicleRegNumberUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_VEHICLE_IN_PARKING);
			ps.setString(1, vehicleRegNumberUser);
			rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getTimestamp(2) == null) {
					result = true;
				}
			}
		} catch (SQLException | ClassNotFoundException | NullPointerException ex) {
			logger.error("Error getting user in the parking", ex);
		} finally {
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);
		}
		return result;
	}
}
