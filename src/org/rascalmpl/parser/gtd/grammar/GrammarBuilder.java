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

public class GrammarBuilder{
	private final IntegerKeyedHashMap<ArrayList<Alternative>> productions;
	private final ObjectKeyedIntegerMap<String> sortMappings;
	private LookAheadRange lookAheadChain;
	private final HashMap<Symbol, ArrayList<Symbol>> restrictions;
	
	private int sortIdentifierCounter = -1;
	
	public GrammarBuilder(){
		super();
		
		productions = new IntegerKeyedHashMap<ArrayList<Alternative>>();
		sortMappings = new ObjectKeyedIntegerMap<String>();
		lookAheadChain = new LookAheadRange(-1, -1);
		restrictions = new HashMap<Symbol, ArrayList<Symbol>>();
	}
	
	private static class LookAheadRange{
		public final int start;
		public int end;
		
		public LookAheadRange next;
		
		public LookAheadRange(int start, int end){
			super();
			
			this.start = start;
			this.end = end;
		}
		
		public void insert(LookAheadRange lookAheadRange){
			if(end == lookAheadRange.start){
				LookAheadRange oldNext = next;
				if(lookAheadRange.start != lookAheadRange.end){
					next = new LookAheadRange(lookAheadRange.start, lookAheadRange.start);
					LookAheadRange nextNext = new LookAheadRange(lookAheadRange.start + 1, lookAheadRange.end);
					next.next = nextNext;
					nextNext.insert(oldNext);
				}else{
					next = lookAheadRange;
					next.next = oldNext;
				}
			}else if(end < lookAheadRange.start){
				if(next != null){
					next.insert(lookAheadRange);
				}else{
					next = lookAheadRange;
				}
			}else{
				if(start == lookAheadRange.start){
					if(end == lookAheadRange.end){
						return;
					}else if(end < lookAheadRange.end){
						LookAheadRange oldNext = next;
						next = new LookAheadRange(end + 1, lookAheadRange.end);
						next.insert(oldNext);
					}else{
						LookAheadRange oldNext = next;
						next = new LookAheadRange(lookAheadRange.end + 1, end);
						next.next = oldNext;
					}
				}else{
					if(end == lookAheadRange.end){
						LookAheadRange oldNext = next;
						next = lookAheadRange;
						end = lookAheadRange.start - 1;
						next.next = oldNext;
					}else{
						LookAheadRange oldNext = next;
						next = lookAheadRange;
						end = lookAheadRange.start - 1;
						next.insert(oldNext);
					}
				}
			}
		}
		
		public int countRanges(){
			int counter = 1;
			LookAheadRange lar = this;
			do{
				++counter;
			}while((lar = lar.next) != null);
			
			return counter;
		}
		
		public IntegerList getIdentifiers(int start, int end){
			IntegerList identifiers = new IntegerList();
			
			LookAheadRange lar = this;
			int index = 0;
			do{
				if(lar.start >= end){
					identifiers.add(index);
				}
				++index;
			}while((lar = lar.next) != null && lar.end <= start);
			
			return identifiers;
		}
	}
	
	private static class Alternative{
		public final Symbol[] symbols;
		public final char[][] lookAheadRanges;
		
		public Alternative(Symbol[] symbols, char[][] lookAheadRanges){
			super();
			
			this.symbols = symbols;
			this.lookAheadRanges = lookAheadRanges;
		}
	}
	
	public void registerAlternative(String sortName, Symbol[] symbols, char[][] lookAheadRanges){
		// Register the missing look-ahead ranges (if any).
		for(int i = lookAheadRanges.length - 1; i >= 0; --i){
			char[] lookAheadRange = lookAheadRanges[i];
			lookAheadChain.insert(new LookAheadRange(lookAheadRange[0], lookAheadRange[1]));
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
		
		alternatives.add(new Alternative(symbols, lookAheadRanges));
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
		int nrOfLookAheadRanges = lookAheadChain.countRanges() - 1;
		// TODO Assign look-ahead identifiers.
		
		int nrOfSorts = sortIdentifierCounter + 1;
		AbstractStackNode[][][] expectMatrix = new AbstractStackNode[nrOfSorts][][];
		
		Iterator<Entry<ArrayList<Alternative>>> productionIterator = productions.entryIterator();
		while(productionIterator.hasNext()){
			Entry<ArrayList<Alternative>> production = productionIterator.next();
			int sortIdentifier = production.key;
			ArrayList<Alternative> alternatives = production.value;
			
			// Get Look-ahead clusters.
			for(int i = alternatives.size() - 1; i >= 0; --i){
				Alternative alternative = alternatives.get(i);
				Symbol[] symbols = alternative.symbols;
				char[][] lookAheadRanges = alternative.lookAheadRanges;
				
				// TODO Get the alternatives
				
				AbstractStackNode[][] expectTable = new AbstractStackNode[nrOfLookAheadRanges][];
				for(int j = lookAheadRanges.length - 1; j >= 0; --j){
					char[] lookAheadRangeIdentifier = lookAheadRanges[j];
					
					// TODO Insert the alternatives.
				}
				
				expectMatrix[sortIdentifier] = expectTable;
			}
		}
		
		// TODO Handle restrictions.
		
		return null; // Temp.
	}
}
