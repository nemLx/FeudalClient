package componentLibrary;

import gameEngine.Data;

import java.awt.Point;
import java.util.ArrayList;


/**
 * @author Nemo Li 
 */
public abstract class Piece {

	protected int id;
	protected int dim;
	protected int cordX;
	protected int cordY;
	
	protected int player;		// 1-A or 2-B
	protected int durability;	// 0 or 1, 0 is mounted, 1 is footman
	protected int moveCount;	// how many times it is moved in a turn
	
	protected boolean isRanged;
	protected boolean isRoyalty;
	protected boolean isSiegable;
	
	protected ArrayList<Point> rangedAttacks;
	protected ArrayList<ArrayList<Point>> reachablePaths;
	protected Board parentBoard;
	

	/**
	 * @return name of piece
	 */
	public abstract String getName();

	

	/**
	 * @return sprite of piece
	 */
	public abstract String getSprite();
	
	
	
	/**
	 * sketches all full paths, needs to be refined 
	 */
	public abstract void sketchAttackPaths();
	
	
	
	/**
	 * @param id
	 * @param player
	 * @param durability
	 * @param isRanged
	 * @param parentBoard
	 */
	public Piece(int id, int player, int durability, boolean isRanged,
			Board parentBoard) {
		this.player = player;
		this.dim = parentBoard.getDim();
		this.moveCount = 0;
		this.isRanged = isRanged;
		this.durability = durability;
		this.parentBoard = parentBoard;
		this.parentBoard.add(this, id);
	}
	
	

	/**
	 * @param x
	 * @param y
	 * @param player
	 * @param durability
	 * @param isRanged
	 * @param parentBoard
	 */
	public Piece(int x, int y, int player, int durability, boolean isRanged,
			Board parentBoard) {
		this.player = player;
		this.dim = parentBoard.getDim();
		this.moveCount = 0;
		this.isRanged = isRanged;
		this.durability = durability;
		this.parentBoard = parentBoard;
		this.parentBoard.add(this, x, y);
	}
	
	
	
	/**
	 * @param x
	 * @param y
	 * @return if this piece could attack (x,y)
	 */
	public boolean canAttack(int x, int y){
		if (isRanged) {		// for ranged piece
			if (rangedAttacks == null) {
				return false;
			}

			for (int i = 0; i < rangedAttacks.size(); i++) {
				Point comp = rangedAttacks.get(i);
				if (comp.x == x && comp.y == y)
					return true;
			}

			return false;
		} else {	// for not ranged, attack is reach
			return canReach(x, y);
		}
	}
	
	
	
	/**
	 * @param x
	 * @param y
	 * @return true if this piece could reach (x,y)
	 */
	public boolean canReach(int x, int y) {
		ArrayList<Point> path = getReachablePoint();
		
		if (path == null){
			return false;
		}
		
		for (int i = 0; i < path.size(); i++) {
			if (path.get(i).x == x && path.get(i).y == y)
				return true;
		}
		return false;
	}
	
	
	
	/**
	 * @return list of points reachable by this piece
	 */
	public ArrayList<Point> getReachablePoint() {
		ArrayList<Point> reachable = new ArrayList<Point>(0);

		for (int i = 0; i < reachablePaths.size(); i++) {
			ArrayList<Point> curr = reachablePaths.get(i);
			reachable.addAll(curr);
		}

		if (reachable.size() < 1) {
			return null;
		}
		return reachable;
	}

	

	/**
	 * @return a list of 1-d id of reachable points
	 */
	public ArrayList<Integer> getReachableId() {
		ArrayList<Point> points2d = getReachablePoint();
		ArrayList<Integer> points1d = new ArrayList<Integer>(0);

		if (points2d == null) {
			return null;
		}

		for (int i = 0; i < points2d.size(); i++) {
			Point curr = points2d.get(i);
			int temp = curr.x + Data.DIMENSION * curr.y;
			points1d.add(temp);
		}
		return points1d;
	}
	
	
	public ArrayList<ArrayList<Point>> getReachablePaths() {
		return reachablePaths;
	}
	
	public ArrayList<Point> getRangedAttacks() {
		return rangedAttacks;
	}
	
	
	public boolean canReach(int id) {
		int cordX = id % dim;
		int cordY = (id - cordX) / dim;
		return this.canReach(cordX, cordY);
	}

	public boolean canAttack(int id) {
		int cordX = id % dim;
		int cordY = (id - cordX) / dim;
		return this.canAttack(cordX, cordY);
	}
	
	
	/**
	 * @param n
	 * @return non-negative value if x, y, and id are set
	 */
	public int setId(int n) {
		int tempX = n % dim;
		int tempY = (n - tempX) / dim;
		
		if (setCords(tempX, tempY)<0)
			return -1;
		
		return id = n;
	}
	
	
	/**
	 * @param x
	 * @param y
	 * @return non-negative value if both x and y are set
	 */
	public int setCords(int x, int y) {
		if (setX(x) < 0 || setY(y) < 0)
			return -1;
		
		return id = x + dim * y;
	}


