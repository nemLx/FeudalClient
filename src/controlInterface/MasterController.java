package controlInterface;

import gameEngine.*;
import userInterface.FileIO;
import userInterface.GUIView;

/**
 * @author Nemo Li
 * 
 * master controller that coordinates the pace
 * and controlls in the game
 */
public class MasterController {

	private Game model;
	private GUIView view;
	private RemoteInterface remoteControl;
	private boolean isOnlineGame = false;
	private int state = Data.STATE_NEWGAME; 		// master state for major procedures
	private int turn = Data.PLAYER_NONE;			// turn sub-state for turns for the game
	private int pieceSelected = Data.PLAYER_NONE;	// focus of piece selection
	private boolean isAIGame = false;
	private boolean isActive = false;
	private boolean remoteClick = false;
	
	// error codes
	private static final int NO_PIECE = -1;
	private static final int SAME_TEAM = 1;
	private static final int OPPOSITE_TEAM = 0;
	
	
	public MasterController(Game model, GUIView view) {
		this.model = model;
		this.view = view;
	}
	
	
	
	/**
	 * @param id
	 * 
	 * handles a remote board click based on the id
	 */
	public void handleRemoteClick(int id){
		remoteClick = true;
		if (id < Data.PROT_L_CLICK_LIMIT){
			leftClicked(id);
		}else{
			rightClicked(id-Data.P_COUNT);
		}
		remoteClick = false;
	}
	
	
	
	/**
	 * @param id - id of the cell clicked on
	 * 
	 * calls different handles on the click depends 
	 * on the state of the game
	 */
	public void leftClicked(int id) {
		
		switch (state){
		case Data.STATE_NEWGAME:
			//view.showMessage(Data.MSG_PROCEDURES);
			break;
			
		case Data.STATE_SET_TERRAIN:
			view.toggleBackground(id, Data.COLOR_MOUNTAIN);
			if (isOnlineGame && !remoteClick){
				remoteControl.leftClick(id);
			}
			break;
		
		case Data.STATE_IN_GAME:
			handleInGameLeftClick(id);
			break;
			
		case Data.STATE_SET_A_PIECES:
			handleFreeMoveLeftClick(Data.PLAYER_A, id);
			break;
			
		case Data.STATE_SET_B_PIECES:
			handleFreeMoveLeftClick(Data.PLAYER_B, id);
			break;
		}
	}


	
	/**
	 * @param id - source of click
	 * 
	 * calls different functions depending on the type
	 * of the click
	 */
	private void handleInGameLeftClick(int id) {
		if (isNullClick(id)){
			return;	// did not select a piece and nothing to select
		}
		
		if (isOnlineGame && !remoteClick){
			remoteControl.leftClick(id);
		}
		
		switch (isSamePlayer(id, turn)) {
		case NO_PIECE: // no piece
			handleEmptyClick(id); // selecting

			break;
		case OPPOSITE_TEAM: // opposite player
			handleAggresiveClick(id); // attacking

			break;
		case SAME_TEAM: // same player
			handleSelectiveClick(id); // selecting
			break;
		}
	}
	
	
	
	/**
	 * @param id - source of click
	 * @return true if the click did not select anything while nothing is selected
	 */
	private boolean isNullClick(int id){
		return 	(pieceSelected == Data.PLAYER_NONE && !model.isPiece(id));
	}

	
	
	/**
	 * @param id - source of click
	 * 
	 * handles the case when the piece at id is selected
	 */
	private void handleSelectiveClick(int id) {
		pieceSelected = id;
		if (remoteClick){
			return;
		}
		view.toggleReachablePaths(model.getReachablePaths(id), true);
	}


	
	/**
	 * @param id - source of click
	 * 
	 * handles the case where the piece at id is being attacked
	 */
	private void handleAggresiveClick(int id) {
		if (model.isPieceMoved(pieceSelected) || 
			!model.canAttack(pieceSelected, id)) {
			return; // cannot move anymore or cannot attack
		}
		attack(pieceSelected, id);
		pieceSelected = Data.PLAYER_NONE;
		checkWinState(id);
	}


	
	/**
	 * @param id - source of click
	 * 
	 * handles the case where the click is not selecting,
	 * but there has already been pieces selected
	 */
	private void handleEmptyClick(int id) {
		if (model.isPieceMoved(pieceSelected) || 
			!model.ruledMovePiece(pieceSelected, id)){
			return; // cannot move anymore or cannot move
		}
		view.eraseAllAttackPaths();
		view.movePiece(pieceSelected, id);
		pieceSelected = Data.PLAYER_NONE;
		checkWinState(id);
	}


	
	/**
	 * @param id - source of click
	 * 
	 * checks against the winning state
	 */
	private void checkWinState(int id) {
		int winner = model.getPlayerWithNoRoyal();
		
		if (winner != Data.PLAYER_NONE){
			handleWin(winner);
		}else if (model.isCastle(id)){
			if (model.getPiecePlayer(id) != model.getCastlePlayer(id)){
				handleWin(model.getPiecePlayer(id));
			}
		}
	}


	
	/**
	 * @param winner
	 * 
	 * handles the case where a player wins
	 */
	private void handleWin(int winner) {
		if (winner == Data.PLAYER_A){
			view.showMessage(Data.MSG_PLAYER_ONE_WIN);
		}else{
			view.showMessage(Data.MSG_PLAYER_TWO_WIN);
		}
		
		state = Data.STATE_END_GAME;
		
		if (isOnlineGame){
			remoteControl.disconnect();
			isOnlineGame = false;
		}
	}


	
	/**
	 * @param id - source of click
	 * 
	 * handles the case of a right click in in_game state
	 */
	private void handleInGameRightClick(int id) {
		if (pieceSelected == id){
			view.toggleReachablePaths(model.getReachablePaths(id), false);
		}else{
			view.eraseAllAttackPaths();
		}
	}
	
	
	
