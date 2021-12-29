package models;

import java.util.LinkedList;

public class Block {

	private Node node;
	private Block fatherBlock;
	private double tempWeight;
	private double weight;
	private Block rightBlock;
	private Block leftBlock;
	
	public Block(Block fatherBlock, Node node, double tempWeight, double weight) {
		this.node = node;
		this.tempWeight = tempWeight+weight;
		this.fatherBlock = fatherBlock;
		this.weight = weight;
	}
	
	public Block(Block fatherBlock, Node node, double tempWeight) {
		this.node = node;
		this.tempWeight = (int)(tempWeight+weight);
		this.fatherBlock = fatherBlock;
	}
	
	public Block(Node node, double tempWeight) {
		this.tempWeight = tempWeight;
		this.node = node;
	}
	
	public Block(double tempWeight) {
		this.tempWeight = tempWeight;
	}
	
	public boolean isLeave() {
		return rightBlock == null && leftBlock == null;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public double getTempWeight() {
		return tempWeight;
	}

	public void setTempWeight(double tempWeight) {
		this.tempWeight = tempWeight;
	}
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public LinkedList<Connection> getConnections() {
		return node.getConnections();
	}

	public Block getFatherNode() {
		return fatherBlock;
	}

	public void setFatherNode(Block fatherNode) {
		this.fatherBlock = fatherNode;
	}

	public Block getFatherBlock() {
		return fatherBlock;
	}

	public void setFatherBlock(Block fatherBlock) {
		this.fatherBlock = fatherBlock;
	}

	public Block getRightBlock() {
		return rightBlock;
	}

	public void setRightBlock(Block rightBlock) {
		this.rightBlock = rightBlock;
	}

	public Block getLeftBlock() {
		return leftBlock;
	}

	public void setLeftBlock(Block leftBlock) {
		this.leftBlock = leftBlock;
	}
}
