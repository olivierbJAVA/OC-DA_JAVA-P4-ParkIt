package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

/**
 * Class including unit tests for the FareCalculatorService Class.
 */
public class FareCalculatorServiceTest {

	private FareCalculatorService fareCalculatorServiceUnderTest;
	private Ticket ticket;

	@BeforeEach
	private void setUpPerTest() {
		fareCalculatorServiceUnderTest = new FareCalculatorService();
		ticket = new Ticket();
	}

	// TESTS for A NON RECURRING USER
	@Test
	public void calculateFareCar_WithOneHourParkingTime_RecurringUserNo() {

		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, false);

		// ASSERT
		assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareBike_WithOneHourParkingTime_RecurringUserNo() {

		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, false);

		// ASSERT
		assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareNullType_WithOneHourParkingTime_RecurringUserNo() {

		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT & ASSERT
		assertThrows(NullPointerException.class, () -> fareCalculatorServiceUnderTest.calculateFare(ticket, false));
	}

	@Test
	public void calculateFareCar_WithFutureInTime_RecurringUserNo() {

		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().plusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT & ASSERT
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorServiceUnderTest.calculateFare(ticket, false));
	}

	@Test
	public void calculateFareBike_WithFutureInTime_RecurringUserNo() {

		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().plusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT & ASSERT
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorServiceUnderTest.calculateFare(ticket, false));
	}

	@Test
	public void calculateFareCar_WithLessThanOneHourParkingTime_RecurringUserNo() {

		// ARRANGE
		// 45 minutes parking time should give 3/4th parking fare
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, false);

		// ASSERT
		assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareBike_WithLessThanOneHourParkingTime_RecurringUserNo() {

		// ARRANGE
		// 45 minutes parking time should give 3/4th parking fare
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, false);

		// ASSERT
		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCar_WithMoreThanADayParkingTime_RecurringUserNo() {

		// ARRANGE
		// 1 day (= 24 hours) parking time should give 24 * parking fare per hour
		LocalDateTime inTime = LocalDateTime.now().minusDays(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, false);

		// ASSERT
		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareBike_WithMoreThanADayParkingTime_RecurringUserNo() {

		// ARRANGE
		// 1 day (= 24 hours) parking time should give 24 * parking fare per hour
		LocalDateTime inTime = LocalDateTime.now().minusDays(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, false);

		// ASSERT
		assertEquals((24 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCar_WithNullOutTime_RecurringUserNo() {

		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusDays(1);
		LocalDateTime outTime = null;
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT & ASSERT
		assertThrows(NullPointerException.class, () -> fareCalculatorServiceUnderTest.calculateFare(ticket, false));
	}

	@Test
	public void calculateFareBike_WithNullOutTime_RecurringUserNo() {

		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusDays(1);
		LocalDateTime outTime = null;
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT & ASSERT
		assertThrows(NullPointerException.class, () -> fareCalculatorServiceUnderTest.calculateFare(ticket, false));
	}

	// TESTS for A RECURRING USER
	@Test
	public void calculateFareCar_WithOneHourParkingTime_RecurringUserYes() {

		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, true);

		// ASSERT
		// The user shall have a 5% discount as recurring user
		assertEquals(0.95 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareBike_WithOneHourParkingTime_RecurringUserYes() {

		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, true);

		// ASSERT
		// The user shall have a 5% discount as recurring user
		assertEquals(0.95 * Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareNullType_WithOneHourParkingTime_RecurringUserYes() {

		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT & ASSERT
		assertThrows(NullPointerException.class, () -> fareCalculatorServiceUnderTest.calculateFare(ticket, false));
	}

	@Test
	public void calculateFareCar_WithFutureInTime_RecurringUserYes() {

		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().plusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT & ASSERT
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorServiceUnderTest.calculateFare(ticket, true));
	}

	@Test
	public void calculateFareBike_WithFutureInTime_RecurringUserYes() {

		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().plusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT & ASSERT
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorServiceUnderTest.calculateFare(ticket, true));
	}

	@Test
	public void calculateFareCar_WithLessThanOneHourParkingTime_RecurringUserYes() {

		// ARRANGE
		// 45 minutes parking time should give 3/4th parking fare
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, true);

		// ASSERT
		// The user shall have a 5% discount as recurring user
		assertEquals((0.95 * 0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareBike_WithLessThanOneHourParkingTime_RecurringUserYes() {

		// ARRANGE
		// 45 minutes parking time should give 3/4th parking fare
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, true);

		// ASSERT
		// The user shall have a 5% discount as recurring user
		assertEquals((0.95 * 0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCar_WithMoreThanADayParkingTime_RecurringUserYes() {

		// ARRANGE
		// 1 day (= 24 hours) parking time should give 24 * parking fare per hour
		LocalDateTime inTime = LocalDateTime.now().minusDays(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, true);

		// ASSERT
		// The user shall have a 5% discount as recurring user
		assertEquals((0.95 * 24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareBike_WithMoreThanADayParkingTime_RecurringUserYes() {

		// ARRANGE
		// 1 day (= 24 hours) parking time should give 24 * parking fare per hour
		LocalDateTime inTime = LocalDateTime.now().minusDays(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, true);

		// ASSERT
		// The user shall have a 5% discount as recurring user
		assertEquals((0.95 * 24 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCar_WithNullOutTime_RecurringUserYes() {

		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusDays(1);
		LocalDateTime outTime = null;
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT & ASSERT
		assertThrows(NullPointerException.class, () -> fareCalculatorServiceUnderTest.calculateFare(ticket, true));
	}

	@Test
	public void calculateFareBike_WithNullOutTime_RecurringUserYes() {

		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusDays(1);
		LocalDateTime outTime = null;
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT & ASSERT
		assertThrows(NullPointerException.class, () -> fareCalculatorServiceUnderTest.calculateFare(ticket, true));
	}
}
