package com.parkit.parkingsystem.model;

import java.time.LocalDateTime;

/**
 * Class materializing a ticket
 */
public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private LocalDateTime inTime;
    private LocalDateTime outTime;

    /**
     * Return the number of the ticket
     * 
     * @return The number of the ticket
     */
    public int getId() {
        return id;
    }

    /**
     * Set the number of the ticket
     * 
     * @param number
     * The number of the ticket
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Return the parking spot of the ticket
     * 
     * @return The parking spot of the ticket
     */
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    /**
     * Set the parking spot of the ticket
     * 
     * @param parkingSpot
     * The parking spot of the ticket
     */
    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    /**
     * Return the vehicle registration number of the ticket
     * 
     * @return The vehicle registration number of the ticket
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     * Set the vehicle registration number of the ticket
     * 
     * @param vehicleRegNumber
     * The vehicle registration number of the ticket
     */
    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    /**
     * Return the price of the ticket
     * 
     * @return The price of the ticket
     */
    public double getPrice() {
        return price;
    }

    /**
     * Set the price of the ticket
     * 
     * @param price
     * The price of the ticket
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Return the entry time in the parking
     * 
     * @return The entry time in the parking
     */
    public LocalDateTime getInTime() {
        return inTime;
    }

    /**
     * Set the entry time in the parking
     * 
     * @param inTime
     * The entry time in the parking
     */
    public void setInTime(LocalDateTime inTime) {
        this.inTime = inTime;
    }

    /**
     * Return the exit time from the parking
     * 
     * @return The exit time from the parking
     */
    public LocalDateTime getOutTime() {
        return outTime;
    }

    /**
     * Set the exit time from the parking
     * 
     * @param outTime
     * The exit time from the parking
     */
    public void setOutTime(LocalDateTime outTime) {
        this.outTime = outTime;
    }
}