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
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.AbstractNode.CycleMark;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.stack.AbstractStackNode;
import org.rascalmpl.parser.sgll.stack.IReducableStackNode;
import org.rascalmpl.parser.sgll.stack.NonTerminalStackNode;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.HashMap;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.parser.sgll.util.LinearIntegerKeyedMap;
import org.rascalmpl.parser.sgll.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.sgll.util.RotatingQueue;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import static org.rascalmpl.interpreter.utils.Timing.getUserTime;

public abstract class SGLL implements IGLL{
	private final static int STREAM_READ_SEGMENT_SIZE = 8192;
	
	protected final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private URI inputURI;
	private char[] input;
	
	private final ArrayList<AbstractStackNode> todoList;
	
	// Updatable
	private final ArrayList<AbstractStackNode> stacksToExpand;
	private final RotatingQueue<AbstractStackNode> stacksWithTerminalsToReduce;
	private final RotatingQueue<AbstractStackNode> stacksWithNonTerminalsToReduce;
	private final ArrayList<AbstractStackNode[]> lastExpects;
	private final ArrayList<AbstractStackNode> possiblySharedExpects;
	private final ArrayList<AbstractStackNode> possiblySharedNextNodes;

	private final ObjectIntegerKeyedHashMap<String, ContainerNode> resultStoreCache;
	
	private int previousLocation;
	private int location;
	
	private AbstractStackNode root;
	
	private final HashMap<String, Method> methodCache;
	
	public SGLL(){
		super();
		
		todoList = new ArrayList<AbstractStackNode>();
		
		stacksToExpand = new ArrayList<AbstractStackNode>();
		stacksWithTerminalsToReduce = new RotatingQueue<AbstractStackNode>();
		stacksWithNonTerminalsToReduce = new RotatingQueue<AbstractStackNode>();
		
		lastExpects = new ArrayList<AbstractStackNode[]>();
		possiblySharedExpects = new ArrayList<AbstractStackNode>();
		
		possiblySharedNextNodes = new ArrayList<AbstractStackNode>();
		
		resultStoreCache = new ObjectIntegerKeyedHashMap<String, ContainerNode>();
		
		previousLocation = -1;
		location = 0;
		
		methodCache = new HashMap<String, Method>();
	}
	
	protected void expect(IConstructor production, AbstractStackNode... symbolsToExpect){
//		System.err.println("expect " + production);
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
	
	protected void invokeExpects(String name){
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
			method.invoke(this);
		}catch(IllegalAccessException iaex){
			throw new ImplementationError(iaex.getMessage(), iaex);
		}catch(InvocationTargetException itex){
			throw new ImplementationError(itex.getMessage(), itex);
		} 
	}
	
	private void updateNextNode(AbstractStackNode next, AbstractStackNode node){
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edges = node.getEdges();
		AbstractNode result = node.getResult();
		
		for(int i = possiblySharedNextNodes.size() - 1; i >= 0; --i){
			AbstractStackNode possibleAlternative = possiblySharedNextNodes.get(i);
			if(possibleAlternative.isSimilar(next)){
				possibleAlternative.addEdges(edges);
				addPrefixes(possibleAlternative, node, result);
				
				if(next.isEndNode()){
					if(!possibleAlternative.isClean() && possibleAlternative.getStartLocation() == location){
						if(possibleAlternative != node){ // List cycle fix.
							// Encountered self recursive epsilon cycle; update the prefixes.
							updatePrefixes(possibleAlternative, node, edges, result);
						}
					}
				}
				return;
			}
		}
		
		if(next.startLocationIsSet()){
			next = next.getCleanCopy();
		}
		
		next.setStartLocation(location);
		next.addEdges(edges);
		addPrefixes(next, node, result);
		
		if(!next.isReducable()){ // Is non-terminal or list.
			ContainerNode resultStore = resultStoreCache.get(next.getName(), location);
			if(resultStore != null){ // Is nullable, add the known results.
				next.setResultStore(resultStore);
				stacksWithNonTerminalsToReduce.put(next);
			}
		}
		
		possiblySharedNextNodes.add(next);
		stacksToExpand.add(next);
	}
	
