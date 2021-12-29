package views;

import java.awt.GridLayout;
import javax.swing.JPanel;
import models.Node;
import observer.GridEngine;
import observer.MazeEngine;

public class MazePanel extends JPanel{
	
	private boolean mazeType;
	private GridEngine gridEngine;

	public MazePanel(boolean mazeType, Node[][] matrix, Node[][] aStarMatrix, MazeEngine mazeEngine) {
		this.mazeType = mazeType;
		gridEngine = new GridEngine();
		setOpaque(false);
		initComponents(matrix, aStarMatrix, mazeEngine);
	}

	private void initComponents(Node[][] dijkstraMatrix, Node[][] aStarMatrix, MazeEngine mazeEngine) {
		Node[][] auxMatrix = dijkstraMatrix;
		if(mazeType) {
			auxMatrix = aStarMatrix;
		}
		setLayout(new GridLayout(auxMatrix.length, auxMatrix[0].length));
		for (int i = 0; i < auxMatrix.length; i++) {
			for (int j = 0; j < auxMatrix[0].length; j++) {
				add(new MazeButton(dijkstraMatrix[i][j], aStarMatrix[i][j], mazeType, mazeEngine, gridEngine));
			}
		}
	}

	public void updateMazeGUI(Node[][] dijkstraMatrix, Node[][] aStarMatrix, MazeEngine mazeEngine) {
		removeAll();
		Node[][] auxMatrix = dijkstraMatrix;
		if(mazeType) {
			auxMatrix = aStarMatrix;
		}
		for (int i = 0; i < auxMatrix.length; i++) {
			for (int j = 0; j < auxMatrix[0].length; j++) {
				add(new MazeButton(dijkstraMatrix[i][j], aStarMatrix[i][j], mazeType, mazeEngine, gridEngine));
			}
		}
		updateUI();
	}
}
