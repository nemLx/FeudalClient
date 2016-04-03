package controlInterface;

import gameEngine.Data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * @author Nemo Li
 * 
 */

public class MenuSelectListener implements ActionListener {

	private String action;
	private MasterController controller;
	private static boolean isOnline = false;
	
	/**
	 * @param action
	 * @param controller
	 * 
	 * initialize each listener to a menu item with action
	 */
	public MenuSelectListener(String action, MasterController controller){
		this.action = new String(action);
		this.controller = controller;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * based on the action, select different controller calls
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (Data.STR_NEW_GAME.equals(action)){
			controller.newGame(false);
		}else if (Data.STR_START_GAME.equals(action) && !isOnline){
			controller.startGame(Data.PLAYER_A, false);
		}else if (Data.STR_SET_TERRAIN.equals(action) && !isOnline){
			controller.setTerrain();
		}else if (Data.STR_SAVE_TERRAIN.equals(action) && !isOnline){
			controller.saveTerrain();
		}else if (Data.STR_PLAYER_A.equals(action) && !isOnline){
			controller.setupPieces(1);
		}else if (Data.STR_PLAYER_B.equals(action) && !isOnline){
			controller.setupPieces(2);
		}else if (Data.STR_TOGGLE_TURN.equals(action)){
			controller.toggleTurn(false);
		}else if (Data.STR_LOAD_TERRAIN.equals(action)){
			controller.loadTerrain();
		}else if (Data.STR_FIND_GAME.equals(action) && !isOnline){
			controller.setOnlineGame();
		}else if (Data.STR_SUBMIT_TERRAIN.equals(action) && isOnline){
			controller.submitTerrain();
		}else if (Data.STR_SUBMIT_SETUP.equals(action) && isOnline){
			controller.submitSetup();
		}else if (Data.STR_AI_GAME.equals(action)){
			controller.newGame(true);
		}
		
	}
	
	public static void toggleOnlineMode(boolean online){
		isOnline = online;
	}
}
