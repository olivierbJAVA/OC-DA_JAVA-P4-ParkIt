package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

//import java.math.BigDecimal;
import java.time.*;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        LocalDateTime inTime = ticket.getInTime();
        LocalDateTime outTime = ticket.getOutTime();

        Duration duration = Duration.between(inTime,outTime);

        long durationLong = duration.getSeconds();
    
        double durationDouble = durationLong / 3600.0;
        
        double durationDoubleToPay;
        
        /*
        //TDD Initial version
        if(durationDouble < 0.5) {
        	durationDoubleToPay = 0.0;
        }
        else {
        	durationDoubleToPay = durationDouble;
        }
        */
        
        //TDD Refactored version
        durationDoubleToPay = (durationDouble < 0.5) ? 0.0 : durationDouble;
        
        switch (ticket.getParkingSpot().getParkingType()){
        	case CAR: {
        		ticket.setPrice(durationDoubleToPay * Fare.CAR_RATE_PER_HOUR);
            	break;
        	}
        	case BIKE: {
            	ticket.setPrice(durationDoubleToPay * Fare.BIKE_RATE_PER_HOUR);
            	break;
        	}
        	default: throw new IllegalArgumentException("Unkown Parking Type");
    	}
        
    }
}