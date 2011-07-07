package org.rascalmpl.parser.gtd.grammar.symbol;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.grammar.State;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.ListStackNode;
import org.rascalmpl.parser.gtd.stack.SeparatedListStackNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public class ListSymbol extends Symbol{
	private final IConstructor production;
	private final boolean isPlusList;
	private final Symbol child;
	private final Symbol[] separators;
	
	public ListSymbol(IConstructor production, Symbol child, boolean isPlusList){
		this(production, child, null, isPlusList, null, null);
	}
	
	public ListSymbol(IConstructor production, Symbol child, Symbol[] separators, boolean isPlusList){
		this(production, child, separators, isPlusList, null, null);
	}
	
	public ListSymbol(IConstructor production, Symbol child, Symbol[] separators, boolean isPlusList, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(enterFilters, completionFilters);
		
		this.production = production;
		this.child = child;
		this.separators = separators;
		this.isPlusList = isPlusList;
	}
	
	public AbstractStackNode buildNode(State state, int dot){
		AbstractStackNode childNode = child.buildNode(state, 0);
		AbstractStackNode[] separatorNodes = new AbstractStackNode[separators.length];
		
		for(int i = separators.length - 1; i >= 0; --i){
			separatorNodes[i] = separators[i].buildNode(state, i);
		}
		
		if(separators == null){
			return new ListStackNode(state.getNextId(this), dot, production, childNode, isPlusList, enterFilters, completionFilters);
		}
		
		return new SeparatedListStackNode(state.getNextId(this), dot, production, childNode, separatorNodes, isPlusList, enterFilters, completionFilters);
	}
	
	public boolean isEqual(Symbol symbol){
		if(!(symbol instanceof ListSymbol)) return false;
		
		ListSymbol otherSymbol = (ListSymbol) symbol;
		
		if(!production.isEqual(otherSymbol.production)) return false;
		
		return hasEqualFilters(otherSymbol);
	}
}