	/**
	 * @param player - the player initiated the click
	 * @param id	 - the source of click
	 * 
	 * handles the case where a player is clicking in
	 * the setup stage
	 */
	private void handleFreeMoveLeftClick(int player, int id){
		if (isIllegalPlacement(player, id) && !isOnlineGame){
			return; // clicked on the area where he cannot place
		}
		
		if (isOnlineGame && !remoteClick){
			remoteControl.leftClick(id);
		}
		
		if (model.isPiece(id) || model.isCastle(id)) { // destination is not
													   // open, cannot move
			if (attempMovedToCastle(id)) {	
				return;	// attempted to move to a castle
			} else {
				freeMoveAttemptHighlight(id);  // select another piece
			}
		} else if (pieceSelected != Data.PLAYER_NONE && 
				model.freeMoveItem(pieceSelected, id, player)) {
			actualFreeMove(player, id);	// actually moving
		}
	}

	

	/**
	 * @param player - player initiated the move
	 * @param id	 - source of the click
	 * 
	 * handles the actual free moving of piece
	 */
	private void actualFreeMove(int player, int id) {
		if (model.isCastle(id)) {	// moving castle
			view.moveCastle(pieceSelected, id, model.getCastleEntrance(player));
			pieceSelected = -1;
		} else {	// moving piece
			view.toggleBackground(pieceSelected, Data.COLOR_HIGHTLIGHT);
			view.movePiece(pieceSelected, id);
			pieceSelected = -1;
			//view.toggleBackground(id, Data.COLOR_HIGHTLIGHT);
		}
	}

	

