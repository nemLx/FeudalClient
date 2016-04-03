package gameEngine;

import java.awt.Point;
import java.util.ArrayList;

import componentLibrary.*;


/**
 * @author Nemo Li
 * 
 */
public class AI extends Player {
	
	public Player opponent;
	private Piece selected = null;
	private int movesMade = 0;
	private int difficulty = 13;
	
	
	
	/**
	 * @param isSetup - true if selecting piece in setup stage
	 * @return id of the selected piece
	 */
	public int selectPiece(boolean isSetup) {
		if (isSetup) {
			if (movesMade == pieceCount) {
				movesMade = 0;
				return -1;
			}
			selected = pieces.get(movesMade);
			return selected.getId();
		}

		if (movesMade > difficulty) {
			movesMade = 0;
			return -1;
		}

		selected = bestPieceToMove();

		if (selected == null) {
			return -1;
		}

		return selected.getId();
	}
	
	
	
	/**
	 * @param isSetup - true if moving in setup stage
	 * @return id of move destination
	 */
	public int moveSelectedPiece(boolean isSetup) {
		movesMade++;

		if (isSetup) {
			if (selected.isRoyal()) {
				return selected.getId(); // do not move royal
			} else {
				int offset = (int) (Math.random() * Data.BOUNDARY_A);
				int dest = Data.BOUNDARY_A + offset; // prepare a randomized value for backup

				while (selected.getBoard().getPieceAt(dest) != null
					|| selected.getBoard().getTerrain(dest) != Data.TERRAIN_DEFAULT) {
					offset = (int) (Math.random() * Data.BOUNDARY_A);
					dest = Data.BOUNDARY_A + offset; // if the random place cannot be moved to, keep generating
				}

				return Data.BOUNDARY_A + offset;
			}
		}

		if (movesMade == pieceCount) {
			movesMade = difficulty; // if no more piece left to move, return
		}

		return bestMoveOfPiece(selected);
	}
	
	
	
	/**
	 * @return - the "best" piece to be moved
	 */
	private Piece bestPieceToMove() {
		Piece best = null;
		Piece curr = null;

		for (int i = 0; i < pieceCount; i++) {
			curr = pieces.get(i);

			if (isBetter(curr, best)) {
				best = curr;
			}
		}

		return best;
	}

	
	
	/**
	 * @param curr
	 * @param best
	 * @return true if a piece is better than another
	 */
	private boolean isBetter(Piece curr, Piece best) {
		if (curr.getMoveCount() > 0) {
			return false;
		}

		if (best == null) {
			return true;
		}

		if (attackValue(curr) > attackValue(best)) {
			return true;
		} else {
			return false;
		}
	}

	
	
	/**
	 * @param p
	 * @return the value of attack of a piece
	 */
	private int attackValue(Piece p) {
		Piece best = bestAttackedPiece(p);

		if (best == null) {
			return 0;
		} else if (best.isRoyal()) {
			return 2;
		} else {
			return 1;
		}
	}

	
	
	/**
	 * @param p
	 * @return best move of a piece
	 */
	private int bestMoveOfPiece(Piece p) {
		ArrayList<Point> attackList = p.getReachablePoint();

		if (attackList == null) {
			return 0;
		}

		int bestMove = 0;
		Piece bestAttackedPiece = bestAttackedPiece(p);
		if (bestAttackedPiece != null) {
			bestMove = bestAttackedPiece.getId();
		}

		if (bestMove != 0) {
			return bestMove;
		} else {
			int randomIndex = (int) (Math.random() * p.getReachablePoint().size());
			Point randomMove = p.getReachablePoint().get(randomIndex);
			return randomMove.x + p.getParentBoardDim() * randomMove.y;
		}
	}

	
	
	/**
	 * @param p
	 * @return the attacked piece with most value to AI
	 */
	private Piece bestAttackedPiece(Piece p) {
		Piece best = null;

		for (int i = 0; i < opponent.pieceCount; i++) {
			if (p.canAttack(opponent.pieces.get(i).getId())) {
				if (opponent.pieces.get(i).isRoyal()) {
					best = opponent.pieces.get(i);
					break;
				} else {
					best = opponent.pieces.get(i);
				}
			}
		}

		return best;
	}
}
