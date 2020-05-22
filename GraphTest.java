package hw4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

public class GraphTest {
	
	
	// simple base nodes
	private Node<String> n1 = new Node<String>("Chicago");
	private Node<String> n2 = new Node<String>("Atlanta");
	private Node<String> n3 = new Node<String>("New York City");
	private Node<String> n4 = new Node<String>("Los Angeles");
	private Node<String> n5 = new Node<String>("Seattle");
	private Node<String> n6 = new Node<String>("Miami");
	
	//simple base edges
	private Edge<String,String,Double> e1 = new Edge<String,String,Double>(n1, n2, "AA 4198", 2.0);
	private Edge<String,String,Double> e2 = new Edge<String,String,Double>(n3, n4, "UA 637", 6.2);
	private Edge<String,String,Double> e3 = new Edge<String,String,Double>( n5, n6, "DL 2053", 9.7);
	private Edge<String,String,Double> e4 = new Edge<String,String,Double>(n2, n3, "SWA 1168", 4.3);
	
	//nodeList and edgeList
	private ArrayList<Node<String>> Nodes = new ArrayList<>(Arrays.asList(n1, n2, n3, n4, n5, n6));
	private ArrayList<Edge<String,String,Double>> Edges = new ArrayList<>(Arrays.asList(e1, e2, e3, e4));
	
	//simple graph
	private Graph<String,String,Double> g = new Graph<String,String,Double>(Nodes, Edges);
	
	@Test //testcheckRepParentNode
	public void testcheckRepP() {
		
		Node<String> n7 = new Node<String>("Albany");
		Edge<String,String,Double> e5 = new Edge<String,String,Double>(n7, n3, "BA 260", 14.8);
		Edges.add(e5);
		
		// null node name
		boolean flag = false;
		try {
			Graph<String,String,Double> X = new Graph<String,String,Double>(Nodes, Edges);
		}
		catch (Exception e) {
			flag = true;
		}
		assertTrue(flag);
		
	}


	@Test //testcheckRepChildNode
	public void testcheckRepC() {
		
		Node<String> n7 = new Node<String>("Albany");
		Edge<String,String,Double> e5 = new Edge<String,String,Double>(n3, n7, "BA 260", 14.8);
		Edges.add(e5);
		
		// null node name
		boolean flag = false;
		try {
			Graph<String,String,Double> X = new Graph<String,String,Double>(Nodes, Edges);
		}
		catch (Exception e) {
			flag = true;
		}
		assertTrue(flag);
		
	}
	
	@Test //addNode
	public void testaddNode() {
		Node<String> n7 = new Node<String>("Albany");
		
		Graph<String,String,Double> X = new Graph<String,String,Double>(Nodes, Edges);
		X.addNode(n7);
		
		//System.out.print(X.toString());
		assertEquals(X.toString(), 
				"Nodes: [Chicago, Atlanta, New York City, Los Angeles, Seattle, Miami, Albany]\n" +
				"Edges: [AA 4198 Chicago -> Atlanta (2.0), UA 637 New York City -> Los Angeles (6.2), DL 2053 Seattle -> Miami (9.7), SWA 1168 Atlanta -> New York City (4.3)"
						);
	}
	
	@Test //addEdge
	public void testaddEdge() {
		Node<String> n7 = new Node<String>("Albany");
		Edge<String,String,Double> e5 = new Edge<String,String,Double>(n7, n3, "BA 260", 14.8);
		
		Graph<String,String,Double> X = new Graph<String,String,Double>(Nodes, Edges);
		X.addNode(n7);
		X.addEdge(e5);
		
		//System.out.print(X.toString());
		assertEquals(X.toString(), 
				"Nodes: [Chicago, Atlanta, New York City, Los Angeles, Seattle, Miami, Albany]\n" +
				"Edges: [AA 4198 Chicago -> Atlanta (2.0), UA 637 New York City -> Los Angeles (6.2), DL 2053 Seattle -> Miami (9.7), SWA 1168 Atlanta -> New York City (4.3), BA 260 Albany -> New York City (14.8)"
						);
	}
	
	@Test //getNodes
	public void testgetNodes() {
		Node<String> n7 = new Node<String>("Albany");
		
		Graph<String,String,Double> X = new Graph<String,String,Double>(Nodes, Edges);
		X.addNode(n7);
		
		ArrayList<Node<String>> gNodeList = new ArrayList<Node<String>>(X.getNodes());
		
		//System.out.print(X.toString());
		assertEquals(X.getNodes(), gNodeList); 

	}
	
	@Test //getEdges
	public void testgetEdges() {
		Node<String> n7 = new Node<String>("Albany");
		Edge<String,String,Double> e5 = new Edge<String,String,Double>(n7, n3, "BA 260", 14.8);
		
		Graph<String,String,Double> X = new Graph<String,String,Double>(Nodes, Edges);
		X.addNode(n7);
		X.addEdge(e5);
		
		
		ArrayList<Edge<String,String,Double>> gEdgeList = new ArrayList<Edge<String,String,Double>>(X.getEdges());
		
		//System.out.print(X.toString());
		assertEquals(X.getEdges(), gEdgeList); 

	}
	
	@Test // node iterator
	public void testlistNode() {
		Graph<String,String,Double> X = new Graph<String,String,Double>(Nodes, Edges);

		Iterator<String> ni =  X.listNodes();
		
		String s = "";
		
		while (ni.hasNext()) 
           s = s + ni.next() + ", ";
		
		//System.out.print(s);
		assertEquals(s, "Atlanta, Chicago, Los Angeles, Miami, New York City, Seattle, ");

	}
	
	
	@Test // edge iterator
	public void testlistEdge() {
		Graph<String,String,Double> X = new Graph<String,String,Double>(Nodes, Edges);

		Iterator<String> ei =  X.listEdges(n2);
		
		String s = "";
		
		while (ei.hasNext()) 
           s = s + ei.next() + ", ";
		
		//System.out.print(s);
		assertEquals(s, "New York City(SWA 1168), ");

	}
	
}
