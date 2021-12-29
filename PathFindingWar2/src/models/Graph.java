package models;

import java.util.LinkedList;
import javax.swing.SwingWorker;
import observer.AnimationEngine;

public class Graph {

	private Node sourceNode;
	private Node goalNode;
	private AnimationEngine animationEngine;
	private SwingWorker<Void, Void> aStarWorker;
	private SwingWorker<Void, Void> dijkstraWorker;
	private Maze maze;
	private int pathLength;

	public Graph(Node sourceNode, Node goalNode, AnimationEngine animationEngine, Maze maze) {
		this.sourceNode = sourceNode;
		this.goalNode = goalNode;
		this.animationEngine = animationEngine;
		this.maze = maze;
	}

	public void initAStarWorker(Node source, Node destiny, MyPriorityQueue pQueue, PathFindingQueue pfl) {
		aStarWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				double initialTime = System.nanoTime();
				belowShortestPathStar(new Block(source, 0), destiny, pQueue, pfl);
				double finalTime = System.nanoTime();
				getTotalPathAStar(destiny, pfl);
				maze.clearAStarMarks();
				animationEngine.updateAStarMazeGUI();
				animationEngine.updateAstarStats(pathLength, (double) ((double) (finalTime - initialTime) / 1000000000));
				animationEngine.enableGUIButtons();
				return null;
			}
		};
		aStarWorker.execute();
	}

	public void initDijkstraWorker(Node source, Node destiny, MyPriorityQueue pQueue, PathFindingQueue pfl) {
		dijkstraWorker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				double initialTime = System.nanoTime();
				belowShortestPathDijkstra(new Block(source, 0), destiny, pQueue, pfl);
				double finalTime = System.nanoTime();
				getTotalPathDijkstra(destiny, pfl);
				maze.clearDijkstraMarks();
				animationEngine.updateDijkstraMazeGUI();
				animationEngine.updateDijkstraStats(pathLength, (double) ((double) (finalTime - initialTime) / 1000000000));
				maze.aStarshortestPath();
				return null;
			}
		};
		dijkstraWorker.execute();
	}

	private double heuristic(Node node, double tie) {
		double hdx = Math.abs(goalNode.getX() - node.getX());
		double hdy = Math.abs(goalNode.getY() - node.getY());
		return (hdx + hdy) * (1 + tie);
	}

	public void shortestPathDijkstra() {
		pathLength = 0;
		MyPriorityQueue pQueue = new MyPriorityQueue();
		PathFindingQueue pfl = new PathFindingQueue();
		initDijkstraWorker(sourceNode, goalNode, pQueue, pfl);
	}

	public void shortestPathStar() {
		pathLength = 0;
		MyPriorityQueue pQueue = new MyPriorityQueue();
		PathFindingQueue pfl = new PathFindingQueue();
		initAStarWorker(sourceNode, goalNode, pQueue, pfl);
	}

	public void getTotalPathDijkstra(Node destiny, PathFindingQueue pfl) {
		Block block = pfl.findBlock(destiny);
		LinkedList<Block> path = new LinkedList<Block>();
		while (block != null) {
			path.push(block);
			block = block.getFatherBlock();
			pathLength++;
		}
		for (int i = 0; i < path.size(); i++) {
			path.get(i).getNode().setPathPart(true);
			animate(10);
		}
	}

	public void getTotalPathAStar(Node destiny, PathFindingQueue pfl) {
		Block block = pfl.findBlock(destiny);
		LinkedList<Block> path = new LinkedList<Block>();
		while (block != null) {
			path.push(block);
			block = block.getFatherBlock();
			pathLength++;
		}
		for (int i = 0; i < path.size(); i++) {
			path.get(i).getNode().setPathPart(true);
			animate(10);
		}
	}

	private void animate(int frequency) {
		try {
			Thread.sleep(frequency);
		} catch (InterruptedException e) {
		}
	}

	public void belowShortestPathDijkstra(Block source, Node destiny, MyPriorityQueue pQueue, PathFindingQueue pfl) {
		boolean founded = false;
		enqueueBlocks(source, pQueue, pfl);
		for (int i = 0; i < source.getConnections().size(); i++) {
			if (pfl.findBlock(source.getConnections().get(i).getNode()) == null) {
				if (!source.getConnections().get(i).getNode().isBlocked()) {
					if (source.getConnections().get(i).getNode() == destiny) {
						pfl.addBlock(new Block(source, source.getConnections().get(i).getNode(), source.getTempWeight() + source.getConnections().get(i).getWeight()));
						founded = true;
						i = source.getConnections().size();
					} else {
						if (pQueue.findNode(source.getConnections().get(i).getNode()).getBlock().getTempWeight() > source.getConnections().get(i).getWeight() + source.getTempWeight()) {
							pQueue.deleteNode(source.getConnections().get(i).getNode());
							pQueue.addNode(new QueueNode(new Block(source, source.getConnections().get(i).getNode(), source.getConnections().get(i).getWeight() + source.getTempWeight())));
						}
					}
				}
			}
		}
		if (!founded && pQueue.getHeadNode() != null) {
			pfl.addBlock(pQueue.getHeadNode().getBlock());
			pQueue.getHeadNode().getBlock().getNode().setMarked(true);
			animate(5);
			belowShortestPathDijkstra(pQueue.poll(), destiny, pQueue, pfl);
		}
	}

	public void belowShortestPathStar(Block source, Node destiny, MyPriorityQueue pQueue, PathFindingQueue pfl) {
		enqueueBlocksStar(source, pQueue, pfl);
		if (pQueue.getHeadNode() != null && pQueue.getHeadNode().getBlock().getNode() == destiny) {
			pfl.addBlock(pQueue.poll());
		} else {
			pQueue.getHeadNode().getBlock().getNode().setMarked(true);
			animate(5);
			pfl.addBlock(pQueue.getHeadNode().getBlock());
			belowShortestPathStar(pQueue.poll(), destiny, pQueue, pfl);
		}
	}
	
	private void enqueueBlocksStar(Block source, MyPriorityQueue pQueue, PathFindingQueue pfl) {
		double tie = 0.001;
		for (int i = 0; i < source.getConnections().size(); i++) {
			if (!source.getConnections().get(i).getNode().isSource() && !source.getConnections().get(i).getNode().isBlocked() && pfl.findBlock(source.getConnections().get(i).getNode()) == null) {
				if (pQueue.findNode(source.getConnections().get(i).getNode()) != null) {
					if (pQueue.findNode(source.getConnections().get(i).getNode()).getBlock().getTempWeight() > heuristic(source.getConnections().get(i).getNode(), tie) + source.getWeight()) {
						pQueue.deleteNode(source.getConnections().get(i).getNode());
						pQueue.addNode(new QueueNode(new Block(source, source.getConnections().get(i).getNode(), heuristic(source.getConnections().get(i).getNode(), tie), source.getWeight() + 1)));
						source.getConnections().get(i).getNode().setEnqueued(true);
					}
				} else {
					pQueue.addNode(new QueueNode(new Block(source, source.getConnections().get(i).getNode(), heuristic(source.getConnections().get(i).getNode(), tie), source.getWeight() + 1)));
					source.getConnections().get(i).getNode().setEnqueued(true);
				}
			}
		}
	}

	private void enqueueBlocks(Block source, MyPriorityQueue pQueue, PathFindingQueue pfl) {
		if (!source.getNode().isBlocked() && pfl.findBlock(source.getNode()) == null) {
			pQueue.deleteNode(source.getNode());
			pQueue.addNode(new QueueNode(new Block(source.getNode(), source.getTempWeight())));
		}
		for (int i = 0; i < source.getConnections().size(); i++) {
			if (!source.getConnections().get(i).getNode().isBlocked() && pfl.findBlock(source.getConnections().get(i).getNode()) == null) {
				pQueue.deleteNode(source.getConnections().get(i).getNode());
				pQueue.addNode(new QueueNode(new Block(source, source.getConnections().get(i).getNode(), 99999)));
				source.getConnections().get(i).getNode().setEnqueued(true);
			}
		}
	}

	public int graphSize(int counter, Node node) {
		int tempCounter = counter;
		node.setMarked(true);
		for (int i = 0; i < node.getConnections().size(); i++) {
			if (!node.getConnections().get(i).getNode().isMarked()) {
				tempCounter = graphSize(counter + 1, node.getConnections().get(i).getNode());
			}
		}
		return tempCounter;
	}

	public void belowShowGraph(Node node) {
		if (!node.isMarked()) {
			System.out.println(node.getX() + ", " + node.getY());
			node.setMarked(true);
			for (int i = 0; i < node.getConnections().size(); i++) {
				belowShowGraph(node.getConnections().get(i).getNode());
			}
		}
	}

	public void showGraph(Node node) {
		belowShowGraph(node);
		clearMarksToGraph(node);
	}

	public void clearMarksToGraph(Node node) {
		if (node.isMarked()) {
			node.setMarked(false);
			for (int i = 0; i < node.getConnections().size(); i++) {
				clearMarksToGraph(node.getConnections().get(i).getNode());
			}
		}
	}

	public void graphFromMatrix(Node[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				Node node = matrix[i][j];
				if (i > 0 && i < matrix.length - 1 && j > 0 && j < matrix[0].length - 1) {
					if (!node.connectedNodeExists(matrix[i - 1][j])) {// superior
						node.addConnection(new Connection(1, matrix[i - 1][j], node));
					}
					if (!node.connectedNodeExists(matrix[i + 1][j])) {// inferior
						node.addConnection(new Connection(1, matrix[i + 1][j], node));
					}
					if (!node.connectedNodeExists(matrix[i][j - 1])) {// izquierdo
						node.addConnection(new Connection(1, matrix[i][j - 1], node));
					}
					if (!node.connectedNodeExists(matrix[i][j + 1])) {// derecho
						node.addConnection(new Connection(1, matrix[i][j + 1], node));
					}
				}
				if (i - 1 < 0) {
					if (j - 1 >= 0 && j < matrix[0].length - 1) {
						if (!node.connectedNodeExists(matrix[i][j + 1])) {// derecho
							node.addConnection(new Connection(1, matrix[i][j + 1], node));
						}
						if (!node.connectedNodeExists(matrix[i][j - 1])) {// izquierdo
							node.addConnection(new Connection(1, matrix[i][j - 1], node));
						}
						if (!node.connectedNodeExists(matrix[i + 1][j])) {// inferior
							node.addConnection(new Connection(1, matrix[i + 1][j], node));
						}
					} else {
						if (j - 1 < 0) {
							if (!node.connectedNodeExists(matrix[i][j + 1])) {// derecho
								node.addConnection(new Connection(1, matrix[i][j + 1], node));
							}
							if (!node.connectedNodeExists(matrix[i + 1][j])) {// inferior
								node.addConnection(new Connection(1, matrix[i + 1][j], node));
							}
						}
						if (j == matrix[0].length - 1) {
							if (!node.connectedNodeExists(matrix[i + 1][j])) {// inferior
								node.addConnection(new Connection(1, matrix[i + 1][j], node));
							}
							if (!node.connectedNodeExists(matrix[i][j - 1])) {// izquierdo
								node.addConnection(new Connection(1, matrix[i][j - 1], node));
							}
						}
					}
				}
				if (i == matrix.length - 1) {
					if (j - 1 >= 0 && j < matrix[0].length - 1) {
						if (!node.connectedNodeExists(matrix[i][j + 1])) {// derecho
							node.addConnection(new Connection(1, matrix[i][j + 1], node));
						}
						if (!node.connectedNodeExists(matrix[i][j - 1])) {// izquierdo
							node.addConnection(new Connection(1, matrix[i][j - 1], node));
						}
						if (!node.connectedNodeExists(matrix[i - 1][j])) {// superior
							node.addConnection(new Connection(1, matrix[i - 1][j], node));
						}
					} else {
						if (j - 1 < 0) {
							if (!node.connectedNodeExists(matrix[i][j + 1])) {// derecho
								node.addConnection(new Connection(1, matrix[i][j + 1], node));
							}
							if (!node.connectedNodeExists(matrix[i - 1][j])) {// superior
								node.addConnection(new Connection(1, matrix[i - 1][j], node));
							}
						}
						if (j == matrix[0].length - 1) {
							if (!node.connectedNodeExists(matrix[i - 1][j])) {// superior
								node.addConnection(new Connection(1, matrix[i - 1][j], node));
							}
							if (!node.connectedNodeExists(matrix[i][j - 1])) {// izquierdo
								node.addConnection(new Connection(1, matrix[i][j - 1], node));
							}
						}
					}
				}
				if (j == 0) {
					if (i > 0 && i < matrix.length - 1) {
						if (!node.connectedNodeExists(matrix[i - 1][j])) {// superior
							node.addConnection(new Connection(1, matrix[i - 1][j], node));
						}
						if (!node.connectedNodeExists(matrix[i + 1][j])) {// inferior
							node.addConnection(new Connection(1, matrix[i + 1][j], node));
						}
						if (!node.connectedNodeExists(matrix[i][j + 1])) {// derecho
							node.addConnection(new Connection(1, matrix[i][j + 1], node));
						}
					} else {
						if (i == 0) {
							if (!node.connectedNodeExists(matrix[i + 1][j])) {// inferior
								node.addConnection(new Connection(1, matrix[i + 1][j], node));
							}
							if (!node.connectedNodeExists(matrix[i][j + 1])) {// derecho
								node.addConnection(new Connection(1, matrix[i][j + 1], node));
							}
						}
						if (i == matrix.length - 1) {
							if (!node.connectedNodeExists(matrix[i - 1][j])) {// superior
								node.addConnection(new Connection(1, matrix[i - 1][j], node));
							}
							if (!node.connectedNodeExists(matrix[i][j + 1])) {// derecho
								node.addConnection(new Connection(1, matrix[i][j + 1], node));
							}
						}
					}
				}
				if (j == matrix[0].length - 1) {
					if (i > 0 && i < matrix.length - 1) {
						if (!node.connectedNodeExists(matrix[i - 1][j])) {// superior
							node.addConnection(new Connection(1, matrix[i - 1][j], node));
						}
						if (!node.connectedNodeExists(matrix[i + 1][j])) {// inferior
							node.addConnection(new Connection(1, matrix[i + 1][j], node));
						}
						if (!node.connectedNodeExists(matrix[i][j - 1])) {// izquierdo
							node.addConnection(new Connection(1, matrix[i][j - 1], node));
						}
					} else {
						if (i == 0) {
							if (!node.connectedNodeExists(matrix[i + 1][j])) {// inferior
								node.addConnection(new Connection(1, matrix[i + 1][j], node));
							}
							if (!node.connectedNodeExists(matrix[i][j - 1])) {// izquierdo
								node.addConnection(new Connection(1, matrix[i][j - 1], node));
							}
						}
						if (i == matrix.length - 1) {
							if (!node.connectedNodeExists(matrix[i - 1][j])) {// superior
								node.addConnection(new Connection(1, matrix[i - 1][j], node));
							}
							if (!node.connectedNodeExists(matrix[i][j - 1])) {// izquierdo
								node.addConnection(new Connection(1, matrix[i][j - 1], node));
							}
						}
					}
				}
			}
		}
	}

	public void clearPath(Node node) {
		if (node.isMarked() || node.isPathPart()) {
			node.setMarked(false);
			node.setPathPart(false);
			for (int i = 0; i < node.getConnections().size(); i++) {
				clearPath(node.getConnections().get(i).getNode());
			}
		}
	}

	public void resetGraph(Node node) {
		if (!node.isMarked() || node.isBlocked()) {
			node.setMarked(true);
			node.setBlocked(false);
			for (int i = 0; i < node.getConnections().size(); i++) {
				resetGraph(node.getConnections().get(i).getNode());
			}
		}
	}

	public Node getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(Node sourceNode) {
		this.sourceNode = sourceNode;
	}

	public Node getGoalNode() {
		return goalNode;
	}

	public void setGoalNode(Node goalNode) {
		this.goalNode = goalNode;
	}

	public void blockAll(Node node) {
		if (!node.isMarked()) {
			node.setMarked(true);
			for (int i = 0; i < node.getConnections().size(); i++) {
				blockAll(node.getConnections().get(i).getNode());
				node.getConnections().get(i).getNode().setBlocked(true);
			}
		}
	}

}
