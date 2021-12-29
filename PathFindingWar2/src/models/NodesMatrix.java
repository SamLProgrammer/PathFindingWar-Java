package models;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.SwingWorker;
import observer.AnimationEngine;

public class NodesMatrix {

	private Node[][] matrix;
	private Node sourceNode;
	private Node goalNode;
	private AnimationEngine animationEngine;
	private SwingWorker<Void, Void> mazeBuilderWorker;
	private SwingWorker<Void, Void> ratInMazeBuilderWorker;
	private SwingWorker<Void, Void> huntAndKillBuilderWorker;
	private SwingWorker<Void, Void> kruskalBuilderWorker;
	private Stack<Node> ratNodesStack = new Stack<Node>();

	public NodesMatrix(int width, int height, AnimationEngine animationEngine) {
		this.animationEngine = animationEngine;
		matrix = new Node[width][height];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = new Node(i, j);
			}
		}
		setSourceNode(matrix[0][1]);
		setGoalNode(matrix[matrix.length - 1][matrix.length - 2]);
	}

	private Node ratHunting(Node[][] matrix2) {
		Node auxNode = null;
		Node node = null;
		boolean flag = false;
		for (int i = 1; i < matrix.length - 1 && !flag; i++) {
			for (int j = 1; j < matrix[0].length - 1 && !flag; j++) {
				if (!matrix[i][j].isVisited()) {
					node = neighboursVisited(ratInMazeNodeNeighbours(matrix[i][j]));
					if (node != null) {
						auxNode = matrix[i][j];
						flag = true;
					}
				}
			}
		}
		if (auxNode != null && node != null) {
			breakWall(node, auxNode, matrix2);
		}
		return auxNode;
	}

	private Node neighboursVisited(LinkedList<Node> neighboursList) {
		Node node = null;
		for (int i = 0; i < neighboursList.size(); i++) {
			if (neighboursList.get(i).isVisited()) {
				node = neighboursList.get(i);
			}
		}
		return node;
	}

	public void prepareRatInMazeBuilder(Node[][] matrix2) {
		animationEngine.updateAStarMazeGUI();
		for (int i = 0; i < matrix[0].length; i++) {
			if (validToBlock(matrix[0][i])) {
				matrix[0][i].setBlocked(true);
				matrix[0][i].setWall(true);
				matrix2[0][i].setBlocked(true);
				matrix2[0][i].setWall(true);
			}
			if (validToBlock(matrix[matrix.length - 1][i])) {
				matrix[matrix.length - 1][i].setBlocked(true);
				matrix[matrix.length - 1][i].setWall(true);
				matrix2[matrix.length - 1][i].setBlocked(true);
				matrix2[matrix.length - 1][i].setWall(true);
			}
		}
		for (int i = 0; i < matrix.length; i++) {
			if (validToBlock(matrix[i][matrix[0].length - 1])) {
				matrix[i][matrix[0].length - 1].setBlocked(true);
				matrix[i][matrix[0].length - 1].setWall(true);
				matrix2[i][matrix[0].length - 1].setBlocked(true);
				matrix2[i][matrix[0].length - 1].setWall(true);
			}
			if (validToBlock(matrix[i][0])) {
				matrix[i][0].setBlocked(true);
				matrix[i][0].setWall(true);
				matrix2[i][0].setBlocked(true);
				matrix2[i][0].setWall(true);
			}
		}
		initialSubgrid(matrix2);
	}

	private void initialSubgrid(Node[][] matrix2) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (validToBlock(matrix[i][j])) {
					matrix[i][j].setBlocked(true);
					matrix2[i][j].setBlocked(true);
					if ((i % 2 == 0) || (j % 2 == 0)) {
						matrix[i][j].setWall(true);
						matrix2[i][j].setWall(true);
						matrix[i][j].setVisited(true);
						matrix2[i][j].setVisited(true);
					}
				}
			}
		}
	}

	public void letKruskalBegin(Node[][] matrix2) {
		initialSubgrid(matrix2);
		belowLetKruskalBegin(matrix2, kruskalGetJunctionWalls());
	}

	public LinkedList<Node> kruskalGetJunctionWalls() {
		LinkedList<Node> nodesList = new LinkedList<Node>();
		int x = 2;
		int y = 1;
		while (y < matrix.length - 1) {
			while (x < matrix.length - 1) {
				nodesList.add(matrix[x][y]);
				x = x + 2;
			}
			y++;
			if (y % 2 == 0) {
				x = 1;
			} else {
				x = 2;
			}
		}
		return nodesList;
	}

	public void belowLetKruskalBegin(Node[][] matrix2, LinkedList<Node> edgesList) {
		while (edgesList.size() > 0) {
			int random = (int) (Math.random() * edgesList.size());
			getPairFromEdge(edgesList.get(random), matrix2);
			edgesList.remove(edgesList.get(random));
		}
	}

	private void getPairFromEdge(Node node, Node[][] matrix2) {
		int random = 0;
		do { 
			random = (int) (node.getConnections().size() * Math.random());
		}while(node.getConnections().get(random).getNode().isWall());
		for (Connection connection : node.getConnections()) {
			if (((node.getConnections().get(random).getNode().getX() == connection.getNode().getX()) && (node.getConnections().get(random).getNode().getY() != connection.getNode().getY()))
				|| ((node.getConnections().get(random).getNode().getY() == connection.getNode().getY()) && (node.getConnections().get(random).getNode().getX() != connection.getNode().getX()))) {
				if(node.getConnections().get(random).getNode().getKruskalFather() != connection.getNode().getKruskalFather() && !connection.getNode().isVisited()) {
					breakWall(node.getConnections().get(random).getNode(), connection.getNode(), matrix2);
					animate(10);
					if(node.getConnections().get(random).getNode().getKruskalChilds().size() == 0) {
						connection.getNode().addKruskalChild(node.getConnections().get(random).getNode().getKruskalFather());
					} else if(connection.getNode().getKruskalChilds().size() == 0){
						node.getConnections().get(random).getNode().addKruskalChild(connection.getNode().getKruskalFather());
					} else {
						node.getConnections().get(random).getNode().addKruskalChild(connection.getNode().getKruskalFather());
					}
					node.getConnections().get(random).getNode().getMazeButton().setBackground(Color.white);
					matrix2[node.getConnections().get(random).getNode().getX()][node.getConnections().get(random).getNode().getY()].getMazeButton().setBackground(Color.white);
					connection.getNode().getMazeButton().setBackground(Color.white);
					matrix2[connection.getNode().getX()][connection.getNode().getY()].getMazeButton().setBackground(Color.white);
					node.getConnections().get(random).getNode().setBlocked(false);
					matrix2[node.getConnections().get(random).getNode().getX()][node.getConnections().get(random).getNode().getY()].setBlocked(false);
					connection.getNode().setBlocked(false);
					matrix2[connection.getNode().getX()][connection.getNode().getY()].setBlocked(false);
				}
			}
		}
	}
	
	public void cleanKruskal() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j].resetKruskal();
			}
		}
	}

	private void breakWall(Node firstNode, Node secondNode, Node[][] matrix2) {
		if (firstNode.getX() == secondNode.getX()) {
			if (firstNode.getY() < secondNode.getY() && validToBlock(matrix[firstNode.getX()][firstNode.getY() + 1])) {
				matrix[firstNode.getX()][firstNode.getY() + 1].setBlocked(false);
				matrix[firstNode.getX()][firstNode.getY() + 1].setWall(false);
				matrix[firstNode.getX()][firstNode.getY() + 1].getMazeButton().setBackground(Color.white);
				matrix2[firstNode.getX()][firstNode.getY() + 1].setBlocked(false);
				matrix2[firstNode.getX()][firstNode.getY() + 1].setWall(false);
				matrix2[firstNode.getX()][firstNode.getY() + 1].getMazeButton().setBackground(Color.white);
			} else {
				matrix[firstNode.getX()][firstNode.getY() - 1].setBlocked(false);
				matrix[firstNode.getX()][firstNode.getY() - 1].setWall(false);
				matrix[firstNode.getX()][firstNode.getY() - 1].getMazeButton().setBackground(Color.white);
				matrix2[firstNode.getX()][firstNode.getY() - 1].setBlocked(false);
				matrix2[firstNode.getX()][firstNode.getY() - 1].setWall(false);
				matrix2[firstNode.getX()][firstNode.getY() - 1].getMazeButton().setBackground(Color.white);
			}
		} else {
			if (firstNode.getX() < secondNode.getX() && validToBlock(matrix[firstNode.getX() + 1][firstNode.getY()])) {
				matrix[firstNode.getX() + 1][firstNode.getY()].setBlocked(false);
				matrix[firstNode.getX() + 1][firstNode.getY()].setWall(false);
				matrix[firstNode.getX() + 1][firstNode.getY()].getMazeButton().setBackground(Color.white);
				matrix2[firstNode.getX() + 1][firstNode.getY()].setBlocked(false);
				matrix2[firstNode.getX() + 1][firstNode.getY()].setWall(false);
				matrix2[firstNode.getX() + 1][firstNode.getY()].getMazeButton().setBackground(Color.white);
			} else {
				matrix[firstNode.getX() - 1][firstNode.getY()].setBlocked(false);
				matrix[firstNode.getX() - 1][firstNode.getY()].setWall(false);
				matrix[firstNode.getX() - 1][firstNode.getY()].getMazeButton().setBackground(Color.white);
				matrix2[firstNode.getX() - 1][firstNode.getY()].setBlocked(false);
				matrix2[firstNode.getX() - 1][firstNode.getY()].setWall(false);
				matrix2[firstNode.getX() - 1][firstNode.getY()].getMazeButton().setBackground(Color.white);
			}
		}
	}
	
