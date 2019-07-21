package World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import Buildings.Greenhouse;
import Buildings.Hub;

public class World {
	
	private int[][] tiles;
	private int[][] resources;
	private int[][] buildingsLayer;
	private int[] buildingIDs = {1, 2};
	private int size;
	private HashMap<String, HashMap<Integer, Vector<Integer>>> tileDeltas;
	private HashMap<String, Player> players;
	//private HashMap<Integer, Vector<Integer>> tethers;
	private ArrayList<TetherLine> tethers;
	//0: nothing
	//1: oxygen flowing
	//2: oxygen and power flowing
	private HashMap<Integer, Resource> resourceTypes;
	private ArrayList<Building> buildings;
	private BuildingFactory buildingFactory;
	private ResourceFactory resourceFactory;
	private BaseNetwork network;
	private int connectIndex = 0;
	private Building[] connect = new Building[2];
	private int tetherDist = 320;
	
	public static World grassWorld(int size) {
		int[][] tiles = new int[size][size];
		for(int i = 0; i < size; i ++) {
			for(int j = 0; j < size; j ++) {
				if(i == 5 && j == 62) {
					tiles[i][j] = 2;
				} else {
					if(i == 0 && j == 0) {
						tiles[i][j] = 2;
					} else {
						tiles[i][j] = 1;
					}
				}
			}
		}
		System.out.println("oof");
		int[][] resources = new int[size][size];
		resources[4][4] = 1;
		return new World(tiles, resources);
	}
	
	public World(int[][] tiles, int[][] resources) {

		this.tileDeltas = new HashMap<String, HashMap<Integer, Vector<Integer>>>();
		this.players = new HashMap<String, Player>();
		this.network = new BaseNetwork();
		this.resources = resources;
		this.tiles = tiles;
		//this.tethers = new HashMap<Integer, Vector<Integer>>();
		this.tethers = new ArrayList<TetherLine>();
		this.resourceTypes = new HashMap<Integer, Resource>();
		this.buildings = new ArrayList<Building>();
		this.size = tiles.length;
		this.buildingsLayer = new int[this.size][this.size];
		this.buildingFactory = new BuildingFactory("Buildings");
		this.resourceFactory = new ResourceFactory("Resources");
		
		for(int i = 0; i < this.size; i ++) {
			for(int ii = 0; ii < this.size; ii ++) {
				if(!resourceTypes.containsKey(Integer.valueOf(resources[i][ii])) && resources[i][ii] != 0)
					resourceTypes.put(Integer.valueOf(resources[i][ii]), resourceFactory.getTile(resources[i][ii]));
			}
		}
		
	}
	
	public Tether nearestTether(int x, int y) {
		double minDist = -1;
		Tether retVal = null;
		for(TetherLine line: this.tethers) {
			for(Tether t: line.getTethers()) {
				int dx = Math.abs(x - t.getX());
				int dy = Math.abs(y - t.getY());
				double dist = Math.sqrt(dx*dx + dy*dy);
				if((dist < minDist || minDist == -1) && dist < Tether.MAX_DIST) {
					minDist = dist;
					retVal = t;
				}
			}
		}
		
		return retVal;
	}
	
	public void update() {
		for(TetherLine line: this.tethers) {
			line.updateState();
			//System.out.println("X: " + line.getConnectedX());
			//System.out.println("Y: " + line.getConnectedY());
			//System.out.println("State: " + line.getState());
		}
	}
	
	public void addTether(int x, int y) {
		//System.out.println(this.tethers.size());
		Tether t = new Tether(x, y);
		ArrayList<TetherLine> lines = new ArrayList<TetherLine>();
		for(TetherLine line: this.tethers) {
			if(line.addTether(t)) lines.add(line);
		}
		if(lines.size() == 0) {
			TetherLine line = new TetherLine(this.network);
			line.addTether(t);
			this.tethers.add(line); 
		} else if(lines.size() > 1){
			TetherLine next = new TetherLine(this.network);
			for(TetherLine line: lines) {
				this.tethers.remove(line);
				next.addLine(line);
			}
			this.tethers.add(next);
		}
	}
	
	public void connectBuilding(int x, int y) {
		if(this.buildingExsits(x, y)) {
			if(!((this.connectIndex == 1) && (this.connect[0].equals(this.getBuiding(x, y))))) {
				this.connect[this.connectIndex] = this.getBuiding(x, y);
				System.out.println(this.connectIndex + " " + this.connect[this.connectIndex].getID());
				if(this.connectIndex == 1) {
					this.connect[0].connectTo(this.connect[1]);
					this.connect[1].connectTo(this.connect[0]);
					
					this.connectIndex = -1;
				}
				
				this.connectIndex ++;
			}
		}
		for(TetherLine line: this.tethers) {
			line.updateLinked();
		}
	}
	
