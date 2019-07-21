package Net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;

import World.Player;
import World.World;

public class Session implements Runnable {
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private Socket socket;
	private Thread thread;
	private World world;

	public Session(Socket socket, World world, ArrayList<String> playerIDs) {
		
		try {
			System.out.println("YEET");
			String playerID = this.newCharID();
			this.world = world;
			this.world.addPlayer(new Player(this.world), playerID);
			this.thread = new Thread(this, "Session Thread");
			this.socket = socket;
			this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			bufferedWriter.write(playerID);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			this.sendWorld();
			System.out.println(playerID);
			
			this.thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendWorld() throws IOException {
		String data = this.getWorld();
		bufferedWriter.write(data);
		bufferedWriter.newLine();
		bufferedWriter.flush();
	}
	
	private String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}

	private String newCharID() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] authTokenBytes = new byte[18];
		secureRandom.nextBytes(authTokenBytes);
		return bytesToHex(authTokenBytes);
	}
	
	public String getWorld() {
		StringBuilder retVal = new StringBuilder("");
		for(int i = 0; i < world.getSize(); i ++) {
			for(int ii = 0; ii < world.getSize(); ii ++) {
				retVal.append(world.getID(ii, i) + ",");
			}
		}
		
		return retVal.toString();
	}
	
	@Override
	public void run() {
		try {
			String line;
			
			while(true) {
				
				//Receive messages
				while ((line = this.bufferedReader.readLine()) != null) {
					//System.out.println(line);
					Request request = Request.parseString(line);
					System.out.println(request.toString());
					if(request.getMessage().equals("edit")) {
						int x = request.getInt("x");
						int y = request.getInt("y");
						this.world.setID(x, y, request.getInt("id"));
					}
					if(request.getMessage().equals("data")) {
						String data = new DataWriter().write(world, request.getString("id"));
						bufferedWriter.write(data);
						bufferedWriter.newLine();
						bufferedWriter.flush();
					} else if(request.getMessage().equals("move")) {
						String direction = request.getString("direction");
						String id = request.getString("id");
						Player p = this.world.getPlayer(id);
						switch(direction) {
						case "up":
							p.up();
							break;
						case "down":
							p.down();
							break;
						case "left":
							p.left();
							break;
						case "right":
							p.right();
							break;
						}
					} else if(request.getMessage().equals("sprint")) {
						String id = request.getString("id");
						Player p = this.world.getPlayer(id);
						p.toggleSprint();
					} else if(request.getMessage().equals("halt")) {
						String id = request.getString("id");
						Player p = this.world.getPlayer(id);
						p.setMovement(false);
					} else if(request.getMessage().equals("build")) {
						String id = request.getString("playerID");
						Player p = this.world.getPlayer(id);
						this.world.placeBuilding(request.getInt("buildID"), p.getWorldX()/32, p.getWorldY()/32);
					} else if(request.getMessage().equals("link")) {
						this.world.connectBuilding(request.getInt("x"), request.getInt("y"));
					} else if(request.getMessage().equals("tether")) {
						String id = request.getString("id");
						Player p = this.world.getPlayer(id);
						p.placeTether();
					}
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}