package userInterface;

import gameEngine.Data;
import gameEngine.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import componentLibrary.*;

import controlInterface.ClickListener;
import controlInterface.MasterController;
import controlInterface.MenuSelectListener;



/**
 * @author Nemo Li
 * 
 */
public class GUIView {
	
	private MasterController controller;

	public JFrame mainFrame;
	private JPanel boardPanel;
	
	
	public GUIView() {
		controller = new MasterController (new Game(false), this);

		setStyle();
		constructMainFrame();

		mainFrame.pack();
		mainFrame.setVisible(true);
	}
	
	
	
	/**
	 * builds the main window
	 */
	private void constructMainFrame() {
		constructBoard(Data.DIMENSION);
		
		mainFrame = new JFrame(Data.STR_GAME_TITLE);
		mainFrame.setJMenuBar(buildMenuBar());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setPreferredSize(Data.MAIN_FRAME_DIM);
		mainFrame.setContentPane(boardPanel);
	}
	
	
	
	/**
	 * @param dim - dimension for the board
	 * 
	 * builds the board area
	 */
	private void constructBoard(int dim) {

		boardPanel = new JPanel();
		
		boardPanel.setPreferredSize(Data.BOARD_DIM);
		boardPanel.setBackground(Data.COLOR_BACKGROUND);
		boardPanel.setLayout(new GridLayout(dim, dim, Data.CELLS_GAP, Data.CELLS_GAP));

		addCellsToBoard(dim, boardPanel);
	}
	
	
	
	
	/**
	 * @param dim
	 * @param boardPanel
	 * 
	 * adds individual cells to the board area
	 * attaches mouse listeners
	 */
	private void addCellsToBoard(int dim, JPanel boardPanel) {
		
		final JPanel cells[] = new JPanel[dim * dim];

		for (int i = 0; i < dim * dim; i++) {
			final int id = i;
			cells[i] = new JPanel();
			cells[i].setBackground(Data.COLOR_DEFAULT);
			cells[i].addMouseListener(new ClickListener(id, controller));
			boardPanel.add(cells[i]);
		}
	}
	
	
	
	/**
	 * @return the menu bar for the main window
	 */
	private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
		
        buildGameMenu(menuBar);
        buildTerrainMenu(menuBar);
        buildPiecesMenu(menuBar);
 
