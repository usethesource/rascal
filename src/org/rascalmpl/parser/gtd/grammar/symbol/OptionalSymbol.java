package org.rascalmpl.parser.gtd.grammar.symbol;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.grammar.State;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.OptionalStackNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public class OptionalSymbol extends Symbol{
	private final IConstructor production;
	private final Symbol optional;
	
	public OptionalSymbol(IConstructor production, Symbol optional){
		this(production, optional, null, null);
	}
	
	public OptionalSymbol(IConstructor production, Symbol optional, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(enterFilters, completionFilters);
		
		this.production = production;
		this.optional = optional;
	}
	
	public AbstractStackNode buildNode(State state, int dot){
		AbstractStackNode optionalNode = optional.buildNode(state, dot);
		
		return new OptionalStackNode(state.getNextId(this), dot, production, optionalNode, enterFilters, completionFilters);
	}
	
	public boolean isEqual(Symbol symbol){
		if(!(symbol instanceof OptionalSymbol)) return false;
		
		OptionalSymbol otherSymbol = (OptionalSymbol) symbol;
		
		if(!production.isEqual(otherSymbol.production)) return false;
		
		return hasEqualFilters(otherSymbol);
	}
}
