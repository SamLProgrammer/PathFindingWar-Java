package models;

import java.awt.Color;
import java.util.LinkedList;

import views.MazeButton;

public class Node {

	private int x;
	private int y;
	private boolean marked;
	private boolean blocked;
	private boolean pathPart;
	private boolean destiny;
	private boolean enqueued;
	private boolean source;
	private boolean wall;
	private boolean visited;
	private boolean previouslyBlocked;
	private MazeButton mazeButton;
	private LinkedList<Connection> connections;
	private Node kruskalFather;
	private LinkedList<Node> kruskalChilds;

	public Node(int x, int y) {
		connections = new LinkedList<Connection>();
		initKruskalChidls();
		marked = false;
		kruskalFather = this;
		this.x = x;
		this.y = y;
	}

	public Node() {
		connections = new LinkedList<Connection>();
		marked = false;
	}

	public boolean connectionExists(Connection connection) {
		boolean flag = false;
		for (Connection auxConnection : connections) {
			if (auxConnection.getNode().getX() == connection.getNode().getX() && auxConnection.getNode().getY() == connection.getNode().getY()) {
				flag = true;
			}
		}
		return flag;
	}

	public boolean connectedNodeExists(Node node) {
		boolean flag = false;
		for (Connection connection : connections) {
			if (connection.getNode() == node) {
				flag = true;
			}
		}
		return flag;
	}

	public void addConnection(Connection connection) {
		if (!connectionExists(connection)) {
			connections.add(connection);
			connection.getNode().addConnection(new Connection(connection.getWeight(), this, connection.getNode()));
		}
	}

	public boolean isMarked() {
		return marked;
	}

	public LinkedList<Connection> getConnections() {
		return connections;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
		if (marked && source == false && destiny == false) {
			mazeButton.setBackground(Color.decode("#DB9FB0"));
		}
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
		if (blocked) {
			mazeButton.setBackground(Color.decode("#494949"));
		}
	}

	public boolean isPathPart() {
		return pathPart;
	}

	public void setPathPart(boolean pathPart) {
		this.pathPart = pathPart;
		if (pathPart && !source && !destiny) {
			mazeButton.setBackground(Color.decode("#9B4279"));
		}
	}

	public boolean isDestiny() {
		return destiny;
	}

	public void setDestiny(boolean destiny) {
		this.destiny = destiny;
	}

	public boolean isSource() {
		return source;
	}

	public void setSource(boolean source) {
		this.source = source;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setMazeButton(MazeButton mazeButton) {
		this.mazeButton = mazeButton;
	}

	public MazeButton getMazeButton() {
		return mazeButton;
	}

	public void setEnqueued(boolean enqueued) {
		this.enqueued = enqueued;
		if (enqueued && !source && !destiny) {
			mazeButton.setBackground(Color.decode("#82C3C2"));
		}
	}

	public boolean isEnqueued() {
		return enqueued;
	}

	public boolean isWall() {
		return wall;
	}

	public void setWall(boolean wall) {
		this.wall = wall;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public boolean isPreviouslyBlocked() {
		return previouslyBlocked;
	}

	public void setPreviouslyBlocked(boolean previouslyBlocked) {
		this.previouslyBlocked = previouslyBlocked;
	}

	public void initKruskalChidls() {
		kruskalChilds = new LinkedList<Node>();
	}

	public void addKruskalChild(Node node) {
		node.setKruskalFather(kruskalFather);
		kruskalChilds.add(node);
		updateAllChildsFather(node);
	}

	private void updateAllChildsFather(Node node) {
		for(Node auxNode : node.getKruskalChilds()) {
			auxNode.setKruskalFather(kruskalFather);
			updateAllChildsFather(auxNode);
		}
	}

	public LinkedList<Node> getKruskalChilds() {
		return kruskalChilds;
	}

	public Node getKruskalFather() {
		return kruskalFather;
	}

	public void setKruskalFather(Node kruskalFather) {
		this.kruskalFather = kruskalFather;
	}

	public void resetKruskal() {
		kruskalChilds = new LinkedList<Node>();
		this.kruskalFather = this;
	}
}