        return menuBar;
    }

	
	
	/**
	 * @param menuBar
	 * 
	 * add the pieces menu to the menuBar
	 */
	private void buildPiecesMenu(JMenuBar menuBar) {
		
		JMenu pieceMenu = new JMenu(Data.STR_SET_PIECES);
		
        JMenuItem itemOne = new JMenuItem(Data.STR_PLAYER_A);
        itemOne.addActionListener(new MenuSelectListener(Data.STR_PLAYER_A, controller));
        pieceMenu.add(itemOne);
        
        JMenuItem itemTwo = new JMenuItem(Data.STR_PLAYER_B);
        itemTwo.addActionListener(new MenuSelectListener(Data.STR_PLAYER_B, controller));
        pieceMenu.add(itemTwo);
        
        JMenuItem itemThree = new JMenuItem(Data.STR_TOGGLE_TURN);
        itemThree.addActionListener(new MenuSelectListener(Data.STR_TOGGLE_TURN, controller));
        itemThree.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.SHIFT_MASK));
        pieceMenu.add(itemThree);
        
        JMenuItem itemFour = new JMenuItem(Data.STR_SUBMIT_SETUP);
        itemFour.addActionListener(new MenuSelectListener(Data.STR_SUBMIT_SETUP, controller));
        pieceMenu.add(itemFour);
        
        menuBar.add(pieceMenu);
	}

	
	/**
	 * @param menuBar
	 * 
	 * add the terrain menu to the menu bar
	 */
	private void buildTerrainMenu(JMenuBar menuBar) {
		
		JMenu terrainMenu = new JMenu(Data.STR_TERRAIN);
        
        JMenuItem itemOne = new JMenuItem(Data.STR_SET_TERRAIN);
        itemOne.addActionListener(new MenuSelectListener(Data.STR_SET_TERRAIN, controller));
        terrainMenu.add(itemOne);
        
        JMenuItem itemTwo = new JMenuItem(Data.STR_SAVE_TERRAIN);
        itemTwo.addActionListener(new MenuSelectListener(Data.STR_SAVE_TERRAIN, controller));
        terrainMenu.add(itemTwo);
        
        JMenuItem itemThree = new JMenuItem(Data.STR_LOAD_TERRAIN);
        itemThree.addActionListener(new MenuSelectListener(Data.STR_LOAD_TERRAIN, controller));
        terrainMenu.add(itemThree);
        
        JMenuItem itemFour = new JMenuItem(Data.STR_SUBMIT_TERRAIN);
        itemFour.addActionListener(new MenuSelectListener(Data.STR_SUBMIT_TERRAIN, controller));
        terrainMenu.add(itemFour);
        
        menuBar.add(terrainMenu);
	}

	
	
	/**
	 * @param menuBar
	 * 
	 * add the game menu to the menu bar
	 */
	private void buildGameMenu(JMenuBar menuBar) {
		
		JMenu gameMenu = new JMenu(Data.STR_GAME);

        JMenuItem itemOne = new JMenuItem(Data.STR_NEW_GAME);
        itemOne.addActionListener(new MenuSelectListener(Data.STR_NEW_GAME, controller));
        gameMenu.add(itemOne);
 

        JMenuItem itemTwo = new JMenuItem(Data.STR_START_GAME);
        itemTwo.addActionListener(new MenuSelectListener(Data.STR_START_GAME, controller));
        gameMenu.add(itemTwo);
        
        JMenuItem itemThree = new JMenuItem(Data.STR_FIND_GAME);
        itemThree.addActionListener(new MenuSelectListener(Data.STR_FIND_GAME, controller));
        gameMenu.add(itemThree);
        
        JMenuItem itemFour = new JMenuItem(Data.STR_AI_GAME);
        itemFour.addActionListener(new MenuSelectListener(Data.STR_AI_GAME, controller));
        gameMenu.add(itemFour);
        
        menuBar.add(gameMenu);
	}
	
	
	
	/**
	 * @return a string that is the IP of the host
	 * 
	 * shows a dialog, prompts for IP address
	 */
	public String getHost(){
		HostDialog dg = new HostDialog(mainFrame);
		dg.pack();
		
		dg.setLocationRelativeTo(mainFrame);
        dg.setVisible(true);
        
        return dg.getValidatedText();
	}

	

	/**
	 * @param paths - paths under effect
	 * @param show	- whether to show or hide
	 * 
	 * given a list of positions, highlight or un-highlight the background with attack color
	 */
	public void toggleReachablePaths(ArrayList<Integer> paths, boolean show) {
		JPanel curr = null;
		
		if (paths == null){
			return;
		}
		
		for (int i = 0; i < paths.size(); i++){
			curr = (JPanel) boardPanel.getComponent(paths.get(i));
			if (show){
				if (curr.getBackground() == Data.COLOR_DEFAULT)
				curr.setBackground(Data.COLOR_ATTACK);
			}else{
				if (curr.getBackground() == Data.COLOR_ATTACK)
				curr.setBackground(Data.COLOR_DEFAULT);
			}
		}
		
		boardPanel.repaint();
	}

	
	
	
	/**
	 * @param p - individual piece to be drawn
	 * 
	 * draws a single piece on board
	 */
	public void drawPiece(Piece p) {
		JPanel curr = (JPanel) boardPanel.getComponent(p.getId());
		
		int pieceHeight = (int) (Data.SPRITE_SCALE*curr.getHeight());	// shrinks the size of the sprite
		int pieceWidth = (int) (Data.SPRITE_SCALE*curr.getWidth());		// to a fraction of the cell
		
		curr.add(constructPieceLabel(p.getSprite(), pieceHeight, pieceWidth), BorderLayout.NORTH);
		curr.revalidate();
		curr.repaint();
	}
	
	
	
	/**
	 * @param sprite
	 * @param h
	 * @param w
	 * @return
	 * 
	 * constructs a label containing the sprite of a piece
	 */
	private JLabel constructPieceLabel(String sprite, int h, int w) {
		Image g = null;
		
		try {
			g = ImageIO.read(new File(sprite));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		g = g.getScaledInstance(w, h, 0);
		
		Dimension pieceDim = new Dimension(h,w);
		ImageIcon pieceIcon = new ImageIcon(g);
		JLabel pieceLabel = new JLabel(pieceIcon, JLabel.CENTER);
		pieceLabel.setPreferredSize(pieceDim);
		
		return pieceLabel;
	}

	

	/**
	 * @param srcId	- id of piece to be moved
	 * @param dest	- position to move to
	 * 
	 * move a piece to specified location
	 */
	public void movePiece(int srcId, int dest) {
		JPanel source = (JPanel) boardPanel.getComponent(srcId);
		JPanel destination = (JPanel) boardPanel.getComponent(dest);
		JLabel srcLabel = (JLabel) source.getComponent(0);
		
		source.removeAll();
		
		if (destination.getComponentCount()!=0){
			destination.removeAll();
		}
		
		destination.add(srcLabel);		// the moving process is done by removing
										// all components from destination and move components
		source.revalidate();			// of source to the destination
		destination.revalidate();
		boardPanel.repaint();
	}


	
	/**
	 * @param srcId		- id of castle to be moved
	 * @param destId	- position to move to
	 * @param entrance	- position of castle green
	 * 
	 * move a castle to specified location, as well as castle green
	 */
	public void moveCastle(int srcId, int destId, int entrance) {
		int srcGreenId = findCastleEntrance(srcId, entrance);
		int destGreenId = findCastleEntrance(destId, entrance);

		boardPanel.getComponent(destId).setBackground(Data.COLOR_CASTLE);
		boardPanel.getComponent(destGreenId).setBackground(Data.COLOR_CASTLE_GREEN);
		
		boardPanel.getComponent(srcId).setBackground(Data.COLOR_DEFAULT);
		boardPanel.getComponent(srcGreenId).setBackground(Data.COLOR_DEFAULT);
		
		boardPanel.revalidate();
		boardPanel.repaint();
		boardPanel.updateUI();
	}
	
	
	
	/**
	 * @param srcId		- id of castle
	 * @param entrance	- entrance code of castle
	 * @return			- the JPanel that contains castle green
	 * 
	 * finds the castle green of the given castle
	 */
	private int findCastleEntrance(int srcId, int entrance){
		int retVal = 0;
		switch (entrance){
		case Data.DIR_WEST: //west
			retVal = srcId-1;
			break;
		case Data.DIR_NORTH: //north
			retVal = srcId-Data.DIMENSION;
			break;
		case Data.DIR_EAST: //east
			retVal = srcId+1;
			break;
		case Data.DIR_SOUTH: //south
			retVal = srcId+Data.DIMENSION;
			break;
		}
		return retVal;
	}

	
	
	/**
	 * @return	- an array containing terrain information at specific locations
	 */
	public int[] getTerrain() {
		int[] terrain = new int[Data.P_COUNT];
		JPanel curr = null;
		
		for (int i = 0; i < Data.P_COUNT; i++){
			curr = (JPanel) boardPanel.getComponent(i);
			Color c = curr.getBackground();
			
			if (c.equals(Data.COLOR_MOUNTAIN) || c.equals(Data.COLOR_CASTLE)){
				terrain[i] = Data.TERRAIN_MOUNTAIN;
			}else if (c.equals(Data.COLOR_WETLAND) || c.equals(Data.COLOR_CASTLE_GREEN)){
				terrain[i] = Data.TERRAIN_WETLAND;
			}else{
				terrain[i] = Data.TERRAIN_DEFAULT;
			}
		}
		
		for (int i = 0; i < 12; i++)
		System.out.println();
		
		return terrain;
	}
	
	
	/**
	 * @param castle	- castle to be drawn
	 * 
	 * draws a castle and its green by setting corresponding backgrounds
	 */
	public void drawCastles(Castle castle) {
		int id = castle.getId();
		int entrance = castle.getEntrance();
		
		JPanel t = (JPanel) boardPanel.getComponent(id);
		t.setBackground(Data.COLOR_CASTLE);
		
		t = (JPanel) boardPanel.getComponent(findCastleEntrance(id, entrance));
		t.setBackground(Data.COLOR_CASTLE_GREEN);
		
		t.repaint();
	}

	
	
	/**
	 * @param terrain	- array containing terrain information
	 * 
	 * draws mountains and wetland based on the given terrain
	 */
	public void loadTerrain(int[] terrain) {
		JPanel curr = null;
		
		for (int i = 0; i < Data.P_COUNT; i++){
			curr = (JPanel) boardPanel.getComponent(i);
			if (terrain[i] == 2){
				curr.setBackground(Data.COLOR_MOUNTAIN);
			}else if (terrain[i] == 1){
				curr.setBackground(Data.COLOR_WETLAND);
			}
		}
		
		boardPanel.repaint();
	}
	
	
	
	/**
	 * @param pieces	- list of pieces to be drawn
	 * 
	 * draws given list of pieces
	 */
	public void drawPieces(ArrayList<Piece> pieces) {
		for (int i = 0; i < pieces.size(); i++){
			drawPiece(pieces.get(i));
		}
		boardPanel.revalidate();
		boardPanel.repaint();
	}

	
	
	/**
	 * clears any left highlights cells during the 
	 * free moving process
	 */
	public void cleanHighlight() {
		JPanel curr = null;
		
		for (int i = 0; i < Data.P_COUNT; i++){
			curr = (JPanel) boardPanel.getComponent(i);
			if (curr.getBackground().equals(Data.COLOR_HIGHTLIGHT)){
				curr.setBackground(Data.COLOR_DEFAULT);
			}
		}
		
		boardPanel.repaint();
	}
	
	
	
	/**
	 * clears all highlights attack paths on board
	 */
	public void eraseAllAttackPaths() {
		JPanel curr = null;
		for (int i = 0; i < Data.P_COUNT; i++){
			curr = (JPanel) boardPanel.getComponent(i);
			if (curr.getBackground() == Data.COLOR_ATTACK){
				curr.setBackground(Data.COLOR_DEFAULT);
			}
		}
		boardPanel.repaint();
	}
	
	
	
	/**
	 * @param target - a specific piece to be removed
	 * 
	 * removes a piece
	 */
	public void erasePiece(int target) {
		JPanel t = (JPanel) boardPanel.getComponent(target);
		t.removeAll();
		t.revalidate();
		boardPanel.repaint();
	}
	
	
	
	/**
	 * set system style
	 */
	private void setStyle() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	

	
	/**
	 * reset the entire board to default look
	 */
	public void reset() {
		JPanel curr = null;
		
		for (int i = 0; i < Data.P_COUNT; i++){
			curr = (JPanel) boardPanel.getComponent(i);
			curr.removeAll();
			curr.setBackground(Color.WHITE);
			curr.revalidate();
		}
		toggleBoardVisibility(true, true);
		toggleBoardVisibility(false, true);
		
		boardPanel.revalidate();
		boardPanel.repaint();
	}

	
	
	/**
	 * @param id - the place to toggle
	 * @param c	 - the color to toggle to
	 * 
	 * toggles the background of a specific cell
	 */
	public void toggleBackground(int id, Color c) {
		JPanel curr = (JPanel) boardPanel.getComponent(id);
		if (curr.getBackground() == c){
			curr.setBackground(Data.COLOR_DEFAULT);
		}else{
			curr.setBackground(c);
		}
		boardPanel.repaint();
	}

	
	
	/**
	 * @param visible
	 * 
	 * toggles the visibility of top half of the board
	 */
	public void toggleBoardVisibility(boolean top, boolean visible) {
		JPanel curr = null;
		
		if (top){

			for (int i = 0; i < Data.BOUNDARY_A; i++) {
				curr = (JPanel) boardPanel.getComponent(i);
				curr.setVisible(visible);
			}

		}else{

			for (int i = Data.BOUNDARY_A; i < Data.P_COUNT; i++) {
				curr = (JPanel) boardPanel.getComponent(i);
				curr.setVisible(visible);
			}
		}
	}
	
	
	
	/**
	 * @param msg - message to be shown
	 * 
	 * pops up a dialog with a message
	 */
	public void showMessage(String msg){
		JOptionPane.showMessageDialog(mainFrame, msg);
	}
}
