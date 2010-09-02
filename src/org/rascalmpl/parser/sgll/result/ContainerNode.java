package org.rascalmpl.parser.sgll.result;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.DoubleArrayList;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;

public class ContainerNode extends AbstractNode{
	private final URI input;
	private final int offset;
	private final int endOffset;
	
	private boolean rejected;

	private Link firstAlternative;
	private IConstructor firstProduction;
	private ArrayList<Link> alternatives;
	private ArrayList<IConstructor> productions;
	
	private IConstructor cachedResult;
	
	public ContainerNode(URI input, int offset, int endOffset){
		super();
		
		this.input = input;
		this.offset = offset;
		this.endOffset = endOffset;
	}
	
	public void addAlternative(IConstructor production, Link children){
		if(firstAlternative == null){
			firstAlternative = children;
			firstProduction = production;
		}else{
			if(alternatives == null){
				alternatives = new ArrayList<Link>(1);
				productions = new ArrayList<IConstructor>(1);
			}
			alternatives.add(children);
			productions.add(production);
		}
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	public void setRejected(){
		rejected = true;
		
		// Clean up.
		firstAlternative = null;
		alternatives = null;
		productions = null;
	}
	
	public boolean isRejected(){
		return rejected;
	}
	
	private void gatherAlternatives(Link child, DoubleArrayList<AbstractNode[], IConstructor> gatheredAlternatives, IConstructor production){
		AbstractNode resultNode = child.node;
		
		if(!(resultNode.isEpsilon() && child.prefixes == null)){
			if(resultNode.isRejected()) return; // Rejected.
			
			AbstractNode[] postFix = new AbstractNode[]{resultNode};
			gatherProduction(child, postFix, gatheredAlternatives, production);
		}else{
			gatheredAlternatives.add(new AbstractNode[]{}, production);
		}
	}
	
	private void gatherProduction(Link child, AbstractNode[] postFix, DoubleArrayList<AbstractNode[], IConstructor> gatheredAlternatives, IConstructor production){
		ArrayList<Link> prefixes = child.prefixes;
		if(prefixes == null){
			gatheredAlternatives.add(postFix, production);
			return;
		}
		
		for(int i = prefixes.size() - 1; i >= 0; --i){
			Link prefix = prefixes.get(i);
			
			if(prefix != null){
				AbstractNode resultNode = prefix.node;
				if(!resultNode.isRejected()){
					int length = postFix.length;
					AbstractNode[] newPostFix = new AbstractNode[length + 1];
					System.arraycopy(postFix, 0, newPostFix, 1, length);
					newPostFix[0] = resultNode;
					gatherProduction(prefix, newPostFix, gatheredAlternatives, production);
				}
			}else{
				gatheredAlternatives.add(postFix, production);
			}
		}
	}
	
	private ArrayList<IConstructor> buildAlternatives(DoubleArrayList<AbstractNode[], IConstructor> alternatives, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore){
		ArrayList<IConstructor> results = new ArrayList<IConstructor>();
		
		int beginLine = -1;
		ISourceLocation sourceLocation = null;
		if(input != null){
			beginLine = positionStore.findLine(offset);
			int endLine = positionStore.findLine(endOffset);
			sourceLocation = vf.sourceLocation(input, offset, endOffset - offset, beginLine, endLine, positionStore.getColumn(offset, beginLine), positionStore.getColumn(endOffset, endLine));
		}
		
		OUTER : for(int i = alternatives.size() - 1; i >= 0; --i){
			if(input != null) positionStore.setCursorTo(beginLine); // Reduce search time.
			
			IConstructor production = alternatives.getSecond(i);
			AbstractNode[] children = alternatives.getFirst(i);
			IListWriter childrenListWriter = vf.listWriter(Factory.Tree);
			for(int j = 0; j < children.length; ++j){
				IConstructor item = children[j].toTerm(stack, depth, cycleMark, positionStore);
				if(item == null) continue OUTER; // Rejected.
				
				childrenListWriter.append(item);
			}
			
			IConstructor result = vf.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
			if(input != null) result = result.setAnnotation(POSITION_ANNNOTATION_LABEL, sourceLocation);
			results.add(result);
		}
		
		return results;
	}
	
	public IConstructor toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore){
		if(cachedResult != null && (depth <= cycleMark.depth)){
			if(depth == cycleMark.depth){
				cycleMark.reset();
			}
			return cachedResult;
		}
		
		if(rejected) return null;
		
		int index = stack.contains(this);
		if(index != -1){ // Cycle found.
			IConstructor cycle = vf.constructor(Factory.Tree_Cycle, ProductionAdapter.getRhs(firstProduction), vf.integer(depth - index));
			if(input != null) cycle = cycle.setAnnotation(Factory.Location, vf.sourceLocation(input, offset, endOffset - offset, -1, -1, -1, -1));
			
			cycleMark.setMark(index);
			return cycle;
		}
		
		stack.push(this, depth); // Push.
		
		// Gather
		DoubleArrayList<AbstractNode[], IConstructor> gatheredAlternatives = new DoubleArrayList<AbstractNode[], IConstructor>();
		gatherAlternatives(firstAlternative, gatheredAlternatives, firstProduction);
		if(alternatives != null){
			for(int i = alternatives.size() - 1; i >= 0; --i){
				gatherAlternatives(alternatives.get(i), gatheredAlternatives, productions.get(i));
			}
		}
		
		// Output.
		ArrayList<IConstructor> alternatives = buildAlternatives(gatheredAlternatives, stack, depth + 1, cycleMark, positionStore);
		
		IConstructor result;
		int nrOfAlternatives = alternatives.size();
		if(nrOfAlternatives == 0){ // Filtered.
			result = null;
		}else if(nrOfAlternatives == 1){ // Not ambiguous.
			result = alternatives.get(0);
		}else{ // Ambiguous.
			ISetWriter ambSetWriter = vf.setWriter(Factory.Tree);
			for(int i = nrOfAlternatives - 1; i >= 0; --i){
				ambSetWriter.insert(alternatives.get(i));
			}
			result = vf.constructor(Factory.Tree_Amb, ambSetWriter.done());
		}
		
		stack.dirtyPurge(); // Pop.
		
		return (depth <= cycleMark.depth) ? (cachedResult = result) : result;
	}
}
