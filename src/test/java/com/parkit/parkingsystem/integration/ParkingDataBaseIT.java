package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareServiceTestsParkingDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareServiceTestsTicketDAO;
import com.parkit.parkingsystem.integration.service.WaitTime;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static DataBasePrepareServiceTestsTicketDAO dataBasePrepareServiceTestsTicketDAO;
	private static DataBasePrepareServiceTestsParkingDAO dataBasePrepareServiceTestsParkingDAO;

	private ParkingService parkingService;
	private ParkingSpotDAO parkingSpotDAO;
	private TicketDAO ticketDAO;
	
	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() {
		dataBasePrepareServiceTestsTicketDAO = new DataBasePrepareServiceTestsTicketDAO();
		dataBasePrepareServiceTestsParkingDAO = new DataBasePrepareServiceTestsParkingDAO();
	}

	@BeforeEach
	private void setUpPerTest() {
		try {

			parkingSpotDAO = new ParkingSpotDAO();
			parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
			ticketDAO = new TicketDAO();
			ticketDAO.dataBaseConfig = dataBaseTestConfig;
			parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
			
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
									
			dataBasePrepareServiceTestsParkingDAO.clearDataBaseEntries();
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
		
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testParkingACar() {
		// TODO: check that a ticket is actually saved in DB and Parking table is updated with availability
		
		//ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(1);
	
		LocalDateTime inTime = LocalDateTime.now();
		
		//ACT
		parkingService.processIncomingVehicle();
		
		//ASSERT
		//Ticket
		Ticket getTicketSaved = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");

		assertThat(getTicketSaved.getVehicleRegNumber()).isEqualTo("ABCDEF");
		assertThat(getTicketSaved.getPrice()).isEqualTo(0.0);
		assertThat(getTicketSaved.getOutTime()).isNull();
		//assertThat(getTicketSaved.getInTime()).isBetween(now.minusSeconds(1), now.plusSeconds(10));
		assertThat(getTicketSaved.getInTime()).isBetween(inTime.truncatedTo(ChronoUnit.SECONDS), inTime.truncatedTo(ChronoUnit.SECONDS).plusSeconds(5));
		
		//Parking
		boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();

		assertThat(availabilityParking).isFalse();
	}

	@Test
	public void testParkingLotExit_WhenStayLessThanThirtyMinutes_WithEffectiveStayTime_RecurringUserNo() {
		// TODO: check that the fare generated and out time are populated correctly in the database

		//ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(1);

		LocalDateTime outTime = LocalDateTime.now();
		
		parkingService.processIncomingVehicle();
		
		//Wait some time for test purposes
		WaitTime waitTimeBeforeProcessExistingVehicle = new WaitTime (1000);
		waitTimeBeforeProcessExistingVehicle.run();
		
		//ACT
		parkingService.processExitingVehicle();
		
		//ASSERT
		//Ticket
		Ticket ticketUpdatedInDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
				
		assertThat(ticketUpdatedInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
		assertThat(ticketUpdatedInDB.getOutTime()).isBetween(outTime.truncatedTo(ChronoUnit.SECONDS), outTime.truncatedTo(ChronoUnit.SECONDS).plusSeconds(5));
		assertThat(ticketUpdatedInDB.getPrice()).isEqualTo(0.0);
		
		//Parking
		boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();

		assertThat(availabilityParking).isTrue();
	}
	
	@Disabled("WARNING : long test which last 45 minutes as the program wait an effective time of 45 minutes to simulate an effective stay of more than 30 minutes")
	@Test
	public void testParkingLotExit_WhenStayMoreThanThirtyMinutes_WithEffectiveStayTime_RecurringUserNo() {
		// TODO: check that the fare generated and out time are populated correctly in the database

		//ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(1);
		
		LocalDateTime outTime = LocalDateTime.now();
	
		parkingService.processIncomingVehicle();
	
		//Wait 31 minutes for tests purposes
		WaitTime waitTimeBeforeProcessExistingVehicle = new WaitTime (45*60*1000);
		waitTimeBeforeProcessExistingVehicle.run();
				
		//ACT
		parkingService.processExitingVehicle();
		
		//ASSERT
		//Ticket
		Ticket ticketUpdatedInDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
				
		assertThat(ticketUpdatedInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
		assertThat(ticketUpdatedInDB.getOutTime()).isBetween(outTime.truncatedTo(ChronoUnit.SECONDS).plusMinutes(45), outTime.truncatedTo(ChronoUnit.SECONDS).plusMinutes(45).plusSeconds(5));
		assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(0.75 * Fare.CAR_RATE_PER_HOUR, within(0.01));
		
		//Parking
		boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();

		assertThat(availabilityParking).isTrue();
	}

	@Test
	public void testParkingLotExit_WhenStayLessThanThirtyMinutes_WithSimulatedStayTime_RecurringUserNo() {
		// TODO: check that the fare generated and out time are populated correctly in the database

		//ARRANGE
		Ticket ticketTest = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticketTest.setParkingSpot(parkingSpot);
		ticketTest.setVehicleRegNumber("ABCDEF");
		ticketTest.setPrice(0.0);
		ticketTest.setInTime(LocalDateTime.now().minusMinutes(15));
		ticketTest.setOutTime(null);
		
		dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATicketInDB(ticketTest);

		LocalDateTime outTime = LocalDateTime.now();
		
		//ACT
		parkingService.processExitingVehicle();
		
		//ASSERT
		//Ticket
		Ticket ticketUpdatedInDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
				
		assertThat(ticketUpdatedInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
		assertThat(ticketUpdatedInDB.getOutTime()).isBetween(outTime.truncatedTo(ChronoUnit.SECONDS), outTime.truncatedTo(ChronoUnit.SECONDS).plusSeconds(5));
		assertThat(ticketUpdatedInDB.getPrice()).isEqualTo(0.0);

		//Parking
		boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();

		assertThat(availabilityParking).isTrue();
	}
	
	@Test
	public void testParkingLotExit_WhenStayMoreThanThirtyMinutes_WithSimulatedStayTime_RecurringUserNo() {
		// TODO: check that the fare generated and out time are populated correctly in the database

		//ARRANGE
		Ticket ticketTest = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticketTest.setParkingSpot(parkingSpot);
		ticketTest.setVehicleRegNumber("ABCDEF");
		ticketTest.setPrice(0.0);
		ticketTest.setInTime(LocalDateTime.now().minusMinutes(45));
		ticketTest.setOutTime(null);
		
		dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATicketInDB(ticketTest);
		
		LocalDateTime outTime = LocalDateTime.now();
		
		//ACT
		parkingService.processExitingVehicle();
		
		//ASSERT
		//Ticket
		Ticket ticketUpdatedInDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
				
		assertThat(ticketUpdatedInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
		assertThat(ticketUpdatedInDB.getOutTime()).isBetween(outTime.truncatedTo(ChronoUnit.SECONDS), outTime.truncatedTo(ChronoUnit.SECONDS).plusSeconds(5));	
		assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(0.75 * Fare.CAR_RATE_PER_HOUR, within(0.01));
		
		//Parking
		boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();

		assertThat(availabilityParking).isTrue();
	}
	
	@Test
	public void testParkingLotExit_WhenStayLessThanThirtyMinutes_WithEffectiveStayTime_RecurringUserYes() {
		// TODO: check that the fare generated and out time are populated correctly in the database

		//ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(1);

		//ACT
		//We simulate two stays : during the first one the user is not a recurring user whereas in the second one he is a recurring user
		for(int i=0;i<2;i++) {
			parkingService.processIncomingVehicle();
			
			//Wait some time for test purposes
			WaitTime waitTimeBeforeProcessExistingVehicle = new WaitTime (1000);
			waitTimeBeforeProcessExistingVehicle.run();
	
			LocalDateTime outTime = LocalDateTime.now();
		
			parkingService.processExitingVehicle();
			
			//ASSERT
			//Ticket
			Ticket ticketUpdatedInDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
			
			assertThat(ticketUpdatedInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
			assertThat(ticketUpdatedInDB.getOutTime()).isBetween(outTime.truncatedTo(ChronoUnit.SECONDS), outTime.truncatedTo(ChronoUnit.SECONDS).plusSeconds(5));
        	assertThat(ticketUpdatedInDB.getPrice()).isEqualTo(0.0);
			
			//Parking
			boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();
			
			assertThat(availabilityParking).isTrue();
		}
	}
	
	@Test
	public void testParkingLotExit_WhenStayMoreThanThirtyMinutes_WithSimulatedStayTime_RecurringUserYes() {
		// TODO: check that the fare generated and out time are populated correctly in the database

		//ARRANGE
		
		//ACT
		//We simulate two stays : during the first one the user is not a recurring user (no discount) whereas in the second one he is a recurring user (5% discount)
		for(int i=0;i<2;i++) {
			
			Ticket ticketTest = new Ticket();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			ticketTest.setParkingSpot(parkingSpot);
			ticketTest.setVehicleRegNumber("ABCDEF");
			ticketTest.setPrice(0.0);
			ticketTest.setInTime(LocalDateTime.now().minusMinutes(45));
			ticketTest.setOutTime(null);
			
			dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATicketInDB(ticketTest);
			
			parkingService.processIncomingVehicle();
			
			LocalDateTime outTime = LocalDateTime.now();

			parkingService.processExitingVehicle();
		
			//ASSERT
			//Ticket
			Ticket ticketUpdatedInDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
				
			assertThat(ticketUpdatedInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
			assertThat(ticketUpdatedInDB.getOutTime()).isBetween(outTime.truncatedTo(ChronoUnit.SECONDS), outTime.truncatedTo(ChronoUnit.SECONDS).plusSeconds(5));	
		
			if(i==0) {
				//first stay, not a recurring user -> no discount
				assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(0.75 * Fare.CAR_RATE_PER_HOUR, within(0.01));
			} else {
				//second stay, not a recurring user -> 5% discount
				assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(0.95 * 0.75 * Fare.CAR_RATE_PER_HOUR, within(0.01));
			}
			
			//Parking
			boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();

			assertThat(availabilityParking).isTrue();
		}
	}
	
	@Test
	public void testParkingACar_UserInParking() {
		
		//ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(1);
		
		//ASSERT : user is not already in the parking before entry
		boolean userInTheParkingBeforeEntry = ticketDAO.vehicleInTheParking("ABCDEF");
		assertThat(userInTheParkingBeforeEntry).isFalse();
		
		//ACT - User entry 
		parkingService.processIncomingVehicle();

		//ASSERT : user is in the parking following its entry
		boolean userInTheParkingAfterEntry = ticketDAO.vehicleInTheParking("ABCDEF");
		assertThat(userInTheParkingAfterEntry).isTrue();
		
		//Wait some time for test purposes
		WaitTime waitTimeBeforeProcessExistingVehicle = new WaitTime (1000);
		waitTimeBeforeProcessExistingVehicle.run();
		
		//ACT - User exit 
		parkingService.processExitingVehicle();
		
		//ASSERT - user is not in the parking anymore following its exit
		boolean userInTheParkingAfterExit = ticketDAO.vehicleInTheParking("ABCDEF");
		assertThat(userInTheParkingAfterExit).isFalse();
	}
}
