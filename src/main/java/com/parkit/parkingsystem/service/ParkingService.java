package com.parkit.parkingsystem.service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

/**
 * Class processing the income and the exit of a vehicle in the parking.
 */
public class ParkingService {

	private static final Logger logger = LogManager.getLogger("ParkingService");

	private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

	private InputReaderUtil inputReaderUtil;
	private ParkingSpotDAO parkingSpotDAO;
	private TicketDAO ticketDAO;

	/**
	 * Constructor of a parking service.
	 * 
	 * @param inputReaderUtil Used to manage the interaction with the user
	 * 
	 * @param parkingSpotDAO  Used to manage the interaction with the database for
	 *                        the parking spot
	 * 
	 * @param ticketDAO       Used to manage the interaction with the database for
	 *                        the ticket
	 */
	public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
		this.inputReaderUtil = inputReaderUtil;
		this.parkingSpotDAO = parkingSpotDAO;
		this.ticketDAO = ticketDAO;
	}

	/**
	 * Method processing the income of a vehicle in the parking, with the following
	 * steps : get the next parking number if available, get the entry vehicle
	 * registration number, check that the vehicle is not already in the parking,
	 * update the parking spot as not available anymore, create a new ticket and
	 * save it in the database, check if the user already came in the parking.
	 */
	public void processIncomingVehicle() {
		try {
			ParkingSpot parkingSpot = getNextParkingSpotIfAvailable();
			if (parkingSpot != null && parkingSpot.getNumber() > 0) {
				String vehicleRegNumber = getVehichleRegNumber();

				// We check that the vehicle which wants to enter into the parking is not
				// already parked
				boolean vehicleAlreadyParked = ticketDAO.vehicleInTheParking(vehicleRegNumber);
				if (vehicleAlreadyParked) {
					System.out.println("\n"
							+ "There is an issue as your vehicle number is already in the parking. Please try again.");
					throw new IllegalArgumentException(
							"Issue with entry : the vehicle number is already in the parking.");
				}

				// We allot this parking space and mark it's availability as false
				parkingSpot.setAvailable(false);
				parkingSpotDAO.updateParking(parkingSpot);

				LocalDateTime inTime = LocalDateTime.now();
				Ticket ticket = new Ticket();
				ticket.setParkingSpot(parkingSpot);
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(0);
				ticket.setInTime(inTime);
				ticket.setOutTime(null);
				ticketDAO.saveTicket(ticket);

				// If the user already came in the parking, we welcome him back
				boolean recurringUser = ticketDAO.recurringUser(vehicleRegNumber);
				if (recurringUser) {
					System.out.println(
							"\nWelcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount.");
				}
				System.out.println("Generated Ticket and saved in DB");
				System.out.println("Please park your vehicle in spot number: " + parkingSpot.getNumber());
				System.out.println("Recorded in-time for vehicle number: " + vehicleRegNumber + " is: " + inTime);
				System.out.println("For your information : a stay less than 30 minutes is free !" + "\n");

			}
		} catch (Exception e) {
			logger.error("Unable to process incoming vehicle", e);
		}
	}

	// Get the vehicle registration number from the user
	private String getVehichleRegNumber() throws Exception {
		System.out.println("Please type the vehicle registration number and press enter key");
		return inputReaderUtil.readVehicleRegistrationNumber();
	}

	// Return the next available parking spot, if any
	private ParkingSpot getNextParkingSpotIfAvailable() {
		int parkingNumber = 0;
		ParkingSpot parkingSpot = null;
		try {
			ParkingType parkingType = getVehichleType();
			parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
			if (parkingNumber > 0) {
				parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
			} else {
				System.out.println(
						"Unfortunately the parking is full. We are sorry for the disagrement.\nDo not hesitate to come back later.");
				throw new Exception("Error fetching parking number from DB. Parking slots might be full");
			}
		} catch (IllegalArgumentException ie) {
			logger.error("Error parsing user input for type of vehicle", ie);
		} catch (Exception e) {
			logger.error("Error fetching next available parking slot", e);
		}
		return parkingSpot;
	}

	// Get the vehicle type from the user
	private ParkingType getVehichleType() {
		System.out.println("Please select vehicle type from menu:");
		System.out.println("1 CAR");
		System.out.println("2 BIKE");
		int input = inputReaderUtil.readSelection();
		switch (input) {
		case 1: {
			return ParkingType.CAR;
		}
		case 2: {
			return ParkingType.BIKE;
		}
		default: {
			System.out.println("Incorrect input provided");
			throw new IllegalArgumentException("Entered input is invalid");
		}
		}
	}

	/**
	 * Method processing the exit of a vehicle in the parking, with the following
	 * steps : get the entry vehicle registration number, check that the vehicle is
	 * currently in the parking, get the ticket corresponding to the vehicle
	 * registration number, check if the user already came in the parking, calculate
	 * the fare price, update the ticket with price and out time in the database,
	 * update the parking spot as available.
	 */
	public void processExitingVehicle() {
		try {
			String vehicleRegNumber = getVehichleRegNumber();

			// We check that the vehicle which wants to exit from the parking is currently
			// parked
			boolean vehicleInTheParking = ticketDAO.vehicleInTheParking(vehicleRegNumber);
			if (!vehicleInTheParking) {
				System.out
						.println("\nThere is an issue as your vehicle number is not in the parking. Please try again.");
				throw new IllegalArgumentException("Issue with exit : vehicle number is not in the parking.");
			}

			Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
			LocalDateTime outTime = LocalDateTime.now();
			ticket.setOutTime(outTime);

			boolean recurringUser = ticketDAO.recurringUser(ticket.getVehicleRegNumber());
			fareCalculatorService.calculateFare(ticket, recurringUser);
			if (ticketDAO.updateTicket(ticket)) {
				ParkingSpot parkingSpot = ticket.getParkingSpot();
				parkingSpot.setAvailable(true);
				parkingSpotDAO.updateParking(parkingSpot);

				// Rounding ticket price
				DecimalFormat df = new DecimalFormat("0.00");
				df.setRoundingMode(RoundingMode.HALF_UP);

				if (ticket.getPrice() == 0.0) {
					// If the stay duration is less than 30 minutes then the user has nothing to pay
					System.out.println("Nothing to pay as a stay less than 30 minutes is free !");
				} else if (recurringUser) {
					// Else, if the user already came in the parking, he got a 5% discount
					System.out.println("As a recurring user of our parking lot, you benefit from a 5% discount.");
					System.out.println("Please pay the parking fare:" + df.format(ticket.getPrice()));
				} else {
					// Else, the user has to pay the normal price
					System.out.println("Please pay the parking fare:" + df.format(ticket.getPrice()));
				}

				System.out.println("Recorded out-time for vehicle number: " + ticket.getVehicleRegNumber() + " is: "
						+ outTime + "\n");
			} else {
				System.out.println("Unable to update ticket information. Error occurred");
			}
		} catch (Exception e) {
			logger.error("Unable to process exiting vehicle", e);
		}
	}
}
