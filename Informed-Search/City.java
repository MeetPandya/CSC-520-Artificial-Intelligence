/*
 * Author : Mitkumar Pandya
 * Unity ID : mhpandya
 */
package com.meet.semester1.CSC520.InformedSearch.AStar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class City {
	String name;
	Double longitude;
	Double latitude;
	Double currentDistance;
	boolean visited = false;
	//This is a map of adjacent cities and their distance from a
	//particular city object
	Map<String,Double> adjacentCities = new HashMap<String, Double>();
	//This is the list of adjacent cities for a particular city object
	List<String> adjacents = new ArrayList<String>();
 	public City(String name){
		this.name = name;
	}
	//Add adjacent city to adjacents list and it's distance to the map
	public void addAdjacentCity(String city, Double distance){
		adjacents.add(city);
		adjacentCities.put(city, distance);
	}
}
