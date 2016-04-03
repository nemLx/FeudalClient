package componentLibrary;

import gameEngine.Data;

import java.awt.Point;

/**
 * @author Nemo Li
 * 
 */
public class Castle {
	
	private int id;
	private int dim;
	private int cordX;
	private int cordY;
	private int entrance; // 1-west, increments clockwise
	
	private int player;
	private Board parentBoard;
	
	
	
	/**
	 * @param id
	 * @param player
	 * @param entrance
	 * @param parentBoard
	 */
	public Castle(int id, int player, int entrance, Board parentBoard) {
		this.dim = parentBoard.getDim();
		this.cordX = id % dim;
		this.cordY = (id - cordX) / dim;
		this.player = player;
		this.entrance = entrance;
		this.parentBoard = parentBoard;
		this.parentBoard.add(this, id, entrance);
	}

	
	/**
	 * @param x
	 * @param y
	 * @param player
	 * @param entrance
	 * @param parentBoard
	 */
	public Castle(int x, int y, int player, int entrance, Board parentBoard) {
		cordX = x;
		cordY = y;
		this.player = player;
		this.dim = parentBoard.getDim();
		this.entrance = entrance;
		this.parentBoard = parentBoard;
		this.parentBoard.add(this, x, y, entrance);
	}

	
	
	/**
	 * @param x
	 * @param y
	 * @return true if (x,y) is its green
	 */
	public boolean isGreen(int x, int y) {
		switch (entrance){
		case Data.DIR_WEST: //west
			return (x == cordX-1 && y == cordY);
		case Data.DIR_NORTH: //north
			return (x == cordX && y == cordY-1);
		case Data.DIR_EAST: //east
			return (x == cordX+1 && y == cordY);
		case Data.DIR_SOUTH: //south
			return (x == cordX && y == cordY+1);
		default:
			return false;
		}
	}
	
	
	
	public boolean isGreen(int id){
		int tempX = id % dim;
		int tempY = (id - tempX) / dim;
		
		return isGreen(tempX, tempY);
	}

	
	
	/**
	 * @return a point with coordinates of the green
	 */
	public Point getEntrancePoint() {
		
		switch (entrance){
		case Data.DIR_WEST: //west
			return new Point(cordX-1, cordY);
		case Data.DIR_NORTH: //north
			return new Point(cordX, cordY-1);
		case Data.DIR_EAST: //east
			return new Point(cordX+1, cordY);
		case Data.DIR_SOUTH: //south
			return new Point(cordX, cordY+1);
		default:
			return null;
		}
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
	 * @return player
	 */
	public int getPlayer() {
		return player;
	}
	
	
	
	/**
	 * @return id
	 */
	public int getId(){
		return id;
	}

	
	
	/**
	 * @return entrance code
	 */
	public int getEntrance() {
		return entrance;
	}
	
	
	/**
	 * @return x
	 */
	public int getX(){
		return cordX;
	}
	
	
	
	/**
	 * @return y
	 */
	public int getY(){
		return cordY;
	}
	
}
