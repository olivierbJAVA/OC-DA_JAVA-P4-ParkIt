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
	public void getTicket_WhenTicketExist_WhenConnectionToDBOK() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		Ticket ticketToGetFromDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATestTicketInDB();

		// Ticket get from DB :
		ParkingSpot parkingSpotToGetFromDB = ticketToGetFromDB.getParkingSpot();
		double priceToGetFromDB = ticketToGetFromDB.getPrice();
		LocalDateTime inTimeToGetFromDB = ticketToGetFromDB.getInTime();
		LocalDateTime outTimeToGetFromDB = ticketToGetFromDB.getOutTime();

		// ACT
		Ticket ticketGetFromDB = ticketDAOUnderTest.getTicket("TEST");

		// ASSERT
		Assertions.assertEquals(priceToGetFromDB, ticketGetFromDB.getPrice());
		Assertions.assertEquals(inTimeToGetFromDB, ticketGetFromDB.getInTime());
		Assertions.assertEquals(outTimeToGetFromDB, ticketGetFromDB.getOutTime());
		Assertions.assertEquals(parkingSpotToGetFromDB.getId(), ticketGetFromDB.getParkingSpot().getId());
		Assertions.assertEquals(parkingSpotToGetFromDB.getParkingType(), ticketGetFromDB.getParkingSpot().getParkingType());
	}

	@Test
	public void getTicket_WhenTicketDoesNotExist_WhenConnectionToDBOK() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();

		// ACT
		Ticket ticketGetFromDB = ticketDAOUnderTest.getTicket("NOTINDB");

		// ASSERT
		Assertions.assertNull(ticketGetFromDB);
	}

	@Test
	public void getTicket_WhenTicketExist_WhenNoConnectionToDB() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfigReturnNullConnection();
		dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATestTicketInDB();

		// ACT
		Ticket ticketGetFromDB = ticketDAOUnderTest.getTicket("TEST");

		// ASSERT
		Assertions.assertNull(ticketGetFromDB);
	}

	@Test
	public void getTicket_WhenTicketDoesNotExist_WhenNoConnectionToDB() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfigReturnNullConnection();

		// ACT
		Ticket ticketGetFromDB = ticketDAOUnderTest.getTicket("NOTINDB");

		// ASSERT
		Assertions.assertNull(ticketGetFromDB);
	}
	
	@Test
	public void saveTicket_WhenConnectionToDBOK() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();

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
		boolean resultFromMethodUnderTest = ticketDAOUnderTest.saveTicket(ticketTestToSaveInDB);

		// ASSERT
		Ticket ticketGetFromDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("TEST");

		Assertions.assertEquals(priceToSaveInDB, ticketGetFromDB.getPrice());
		Assertions.assertEquals(inTimeToSaveInDB, ticketGetFromDB.getInTime());
		Assertions.assertEquals(outTimeToSaveInDB, ticketGetFromDB.getOutTime());
		Assertions.assertEquals(parkingSpotToSaveInDB.getId(), ticketGetFromDB.getParkingSpot().getId());
		Assertions.assertEquals(parkingSpotToSaveInDB.getParkingType(),	ticketGetFromDB.getParkingSpot().getParkingType());

		Assertions.assertTrue(resultFromMethodUnderTest);
	}

	@Test
	public void saveTicket_WhenNoConnectionToDB() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfigReturnNullConnection();

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
		boolean resultFromMethodUnderTest = ticketDAOUnderTest.saveTicket(ticketToSaveInDB);

		// ASSERT
		Assertions.assertFalse(resultFromMethodUnderTest);
	}

	@Test
	public void updateTicket_WhenTicketExist_WhenConnectionToDBOK() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		Ticket ticketTestToUpdate = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATestTicketInDB();

		ticketTestToUpdate.setOutTime(LocalDateTime.of(2019, 4, 26, 7, 6, 9));
		ticketTestToUpdate.setPrice(123.0);

		// ACT
		Boolean result = ticketDAOUnderTest.updateTicket(ticketTestToUpdate);

		Ticket ticketTestUpdated = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("TEST");

		// ASSERT
		Assertions.assertEquals(ticketTestToUpdate.getPrice(), ticketTestUpdated.getPrice());
		Assertions.assertEquals(ticketTestToUpdate.getOutTime(), ticketTestUpdated.getOutTime());

		Assertions.assertTrue(result);
	}

	@Test
	public void updateTicket_WhenNoTicketExist_WhenConnectionToDBOK() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();

		Ticket ticketTestNotInDB = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		double price = 0.0;
		LocalDateTime inTime = LocalDateTime.of(2019, 4, 26, 3, 6, 9);
		LocalDateTime outTime = null;
		ticketTestNotInDB.setParkingSpot(parkingSpot);
		ticketTestNotInDB.setVehicleRegNumber("NOTINDB");
		ticketTestNotInDB.setPrice(price);
		ticketTestNotInDB.setInTime(inTime);
		ticketTestNotInDB.setOutTime(outTime);

		// ACT
		Boolean result = ticketDAOUnderTest.updateTicket(ticketTestNotInDB);

		// ASSERT
		Ticket ticketTestUpdated = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("NOTINDB");
		Assertions.assertNull(ticketTestUpdated);
		
		Assertions.assertFalse(result);
	}

	@Test
	public void updateTicket_WhenTicketExist_WhenNoConnection() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfigReturnNullConnection();
		Ticket ticketTestToUpdate = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATestTicketInDB();

		ticketTestToUpdate.setOutTime(LocalDateTime.of(2019, 4, 26, 7, 6, 9));
		ticketTestToUpdate.setPrice(123.0);

		// ACT
		Boolean result = ticketDAOUnderTest.updateTicket(ticketTestToUpdate);

		Ticket ticketTestUpdated = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("TEST");

		// ASSERT
		Assertions.assertEquals(0.0, ticketTestUpdated.getPrice());
		Assertions.assertNull(ticketTestUpdated.getOutTime());

		Assertions.assertFalse(result);
	}

	@Test
	public void updateTicket_WhenNoTicketExist_WhenNoConnection() {

		// ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfigReturnNullConnection();
		dataBasePrepareServiceTestsTicketDAO = new DataBasePrepareServiceTestsTicketDAO();

		Ticket ticketTestNotInDB = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		double price = 333.0;
		LocalDateTime inTime = LocalDateTime.of(2019, 4, 26, 3, 6, 9);
		LocalDateTime outTime = LocalDateTime.of(2019, 4, 26, 7, 6, 9);
		ticketTestNotInDB.setParkingSpot(parkingSpot);
		ticketTestNotInDB.setId(1);
		ticketTestNotInDB.setVehicleRegNumber("NOTINDB");
		ticketTestNotInDB.setPrice(price);
		ticketTestNotInDB.setInTime(inTime);
		ticketTestNotInDB.setOutTime(outTime);

		// ACT
		Boolean result = ticketDAOUnderTest.updateTicket(ticketTestNotInDB);

		// ASSERT
		Ticket ticketTestUpdated = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("NOTINDB");
		Assertions.assertNull(ticketTestUpdated);
		
		Assertions.assertFalse(result);
	}
	
	@Test
	public void recurringUser_WhenFirstStay() {
		
		//ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATestTicketInDB();
		
		//ACT
		boolean recurringUser = ticketDAOUnderTest.recurringUser("TEST");

		//ASSERT
		Assertions.assertFalse(recurringUser);

	}

	@Test
	public void recurringUser_WhenAlreadyStayed() {
		//ARRANGE
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATestTicketInDB();
		dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATestTicketInDB();
		
		//ACT
		boolean recurringUser = ticketDAOUnderTest.recurringUser("TEST");

		//ASSERT
		Assertions.assertTrue(recurringUser);
	}

}