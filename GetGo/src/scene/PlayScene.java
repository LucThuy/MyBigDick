package scene;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.json.simple.parser.ParseException;

import assets.Position;
import map.Map;

import minhdeptrai.Player;

public class PlayScene extends JPanel {

	public Map map;
	public Player player;
	public Timer update;
	
	public int FPS = 60;
	
	
	public PlayScene() throws FileNotFoundException, IOException, ParseException {
		map = new Map();
		addPlayer();
		addKeyListener(new CustomKeyListener());
		update = new Timer(1000/FPS, new CustomActionListener());
		update.restart();
	}
	
	public void paint(Graphics g) {
		for(int i = 0; i < map.layer.size(); i++) {
			map.layer.get(i).draw(g);
		}	
		map.elevator.draw(g);
		player.draw(g);
	}

	public void addPlayer() {
		Rectangle startPos = new Rectangle(map.elevator.checkPoint.get(0));
		player = new Player(startPos.x, startPos.y);
	}
	
	public boolean isWin() {
		Rectangle finishPos = new Rectangle(map.elevator.checkPoint.get(1));
		if(finishPos.intersects(player.bound)) {
			return true;
		}
		return false;
	}
	
	
	class CustomActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			player.move(map.nopath.bound);
			if(isWin()) {
				
			}
			repaint();
		}	
	}
	
	
	private boolean isIPress = false;
	
	class CustomKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {	
			int key  = e.getKeyCode();
			
			if(key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
				player.msE = player.ms;
			}
			if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
				player.msN = player.ms;
			}
			if(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
				player.msW = player.ms;
			}
			if(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
				player.msS = player.ms;
			}
			
			if(key == KeyEvent.VK_I) {
				if(!isIPress) {
					player.isBlink = true;
					isIPress = true;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int key  = e.getKeyCode();
			
			if(key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
				player.msE = 0;
			}
			if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
				player.msN = 0;
			}
			if(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
				player.msW = 0;
			}
			if(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
				player.msS = 0;
			}
			
			if(key == KeyEvent.VK_I) {
				isIPress = false;
			}
		}
	}
}
