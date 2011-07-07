package org.rascalmpl.parser.gtd.grammar;

import java.util.Iterator;

import org.rascalmpl.parser.gtd.grammar.symbol.Symbol;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.HashMap;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.ObjectKeyedIntegerMap;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap.Entry;

// TODO Split look ahead ranges into unique sections.
public class GrammarBuilder{
	private final IntegerKeyedHashMap<ArrayList<Alternative>> productions;
	private final ObjectKeyedIntegerMap<String> sortMappings;
	private final ObjectKeyedIntegerMap<LookAheadRange> lookAheadMappings;
	private final HashMap<Symbol, ArrayList<Symbol>> restrictions;
	
	private int sortIdentifierCounter = -1;
	private int lookAheadIdentifierCounter = -1;
	
	public GrammarBuilder(){
		super();
		
		productions = new IntegerKeyedHashMap<ArrayList<Alternative>>();
		sortMappings = new ObjectKeyedIntegerMap<String>();
		lookAheadMappings = new ObjectKeyedIntegerMap<LookAheadRange>();
		restrictions = new HashMap<Symbol, ArrayList<Symbol>>();
	}
	
	private static class LookAheadRange{
		public final char[] lookAheadRange;
		
		public LookAheadRange(char[] lookAheadRange){
			super();
			
			this.lookAheadRange = lookAheadRange;
		}
		
		public int hashCode(){
			return lookAheadRange[0] + lookAheadRange[1];
		}
		
		public boolean equals(Object other){
			if(!(other instanceof LookAheadRange)) return false;
			
			LookAheadRange otherRange = (LookAheadRange) other;
			
			return (lookAheadRange[0] == otherRange.lookAheadRange[0] && lookAheadRange[1] == otherRange.lookAheadRange[1]);
		}
	}
	
	private static class Alternative{
		public final Symbol[] symbols;
		public final IntegerList lookAheadRangeIdentifiers;
		
		public Alternative(Symbol[] symbols, IntegerList lookAheadRangeIdentifiers){
			super();
			
			this.symbols = symbols;
			this.lookAheadRangeIdentifiers = lookAheadRangeIdentifiers;
		}
	}
	
	public void registerAlternative(String sortName, Symbol[] symbols, char[][] lookAheadRanges){
		IntegerList lookAheadRangeIdentifiers = new IntegerList();
		
		// Register the missing look-ahead ranges (if any).
		for(int i = lookAheadRanges.length - 1; i >= 0; --i){
			char[] lookAheadRange = lookAheadRanges[i];
			LookAheadRange lookAheadRangeNode = new LookAheadRange(lookAheadRange);
			int lookAheadRangeIdentifier = lookAheadMappings.get(lookAheadRangeNode);
			if(lookAheadRangeIdentifier == -1){
				lookAheadMappings.putUnsafe(lookAheadRangeNode, (lookAheadRangeIdentifier = ++lookAheadIdentifierCounter));
			}
			lookAheadRangeIdentifiers.add(lookAheadRangeIdentifier);
		}
		
		// Register the sort (if it hasn't been already).
		int sortIdentifier = sortMappings.get(sortName);
		if(sortIdentifier == -1){
			sortMappings.putUnsafe(sortName, (sortIdentifier = ++sortIdentifierCounter));
		}
		
		// Register the alternative.
		ArrayList<Alternative> alternatives = productions.get(sortIdentifier);
		if(alternatives == null){
			alternatives = new ArrayList<Alternative>();
			productions.putUnsafe(sortIdentifier, alternatives);
		}
		
		alternatives.add(new Alternative(symbols, lookAheadRangeIdentifiers));
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
		int nrOfSorts = sortIdentifierCounter + 1;
		AbstractStackNode[][][] expectMatrix = new AbstractStackNode[nrOfSorts][][];
		
		int nrOfLookAheadRanges = lookAheadIdentifierCounter + 1;
		
		Iterator<Entry<ArrayList<Alternative>>> productionIterator = productions.entryIterator();
		while(productionIterator.hasNext()){
			Entry<ArrayList<Alternative>> production = productionIterator.next();
			int sortIdentifier = production.key;
			ArrayList<Alternative> alternatives = production.value;
			
			// Get Look-ahead clusters.
			for(int i = alternatives.size() - 1; i >= 0; --i){
				Alternative alternative = alternatives.get(i);
				IntegerList lookAheadRangeIdentifiers = alternative.lookAheadRangeIdentifiers;
				Symbol[] symbols = alternative.symbols;
				
				// TODO Get the alternatives
				
				AbstractStackNode[][] expectTable = new AbstractStackNode[nrOfLookAheadRanges][];
				for(int j = lookAheadRangeIdentifiers.size() - 1; j >= 0; --j){
					int lookAheadRangeIdentifier = lookAheadRangeIdentifiers.get(j);
					
					// TODO Insert the alternatives.
				}
				
				expectMatrix[sortIdentifier] = expectTable;
			}
		}
		
		// TODO Handle restrictions.
		
		return null; // Temp.
	}
}
