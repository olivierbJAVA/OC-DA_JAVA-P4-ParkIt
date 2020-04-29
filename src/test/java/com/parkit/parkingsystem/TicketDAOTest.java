package com.parkit.parkingsystem;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfigReturnNullConnection;
import com.parkit.parkingsystem.integration.service.DataBasePrepareServiceTestsParkingDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareServiceTestsTicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketDAOTest {

	private static DataBasePrepareServiceTestsTicketDAO dataBasePrepareServiceTicketDAOUnitTest;

    @BeforeAll
    private static void setUp() throws Exception{
    	dataBasePrepareServiceTicketDAOUnitTest = new DataBasePrepareServiceTestsTicketDAO();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
    	dataBasePrepareServiceTicketDAOUnitTest.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }
	
	@Test
	public void getTicket_WhenTicketExist_WhenConnectionToDBOK() throws Exception {

		// ARRANGE
		TicketDAO ticketDAOUnderTest = new TicketDAO();
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		Ticket ticketToGetFromDB = dataBasePrepareServiceTicketDAOUnitTest.ticketDAOTest_SaveATicketInDB();

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
	public void getTicket_WhenTicketDoesNotExist() throws Exception {

		// ARRANGE
		TicketDAO ticketDAOUnderTest = new TicketDAO();
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		dataBasePrepareServiceTicketDAOUnitTest.ticketDAOTest_ClearTicketDB();

		// ACT
		Ticket ticketGetFromDB = ticketDAOUnderTest.getTicket("TESTNOK");

		// ASSERT
		Assertions.assertNull(ticketGetFromDB);

	}

	@Test
	public void getTicket_WhenNoConnectionToDB() throws Exception {

		// ARRANGE
		TicketDAO ticketDAOUnderTest = new TicketDAO();
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfigReturnNullConnection();
		Ticket ticketToGetFromDB = dataBasePrepareServiceTicketDAOUnitTest.ticketDAOTest_SaveATicketInDB();
		
		// ACT
		Ticket ticketGetFromDB = ticketDAOUnderTest.getTicket("TEST");

		// ASSERT
		Assertions.assertNull(ticketGetFromDB);
	}
	
	@Test
	public void saveTicket_WhenConnectionToDBOK() throws Exception {

		// ARRANGE
		TicketDAO ticketDAOUnderTest = new TicketDAO();
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();

		Ticket ticketToSaveInDB = new Ticket();
		ParkingSpot parkingSpotToSaveInDB = new ParkingSpot(1, ParkingType.CAR, true);
		double priceToSaveInDB = 123.0;
		LocalDateTime inTimeToSaveInDB = LocalDateTime.of(2019, 4, 26, 3, 6, 9);
		LocalDateTime outTimeToSaveInDB = null;
		//LocalDateTime outTimeToSaveInDB = LocalDateTime.of(2019, 4, 26, 7, 6, 9);
		ticketToSaveInDB.setParkingSpot(parkingSpotToSaveInDB);
		// ticket.setId(1);
		ticketToSaveInDB.setVehicleRegNumber("ABC123");
		ticketToSaveInDB.setPrice(priceToSaveInDB);
		ticketToSaveInDB.setInTime(inTimeToSaveInDB);
		ticketToSaveInDB.setOutTime(outTimeToSaveInDB);

		// ACT
		boolean resultFromMethodUnderTest = ticketDAOUnderTest.saveTicket(ticketToSaveInDB);

		// ASSERT
		Ticket ticketGetFromDB = dataBasePrepareServiceTicketDAOUnitTest.ticketDAOTest_GetATicketFromDB("ABC123");

		Assertions.assertEquals(priceToSaveInDB, ticketGetFromDB.getPrice());
		Assertions.assertEquals(inTimeToSaveInDB, ticketGetFromDB.getInTime());
		Assertions.assertEquals(outTimeToSaveInDB,ticketGetFromDB.getOutTime());
		Assertions.assertEquals(parkingSpotToSaveInDB.getId(), ticketGetFromDB.getParkingSpot().getId());
		Assertions.assertEquals(parkingSpotToSaveInDB.getParkingType(), ticketGetFromDB.getParkingSpot().getParkingType());

		Assertions.assertTrue(resultFromMethodUnderTest);
	}

	@Test
	public void saveTicket_WhenNoConnectionToDB() throws Exception {

		// ARRANGE
		TicketDAO ticketDAOUnderTest = new TicketDAO();
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfigReturnNullConnection();

		Ticket ticketToSaveInDB = new Ticket();
		ParkingSpot parkingSpotToSaveInDB = new ParkingSpot(1, ParkingType.CAR, true);
		double priceToSaveInDB = 123.0;
		LocalDateTime inTimeToSaveInDB = LocalDateTime.of(2019, 4, 26, 3, 6, 9);
		LocalDateTime outTimeToSaveInDB = null;
		//LocalDateTime outTimeToSaveInDB = LocalDateTime.of(2019, 4, 26, 7, 6, 9);
		ticketToSaveInDB.setParkingSpot(parkingSpotToSaveInDB);
		// ticket.setId(1);
		ticketToSaveInDB.setVehicleRegNumber("ABC123");
		ticketToSaveInDB.setPrice(priceToSaveInDB);
		ticketToSaveInDB.setInTime(inTimeToSaveInDB);
		ticketToSaveInDB.setOutTime(outTimeToSaveInDB);

		// ACT
		boolean resultFromMethodUnderTest = ticketDAOUnderTest.saveTicket(ticketToSaveInDB);
		
		// ASSERT
		Assertions.assertFalse(resultFromMethodUnderTest);
	}
	
}