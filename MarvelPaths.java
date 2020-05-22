package hw5;

import hw4.Graph;
import hw4.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
//import java.util.Map;
import java.util.Set;

import hw4.Edge;


public class MarvelPaths {
	
	/**
	 * MarvelPaths represents a mutable object containing a Graph object and a Map object that stores books and characters.
	 * Rep Invariant: All edge labels (name of book) found in set of books
	 * Abstraction Function: A Graph graph defined by graph.nodes = marvel characters
	 * graph.edges = characters in a book, with the label being the book name	
	 */
	
	private static Graph<String,String,Double> g;
	
	/**
	 * @param: filename is the name of the file storing the marvel characters and book data
	 * @effects: create a new graph from input file parameter
	 * @throws: IOException through MarvelParser
	 */
	public static void createNewGraph(String fileName){
		
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
		
		// read the characters set, and load our nodes
		Iterator<String> charIt = chars.iterator();
	    while(charIt.hasNext()){
	    	Node<String> temp = new Node<String>(charIt.next());
	    	N.add(temp);
	     }
	    
	    ArrayList<Edge<String,String,Double>> E = new ArrayList<Edge<String,String,Double>>();
		
		// read the books set, one book at a time
	    for (String book : charsInBooks.keySet()) {
	    	
			// for each book, get characters in the book
	    	Set<String> in_book = charsInBooks.get(book);
	    	
	    	//iterate through books
	    	Iterator<String> bookIt1 = in_book.iterator();
	    	while (bookIt1.hasNext()) {
	    		
	    		String s1 = bookIt1.next();
	    		
	    		//iterate through characters in each book
		    	Iterator<String> bookIt2 = in_book.iterator();
	    		while (bookIt2.hasNext()) {
	    			
	    			String s2 = bookIt2.next();
	    			
	    			//add edges to edge list
	    			if (!s1.equals(s2)) {
	    				Node<String> n1 = new Node<String>(s1);
	    				Node<String> n2 = new Node<String> (s2);
	    				
	    				Edge<String,String,Double> e = new Edge<String,String,Double>(n1, n2, book);
	    				
	    				E.add(e);
	    			}
	    			
	    		}
	    	}
	    	
	    }
	     
		// finally set the nodes and edges for the graph
		g = new Graph<String,String,Double>(N, E);
		//System.out.println(g.getNodes().size());
		//System.out.println(g.getEdges().size());
	    
	}
	

	/**
	 * interface to Comparator to compare two edges lexically by child-node-name + label 
	 * @return compares two edges by child node name and label. Returns -
	 * 			0 if e1.cnn+e1.label .equals e2.cnn+e2.label 
	 * 			1 if e1.cnn+e1.label > e2.cnn+e2.label
	 * 		   -1 if e1.cnn+e1.label < e2.cnn+e2.label
	 */
	public Comparator<Edge<String,String,Double>> childComparator = new Comparator<Edge<String,String,Double>>() {
	@Override
		public int compare(Edge<String,String,Double> e1, Edge<String,String,Double> e2) {
			String s1 = e1.getCnode().getName() + e1.getLabel();
			String s2 = e2.getCnode().getName() + e2.getLabel();
			return s1.compareTo(s2);
		}
	};
	
	
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
				
		
		// get all our Nodes and store in a temp arraylist
		// this avoids getNode() method calls to access Nodes in the graph
		ArrayList<Node<String>> N = new ArrayList<Node<String>>(g.getNodes());
		
		// initialize a list of edges traversed. 
		// once complete, we will clean up to remove non-related traversals
		ArrayList<Edge<String,String,Double>> path = new ArrayList<Edge<String,String,Double>>();

		// start an array to denote nodes visited or not
		// default boolean is set to false
		boolean[] visited = new boolean[g.getNodes().size()];
		
		// create queue of nodes to be visited, starting with n1
		LinkedList<Node<String>> queue = new LinkedList<Node<String>> ();
		
		// mark the starting node n1 as visited and enqueue it
		visited[N.indexOf(n1)] = true;
		queue.add(n1);
		
		String s = "path from " + node1 + " to " + node2 + ":\n";
		
		// if pathing to itself, nothing to search for
		// simply return back with s
		if (node1.equals(node2))
			return s;

		// while we have nodes in the queue to traverse
		while (queue.size() != 0) {

			//dequeue the node. we are going to traverse its children
			Node<String> qn = queue.poll();
			

			// Get all Edges of the dequeued node qn (the edges has the child nodes info) 
			// sort this list of edges by child+label; and use the iterator to this list for pathing
			// If any of the child of these edges nodes is n2, we found the shortest lexical bfs path => return path
			// mark node as visited, and add to queue => to traverse children of this child node
			// create qe edge list, to be used in pathing
			ArrayList<Edge<String,String,Double>> qe = new ArrayList<Edge<String,String,Double>>(g.listChildEdges(qn));
			
			Collections.sort(qe, this.childComparator);

			for (int i = 0; i < qe.size(); i++)
			{
				Edge<String,String,Double> next_edge = qe.get(i);
				path.add(next_edge);

				Node<String> cn = next_edge.getCnode();

				if (cn.equals(n2)) { // we have found the lexically shortest bfs path

					// clean up path - remove other paths traversed
					// start with last edge. compare with one prior edge
					// if parent of last edge != child of one-prior-edge, 
					// drop the one-prior-edge

					int last = path.size()-1;
					int prior = last -1;
					while (last > 0){
						if (!path.get(last).getPnode().equals(path.get(prior).getCnode())) 
							path.remove(prior);
						last = last  -1;
						prior = last -1;
					}

					// write out the path
					for (int j= 0; j < path.size(); j++)
						s = s + path.get(j).getPnode().getName() + " to " + path.get(j).getCnode().getName() + " via " + path.get(j).getLabel() + "\n";           		
					return s;
				}
				
				// if here, the child node was not the destined node2. Therefore:
				// now that this child node has been visited, add it to the visited queue
				// need to do this, to bail out of circular paths once node is visited
				// finally add this child node to the queue, to traverse its child nodes further
				int idx = N.indexOf(cn);
				if (!visited[idx]) 
				{ 
					visited[idx] = true; 
					queue.add(cn);
				}
				else
					path.remove(path.size()-1); // child node has been visited before from this path, so remove

			}

		}

		// if here, we did not find a path from n1 to n1
		s = s + "no path found\n";
		
		return s;
	}
	
//	public static void main(String[] arg) {
//		
//		//long start = System.currentTimeMillis();
//		
//		//create graph using marvel.csv
//		MarvelPaths mp = new MarvelPaths();
//		mp.createNewGraph("data/marvel.csv");
//		
//		//long end = System.currentTimeMillis();
//		
//		//System.out.println(end-start);
//		
//	}
	
}