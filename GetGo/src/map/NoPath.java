package map;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

public class NoPath extends Layer {

	public NoPath(long id, long[] data, String name, Vector<Tile> tiles) {
		super(id, data, name, tiles);
	}

	public void draw(Graphics g) {
		for(int i = 0; i < HEIGHT; i++) {
			for(int j = 0; j < WIDTH; j++) {
				if(dataArr[j][i] != 0) {
					g.setColor(Color.CYAN);
					g.fillRect(j * SIZE, i * SIZE, SIZE, SIZE);
				}
			}
		}
	}
}
