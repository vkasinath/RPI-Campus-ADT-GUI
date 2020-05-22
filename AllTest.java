package hw4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hw5.MarvelPathsTest;
import hw6.MarvelPaths2Test;
import hw7.CampusDataTest;
import hw7.CampusPathsTest;

/**
 * This class contains the suite of tests for all the class objects in this package
 * Classes tested are:
 * <ul>
 * <li>Node</li>
 * <li>Edge</li>
 * <li>Graph</li>
 * <li>GraphWrapper</li>
 * </ul>
 */



@RunWith(Suite.class)
@SuiteClasses({	NodeTest.class, EdgeTest.class, 
				GraphTest.class, GraphWrapperTest.class,
				MarvelPathsTest.class, MarvelPaths2Test.class,
				CampusDataTest.class, CampusPathsTest.class
				})
public class AllTest {

	// no real code needed. just a stub class

}
