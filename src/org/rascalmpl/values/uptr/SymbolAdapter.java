/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.values.uptr;

import static org.rascalmpl.values.uptr.RascalValueFactory.CharRange_Range;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Adt;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Alias;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Alt;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Bag;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Bool;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_CharClass;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_CiLit;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Conditional;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Cons;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Datetime;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Empty;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Func;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Int;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_IterPlus;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_IterSepX;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_IterStar;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_IterStarSepX;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Keyword;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Label;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_LayoutX;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Lex;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_List;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_ListRel;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Lit;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Loc;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Map;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Meta;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Node;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Num;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Opt;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Parameter;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_ParameterizedLex;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_ParameterizedSort;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Rat;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Real;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_ReifiedType;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Rel;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Seq;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Set;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Sort;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Start_Sort;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Str;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Tuple;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Value;
import static org.rascalmpl.values.uptr.RascalValueFactory.Symbol_Void;

import org.rascalmpl.interpreter.asserts.ImplementationError;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class SymbolAdapter {
  private static final IValueFactory VF = ValueFactoryFactory.getValueFactory();
	
	private SymbolAdapter() {
		super();
	}
	
	public static IConstructor delabel(IConstructor sym) {
		if (isLabel(sym)) {
			return (IConstructor) sym.get("symbol"); // do not use getSymbol() here!
		}
		return sym;
	}

	public static boolean isLabel(IConstructor sym) {
		return sym.getConstructorType() == Symbol_Label;
	}

	public static boolean isSort(IConstructor tree) {
		tree = delabel(tree);
		return tree.getConstructorType() == Symbol_Sort;
	}

	public static boolean isMeta(IConstructor tree) {
		tree = delabel(tree);
		return tree.getConstructorType() == Symbol_Meta;
	}
	
	public static boolean isStartSort(IConstructor tree) {
		tree = delabel(tree);
		return tree.getConstructorType() == Symbol_Start_Sort;
	}  
	
//	public static boolean isStart(IConstructor tree) {
//		tree = delabel(tree);
//		return tree.getConstructorType() == Factory.Symbol_START;
//	}
	  
	public static IConstructor getStart(IConstructor tree) {
		if (isStartSort(tree)) {
			tree = delabel(tree);
			return (IConstructor) tree.get("symbol");
		}
		throw new ImplementationError("Symbol does not have a child named symbol: " + tree);
	}

	
	public static IConstructor getLabeledSymbol(IConstructor tree) {
		return ((IConstructor) tree.get("symbol"));
	}
		
	public static IConstructor getSymbol(IConstructor tree) {
		tree = delabel(tree);
		if (isOpt(tree) || isIterPlus(tree) || isIterStar(tree)  || isIterPlusSeps(tree) || isIterStarSeps(tree) || isMeta(tree) || isConditional(tree)) {
			return ((IConstructor) tree.get("symbol"));
		}
		
		throw new ImplementationError("Symbol does not have a child named symbol: " + tree);
	}
	
	public static boolean isConditional(IConstructor tree) {
		return tree.getConstructorType() == Symbol_Conditional;
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
		
		return ((IString) tree.get("name")).getValue();
	}
	
	public static String getLabel(IConstructor tree) {
		if (isLabel(tree)) {
			return ((IString) tree.get("name")).getValue();
		}
		
		throw new ImplementationError("Symbol does not have a child named \"label\" : " + tree);
	}

	public static boolean isParameterizedSort(IConstructor tree) {
		return tree.getConstructorType() == Symbol_ParameterizedSort;
	}
	
	public static boolean isParameterizedLex(IConstructor tree) {
    return tree.getConstructorType() == Symbol_ParameterizedLex;
  }
	
	public static boolean isLiteral(IConstructor tree) {
		return tree.getConstructorType() == Symbol_Lit;
	}

	public static boolean isCILiteral(IConstructor tree) {
		return tree.getConstructorType() == Symbol_CiLit;
	}

	public static boolean isIterStar(IConstructor tree) {
		return tree.getConstructorType() == Symbol_IterStar;
	}
	
	public static boolean isIterPlus(IConstructor tree) {
		return tree.getConstructorType() == Symbol_IterPlus;
	}
	
	public static boolean isLayouts(IConstructor tree) {
		return tree.getConstructorType() == Symbol_LayoutX;
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
	
	public static boolean isOpt(IConstructor tree) {
		return delabel(tree).getConstructorType() == Symbol_Opt;
	}
	
	public static boolean isSequence(IConstructor tree){
		return delabel(tree).getConstructorType() == Symbol_Seq;
	}
	
	public static boolean isAlternative(IConstructor tree){
		return delabel(tree).getConstructorType() == Symbol_Alt;
	}

	public static String toString(IConstructor symbol, boolean withLayout) {
		// TODO: this code clones the symbol formatter impemented in Rascal. 
	  // When we have a faster Rascal (compiler?) we should remove this clone.
		
		if (isLabel(symbol)) {
			return toString((IConstructor) symbol.get("symbol"), withLayout) + " " + ((IString) symbol.get("name")).getValue();
		}
		if (isSort(symbol) || isLex(symbol) || isKeyword(symbol)) {
			return getName(symbol);
		}
		if (isEmpty(symbol)) {
			return "()";
		}
		if (isCharClass(symbol)) {
		  IList ranges = getRanges(symbol);
		  StringBuilder b = new StringBuilder();
		  b.append("[");
		  for (IValue range : ranges) {
		    IInteger from = getRangeBegin(range);
		    IInteger to = getRangeEnd(range);
		    if  (from.equals(to)) {
		      b.append(escapeChar(from.intValue()));
		    }
		    else {
		      b.append(escapeChar(from.intValue()));
		      b.append("-");
		      b.append(escapeChar(to.intValue()));
		    }
		  }
		  b.append("]");
		  return b.toString();
		}
		
		
    if (isIterPlusSeps(symbol)) {
      IList seps = getSeparators(symbol);
      StringBuilder b = new StringBuilder();
      
      if (!withLayout && seps.length() == 1 && isLayouts((IConstructor) seps.get(0))) {
        b.append(toString(getSymbol(symbol), withLayout));
        b.append('+');
      }
      else {
        b.append('{');
        b.append(toString(getSymbol(symbol), withLayout));
        for (IValue sep : seps) {
          b.append(" ");
          b.append(toString((IConstructor) sep, withLayout));
        }
        b.append('}');
        b.append('+');
      }
      
      return b.toString();
			
		}
    
		if (isIterStarSeps(symbol)) {
			StringBuilder b = new StringBuilder();
			 IList seps = getSeparators(symbol);
			 
			if (!withLayout && seps.length() == 1 && isLayouts((IConstructor) seps.get(0))) {
        b.append(toString(getSymbol(symbol), withLayout));
        b.append('*');
      }
      else {
        b.append('{');
        b.append(toString(getSymbol(symbol), withLayout));
        for (IValue sep : seps) {
          if (!isLayouts((IConstructor) sep)) {
            b.append(" ");
            b.append(toString((IConstructor) sep, withLayout));
          }
        }
        b.append('}');
        b.append('*');
      }
			return b.toString();
		}
		if (isIterPlus(symbol)) {
			return toString(getSymbol(symbol), withLayout) + '+';
		}
		if (isIterStar(symbol)) {
			return toString(getSymbol(symbol), withLayout) + '*';
		}
		if (isOpt(symbol)) {
			return toString(getSymbol(symbol), withLayout) + '?';
		}
		if (isSeq(symbol)) {
		  return "(" + toString(getSymbols(symbol), ' ', withLayout) + ")";
		}
		if (isAlt(symbol)) {
		  ISet alts = getAlternatives(symbol);
		  StringBuilder b = new StringBuilder();
		  b.append("(");
		  boolean first = true;
		  for (IValue elem : alts) {
		    if (!first) {
		      first = false;
		      b.append(" | ");
		    }
		    b.append(toString((IConstructor) elem, withLayout));
		  }
		  b.append(")");
		  return b.toString(); 
		}
		if (isLayouts(symbol)) {
			return withLayout ? "layout[" + symbol.get("name") + "]" : "";
		}
		if (isLiteral(symbol)) {
			return '"' + ((IString) symbol.get("string")).getValue() + '"';
		}
		if (isCILiteral(symbol)) {
			return '\'' + ((IString) symbol.get("string")).getValue() + '\'';
		}
		if (isParameterizedSort(symbol) || isParameterizedLex(symbol)) {
			StringBuilder b = new StringBuilder();
			b.append(getName(symbol));
			IList params = (IList) symbol.get("parameters");
			b.append('[');
			b.append(toString(params, ',', withLayout));
			b.append(']');
			return b.toString();
		}
		if (isStartSort(symbol)) {
			return "start[" + toString(getStart(symbol), withLayout) + "]";
		}
		if (isParameter(symbol)) {
			return "&" + getName(symbol);
		}
		
		if (isInt(symbol) || isStr(symbol) || isReal(symbol) || isBool(symbol) || isRat(symbol)
				|| isNode(symbol) || isValue(symbol) || isVoid(symbol) || isNum(symbol) || isDatetime(symbol) || isLoc(symbol)) {
			return symbol.getName();
		}
		
		if (isSet(symbol) || isList(symbol) || isBag(symbol) || isReifiedType(symbol)) {
			return symbol.getName() + "[" + toString((IConstructor) symbol.get("symbol"), withLayout) + "]";
		}
		
		if (isRel(symbol) || isListRel(symbol) || isTuple(symbol)) {
			StringBuilder b = new StringBuilder();
			b.append(symbol.getName());
			IList symbols = (IList) symbol.get("symbols");
			b.append('[');
			b.append(toString(symbols, ',', withLayout));
			b.append(']');
			return b.toString();
		}
		
		if (isMap(symbol)) {
			return symbol.getName() + "[" + toString((IConstructor) symbol.get("from"), withLayout) + "," + toString((IConstructor) symbol.get("to"), withLayout) + "]";
		}
		
		if (isConditional(symbol)) {
		  ISet conditions = getConditions(symbol);
		  StringBuilder b = new StringBuilder();
		  // first prefix conditions
		  for (IValue elem : conditions) {
		    IConstructor cond = (IConstructor) elem;
		    switch (cond.getConstructorType().getName()) {
		    case "precede": 
		      b.append(toString((IConstructor) cond.get("symbol"), withLayout));
		      b.append(" << ");
		      break;
		    case "not-precede":
		      b.append(toString((IConstructor) cond.get("symbol"), withLayout));
          b.append(" !<< ");
          break;
		    case "begin-of-line":
          b.append("^ ");
          break;
		    }
		  }
		  // then the symbol
		  b.append(toString(getSymbol(symbol), withLayout));
		  
		  // then the postfix conditions
		  for (IValue elem : conditions) {
		    IConstructor cond = (IConstructor) elem;
		    switch (cond.getConstructorType().getName()) {
        case "follow": 
          b.append(" >> ");
          b.append(toString((IConstructor) cond.get("symbol"), withLayout));
          break;
		    case "not-follow":
		      b.append(" !>> ");
		      b.append(toString((IConstructor) cond.get("symbol"), withLayout));
          break;
		    case "delete":
          b.append(" \\ ");
          b.append(toString((IConstructor) cond.get("symbol"), withLayout));
          break;
		    case "except":
		      b.append("!");
		      b.append(((IString) cond.get("label")).getValue());
		    case "end-of-line":
		      b.append(" $");
		      break;
		    }
      }
		    
		  return b.toString();
		}
		
		if (isADT(symbol) || isAlias(symbol)) {
			StringBuilder b = new StringBuilder();
			b.append(getName(symbol));
			IList params = (IList) symbol.get("parameters");
			
			if (!params.isEmpty()) {
				b.append('[');
				b.append(toString(params, ',', withLayout));
				b.append(']');
			}
			return b.toString();
		}
		
		if (isFunc(symbol)) {
			StringBuilder b = new StringBuilder();
			b.append(toString((IConstructor) symbol.get("ret"), withLayout));
			b.append("(");
			b.append(toString((IList) symbol.get("parameters"), ',', withLayout));
			b.append(")");
			return b.toString();
		}
		
		if (isCons(symbol)) {
			StringBuilder b = new StringBuilder();
			b.append(toString((IConstructor) symbol.get("adt"), withLayout));
			b.append(" ");
			b.append(((IString) symbol.get("name")).getValue());
			b.append("(");
			b.append(toString((IList) symbol.get("parameters"), ',', withLayout));
			b.append(")");
		}
		
		// TODO: add more to cover all different symbol constructors
		return symbol.toString();
	}

	private static ISet getConditions(IConstructor symbol) {
    return (ISet) symbol.get("conditions");
  }

  /**
	 * char-classes have almost the same escape convention as char classes except for spaces
	 * @param from
	 * @return
	 */
  private static String escapeChar(int from) {
    if (from == ' ') {
      return "\\ ";
    }
    
    String strRep = VF.string(from).toString();
    return strRep.substring(1, strRep.length() - 1);
  }

  private static IInteger getRangeEnd(IValue range) {
    return (IInteger) ((IConstructor) range).get("end");
  }

  private static IInteger getRangeBegin(IValue range) {
    return (IInteger) ((IConstructor) range).get("begin");
  }

	public static IList getRanges(IConstructor symbol) {
    return (IList) symbol.get("ranges");
  }

  public static IList getSymbols(IConstructor symbol) {
    return (IList) symbol.get("symbols");
  }

  private static String toString(IList symbols, char sep, boolean withLayout) {
		StringBuilder b = new StringBuilder();
		
		if (symbols.length() > 0) {
			b.append(toString((IConstructor) symbols.get(0), false));
			for (int i = 1; i < symbols.length(); i++) {
			  IConstructor sym = (IConstructor) symbols.get(i);
			  if (!withLayout && isLayouts(sym)) {
			    continue;
			  }
				b.append(sep);
        b.append(toString(sym, withLayout));
			}
		}
		
		return b.toString();
	}
	
	public static boolean isCons(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Cons;
	}
	
	public static boolean isFunc(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Func;
	}

	public static boolean isAlias(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Alias;
	}

	public static boolean isADT(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Adt;
	}

	public static boolean isReifiedType(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_ReifiedType;
	}

	public static boolean isBag(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Bag;
	}

	public static boolean isList(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_List;
	}

	public static boolean isSet(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Set;
	}

	public static boolean isTuple(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Tuple;
	}

	public static boolean isRel(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Rel;
	}
	
	public static boolean isListRel(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_ListRel;
	}

	public static boolean isMap(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Map;
	}

	public static boolean isDatetime(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Datetime;
	}
	
	public static boolean isLoc(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Loc;
	}

	public static boolean isNum(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Num;
	}

	public static boolean isVoid(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Void;
	}

	public static boolean isValue(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Value;
	}

	public static boolean isNode(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Node;
	}

	public static boolean isRat(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Rat;
	}

	public static boolean isBool(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Bool;
	}

	public static boolean isReal(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Real;
	}

	public static boolean isStr(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Str;
	}

	public static boolean isInt(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Int;
	}

	public static boolean isParameter(IConstructor symbol) {
		return symbol.getConstructorType() == Symbol_Parameter;
	}

	public static IConstructor getRhs(IConstructor symbol) {
		symbol = delabel(symbol);
		return (IConstructor) symbol.get("rhs");
	}
	
	public static boolean isIterStarSeps(IConstructor rhs) {
		rhs = delabel(rhs);
		return rhs.getConstructorType() == Symbol_IterStarSepX;
	}
	
	public static boolean isIterPlusSeps(IConstructor rhs) {
		rhs = delabel(rhs);
		return rhs.getConstructorType() == Symbol_IterSepX;
	}

	public static IList getSeparators(IConstructor rhs) {
		rhs = delabel(rhs);
		return (IList) rhs.get("separators");
	}

	public static boolean isLex(IConstructor rhs) {
		return rhs.getConstructorType() == Symbol_Lex;
	}
	
	public static boolean isKeyword(IConstructor rhs) {
		return rhs.getConstructorType() == Symbol_Keyword;
	}

	public static boolean isEmpty(IConstructor rhs) {
		return rhs.getConstructorType() == Symbol_Empty;
	}
	
	/**
	 * Computes symbol equality modulo lex/sort/layout/keyword distinction and modulo labels and conditions
	 */
	public static boolean isEqual(IConstructor l, IConstructor r) {
		while (isLabel(l)) {
			l = getLabeledSymbol(l);
		}
		
		while (isLabel(r)) {
			r = getLabeledSymbol(r);
		}
		
		while (isConditional(l)) {
			l = getSymbol(l);
		}
		
		while (isConditional(r)) {
			r = getSymbol(r);
		}
		
		if (isLayouts(l) && isLayouts(r)) {
			return true;
		}
		
		if (isSort(l) || isLex(l) || isKeyword(l) || isLayouts(l)) {
			if (isLex(r) || isSort(r) || isKeyword(r) || isLayouts(r)) {
				return getName(l).equals(getName(r));
			}
		}
		
		if (isParameterizedSort(l) && isParameterizedSort(r)) {
			return getName(l).equals(getName(r)) && isEqual(getParameters(l), getParameters(r));
		}
		
		if (isParameterizedLex(l) && isParameterizedLex(r)) {
      return getName(l).equals(getName(r)) && isEqual(getParameters(l), getParameters(r));
    }
		
		if (isParameter(l) && isParameter(r)) {
			return getName(l).equals(getName(r));
		}
		
		if ((isIterPlusSeps(l) && isIterPlusSeps(r)) || (isIterStarSeps(l) && isIterStarSeps(r))) {
			return isEqual(getSymbol(l), getSymbol(r)) && isEqual(getSeparators(l), getSeparators(r));
		}
		
		if ((isIterPlus(l) && isIterPlus(r)) || (isIterStar(l) && isIterStar(r)) || (isOpt(l) && isOpt(r))) {
			return isEqual(getSymbol(l), getSymbol(r));
		}
		
		if (isEmpty(l) && isEmpty(r)) {
			return true;
		}
		
		if (isAlt(l) && isAlt(r)) {
			return isEqual(getAlternatives(l), getAlternatives(r));
		}
		
		if (isSeq(l) && isSeq(r)) {
			return isEqual(getSequence(l), getSequence(r));
		}
		
		if ((isLiteral(l) && isLiteral(r)) || (isCILiteral(l) && isCILiteral(r)) || (isCharClass(l) && isCharClass(r))) {
			return l.isEqual(r);
		}
		
		return false;
	}

	private static IList getParameters(IConstructor l) {
		return (IList) l.get("parameters");
	}

	public static boolean isCharClass(IConstructor r) {
		return r.getConstructorType() == Symbol_CharClass;
	}

	private static IList getSequence(IConstructor r) {
		return (IList) r.get("symbols");
	}

	public static boolean isEqual(ISet l, ISet r) {
		if (l.size() != r.size()) {
			return false;
		}
		
		OUTER:for (IValue le : l) {
			for (IValue re : r) {
				if (isEqual((IConstructor) le, (IConstructor) re)) {
					continue OUTER; // found a match
				}

				return false; // no partner found
			}
		}

		return true;
	}

	private static ISet getAlternatives(IConstructor r) {
		return (ISet) r.get("alternatives");
	}

	public static boolean isAlt(IConstructor l) {
		return l.getConstructorType() == Symbol_Alt;
	}

	public static boolean isSeq(IConstructor l) {
		return l.getConstructorType() == Symbol_Seq;
	}
	
	public static boolean isEqual(IList l, IList r) {
		if (l.length() != r.length()) {
			return false;
		}
			
		for (int i = 0; i < l.length(); i++) {
			if (!isEqual((IConstructor) l.get(i), (IConstructor) r.get(i))) {
				return false;
			}
		}
		
		return true;
	}

	public static int getListSkipDelta(IConstructor sym) {
		if (SymbolAdapter.isIterPlus(sym) || SymbolAdapter.isIterStar(sym)){
			return 1; // new iters never have layout separators
		} 
		else if (SymbolAdapter.isIterPlusSeps(sym) || SymbolAdapter.isIterStarSeps(sym)) {
			return SymbolAdapter.getSeparators(sym).length() + 1;
		}
		assert false; // should be all cases
		return 1;
	}

	public static IConstructor starToPlus(IConstructor sym) {
		assert isStarList(sym) && !isLabel(sym) && !isConditional(sym);
		
		if (isIterStar(sym)) {
			return VF.constructor(Symbol_IterPlus, getSymbol(sym));
		}
		else if (isIterStarSeps(sym)) {
			return VF.constructor(Symbol_IterSepX, getSymbol(sym), getSeparators(sym));
		}
		
		assert false;
		return sym;
	}

	public static IConstructor charClass(int ch) {
		return VF.constructor(Symbol_CharClass, VF.list(VF.constructor(CharRange_Range, VF.integer(ch), VF.integer(ch))));
	}
}
