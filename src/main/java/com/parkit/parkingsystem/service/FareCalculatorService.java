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
        
        switch (ticket.getParkingSpot().getParkingType()){
        	case CAR: {
        		ticket.setPrice(durationDouble * Fare.CAR_RATE_PER_HOUR);
            	break;
        	}
        	case BIKE: {
            	ticket.setPrice(durationDouble * Fare.BIKE_RATE_PER_HOUR);
            	break;
        	}
        	default: throw new IllegalArgumentException("Unkown Parking Type");
    	}
        
    }
}