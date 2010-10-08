package org.rascalmpl.parser.sgll;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.parser.sgll.result.AbstractContainerNode;
import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.ListContainerNode;
import org.rascalmpl.parser.sgll.result.SortContainerNode;
import org.rascalmpl.parser.sgll.result.AbstractNode.CycleMark;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.IMatchableStackNode;
import org.rascalmpl.parser.sgll.stack.NonTerminalStackNode;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.DoubleRotatingQueue;
import org.rascalmpl.parser.sgll.util.HashMap;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.parser.sgll.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.sgll.util.LinearIntegerKeyedMap;
import org.rascalmpl.parser.sgll.util.RotatingQueue;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public abstract class SGLL implements IGLL{
	private final static int STREAM_READ_SEGMENT_SIZE = 8192;
	
	protected final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private URI inputURI;
	protected char[] input;
	private final PositionStore positionStore;
	
	private final ArrayList<AbstractStackNode> todoList;
	
	private final ArrayList<AbstractStackNode> stacksToExpand;
	private final RotatingQueue<AbstractStackNode> stacksWithTerminalsToReduce;
	private final DoubleRotatingQueue<AbstractStackNode, AbstractNode> stacksWithNonTerminalsToReduce;
	
	private final ArrayList<AbstractStackNode[]> lastExpects;
	private final LinearIntegerKeyedMap<AbstractStackNode> sharedLastExpects;
	private final LinearIntegerKeyedMap<AbstractStackNode> sharedPrefixNext;
	private final HashMap<String, LinearIntegerKeyedMap<AbstractStackNode>> cachedExpects;
	protected int currentParentId;
	
	private final IntegerKeyedHashMap<AbstractStackNode> sharedNextNodes;

	private final IntegerKeyedHashMap<HashMap<String, AbstractContainerNode>> resultStoreCache;
	
	private int previousLocation;
	protected int location;
	
	protected char lookAheadChar;
	
	private AbstractStackNode root;
	
	private final HashMap<String, Method> methodCache;
	
	public SGLL(){
		super();
		
		positionStore = new PositionStore();
		
		todoList = new ArrayList<AbstractStackNode>();
		
		stacksToExpand = new ArrayList<AbstractStackNode>();
		stacksWithTerminalsToReduce = new RotatingQueue<AbstractStackNode>();
		stacksWithNonTerminalsToReduce = new DoubleRotatingQueue<AbstractStackNode, AbstractNode>();
		
		lastExpects = new ArrayList<AbstractStackNode[]>();
		sharedLastExpects = new LinearIntegerKeyedMap<AbstractStackNode>();
		sharedPrefixNext = new LinearIntegerKeyedMap<AbstractStackNode>();
		cachedExpects = new HashMap<String, LinearIntegerKeyedMap<AbstractStackNode>>();
		
		sharedNextNodes = new IntegerKeyedHashMap<AbstractStackNode>();
		
		resultStoreCache = new IntegerKeyedHashMap<HashMap<String, AbstractContainerNode>>();
		
		previousLocation = -1;
		location = 0;
		
		methodCache = new HashMap<String, Method>();
	}
	
	protected void expect(IConstructor production, AbstractStackNode... symbolsToExpect){
		lastExpects.add(symbolsToExpect);
		
		AbstractStackNode lastNode = symbolsToExpect[symbolsToExpect.length - 1];
		lastNode.setParentProduction(production);
	}
	
	protected void expect(IConstructor production, IMatchableStackNode[] followRestrictions, AbstractStackNode... symbolsToExpect){
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
	
	protected void expectReject(IConstructor production, IMatchableStackNode[] followRestrictions, AbstractStackNode... symbolsToExpect){
		lastExpects.add(symbolsToExpect);
		
		AbstractStackNode lastNode = symbolsToExpect[symbolsToExpect.length - 1];
		lastNode.setParentProduction(production);
		lastNode.setFollowRestriction(followRestrictions);
		lastNode.markAsReject();
	}
	
	protected void invokeExpects(AbstractStackNode nonTerminal){
		String name = nonTerminal.getName();
		Method method = methodCache.get(name);
		if(method == null){
			try{
				method = getClass().getMethod(name);
				try{
					method.setAccessible(true); // Try to bypass the 'isAccessible' check to save time.
				}catch(SecurityException sex){
					// Ignore this if it happens.
				}
			}catch(NoSuchMethodException nsmex){
				throw new ImplementationError(nsmex.getMessage(), nsmex);
			}
			methodCache.putUnsafe(name, method);
		}
		
		try{
			currentParentId = nonTerminal.getId();
			method.invoke(this);
		}catch(IllegalAccessException iaex){
			throw new ImplementationError(iaex.getMessage(), iaex);
		}catch(InvocationTargetException itex){
			throw new ImplementationError(itex.getTargetException().getMessage(), itex.getTargetException());
		} 
	}
	
	private AbstractStackNode updateNextNode(AbstractStackNode next, AbstractStackNode node, AbstractNode result){
		int id = next.getId();
		AbstractStackNode alternative = sharedNextNodes.get(id);
		if(alternative != null){
			alternative.updateNode(node, result);
			
			if(alternative.isEndNode()){
				if(result.isNullable() && !node.isMatchable() && !next.isMatchable() && node.getName() == next.getName()){
					if(alternative.getId() != node.getId()){ // List cycle fix.
						HashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(location);
						AbstractContainerNode resultStore = levelResultStoreMap.get(alternative.getName());
						if(resultStore != null){
							// Encountered self recursive epsilon cycle; update the prefixes.
							updatePrefixes(alternative, node, resultStore);
						}
					}
				}
			}
			return alternative;
		}
		
		next = next.getCleanCopy();
		next.setStartLocation(location);
		next.updateNode(node, result);
		
		if(!next.isMatchable()){ // Is non-terminal or list.
			HashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(location);
			if(levelResultStoreMap != null){
				AbstractContainerNode resultStore = levelResultStoreMap.get(next.getName());
				if(resultStore != null){ // Is nullable, queue for reduction.
					stacksWithNonTerminalsToReduce.put(next, resultStore);
				}
			}
		}
		
		sharedNextNodes.putUnsafe(id, next);
		stacksToExpand.add(next);
		
		return next;
	}
	
	private void updateAlternativeNextNode(AbstractStackNode next, LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap, ArrayList<Link>[] prefixesMap){
		int id = next.getId();
		AbstractStackNode alternative = sharedNextNodes.get(id);
		if(alternative != null){
			alternative.updatePrefixSharedNode(edgesMap, prefixesMap); // Prevent unnecessary overhead; share whenever possible.
		}else{
			next = next.getCleanCopy();
			next.updatePrefixSharedNode(edgesMap, prefixesMap); // Prevent unnecessary overhead; share whenever possible.
			next.setStartLocation(location);
			
			if(!next.isMatchable()){ // Is non-terminal or list.
				HashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(location);
				if(levelResultStoreMap != null){
					AbstractContainerNode resultStore = levelResultStoreMap.get(next.getName());
					if(resultStore != null){ // Is nullable, add the known results.
						stacksWithNonTerminalsToReduce.put(next, resultStore);
					}
				}
			}
			
			sharedNextNodes.putUnsafe(id, next);
			stacksToExpand.add(next);
		}
	}
	
	private void updatePrefixes(AbstractStackNode next, AbstractStackNode node, AbstractNode resultStore){
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		IConstructor production = next.getParentProduction();
		
		// Update one (because of sharing all will be updated).
		ArrayList<Link> edgePrefixes = new ArrayList<Link>();
		Link prefix = constructPrefixesFor(edgesMap, prefixesMap, resultStore, location);
		edgePrefixes.add(prefix);
		
		ArrayList<AbstractStackNode> edgesPart = edgesMap.findValue(location);
		AbstractStackNode edge = edgesPart.get(0);
		
		HashMap<String, AbstractContainerNode>  levelResultStoreMap = resultStoreCache.get(location);
		levelResultStoreMap.get(edge.getName()).addAlternative(production, new Link(edgePrefixes, resultStore));
	}
	
	private void updateEdges(AbstractStackNode node, AbstractNode result){
		IConstructor production = node.getParentProduction();
		
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			int startLocation = edgesMap.getKey(i);
			ArrayList<AbstractStackNode> edgeList = edgesMap.getValue(i);
			
			AbstractStackNode edge = edgeList.get(0);
			String nodeName = edge.getName();
			HashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(startLocation);
			AbstractContainerNode resultStore = null;
			if(levelResultStoreMap != null){
				resultStore = levelResultStoreMap.get(nodeName);
			}else{
				levelResultStoreMap = new HashMap<String, AbstractContainerNode>();
				resultStoreCache.putUnsafe(startLocation, levelResultStoreMap);
			}
			Link resultLink = new Link((prefixesMap != null) ? prefixesMap[i] : null, result);
			if(resultStore != null){
				if(!resultStore.isRejected()) resultStore.addAlternative(production, resultLink);
			}else{
				resultStore = (!edge.isList()) ? new SortContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout()) : new ListContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout());
				levelResultStoreMap.putUnsafe(nodeName, resultStore);
				resultStore.addAlternative(production, resultLink);
				
				stacksWithNonTerminalsToReduce.put(edge, resultStore);
				if(location == input.length && !edge.hasEdges()){
					root = edge; // Root reached.
				}
				
				for(int j = edgeList.size() - 1; j >= 1; --j){
					edge = edgeList.get(j);
					
					stacksWithNonTerminalsToReduce.put(edge, resultStore);
					if(location == input.length && !edge.hasEdges()){
						root = edge; // Root reached.
					}
				}
			}
		}
	}
	
	private void updateRejects(AbstractStackNode node){
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			int startLocation = edgesMap.getKey(i);
			ArrayList<AbstractStackNode> edgeList = edgesMap.getValue(i);
			
			AbstractStackNode edge = edgeList.get(0);
			String nodeName = edge.getName();
			HashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(startLocation);
			AbstractContainerNode resultStore = null;
			if(levelResultStoreMap != null){
				resultStore = levelResultStoreMap.get(nodeName);
			}else{
				levelResultStoreMap = new HashMap<String, AbstractContainerNode>();
				resultStoreCache.putUnsafe(startLocation, levelResultStoreMap);
			}
			if(resultStore != null){
				resultStore.setRejected();
			}else{
				resultStore = (!edge.isList()) ? new SortContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout()) : new ListContainerNode(inputURI, startLocation, location, startLocation == location, edge.isSeparator(), edge.isLayout());
				levelResultStoreMap.putUnsafe(nodeName, resultStore);
				resultStore.setRejected();
				
				
				stacksWithNonTerminalsToReduce.put(edge, resultStore);
				if(location == input.length && !edge.hasEdges()){
					root = edge; // Root reached.
				}
				
				for(int j = edgeList.size() - 1; j >= 1; --j){
					edge = edgeList.get(j);
					
					stacksWithNonTerminalsToReduce.put(edge, resultStore);
					if(location == input.length && !edge.hasEdges()){
						root = edge; // Root reached.
					}
				}
			}
		}
	}
	
	private void move(AbstractStackNode node, AbstractNode result){
		if(node.isEndNode()){
			if(!node.isReject()){
				updateEdges(node, result);
			}else if(node.isMatchable() || !result.isRejected()){
				updateRejects(node);
			}
		}

		if(node.hasNext()){
			int nextDot = node.getDot() + 1;

			AbstractStackNode[] prod = node.getNext();
			AbstractStackNode next = prod[nextDot];
			next.setNext(prod);
			next = updateNextNode(next, node, result);
			
			ArrayList<AbstractStackNode[]> alternateProds = node.getAlternateNexts();
			if(alternateProds != null){
				int nextNextDot = nextDot + 1;
				
				// Handle alternative nexts (and prefix sharing).
				sharedPrefixNext.dirtyClear();
				
				sharedPrefixNext.add(next.getId(), next);
				
				LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = next.getEdges();
				ArrayList<Link>[] prefixesMap = next.getPrefixesMap();
				
				for(int i = alternateProds.size() - 1; i >= 0; --i){
					prod = alternateProds.get(i);
					AbstractStackNode alternativeNext = prod[nextDot];
					int alternativeNextId = alternativeNext.getId();
					
					AbstractStackNode sharedNext = sharedPrefixNext.findValue(alternativeNextId);
					if(sharedNext == null){
						alternativeNext.setNext(prod);
						updateAlternativeNextNode(alternativeNext, edgesMap, prefixesMap);
						
						sharedPrefixNext.add(alternativeNextId, alternativeNext);
					}else if(nextNextDot < prod.length){
						if(sharedNext.hasNext()){
							sharedNext.addNext(prod);
						}else{
							sharedNext.setNext(prod);
						}
					}
				}
			}
		}
	}
	
	private Link constructPrefixesFor(LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap, ArrayList<Link>[] prefixesMap, AbstractNode result, int startLocation){
		if(prefixesMap == null){
			return new Link(null, result);
		}
		
		int index = edgesMap.findKey(startLocation);
		return new Link(prefixesMap[index], result);
	}
	
	private void reduceTerminal(AbstractStackNode terminal){
		if(terminal.isLocatable()) terminal.setPositionStore(positionStore); // Ugly, but necessary.
		
		if(!terminal.match(input)) return;
		
		// Filtering
		if(terminal.isReductionFiltered(input, location)) return;
		
		move(terminal, terminal.getResult());
	}
	
	private void reduceNonTerminal(AbstractStackNode nonTerminal, AbstractNode result){
		// Filtering
		if(nonTerminal.isReductionFiltered(input, location)) return;
		
		move(nonTerminal, result);
	}
	
	private void reduce(){
		if(previousLocation != location){ // Epsilon fix.
			sharedNextNodes.clear();
			resultStoreCache.clear();
		}
		
		// Reduce terminals.
		while(!stacksWithTerminalsToReduce.isEmpty()){
			AbstractStackNode terminal = stacksWithTerminalsToReduce.getDirtyUnsafe();
			reduceTerminal(terminal);

			todoList.remove(terminal);
		}
		
		// Reduce non-terminals.
		while(!stacksWithNonTerminalsToReduce.isEmpty()){
			reduceNonTerminal(stacksWithNonTerminalsToReduce.peekFirstUnsafe(), stacksWithNonTerminalsToReduce.getSecondDirtyUnsafe());
		}
	}
	
	private void findStacksToReduce(){
		// Find the stacks that will progress the least.
		int closestNextLocation = Integer.MAX_VALUE;
		for(int i = todoList.size() - 1; i >= 0; --i){
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
	
	private boolean shareListNode(int id, AbstractStackNode stack){
		AbstractStackNode sharedNode = sharedNextNodes.get(id);
		if(sharedNode != null){
			sharedNode.addEdgeWithPrefix(stack, null, location);
			return true;
		}
		return false;
	}
	
	private void handleExpects(AbstractStackNode stackBeingWorkedOn){
		int parentId = stackBeingWorkedOn.getId();
		
		sharedLastExpects.dirtyClear();
		
		int nrOfExpects = lastExpects.size();
		LinearIntegerKeyedMap<AbstractStackNode> expects = new LinearIntegerKeyedMap<AbstractStackNode>(nrOfExpects);
		
		for(int i = nrOfExpects - 1; i >= 0; --i){
			AbstractStackNode[] expectedNodes = lastExpects.get(i);
			
			AbstractStackNode last = expectedNodes[expectedNodes.length - 1];
			if(isPrioFiltered(parentId, last.getId())){
				continue;
			}
			last.markAsEndNode();
			last.setParentProduction(last.getParentProduction());
			last.setFollowRestriction(last.getFollowRestriction());
			last.setReject(last.isReject());
			
			AbstractStackNode first = expectedNodes[0];
			
			// Handle prefix sharing.
			int firstId = first.getId();
			AbstractStackNode sharedNode;
			if((sharedNode = sharedLastExpects.findValue(firstId)) != null){
				sharedNode.addNext(expectedNodes);
				continue;
			}
			
			first = first.getCleanCopy();
			first.setStartLocation(location);
			first.setNext(expectedNodes);
			first.initEdges();
			first.addEdge(stackBeingWorkedOn);
			
			sharedLastExpects.add(firstId, first);
			
			stacksToExpand.add(first);
			
			expects.add(last.getId(), first);
		}
		
		cachedExpects.put(stackBeingWorkedOn.getName(), expects);
	}
	
	protected boolean isPrioFiltered(int parentId, int childId){
		return false; // Default implementation; intended to be overwritten in sub-classes.
	}
	
	private void expandStack(AbstractStackNode stack){
		if(stack.isMatchable()){
			if((location + stack.getLength()) <= input.length) todoList.add(stack);
			return;
		}
		
		if(!stack.isList()){
			LinearIntegerKeyedMap<AbstractStackNode> expects = cachedExpects.get(stack.getName());
			if(expects != null){
				int parentId = stack.getId();
				for(int i = expects.size() - 1; i >= 0; --i){
					if(!isPrioFiltered(parentId, expects.getKey(i))){
						AbstractStackNode expect = expects.getValue(i);
						if(!expect.hasEdges()) stacksToExpand.add(expect);
						expect.addEdge(stack);
					}
				}
			}else{
				invokeExpects(stack);
				
				handleExpects(stack);
			}
		}else{ // List
			AbstractStackNode[] listChildren = stack.getChildren();
			
			AbstractStackNode child = listChildren[0];
			int childId = child.getId();
			if(!shareListNode(childId, stack)){
				child = child.getCleanCopy();
				
				sharedNextNodes.putUnsafe(childId, child);
				
				child.setStartLocation(location);
				child.initEdges();
				child.addEdgeWithPrefix(stack, null, location);
				
				stacksToExpand.add(child);
			}
			
			if(listChildren.length > 1){ // Star list or optional.
				// This is always epsilon (and unique for this position); so shouldn't be shared.
				AbstractStackNode empty = listChildren[1].getCleanCopy();
				empty.setStartLocation(location);
				empty.initEdges();
				empty.addEdge(stack);
				
				stacksToExpand.add(empty);
			}
		}
	}
	
	private void expand(){
		if(previousLocation != location){
			cachedExpects.clear();
		}
		while(stacksToExpand.size() > 0){
			lastExpects.dirtyClear();
			expandStack(stacksToExpand.remove(stacksToExpand.size() - 1));
		}
	}
	
	protected boolean isAtEndOfInput(){
		return (location == input.length);
	}
	
	protected boolean isInLookAhead(char[][] ranges, char[] characters){
		if(location == input.length) return false;
		
		for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			if(lookAheadChar >= range[0] && lookAheadChar <= range[1]) return true;
		}
		
		for(int i = characters.length - 1; i >= 0; --i){
			if(lookAheadChar == characters[i]) return true;
		}
		
		return false;
	}
	
	protected IConstructor parse(AbstractStackNode startNode, URI inputURI, char[] input){
		// Initialize.
		this.inputURI = inputURI;
		this.input = input;
		positionStore.index(input);

		AbstractStackNode rootNode = startNode.getCleanCopy();
		rootNode.initEdges();
		rootNode.setStartLocation(0);
		stacksToExpand.add(rootNode);
		lookAheadChar = (input.length > 0) ? input[0] : 0;
		expand();
		
		do{
			findStacksToReduce();
			
			reduce();

			lookAheadChar = (location < input.length) ? input[location] : 0;
			expand();
		}while(todoList.size() > 0);
		
		if(root == null){
			int errorLocation = (location == Integer.MAX_VALUE ? 0 : location);
			int line = positionStore.findLine(errorLocation) + 1;
			int column = positionStore.getColumn(errorLocation, line);
			throw new SyntaxError("Parse Error before: "+errorLocation, vf.sourceLocation(inputURI, errorLocation, 0, line, line, column, column));
		}
		
		HashMap<String, AbstractContainerNode> levelResultStoreMap = resultStoreCache.get(0);
		IConstructor result = levelResultStoreMap.get(startNode.getName()).toTerm(new IndexedStack<AbstractNode>(), 0, new CycleMark(), positionStore);
		
		if(result == null) throw new SyntaxError("Parse Error: all trees were filtered.", vf.sourceLocation(inputURI));
		
		return makeParseTree(result);
	}
	
	private IConstructor makeParseTree(IConstructor tree){
		return vf.constructor(Factory.ParseTree_Top, tree, vf.integer(-1)); // Amb counter is unsupported.
	}
	
	protected IConstructor parseFromString(AbstractStackNode startNode, URI inputURI, String inputString){
		return parse(startNode, inputURI, inputString.toCharArray());
	}
	
	protected IConstructor parseFromFile(AbstractStackNode startNode, URI inputURI, File inputFile) throws IOException{
		int inputFileLength = (int) inputFile.length();
		char[] input = new char[inputFileLength];
		Reader in = new BufferedReader(new FileReader(inputFile));
		try{
			in.read(input, 0, inputFileLength);
		}finally{
			in.close();
		}
		
		return parse(startNode, inputURI, input);
	}
	
	// This is kind of ugly.
	protected IConstructor parseFromReader(AbstractStackNode startNode, URI inputURI, Reader in) throws IOException{
		ArrayList<char[]> segments = new ArrayList<char[]>();
		
		// Gather segments.
		int nrOfWholeSegments = -1;
		int bytesRead;
		do{
			char[] segment = new char[STREAM_READ_SEGMENT_SIZE];
			bytesRead = in.read(segment, 0, STREAM_READ_SEGMENT_SIZE);
			
			segments.add(segment);
			++nrOfWholeSegments;
		}while(bytesRead == STREAM_READ_SEGMENT_SIZE);
		
		// Glue the segments together.
		char[] segment = segments.get(nrOfWholeSegments);
		char[] input;
		if(bytesRead != -1){
			input = new char[(nrOfWholeSegments * STREAM_READ_SEGMENT_SIZE) + bytesRead];
			System.arraycopy(segment, 0, input, (nrOfWholeSegments * STREAM_READ_SEGMENT_SIZE), bytesRead);
		}else{
			input = new char[(nrOfWholeSegments * STREAM_READ_SEGMENT_SIZE)];
		}
		for(int i = nrOfWholeSegments - 1; i >= 0; --i){
			segment = segments.get(i);
			System.arraycopy(segment, 0, input, (i * STREAM_READ_SEGMENT_SIZE), STREAM_READ_SEGMENT_SIZE);
		}
		
		return parse(startNode, inputURI, input);
	}
	
	public IConstructor parseFromStream(AbstractStackNode startNode, URI inputURI, InputStream in) throws IOException{
		return parseFromReader(startNode, inputURI, new InputStreamReader(in));
	}
	
	public IConstructor parse(String nonterminal, URI inputURI, char[] input){
		return parse(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, nonterminal), inputURI, input);
	}
	
	public IConstructor parse(String nonterminal, URI inputURI, String input){
		return parseFromString(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, nonterminal), inputURI, input);
	}
	
	public IConstructor parse(String nonterminal, URI inputURI, InputStream in) throws IOException{
		return parseFromStream(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, nonterminal), inputURI, in);
	}
	
	public IConstructor parse(String nonterminal, URI inputURI, Reader in) throws IOException{
		return parseFromReader(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, nonterminal), inputURI, in);
	}
	
	public IConstructor parse(String nonterminal, URI inputURI, File inputFile) throws IOException{
		return parseFromFile(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, 0, nonterminal), inputURI, inputFile);
	}
}
