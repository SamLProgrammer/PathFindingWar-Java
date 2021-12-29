package models;

public class QueueNode {

	private Block block;
	private QueueNode nextNode;

	public QueueNode(Block block) {
		this.block = block;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public QueueNode getNextNode() {
		return nextNode;
	}

	public void setNextNode(QueueNode nextNode) {
		this.nextNode = nextNode;
	}
}
