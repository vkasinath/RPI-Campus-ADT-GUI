package hw7;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import hw4.Graph;
import hw4.Node;
import hw6.MarvelPaths2;
import hw4.Edge;

/**
 * CampusData represents a mutable object containing a Graph object,
 * a bData object that stores all building data, and a pData object that stores all path data
 * Rep Invariant: All paths found in the set of paths
 * Abstraction Function: A Graph g defined by nodes = building and edges = paths
 * with the label being the distance
 */
public class CampusData {

	//private variables
	private static HashMap<String, BldgData> bData;
	private static HashMap<String, ArrayList<PathData>> pData;
	private static Graph<String, Double, Double> g;

	/**
	 * @param: bfile is a file that contains all building, pfile is a file that contains all paths
	 * @effects: calculates distances between buildings and
	 * creates a new graph from input file parameters
	 * @throws: IOException if unable to parse files through BDataParser and PDataParser
	 */
	public CampusData(String bfile, String pfile) {

		bData = new HashMap<String, BldgData>();
		pData = new HashMap<String, ArrayList<PathData>>();
		g = new Graph<String,Double,Double>();

		//building data parser
		try {
			BDataParser(bfile);
		} catch (IOException e) {
			System.out.println(e.toString());
			throw new RuntimeException(e);
		}

		//path data parser
		try {
			PDataParser(pfile);
		} catch (IOException e) {
			System.out.println(e.toString());
			throw new RuntimeException(e);
		}

		CalcDist();

		CreateGraph();

	}


	/**
	 * @param: bfile is a file that contains all building
	 * @effects: puts all building information in a hashMap
	 * @throws: IOException if unable to parse file
	 */
	public void BDataParser(String filename)
			throws IOException {

		//reads in file and determines if it is valid
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = null;

		while ((line = reader.readLine()) != null) {
			int i = line.indexOf(",");
			if (i == -1)
				throw new IOException("File "+filename+" not a CSV (Building Name, Bulding ID, x, y) file.");

			//separates all data into BldgData variables
			BldgData b = new BldgData();

			int pos = 0;
			b.setBldgName(line.substring(pos,i));

			pos = i+1;
			i = line.indexOf(",", pos);
			b.setBldgID(line.substring(pos,i));

			pos = i+1;
			i = line.indexOf(",", pos);
			b.setX(Double.valueOf(line.substring(pos, i)));

			pos = i+1;
			b.setY(Double.valueOf(line.substring(pos, line.length())));	

			//store BldgData info in bData hashMap
			bData.put(b.getBldgID(), b);
		}
	}

	/**
	 * @param: pfile is a file that contains all building
	 * @effects: puts all building paths in a hashMap
	 * @throws: IOException if unable to parse file
	 */
	public void PDataParser(String filename)
			throws IOException {

		//reads in file and determines if it is valid
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = null;

		while ((line = reader.readLine()) != null) {
			int i = line.indexOf(",");
			if (i == -1)
				throw new IOException("File "+filename+" not a CSV (FromID, ToID) file.");

			PathData p = new PathData();

			//separates all data into PathData variables
			int pos = 0;
			p.setFromID(line.substring(pos,i));

			pos = i+1;
			p.setToID(line.substring(pos, line.length()));	

			ArrayList<PathData> pd = pData.get(p.getFromID());

			if (pd == null) 
				pd = new ArrayList<PathData>();

			//store PathData info in pData hashMap
			pd.add(p);
			pData.put(p.getFromID(), pd);
		}

	}
	
	

	public static HashMap<String, BldgData> getbData() {
		return bData;
	}


	public static HashMap<String, ArrayList<PathData>> getpData() {
		return pData;
	}


