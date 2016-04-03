package componentLibrary;

import java.awt.Point;
import java.util.ArrayList;

public class Squire extends Piece {

	private static final String NAME = "S";
	private static final String SPRITE_1 = "sprites/squire_1.png";
	private static final String SPRITE_2 = "sprites/squire_2.png";
	
	public Squire(int id, int team, Board parent) {
		super(id, team, 1, false, parent);
		isRoyalty = false;
		isSiegable = false;
	}
	
	public Squire(int x, int y, int team, Board parent) {
		super(x, y, team, 1, false, parent);
		isRoyalty = false;
		isSiegable = false;
	}
	
	@Override
	public void sketchAttackPaths() {
		reachablePaths = new ArrayList<ArrayList<Point>> (0);
		
		ArrayList<Point> nw = new ArrayList<Point> (0);
		ArrayList<Point> ne = new ArrayList<Point> (0);
		ArrayList<Point> sw = new ArrayList<Point> (0);
		ArrayList<Point> se = new ArrayList<Point> (0);
		ArrayList<Point> wn = new ArrayList<Point> (0);
		ArrayList<Point> ws = new ArrayList<Point> (0);
		ArrayList<Point> en = new ArrayList<Point> (0);
		ArrayList<Point> es = new ArrayList<Point> (0);
		
		for (int offset = 1; offset <=2; offset++){
			nw.add(new Point(cordX, cordY-offset));
			ne.add(new Point(cordX, cordY-offset));
			sw.add(new Point(cordX, cordY+offset));
			se.add(new Point(cordX, cordY+offset));
			wn.add(new Point(cordX-offset, cordY));
			ws.add(new Point(cordX-offset, cordY));
			en.add(new Point(cordX+offset, cordY));
			es.add(new Point(cordX+offset, cordY));
		}
		
		nw.add(new Point(cordX-1, cordY-2));
		ne.add(new Point(cordX+1, cordY-2));
		sw.add(new Point(cordX-1, cordY+2));
		se.add(new Point(cordX+1, cordY+2));
		wn.add(new Point(cordX-2, cordY-1));
		ws.add(new Point(cordX-2, cordY+1));
		en.add(new Point(cordX+2, cordY-1));
		es.add(new Point(cordX+2, cordY+1));
		
		reachablePaths.add(nw);
		reachablePaths.add(ne);
		reachablePaths.add(sw);
		reachablePaths.add(se);
		reachablePaths.add(wn);
		reachablePaths.add(ws);
		reachablePaths.add(en);
		reachablePaths.add(es);
		
		verifyAttackPaths();
	}

	@Override
	protected void verifyAttackPaths() {
		ArrayList<ArrayList<Point>> masterCopy = new ArrayList<ArrayList<Point>> (0);
		
		for (int i = 0; i < 8; i++){
			ArrayList<Point> curr = reachablePaths.get(i);
			ArrayList<Point> copy = new ArrayList<Point> (0);
			
			for (int j = 0; j < curr.size(); j++){
				Point p = curr.get(j);
				
				if (p.x >= dim || p.x <0 || p.y >= dim || p.y < 0){
					break;
				}
				
				int terrain = parentBoard.getTerrain(p.x, p.y);
				
				if (terrain >= durability){
					Castle c = parentBoard.getCastleByPlayer(3-player);
					if (c!=null && (c.isGreen(cordX, cordY) || c.isGreen(p.x, p.y))){
						if (j==curr.size()-1){
							copy.add(p);
						}
					}
					break;
				}
				
				Piece target = parentBoard.getPieceAt(p.x, p.y);
				
				if ( target != null ){
					if ( player != target.getPlayer() && j==curr.size()-1){
						copy.add(p);
					}
					break;
				}
				
				if (j == curr.size()-1){
					copy.add(p);	
				}
			}
			masterCopy.add(copy);
		}
		reachablePaths = masterCopy;
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
