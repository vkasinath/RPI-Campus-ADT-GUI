package hw4;

/**
 * <b>Node</b> Class/Object representing a <b>Node</b> in a graph.<br>
 * it is defined by a unique name.
 * Define a Node of type String<br>
 * Abstraction:	the name of the node in the graph<br>
 * Rep Invariant: the name cannot be null or zero-length<br>
 **/

public class Node<T1>{
    private T1 name;

    // Constructor
 	/**
 	 * 	@param nm:	The Node Name<br>
 	 *	This method constructs a new node with name = nm<br>
 	 **/
    public Node(T1 nm) {
        this.name = nm;
        checkRep();
    }

	/**
	 * 	getName: get Name of Node<br>
	 * 	@return	String name
	 **/
    
    public T1 getName() {
        return name;
    }

    /**
    * SetName of Node<br>
    * change the Name of a node<p>
    *	 
 	* @param nm : this.nm will be set to argument new name <br>
 	* @effects this.name changed to nm<br>
 	**/

    public void setName(T1 nm) {
        this.name = nm;
        checkRep();
    }
    
   	/**
	 *  Standard equality operation.<p>
	 *  @param nodeComp The object to be compared for equality.<br>
	 *  @return true if and only if 'nodeComp' is an instance of<br>
	 *  a Node and 'this' and 'nodeComp' represent the same node.<br>
	 **/
    
    
    @Override //compares two nodes
    public boolean equals(/*@Nullable*/ Object nodeComp) {
        if (getClass() != nodeComp.getClass()) {
        	return false;
        }
            
        Node<?> nc = (Node<?>) nodeComp;
        
        if (this.name == null &&  nc.name == null) {
            return false;
        } 

        if (this.name.toString().equals("") &&  nc.name.toString().equals("")) {
            return false;
        } 
        
        if ( (!this.name.toString().equals(nc.name.toString())) ) {
        	return false;
        }
            
        return true;

    }   
    
	/** 
	 * @return a String representing this, in reduced terms.<br>
	 * Example, Node object with a name "Atlanta" will be<br>
	 * represented as a string 'node: Atlanta'.<br>
	 **/
    
	@Override	//prints node name
	public String toString() {
		return ("node: " + this.name.toString());
	}

	/**
	 *Checks that the representation invariant holds (if any).<br>
	 *Throws a RuntimeException if the rep invariant is violated.<br>
	 **/

    // Throws a RuntimeException if the rep invariant is violated.
    private void checkRep() throws RuntimeException {
        if (this.name.toString().length() == 0) {
            throw new RuntimeException("Node name is invalid");
        }
     
        if (this.name == null) {
            throw new RuntimeException("Node name is undefined");
        }
    }
    
    //overrides the hashCode of a node in favore of the node name
    @Override
    public int hashCode() {
    	return this.name.hashCode();
    }
            
}
    


