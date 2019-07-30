package GameLoop;
import java.io.IOException;
import java.util.HashMap;

import Net.Server;
import World.Player;
import World.World;

public class GameLoop {
	
	World world;
	long time;
	
	public void setElements(World world) {
		this.world = world;
		time = System.currentTimeMillis();
	}
	
	public void run(Server server) {
		HashMap<String, Player> players = world.getPlayers();
		for(String id: players.keySet()) {
			Player p = players.get(id);
			p.updateSprite();
		}
		
		if(System.currentTimeMillis() - time >= 1000) {
			this.world.distributeResources();
			time = System.currentTimeMillis();		
			for(Player p: world.getPlayers().values()) {
				p.update();
			}
			
			try {
				server.sendToAll();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.world.update();
	}
	
}
