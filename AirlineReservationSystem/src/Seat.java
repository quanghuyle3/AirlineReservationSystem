/**
 * A class represents a seat in the plane
 */
public class Seat {
	
	private String name;
	private String type;
	private Passenger passenger;
	private Boolean reservedStatus;
	
	/**
	 * Construct a seat in the plane with the initial reserved status to be false
	 * @param name - Name of the seat (A/B/C/D/E/F)
	 * @param type - Type of the seat (Window/Center/Aisle)
	 */
	public Seat(String name, String type) {
		this.name = name;
		this.type = type;
		reservedStatus = false;
	}
	
	// Setter and Getter methods
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Passenger getPassenger() {
		return passenger;
	}
	
	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
		if (this.passenger != null) {
			reservedStatus = true;
		}
		else {
			reservedStatus = false;
		}
	}
	
	public boolean getReservedStatus() {
		return reservedStatus;
	}
	
	public void setReservedStatus(Boolean reservedStatus) {
		this.reservedStatus = reservedStatus;
	}

}