//	private Node breakWallXd(Node firstNode, Node secondNode, Node[][] matrix2) {
//		if(secondNode != null) {
//		if (firstNode.getX() == secondNode.getX()) {
//			if (firstNode.getY() < secondNode.getY() && validToBlock(matrix[firstNode.getX()][firstNode.getY() + 1])) {
//				matrix[firstNode.getX()][firstNode.getY() + 1].setBlocked(false);
//				matrix[firstNode.getX()][firstNode.getY() + 1].setWall(false);
//				matrix[firstNode.getX()][firstNode.getY() + 1].getMazeButton().setBackground(Color.white);
//				matrix2[firstNode.getX()][firstNode.getY() + 1].setBlocked(false);
//				matrix2[firstNode.getX()][firstNode.getY() + 1].setWall(false);
//				matrix2[firstNode.getX()][firstNode.getY() + 1].getMazeButton().setBackground(Color.white);
//			} else {
//				matrix[firstNode.getX()][firstNode.getY() - 1].setBlocked(false);
//				matrix[firstNode.getX()][firstNode.getY() - 1].setWall(false);
//				matrix[firstNode.getX()][firstNode.getY() - 1].getMazeButton().setBackground(Color.white);
//				matrix2[firstNode.getX()][firstNode.getY() - 1].setBlocked(false);
//				matrix2[firstNode.getX()][firstNode.getY() - 1].setWall(false);
//				matrix2[firstNode.getX()][firstNode.getY() - 1].getMazeButton().setBackground(Color.white);
//			}
//			firstNode.getMazeButton().setBackground(Color.white);
//			secondNode.getMazeButton().setBackground(Color.white);
//		} else {
//			if (firstNode.getX() < secondNode.getX() && validToBlock(matrix[firstNode.getX() + 1][firstNode.getY()])) {
//				matrix[firstNode.getX() + 1][firstNode.getY()].setBlocked(false);
//				matrix[firstNode.getX() + 1][firstNode.getY()].setWall(false);
//				matrix[firstNode.getX() + 1][firstNode.getY()].getMazeButton().setBackground(Color.white);
//				matrix2[firstNode.getX() + 1][firstNode.getY()].setBlocked(false);
//				matrix2[firstNode.getX() + 1][firstNode.getY()].setWall(false);
//				matrix2[firstNode.getX() + 1][firstNode.getY()].getMazeButton().setBackground(Color.white);
//			} else {
//				matrix[firstNode.getX() - 1][firstNode.getY()].setBlocked(false);
//				matrix[firstNode.getX() - 1][firstNode.getY()].setWall(false);
//				matrix[firstNode.getX() - 1][firstNode.getY()].getMazeButton().setBackground(Color.white);
//				matrix2[firstNode.getX() - 1][firstNode.getY()].setBlocked(false);
//				matrix2[firstNode.getX() - 1][firstNode.getY()].setWall(false);
//				matrix2[firstNode.getX() - 1][firstNode.getY()].getMazeButton().setBackground(Color.white);
//			}
//			firstNode.getMazeButton().setBackground(Color.white);
//			secondNode.getMazeButton().setBackground(Color.white);
//		}
//		} else {
//			System.out.println("uha");
//		}
//		return secondNode;
//	}

	public void ratInMazeGenerator(Node[][] matrix2, int size) {
		int random = (int) (Math.random() * size);
		if (random % 2 == 0) {
			random += 1;
		}
		ratNodesStack.push(matrix[random][random]);
		belowRatInMazeGenerator(matrix[random][random], matrix2);
//		belowRatInMazeGenerator(matrix[random][random], matrix[random][random], matrix2);
	}

	public void huntAndKillGenerator(Node[][] matrix2, int size) {
		int random = (int) (Math.random() * size);
		if (random % 2 == 0) {
			random += 1;
		}
		belowHuntAndKillMazeGenerator(matrix[1][random], matrix2);
	}

	private void belowRatInMazeGenerator(Node node, Node[][] matrix2) {
		node.setVisited(true);
		node.setBlocked(false);
		matrix2[node.getX()][node.getY()].setBlocked(false);
		if (validToBlock(node)) {
			node.getMazeButton().setBackground(Color.white);
			matrix2[node.getX()][node.getY()].getMazeButton().setBackground(Color.white);
		}
		if (ratNodesStack.size() > 0) {
			Node auxNode = choseRandomNeighbour(ratInMazeNodeNeighbours(node));
			if (auxNode != null) {
				ratNodesStack.push(auxNode);
				breakWall(node, auxNode, matrix2);
				animate(10);
			} else {
				do {
					auxNode = ratNodesStack.pop();
				} while (choseRandomNeighbour(ratInMazeNodeNeighbours(auxNode)) == null && ratNodesStack.size() > 0);
			}
			belowRatInMazeGenerator(auxNode, matrix2);
		}
	}
	