	public ArrayList<TetherLine> getTethers() {
		return this.tethers;
	}
	
	public int[] getBuildingIDs() {
		return this.buildingIDs;
	}
	
	public void resetDeltas(String id) {
		this.tileDeltas.replace(id, new HashMap<Integer, Vector<Integer>>());
	}
	
	public HashMap<Integer, Vector<Integer>> getDeltas(String id) {
		return this.tileDeltas.get(id);
	}

	public HashMap<String, Player> getPlayers() {
		return this.players;
	}
	
	public Player getPlayer(String ID) {
		return this.players.get(ID);
	}
	
	public void addPlayer(Player player, String ID) {
		System.out.println("PLAYER " + ID + " PUT!");
		this.players.put(ID, player);
		this.tileDeltas.put(ID, new HashMap<Integer, Vector<Integer>>());
	}
	
	public void addToDeltas(int x, int y, int ID) {
		for(String playerID: this.tileDeltas.keySet()) {
			Vector<Integer> pos;
			if(this.tileDeltas.containsKey(ID)) {
				pos = this.tileDeltas.get(playerID).get(ID);
			} else {
				pos = new Vector<Integer>();
			}
			pos.add(y);
			pos.add(x);
			this.tileDeltas.get(playerID).put(ID, pos);
		}
	}
	
	public void setID(int x, int y, int ID) {
		this.tiles[x][y] = ID;
		this.addToDeltas(x, y, ID);
	}
	
	public int getID(int x, int y) {
		return this.tiles[y][x];
	}
	
	public boolean buildingExsits(int x, int y) {
		return this.buildingsLayer[y][x] != 0;
	}
	
	public int getResourceID(int i, int j) {
		return this.resources[i][j];
	}
	
	public Building getBuiding(int x, int y) {
		return this.buildings.get(this.buildingsLayer[y][x]-1);
	}
	
	public void distributeResources() {
		this.network.transferResources();
	}
	
	public void placeBuilding(int ID, int x, int y) {
		
		Building b = this.buildingFactory.getBuilding(ID);
		b.setX(x);
		b.setY(y);
		this.buildings.add(b);
		for(int i = y; i < y+b.getHeight(); i ++) {
			for(int ii = x; ii < x+b.getWidth(); ii ++) {
				System.out.println("HI");
				this.buildingsLayer[i][ii] = this.buildings.size();
			}
		}
		this.network.addBuilding(b);
	}
	
	public ArrayList<Building> getBuildings() {
		return this.buildings;
	}
	
	
	
	/*
	public HashMap<Integer, Vector<Integer>> getTethers() {
		return this.tethers;
	}
	
	public void placeTether(int x, int y) {
		Vector<Integer> v = new Vector<Integer>();
		v.add(Integer.valueOf(x));
		v.add(Integer.valueOf(y));
		this.tethers.put(tethers.size(), v);
		this.updateConnections();
	}
	
	private void updateConnections() {
		
		int x = tethers.get(tethers.size()-1).get(0);
		int y = tethers.get(tethers.size()-1).get(1);
		Vector<Integer> connectedIDs = tethers.get(tethers.size()-1);
		
		for(int i = 0; i < this.tethers.size(); i ++) {
			int x2 = this.tethers.get(Integer.valueOf(i)).get(0);
			int y2 = this.tethers.get(Integer.valueOf(i)).get(1);
			int[] pos1 = Camera.worldToScreen(x, y, this.tetherDist, 1);
			int[] pos2 = Camera.worldToScreen(x2, y2, this.tetherDist, 1);
			if(Math.abs(pos2[0]-pos1[0]) <= this.tetherDist && Math.abs(pos2[1]-pos1[1]) <= this.tetherDist) {
				System.out.println("HIII");
				if(dist(pos1[0], pos1[1], pos2[0], pos2[1]) <= this.tetherDist) {
					connectedIDs.add(i);
				}
			}
		}
		
		tethers.put(tethers.size()-1, connectedIDs);
	}
	*/
	
	public int getTetherDist() {
		return this.tetherDist;
	}
	
	private float dist(int x1, int y1, int x2, int y2) {
			
		int dx = x1-x2;
		int dy = y1-y2;

		return (float) Math.sqrt(dx*dx + dy*dy);
	}
	
	public void removeResource(int i, int j) {
		this.resources[i][j] = 0;
	}
	
	public Resource getResource(int i, int j) {
		return resourceTypes.get(Integer.valueOf(this.getID(i, j)));
	}
	
	public int getSize() {
		return this.size;
	}
	
}