	//calculates distances between buildings from bData and assigns them to paths from pData
	public void CalcDist() {

		//gets y coordinates and x coordinates for two buldings
		for (String p : pData.keySet()) {
			Double x1 = bData.get(p).getX();
			Double y1 = bData.get(p).getY();
			for (PathData s : pData.get(p)) {
				Double x2 = bData.get(s.getToID()).getX();
				Double y2 = bData.get(s.getToID()).getY();

				//calculates distance and stores them in dist
				Double dist = Math.sqrt( Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2) );
				s.setDist(dist);
			}

		}

	}

	//creates new graph from bData and pData
	public void CreateGraph() {

		//gets a list of nodes of building IDs from bData
		ArrayList<Node<String>> N = new ArrayList<Node<String>>();
		for (String b : bData.keySet()) {
			N.add(new Node<String>(b));
		}

		//create a list of edges of paths between buildings from pData
		ArrayList<Edge<String,Double,Double>> E = new ArrayList<Edge<String,Double,Double>>();

		for (Map.Entry<String, ArrayList<PathData>> fid : pData.entrySet()) {

			ArrayList<PathData> paths = fid.getValue();

			for (PathData p : paths) {
				E.add(new Edge<String,Double,Double>(new Node<String>(p.getFromID()),
						new Node<String>(p.getToID()), p.getDist(), p.getDist()));

				E.add(new Edge<String,Double,Double>(new Node<String>(p.getToID()),
						new Node<String>(p.getFromID()), p.getDist(), p.getDist()));

				//System.out.println(E.size());
			}
		}

		//create graph using NodeList and EdgeList
		g = new Graph<String,Double,Double>(N, E);


	}

	/**
		@param: a starting node node1 for the path
		@param: a destination node node2 for the path
		@return: a string listing all of the nodes and edges visited along the shortest distance
	 */
	public String findPath(String node1, String node2) {

		//turn all input strings into its corresponding building ID
		String snode1 = cleanID(node1);
		String snode2 = cleanID(node2);


		//test for both unknowns being the same
		if ((snode1 == null) && (snode2 == null) && (!node1.equals(node2)))
			return "Unknown building: [" + node1 + "]\nUnknown building: [" + node2 + "]";

		if ((snode1 == null) && (snode2 == null) && (node1.equals(node2)))
			return "Unknown building: [" + node1 + "]";

		if (snode1 == null)
			return "Unknown building: [" + node1 + "]";

		if (snode2 == null)
			return "Unknown building: [" + node2 + "]";

		//test for both unknowns being the same
		if ((bData.get(snode1).getBldgName().equals("")) && (bData.get(snode2).getBldgName().equals("")) && (!node1.equals(node2)))
			return "Unknown building: [" + node1 + "]\nUnknown building: [" + node2 + "]";

		if ((bData.get(snode1).getBldgName().equals("")) && (bData.get(snode2).getBldgName().equals("")) && (node1.equals(node2)))
			return "Unknown building: [" + node1 + "]";

		if ((bData.get(snode1).getBldgName().equals("")))
			return "Unknown building: [" + node1 + "]";

		if ((bData.get(snode2).getBldgName().equals("")))
			return "Unknown building: [" + node2 + "]";


		Double tcost = 0.0;

		Node<String> n1 = new Node<String>(snode1);
		Node<String> n2 = new Node<String>(snode2);

		String s = "";

		// if pathing to itself, nothing to search for
		// simply return back with s
		if (snode1.equals(snode2)) {
			s = s + "Path from " + getBldgName(snode1) + " to " + getBldgName(snode2) + ":\n";
			return s + "Total distance: " + String.format("%.3f", tcost) + " pixel units.";

		}

		// get all our Nodes and store in a temp arraylist
		// this avoids getNode() method calls to access Nodes in the graph
		ArrayList<Node<String>> N = new ArrayList<Node<String>>(g.getNodes());

		// initialize a list of edges traversed. 
		// once complete, we will clean up to remove non-related traversals
		HashMap<Node<String>, Edge<String,Double,Double>> path = new HashMap<Node<String>, Edge<String,Double,Double>>();

		boolean found = false;

		HashMap<Node<String>, ArrayList<Edge<String,Double,Double>>> 
		HE = new HashMap<Node<String>, ArrayList<Edge<String,Double,Double>>>();

		ArrayList<Edge<String,Double,Double>> E = new ArrayList<Edge<String,Double,Double>>(g.getEdges());

		found = MarvelPaths2.Dijkstra(snode1, snode2, path, HE, N, E);

		//if the node is equal to the final node
		if(found) {

			s = s + "Path from " + getBldgName(snode1) + " to " + getBldgName(snode2) + ":\n";

			ArrayList<Edge<String,Double,Double>> fpath = new ArrayList<Edge<String,Double,Double>>();

			Node<String> u = n2;

			while (!u.equals(n1)) {
				fpath.add(path.get(u));
				u = path.get(u).getPnode();
			}

			Collections.reverse(fpath);


			//print out 
			tcost = 0.0;
			for (int j=0; j < fpath.size(); j++) {
				String dir = calcDirection(fpath.get(j).getPnode().getName(), 
						fpath.get(j).getCnode().getName());
				s = s + "\tWalk " + dir + " to (" +
						getBldgName(fpath.get(j).getCnode().getName())+ ")\n";
				tcost = tcost + fpath.get(j).getLabel();
			}

			s = s + "Total distance: " + String.format("%.3f", tcost)+ " pixel units.";
			return s;
		}

		else {
			// if here, we did not find a path from n1 to n1
			s = s + "There is no path from " + getBldgName(snode1) + " to " + getBldgName(snode2) + ".";

		}
		return s;
	}

	/**
	 * Find shortest path between two nodes, based on Dijkstra's algorithm (From
	 * prior HW MarvelPaths2)
	 * 
	 * @param: a      starting node node1 for the path
	 * @param: a      destination node node2 for the path
	 * @param: search type (least_distance, least_hops, avoid_intersections)
	 * @return: a list of edges
	 */
	public ArrayList<Edge<String, Double, Double>> findPathAL(String node1, String node2, String searchType) {

		// users can search by name or ID.
		// call method to get standardized ID
		String snode1 = cleanID(node1);
		String snode2 = cleanID(node2);

		// if the sanitized node1 and node2 is null and they are different text values
		if ((snode1 == null) && (snode2 == null) && !node1.equals(node2)) {
			return (new ArrayList<Edge<String, Double, Double>>());
		}

		// if the sanitized node1 and node2 is null and they are the same text values
		if ((snode1 == null) && (snode2 == null) && node1.equals(node2)) {
			return (new ArrayList<Edge<String, Double, Double>>());
		}

		// if sanitized node1 is null
		if (snode1 == null) {
			return (new ArrayList<Edge<String, Double, Double>>());
		}

		// if the sanitized node2 is null
		if (snode2 == null) {
			return (new ArrayList<Edge<String, Double, Double>>());
		}

		// if the sanitized node1 and node2 are intersections and they are different
		// text values
		if ((bData.get(snode1).getBldgName().equals("")) && (bData.get(snode2).getBldgName().equals(""))
				&& !node1.equals(node2)) {
			return (new ArrayList<Edge<String, Double, Double>>());
		}

		// if the sanitized node1 and node2 are intersections and they are the same text
		// values
		if ((bData.get(snode1).getBldgName().equals("")) && (bData.get(snode2).getBldgName().equals(""))
				&& node1.equals(node2)) {
			return (new ArrayList<Edge<String, Double, Double>>());
		}

		// if the first node is an intersection
		if (bData.get(snode1).getBldgName().equals(""))
			return (new ArrayList<Edge<String, Double, Double>>());

		// if the second node is an intersection
		if (bData.get(snode2).getBldgName().equals(""))
			return (new ArrayList<Edge<String, Double, Double>>());

		// snode1 and snode2 are our standardized node IDs
		// create local nodes from input source & target node-strings
		Node<String> n1 = new Node<String>(snode1);
		Node<String> n2 = new Node<String>(snode2);

		String s = "";
		// if pathing to itself, nothing to search for. simply return back with s
		if (snode1.equals(snode2)) {
			s = s + "path from " + getBldgName(snode1) + " to " + getBldgName(snode2) + ":\n";
			//			return s + "Total distance: " + String.format("%.3f", totWeight) + " pixel units.";
		}

		HashMap<Node<String>, ArrayList<Edge<String, Double, Double>>> HE = new HashMap<Node<String>, ArrayList<Edge<String, Double, Double>>>();

		ArrayList<Edge<String, Double, Double>> E = new ArrayList<Edge<String, Double, Double>>();

		if (searchType.equals("least_distance")) {
			for (Edge<String, Double, Double> e : g.getEdges()) {
				Edge<String, Double, Double> x = new Edge<String, Double, Double>(
						new Node<String>(e.getPnode().getName()), new Node<String>(e.getCnode().getName()),
						e.getLabel(), e.getWeight());
				E.add(x);
			}
		} else if (searchType.equals("least_hops")) {
			for (Edge<String, Double, Double> e : g.getEdges()) {
				Edge<String, Double, Double> x = new Edge<String, Double, Double>(
						new Node<String>(e.getPnode().getName()), new Node<String>(e.getCnode().getName()), 1.0,
						e.getWeight());
				E.add(x);
			}
		} else if (searchType.equals("avoid_intersections")) {
			for (Edge<String, Double, Double> e : g.getEdges()) {
				String p = e.getPnode().getName();
				String c = e.getCnode().getName();

				boolean pIsIntersection = false;
				boolean cIsIntersection = false;

				if (getBldgName(p).contains("Intersection "))
					pIsIntersection = true;

				if (getBldgName(c).contains("Intersection "))
					cIsIntersection = true;

				Double pc_wt = 0.0;
				if (pIsIntersection && cIsIntersection)
					pc_wt = e.getLabel() * 1000.0;
				else if (pIsIntersection || cIsIntersection)
					pc_wt = e.getLabel();
				else
					pc_wt = e.getLabel() * 0.5;

				Edge<String, Double, Double> x = new Edge<String, Double, Double>(
						new Node<String>(e.getPnode().getName()), new Node<String>(e.getCnode().getName()), pc_wt, // label
						// used
						// for
						// adjusted
						// weights
						e.getWeight());
				E.add(x);
			}
		} else if (searchType.equals("avoid_buildings")) {
			for (Edge<String, Double, Double> e : g.getEdges()) {
				String p = e.getPnode().getName();
				String c = e.getCnode().getName();

				boolean pIsIntersection = false;
				boolean cIsIntersection = false;

				if (getBldgName(p).contains("Intersection "))
					pIsIntersection = true;

				if (getBldgName(c).contains("Intersection "))
					cIsIntersection = true;

				Double pc_wt = 0.0;
				if (pIsIntersection && cIsIntersection)
					pc_wt = e.getLabel() * 0.5;
				else if (pIsIntersection || cIsIntersection)
					pc_wt = e.getLabel();
				else
					pc_wt = e.getLabel() * 1000.0;

				Edge<String, Double, Double> x = new Edge<String, Double, Double>(
						new Node<String>(e.getPnode().getName()), new Node<String>(e.getCnode().getName()), pc_wt, // label
						// used
						// for
						// adjusted
						// weights
						e.getWeight());
				E.add(x);
			}
		}

		// get all our Nodes and store in a temp arraylist. this avoids getNode() method
		// calls to access Nodes in the graph
		ArrayList<Node<String>> N = new ArrayList<Node<String>>(g.getNodes());

		// initialize map of nodes -> edges... the map stores the least path edge to
		// arrive at a node
		HashMap<Node<String>, Edge<String, Double, Double>> path = new HashMap<Node<String>, Edge<String, Double, Double>>();

		// fill HE (hash map of key = node W, value = list of edges with parent node = W
		// call Dijkstra's algorithm method (written in prior HW MarvelPaths2)

		//boolean found = MarvelPaths2.Dijkstra(n1, n2, N, E, HE, path);
		boolean found = MarvelPaths2.Dijkstra(snode1, snode2, path, HE, N, E);

		// if least distance path found
		if (found) { // we are done. process the path

			Node<String> u = n2;

			// create an array for final path (fp)
			ArrayList<Edge<String, Double, Double>> fp = new ArrayList<Edge<String, Double, Double>>();

			// walk back through the path, starting with edge assigned to u (at start, u=n2)
			// path(u=n2)->edge)
			// add this edge path.get(u) to fp
			// then set u for the next round... set u = parent node of the edge
			// {parent.get(u)}
			// stop when the final parent u = n1
			// Finally, the answer path sequence in fp will be backwards ... so reverse fp

			while (!u.equals(n1)) {
				fp.add(path.get(u));
				u = path.get(u).getPnode();
			}
			Collections.reverse(fp);
			return (fp);

		}

		return (new ArrayList<Edge<String, Double, Double>>());

	}



	/**
	 * @param: String bldg_id can be a building or intersection
	 * @returns: returns building id or intersection id
	 */
	public String getBldgName(String bldg_id) {

		//get building id from input string
		String s = "";

		BldgData b = new BldgData();

		b = bData.get(bldg_id);

		//if id is null return
		if (b == null) {
			return bldg_id;
		}

		String name = b.getBldgName();

		//if id is a building return, else return intersection + id
		if (name.length() > 0) {
			s = name;
		}
		else
			s = "Intersection " + bldg_id;

		return s;

	}

	/**
	 * @param: String input can be a building name or id
	 * @returns: returns building id 
	 */
	public String cleanID(String input) {


		String s = null;

		BldgData b = new BldgData();

		b = bData.get(input);

		//if null return input
		if(b != null)
			s = input;
		//return id given name for building
		else {
			for (BldgData b1 : bData.values()) {
				if (b1.getBldgName().equals(input))
					s = b1.getBldgID();
			}
		}

		return s;	
	}

	//creates a lexicographically ordered list of all building names.
	public static ArrayList<String> listBldgNames(){

		String s = "";

		ArrayList<String> lNames = new ArrayList<String>();

		//add all buildings to list
		for (BldgData b1 : bData.values()) {
			if (b1.getBldgName().length() > 0) {
				s = b1.getBldgName() + "," +  b1.getBldgID();
				lNames.add(s);
			}	
		}

		//sort list of building names
		Collections.sort(lNames);

		return lNames;
	}

	/**
	 * @param: two strings representing two buildings
	 * @returns: returns direction needed to travel from one building to another
	 */
	public static String calcDirection(String node1, String node2) {

		String s = "";

		BldgData b1 = new BldgData();
		b1 = bData.get(node1);

		BldgData b2 = new BldgData();
		b2 = bData.get(node2);

		//calculate distance between two buildings
		Double x = b2.getX() - b1.getX();
		Double y = b2.getY() - b1.getY();

		Double angle = Math.toDegrees(Math.atan2(x, y));

		//give direction based off angle calculated
		if (157.5 <= angle || angle < -157.5)
			s = "North";
		else if(112.5 <= angle && angle < 157.5)
			s = "NorthEast";
		else if (67.5 <= angle && angle < 112.5)
			s = "East";
		else if(22.5 <= angle && angle < 67.5)
			s = "SouthEast";
		else if(-22.5 <= angle && angle < 22.5)
			s = "South";
		else if (-67.5 <= angle && angle < -22.5)
			s = "SouthWest";
		else if(-112.5 <= angle && angle < -67.5)
			s = "West";
		else if(-157.5 <= angle && angle < -112.5)
			s = "NorthWest";


		return s;

	}


	//main function
	public static void main(String arg[]) {

		//load data into cd
		CampusData cd = new CampusData("data/RPI_map_data_Nodes.csv", "data/RPI_map_data_Edges.csv");
		//		System.out.println(bData.size());
		//		for (String b : bData.keySet())
		//			System.out.println(b + " " + bData.get(b).bldgName);

		//		System.out.println(pData.size());
		//		for (String p : pData.keySet()) {
		//			for (PathData s : pData.get(p))
		//				System.out.println(s.fromID + " " + s.toID + " " + s.dist);	
		//		}

		//System.out.println(g.getEdges().size() + " " + g.getNodes().size());

		//System.out.println(cd.findPath("7", "700"));

		//System.out.println(listBldgNames());
	}

}
