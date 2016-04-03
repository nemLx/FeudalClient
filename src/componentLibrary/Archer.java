package componentLibrary;

import java.awt.Point;
import java.util.ArrayList;

public class Archer extends Piece {

	private static final String NAME = "K";
	private static final String SPRITE_1 = "sprites/archer_1.png";
	private static final String SPRITE_2 = "sprites/archer_2.png";
	
	
	public Archer(int id, int team, Board parent) {
		super(id, team, 1, true, parent);
		isRoyalty = false;
		isSiegable = false;
	}
	
	
	public Archer(int x, int y, int team, Board parent) {
		super(x, y, team, 1, true, parent);
		isRoyalty = false;
		isSiegable = false;
	}


	@Override
	public void sketchAttackPaths() {
		rangedAttacks = new ArrayList<Point> (0);
		reachablePaths = new ArrayList<ArrayList<Point>> (0);
		
		ArrayList<Point> n = new ArrayList<Point> (0);
		ArrayList<Point> s = new ArrayList<Point> (0);
		ArrayList<Point> w = new ArrayList<Point> (0);
		ArrayList<Point> e = new ArrayList<Point> (0);
		ArrayList<Point> nw = new ArrayList<Point> (0);
		ArrayList<Point> ne = new ArrayList<Point> (0);
		ArrayList<Point> sw = new ArrayList<Point> (0);
		ArrayList<Point> se = new ArrayList<Point> (0);
		
		for (int offset = 1; offset <= 3; offset++){
			n.add(new Point(cordX, cordY-offset));
			s.add(new Point(cordX, cordY+offset));
			e.add(new Point(cordX+offset, cordY));
			w.add(new Point(cordX-offset, cordY));
			nw.add(new Point(cordX-offset, cordY-offset));
			ne.add(new Point(cordX+offset, cordY-offset));
			sw.add(new Point(cordX-offset, cordY+offset));
			se.add(new Point(cordX+offset, cordY+offset));
		}
		
		reachablePaths.add(n);
		reachablePaths.add(s);
		reachablePaths.add(w);
		reachablePaths.add(e);
		reachablePaths.add(nw);
		reachablePaths.add(ne);
		reachablePaths.add(sw);
		reachablePaths.add(se);
		
		verifyAttackPaths();
	}
	
	
	@Override
	public String getName() {
		return NAME;
	}

	
	@Override
	public String getSprite() {
		if (player == 1){
			return SPRITE_1;
		}else{
			return SPRITE_2;
		}
	}

}
