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

        long durationInSeconds = duration.getSeconds();
    
        double durationInHours = durationInSeconds / 3600.0;
        
        /*
        //TDD_US1 - Initial version :
        if(durationInHours < 0.5) {
        	double durationToPayIncFreeTime = 0.0;
        }
        else {
        	double durationToPayIncFreeTime = durationInHours;
        }
        */
        
        /*
        //TDD_US2 - Initial version : 
        TicketDAO ticketDAO = new TicketDAO();
        boolean recurringUser = ticketDAO.recurringUser(ticket.getVehicleRegNumber());
       	*/
        
        //TDD_US1 - Refactoring version :
        double durationToPayIncFreeTime = (durationInHours < 0.5) ? 0.0 : durationInHours;

        double durationToPayIncFreeTimeAndReduction = ( (recurringUser) ? (durationToPayIncFreeTime * 0.95) : durationToPayIncFreeTime);
        
        switch (ticket.getParkingSpot().getParkingType()){
        	case CAR: {
        		ticket.setPrice(durationToPayIncFreeTimeAndReduction * Fare.CAR_RATE_PER_HOUR);
            	break;
        	}
        	case BIKE: {
            	ticket.setPrice(durationToPayIncFreeTimeAndReduction * Fare.BIKE_RATE_PER_HOUR);
            	break;
        	}
        	default: throw new IllegalArgumentException("Unkown Parking Type");
    	}
        
    }
}