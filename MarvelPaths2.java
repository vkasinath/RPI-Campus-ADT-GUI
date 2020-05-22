package hw6;

import hw4.Graph;
import hw4.Node;
import hw5.MarvelParser;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;

import hw4.Edge;


public class MarvelPaths2 {
	
	/**
	 * MarvelPaths represents a mutable object containing a Graph object and a Map object that stores books and characters.
	 * Rep Invariant: All edge labels (name of book) found in set of books
	 * Abstraction Function: A Graph graph defined by graph.nodes = marvel characters
	 * graph.edges = characters in a book, with the label being the book name	
	 */
	
	//global variable
	private Graph<String,Double,Double> g;
	
	//constructor
	public MarvelPaths2() {
		g = new Graph<String,Double,Double>();
	}
	
	/**
	 * @param: hashMap of books with characters
	 * @effects: create a hashMap of characters and the number of common books
	 */
	private void getNodeBooks(HashMap<String, Set<String>> books,
			HashMap<ArrayList<String>, Double> charsNumBooks) {

		for(String book: books.keySet()) {

			Set<String> in_book = books.get(book);

			for(String char1 : in_book) {
				for(String char2 : in_book) {

					if(char1.contentEquals(char2))
						continue;

					ArrayList<String> s = new ArrayList<String>();
					s.add(char1);
					s.add(char2);
					Double x = charsNumBooks.get(s);

					if(x == null)
						x = 0.0;

					charsNumBooks.put(s, x+1.0);

				}
			}
		}	
	}
	
