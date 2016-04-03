package componentLibrary;


public class Prince extends MountedPiece {
	
	private static final String NAME = "P";
	private static final String SPRITE_1 = "sprites/prince_1.png";
	private static final String SPRITE_2 = "sprites/prince_2.png";

	public Prince(int id, int t, Board parent) {
		super(id, t, false, parent);
		isRoyalty = true;
	}

	public Prince(int x, int y, int t, Board parent) {
		super(x, y, t, false, parent);
		isRoyalty = true;
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
