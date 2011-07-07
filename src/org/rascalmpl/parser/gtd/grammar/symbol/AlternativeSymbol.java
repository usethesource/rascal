package org.rascalmpl.parser.gtd.grammar.symbol;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.grammar.State;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.AlternativeStackNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public class AlternativeSymbol extends Symbol{
	private final IConstructor production;
	private final Symbol[] alternatives;
	
	public AlternativeSymbol(IConstructor production, Symbol[] alternatives){
		this(production, alternatives, null, null);
	}
	
	public AlternativeSymbol(IConstructor production, Symbol[] alternatives, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(enterFilters, completionFilters);
		
		this.production = production;
		this.alternatives = alternatives;
	}
	
	public AbstractStackNode buildNode(State state, int dot){
		AbstractStackNode[] alternativeNodes = new AbstractStackNode[alternatives.length];
		for(int i = alternatives.length - 1; i >= 0; --i){
			alternativeNodes[i] = alternatives[i].buildNode(state, 0);
		}
		
		return new AlternativeStackNode(state.getNextId(this), dot, production, alternativeNodes, enterFilters, completionFilters);
	}
	
	public boolean isEqual(Symbol symbol){
		if(!(symbol instanceof AlternativeSymbol)) return false;
		
		AlternativeSymbol otherSymbol = (AlternativeSymbol) symbol;

		if(!production.isEqual(otherSymbol.production)) return false;
		
		return hasEqualFilters(otherSymbol);
	}
}
