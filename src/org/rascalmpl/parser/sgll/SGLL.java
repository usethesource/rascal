package org.rascalmpl.parser.sgll;

import java.lang.reflect.Method;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.parser.sgll.result.INode;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.IReducableStackNode;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.parser.sgll.util.IntegerHashMap;

public abstract class SGLL implements IGLL{
	private char[] input;
	
	private final ArrayList<AbstractStackNode> todoList;
	
	// Updatable
	private final ArrayList<AbstractStackNode> stacksToExpand;
	private final ArrayList<AbstractStackNode> stacksWithTerminalsToReduce;
	private final ArrayList<AbstractStackNode> stacksWithNonTerminalsToReduce;
	private final ArrayList<AbstractStackNode[]> lastExpects;
	private final ArrayList<AbstractStackNode> possiblySharedExpects;
	private final ArrayList<AbstractStackNode> possiblySharedExpectsEndNodes;
	private final ArrayList<AbstractStackNode> possiblySharedNextNodes;
	private final IntegerHashMap<ArrayList<AbstractStackNode>> possiblySharedEdgeNodesMap;
	
	private int previousLocation;
	private int location;
	
	private AbstractStackNode root;
	
	public SGLL(){
		super();
		todoList = new ArrayList<AbstractStackNode>();
		
		stacksToExpand = new ArrayList<AbstractStackNode>();
		stacksWithTerminalsToReduce = new ArrayList<AbstractStackNode>();
		stacksWithNonTerminalsToReduce = new ArrayList<AbstractStackNode>();
		
		lastExpects = new ArrayList<AbstractStackNode[]>();
		possiblySharedExpects = new ArrayList<AbstractStackNode>();
		possiblySharedExpectsEndNodes = new ArrayList<AbstractStackNode>();
		
		possiblySharedNextNodes = new ArrayList<AbstractStackNode>();
		possiblySharedEdgeNodesMap = new IntegerHashMap<ArrayList<AbstractStackNode>>();
		
		previousLocation = -1;
		location = 0;
	}
	
	public void expect(IConstructor production, AbstractStackNode... symbolsToExpect){
		lastExpects.add(symbolsToExpect);
		
		AbstractStackNode lastNode = symbolsToExpect[symbolsToExpect.length - 1];
		lastNode.setParentProduction(production);
	}
	
	public void expect(IConstructor production, IReducableStackNode[] followRestrictions, AbstractStackNode... symbolsToExpect){
		lastExpects.add(symbolsToExpect);
		
		AbstractStackNode lastNode = symbolsToExpect[symbolsToExpect.length - 1];
		lastNode.setParentProduction(production);
		lastNode.setFollowRestriction(followRestrictions);
	}
	
	private void callMethod(String methodName){
		try{
			Method method = getClass().getMethod(methodName);
			method.invoke(this);
		}catch(Exception ex){
			// Not going to happen.
			ex.printStackTrace(); // Temp
		}
	}
	
	private AbstractStackNode updateNextNode(AbstractStackNode node){
		for(int i = possiblySharedNextNodes.size() - 1; i >= 0; i--){
			AbstractStackNode possibleAlternative = possiblySharedNextNodes.get(i);
			if(possibleAlternative.isSimilar(node)){
				if(node.hasEdges()){
					possibleAlternative.addEdges(node.getEdges());
				}
				return possibleAlternative;
			}
		}
		
		if(node.startLocationIsSet()){
			node = node.getCleanCopy();
		}
		
		node.setStartLocation(location);
		possiblySharedNextNodes.add(node);
		stacksToExpand.add(node);
		return node;
	}
	
