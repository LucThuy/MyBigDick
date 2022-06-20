package scene;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.json.simple.parser.ParseException;

import algorithm.AStar;
import algorithm.Cooldown;
import algorithm.Node;
import map.EndPoint;
import map.Map;
import minhdeptrai.Dog;
import minhdeptrai.Player;
import minhdeptrai.ZaWarudo;

public class PlayScene extends JPanel {

	public Container container;
	
	public Vector<Dog> dog = new Vector<>();
	private int dogID = 0;
	private Vector<EndPoint> endPoint = new Vector<>();
	public Cooldown dogCD;
	
	public Vector<ZaWarudo> zaWarudo = new Vector<>();	
	public Cooldown zaWarudoCD;
	
	public Map map;
	public Player player;
	public Timer update;
	public AStar AStar;
	
	public final int FPS = 60;
	public final int SIZE = 28;
	
	
	public PlayScene(Container container) throws FileNotFoundException, IOException, ParseException {
		this.container = container;
		this.dogCD = new Cooldown(5000);
		
		this.zaWarudoCD = new Cooldown(20000);
		
		this.map = new Map();
		this.AStar = new AStar(map.path.dataArr, (int) map.path.WIDTH, (int) map.path.HEIGHT);	
		
		setUp();
		
		addKeyListener(new CustomKeyListener());
		this.update = new Timer(1000/FPS, new CustomActionListener());
		this.update.restart();
	}
	
	public void setUp() throws IOException {
		this.dogID = 0;
		endPoint.clear();
		dog.clear();
		zaWarudo.clear();
		addPlayer();
	}
	
	public void paint(Graphics g) {
		if(ZaWarudo.isZaWarudo) {
			for(int i = 0; i < this.map.layer.size(); i++) {
				this.map.layer.get(i).draw(g, true);
			}	
			if(this.dog != null) {
				for(int i = 0; i < this.dog.size(); i++) {
					this.dog.get(i).draw(g, true);
				}
			}
			this.map.door.drawEnd(g, endPoint);
		}
		else {
			for(int i = 0; i < this.map.layer.size(); i++) {
				this.map.layer.get(i).draw(g);
			}	
			if(this.dog != null) {
				for(int i = 0; i < this.dog.size(); i++) {
					this.dog.get(i).draw(g);
				}
			}
			this.map.door.drawEnd(g, endPoint);
		}
		
		if(this.zaWarudo != null) {
			for(int i = 0; i < this.zaWarudo.size(); i++) {
				this.zaWarudo.get(i).draw(g);
			}
		}
		this.player.draw(g);
	}

	public void addPlayer() throws IOException {
		Rectangle startPos = new Rectangle(this.map.elevator.bound.get(2));
		this.player = new Player(startPos.x, startPos.y, this.map.path.dataArr);
	}
	
	public void addDog() throws IOException {
		Random rd = new Random();
		int tmp = rd.nextInt(100);
		if(tmp < 1) {
			if(!dogCD.isCD()) {
				int index = rd.nextInt(this.map.door.bound.size());
				Rectangle endPos = this.map.door.bound.get(index);
				
				Rectangle startPos = new Rectangle(this.map.elevator.bound.get(0));
				Node start = this.AStar.map[startPos.x / SIZE][startPos.y / SIZE];
//				Rectangle endPos = new Rectangle(this.map.elevator.checkPoint.get(1));
				Node end = this.AStar.map[endPos.x / SIZE][endPos.y / SIZE];
				
				this.endPoint.add(new EndPoint(dogID, index));
				Vector<Node> path = this.AStar.AStarAlgorithm(start, end);
				Dog newDog = new Dog(startPos.x, startPos.y, path, dogID++);
				this.dog.add(newDog);
			}
		}
	}
	
	public void addZaWarudo() {
		Random rd = new Random();
		int tmp = rd.nextInt(1000);
		if(tmp < 1) {
			if(!this.zaWarudoCD.isCD()) {
				int index = rd.nextInt(this.map.path.bound.size());
				int x = this.map.path.bound.get(index).x;
				int y = this.map.path.bound.get(index).y;
				ZaWarudo newZaWarudo = new ZaWarudo(x, y, this.map.path.SIZE);
				this.zaWarudo.add(newZaWarudo);
			}
		}
	}
	
	public void updateZaWarudo() {
		for(int i = 0; i < this.zaWarudo.size(); i++) {
			if(this.player.isCollision(this.player.bound, this.zaWarudo.get(i).bound)) {
				ZaWarudo.isZaWarudo = true;
				ZaWarudo.zaWarudoCD.setTime();
				this.zaWarudo.remove(i);
			}
		}
		if(ZaWarudo.isZaWarudo && !ZaWarudo.zaWarudoCD.isCD()) {
			ZaWarudo.isZaWarudo = false;
		}
	}
	
	public boolean isWin() {
		Rectangle endPos = new Rectangle(this.map.elevator.bound.get(1));
		endPos.add(this.map.elevator.bound.get(3));
		if(endPos.contains(this.player.bound)) {
			return true;
		}
		return false;
	}
	
	
	class CustomActionListener implements ActionListener {
		
		private Vector<Rectangle> playerBlock = new Vector<>();
		private Vector<Rectangle> dogBlock = new Vector<>();
		private Vector<Rectangle> dogBound = new Vector<>();
		
		@Override
		public void actionPerformed(ActionEvent e) {	
			
			addZaWarudo();
			
			updateZaWarudo();
			
			if(!ZaWarudo.isZaWarudo) {
//				try {
//					addDog();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
				
				dogBound.clear();
				
				dogBlock.clear();
				dogBlock.addAll(map.nopath.bound);
				dogBlock.add(player.bound);
				for(int i = 0; i < dog.size(); i++) {
					dog.get(i).move(dogBlock);
					if(dog.get(i).isDogDone) {
						endPoint.remove(i);
						dog.remove(i);
					}
					
					dogBound.add(dog.get(i).bound);
				}
			}
			
			playerBlock.clear();
			playerBlock.addAll(map.nopath.bound);
			playerBlock.addAll(dogBound);
			player.move(playerBlock);
			repaint();
			if(isWin()) {
				container.showWinScene();	
			}		
		}	
	}
	
	
	class CustomKeyListener implements KeyListener {
		
		private boolean isIPress = false;
		
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
					if(ZaWarudo.isZaWarudo) {
						player.blink.isBlink = true;
					}
					else if(!player.blink.blinkCD.isCD()) {
						player.blink.isBlink = true;
					}
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
