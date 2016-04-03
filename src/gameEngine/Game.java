/**
 * 
 */
package gameEngine;

import java.util.ArrayList;

import componentLibrary.*;



/**
 * @author Nemo Li
 * 
 */
public class Game {
	
	private Board board;
	private Player playerA;
	private Player playerB;
	
	
	/**
	 * Constructor, creates a board and two players
	 */
	public Game(boolean Ai) {
		board = new Board(Data.DIMENSION);
		playerA = new Player();
		if (Ai){
			playerB = new AI();
			((AI)playerB).opponent = playerA;
		}else{
			playerB = new Player();
		}
	}

	
	
	/**
	 * called when the game transitions into in game state
	 * updates all attack paths and reset move count of all pieces
	 */
	public void startGame() {
		board.updateAttack();
		board.resetMoveCount();
	}

	
	
	/**
	 * @param player - player focused on
	 * @return a list of initialized pieces
	 * 
	 * initializes pieces depending on which one is asked for
	 * should only be called once
	 */
	public ArrayList<Piece> setupPieces(int player) {
		if (player == Data.PLAYER_A) {
			return initPiecesA();
		} else {
			return initPiecesB();
		}
	}
	
	
	
	/**
	 * @param srcId	 - id of potential attacker
	 * @param destId - id of potential target
	 * @return true id src can attack dest
	 */
	public boolean canAttack(int srcId, int destId) {
		if (srcId == Data.PLAYER_NONE) {
			return false;
		}
		Piece attacker = board.getPieceAt(srcId);
		if (attacker == null) {
			return false;
		}

		return attacker.canAttack(destId);
	}

	
	
	/**
	 * @param attacker - id of attacker
	 * @param target   - id of target
	 * 
	 * carries out the attack, update player information
	 */
	public void attack(int attacker, int target) {
		if (board.getPieceAt(target).isRoyal()) {
			if (board.getPieceAt(attacker).getPlayer() == Data.PLAYER_A) {
				playerA.royalCount--;
			} else {
				playerB.royalCount--;
			}
		}
		board.attack(attacker, target);
	}
	
	
	
	/**
	 * @param id - source id
	 * @return player of the piece at id
	 */
	public int getPiecePlayer(int id) {
		if (id == Data.PLAYER_NONE) {
			return Data.PLAYER_NONE;
		}
		Piece p = board.getPieceAt(id);
		if (p == null) {
			return Data.PLAYER_NONE;
		}
		return board.getPieceAt(id).getPlayer();
	}

	
	