	private void updatePrefixes(AbstractStackNode next, AbstractStackNode node, LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap, AbstractNode result){
		IConstructor production = next.getParentProduction();
		
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		// Update results (if necessary).
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			int startLocation = edgesMap.getKey(i);
			ArrayList<AbstractStackNode> edgesPart = edgesMap.getValue(i);
			
			// Update one (because of sharing all will be updated).
			AbstractStackNode edge = edgesPart.get(0);
			ContainerNode resultStore = edge.getResultStore();
			if(!resultStore.isRejected()){
				ArrayList<Link> edgePrefixes = new ArrayList<Link>();
				Link prefix = constructPrefixesFor(edgesMap, prefixesMap, result, startLocation);
				edgePrefixes.add(prefix);
				resultStore.addAlternative(production, new Link(edgePrefixes, next.getResult()));
			}
		}
	}
	
	private void addPrefixes(AbstractStackNode next, AbstractStackNode node, AbstractNode result){
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		if(prefixesMap == null){
			next.addPrefix(new Link(null, result), node.getStartLocation());
		}else{
			int nrOfPrefixes = edgesMap.size();
			for(int i = nrOfPrefixes - 1; i >= 0; --i){
				next.addPrefix(new Link(prefixesMap[i], result), edgesMap.getKey(i));
			}
		}
	}
	
	private void updateEdges(AbstractStackNode node){
		IConstructor production = node.getParentProduction();
		
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		AbstractNode result = node.getResult();
		
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			int startLocation = edgesMap.getKey(i);
			ArrayList<AbstractStackNode> edgeList = edgesMap.getValue(i);
			
			AbstractStackNode edge = edgeList.get(0);
			String nodeName = edge.getName();
			ContainerNode resultStore = resultStoreCache.get(nodeName, startLocation);
			Link resultLink = new Link((prefixesMap != null) ? prefixesMap[i] : null, result);
			if(resultStore != null){
				if(!resultStore.isRejected()) resultStore.addAlternative(production, resultLink);
			}else{
				resultStore = new ContainerNode(inputURI, startLocation, (location - startLocation), edge.isList());
				resultStoreCache.unsafePut(nodeName, startLocation, resultStore);
				resultStore.addAlternative(production, resultLink);
				
				if(!edge.isClean()){
					edge = edge.getCleanCopyWithPrefix();
				}
				edge.setResultStore(resultStore);
				stacksWithNonTerminalsToReduce.put(edge);
				if(location == input.length && !edge.hasEdges()){
					root = edge; // Root reached.
				}
				
				for(int j = edgeList.size() - 1; j >= 1; --j){
					edge = edgeList.get(j);
					if(!edge.isClean()){
						edge = edge.getCleanCopyWithPrefix();
					}
					edge.setResultStore(resultStore);
					stacksWithNonTerminalsToReduce.put(edge);
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
			ContainerNode resultStore = resultStoreCache.get(nodeName, startLocation);
			if(resultStore != null){
				resultStore.setRejected();
			}else{
				resultStore = new ContainerNode(inputURI, startLocation, (location - startLocation), edge.isList());
				resultStoreCache.unsafePut(nodeName, startLocation, resultStore);
				resultStore.setRejected();
				
				if(!edge.isClean()){
					edge = edge.getCleanCopyWithPrefix();
				}
				edge.setResultStore(resultStore);
				stacksWithNonTerminalsToReduce.put(edge);
				if(location == input.length && !edge.hasEdges()){
					root = edge; // Root reached.
				}
				
				for(int j = edgeList.size() - 1; j >= 1; --j){
					edge = edgeList.get(j);
					if(!edge.isClean()){
						edge = edge.getCleanCopyWithPrefix();
					}
					edge.setResultStore(resultStore);
					stacksWithNonTerminalsToReduce.put(edge);
					if(location == input.length && !edge.hasEdges()){
						root = edge; // Root reached.
					}
				}
			}
		}
	}
	
	private void move(AbstractStackNode node){
		if(node.isEndNode()){
			if(!node.isReject()){
				updateEdges(node);
			}else if(node.isReducable() || !node.getResultStore().isRejected()){
				updateRejects(node);
			}
		}

		AbstractStackNode next;
		if((next = node.getNext()) != null){
			updateNextNode(next, node);
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
		if(!terminal.reduce(input)) return;
		
		// Filtering
		if(terminal.isReductionFiltered(input, location)) return;
		
//		System.err.println("reducing " + terminal);
		move(terminal);
	}
	
	private void reduceNonTerminal(AbstractStackNode nonTerminal){
		// Filtering
		if(nonTerminal.isReductionFiltered(input, location)) return;
//		System.err.println("reducing " + nonTerminal);
		move(nonTerminal);
	}
	
	private void reduce(){
		if(previousLocation != location){ // Epsilon fix.
			possiblySharedNextNodes.clear();
			resultStoreCache.clear();
		}
		
		// Reduce terminals.
		while(!stacksWithTerminalsToReduce.isEmpty()){
			AbstractStackNode terminal = stacksWithTerminalsToReduce.unsafeGet();
			reduceTerminal(terminal);

			todoList.remove(terminal);
		}
		
		// Reduce non-terminals.
		while(!stacksWithNonTerminalsToReduce.isEmpty()){
			reduceNonTerminal(stacksWithNonTerminalsToReduce.unsafeGet());
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
	
	private boolean shareNode(AbstractStackNode node, AbstractStackNode stack){
		for(int j = possiblySharedExpects.size() - 1; j >= 0; --j){
			AbstractStackNode possiblySharedNode = possiblySharedExpects.get(j);
			if(possiblySharedNode.isSimilar(node)){
				possiblySharedNode.addEdge(stack);
				return true;
			}
		}
		return false;
	}
	
	private boolean shareListNode(AbstractStackNode node, AbstractStackNode stack){
		for(int j = possiblySharedNextNodes.size() - 1; j >= 0; --j){
			AbstractStackNode possiblySharedNode = possiblySharedNextNodes.get(j);
			if(possiblySharedNode.isSimilar(node)){
				possiblySharedNode.addEdge(stack);
				possiblySharedNode.addPrefix(null, location);
				return true;
			}
		}
		return false;
	}
	
	private void handleExpects(AbstractStackNode stackBeingWorkedOn){
		for(int i = lastExpects.size() - 1; i >= 0; --i){
			AbstractStackNode[] expectedNodes = lastExpects.get(i);
			int numberOfNodes = expectedNodes.length;
			AbstractStackNode first = expectedNodes[0];
			
			// Handle sharing (and loops).
			if(!shareNode(first, stackBeingWorkedOn)){
				AbstractStackNode next = expectedNodes[numberOfNodes - 1].getCleanCopy();
				next.markAsEndNode();
				
				for(int k = numberOfNodes - 2; k >= 0; --k){
					AbstractStackNode current = expectedNodes[k].getCleanCopy();
					current.addNext(next);
					next = current;
				}
				
				next.addEdge(stackBeingWorkedOn);
				next.setStartLocation(location);
				
				stacksToExpand.add(next);
				possiblySharedExpects.add(next);
			}
		}
	}
	
	private void expandStack(AbstractStackNode node){
		if(node.isReducable()){
			if((location + node.getLength()) <= input.length) todoList.add(node);
			return;
		}
		
		if(!node.isList()){
			invokeExpects(node.getName());
			
			handleExpects(node);
		}else{ // List
			AbstractStackNode[] listChildren = node.getChildren();
			
			AbstractStackNode child = listChildren[0];
			if(!shareListNode(child, node)){
				stacksToExpand.add(child);
				possiblySharedNextNodes.add(child); // For epsilon list cycles.
			}
			
			if(listChildren.length > 1){ // Star list or optional.
				// This is always epsilon; so shouldn't be shared.
				stacksToExpand.add(listChildren[1]);
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
		for(int i = ranges.length - 1; i >= 0; --i){
			char[] range = ranges[i];
			if(next >= range[0] && next <= range[1]) return true;
		}
		
		for(int i = characters.length - 1; i >= 0; --i){
			if(next == characters[i]) return true;
		}
		
		return false;
	}
	
	protected IConstructor parse(AbstractStackNode startNode, URI inputURI, char[] input){
		// Initialize.
		this.inputURI = inputURI;
		this.input = input;
		
		long time = getUserTime();

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
		
		long span = (getUserTime() - time) / (1000 * 1000);
		System.err.println("parsing took: " + span + " ms");
		System.err.println("building tree starts");
		time = getUserTime();
		IValue result = root.getResult().toTerm(new IndexedStack<AbstractNode>(), 0, new CycleMark());
		
		span = (getUserTime() - time) / (1000 * 1000);
		if(result == null) throw new SyntaxError("Parse Error: all trees were filtered.", vf.sourceLocation("-"));
		System.err.println("building tree took " + span + " ms");
		return makeParseTree(result);
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
	
	private IConstructor makeParseTree(IValue tree){
		return vf.constructor(Factory.ParseTree_Top, tree, vf.integer(-1)); // Amb field is unsupported.
	}
	
	public IConstructor parse(String nonterminal, URI inputURI, char[] input) {
		return parse(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, nonterminal), inputURI, input);
	}
	
	public IConstructor parse(String nonterminal, URI inputURI, String input) {
		return parseFromString(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, nonterminal), inputURI, input);
	}
	
	public IConstructor parse(String nonterminal, URI inputURI, InputStream in) throws IOException {
		return parseFromStream(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, nonterminal), inputURI, in);
	}
	
	public IConstructor parse(String nonterminal, URI inputURI, Reader in) throws IOException {
		return parseFromReader(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, nonterminal), inputURI, in);
	}
	
	public IConstructor parse(String nonterminal, URI inputURI, File inputFile) throws IOException {
		return parseFromFile(new NonTerminalStackNode(AbstractStackNode.START_SYMBOL_ID, nonterminal), inputURI, inputFile);
	}
}
