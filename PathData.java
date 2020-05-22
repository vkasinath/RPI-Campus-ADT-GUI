package hw7;

/**
 * PathData  is a class that represents all the path data
 * Includes the fromID(leaving from what building), toID(going to what building) and dist
 */
public class PathData {

	//private variables
	private String fromID;
	private String toID;
	private Double dist;
	
	// Constructor
		/**
		 * This Method constructs a new PathData with a <code>0</code> distance
		 * and null fromID and toID
		 **/
	public PathData() {
		fromID = null;
		toID = null;
		dist = 0.0;

	}

	/**
	 * getFromID: get path fromID
	 * @return String fromID
	 **/
	public String getFromID() {
		return fromID;
	}

	/**
	 * Set fromID for a PathData
	 * 
	 * @param fromID : A string value for updating the path fromID.<br>
	 * @effects this.fromID changed to fromID<br>
	 **/
	public void setFromID(String fromID) {
		this.fromID = fromID;
	}

	/**
	 * getToID: get path toID
	 * @return String toID
	 **/
	public String getToID() {
		return toID;
	}

	/**
	 * Set toID for a PathData
	 * 
	 * @param toID : A string value for updating the path toID.<br>
	 * @effects this.toID changed to toID<br>
	 **/
	public void setToID(String toID) {
		this.toID = toID;
	}

	/**
	 * getDist: get path distance
	 * @return Double dist
	 **/
	public Double getDist() {
		return dist;
	}

	/**
	 * Set dist for a PathData
	 * 
	 * @param toID : A Double value for updating the path dist.<br>
	 * @effects this.dist changed to dist<br>
	 **/
	public void setDist(Double dist) {
		this.dist = dist;
	}
	
	
}
