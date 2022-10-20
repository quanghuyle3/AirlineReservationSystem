import java.util.ArrayList;

/**
 * A class represents a group of passengers 
 */
public class GroupPassenger {
	
	private String name;
	private ArrayList<Passenger> passengers; 	// An array list holding all passengers in this group
	private int size = 0;
	
	/**
	 * Construct a group of passengers
	 * @param name - The name of the group
	 */
	public GroupPassenger(String name) {
		this.name = name;
		passengers = new ArrayList<>();
	}
	
	
	// Setter and Getter methods
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public ArrayList<Passenger> getPassengers() {
		return passengers;
	}
	
	public void addPassenger(Passenger p) {
		passengers.add(p);
		size++;
	}
	
}
