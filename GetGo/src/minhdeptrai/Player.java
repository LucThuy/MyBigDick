package minhdeptrai;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Vector;

import assets.Position;

public class Player {
	
	public Position position = new Position();
	public int msE;
	public int msN;
	public int msW;
	public int msS;
	public int ms;
	
	public boolean isBlink;
	public final int BLINK_RANGE = 2;
	
	public Rectangle bound = new Rectangle();
	
	public final int WIDTH = 15;
	public final int HEIGHT = 15;
	public final int SIZE = 16;
	
	public Player(int x, int y) {
		this.position.x = x;
		this.position.y = y;
		this.ms = 1;
		this.msE = 0;
		this.msN = 0;
		this.msW = 0;
		this.msS = 0;
		
		this.isBlink = false;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.drawRect(this.position.x, this.position.y, WIDTH, HEIGHT);
	}
	
	public void setBound() {
		this.bound.setBounds(this.position.x, this.position.y, WIDTH, HEIGHT);
	}
	
	public void move(Vector<Rectangle> block) {
		Position tmp = new Position(this.position.x, this.position.y);
		
		if(this.msE != 0) {
			tmp.x += this.msE;
			if(this.isBlink) {
				tmp.x += BLINK_RANGE * SIZE;
				this.isBlink = false;
			}
			while(!isOK(tmp, block)) {
				tmp.x --;
			}
		}
		if(this.msN != 0) {
			tmp.y -= this.msN;
			if(this.isBlink) {
				tmp.y -= BLINK_RANGE * SIZE;
				this.isBlink = false;
			}
			while(!isOK(tmp, block)) {
				tmp.y ++;
			}
		}
		if(this.msW != 0) {
			tmp.x -= this.msW;
			if(this.isBlink) {
				tmp.x -= BLINK_RANGE * SIZE;
				this.isBlink = false;
			}
			while(!isOK(tmp, block)) {
				tmp.x ++;
			}
		}
		if(this.msS != 0) {
			tmp.y += this.msS;
			if(this.isBlink) {
				tmp.y += BLINK_RANGE * SIZE;
				this.isBlink = false;
			}
			while(!isOK(tmp, block)) {
				tmp.y --;
			}
		}
		
		this.position = tmp;
		setBound();
	}
	
	public boolean isOK(Position tmp, Vector<Rectangle> block) {
		Rectangle tmpBound = new Rectangle(tmp.x, tmp.y, WIDTH, HEIGHT);
		for(int i = 0; i < block.size(); i++) {
			if(isCollision(tmpBound, block.get(i))) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isCollision(Rectangle a, Rectangle b) {
		if(a.intersects(b)) {
			return true;
		}
		return false;
	}
}
