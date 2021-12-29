package observer;

import controller.Controller;
import models.Node;

public class MazeEngine {
	
	private Controller controller;
	
	public MazeEngine(Controller controller) {
		this.controller = controller;
		}
	
	public void updateMazePanel() {
		controller.updateMazePanel();
	}

	public void clearMaze() {
		controller.clearMaze();
	}

	public void resetMaze() {
		controller.resetMaze();
	}

	public void initPathing() {
		controller.initPathing();
	}

	public void updateSourceNode(Node dijkstraNode, Node aStarNode) {
		controller.updateSourceNode(dijkstraNode, aStarNode);
	}

	public void updateDestinyNode(Node dijkstraNode, Node aStarNode) {
		controller.updateDestinyNode(dijkstraNode, aStarNode);
	}

	public void generateWarField() {
		controller.generateWarField();
	}

	public void generateRatMaze() {
		controller.generateRatMaze();
	}

	public void generateHuntAndKillMaze() {
		controller.generateHuntAndKillMaze();
	}

	public void generateKruskalMaze() {
		controller.generateKruskalMaze();
	}
}
