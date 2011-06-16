package org.rascalmpl.parser.gtd.preprocessing;

import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;
import org.rascalmpl.parser.gtd.util.SortedIntegerObjectList;

public class ExpectBuilder{
	private final SortedIntegerObjectList<ArrayList<AbstractStackNode[]>> alternatives;
	
	public ExpectBuilder(){
		super();
		
		alternatives = new SortedIntegerObjectList<ArrayList<AbstractStackNode[]>>();
	}
	
	public void addAlternative(AbstractStackNode... alternative){
		int alternativeLength = alternative.length;
		ArrayList<AbstractStackNode[]> alternativesList = alternatives.findValue(alternativeLength);
		if(alternativesList == null){
			alternativesList = new ArrayList<AbstractStackNode[]>();
			alternatives.add(alternativeLength, alternativesList);
		}
		
		alternativesList.add(alternative);
	}
	
	// Builds the expect matrix and calculates sharing.
	public AbstractStackNode[][] buildExpectMatrix(){
		IntegerObjectList<AbstractStackNode[]> constructedExpects = new IntegerObjectList<AbstractStackNode[]>();
		
		for(int i = alternatives.size() - 1; i >= 0; --i){
			ArrayList<AbstractStackNode[]> alternativesList = alternatives.getValue(i);
			
			for(int j = alternativesList.size() - 1; j >= 0; --j){
				AbstractStackNode[] alternative = alternativesList.get(j);
				
				int identifier = alternative[0].getId();
				AbstractStackNode[] sharedExpect = constructedExpects.findValue(identifier);
				if(sharedExpect == null){
					alternative[alternative.length - 1].setProduction(alternative);
					alternative[alternative.length - 1].markAsEndNode();
					
					for(int k = alternative.length - 2; k >= 0; --k){
						alternative[k].setProduction(alternative);
					}
					
					constructedExpects.add(identifier, alternative);
				}else{
					int k = 1;
					for(; k < alternative.length; ++k){
						AbstractStackNode alternativeItem = alternative[k];
						alternativeItem.setProduction(alternative);
						
						if(alternativeItem.getId() != sharedExpect[k].getId()){
							break;
						}
					}
					
					if(k < alternative.length){
						sharedExpect[k - 1].addProduction(alternative);
					}else{
						sharedExpect[alternative.length - 1].markAsEndNode();
					}
					
					for(; k < alternative.length; ++k){
						alternative[k].setProduction(alternative);
					}
					
					alternative[alternative.length - 1].markAsEndNode();
				}
			}
		}
		
		int nrOfConstructedExpects = constructedExpects.size();
		AbstractStackNode[][] expectMatrix = new AbstractStackNode[nrOfConstructedExpects][];
		for(int i = nrOfConstructedExpects - 1; i >= 0; --i){
			expectMatrix[i] = constructedExpects.getValue(i);
		}
		
		return expectMatrix;
	}
}
