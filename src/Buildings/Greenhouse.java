package Buildings;

import Consumables.Oxygen;
import Consumables.Power;
import Consumables.Wood;
import World.Building;

public class Greenhouse extends Building {
	
	public Greenhouse() {
		
		super(3, 2, 2, false);
		super.addConsumable(new Power(4, 100));
		super.addConsumable(new Oxygen(10, 500));
		super.addConsumable(new Wood(0, 100));
		
	}
	
}
