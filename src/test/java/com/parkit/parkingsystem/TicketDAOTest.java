package com.parkit.parkingsystem;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfigReturnNullConnection;
import com.parkit.parkingsystem.integration.service.DataBasePrepareServiceTestsTicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

/**
 * Class including unit tests for the TicketDAO Class.
 */
public class TicketDAOTest {

	private static DataBasePrepareServiceTestsTicketDAO dataBasePrepareServiceTestsTicketDAO;

	private TicketDAO ticketDAOUnderTest;

	@BeforeAll
	private static void setUp() throws Exception {
		dataBasePrepareServiceTestsTicketDAO = new DataBasePrepareServiceTestsTicketDAO();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		dataBasePrepareServiceTestsTicketDAO.clearDataBaseEntries();
		ticketDAOUnderTest = new TicketDAO();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void getTicket_TicketExist_ConnectionToDBOK() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		// We save a test ticket in the database
		Ticket ticketToGetFromDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATestTicketInDB();
		ParkingSpot parkingSpotToGetFromDB = ticketToGetFromDB.getParkingSpot();
		double priceToGetFromDB = ticketToGetFromDB.getPrice();
		LocalDateTime inTimeToGetFromDB = ticketToGetFromDB.getInTime();
		LocalDateTime outTimeToGetFromDB = ticketToGetFromDB.getOutTime();

		// ACT
		// We get the test ticket
		Ticket ticketGetFromDB = ticketDAOUnderTest.getTicket("TEST");

		// ASSERT
		// We check that the ticket got from database is the one saved
		Assertions.assertEquals(priceToGetFromDB, ticketGetFromDB.getPrice());
		Assertions.assertEquals(inTimeToGetFromDB, ticketGetFromDB.getInTime());
		Assertions.assertEquals(outTimeToGetFromDB, ticketGetFromDB.getOutTime());
		Assertions.assertEquals(parkingSpotToGetFromDB.getNumber(), ticketGetFromDB.getParkingSpot().getNumber());
		Assertions.assertEquals(parkingSpotToGetFromDB.getParkingType(),
				ticketGetFromDB.getParkingSpot().getParkingType());
	}

	@Test
	public void getTicket_TicketDoesNotExist_ConnectionToDBOK() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();

		// ACT
		// We try to get a ticket that is not in the database
		Ticket ticketGetFromDB = ticketDAOUnderTest.getTicket("NOTINDB");

