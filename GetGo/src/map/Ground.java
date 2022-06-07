package map;

import java.awt.Color;
import java.awt.Graphics;

public class Ground extends Layer {

	public Ground(long id, long[] data, String name) {
		super(id, data, name);
	}
	
	public void draw(Graphics g) {
		for(int i = 0; i < HEIGHT; i++) {
			for(int j = 0; j < WIDTH; j++) {
				if(dataArr[j][i] != 0) {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(j * SIZE, i * SIZE, SIZE, SIZE);
				}
			}
		}
	}
}
