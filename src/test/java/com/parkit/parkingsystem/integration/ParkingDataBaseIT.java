package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
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
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;
	private static DataBasePrepareServiceTestsTicketDAO dataBasePrepareServiceTestsTicketDAO;
	private static DataBasePrepareServiceTestsParkingDAO dataBasePrepareServiceTestsParkingDAO;
	
	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
		dataBasePrepareServiceTestsTicketDAO = new DataBasePrepareServiceTestsTicketDAO();
		dataBasePrepareServiceTestsParkingDAO = new DataBasePrepareServiceTestsParkingDAO();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		//when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testParkingACar() {
		// TODO: check that a ticket is actually saved in DB and Parking table is updated with availability
		
		//ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(1);
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		LocalDateTime now = LocalDateTime.now();
		
		//ACT
		parkingService.processIncomingVehicle();
		
		//ASSERT
		//Ticket
		Ticket getTicketSaved = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");

		assertThat(getTicketSaved.getVehicleRegNumber()).isEqualTo("ABCDEF");
		assertThat(getTicketSaved.getPrice()).isEqualTo(0);
		assertThat(getTicketSaved.getOutTime()).isNull();
		//assertThat(getTicketSaved.getInTime()).isBetween(now.minusSeconds(1), now.plusSeconds(10));
		assertThat(getTicketSaved.getInTime()).isBetween(now.truncatedTo(ChronoUnit.SECONDS), now.truncatedTo(ChronoUnit.SECONDS).plusSeconds(10));
		
		//Parking
		boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();

		assertThat(availabilityParking).isFalse();
	}

	@Test
	public void testParkingLotExit_WhenStayLessThanThirtyMinutes_WithEffectiveStayTime() {
		// TODO: check that the fare generated and out time are populated correctly in the database

		//ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(1);
		
		testParkingACar();
		
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		
		LocalDateTime now = LocalDateTime.now();
		
		//Wait some time for test purposes
		WaitTime waitTimeBeforeProcessExistingVehicle = new WaitTime (1000);
		waitTimeBeforeProcessExistingVehicle.run();
		
		//ACT
		parkingService.processExitingVehicle();
		
		//ASSERT
		//Ticket
		Ticket ticketUpdatedInDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
				
		assertThat(ticketUpdatedInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
		
		//assertThat(ticketUpdatedInDB.getOutTime()).isBetween(now, now.plusSeconds(10));
		assertThat(ticketUpdatedInDB.getOutTime()).isBetween(now.truncatedTo(ChronoUnit.SECONDS), now.truncatedTo(ChronoUnit.SECONDS).plusSeconds(10));
		
		Duration duration = Duration.between(ticketUpdatedInDB.getInTime(),ticketUpdatedInDB.getOutTime());
        long durationLong = duration.getSeconds();
        double durationDouble = durationLong / 3600.0;
        double durationDoubleToPay;
        durationDoubleToPay = (durationDouble < 0.5) ? 0.0 : durationDouble;
        double actualPrice = durationDoubleToPay * Fare.CAR_RATE_PER_HOUR;
		
		assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(actualPrice, within(0.1));
		//assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(actualPrice, byLessThan(0.1));
		
		//Parking
		boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();

		assertThat(availabilityParking).isTrue();
	}
	
	@Disabled("WARNING : long test which last 31 minutes as the program wait an effective time of 31 minutes to simulate an effective stay of more than 30 minutes")
	@Test
	public void testParkingLotExit_WhenStayMoreThanThirtyMinutes_WithEffectiveStayTime() {
		// TODO: check that the fare generated and out time are populated correctly in the database

		//ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(1);
		
		testParkingACar();
		
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		
		LocalDateTime now = LocalDateTime.now();
		
		//Wait some time for tests purposes
		WaitTime waitTimeBeforeProcessExistingVehicle = new WaitTime (31*60*1000);
		waitTimeBeforeProcessExistingVehicle.run();
				
		//ACT
		parkingService.processExitingVehicle();
		
		//ASSERT
		//Ticket
		Ticket ticketUpdatedInDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
				
		assertThat(ticketUpdatedInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
		
		//assertThat(ticketUpdatedInDB.getOutTime()).isBetween(now, now.plusSeconds(10));
		assertThat(ticketUpdatedInDB.getOutTime()).isBetween(now.truncatedTo(ChronoUnit.SECONDS), now.truncatedTo(ChronoUnit.SECONDS).plusSeconds(10));
		
		Duration duration = Duration.between(ticketUpdatedInDB.getInTime(),ticketUpdatedInDB.getOutTime());
        long durationLong = duration.getSeconds();
        double durationDouble = durationLong / 3600.0;
        double durationDoubleToPay;
        durationDoubleToPay = (durationDouble < 0.5) ? 0.0 : durationDouble;
        double actualPrice = durationDoubleToPay * Fare.CAR_RATE_PER_HOUR;
		
		assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(actualPrice, within(0.1));
		//assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(actualPrice, byLessThan(0.1));
		
		//Parking
		boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();

		assertThat(availabilityParking).isTrue();
	}


	@Test
	public void testParkingLotExit_WhenStayLessThanThirtyMinutes_WithSimulatedStayTime() {
		// TODO: check that the fare generated and out time are populated correctly in the database

		//ARRANGE
		//testParkingACar();
		
		Ticket ticketTest = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		ticketTest.setParkingSpot(parkingSpot);
		ticketTest.setId(1);
		ticketTest.setVehicleRegNumber("ABCDEF");
		ticketTest.setPrice(0.0);
		ticketTest.setInTime(LocalDateTime.now().minusMinutes(15));
		ticketTest.setOutTime(null);
		
		LocalDateTime now = LocalDateTime.now();
		//LocalDateTime LocalDateTime2 = LocalDateTime(now.getSecond().to);
		
		dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATicketInDB(ticketTest);
		
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		
		//LocalDateTime now = LocalDateTime.now();
		
		//ACT
		parkingService.processExitingVehicle();
		
		//ASSERT
		//Ticket
		Ticket ticketUpdatedInDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
				
		assertThat(ticketUpdatedInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
		
		//assertThat(ticketUpdatedInDB.getOutTime()).isBetween(now, now.plusSeconds(10));
		//assertThat(ticketUpdatedInDB.getOutTime()).isBetween(now.minusSeconds(1), now.plusSeconds(10));
		assertThat(ticketUpdatedInDB.getOutTime()).isBetween(now.truncatedTo(ChronoUnit.SECONDS), now.truncatedTo(ChronoUnit.SECONDS).plusSeconds(10));
		
		Duration duration = Duration.between(ticketUpdatedInDB.getInTime(),ticketUpdatedInDB.getOutTime());
        long durationLong = duration.getSeconds();
        double durationDouble = durationLong / 3600.0;
        double durationDoubleToPay = (durationDouble < 0.5) ? 0.0 : durationDouble;
        double actualPrice = durationDoubleToPay * Fare.CAR_RATE_PER_HOUR;
		
		assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(actualPrice, within(0.1));
		//assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(actualPrice, byLessThan(0.1));
		
		//Parking
		boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();

		assertThat(availabilityParking).isTrue();
	}
	
	@Test
	public void testParkingLotExit_WhenStayMoreThanThirtyMinutes_WithSimulatedStayTime() {
		// TODO: check that the fare generated and out time are populated correctly in the database

		//ARRANGE
		//testParkingACar();
		
		Ticket ticketTest = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		ticketTest.setParkingSpot(parkingSpot);
		ticketTest.setId(1);
		ticketTest.setVehicleRegNumber("ABCDEF");
		ticketTest.setPrice(0.0);
		ticketTest.setInTime(LocalDateTime.now().minusMinutes(35));
		ticketTest.setOutTime(null);
		
		dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATicketInDB(ticketTest);
		
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		
		LocalDateTime now = LocalDateTime.now();
		
		//ACT
		parkingService.processExitingVehicle();
		
		//ASSERT
		//Ticket
		Ticket ticketUpdatedInDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
				
		assertThat(ticketUpdatedInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
		
		//assertThat(ticketUpdatedInDB.getOutTime()).isBetween(now.minusSeconds(1), now.plusSeconds(10));
		assertThat(ticketUpdatedInDB.getOutTime()).isBetween(now.truncatedTo(ChronoUnit.SECONDS), now.truncatedTo(ChronoUnit.SECONDS).plusSeconds(10));	
		
		Duration duration = Duration.between(ticketUpdatedInDB.getInTime(),ticketUpdatedInDB.getOutTime());
        long durationLong = duration.getSeconds();
        double durationDouble = durationLong / 3600.0;
        double durationDoubleToPay = (durationDouble < 0.5) ? 0.0 : durationDouble;
        double actualPrice = durationDoubleToPay * Fare.CAR_RATE_PER_HOUR;
		
		assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(actualPrice, within(0.1));
		//assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(actualPrice, byLessThan(0.1));
		
		//Parking
		boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();

		assertThat(availabilityParking).isTrue();
	}
}
