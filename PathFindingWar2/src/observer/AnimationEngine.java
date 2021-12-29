package observer;

import controller.Controller;

public class AnimationEngine {
	
	private Controller controller;
	
	public AnimationEngine(Controller controller) {
		this.controller = controller;
	}
	
	public void updateDijkstraMazeGUI() {
		controller.updateDijkstraMazePanel();
	}
	
	public void updateAStarMazeGUI() {
		controller.updateAStarMazePanel();
	}

	public void updateAstarStats(int pathLength, double executionTime) {
		controller.updateAStarMazeStats(pathLength, executionTime);
	}

	public void updateDijkstraStats(int pathLength, double executionTime) {
		controller.updateDijkstraMazeStats(pathLength, executionTime);
	}

	public void enableGUIButtons() {
		controller.enableGUIButtons();
	}

}
