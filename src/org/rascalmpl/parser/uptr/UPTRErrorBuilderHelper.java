package org.rascalmpl.parser.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.parser.gtd.result.error.IErrorBuilderHelper;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class UPTRErrorBuilderHelper implements IErrorBuilderHelper{
	
	public UPTRErrorBuilderHelper(){
		super();
	}
	
	/**
	 * Checks if the given production is a list production.
	 */
	public boolean isListProduction(Object production){
		return ProductionAdapter.isRegular((IConstructor) production);
	}
	
	/**
	 * Returns the type of the production.
	 */
	public IConstructor getLHS(Object production){
		return ProductionAdapter.getType((IConstructor) production);
	}
	
	/**
	 * Returns the symbol at the given location in the production.
	 * 
	 * TODO: Alternatives aren't supported at this time.
	 */
	public IConstructor getSymbol(Object production, int dot){
		IConstructor prod = (IConstructor) production;
		
		if(!ProductionAdapter.isRegular(prod)){
			IList lhs = ProductionAdapter.getSymbols(prod);
			return (IConstructor) lhs.get(dot);
		}
		
		// Regular
		IConstructor rhs = ProductionAdapter.getType(prod);
		
		if(SymbolAdapter.isOpt(rhs)){
			return (IConstructor) rhs.get("symbol");
		}
		
		if(SymbolAdapter.isAnyList(rhs)){
			IConstructor symbol = (IConstructor) rhs.get("symbol");
			if(dot == 0){
				return symbol;
			}
			
			if(SymbolAdapter.isIterPlusSeps(rhs) || SymbolAdapter.isIterStarSeps(rhs)){
				IList separators = (IList) rhs.get("separators");
				return (IConstructor) separators.get(dot - 1);
			}
		}
		
		if(SymbolAdapter.isSequence(rhs)){
			IList symbols = (IList) rhs.get("symbols");
			return (IConstructor) symbols.get(dot);
		}
		
		if(SymbolAdapter.isAlternative(rhs)){
			throw new RuntimeException("Retrieving the correct symbol from alternatives is not possible.");
		}
		
		throw new RuntimeException("Unknown type of production: "+prod);
	}
}
