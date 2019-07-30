package Items;

import World.Consumable;

public class Item {
	
	protected int ID;
	private int powerReq;
	private Consumable refined;
	
	public Item(int ID, Consumable refined, int powerReq) {
		this.ID = ID;
		this.refined = refined;
		this.powerReq = powerReq;
	}
	
	public int getPowerReq() {
		return this.powerReq;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public Consumable getRefined() {
		return this.refined;
	}
}
