package Net;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import World.World;

public class Server implements Runnable {
	private ServerSocket serverSocket;
	private Thread thread;
	private World world;
	private ArrayList<String> playerIDs;

	public Server() {
		playerIDs = new ArrayList<String>();
		this.world = World.grassWorld(256);
		new Manager(world);
		try {
			this.thread = new Thread(this, "Server Thread");
			this.serverSocket = new ServerSocket(7777);
			this.thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {		
			try {
				while (true) {
					new Session(this.serverSocket.accept(), world, playerIDs);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}