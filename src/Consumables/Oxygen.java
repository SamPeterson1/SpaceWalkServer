package Consumables;

import java.awt.Color;

import World.Consumable;

public class Oxygen extends Consumable{
	
	public Oxygen(int production) {
		super(1, Color.CYAN, "Oxygen", production, 100, true);
	}
	
}