		// ASSERT
		// We check that the method return null as the ticket is not in the database
		Assertions.assertNull(ticketGetFromDB);
	}

	@Test
	public void getTicket_TicketExist_NoConnectionToDB() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfigReturnNullConnection();
		// We save a test ticket in the database
		dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATestTicketInDB();

		// ACT
		// We try to get the ticket save from the database
		Ticket ticketGetFromDB = ticketDAOUnderTest.getTicket("TEST");

		// ASSERT
		// We check that the method return null as there is no connection to the
		// database
		Assertions.assertNull(ticketGetFromDB);
	}

	@Test
	public void saveTicket_WhenConnectionToDBOK() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		// We create a ticket test to save in database
		Ticket ticketTestToSaveInDB = new Ticket();
		ParkingSpot parkingSpotToSaveInDB = new ParkingSpot(1, ParkingType.CAR, true);
		double priceToSaveInDB = 0.0;
		LocalDateTime inTimeToSaveInDB = LocalDateTime.of(2019, 4, 26, 3, 6, 9);
		LocalDateTime outTimeToSaveInDB = null;
		ticketTestToSaveInDB.setParkingSpot(parkingSpotToSaveInDB);
		ticketTestToSaveInDB.setVehicleRegNumber("TEST");
		ticketTestToSaveInDB.setPrice(priceToSaveInDB);
		ticketTestToSaveInDB.setInTime(inTimeToSaveInDB);
		ticketTestToSaveInDB.setOutTime(outTimeToSaveInDB);

		// ACT
		// We save a test ticket in the database
		boolean resultFromMethodUnderTest = ticketDAOUnderTest.saveTicket(ticketTestToSaveInDB);

		// ASSERT
		// We get the test ticket saved in the database
		Ticket ticketGetFromDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("TEST");
		// We check that the ticket saved in the database is the correct one
		Assertions.assertEquals(priceToSaveInDB, ticketGetFromDB.getPrice());
		Assertions.assertEquals(inTimeToSaveInDB, ticketGetFromDB.getInTime());
		Assertions.assertEquals(outTimeToSaveInDB, ticketGetFromDB.getOutTime());
		Assertions.assertEquals(parkingSpotToSaveInDB.getNumber(), ticketGetFromDB.getParkingSpot().getNumber());
		Assertions.assertEquals(parkingSpotToSaveInDB.getParkingType(),
				ticketGetFromDB.getParkingSpot().getParkingType());
		// We check that the method return true as the execution went well
		Assertions.assertTrue(resultFromMethodUnderTest);
	}

	@Test
	public void saveTicket_NoConnectionToDB() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfigReturnNullConnection();
		// We create a ticket test to save in database
		Ticket ticketToSaveInDB = new Ticket();
		ParkingSpot parkingSpotToSaveInDB = new ParkingSpot(1, ParkingType.CAR, true);
		double priceToSaveInDB = 0.0;
		LocalDateTime inTimeToSaveInDB = LocalDateTime.of(2019, 4, 26, 3, 6, 9);
		LocalDateTime outTimeToSaveInDB = null;
		ticketToSaveInDB.setParkingSpot(parkingSpotToSaveInDB);
		ticketToSaveInDB.setVehicleRegNumber("TEST");
		ticketToSaveInDB.setPrice(priceToSaveInDB);
		ticketToSaveInDB.setInTime(inTimeToSaveInDB);
		ticketToSaveInDB.setOutTime(outTimeToSaveInDB);

		// ACT
		// We try to save the test ticket in the database
		boolean resultFromMethodUnderTest = ticketDAOUnderTest.saveTicket(ticketToSaveInDB);

		// ASSERT
		// We check that the method return false as there was an issue in the execution
		// (no connection to the database)
		Assertions.assertFalse(resultFromMethodUnderTest);
	}

	@Test
	public void updateTicket_TicketExist_ConnectionToDBOK() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		// We create a ticket test to update in database
		Ticket ticketTestToUpdate = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATestTicketInDB();

		ticketTestToUpdate.setOutTime(LocalDateTime.of(2019, 4, 26, 7, 6, 9));
		ticketTestToUpdate.setPrice(123.0);

		// ACT
		// We update the test ticket in the database
		Boolean result = ticketDAOUnderTest.updateTicket(ticketTestToUpdate);

		// ASSERT
		// We get the updated test ticket from the database
		Ticket ticketTestUpdated = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("TEST");
		// We check that the test ticket has rightly been updated
		Assertions.assertEquals(ticketTestToUpdate.getPrice(), ticketTestUpdated.getPrice());
		Assertions.assertEquals(ticketTestToUpdate.getOutTime(), ticketTestUpdated.getOutTime());
		// We check that the method return true as the execution went well
		Assertions.assertTrue(result);
	}

	@Test
	public void updateTicket_NoTicketExist_ConnectionToDBOK() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		// We create a ticket test to update that is not in database
		Ticket ticketTestNotInDB = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		double price = 0.0;
		LocalDateTime inTime = LocalDateTime.of(2019, 4, 26, 3, 6, 9);
		LocalDateTime outTime = null;
		ticketTestNotInDB.setParkingSpot(parkingSpot);
		ticketTestNotInDB.setVehicleRegNumber("NOTINDB"); // Ticket not in database
		ticketTestNotInDB.setPrice(price);
		ticketTestNotInDB.setInTime(inTime);
		ticketTestNotInDB.setOutTime(outTime);

		// ACT
		// We try to update the test ticket that is not in the database
		Boolean result = ticketDAOUnderTest.updateTicket(ticketTestNotInDB);

		// ASSERT
		// We get the updated test ticket from the database
		Ticket ticketTestUpdated = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("NOTINDB");
		// As the ticket to update was not in the database, we check that the ticket is
		// still null (i.e. has not been updated)
		Assertions.assertNull(ticketTestUpdated);
		// We check that the method return false as there was an issue in the execution
		Assertions.assertFalse(result);
	}

	@Test
	public void updateTicket_TicketExist_NoConnection() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfigReturnNullConnection();
		// We save a test ticket to update in the database
		Ticket ticketTestToUpdate = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATestTicketInDB();

		ticketTestToUpdate.setOutTime(LocalDateTime.of(2019, 4, 26, 7, 6, 9));
		ticketTestToUpdate.setPrice(123.0);

		// ACT
		// We try to update the test ticket
		Boolean result = ticketDAOUnderTest.updateTicket(ticketTestToUpdate);

		// ASSERT
		// We get from the database the test ticket that should have been updated
		Ticket ticketTestUpdated = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("TEST");
		// We check that the test ticket has not been updated as there was an issue in
		// the execution (no connection to database)
		Assertions.assertEquals(0.0, ticketTestUpdated.getPrice());
		Assertions.assertNull(ticketTestUpdated.getOutTime());
		// We check that the method return false as there was an issue in the execution
		Assertions.assertFalse(result);
	}

	@Test
	public void recurringUser_FirstStay() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		// We save a test ticket in the database ONE time
		dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATestTicketInDB();

		// ACT
		boolean recurringUser = ticketDAOUnderTest.recurringUser("TEST");

		// ASSERT
		// We check that the user not already came in the parking (i.e. is not a
		// recurring user)
		Assertions.assertFalse(recurringUser);

	}

	@Test
	public void recurringUser_AlreadyStayed() {
		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		// We save the same test ticket (i.e. with the same vehicle registration number)
		// in the database TWO times
		dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATestTicketInDB();
		dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATestTicketInDB();

		// ACT
		boolean recurringUser = ticketDAOUnderTest.recurringUser("TEST");

		// ASSERT
		// We check that the user already came in the parking (i.e. is a recurring user)
		Assertions.assertTrue(recurringUser);
	}

}