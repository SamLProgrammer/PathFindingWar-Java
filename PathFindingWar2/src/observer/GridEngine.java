package observer;

import models.Node;

public class GridEngine {

	private boolean sourceSelected;
	private boolean destinySelected;
	private boolean wallBuilding;
	private Node sourceDijkstraNode;
	private Node sourceAStarNode;
	private Node destinyDijkstraNode;
	private Node destinyAStarNode;

	public Node getDestinyDijkstraNode() {
		return destinyDijkstraNode;
	}

	public void setDestinyDijkstraNode(Node destinyDijkstraNode) {
		this.destinyDijkstraNode = destinyDijkstraNode;
	}

	public Node getDestinyAStarNode() {
		return destinyAStarNode;
	}

	public void setDestinyAStarNode(Node destinyAStarNode) {
		this.destinyAStarNode = destinyAStarNode;
	}

	public boolean isSourceSelected() {
		return sourceSelected;
	}

	public void setSourceSelected(boolean sourceSelected) {
		this.sourceSelected = sourceSelected;
	}

	public boolean isDestinySelected() {
		return destinySelected;
	}

	public void setDestinySelected(boolean destinySelected) {
		this.destinySelected = destinySelected;
	}

	public Node getSourceDijkstraNode() {
		return sourceDijkstraNode;
	}

	public void setSourceDijkstraNode(Node sourceDijkstraNode) {
		this.sourceDijkstraNode = sourceDijkstraNode;
	}

	public Node getSourceAStarNode() {
		return sourceAStarNode;
	}

	public void setSourceAStarNode(Node sourceAStarNode) {
		this.sourceAStarNode = sourceAStarNode;
	}

	public boolean isWallBuilding() {
		return wallBuilding;
	}

	public void setWallBuilding(boolean wallBuilding) {
		this.wallBuilding = wallBuilding;
	}
}
