package hw7;

import java.util.Scanner;

/**
 * CampusPaths represents a CampusData object that stores all information regarding
 * buildings/paths and asks the user for input and produces the corresponding information
 */
public class CampusPaths{

	public static void main(String arg[]) {

		//loads cd with all information from files
		CampusData cd = new CampusData("data/RPI_map_data_Nodes.csv", "data/RPI_map_data_Edges.csv");

		//scanner object for reading user input
		Scanner myObj = new Scanner(System.in);  // Create a Scanner object
		
		while (myObj.hasNext()) {
			String uinput = myObj.nextLine().trim();  // Read user input
			
			//if user inputed b, print out building names
			if (uinput.equals("b")) {
				for (String s : cd.listBldgNames()) {
					System.out.println(s);
				}
			}
			//if user inputed m, print out menu
			else if (uinput.equals("m")) {
				System.out.println("b, lists all buildings.");
				System.out.println("r, prints directions for the shortest route between two building.");
				System.out.println("q, quits the program.");
				System.out.println("m, prints a menu of all commands.");
			}
			//if user inputed r, ask for two building names/ids and print out path 
			else if (uinput.equals("r")) {
				System.out.print("First building id/name, followed by Enter: ");
				String build1 = myObj.nextLine();  // Read user input
				
				System.out.print("Second building id/name, followed by Enter: ");
				String build2 = myObj.nextLine();  // Read user input
				
				System.out.println(cd.findPath(build1, build2));

			}
			//if user inputed q, quit
			else if (uinput.equals("q"))
				break;
			else {
				//if inputed command is unrecognized
				System.out.println("Unknown option");
			}
		}
		
		//close scanner
		myObj.close();

	}
}