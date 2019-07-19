package World;

import java.awt.Color;

public class Consumable {
	
	int ID;
	int amount;
	int production;
	int max;
	String name;
	boolean tetherable;
	Color c;
	
	public Consumable(int ID, Color c, String name, int production, int max, boolean tetherable) {
		this.ID = ID;
		this.name = name;
		this.tetherable = tetherable;
		this.amount = 0;
		this.production = production;
		this.max = max;
		this.c = c;
	}
	
	public Color getColor() {
		return this.c;
	}
	
	public boolean tick() {
		boolean retVal = (this.setAmount(this.production + this.amount) == 0);
		System.out.println(this.production + " TIKEEEE" + retVal);
		return retVal;
	}
	
	public int setAmount(int amount) {
		if(amount <= this.max & amount >= 0) {
			this.amount = amount;
			return 0;
		} else if(amount < 0) {
			this.amount = 0;
			return -1;
		} else {
			this.amount = this.max;
			return amount - this.max;
		}
	}
	
	public int getMax() {
		return this.max;
	}
	
	public int removeAmount(int amount) {
		return this.setAmount(this.amount - amount);
	}
	
	public int addAmount(int amount) {
		return this.setAmount(this.amount + amount);
	}

	public int getProduction() {
		return this.production;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isTetherable() {
		return this.tetherable;
	}
	
}
