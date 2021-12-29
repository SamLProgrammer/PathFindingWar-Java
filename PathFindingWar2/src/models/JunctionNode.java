package models;

import java.util.LinkedList;

public class JunctionNode {

	Node node;
	Node rootNode;
	LinkedList<JunctionNode> childs;
	
	public JunctionNode(Node node) {
		this.node = node;
		rootNode = node;
	}
	
	public void addNode(JunctionNode node) {
		childs.add(node);
	}
	
	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}
	
	public Node getRootNode() {
		return rootNode;
	}
}
