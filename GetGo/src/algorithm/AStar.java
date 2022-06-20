package algorithm;

import java.util.Vector;

public class AStar {
	
	public Node[][] map;
	
	public int width;
	public int height;
	
	public AStar(long[][] data, int width, int height) {
		this.width = width;
		this.height = height;
		map = new Node[width][height];
		
		loadMap(data);
	}
	
	public void loadMap(long[][] data) {
		for(int  i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				Position position = new Position(j, i);
				boolean isWalkable = data[j][i] == 0 ? false : true;
				map[j][i] = new Node(position, isWalkable);
			}
		}
		setupNextNode();
	}
	
	public void setupNextNode() {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j< width; j++) {
				if(j + 1 < width && map[j+1][i].isWalkable) {
					map[j][i].nextNode.add(map[j+1][i]);
				}
				if(j - 1 >= 0 && map[j-1][i].isWalkable) {
					map[j][i].nextNode.add(map[j-1][i]);
				}
				if(i - 1 > 0 && map[j][i-1].isWalkable) {
					map[j][i].nextNode.add(map[j][i-1]);
				}
				if(i + 1 < height && map[j][i+1].isWalkable) {
					map[j][i].nextNode.add(map[j][i+1]);
				}
			}
		}
	}
	
	public Vector<Node> AStarAlgorithm(Node start, Node end) {
		Vector<Node> open = new Vector<>();
		Vector<Node> close = new Vector<>();
		
		start.G = 0;
		start.H = Position.distance(start.position, end.position);
		start.F = start.G + start.H;
		
		open.add(start);
		
		while(open.size() != 0) {
			Node currentNode = open.get(0);
			for(int i = 0; i < open.size(); i++) {
				Node nodeIndex = open.get(i);
				if(nodeIndex.F < currentNode.F) {
					currentNode = nodeIndex;
				}
				open.remove(currentNode);
				close.add(currentNode);
				if(currentNode == end) {
					open.clear();
					close.clear();
					return tracePath(end);
				}
				else {
					for(Node nextNode : currentNode.nextNode) {
						if(close.contains(nextNode)) {
							continue;
						}
						double tmpG = currentNode.G + Position.distance(currentNode.position, nextNode.position);
						if(!open.contains(nextNode) || tmpG < nextNode.G) {
							nextNode.preNode = currentNode;
							nextNode.G = tmpG;
							nextNode.H = Position.distance(nextNode.position, end.position);
							nextNode.F = nextNode.G + nextNode.H;
							
							if(!open.contains(nextNode)) {
								open.add(nextNode);
							}
						}
					}
				}
			}
		}
	
		return null;	
	}
	
	public Vector<Node> tracePath(Node t){
		Vector<Node> path = new Vector<>();
		Node tmp = t;
		while(tmp != null) {
			if(tmp.preNode == null) {
				return path;
			}
			path.add(tmp);
			tmp = tmp.preNode;
		}
		return path;		
	}
}
