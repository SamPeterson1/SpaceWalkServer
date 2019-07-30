package World;

import java.util.ArrayList;

public class BaseNetwork {
	
	ArrayList<Building> buildings;
	String[] consumables = {"Oxygen", "Power", "Wood"};
	ArrayList<Refinery> refineries; 
	
	public BaseNetwork() {
		this.buildings = new ArrayList<Building>();
		this.refineries = new ArrayList<Refinery>();
	}
	
	public void transferResources() {
		for(String name: consumables) {
			this.transferResource(name);
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
	
	public void transferResource(String name) {
		int surplus = 0;
		float leftOver = 0;
		float totalGiven = 0;
		int sumConsumption = 0;
		int numNonConsuming = 0;
		int refinerySurplus = 0;
		for(Building b: this.buildings) {
			if(b.hasConsumable(name)) {
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
		}
		
		for(Refinery r: this.refineries) {
			if(r.getProducing().equals(name)) {
				r.tick();
				sumConsumption -= r.getRate();
				refinerySurplus += r.getRate();
			}
		}
		
		boolean distributed = false;
		for(Building b: this.buildings) {
			if(b.hasConsumable(name)) {
				Consumable c = b.getConsumable(name);
				if(c.getProduction() < 0) {
					distributed = true;
					b.updateActive(c);
					int consumption = -(c.getProduction());
					float given = surplus*consumption/sumConsumption;
					float capped = given;
					if(capped > 5) capped = 5;
					totalGiven += capped;
					leftOver += c.addAmount((int)capped);
				}
			}
		}
		
		for(Building b: this.buildings) {
			if(b.hasConsumable(name)) {
				Consumable c = b.getConsumable(name);
				if(c.getProduction() >= 0) {
					float blah = leftOver-totalGiven;
					if(!distributed) blah = refinerySurplus;
					System.out.println(distributed + " " + blah);
					c.addAmount((int) (blah/numNonConsuming));
					b.updateActive(c);
				}
			}
		}
	}
	
	public ArrayList<Refinery> getRefineries() {
		return this.refineries;
	}
	
	public ArrayList<Building> getBuildings() {
		return this.buildings;
	}
	
	public void addRefinery(Refinery r) {
		this.refineries.add(r);
	}
	
	public void addBuilding(Building b) {
		this.buildings.add(b);
	}
	
}
