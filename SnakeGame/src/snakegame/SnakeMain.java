package snakegame;

import java.awt.Dimension;

import javax.swing.JFrame;

public class SnakeMain {
	public static void main(String args[])
	{
		JFrame frame = new JFrame("Snake");
		frame.setContentPane(new SnakePanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setPreferredSize(new Dimension(SnakePanel.WIDTH, SnakePanel.HEIGHT));
		frame.setVisible(true);		
	}
}
