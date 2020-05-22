package hw4;

import java.util.Iterator;

/**
 * <b>GraphWrapper</b> is an interface to the <b>Graph</b> ADT.<br>
 * The graph may be empty (<code>0</code> Nodes and <code>0</code> Edges).<br>
 * The Graph is configured as a Directed Graph.<br>
 * The Graph does not retain duplicate nodes or edges.<br>
 * Duplicating Nodes and Edges are silently discarded.<br> 
 * Only unique values of parent node-child node-edge label are stored<br>
 * 
 * Abstraction: Graph(): a mutable Graph object
 **/

public class GraphWrapper<T1,T2,T3> {
	
	private Graph<T1,T2,T3> g;
	
	// Constructor
	
	/**
	* This method constructs a new, empty graph<br>
	* No parameters are needed<br>
	**/
	
	public GraphWrapper() {
		g = new Graph<T1,T2,T3>();
	}
	
	/**
	 * @param nodeData : a single Node identified by nodeData.<br>
	 * @modifies new node aNode added to the graph
	 **/

	public void addNode(T1 nodeData) {
		g.addNode(new Node<T1>(nodeData));
	}
	
	/**
	 * @param parentNode : the parent node of the edge to be added<br>
	 * @param childNode  : the child node of the edge to be added<br>
	 * @param edgeLabel  : the label of the edge, identifies the edge<br>
	 *
	 * @modifies add edge to graph if one does not exist<br> 
	 **/

	public void addEdge(T1 parentNode, T1 childNode, T2 edgeLabel) {
		g.addEdge(new Edge<T1,T2,T3>(new Node<T1>(parentNode), new Node<T1>(childNode), edgeLabel));
	}
	
	/**
	 * @return an iterator of sorted node names
	 **/

	public Iterator<String> listNodes(){
		return g.listNodes();
	}

	/**
	 * 
	 * @param parentNode : Parent node in a graph for which listChildren is generated<br>
	 * @return an iterator of sorted childnodes for a given parent, along with the edge label<br>
	 **/

	public Iterator<String> listChildren(T1 parentNode){
		Node<T1> n = new Node<T1>(parentNode);
		return g.listEdges(n);
	}
	
	/**
	 * @return an iterator of edges with all empty labels
	 **/

	
	public Iterator<String> listEmptyLabelEdges(){
		return g.listEmptyLabelEdges();
	}

	/**
	 * @return an iterator of all reflexive edges
	 **/

	public Iterator<String> listMultipleReflexiveEdges(){
		return g.listMultipleReflexiveEdges();
	}
	
	/**
	 * @return a String representing this, in reduced terms.
	 **/

	@Override	//prints graph information
	public String toString() {
		
		return g.toString();
	}
}
