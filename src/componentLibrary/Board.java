package componentLibrary;

import gameEngine.Data;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Nemo Li
 * 
 */
public class Board {

	private int dim;
	private int pCount;
	private int[][] terrain;
	private Piece[][] pGrid;		// using both array and list to store pieces is convenient to lookup
	private ArrayList<Piece> pList;	// and insert, and facilitate use by other functions
	
	private Castle[][] cGrid;
	private ArrayList<Castle> cList;
	
	
	
	/**
	 * @param d generates board with dimension d
	 */
	public Board(int d) {
		dim = d;
		pCount = 0;
		pGrid = new Piece[dim][dim];
		pList = new ArrayList<Piece>(0);
		cGrid = new Castle[dim][dim];
		cList = new ArrayList<Castle>(0);
		terrain = new int[dim][dim];
	}
	
	
	
	/**
	 * @param p - piece to be added
	 * @param x
	 * @param y
	 * 
	 * simple add piece p to (x,y) and updates
	 * attack paths on board
	 */
	public void add(Piece p, int x, int y){
		pGrid[x][y] = p;
		pList.add(p);
		pCount++;
		
		p.setCords(x, y);
		updateAttack();
	}
	
	
	/**
	 * @param c - castle to be added
	 * @param x
	 * @param y
	 * @param entrance - direction code for castle green
	 * 
	 * adds a castle to the board and updates attack paths
	 */
	public void add(Castle c, int x, int y, int entrance){
		Point green = c.getEntrancePoint();
		setTerrain(green.x, green.y);
		setTerrain(x, y, 2);
		cGrid[x][y] = c;
		cList.add(c);
		c.setCords(x, y);
		updateAttack();
	}
	
	
	
	/**
	 * @param attacker
	 * @param target
	 * 
	 * carries out attack operation on board
	 * increments move count of attacker
	 */
	public void attack(int attacker, int target) {
		Piece src = getPieceAt(attacker);
		if (src.isRanged) {
			rangedAttack(attacker, target);
		} else {
			moveAttack(attacker, target);
		}
		updateAttack();
		src.setMoveCount(src.getMoveCount() + 1);
	}
	
	
	
	/**
	 * @param attacker
	 * @param target
	 * 
	 * carries out move attack operation
	 */
	public void moveAttack(int attacker, int target) {
		Piece t = getPieceAt(target);
		int tX = t.getX();
		int tY = t.getY();

		pGrid[tX][tY] = null;
		pList.remove(t);
		pCount--;
		freeMovePiece(attacker, target);
	}
	
	
	
	/**
	 * @param attacker
	 * @param target
	 * 
	 * carries out range attack operation
	 */
	public void rangedAttack(int attacker, int target) {
		Piece t = getPieceAt(target);
		int tX = t.getX();
		int tY = t.getY();

		pGrid[tX][tY] = null;
		pList.remove(t);
		pCount--;
	}	
	
	
	/**
	 * @param source
	 * @param destination
	 * @param team
	 * @return true if free moved
	 */
	public boolean freeMoveCastle(int source, int destination, int team) {		
		Castle c = cList.get(team - 1);
		if (c.isGreen(destination)){
			return false;
		}
		int entrance = c.getEntrance();
		int srcX = c.getX();
		int srcY = c.getY();
		int destX = destination % dim;
		int destY = (destination - destX) / dim;

		if (!freeMoveCastleGreen(entrance, srcX, srcY, destX, destY)){
			return false;
		}
		
		setTerrain(srcX, srcY, 0);
		setTerrain(destX, destY, 2);
		cGrid[srcX][srcY] = null;
		cGrid[destX][destY] = c;
		c.setCords(destX, destY);
		updateAttack();
		
		return true;
	}

	
	
	/**
	 * @param entrance
	 * @param srcX
	 * @param srcY
	 * @param destX
	 * @param destY
	 * @return true if the placement of castle green is successful
	 */
	private boolean freeMoveCastleGreen(int entrance, int srcX, int srcY,
			int destX, int destY) {
		switch (entrance) {
		case Data.DIR_WEST: // west
			if (destX < 1)
				return false;
			setTerrain(srcX - 1, srcY, 0);
			setTerrain(destX - 1, destY, 1);
			break;
		case Data.DIR_NORTH: // north
			setTerrain(srcX, srcY - 1, 0);
			setTerrain(destX, destY - 1, 1);
			break;
		case Data.DIR_EAST: // east
			if (destX >= dim - 1)
				return false;
			setTerrain(srcX + 1, srcY, 0);
			setTerrain(destX + 1, destY, 1);
			break;
		case Data.DIR_SOUTH: // south
			setTerrain(srcX, srcY + 1, 0);
			setTerrain(destX, destY + 1, 1);
			break;
		}
		return true;
	}

	
	
	/**
	 * @param srcId  - id of source piece
	 * @param destId - id of destination
	 * @return true if the free move of source is successful
	 */
	public boolean freeMovePiece(int srcId, int destId) {
		if (srcId == destId)
			return false;

		int srcX = srcId % dim;
		int srcY = (srcId - srcX) / dim;
		int destX = destId % dim;
		int destY = (destId - destX) / dim;

		if (getTerrain(destX, destY) != 0) {
			return false;
		}

		Piece p = pGrid[srcX][srcY];
		p.setId(destId);
		pGrid[srcX][srcY] = null;
		pGrid[destX][destY] = p;
		return true;
	}
	
	

