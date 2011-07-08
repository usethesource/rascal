package org.rascalmpl.parser.gtd.grammar;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.grammar.symbol.Symbol;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.HashMap;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.ObjectKeyedIntegerMap;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap.Entry;

// TODO Consolidate expect id generation.
public class GrammarBuilder{
	private final IntegerKeyedHashMap<ArrayList<Alternative>> productions;
	private final ObjectKeyedIntegerMap<String> sortMappings;
	private final LookAheadRange lookAheadChain;
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
			int counter = 0;
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
		public final IConstructor prod;
		public final Symbol[] symbols;
		public final char[][] lookAheadRanges;
		
		public Alternative(IConstructor prod, Symbol[] symbols, char[][] lookAheadRanges){
			super();
			
			this.prod = prod;
			this.symbols = symbols;
			this.lookAheadRanges = lookAheadRanges;
		}
	}
	
	public void registerAlternative(String sortName, IConstructor prod, Symbol[] symbols, char[][] lookAheadRanges){
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
		
		alternatives.add(new Alternative(prod, symbols, lookAheadRanges));
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
		State state = new State();
		
		int nrOfLookAheadRanges = lookAheadChain.countRanges() - 1;
		
		int nrOfSorts = sortIdentifierCounter + 1;
		AbstractStackNode[][][] expectMatrix = new AbstractStackNode[nrOfSorts][nrOfLookAheadRanges][];
		
		Iterator<Entry<ArrayList<Alternative>>> productionIterator = productions.entryIterator();
		while(productionIterator.hasNext()){
			Entry<ArrayList<Alternative>> production = productionIterator.next();
			int sortIdentifier = production.key;
			ArrayList<Alternative> alternatives = production.value;
			
			// Gather the data for the construction of look-ahead clusters.
			IntegerKeyedHashMap<DoubleArrayList<IConstructor, Symbol[]>> lookAheadClusters = new IntegerKeyedHashMap<DoubleArrayList<IConstructor, Symbol[]>>();
			for(int i = alternatives.size() - 1; i >= 0; --i){
				Alternative alternative = alternatives.get(i);
				IConstructor prod = alternative.prod;
				Symbol[] symbols = alternative.symbols;
				char[][] lookAheadRanges = alternative.lookAheadRanges;
				
				for(int j = lookAheadRanges.length - 1; j >= 0; --j){
					char[] lookAheadRange = lookAheadRanges[j];
					int startOfRange = lookAheadRange[0];
					int endOfRange = lookAheadRange[1];
					
					if(endOfRange <= Grammar.MAX_CODE_POINT){
						IntegerList lookAheadIdentifiers = lookAheadChain.getIdentifiers(startOfRange, endOfRange);
						
						for(int k = lookAheadIdentifiers.size() - 1; k >= 0; --k){
							int lookAheadIdentifier = lookAheadIdentifiers.get(k);
							
							DoubleArrayList<IConstructor, Symbol[]> lookAheadCluster = lookAheadClusters.get(lookAheadIdentifier);
							if(lookAheadCluster == null){
								lookAheadCluster = new DoubleArrayList<IConstructor, Symbol[]>();
								lookAheadClusters.putUnsafe(lookAheadIdentifier, lookAheadCluster);
							}
							lookAheadCluster.add(prod, symbols);
						}
					}else{ // Treat Unicode code points differently.
						int lookAheadIdentifier = Grammar.UNICODE_OVERFLOW_SLOT;
						
						DoubleArrayList<IConstructor, Symbol[]> lookAheadCluster = lookAheadClusters.get(lookAheadIdentifier);
						if(lookAheadCluster == null){
							lookAheadCluster = new DoubleArrayList<IConstructor, Symbol[]>();
							lookAheadClusters.putUnsafe(lookAheadIdentifier, lookAheadCluster);
						}
						lookAheadCluster.add(prod, symbols);
					}
				}
			}
			
			// Build the alternatives for each look-ahead cluster.
			Iterator<Entry<DoubleArrayList<IConstructor, Symbol[]>>> lookAheadClustersIterator = lookAheadClusters.entryIterator();
			while(lookAheadClustersIterator.hasNext()){
				Entry<DoubleArrayList<IConstructor, Symbol[]>> lookAheadClusterEntry = lookAheadClustersIterator.next();
				int lookAheadIdentifier = lookAheadClusterEntry.key;
				DoubleArrayList<IConstructor, Symbol[]> lookAheadCluster = lookAheadClusterEntry.value;
				
				ExpectBuilder expectBuilder = new ExpectBuilder();
				
				for(int j = lookAheadCluster.size() - 1; j >= 0; --j){
					IConstructor prod = lookAheadCluster.getFirst(j);
					Symbol[] symbols = lookAheadCluster.getSecond(j);
					AbstractStackNode[] symbolNodes = new AbstractStackNode[symbols.length];
					for(int k = symbols.length - 1; k >= 0; --k){
						symbolNodes[k] = symbols[k].buildNode(state, k);
					}
					
					expectBuilder.addAlternative(prod, symbolNodes);
				}
				
				expectMatrix[sortIdentifier][lookAheadIdentifier] = expectBuilder.buildExpectArray();
			}
		}
		
		// Build the look-ahead table.
		int[] lookAheadTable = new int[Grammar.LOOK_AHEAD_TABLE_SIZE];
		
		LookAheadRange lookAheadRange = lookAheadChain.next;
		int lookAheadIdentifier = -1;
		do{
			for(int i = lookAheadRange.end - 1; i >= lookAheadRange.start; --i){
				lookAheadTable[i] = ++lookAheadIdentifier;
			}
		}while((lookAheadRange = lookAheadRange.next) != null);
		
		// TODO Handle restrictions.
		
		
		return new Grammar(expectMatrix, lookAheadTable);
	}
}
