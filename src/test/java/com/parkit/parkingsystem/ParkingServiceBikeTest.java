package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceBikeTest {

	private ParkingService parkingServiceUnderTest;
	private LocalDateTime inTimeTest = LocalDateTime.now().minusHours(1);
	private Ticket ticket;

	@Mock
	private InputReaderUtil inputReaderUtil;
	@Mock
	private ParkingSpotDAO parkingSpotDAO;
	@Mock
	private TicketDAO ticketDAO;

	@BeforeEach
	private void setUpPerTest() {
		try {
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket = new Ticket();
			// ticket.setInTime(LocalDateTime.now().minusHours(1));
			ticket.setInTime(inTimeTest);
			ticket.setParkingSpot(parkingSpot);
			ticket.setVehicleRegNumber("ABCDEF");

			parkingServiceUnderTest = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}

	@Test
	public void processIncomingVehicle_WhenRegNumberNotAlreadyInParking() {
		// ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(2);
		when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
		when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
		when(ticketDAO.vehicleInTheParking((anyString()))).thenReturn(false);

		// ACT
		parkingServiceUnderTest.processIncomingVehicle();

		// ASSERT
		// We check that methods updateParking and saveTicket have been called one time
		// to process the income of the vehicle
		verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
	}

	@Test
	public void processIncomingVehicle_WhenRegNumberAlreadyInParking() {
		// ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
		when(ticketDAO.vehicleInTheParking((anyString()))).thenReturn(true);
		// ACT
		parkingServiceUnderTest.processIncomingVehicle();

		// ASSERT
		// As the vehicle is already in the parking, we check that methods updateParking
		// and saveTicket are not called (no need to process the income of the vehicle)
		verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, never()).saveTicket(any(Ticket.class));
	}

	@Test
	public void processIncomingVehicle_WhenReadVehicleRegistrationNumberThrowAnException() {
		try {
			// ARRANGE
			when(inputReaderUtil.readSelection()).thenReturn(2);
			when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(new Exception("Invalid input provided"));

			// ACT
			parkingServiceUnderTest.processIncomingVehicle();

			// ASSERT
			// We check that methods updateParking and saveTicket are not called as the
			// readVehicleRegistrationNumber method has thrown an Exception
			verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
			verify(ticketDAO, never()).saveTicket(any(Ticket.class));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}

	@Test
	public void processExitingVehicle_WhenUpdateTicketIsTrue_WhenRegNumberInParking() {

		// ARRANGE
		when(ticketDAO.vehicleInTheParking((anyString()))).thenReturn(true);
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

		// ACT
		parkingServiceUnderTest.processExitingVehicle();

		// ASSERT correct method calls
		// We check that methods getTicket, updateTicket and updateParking are called
		// one time to process the exit of the vehicle
		verify(ticketDAO, Mockito.times(1)).getTicket(anyString());
		verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));

		// ASSERT correct arguments
		// We check that methods have been called with correct arguments
		ArgumentCaptor<Ticket> argumentCaptorticket = ArgumentCaptor.forClass(Ticket.class);
		verify(ticketDAO, Mockito.times(1)).updateTicket(argumentCaptorticket.capture());
		Ticket ticketTest = argumentCaptorticket.getValue();
		assertEquals(inTimeTest, ticketTest.getInTime());
		assertEquals(new ParkingSpot(1, ParkingType.BIKE, false), ticketTest.getParkingSpot());
		assertEquals("ABCDEF", ticketTest.getVehicleRegNumber());

		ArgumentCaptor<ParkingSpot> argumentCaptorParkingSpot = ArgumentCaptor.forClass(ParkingSpot.class);
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(argumentCaptorParkingSpot.capture());
		ParkingSpot parkingSpotTest = argumentCaptorParkingSpot.getValue();
		assertEquals(1, parkingSpotTest.getNumber());
		assertEquals(ParkingType.BIKE, parkingSpotTest.getParkingType());
		assertTrue(parkingSpotTest.isAvailable());
	}

	@Test
	public void processExitingVehicle_WhenUpdateTicketIsTrue_WhenRegNumberNotInParking() {

		// ARRANGE
		when(ticketDAO.vehicleInTheParking((anyString()))).thenReturn(false);

		// ACT
		parkingServiceUnderTest.processExitingVehicle();

		// ASSERT
		// As the vehicle is not in the parking, we check that methods getTicket
		// updateTicket and updateParking are not called (no need to process the exit of
		// the vehicle)
		verify(ticketDAO, Mockito.never()).getTicket(anyString());
		verify(ticketDAO, Mockito.never()).updateTicket(any(Ticket.class));
		verify(parkingSpotDAO, Mockito.never()).updateParking(any(ParkingSpot.class));
	}

	@Test
	public void processExitingVehicle_WhenUpdateTicketIsFalse() {

		// ARRANGE
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);
		when(ticketDAO.vehicleInTheParking((anyString()))).thenReturn(true);

		// ACT
		parkingServiceUnderTest.processExitingVehicle();

		// ASSERT
		// If the program is not able to update the ticket, we check that the method
		// updateParking is not called
		verify(ticketDAO, Mockito.times(1)).getTicket(anyString());
		verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
		verify(parkingSpotDAO, Mockito.never()).updateParking(any(ParkingSpot.class));
	}

	@Test
	public void processExitingVehicle_WhenReadVehicleRegistrationNumberThrowAnException() {
		try {
			// ARRANGE
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(new Exception("Invalid input provided"));

			// ACT
			parkingServiceUnderTest.processExitingVehicle();

			// ASSERT
			// We check that methods getTicket, updateParking and saveTicket are not called
			// as the readVehicleRegistrationNumber method has thrown an Exception
			Mockito.verify(ticketDAO, never()).getTicket(anyString());
			Mockito.verify(ticketDAO, never()).updateTicket(any(Ticket.class));
			Mockito.verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}

}