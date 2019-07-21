package World;

import java.util.ArrayList;
import java.util.HashMap;

public class TetherLine {
	
	private ArrayList<Tether> tethers;
	private BaseNetwork network;
	private boolean linked;
	private int state;
	private volatile int connectX;
	private volatile int connectY;
	private int oxygenRate;
	private int powerRate;
	
	public TetherLine(BaseNetwork network) {
		this.network = network;
		this.tethers = new ArrayList<Tether>();
		this.connectX = -2;
		this.connectY = -2;
		this.state = 0;
		this.linked = false;
	}
	
	public void updateLinked() {
		if(!this.linked) {
			for(Tether t: this.getTethers()) {
				double dist = network.closestPoint(t.getX(), t.getY())[2];
				if(dist < Tether.MAX_DIST) {
					this.linked = true;
					return;
				}
			}
		}
	}
	
	
	public int getOxygenRate() {
		return this.oxygenRate;
	}
	
	public int getPowerRate() {
		return this.powerRate;
	}
	
	public int getState() {
		return this.state;
	}
	
	public void updateState() {
		this.state = 0;
		//System.out.println("):");
		if(this.linked) {
			if(network.getTotal("Oxygen") > 0) {
				this.state ++;
				this.oxygenRate = network.getTotal("Oxygen");
			}
			if(network.getTotal("Power") > 0 && this.state == 1) {
				this.powerRate = network.getTotal("Power");
				this.state ++;
			}
		}
		for(Tether t: this.tethers) {
			t.setState(this.state);
			t.setOxygenRate(this.oxygenRate);
			t.setPowerRate(this.powerRate);
		}
	}
	
	public void addLine(TetherLine l) {
		for(Tether t: l.getTethers()) {
			this.tethers.add(t);
		}
		this.updateLinked();
	}
	
	public int getConnectedX() {
		//System.out.println(this.connectX);
		return this.connectX;
	}
	
	public int getConnectedY() {
		//System.out.println(this.connectY);
		return this.connectY;
	}
	
	public synchronized boolean addTether(Tether t) {
		boolean retVal = false;
		if(this.tethers.size() == 0) {
			this.tethers.add(t);
			this.attemptConnection(t.getX(), t.getY());
			return false;
		} else {
			for(Tether t2: (ArrayList<Tether>)this.tethers.clone()) {
				int dx = Math.abs(t.getX() - t2.getX());
				int dy = Math.abs(t.getY() - t2.getY());
				if(dx < Tether.MAX_DIST && dy < Tether.MAX_DIST) {
					double accuDist = Math.sqrt(dx*dx + dy*dy);
					if(accuDist < Tether.MAX_DIST) {
						System.out.println("YOU TOE YOU" + dx + " " + dy);
						if(!retVal) {
							this.tethers.add(t);
							this.attemptConnection(t.getX(), t.getY());
						}
						t.addConnection(t2);
						retVal = true;
					}
				}
			}
		}
		
		return retVal;
	}
	
	public void attemptConnection(int x, int y) {
		if(!this.linked) {
			double dist = network.closestPoint(x, y)[2];
			if(dist < Tether.MAX_DIST) {
				this.linked = true;
			}
		}
	}
	
	public ArrayList<Tether> getTethers() {
		return this.tethers;
	}
}
