package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

// TESTS for implementation of User Story 1 : a stay less than 30 minutes is free
public class FareCalculatorServiceUS1Test {

	private FareCalculatorService fareCalculatorServiceUnderTest;
	private Ticket ticket;

	@BeforeEach
	private void setUpPerTest() {
		fareCalculatorServiceUnderTest = new FareCalculatorService();
		ticket = new Ticket();
	}

	// TESTS for A NON RECURRING USER
	// NORMAL CASES
	@Test
	public void calculateFareCar_LessThanThirtyMinutes_RecurringUserNo() {
		// ARRANGE
		// 25 minutes parking time should give a fare equals to 0
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(25);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, false);

		// ASSERT
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void calculateFareBike_LessThanThirtyMinutes_RecurringUserNo() {
		// ARRANGE
		// 25 minutes parking time should give a fare equals to 0
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(25);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, false);

		// ASSERT
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void calculateFareCar_MoreThanThirtyMinutes_RecurringUserNo() {
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
		assertEquals(0.75 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareBike_MoreThanThirtyMinutes_RecurringUserNo() {
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
		assertEquals(0.75 * Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
	}

	// EDGE CASES
	@Test
	public void calculateFareCar_ThirtyMinutesMinusOneSecond_RecurringUserNo() {
		// ARRANGE
		// 30 minutes minus 1 second parking time should give a fare equals to 0
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30).plusSeconds(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, false);

		// ASSERT
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void calculateFareBike_ThirtyMinutesMinusOneSecond_RecurringUserNo() {
		// ARRANGE
		// 30 minutes minus 1 second parking time should give a fare equals to 0
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30).plusSeconds(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, false);

		// ASSERT
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void calculateFareCar_ThirtyMinutes_RecurringUserNo() {
		// ARRANGE
		// 30 minutes parking time should give 1/2 parking fare per hour
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, false);

		// ASSERT
		assertEquals(0.5 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareBike_ThirtyMinutes_RecurringUserNo() {
		// ARRANGE
		// 30 minutes parking time should give 1/2 parking fare per hour
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, false);

		// ASSERT
		assertEquals(0.5 * Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareCar_ThirtyMinutesPlusOneSecond_RecurringUserNo() {
		// ARRANGE
		// 30 minutes plus 1 second parking time should give a positive parking fare
		// (slightly above 1/2 parking fare per hour)
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30).minusSeconds(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, false);

		// ASSERT
		// Use of AssertJ for the approximation / 0.5002778 = 30mins+1s in hour
		assertThat(ticket.getPrice()).isCloseTo(0.5002778 * Fare.CAR_RATE_PER_HOUR, within(0.01));
	}

	@Test
	public void calculateFareBike_ThirtyMinutesPlusOneSecond_RecurringUserNo() {
		// ARRANGE
		// 30 minutes plus 1 second parking time should give a positive parking fare
		// (slightly above 1/2 parking fare per hour)
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30).minusSeconds(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, false);

		// ASSERT
		// Use of AssertJ for the approximation / 0.5002778 = 30mins+1s in hour
		assertThat(ticket.getPrice()).isCloseTo(0.5002778 * Fare.BIKE_RATE_PER_HOUR, within(0.01));
	}

	// TESTS for A RECURRING USER
	// NORMAL CASES
	@Test
	public void calculateFareCar_LessThanThirtyMinutes_RecurringUserYes() {
		// ARRANGE
		// 25 minutes parking time should give a fare equals to 0
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(25);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, true);

		// ASSERT
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void calculateFareBike_LessThanThirtyMinutes_RecurringUserYes() {
		// ARRANGE
		// 25 minutes parking time should give a fare equals to 0
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(25);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, true);

		// ASSERT
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void calculateFareCar_MoreThanThirtyMinutes_RecurringUserYes() {
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
		assertEquals(0.95 * 0.75 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareBike_MoreThanThirtyMinutes_RecurringUserYes() {
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
		assertEquals(0.95 * 0.75 * Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
	}

	// EDGE CASES
	@Test
	public void calculateFareCar_ThirtyMinutesMinusOneSecond_RecurringUserYes() {
		// ARRANGE
		// 30 minutes minus 1 second parking time should give a fare equals to 0
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30).plusSeconds(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, true);

		// ASSERT
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void calculateFareBike_ThirtyMinutesMinusOneSecond_RecurringUserYes() {
		// ARRANGE
		// 30 minutes minus 1 second parking time should give a fare equals to 0
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30).plusSeconds(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, true);

		// ASSERT
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void calculateFareCar_ThirtyMinutes_RecurringUserYes() {
		// ARRANGE
		// 30 minutes parking time should give 1/2 parking fare per hour
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, true);

		// ASSERT
		// The user shall have a 5% discount as recurring user
		assertEquals(0.95 * 0.5 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareBike_ThirtyMinutes_RecurringUserYes() {
		// ARRANGE
		// 30 minutes parking time should give 1/2 parking fare per hour
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, true);

		// ASSERT
		// The user shall have a 5% discount as recurring user
		assertEquals(0.95 * 0.5 * Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareCar_ThirtyMinutesPlusOneSecond_RecurringUserYes() {
		// ARRANGE
		// 30 minutes plus 1 second parking time should give a positive parking fare
		// (slightly above 1/2 parking fare per hour)
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30).minusSeconds(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, true);

		// ASSERT
		// The user shall have a 5% discount as recurring user
		// Use of AssertJ for the approximation / 0.5002778 = 30mins+1s in hour
		assertThat(ticket.getPrice()).isCloseTo(0.95 * 0.5002778 * Fare.CAR_RATE_PER_HOUR, within(0.01));
	}

	@Test
	public void calculateFareBike_ThirtyMinutesPlusOneSecond_RecurringUserYes() {
		// ARRANGE
		// 30 minutes plus 1 second parking time should give a positive parking fare
		// (slightly above 1/2 parking fare per hour)
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(30).minusSeconds(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		// ACT
		fareCalculatorServiceUnderTest.calculateFare(ticket, true);

		// ASSERT
		// The user shall have a 5% discount as recurring user
		// Use of AssertJ for the approximation / 0.5002778 = 30mins+1s in hour
		assertThat(ticket.getPrice()).isCloseTo(0.95 * 0.5002778 * Fare.BIKE_RATE_PER_HOUR, within(0.01));
	}
}