	/**
	 * @param id - source of click
	 * 
	 * handles the case where no movement happened but
	 * selected, therefore highlighted piece
	 */
	private void freeMoveAttemptHighlight(int id) {
		if (pieceSelected != Data.PLAYER_NONE && pieceSelected != id && 
			!model.isCastle(pieceSelected)){	// selected a different piece that is not castle
			view.toggleBackground(pieceSelected, Data.COLOR_HIGHTLIGHT);
		}
		
		view.toggleBackground(id, Data.COLOR_HIGHTLIGHT);
		
		if (pieceSelected == id){
			pieceSelected = Data.PLAYER_NONE;
		}else{
			pieceSelected = id;
		}
	}


	
	/**
	 * @param id - source of click
	 * @return true if attempted to move to a castle
	 * 
	 * if attempted to move/select castle, toggle
	 * highlight on the old piece and select castle
	 */
	private boolean attempMovedToCastle(int id) {
		if (model.isCastle(id)){
			if (pieceSelected != Data.PLAYER_NONE && 
					!model.isCastle(pieceSelected)){
				view.toggleBackground(pieceSelected, Data.COLOR_HIGHTLIGHT);
			}
			pieceSelected = id;
			return true;
		}
		return false;
	}


	
	/**
	 * @param player
	 * @param id
	 * @return true if the movement is illegal
	 */
	private boolean isIllegalPlacement(int player, int id) {
		if (player == Data.PLAYER_A){
			if (id > Data.BOUNDARY_B){
				view.showMessage(Data.MSG_CANNOT_PLACE);
				return true;
			}
		}else{
			if (id < Data.BOUNDARY_A){
				view.showMessage(Data.MSG_CANNOT_PLACE+" "+id);
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * @param id
	 * @param turn
	 * @return true if the source of the click has a 
	 * 			piece that is on the same player as
	 * 			the turn
	 */
	private int isSamePlayer(int id, int turn) {
		int player = model.getPiecePlayer(id);
		
		if (player == Data.PLAYER_NONE){
			return NO_PIECE;
		}else if (player == turn){
			return SAME_TEAM;
		}else{
			return OPPOSITE_TEAM;
		}
	}


	
	/**
	 * @param attacker - id of attacking piece
	 * @param target   - id of attacked piece
	 * 
	 * carries out the attacking operation alone
	 */
	private void attack(int attacker, int target) {
		model.attack(attacker, target);
		view.eraseAllAttackPaths();
		
		if (model.isRanged(attacker)){
			view.erasePiece(target);
		}else{
			view.movePiece(attacker, target);
		}
	}
	
	
	
	/**
	 * @param isSetup - if this series of actions is a setup movement
	 * 
	 * moves all pieces AI chooses to move in a turn/setup
	 */
	private void AIActions(boolean isSetup) {
		
		int selected = model.AISelect(isSetup);
		int moved = model.AIMove(isSetup);

		while (selected != -1) {
			this.leftClicked(selected);
			this.leftClicked(moved);
			selected = model.AISelect(isSetup);
			moved = model.AIMove(isSetup);
		}
	}
	
	
	
	/**
	 * @param player
	 * 
	 * handles setup piece menu selection
	 */
	public void setupPieces(int player) {
		if (isOnlineGame){
			return;	// in online mode, user could not setup pieces by clicking menu
		}
		
		if (player == Data.PLAYER_A){
			if (state == Data.STATE_SET_TERRAIN){
				playerASetup(true);	// A could only setup from set terrain state
			}
		}else{
			if (state == Data.STATE_SET_A_PIECES){
				playerBSetup(true); // B could only setup from A setup state
			}
		}
	}
	
	
	
	/**
	 * handles when setup for A is selected
	 */
	public void playerASetup(boolean active) {
		state = Data.STATE_SET_A_PIECES;
		pieceSelected = Data.PLAYER_NONE;
		model.setTerrain(view.getTerrain());
		
		this.toggleActive(active);
		view.toggleBoardVisibility(active, true);
		view.toggleBoardVisibility(!active, false);
		
		view.drawPieces(model.setupPieces(Data.PLAYER_A));
		view.drawCastles(model.getCastleByPlayer(Data.PLAYER_A));
		
		view.drawPieces(model.setupPieces(Data.PLAYER_B));
		view.drawCastles(model.getCastleByPlayer(Data.PLAYER_B));
		
		if (!active){
			this.showMessage(Data.MSG_WAIT_FOR_PLAYER);
		}else if (isOnlineGame){
			this.showMessage(Data.MSG_YOUR_TURN);
		}
	}


	
	/**
	 * handles when setup for B is selected
	 */
	public void playerBSetup(boolean active) {
		state = Data.STATE_SET_B_PIECES;
		pieceSelected = Data.PLAYER_NONE;
		
		if (!active){
			this.toggleActive(false);
			this.showMessage(Data.MSG_WAIT_FOR_PLAYER);
			return;
		}else if (isOnlineGame){
			this.showMessage(Data.MSG_YOUR_TURN);
		}
		
		if (!isOnlineGame){
			view.toggleBoardVisibility(false, true);
			view.toggleBoardVisibility(true, false);
		}
		
		if (isAIGame && !isOnlineGame){
			this.AIActions(true);
			this.startGame(Data.PLAYER_A, false);
		}else{
			this.toggleActive(true);
		}
	}


	
	/**
	 * @param id - source of click
	 * 
	 * handles all right clicks
	 */
	public void rightClicked(int id) {
		switch (state) {
		case Data.STATE_SET_TERRAIN:
			view.toggleBackground(id, Data.COLOR_WETLAND);
			if (isOnlineGame && !remoteClick){
				remoteControl.rightClick(id);
			}
			remoteClick = false;
			break;
		case Data.STATE_IN_GAME:
			handleInGameRightClick(id);
			if (isOnlineGame && !remoteClick){
				remoteControl.rightClick(id);
			}
			remoteClick = false;
			break;
		}
	}
	
	
	
	/**
	 * toggles the turn of the game
	 */
	public void toggleTurn(boolean given) {
		if (state != Data.STATE_IN_GAME){
			return;
		}
		this.toggleActive(true);
		
		if (isOnlineGame && !given){
			remoteControl.submitTurn();
			this.toggleActive(false);
		}
		if (turn == Data.PLAYER_A){
			turn = Data.PLAYER_B;
		}else{
			turn = Data.PLAYER_A;
		}
		view.eraseAllAttackPaths();
		pieceSelected = Data.PLAYER_NONE;
		model.resetMoveCount();
		
		if (isAIGame && turn == Data.PLAYER_B){
			this.toggleActive(false);
			this.AIActions(false);
			toggleTurn(false);
		}
	}
	
	
	
	/**
	 * handles new game menu selection
	 */
	public void newGame(boolean Ai) {
		if (isOnlineGame){
			isOnlineGame = false;
			remoteControl.disconnect();
		}
		
		this.toggleActive(true);
		MenuSelectListener.toggleOnlineMode(false);
		view.reset();
		view.showMessage(Data.MSG_PROCEDURES);
		state = Data.STATE_NEWGAME;
		model = new Game(Ai);
		isAIGame = Ai;
	}
	
	
	
	/**
	 * prepares and searches for server and other player
	 */
	public void setOnlineGame() {
		Data.loadProtocal();
		
		if ((Data.HOST = view.getHost()) == null){
			return;
		}
		
		this.showMessage(Data.MSG_ONLINE_HELP);
		
		remoteControl = new RemoteInterface(this);
		
		if (!remoteControl.isLive()){
			this.showMessage(Data.MSG_NETWORK_ERR);
			return;
		}
		
		isOnlineGame = true;
		MenuSelectListener.toggleOnlineMode(true);
		
		Thread remoteThread = new RemoteControlThread(remoteControl);
		remoteThread.start();
	}


	
	/**
	 * handles start game menu selection
	 */
	public void startGame(int firstTurn, boolean onlineOveride) {
		if (state != Data.STATE_SET_B_PIECES && !onlineOveride){
			return;
		}
		
		turn = firstTurn;
		remoteClick = false;
		state = Data.STATE_IN_GAME;
		pieceSelected = Data.PLAYER_NONE;
		
		view.cleanHighlight();
		view.toggleBoardVisibility(true, true);
		view.toggleBoardVisibility(false, true);
		
		model.setTerrain(view.getTerrain());
		model.startGame();
	}


	
	/**
	 * handles save game menu selection
	 */
	public void saveGame() {
		model.saveGame();
		view.showMessage(Data.MSG_SAVE_GAME);
	}
	
	
	
	/**
	 * handles set terrain menu selection
	 */
	public void setTerrain(){
		if (state != Data.STATE_NEWGAME){
			return; // could only set terrain from new game state
		}
		if (isOnlineGame && isActive){
			this.showMessage(Data.MSG_SET_TERRAIN_HELP);
		}
		state = Data.STATE_SET_TERRAIN;
		//view.showMessage(Data.MSG_SET_TERRAIN_HELP);
	}

	
	
	/**
	 * handles save terrain menu selection
	 */
	public void saveTerrain() {
		if (state != Data.STATE_SET_TERRAIN){
			return; // could only save terrain in set terrain state
		}
		FileIO fileHandler = new FileIO();
		fileHandler.saveTerrainToFile(view.getTerrain());
		view.showMessage(Data.MSG_SAVE_TERRAIN);
	}
	
	
	
	/**
	 * handles load terrain menu selection
	 */
	public void loadTerrain(){
		if (state != Data.STATE_SET_TERRAIN){
			return; // could only load terrain in set terrain state
		}
		state = Data.STATE_SET_TERRAIN;
		FileIO fileHandler = new FileIO();
		int[] terrain = fileHandler.readTerrainFile();
		view.loadTerrain(terrain);
		
		if (isOnlineGame){
			remoteControl.loadTerrain(terrain);
		}
	}
	
	
	
	/**
	 * handle submit of terrain in online mode
	 */
	public void submitTerrain(){
		if (!isOnlineGame){
			return;
		}
		
		state = Data.STATE_WAIT;
		remoteControl.doneSetTerrain();
	}
	
	
	
	/**
	 * handles submit of pieces setup in online mode
	 */
	public void submitSetup(){
		if (!isOnlineGame
				|| (state != Data.STATE_SET_A_PIECES
				&& state != Data.STATE_SET_B_PIECES)){
			return;
		}
		
		state = Data.STATE_WAIT;
		pieceSelected = -1;
		remoteControl.doneSetup();
	}
	
	
	
	/**
	 * @param msg
	 * 
	 * calls view to display a message
	 */
	public void showMessage(String msg){
		view.showMessage(msg);
	}
	
	
	
	/**
	 * @param active
	 * 
	 * activates/deactivates the mouse click listeners
	 */
	public void toggleActive(boolean active){
		isActive = active;
		ClickListener.activated = active;
	}


	
	/**
	 * makes entire board visible
	 */
	public void setAllVisible() {
		view.toggleBoardVisibility(true, true);
		view.toggleBoardVisibility(false, true);
	}
	
	
	public void indicateTurn(){
		showMessage(Data.MSG_YOUR_TURN);
	}
}
