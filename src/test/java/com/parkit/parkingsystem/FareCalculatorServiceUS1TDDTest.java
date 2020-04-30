package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.Assertions.withinPercentage;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceUS1TDDTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	// RECURRING USER : NO
	// NORMAL CASES
	@Test
	public void calculateFareCar_LessThanThirtyMinutes_RecurringUserNo() {
		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(25);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket, false);

		// ASSERT
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void calculateFareCar_MoreThanThirtyMinutes_RecurringUserNo() {
		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket, false);

		// ASSERT
		assertEquals(0.75 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	// EDGE CASES
	@Test
	public void calculateFareCar_LessThanThirtyMinutes_ThirtyMinutesMinusOneSecond_RecurringUserNo() {
		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30).plusSeconds(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket, false);

		// ASSERT
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void calculateFareCar_ThirtyMinutes_RecurringUserNo() {
		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket, false);

		// ASSERT
		assertEquals(0.5 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareCar_MoreThanThirtyMinutes_ThirtyMinutesPlusOneSecond_RecurringUserNo() {
		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30).minusSeconds(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket, false);

		// ASSERT
		// assertEquals(0.5002778 * Fare.CAR_RATE_PER_HOUR,ticket.getPrice());

		// Use of AssertJ for the approximation
		assertThat(ticket.getPrice()).isCloseTo(0.5002778 * Fare.CAR_RATE_PER_HOUR, within(0.01));

	}

	// RECURRING USER : YES
	// NORMAL CASES
	@Test
	public void calculateFareCar_LessThanThirtyMinutes_RecurringUserYes() {
		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(25);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket, true);

		// ASSERT
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void calculateFareCar_MoreThanThirtyMinutes_RecurringUserYes() {
		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket, true);

		// ASSERT
		assertEquals(0.95 * 0.75 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	// EDGE CASES
	@Test
	public void calculateFareCar_LessThanThirtyMinutes_ThirtyMinutesMinusOneSecond_RecurringUserYes() {
		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30).plusSeconds(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket, true);

		// ASSERT
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void calculateFareCar_ThirtyMinutes_RecurringUserYes() {
		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket, true);

		// ASSERT
		assertEquals(0.95 * 0.5 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareCar_MoreThanThirtyMinutes_ThirtyMinutesPlusOneSecond_RecurringUserYes() {
		// ARRANGE
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30).minusSeconds(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorService.calculateFare(ticket, true);

		// ASSERT
		// assertEquals(0.5002778 * Fare.CAR_RATE_PER_HOUR,ticket.getPrice());

		// Use of AssertJ for the approximation
		assertThat(ticket.getPrice()).isCloseTo(0.95 * 0.5002778 * Fare.CAR_RATE_PER_HOUR, within(0.01));
	}

}
