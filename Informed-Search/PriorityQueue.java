/*
 * Author : Mitkumar Pandya
 * Unity ID : mhpandya
 */
package com.meet.semester1.CSC520.InformedSearch.AStar;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//A priority queue implemented from min-heap
public class PriorityQueue {
	//List array of heap
	public List<Path> heapArray;
	public int currentIndex = 0;
	
	public PriorityQueue(){
		heapArray = new LinkedList<Path>();
	}
	
	public void insert(Path path){
		heapArray.add(currentIndex, path);
		percolateUp(currentIndex);
		currentIndex++;
	}
	
	public Path remove(){
		Path top = heapArray.get(0);
		Collections.swap(heapArray, 0, currentIndex-1);
		heapArray.remove(--currentIndex);
		if(currentIndex>1){
			percolateDown(0);
		}
		return top;
	}
	
	public int size(){
		return heapArray.size();
	}
	
	public void percolateUp(int index){
		//if current path distance is less than parent interchange them
		int parent = (index-1)/2;
		Path current = heapArray.get(index);
		while(index > 0 && heapArray.get(parent).heuristicDistance > current.heuristicDistance){
			Collections.swap(heapArray, index, parent);
			index = parent;
			parent = (parent-1)/2;
		}
	}
	
	public void percolateDown(int index){
		//after removing root and interchanging the last node with root
		//loop through it's children until you find the correct position for new root
		Path current = heapArray.get(index);
		int small;
		while(index < currentIndex/2){
			int left = 2*index + 1;
			int right = left + 1;
			if(right < currentIndex 
					&& heapArray.get(left).heuristicDistance > heapArray.get(right).heuristicDistance)
				
				small = right;
			else
				small = left;
			if(current.heuristicDistance <= heapArray.get(small).heuristicDistance)
				break;
			Collections.swap(heapArray, index, small);
			index = small;
		}
	}
	

}

