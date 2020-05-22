package hw4;

import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

public class GraphWrapperTest {
	
	
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
	
//	//nodeList and edgeList
//	private ArrayList<Node<String>> Nodes = new ArrayList<>(Arrays.asList(n1, n2, n3, n4, n5, n6));
//	private ArrayList<Edge<String,String,Double>> Edges = new ArrayList<>(Arrays.asList(e1, e2, e3, e4));
//	
//	//simple graph
//	private GraphWrapper<String,String,Double> g = new GraphWrapper<String,String,Double>();

	@Test //addNode
	public void testaddNode() {
		Node<String> n7 = new Node<String>("Albany");
		
		GraphWrapper<String,String,Double> X = new GraphWrapper<String,String,Double>();
		X.addNode(n7.getName());
		
		Iterator<String> n = X.listNodes();
		
		String s = "";
		
		while (n.hasNext()) {
			s = n.next();
			if (s.equals(n7.getName()))
				break;
		}
		
		assertEquals(s, n7.getName());
	}
	
	@Test //addEdge
	public void testaddEdge() {
		Node<String> n7 = new Node<String>("Albany");
		Edge<String,String,Double> e5 = new Edge<String,String,Double>(n7, n3, "BA 260", 14.8);
		
		GraphWrapper<String,String,Double> X = new GraphWrapper<String,String,Double>();
		X.addNode(n7.getName());
		X.addEdge(e5.getPnode().getName(), e5.getCnode().getName(), e5.getLabel());
		

		Iterator<String> n = X.listChildren(n7.getName());
		String s = "";
		
		while (n.hasNext()) {
			s = n.next();
			if (s.equals("New York City(BA 260)"))
				break;
		}
		
		assertEquals(s, "New York City(BA 260)");
		
		//System.out.print(X.toString());

	}

	@Test // node iterator
	public void testlistNode() {
		GraphWrapper<String,String,Double> X = new GraphWrapper<String,String,Double>();
		X.addNode(n1.getName());
		X.addNode(n2.getName());
		X.addNode(n3.getName());
		X.addNode(n4.getName());
		X.addNode(n5.getName());
		X.addNode(n6.getName());

		Iterator<String> ni =  X.listNodes();
		
		String s = "";
		
		while (ni.hasNext()) 
           s = s + ni.next() + ", ";
		
		//System.out.print(s);
		assertEquals(s, "Atlanta, Chicago, Los Angeles, Miami, New York City, Seattle, ");

	}
	
	
	@Test // edge iterator
	public void testlistEdge() {
		GraphWrapper<String,String,Double> X = new GraphWrapper<String,String,Double>();
		X.addNode(n1.getName());
		X.addNode(n2.getName());
		X.addNode(n3.getName());
		X.addNode(n4.getName());
		X.addNode(n5.getName());
		X.addNode(n6.getName());

		X.addEdge(e1.getPnode().getName(), e1.getCnode().getName(), e1.getLabel());
		X.addEdge(e2.getPnode().getName(), e2.getCnode().getName(), e2.getLabel());
		X.addEdge(e3.getPnode().getName(), e3.getCnode().getName(), e3.getLabel());
		X.addEdge(e4.getPnode().getName(), e4.getCnode().getName(), e4.getLabel());

		Iterator<String> ei =  X.listChildren(n2.getName());
		
		String s = "";
		
		while (ei.hasNext()) 
           s = s + ei.next() + ", ";
		
		//System.out.print(s);
		assertEquals(s, "New York City(SWA 1168), ");

	}
	
	@Test //test for reflexive edges
	public void testListMultipleReflexiveEdges() {
		
		GraphWrapper<String,String,Double> X = new GraphWrapper<String,String,Double>();
		X.addNode("A");
		X.addNode("B");
		X.addNode("C");
		
		X.addEdge("A", "A", "label1");
		X.addEdge("B", "B", "label3");
		X.addEdge("C", "C", "label2");
		

		Iterator<String> n = X.listMultipleReflexiveEdges();
		String s = "[";
		boolean b = true;
		
		while (n.hasNext()) {
			if (b) {
				s = s + n.next();
				b = false;
			}
			else
				s = s + ", " + n.next();
		}
		s = s + "]";
		
		
		assertEquals(s, "[A(label1), B(label3), C(label2)]");
		
		//System.out.print(X.toString());

	}
	
	@Test //test for empty label edges
	public void testListEmptyLabelEdges(){
		
		GraphWrapper<String,String,Double> X = new GraphWrapper<String,String,Double>();
		X.addNode("A");
		X.addNode("B");
		X.addNode("C");
		
		X.addEdge("A", "A", "");
		X.addEdge("B", "B", "");
		X.addEdge("C", "C", "");
		

		Iterator<String> n = X.listEmptyLabelEdges();
		String s = "[";
		boolean b = true;
		
		while (n.hasNext()) {
			if (b) {
				s = s + n.next();
				b = false;
			}
			else
				s = s + ", " + n.next();
		}
		s = s + "]";
		
		
		assertEquals(s, "[A(A), B(B), C(C)]");
		
		//System.out.print(X.toString());

	}
	
}
