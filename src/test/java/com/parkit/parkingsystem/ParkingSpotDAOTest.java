package com.parkit.parkingsystem;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfigReturnNullConnection;
import com.parkit.parkingsystem.integration.service.DataBasePrepareServiceTestsParkingDAO;
import com.parkit.parkingsystem.model.ParkingSpot;

/**
 * Class including unit tests for the ParkingSpotDAO Class.
 */
public class ParkingSpotDAOTest {

	private static DataBasePrepareServiceTestsParkingDAO dataBasePrepareServiceTestsParkingDAO;

	private ParkingSpotDAO parkingSpotDAOUnderTest;

	@BeforeAll
	private static void setUp() throws Exception {
		dataBasePrepareServiceTestsParkingDAO = new DataBasePrepareServiceTestsParkingDAO();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		dataBasePrepareServiceTestsParkingDAO.clearDataBaseEntries();
		parkingSpotDAOUnderTest = new ParkingSpotDAO();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void updateParking_AvailabilityToTrue_ConnectionToDBOK() {

		// ARRANGE
		parkingSpotDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		// We put the availability of parking one at false
		dataBasePrepareServiceTestsParkingDAO.updateParkingSpotDAOTest_SetParkingOneAvailabilityToFalse();

		// ACT
		ParkingSpot parkingSpotTest = new ParkingSpot(1, ParkingType.CAR, true);
		// We update the availability of parking one at true
		boolean resultFromMethodUnderTest = parkingSpotDAOUnderTest.updateParking(parkingSpotTest);

		// ASSERT
		boolean resultFromDB = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();

		// We check that the availability of parking one has been updated at true and
		// the method return true (as execution went well)
		Assertions.assertTrue(resultFromDB);
		Assertions.assertTrue(resultFromMethodUnderTest);
	}

	@Test
	public void updateParking_AvailabilityToTrue_NoConnectionToDB() {

		// ARRANGE
		parkingSpotDAOUnderTest.dataBaseConfig = new DataBaseTestConfigReturnNullConnection();
		// We put the availability of parking one at false
		dataBasePrepareServiceTestsParkingDAO.updateParkingSpotDAOTest_SetParkingOneAvailabilityToFalse();

		// ACT
		ParkingSpot parkingSpotTest = new ParkingSpot(1, ParkingType.CAR, true);
		// We try to update the availability of parking one at true
		boolean resultFromMethodUnderTest = parkingSpotDAOUnderTest.updateParking(parkingSpotTest);

		// ASSERT
		boolean resultFromDB = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();

		// As there is not connection to the database, we check that the availability of
		// parking one has not been updated and the method return false (as there was an
		// issue in the execution)
		Assertions.assertFalse(resultFromDB);
		Assertions.assertFalse(resultFromMethodUnderTest);
	}

	@Test
	public void updateParking_AvailabilityToFalse_ConnectionToDBOK() {

		// ARRANGE
		parkingSpotDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		// We put the availability of parking one at true
		dataBasePrepareServiceTestsParkingDAO.updateParkingSpotDAOTest_SetParkingOneAvailabilityToTrue();

		// ACT
		ParkingSpot parkingSpotTest = new ParkingSpot(1, ParkingType.CAR, false);
		// We update the availability of parking one at false
		boolean resultFromMethodUnderTest = parkingSpotDAOUnderTest.updateParking(parkingSpotTest);

		// ASSERT
		boolean resultFromDB = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();
		// We check that the availability of parking one has been updated at false and
		// the method return true (as execution went well)
		Assertions.assertFalse(resultFromDB);
		Assertions.assertTrue(resultFromMethodUnderTest);

	}

	@Test
	public void updateParking_AvailabilityToFalse_NoConnectionToDB() {

		// ARRANGE
		parkingSpotDAOUnderTest.dataBaseConfig = new DataBaseTestConfigReturnNullConnection();
		// We put the availability of parking one at true
		dataBasePrepareServiceTestsParkingDAO.updateParkingSpotDAOTest_SetParkingOneAvailabilityToTrue();

		// ACT
		ParkingSpot parkingSpotTest = new ParkingSpot(1, ParkingType.CAR, false);
		// We try to update the availability of parking one at false
		boolean resultFromMethodUnderTest = parkingSpotDAOUnderTest.updateParking(parkingSpotTest);

		// ASSERT
		boolean resultFromDB = dataBasePrepareServiceTestsParkingDAO.getParkingSpotDAOTest_GetAvailabilityParkingOne();
		// As there is not connection to the database, we check that the availability of
		// parking one has not been updated and the method return false (as there was an
		// issue in the execution)
		Assertions.assertTrue(resultFromDB);
		Assertions.assertFalse(resultFromMethodUnderTest);

	}

	@Test
	public void getNextAvailableSlot_AllSlotAreAvailable_ConnectionToDBOK() {

		// ARRANGE
		parkingSpotDAOUnderTest.dataBaseConfig = new DataBaseTestConfig();
		// We put all parking availability at true
		dataBasePrepareServiceTestsParkingDAO.updateParkingSpotDAOTest_SetAvailabilityAllToTrue();

		// ACT
		// We get the next available slot
		int result = parkingSpotDAOUnderTest.getNextAvailableSlot(ParkingType.CAR);

		// ASSERT
		// We check that the next parking available returned is the first one
		Assertions.assertEquals(1, result);
	}

	@Test
	public void getNextAvailableSlot_AllSlotAreAvailable_NoConnectionToDB() {

		// ARRANGE
		parkingSpotDAOUnderTest.dataBaseConfig = new DataBaseTestConfigReturnNullConnection();
		// We put all parking availability at true
		dataBasePrepareServiceTestsParkingDAO.updateParkingSpotDAOTest_SetAvailabilityAllToTrue();

		// ACT
		// We try to get the next available slot
		int result = parkingSpotDAOUnderTest.getNextAvailableSlot(ParkingType.CAR);

		// ASSERT
		// We check that the method return -1 as there was an issue in the execution
		// (because no connection to database)
		Assertions.assertEquals(-1, result);
	}

	@Test
	public void getNextAvailableSlot_NoSlotIsAvailable_NoConnectionToDB() {
		int result = 10;

		// ARRANGE
		parkingSpotDAOUnderTest.dataBaseConfig = new DataBaseTestConfigReturnNullConnection();
		// We put all parking availability at false
		dataBasePrepareServiceTestsParkingDAO.updateParkingSpotDAOTest_SetAvailabilityAllToFalse();

		// ACT
		// We try to get the next available slot
		result = parkingSpotDAOUnderTest.getNextAvailableSlot(ParkingType.CAR);

		// ASSERT
		// We check that the method return -1 as there was an issue in the execution
		// (because no connection to database)
		Assertions.assertEquals(-1, result);
	}

}
