package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * Class materializing a parking spot.
 */
public class ParkingSpot {
	private int number;
	private ParkingType parkingType;
	private boolean isAvailable;

	/**
	 * Constructor of a parking spot.
	 * 
	 * @param number      The number of the parking spot
	 * 
	 * @param parkingType The type (car or bike) of the parking spot
	 * 
	 * @param isAvailable The availability (true or false) of the parking spot
	 */
	public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
		this.number = number;
		this.parkingType = parkingType;
		this.isAvailable = isAvailable;
	}

	/**
	 * Return the number of the parking spot.
	 * 
	 * @return The number of the parking spot
	 */
	public int getId() {
		return number;
	}

	/**
	 * Set the number of the parking spot.
	 * 
	 * @param number The number of the parking spot
	 */
	public void setId(int number) {
		this.number = number;
	}

	/**
	 * Return the type of the parking spot.
	 * 
	 * @return The type of the parking spot
	 */
	public ParkingType getParkingType() {
		return parkingType;
	}

	/**
	 * Set the type of the parking spot.
	 * 
	 * @param parkingType The type of the parking spot
	 */
	public void setParkingType(ParkingType parkingType) {
		this.parkingType = parkingType;
	}

	/**
	 * Return the availability of the parking spot.
	 * 
	 * @return The availability of the parking spot
	 */
	public boolean isAvailable() {
		return isAvailable;
	}

	/**
	 * Set the availability of the parking spot.
	 * 
	 * @param available The availability of the parking spot
	 */
	public void setAvailable(boolean available) {
		isAvailable = available;
	}

	/**
	 * Indicates if an object is equal to this parking spot.
	 * 
	 * @param object The object we want to know if it is equal to this parking spot
	 * 
	 * @return True if the object is equal to this parking spot, false otherwise
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} 
		if (object == null || getClass() != object.getClass()) {
			return false;
		}	
		ParkingSpot that = (ParkingSpot) object;
		return number == that.number;
	}

	/**
	 * Return the hash code of the parking spot.
	 * 
	 * @return The hash code of the parking spot
	 */
	@Override
	public int hashCode() {
		return number;
	}
}
