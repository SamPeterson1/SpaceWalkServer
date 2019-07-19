package World;

public class Camera {
	
	static int x = 512;
	static int y = 512;
	static int WORLD_SIZE = 256;
	
	public static int[] worldToScreen(int x, int y, int thresh, int gridSize) {
		
		if(Math.abs(x*gridSize-Camera.x) >= 48*32)
			if(x < 512/gridSize) {
				x += Camera.WORLD_SIZE*32/gridSize;
			} else {
				x -= Camera.WORLD_SIZE*32/gridSize;
			}
		
		if(Math.abs(y*gridSize-Camera.y) >= 48*32)
			if(y < 512/gridSize) {
				y += Camera.WORLD_SIZE*32/gridSize;
			} else {
				y -= Camera.WORLD_SIZE*32/gridSize;
			}
		
		int dx = x*gridSize-Camera.x;
		int dy = y*gridSize-Camera.y;
		
		int[] retVal = {512+dx, 512+dy};

		return retVal;
	}

}