//	private Node belowRatInMazeGenerator(Node node, Node lastViableNode, Node[][] matrix2) {
//		node.setVisited(true);
//		Node auxNode = breakWallXd(node, choseRandomNeighbour(ratInMazeNodeNeighbours(node)), matrix2);
//		if(auxNode != null && (auxNode = belowRatInMazeGenerator(auxNode, node, matrix2)) != null) {
//			
//		} else if(auxNode == null){
//			auxNode = belowRatInMazeGenerator(lastViableNode, lastViableNode, matrix2);
//		}
//		return auxNode;
//	}

	private void belowHuntAndKillMazeGenerator(Node node, Node[][] matrix2) {
		while (node != null) {
			node.setVisited(true);
			node.setBlocked(false);
			matrix2[node.getX()][node.getY()].setBlocked(false);
			if (validToBlock(node)) {
				node.getMazeButton().setBackground(Color.white);
				matrix2[node.getX()][node.getY()].getMazeButton().setBackground(Color.white);
			}
			Node auxNode = choseRandomNeighbour(ratInMazeNodeNeighbours(node));
			if (auxNode != null) {
				breakWall(node, auxNode, matrix2);
				node = auxNode;
			} else {
				node = ratHunting(matrix2);
			}
			animate(10);
		}
	}

	private Node choseRandomNeighbour(LinkedList<Node> nodesList) {
		Node auxNode = null;
		boolean flag = false;
		for (int i = 0; i < nodesList.size(); i++) {
			if (!nodesList.get(i).isVisited()) {
				flag = true;
			}
		}
		if (flag) {
			do {
				auxNode = nodesList.get((int) (Math.random() * nodesList.size()));
			} while (auxNode.isVisited());
		}
		return auxNode;
	}

	private LinkedList<Node> ratInMazeNodeNeighbours(Node node) {
		LinkedList<Node> neighboursCells = new LinkedList<Node>();
		if (node.getX() > 2 && node.getX() < matrix.length - 3 && node.getY() > 2 && node.getY() < matrix[0].length - 3) {
			neighboursCells.add(matrix[node.getX()][node.getY() + 2]);
			neighboursCells.add(matrix[node.getX()][node.getY() - 2]);
			neighboursCells.add(matrix[node.getX() + 2][node.getY()]);
			neighboursCells.add(matrix[node.getX() - 2][node.getY()]);
		} else {
			if (node.getX() == 1 && node.getY() > 2 && node.getY() < matrix[0].length - 2) {
				neighboursCells.add(matrix[node.getX()][node.getY() + 2]);
				neighboursCells.add(matrix[node.getX()][node.getY() - 2]);
				neighboursCells.add(matrix[node.getX() + 2][node.getY()]);
			}
			if (node.getX() == matrix.length - 2 && node.getY() > 2 && node.getY() < matrix[0].length - 2) {
				neighboursCells.add(matrix[node.getX()][node.getY() + 2]);
				neighboursCells.add(matrix[node.getX()][node.getY() - 2]);
				neighboursCells.add(matrix[node.getX() - 2][node.getY()]);
			}
			if (node.getX() > 2 && node.getX() < matrix.length - 3 && node.getY() == 1) {
				neighboursCells.add(matrix[node.getX() + 2][node.getY()]);
				neighboursCells.add(matrix[node.getX() - 2][node.getY()]);
				neighboursCells.add(matrix[node.getX()][node.getY() + 2]);
			}
			if (node.getX() > 2 && node.getX() < matrix.length - 3 && node.getY() == matrix[0].length - 2) {
				neighboursCells.add(matrix[node.getX() + 2][node.getY()]);
				neighboursCells.add(matrix[node.getX() - 2][node.getY()]);
				neighboursCells.add(matrix[node.getX()][node.getY() - 2]);
			}
			if (node.getX() == 1 && node.getY() == 1) {
				neighboursCells.add(matrix[node.getX()][node.getY() + 2]);
				neighboursCells.add(matrix[node.getX() + 2][node.getY()]);
			}
			if (node.getX() == 1 && node.getY() == matrix[0].length - 2) {
				neighboursCells.add(matrix[node.getX() + 2][node.getY()]);
				neighboursCells.add(matrix[node.getX()][node.getY() - 2]);
			}
			if (node.getX() == matrix.length - 2 && node.getY() == 1) {
				neighboursCells.add(matrix[node.getX()][node.getY() + 2]);
				neighboursCells.add(matrix[node.getX() - 2][node.getY()]);
			}
			if (node.getX() == matrix.length - 2 && node.getY() == matrix[0].length - 2) {
				neighboursCells.add(matrix[node.getX()][node.getY() - 2]);
				neighboursCells.add(matrix[node.getX() - 2][node.getY()]);
			}
		}
		return neighboursCells;
	}

	public void initMazeBuilderWorker(boolean flag, Node[][] matrix2) {
		mazeBuilderWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				generateRandomMaze(flag, matrix2);
				animationEngine.enableGUIButtons();
				return null;
			}
		};
		mazeBuilderWorker.execute();
	}

	public void initRatInMazeBuilderWorker(Node[][] matrix2, int size) {
		ratInMazeBuilderWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				ratInMazeGenerator(matrix2, size);
				clearVisiteds();
				animationEngine.updateDijkstraMazeGUI();
				animationEngine.enableGUIButtons();
				return null;
			}
		};
		ratInMazeBuilderWorker.execute();
	}
	
	public void initKruskalBuilderWorker(Node[][] matrix2) {
		kruskalBuilderWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				letKruskalBegin(matrix2);
				clearVisiteds();
				animationEngine.updateDijkstraMazeGUI();
				animationEngine.enableGUIButtons();
				return null;
			}
		};
		kruskalBuilderWorker.execute();
	}

	public void initHuntAndKillBuilderWorker(Node[][] matrix2, int size) {
		huntAndKillBuilderWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				huntAndKillGenerator(matrix2, size);
				clearVisiteds();
				animationEngine.updateDijkstraMazeGUI();
				animationEngine.enableGUIButtons();
				return null;
			}
		};
		huntAndKillBuilderWorker.execute();
	}

	private void clearVisiteds() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j].setVisited(false);
			}
		}
	}

	private void generateRandomMaze(boolean flag, Node[][] matrix2) {
		for (int i = 0; i < matrix[0].length; i++) {
			if (validToBlock(matrix[0][i])) {
				matrix[0][i].setBlocked(true);
				matrix2[0][i].setBlocked(true);
			}
			animate(5);
			if (validToBlock(matrix[matrix.length - 1][i])) {
				matrix[matrix.length - 1][i].setBlocked(true);
				matrix2[matrix.length - 1][i].setBlocked(true);
			}
			animate(5);
		}
		for (int i = 0; i < matrix.length; i++) {
			if (validToBlock(matrix[i][matrix[0].length - 1])) {
				matrix[i][matrix[0].length - 1].setBlocked(true);
				matrix2[i][matrix[0].length - 1].setBlocked(true);
			}
			animate(5);
			if (validToBlock(matrix[i][0])) {
				matrix[i][0].setBlocked(true);
				matrix2[i][0].setBlocked(true);
			}
			animate(5);
		}
		belowGenerateRandomMaze2(0, 0, matrix.length - 1, matrix[0].length - 1, matrix2);
	}

	private boolean validToBlock(Node node) {
		return !node.isSource() && !node.isDestiny();
	}

	private void animate(int frequency) {
		try {
			Thread.sleep(frequency);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void belowGenerateRandomMaze2(int firstRow, int firstColumn, int secondRow, int secondColumn, Node[][] matrix2) {
		if (validInputs(firstRow, firstColumn, secondRow, secondColumn)) {
			if (secondRow - firstRow > secondColumn - firstColumn) {
				if (secondRow - firstRow > 3) {
					int randomPartition = amongRandomNumberGenerator(firstRow + 1, secondRow - 1);
					int randomGap = amongRandomNumberGenerator(firstColumn, secondColumn);
					int cursor = firstColumn;
					if (!matrix[randomPartition][cursor].isBlocked()) {
						cursor += 2;
					}
					while (cursor < secondColumn) {
						if (cursor != randomGap && validWall(matrix[randomPartition][cursor])) {
							matrix[randomPartition][cursor].setBlocked(true);
							matrix2[randomPartition][cursor].setBlocked(true);
							animate(5);
						}
						cursor++;
					}
					if (!matrix[randomPartition][secondColumn].isBlocked()) {
						matrix[randomPartition][cursor - 1].setBlocked(false);
						matrix[randomPartition][cursor - 1].getMazeButton().setBackground(Color.white);
						matrix2[randomPartition][cursor - 1].setBlocked(false);
						matrix2[randomPartition][cursor - 1].getMazeButton().setBackground(Color.white);
						animate(5);
					}
					belowGenerateRandomMaze2(firstRow, firstColumn, randomPartition, secondColumn, matrix2);
					belowGenerateRandomMaze2(randomPartition, firstColumn, secondRow, secondColumn, matrix2);
				}
			} else {
				if (secondColumn - firstColumn > 3) {
					int randomPartition = amongRandomNumberGenerator(firstColumn + 1, secondColumn - 1);
					int randomGap = amongRandomNumberGenerator(firstRow, secondRow);
					int cursor = firstRow;
					if (!matrix[cursor][randomPartition].isBlocked()) {
						cursor += 2;
					}
					while (cursor < secondRow) {
						if (cursor != randomGap && validWall(matrix[cursor][randomPartition])) {
							matrix[cursor][randomPartition].setBlocked(true);
							matrix2[cursor][randomPartition].setBlocked(true);
							animate(5);
						}
						cursor++;
					}
					if (!matrix[secondRow][randomPartition].isBlocked()) {
						matrix[cursor - 1][randomPartition].setBlocked(false);
						matrix[cursor - 1][randomPartition].getMazeButton().setBackground(Color.white);
						matrix2[cursor - 1][randomPartition].setBlocked(false);
						matrix2[cursor - 1][randomPartition].getMazeButton().setBackground(Color.white);
						animate(5);
					}
					belowGenerateRandomMaze2(firstRow, firstColumn, secondRow, randomPartition, matrix2);
					belowGenerateRandomMaze2(firstRow, randomPartition, secondRow, secondColumn, matrix2);
				}
			}
		}
	}

	private boolean validWall(Node node) {
		return !node.isDestiny() && !node.isSource();
	}

	private boolean validInputs(int firstRow, int firstColumn, int secondRow, int secondColumn) {
		return (firstRow > -1 && firstRow < matrix.length) && (firstColumn > -1 && firstColumn < matrix.length) && (secondRow > -1 && secondRow < matrix.length) && (secondColumn > -1 && secondColumn < matrix.length);
	}

	private int amongRandomNumberGenerator(int a, int b) {
		return (a + 1) + (int) (Math.random() * (b - a - 1));
	}

	public Node[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(Node[][] matrix) {
		this.matrix = matrix;
	}

	public Node getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(Node sourceNode) {
		if (this.sourceNode != null) {
			this.sourceNode.setSource(false);
		}
		this.sourceNode = sourceNode;
		this.sourceNode.setSource(true);
	}

	public Node getGoalNode() {
		return goalNode;
	}

	public void setGoalNode(Node goalNode) {
		if (this.goalNode != null) {
			this.goalNode.setDestiny(false);
		}
		this.goalNode = goalNode;
		this.goalNode.setDestiny(true);
		this.goalNode.setBlocked(false);
	}

	public void clearMarks() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j].isMarked()) {
					matrix[i][j].setMarked(false);
					matrix[i][j].getMazeButton().setBackground(Color.decode("#ffffff"));
				}
			}
		}
	}

	public void clearPath() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j].isPathPart()) {
					matrix[i][j].setPathPart(false);
					matrix[i][j].getMazeButton().setBackground(Color.decode("#ffffff"));
				}
			}
		}
	}
}