	private AbstractStackNode updateEdgeNode(AbstractStackNode node){
		int startLocation = node.getStartLocation();
		ArrayList<AbstractStackNode> possiblySharedEdgeNodes = possiblySharedEdgeNodesMap.get(startLocation);
		if(possiblySharedEdgeNodes != null){
			for(int i = possiblySharedEdgeNodes.size() - 1; i >= 0; i--){
				AbstractStackNode possibleAlternative = possiblySharedEdgeNodes.get(i);
				if(possibleAlternative.isSimilar(node)){
					return possibleAlternative;
				}
			}
		}else{
			possiblySharedEdgeNodes = new ArrayList<AbstractStackNode>();
			possiblySharedEdgeNodesMap.unsafePut(startLocation, possiblySharedEdgeNodes);
		}
		
		if(node.isMarked()){
			node = node.getCleanCopyWithPrefix();
			node.setStartLocation(startLocation);
		}
		node.mark();
		
		possiblySharedEdgeNodes.add(node);
		stacksWithNonTerminalsToReduce.add(node);
		
		return node;
	}
	
	private void move(AbstractStackNode node){
		IConstructor production = node.getParentProduction();
		INode[][] results = node.getResults();
		int[] resultStartLocations = node.getResultStartLocations();
		
		ArrayList<AbstractStackNode> edges;
		if((edges = node.getEdges()) != null){
			for(int i = edges.size() - 1; i >= 0; i--){
				AbstractStackNode edge = edges.get(i);
				edge = updateEdgeNode(edge);
				addResults(production, edge, results, resultStartLocations);
			}
		}
		
		AbstractStackNode next;
		if((next = node.getNext()) != null){
			next = updateNextNode(next);
			addPrefixes(next, results, resultStartLocations);
		}
	}
	
	private void addPrefixes(AbstractStackNode next, INode[][] prefixes, int[] prefixStartLocations){
		for(int i = prefixes.length - 1; i >= 0; i--){
			next.addPrefix(prefixes[i], prefixStartLocations[i]);
		}
	}
	
	private void addResults(IConstructor production, AbstractStackNode edge, INode[][] results, int[] resultStartLocations){
		if(location == input.length && !edge.hasEdges() && !edge.hasNext()){
			root = edge; // Root reached.
		}
		
		int nrOfResults = results.length;
		for(int i = nrOfResults - 1; i >= 0; i--){
			if(edge.getStartLocation() == resultStartLocations[i]){
				edge.addResult(production, results[i]);
			}
		}
	}
	
	private void reduceTerminal(AbstractStackNode terminal){
		if(!terminal.reduce(input)) return;
		
		// Filtering
		if(terminal.isReductionFiltered(input, location)) return;
		
		move(terminal);
	}
	
	private void reduceNonTerminal(AbstractStackNode nonTerminal){
		// Filtering
		if(nonTerminal.isReductionFiltered(input, location)) return;
		
		move(nonTerminal);
	}
	
	private void reduce(){
		if(previousLocation != location){ // Epsilon fix.
			possiblySharedNextNodes.clear();
			possiblySharedEdgeNodesMap.clear();
		}
		
		// Reduce terminals.
		while(stacksWithTerminalsToReduce.size() > 0){
			AbstractStackNode terminal = stacksWithTerminalsToReduce.remove(stacksWithTerminalsToReduce.size() - 1);
			reduceTerminal(terminal);

			todoList.remove(terminal);
		}
		
		// Reduce non-terminals.
		while(stacksWithNonTerminalsToReduce.size() > 0){
			AbstractStackNode nonTerminal = stacksWithNonTerminalsToReduce.remove(stacksWithNonTerminalsToReduce.size() - 1);
			reduceNonTerminal(nonTerminal);
		}
	}
	
	private void findStacksToReduce(){
		// Find the stacks that will progress the least.
		int closestNextLocation = Integer.MAX_VALUE;
		for(int i = todoList.size() - 1; i >= 0; i--){
			AbstractStackNode node = todoList.get(i);
			int nextLocation = node.getStartLocation() + node.getLength();
			if(nextLocation < closestNextLocation){
				stacksWithTerminalsToReduce.clear();
				stacksWithTerminalsToReduce.add(node);
				closestNextLocation = nextLocation;
			}else if(nextLocation == closestNextLocation){
				stacksWithTerminalsToReduce.add(node);
			}
		}
		
		previousLocation = location;
		location = closestNextLocation;
	}
	
