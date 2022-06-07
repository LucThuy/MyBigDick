package map;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Map {
	
	public Vector<Layer> layer = new Vector<>();
	
//	public long numLayers;
	
	public Ground ground;
	public Path path;
	public Room room;
	public Door door;
	public Wall wall;
	public NoPath nopath;
	public Gate gate;
	public Elevator elevator;
	public Bed bed;
	
	public Map() throws FileNotFoundException, IOException, ParseException {
		loadMap();
	}
	
	public void loadMap() throws FileNotFoundException, IOException, ParseException {
		Object obj = new JSONParser().parse(new FileReader("C:\\Users\\d\\eclipse-workspace\\GetGo\\src\\assets\\hospital.json"));
		JSONObject loadJSON = (JSONObject)obj;
		
//		numLayers = (long)loadJSON.get("nextlayerid") - 1;
		JSONArray loadLayers = (JSONArray) loadJSON.get("layers");
		
		for(int i = 0; i < loadLayers.size(); i++) {
			JSONObject layerIndex = (JSONObject) loadLayers.get(i);	
			String name = (String) layerIndex.get("name");
			long id = (long) layerIndex.get("id");
//			long height =  (long) layerIndex.get("height");
//			long width = (long) layerIndex.get("width");
//			long x = (long) layerIndex.get("x");
//			long y = (long) layerIndex.get("y");	
			JSONArray dataIndex = (JSONArray) layerIndex.get("data");
			long[] data = new long[dataIndex.size()];
			for(int j = 0; j < dataIndex.size(); j++) {
				data[j] = (long) dataIndex.get(j);
			}
			
			
			switch(name) {
				case "ground":{
					ground = new Ground(id, data, name);
					layer.add(ground);
					break;
				}
				case "path":{
					path = new Path(id, data, name);
					layer.add(path);
					break;
				}
				case "room":{
					room = new Room(id, data, name);
					layer.add(room);
					break;
				}
				case "door":{
					door = new Door(id, data, name);
					layer.add(door);
					break;
				}
				case "wall":{
					wall = new Wall(id, data, name);
					layer.add(wall);
					break;
				}
				case "nopath":{
					nopath = new NoPath(id, data, name);
					layer.add(nopath);
					break;
				}
				case "gate":{
					gate = new Gate(id, data, name);
					layer.add(gate);
					break;
				}
				case "elevator":{
					elevator = new Elevator(id, data, name);
					layer.add(elevator);
					break;
				}
				case "bed":{
					bed = new Bed(id, data, name);
					layer.add(bed);
					break;
				}
			}
		}
	}
}
