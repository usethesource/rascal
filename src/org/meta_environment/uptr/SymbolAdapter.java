package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;

public class SymbolAdapter {
	
	private SymbolAdapter() {
		super();
	}

	public static boolean isCf(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_Cf;
	}

	public static boolean isLex(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_Lex;
	}
	
	public static boolean isSort(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_Sort;
	}

	public static IConstructor getSymbol(IConstructor tree) {
		if (isCf(tree) || isLex(tree) || isOpt(tree) || isIterPlus(tree) || isIterPlusSep(tree) || isIterStar(tree) || isIterStarSep(tree)) {
			return ((IConstructor) tree.get("symbol"));
		}
		
		throw new ImplementationError("Symbol does not have a child named symbol: " + tree);
	}
	
	public static IConstructor getSeparator(IConstructor tree) {
		if (isIterPlusSep(tree) || isIterStarSep(tree)) {
			return (IConstructor) tree.get("separator");
		}
		
		throw new ImplementationError("Symbol does not have a child named separator: " + tree);
	}
	
	public static String getName(IConstructor tree) {
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

	public static boolean isParameterizedSort(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_ParameterizedSort;
	}
	
	public static boolean isLiteral(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_Lit;
	}

	public static boolean isCILiteral(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_CiLit;
	}

	public static boolean isIterPlusSep(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_IterPlusSep;
	}
	
	public static boolean isIterStar(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_IterStar;
	}
	
	public static boolean isIterPlus(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_IterPlus;
	}
	
	public static boolean isLayout(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_Layout;
	}
	
	public static boolean isStarList(IConstructor tree) {
		if (isCf(tree) || SymbolAdapter.isLex(tree)) {
			tree = SymbolAdapter.getSymbol(tree);
		}
		
		return isIterStar(tree) || isIterStarSep(tree);
	}
	
	public static boolean isPlusList(IConstructor tree) {
		if (isCf(tree) || SymbolAdapter.isLex(tree)) {
			tree = getSymbol(tree);
		}
		
		return isIterPlus(tree) || isIterPlusSep(tree);
	}
	
	public static boolean isAnyList(IConstructor tree) {
		return isStarList(tree) || isPlusList(tree);
	}
	
	public static boolean isCfOptLayout(IConstructor tree) {
		if (isCf(tree)) {
			tree = getSymbol(tree);
			if (isOpt(tree)) {
				tree = getSymbol(tree);
				if (isLayout(tree)) {
					return true;
				}
			}
		}
		
		return false;
	}
	private static boolean isOpt(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_Opt;
	}

	public static boolean isIterStarSep(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_IterStarSep;
	}
}
