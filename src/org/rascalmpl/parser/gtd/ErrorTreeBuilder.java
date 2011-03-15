package org.rascalmpl.parser.gtd;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.result.ListContainerNode;
import org.rascalmpl.parser.gtd.result.SortContainerNode;
import org.rascalmpl.parser.gtd.result.error.ErrorListContainerNode;
import org.rascalmpl.parser.gtd.result.error.ErrorSortContainerNode;
import org.rascalmpl.parser.gtd.result.error.ExpectedNode;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.LinearIntegerKeyedMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashSet;
import org.rascalmpl.parser.gtd.util.Stack;
import org.rascalmpl.values.ValueFactoryFactory;

public class ErrorTreeBuilder{
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	private final static IList EMPTY_LIST = VF.list();
	
	private final SGTDBF parser;
	private final char[] input;
	private final int location;
	private final URI inputURI;
	
	private final DoubleStack<AbstractStackNode, AbstractNode> errorNodes;
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<String, AbstractContainerNode>> errorResultStoreCache;
	
	private final LinearIntegerKeyedMap<AbstractStackNode> sharedPrefixNext;
	
	public ErrorTreeBuilder(SGTDBF parser, char[] input, int location, URI inputURI){
		super();
		
		this.parser = parser;
		this.input = input;
		this.location = location;
		this.inputURI = inputURI;
		
		errorNodes = new DoubleStack<AbstractStackNode, AbstractNode>();
		errorResultStoreCache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<String,AbstractContainerNode>>();

		sharedPrefixNext = new LinearIntegerKeyedMap<AbstractStackNode>();
	}
	
	private AbstractStackNode updateNextNode(AbstractStackNode next, AbstractStackNode node, AbstractNode result){
		next = next.getCleanCopy();
		next.setStartLocation(location);
		next.updateNode(node, result);
		
		// TODO Add to queue.
		
		return next;
	}
	
	private void updateAlternativeNextNode(AbstractStackNode next, AbstractNode result, LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap, ArrayList<Link>[] prefixesMap){
		next = next.getCleanCopy();
		next.updatePrefixSharedNode(edgesMap, prefixesMap); // Prevent unnecessary overhead; share whenever possible.
		next.setStartLocation(location);
		
		// TODO Add to queue.
	}
	
	private void moveToNext(AbstractStackNode node, AbstractNode result){
		int nextDot = node.getDot() + 1;

		AbstractStackNode[] prod = node.getProduction();
		AbstractStackNode next = prod[nextDot];
		next.setProduction(prod);
		next = updateNextNode(next, node, result);
		
		ArrayList<AbstractStackNode[]> alternateProds = node.getAlternateProductions();
		if(alternateProds != null){
			int nextNextDot = nextDot + 1;
			
			// Handle alternative nexts (and prefix sharing).
			sharedPrefixNext.dirtyClear();
			
			sharedPrefixNext.add(next.getId(), next);
			
			LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = next.getEdges();
			ArrayList<Link>[] prefixesMap = next.getPrefixesMap();
			
			for(int i = alternateProds.size() - 1; i >= 0; --i){
				prod = alternateProds.get(i);
				if(nextDot == prod.length) continue;
				AbstractStackNode alternativeNext = prod[nextDot];
				int alternativeNextId = alternativeNext.getId();
				
				AbstractStackNode sharedNext = sharedPrefixNext.findValue(alternativeNextId);
				if(sharedNext == null){
					alternativeNext.setProduction(prod);
					updateAlternativeNextNode(alternativeNext, result, edgesMap, prefixesMap);
					
					sharedPrefixNext.add(alternativeNextId, alternativeNext);
				}else if(nextNextDot < prod.length){
					if(alternativeNext.isEndNode()){
						sharedNext.markAsEndNode();
						sharedNext.setParentProduction(alternativeNext.getParentProduction());
						sharedNext.setFollowRestriction(alternativeNext.getFollowRestriction());
						sharedNext.setReject(alternativeNext.isReject());
					}
					
					sharedNext.addProduction(prod);
				}
			}
		}
	}
	
