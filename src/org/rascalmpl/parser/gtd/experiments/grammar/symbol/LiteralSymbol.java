package org.rascalmpl.parser.gtd.experiments.grammar.symbol;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.experiments.grammar.State;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.CaseInsensitiveLiteralStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

public class LiteralSymbol extends Symbol{
	private final IConstructor production;
	private final char[] literal;
	private final boolean isCaseSensitive;
	
	public LiteralSymbol(IConstructor production, char[] literal, boolean isCaseSensitive){
		this(production, literal, isCaseSensitive, null, null);
	}
	
	public LiteralSymbol(IConstructor production, char[] literal, boolean isCaseSensitive, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(enterFilters, completionFilters);
		
		this.production = production;
		this.literal = literal;
		this.isCaseSensitive = isCaseSensitive;
	}
	
	public AbstractStackNode buildNode(State state, int dot){
		if(!isCaseSensitive){
			return new LiteralStackNode(state.getNextId(this), dot, production, literal, enterFilters, completionFilters);
		}
		
		return new CaseInsensitiveLiteralStackNode(state.getNextId(this), dot, production, literal, enterFilters, completionFilters);
	}
	
	public boolean isEqual(Symbol symbol){
		if(!(symbol instanceof LiteralSymbol)) return false;
		
		LiteralSymbol otherSymbol = (LiteralSymbol) symbol;
		
		if(!production.isEqual(otherSymbol.production)) return false;
		
		return hasEqualFilters(otherSymbol);
	}
}
