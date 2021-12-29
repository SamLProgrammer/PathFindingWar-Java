package models;

import java.util.LinkedList;

public class Connection {
	
	private Node node;
	private Node[] nodesPair;
	private double weight;
	
	public Connection(double weight, Node node, Node node1) {
		this.node = node;
		this.weight = weight;
		nodesPair = new Node[2];
		nodesPair[0] = node;
		nodesPair[1] = node1;
	}
	
	public Node getNode() {
		return node;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public LinkedList<Connection> getConnections() {
		return node.getConnections();
	}
}