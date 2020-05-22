package hw7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import hw6.MarvelPaths2;

public class CampusDataTest {
	
	CampusData cd = new CampusData("data/RPI_map_data_Nodes.csv", "data/RPI_map_data_Edges.csv");

		@Test
		public void testValidPath() {
			
			String s = "";
			
			s = cd.findPath("7", "11");
			assertEquals(s, "Path from Troy Building to 87 Gymnasium:\n" + 
					"	Walk East to (Ricketts Building)\n" + 
					"	Walk East to (87 Gymnasium)\n" + 
					"Total distance: 156.051 pixel units.");
			
			s = cd.findPath("11", "7");
			assertEquals(s, "Path from 87 Gymnasium to Troy Building:\n" + 
					"	Walk West to (Ricketts Building)\n" + 
					"	Walk West to (Troy Building)\n" + 
					"Total distance: 156.051 pixel units.");
			
			s = cd.findPath("Troy Building", "87 Gymnasium");
			assertEquals(s, "Path from Troy Building to 87 Gymnasium:\n" + 
					"	Walk East to (Ricketts Building)\n" + 
					"	Walk East to (87 Gymnasium)\n" + 
					"Total distance: 156.051 pixel units.");
			
			s = cd.findPath("87 Gymnasium", "Troy Building");
			assertEquals(s, "Path from 87 Gymnasium to Troy Building:\n" + 
					"	Walk West to (Ricketts Building)\n" + 
					"	Walk West to (Troy Building)\n" + 
					"Total distance: 156.051 pixel units.");
			
			s = cd.findPath("12", "15");
			assertEquals(s, "Path from Quadrangle Complex to Playhouse:\n" + 
					"	Walk South to (Russell Sage Dining Hall)\n" + 
					"	Walk South to (Intersection 141)\n" + 
					"	Walk SouthEast to (Playhouse)\n" + 
					"Total distance: 185.375 pixel units.");
			
			s = cd.findPath("15", "12");
			assertEquals(s, "Path from Playhouse to Quadrangle Complex:\n" + 
					"	Walk NorthWest to (Intersection 141)\n" + 
					"	Walk North to (Russell Sage Dining Hall)\n" + 
					"	Walk North to (Quadrangle Complex)\n" + 
					"Total distance: 185.375 pixel units.");
			
			s = cd.findPath("13", "19");
			assertEquals(s, "Path from Russell Sage Dining Hall to Science Center:\n" + 
					"	Walk South to (Intersection 141)\n" + 
					"	Walk SouthWest to (CII)\n" + 
					"	Walk SouthWest to (DCC)\n" + 
					"	Walk SouthWest to (Intersection 138)\n" + 
					"	Walk SouthWest to (Science Center)\n" + 
					"Total distance: 234.128 pixel units.");
			
			s = cd.findPath("19", "13");
			assertEquals(s, "Path from Science Center to Russell Sage Dining Hall:\n" + 
					"	Walk NorthEast to (Intersection 138)\n" + 
					"	Walk NorthEast to (DCC)\n" + 
					"	Walk NorthEast to (CII)\n" + 
					"	Walk NorthEast to (Intersection 141)\n" + 
					"	Walk North to (Russell Sage Dining Hall)\n" + 
					"Total distance: 234.128 pixel units.");
			

		}
		
		@Test
		public void testReflexivePath() {
			
			String s = "";
			
			s = cd.findPath("7", "7");
			assertEquals(s, "Path from Troy Building to Troy Building:\n" + 
					"Total distance: 0.000 pixel units.");
			
			
		}
		
		@Test
		public void testPathNotFound() {
			
			String s = "";
			
			s = cd.findPath("7", "700");
			assertEquals(s, "Unknown building: [700]");
			
			s = cd.findPath("700", "7");
			assertEquals(s, "Unknown building: [700]");
			
			s = cd.findPath("700", "700");
			assertEquals(s, "Unknown building: [700]");
			
			s = cd.findPath("780", "700");
			assertEquals(s, "Unknown building: [780]\nUnknown building: [700]");
			
		}
		
