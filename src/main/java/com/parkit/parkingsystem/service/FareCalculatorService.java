package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.LocalDateTime;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket, boolean recurringUser){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        LocalDateTime inTime = ticket.getInTime();
        LocalDateTime outTime = ticket.getOutTime();

        Duration duration = Duration.between(inTime,outTime);

        long durationLong = duration.getSeconds();
    
        double durationDouble = durationLong / 3600.0;
        
        /*
        //TDD_US1 - Initial version :
        if(durationDouble < 0.5) {
        	durationToPayThirtyMinutesFree = 0.0;
        }
        else {
        	durationToPayThirtyMinutesFree = durationDouble;
        }
        */
        
        /*
        //TDD_US2 - Initial version : 
        TicketDAO ticketDAO = new TicketDAO();
        boolean recurringUser = ticketDAO.recurringUser(ticket.getVehicleRegNumber());
       	*/
        
        //TDD_US1 - Refactoring version :
        double durationToPayThirtyMinutesFree = (durationDouble < 0.5) ? 0.0 : durationDouble;

        double durationToPayThirtyMinutesFreeReduction = ( (recurringUser) ? (durationToPayThirtyMinutesFree * 0.95) : durationToPayThirtyMinutesFree);
        
        switch (ticket.getParkingSpot().getParkingType()){
        	case CAR: {
        		ticket.setPrice(durationToPayThirtyMinutesFreeReduction * Fare.CAR_RATE_PER_HOUR);
            	break;
        	}
        	case BIKE: {
            	ticket.setPrice(durationToPayThirtyMinutesFreeReduction * Fare.BIKE_RATE_PER_HOUR);
            	break;
        	}
        	default: throw new IllegalArgumentException("Unkown Parking Type");
    	}
        
    }
}