	/**
	 * @param: filename is the name of the file storing the marvel characters and book data
	 * @effects: create a new graph from input file parameter
	 * @throws: IOException through MarvelParser
	 */
	public void createNewGraph(String fileName){
		
		//create node list and edge list
		HashMap<String, Set<String>> charsInBooks = new HashMap<String,Set<String>>();
		HashSet<String> chars = new HashSet<String>();
		
		try {
			MarvelParser.readData(fileName, charsInBooks, chars);
			//System.out.println(charsInBooks.size() + " " +  chars.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<Node<String>> N = new ArrayList<Node<String>>();
		ArrayList<Edge<String, Double, Double>> E = new ArrayList<Edge<String, Double, Double>>();
		
		// read the characters set, and load our nodes
		Iterator<String> charIt = chars.iterator();
	    while(charIt.hasNext()){
	    	Node<String> temp = new Node<String>(charIt.next());
	    	N.add(temp);
	     }
	    
	    HashMap<ArrayList<String>, Double> charsNumBooks = new HashMap<ArrayList<String>, Double>();
	    getNodeBooks(charsInBooks, charsNumBooks);
	    
	    for (ArrayList<String> nodePair : charsNumBooks.keySet()) {
	    	
	    	String char1 = nodePair.get(0);
	    	String char2 = nodePair.get(1);
	    			
	    	Node<String> Node1 = new Node<String>(char1);
	    	Node<String> Node2 = new Node<String>(char2);
	    	Double numBooks = charsNumBooks.get(nodePair);
	    	Double wt = Double.POSITIVE_INFINITY;

	    	if(numBooks > 0) 
	    		wt = 1.0/numBooks;

	    	Edge<String, Double, Double> bookChars = new Edge<String, Double, Double>(Node1, Node2, wt, wt);
	    	E.add(bookChars);
	    	
	    }
	    
		// finally set the nodes and edges for the graph
		this.g.setNodesEdges(N, E);
		//System.out.println(g.getNodes().size());
		//System.out.println(g.getEdges().size());
	    
	}
	
	
	//weight comparator for comparing double values
	private static class weightComparator implements Comparator<Map.Entry<Node<String>, Double>> {

		public int compare(Map.Entry<Node<String>, Double> n1, Map.Entry<Node<String>, Double> n2) {
			Double d1 = n1.getValue();
			Double d2 = n2.getValue();
			
			return d1.compareTo(d2);
		}
	};
	
	// Fills the HE HashMap with a set of characters as the key,
	// and the number of books both characters are in as the value
	private static void fillHE(HashMap<Node<String>, ArrayList<Edge<String,Double,Double>>> HE,
			ArrayList<Edge<String,Double,Double>> E) {
		
		ArrayList<Edge<String,Double,Double>> nodePaths = new ArrayList<Edge<String,Double,Double>>();
		
		HE.clear();
		
		for (Edge<String,Double,Double> e : E) {
			
			Node<String> pNode = e.getPnode();
			Node<String> cNode = e.getCnode();
			Double ew = e.getLabel();
			
			Edge<String,Double,Double> newE = new Edge<String,Double,Double>(pNode, cNode, ew, ew);
			
			if (HE.get(pNode) == null) {
				nodePaths = new ArrayList<Edge<String,Double,Double>>();
				nodePaths.add(newE);
				
				HE.put(pNode, nodePaths);
				continue;
			}	
			
			nodePaths = HE.get(pNode);
			
			for(Edge<String,Double,Double> np : nodePaths) {
				
				if (np.getPnode().equals(pNode) && np.getCnode().equals(cNode)) {
					Double cw = np.getLabel();
					newE.setLabel(1.0/ ((1.0/cw)+(1.0/ew)) );
					newE.setWeight(1.0/ ((1.0/cw)+(1.0/ew)) );
					nodePaths.remove(np);
					break;
				}
				
			}
			
			nodePaths.add(newE);
			HE.put(pNode, nodePaths);
			
		}
		
	}
	
	/**
	@param: a starting node node1 for the path
	@param: a destination node node2 for the path
	@return: a string listing all of the nodes and edges visited along the shortest lexical path/ BFS
	 */
	public String findPath(String node1, String node2) {

		Node<String> n1 = new Node<String>(node1);
		Node<String> n2 = new Node<String>(node2);

		//test for both unknowns being the same
		if (!g.getNodes().contains(n1) && node1.contentEquals(node2))
			return "unknown character " + node1 + "\n";

		if (!g.getNodes().contains(n1) && !g.getNodes().contains(n2))
			return "unknown character " + node1 + "\nunknown character " + node2 + "\n";

		if (!g.getNodes().contains(n1))
			return "unknown character " + node1 + "\n";


		if (!g.getNodes().contains(n2))
			return "unknown character " + node2 + "\n";

		String s = "path from " + node1 + " to " + node2 + ":\n";

		Double tcost = 0.0;

		// if pathing to itself, nothing to search for
		// simply return back with s
		if (node1.equals(node2)) {
			return s + "total cost: " + String.format("%.3f", tcost) + "\n";

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
		
		found = Dijkstra(node1, node2, path, HE, N, E);

		//if the node is equal to the final node
		if(found) {

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
				s = s + fpath.get(j).getPnode().getName() + " to " + 
						fpath.get(j).getCnode().getName() + " with weight " +
						String.format("%.3f", fpath.get(j).getLabel()) + "\n";
				tcost = tcost + fpath.get(j).getLabel();
			}

			s = s + "total cost: " + String.format("%.3f", tcost)+ "\n";
			return s;
		}

		else {
			// if here, we did not find a path from n1 to n1
			s = s + "no path found\n";

		}
		return s;
	}
	
	/**
	@param: a starting node node1 for the path
	@param: a destination node node2 for the path
	@return: a string listing all of the nodes and edges visited along the shortest lexical path/ BFS
	*/
	public static boolean Dijkstra(String node1, String node2,
			HashMap<Node<String>, Edge<String,Double,Double>> path,
			HashMap<Node<String>, ArrayList<Edge<String,Double,Double>>> HE,
			ArrayList<Node<String>> N,
			ArrayList<Edge<String,Double,Double>> E) {
		
		fillHE(HE, E);
		
		boolean found = false;
		
		Node<String> n1 = new Node<String>(node1);
		Node<String> n2 = new Node<String>(node2);			

		// start an array to denote nodes visited or not
		// default boolean is set to false
		HashMap<Node<String>, Boolean> visited = new HashMap<Node<String>, Boolean>();
		
		HashMap<Node<String>, Double> weight = new HashMap<Node<String>, Double>(); 
		
		for (Node<String> n : N) {
			visited.put(n, false);
	    	weight.put(n, Double.POSITIVE_INFINITY);
		}
		
		PriorityQueue<Map.Entry<Node<String>, Double>> queue = 
				new PriorityQueue<Map.Entry<Node<String>, Double>>(new weightComparator());
		
		weight.put(n1, 0.0);
		//queue.add(Map.entry(n1, weight.get(n1)));
		queue.add(new AbstractMap.SimpleEntry< Node<String>, Double> (n1, weight.get(n1)));
		
		// while we have nodes in the queue to traverse
		while (!queue.isEmpty()) {

			Map.Entry<Node<String>, Double> u = queue.poll();
			
			visited.put(u.getKey(), true);
			
			//if the node is equal to the final node
			if(u.getKey().equals(n2)) {
				
				found = true;
				return found;
			
			}
		
			if (HE.get(u.getKey()) == null)
				break;
			
			ArrayList<Edge<String,Double,Double>> child_edges = new ArrayList<Edge<String,Double,Double>>();
			
			child_edges = HE.get(u.getKey());
			
			//calculate lowest cost path
			for (Edge<String,Double,Double> e2 : child_edges) {
				
				Node<String> v = e2.getCnode();
				
				if(!visited.get(v)) {
					
					Double eWeight = e2.getLabel();
					Double uWeight = weight.get(u.getKey());
					Double vWeight = weight.get(v);
					
					if(eWeight+uWeight < vWeight) {
						
						weight.put(v, eWeight+uWeight);
						//queue.remove(Map.entry(v, weight.get(v)));
						queue.remove(new AbstractMap.SimpleEntry< Node<String>, Double> (v, weight.get(v)));
						//queue.add(Map.entry(v, weight.get(v)));
						queue.add(new AbstractMap.SimpleEntry< Node<String>, Double> (v, weight.get(v)));
						path.put(v, e2);
						
					}
				}
			}
		}

		return found;
	}
	
//	
//	public static void main(String[] arg) {
//	
//	//long start = System.currentTimeMillis();
//	
//	//create graph using marvel.csv
//	MarvelPaths2 mp2 = new MarvelPaths2();
//	mp2.createNewGraph("data/marvel.csv");
//	
//	//long end = System.currentTimeMillis();
//	
//	//System.out.println(end-start);
//	
//	System.out.println(mp2.findPath("WISDOM, ROMANY", "CAPTAIN AMERICA"));
//	
//	}
}