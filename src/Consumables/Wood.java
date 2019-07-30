package Consumables;

import java.awt.Color;

import World.Consumable;

public class Wood extends Consumable{
	//int ID, Color c, String name, int production, int max, int consequense, int val, boolean tetherable
	public Wood(int production, int max) {
		super(4, new Color(40, 26, 13), "Wood", production, max, 0, 0, false);
	}
	
}
