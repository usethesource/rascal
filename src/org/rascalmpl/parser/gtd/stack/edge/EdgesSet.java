package org.rascalmpl.parser.gtd.stack.edge;

import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.IntegerMap;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;

public class EdgesSet{
	public final static int DEFAULT_RESULT_STORE_ID = -1;
	
	private final static int DEFAULT_SIZE = 4;
	
	private AbstractStackNode[] edges;
	private int size;
	
	private int lastVisitedLevel = -1;
	private IntegerMap lastVisitedFilteredLevel;
	
	private AbstractContainerNode lastResults;
	private IntegerObjectList<AbstractContainerNode> lastFilteredResults;
	
	public EdgesSet(){
		super();
		
		edges = new AbstractStackNode[DEFAULT_SIZE];
		size = 0;
	}
	
	public EdgesSet(int initialSize){
		super();
		
		edges = new AbstractStackNode[initialSize];
		size = 0;
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
		if(resultStoreId == DEFAULT_RESULT_STORE_ID){
			lastVisitedLevel = level;
		}else{
			if(lastVisitedFilteredLevel == null){
				lastVisitedFilteredLevel = new IntegerMap();
			}
			
			lastVisitedFilteredLevel.put(resultStoreId, level);
		}
	}
	
	public int getLastVisistedLevel(int resultStoreId){
		if(resultStoreId == DEFAULT_RESULT_STORE_ID) return lastVisitedLevel;
		
		if(lastVisitedFilteredLevel == null){
			lastVisitedFilteredLevel = new IntegerMap();
			return -1;
		}
		return lastVisitedFilteredLevel.get(resultStoreId);
	}
	
	public void setLastResult(AbstractContainerNode lastResult, int resultStoreId){
		if(resultStoreId == DEFAULT_RESULT_STORE_ID){
			lastResults = lastResult;
		}else{
			if(lastFilteredResults == null){
				lastFilteredResults = new IntegerObjectList<AbstractContainerNode>(DEFAULT_SIZE);
			}
			
			lastFilteredResults.add(resultStoreId, lastResult);
		}
	}
	
	public AbstractContainerNode getLastResult(int resultStoreId){
		if(resultStoreId == DEFAULT_RESULT_STORE_ID) return lastResults;
		
		if(lastFilteredResults == null){
			lastFilteredResults = new IntegerObjectList<AbstractContainerNode>(DEFAULT_SIZE);
			return null;
		}
		return lastFilteredResults.findValue(resultStoreId);
	}
	
	public int size(){
		return size;
	}
	
	public void clear(){
		size = 0;
	}
}
