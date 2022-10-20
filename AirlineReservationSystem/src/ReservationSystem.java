import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Comparator;

/**
 * Airline Reservation System
 * @author Quang Le
 * 10/01/2022
 */
public class ReservationSystem {
	
	public static ArrayList<Row> planeRows = new ArrayList<>();
	public static ArrayList<Passenger> individualPassengers = new ArrayList<>();
	public static ArrayList<GroupPassenger> groupPassengers = new ArrayList<>();
	public static Scanner sc;
	public static int firstClassBound = 9; 	// limit number of rows in first class 
	public static int economyClassBound = 30;	// limit number of rows in economy class
	public static String flightName;
	
	/**
	 * A main method to test all functionalities of this airline reservation system
	 * @param args - name of the flight
	 */
	public static void main(String[] args) {
		initializePlaneRows();
		
		flightName = args[0];
		
		load(flightName);	// Try to load the data of that flight if it has been created before
		
		sc = new Scanner(System.in);
		System.out.print("\nMAIN MENU: Add [P]assenger, Add [G]roup, [C]ancel Reservations, Print Seating [A]vailability Chart, Print [M]anifest, [Q]uit \n - ");
		String input = sc.nextLine();
		
		while (!input.equalsIgnoreCase("q")) {
			
			if (input.equalsIgnoreCase("p")) {
				addPassenger();
			}
			else if (input.equalsIgnoreCase("g")) {
				addGroup();
			}
			else if (input.equalsIgnoreCase("c")) {
				cancel();
			}
			else if (input.equalsIgnoreCase("a")) {
				availabilityChart();
			}
			else if (input.equalsIgnoreCase("m")) {
				manifest();
			}
			System.out.print("\nMAIN MENU: Add [P]assenger, Add [G]roup, [C]ancel Reservations, Print Seating [A]vailability Chart, Print [M]anifest, [Q]uit \n - ");
			input = sc.nextLine();
		}
		save();		// Save all info of that flight to file
	}
	
	/**
	 * Initialize all the rows for the plane
	 */
	public static void initializePlaneRows() {
		for (int i = 0; i < firstClassBound; i++) {
			planeRows.add(new Row(i, "First"));
		}
		
		for (int i = firstClassBound; i < economyClassBound; i++) {
			planeRows.add(new Row(i, "Economy"));
		}
	}
	
