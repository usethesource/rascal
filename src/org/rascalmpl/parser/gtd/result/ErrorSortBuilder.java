package org.rascalmpl.parser.gtd.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.gtd.result.AbstractNode.CycleMark;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.gtd.util.specific.PositionStore;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;

public class ErrorSortBuilder{
	private final static IValueFactory VF = AbstractNode.VF;
	
	private ErrorSortBuilder(){
		super();
	}
	
	private static class IsInError{
		public boolean inError;
	}
	
	protected static void gatherAlternatives(Link child, DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor, IsInError isInError){
		AbstractNode resultNode = child.node;
		
		if(!(resultNode.isEpsilon() && child.prefixes == null)){
			AbstractNode[] postFix = new AbstractNode[]{resultNode};
			gatherProduction(child, postFix, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, actionExecutor, isInError);
		}else{
			gatheredAlternatives.add(new IConstructor[]{}, production);
		}
	}
	
	private static void gatherProduction(Link child, AbstractNode[] postFix, DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor, IsInError isInError){
		ArrayList<Link> prefixes = child.prefixes;
		if(prefixes == null){
			int postFixLength = postFix.length;
			IConstructor[] constructedPostFix = new IConstructor[postFixLength];
			for(int i = 0; i < postFixLength; ++i){
				constructedPostFix[i] = postFix[i].toErrorTree(stack, depth, cycleMark, positionStore, actionExecutor);
			}
			
			gatheredAlternatives.add(constructedPostFix, production);
			return;
		}
		
		for(int i = prefixes.size() - 1; i >= 0; --i){
			Link prefix = prefixes.get(i);
			
			AbstractNode resultNode = prefix.node;
			if(!resultNode.isRejected()){
				isInError.inError = true;
			}
			
			int length = postFix.length;
			AbstractNode[] newPostFix = new AbstractNode[length + 1];
			System.arraycopy(postFix, 0, newPostFix, 1, length);
			newPostFix[0] = resultNode;
			gatherProduction(prefix, newPostFix, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, actionExecutor, isInError);
		}
	}
	
	private static IConstructor buildAlternative(IConstructor production, IValue[] children, boolean error){
		IListWriter childrenListWriter = VF.listWriter(Factory.Tree);
		for(int i = children.length - 1; i >= 0; --i){
			childrenListWriter.insert(children[i]);
		}
		
		if(!error){
			return VF.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
		}
		return VF.constructor(Factory.Tree_Error, production, childrenListWriter.done(), AbstractContainerNode.EMPTY_LIST);
	}
	
	public static IConstructor toErrorSortTree(SortContainerNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor){
		ISourceLocation sourceLocation = null;
		if(!(node.isLayout || node.input == null)){
			int beginLine = positionStore.findLine(node.offset);
			int endLine = positionStore.findLine(node.endOffset);
			sourceLocation = VF.sourceLocation(node.input, node.offset, node.endOffset - node.offset, beginLine + 1, endLine + 1, positionStore.getColumn(node.offset, beginLine), positionStore.getColumn(node.endOffset, endLine));
		}
		
		int index = stack.contains(node);
		if(index != -1){ // Cycle found.
			IConstructor cycle = VF.constructor(Factory.Tree_Cycle, ProductionAdapter.getRhs(node.firstProduction), VF.integer(depth - index));
			cycle = actionExecutor.filterCycle(cycle);
			if(cycle == null){
				cycle = VF.constructor(Factory.Tree_Error_Cycle, ProductionAdapter.getRhs(node.firstProduction), VF.integer(depth - index));
			}
			
			if(sourceLocation != null) cycle = cycle.setAnnotation(Factory.Location, sourceLocation);
			
			cycleMark.setMark(index);
			
			return cycle;
		}
		
		int childDepth = depth + 1;
		
		stack.push(node, depth); // Push.
		
		// Gather
		IsInError isInError = new IsInError();
		isInError.inError = node.rejected;
		DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives = new DoubleArrayList<IConstructor[], IConstructor>();
		gatherAlternatives(node.firstAlternative, gatheredAlternatives, node.firstProduction, stack, childDepth, cycleMark, positionStore, actionExecutor, isInError);
		if(node.alternatives != null){
			for(int i = node.alternatives.size() - 1; i >= 0; --i){
				gatherAlternatives(node.alternatives.get(i), gatheredAlternatives, node.productions.get(i), stack, childDepth, cycleMark, positionStore, actionExecutor, isInError);
			}
		}
		
		// Output.
		IConstructor result = null;
		
		int nrOfAlternatives = gatheredAlternatives.size();
		if(nrOfAlternatives == 1){ // Not ambiguous.
			IConstructor production = gatheredAlternatives.getSecond(0);
			IValue[] alternative = gatheredAlternatives.getFirst(0);
			result = buildAlternative(production, alternative, isInError.inError);
			result = actionExecutor.filterProduction(result);
			if(result == null){
				// Build error alternative.
				result = buildAlternative(production, alternative, true);
			}
			if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
		}else if(nrOfAlternatives > 0){ // Ambiguous.
			ISetWriter ambSetWriter = VF.setWriter(Factory.Tree);
			IConstructor lastAlternative = null;
			
			for(int i = nrOfAlternatives - 1; i >= 0; --i){
				IConstructor production = gatheredAlternatives.getSecond(i);
				IValue[] alternative = gatheredAlternatives.getFirst(i);
				
				IConstructor alt = buildAlternative(production, alternative, isInError.inError);
				alt = actionExecutor.filterProduction(alt);
				if(alt != null){
					if(sourceLocation != null) alt = alt.setAnnotation(Factory.Location, sourceLocation);
					lastAlternative = alt;
					ambSetWriter.insert(alt);
				}
			}
			
			if(ambSetWriter.size() == 1){
				result = lastAlternative;
			}else if(ambSetWriter.size() == 0){
				// Build error trees for the alternatives.
				for(int i = nrOfAlternatives - 1; i >= 0; --i){
					IConstructor production = gatheredAlternatives.getSecond(i);
					IValue[] alternative = gatheredAlternatives.getFirst(i);
					
					IConstructor alt = buildAlternative(production, alternative, true);
					if(sourceLocation != null) alt = alt.setAnnotation(Factory.Location, sourceLocation);
					ambSetWriter.insert(alt);
				}
				
				result = VF.constructor(Factory.Tree_Error_Amb, ambSetWriter.done());
				// Don't filter error ambs.
			}else{
				result = VF.constructor(Factory.Tree_Amb, ambSetWriter.done());
				result = actionExecutor.filterAmbiguity(result);
				if(result == null){
					// Build error amb.
					result = VF.constructor(Factory.Tree_Error_Amb, ambSetWriter.done());
				}
				
				if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
			}
		}
		
		stack.dirtyPurge(); // Pop.
		
		return result;
	}
}