	private boolean shareNode(AbstractStackNode node, AbstractStackNode stack){
		if(!node.isEpsilon()){
			for(int j = possiblySharedExpects.size() - 1; j >= 0; j--){
				AbstractStackNode possiblySharedNode = possiblySharedExpects.get(j);
				if(possiblySharedNode.isSimilar(node)){
					possiblySharedExpectsEndNodes.get(j).addEdge(stack);
					return true;
				}
			}
		}
		return false;
	}
	
	private void handleExpects(AbstractStackNode stackBeingWorkedOn){
		for(int i = lastExpects.size() - 1; i >= 0; i--){
			AbstractStackNode[] expectedNodes = lastExpects.get(i);
			int numberOfNodes = expectedNodes.length;
			AbstractStackNode first = expectedNodes[0];
			
			// Handle sharing (and loops).
			if(!shareNode(first, stackBeingWorkedOn)){
				AbstractStackNode last = expectedNodes[numberOfNodes - 1].getCleanCopy();
				AbstractStackNode next = last;
				
				for(int k = numberOfNodes - 2; k >= 0; k--){
					AbstractStackNode current = expectedNodes[k].getCleanCopy();
					current.addNext(next);
					next = current;
				}
				
				last.addEdge(stackBeingWorkedOn);
				
				next.setStartLocation(location);
				
				stacksToExpand.add(next);
				possiblySharedExpects.add(next);
				possiblySharedExpectsEndNodes.add(last);
			}
		}
	}
	
	private void expandStack(AbstractStackNode node){
		if(node.isReducable()){
			if((location + node.getLength()) <= input.length) todoList.add(node);
			return;
		}
		
		if(!node.isList()){
			callMethod(node.getMethodName());
			
			handleExpects(node);
		}else{ // List
			AbstractStackNode[] listChildren = node.getChildren();
			
			AbstractStackNode child = listChildren[0];
			if(!shareNode(child, node)){
				stacksToExpand.add(child);
				possiblySharedExpects.add(child);
				possiblySharedExpectsEndNodes.add(child);
			}
			
			if(listChildren.length > 1){ // Star list or optional.
				child = listChildren[1];
				//if(!shareNode(child, node)){ This is always epsilon.
				stacksToExpand.add(child);
				possiblySharedExpects.add(child);
				possiblySharedExpectsEndNodes.add(child);
				//}
			}
		}
	}
	
	private void expand(){
		if(previousLocation != location){
			possiblySharedExpects.clear();
			possiblySharedExpectsEndNodes.clear();
		}
		while(stacksToExpand.size() > 0){
			lastExpects.clear(1);
			expandStack(stacksToExpand.remove(stacksToExpand.size() - 1));
		}
	}
	
	protected boolean isInLookAhead(char[][] ranges, char[] characters){
		char next = input[location];
		for(int i = ranges.length - 1; i >= 0; i--){
			char[] range = ranges[i];
			if(next >= range[0] && next <= range[1]) return true;
		}
		
		for(int i = characters.length - 1; i >= 0; i--){
			if(next == characters[i]) return true;
		}
		
		return false;
	}
	
	protected IValue parse(AbstractStackNode startNode, char[] input){
		// Initialize.
		this.input = input;
		AbstractStackNode rootNode = startNode.getCleanCopy();
		rootNode.setStartLocation(0);
		stacksToExpand.add(rootNode);
		expand();
		
		do{
			findStacksToReduce();
			
			reduce();
			
			expand();
		}while(todoList.size() > 0);
		
		if(root == null) throw new RuntimeException("Parse Error before: "+(location == Integer.MAX_VALUE ? 0 : location));
		
		return root.getResult().toTerm(new IndexedStack<INode>(), 0);
	}
}
