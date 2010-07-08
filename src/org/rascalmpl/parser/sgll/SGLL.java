package org.rascalmpl.parser.sgll;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.INode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.IReducableStackNode;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.DoubleArrayList;
import org.rascalmpl.parser.sgll.util.HashSet;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.parser.sgll.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.sgll.util.LinearIntegerKeyedMap;
import org.rascalmpl.parser.sgll.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.sgll.util.RotatingQueue;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public abstract class SGLL implements IGLL{
	private final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private char[] input;
	
	private final ArrayList<AbstractStackNode> todoList;
	
	// Updatable
	private final ArrayList<AbstractStackNode> stacksToExpand;
	private final RotatingQueue<AbstractStackNode> stacksWithTerminalsToReduce;
	private final RotatingQueue<AbstractStackNode> stacksWithNonTerminalsToReduce;
	private final ArrayList<AbstractStackNode[]> lastExpects;
	private final DoubleArrayList<AbstractStackNode, AbstractStackNode> possiblySharedExpects;
	private final ArrayList<AbstractStackNode> possiblySharedNextNodes;
	private final IntegerKeyedHashMap<ArrayList<AbstractStackNode>> possiblySharedEdgeNodesMap;

	private final ObjectIntegerKeyedHashMap<IConstructor, ContainerNode> resultStoreCache;
	private final HashSet<AbstractStackNode> withResults;
	
	private int previousLocation;
	private int location;
	
	private AbstractStackNode root;
	
	public SGLL(){
		super();
		todoList = new ArrayList<AbstractStackNode>();
		
		stacksToExpand = new ArrayList<AbstractStackNode>();
		stacksWithTerminalsToReduce = new RotatingQueue<AbstractStackNode>();
		stacksWithNonTerminalsToReduce = new RotatingQueue<AbstractStackNode>();
		
		lastExpects = new ArrayList<AbstractStackNode[]>();
		possiblySharedExpects = new DoubleArrayList<AbstractStackNode, AbstractStackNode>();
		
		possiblySharedNextNodes = new ArrayList<AbstractStackNode>();
		possiblySharedEdgeNodesMap = new IntegerKeyedHashMap<ArrayList<AbstractStackNode>>();
		
		resultStoreCache = new ObjectIntegerKeyedHashMap<IConstructor, ContainerNode>();
		withResults = new HashSet<AbstractStackNode>();
		
		previousLocation = -1;
		location = 0;
	}
	
	protected void expect(IConstructor production, AbstractStackNode... symbolsToExpect){
		lastExpects.add(symbolsToExpect);
		
		AbstractStackNode lastNode = symbolsToExpect[symbolsToExpect.length - 1];
		lastNode.setParentProduction(production);
	}
	
	protected void expect(IConstructor production, IReducableStackNode[] followRestrictions, AbstractStackNode... symbolsToExpect){
		lastExpects.add(symbolsToExpect);
		
		AbstractStackNode lastNode = symbolsToExpect[symbolsToExpect.length - 1];
		lastNode.setParentProduction(production);
		lastNode.setFollowRestriction(followRestrictions);
	}
	
	protected void expectReject(IConstructor production, AbstractStackNode... symbolsToExpect){
		lastExpects.add(symbolsToExpect);
		
		AbstractStackNode lastNode = symbolsToExpect[symbolsToExpect.length - 1];
		lastNode.setParentProduction(production);
		lastNode.markAsReject();
	}
	
	protected void expectReject(IConstructor production, IReducableStackNode[] followRestrictions, AbstractStackNode... symbolsToExpect){
		lastExpects.add(symbolsToExpect);
		
		AbstractStackNode lastNode = symbolsToExpect[symbolsToExpect.length - 1];
		lastNode.setParentProduction(production);
		lastNode.setFollowRestriction(followRestrictions);
		lastNode.markAsReject();
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
	
	private void updateProductionEndNode(AbstractStackNode sharedNode, AbstractStackNode node){
		AbstractStackNode prev = node;
		AbstractStackNode next = node.getNext();
		
		AbstractStackNode sharedPrev = sharedNode;
		AbstractStackNode sharedNext = sharedNode.getNext().getCleanCopy();
		do{
			prev = next;
			next = next.getNext();
			
			sharedNext = sharedNext.getCleanCopy();
			sharedPrev.addNext(sharedNext);
			
			sharedPrev = sharedNext;
			sharedNext = sharedNext.getNext();
			
			if(prev.hasEdges()){
				sharedPrev.addEdges(prev.getEdges());
			}
		}while(sharedNext != null);
	}
	
	private void updateNextNode(AbstractStackNode next, AbstractStackNode node){
		for(int i = possiblySharedNextNodes.size() - 1; i >= 0; i--){
			AbstractStackNode possibleAlternative = possiblySharedNextNodes.get(i);
			if(possibleAlternative.isSimilar(next)){
				if(next.hasEdges()){
					possibleAlternative.addEdges(next.getEdges());
				}else{
					// Don't lose any edges.
					updateProductionEndNode(possibleAlternative, next);
				}
				
				addPrefixes(possibleAlternative, node);
				
				if(!possibleAlternative.isClean()){
					// Something horrible happened; update the prefixes.
					updatePrefixes(possibleAlternative, node);
				}
				return;
			}
		}
		
		if(next.startLocationIsSet()){
			next = next.getCleanCopy();
		}
		
		next.setStartLocation(location);
		possiblySharedNextNodes.add(next);
		stacksToExpand.add(next);
		
		addPrefixes(next, node);
	}
	
	private void addPrefixes(AbstractStackNode next, AbstractStackNode node){
		LinearIntegerKeyedMap<ArrayList<Link>> prefixesMap = node.getPrefixesMap();
		INode result = node.getResult();
		
		if(prefixesMap == null){
			next.addPrefix(new Link(null, result), node.getStartLocation());
		}else{
			int nrOfPrefixes = prefixesMap.size();
			for(int i = nrOfPrefixes - 1; i >= 0; i--){
				int startLocation = prefixesMap.getKey(i);
				
				next.addPrefix(new Link(prefixesMap.getValue(i), result), startLocation);
			}
		}
	}
	
	private void updatePrefixes(AbstractStackNode next, AbstractStackNode node){
		IConstructor production = next.getParentProduction();
		
		LinearIntegerKeyedMap<ArrayList<Link>> prefixesMap = node.getPrefixesMap();
		INode result = node.getResult();
		
		// Update results (if necessary).
		ArrayList<AbstractStackNode> edges;
		if((edges = next.getEdges()) != null){
			for(int i = edges.size() - 1; i >= 0; i--){
				AbstractStackNode edge = edges.get(i);
				if(withResults.contains(edge)){
					Link prefix = constructPrefixesFor(prefixesMap, result, edge.getStartLocation());
					if(prefix != null){
						ArrayList<Link> edgePrefixes = new ArrayList<Link>();
						edgePrefixes.add(prefix);
						ContainerNode resultStore = edge.getResultStore();
						if(!resultStore.isRejected()){
							resultStore.addAlternative(production, new Link(edgePrefixes, next.getResult()));
						}
					}
				}
			}
		}
	}
	
	private void updateEdgeNode(AbstractStackNode node, ArrayList<Link> prefixes, INode result, IConstructor production){
		int startLocation = node.getStartLocation();
		ArrayList<AbstractStackNode> possiblySharedEdgeNodes = possiblySharedEdgeNodesMap.get(startLocation);
		if(possiblySharedEdgeNodes != null){
			for(int i = possiblySharedEdgeNodes.size() - 1; i >= 0; i--){
				AbstractStackNode possibleAlternative = possiblySharedEdgeNodes.get(i);
				if(possibleAlternative.isSimilar(node)){
					if(withResults.contains(possibleAlternative)){
						ContainerNode resultStore = possibleAlternative.getResultStore();
						if(!(possibleAlternative.isReject() || resultStore.isRejected())){
							resultStore.addAlternative(production, new Link(prefixes, result));
						}else{
							resultStore.setRejected(); // Rejected.
						}
					}
					return;
				}
			}
		}else{
			possiblySharedEdgeNodes = new ArrayList<AbstractStackNode>();
			possiblySharedEdgeNodesMap.unsafePut(startLocation, possiblySharedEdgeNodes);
		}
		
		if(!node.isClean()){
			node = node.getCleanCopyWithPrefix();
			node.setStartLocation(startLocation);
		}
		
		ContainerNode resultStore = resultStoreCache.get(production, startLocation);
		if(resultStore == null){
			if(!node.isReject()){
				resultStore = new ContainerNode();
				resultStoreCache.unsafePut(production, startLocation, resultStore);
				withResults.unsafePut(node);
				
				resultStore.addAlternative(production, new Link(prefixes, result));
			}else{
				return; // Rejected & pruned.
			}
		}
		node.setResultStore(resultStore);
		
		if(location == input.length && !node.hasEdges() && !node.hasNext()){
			root = node; // Root reached.
		}
		
		possiblySharedEdgeNodes.add(node);
		stacksWithNonTerminalsToReduce.put(node);
	}
	
	private void move(AbstractStackNode node){
		IConstructor production = node.getParentProduction();
		ArrayList<AbstractStackNode> edges;
		if((edges = node.getEdges()) != null){
			LinearIntegerKeyedMap<ArrayList<Link>> prefixesMap = node.getPrefixesMap();
			INode result = node.getResult();
			
			for(int i = edges.size() - 1; i >= 0; i--){
				AbstractStackNode edge = edges.get(i);
				ArrayList<Link> prefixes = null;
				if(prefixesMap != null){
					prefixes = prefixesMap.findValue(edge.getStartLocation());
					if(prefixes == null) continue;
				}
				updateEdgeNode(edge, prefixes, result, production);
			}
		}
		
		AbstractStackNode next;
		if((next = node.getNext()) != null){
			updateNextNode(next, node);
		}
	}
	
	private Link constructPrefixesFor(LinearIntegerKeyedMap<ArrayList<Link>> prefixesMap, INode result, int startLocation){
		if(prefixesMap == null){
			return new Link(null, result);
		}
		
		ArrayList<Link> prefixes = prefixesMap.findValue(startLocation);
		if(prefixes != null){
			return new Link(prefixes, result);
		}
		return null;
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
			resultStoreCache.clear();
			withResults.clear();
		}
		
		// Reduce terminals.
		while(!stacksWithTerminalsToReduce.isEmpty()){
			AbstractStackNode terminal = stacksWithTerminalsToReduce.unsafeGet();
			reduceTerminal(terminal);

			todoList.remove(terminal);
		}
		
		// Reduce non-terminals.
		while(!stacksWithNonTerminalsToReduce.isEmpty()){
			AbstractStackNode nonTerminal = stacksWithNonTerminalsToReduce.unsafeGet();
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
				stacksWithTerminalsToReduce.dirtyClear();
				stacksWithTerminalsToReduce.put(node);
				closestNextLocation = nextLocation;
			}else if(nextLocation == closestNextLocation){
				stacksWithTerminalsToReduce.put(node);
			}
		}
		
		previousLocation = location;
		location = closestNextLocation;
	}
	
	private boolean shareNode(AbstractStackNode node, AbstractStackNode stack){
		if(!node.isEpsilon()){
			for(int j = possiblySharedExpects.size() - 1; j >= 0; j--){
				AbstractStackNode possiblySharedNode = possiblySharedExpects.getFirst(j);
				if(possiblySharedNode.isSimilar(node)){
					possiblySharedExpects.getSecond(j).addEdge(stack);
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
				possiblySharedExpects.add(next, last);
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
				possiblySharedExpects.add(child, child);
			}
			
			if(listChildren.length > 1){ // Star list or optional.
				child = listChildren[1];
				// This is always epsilon; so shouldn't be shared.
				stacksToExpand.add(child);
			}
		}
	}
	
	private void expand(){
		if(previousLocation != location){
			possiblySharedExpects.clear();
		}
		while(stacksToExpand.size() > 0){
			lastExpects.dirtyClear();
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
		
		if(root == null){
			int errorLocation = (location == Integer.MAX_VALUE ? 0 : location);
			throw new SyntaxError("Parse Error before: "+errorLocation, vf.sourceLocation("-", errorLocation, 0, -1, -1, -1, -1));
		}
		
		IValue result = root.getResult().toTerm(new IndexedStack<INode>(), 0);
		
		if(result == null) throw new SyntaxError("Parse Error: all trees were filtered.", vf.sourceLocation("-"));
		
		return makeParseTree(result);
	}
	
	protected IValue parse(AbstractStackNode startNode, String inputString){
		return parse(startNode, inputString.toCharArray());
	}
	
	protected IValue parse(AbstractStackNode startNode, File inputFile) throws IOException{
		int inputFileLength = (int) inputFile.length();
		char[] input = new char[inputFileLength];
		Reader in = new BufferedReader(new FileReader(inputFile));
		try{
			in.read(input, 0, inputFileLength);
		}finally{
			in.close();
		}
		
		return parse(startNode, input);
	}
	
	protected IValue parse(AbstractStackNode startNode, Reader in) throws IOException{
		int segmentSize = 8192;
		ArrayList<char[]> segments = new ArrayList<char[]>();
		
		// Gather segments.
		int nrOfWholeSegments = -1;
		int bytesRead;
		do{
			char[] segment = new char[segmentSize];
			bytesRead = in.read(segment, 0, segmentSize);
			
			segments.add(segment);
			nrOfWholeSegments++;
		}while(bytesRead < segmentSize);
		
		// Glue the segments together.
		char[] segment = segments.get(nrOfWholeSegments);
		char[] input;
		if(bytesRead != -1){
			input = new char[(nrOfWholeSegments * segmentSize) + bytesRead];
			System.arraycopy(segment, 0, input, (nrOfWholeSegments * segmentSize), bytesRead);
		}else{
			input = new char[(nrOfWholeSegments * segmentSize)];
		}
		for(int i = nrOfWholeSegments - 1; i >= 0; i--){
			segment = segments.get(i);
			System.arraycopy(segment, 0, input, (i * segmentSize), segmentSize);
		}
		
		return parse(startNode, input);
	}
	
	private IValue makeParseTree(IValue tree){
		return vf.constructor(Factory.ParseTree_Top, tree, vf.integer(-1)); // Amb field is unsupported.
	}
}
