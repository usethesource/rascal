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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.values.parsetrees;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.regex.Pattern;

import org.rascalmpl.ast.CaseInsensitiveStringConstant;
import org.rascalmpl.ast.Char;
import org.rascalmpl.ast.Class;
import org.rascalmpl.ast.Nonterminal;
import org.rascalmpl.ast.Range;
import org.rascalmpl.ast.StringConstant;
import org.rascalmpl.ast.Sym;
import org.rascalmpl.ast.Type;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.parser.ASTBuilder;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.StandardTextReader;

import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class SymbolFactory {
	private static IValueFactory factory = ValueFactoryFactory.getValueFactory();
	private static ASTBuilder builder = new ASTBuilder();
	
	public static IConstructor typeToSymbol(ITree parseTree, boolean lex, String layout) {
		return SymbolFactory.typeToSymbol((Sym) builder.buildValue(parseTree), lex, layout);
	}

	public static IConstructor typeToSymbol(Sym type, boolean lex, String layout) {
	  return (IConstructor) symbolAST2SymbolConstructor(type, lex, layout);
  	}
  
	public static IConstructor typeToSymbol(Type type, boolean lex, String layout) {
		if (type.isUser()) {
			if (lex) {
				return factory.constructor(RascalValueFactory.Symbol_Lex, factory.string(((org.rascalmpl.semantics.dynamic.QualifiedName.Default) type.getUser().getName()).lastName()));
			}
			else {
				return factory.constructor(RascalValueFactory.Symbol_Sort, factory.string(Names.name(Names.lastName(type.getUser().getName()))));
			}
		}
		
		if (type.isSymbol()) {
			return (IConstructor) symbolAST2SymbolConstructor(type.getSymbol(), lex, layout);
		}

		throw new RuntimeException("Can't convert type to symbol: "+type);
	}
	
	// TODO: distribute this code over the dynamic.Sym classes in typeOf method
	private static IValue symbolAST2SymbolConstructor(Sym symbol, boolean lex, String layout) {
		boolean noExpand = lex || layout == null;

		if (symbol.isCaseInsensitiveLiteral()) {
			return factory.constructor(RascalValueFactory.Symbol_Cilit, ciliteral2Symbol(symbol.getCistring()));
		}
		if (symbol.isCharacterClass()) {
			Class cc = symbol.getCharClass();
			return charclass2Symbol(cc);
		}
		if (symbol.isIter()) {
			if (noExpand) {
				return factory.constructor(RascalValueFactory.Symbol_Iter, symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout));
			}
			else {
				IValue layoutSymbol = factory.constructor(RascalValueFactory.Symbol_Layouts, factory.string(layout));
				IValue elementSym = symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout);
				IValue seps = factory.list(layoutSymbol);
				return factory.constructor(RascalValueFactory.Symbol_IterSeps, elementSym, seps);
			}
		}
		if (symbol.isIterStar()) {
			if (noExpand) {
				return factory.constructor(RascalValueFactory.Symbol_IterStar, symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout));
			}
			else {
				IValue layoutSymbol = factory.constructor(RascalValueFactory.Symbol_Layouts, factory.string(layout));
				IValue elementSym = symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout);
				IValue seps = factory.list(layoutSymbol);
				return factory.constructor(RascalValueFactory.Symbol_IterStarSeps, elementSym, seps);
			}
		}
		if (symbol.isIterSep()) {
			IValue layoutSymbol = factory.constructor(RascalValueFactory.Symbol_Layouts, factory.string(layout));
			IValue elementSym = symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout);
			IValue sepSym = symbolAST2SymbolConstructor(symbol.getSep(), lex, layout);
			IValue seps;
			
			if (noExpand) {
				seps = factory.list(sepSym);
			}
			else {
				seps = factory.list(layoutSymbol, sepSym, layoutSymbol);
			}
			
			return factory.constructor(RascalValueFactory.Symbol_IterSeps, elementSym, seps);
		}
		
		if (symbol.isIterStarSep()) {
			IValue layoutSymbol = factory.constructor(RascalValueFactory.Symbol_Layouts, factory.string(layout));
			IValue elementSym = symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout);
			IValue sepSym = symbolAST2SymbolConstructor(symbol.getSep(), lex, layout);
			IValue seps;
			if (noExpand) {
				seps = factory.list(sepSym);
			}
			else {
				seps = factory.list(layoutSymbol, sepSym, layoutSymbol);
			}
			return factory.constructor(RascalValueFactory.Symbol_IterStarSeps, elementSym, seps);
		}
		
		if (symbol.isLiteral()) {
			return literal2Symbol(symbol.getString());
		}
		if (symbol.isOptional()) {
			return factory.constructor(RascalValueFactory.Symbol_Opt, symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout));
		}
		
		if (symbol.isStart()) {
			Nonterminal nonterminal = symbol.getNonterminal();
			return factory.constructor(RascalValueFactory.Symbol_Start, factory.constructor(RascalValueFactory.Symbol_Sort, factory.string(((Nonterminal.Lexical) nonterminal).getString())));	
		}
		if (symbol.isNonterminal()) {
			Nonterminal nonterminal = symbol.getNonterminal();
			IString name = factory.string(((Nonterminal.Lexical) nonterminal).getString());
			if (lex) {
				return factory.constructor(RascalValueFactory.Symbol_Lex, name);
			}
			else {
				return factory.constructor(RascalValueFactory.Symbol_Sort, name);
			}
		}
		
		if(symbol.isSequence()){
			List<Sym> symbols = symbol.getSequence();
			IValue layoutSymbol = factory.constructor(RascalValueFactory.Symbol_Layouts, factory.string(layout));
			IValue[] symValues = new IValue[noExpand ? symbols.size() : symbols.size() * 2 - 1];
			for(int i = symbols.size() - 1; i >= 0; i -= noExpand ? 1 : 2) {
				symValues[noExpand ? i : i * 2] = symbolAST2SymbolConstructor(symbols.get(i), lex, layout);
				if (!noExpand && i > 0) {
					symValues[i - 1] = layoutSymbol;
				}
			}
			IValue syms = factory.list(symValues);
			return factory.constructor(RascalValueFactory.Symbol_Seq, syms);
		}
		if(symbol.isAlternative()){
			List<Sym> symbols = symbol.getAlternatives();
			IValue[] symValues = new IValue[symbols.size()];
			for(int i = symbols.size() - 1; i >= 0; --i){
				symValues[i] = symbolAST2SymbolConstructor(symbols.get(i), lex, layout);
			}
			IValue syms = factory.set(symValues);
			return factory.constructor(RascalValueFactory.Symbol_Alt, syms);
		}
		if (symbol.isParametrized()) {
			List<Sym> symbols = symbol.getParameters();
			IValue[] symValues = new IValue[symbols.size()];
			for(int i = symbols.size() - 1; i >= 0; --i){
				symValues[i] = symbolAST2SymbolConstructor(symbols.get(i), lex, layout);
			}
			IValue syms = factory.list(symValues);
			return factory.constructor(RascalValueFactory.Symbol_ParameterizedSort, factory.string(((Nonterminal.Lexical) symbol.getNonterminal()).getString()), syms);
		}
		
		if (symbol.isParameter()) {
			IConstructor treeSymbol = factory.constructor(RascalValueFactory.Symbol_Adt, factory.string("Tree"), factory.listWriter().done());
			return factory.constructor(RascalValueFactory.Symbol_Parameter, factory.string(((Nonterminal.Lexical) symbol.getNonterminal()).getString()), treeSymbol);
		}
		
		if (symbol.isPrecede() || symbol.isNotPrecede() || symbol.isFollow() || symbol.isNotFollow() || symbol.isColumn() || symbol.isStartOfLine() || symbol.isEndOfLine() || symbol.isExcept()) {
		  return symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout);
		}
		
		throw new RuntimeException("Symbol has unknown type: "+ symbol);
	}

	private static IValue literal2Symbol(StringConstant sep) {
		try {
			String lit = ((StringConstant.Lexical) sep).getString();
			// this should be the exact notation for string literals in vallang
			IValue string = new StandardTextReader().read(factory, new StringReader(lit));

			return factory.constructor(RascalValueFactory.Symbol_Lit, string);
		}
		catch (FactTypeUseException | IOException e) {
			throw new RuntimeException("Internal error: parsed stringconstant notation does not coincide with vallang stringconstant notation");
		}
	}
	
	private static IValue ciliteral2Symbol(CaseInsensitiveStringConstant constant) {
		String lit = ((CaseInsensitiveStringConstant.Lexical) constant).getString();
		StringBuilder builder = new StringBuilder(lit.length());
		
		for (int i = 1; i < lit.length() - 1; i++) {
			if (lit.charAt(i) == '\\') {
				i++;
				switch (lit.charAt(i)) {
				case 'n':
					builder.append('\n');
					break;
				case 't':
					builder.append('\t');
					break;
				case 'r':
					builder.append('\r');
					break;
				case '\\':
					builder.append('\\');
					break;
				case '\"':
					builder.append('\'');
					break;
				default:
					int a = lit.charAt(i++);
					int b = lit.charAt(i++);
					int c = lit.charAt(i);
					builder.append( (char) (100 * a + 10 * b + c));	
				}
			}
			else {
				builder.append(lit.charAt(i));
			}
		}
		
		return factory.constructor(RascalValueFactory.Symbol_Lit, factory.string(builder.toString()));
	}

	private static IConstructor charclass2Symbol(Class cc) {
		if (cc.isSimpleCharclass()) {
			return SymbolAdapter.normalizeCharClassRanges(SymbolAdapter.charclass(ranges2Ranges(cc.getRanges())));
		}
		else if (cc.isComplement()) {
		    return SymbolAdapter.complementCharClass(charclass2Symbol(cc.getCharClass()));
		}
		else if (cc.isUnion()) {
		    return SymbolAdapter.unionCharClasses(charclass2Symbol(cc.getLhs()), charclass2Symbol(cc.getRhs()));
		}
		else if (cc.isIntersection()) {
		    return SymbolAdapter.intersectCharClasses(charclass2Symbol(cc.getLhs()), charclass2Symbol(cc.getRhs()));
		}
		else if (cc.isDifference()) {
		    return SymbolAdapter.differencesCharClasses(charclass2Symbol(cc.getLhs()), charclass2Symbol(cc.getRhs()));
		}
		else if (cc.isBracket()) {
		    return charclass2Symbol(cc.getCharClass());
		}
		
		throw new NotYetImplemented(cc);
	}
	
	private static IList ranges2Ranges(List<Range> ranges) {
		IListWriter result = factory.listWriter();
		
		for (Range range : ranges) {
			if (range.isCharacter()) {
				IValue ch = char2int(range.getCharacter());
				result.append(factory.constructor(RascalValueFactory.CharRange_Range, ch, ch));
			}
			else if (range.isFromTo()) {
				IValue from = char2int(range.getStart());
				IValue to = char2int(range.getEnd());
				result.append(factory.constructor(RascalValueFactory.CharRange_Range, from, to));
			}
		}
		
		return result.done();
	}

	private static final Pattern IS_UNICODE_ESCAPE = Pattern.compile("\\\\[auU][0-9A-Fa-f]+");

	private static IValue char2int(Char character) {
		String s = ((Char.Lexical) character).getString();
		if (s.startsWith("\\")) {
			// builtin escape
			int cha = s.codePointAt(1);
			switch (cha) {
				case 'a': // fallthrough
				case 'u': // fallthrough
				case 'U':
					if (IS_UNICODE_ESCAPE.matcher(s).matches()) {
						// ascii escape (a), utf16 escape (u) or utf32 escape (U)
						return factory.integer(Integer.parseInt(s.substring(2), 16));
					}
					else {
						return factory.integer(cha);
					}
				case 't': return factory.integer('\t');
				case 'n': return factory.integer('\n');
				case 'r': return factory.integer('\r');
				case 'f': return factory.integer('\f');
				case 'b': return factory.integer('\b');
				default: return factory.integer(cha); //fallback is just the character thats escaped
			}
		}
		else {
			// just a single character (but possibly two char's)
			return factory.integer(s.codePointAt(0));
		}
	}
	
	public static IConstructor charClass(int ch) {
		return factory.constructor(RascalValueFactory.Symbol_CharClass, factory.list(factory.constructor(RascalValueFactory.CharRange_Range, factory.integer(ch), factory.integer(ch))));
	}

    public static IConstructor unionCharClasses(IConstructor lhs, IConstructor rhs) {
        if (lhs == rhs || lhs.equals(rhs)) {
            return lhs;
        }
        return charclass(unionRanges(SymbolAdapter.getRanges(lhs), SymbolAdapter.getRanges(rhs)));
    }
    
    public static IConstructor charclass(IList ranges) {
        return factory.constructor(RascalValueFactory.Symbol_CharClass, ranges);
    }
    
    public static IConstructor complementCharClass(IConstructor cc) {
        IConstructor everything = charclass(factory.list(range(1, 0x10FFFF)));
        
        return differencesCharClasses(everything, cc);
    }
    
    public static IConstructor differencesCharClasses(IConstructor lhs, IConstructor rhs) {
        if (lhs == rhs || lhs.equals(rhs)) {
            return factory.constructor(RascalValueFactory.Symbol_CharClass, factory.list());
        }
        
        return charclass(differenceRanges(SymbolAdapter.getRanges(lhs), SymbolAdapter.getRanges(rhs)));
    }
    
    private static IList newRange(int from, int to) {
        return from > to ? factory.list() : factory.list(range(from, to));
    }
    
    private static IList differenceRanges(IList l, IList r) {
        if (l.isEmpty() || r.isEmpty()) {
            return l;
        }
        
        IConstructor lhead = (IConstructor) l.get(0);
        IList ltail = l.delete(0);
        IConstructor rhead = (IConstructor) r.get(0);
        IList rtail = r.delete(0);

        // left beyond right
        // <-right-> --------
        // --------- <-left->
        if (rangeBegin(lhead) > rangeEnd(rhead)) {
          return differenceRanges(l,rtail); 
        }

        // left before right
        // <-left-> ----------
        // -------- <-right->
        if (rangeEnd(lhead) < rangeBegin(rhead)) {
          return differenceRanges(ltail,r).insert(lhead);
        }

        // inclusion of left into right
        // <--------right------->
        // ---------<-left->-----
        if (rangeBegin(lhead) >= rangeBegin(rhead) && rangeEnd(lhead) <= rangeEnd(rhead)) {
          return differenceRanges(ltail,r); 
        }

        // inclusion of right into left
        // -------<-right->------->
        // <---------left--------->
        if (rangeBegin(rhead) >= rangeBegin(lhead) && rangeEnd(rhead) <= rangeEnd(lhead)) {
          return newRange(rangeBegin(lhead),rangeBegin(rhead)-1).concat( 
              differenceRanges(newRange(rangeEnd(rhead)+1,rangeEnd(lhead)).concat(ltail), rtail));
        }

        // overlap on left side of right
        // <--left-------->----------
        // ---------<-----right----->
        if (rangeEnd(lhead) < rangeEnd(rhead)) 
          return newRange(rangeBegin(lhead),rangeBegin(rhead)-1).concat(differenceRanges(ltail,r)); 
          
        // overlap on right side of right
        // -------------<---left---->
        // <----right------->--------
        if (rangeBegin(lhead) > rangeBegin(rhead)) {
          return differenceRanges(newRange(rangeEnd(rhead)+1,rangeEnd(lhead)).concat(ltail), rtail);
        }

        throw new IllegalArgumentException("did not expect to end up here! <l> - <r>");
    }

    public static IConstructor intersectCharClasses(IConstructor lhs, IConstructor rhs) {
        if (lhs == rhs || lhs.equals(rhs)) {
            return lhs;
        }
        return charclass(intersectRanges(SymbolAdapter.getRanges(lhs), SymbolAdapter.getRanges(rhs)));
    }
    
    private static IList intersectRanges(IList l, IList r) {
        if (l.isEmpty()) {
            return l;
        }
        
        if (r.isEmpty()) {
            return r;
        }
        
        IConstructor lhead = (IConstructor) l.get(0);
        IList ltail = l.delete(0);
        IConstructor rhead = (IConstructor) r.get(0);
        IList rtail = r.delete(0);
        
        // left beyond right
        // <-right-> --------
        // --------- <-left->
        if (rangeBegin(lhead) > rangeEnd(rhead)) {
          return intersectRanges(l,rtail); 
        }

        // left before right
        // <-left-> ----------
        // -------- <-right->
        if (rangeEnd(lhead) < rangeBegin(rhead)) { 
          return intersectRanges(ltail,r);
        }

        // inclusion of left into right
        // <--------right------->
        // ---------<-left->-----
        if (rangeBegin(lhead) >= rangeBegin(rhead) && rangeEnd(lhead) <= rangeEnd(rhead)) { 
          return intersectRanges(ltail,r).insert(lhead); 
        }

        // inclusion of right into left
        // -------<-right->------->
        // <---------left--------->
        if (rangeBegin(rhead) >= rangeBegin(lhead) && rangeEnd(rhead) <= rangeEnd(lhead)) { 
          return intersectRanges(l,rtail).insert(rhead); 
        }

        // overlap on left side of right
        // <--left-------->----------
        // ---------<-----right----->
        if (rangeEnd(lhead) < rangeEnd(rhead)) { 
            return intersectRanges(ltail,r).insert(range(rangeBegin(rhead), rangeEnd(lhead)));
        }
          
        // overlap on right side of right
        // -------------<---left---->
        // <----right------->--------
        if (rangeBegin(lhead) > rangeBegin(rhead)) {
          return intersectRanges(l,rtail).insert(range(rangeBegin(lhead), rangeEnd(rhead)));
        }
          
        throw new IllegalArgumentException("did not expect to end up here! <l> - <r>");
    }
    
    public static IConstructor normalizeCharClassRanges(IConstructor cc) {
        IList ranges = SymbolAdapter.getRanges(cc);
        IList result = factory.list();
        
        for (IValue range : ranges) {
            result = unionRanges(result, factory.list(range));
        }
        
        return charclass(result);
    }
    
    private static IList unionRanges(IList l, IList r) {
        if (l.isEmpty()) {
            return r;
        }

        if (r.isEmpty()) {
            return l;
        }

        IConstructor lhead = (IConstructor) l.get(0);
        IList ltail = l.delete(0);
        IConstructor rhead = (IConstructor) r.get(0);
        IList rtail = r.delete(0);

        // left beyond right
        // <-right-> --------
        // --------- <-left->
        if (rangeBegin(lhead) > rangeEnd(rhead) + 1) {
            return unionRanges(l, rtail).insert(rhead); 
        }

        // left before right
        // <-left-> ----------
        // -------- <-right->
        if (rangeEnd(lhead) + 1 < rangeBegin(rhead)) {
            return unionRanges(ltail,r).insert(lhead);
        }

        // inclusion of left into right
        // <--------right------->
        // ---------<-left->-----
        if (rangeBegin(lhead) >= rangeBegin(rhead) && rangeEnd(lhead) <= rangeEnd(rhead)) { 
            return unionRanges(ltail,r); 
        }

        // inclusion of right into left
        // -------<-right->------->
        // <---------left--------->
        if (rangeBegin(rhead) >= rangeBegin(lhead) && rangeEnd(rhead) <= rangeEnd(lhead)) { 
            return unionRanges(l,rtail); 
        }

        // overlap on left side of right
        // <--left-------->----------
        // ---------<-----right----->
        if (rangeEnd(lhead) < rangeEnd(rhead)) { 
            return unionRanges(ltail.insert(range(rangeBegin(lhead), rangeEnd(rhead))), rtail);
        }

        // overlap on right side of right
        // -------------<---left---->
        // <----right------->--------
        if (rangeBegin(lhead) > rangeBegin(rhead)) {
            return unionRanges(ltail, rtail.insert(range(rangeBegin(rhead), rangeEnd(lhead))));
        }

        throw new IllegalArgumentException("did not expect to end up here! union(<l>,<r>)");
    }
    
    private static IConstructor range(int begin, int end) {
        return factory.constructor(RascalValueFactory.CharRange_Range, factory.integer(begin), factory.integer(end));
    }
    
    private static int rangeBegin(IConstructor range) {
        return ((IInteger) range.get("begin")).intValue();
    }
    
    private static int rangeEnd(IConstructor range) {
        return ((IInteger) range.get("end")).intValue();
    }
}
