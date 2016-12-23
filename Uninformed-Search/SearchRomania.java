/*
 * Author : Mitkumar Pandya
 * CSC-505 Fall 2016
 * Department of Computer Science
 * 	NC State
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/** Main method **/
public class SearchRomania {
	//A global map object which will be used by other methods
	public static Map<String, String[]> romaniaMap = new HashMap<String, String[]>();
	
	public static void main(String[] args) {
		
		//Put all the map values in a Map Object
		romaniaMap.put("arad",new String[]{"zerind","timisoara","sibiu"});
		romaniaMap.put("bucharest",new String[]{"pitesti","giurgiu","fagaras","urziceni"});
		romaniaMap.put("craiova",new String[]{"dobreta","pitesti","rimnicu_vilcea"});
		romaniaMap.put("dobreta",new String[]{"craiova","mehadia"});
		romaniaMap.put("eforie",new String[]{"hirsova"});
		romaniaMap.put("fagaras",new String[]{"bucharest","sibiu"});
		romaniaMap.put("giurgiu",new String[]{"bucharest"});
		romaniaMap.put("hirsova",new String[]{"eforie","urziceni"});
		romaniaMap.put("iasi",new String[]{"neamt","vaslui"});
		romaniaMap.put("lugoj",new String[]{"timisoara","mehadia"});
		romaniaMap.put("mehadia",new String[]{"dobreta","lugoj"});
		romaniaMap.put("neamt",new String[]{"iasi"});
		romaniaMap.put("oradea",new String[]{"zerind","sibiu"});
		romaniaMap.put("pitesti",new String[]{"craiova","bucharest","rimnicu_vilcea"});
		romaniaMap.put("rimnicu_vilcea",new String[]{"craiova","pitesti","sibiu"});
		romaniaMap.put("sibiu",new String[]{"arad","fagaras","oradea","rimnicu_vilcea"});
		romaniaMap.put("timisoara",new String[]{"arad","lugoj"});
		romaniaMap.put("urziceni",new String[]{"bucharest","hirsova","vaslui"});
		romaniaMap.put("vaslui",new String[]{"iasi","urziceni"});
		romaniaMap.put("zerind",new String[]{"arad","oradea"});
		
		
		//Handling the command line arguments
		if(args.length>0 && args[0]!=null){
			if(args[0].equalsIgnoreCase("DFS")){
				System.out.println(DFS(args[1].trim().toLowerCase(),args[2].trim().toLowerCase()));
			}else if(args[0].equalsIgnoreCase("BFS")){
				System.out.println(BFS(args[1].toLowerCase().trim(),args[2].toLowerCase().trim()));
			}else
				System.out.println("Please enter correct search type!");
		}
		
	}
	
	/** Depth First Search Implementation **/
	/*
	 * Method to search using Depth First
	 * Takes two input as Source and Destination
	 * Returns a path from source to destination if there exists one
	 * Here the logic is start with source/root node push it to stack and mark visited
	 * Check the top of stack for it's unvisited child in increasing alphabetical order
	 * Traverse that child all the way down, if no child is left than pop the node and backtrack
	 * Exit loop when goal is found or stack is empty i.e. all children are visited
	 */
	public static String DFS(String source, String destination){
		Stack stack = new Stack();
		List<String> visitedDFS = new ArrayList<String>();
		//Push unvisited root to stack and mark it as visited
		stack.push(source);
		visitedDFS.add(source);
		while(!stack.isEmpty()){
			String current = (String) stack.peek();
			//check if goal node found?
			if(current.equals(destination))
				return visitedDFS.toString();
			//Get next possible unvisited node
			String next = getNextMoveDFS(current, visitedDFS);
			if(next == null){
				//if top of the stack has no unvisited node pop it
				stack.pop();
			}else{
				//push the unvisited node to stack and mark it as visited
				visitedDFS.add(next);
				stack.push(next);
			}
		}
		return ("No route found for the given city, please enter correct city name!");
	}
	
	//Method to find the next possible move for DFS traverse
	public static String getNextMoveDFS(String current, List visitedDFS){
		String[] adjacents = romaniaMap.get(current);
		//Here our algorithm will traverse alphabetical city name wise so before that we will sort the value array in map
		Arrays.sort(adjacents);
		for (int i = 0; i < adjacents.length; i++) {
			if(!visitedDFS.contains(adjacents[i])){
				return adjacents[i];
			}
		}
		return null;
	}
	
	
	/** Breadth First Search Implementation **/
	/*
	 * Method to search using Breadth First
	 * Takes two input as Source and Destination
	 * Returns a path from source to destination if there exists one
	 * Here the logic is start with source/root node push it to queue and mark visited
	 * Check the top of queue for it's unvisited child in increasing alphabetical order
	 * Traverse all the child for that node, if no child is left than remove the node and backtrack
	 * Exit loop when goal is found or queue is empty i.e. all children are visited
	 */
	public static String BFS(String source, String destination){
		Queue queue = new Queue();
		boolean found = false;
		List<String> visitedBFS = new ArrayList<String>();
		queue.add(source);
		while(!queue.isEmpty()){
			String current = (String) queue.remove();
			if(!visitedBFS.contains(current))
				//if node is not visited than mark it visited
				visitedBFS.add(current);
			if(current.equals(destination)){
				//if goal is found, exit
				found = true;
				return visitedBFS.toString();
			}
			String[] adjacents = romaniaMap.get(current);
			//Here our algorithm will traverse alphabetical city name wise so before that we will sort the value array in map
			Arrays.sort(adjacents);
			for (String string : adjacents) {
				if(!visitedBFS.contains(string)){
					//add unvisited child to queue 
					queue.add(string);
				}
			}
		}
		return ("No route found for the given city, please enter correct city name!");
	}
		
}



/** Linkedlist implementation of Stack **/
class Stack {

	private LinkedList list = new LinkedList<Object>();

	public void push(Object value){
		list.addFirst(value);
	}
	public Object pop(){
		return list.removeFirst();
	}
	public Object peek(){
		return list.peekFirst();
	}
	public int size(){
		return list.size();
	}
	public boolean isEmpty(){
		return list.size()==0;
	}
	public boolean contains(Object e){
		return list.contains(e);
	}

}

/** Linkedlist implementation of Queue **/
class Queue {

	private LinkedList list = new LinkedList<Object>();

	public void add(Object value){
		list.addFirst(value);
	}
	public Object remove(){
		return list.removeLast();
	}
	public Object peek(){
		return list.peekFirst();
	}
	public int size(){
		return list.size();
	}
	public boolean isEmpty(){
		return list.size()==0;
	}

}
