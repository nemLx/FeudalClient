package gameEngine;

import java.awt.Color;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author Nemo Li
 * 
 * A constant dame data storage class
 */
public final class Data {
	
	public static final int STATE_IN_GAME = 1;
	
	public static final int STATE_SET_TERRAIN = 2;
	
	public static final int STATE_SET_A_PIECES = 3;
	
	public static final int STATE_SET_B_PIECES = 4;
	
	public static final int STATE_NEWGAME = 5;
	
	public static final int STATE_END_GAME = 6;
	
	public static final int STATE_WAIT = 7;
	
	public static final int STATE_SET_BOTH = 8;
	
	
	public static final int DIMENSION = 24;
	
	public static final int P_COUNT = DIMENSION*DIMENSION;
	
	public static final int BOUNDARY_A = P_COUNT/2;
	
	public static final int BOUNDARY_B = BOUNDARY_A-1;
	
	public static final Dimension MAIN_FRAME_DIM = new Dimension (863, 859);
	
	public static final Dimension BOARD_DIM = new Dimension (850, 850);
	
	public static final int CELLS_GAP = 1;
	
	public static final double SPRITE_SCALE = 0.86;
	
	public static final int PLAYER_NONE = -1;
	
	public static final int PLAYER_A = 1;
	
	public static final int PLAYER_B = 2;
	
	public static final int TERRAIN_MOUNTAIN = 2;
	
	public static final int TERRAIN_WETLAND = 1;
	
	public static final int TERRAIN_DEFAULT = 0;
	
	public static final int DIR_WEST = 0;
	
	public static final int DIR_NORTH = 1;
	
	public static final int DIR_EAST = 2;
	
	public static final int DIR_SOUTH = 3;
	
	
	
	public static final Color COLOR_MOUNTAIN = new Color(0, 100, 0);
	
	public static final Color COLOR_WETLAND = new Color(220, 220, 220);
	
	public static final Color COLOR_ATTACK = new Color(230, 180, 180);
	
	public static final Color COLOR_CASTLE = new Color(130, 130, 240);
	
	public static final Color COLOR_CASTLE_GREEN = new Color(151, 216, 160);
	
	public static final Color COLOR_HIGHTLIGHT = Color.ORANGE;
	
	public static final Color COLOR_DEFAULT = Color.WHITE;
	
	public static final Color COLOR_BACKGROUND = Color.BLACK;
	
	
	
	public static final String STR_GAME_TITLE = "Feudal";
	
	public static final String STR_GAME = "Game";
	
	public static final String STR_NEW_GAME = "New Game";
	
	public static final String STR_START_GAME = "Start Game";
	
	public static final String STR_FIND_GAME = "Online Game";
	
	public static final String STR_AI_GAME = "AI Game";
	
	
	
	public static final String STR_TERRAIN = "Terrain";
	
	public static final String STR_SET_TERRAIN = "Set Terrain";
	
	public static final String STR_LOAD_TERRAIN = "Load Terrain";
	
	public static final String STR_SAVE_TERRAIN = "Save Terrain";
	
	public static final String STR_SUBMIT_TERRAIN = "Submit Terrain";
	
	
	public static final String STR_SET_PIECES = "Set Pieces";
	
	public static final String STR_PLAYER_A = "Player A Setup";
	
	public static final String STR_PLAYER_B = "Player B Setup";
	
	public static final String STR_SUBMIT_SETUP = "Submit Setup";
	
	public static final String STR_TOGGLE_TURN = "Toggle Turn";
	
	
	
	public static final String FILE_TERRAIN = "saves/feudal_terrain.txt";
	
	public static final String FILE_GAME = "saves/feudal_game.txt";
	
	
	
	public static final String MSG_NOT_IMPLEMENTED = "This feature is yet to be implemented.";
	
	public static final String MSG_HELP = "Select puzzle from first pulldown list.\n" +
										  "Select board size from second pulldown list.\n";

	public static final String MSG_SET_TERRAIN_PROMPT = "Please Set Terrain First";
	
	public static final String MSG_SET_TERRAIN_HELP = "Left click to place mountain, " +
													  "right click to place wetland";

	public static final String MSG_PROCEDURES = "Game procedures: \n"
			+ "Set or Load Terrain \n" + "Setup Player A's Pieces \n"
			+ "Setup Player B's pieces \n" + "Start Game\n"
			+ "Click SHIFT+T to toggle between players";

