package org.rascalmpl.parser.uptr.debug;

import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.debug.IDebugListener;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.edge.EdgesSet;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.values.uptr.ProductionAdapter;

public class DebugLogger implements IDebugListener<IConstructor>{
	private final PrintWriter out;
	private final boolean verbose;
	
	public DebugLogger(PrintWriter out, boolean verbose){
		super();
		
		this.out = out;
		this.verbose = verbose;
	}
	
	public DebugLogger(PrintWriter out){
		this(out, false);
	}
	
	private void collectProductions(AbstractStackNode<IConstructor> node, ArrayList<IConstructor> productions){
		AbstractStackNode<IConstructor>[] production = node.getProduction();
		if(production == null) return; // Can happen, but can't remember why.
		
		int dot = node.getDot();
		
		if(node.isEndNode()){
			IConstructor parentProduction = node.getParentProduction();
			
			productions.add(parentProduction);
			
			if(ProductionAdapter.isList(parentProduction)) return; // Don't follow productions in lists productions, since they are 'cyclic'.
		}
		 
		for(int i = dot + 1; i < production.length; ++i){
			AbstractStackNode<IConstructor> currentNode = production[i];
			if(currentNode.isEndNode()){
				productions.add(currentNode.getParentProduction());
			}
			
			AbstractStackNode<IConstructor>[][] alternateProductions = currentNode.getAlternateProductions();
			if(alternateProductions != null){
				for(int j = alternateProductions.length - 1; j >= 0; --j){
					collectProductions(alternateProductions[j][i], productions);
				}
			}
		}
	}
	
	private void printProductions(AbstractStackNode<IConstructor> node, boolean indent){
		ArrayList<IConstructor> productions = new ArrayList<IConstructor>();
		collectProductions(node, productions);
		
		for(int i = productions.size() - 1; i >= 0; --i){
			if(indent) out.print('\t');
			out.println(productions.get(i));
		}
	}

	public void shifting(int offset, int[] input, PositionStore positionStore){
		int line = positionStore.findLine(offset);
		int column = positionStore.getColumn(offset, line);
		out.println(String.format("Shifting to offset: %d (line: %d, column: %d)", offset, line, column));
	}

	public void iterating(){
		out.println("Iterating");
	}

	public void matched(AbstractStackNode<IConstructor> node, AbstractNode result){
		out.println(String.format("Matched: %s", node));
	}

	public void failedToMatch(AbstractStackNode<IConstructor> node){
		out.println(String.format("Failed to match: %s", node));
	}

	public void expanding(AbstractStackNode<IConstructor> node){
		out.println(String.format("Expanding: %s", node));
		
		if(verbose){
			out.println("\tPart of the following production(s):");
			printProductions(node, true);
		}
	}

	public void expanded(AbstractStackNode<IConstructor> node, AbstractStackNode<IConstructor> child){
		out.println(String.format("Expanded: %s", node));
	}

	public void foundIterationCachedNullableResult(AbstractStackNode<IConstructor> node){
		out.println(String.format("Found cached nullable result for: %s", node));
	}

	public void moving(AbstractStackNode<IConstructor> node, AbstractNode result){
		out.println(String.format("Moving: %s", node));
		
		if(verbose){
			out.println("\tPart of the following production(s):");
			printProductions(node, true);
		}
	}

	public void progressed(AbstractStackNode<IConstructor> node, AbstractNode result, AbstractStackNode<IConstructor> next){
		out.println(String.format("Progressed: %s to %s", node, next));
		
		if(verbose){
			out.println("\tPart of the following production(s):");
			printProductions(next, true);
		}
	}

	public void propagated(AbstractStackNode<IConstructor> node, AbstractNode nodeResult, AbstractStackNode<IConstructor> next){
		out.println(String.format("Propagated prefixes from %s to %s", node, next));
		
		if(verbose){
			out.println("\tPart of the following production(s):");
			printProductions(next, true);
		}
	}

	public void reducing(AbstractStackNode<IConstructor> node, Link resultLink, EdgesSet<IConstructor> edges){
		out.println(String.format("Reducing: %s, start location: %d", node.getParentProduction(), edges.get(0).getStartLocation()));
	}

	public void reduced(AbstractStackNode<IConstructor> parent){
		out.println(String.format("Reduced to: %s", parent));
		
		if(verbose){
			out.println(String.format("\tPart of the following production(s) (Dot position: %d):", parent.getDot()));
			printProductions(parent, true);
		}
	}

	public void filteredByNestingRestriction(AbstractStackNode<IConstructor> parent){
		out.println(String.format("Filtered by nesting restriction: %s (parent)", parent));
	}

	public void filteredByEnterFilter(AbstractStackNode<IConstructor> node){
		out.println(String.format("Filtered by enter filter restriction: %s", node));
	}

	public void filteredByCompletionFilter(AbstractStackNode<IConstructor> node, AbstractNode result){
		out.println(String.format("Filtered by completion filter: %s", node));
	}
}
