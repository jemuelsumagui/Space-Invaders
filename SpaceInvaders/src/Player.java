import java.awt.Rectangle;

public class Player{
	
	private int posX;
	private int posY;
	public int dirX;
	private int lives;
	private boolean dead;
	
	public final int shotDelay = 15;
	
	public Player() {
		posX = Reference.WINDOW_WIDTH/2;
		posY = Reference.WINDOW_HEIGHT-70;
		lives = 3;
		dead = false;
	}
	public int getPosX() {
		return posX;
	}
	public int getPosY() {
		return posY;
	}
	
	public void move(int dirX) {
		posX+=dirX;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
	public int getLives() {
		return lives;
	}
	
	public void takeLives() {
		lives--;
	}
	
	public Rectangle getBoundingBox() {
		return new Rectangle(posX, posY, Resources.player.getWidth()*4, Resources.player.getHeight()*4);
	}
		
	
}