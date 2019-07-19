package World;

import java.awt.Image;

import javax.swing.ImageIcon;

import Items.Item;

public class Resource {
	
	Item drop;
	int ID;
	
	public Resource(Item drop, int ID) {
		this.drop = drop;
		this.ID = ID;
	}
	
	public Item getDrop() {
		return this.drop;
	}
	
	public int getID() {
		return this.ID;
	}
}
