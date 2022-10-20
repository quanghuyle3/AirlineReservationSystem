import java.util.ArrayList;
/**
 * A class represents a row in the plane
 */

public class Row {
	
	private int rowIndex;
	private String type;									// First/Economy class
	private ArrayList<Seat> seats = new ArrayList<>();		// An array list holding all seats in this row of plane
	
	public Row(int rowIndex, String type) {
		this.rowIndex = rowIndex;
		this.type = type;
		if (type.equals("First")) {
			seats.add(new Seat("A", "W"));
			seats.add(new Seat("B", "A"));
			seats.add(new Seat("C", "A"));
			seats.add(new Seat("D", "W"));
		}
		else {
			seats.add(new Seat("A", "W"));
			seats.add(new Seat("B", "C"));
			seats.add(new Seat("C", "A"));
			seats.add(new Seat("D", "A"));
			seats.add(new Seat("E", "C"));
			seats.add(new Seat("F", "W"));
		}
	}
	
	// Getter and Setter methods
	public int getRowIndex() {
		return rowIndex;
	}
	
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public ArrayList<Seat> getSeats() {
		return seats;
	}
	
	/**
	 * A method to get all available seats on this row
	 * @return an array list containing all available seats
	 */
	public ArrayList<Seat> getAvailableSeatsOnThisRow() {
		ArrayList<Seat> availableSeats = new ArrayList<>();
		for (Seat s : seats) {
			if (!s.getReservedStatus()) availableSeats.add(s);
		}
		return availableSeats;
	}
	
	/**
	 * A method to get the maximum number of available adjacent seats on this row
	 * @return an array - integer at [0] to be the max available adjacent seats, and integer at [1] to be the start index of that adjacent seat
	 */
	public int[] getMaxAdjacentSeats() {
		int[] result = {0, 0};
		int maxAdjacentSeats = 0;
		int savedMax = 0;
		int indexFirstAdjacent = 0;
		int savedIndex = 0;
		int startCodePoint = 0;
		
		for (Seat s : seats) {
			if (!s.getReservedStatus() && maxAdjacentSeats == 0 && s.getName().codePointAt(0) > startCodePoint) {
				maxAdjacentSeats++;
				indexFirstAdjacent = seats.indexOf(s);
				startCodePoint = s.getName().codePointAt(0);
			}
			else if (!s.getReservedStatus() && s.getName().codePointAt(0) == startCodePoint +1) {
				maxAdjacentSeats++;
				
				startCodePoint = s.getName().codePointAt(0);
			}
			else if (!s.getReservedStatus() && s.getName().codePointAt(0) != startCodePoint +1) {
				
				// save the maxAdjacentSeats and indexFirstAdjacent if needed
				if (maxAdjacentSeats > savedMax) {
					savedMax = maxAdjacentSeats;
					savedIndex = indexFirstAdjacent;
				}
				
				maxAdjacentSeats = 1;
				indexFirstAdjacent = seats.indexOf(s);
				startCodePoint = s.getName().codePointAt(0);
			}
		}
		
		if (maxAdjacentSeats > savedMax) {
			result[0] = maxAdjacentSeats;
			result[1] = indexFirstAdjacent;
		}
		else {
			result[0] = savedMax;
			result[1] = savedIndex;
		}
		
		return result;
	}
}
