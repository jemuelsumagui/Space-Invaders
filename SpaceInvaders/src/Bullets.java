import java.awt.Rectangle;

public class Bullets {
	
	private int posX;
	private int posY;
	private int dirY;
	
	
	public Bullets(int posX, int posY, int dirY) {
		this.posX = posX;
		this.posY = posY;
		this.dirY = dirY;
	}
	
	public void moveUp() {
		this.posY-=dirY;
	}
	public void moveDown() {
		this.posY+=dirY;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public Rectangle getBoundingBox() {
		return new Rectangle(posX, posY, 3, 10);
	}

}
