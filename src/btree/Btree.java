package btree;

import preprocess.*;
import utility.*;

public class Btree {
	public Node root;

	final int ORDER;
	final int MAX_KEY;
	final int HALF_KEY;
	

	public Btree(int order){
		this.ORDER=order;
		this.MAX_KEY=ORDER-1;
		this.HALF_KEY=MAX_KEY/2;
		this.root=new Node(ORDER);
	}
	
	//insert one record into current b tree
	public void insert(MyRecord record){
		Node leafNode=findToInsert(record);
		insert(record, leafNode);
	}
	
	
	//start from root to find the right leaf to insert the record
	public Node findToInsert(MyRecord record){
		return findLeafToInsert(record, root);
	}
	
	//recursively search the right node to insert or one level down
	public Node findLeafToInsert(MyRecord record, Node node){
		if(node.isLeaf()){
			return node;
		}
		else{
			int flag=0;
			for(int i=0;i<node.getKeys().size();i++){
				if(node.getKeys().get(i).getName().compareTo(record.getName())>0){
					node=node.getChildren().get(i);
					flag=1;
					break;
				}
			}
			if(flag==0) {node=node.getChildren().get(node.getKeys().size()-1);}
			return findLeafToInsert(record,node);
		}
	}
	
	//insert into a leaf node, if split happens, might need rebuild a root
	public void insert(MyRecord record, Node leaf){
		if(leaf.getKeys().size()==MAX_KEY){//leaf is full already
			leaf.getKeys().add(record);
			DataHandler.sortMyRecord(leaf.getKeys());
			split(leaf);
		}
		else{
			leaf.getKeys().add(record);
			DataHandler.sortMyRecord(leaf.getKeys());
		}
	}
	
	//split a node bottom up to the root if necessary, keys in the node are sorted
	private void split(Node node){
		Node nodeL=new Node(ORDER);
		Node nodeR=new Node(ORDER);
		MyRecord keyUp=node.getKeys().get(HALF_KEY);
		
		//already full and one more entered to be split
			for(int i=0;i<HALF_KEY;i++){
				nodeL.getKeys().add(node.getKeys().get(i));
			}
			for(int i=HALF_KEY+1;i<ORDER;i++){
				nodeR.getKeys().add(node.getKeys().get(i));
			}
			if(!node.isLeaf()){//if not leaf, have to re allocate children. There should be one more child than ORDER
				for(int i=0;i<=HALF_KEY;i++){
					nodeL.getChildren().add(node.getChildren().get(i));
				}
				for(int i=HALF_KEY+1;i<=ORDER;i++){
					nodeR.getChildren().add(node.getChildren().get(i));
				}
			}
			if(node.getParent()==null){//create new parent, also the new root of the btree
				nodeL.setParent(node);
				nodeR.setParent(node);
				node.getKeys().clear();
				node.getKeys().add(keyUp);
				node.getChildren().clear();
				node.getChildren().add(nodeL);
				node.getChildren().add(nodeR);
				this.root=node;
			}
			else{//add to current parent. Might cause parent having too many (one more) keys and children
				nodeL.setParent(node.getParent());
				nodeR.setParent(node.getParent());
				node.getParent().getKeys().add(keyUp);//keyUp
				DataHandler.sortMyRecord(node.getKeys());//sort current keys (might be one more than KEY_MAX)
				node.getParent().getChildren().set(node.getParent().getKeys().indexOf(keyUp), nodeL);//node's position is the new Left Node's place.
				node.getParent().getChildren().add(node.getParent().getKeys().indexOf(keyUp)+1,nodeR);//add one more position for new right node.
				if(node.getParent().getKeys().size()==(MAX_KEY+1)){//If parent is full, split it.
					split(node.getParent());
				}
			}
			
	}
	
	//find the record with name. Search from root until the leaf. If not exist, return null.
	public MyRecord search(String name){
		Node node=root;
		MyRecord result=null;
		//when node isn't leaf, find the record in the node, or find the right next level node to search
		while(!node.isLeaf()){
			int flag=0;
			for(int i=0;i<node.getKeys().size();i++){
				if(node.getKeys().get(i).getName().compareTo(name)==0){
					result=node.getKeys().get(i);
					return result;
				}
				else if(node.getKeys().get(i).getName().compareTo(name)>0){
					node=node.getChildren().get(i);
					flag=1;
					break;
				}
			}
			if(flag==0){node=node.getChildren().get(node.getChildren().size()-1);}
		}
		//when node is leaf
		for(int i=0;i<node.getKeys().size();i++){
			if(node.getKeys().get(i).getName().compareTo(name)==0){
				result=node.getKeys().get(i);
				return result;
			}
		}
		return result;
	}
	
	
	
}
