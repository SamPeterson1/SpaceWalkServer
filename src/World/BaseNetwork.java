package World;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseNetwork {
	
	ArrayList<Building> buildings;
	HashMap<String, Consumable> consumables;
	
	public BaseNetwork() {
		this.buildings = new ArrayList<Building>();
		this.consumables = new HashMap<String, Consumable>();
	}
	
	public void transferResources() {
		for(Consumable c: this.consumables.values()) {
			this.transferResource(c.getName());
		}
	}
	
	public int getTotal(String name) {
		//System.out.println("yeeeeesm " + this.consumables.size());
		int production = 0;
		for(Building b: this.buildings) {
			if(b.hasConsumable(name) && b.linked() && b.isActive()) {
				production += b.getConsumable(name).getProduction();
			}
		}
		
		return production;
	}
	
	public double[] closestPoint(int x, int y) {
		double minDist = -1;
		int minX = 0;
		int minY = 0;
		for(Building b: this.buildings) {
			if(b.linked()) {
				int dx = Math.abs(b.getX()*32 - x);
				int dy = Math.abs(b.getY()*32 - y);
				double dist = Math.sqrt(dx*dx + dy*dy);
				if(dist < minDist || minDist == -1) {
					minDist = dist;
					minX = b.getX();
					minY = b.getY();
				}
			}
		}
		
		double[] retVal = {minX, minY, minDist};
		return retVal;
 	}
	
	public HashMap<String, Consumable> getConsumables() {
		return this.consumables;
	}
	
	public void transferResource(String name) {
		int surplus = 0;
		float leftOver = 0;
		float totalGiven = 0;
		int sumConsumption = 0;
		int numNonConsuming = 0;
		for(Building b: this.buildings) {
			Consumable c = b.getConsumable(name);
			if(b.linked()) {
				if(c.getProduction() >= 0) {
					surplus += c.getAmount();
					numNonConsuming ++;
				} else {
					sumConsumption -= c.getProduction();
				}
			}
		}
		
		for(Building b: this.buildings) {
			Consumable c = b.getConsumable(name);
			if(c.getProduction() < 0) {
				b.updateActive(c);
				int consumption = -(c.getProduction());
				float given = surplus*consumption/sumConsumption;
				float capped = given;
				if(capped > 5) capped = 5;
				totalGiven += capped;
				leftOver += c.addAmount((int)capped);
			}
		}
		
		for(Building b: this.buildings) {
			Consumable c = b.getConsumable(name);
			if(c.getProduction() >= 0) {
				c.addAmount((int) ((leftOver-totalGiven)/numNonConsuming));
				b.updateActive(c);
			}
		}
	}
	
	public ArrayList<Building> getBuildings() {
		return this.buildings;
	}
	
	public void addBuilding(Building b) {
		this.buildings.add(b);
		for(Consumable c: b.getConsumables()) {
			this.consumables.putIfAbsent(c.getName(), c);
		}
	}
	
}
