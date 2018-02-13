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
package org.rascalmpl.core.utils;

import java.util.List;

import org.rascalmpl.ast.CaseInsensitiveStringConstant;
import org.rascalmpl.ast.Char;
import org.rascalmpl.ast.Class;
import org.rascalmpl.ast.Nonterminal;
import org.rascalmpl.ast.Range;
import org.rascalmpl.ast.StringConstant;
import org.rascalmpl.ast.Sym;
import org.rascalmpl.ast.Type;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import org.rascalmpl.core.values.ValueFactoryFactory;
import org.rascalmpl.core.values.uptr.RascalValueFactory;

public class Symbols {
	private static IValueFactory factory = ValueFactoryFactory.getValueFactory();
	
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
		
		if (symbol.isCaseInsensitiveLiteral()) {
			return factory.constructor(RascalValueFactory.Symbol_CiLit, ciliteral2Symbol(symbol.getCistring()));
		}
		if (symbol.isCharacterClass()) {
			Class cc = symbol.getCharClass();
			return charclass2Symbol(cc);
		}
		if (symbol.isIter()) {
			if (lex) {
				return factory.constructor(RascalValueFactory.Symbol_IterPlus, symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout));
			}
			else {
				IValue layoutSymbol = factory.constructor(RascalValueFactory.Symbol_LayoutX, factory.string(layout));
				IValue elementSym = symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout);
				IValue seps = factory.list(layoutSymbol);
				return factory.constructor(RascalValueFactory.Symbol_IterSepX, elementSym, seps);
			}
		}
		if (symbol.isIterStar()) {
			if (lex) {
				return factory.constructor(RascalValueFactory.Symbol_IterStar, symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout));
			}
			else {
				IValue layoutSymbol = factory.constructor(RascalValueFactory.Symbol_LayoutX, factory.string(layout));
				IValue elementSym = symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout);
				IValue seps = factory.list(layoutSymbol);
				return factory.constructor(RascalValueFactory.Symbol_IterStarSepX, elementSym, seps);
			}
		}
		if (symbol.isIterSep()) {
			IValue layoutSymbol = factory.constructor(RascalValueFactory.Symbol_LayoutX, factory.string(layout));
			IValue elementSym = symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout);
			IValue sepSym = symbolAST2SymbolConstructor(symbol.getSep(), lex, layout);
			IValue seps;
			
			if (lex) {
				seps = factory.list(sepSym);
			}
			else {
				seps = factory.list(layoutSymbol, sepSym, layoutSymbol);
			}
			
			return factory.constructor(RascalValueFactory.Symbol_IterSepX, elementSym, seps);
		}
		
		if (symbol.isIterStarSep()) {
			IValue layoutSymbol = factory.constructor(RascalValueFactory.Symbol_LayoutX, factory.string(layout));
			IValue elementSym = symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout);
			IValue sepSym = symbolAST2SymbolConstructor(symbol.getSep(), lex, layout);
			IValue seps;
			if (lex) {
				seps = factory.list(sepSym);
			}
			else {
				seps = factory.list(layoutSymbol, sepSym, layoutSymbol);
			}
			return factory.constructor(RascalValueFactory.Symbol_IterStarSepX, elementSym, seps);
		}
		
		if (symbol.isLiteral()) {
			return literal2Symbol(symbol.getString());
		}
		if (symbol.isOptional()) {
			return factory.constructor(RascalValueFactory.Symbol_Opt, symbolAST2SymbolConstructor(symbol.getSymbol(), lex, layout));
		}
		
		if (symbol.isStart()) {
			Nonterminal nonterminal = symbol.getNonterminal();
			return factory.constructor(RascalValueFactory.Symbol_Start_Sort, factory.constructor(RascalValueFactory.Symbol_Sort, factory.string(((Nonterminal.Lexical) nonterminal).getString())));	
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
			IValue layoutSymbol = factory.constructor(RascalValueFactory.Symbol_LayoutX, factory.string(layout));
			IValue[] symValues = new IValue[lex ? symbols.size() : symbols.size() * 2 - 1];
			for(int i = symbols.size() - 1; i >= 0; i -= lex ? 1 : 2) {
				symValues[lex ? i : i * 2] = symbolAST2SymbolConstructor(symbols.get(i), lex, layout);
				if (lex && i > 0) {
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
		String lit = ((StringConstant.Lexical) sep).getString();
		StringBuilder builder = new StringBuilder(lit.length());
		
		// TODO: did we deal with all escapes here? probably not!
		for (int i = 1; i < lit.length() - 1; i++) {
			if (lit.charAt(i) == '\\') {
				i++;
				switch (lit.charAt(i)) {
				case 'b':
					builder.append('\b');
					break;
				case 'f':
					builder.append('\f');
					break;
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
					builder.append('\"');
					break;
				case '>':
					builder.append('>');
					break;
				case '<':
					builder.append('<');
					break;
				case '\'':
					builder.append('\'');
					break;
				case 'u':
					while (lit.charAt(i++) == 'u');
					builder.append((char) Integer.decode("0x" + lit.substring(i, i+4)).intValue());
					i+=4;
					break;
				default:
					// octal escape
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

	private static IValue charclass2Symbol(Class cc) {
		if (cc.isSimpleCharclass()) {
			return factory.constructor(RascalValueFactory.Symbol_CharClass, ranges2Ranges(cc.getRanges()));
		}
		throw new NotYetImplemented(cc);
	}
	
	private static IList ranges2Ranges(List<Range> ranges) {
		IListWriter result = factory.listWriter(RascalValueFactory.CharRanges.getElementType());
		
		for (Range range : ranges) {
			if (range.isCharacter()) {
				IValue ch = char2int(range.getCharacter());
				result.append(factory.constructor(RascalValueFactory.CharRange_Single, ch));
			}
			else if (range.isFromTo()) {
				IValue from = char2int(range.getStart());
				IValue to = char2int(range.getEnd());
				result.append(factory.constructor(RascalValueFactory.CharRange_Range, from, to));
			}
		}
		
		return result.done();
	}

	private static IValue char2int(Char character) {
		String s = ((Char.Lexical) character).getString();
		if (s.startsWith("\\")) {
			if (s.length() > 1 && java.lang.Character.isDigit(s.charAt(1))) { // octal escape
				// TODO
				throw new NotYetImplemented("octal escape sequence in character class types");
			}
			if (s.length() > 1 && s.charAt(1) == 'u') { // octal escape
				// TODO
				throw new NotYetImplemented("unicode escape sequence in character class types");
			}
			char cha = s.charAt(1);
			switch (cha) {
			case 't': return factory.integer('\t');
			case 'n': return factory.integer('\n');
			case 'r': return factory.integer('\r');
			case '\"' : return factory.integer('\"');
			case '\'' : return factory.integer('\'');
			case '-' : return factory.integer('-');
			case '<' : return factory.integer('<');
			case '>' : return factory.integer('>');
			case '\\' : return factory.integer('\\');
			}
			s = s.substring(1);
		}
		char cha = s.charAt(0);
		return factory.integer(cha);
	}
}
