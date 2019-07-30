package Consumables;

import java.awt.Color;

import World.Consumable;

public class Health extends Consumable {
	
	public Health(int production, int max) {
		super(3, Color.RED, "Health", production, max, 0, max, true);
	}
	
}
