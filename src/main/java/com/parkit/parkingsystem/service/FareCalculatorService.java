package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.LocalDateTime;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * Class computing the fare for a ticket depending on the stay time, the vehicle
 * type and if the user already came in the parking.
 */
public class FareCalculatorService {

	/**
	 * Method computing the fare for a ticket.
	 * 
	 * @param ticket        The ticket for which we want to compute the price
	 * 
	 * @param recurringUser Indicates if the user already came in the parking
	 */
	public void calculateFare(Ticket ticket, boolean recurringUser) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		LocalDateTime inTime = ticket.getInTime();
		LocalDateTime outTime = ticket.getOutTime();

		Duration duration = Duration.between(inTime, outTime);

		// Duration of the stay in seconds
		long durationInSeconds = duration.getSeconds();

		// Duration of the stay in hours
		double durationInHours = durationInSeconds / 3600.0;

		// Duration of the stay in hours taking into account free time for a stay less
		// than 30 minutes
		double durationToPayIncFreeTime = (durationInHours < 0.5) ? 0.0 : durationInHours;

		// Duration of the stay in hours including free time and 5% discount for a
		// recurring user
		double durationToPayIncFreeTimeAndReduction = ((recurringUser) ? (durationToPayIncFreeTime * 0.95)
				: durationToPayIncFreeTime);

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			ticket.setPrice(durationToPayIncFreeTimeAndReduction * Fare.CAR_RATE_PER_HOUR);
			break;
		}
		case BIKE: {
			ticket.setPrice(durationToPayIncFreeTimeAndReduction * Fare.BIKE_RATE_PER_HOUR);
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}

	}
}
