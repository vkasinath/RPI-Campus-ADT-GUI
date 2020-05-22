package hw4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

/**
 * <b>Graph</b> Class/Object representing a Collection of <b>Nodes</b> and <b>Edges</b>.<br> 
 * A graph may have <code>0</code> Nodes and <code>0</code> edges (empty graph).<br>
 * The Graph is configured as a Directed Graph.<br>
 * The Graph does not allow duplicate nodes or edges.<br>
 * Only unique values of parent node-child node-edge label are stored.<br>
 * Additional duplicate nodes, and duplicate edges are discarded silently.<p>
 * Abstraction: Graph is an abstraction function that connects nodes and edges<br>
 * Arraylist of Nodes: a mutable list of nodes. <br>
 * Arraylist of Edges: a mutable list of edges, for each parent-child node path<p>
 * Rep Invariant: Check for Duplicate Nodes Check for Duplicate Edges
 **/


public class Graph<T1,T2,T3> {

	private ArrayList<Node<T1>> nodeList;
	private ArrayList<Edge<T1,T2,T3>> edgeList;
	
	// Constructor
	/**
	 * This Method constructs a new graph with <code>0</code> Nodes 
	 * and <code>0</code> Edges
	 **/
	
	public Graph() {
		nodeList = new ArrayList<Node<T1>>();
		edgeList = new ArrayList<Edge<T1,T2,T3>>();
	}
	
	/**
	 * @param N : ArrayList of Nodes - The list of nodes in the graph<br>
	 * @param E : ArrayList of Edges - The list of Edges in the graph<p>
	 *
	 * This method constructs a new graph with List of Nodes and Edges<br>
	 * Parent And Child nodes in edges should be in the list of Nodes <br>
	 **/
	public void setNodesEdges(ArrayList<Node<T1>> n, ArrayList<Edge<T1,T2,T3>> e) {
		this.nodeList = n;
		this.edgeList= e;
		checkRep();
	}

	/**
	 * @param N : ArrayList of Nodes - The list of nodes in the graph<br>
	 * @param E : ArrayList of Edges - The list of Edges in the graph<p>
	 *
	 * This method constructs a new graph with List of Nodes and Edges<br>
	 * Parent And Child nodes in edges should be in the list of Nodes <br>
	 **/

	public Graph(ArrayList<Node<T1>> nl, ArrayList<Edge<T1,T2,T3>> el) {
		this();
		this.nodeList = nl;
		this.edgeList = el;
		checkRep();
	}
	
	/**
	 * @param aNode : a single Node identified by aNode<br>
	 * @modifies new node aNode added to the graph
	 **/

	public void addNode(Node<T1> aNode) {
		
		if (!nodeList.contains(aNode))
			nodeList.add(aNode);
		
	}
	
	/**
	 * @return ArrayList of Nodes in the Graph
	 **/

	public ArrayList<Node<T1>> getNodes(){
		return this.nodeList;
	}
	
	/**
	 * @param aEdge : a single Edge identified by aEdge<br>
	 *
	 * @modifies add edge to graph if one does not exist<br> 
	 **/

	public void addEdge(Edge<T1,T2,T3> aEdge) {

		if (!nodeList.contains(aEdge.getPnode()))
			nodeList.add(aEdge.getPnode());
		
		if (!nodeList.contains(aEdge.getCnode()))
			nodeList.add(aEdge.getCnode());
		
		//if (!edgeList.contains(aEdge))
			edgeList.add(aEdge);
	
		checkRep();

	}
	/**
	 * @return ArrayList of Edges in the Graph
	 **/

	public ArrayList<Edge<T1,T2,T3>> getEdges(){
		return this.edgeList;
	}
	
	/**
	 * @return Iterator of Edges with empty labels in Graph
	 **/

	public Iterator<String> listEmptyLabelEdges() {

		final ArrayList<String> lEmpty = new ArrayList<String>();

		for (int i = 0; i < this.edgeList.size(); i++)
			if (edgeList.get(i).getLabel().toString().equals("")) {
				String s = edgeList.get(i).getPnode().getName().toString() + "(" + edgeList.get(i).getCnode().getName().toString() + ")";
				lEmpty.add(s);
			}
			Collections.sort(lEmpty);
			return lEmpty.iterator();
	}
	
	/**
	 * @return Iterator of reflexive edges in the Graph
	 **/

