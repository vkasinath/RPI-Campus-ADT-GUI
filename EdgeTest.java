package hw4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EdgeTest {

	
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
	
	//comparison edge
	private Edge<String,String,Double> e4 = new Edge<String,String,Double>(n5, n6, "DL 2053", 9.7);
	

	@Test 	// Runtime exception for null edge
	public void testNullEdge()
	{
		// null node name
		boolean flag = false;
		String s = null;
		try {
			Edge<String,String,Double> X = new Edge<String,String,Double>(n1, n2, s, 0.0);
		}
		catch (Exception e) {
			flag = true;
		}
		assertTrue(flag);
	}
	
	@Test // String getLabel
	public void testgetLabel() {
		assertEquals(e1.getLabel(), "AA 4198");
		assertEquals(e2.getLabel(), "UA 637");

		assertEquals(e3.toString(), "edge: DL 2053 Seattle -> Miami (9.7)");
	}
	
	
	@Test // String getPnode
	public void testgetPnode() {
		assertEquals(e1.getPnode().getName(), "Chicago");
		assertEquals(e2.getPnode().getName(), "New York City");
	}
	
	@Test // String getCnode
	public void testgetCnode() {
	
		assertEquals(e1.getCnode().getName(), "Atlanta");
		assertEquals(e2.getCnode().getName(), "Los Angeles");

	}
	
	@Test // String getWeight
	public void testgetWeight() {
		
		assertEquals(e2.getWeight(), 6.2, 0.001);
		assertEquals(e3.getWeight(), 9.7, 0.001);
	}
	
	@Test // String setLabel
	public void testsetLabel() {

		e1.setLabel("AA 374");
		e2.setLabel("UA 2916");
		
		assertEquals(e1.toString(), "edge: AA 374 Chicago -> Atlanta (2.0)");
		assertEquals(e2.toString(), "edge: UA 2916 New York City -> Los Angeles (6.2)");
	}
	
	@Test // String setPnode
	public void testsetPnode() {
		
		e1.setPnode(n4);
		e2.setPnode(n6);
		
		assertEquals(e1.getPnode().getName(), "Los Angeles");
		assertEquals(e2.getPnode().getName(), "Miami");
	}
	
	
	@Test // String setCnode
	public void testsetCnode() {
		
		e1.setCnode(n5);
		e2.setCnode(n1);
		
		assertEquals(e1.getCnode().getName(), "Seattle");
		assertEquals(e2.getCnode().getName(), "Chicago");
	}
	
	@Test // String setWeight
	public void testsetWeight() {
		
		e1.setWeight(1.2);
		e2.setWeight(4.7);
		
		assertEquals(e1.getWeight(), 1.2, 0.001);
		assertEquals(e2.getWeight(), 4.7, 0.001);
	}
	
	@Test // equals testing
	public void testequals() {

		assertEquals(false, e1.equals(e2));
		
		assertEquals(false, e2.equals(e3));

		assertEquals(true, e3.equals(e4));


		// equals should fail for different class comparison
		// compare Edge with string
		String s = new String("Atlanta");
		assertEquals(false, e1.equals(s));
	}
		  
}
