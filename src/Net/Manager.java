package Net;
import GameLoop.GameLoop;
import World.World;

public class Manager implements Runnable{
	
	private Thread thread;
	private World world;
	private GameLoop loop;
	private int loopSpeedMs = 10;
	
	public Manager(World world) {
		this.world = world;
		this.loop = new GameLoop();
		this.loop.setElements(world);
		thread = new Thread(this, "Manager Thread");
		thread.start();
	}
	
	@Override
	public void run() {
		while(true) {
			this.loop.run();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
