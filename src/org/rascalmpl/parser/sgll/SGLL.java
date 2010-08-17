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
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.parser.sgll.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.sgll.util.LinearIntegerKeyedMap;
import org.rascalmpl.parser.sgll.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.sgll.util.RotatingQueue;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;

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
	private final IntegerKeyedHashMap<ArrayList<AbstractStackNode>> possiblySharedEdgeNodesMap;

	private final ObjectIntegerKeyedHashMap<IConstructor, ContainerNode> resultStoreCache;
	
	private int previousLocation;
	private int location;
	
	private boolean nullableEncountered;
	
	private AbstractStackNode root;
	
	public SGLL(){
		super();
		
		todoList = new ArrayList<AbstractStackNode>();
		
		stacksToExpand = new ArrayList<AbstractStackNode>();
		stacksWithTerminalsToReduce = new RotatingQueue<AbstractStackNode>();
		stacksWithNonTerminalsToReduce = new RotatingQueue<AbstractStackNode>();
		
		lastExpects = new ArrayList<AbstractStackNode[]>();
		possiblySharedExpects = new ArrayList<AbstractStackNode>();
		
		possiblySharedNextNodes = new ArrayList<AbstractStackNode>();
		possiblySharedEdgeNodesMap = new IntegerKeyedHashMap<ArrayList<AbstractStackNode>>();
		
		resultStoreCache = new ObjectIntegerKeyedHashMap<IConstructor, ContainerNode>();
		
		previousLocation = -1;
		location = 0;
	}
	
	protected void expect(IConstructor production, AbstractStackNode... symbolsToExpect){
		lastExpects.add(symbolsToExpect);
		
		AbstractStackNode lastNode = symbolsToExpect[symbolsToExpect.length - 1];
		lastNode.setParentProduction(production);
	}
	
	protected void expectReject(IConstructor production, AbstractStackNode... symbolsToExpect){
		lastExpects.add(symbolsToExpect);
		
		AbstractStackNode lastNode = symbolsToExpect[symbolsToExpect.length - 1];
		lastNode.setParentProduction(production);
		lastNode.markAsReject();
	}
	
	protected void invokeExpects(String name){
		try{
			Method method = getClass().getMethod(name);
			method.invoke(this);
		}catch(SecurityException e){
			throw new ImplementationError(e.getMessage(), e);
		}catch(NoSuchMethodException e){
			throw new ImplementationError(e.getMessage(), e);
		}catch(IllegalArgumentException e){
			throw new ImplementationError(e.getMessage(), e);
		}catch(IllegalAccessException e){
			throw new ImplementationError(e.getMessage(), e);
		}catch(InvocationTargetException e){
			throw new ImplementationError(e.getMessage(), e);
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
						// Something horrible happened; update the prefixes.
						if(possibleAlternative != node){ // List cycle fix.
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
		
		possiblySharedNextNodes.add(next);
		stacksToExpand.add(next);
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
	
	private void updatePrefixes(AbstractStackNode next, AbstractStackNode node, LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap, AbstractNode result){
		IConstructor production = next.getParentProduction();
		
		ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
		
		// Update results (if necessary).
		for(int i = edgesMap.size() - 1; i >= 0; --i){
			int startLocation = edgesMap.getKey(i);
			ArrayList<AbstractStackNode> edgesPart = edgesMap.getValue(i);
			for(int j = edgesPart.size() - 1; j >= 0; --j){
				AbstractStackNode edge = edgesPart.get(j);
				
				if(edge.isMarkedAsWithResults()){
					Link prefix = constructPrefixesFor(edgesMap, prefixesMap, result, startLocation);
					if(prefix != null){
						ArrayList<Link> edgePrefixes = new ArrayList<Link>();
						edgePrefixes.add(prefix);
						ContainerNode resultStore = edge.getResultStore();
						resultStore.addAlternative(production, new Link(edgePrefixes, next.getResult()));
					}
				}
			}
		}
	}
	
	private boolean updateEdgeNode(AbstractStackNode node, ArrayList<Link> prefixes, AbstractNode result, IConstructor production){
		int startLocation = node.getStartLocation();
		ArrayList<AbstractStackNode> possiblySharedEdgeNodes = possiblySharedEdgeNodesMap.get(startLocation);
		if(possiblySharedEdgeNodes != null){
			for(int i = possiblySharedEdgeNodes.size() - 1; i >= 0; --i){
				AbstractStackNode possibleAlternative = possiblySharedEdgeNodes.get(i);
				if(possibleAlternative.isSimilar(node)){
					if(possibleAlternative.isMarkedAsWithResults()){
						ContainerNode resultStore = possibleAlternative.getResultStore();
						if(!resultStore.isRejected()){
							resultStore.addAlternative(production, new Link(prefixes, result));
						}
						return true;
					}
					return false;
				}
			}
		}else{
			possiblySharedEdgeNodes = new ArrayList<AbstractStackNode>();
			possiblySharedEdgeNodesMap.unsafePut(startLocation, possiblySharedEdgeNodes);
		}
		
		if(!node.isClean()){
			node = node.getCleanCopyWithPrefix();
		}
		
		ContainerNode resultStore = resultStoreCache.get(ProductionAdapter.getRhs(production), startLocation);
		if(resultStore == null){
			resultStore = new ContainerNode(inputURI, startLocation, location - startLocation, node.isList());
			resultStoreCache.unsafePut(ProductionAdapter.getRhs(production), startLocation, resultStore);
			node.markAsWithResults();
			
			resultStore.addAlternative(production, new Link(prefixes, result));
		}
		node.setResultStore(resultStore);
		
		if(location == input.length && !node.hasEdges()){
			root = node; // Root reached.
		}
		
		possiblySharedEdgeNodes.add(node);
		stacksWithNonTerminalsToReduce.put(node);
		
		return false;
	}
	
	private boolean rejectEdgeNode(AbstractStackNode node){
		int startLocation = node.getStartLocation();
		ArrayList<AbstractStackNode> possiblySharedEdgeNodes = possiblySharedEdgeNodesMap.get(startLocation);
		if(possiblySharedEdgeNodes != null){
			for(int i = possiblySharedEdgeNodes.size() - 1; i >= 0; --i){
				AbstractStackNode possibleAlternative = possiblySharedEdgeNodes.get(i);
				if(possibleAlternative.isSimilar(node)){
					if(possibleAlternative.isMarkedAsWithResults()){
						ContainerNode resultStore = possibleAlternative.getResultStore();
						resultStore.setRejected();
						return true;
					}
					return false;
				}
			}
		}else{
			possiblySharedEdgeNodes = new ArrayList<AbstractStackNode>();
			possiblySharedEdgeNodesMap.unsafePut(startLocation, possiblySharedEdgeNodes);
		}
		
		if(!node.isClean()){
			node = node.getCleanCopyWithPrefix();
		}
		
		possiblySharedEdgeNodes.add(node);
		
		return false;
	}
	
	private void move(AbstractStackNode node){
		IConstructor production = node.getParentProduction();
		
		if(node.isEndNode()){
			LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
			ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
			if(!node.isReject()){
				AbstractNode result = node.getResult();
				
				for(int i = edgesMap.size() - 1; i >= 0; --i){
					ArrayList<Link> prefixes = null;
					if(prefixesMap != null){
						prefixes = prefixesMap[i];
					}
					
					ArrayList<AbstractStackNode> edgeList = edgesMap.getValue(i);
					for(int j = edgeList.size() - 1; j >= 0; --j){
						if(updateEdgeNode(edgeList.get(j), prefixes, result, production)) break;
					}
				}
			}else if(node.isReducable() || !node.getResultStore().isRejected()){
				for(int i = edgesMap.size() - 1; i >= 0; --i){
					ArrayList<AbstractStackNode> edgeList = edgesMap.getValue(i);
					for(int j = edgeList.size() - 1; j >= 0; --j){
						if(rejectEdgeNode(edgeList.get(j))) break;
					}
				}
			}
		}
		
		AbstractStackNode next;
		if((next = node.getNext()) != null){
			updateNextNode(next, node);
		}
	}
	
	private void moveNullable(AbstractStackNode node, AbstractStackNode edge){
		nullableEncountered = true;
		
		IConstructor production = node.getParentProduction();
		
		LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap = node.getEdges();
		if(!node.isReject()){
			ArrayList<Link>[] prefixesMap = node.getPrefixesMap();
			ArrayList<Link> prefixes = null;
			if(prefixesMap != null){
				prefixes = prefixesMap[edgesMap.findKey(location)];
			}
			
			updateEdgeNode(edge, prefixes, node.getResult(), production);
		}else if(node.isReducable() || !node.getResultStore().isRejected()){
			rejectEdgeNode(edge);
		}
	}
	
	private Link constructPrefixesFor(LinearIntegerKeyedMap<ArrayList<AbstractStackNode>> edgesMap, ArrayList<Link>[] prefixesMap, AbstractNode result, int startLocation){
		if(prefixesMap == null){
			return new Link(null, result);
		}
		
		int index = edgesMap.findKey(startLocation);
		ArrayList<Link> prefixes = prefixesMap[index];
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
		if(!node.isEpsilon()){
			for(int j = possiblySharedExpects.size() - 1; j >= 0; --j){
				AbstractStackNode possiblySharedNode = possiblySharedExpects.get(j);
				if(possiblySharedNode.isSimilar(node)){
					if(!possiblySharedNode.isClean()){ // Is nullable.
						AbstractStackNode last;
						AbstractStackNode next = possiblySharedNode;
						do{
							last = next;
						}while((next = next.getNext()) != null);
						moveNullable(last, stack);
					}
					possiblySharedNode.addEdge(stack);
					return true;
				}
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
			if(!shareNode(child, node)){
				stacksToExpand.add(child);
				possiblySharedExpects.add(child);
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
	
	protected IValue parse(AbstractStackNode startNode, URI inputURI, char[] input){
		// Initialize.
		this.inputURI = inputURI;
		this.input = input;
		
		AbstractStackNode rootNode = startNode.getCleanCopy();
		rootNode.setStartLocation(0);
		stacksToExpand.add(rootNode);
		expand();
		
		do{
			if(!nullableEncountered) findStacksToReduce();

			nullableEncountered = false;
			reduce();
			
			if(!nullableEncountered) expand();
		}while((todoList.size() > 0) || nullableEncountered);
		
		if(root == null){
			int errorLocation = (location == Integer.MAX_VALUE ? 0 : location);
			throw new SyntaxError("Parse Error before: "+errorLocation, vf.sourceLocation("-", errorLocation, 0, -1, -1, -1, -1));
		}
		
		IValue result = root.getResult().toTerm(new IndexedStack<AbstractNode>(), 0, new CycleMark());
		
		if(result == null) throw new SyntaxError("Parse Error: all trees were filtered.", vf.sourceLocation("-"));
		
		return makeParseTree(result);
	}
	
	protected IValue parseFromString(AbstractStackNode startNode, URI inputURI, String inputString){
		return parse(startNode, inputURI, inputString.toCharArray());
	}
	
	protected IValue parseFromFile(AbstractStackNode startNode, URI inputURI, File inputFile) throws IOException{
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
	protected IValue parseFromReader(AbstractStackNode startNode, URI inputURI, Reader in) throws IOException{
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
	
	public IValue parseFromStream(AbstractStackNode startNode, URI inputURI, InputStream in) throws IOException{
		return parseFromReader(startNode, inputURI, new InputStreamReader(in));
	}
	
	private IValue makeParseTree(IValue tree){
		return vf.constructor(Factory.ParseTree_Top, tree, vf.integer(-1)); // Amb field is unsupported.
	}
}
