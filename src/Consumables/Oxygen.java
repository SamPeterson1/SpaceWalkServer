package Consumables;

import java.awt.Color;

import World.Consumable;

public class Oxygen extends Consumable{
	
	public Oxygen(int production, int max) {
		super(1, Color.CYAN, "Oxygen", production, max, 5, 0, true);
	}
	
}
