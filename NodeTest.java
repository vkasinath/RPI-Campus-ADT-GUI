package hw4;

import static org.junit.Assert.*;
import org.junit.Test;

public class NodeTest {

	// simple base nodes
	private Node<String> n1 = new Node<String>("Chicago");
	private Node<String> n2 = new Node<String>("Atlanta");
	private Node<String> n3 = new Node<String>("New York City");
	private Node<String> n4 = new Node<String>("Los Angeles");
	private Node<String> n5 = new Node<String>("Seattle");
	private Node<String> n6 = new Node<String>("Miami");

	// comparison nodes
	private Node<String> n8 = new Node<String>("Atlanta");
	private Node<String> n9 = new Node<String>("Atalanta");

	@Test 	// Runtime exception for zero length node
	public void testZeroLengthNode()
	{
		// zero length node name
		boolean flag = false;
		String s = "";
		try {
			Node<String> X = new Node<String>(s);
		}
		catch (Exception e) {
			flag = true;
		}
		assertTrue(flag);
	}

	
	@Test 	// Runtime exception for null node
	public void testNullNode()
	{
		// null node name
		boolean flag = false;
		String s = null;
		try {
			Node<String> X = new Node<String>(s);
		}
		catch (Exception e) {
			flag = true;
		}
		assertTrue(flag);
	}

	@Test // String getName
	public void testgetName() {
		assertEquals(n1.getName(), "Chicago");
		assertEquals(n2.getName(), "Atlanta");
		assertEquals(n3.getName(), "New York City");
		assertEquals(n4.getName(), "Los Angeles");

		assertEquals(n5.toString(), "node: Seattle");
		assertEquals(n6.toString(), "node: Miami");
	}
	
	@Test // String setName
	public void testsetName() {

		n1.setName("Indianapolis");
		n3.setName("Newark");
		n4.setName("Albany");

		assertEquals(n1.toString(), "node: Indianapolis");
		assertEquals(n3.toString(), "node: Newark");
		assertEquals(n4.toString(), "node: Albany");
	}


	@Test // equals testing
	public void testequals() {

		assertEquals(false, n2.equals(n9));
		
		assertEquals(false, n3.equals(n5));

		assertEquals(true, n2.equals(n8));


		// equals should fail for different class comparison
		// compare node with string
		String s = new String("Atlanta");
		assertEquals(false, n2.equals(s));
	}

}
