package Buildings;

import Consumables.Oxygen;
import Consumables.Power;
import World.Building;

public class Hub extends Building{

	public Hub() {
		super(4, 4, 1, true);
		super.addConsumable(new Oxygen(-1));
		super.addConsumable(new Power(-1));
	}
	
}
