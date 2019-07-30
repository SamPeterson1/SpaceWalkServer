package Net;
import GameLoop.GameLoop;
import World.World;

public class Manager implements Runnable{
	
	private Thread thread;
	private World world;
	private GameLoop loop;
	private Server server;
	private int loopSpeedMs = 10;
	
	public Manager(World world, Server server) {
		this.world = world;
		this.loop = new GameLoop();
		this.loop.setElements(world);
		thread = new Thread(this, "Manager Thread");
		this.server = server;
		thread.start();
	}
	
	@Override
	public void run() {
		while(true) {
			this.loop.run(this.server);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