	/**
	 * @param id - source id
	 * @return true if there is a piece at id
	 */
	public boolean isPiece(int id) {
		if (board.getPieceAt(id) != null) {
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * @param srcId	 - source id
	 * @param destId - destination id
	 * @param player - the moving piece's player
	 * @return true if moved successfully
	 */
	public boolean freeMoveItem(int srcId, int destId, int player) {
		if (board.isCastle(srcId)) {
			return board.freeMoveCastle(srcId, destId, player);
		} else {
			return board.freeMovePiece(srcId, destId);
		}
	}
	
	

	/**
	 * @param terrain - terrain type
	 * 
	 * updates the terrain of the entire board
	 */
	public void setTerrain(int[] terrain) {
		for (int i = 0; i < Data.P_COUNT; i++) {
			board.setTerrain(i, terrain[i]);
		}
	}
	
	
	
	/**
	 * @param id - source id
	 * @return true if the piece at id is ranged
	 */
	public boolean isRanged(int id) {
		if (id == Data.PLAYER_NONE) {
			return false;
		}
		Piece p = board.getPieceAt(id);
		if (p == null)
			return false;
		return p.isRanged();
	}
	
	
	
	/**
	 * @param player
	 * @return castle of player
	 */
	public Castle getCastleByPlayer(int player) {
		if (player == Data.PLAYER_A) {
			return playerA.castle;
		} else {
			return playerB.castle;
		}
	}
	
	
	
	/**
	 * @param id
	 * @return player of castle at id
	 */
	public int getCastlePlayer(int id){
		if (board.getCastle(id) == null){
			return Data.PLAYER_NONE;
		}
		return board.getCastle(id).getPlayer();
	}

	
	
	/**
	 * @param player
	 * @return entrance of player's castle
	 */
	public int getCastleEntrance(int player) {
		if (player == Data.PLAYER_A) {
			return playerA.castle.getEntrance();
		} else {
			return playerB.castle.getEntrance();
		}
	}
	
	
	
	/**
	 * @param id - source id
	 * @return true id the piece at id is moved in a turn
	 */
	public boolean isPieceMoved(int id) {
		if (id == Data.PLAYER_NONE || board.getPieceAt(id) == null) {
			return false;
		}
		return board.getPieceAt(id).getMoveCount() > 0;
	}
	
	
	
	/**
	 * @return the player that has no royal pieces
	 * 			0 if both player has pieces
	 */
	public int getPlayerWithNoRoyal() {
		if (playerA.royalCount == 0) {
			return Data.PLAYER_A;
		} else if (playerB.royalCount == 0) {
			return Data.PLAYER_B;
		} else {
			return Data.PLAYER_NONE;
		}
	}
	
	
	
	/**
	 * @param source
	 * @param destination
	 * @return true if board could move source to destination
	 * 			according to the rules of the pieces and terrain
	 */
	public boolean ruledMovePiece(int source, int destination) {
		return board.ruledMovePiece(source, destination);
	}

	
	
	/**
	 * @param id
	 * @return true if position id has a castle
	 */
	public boolean isCastle(int id) {
		return board.isCastle(id);
	}

	
	
	/**
	 * updates all attack paths on board
	 */
	public void updateAttack() {
		board.updateAttack();
	}

	
	
	/**
	 * resets all move counts of pieces on board
	 */
	public void resetMoveCount() {
		board.resetMoveCount();
	}
	
	
	
	/**
	 * @param id - source id
	 * @return list of reachable positions by piece at id
	 */
	public ArrayList<Integer> getReachablePaths(int id) {
		return board.getPieceAt(id).getReachableId();
	}
	
	
	
	/**
	 * @param id - source id
	 * @return returns terrain at id
	 */
	public int getTerrain(int id){
		return board.getTerrain(id);
	}
	
	
	
	/**
	 * saves the state of the game
	 */
	public void saveGame() {
		// TODO to be implemented
	}
	
	
	
	/**
	 * @param isSetup
	 * @return the id AI selected
	 */
	public int AISelect(boolean isSetup) {
		return ((AI)playerB).selectPiece(isSetup);
	}
	

	
	/**
	 * @param isSetup
	 * @return the id AI moved to
	 */
	public int AIMove(boolean isSetup) {
		return ((AI)playerB).moveSelectedPiece(isSetup);
	}

	
	
	/**
	 * @return list of initialized pieces for player A
	 */
	private ArrayList<Piece> initPiecesA() {
		ArrayList<Integer> emptyA = new ArrayList<Integer>(0);
		ArrayList<Piece> pA = new ArrayList<Piece>(0);

		findEmptySlots(emptyA, Data.PLAYER_A);			// find available places to put pieces
		addPiecesToPlayer(emptyA, pA, Data.PLAYER_A);	// adds all game pieces to first available spots

		playerA.pieces = pA;
		playerA.castle = new Castle(emptyA.get(13), Data.PLAYER_A, Data.DIR_EAST, board);
		playerA.royalCount = 3;
		playerA.pieceCount = 13;

		return pA;
	}

	
	
	/**
	 * @return list of initialized pieces for player B
	 */
	private ArrayList<Piece> initPiecesB() {
		ArrayList<Integer> emptyB = new ArrayList<Integer>(0);
		ArrayList<Piece> pB = new ArrayList<Piece>(0);

		findEmptySlots(emptyB, Data.PLAYER_B);
		addPiecesToPlayer(emptyB, pB, Data.PLAYER_B);

		playerB.pieces = pB;
		playerB.castle = new Castle(emptyB.get(13), Data.PLAYER_B, Data.DIR_WEST, board);
		playerB.royalCount = 3;
		playerB.pieceCount = 13;

		return pB;
	}

	
	
	/**
	 * @param emptySlots - empty places on board to place pieces
	 * @param player	 - the player we are adding pieces for
	 */
	private void findEmptySlots(ArrayList<Integer> emptySlots, int player) {
		if (player == Data.PLAYER_A) {
			for (int i = 0; i < Data.BOUNDARY_A; i++) {
				if (board.getTerrain(i) == Data.TERRAIN_DEFAULT) {
					emptySlots.add(i);
				}
			}
		} else {
			for (int i = Data.P_COUNT - 1; i > Data.BOUNDARY_B; i--) {
				if (board.getTerrain(i) == Data.TERRAIN_DEFAULT
						&& board.getTerrain(i - 1) == Data.TERRAIN_DEFAULT
						&& i % Data.DIMENSION != 0) {
					emptySlots.add(i);
				}
			}
		}
	}

	
	
	/**
	 * @param emptySlots
	 * @param pieces
	 * @param player
	 * 
	 * adds all pieces to the first available places on board
	 */
	private void addPiecesToPlayer(ArrayList<Integer> emptySlots,
			ArrayList<Piece> pieces, int player) {
		pieces.add(new King(emptySlots.get(0), player, board));
		pieces.add(new Duke(emptySlots.get(1), player, board));
		pieces.add(new Prince(emptySlots.get(2), player, board));
		pieces.add(new Knight(emptySlots.get(3), player, board));
		pieces.add(new Knight(emptySlots.get(4), player, board));
		pieces.add(new Sergeant(emptySlots.get(5), player, board));
		pieces.add(new Sergeant(emptySlots.get(6), player, board));
		pieces.add(new Squire(emptySlots.get(7), player, board));
		pieces.add(new Archer(emptySlots.get(8), player, board));
		pieces.add(new Pikeman(emptySlots.get(9), player, board));
		pieces.add(new Pikeman(emptySlots.get(10), player, board));
		pieces.add(new Pikeman(emptySlots.get(11), player, board));
		pieces.add(new Pikeman(emptySlots.get(12), player, board));
	}

}
