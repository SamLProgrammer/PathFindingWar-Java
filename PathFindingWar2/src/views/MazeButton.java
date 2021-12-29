package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import models.Node;
import observer.GridEngine;
import observer.MazeEngine;

public class MazeButton extends JButton {

	private Node dijkstraNode;
	private Node aStarNode;
	private boolean xd;
	private boolean sourceSelected;
	private boolean destinySelected;
	private MazeEngine mazeEngine;
	private GridEngine gridEngine;

	public MazeButton(Node dijkstraNode, Node aStarNode, boolean mazeType, MazeEngine mazeEngine, GridEngine gridEngine) {
		this.dijkstraNode = dijkstraNode;
		this.aStarNode = aStarNode;
		this.mazeEngine = mazeEngine;
		this.gridEngine = gridEngine;
		if (!mazeType) {
			dijkstraNode.setMazeButton(this);
			setBackground(Color.white);
			if (dijkstraNode.isMarked()) {
				setBackground(Color.decode("#DB9FB0"));
			}
			if (dijkstraNode.isBlocked()) {
				setBackground(Color.decode("#494949"));
			}
			if (dijkstraNode.isPathPart()) {
				setBackground(Color.decode("#9B4279"));
			}
			if (dijkstraNode.isDestiny()) {
				setBackground(Color.decode("#F14D52"));
			}
			if (dijkstraNode.isSource()) {
				setBackground(Color.decode("#539F38"));
			}
		} else {
			aStarNode.setMazeButton(this);
			setBackground(Color.white);
			if (aStarNode.isMarked()) {
				setBackground(Color.decode("#DB9FB0"));
			}
			if (aStarNode.isBlocked()) {
				setBackground(Color.decode("#494949"));
			}
			if (aStarNode.isPathPart()) {
				setBackground(Color.decode("#9B4279"));
			}
			if (aStarNode.isDestiny()) {
				setBackground(Color.decode("#F14D52"));
			}
			if (aStarNode.isSource()) {
				setBackground(Color.decode("#539F38"));
			}

		}
		setBorderPainted(false);
		initMouseListener();
	}

	private void initMouseListener() {
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				Point location = MouseInfo.getPointerInfo().getLocation();
				JPanel panel = (JPanel) e.getComponent().getParent();
				SwingUtilities.convertPointFromScreen(location, panel);
				Component mouseOver = panel.findComponentAt(location);
				if (mouseOver instanceof MazeButton) {
					MazeButton button = (MazeButton) mouseOver;
					if (!button.isXd()) {
						button.switchBlockedState();
					}
				}
			}
		});
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				Point location = MouseInfo.getPointerInfo().getLocation();
				JPanel panel = (JPanel) e.getComponent().getParent();
				SwingUtilities.convertPointFromScreen(location, panel);
				Component mouseOver = panel.findComponentAt(location);
				if (mouseOver instanceof MazeButton) {
					MazeButton button = (MazeButton) mouseOver;
					if (gridEngine.isSourceSelected()) {
						if (button.getDijkstraNode().isDestiny()) {
							gridEngine.setSourceSelected(false);
						} else {
							mazeEngine.clearMaze();
							setBackground(Color.green);
							button.getDijkstraNode().setPreviouslyBlocked(true);
							button.getaStarNode().setPreviouslyBlocked(true);
							mazeEngine.updateSourceNode(button.getDijkstraNode(), button.getaStarNode());
							gridEngine.setSourceSelected(false);
						}
					}
					if (gridEngine.isDestinySelected()) {
						if (button.getDijkstraNode().isSource()) {
							gridEngine.setDestinySelected(false);
						} else {
							mazeEngine.clearMaze();
							setBackground(Color.red);
							if (button.getDijkstraNode().isBlocked()) {
								button.getDijkstraNode().setPreviouslyBlocked(true);
								button.getaStarNode().setPreviouslyBlocked(true);
							}
							mazeEngine.updateDestinyNode(button.getDijkstraNode(), button.getaStarNode());
							gridEngine.setDestinySelected(false);
						}
					}
				}
				gridEngine.setWallBuilding(false);
				mazeEngine.updateMazePanel();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				switchBlockedState();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (gridEngine.isSourceSelected() && !dijkstraNode.isDestiny()) {
					if (dijkstraNode.isBlocked()) {
						setBackground(Color.decode("#494949"));
					} else {
						setBackground(Color.white);
					}
				}
				if (gridEngine.isDestinySelected() && !dijkstraNode.isSource()) {
					if (dijkstraNode.isBlocked()) {
						setBackground(Color.decode("#494949"));
					} else if (dijkstraNode.isPreviouslyBlocked()) {
						dijkstraNode.setBlocked(true);
						dijkstraNode.setPreviouslyBlocked(false);
						setBackground(Color.decode("#494949"));
					} else {
						setBackground(Color.white);
					}
				}
				xd = false;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (gridEngine.isSourceSelected() && !dijkstraNode.isDestiny()) {
					setBackground(Color.decode("#539F38"));
				}
				if (gridEngine.isDestinySelected() && !dijkstraNode.isSource()) {
					setBackground(Color.decode("#F14D52"));
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}

	public void switchBlockedState() {
		if (!dijkstraNode.isDestiny() && !dijkstraNode.isSource() && !gridEngine.isSourceSelected() && !gridEngine.isDestinySelected()) {
			gridEngine.setWallBuilding(true);
			if (!dijkstraNode.isBlocked() && !aStarNode.isBlocked()) {
				dijkstraNode.setBlocked(true);
				aStarNode.setBlocked(true);
				setBackground(Color.decode("#494949"));
			} else {
				dijkstraNode.setBlocked(false);
				aStarNode.setBlocked(false);
				setBackground(Color.white);
			}
		} else if (!gridEngine.isWallBuilding() && dijkstraNode.isSource() && !gridEngine.isDestinySelected()) {
			if (gridEngine.isSourceSelected() && dijkstraNode != gridEngine.getSourceDijkstraNode()) {

			} else {
				gridEngine.setSourceSelected(true);
			}
		} else if (!gridEngine.isWallBuilding() && dijkstraNode.isDestiny() && !gridEngine.isSourceSelected()) {
			if (gridEngine.isDestinySelected() && dijkstraNode != gridEngine.getDestinyDijkstraNode()) {

			} else {
				gridEngine.setDestinySelected(true);
			}
		}
		xd = true;
	}

	public void mark() {
		if (dijkstraNode.isMarked()) {
			dijkstraNode.setMarked(false);
		} else {
			dijkstraNode.setMarked(true);
		}
	}

	public boolean isXd() {
		return xd;
	}

	public boolean isSourceSelected() {
		return sourceSelected;
	}

	public boolean isDestinySelected() {
		return destinySelected;
	}

	public Node getDijkstraNode() {
		return dijkstraNode;
	}

	public Node getaStarNode() {
		return aStarNode;
	}
}
