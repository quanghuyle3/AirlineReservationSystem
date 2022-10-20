/**
 * A class represents the passenger
 */
public class Passenger {
	
	private String name;
	private int rowIndex;				// 0 <= i <= size() - 1
	private Seat seat;
	
	/**
	 * Construct a passenger with necessary information of the reserved row and seat from the plane
	 * @param name - Name of passenger 
	 * @param rowIndex - Index of the row that passenger reserves from the plane
	 * @param seat - The seat that passenger reserves from the plane
	 */
	public Passenger(String name, int rowIndex, Seat seat) {
		this.name = name;
		this.rowIndex = rowIndex;
		this.seat = seat;
	}
	
	// Getter and Setter methods
	public String getName() {
		return name;
	}
	
	public int getRowIndex() {
		return rowIndex;
	}
	
	public Seat getSeat() {
		return seat;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	
	public void setSeat(Seat seat) {
		this.seat = seat;
	}
}
