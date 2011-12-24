package org.rascalmpl.parser.gtd.stack.edge;

import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerMap;

public class EdgesSet{
	private final static int DEFAULT_SIZE = 4;
	
	private AbstractStackNode[] edges;
	private int size;
	
	private IntegerMap lastVisitedLevel;
	
	private IntegerKeyedHashMap<AbstractContainerNode> lastFilteredResults;
	
	public EdgesSet(){
		super();
		
		edges = new AbstractStackNode[DEFAULT_SIZE];
		size = 0;
		
		lastVisitedLevel = new IntegerMap();
		lastFilteredResults = new IntegerKeyedHashMap<AbstractContainerNode>();
	}
	
	public EdgesSet(int initialSize){
		super();
		
		edges = new AbstractStackNode[initialSize];
		size = 0;
		
		lastVisitedLevel = new IntegerMap();
		lastFilteredResults = new IntegerKeyedHashMap<AbstractContainerNode>();
	}
	
	private void enlarge(){
		AbstractStackNode[] oldEdges = edges;
		edges = new AbstractStackNode[size << 1];
		System.arraycopy(oldEdges, 0, edges, 0, size);
	}
	
	public void add(AbstractStackNode edge){
		while(size >= edges.length){
			enlarge();
		}
		
		edges[size++] = edge;
	}
	
	public boolean contains(AbstractStackNode node){
		for(int i = size - 1; i >= 0; --i){
			if(edges[i] == node) return true;
		}
		return false;
	}
	
	public boolean containsBefore(AbstractStackNode node, int limit){
		for(int i = limit - 1; i >= 0; --i){
			if(edges[i] == node) return true;
		}
		return false;
	}
	
	public boolean containsAfter(AbstractStackNode node, int limit){
		if(limit >= 0){ // Bounds check elimination helper.
			for(int i = size - 1; i >= limit; --i){
				if(edges[i] == node) return true;
			}
		}
		return false;
	}
	
	public AbstractStackNode get(int index){
		return edges[index];
	}
	
	public void setLastVisistedLevel(int level, int resultStoreId){
		lastVisitedLevel.put(resultStoreId, level);
	}
	
	public int getLastVisistedLevel(int resultStoreId){
		return lastVisitedLevel.get(resultStoreId);
	}
	
	public void setLastResult(AbstractContainerNode lastResult, int resultStoreId){
		lastFilteredResults.put(resultStoreId, lastResult);
	}
	
	public AbstractContainerNode getLastResult(int resultStoreId){
		return lastFilteredResults.get(resultStoreId);
	}
	
	public int size(){
		return size;
	}
	
	public void clear(){
		size = 0;
	}
}
