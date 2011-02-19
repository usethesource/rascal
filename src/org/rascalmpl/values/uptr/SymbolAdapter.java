package org.rascalmpl.values.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.asserts.ImplementationError;

public class SymbolAdapter {
	
	private SymbolAdapter() {
		super();
	}
	
	private static IConstructor delabel(IConstructor sym) {
		if (isLabel(sym)) {
			return (IConstructor) sym.get("symbol"); // do not use getSymbol() here!
		}
		return sym;
	}

	public static boolean isLabel(IConstructor sym) {
		return sym.getConstructorType() == Factory.Symbol_Label;
	}

	public static boolean isSort(IConstructor tree) {
		tree = delabel(tree);
		return tree.getConstructorType() == Factory.Symbol_Sort;
	}
	
	public static boolean isStartSort(IConstructor tree) {
		tree = delabel(tree);
		return tree.getConstructorType() == Factory.Symbol_Start_Sort;
	}  
	
	public static boolean isStart(IConstructor tree) {
		tree = delabel(tree);
		return tree.getConstructorType() == Factory.Symbol_START;
	}
	  
	public static IConstructor getStart(IConstructor tree) {
		if (isStartSort(tree)) {
			tree = delabel(tree);
			return (IConstructor) tree.get("start");
		}
		throw new ImplementationError("Symbol does not have a child named start: " + tree);
	}

	
	public static IConstructor getLabeledSymbol(IConstructor tree) {
		return ((IConstructor) tree.get("symbol"));
	}
		
	public static IConstructor getSymbol(IConstructor tree) {
		tree = delabel(tree);
		if (isOpt(tree) || isIterPlus(tree) || isIterStar(tree)  || isIterPlusSeps(tree) || isIterStarSeps(tree)) {
			return ((IConstructor) tree.get("symbol"));
		}
		
		throw new ImplementationError("Symbol does not have a child named symbol: " + tree);
	}
	
	public static String getLabelName(IConstructor tree) {
		return ((IString) tree.get("name")).getValue();
	}
	
	/**
	 * Use this to get a name of a sort or parameterized sort, not to get the name of a label.
	 * @param tree sort or parameterized sort
	 * @return the name of the sort
	 */
	public static String getName(IConstructor tree) {
		tree = delabel(tree);
		
		if (isSort(tree)) {
			return ((IString) tree.get("string")).getValue();
		}
		else if (isParameterizedSort(tree)) {
			return ((IString) tree.get("sort")).getValue();
		}
		else {
			throw new ImplementationError("Symbol does not have a child named \"name\": " + tree);
		}
	}
	
	public static String getLabel(IConstructor tree) {
		if (isLabel(tree)) {
			return ((IString) tree.get("name")).getValue();
		}
		
		throw new ImplementationError("Symbol does not have a child named \"label\" : " + tree);
	}

	public static boolean isParameterizedSort(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_ParameterizedSort;
	}
	
	public static boolean isLiteral(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_Lit;
	}

	public static boolean isCILiteral(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_CiLit;
	}

	public static boolean isIterStar(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_IterStar;
	}
	
	public static boolean isIterPlus(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_IterPlus;
	}
	
	public static boolean isLayouts(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_LayoutX;
	}
	
	public static boolean isStarList(IConstructor tree) {
		tree = delabel(tree);
		return isIterStar(tree) || isIterStarSeps(tree) ;
	}
	
	public static boolean isPlusList(IConstructor tree) {
		tree = delabel(tree);
		return isIterPlus(tree) || isIterPlusSeps(tree);
	}
	
	public static boolean isSepList(IConstructor tree){
		tree = delabel(tree);
		return isIterPlusSeps(tree) || isIterStarSeps(tree);
	}
	
	public static boolean isAnyList(IConstructor tree) {
		tree = delabel(tree);
		return isStarList(tree) || isPlusList(tree);
	}
	
	public static boolean isCfOptLayout(IConstructor tree) {
		return false;
	}
	
	public static boolean isOpt(IConstructor tree) {
		tree = delabel(tree);
		return tree.getConstructorType() == Factory.Symbol_Opt;
	}

	public static String toString(IConstructor symbol) {
		return symbol.toString();
	}
	
	public static IConstructor getRhs(IConstructor symbol) {
		symbol = delabel(symbol);
		return (IConstructor) symbol.get("rhs");
	}
	
	public static boolean isIterStarSeps(IConstructor rhs) {
		rhs = delabel(rhs);
		return rhs.getConstructorType() == Factory.Symbol_IterStarSepX;
	}
	
	public static boolean isIterPlusSeps(IConstructor rhs) {
		rhs = delabel(rhs);
		return rhs.getConstructorType() == Factory.Symbol_IterSepX;
	}

	public static IList getSeparators(IConstructor rhs) {
		rhs = delabel(rhs);
		return (IList) rhs.get("separators");
	}
}
