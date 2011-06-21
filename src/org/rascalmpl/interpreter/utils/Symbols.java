/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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
package org.rascalmpl.interpreter.utils;

import java.util.List;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.ast.CaseInsensitiveStringConstant;
import org.rascalmpl.ast.Char;
import org.rascalmpl.ast.Class;
import org.rascalmpl.ast.Nonterminal;
import org.rascalmpl.ast.Range;
import org.rascalmpl.ast.StringConstant;
import org.rascalmpl.ast.Sym;
import org.rascalmpl.ast.Type;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public class Symbols {
	private static IValueFactory factory = ValueFactoryFactory.getValueFactory();
	
	public static IValue typeToSymbol(Type type) {
		if (type.isUser()) {
			return Factory.Symbol_Sort.make(factory, factory.string(Names.name(Names.lastName(type.getUser().getName()))));
		}
		
		if (type.isSymbol()) {
			return symbolAST2SymbolConstructor(type.getSymbol());
		}

		throw new RuntimeException("Can't convert type to symbol: "+type);
	}
	
	// TODO: there is a lot more to do to get this right.
	public static IValue typeToLexSymbol(Type type) {
		if (type.isUser()) {
			return Factory.Symbol_Lex.make(factory, factory.string(Names.name(Names.lastName(type.getUser().getName()))));
		}
		
		// For nested lexicals some other solution must be designed
		if (type.isSymbol()) {
			return symbolAST2SymbolConstructor(type.getSymbol());
		}

		throw new RuntimeException("Can't convert type to symbol: "+type);
	}


	// TODO: distribute this code over the dynamic.Sym classes in typeOf method
	private static IValue symbolAST2SymbolConstructor(Sym symbol) {
		// TODO: this has to become a parameter
		String layout = "LAYOUTLIST";
		
		if (symbol.isCaseInsensitiveLiteral()) {
			return Factory.Symbol_CiLit.make(factory, ciliteral2Symbol(symbol.getCistring()));
		}
		if (symbol.isCharacterClass()) {
			Class cc = symbol.getCharClass();
			return charclass2Symbol(cc);
		}
		if (symbol.isIter()) {
			return Factory.Symbol_IterPlus.make(factory, symbolAST2SymbolConstructor(symbol.getSymbol()));
		}
		if (symbol.isIterStar()) {
			return Factory.Symbol_IterStar.make(factory, symbolAST2SymbolConstructor(symbol.getSymbol()));
		}
		if (symbol.isIterSep()) {
			IValue layoutSymbol = Factory.Symbol_LayoutX.make(factory, layout);
			IValue elementSym = symbolAST2SymbolConstructor(symbol.getSymbol());
			IValue sepSym = symbolAST2SymbolConstructor(symbol.getSep());
//			IValue sepSym = literal2Symbol(symbol.getSep());
			IValue seps = Factory.Symbols.make(factory, layoutSymbol, sepSym, layoutSymbol);
			return Factory.Symbol_IterSepX.make(factory, elementSym, seps);
		}
		if (symbol.isIterStarSep()) {
			IValue layoutSymbol = Factory.Symbol_LayoutX.make(factory, layout);
			IValue elementSym = symbolAST2SymbolConstructor(symbol.getSymbol());
			IValue sepSym = symbolAST2SymbolConstructor(symbol.getSep());
//			IValue sepSym = literal2Symbol(symbol.getSep());
			IValue seps = Factory.Symbols.make(factory, layoutSymbol, sepSym, layoutSymbol);
			return Factory.Symbol_IterStarSepX.make(factory, elementSym, seps);
		}
		if (symbol.isLiteral()) {
			return literal2Symbol(symbol.getString());
		}
		if (symbol.isOptional()) {
			return Factory.Symbol_Opt.make(factory, symbolAST2SymbolConstructor(symbol.getSymbol()));
		}
		if (symbol.isNonterminal()) {
			IString name = factory.string(((Nonterminal.Lexical) symbol.getNonterminal()).getString());
			return Factory.Symbol_Sort.make(factory, name);
		}
		
		throw new RuntimeException("Symbol has unknown type: "+symbol.getTree());
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
		
		return Factory.Symbol_Lit.make(factory, factory.string(builder.toString()));
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
		
		return Factory.Symbol_Lit.make(factory, factory.string(builder.toString()));
	}

	private static IValue charclass2Symbol(Class cc) {
		if (cc.isSimpleCharclass()) {
			return Factory.Symbol_CharClass.make(factory, ranges2Ranges(cc.getRanges()));
		}
		throw new NotYetImplemented(cc);
	}
	
	private static IList ranges2Ranges(List<Range> ranges) {
		IListWriter result = Factory.CharRanges.writer(factory);
		
		for (Range range : ranges) {
			if (range.isCharacter()) {
				IValue ch = char2int(range.getCharacter());
				result.append(Factory.CharRange_Single.make(factory, ch));
			}
			else if (range.isFromTo()) {
				IValue from = char2int(range.getStart());
				IValue to = char2int(range.getEnd());
				result.append(Factory.CharRange_Range.make(factory, from, to));
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
