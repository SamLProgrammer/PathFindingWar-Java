package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import models.Node;
import observer.MazeEngine;

public class MazeFrame extends JFrame {

	private static final String FONT = "GLOUCESTER MT EXTRA CONDENSED";
	private static final String BACKGROUND_COLOR = "#F5EED0";
	private MazePanel dijkstraMazePanel;
	private MazePanel aStarMazePanel;
	private NiceButton pathButton;
	private JButton clearButton;
	private JButton resetButton;
	private NiceButton mazeGeneratorButton;
	private NiceButton huntAndKillMazeButton;
	private NiceButton kruskalMazeButton;
	private NiceButton ratMazeGeneratorButton;
	private JLabel dijkstraTimeLabel;
	private JLabel aStarTimeLabel;
	private JLabel dijkstraPathCounterLabel;
	private JLabel aStarPathCounterLabel;
	private MouseListener kruskalMouseListener;
	private MouseListener huntAndKillMouseListener;
	private MouseListener pathButtonMouseListener;
	private MouseListener clearButtonMouseListener;
	private MouseListener resetButtonMouseListener;
	private MouseListener mazeGeneratorButtonMouseListener;
	private MouseListener ratMazeGeneratorButtonMouseListener;

	public MazeFrame(Node[][] dijkstraMatrix, Node[][] aStarMatrix, MazeEngine mazeEngine) {
		locate();
		initComponents(dijkstraMatrix, aStarMatrix, mazeEngine);
		setVisible(true);
		setIconImage(new ImageIcon(getClass().getResource("/img/Rat3.png")).getImage());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void initComponents(Node[][] dijkstraMatrix, Node[][] aStarMatrix, MazeEngine mazeEngine) {
		dijkstraMazePanel = new MazePanel(false, dijkstraMatrix, aStarMatrix, mazeEngine);
		aStarMazePanel = new MazePanel(true, dijkstraMatrix, aStarMatrix, mazeEngine);

		JPanel dijkstraPanel = new JPanel(new BorderLayout());
		dijkstraPanel.setOpaque(false);
		JPanel aStarPanel = new JPanel(new BorderLayout());
		aStarPanel.setOpaque(false);

		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();

		JPanel buttonsAndMeasuresPanel = new JPanel();
		buttonsAndMeasuresPanel.setBackground(Color.decode(BACKGROUND_COLOR));
		JPanel mazesPanel = new JPanel(new GridLayout(1, 2));
		mazesPanel.setBorder(BorderFactory.createEmptyBorder(0, width / 40, 0, width / 40));
		mazesPanel.setOpaque(false);

		dijkstraTimeLabel = new JLabel("Execution Time: ", SwingConstants.CENTER);
		dijkstraTimeLabel.setForeground(Color.black);
		dijkstraTimeLabel.setFont(new Font(FONT, Font.PLAIN, height / 60));
		aStarTimeLabel = new JLabel("Execution Time: ", SwingConstants.CENTER);
		aStarTimeLabel.setForeground(Color.black);
		aStarTimeLabel.setFont(new Font(FONT, Font.PLAIN, height / 60));
		dijkstraPathCounterLabel = new JLabel("Path Length: ", SwingConstants.CENTER);
		dijkstraPathCounterLabel.setForeground(Color.black);
		dijkstraPathCounterLabel.setFont(new Font(FONT, Font.PLAIN, height / 60));
		aStarPathCounterLabel = new JLabel("Path Length: ", SwingConstants.CENTER);
		aStarPathCounterLabel.setForeground(Color.black);
		aStarPathCounterLabel.setFont(new Font(FONT, Font.PLAIN, height / 60));

		JPanel dijkstraStatsPanel = new JPanel(new GridLayout(1, 2));
		dijkstraStatsPanel.setOpaque(false);
		dijkstraStatsPanel.add(dijkstraTimeLabel);
		dijkstraStatsPanel.add(dijkstraPathCounterLabel);
		JPanel aStarStatsPanel = new JPanel(new GridLayout(1, 2));
		aStarStatsPanel.setOpaque(false);
		aStarStatsPanel.add(aStarTimeLabel);
		aStarStatsPanel.add(aStarPathCounterLabel);

		JPanel statsPanel = new JPanel(new GridBagLayout());
		statsPanel.setBackground(Color.decode(BACKGROUND_COLOR));

		JLabel blueLabel = new JLabel(); // cuadritos de abajo
		blueLabel.setOpaque(true);
		blueLabel.setBackground(Color.decode("#DB9FB0"));
		blueLabel.setBorder(BorderFactory.createEmptyBorder(height / 120, width / 120, height / 120, width / 120));
		JLabel blackLabel = new JLabel();
		blackLabel.setOpaque(true);
		blackLabel.setBackground(Color.decode("#494949"));
		blackLabel.setBorder(BorderFactory.createEmptyBorder(height / 120, width / 120, height / 120, width / 120));
		JLabel yellowLabel = new JLabel();
		yellowLabel.setOpaque(true);
		yellowLabel.setBackground(Color.decode("#9B4279"));
		yellowLabel.setBorder(BorderFactory.createEmptyBorder(height / 120, width / 120, height / 120, width / 120));
		JLabel greenLabel = new JLabel();
		greenLabel.setOpaque(true);
		greenLabel.setBackground(Color.decode("#539F38"));
		greenLabel.setBorder(BorderFactory.createEmptyBorder(height / 120, width / 120, height / 120, width / 120));
		JLabel redLabel = new JLabel();
		redLabel.setOpaque(true);
		redLabel.setBackground(Color.decode("#F14D52"));
		redLabel.setBorder(BorderFactory.createEmptyBorder(height / 120, width / 120, height / 120, width / 120));

		JLabel analyzedNodesLabel = new JLabel("Analyzed Nodes", SwingConstants.CENTER);
		analyzedNodesLabel.setFont(new Font(FONT, Font.BOLD, height / 60));
		analyzedNodesLabel.setForeground(Color.black);
		JLabel blockedNodesLabel = new JLabel("Blocked Nodes", SwingConstants.CENTER);
		blockedNodesLabel.setFont(new Font(FONT, Font.BOLD, height / 60));
		blockedNodesLabel.setForeground(Color.black);
		JLabel pathNodesLabel = new JLabel("Path Nodes", SwingConstants.CENTER);
		pathNodesLabel.setFont(new Font(FONT, Font.BOLD, height / 60));
		pathNodesLabel.setForeground(Color.black);
		JLabel startNodeLabel = new JLabel("Start Node", SwingConstants.CENTER);
		startNodeLabel.setFont(new Font(FONT, Font.BOLD, height / 60));
		startNodeLabel.setForeground(Color.black);
		JLabel goalNodeLabel = new JLabel("Goal Node", SwingConstants.CENTER);
		goalNodeLabel.setFont(new Font(FONT, Font.BOLD, height / 60));
		goalNodeLabel.setForeground(Color.black);

		JPanel containerPanel = new JPanel(new GridBagLayout());
		containerPanel.setBackground(Color.decode(BACKGROUND_COLOR));
		GridBagConstraints gbc = new GridBagConstraints();

		JLabel dijkstraTitleLabel = new JLabel("Dijkstra", SwingConstants.CENTER);
		dijkstraTitleLabel.setFont(new Font(FONT, Font.BOLD, height / 30));
		dijkstraTitleLabel.setForeground(Color.black);
		JLabel aStarTitleLabel = new JLabel("A*(Star)", SwingConstants.CENTER);
		aStarTitleLabel.setFont(new Font(FONT, Font.BOLD, height / 30));
		aStarTitleLabel.setForeground(Color.black);
		
		initButtonsListeners(mazeEngine);

		pathButton = new NiceButton("/img/Rat.png","War", "#000000","#ffffff", FONT, height / 40);
		pathButton.addMouseListener(pathButtonMouseListener);

		mazeGeneratorButton = new NiceButton("/img/Rat7.png","Gnawed Field", "#000000","#ffffff", FONT, height / 40);
		mazeGeneratorButton.addMouseListener(mazeGeneratorButtonMouseListener);
		
		ratMazeGeneratorButton = new NiceButton("/img/Rat2.png","Rat Field", "#000000","#ffffff", FONT, height / 40);
		ratMazeGeneratorButton.addMouseListener(ratMazeGeneratorButtonMouseListener);
		
		huntAndKillMazeButton = new NiceButton("/img/Rat2.png","Hunt & Kill Field", "#000000","#ffffff", FONT, height / 40);
		huntAndKillMazeButton.addMouseListener(huntAndKillMouseListener);
		
		kruskalMazeButton = new NiceButton("/img/Rat2.png","Nice & Easy Field", "#000000","#ffffff", FONT, height / 40);
		kruskalMazeButton.addMouseListener(kruskalMouseListener);		

		clearButton = new NiceButton("/img/mask.png","Detox", "#000000","#ffffff", FONT, height / 40);
		clearButton.addMouseListener(clearButtonMouseListener);

		resetButton = new NiceButton("/img/purge.png","Purge", "#000000","#ffffff", FONT, height / 40);
		resetButton.addMouseListener(resetButtonMouseListener);

		dijkstraPanel.add(dijkstraMazePanel);
		dijkstraPanel.add(dijkstraStatsPanel, BorderLayout.SOUTH);

		aStarPanel.add(aStarMazePanel);
		aStarPanel.add(aStarStatsPanel, BorderLayout.SOUTH);

		mazesPanel.add(dijkstraPanel);
		mazesPanel.add(aStarPanel);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 5;
		gbc.gridheight = 1;
		gbc.weightx = 5;
		gbc.weighty = 0.3;
		gbc.fill = GridBagConstraints.NONE;

		containerPanel.add(dijkstraTitleLabel, gbc);

		gbc.gridx = 5;
		gbc.gridy = 0;

		containerPanel.add(aStarTitleLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 10;
		gbc.gridheight = 10;
		gbc.weightx = 10;
		gbc.weighty = 10;
		gbc.fill = GridBagConstraints.BOTH;

		containerPanel.add(mazesPanel, gbc);

		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;

		statsPanel.add(greenLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 3;
		gbc.weighty = 3;

		statsPanel.add(startNodeLabel, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;

		statsPanel.add(redLabel, gbc);

		gbc.gridx = 3;
		gbc.gridy = 0;

		statsPanel.add(goalNodeLabel, gbc);

		gbc.gridx = 4;
		gbc.gridy = 1;

		statsPanel.add(blackLabel, gbc);

		gbc.gridx = 5;
		gbc.gridy = 0;

		statsPanel.add(blockedNodesLabel, gbc);

		gbc.gridx = 6;
		gbc.gridy = 1;

		statsPanel.add(blueLabel, gbc);

		gbc.gridx = 7;
		gbc.gridy = 0;

		statsPanel.add(analyzedNodesLabel, gbc);

		gbc.gridx = 8;
		gbc.gridy = 1;

		statsPanel.add(yellowLabel, gbc);

		gbc.gridx = 9;
		gbc.gridy = 0;

		statsPanel.add(pathNodesLabel, gbc);
		
		buttonsAndMeasuresPanel.add(mazeGeneratorButton);
		buttonsAndMeasuresPanel.add(huntAndKillMazeButton);
		buttonsAndMeasuresPanel.add(kruskalMazeButton);
		buttonsAndMeasuresPanel.add(ratMazeGeneratorButton);
		buttonsAndMeasuresPanel.add(pathButton);
		buttonsAndMeasuresPanel.add(clearButton);
		buttonsAndMeasuresPanel.add(resetButton);

		JPanel southPanel = new JPanel(new GridLayout(2, 1));
		southPanel.add(buttonsAndMeasuresPanel);
		southPanel.add(statsPanel);

		add(containerPanel);
		add(southPanel, BorderLayout.SOUTH);
	}

	private void initButtonsListeners(MazeEngine mazeEngine) {
		mazeGeneratorButtonMouseListener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mazeGeneratorButton.setForeground(Color.decode("#8E3542"));// #8E3542, #A63230
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mazeGeneratorButton.setForeground(Color.decode("#000000"));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				shutDownButtonListeners();
				disableButtons();
				mazeEngine.resetMaze();
				mazeEngine.generateWarField();
				resetStatsLabels();
			}
		};
		
		kruskalMouseListener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				kruskalMazeButton.setForeground(Color.decode("#8E3542"));// #8E3542, #A63230
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				kruskalMazeButton.setForeground(Color.decode("#000000"));
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				shutDownButtonListeners();
				disableButtons();
				mazeEngine.resetMaze();
				mazeEngine.generateKruskalMaze();
				resetStatsLabels();
			}
		};
		
		huntAndKillMouseListener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				huntAndKillMazeButton.setForeground(Color.decode("#8E3542"));// #8E3542, #A63230
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				huntAndKillMazeButton.setForeground(Color.decode("#000000"));
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				shutDownButtonListeners();
				disableButtons();
				mazeEngine.resetMaze();
				mazeEngine.generateHuntAndKillMaze();
				resetStatsLabels();
			}
		};
		
		ratMazeGeneratorButtonMouseListener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				ratMazeGeneratorButton.setForeground(Color.decode("#8E3542"));// #8E3542, #A63230
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				ratMazeGeneratorButton.setForeground(Color.decode("#000000"));
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				shutDownButtonListeners();
				disableButtons();
				mazeEngine.resetMaze();
				mazeEngine.generateRatMaze();
				resetStatsLabels();
			}
		};
		
		pathButtonMouseListener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				pathButton.setForeground(Color.decode("#8E3542"));// #8E3542, #A63230
			}

			@Override
			public void mouseExited(MouseEvent e) {
				pathButton.setForeground(Color.decode("#000000"));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				shutDownButtonListeners();
				mazeEngine.clearMaze();
				disableButtons();
				resetStatsLabels();
				mazeEngine.initPathing();
			}
		};
		
		clearButtonMouseListener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				clearButton.setForeground(Color.decode("#8E3542"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				clearButton.setForeground(Color.decode("#000000"));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				shutDownButtonListeners();
				mazeEngine.clearMaze();
				resetStatsLabels();
				resetButtonsListeners();
			}
		};
		
		resetButtonMouseListener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				resetButton.setForeground(Color.decode("#8E3542"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				resetButton.setForeground(Color.decode("#000000"));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				shutDownButtonListeners();
				mazeEngine.resetMaze();
				resetStatsLabels();
				resetButtonsListeners();
			}
		};
	}
	
	private void locate() {
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setSize(4 * width / 5, 4 * height / 5);
		setLocationRelativeTo(null);
		setExtendedState(MAXIMIZED_BOTH);
	}

	public void updateMazeGUI(Node[][] dijkstraMatrix, Node[][] aStarMatrix, MazeEngine mazeEngine) {
		dijkstraMazePanel.updateMazeGUI(dijkstraMatrix, aStarMatrix, mazeEngine);
		aStarMazePanel.updateMazeGUI(dijkstraMatrix, aStarMatrix, mazeEngine);
	}

	private void resetStatsLabels() {
		dijkstraTimeLabel.setText("Execution Time: ");
		dijkstraPathCounterLabel.setText("Path Length: ");
		aStarTimeLabel.setText("Execution Time: ");
		aStarPathCounterLabel.setText("Path Length: ");
	}

	public void updateAStarMazeGUI(Node[][] dijkstraMazeMatrix, Node[][] aStarMazeMatrix, MazeEngine mazeEngine) {
		aStarMazePanel.updateMazeGUI(dijkstraMazeMatrix, aStarMazeMatrix, mazeEngine);
	}

	public void updateDijkstraMazeGUI(Node[][] dijkstraMazeMatrix, Node[][] aStarMazeMatrix, MazeEngine mazeEngine) {
		dijkstraMazePanel.updateMazeGUI(dijkstraMazeMatrix, aStarMazeMatrix, mazeEngine);
	}

	public void updateAStarMazeStats(int pathLength, double executionTime) {
		aStarPathCounterLabel.setText("Path Length: " + pathLength + " Nodes");
		aStarTimeLabel.setText("Execution Time: " + executionTime + " (Sec)");
	}

	public void updateDijkstraMazeStats(int pathLength, double executionTime) {
		dijkstraPathCounterLabel.setText("Path Length: " + pathLength + " Nodes");
		dijkstraTimeLabel.setText("Execution Time: " + executionTime + " (Sec)");
	}
	
	private void shutDownButtonListeners() {		
		pathButton.removeMouseListener(pathButtonMouseListener);
		clearButton.removeMouseListener(clearButtonMouseListener);
		resetButton.removeMouseListener(resetButtonMouseListener);
		mazeGeneratorButton.removeMouseListener(mazeGeneratorButtonMouseListener);
		huntAndKillMazeButton.removeMouseListener(huntAndKillMouseListener);
		ratMazeGeneratorButton.removeMouseListener(ratMazeGeneratorButtonMouseListener);
		kruskalMazeButton.removeMouseListener(kruskalMouseListener);
	}
	
	private void resetButtonsListeners() {
		pathButton.addMouseListener(pathButtonMouseListener);
		clearButton.addMouseListener(clearButtonMouseListener);
		resetButton.addMouseListener(resetButtonMouseListener);		
		mazeGeneratorButton.addMouseListener(mazeGeneratorButtonMouseListener);
		huntAndKillMazeButton.addMouseListener(huntAndKillMouseListener);
		ratMazeGeneratorButton.addMouseListener(ratMazeGeneratorButtonMouseListener);
		kruskalMazeButton.addMouseListener(kruskalMouseListener);
	}

	public void disableButtons() {
		pathButton.setEnabled(false);
		clearButton.setEnabled(false);
		resetButton.setEnabled(false);
		mazeGeneratorButton.setEnabled(false);
		kruskalMazeButton.setEnabled(false);
		huntAndKillMazeButton.setEnabled(false);
		ratMazeGeneratorButton.setEnabled(false);
	}

	public void enableButtons() {
		resetButtonsListeners();
		pathButton.setEnabled(true);
		pathButton.setForeground(Color.decode("#000000"));
		clearButton.setEnabled(true);
		clearButton.setForeground(Color.decode("#000000"));
		resetButton.setEnabled(true);
		resetButton.setForeground(Color.decode("#000000"));
		mazeGeneratorButton.setEnabled(true);
		mazeGeneratorButton.setForeground(Color.decode("#000000"));
		ratMazeGeneratorButton.setEnabled(true);
		ratMazeGeneratorButton.setForeground(Color.decode("#000000"));
		huntAndKillMazeButton.setEnabled(true);
		huntAndKillMazeButton.setForeground(Color.decode("#000000"));
		kruskalMazeButton.setEnabled(true);
		kruskalMazeButton.setForeground(Color.decode("#000000"));
	}
}
