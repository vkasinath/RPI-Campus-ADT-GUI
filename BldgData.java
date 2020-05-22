/**
 * 
 */
package hw7;

/**
 * BldgData  is a class that represents all the building data
 * Includes the building id, name, x coordinate and y coordinate
 */
public class BldgData {
	
	//private variables
	private String bldgID;
	private String bldgName;
	private Double x;
	private Double y;
	
	
	// Constructor
	/**
	 * This Method constructs a new BldgData x coordinate <code>0</code>
	 * and y coordinate <code>0</code> as well as a null bldgID and bldgName
	 **/
	public BldgData() {
		bldgID = null;
		bldgName = null;
		x = 0.0;
		y = 0.0;
	}

	
	/**
	 * getBldgID: get building ID
	 * @return String bldgID
	 **/
	public String getBldgID() {
		return bldgID;
	}

	/**
	 * Set BldgID for a BldgData
	 * 
	 * @param bldgID : A string value for updating the building ID.<br>
	 * @effects this.bldgID changed to bldgID<br>
	 **/
	public void setBldgID(String bldgID) {
		this.bldgID = bldgID;
	}

	/**
	 * getBldgName: get building name
	 * @return String bldgName
	 **/
	public String getBldgName() {
		return bldgName;
	}

	/**
	 * Set BldgName for a BldgData
	 * 
	 * @param bldgName : A string value for updating the building Name.<br>
	 * @effects this.bldgName changed to bldgName<br>
	 **/
	public void setBldgName(String bldgName) {
		this.bldgName = bldgName;
	}

	/**
	 * getX: get building x coordinate
	 * @return String Double x
	 **/
	public Double getX() {
		return x;
	}

	/**
	 * Set X coordinate for a BldgData
	 * 
	 * @param x : A Double value for updating the building x coordinate.<br>
	 * @effects this.x changed to x<br>
	 **/
	public void setX(Double x) {
		this.x = x;
	}

	/**
	 * getY: get building y coordinate
	 * @return Double y
	 **/
	public Double getY() {
		return y;
	}

	/**
	 * Set Y coordinate for a BldgData
	 * 
	 * @param y : A Double value for updating the building y coordinate.<br>
	 * @effects this.y changed to y<br>
	 **/
	public void setY(Double y) {
		this.y = y;
	}

	
	
}
