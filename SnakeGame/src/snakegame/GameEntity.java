package snakegame;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class GameEntity {
	private int x, y, size;
	public GameEntity(int size)
	{
		this.size = size;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void move(int dx, int dy)
	{
		x += dx;
		y += dy;
	}
	
	public Rectangle getBound()
	{
		return new Rectangle(x, y, size, size);
	}
	
	public boolean isCollision(GameEntity e)
	{
		if (e == this)
			return false;
		return getBound().intersects(e.getBound());
	}
	
	public void render(Graphics2D g)
	{
		g.fillRect(x + 1, y + 1, size - 2, size - 2);
	}
}
