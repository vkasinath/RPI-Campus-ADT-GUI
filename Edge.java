package hw4;

/**
 * <b>Edge</b> Class/obj representing an <b>Edge</b> in a graph. <br>
 * It is defined by two Nodes (Parent, Child), an edge label, and<br> 
 * an optional edge value.<br>
 * If not provided, edge value will be set to 0.0<br>
 * Abstraction: label - identifier of the label pNode<br>
 * 			pnode - the parent Node<br>
 * 			cnode - cNode the child Node<br> 
 * 			weight -the edge Value (round to 2 decimal places)<br>
 * Abstraction function: Edge is the abstraction function that<br>
 * connects a parent node to a child node by the edge label<br>
 * Rep Invariant: Edge Label cannot be null Parent and Child node<br> 
 * cannot be null<br>
 **/

public class Edge<T1,T2,T3> {
	private T2 label;
	private Node<T1> pnode;
	private Node<T1> cnode;
	private T3 weight;
	
	
	// Constructor
	/**
	 * @param pn   : Parent node for constructing the edge<br>
	 * @param cn   : Child node for constructing the edge<br>
	 * This method constructs a new edge, connecting parent pn<br> 
	 * with child cn, the edge weight is set to 0.0,<br>
	 * the edge label is set to "" (aka empty).<br>
	 **/

	private Edge(Node<T1> pn, Node<T1> cn) {
		this.label = null;
		this.pnode = pn;
		this.cnode = cn;
		this.weight = null;
	}
	
	/**
	 * @param pn   : Parent node for constructing the edge<br>
	 * @param cn   : Child node for constructing the edge<br>
	 * @param lbl: Edge label used to identify the edge<br>
	 * This method constructs a new<br>
	 * edge named label, connecting parent pn with child cn<br>
	 * the edge weight is set to 0.0<br>
	 **/

	public Edge(Node<T1> pn, Node<T1> cn, T2 lbl) {
		this(pn, cn);
		this.label = lbl;
		checkRep();
	}

	/**
	 * @param pn   : Parent node for constructing the edge<br>
	 * @param cn   : Child node for constructing the edge<br>
	 * @param lbl: Edge label used to identify the edge<br>
	 * @param eweight: Edge value (aka weight)<br>
	 * This method constructs a new<br>
	 * edge named label, connecting parent pn with child cn<br>
	 * the edge weight is set to eweight<br>
	 **/
	
	public Edge(Node<T1> pn, Node<T1> cn, T2 lbl, T3 eweight) {
		this(pn, cn, lbl);
		this.weight = eweight;
		checkRep();
	}

	/**
	 * getLabel: get label of Edge This method gets the lable for 
	 * this edge<br>
	 * 
	 * @return String name
	 **/

	public T2 getLabel() {
		return label;
	}
	
	/**
	 * Set label of Edge change the label of an Edge<br>
	 * 
	 * @param lbl : A string value for updating the edge label.<br>
	 * @effects this.label changed to eLabel<br>
	 **/
	
	public void setLabel(T2 eLabel) {
		this.label = eLabel;
		checkRep();
	}
	
	/**
	 * getPnode: get parent node of Edge.<br>
	 * This method returns the parent node of an edge<br>
	 * 
	 * @return Node pnode
	 **/
	
	public Node<T1> getPnode() {
		return pnode;
	}
	
	/**
	 * Set Parent of Edge change the Parent Node of an Edge<br>
	 * 
	 * @param npNode: Node obj for changing a parent of an edge.<br>
	 * @effects changes the pnode of this edge to pNode<br>
	 **/
	
	public void setPnode(Node<T1> pNode) {
		this.pnode = pNode;
		checkRep();
	}
	
	/**
	 * getCnode: get child node of Edge<br>
	 * This method returns the child node of an edge<br>
	 * 
	 * @return Node cnode
	 **/

	public Node<T1> getCnode() {
		return cnode;
	}
	
	/**
	 * Set Child of Edge change the Child Node of an Edge<br>
	 * 
	 * @param cNode : Node obj set as the child obj for an edge.<br>
	 * @effects changes the cnode of this edge to cNode<br>
	 */

	public void setCnode(Node<T1> cNode) {
		this.cnode = cNode;
		checkRep();
	}

	/**
	 * This method gets the value of the edge, 2-decimal places <br>
	 * 
	 * @return weight
	 **/

	public T3 getWeight() {
		return weight;
	}
	
	/**
	 * Set value of Edge change the value of an Edge<br>
	 * 
	 * @param eWeight : a Double value used to update edge value.<br>
	 * @effects changes the value of this edge to eWeight<br>
	 **/

	public void setWeight(T3 eWeight) {
		this.weight = eWeight;
	}
	
	/**
	 * Standard equality operation.<p>
	 * @param nodeComp The object to be compared for equality.<br>
	 * @return true if and only if 'nodeComp' is an instance of<br>
	 * a Node and 'this' and 'nodeComp' represent the same node.<br>
	 **/
	
	@Override //compares two edges
    public boolean equals(/*@Nullable*/ Object edgeComp) {
    	//only checks label of edge as multiple edges allowed between same nodes
        if (getClass() != edgeComp.getClass()) {
        	return false;
        }
            
        Edge<?,?,?> ec = (Edge<?,?,?>) edgeComp;
        
        if (this.pnode.getName().equals(ec.getPnode().getName()) &&
        		this.cnode.getName().equals(ec.getCnode().getName()) &&
        		this.label.equals(ec.getLabel()) )
        	return true;
        else
        	return false;
    }  
    
    /** 
	 * @return a String representing this, in reduced terms.<br>
	 * Example, Node object with a name "Atlanta" will be<br>
	 * represented as a string 'node: Atlanta'.<br>
	 **/

	@Override	//prints edge information
	public String toString() {
		return ("edge: " + this.label + " " + this.pnode.getName() + " -> " + this.cnode.getName() + " (" + this.weight + ")");
	}
 
	/**
	 *Checks that the representation invariant holds (if any).<br>
	 *Throws a RuntimeException if the rep invariant is violated.<br>
	 **/

    // Throws a RuntimeException if the rep invariant is violated.
    private void checkRep() throws RuntimeException {
    	
    	//check edge label
        if (this.label == null) {
            throw new RuntimeException("Edge label is undefined");
        }
        
        //check parent node
        if (this.pnode.getName().toString().length() == 0) {
            throw new RuntimeException("Parent node name is invalid");
        }

        if (this.pnode.getName() == null) {
            throw new RuntimeException("Parent node name is undefined");
        }
        
        //check child node
        if (this.cnode.getName().toString().length() == 0) {
            throw new RuntimeException("Child node name is invalid");
        }
      
        if (this.cnode.getName() == null) {
            throw new RuntimeException("Child node name is undefined");
        }
        
    }
        
}
