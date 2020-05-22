package hw5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;



public class MarvelPathsTest {

	//file load
	@Test
	public void testValidFile() {

		boolean flag = false;
		try {
			MarvelPaths mp = new MarvelPaths();
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

		MarvelPaths mp = new MarvelPaths();
		mp.createNewGraph("data/marvel.csv");

		String s = mp.findPath("CAPTAIN AMERICA V/RO", "CLINTON, BILL");


		assertEquals(s, "path from CAPTAIN AMERICA V/RO to CLINTON, BILL:\n" + 
				"CAPTAIN AMERICA V/RO to CAPTAIN AMERICA via CA 178\n" + 
				"CAPTAIN AMERICA to CLINTON, BILL via CA 450\n");
	}

	//invalid n1
	@Test
	public void testInvalidNode1() {

		MarvelPaths mp = new MarvelPaths();
		mp.createNewGraph("data/marvel.csv");

		String s = mp.findPath("VISHAAL KASINATH", "CLINTON, BILL");


		assertEquals(s, "unknown character VISHAAL KASINATH\n");
	}

	//test invalid n2
	@Test
	public void testInvalidNode2() {

		MarvelPaths mp = new MarvelPaths();
		mp.createNewGraph("data/marvel.csv");

		String s = mp.findPath("CAPTAIN AMERICA V/RO", "KASINATH, VISHAAL");


		assertEquals(s, "unknown character KASINATH, VISHAAL\n");
	}
	
	//test reflexive node
	@Test
	public void testReflexiveNode() {

		MarvelPaths mp = new MarvelPaths();
		mp.createNewGraph("data/marvel.csv");

		String s = mp.findPath("CAPTAIN AMERICA V/RO", "CAPTAIN AMERICA V/RO");


		assertEquals(s, "path from CAPTAIN AMERICA V/RO to CAPTAIN AMERICA V/RO:\n");
	}

}