	/**
	 * Load all data of the flight
	 * @param name - Name of that flight
	 */
	public static void load(String name) {
		String flightFileName = name.concat(".txt");
		try {	
			File file = new File(flightFileName);
			
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String line1 = sc.nextLine();
				String line2 = sc.nextLine();
				String line3 = new String();
				if (line1.charAt(0) == 'I') {
					loadIndPassenger(line1, line2);
				}
				else {
					line3 = sc.nextLine();
					loadGroupPassengers(line1, line2, line3);
				}
			}
			sc.close();
			System.out.println("All data of " + name + " has been loaded! \nYou may start reservation!");
		}
		catch(FileNotFoundException e) {
			System.out.println(name + " has been initialized! \nReservation ready!");
		}
	}
	
	/**
	 * Load individual passenger information
	 * @param l1 - Contains individual name
	 * @param l2 - Contains individual's reserved seat information
	 */
	public static void loadIndPassenger(String l1, String l2) {
		String name = l1.split(": ")[1];
		String[] info = l2.split(" ");
		int rowIndex = Integer.valueOf(info[0]) - 1;
		String seatName = info[1];
		
		// find that seat
		Seat s = findSeat(rowIndex, seatName);
		
		// create passenger
		Passenger p = new Passenger(name, rowIndex, s);
		
		// put into the seat
		s.setPassenger(p);
		
		// add that passenger to individual arrayList
		individualPassengers.add(p);
	}
	
	/**
	 * Load the information of the group and all passengers of that group
	 * @param l1 - Contains group name
	 * @param l2 - Contains all passengers' name
	 * @param l3 - Contain all passengers' seat information
	 */
	public static void loadGroupPassengers(String l1, String l2, String l3) {
		String groupName = l1.split(": ")[1];
		String[] passengerNames = l2.split(", ");
		String[] passengerInfos = l3.split(", ");
		
		// create group
		GroupPassenger group = new GroupPassenger(groupName);
		
		// put that group into arrayList
		groupPassengers.add(group);
		
		// loop through each passenger
		for (int i = 0; i < passengerNames.length; i++) {
			String[] seatInfo = passengerInfos[i].split(" "); 
			int rowIndex = Integer.parseInt(seatInfo[0]) - 1;
			String seatName = seatInfo[1];
			// find seat
			Seat s = findSeat(rowIndex, seatName);
			// create passenger
			Passenger p = new Passenger(passengerNames[i], rowIndex, s);
			// put into seat
			s.setPassenger(p);
			// add individual to the group which has been added to the arrayList
			group.addPassenger(p);
		}
	}
	
	/** 
	 * A method to find a seat using its name and the row number
	 * @param rowNum - Row number contains that seat
	 * @param seatName - The name of that seat
	 * @return
	 */
	public static Seat findSeat(int rowNum, String seatName) {
		Row r = planeRows.get(rowNum);
		Seat s = null;
		for (Seat s1 : r.getSeats()) {
			if (s1.getName().equals(seatName)) {
				s = s1;
				break;
			}
		}
		return s;
	}
	
	/**
	 * A method to save all information of this flight to a file
	 */
	public static void save() {
		try {
			String flightFileName = flightName.concat(".txt");
			File output = new File(flightFileName);
			if (!output.exists()) {
				output.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(output);
			BufferedWriter bufferWritter = new BufferedWriter(fileWriter);
			
			// Save Individual Passengers
			for (Passenger p : individualPassengers) {
				bufferWritter.write("Individual: " + p.getName());
				bufferWritter.newLine();
				bufferWritter.write((p.getRowIndex()+1) + " " + p.getSeat().getName());
				bufferWritter.newLine();
			}
			
			// Save Group Passengers
			for (int i = 0; i < groupPassengers.size(); i++) {
				GroupPassenger g = groupPassengers.get(i);
				
				bufferWritter.write("Group: ");
				bufferWritter.write(g.getName());
				bufferWritter.newLine();
				for (int j = 0; j < g.getPassengers().size(); j++) {
					Passenger p = g.getPassengers().get(j);
					bufferWritter.write(p.getName());
					if (j != g.getPassengers().size() - 1) {
						bufferWritter.write(", ");
					}
				}
				bufferWritter.newLine();
				// Save all passengers of each group
				for (int j = 0; j < g.getPassengers().size(); j++) {
					Passenger p = g.getPassengers().get(j);
					bufferWritter.write((p.getRowIndex()+1) + " " + p.getSeat().getName());
					if (j != g.getPassengers().size() - 1) {
						bufferWritter.write(", ");
					}
				}
				if (i != groupPassengers.size() - 1) {
					bufferWritter.newLine();
				}
			}
			bufferWritter.close();
			System.out.println("\nAll data of " + flightName+ " has been saved!");
		}
		catch (IOException e) {
			System.out.println("ERROR - FAILED SAVE!");
			}
	}
	
	/**
	 * A method to print out the seating availability chart
	 */
	public static void availabilityChart() {
		System.out.print("[F]irst class or [E]conomy class? - ");
		String input = sc.nextLine();
		while (!input.equalsIgnoreCase("f") && !input.equalsIgnoreCase("e")) {
			System.out.println("Error! Please enter your choice again!");
			System.out.print("[F]irst class or [E]conomy class? - ");
			input = sc.nextLine();
		}
		// User chooses first class
		if (input.equalsIgnoreCase("f")) {
			System.out.println("\nFirst");
			printOutAvailableRows(0, firstClassBound);
		}
		// User chooses economy class
		else {
			System.out.println("\nEconomy");
			printOutAvailableRows(firstClassBound, economyClassBound);
		}
	}
	
	/**
	 * A method to print out all available rows along with available seats in each row
	 * @param leftBound - Left index of the row on the plane
	 * @param rightBound - Right index of the row on the plane
	 */
	public static void printOutAvailableRows (int leftBound, int rightBound) {
		for (int i=leftBound; i < rightBound; i++) {
			System.out.print((planeRows.get(i).getRowIndex() + 1) + ": ");
			ArrayList<Seat> availableSeats = planeRows.get(i).getAvailableSeatsOnThisRow();
			if (availableSeats.size() > 0) {
				for (int j=0; j < availableSeats.size(); j++) {
					Seat s = availableSeats.get(j);
					System.out.print(s.getName());
					if (j != availableSeats.size() - 1) {
						System.out.print(", ");
					}
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * A method to print out all rows having been reserved
	 */
	public static void manifest() {
		System.out.print("[F]irst class or [E]conomy class? - ");
		String input = sc.nextLine();
		while (!input.equalsIgnoreCase("f") && !input.equalsIgnoreCase("e")) {
			System.out.println("Error! Please enter your choice again!");
			System.out.print("[F]irst class or [E]conomy class? - ");
			input = sc.nextLine();
		}
		// User chooses first class
		if (input.equalsIgnoreCase("f")) {
			System.out.println("\nFirst");
			printOutReservedRows(0, firstClassBound);
		}
		// User chooses economy class
		else {
			System.out.println("\nEconomy");
			printOutReservedRows(firstClassBound, economyClassBound);
		}
	}
	
	/**
	 * A method to print out all reserved rows number along with passenger name on that row
	 * @param leftBound - Left index of the row on the plane
	 * @param rightBound - Right index of the row on the plane
	 */
	public static void printOutReservedRows (int leftBound, int rightBound) {
		for (int i=leftBound; i < rightBound; i++) {
			for (Seat s : planeRows.get(i).getSeats()) {
				if (s.getReservedStatus()) {
					System.out.println((i+1) + s.getName() + ": " + s.getPassenger().getName());
				}
			}
		}
	}

	/**
	 * A method to add a passenger to the flight
	 */
	public static void addPassenger() {
		
		System.out.println("Enter the information below to reserve a preference seat.");
		
		System.out.print("Name: ");
		String name = sc.nextLine();
		
		System.out.print("Service Class(First/Economy): ");
		String serviceClass = sc.nextLine();
		
		// Validate the input
		while (!serviceClass.equalsIgnoreCase("first") && !serviceClass.equalsIgnoreCase("economy")) {
			System.out.println("Error! Please enter again!");
			System.out.print("Service Class([First]/[Economy]): ");
			serviceClass = sc.nextLine();
		}
		
		if (serviceClass.equalsIgnoreCase("first")) {
			System.out.print("Seat Preference - [W]indow, [A]isle: ");
		}
		else {
			System.out.print("Seat Preference - [W]indow, [C]enter, [A]isle: ");
		}
		
		String seatPref = sc.nextLine();
		
		// Validate the input
		if (serviceClass.equalsIgnoreCase("first")) {
			while (!seatPref.equalsIgnoreCase("w") && !seatPref.equalsIgnoreCase("a")) {
				System.out.println("Error! Please enter again!");
				System.out.print("[W]indow, [A]isle: ");
				seatPref = sc.nextLine();
			}
		}
		else {
			while (!seatPref.equalsIgnoreCase("w") && !seatPref.equalsIgnoreCase("c") && !seatPref.equalsIgnoreCase("a")) {
				System.out.println("Error! Please enter again!");
				System.out.print("[W]indow, [C]enter, [A]isle: ");
				seatPref = sc.nextLine();
			}
		}
		
		// Return failed if there is no more available seat to be reserved
		if (getTotalAvailableSeatsOnClass(serviceClass) == 0) {
			System.out.println("Request failed! No more available seat on this class to be reserved.");
		}
		
		
		int leftBound, rightBound;	// set leftBound, rightBound to loop depends on the service class customer wants
		if (serviceClass.equalsIgnoreCase("first")) {
			leftBound = 0;
			rightBound = firstClassBound;
		}
		else {
			leftBound = firstClassBound;
			rightBound = economyClassBound;
		}
			
		boolean added = false;
		// Adding this individual passenger
		while (leftBound < rightBound && !added) {
			for (Seat s : planeRows.get(leftBound).getSeats()) {
				if (!s.getReservedStatus() && s.getType().equalsIgnoreCase(seatPref)) {
					// create passenger and add to database
					Passenger p = new Passenger(name.strip(), leftBound, s);
					s.setPassenger(p);
					System.out.println("\nSeat " + (leftBound + 1) + s.getName() + " has been reserved for " + name + ".");
					// add to the arraylist holding all individual passsengers
					individualPassengers.add(p);
					
					added = true;
					break;
				}
			}
			leftBound++;
		}
		if (!added) System.out.println("Failed reservation! There is no available seat with this preference and service class!");
	}
	
	/**
	 * A method to add a group of passengers to the flight
	 */
	public static void addGroup() {
		
		System.out.println("Enter the information below to reserve a preference seat.");
		// ask for group name
		System.out.print("Group Name: ");
		String groupName = sc.nextLine();
		
		// ask for names of individual
		System.out.print("Names: ");
		String names = sc.nextLine();
		
		// ask seat preference
		System.out.print("Service Class(First/Economy): ");
		String serviceClass = sc.nextLine();
		
		// Validate the input
		while (!serviceClass.equalsIgnoreCase("first") && !serviceClass.equalsIgnoreCase("economy")) {
			System.out.println("Error! Please enter again!");
			System.out.print("Service Class([First]/[Economy]): ");
			serviceClass = sc.nextLine();
		}
		
		// split string names to array
		String[] namesArray = names.split(",");
		
		// check if enough available seats to reserve for this group
		if (namesArray.length > getTotalAvailableSeatsOnClass(serviceClass)) {
			System.out.println("Request falied! Not enough available seats on " + serviceClass + " class to reserve.");
			return;
		}
		
		// Create group to hold all this passengers
		GroupPassenger group = new GroupPassenger(groupName); 
		// Add to arrayList contains all group 
		groupPassengers.add(group);
		
		ArrayList<Row> availableRows = getAvailableRowsByClass(serviceClass);
		System.out.println();
		// Start filling in if there's a row that has sufficient number of adjacent seats
		for (int i=0; i < availableRows.size(); i++) {
			Row r = availableRows.get(i);
			if (r.getMaxAdjacentSeats()[0] >= namesArray.length) {
				for (int a = r.getMaxAdjacentSeats()[1]; a < r.getSeats().size(); a++) {
					Seat s = r.getSeats().get(a);
					if (group.getSize() == namesArray.length) break; 	// break if already filling all passengers but still have available seats
					String passengerName = namesArray[group.getSize()].strip();
					// create passenger
					Passenger p = new Passenger(passengerName, r.getRowIndex(), s);
					System.out.println("Seat " + (r.getRowIndex()+1) + s.getName() + " has been reserved for " + passengerName + ".");
					// plug into seat
					s.setPassenger(p);
					// add members to group
					group.addPassenger(p);
				}
			}
			if (group.getSize() == namesArray.length) break;
		}
		
		// Continue filling in row with largest number of adjacent seats
		if (group.getSize() != namesArray.length) {
			// Sorted rows based on largest number of adjacent seats (descending)
			ArrayList<Row> sortedRowsWithLargestAdSeats = getSortedRowsWithLargestAdjacentSeats(serviceClass);
			
			while (group.getSize() != namesArray.length) {
				Row r2 = sortedRowsWithLargestAdSeats.get(0);
				for (Seat s : r2.getAvailableSeatsOnThisRow()) {
					// create passenger
					Passenger p = new Passenger(namesArray[group.getSize()].strip(), r2.getRowIndex(), s);
					System.out.println("Seat " + r2.getRowIndex() + s.getName() + " has been reserved for " + p.getName() + ".");
					
					// plug into seat
					s.setPassenger(p);
							
					// add members to group
					group.addPassenger(p);
					break;
				}
				// delete this row from our arrayList if it has been filled up already
				if (sortedRowsWithLargestAdSeats.get(0).getAvailableSeatsOnThisRow().size() == 0) {
					sortedRowsWithLargestAdSeats.remove(0);
				}
			}
		}
	}
	
	/**
	 * A method to cancel the reservation of an Individual or a Group
	 */
	public static void cancel() {
		System.out.print("Cancel [I]ndividual or [G]roup? - ");
		String input = sc.nextLine();
		while (!input.equalsIgnoreCase("i") && !input.equalsIgnoreCase("g")) {
			System.out.println("Error! Please enter your choice again!");
			System.out.print("Cancel [I]ndividual or [G]roup? - ");
			input = sc.nextLine();
		}
		if (input.equalsIgnoreCase("i")) {
			System.out.print("Names: ");
			input = sc.nextLine();
			cancelIndividual(input);
		}
		else {
			System.out.print("Group Name: ");
			input = sc.nextLine();
			cancelGroup(input);
		}
		
	}
	
	/**
	 * A method to cancel individual's reservation 
	 * @param name - Name of that individual
	 */
	public static void cancelIndividual(String name) {
		int i = 0;
		Passenger p = null;
		while (p == null && i < individualPassengers.size()) {
			if (name.equalsIgnoreCase(individualPassengers.get(i).getName())) {
				p = individualPassengers.get(i);
			}
			i++;
		}
		// remove this passenger from the seat in plane, seat reservedStatus reset to false automatically
		p.getSeat().setPassenger(null);
		
		// remove passenger from individualPassenger group
		individualPassengers.remove(p);
		System.out.println("\nSuccessfully canceled " + p.getName() + "'s reservation!");
	}
	
	/**
	 * A method to cancel group's reservation
	 * @param name - Name of that group
	 */
	public static void cancelGroup(String name) {
		int i = 0;
		GroupPassenger group = null;
		while (group == null && i< groupPassengers.size()) {
			if (name.equalsIgnoreCase(groupPassengers.get(i).getName())) {
				group = groupPassengers.get(i);
				break;
			}
			i++;
		}
		// start removing all passengers
		for (Passenger p : group.getPassengers()) {
			// remove this passenger from the seat in plane, seat reservedStatus reset to false automatically
			p.getSeat().setPassenger(null);
		}
		groupPassengers.remove(group);
		System.out.println("\nSuccessfully canceled " +  group.getName() + " group's reservation "  + "!");
	}
	
	/**
	 * A method to get all available rows in a service class
	 * @param c - The service class
	 * @return An array list containing all available rows
	 */
	public static ArrayList<Row> getAvailableRowsByClass(String c) {
		if (c.equalsIgnoreCase("first")) {
			return getFirstClassRowsAvailable();
		}
		else {
			return getEconomyClassRowsAvailable();
		}
	}
	
	/**
	 * A method to get all available rows of the first class
	 * @return An array list containing all available rows 
	 */
	public static ArrayList<Row> getFirstClassRowsAvailable() {
		ArrayList<Row> rows = new ArrayList<>();
		
		for (int i = 0; i < firstClassBound; i++) {
			if (planeRows.get(i).getAvailableSeatsOnThisRow().size() > 0) {
				rows.add(planeRows.get(i));
			}
		}
		return rows;
	}
	
	/**
	 * A method to get all available rows of the economy class
	 * @return An array list containing all available rows 
	 */
	public static ArrayList<Row> getEconomyClassRowsAvailable() {
		ArrayList<Row> rows = new ArrayList<>();
		
		for (int i = firstClassBound; i < economyClassBound; i++) {
			if (planeRows.get(i).getAvailableSeatsOnThisRow().size() > 0) {
				rows.add(planeRows.get(i));
			}
		}
		return rows;
	}
	
	/**
	 * A method to get total amount of all available seats of a specific class service
	 * @param c - The class service 
	 * @return the total amount
	 */
	public static int getTotalAvailableSeatsOnClass(String c) {
		int total = 0;
		if (c.equalsIgnoreCase("f") || c.equalsIgnoreCase("first")) {
			for (int i = 0; i < firstClassBound; i++) {
				total += planeRows.get(i).getAvailableSeatsOnThisRow().size();
			}
		}
		else if (c.equalsIgnoreCase("e") || c.equalsIgnoreCase("economy")) {
			for (int i = firstClassBound; i < economyClassBound; i++) {
				total += planeRows.get(i).getAvailableSeatsOnThisRow().size();
			}
		}
		return total;
	}
	
	/**
	 * A methods to sort all the rows by the their largest available adjacent seats (descending order)
	 * @param c - The service class
	 * @return - An array list containing all row in sorted order (descending)
	 */
	public static ArrayList<Row> getSortedRowsWithLargestAdjacentSeats(String c) {
		ArrayList<Row> rows = new ArrayList<>();
		int leftBound, rightBound;
		
		// Get left bound right bound depends on service class 
		if (c.equalsIgnoreCase("f") || c.equalsIgnoreCase("first")) {
			leftBound = 0;
			rightBound = firstClassBound;
		}
		else {
			leftBound = firstClassBound;
			rightBound = economyClassBound;
		}
		
		for (int i = leftBound ; i < rightBound; i++) {
			if (planeRows.get(i).getAvailableSeatsOnThisRow().size() > 0) {
				rows.add(planeRows.get(i));
			}
		}
		
		Comparator<Row> comp = new Comparator<Row>() {
			public int compare(Row row1, Row row2) 
				{
				return row2.getMaxAdjacentSeats()[0] - row1.getMaxAdjacentSeats()[0];}
			};
		Collections.sort(rows, comp);
		return rows;
	}
}
