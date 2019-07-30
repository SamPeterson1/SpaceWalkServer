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
	private ArrayList<Session> sessions;

	public Server() {
		playerIDs = new ArrayList<String>();
		this.world = World.grassWorld(256);
		this.sessions = new ArrayList<Session>();
		new Manager(world, this);
		try {
			this.thread = new Thread(this, "Server Thread");
			this.serverSocket = new ServerSocket(7777);
			this.thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect(Session session) {
		this.sessions.remove(session);
		System.out.println("Session Disconnected: " + session.getID());
	}
	
	public void sendToAll() throws IOException{
		for(Session s: this.sessions) {
			s.sendData();
		}
	}
	
	@Override
	public void run() {		
			try {
				while (true) {
					this.sessions.add(new Session(this.serverSocket.accept(), world, playerIDs, this));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}