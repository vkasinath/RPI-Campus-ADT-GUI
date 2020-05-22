package hw6;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;



public class MarvelPaths2Test{

	//file load
	@Test
	public void testValidFile() {

		boolean flag = false;
		try {
			MarvelPaths2 mp = new MarvelPaths2();
			mp.createNewGraph("data/marvel.csv");
		}
		catch (Exception e) {
			flag = true;
		}
		assertFalse(flag);

	}

	//start and end node find path
	@Test
	public void testValidPath() {

		MarvelPaths2 mp = new MarvelPaths2();
		mp.createNewGraph("data/marvel.csv");

		String s = mp.findPath("CAPTAIN AMERICA V/RO", "CLINTON, BILL");


		assertEquals(s, "path from CAPTAIN AMERICA V/RO to CLINTON, BILL:\n" + 
				"CAPTAIN AMERICA V/RO to FALCON/SAM WILSON with weight 0.143\n" + 
				"FALCON/SAM WILSON to CAPTAIN AMERICA with weight 0.005\n" + 
				"CAPTAIN AMERICA to IRON MAN/TONY STARK  with weight 0.002\n" + 
				"IRON MAN/TONY STARK  to CLINTON, BILL with weight 0.143\n" + 
				"total cost: 0.293\n");
	}

	//invalid n1
	@Test
	public void testInvalidNode1() {

		MarvelPaths2 mp = new MarvelPaths2();
		mp.createNewGraph("data/marvel.csv");

		String s = mp.findPath("VISHAAL KASINATH", "CLINTON, BILL");


		assertEquals(s, "unknown character VISHAAL KASINATH\n");
	}

	//test invalid n2
	@Test
	public void testInvalidNode2() {

		MarvelPaths2 mp = new MarvelPaths2();
		mp.createNewGraph("data/marvel.csv");

		String s = mp.findPath("CAPTAIN AMERICA V/RO", "KASINATH, VISHAAL");


		assertEquals(s, "unknown character KASINATH, VISHAAL\n");
	}
	

	//test reflexive node
	@Test
	public void testReflexiveNode() {

		MarvelPaths2 mp = new MarvelPaths2();
		mp.createNewGraph("data/marvel.csv");

		String s = mp.findPath("CAPTAIN AMERICA V/RO", "CAPTAIN AMERICA V/RO");


		assertEquals(s, "path from CAPTAIN AMERICA V/RO to CAPTAIN AMERICA V/RO:\ntotal cost: 0.000\n");
	}
	
}