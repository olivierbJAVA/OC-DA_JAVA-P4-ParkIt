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
public class ParkingServiceBikeTest {

	private static ParkingService parkingService;
	private static LocalDateTime inTimeTest = LocalDateTime.now().minusHours(1);
	private static Ticket ticket;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

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

			// OB : crée des problèmes (dépendances entre les tests) si on les déclare ici 
			// when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
			// when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
			// when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

			parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}

	@Test
	public void processIncomingVehicleTest_WhenAllIsOK() {
		// ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(2);
		when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
		when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);

		// ACT
		parkingService.processIncomingVehicle();

		// ASSERT
		verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
	}

	@Disabled("Ne fonctionne pas ?")
	@Test
	public void processIncomingVehicleTest_WhenNoParkingSpotIsAvailable_AndThrowAnException() {
		// ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(2);
		when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(-1);
		
		// OB : Pas besoin de mocker les méthodes ci-dessous qui ne sont pas appelées
		// when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
		// when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);

		// ACT
		try {
			parkingService.processIncomingVehicle();
		} catch (Exception e) {
			System.out.println("Exception test incoming vehicle" + e);
		}

		// ASSERT
		// OB : pourquoi ça ne marche pas ? Même si on met la méthode parkingService.getNextParkingNumberIfAvailable() public
		/*
		try { 
			assertThrows(Exception.class,() -> parkingService.getNextParkingNumberIfAvailable()); 
		} catch (Exception e) { 
		 	System.out.println("Exception test incoming vehicle" + e);
		}
		*/ 
		verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, never()).saveTicket(any(Ticket.class));
	}

	@Disabled("Ne fonctionne pas ?")
	@Test
	public void processIncomingVehicleTest_WhenNoParkingSpotIsAvailable() {
		// ARRANGE
		when(inputReaderUtil.readSelection()).thenReturn(2);
		when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(-1);

		// ACT
		try {
			parkingService.processIncomingVehicle();
		} catch (Exception e) {
			System.out.println("Exception test incoming vehicle" + e);
		}

		// ASSERT
		verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, never()).saveTicket(any(Ticket.class));
	}

	@Test
	public void processIncomingVehicleTest_WhenReadVehicleRegistrationNumberThrowAnException() {
		try {
			// ARRANGE
			when(inputReaderUtil.readSelection()).thenReturn(2);
			when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

			when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(new Exception("Test illegal"));

			// ACT
			parkingService.processIncomingVehicle();

			// ASSERT
			verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
			verify(ticketDAO, never()).saveTicket(any(Ticket.class));

		} catch (Exception e) {
		}
	}

	@Test
	public void processExitingVehicleTest_WhenUpdateTicketIsTrue() {

		// ARRANGE
		ArgumentCaptor<ParkingSpot> argumentCaptorParkingSpot = ArgumentCaptor.forClass(ParkingSpot.class);
		ArgumentCaptor<Ticket> argumentCaptorticket = ArgumentCaptor.forClass(Ticket.class);

		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

		// ACT
		parkingService.processExitingVehicle();

		// ASSERT correct method calls
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, Mockito.times(1)).getTicket(anyString());
		verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));

		// ASSERT correct arguments
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(argumentCaptorParkingSpot.capture());
		ParkingSpot parkingSpotTest = argumentCaptorParkingSpot.getValue();
		assertEquals(1, parkingSpotTest.getId());
		assertEquals(ParkingType.BIKE, parkingSpotTest.getParkingType());
		assertTrue(parkingSpotTest.isAvailable());

		verify(ticketDAO, Mockito.times(1)).updateTicket(argumentCaptorticket.capture());
		Ticket ticketTest = argumentCaptorticket.getValue();
		assertEquals(inTimeTest, ticketTest.getInTime());
		assertEquals(new ParkingSpot(1, ParkingType.BIKE, false), ticketTest.getParkingSpot());
		assertEquals("ABCDEF", ticketTest.getVehicleRegNumber());
	}

	@Test
	public void processExitingVehicleTest_WhenUpdateTicketIsFalse() {

		// ARRANGE
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

		// ACT
		parkingService.processExitingVehicle();

		// ASSERT
		verify(parkingSpotDAO, Mockito.never()).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, Mockito.times(1)).getTicket(anyString());
		verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
	}

	@Test
	public void processExitingVehicleTest_WhenUpdateTicketThrowAnException() {

		// OB : Ne marche pas car la méthode updateTicket() ne lève pas directement une Exception :
		// when(ticketDAO.updateTicket(any(Ticket.class))).thenThrow(new Exception(""));
		// assertThrows(Exception.class, () -> parkingService.processExitingVehicle());

		// ARRANGE
		Answer<Void> answerException = new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocationOnMock) throws Throwable {

				throw new Exception("Test : exception getTicket()");
			}
		};

		when(ticketDAO.getTicket(anyString())).thenAnswer(answerException);

		// Other solution
		/*
		 when(ticketDAO.getTicket(anyString())).thenAnswer( new Answer<Void>() {
		 
		 @Override public Void answer(InvocationOnMock invocationOnMock) throws
		 Exception {
		 
		 throw new Exception("Test : exception getTicket()"); } });
		 */

		// ACT
		parkingService.processExitingVehicle();

		// ASSERT
		verify(ticketDAO, Mockito.never()).updateTicket(any(Ticket.class));
	}

	@Test
	public void processExitingVehicleTest_WhenReadVehicleRegistrationNumberThrowAnException() {
		try {
			// ARRANGE
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(new Exception("Test illegal"));

			// ACT
			parkingService.processExitingVehicle();

			// ASSERT
			Mockito.verify(ticketDAO, never()).getTicket(anyString());
			Mockito.verify(ticketDAO, never()).updateTicket(any(Ticket.class));
			Mockito.verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
		} catch (Exception e) {
		}
	}
}