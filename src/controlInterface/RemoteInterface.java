package controlInterface;

import gameEngine.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Nemo Li
 * 
 * interface to sending and interpreting action codes
 * from/to server
 */
public class RemoteInterface {

	private boolean isLive = false;
	private boolean isMyTurn = false;
	private Socket client;
	private PrintWriter out;
	private BufferedReader in;
	private MasterController controller;

	
	/**
	 * @param c
	 * 
	 * Constructor, sets up socket connection
	 */
	public RemoteInterface(MasterController c) {
		try {
			client = new Socket(Data.HOST, Data.PORT);
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
		} catch (Exception e) {
			System.out.println("failed to establish connection");
			return;
		}

		isLive = true;
		controller = c;
	}
	
	
	
	/**
	 * loops and listens for commands
	 */
	public void listen() {
		while (isLive) {
			int remoteAction = -1;

			remoteAction = readAction();

			if (remoteAction == Data.PROT_SHUTDOWN) {
				controller.newGame(false);
				return;
			}

			handleRemoteAction(remoteAction);
		}
	}
	
	
	
	/**
	 * @return action code read from server
	 */
	private int readAction(){
		String line = null;
		
		try {
			line = in.readLine();
		} catch (IOException e) {
			return Data.PROT_SHUTDOWN;
		}
		
		if (line == null){
			return Data.PROT_SHUTDOWN;
		}else{
			return Integer.parseInt(line);
		}
	}


	
	/**
	 * @param id - position id
	 * 
	 * sends id to server
	 */
	public void leftClick(int id) {
		sendActionCode(id);
	}
	
	

	/**
	 * @param id - position id
	 * 
	 * sends id to server, encode it as a right click
	 */
	public void rightClick(int id) {
		sendActionCode(id+Data.P_COUNT);
	}

	

	/**
	 *	sends signal to notify server terrain has been set 
	 */
	public void doneSetTerrain() {
		sendActionCode(Data.PROT_DONE_SET_TERRAIN);
	}
	

	
	/**
	 * sends signal to notify server setup piece is done
	 */
	public void doneSetup() {
		sendActionCode(Data.PROT_DONE_SET_PIECE);
	}
	
	
	
	/**
	 * @param action - code to be sent to server
	 * 
	 * resets the game if there is an exception
	 */
	private void sendActionCode(int action){
		try{
			out.println(action);
		}catch (Exception e){
			controller.newGame(false);
			return;
		}
	}
	

	
	/**
	 * notify the server I have finished my turn
	 */
	public void submitTurn() {
		if (!isMyTurn) {
			return;
		}

		sendActionCode(Data.PROT_TOGGLE_TURN);

		isMyTurn = false;
	}
	
	
	
	/**
	 * @param terrain - array of terrain to be sent
	 * 
	 * relays terrain information in the form of clicks
	 */
	public void loadTerrain(int[] terrain) {
		try {
			for (int i = 0; i < terrain.length; i++) {
				
				if (terrain[i] == 2) {
					out.println(i);
				} else if (terrain[i] == 1) {
					out.println(i + Data.P_COUNT);
				}
			}
		} catch (Exception e) {
			controller.newGame(false);
			return;
		}
	}
	
	
	
	/**
	 * @param first - true if starting game as first to go
	 * 
	 * starts the game
	 */
	private void startGame(boolean first){
		controller.startGame(Data.PLAYER_A, true);
		controller.toggleActive(first);
		isMyTurn = first;
	}
	
	
	
	/**
	 * @param action - action code
	 * @return
	 */
	private static boolean isBoardClick(int action) {
		return action > -1 && action < Data.PROT_R_CLICK_LIMIT;
	}
	
	
	/**
	 * kills the listening loop
	 */
	public void disconnect() {
		isLive = false;
	}
	
	
	
	/**
	 * @return isLive
	 */
	public boolean isLive(){
		return isLive;
	}
	
	
	
	/**
	 * @param remoteAction - action code
	 * 
	 * calls appropriate controller function based on the action code from server
	 */
	private void handleRemoteAction(int remoteAction) {
		if (remoteAction == Data.PROT_TOGGLE_TURN) {
			controller.toggleTurn(true);
			controller.indicateTurn();
			isMyTurn = true;
		} else if (remoteAction == Data.PROT_SET_TERRAIN) {
			controller.setTerrain();
		} else if (remoteAction == Data.PROT_SETUP_A) {
			controller.playerASetup(true);
		} else if (remoteAction == Data.PROT_SETUP_B) {
			controller.playerBSetup(true);
		} else if (remoteAction == Data.PROT_SHUTDOWN) {
			controller.newGame(false);
		} else if (isBoardClick(remoteAction)) {
			controller.handleRemoteClick(remoteAction);
		} else if (remoteAction == Data.PROT_GAME_CONFIRM) {
			controller.showMessage(Data.MSG_HELP);
		} else if (remoteAction == Data.PROT_PASSIVE) {
			controller.toggleActive(false);
		} else if (remoteAction == Data.PROT_DONE_SET_PIECE) {
			controller.setAllVisible();
		} else if (remoteAction == Data.PROT_ACTIVE) {
			controller.toggleActive(true);
		} else if (remoteAction == Data.PROT_WAIT_FOR_A) {
			controller.playerASetup(false);
		} else if (remoteAction == Data.PROT_WAIT_FOR_B) {
			controller.playerBSetup(false);
		} else if (remoteAction == Data.PROT_START_FIRST) {
			startGame(true);
			controller.indicateTurn();
		} else if (remoteAction == Data.PROT_START_SECOND) {
			startGame(false);
		}
	}
}