		@Test
		public void testListBuildings() {
			
			String s = "";
			
			s = cd.listBldgNames().toString();
			assertEquals(s, "[133 Sunset Terrace,71, 200 Sunset Terrace,54,"
					+ " 2021 Peoples Avenue,33, 2144 Burdett Avenue,51,"
					+ " 41 Ninth Street,27, 87 Gymnasium,11, Academy Hall,67,"
					+ " Admissions,34, Alumni House,32,"
					+ " Alumni Sports & Recreation Center,37, Amos Eaton Hall,26,"
					+ " Barton Hall,73, Beman Park Firehouse,69, Blaw-Knox 1 & 2,29,"
					+ " Blitman Residence Commons,85, Boiler House at 11th Street,77,"
					+ " Boiler House at Sage Avenue,5, Bray Hall,48, Bryckwyck,61,"
					+ " Burdett Avenue Residence Hall,50, CBIS,74, CII,14,"
					+ " Carnegie Building,3, Cary Hall,47, Chapel and Cultural Center,49,"
					+ " Cogswell Laboratory,20, Colonie Apartments,66,"
					+ " Commons Dining Hall,39, Crockett Hall,40, DCC,17, Davison Hall,42,"
					+ " E Complex,9, EMPAC,76, East Campus Athletic Village Arena,89,"
					+ " East Campus Athletic Village Stadium,90, Empire State Hall,68,"
					+ " Engineering Center,18, Field House Houston,52, Folsom Library,23,"
					+ " Greene Building,24, Greenhouses and Grounds Barn,57, H Building,31,"
					+ " Hall Hall,46, J Building,30, Java++ Cafe,80, LINAC Facility,58,"
					+ " Lally Hall,25, Louis Rubin Memorial Approach,79, MRC,21,"
					+ " Mueller Center,72, Nason Hall,41, North Hall,8, Nugent Hall,44,"
					+ " OGE,91, Parking Garage,75, Patroon Manor,65, Pittsburgh Building,1,"
					+ " Playhouse,15, Polytechnic Residence Commons,86, Public Safety,36,"
					+ " Quadrangle Complex,12, RPI Ambulance,81, Radio Club,60,"
					+ " Rensselaer Apartment Housing Project RAHP A Site,53,"
					+ " Rensselaer Apartment Housing Project RAHP B Site,62,"
					+ " Rensselaer Union,35, Ricketts Building,10, Robison Swimming Pool,38,"
					+ " Russell Sage Dining Hall,13, Russell Sage Laboratory,6,"
					+ " Science Center,19, Seismograph Laboratory,55, Service Building,28,"
					+ " Sharp Hall,43, Stacwyck Apartments,59, Troy Building,7, VCC,22,"
					+ " Walker Laboratory,4, Warren Hall,45, West Hall,2, Winslow Building,78]");
			
			
		}
		
		@Test (expected = RuntimeException.class)
		public void testInvalidBFile() {
		
			cd = new CampusData("data/uknown.csv", "data/RPI_map_data_Edges.csv");	

		}
		
		@Test (expected = RuntimeException.class)
		public void testInvalidPFile() {
		
			cd = new CampusData("data/RPI_map_data_Nodes.csv", "data/unkown.csv");
			
		}
		
		@Test (expected = RuntimeException.class)
		public void testInvalidBuildFile() {
		
			cd = new CampusData("data/test_Nodes_no_comma.csv", "data/RPI_map_data_Edges.csv");
			
		}
		
		@Test (expected = RuntimeException.class)
		public void testInvalidPathFile() {
	
			cd = new CampusData("data/RPI_map_data_Nodes.csv", "data/test_Edges_no_comma.csv");
			
		}
		
		
		
	
}
