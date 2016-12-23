/*
 * Author : Mitkumar Pandya
 * CSC-505 Fall 2016
 * Department of Computer Science
 * 	NC State
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchUSA {
	
	public static void main(String[] args) {
		//This is a map object for all city objects and it's names
		Map<String,City> map = new HashMap<String,City>();
		try{
			Pattern p1 = Pattern.compile("^road.");
			Pattern p2 = Pattern.compile("^city.");
			FileReader file = new FileReader("usroads.txt");
			Scanner sc = new Scanner(file);
			try{
				while (sc.hasNext()) {
					//System.out.println(sc.nextLine());
					String line = sc.nextLine();
					/*Here we will parse our usroads.pl file and
					 * create a map which contains city name, all its adjacents cities
					 * and their distance
					 */
					Matcher m1 = p1.matcher(line);//matching if line contains road details
					if(m1.find()){
						String[] lines = line.split("\\.");
						for (String string : lines) {
							string = string.trim().replace("road(", "");
							string = string.replace(")", "");
							String name = string.split(",")[0].trim();
							City city;
							//check if city is added in the map, if not add it
							if(map.containsKey(name))
								city = map.get(name);
							else
								city = new City(name);
							string = string.replace(name+",", "");
							String[] adjacents = string.split(",");
							String aCity = adjacents[0].trim();
							//add adjacent cities in a list and distance in a map
							city.addAdjacentCity(aCity,Double.parseDouble(adjacents[1]));
							City city1;
							if(map.containsKey(aCity))
								city1 = map.get(aCity);
							else
								city1 = new City(aCity);
							//add adjacent city to map
							city1.addAdjacentCity(name,Double.parseDouble(adjacents[1]));
							if(!map.containsKey(name))
								map.put(name,city);
							if(!map.containsKey(city1))
								map.put(aCity,city1);
						}
					}
					try{
						Matcher m2 = p2.matcher(line);//matching if line contains latitude,longitude details
						if(m2.find()){
							line = line.replace("city(", "");
							line = line.replace(").", "");
							String[] line1 = line.split(",");
							City city;
							city = map.get(line1[0].trim());
							city.latitude = Double.parseDouble(line1[1].trim());
							city.longitude = Double.parseDouble(line1[2].trim());
						}
					}catch (Exception e) {
						System.err.println("Error parsing the latitude, longitude details");
						e.printStackTrace();
					}
				}
			}catch (Exception e) {
				System.err.println("Error Parsing the usroads.pl file");
				e.printStackTrace();
			}
		
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try{
			if(args.length==3){
				String searchType = args[0];
				String source = args[1];
				String destination = args[2];
				Path path;
				if(searchType.equals("astar")){
					 path = findPathAstar(map, source, destination);
				}
				else if(searchType.equals("greedy")){
					 path = findPathGBFS(map, source, destination);
				}
				else if(searchType.equals("uniform")){
					 path = findPathUCS(map, source, destination);
				}
				else{
					System.err.println("Please enter correct search type!");
					return;
				}
				if(path==null)
					System.err.println("Path not found!");
				else {
					System.out.println("Path : "+path.path.toString());
					System.out.println("Number of nodes in the path : "+path.path.size());
					System.out.println("Total distance : "+path.distance);
				}
			}
			else{
				System.err.println("Invalid no. of arguments");
			}
			
		}catch (Exception e) {
			System.err.println("Error parsing the map, please check whether city names entered are correct!");
			e.printStackTrace();
		}
	}
	
	
	public static Path findPathUCS(Map<String, City> map ,String source, String destination){
		//create a priority queue object
		PriorityQueue pq = new PriorityQueue();
		//expanded or visited nodes list
		List<String> visited = new ArrayList<String>();
		//open nodes list
		List<String> open = new ArrayList<String>();
		City start = map.get(source);
		//create new path object
		Path path = new Path();
		//add source to path
		path.path.add(start.name);
		path.distance = 0.0;
		Double distanceCovered = 0.0;
		pq.insert(path);
		while(pq.size()>0){
			//if queue not empty remove path with minimum distance
			path = pq.remove();
			//remove last node from path and check if it's goal	
			String last = path.path.getLast();
			distanceCovered = path.distance;
			//if found print values and return path
			if(last.equals(destination)){
				path.distance = distanceCovered;
				System.out.println("Open List "+open.toString());
				System.out.println();
				System.out.println("=======================================");
				System.out.println("Found destination : "+destination);
				System.out.println("Nodes expanded (closed list) "+visited.toString());
				System.out.println("No. of nodes expanded : "+visited.size());
				return path;
			}
			//else expand last node
			City c = map.get(last);
			System.out.println("Current State "+c.name );
			open.remove(c.name);
			System.out.println("Open List "+open.toString());
			System.out.println("Visited List "+visited.toString());
			LinkedList<String> prevPath = path.path;
			if(!visited.contains(c.name))
				visited.add(c.name);
			for (int i = 0; i < c.adjacents.size(); i++) {
				//get adjacents of current city
				City a = map.get(c.adjacents.get(i));
				//if adjacents are not expanded expand them
				if(!visited.contains(a.name)){
					Path newPath = new Path();
					newPath.path = new LinkedList<String>(prevPath);
					newPath.path.add(a.name);
					newPath.distance = a.adjacentCities.get(last) + distanceCovered;
					//here we don't use heuristic function just update distance with distance to next node
					newPath.heuristicDistance = a.adjacentCities.get(last) + distanceCovered;
					//add adjacents to open list and priority queue
					open.add(a.name);
					pq.insert(newPath);
				}
			}
		}
		return null;
	}
	
	public static Path findPathGBFS(Map<String, City> map ,String source, String destination){
		//create a priority queue object
		PriorityQueue pq = new PriorityQueue();
		//expanded or visited nodes list
		List<String> visited = new ArrayList<String>();
		//open nodes list
		List<String> open = new ArrayList<String>();
		City start = map.get(source);
		//create new path object
		Path path = new Path();
		//add source to path
		path.path.add(start.name);
		path.distance = 0.0;
		Double distanceCovered = 0.0;
		pq.insert(path);
		while(pq.size()>0){
			//if queue not empty remove path with minimum distance
			path = pq.remove();
			//remove last node from path and check if it's goal
			String last = path.path.getLast();
			distanceCovered = path.distance;
			//if found print values and return path
			if(last.equals(destination)){
				path.distance = distanceCovered;
				System.out.println("Open List "+open.toString());
				System.out.println();
				System.out.println("=======================================");
				System.out.println("Found destination : "+destination);
				System.out.println("Nodes expanded (closed list) "+visited.toString());
				System.out.println("No. of nodes expanded : "+visited.size());
				return path;
			}
			//else expand last node
			City c = map.get(last);
			System.out.println("Current State "+c.name );
			open.remove(c.name);
			System.out.println("Open List "+open.toString());
			System.out.println("Visited List "+visited.toString());
			LinkedList<String> prevPath = path.path;
			if(!visited.contains(c.name))
				visited.add(c.name);
			for (int i = 0; i < c.adjacents.size(); i++) {
				//get adjacents of current city
				City a = map.get(c.adjacents.get(i));
				//if adjacents are not expanded expand them
				if(!visited.contains(a.name)){
					Path newPath = new Path();
					newPath.path = new LinkedList<String>(prevPath);
					newPath.path.add(a.name);
					//update total distance travelled
					newPath.distance = a.adjacentCities.get(last) + distanceCovered;
					//heuristically calculate distance to goal from current city
					newPath.heuristicDistance = heuristic(map,a,destination);
					//add adjacents to open list and priority queue
					if(!open.contains(a.name))
						open.add(a.name);
					pq.insert(newPath);
				}
			}
		}
		return null;
	}
	
	public static Path findPathAstar(Map<String, City> map ,String source, String destination){
		//create a priority queue object
		PriorityQueue pq = new PriorityQueue();
		//expanded or visited nodes list
		List<String> visited = new ArrayList<String>();
		//open nodes list
		List<String> open = new ArrayList<String>();
		City start = map.get(source);
		//create new path object
		Path path = new Path();
		//add source to path
		path.path.add(start.name);
		path.distance = 0.0;
		Double distanceCovered = 0.0;
		pq.insert(path);
		while(pq.size()>0){
			//if queue not empty remove path with minimum distance
			path = pq.remove();
			//remove last node from path and check if it's goal
			String last = path.path.getLast();
			distanceCovered = path.distance;
			//if found print values and return path
			if(last.equals(destination)){
				path.distance = distanceCovered;
				System.out.println("Open List "+open.toString());
				System.out.println();
				System.out.println("=======================================");
				System.out.println("Found destination : "+destination);
				System.out.println("Nodes expanded (closed list) "+visited.toString());
				System.out.println("No. of nodes expanded : "+visited.size());
				return path;
			}
			//else expand last node
			City c = map.get(last);
			System.out.println("Current State "+c.name );
			open.remove(c.name);
			System.out.println("Open List "+open.toString());
			System.out.println("Visited List "+visited.toString());
			if(!visited.contains(c.name))
				 visited.add(c.name);
			LinkedList<String> prevPath = path.path;
			for (int i = 0; i < c.adjacents.size(); i++) {
				//get adjacents of current city
				City a = map.get(c.adjacents.get(i));
				//if adjacents are not expanded expand them
				if(!visited.contains(a.name)){
					Path newPath = new Path();
					newPath.path = new LinkedList<String>(prevPath);
					newPath.path.add(a.name);
					//update total distance travelled
					newPath.distance = a.adjacentCities.get(last) + distanceCovered;
					//update distance with total travelled and heuristically calculated distance to goal
					newPath.heuristicDistance = newPath.distance + heuristic(map,a,destination);
					//add adjacents to open list and priority queue
					open.add(a.name);
					pq.insert(newPath);
				}
			}
		}
		return null;
	}
	
	public static double heuristic(Map<String, City> map, City c, String destination){
		//according to the formulae
		//sqrt((69.5 * (Lat1 - Lat2)) ^ 2 + (69.5 * cos((Lat1 + Lat2)/360 * pi) * (Long1 - Long2)) ^ 2)
		City g = map.get(destination);
		Double hValue = Math.sqrt(Math.pow(69.5 * (c.latitude - g.latitude),2)+Math.pow((69.5 * Math.cos((c.latitude + g.latitude)/360 * Math.PI) * (c.longitude - g.longitude)),2));
		return hValue;
	}
	
}
class Path{
	LinkedList<String> path = new LinkedList<String>();
	Double distance = 0.0;
	Double heuristicDistance = 0.0;

}