	public Iterator<String> listMultipleReflexiveEdges() {

		final ArrayList<String> lRef = new ArrayList<String>();

		for (int i = 0; i < this.edgeList.size(); i++)
			if (edgeList.get(i).getPnode().getName().toString().equals(edgeList.get(i).getCnode().getName().toString())) {
				String s = edgeList.get(i).getPnode().getName().toString() + "(" + edgeList.get(i).getLabel().toString() + ")";
				lRef.add(s);
			}
			Collections.sort(lRef);
			return lRef.iterator();
	}
	
	/**
	 * @return an iterator of sorted node names
	 **/
	
	public Iterator<String> listNodes() {

		final ArrayList<String> lNode = new ArrayList<String>();

		for (int i = 0; i < this.nodeList.size(); i++)
			lNode.add(this.nodeList.get(i).getName().toString());
			Collections.sort(lNode);
			return lNode.iterator();
	}
	

	/**
	 * @param parentNode : The parent node of the list of children generated<br>
	 * @return for a given parent, returns an iterator of sorted childNode(edge label)
	 **/

	public Iterator<String> listEdges(Node<T1> parentNode) {

		final ArrayList<String> lEdge = new ArrayList<String>();

		for (int i = 0; i < this.edgeList.size(); i++)
			if (edgeList.get(i).getPnode().getName().toString().equals(parentNode.getName().toString())) {
				String s = edgeList.get(i).getCnode().getName().toString() + "(" + 
						   edgeList.get(i).getLabel().toString() + ")";
				lEdge.add(s);
			}
			Collections.sort(lEdge);
			return lEdge.iterator();
	}
	
	
	/**
	 * @param parentNode : The parent node of the list of edges generated<br>
	 * @return for a given parent, returns an arrayList of edges
	 **/

	public ArrayList<Edge<T1,T2,T3>> listChildEdges(Node<T1> parentNode) {

		final ArrayList<Edge<T1,T2,T3>> E = new ArrayList<Edge<T1,T2,T3>>();

		for (int i = 0; i < this.edgeList.size(); i++)
			if (edgeList.get(i).getPnode().equals(parentNode))
				E.add(edgeList.get(i));

			return E;
	}
	/**
	 * @return a String representing this, in reduced terms.
	 **/

	@Override	//prints graph information
	public String toString() {
		String s = "";
		
		s = "Nodes: [";
		for (int i=0; i < nodeList.size(); i++) {
			if (i==0)
				s = s + nodeList.get(i).getName().toString();
			else
				s = s + ", " + nodeList.get(i).getName().toString();
		}
		s = s + "]\n";
		
		s = s+ "Edges: [";
		for (int i=0; i < edgeList.size(); i++) {
			if (i==0)
				s = s + edgeList.get(i).getLabel().toString() + " " + 
						edgeList.get(i).getPnode().getName().toString() + " -> " +
						edgeList.get(i).getCnode().getName().toString() + " (" + 
						edgeList.get(i).getWeight().toString() + ")";
			else
				s = s + ", " + edgeList.get(i).getLabel().toString() + " " + 
							   edgeList.get(i).getPnode().getName().toString() + " -> " +
							   edgeList.get(i).getCnode().getName().toString() + " (" + 
							   edgeList.get(i).getWeight().toString() + ")";

		}
		return s;
	}
	
	/**
	 * Checks that the representation invariant holds (if any). 
	 * Throws a RuntimeException if rep invariant is violated.<br>
	 **/

    // Throws a RuntimeException if the rep invariant is violated.
    private void checkRep() throws RuntimeException {
    	
    	//Create set based check for checkRep
    	HashSet<Node<T1>> pl = new HashSet<Node<T1>>();
    	HashSet<Node<T1>> cl = new HashSet<Node<T1>>();
    	
    	for (int i=0; i < edgeList.size(); i++) {
    		Edge<T1,T2,T3> e = edgeList.get(i);
    		pl.add(e.getPnode());
    		cl.add(e.getCnode());
    	}
    	
    	//Check if nl retains all parent edge nodes
    	HashSet<Node<T1>> nl = new HashSet<Node<T1>>(this.nodeList);
    	if (!nl.containsAll(pl))
    		throw new RuntimeException("Parent node not found in nodeList");
    	
    	//Check if cl retains all child edge nodes
    	nl = new HashSet<Node<T1>>(this.nodeList);
    	if (!nl.containsAll(cl))
    		throw new RuntimeException("Child node not found in nodeList");
    }
	
}
