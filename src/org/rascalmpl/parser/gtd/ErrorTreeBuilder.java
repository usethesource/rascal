package org.rascalmpl.parser.gtd;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.result.AbstractNode.CycleMark;
import org.rascalmpl.parser.gtd.result.AbstractNode.FilteringTracker;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.action.VoidActionExecutor;
import org.rascalmpl.parser.gtd.result.error.ErrorListContainerNode;
import org.rascalmpl.parser.gtd.result.error.ErrorSortContainerNode;
import org.rascalmpl.parser.gtd.result.error.ExpectedNode;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.LinearIntegerKeyedMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashSet;
import org.rascalmpl.parser.gtd.util.Stack;
import org.rascalmpl.parser.gtd.util.specific.PositionStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class ErrorTreeBuilder{
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	private final static IList EMPTY_LIST = VF.list();
	
	private final SGTDBF parser;
	private final AbstractStackNode startNode;
	private final PositionStore positionStore;
	private final IActionExecutor actionExecutor;
	
	private final char[] input;
	private final int location;
	private final URI inputURI;
	
	private final DoubleStack<AbstractStackNode, AbstractNode> errorNodes;
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<String, AbstractContainerNode>> errorResultStoreCache;
	
	private final LinearIntegerKeyedMap<AbstractStackNode> sharedPrefixNext;
	
	public ErrorTreeBuilder(SGTDBF parser, AbstractStackNode startNode, PositionStore positionStore, IActionExecutor actionExecutor, char[] input, int location, URI inputURI){
		super();
		
		this.parser = parser;
		this.startNode = startNode;
		this.positionStore = positionStore;
		this.actionExecutor = actionExecutor;
		
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
		
		int dot = next.getDot();
		IList productionElements = getProductionElements(next);
		IConstructor nextSymbol = (IConstructor) productionElements.get(dot);
		AbstractNode resultStore = new ExpectedNode(new AbstractNode[]{}, nextSymbol, inputURI, location, location, next.isSeparator(), next.isLayout());
		
		errorNodes.push(next, resultStore);
		
		return next;
	}
	
	private void updateAlternativeNextNode(AbstractStackNode next, LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap, ArrayList<Link>[] prefixesMap){
		next = next.getCleanCopy();
		next.updatePrefixSharedNode(edgesMap, prefixesMap); // Prevent unnecessary overhead; share whenever possible.
		next.setStartLocation(location);
		
		int dot = next.getDot();
		IList productionElements = getProductionElements(next);
		IConstructor nextSymbol = (IConstructor) productionElements.get(dot);
		AbstractNode resultStore = new ExpectedNode(new AbstractNode[]{}, nextSymbol, inputURI, location, location, next.isSeparator(), next.isLayout());
		
		errorNodes.push(next, resultStore);
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
					updateAlternativeNextNode(alternativeNext, edgesMap, prefixesMap);
					
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
	
	private boolean followEdges(AbstractStackNode node, AbstractNode result){
		IConstructor production = node.getParentProduction();
		
		boolean wasListChild = ProductionAdapter.isRegular(production);
		
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
		
		return wasListChild;
	}
	
	private void move(AbstractStackNode node, AbstractNode result){
		boolean handleNexts = true;
		if(node.isEndNode()){
			if(!result.isRejected()){
				if(!node.isReject()){
					handleNexts = !followEdges(node, result);
				}else{
					// Ignore rejects.
				}
			}
		}
		
		if(handleNexts && node.hasNext()){
			moveToNext(node, result);
		}
	}
	
	private IConstructor getParentSymbol(AbstractStackNode node){
		AbstractStackNode[] production = node.getProduction();
		AbstractStackNode last = production[production.length - 1];
		return ProductionAdapter.getRhs(last.getParentProduction());
	}
	
	private IList getProductionElements(AbstractStackNode node){
		AbstractStackNode[] production = node.getProduction();
		AbstractStackNode last = production[production.length - 1];
		
		IConstructor prod = last.getParentProduction();
		if(!ProductionAdapter.isRegular(prod)){
			return ProductionAdapter.getLhs(prod);
		}
		
		// Regular
		IConstructor rhs = ProductionAdapter.getRhs(prod);
		IConstructor symbol = (IConstructor) rhs.get("symbol");
		if(SymbolAdapter.isIterPlusSeps(rhs) || SymbolAdapter.isIterStarSeps(rhs)){
			IList separators = (IList) rhs.get("separators");
			
			IListWriter listChildrenWriter = VF.listWriter();
			listChildrenWriter.insert(symbol);
			for(int i = separators.length(); i >= 0; --i){
				listChildrenWriter.insert(separators.get(i));
			}
			listChildrenWriter.insert(symbol);
			
			return listChildrenWriter.done();
		}
		
		return VF.list(symbol);
	}
	
	IConstructor buildErrorTree(Stack<AbstractStackNode> unexpandableNodes, Stack<AbstractStackNode> unmatchableNodes, DoubleStack<AbstractStackNode, AbstractNode> filteredNodes){
		while(!unexpandableNodes.isEmpty()){
			AbstractStackNode unexpandableNode = unexpandableNodes.pop();
			
			IConstructor symbol = getParentSymbol(unexpandableNode);
			AbstractNode resultStore = new ExpectedNode(new AbstractNode[]{}, symbol, inputURI, location, location, unexpandableNode.isSeparator(), unexpandableNode.isLayout());
			
			errorNodes.push(unexpandableNode, resultStore);
		}
		
		while(!unmatchableNodes.isEmpty()){
			AbstractStackNode unmatchableNode = unmatchableNodes.pop();
			
			int startLocation = unmatchableNode.getStartLocation();
			
			AbstractNode[] children = new AbstractNode[location - startLocation];
			for(int i = children.length - 1; i >= 0; --i){
				children[i] = new CharNode(input[startLocation - i]);
			}
			
			int dot = unmatchableNode.getDot();
			IList productionElements = getProductionElements(unmatchableNode);
			IConstructor symbol = (IConstructor) productionElements.get(dot);
			
			AbstractNode result = new ExpectedNode(children, symbol, inputURI, startLocation, location, unmatchableNode.isSeparator(), unmatchableNode.isLayout());
			
			errorNodes.push(unmatchableNode, result);
		}
		
		while(!filteredNodes.isEmpty()){
			AbstractStackNode filteredNode = filteredNodes.peekFirst();
			AbstractNode resultStore = filteredNodes.popSecond();
			
			errorNodes.push(filteredNode, resultStore);
		}
		
		while(!errorNodes.isEmpty()){
			AbstractStackNode errorStackNode = errorNodes.peekFirst();
			AbstractNode result = errorNodes.popSecond();
			
			move(errorStackNode, result);
		}
		
		// TODO Handle the post-parse reject and action filtering mess.
		
		ObjectIntegerKeyedHashMap<String, AbstractContainerNode> levelResultStoreMap = errorResultStoreCache.get(0);
		AbstractContainerNode result = levelResultStoreMap.get(startNode.getName(), parser.getResultStoreId(startNode.getId()));
		FilteringTracker filteringTracker = new FilteringTracker();
		IConstructor resultTree = result.toTerm(new IndexedStack<AbstractNode>(), 0, new CycleMark(), positionStore, filteringTracker, new VoidActionExecutor());
		return resultTree;
	}
}
