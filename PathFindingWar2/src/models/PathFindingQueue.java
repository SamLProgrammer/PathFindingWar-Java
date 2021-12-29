package models;

import java.util.LinkedList;

public class PathFindingQueue {

	private LinkedList<Block> blocksQueue;

	public PathFindingQueue() {
		blocksQueue = new LinkedList<Block>();
	}
	
	public int size() {
		return blocksQueue.size();
	}
	
	public void addBlock(Block block) {
		blocksQueue.add(block);
	}

	public LinkedList<Block> getBlocksQueue() {
		return blocksQueue;
	}

	public Block findBlock(Node node) {
		Block block = null;
		for (Block block2 : blocksQueue) {
			if (block2.getNode() == node) {
				block = block2;
			}
		}
		return block;
	}

	public void show() {
		for (Block block : blocksQueue) {
			System.out.println(block.getNode().getX() + ", " + block.getNode().getY());
		}
	}
}
