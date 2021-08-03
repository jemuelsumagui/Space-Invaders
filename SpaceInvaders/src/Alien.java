import java.awt.Rectangle;

public class Alien {
	
	private int posX;
	private int posY;
	private static int dirX;
	private boolean dead;
	
	private static int frame = 0;
	private int deathDelay = 2;
	private static int motionDelay = 30;
	private static int shootDelay = 30;
	
	
	public Alien(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
		this.dirX = 10;
		this.dead = false;
	}
	
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public void lateralMove() {
		posX+=dirX;
	}
	
	public void downMove() {
		posY+=20;
	}
	
	public boolean checkDir() {
		if(posX<20) {
			dirX = 10;
			return true;
			
		} else if(posX>810) {
			dirX = -10;
			return true;
		}
		return false;
	}
	
	public int getDir() {
		return dirX;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
	public int getDeathDelay() {
		return deathDelay;
	}
	
	public static int getMotionDelay() {
		return motionDelay;
	}
	public static int getShootDelay() {
		return shootDelay;
	}
	public static void decreaseMotionDelay() {

		if(motionDelay <= 10) { //se motionDelay e shootDelay sono minori o uguali a 10 decremento di 1
			motionDelay--;
			shootDelay--;
		} else {
			motionDelay-=2;
			shootDelay-=2;
		}
	}
	public void decreaseDeathDelay() {
		deathDelay--;
	}
	
	public static void changeFrame() {
		if(frame == 0) {
			frame = 1;
		} else if(frame == 1) {
			frame = 0;
		}
	}
	
	public static int getFrame() {
		return frame;
	}
	
	
	
	
	public Rectangle getBoundingBox() {
		return new Rectangle(posX, posY, Resources.alien1.getWidth()*3,Resources.alien1.getHeight()*3);
	}

}
