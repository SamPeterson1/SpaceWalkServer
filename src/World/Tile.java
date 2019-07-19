package World;

import java.awt.Image;
import java.util.ArrayList;

public class Tile {
	
	protected boolean habitable;
	protected Image img;
	protected int ID;
	protected boolean animated;
	protected ArrayList<Image> sprites;
	
	public Tile(String tileSet, int ID, boolean habitable) {
		
		this.habitable = habitable;
		this.ID = ID;
		
	}

	public boolean isHabitable() {
		return habitable;
	}
	
	public Image getImg() {
		return img;
	}

	public int getID() {
		return ID;
	}
}
