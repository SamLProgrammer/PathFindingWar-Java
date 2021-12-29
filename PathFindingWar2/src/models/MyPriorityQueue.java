package models;

public class MyPriorityQueue {

	QueueNode headNode;

	public void addNode(QueueNode node) {
		if (headNode == null) {
			headNode = node;
		} else {
			QueueNode auxNode = headNode;
			if (headNode.getBlock().getTempWeight() > node.getBlock().getTempWeight()) {
				node.setNextNode(auxNode);
				headNode = node;
			} else {
				while (auxNode.getNextNode() != null && auxNode.getNextNode().getBlock().getTempWeight() <= node.getBlock().getTempWeight()) {
					auxNode = auxNode.getNextNode();
				}
				node.setNextNode(auxNode.getNextNode());
				auxNode.setNextNode(node);
			}
		}
	}

	public void deleteNode(Node node) {
		if (findNode(node) != null) {
			if (headNode.getBlock().getNode() == node) {
				headNode = headNode.getNextNode();
			} else {
				QueueNode auxNode = headNode;
				while (auxNode.getNextNode().getBlock().getNode() != node) {
					auxNode = auxNode.getNextNode();
				}
				auxNode.setNextNode(auxNode.getNextNode().getNextNode());
			}
		}
	}

	public QueueNode findNode(Node node) {
		QueueNode auxNode = null;
		if (size() > 0) {
			auxNode = belowFindNode(node);
		}
		return auxNode;
	}

	private QueueNode belowFindNode(Node node) {
		QueueNode auxNode = headNode;
		while (auxNode != null && auxNode.getBlock().getNode() != node) {
			auxNode = auxNode.getNextNode();
		}
		return auxNode;
	}

	public int size() {
		int size = 0;
		QueueNode auxNode = headNode;
		while (auxNode != null) {
			size++;
			auxNode = auxNode.getNextNode();
		}
		return size;
	}

	public void showQueue() {
		QueueNode auxNode = headNode;
		while (auxNode != null) {
			System.out.print(auxNode.getBlock().getTempWeight() + "[" + auxNode.getBlock().getNode().getX() +"][" + auxNode.getBlock().getNode().getY() + "], ");
			auxNode = auxNode.getNextNode();
		}
		System.out.println();
	}

	public QueueNode getHeadNode() {
		return headNode;
	}

	public void setHeadNode(QueueNode headNode) {
		this.headNode = headNode;
	}

	public Block poll() {
		Block block = headNode.getBlock();
		deleteNode(headNode.getBlock().getNode());
		return block;
	}
}
