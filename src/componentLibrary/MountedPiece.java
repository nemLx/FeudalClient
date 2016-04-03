package componentLibrary;

import java.awt.Point;
import java.util.ArrayList;

public abstract class MountedPiece extends Piece {
	
	@Override
	public abstract String getName();

	@Override
	public abstract String getSprite();
	
	
	public MountedPiece(int id, int team, boolean isRanged, Board parent) {
		super(id, team, 0, isRanged, parent);
		isSiegable = true;
	}

	public MountedPiece(int x, int y, int team, boolean isRanged, Board parent) {
		super(x, y, team, 0, isRanged, parent);
		isSiegable = true;
	}
	

	@Override
	public void sketchAttackPaths() {
		reachablePaths = new ArrayList<ArrayList<Point>> (0);
		
		ArrayList<Point> n = new ArrayList<Point> (0);
		ArrayList<Point> s = new ArrayList<Point> (0);
		ArrayList<Point> w = new ArrayList<Point> (0);
		ArrayList<Point> e = new ArrayList<Point> (0);
		ArrayList<Point> nw = new ArrayList<Point> (0);
		ArrayList<Point> ne = new ArrayList<Point> (0);
		ArrayList<Point> sw = new ArrayList<Point> (0);
		ArrayList<Point> se = new ArrayList<Point> (0);
		
		for (int offset = 1; offset <= dim; offset++){
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

}
