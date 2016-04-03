package componentLibrary;

public class Duke extends MountedPiece {
	
	private static final String NAME = "D";
	private static final String SPRITE_1 = "sprites/duke_1.png";
	private static final String SPRITE_2 = "sprites/duke_2.png";
	
	
	public Duke(int id, int t, Board parent) {
		super(id, t, false, parent);
		isRoyalty = true;
	}

	public Duke(int x, int y, int t, Board parent) {
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
