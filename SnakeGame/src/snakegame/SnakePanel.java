// PowerUps spawn with 20% probability on eating food
// PowerUps implemented:
//          Elongation (Orange) - Lengthens the snake by 2 squares
// PowerUps to be implemented:
//			Contraction (Grey) - Contracts the snake by 2 squares
//			Gluttony (Purple) - Spawns 7 instances of food all over
//			Minimize (Yellow) - Minimizes the snake to 2 squares
//			Cripple (White) - Slows the snake to half its speed for 10 seconds
//			Speed Tonic (Magenta) - Speeds up the snake to twice its speed for 10 seconds
//			Pass Through Tonic (Cyan) - Enables the snake to pass through its body for 10 seconds

package snakegame;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SnakePanel extends JPanel implements Runnable, KeyListener {

	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;
	
	private Graphics2D g;
	private BufferedImage img;
	
	private Thread t;
	private boolean running;
	private long targettime;
	private int size = 10;
	
	GameEntity head, food, powerUp;
	ArrayList<GameEntity> snake;
	private int score;
	private int level;
	private boolean gameover;
	private boolean powerUpAvailable;
	
	private int dx, dy;
	private boolean up, down, left, right, start;
	
	public SnakePanel()
	{
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();		
		addKeyListener(this);
	}
	
	private void setFPS(int fps)
	{
		targettime = 1000 / fps;
	}
	
	public void addNotify()
	{
		super.addNotify();
		t = new Thread(this);
		t.start();
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int k = e.getKeyCode();
		
		if (k == KeyEvent.VK_UP)
			up = true;
		if (k == KeyEvent.VK_DOWN)
			down = true;
		if (k == KeyEvent.VK_LEFT)
			left = true;
		if (k == KeyEvent.VK_RIGHT)
			right = true;
		if (k == KeyEvent.VK_ENTER)
			start = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int k = e.getKeyCode();
		
		if (k == KeyEvent.VK_UP)
			up = false;
		if (k == KeyEvent.VK_DOWN)
			down = false;
		if (k == KeyEvent.VK_LEFT)
			left = false;
		if (k == KeyEvent.VK_RIGHT)
			right = false;
		if (k == KeyEvent.VK_ENTER)
			start = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (running)
			return;
		init();
		long begin;
		long elapsed;
		long wait;		
		
		while (running)
		{
			begin = System.nanoTime();
			
			update();
			requestRender();
			elapsed = System.nanoTime() - begin;
			wait = targettime - elapsed / 100000;
			if (wait > 0)
			{
				try
				{
					Thread.sleep(wait);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private void requestRender() {
		// TODO Auto-generated method stub
		render(g);
		Graphics gf = getGraphics();
		gf.drawImage(img, 0, 0, null);
		gf.dispose();
	}

	private void update() {
		// TODO Auto-generated method stub
		
		if (gameover)
		{
			if (start)
				setUpLevel();
			return;
		}
		if (up && dy == 0)
		{
			dy = -size;
			dx = 0;
		}
		if (down && dy == 0)
		{
			dy = size;
			dx = 0;
		}
		if (left && dx == 0)
		{
			dy = 0;
			dx = -size;
		}
		if (right && dx == 0 && dy != 0)
		{
			dy = 0;
			dx = size;
		}	
		
		if (dx != 0 || dy != 0)
		{	
			for (int i = snake.size() - 1; i > 0; i--)
			{
				snake.get(i).setPosition(snake.get(i - 1).getX(), snake.get(i - 1).getY());
			}
			head.move(dx,  dy);
		
			if (head.getX() < 0)
				head.setX(WIDTH - size);
			if (head.getY() < 0)
				head.setY(HEIGHT - size);
			if (head.getX() > WIDTH - size)
				head.setX(0);
			if (head.getY() > HEIGHT - size)
				head.setY(0);
		}		
		
		if (food.isCollision(head))
		{
			score++;
			setFood();
			GameEntity e = new GameEntity(size);
			e.setPosition(-100, -100);
			snake.add(e);
			if (score % 20 == 0)
				level++;
				if (level > 10)
					level = 10;
				
			int x = (int)(Math.random() * 5);
			if (x == 0)
			{
				spawnRandomPowerUp();
			}
		}
		
		if (powerUpAvailable && powerUp.isCollision(head))
		{
			elongationPowerUp();
			powerUpAvailable = false;
		}
		
		for (GameEntity e : snake)
		{
			if (e.isCollision(head))
			{
				gameover = true;
				break;
			}	
		}
					
	}

	private void elongationPowerUp() {
		// TODO Auto-generated method stub
		GameEntity e1 = new GameEntity(size);
		GameEntity e2 = new GameEntity(size);
		snake.add(e1);
		snake.add(e2);
		score = score + 2;
		if (score % 20 == 0 || (score - 1) % 20 == 0)
		{
			level++;
			if (level > 10)
				level = 10;
		}	
	}

	private void spawnRandomPowerUp() {
		// TODO Auto-generated method stub
		int x = (int)(Math.random() * (WIDTH - size));
		int y = (int)(Math.random() * (HEIGHT - size));
		y = y - (y % size);
		x = x - (x % size);
		powerUp = new GameEntity(15);
		for (GameEntity e : snake)
		{
			if (e.getX() != x && e.getY() != y)
				powerUp.setPosition(x, y);
		}		
		powerUpAvailable = true;
		
	}

	private void init()
	{
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g = img.createGraphics();
		running = true;
		setUpLevel();
		gameover = false;
		level = 1;
		setFPS(10);
		
	}
	
	public void render(Graphics2D g)
	{
		g.clearRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.GREEN);
		for (GameEntity e : snake)
		{
			e.render(g);
		}
		
		g.setColor(Color.RED);
		food.render(g);
		if (powerUpAvailable)
		{
			g.setColor(Color.ORANGE);
			powerUp.render(g);
			
		}
		if (gameover)
		{
			g.setColor(Color.RED);
			g.drawString("GAME OVER", 200, 200);
		}
		g.setColor(Color.WHITE);
		g.drawString("Score : " + score, 10, 10);
		g.drawString("Level : " + level, 100, 10);	
		if (dx == 0 && dy == 0)
			g.drawString("Ready!", 200, 200);
	}
	private void setUpLevel()
	{
		snake = new ArrayList<GameEntity>();
		head = new GameEntity(size);
		head.setPosition(WIDTH / 2, HEIGHT / 2);
		snake.add(head);
		for (int i = 1; i < 2; i++)
		{
			GameEntity e = new GameEntity(size);
			e.setPosition(head.getX() + (i * size), head.getY());
			snake.add(e);
		}
		
		food = new GameEntity(10);
		setFood();
		score = 0;
		gameover = false;
		level = 1;
		powerUpAvailable = false;
	}
	
	public void setFood()
	{
		int x = (int)(Math.random() * (WIDTH - size));
		int y = (int)(Math.random() * (HEIGHT - size));
		y = y - (y % size);
		x = x - (x % size);
		for (GameEntity e : snake)
		{
			if (e.getX() == x && e.getY() == y)
				food.setPosition((x + 5) % WIDTH, (y - 5) % HEIGHT);
			else
				food.setPosition(x, y);
				
		}
	}
}
