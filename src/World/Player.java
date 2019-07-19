package World;

import java.awt.Image;
import java.util.ArrayList;

import Items.Item;

public class Player {
	
	public static final int EAST = 2;
	public static final int NORTH = 1;
	public static final int SOUTH = 3;
	public static final int WEST = 4;
	
	private ArrayList<Item> bag;
	private int bagSize = 8;
	private int baseSpeed = 3;
	private Building[] buildings = new Building[2];
	private int connectIndex = 0;
	private int direction = Player.NORTH;
	private String[] directions = {"North", "East", "South", "West"};
	private Item hand;
	private Image img;
	private boolean inBag;
	private int iters = 0;
	private boolean moving = false;
	private boolean oxygenSupply;
	private int selectedX = 4;
	private int selectedY = 4;
	private boolean sprint = false;
	private double sprintMult = 1.5;
	private int spriteProg = 0;
	private ArrayList<Image> sprites;
	private World world;
	private int worldX = 512;
	private int worldY = 512;
	private int sprite;
	private int x = 0;
	private int y = 0;
	
	public Player(World world) {
		this.world = world;
	}

	public void center(int canvasWidth, int canvasHeight) {
		this.x = canvasWidth/2 + 16;
		this.y = canvasHeight/2 + 16;
	}
	
	public void down() {
		this.worldY += this.moveSpeed();
		this.direction = Player.SOUTH;
		this.setMovement(true);
		if(this.worldY >= world.getSize()*32) this.worldY -= world.getSize()*32;
	}
	
	public ArrayList<Item> getBag() {
		return this.bag;
	}
	
	public void closeBag() {
		this.inBag = false;
	}
	
	public void connectBuilding(int x, int y) {
		if(world.buildingExsits(x, y)) {
			if(!((this.connectIndex == 1) && (this.buildings[0].equals(world.getBuiding(x, y))))) {
				this.buildings[this.connectIndex] = world.getBuiding(x, y);
				System.out.println(this.connectIndex + " " + this.buildings[this.connectIndex].getID());
				if(this.connectIndex == 1) {
					this.buildings[0].connectTo(this.buildings[1]);
					this.buildings[1].connectTo(this.buildings[0]);
					
					this.connectIndex = -1;
				}
				
				this.connectIndex ++;
			}
		}
	}
	
	public Item getHand() {
		return this.hand;
	}
	
	public int getSelectedX() {
		return this.selectedX;
	}
	
	public int getSelectedY() {
		return this.selectedY;
	}
	
	public void harvestResource() {
		if(this.world.getResourceID(selectedY, selectedX) != 0) {
			this.bag.add(this.world.getResource(selectedX, selectedY).getDrop());
			this.world.removeResource(selectedX,  selectedY);
		}
	}
	
	
	public void moveToHand(Item i) {
		if(hand == null) {
			bag.remove(i);
			hand = i;
		} else {
			bag.remove(i);
			bag.add(hand);
			hand = i;
		}
	}
	
	public void openBag() {
		this.inBag = true;
	}
	
	public void placeBuilding(int ID) {
		this.world.placeBuilding(ID, this.worldX/32, this.worldY/32);
	}
	
	public void placeTether() {
		this.world.placeTether(this.worldX, this.worldY);
	}
	
	public void select(int x, int y) {
		this.selectedX = x;
		this.selectedY = y;
	}
		
	public int getSprite() {
		return this.sprite;
	}
	
	public int facing() {
		return this.direction;
	}
	
	public void toggleBag() {
		this.inBag = !this.inBag;
	}
	
	
	public World getWorld() {
		return this.world;
	}
	
	public int getWorldX() {
		return this.worldX;
	}
	
	public int getWorldY() {
		return this.worldY;
	}
	
	public void left() {
		this.worldX -= this.moveSpeed();
		this.direction = Player.WEST;
		this.setMovement(true);
		if(this.worldX <= 0) this.worldX += world.getSize()*32;
	}
	
	public int moveSpeed() {
		if(sprint) return this.sprintSpeed();
		return this.walkSpeed();
	}
	
	public void progressSprite() {
		iters++;
		int max = 24;
		if(this.sprint) max = 12;
		if(iters == max) {
			this.spriteProg ++;
			if(this.spriteProg == 3) this.spriteProg = 0;
			iters = 0;
		}
	}
	
	public boolean addToBag(Item i) {
		if(this.bag.size() < this.bagSize) {
			this.bag.add(i);
			System.out.println("Added to bag: " + i.getID());
			return true;
		} else if(this.hand == null) {
			this.hand = i;
			return true;
		}
 		return false;
	}
	
	public void right() {
		this.worldX += this.moveSpeed();
		this.direction = Player.EAST;
		this.setMovement(true);
		if(this.worldX >= world.getSize()*32) this.worldX -= world.getSize()*32;
	}

	public void setMovement(boolean moving) {
		this.moving = moving;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public int sprintSpeed() {
		return (int)(this.baseSpeed*sprintMult);
	}

	
	public void toggleSprint() {
		this.sprint = !this.sprint;
		iters = 0;
	}
	
	public void up() {
		this.worldY -= this.moveSpeed();
		this.direction = Player.NORTH;
		this.setMovement(true);
		if(this.worldY <= 0) this.worldY += world.getSize()*32;
	}
	
	public void updateSprite() {
		if(this.moving) {
			this.progressSprite();
			this.sprite = (direction-1)*3 + spriteProg;
		} else {
			this.sprite = (direction-1)*3 + 1;
		}
	}
	
	public int walkSpeed() {
		return this.baseSpeed;
	}
}
