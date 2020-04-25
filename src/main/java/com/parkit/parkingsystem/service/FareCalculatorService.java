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
        
        // Not rounded version - pass original tests
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
        
        /*
        // Rounded version - pass modified tests
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                double price = durationDouble* Fare.CAR_RATE_PER_HOUR;
                BigDecimal bd = new BigDecimal(price);
                bd= bd.setScale(2,BigDecimal.ROUND_HALF_UP);
                double roundedPrice = bd.doubleValue();
                ticket.setPrice(roundedPrice);
                break;
            }
            case BIKE: {
            	double price = durationDouble* Fare.BIKE_RATE_PER_HOUR;
                BigDecimal bd = new BigDecimal(price);
                bd= bd.setScale(2,BigDecimal.ROUND_HALF_UP);
                double roundedPrice = bd.doubleValue();
                ticket.setPrice(roundedPrice);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
        */
    }
}