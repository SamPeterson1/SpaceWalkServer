package World;

import Consumables.Power;
import Items.Item;

public class Refinery{
	
	private Power power;
	private int unrefined;
	private int rate;
	private int maxRate;
	String producing;
	private int x;
	private int y;
	private Item item;
	
	public Refinery(int x, int y) {
		this.x = x;
		this.y = y;
		this.power = new Power(5, 100);
	}
	
	public void tick() {
		if(this.power.tick()) {
			this.rate = this.maxRate;
			System.out.println(this.unrefined);
			if(this.unrefined > rate) {
				this.unrefined -= rate;
			} else {
				this.rate = this.unrefined;
				this.unrefined = 0;
			}
		} else {
			this.rate = 0;
		}
	}
	
	public void setItem(Item i) {
		this.item = i;
		this.unrefined = i.getRefined().getMax();
		this.rate = i.getRefined().getProduction();
		this.maxRate = rate;
		this.power.setProduction(-i.getPowerReq());
		this.producing = i.getRefined().getName();
	}

	public Item getItem() {
		return this.item;
	}
	
	public String getProducing() {
		return this.producing;
	}
	
	public int getPower() {
		return this.power.getAmount();
	}
	
	public int getRate() {
		return this.rate;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
