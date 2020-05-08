package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareServiceTestsParkingDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareServiceTestsTicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

/**
 * Class including integration tests for Parking System application.
 */
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

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}

	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testParkingACar() {

		// ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(1);

		LocalDateTime inTime = LocalDateTime.now();

		// ACT
		// We process the income of the vehicle
		parkingService.processIncomingVehicle();

		// ASSERT
		// Ticket : we check that the correct ticket has actually been save in the
		// database
		Ticket getSavedTicket = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
		assertThat(getSavedTicket.getVehicleRegNumber()).isEqualTo("ABCDEF");
		assertThat(getSavedTicket.getPrice()).isEqualTo(0.0);
		assertThat(getSavedTicket.getOutTime()).isNull();
		assertThat(getSavedTicket.getInTime()).isBetween(inTime.truncatedTo(ChronoUnit.SECONDS),
				inTime.truncatedTo(ChronoUnit.SECONDS).plusSeconds(5));

		// Parking : we check that the availability of parking one has correctly been
		// updated at false
		boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO
				.getParkingSpotDAOTest_GetAvailabilityParkingOne();
		assertThat(availabilityParking).isFalse();
	}

	@Test
	public void testParkingLotExit_StayLessThanThirtyMinutes_RecurringUserNo() {

		// ARRANGE
		// We create and save in the database a ticket with entry time 15
		// minutes before the test
		Ticket ticketTest = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticketTest.setParkingSpot(parkingSpot);
		ticketTest.setVehicleRegNumber("ABCDEF");
		ticketTest.setPrice(0.0);
		ticketTest.setInTime(LocalDateTime.now().minusMinutes(15));
		ticketTest.setOutTime(null);

		dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATicketInDB(ticketTest);

		LocalDateTime outTime = LocalDateTime.now();

		// ACT
		// We process the exit of the vehicle
		parkingService.processExitingVehicle();

		// ASSERT
		// Ticket : we check that the fare generated and out time are populated
		// correctly in the database
		Ticket ticketUpdatedInDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
		assertThat(ticketUpdatedInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
		assertThat(ticketUpdatedInDB.getOutTime()).isBetween(outTime.truncatedTo(ChronoUnit.SECONDS),
				outTime.truncatedTo(ChronoUnit.SECONDS).plusSeconds(5));
		// Fare should be equal to 0 as the stay time in the parking is less than 30
		// minutes
		assertThat(ticketUpdatedInDB.getPrice()).isEqualTo(0.0);

		// Parking : we check that the availability of parking one has correctly been
		// updated at true
		boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO
				.getParkingSpotDAOTest_GetAvailabilityParkingOne();
		assertThat(availabilityParking).isTrue();
	}

	@Test
	public void testParkingLotExit_StayMoreThanThirtyMinutes_RecurringUserNo() {

		// ARRANGE
		// We create and save in the database a ticket with entry time 45
		// minutes before the test
		Ticket ticketTest = new Ticket();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticketTest.setParkingSpot(parkingSpot);
		ticketTest.setVehicleRegNumber("ABCDEF");
		ticketTest.setPrice(0.0);
		ticketTest.setInTime(LocalDateTime.now().minusMinutes(45));
		ticketTest.setOutTime(null);

		dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATicketInDB(ticketTest);

		LocalDateTime outTime = LocalDateTime.now();

		// ACT
		// We process the exit of the vehicle
		parkingService.processExitingVehicle();

		// ASSERT
		// Ticket : we check that the fare generated and out time are populated
		// correctly in the database
		Ticket ticketUpdatedInDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
		assertThat(ticketUpdatedInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
		assertThat(ticketUpdatedInDB.getOutTime()).isBetween(outTime.truncatedTo(ChronoUnit.SECONDS),
				outTime.truncatedTo(ChronoUnit.SECONDS).plusSeconds(5));
		// Fare should be equal to 3/4 of fare per hour as the stay time in the parking
		// is more than 30 minutes and the user is not a recurring user
		assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(0.75 * Fare.CAR_RATE_PER_HOUR, within(0.01));

		// Parking : we check that the availability of parking one has correctly been
		// updated at true
		boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO
				.getParkingSpotDAOTest_GetAvailabilityParkingOne();
		assertThat(availabilityParking).isTrue();
	}

	@Test
	public void testParkingLotExit_StayLessThanThirtyMinutes_RecurringUserYes() {

		// We simulate two stays : during the first one the user is not a recurring user
		// whereas in the second one he is a recurring user. As both stays are less than
		// 30 minutes fare should be equal to 0 in both cases
		for (int i = 0; i < 2; i++) {

			// ARRANGE
			// We create and save in the database a ticket with entry time 15
			// minutes before the test
			Ticket ticketTest = new Ticket();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			ticketTest.setParkingSpot(parkingSpot);
			ticketTest.setVehicleRegNumber("ABCDEF");
			ticketTest.setPrice(0.0);
			ticketTest.setInTime(LocalDateTime.now().minusMinutes(15));
			ticketTest.setOutTime(null);

			dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATicketInDB(ticketTest);

			// ACT
			// We process the income of the vehicle
			parkingService.processIncomingVehicle();

			LocalDateTime outTime = LocalDateTime.now();

			// Then we process the exit of the vehicle
			parkingService.processExitingVehicle();

			// ASSERT
			// Ticket : we check that the fare generated and out time are populated
			// correctly in the database
			Ticket ticketUpdatedInDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
			assertThat(ticketUpdatedInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
			assertThat(ticketUpdatedInDB.getOutTime()).isBetween(outTime.truncatedTo(ChronoUnit.SECONDS),
					outTime.truncatedTo(ChronoUnit.SECONDS).plusSeconds(5));
			// Fare should be equal to 0 for both stays as the stay time in the parking is
			// less than 30 minutes
			assertThat(ticketUpdatedInDB.getPrice()).isEqualTo(0.0);

			// Parking : we check that the availability of parking one has correctly been
			// updated at true
			boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO
					.getParkingSpotDAOTest_GetAvailabilityParkingOne();
			assertThat(availabilityParking).isTrue();
		}
	}

	@Test
	public void testParkingLotExit_StayMoreThanThirtyMinutes_RecurringUserYes() {

		// We simulate two stays of more then 30 minutes : during the first one the user
		// is not a recurring user (no discount) whereas in the second one he is a
		// recurring user (5% discount)
		for (int i = 0; i < 2; i++) {

			// ARRANGE
			// We create and save in the database a ticket with entry time 45
			// minutes before the test
			Ticket ticketTest = new Ticket();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			ticketTest.setParkingSpot(parkingSpot);
			ticketTest.setVehicleRegNumber("ABCDEF");
			ticketTest.setPrice(0.0);
			ticketTest.setInTime(LocalDateTime.now().minusMinutes(45));
			ticketTest.setOutTime(null);

			dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_SaveATicketInDB(ticketTest);

			// ACT
			// We process the income of the vehicle
			parkingService.processIncomingVehicle();

			LocalDateTime outTime = LocalDateTime.now();

			// We process the exit of the vehicle
			parkingService.processExitingVehicle();

			// ASSERT
			// Ticket : we check that the fare generated and out time are populated
			// correctly in the database
			Ticket ticketUpdatedInDB = dataBasePrepareServiceTestsTicketDAO.ticketDAOTest_GetATicketFromDB("ABCDEF");
			assertThat(ticketUpdatedInDB.getVehicleRegNumber()).isEqualTo("ABCDEF");
			assertThat(ticketUpdatedInDB.getOutTime()).isBetween(outTime.truncatedTo(ChronoUnit.SECONDS),
					outTime.truncatedTo(ChronoUnit.SECONDS).plusSeconds(5));

			if (i == 0) {
				// First stay, not a recurring user -> no discount
				assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(0.75 * Fare.CAR_RATE_PER_HOUR, within(0.01));
			} else {
				// Second stay, recurring user -> 5% discount
				assertThat(ticketUpdatedInDB.getPrice()).isCloseTo(0.95 * 0.75 * Fare.CAR_RATE_PER_HOUR, within(0.01));
			}

			// Parking : we check that the availability of parking one has correctly been
			// updated at true
			boolean availabilityParking = dataBasePrepareServiceTestsParkingDAO
					.getParkingSpotDAOTest_GetAvailabilityParkingOne();
			assertThat(availabilityParking).isTrue();
		}
	}

}
