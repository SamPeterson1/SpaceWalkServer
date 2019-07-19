package World;

import java.awt.Image;

public class Production {
	
	protected Consumable resource;
	protected int speed;
	private Image img;
	
	public Production(Consumable resource, int speed, Image img) {
		this.resource = resource;
		this.speed = speed;
		this.img = img;
	}
	
	public Consumable getResource() {
		return this.resource;
	}
	
	public int getSpeed() {
		return this.getSpeed();
	}
	
	public Image getImage() {
		return this.img;
	}
}
