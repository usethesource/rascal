package org.rascalmpl.parser.gtd.grammar.symbol;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.grammar.State;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.SequenceStackNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public class SequenceSymbol extends Symbol{
	private final IConstructor production;
	private final Symbol[] children;
	
	public SequenceSymbol(IConstructor production, Symbol[] children){
		this(production, children, null, null);
	}
	
	public SequenceSymbol(IConstructor production, Symbol[] children, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(enterFilters, completionFilters);
		
		this.production = production;
		this.children = children;
	}
	
	public AbstractStackNode buildNode(State state, int dot){
		AbstractStackNode[] childNodes = new AbstractStackNode[children.length];
		for(int i = children.length - 1; i >= 0; --i){
			childNodes[i] = children[i].buildNode(state, i);
		}
		
		return new SequenceStackNode(state.getNextId(this), dot, production, childNodes, enterFilters, completionFilters);
	}
	
	public boolean isEqual(Symbol symbol){
		if(!(symbol instanceof SequenceSymbol)) return false;
		
		SequenceSymbol otherSymbol = (SequenceSymbol) symbol;
		
		if(!production.isEqual(otherSymbol.production)) return false;
		
		return hasEqualFilters(otherSymbol);
	}
}
