package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    private LocalDateTime inTimeTest = LocalDateTime.now().minusHours(1);
    
    @BeforeEach
    private void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            
            Ticket ticket = new Ticket();
            //ticket.setInTime(LocalDateTime.now().minusHours(1));
            ticket.setInTime(inTimeTest);
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            
            //when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            //when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processExitingVehicleTestUpdateTicketTrue(){
    	
    	ArgumentCaptor<ParkingSpot> argumentCaptorParkingSpot = ArgumentCaptor.forClass(ParkingSpot.class);
    	ArgumentCaptor<Ticket> argumentCaptorticket = ArgumentCaptor.forClass(Ticket.class);
    	
    	when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
    	
    	when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
    	
    	parkingService.processExitingVehicle();
    	
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).getTicket(anyString());
        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
                
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(argumentCaptorParkingSpot.capture());
       
        ParkingSpot parkingSpotTest = argumentCaptorParkingSpot.getValue();
        
        assertEquals(1, parkingSpotTest.getId());
        assertEquals(ParkingType.CAR, parkingSpotTest.getParkingType());
        assertTrue(parkingSpotTest.isAvailable());
   
        verify(ticketDAO, Mockito.times(1)).updateTicket(argumentCaptorticket.capture());
        
        Ticket ticketTest = argumentCaptorticket.getValue();
        
        assertEquals(inTimeTest, ticketTest.getInTime());
        assertEquals(new ParkingSpot(1, ParkingType.CAR, false), ticketTest.getParkingSpot());
        assertEquals("ABCDEF", ticketTest.getVehicleRegNumber());
            
    }

    @Test
    public void processExitingVehicleTestUpdateTicketFalse(){
    	
    	when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);
    	
    	parkingService.processExitingVehicle();
    	
        verify(parkingSpotDAO, Mockito.never()).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).getTicket(anyString());
        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
    }
    
    @Test
    public void processExitingVehicleTestThrowException(){
    	
    	//when(ticketDAO.updateTicket(any(Ticket.class))).thenThrow(new Exception(""));  
    	//assertThrows(Exception.class, () -> parkingService.processExitingVehicle());
    	
        Answer<Void> answerException = new Answer<Void>() 
        {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) 
                                 throws Throwable {
        
            	throw new Exception("Test : exception getTicket()");
            }			
        };
    	
        when(ticketDAO.getTicket(anyString())).thenAnswer(answerException);
    	        
     	/*  	
    	when(ticketDAO.getTicket(anyString())).thenAnswer(
    		     new Answer<Void>() {
    		      @Override
    		      public Void answer(InvocationOnMock invocationOnMock) throws Exception {
    		       
    		    	  throw new Exception("Test : exception getTicket()");
    		      }
    		     });
    	*/
    	parkingService.processExitingVehicle();
    	        
    	verify(ticketDAO, Mockito.never()).updateTicket(any(Ticket.class));
      	
    }
      
}