	/**
	 * @param srcId
	 * @param destId
	 * @return true if the ruled move of source piece is successful
	 */
	public boolean ruledMovePiece(int srcId, int destId) {
		if (srcId == destId)
			return false;

		int srcX = srcId % dim;
		int srcY = (srcId - srcX) / dim;
		int destX = destId % dim;
		int destY = (destId - destX) / dim;

		Piece p = pGrid[srcX][srcY];
		if (!p.canReach(destX, destY)) {
			return false;
		}

		p.setId(destId);
		pGrid[srcX][srcY] = null;
		pGrid[destX][destY] = p;
		updateAttack();
		p.setMoveCount(p.getMoveCount() + 1);
		return true;
	}
	
	
	
	/**
	 * @param srcId
	 * @return true id source is a castle
	 */
	public boolean isCastle(int srcId) {
		if (cList == null) {
			return false;
		}
		for (int i = 0; i < cList.size(); i++) {
			if (cList.get(i).getId() == srcId) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * @param player
	 * @return castle belong to player
	 */
	public Castle getCastleByPlayer(int player) {
		if (cList == null) {
			return null;
		} else {
			for (int i = 0; i < cList.size(); i++) {
				if (cList.get(i).getPlayer() == player)
					return cList.get(i);
			}
		}
		return null;
	}
	
	
	
	/**
	 * @param id
	 * @param type
	 * 
	 * set terrain at id to be type
	 */
	public void setTerrain(int id, int type){
		int cordX = id % dim;
		int cordY = (id - cordX) / dim;
		setTerrain(cordX, cordY, type);
	}
	
	
	/**
	 * @param x
	 * @param y
	 * @param type
	 * 
	 * set terrain at (x,y) to be type
	 */
	public void setTerrain(int x, int y, int type){
		terrain[x][y] = type;
	}
	
	
	
	/**
	 * @param id
	 * @return the terrain type at id
	 */
	public int getTerrain(int id){
		int cordX = id % dim;
		int cordY = (id - cordX) / dim;
		return getTerrain(cordX, cordY);
	}
	
	
	
	/**
	 * @param x
	 * @param y
	 * @return the terrain type at (x,y)
	 */
	public int getTerrain(int x, int y){
		return terrain[x][y];
	}
	
	
	
	/**
	 * @param c
	 * @param id
	 * @param entrance
	 * 
	 * adds castle c to position id with entrance code
	 */
	public void add(Castle c, int id, int entrance){
		int cordX = id % dim;
		int cordY = (id - cordX) / dim;
		add(c, cordX, cordY, entrance);
	}
	
	
	
	/**
	 * @param p
	 * @param id
	 * 
	 * adds piece p to position id
	 */
	public void add(Piece p, int id){
		int cordX = id % dim;
		int cordY = (id - cordX) / dim;
		add(p, cordX, cordY);
	}
	
	

	/**
	 * @param x
	 * @param y
	 * @return piece at (x,y)
	 */
	public Piece getPieceAt(int x, int y) {
		if (x >= dim || y >= dim || x < 0 || y < 0) {
			return null;
		}
		
		return pGrid[x][y];
	}
	
	
	
	/**
	 * @param id
	 * @return piece at id
	 */
	public Piece getPieceAt(int id){
		int cordX = id % dim;
		int cordY = (id - cordX) / dim;
		return getPieceAt(cordX, cordY);
	}
	
	
	
	public Piece getPieceAt(Point p){
		return getPieceAt(p.x, p.y);
	}
	
	
	
	public int getPlayerAt(Point p){
		Piece piece = getPieceAt(p);
		if (piece == null){
			return -1;
		}
		return piece.getPlayer();
	}
	
	

	/**
	 * clears all pieces on board and reset piece count
	 */
	public void clear() {
		pGrid = new Piece[dim][dim];
		pList.clear();
		pCount = 0;
	}

	
	
	/**
	 * @return dimension of board
	 */
	public int getDim() {
		return dim;
	}
	
	

	/**
	 * @return number of pieces on board
	 */
	public int getPiecesCount() {
		return pCount;
	}
	
	
	
	/**
	 * updates attack paths for all pieces on board
	 */
	public void updateAttack(){
		for (int i = 0; i < pList.size(); i++)
			pList.get(i).sketchAttackPaths();
	}
	
	
	
	/**
	 * @param id
	 * @return castle at id
	 */
	public Castle getCastle(int id) {
		int cordX = id % dim;
		int cordY = (id - cordX) / dim;
		return getCastle(cordX, cordY);
	}

	
	
	/**
	 * @param x
	 * @param y
	 * @return castle at (x,y)
	 */
	public Castle getCastle(int x, int y) {
		return cGrid[x][y];
	}

	
	
	/**
	 * reset move count for all pieces on board
	 */
	public void resetMoveCount(){
		for (int i = 0; i < pCount; i++){
			pList.get(i).setMoveCount(0);
		}
	}
	
	/**
	 * returns a string representation of the board
	 */
	@Override
	public String toString() {
		String output = new String();

		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				Piece printed = pGrid[i][j];
				if (printed == null)
					output = output + "*" + " ";
				else
					output = output + printed.getName() + " ";
			}
			output = output + "\n";
		}

		return output;
	}
}
