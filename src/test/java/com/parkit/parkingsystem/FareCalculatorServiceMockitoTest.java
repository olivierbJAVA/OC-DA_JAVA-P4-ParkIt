package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

@PrepareForTest(ParkingType.class)
//@ExtendWith(PowerMockExtension.class)
@ExtendWith(MockitoExtension.class)
public class FareCalculatorServiceMockitoTest {

	private static FareCalculatorService fareCalculatorService;

	// @Mock
	// private ParkingSpot parkingSpot;

	private Ticket ticket;

	// @Mock
	// private static FareCalculatorService fareCalculatorServiceMock;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	public void unknownValueShouldThrowException() throws Exception {
		ParkingType ParkingTypeTest = Mockito.mock(ParkingType.class);
		Mockito.when(ParkingTypeTest.ordinal()).thenReturn(2);

		// ticket.getParkingSpot().getParkingType()
		// Mockito.when(ParkingTypeTest.ordinal()).thenThrow(new
		// IllegalArgumentException());

		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingTypeTest, true);

		// ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, true);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		// Mockito.when(ticket.getParkingSpot().getParkingType()).thenThrow(new
		// IllegalArgumentException());

		// ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));

		ticket.setParkingSpot(parkingSpot);
		/*
		 when(parkingSpot.getParkingType()).thenAnswer( new Answer<Void>() {
		 
		 @Override public Void answer(InvocationOnMock invocationOnMock) throws
		 Exception {
		 
		 throw new IllegalArgumentException("Test : exception getTicket()"); } });
		 */
		// ticket.setParkingSpot(parkingSpot);

		// fareCalculatorService.calculateFare(ticket);

		assertThrows(Exception.class, () -> fareCalculatorService.calculateFare(ticket));

		// bar.foo(C);
	}

	@Disabled("Use PowerMockito")
	@Test
	@PrepareForTest(ParkingType.class)
	public void unknownValueShouldThrowException2() throws Exception {
		// @PrepareForTest(ParkingType.class)
		ParkingType ParkingTypeTest = Mockito.mock(ParkingType.class);
		Mockito.when(ParkingTypeTest.ordinal()).thenReturn(2);

		PowerMockito.mockStatic(ParkingType.class);
		PowerMockito.when(ParkingType.values())
				.thenReturn(new ParkingType[] { ParkingType.CAR, ParkingType.BIKE, ParkingTypeTest });
		// ticket.getParkingSpot().getParkingType()
		// Mockito.when(ParkingTypeTest.ordinal()).thenThrow(new
		// IllegalArgumentException());

		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		// inTime.setTime( System.currentTimeMillis() - ( 60 * 60 * 1000) );
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingTypeTest, true);

		// ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, true);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		// Mockito.when(ticket.getParkingSpot().getParkingType()).thenThrow(new
		// IllegalArgumentException());

		// ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));

		ticket.setParkingSpot(parkingSpot);

		/*
		 when(parkingSpot.getParkingType()).thenAnswer( new Answer<Void>() {
		 
		 @Override public Void answer(InvocationOnMock invocationOnMock) throws
		 Exception {
		 
		 throw new IllegalArgumentException("Test : exception getTicket()"); } });
		 */
		// ticket.setParkingSpot(parkingSpot);

		// fareCalculatorService.calculateFare(ticket);

		assertThrows(Exception.class, () -> fareCalculatorService.calculateFare(ticket));

		// bar.foo(C);
	}

}
