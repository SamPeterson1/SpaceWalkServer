package Net;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import Items.Item;
import World.Building;
import World.BuildingFactory;
import World.Consumable;
import World.Player;
import World.Refinery;
import World.Tether;
import World.TetherLine;
import World.World;

public class DataWriter {
	
	public String write(World world, String id) {
		JSONObject data = new JSONObject();
		JSONObject tiles = new JSONObject();
		JSONObject playerList = new JSONObject();

		
		
		HashMap<Integer, Vector<Integer>> deltas = world.getDeltas(id);
		JSONArray ids = new JSONArray();
		if(deltas != null) {
			for(Integer i: deltas.keySet()) {
				tiles.put(i.toString(), deltas.get(i));
				ids.put(i.toString());
			}
		}
		
		tiles.put("ids", ids);
		
		HashMap<String, Player> players = world.getPlayers();
		JSONArray playerIDs = new JSONArray();
		for(String i: players.keySet()) {
			playerIDs.put(i);
			JSONArray loc = new JSONArray();
			loc.put(0, players.get(i).getWorldX());
			loc.put(1, players.get(i).getWorldY());
			loc.put(2, players.get(i).getSprite());
			loc.put(3, players.get(i).moveSpeed());
			loc.put(4, players.get(i).getConnected().getX());
			loc.put(5, players.get(i).getConnected().getY());
			playerList.put(i.toString(), loc);
		}
		
		JSONArray vals = new JSONArray();
		JSONArray name = new JSONArray();
		JSONArray maximums = new JSONArray();
		for(Consumable c: world.getPlayer(id).getConsumables()) {
			vals.put(c.getAmount());
			name.put(c.getName());
			maximums.put(c.getMax());
		}
		
		JSONArray bag = new JSONArray();
		for(Item i: world.getPlayer(id).getBag()) {
			bag.put(i.getID());
		}
		
		for(int i = bag.length(); i < 8; i ++) {
			bag.put(0);
		}
		
		playerList.put("inBag", world.getPlayer(id).inBag());
		playerList.put("bag", bag);
		playerList.put("vals", vals);
		playerList.put("names", name);
		playerList.put("ids", playerIDs);
		playerList.put("max", maximums);
		
		
		ArrayList<Building> buildings = world.getBuildings();
		int[] buildingIDs = world.getBuildingIDs();
		BuildingFactory factory = new BuildingFactory("Buildings");
		
		
		for(int i = 0; i < buildingIDs.length; i ++) {
			JSONObject building = new JSONObject();
			JSONArray pos = new JSONArray();
			JSONArray[] resources = new JSONArray[factory.getBuilding(buildingIDs[i]).getConsumables().size()];
			JSONArray active = new JSONArray();
			JSONArray connectedNum = new JSONArray();
			JSONArray connectedPos = new JSONArray();
			JSONArray max = new JSONArray();
			JSONArray production = new JSONArray();
			JSONArray linked = new JSONArray();
			JSONArray names = new JSONArray();
			for(int ii = 0; ii < resources.length; ii ++) {
				resources[ii] = new JSONArray();
			}
			for(Consumable c: factory.getBuilding(buildingIDs[i]).getConsumables()) {
				names.put(c.getName());
				max.put(c.getMax());
				production.put(c.getProduction());
			}
			for(Building b: buildings) {
				if(b.getID() == buildingIDs[i]) {
					pos.put(b.getX());
					pos.put(b.getY());
					active.put(b.isActive());
					linked.put(b.linked());
					connectedNum.put(b.getConnected().size());
					for(Building b2: b.getConnected()) {
						connectedPos.put(b2.getX());
						connectedPos.put(b2.getY());
					}
					int index = 0;
					for(Consumable c: b.getConsumables()) {
						resources[index].put(c.getAmount());
						index ++;
					}
				}
			}
			
			building.put("pos", pos);
			for(int ii = 0; ii < resources.length; ii ++) {
				building.put(names.getString(ii), resources[ii]);
			}
			
			building.put("production", production);
			building.put("max", max);
			building.put("connectedPos", connectedPos);
			building.put("active", active);
			building.put("linked", linked);
			building.put("resources", names);
			building.put("connectedNum", connectedNum);
			data.put("Building " + buildingIDs[i], building);
		}
		
		
		JSONObject tethers = new JSONObject();
		for(TetherLine line: world.getTethers()) {
			for(Tether t: line.getTethers()) {
				JSONArray tether = new JSONArray();
				tether.put(t.getX());
				tether.put(t.getY());
				tether.put(t.getState());
				for(Tether t2: t.getConnected().values()) {
					tether.put(t2.getID());
				}
				tethers.put(String.valueOf(t.getID()), tether);
			}
		}
		
		JSONObject refineries = new JSONObject();
		int index = 0;
		for(Refinery r: world.getRefineries()) {
			JSONArray dat = new JSONArray();
			dat.put(r.getX());
			dat.put(r.getY());
			dat.put(r.getProducing());
			dat.put(r.getRate());
			dat.put(r.getPower());
			dat.put(r.getItem().getID());
			refineries.put(String.valueOf(index), dat);
			index ++;
		}
		
		data.put("tethers", tethers);
		data.put("tiles", tiles);
		data.put("players", playerList);
		data.put("refineries", refineries);
		world.resetDeltas(id);
		System.out.println(data.toString());
		return data.toString();
	}
	
}
