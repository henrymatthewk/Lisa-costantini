package btree;

import preprocess.*;
import java.util.ArrayList;

public class Node {
	private int ORDER;
	private int MAX_CHILDREN;
	private int MAX_KEY;
	private Node parent;
	private ArrayList<Node> children;
	private ArrayList<MyRecord> key;
	
	public Node(int order){
		this.ORDER=order;
		this.MAX_CHILDREN=order;
		this.MAX_KEY=order-1;
		this.parent=null;
		this.children=new ArrayList<Node>(MAX_CHILDREN+1);//one more position so that the split could be done after insertion.
		this.key=new ArrayList<MyRecord>(MAX_KEY+1);//one more position so that the split could be done after insertion.
	}
	
	public void setParent(Node parent){
		this.parent=parent;
	}
	
	public Node getParent(){
		return this.parent;
	}
	
	public boolean isLeaf(){
		return this.children.isEmpty();
	}
	
	public void setChildren(ArrayList<Node> children){
		this.children=children;
	}
	
	public ArrayList<Node> getChildren(){
		return this.children;
	}
	
	public ArrayList<MyRecord> getKeys(){
		return this.key;
	}
	
	public boolean isRoot(){
		return this.getParent()==null;
	}
}