	private void followEdges(AbstractStackNode node, AbstractNode result){
		IConstructor production = node.getParentProduction();
		
		IntegerList filteredParents = parser.getFilteredParents(node.getId());
		
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			int startLocation = edgesMap.getKey(i);
			ArrayList<AbstractStackNode> edgeList = edgesMap.getValue(i);
			
			ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = errorResultStoreCache.get(startLocation);
			
			if(levelResultStoreMap == null){
				levelResultStoreMap = new ObjectIntegerKeyedHashMap<String, AbstractContainerNode>();
				errorResultStoreCache.putUnsafe(startLocation, levelResultStoreMap);
			}
			
			Link resultLink = new Link((prefixesMap != null) ? prefixesMap[i] : null, result);
			
			ObjectIntegerKeyedHashMap<String, AbstractContainerNode> firstTimeReductions = new ObjectIntegerKeyedHashMap<String, AbstractContainerNode>();
			ObjectIntegerKeyedHashSet<String> firstTimeRegistration = new ObjectIntegerKeyedHashSet<String>();
			for(int j = edgeList.size() - 1; j >= 0; --j){
				AbstractStackNode edge = edgeList.get(j);
				String nodeName = edge.getName();
				int resultStoreId = parser.getResultStoreId(edge.getId());
				
				AbstractContainerNode resultStore = firstTimeReductions.get(nodeName, resultStoreId);
				if(resultStore == null){
					if(firstTimeRegistration.contains(nodeName, resultStoreId)) continue;
					firstTimeRegistration.putUnsafe(nodeName, resultStoreId);
					
					if(filteredParents == null || !filteredParents.contains(edge.getId())){
						resultStore = levelResultStoreMap.get(nodeName, resultStoreId);
						if(resultStore != null){
							if(!resultStore.isRejected()) resultStore.addAlternative(production, resultLink);
						}else{
							resultStore = (!edge.isList()) ? new ErrorSortContainerNode(EMPTY_LIST, inputURI, startLocation, location, edge.isSeparator(), edge.isLayout()) : new ErrorListContainerNode(EMPTY_LIST, inputURI, startLocation, location, edge.isSeparator(), edge.isLayout());
							levelResultStoreMap.putUnsafe(nodeName, resultStoreId, resultStore);
							resultStore.addAlternative(production, resultLink);
							
							errorNodes.push(edge, resultStore);
							firstTimeReductions.putUnsafe(nodeName, resultStoreId, resultStore);
						}
					}
				}else{
					errorNodes.push(edge, resultStore);
				}
			}
		}
	}
	
	private void move(AbstractStackNode node, AbstractNode result){
		if(node.isEndNode()){
			if(!result.isRejected()){
				if(!node.isReject()){
					followEdges(node, result);
				}else{
					// Ignore rejects.
				}
			}
		}
		
		if(node.hasNext()){
			moveToNext(node, result);
		}
	}
	
	IConstructor buildErrorTree(Stack<AbstractStackNode> unexpandableNodes, Stack<AbstractStackNode> unmatchableNodes, Stack<AbstractStackNode> filteredNodes){
		while(!unexpandableNodes.isEmpty()){
			AbstractStackNode unexpandableNode = unexpandableNodes.pop();
			
			// TODO Get the right symbol.
			AbstractNode resultStore = new ExpectedNode(null, inputURI, location, location, unexpandableNode.isSeparator(), unexpandableNode.isLayout());
			
			errorNodes.push(unexpandableNode, resultStore);
		}
		
		while(!unmatchableNodes.isEmpty()){
			AbstractStackNode unmatchableNode = unmatchableNodes.pop();
			
			int startLocation = unmatchableNode.getStartLocation();
			
			AbstractNode[] children = new AbstractNode[location - startLocation];
			for(int i = children.length - 1; i >= 0; --i){
				children[i] = new CharNode(input[startLocation - i]);
			}
			
			// TODO Construct proper node.
			
			errorNodes.push(unmatchableNode, null); // TODO Use the proper result store.
		}
		
		while(!filteredNodes.isEmpty()){
			AbstractStackNode filteredNode = filteredNodes.pop();
			
			AbstractNode resultStore = (!filteredNode.isList()) ? new ErrorSortContainerNode(EMPTY_LIST, inputURI, filteredNode.getStartLocation(), location, filteredNode.isSeparator(), filteredNode.isLayout()) : new ErrorListContainerNode(EMPTY_LIST, inputURI, filteredNode.getStartLocation(), location, filteredNode.isSeparator(), filteredNode.isLayout());
			
			// TODO Set children.
			
			errorNodes.push(filteredNode, resultStore);
		}
		
		while(!errorNodes.isEmpty()){
			AbstractStackNode errorStackNode = errorNodes.peekFirst();
			AbstractNode result = errorNodes.popSecond();
			
			move(errorStackNode, result);
		}
		
		return null; // Temp.
	}
}