	/**
	 * @param x
	 * @return non-negative value if set x coordinate
	 */
	public int setX(int x) {
		if (x < 0 || x > dim)
			return -1;
		id = x+cordY*dim;
		return cordX = x;
	}

	
	
	/**
	 * @param y
	 * @return non-negative value if set y coordinate
	 */
	public int setY(int y) {
		if (y < 0 || y > dim)
			return -1;
		id = cordX+y*dim;
		return cordY = y;
	}

	
	
	/**
	 * @return parent board's dimension
	 */
	public int getParentBoardDim() {
		return dim;
	}

	
	
	/**
	 * @return player
	 */
	public int getPlayer() {
		return player;
	}
	
	
	
	/**
	 * @return x coordinate
	 */
	public int getX() {
		return cordX;
	}

	
	
	/**
	 * @return y coordinate
	 */
	public int getY() {
		return cordY;
	}
	
	

	/**
	 * @return id of a piece
	 */
	public int getId() {
		return id;
	}
	
	
	
	/**
	 * @return isRanged
	 */
	public boolean isRanged(){
		return isRanged;
	}

	
	
	/**
	 * @return moveCount
	 */
	public int getMoveCount(){
		return moveCount;
	}
	
	
	
	public Board getBoard(){
		return parentBoard;
	}
	
	
	
	/**
	 * @param
	 */
	public void setMoveCount(int x){
		moveCount = x;
	}
	
	

	/**
	 * @return isRoyalty
	 */
	public boolean isRoyal() {
		return isRoyalty;
	}
	
	
	/**
	 * refines sketched attack paths, removes all
	 * unreachable places added by simple pattern matching
	 */
	protected void verifyAttackPaths() {
		if (parentBoard.isCastle(id)) {
			siegeMode();
			return;
		}
		ArrayList<ArrayList<Point>> masterCopy = new ArrayList<ArrayList<Point>>(0);

		for (int i = 0; i < 8; i++) {
			ArrayList<Point> curr = reachablePaths.get(i);
			ArrayList<Point> copy = new ArrayList<Point>(0);
			for (int j = 0; j < curr.size(); j++) {
				if (checkPosition(curr, copy, j)) {
					break;
				}
			}
			masterCopy.add(copy);
		}
		reachablePaths = masterCopy;
	}



	/**
	 * @param curr 		- the current list of positions treading
	 * @param copy		- the list of reachable positions to copy to
	 * @param currIndex - the current index being examined
	 * @return true if cannot move further
	 */
	private boolean checkPosition(ArrayList<Point> curr, ArrayList<Point> copy, int currIndex) {
		Point p = curr.get(currIndex);
		if (p.x >= dim || p.x < 0 || p.y >= dim || p.y < 0) { // out of bounds
			return true;
		}

		int terrain = parentBoard.getTerrain(p.x, p.y);
		if (terrain > durability) {
			if (isEnteringCastle(p))
				copy.add(p);
			return true; // castle or not, must stop
		} else if (terrain == durability && durability != 0) {
			copy.add(p); // could go onto but stopped
			return true;
		}

		Piece target = parentBoard.getPieceAt(p.x, p.y);
		if (target != null) {
			handlePiece(copy, p, target);
			return true; // stop by any piece
		}
		copy.add(p);
		return false;
	}



	/**
	 * @param copy	 - current list of positions treading
	 * @param p		 - current piece at the position examined
	 * @param target - target piece
	 */
	private void handlePiece(ArrayList<Point> copy, Point p, Piece target) {
		if (player != target.getPlayer()) {
			if (isRanged) {
				rangedAttacks.add(p);
			} else {
				copy.add(p);
			}
		}
	}
	
	
	
	/**
	 * @param p
	 * @return true if piece could move to this point
	 * 			given that it is rough terrain
	 */
	private boolean isEnteringCastle(Point p){
		Castle c1 = parentBoard.getCastle(p.x, p.y);
		
		if (c1 != null){	// there is a castle
			if (c1.isGreen(cordX, cordY)){ // standing on green
				if (isSiegable){
					return true;
				}else if(c1.getPlayer()!=player){
					return true; // if not siegable, only could go into enemy
				}
			}else{
				return false; // not standing on green, cannot go
			}
		}else{	// no castle, but could be a green
			Castle c2 = parentBoard.getCastleByPlayer(Data.PLAYER_A);
			Castle c3 = parentBoard.getCastleByPlayer(Data.PLAYER_B);
			if (c2 != null && c2.isGreen(p.x, p.y)){
				return true;
			}else if (c3 != null && c3.isGreen(p.x, p.y)){
				return true;
			}
		}
		return false;
	}
	
	

	/**
	 * handles the verify path when the piece is in castle
	 */
	private void siegeMode() {
		ArrayList<Point> single = new ArrayList<Point>(0);
		single.add(parentBoard.getCastle(id).getEntrancePoint());
		reachablePaths = new ArrayList<ArrayList<Point>>(0);
		reachablePaths.add(single);
		if (isRanged) {
			rangedAttacks.add(single.get(0));
		}
	}
}
