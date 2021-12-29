package controller;

import models.Maze;
import models.Node;
import observer.AnimationEngine;
import observer.MazeEngine;
import views.MazeFrame;

public class Controller{

	private Maze maze;
	private MazeFrame mazeFrame;
	private MazeEngine mazeEngine;
	private AnimationEngine animationEngine;
	
	public Controller() {
		mazeEngine = new MazeEngine(this);
		animationEngine = new AnimationEngine(this);
		maze = new Maze(animationEngine);
		mazeFrame = new MazeFrame(maze.getDijkstraMazeMatrix(), maze.getAStarMazMatrix(), mazeEngine);
	}

	public void updateMazePanel() {
		 mazeFrame.updateMazeGUI(maze.getDijkstraMazeMatrix(), maze.getAStarMazMatrix(), mazeEngine);
	}
	
	public void clearMaze() {
		maze.clearMaze();
		mazeFrame.updateMazeGUI(maze.getDijkstraMazeMatrix(), maze.getAStarMazMatrix(), mazeEngine);
	}

	public void resetMaze() {
		maze.resetMaze();
		mazeFrame.updateMazeGUI(maze.getDijkstraMazeMatrix(), maze.getAStarMazMatrix(), mazeEngine);
	}

	public void updateDijkstraMazePanel() {
		mazeFrame.updateDijkstraMazeGUI(maze.getDijkstraMazeMatrix(), maze.getAStarMazMatrix(), mazeEngine);
	}

	public void updateAStarMazePanel() {
		mazeFrame.updateAStarMazeGUI(maze.getDijkstraMazeMatrix(), maze.getAStarMazMatrix(), mazeEngine);
	}

	public void updateAStarMazeStats(int pathLength, double executionTime) {
		mazeFrame.updateAStarMazeStats(pathLength, executionTime);
	}

	public void updateDijkstraMazeStats(int pathLength, double executionTime) {
		mazeFrame.updateDijkstraMazeStats(pathLength, executionTime);
	}

	public void initPathing() {
		maze.startPathing();
	}

	public void enableGUIButtons() {
		mazeFrame.enableButtons();
	}

	public void updateSourceNode(Node dijkstraNode, Node aStarNode) {
		maze.updateSourceNode(dijkstraNode, aStarNode);
		mazeFrame.updateAStarMazeGUI(maze.getDijkstraMazeMatrix(), maze.getAStarMazMatrix(), mazeEngine);
	}

	public void updateDestinyNode(Node dijkstraNode, Node aStarNode) {
		maze.updateDestinyNode(dijkstraNode, aStarNode);
		mazeFrame.updateAStarMazeGUI(maze.getDijkstraMazeMatrix(), maze.getAStarMazMatrix(), mazeEngine);
	}

	public void generateWarField() {
		maze.generateSameMazes();
		updateMazePanel();
	}

	public void generateRatMaze() {
		maze.generateSameRatMaze();
	}

	public void generateHuntAndKillMaze() {
		maze.generateHuntAndKillMaze();
	}

	public void generateKruskalMaze() {
		maze.generateKruskalMaze();
	}
}
