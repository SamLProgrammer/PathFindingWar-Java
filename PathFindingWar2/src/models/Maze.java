package models;

import observer.AnimationEngine;

public class Maze {

	private NodesMatrix dijkstraMaze;
	private NodesMatrix aStarMaze;
	private Graph dijkstraGraph;
	private Graph aStarGraph;
	private int size = 31;

	public Maze(AnimationEngine animationEngine) {
		initMazeMatrix(animationEngine);
	}

	private void initMazeMatrix(AnimationEngine animationEngine) {
		dijkstraMaze = new NodesMatrix(size, size, animationEngine);
		aStarMaze = new NodesMatrix(size, size, animationEngine);

		dijkstraGraph = new Graph(dijkstraMaze.getSourceNode(), dijkstraMaze.getGoalNode(), animationEngine, this);
		dijkstraGraph.graphFromMatrix(dijkstraMaze.getMatrix());

		aStarGraph = new Graph(aStarMaze.getSourceNode(), aStarMaze.getGoalNode(), animationEngine, this);
		aStarGraph.graphFromMatrix(aStarMaze.getMatrix());
	}
	
	public void generateSameMazes() {
		dijkstraMaze.initMazeBuilderWorker(true, getAStarMazMatrix());
	}
	
	public void generateSameRatMaze() {
		dijkstraMaze.prepareRatInMazeBuilder(getAStarMazMatrix());
		dijkstraMaze.initRatInMazeBuilderWorker(getAStarMazMatrix(), size);
	}
	
	public void generateHuntAndKillMaze() {
		dijkstraMaze.prepareRatInMazeBuilder(getAStarMazMatrix());
		dijkstraMaze.initHuntAndKillBuilderWorker(getAStarMazMatrix(), size);
	}	
	
	public void generateKruskalMaze() {
		dijkstraMaze.initKruskalBuilderWorker(getAStarMazMatrix());
		dijkstraMaze.cleanKruskal();
	}
	
	public void startPathing() {
		dijkstraGraph.shortestPathDijkstra();
	}

	public void aStarshortestPath() {
		aStarGraph.shortestPathStar();

	}

	public Node[][] getDijkstraMazeMatrix() {
		return dijkstraMaze.getMatrix();
	}

	public Node[][] getAStarMazMatrix() {
		return aStarMaze.getMatrix();
	}

	public void clearMaze() {
		dijkstraGraph.clearPath(dijkstraGraph.getSourceNode());
		aStarGraph.clearPath(aStarGraph.getSourceNode());
	}
	
	public void fullClearMaze() {
		dijkstraMaze.clearPath();
		aStarMaze.clearPath();
		dijkstraMaze.clearMarks();
		aStarMaze.clearMarks();
	}
	
	public void clearDijkstraMarks() {
		dijkstraMaze.clearMarks();
	}
	
	public void clearAStarMarks() {
		aStarMaze.clearMarks();
	}

	public void resetMaze() {
		clearMaze();
		dijkstraGraph.resetGraph(dijkstraMaze.getMatrix()[0][0]);
		aStarGraph.resetGraph(aStarMaze.getMatrix()[0][0]);
		clearMaze();
	}

	public void updateSourceNode(Node dijkstraNode, Node aStarNode) {
		dijkstraMaze.setSourceNode(dijkstraNode);
		aStarMaze.setSourceNode(aStarNode);
		dijkstraGraph.setSourceNode(dijkstraNode);
		aStarGraph.setSourceNode(aStarNode);
	}

	public void updateDestinyNode(Node dijkstraNode, Node aStarNode) {
		dijkstraMaze.setGoalNode(dijkstraNode);
		aStarMaze.setGoalNode(aStarNode);
		dijkstraGraph.setGoalNode(dijkstraNode);
		aStarGraph.setGoalNode(aStarNode);
	}

}
