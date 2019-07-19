package Consumables;

import java.awt.Color;

import World.Consumable;

public class Power extends Consumable{

	public Power(int production) {
		super(2, Color.YELLOW, "Power", production, 1000, true);
	}
	
}
