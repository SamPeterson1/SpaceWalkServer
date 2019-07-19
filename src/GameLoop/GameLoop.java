package GameLoop;
import java.util.HashMap;

import World.Building;
import World.Consumable;
import World.Player;
import World.World;

public class GameLoop {
	
	World world;
	long time;
	
	public void setElements(World world) {
		this.world = world;
		time = System.currentTimeMillis();
	}
	
	public void run() {
		HashMap<String, Player> players = world.getPlayers();
		for(String id: players.keySet()) {
			players.get(id).updateSprite();
		}
		
		if(System.currentTimeMillis() - time >= 1000) {
			this.world.distributeResources();
			time = System.currentTimeMillis();		
		}
	}
	
}
