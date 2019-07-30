package Consumables;

import java.awt.Color;

import World.Consumable;

public class Power extends Consumable{

	public Power(int production, int max) {
		super(2, Color.YELLOW, "Power", production, max, 2, 10, true);
	}
	
}
