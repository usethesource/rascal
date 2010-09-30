package org.rascalmpl.values.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.asserts.ImplementationError;

public class SymbolAdapter {
	
	private SymbolAdapter() {
		super();
	}

	public static boolean isCf(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_Cf;
	}
	
	public static boolean isLabel(IConstructor sym) {
		return sym.getConstructorType() == Factory.Symbol_Label;
	}

	public static boolean isLex(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_Lex;
	}
	
	public static boolean isSort(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_Sort;
	}
	
	public static boolean isStartSort(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_Start_Sort;
	}  
	
	public static boolean isStart(IConstructor tree) {
		return tree.getConstructorType() == Factory.Symbol_START;
	}
	  
	public static IConstructor getStart(IConstructor tree) {
		if (isStartSort(tree)) {
			return (IConstructor) tree.get("start");
		}
		throw new ImplementationError("Symbol does not have a child named start: " + tree);
	}

	public static IConstructor getSymbol(IConstructor tree) {
		if (isLabel(tree) || isCf(tree) || isLex(tree) || isOpt(tree) || isIterPlus(tree) || isIterPlusSep(tree) || isIterStar(tree) || isIterStarSep(tree)) {
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
		else if (isLabel(tree)) {
			return ((IString) tree.get("name")).getValue();
		}
		else {
			throw new ImplementationError("Symbol does not have a child named \"name\": " + tree);
		}
	}
	
	public static String getLabel(IConstructor tree) {
		if (isLabel(tree)) {
			return ((IString) tree.get("label")).getValue();
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
		if(isProductionList(tree)){
			return isLayout((IConstructor) tree.get("rhs"));
		}
		if (isCf(tree) || isLex(tree) || isOpt(tree) || isIterPlus(tree) || isIterPlusSep(tree) || isIterStar(tree) || isIterStarSep(tree)){
			IConstructor t = (IConstructor) tree.get("symbol");
			if(t.getConstructorType() == Factory.Symbol_Layout){
				return true;
			}
			return isLayout(t);
		}
		
		return (tree.getConstructorType() == Factory.Symbol_Layout);
	}
	
	private static boolean isProductionList(IConstructor tree){
		return (tree.getConstructorType() == Factory.Production_List);
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
	
	public static boolean isSepList(IConstructor tree){
		if (isCf(tree) || SymbolAdapter.isLex(tree)) {
			tree = getSymbol(tree);
		}
		return isIterStarSep(tree) || isIterPlusSep(tree);
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

	public static String toString(IConstructor symbol) {
		if (isCf(symbol) || isLex(symbol)) {
			return toString(getSymbol(symbol));
		}
		if (isSort(symbol)) {
			return getName(symbol);
		}
		if (isIterPlus(symbol)) {
			return toString(getSymbol(symbol)) + '+';
		}
		if (isIterStar(symbol)) {
			return toString(getSymbol(symbol)) + '*';
		}
		if (isIterStarSep(symbol)) {
			return '{' + toString(getSymbol(symbol)) + ' ' + toString(getSeparator(symbol)) + '}' + '*';
		}
		if (isIterPlusSep(symbol)) {
			return '{' + toString(getSymbol(symbol)) + ' ' + toString(getSeparator(symbol)) + '}' + '+';
		}
		if (isOpt(symbol)) {
			return toString(getSymbol(symbol)) + '?';
		}
		if (isLayout(symbol)) {
			return "LAYOUT";
		}
		if (isLiteral(symbol)) {
			return '\"' + ((IString) symbol.get("string")).getValue() + '\"';
		}
		if (isCILiteral(symbol)) {
			return '\'' + ((IString) symbol.get("string")).getValue() + '\'';
		}
		if (isAlt(symbol)) {
			return toString(getLhs(symbol)) + ' ' + '|' + ' ' + toString(getRhs(symbol));
		}
		if (isSeq(symbol)) {
			StringBuilder b = new StringBuilder();
			b.append('(');
			b.append(' ');
			for (IValue elem : getSymbols(symbol)) {
				b.append(toString((IConstructor) elem));
				b.append(' ');
			}
			b.append(')');
		}
		
		if (isParameterizedSort(symbol)) {
			StringBuilder b = new StringBuilder();
			b.append(getName(symbol));
			b.append('[');
			b.append('[');
			for (IValue elem : getSymbols(symbol)) {
				b.append(toString((IConstructor) elem));
				b.append(',');
			}
			b.append(']');
			b.append(']');
		}
		
		if (isEmpty(symbol)) {
			return "()";
		}
		
		if (isCharClass(symbol)) {
			for (IValue range : getRanges(symbol)) {
				IConstructor r = (IConstructor) range;
				if (r.getConstructorType() == Factory.CharRange_Single) {
					return decode((IInteger) r.get("start"));
				}
				if (r.getConstructorType() == Factory.CharRange_Range) {
					return '[' +  decode((IInteger) r.get("start")) + '-' + decode((IInteger) r.get("end"))+ ']';
				}
			}
		}
		
		// TODO more variants
		return symbol.toString();
	}


	private static String decode(IInteger iInteger) {
		char ch = (char) iInteger.intValue();
		
		if (Character.isLetterOrDigit(ch)) {
			return String.valueOf(ch);
		}
		
		switch (ch) {
		case '\n':
			return "\\n";
		case '\t':
			return "\\t";
		case '\r':
			return "\\r"; 
		case ' ':
			return "\\ ";
		}

		int digit1 = ch % 10;
		ch = (char) (ch / 10);
		int digit2 = ch % 10;
		ch = (char) (ch / 10);
		int digit3 = ch % 10;
		
		return "\\" + digit1 + digit2 + digit3;
	}

	private static IList getRanges(IConstructor symbol) {
		return (IList) symbol.get("ranges");
	}

	private static boolean isCharClass(IConstructor symbol) {
		return symbol.getConstructorType() == Factory.Symbol_CharClass;
	}

	private static boolean isEmpty(IConstructor symbol) {
		return symbol.getConstructorType() == Factory.Symbol_Empty;
	}

	private static IList getSymbols(IConstructor symbol) {
		return (IList) symbol.get("symbols");
	}

	private static boolean isSeq(IConstructor symbol) {
		return symbol.getConstructorType() == Factory.Symbol_Seq;
	}

	public static IConstructor getRhs(IConstructor symbol) {
		return (IConstructor) symbol.get("rhs");
	}

	private static IConstructor getLhs(IConstructor symbol) {
		return (IConstructor) symbol.get("lhs");
	}

	private static boolean isAlt(IConstructor symbol) {
		return symbol.getConstructorType() == Factory.Symbol_Alt;
	}

	public static boolean isIterStarSeps(IConstructor rhs) {
		return rhs.getConstructorType() == Factory.Symbol_IterStarSepX;
	}
	
	public static boolean isIterPlusSeps(IConstructor rhs) {
		return rhs.getConstructorType() == Factory.Symbol_IterSepX;
	}

	public static IList getSeparators(IConstructor rhs) {
		return (IList) rhs.get("separators");
	}
}
