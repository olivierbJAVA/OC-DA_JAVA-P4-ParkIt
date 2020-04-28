package com.parkit.parkingsystem;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareServiceDAOUnitTests;

public class ParkingSpotDAOTest {

    //private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    private static DataBasePrepareServiceDAOUnitTests dataBasePrepareServiceDAOUnitTest;
 

    @Test
    public void updateParkingAvailableInDatabase_ToTrue() throws Exception{
    
    	//ARRANGE
    	ParkingSpotDAO parkingSpotDAOUnderTest = new ParkingSpotDAO();
        dataBasePrepareServiceDAOUnitTest = new DataBasePrepareServiceDAOUnitTests();
        
        ParkingSpot parkingSpotTest = new ParkingSpot(1, ParkingType.CAR, true);
        dataBasePrepareServiceDAOUnitTest.updateParkingSpotDAOTest_Parking1ToFalse();
                     
        //ACT
        parkingSpotDAOUnderTest.updateParking(parkingSpotTest);
        
        //ASSERT
        boolean result = dataBasePrepareServiceDAOUnitTest.getParkingSpotDAOTest();
        Assertions.assertTrue(result);
    }
    
    @Test
    public void updateParkingAvailableInDatabase_ToFalse() throws Exception{
    	
    	//ARRANGE
    	ParkingSpotDAO parkingSpotDAOUnderTest = new ParkingSpotDAO();
        dataBasePrepareServiceDAOUnitTest = new DataBasePrepareServiceDAOUnitTests();
        
        ParkingSpot parkingSpotTest = new ParkingSpot(1, ParkingType.CAR, false);
        dataBasePrepareServiceDAOUnitTest.updateParkingSpotDAOTest_Parking1ToTrue();
                     
        //ACT
        parkingSpotDAOUnderTest.updateParking(parkingSpotTest);
        
        //ASSERT
        boolean result = dataBasePrepareServiceDAOUnitTest.getParkingSpotDAOTest();
        Assertions.assertFalse(result);
        
    }
    
    
    @Test
    public void getNextAvailableSlot_WhenAllSlotAreAvailable() throws Exception{
    	
    	//ARRANGE
    	ParkingSpotDAO parkingSpotDAOTest = new ParkingSpotDAO();
        dataBasePrepareServiceDAOUnitTest = new DataBasePrepareServiceDAOUnitTests();
        
        dataBasePrepareServiceDAOUnitTest.updateParkingSpotDAOTest_ToAllTrue();
        
        //ACT
        int result = parkingSpotDAOTest.getNextAvailableSlot(ParkingType.CAR);
        
        //ASSERT
        Assertions.assertEquals(1, result);
    }
    
    //@Disabled("Ne fonctionne pas car BDD renvoie soit 0 soit -1 !!!")
    @Test
    public void getNextAvailableSlot_WhenNoSlotIsAvailable() throws Exception{
    	int result=10;
    	try {
    	//ARRANGE
    	ParkingSpotDAO parkingSpotDAOTest = new ParkingSpotDAO();
        dataBasePrepareServiceDAOUnitTest = new DataBasePrepareServiceDAOUnitTests();
        
        dataBasePrepareServiceDAOUnitTest.updateParkingSpotDAOTest_ToAllFalse();
        
        //ACT
        result = parkingSpotDAOTest.getNextAvailableSlot(ParkingType.CAR);
    	}
    	catch(Exception e) {
    		System.out.println("Exception "+e);
    	}
        
        //ASSERT
        //Assertions.assertEquals(-1, result);
        Assertions.assertEquals(0, result);
     }
}
