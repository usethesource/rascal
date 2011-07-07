package org.rascalmpl.parser.gtd.grammar;

import java.util.Iterator;

import org.rascalmpl.parser.gtd.grammar.symbol.Symbol;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.HashMap;
import org.rascalmpl.parser.gtd.util.HashMap.Entry;

public class GrammarBuilder{
	private final HashMap<String, ArrayList<Alternative>> productions;
	private final HashMap<Symbol, ArrayList<Symbol>> restrictions;
	
	public GrammarBuilder(){
		super();
		
		productions = new HashMap<String, ArrayList<Alternative>>();
		restrictions = new HashMap<Symbol, ArrayList<Symbol>>();
	}
	
	private static class Alternative{
		public final Symbol[] alternative;
		public final char[][] lookAheadRanges;
		
		public Alternative(Symbol[] alternative, char[][] lookAheadRanges){
			super();
			
			this.alternative = alternative;
			this.lookAheadRanges = lookAheadRanges;
		}
	}
	
	public void registerAlternative(String sortName, Symbol[] alternative, char[][] lookAheadRanges){
		ArrayList<Alternative> alternatives = productions.get(sortName);
		if(alternatives == null){
			alternatives = new ArrayList<Alternative>();
			productions.putUnsafe(sortName, alternatives);
		}
		
		alternatives.add(new Alternative(alternative, lookAheadRanges));
	}
	
	public void restrict(Symbol child, Symbol parent){
		ArrayList<Symbol> restrictedParents = restrictions.get(child);
		if(restrictedParents == null){
			restrictedParents = new ArrayList<Symbol>();
			restrictions.putUnsafe(child, restrictedParents);
		}
		
		restrictedParents.add(parent);
	}
	
	public Grammar build(){
		Iterator<Entry<String, ArrayList<Alternative>>> productionIterator = productions.entryIterator();
		while(productionIterator.hasNext()){
			Entry<String, ArrayList<Alternative>> production = productionIterator.next();
			String sortName = production.key;
			ArrayList<Alternative> alternatives = production.value;
			
			// Get Look-ahead clusters.
			for(int i = alternatives.size() - 1; i >= 0; --i){
				Alternative alternative = alternatives.get(i);
				char[][] lookAheadRanges = alternative.lookAheadRanges;
				for(int j = lookAheadRanges.length - 1; j >= 0; --j){
					char[] range = lookAheadRanges[j];
					char start = range[0];
					char end = range[1];
					// TODO Implement (sort clusters and split on collision).
				}
			}
			
			// TODO Get the alternatives, bundle them by look-ahead range and construct the expects.
			
		}
		
		// TODO Handle restrictions.
		
		return null; // Temp.
	}
}
