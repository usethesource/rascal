package org.rascalmpl.parser.gtd.preprocessing;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.HashMap;
import org.rascalmpl.parser.gtd.util.IntegerMap;
import org.rascalmpl.parser.gtd.util.SortedIntegerObjectList;

public class ExpectBuilder{
	private final IntegerMap resultStoreMappings;
	
	private final SortedIntegerObjectList<DoubleArrayList<IConstructor, AbstractStackNode[]>> alternatives;
	
	public ExpectBuilder(IntegerMap resultStoreMappings){
		super();
		
		this.resultStoreMappings = resultStoreMappings;
		
		alternatives = new SortedIntegerObjectList<DoubleArrayList<IConstructor, AbstractStackNode[]>>();
	}
	
	public void addAlternative(IConstructor production, AbstractStackNode... alternative){
		int alternativeLength = alternative.length;
		DoubleArrayList<IConstructor, AbstractStackNode[]> alternativesList = alternatives.findValue(alternativeLength);
		if(alternativesList == null){
			alternativesList = new DoubleArrayList<IConstructor, AbstractStackNode[]>();
			alternatives.add(alternativeLength, alternativesList);
		}
		
		// Clone the alternative so we don't get entangled in look-ahead related issues.
		AbstractStackNode[] clonedAlternative = new AbstractStackNode[alternativeLength];
		for(int i = alternativeLength - 1; i >= 0; --i){
			clonedAlternative[i] = alternative[i].getCleanCopy(AbstractStackNode.DEFAULT_START_LOCATION);
		}
		
		alternativesList.add(production, clonedAlternative);
	}
	
	// Builds the expect matrix and calculates sharing.
	public AbstractStackNode[] buildExpectArray(){
		HashMap<AbstractStackNode, AbstractStackNode[]> constructedExpects = new HashMap<AbstractStackNode, AbstractStackNode[]>();
		
		for(int i = alternatives.size() - 1; i >= 0; --i){
			DoubleArrayList<IConstructor, AbstractStackNode[]> alternativesList = alternatives.getValue(i);
			
			for(int j = alternativesList.size() - 1; j >= 0; --j){
				IConstructor production = alternativesList.getFirst(j);
				AbstractStackNode[] alternative = alternativesList.getSecond(j);
				
				AbstractStackNode first = alternative[0];
				int firstItemResultStoreId = resultStoreMappings.get(first.getId());
				
				AbstractStackNode[] sharedExpect = constructedExpects.get(first);
				if(sharedExpect == null || firstItemResultStoreId != resultStoreMappings.get(sharedExpect[0].getId())){
					alternative[alternative.length - 1].setProduction(alternative);
					alternative[alternative.length - 1].setParentProduction(production);
					
					for(int k = alternative.length - 2; k >= 0; --k){
						alternative[k].setProduction(alternative);
					}
					
					constructedExpects.putUnsafe(first, alternative);
				}else{
					int k = 1;
					CHAIN: for(; k < alternative.length; ++k){
						AbstractStackNode alternativeItem = alternative[k];
						int alternativeItemResultStoreId = resultStoreMappings.get(alternativeItem.getId());
						
						AbstractStackNode sharedExpectItem = sharedExpect[k];
						
						if(!alternativeItem.isEqual(sharedExpectItem) || alternativeItemResultStoreId != resultStoreMappings.get(sharedExpectItem.getId())){
							AbstractStackNode[][] otherSharedExpects = sharedExpectItem.getAlternateProductions();
							if(otherSharedExpects != null){
								for(int l = otherSharedExpects.length - 1; l >= 0; --l){
									AbstractStackNode[] otherSharedExpect = otherSharedExpects[l];
									AbstractStackNode otherSharedExpectItem = otherSharedExpect[k];
									if(otherSharedExpectItem.isEqual(alternativeItem) && alternativeItemResultStoreId == resultStoreMappings.get(otherSharedExpectItem.getId())){
										sharedExpect = otherSharedExpect;
										continue CHAIN;
									}
								}
							}
							
							break;
						}
						
						alternative[k] = sharedExpect[k]; // Remove 'garbage'.
					}
					
					if(k < alternative.length){
						sharedExpect[k - 1].addProduction(alternative);
					}else{
						sharedExpect[alternative.length - 1].setParentProduction(production);
					}
					
					for(; k < alternative.length; ++k){
						alternative[k].setProduction(alternative);
					}
					
					alternative[alternative.length - 1].setParentProduction(production);
				}
			}
		}
		
		int nrOfConstructedExpects = constructedExpects.size();
		AbstractStackNode[] expectMatrix = new AbstractStackNode[nrOfConstructedExpects];
		Iterator<AbstractStackNode[]> constructedExpectsIterator = constructedExpects.valueIterator();
		int i = nrOfConstructedExpects;
		while(constructedExpectsIterator.hasNext()){
			expectMatrix[--i] = constructedExpectsIterator.next()[0];
		}
		
		return expectMatrix;
	}
}