	public static final String MSG_ONLINE_HELP = "Game procedures: \n"
			+ "Set terrain if you are prompted\n"
			+ "Click Submit Terrain when you are done\n"
			+ "Setup piece when prompted\n"
			+ "Click Submit Setup when you are done\n"
			+ "Click Toggle Turn to finish your turn.";

	public static final String MSG_CANNOT_PLACE = "You cannot place pieces in this area";
	
	public static final String MSG_SAVE_GAME = MSG_NOT_IMPLEMENTED;
	
	public static final String MSG_SAVE_TERRAIN = "Terrain saved as feudal_terrain.txt";
	
	public static final String MSG_PLAYER_ONE_WIN = "Player A is the WINNER!";
	
	public static final String MSG_PLAYER_TWO_WIN = "Player B is the WINNER!";
	
	public static final String MSG_NETWORK_ERR = "A network failure has occurred, resetting game.";
	
	public static final String MSG_WAIT_FOR_PLAYER = "Waiting for the other player.";
	
	public static final String MSG_YOUR_TURN = "It is your turn to move.";
	
	
	public static String HOST = null;
	
	public static int PORT = 7890;
	
	
	public static int PROT_TOGGLE_TURN;
	
	public static int PROT_SET_TERRAIN;
	
	public static int PROT_DONE_SET_TERRAIN;
	
	public static int PROT_SETUP_A;
	
	public static int PROT_SETUP_B;
	
	public static int PROT_SHUTDOWN;
	
	public static int PROT_L_CLICK_LIMIT;
	
	public static int PROT_R_CLICK_LIMIT;
	
	public static int PROT_GAME_CONFIRM;
	
	public static int PROT_PASSIVE;
	
	public static int PROT_DONE_SET_PIECE;
	
	public static int PROT_ACTIVE;
	
	public static int PROT_WAIT_FOR_A;
	
	public static int PROT_WAIT_FOR_B;
	
	public static int PROT_START_FIRST;
	
	public static int PROT_START_SECOND;
	
	public static int SERVER_CAPACITY = 1000;
	
	
	public static void loadProtocal(){
		
		Properties prot = new Properties();
		 
    	try {
    		prot.load(new FileInputStream("protocol.properties"));

    		PROT_GAME_CONFIRM = Integer.parseInt(prot.getProperty("GAME_CONFIRM"));
    		
    		PROT_PASSIVE = Integer.parseInt(prot.getProperty("PASSIVE"));
    		
    		PROT_DONE_SET_PIECE = Integer.parseInt(prot.getProperty("DONE_SET_PIECE"));
    		
    		PROT_ACTIVE = Integer.parseInt(prot.getProperty("ACTIVE"));
    		
    		PROT_WAIT_FOR_A = Integer.parseInt(prot.getProperty("WAIT_FOR_A"));
    		
    		PROT_WAIT_FOR_B = Integer.parseInt(prot.getProperty("WAIT_FOR_B"));
    		
    		PROT_START_FIRST = Integer.parseInt(prot.getProperty("START_FIRST"));
    		
    		PROT_START_SECOND = Integer.parseInt(prot.getProperty("START_SECOND"));
    		
    		PROT_TOGGLE_TURN = Integer.parseInt(prot.getProperty("TOGGLE_TURN"));
    		
    		PROT_SET_TERRAIN = Integer.parseInt(prot.getProperty("SET_TERRAIN"));
    		
    		PROT_DONE_SET_TERRAIN = Integer.parseInt(prot.getProperty("DONE_SET_TERRAIN"));
    		
    		PROT_SETUP_A = Integer.parseInt(prot.getProperty("SETUP_A"));
    		
    		PROT_SETUP_B = Integer.parseInt(prot.getProperty("SETUP_B"));
    		
    		PROT_SHUTDOWN = Integer.parseInt(prot.getProperty("SHUTDOWN"));
    	
    		PROT_L_CLICK_LIMIT = DIMENSION*DIMENSION;
    		
    		PROT_R_CLICK_LIMIT = 2*PROT_L_CLICK_LIMIT;
    		
    	} catch (Exception ex) {
    		ex.printStackTrace();
        }
	}
	
}
