package World;

import java.util.ArrayList;
import java.util.HashMap;

public class Tether {

	private static int id_counter;
	public static int MAX_DIST = 160;
	private static int O2_RATE = 4;
	private static int POWER_RATE = 2;
	
	private int x;
	private int y;
	private int state;
	private int id;
	private int oxygenRate = 0;
	private int powerRate = 0;
	HashMap<Integer, Tether> connected;

	
	public Tether(int x, int y) {
		this.id = Tether.id_counter;
		if(x != -1 && y != -1) 
			Tether.id_counter ++;
		this.connected = new HashMap<Integer, Tether>();
		this.x = x;
		this.y = y;
	}
	
	public HashMap<Integer, Tether> getConnected() {
		return this.connected;
	}
	
	public void setOxygenRate(int rate) { 
		this.oxygenRate = rate > Tether.O2_RATE ? Tether.O2_RATE : rate;
	}
	
	public void setPowerRate(int rate) {
		this.powerRate = rate > Tether.POWER_RATE ? Tether.POWER_RATE : rate;
	}
	
	public int getOxygenRate() {
		return this.oxygenRate;
	}
	
	public int getPowerRate() {
		return this.powerRate;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return this.state;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getID() {
		return this.id;
	}
	
	public void addConnection(Tether t) {
		this.connected.put(t.getID(), t);
	}
}
