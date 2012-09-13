package org.rascalmpl.parser.gtd.stack.edge;

import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.IntegerMap;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;

@SuppressWarnings("unchecked")
public class EdgesSet<P>{
	public final static int DEFAULT_RESULT_STORE_ID = -1;
	
	private final static int DEFAULT_SIZE = 4;
	
	private AbstractStackNode<P>[] edges;
	private int size;
	
	private int lastVisitedLevel = -1;
	private IntegerMap lastVisitedFilteredLevel;
	
	private AbstractContainerNode<P> lastResults;
	private IntegerObjectList<AbstractContainerNode<P>> lastFilteredResults;
	
	public EdgesSet(){
		super();
		
		edges = (AbstractStackNode<P>[]) new AbstractStackNode[DEFAULT_SIZE];
		size = 0;
	}
	
	public EdgesSet(int initialSize){
		super();
		
		edges = (AbstractStackNode<P>[]) new AbstractStackNode[initialSize];
		size = 0;
	}
	
	private void enlarge(){
		AbstractStackNode<P>[] oldEdges = edges;
		edges = (AbstractStackNode<P>[]) new AbstractStackNode[size << 1];
		System.arraycopy(oldEdges, 0, edges, 0, size);
	}
	
	public void add(AbstractStackNode<P> edge){
		while(size >= edges.length){
			enlarge();
		}
		
		edges[size++] = edge;
	}
	
	public boolean contains(AbstractStackNode<P> node){
		for(int i = size - 1; i >= 0; --i){
			if(edges[i] == node) return true;
		}
		return false;
	}
	
	public boolean containsBefore(AbstractStackNode<P> node, int limit){
		for(int i = limit - 1; i >= 0; --i){
			if(edges[i] == node) return true;
		}
		return false;
	}
	
	public boolean containsAfter(AbstractStackNode<P> node, int limit){
		if(limit >= 0){ // Bounds check elimination helper.
			for(int i = size - 1; i >= limit; --i){
				if(edges[i] == node) return true;
			}
		}
		return false;
	}
	
	public AbstractStackNode<P> get(int index){
		return edges[index];
	}
	
	public void setLastVisitedLevel(int level, int resultStoreId){
		if(resultStoreId == DEFAULT_RESULT_STORE_ID){
			lastVisitedLevel = level;
		}else{
			if(lastVisitedFilteredLevel == null){
				lastVisitedFilteredLevel = new IntegerMap();
			}
			
			lastVisitedFilteredLevel.put(resultStoreId, level);
		}
	}
	
	public int getLastVisitedLevel(int resultStoreId){
		if(resultStoreId == DEFAULT_RESULT_STORE_ID) return lastVisitedLevel;
		
		if(lastVisitedFilteredLevel == null){
			lastVisitedFilteredLevel = new IntegerMap();
			return -1;
		}
		return lastVisitedFilteredLevel.get(resultStoreId);
	}
	
	public void setLastResult(AbstractContainerNode<P> lastResult, int resultStoreId){
		if(resultStoreId == DEFAULT_RESULT_STORE_ID){
			lastResults = lastResult;
		}else{
			if(lastFilteredResults == null){
				lastFilteredResults = new IntegerObjectList<AbstractContainerNode<P>>(DEFAULT_SIZE);
			}
			
			lastFilteredResults.add(resultStoreId, lastResult);
		}
	}
	
	public AbstractContainerNode<P> getLastResult(int resultStoreId){
		if(resultStoreId == DEFAULT_RESULT_STORE_ID) return lastResults;
		
		if(lastFilteredResults == null){
			lastFilteredResults = new IntegerObjectList<AbstractContainerNode<P>>(DEFAULT_SIZE);
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
