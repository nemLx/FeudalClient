package componentLibrary;

public class Knight extends MountedPiece {

	private static final String NAME = "K";
	private static final String SPRITE_1 = "sprites/knight_1.png";
	private static final String SPRITE_2 = "sprites/knight_2.png";
	
	
	public Knight(int id, int t, Board parent) {
		super(id, t, false, parent);
		isRoyalty = false;
	}

	public Knight(int x, int y, int t, Board parent) {
		super(x, y, t, false, parent);
		isRoyalty = false;
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
