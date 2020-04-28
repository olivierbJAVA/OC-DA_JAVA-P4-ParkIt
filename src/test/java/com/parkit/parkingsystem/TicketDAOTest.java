package com.parkit.parkingsystem;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareServiceDAOUnitTests;
import com.parkit.parkingsystem.integration.service.DataBasePrepareServiceTicketDAOUnitTests;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketDAOTest {

	private static DataBasePrepareServiceTicketDAOUnitTests dataBasePrepareServiceTicketDAOUnitTest;

	@Test
	public void getTicket_WhenTicketExist() throws Exception {

		// ARRANGE
		TicketDAO ticketDAOUnderTest = new TicketDAO();
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		dataBasePrepareServiceTicketDAOUnitTest = new DataBasePrepareServiceTicketDAOUnitTests();
		Ticket ticketToGetFromDB = dataBasePrepareServiceTicketDAOUnitTest.ticketDAOTest_SaveATicketInDB();

		// Ticket that has been inserted in DB :
		ParkingSpot parkingSpotToGetFromDB = ticketToGetFromDB.getParkingSpot();
		double priceToGetFromDB = ticketToGetFromDB.getPrice();
		LocalDateTime inTimeToGetFromDB = ticketToGetFromDB.getInTime();
		LocalDateTime outTimeToGetFromDB = ticketToGetFromDB.getOutTime();

		// ACT
		Ticket ticketGetFromDB = new Ticket();
		ticketGetFromDB = ticketDAOUnderTest.getTicket("TESTABC");

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
		dataBasePrepareServiceTicketDAOUnitTest = new DataBasePrepareServiceTicketDAOUnitTests();
		dataBasePrepareServiceTicketDAOUnitTest.ticketDAOTest_ClearTicketDB();

		// ACT
		Ticket ticketGetFromDB = new Ticket();
		ticketGetFromDB = ticketDAOUnderTest.getTicket("TESTABC");

		// ASSERT
		Assertions.assertNull(ticketGetFromDB);

	}

	@Test
	public void saveTicket() throws Exception {

		// ARRANGE
		TicketDAO ticketDAOUnderTest = new TicketDAO();
		ticketDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();

		dataBasePrepareServiceTicketDAOUnitTest = new DataBasePrepareServiceTicketDAOUnitTests();

		Ticket ticketToSaveInDB = new Ticket();
		ParkingSpot parkingSpotToSaveInDB = new ParkingSpot(1, ParkingType.CAR, true);
		double priceToSaveInDB = 123.0;
		LocalDateTime inTimeToSaveInDB = LocalDateTime.of(2019, 4, 26, 3, 6, 9);
		LocalDateTime outTimeToSaveInDB = LocalDateTime.of(2019, 4, 26, 7, 6, 9);
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
		Assertions.assertEquals(outTimeToSaveInDB, ticketGetFromDB.getOutTime());
		Assertions.assertEquals(parkingSpotToSaveInDB.getId(), ticketGetFromDB.getParkingSpot().getId());
		Assertions.assertEquals(parkingSpotToSaveInDB.getParkingType(), ticketGetFromDB.getParkingSpot().getParkingType());

		Assertions.assertTrue(resultFromMethodUnderTest);
	}

}