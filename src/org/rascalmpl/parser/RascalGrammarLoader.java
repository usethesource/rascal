/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Ali Afroozeh - Ali.Afroozeh@cwi.nl - CWI
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.parser;

import static org.rascalmpl.values.uptr.ProductionAdapter.getSymbols;
import static org.rascalmpl.values.uptr.ProductionAdapter.isRegular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.jgll.grammar.Grammar;
import org.jgll.grammar.Grammar.Builder;
import org.jgll.grammar.condition.Condition;
import org.jgll.grammar.condition.ConditionType;
import org.jgll.grammar.condition.ContextFreeCondition;
import org.jgll.grammar.condition.PositionalCondition;
import org.jgll.grammar.condition.RegularExpressionCondition;
import org.jgll.grammar.precedence.OperatorPrecedence;
import org.jgll.grammar.symbol.CharacterClass;
import org.jgll.grammar.symbol.Keyword;
import org.jgll.grammar.symbol.Nonterminal;
import org.jgll.grammar.symbol.Range;
import org.jgll.grammar.symbol.Rule;
import org.jgll.grammar.symbol.Symbol;
import org.jgll.regex.RegexAlt;
import org.jgll.regex.RegexOpt;
import org.jgll.regex.RegexPlus;
import org.jgll.regex.RegexStar;
import org.jgll.regex.RegularExpression;
import org.jgll.regex.Sequence;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class RascalGrammarLoader {

	public static final int CHARACTER_LEVEL = 0;
	public static final int TOKEN_BASED = 1;
	
	private Map<IValue, Rule> rulesMap;

	private Map<String, Keyword> keywordsMap;
	
	private Map<String, RegularExpression> regularExpressionsMap;
	
	private Map<IConstructor, RegularExpression> regularExpressionsCache;
	
	private Map<List<IConstructor>, RegularExpression> deleteSetCache;

	private IConstructor rascalGrammar;
	
	private final IValueFactory vf;
	
	public RascalGrammarLoader(IValueFactory vf) {
		this.vf = vf;
		deleteSetCache = new HashMap<>();
		regularExpressionsCache = new HashMap<>();
	}

	private Condition getNotFollow(IConstructor symbol) {

		switch (symbol.getName()) {

			case "char-class":
				List<Range> targetRanges = buildRanges(symbol);
				return RegularExpressionCondition.notFollow(CharacterClass.from(targetRanges));
	
			case "lit":
				return RegularExpressionCondition.notFollow(getKeyword(symbol));
	
			case "seq":
				IList list = SymbolAdapter.getSymbols(symbol);
				List<Symbol> symbols = new ArrayList<>();
				for (IValue v : list) {
					symbols.add(getSymbol((IConstructor) v));
				}
				symbols.remove(1);
				if(isAllRegularExpression(symbols)) {
					return RegularExpressionCondition.notFollow(Sequence.from(convert(symbols)));				
				} else {
					return ContextFreeCondition.notFollow(symbols);
				}
	
			default:
				throw new IllegalStateException("Should not be here!");
		}
	}
	
	private Condition getFollow(IConstructor symbol) {

		switch (symbol.getName()) {

			case "char-class":
				List<Range> targetRanges = buildRanges(symbol);
				return RegularExpressionCondition.follow(CharacterClass.from(targetRanges));
	
			case "lit":
				return RegularExpressionCondition.follow(getKeyword(symbol));
	
			case "seq":
				IList list = (IList) symbol.get("symbols");
				List<Symbol> symbols = new ArrayList<>();
				for (IValue v : list) {
					symbols.add(getSymbol((IConstructor) v));
				}
				symbols.remove(1);
				if(isAllRegularExpression(symbols)) {
					return RegularExpressionCondition.follow(Sequence.from(convert(symbols)));				
				} else {
					return ContextFreeCondition.follow(symbols);
				}
	
			default:
				throw new IllegalStateException("Should not be here!");
		}
	}

	private Condition getNotPrecede(IConstructor symbol) {
		switch (symbol.getName()) {
			
			case "char-class":
				List<Range> targetRanges = buildRanges(symbol);
				return RegularExpressionCondition.notPrecede(CharacterClass.from(targetRanges));
	
			case "lit":
				return RegularExpressionCondition.notPrecede(getKeyword(symbol));
	
			default:
				throw new IllegalStateException("Should not be here!");
		}
	}
	
	private Condition getPrecede(IConstructor symbol) {
		switch (symbol.getName()) {
		
			case "char-class":
				List<Range> targetRanges = buildRanges(symbol);
				return RegularExpressionCondition.precede(CharacterClass.from(targetRanges));
	
			case "lit":
				return RegularExpressionCondition.precede(getKeyword(symbol));
	
			default:
				throw new IllegalStateException("Should not be here!");
		}
	}

	public Grammar convert(String name, IConstructor rascalGrammar) {
		Builder builder = new Grammar.Builder(); 
		
		IMap definitions = (IMap) rascalGrammar.get("rules");

		rulesMap = new HashMap<>();
		keywordsMap = new HashMap<>();
		regularExpressionsMap = new HashMap<>();
		regularExpressionsCache = new HashMap<>();
		deleteSetCache = new HashMap<>();
		
		for (Entry<String, RegularExpression> e : regularExpressionsMap.entrySet()) {
			System.out.println(e.getKey() + ": " + e.getValue());
		}

		for (IValue nonterminal : definitions) {
			IConstructor constructor = (IConstructor) nonterminal;
			
			boolean ebnf = isEBNF(constructor);

			Nonterminal head = getHead(constructor);
			
			IConstructor choice = (IConstructor) definitions.get(nonterminal);
			assert choice.getName().equals("choice");
			ISet alts = (ISet) choice.get("alternatives");

			for (IValue alt : alts) {
				IConstructor prod = (IConstructor) alt;
				IConstructor object;

				if (ebnf) {
					object = getRegularDefinition(alts);
				} else {
					object = (IConstructor) alt;
				}

				if (!isRegular(prod)) {
					IList rhs = getSymbols(prod);
					List<Symbol> body = getSymbolList(rhs);
					Rule rule = new Rule(head, body, new SerializableValue(object));
					rulesMap.put(prod, rule);
					builder.addRule(rule);
				}
			}
		}
		
		
		Grammar g = builder.build();
		IMap notAllowed = (IMap) ((IMap) rascalGrammar.get("about")).get(vf.string("notAllowed"));
		IMap except = (IMap) ((IMap) rascalGrammar.get("about")).get(vf.string("excepts"));

		OperatorPrecedence op = new OperatorPrecedence();
 		addExceptPatterns(op, except);
 		addPrecedencePatterns(op, notAllowed);
		return op.transform(g);
	}

	private void addPrecedencePatterns(OperatorPrecedence op, IMap notAllowed) {

		Iterator<Entry<IValue, IValue>> it = notAllowed.entryIterator();

		while (it.hasNext()) {
			Entry<IValue, IValue> next = it.next();

			// Tuple(prod, position)
			ITuple key = (ITuple) next.getKey();
			ISet set = (ISet) next.getValue();

			Rule rule = (Rule) rulesMap.get(key.get(0));
			int position = ((IInteger) key.get(1)).intValue();

			Iterator<IValue> iterator = set.iterator();
			while (iterator.hasNext()) {
				// Create a new filter for each filtered nonterminal
				op.addPrecedencePattern(rule.getHead(), rule, position, rulesMap.get(iterator.next()));
			}
		}
	}

	@SuppressWarnings("unused") // for tokens
	private void createRegularExpressions(IMap regularExpressions) {
		Iterator<Entry<IValue, IValue>> it = regularExpressions.entryIterator();

		while (it.hasNext()) {
			Entry<IValue, IValue> regularExpression = it.next();

			Nonterminal head = getHead((IConstructor) regularExpression.getKey());
			IValue prod = regularExpression.getValue();
			IList rhs = (IList) ((IConstructor) prod).get("symbols");

			List<RegularExpression> body = getRegularExpressionList(rhs);
			if(body.size() == 1) {
				regularExpressionsMap.put(head.getName(), body.get(0));				
			} else {
				regularExpressionsMap.put(head.getName(), Sequence.from(body));
			}
		}
	}
	
	private void addExceptPatterns(OperatorPrecedence op, IMap map) {

		Iterator<Entry<IValue, IValue>> it = map.entryIterator();

		while (it.hasNext()) {
			Entry<IValue, IValue> next = it.next();

			// Tuple(prod, position)
			ITuple key = (ITuple) next.getKey();
			ISet set = (ISet) next.getValue();

			Rule rule = (Rule) rulesMap.get(key.get(0));
			int position = ((IInteger) key.get(1)).intValue();

			Iterator<IValue> iterator = set.iterator();
			while (iterator.hasNext()) {
				// Create a new filter for each filtered nonterminal
				op.addExceptPattern(rule.getHead(), rule, position, rulesMap.get(iterator.next()));
			}
		}
	}

	private static List<Range> buildRanges(IConstructor symbol) {
		List<Range> targetRanges = new LinkedList<Range>();
		IList ranges = (IList) symbol.get("ranges");
		for (IValue r : ranges) {
			IConstructor range = (IConstructor) r;
			int begin = ((IInteger) range.get("begin")).intValue();
			int end = ((IInteger) range.get("end")).intValue();
			targetRanges.add(Range.in(begin, end));
		}
		return targetRanges;
	}
	
	private List<Symbol> getSymbolList(IList rhs) {
		
		List<Symbol> result = new ArrayList<>();
		
		for(int i = 0; i < rhs.length(); i++) {
			IConstructor current = (IConstructor) rhs.get(i);
			
			Symbol symbol = getSymbol(current);				
			
			if (symbol != null) {
				result.add(symbol);
			}
		}
		
		return result;
	}
	
	private List<RegularExpression> getRegularExpressionList(Iterable<IValue> rhs) {
		
		List<RegularExpression> result = new ArrayList<>();
		
		for(IValue val : rhs) {
			IConstructor constructor = (IConstructor) val;
			RegularExpression regex = getRegularExpression(constructor);	
			
			if (regex != null) {
				result.add(regex);
			}
		}
		
		return result;
	}

	private Keyword getKeyword(IConstructor symbol) {

		String name = SymbolAdapter.toString(symbol, true);
		Keyword keyword = keywordsMap.get(name);

		if (keyword == null) {

			IMap definitions = (IMap) rascalGrammar.get("rules");
			IConstructor choice = (IConstructor) definitions.get(symbol);

			// Keywords are already expanded into a sequence
			if(choice == null) {
				if (symbol.getName().equals("seq")) {
					keyword = Keyword.from(getChars((IList) symbol.get("symbols")));
				} else if (symbol.getName().equals("char-class")) {
						keyword = Keyword.from(getChars(symbol));	
				}
			} else {
				ISet alts = null;
				try {
					alts = (ISet) choice.get("alternatives");
				} catch(Exception e) {
					throw e;
				}

				assert alts.size() == 1;

				for (IValue alt : alts) {
					
					IConstructor prod = (IConstructor) alt;

					IList rhs = null;
					try {
						rhs = (IList) prod.get("symbols");
					} catch(Exception e) {
						throw e;
					}
					
					keyword = Keyword.from(getChars(rhs));
				}				
			}
			
			keywordsMap.put(name, keyword);
		}

		return keyword;
	}
	
	private int[] getChars(IList rhs) {
		int[] chars = new int[rhs.length()];

		int i = 0;
		for (IValue s : rhs) {

			IList ranges = null;
			try {
				ranges = (IList) ((IConstructor) s).get("ranges");
			} catch(Exception e) {
				throw e;
			}

			assert ranges.length() == 1;
			
			for (IValue r : ranges) {
				IConstructor range = (IConstructor) r;
				int begin = ((IInteger) range.get("begin")).intValue();
				int end = ((IInteger) range.get("end")).intValue();
				assert begin == end;
				chars[i++] = begin;
			}
		}
		
		return chars;
	}
	
	/**
	 * Transforms a charclas with a single char into an int array 
	 */
	private int[] getChars(IConstructor charClass) {
		int[] chars = new int[1];

		IList ranges = null;
		try {
			ranges = (IList) charClass.get("ranges");
		} catch(Exception e) {
			throw e;
		}

		assert ranges.length() == 1;
		
		IConstructor range = (IConstructor) ranges.get(0);
		int begin = ((IInteger) range.get("begin")).intValue();
		int end = ((IInteger) range.get("end")).intValue();
		assert begin == end;
		chars[0] = begin;
		
		return chars;
	}
	
	
	@SuppressWarnings("unused") // for tokens
	// TODO: this code is better expressed in Rascal
	private boolean isKeyword(IConstructor symbol) {
		return symbol.getName().equals("lit");
	}
	
	@SuppressWarnings("unused") // for tokens
	// TODO: this code is better expressed in Rascal
	private boolean isRegularExpression(IConstructor symbol) {
		if(SymbolAdapter.isLex(symbol)) {
			return regularExpressionsMap.containsKey(((IString)symbol.get("name")).getValue());
		} 
		else if(SymbolAdapter.isIterStar(symbol)) {
			return isRegularExpression(getSymbolCons(symbol));
		}
		else if(SymbolAdapter.isIterStarSeps(symbol)) {
			return isRegularExpression(getSymbolCons(symbol));
		}
		else if(SymbolAdapter.isIterPlusSeps(symbol)) {
			return isRegularExpression(getSymbolCons(symbol));
		}
		else if(SymbolAdapter.isIterPlus(symbol)) {
			return isRegularExpression(getSymbolCons(symbol));
		} 
		else if(SymbolAdapter.isAlt(symbol)) {
			ISet alts = (ISet) symbol.get("alternatives");
			for(IValue i : alts) {
				if(!isRegularExpression((IConstructor) i)) {
					return false;
				}
			}
			return true;
		}
		else if(SymbolAdapter.isSequence(symbol)) {
			IList symbols = (IList) symbol.get("symbols");
			for(IValue i : symbols) {
				if(!isRegularExpression((IConstructor) i)) {
					return false;
				}
			}
			return true;
		} 
		
		else if(SymbolAdapter.isCharClass(symbol)) {
			return true;
		}

		
		return false;
	}
		

	private Nonterminal getHead(IConstructor symbol) {
		return Nonterminal.withName(SymbolAdapter.toString(symbol, true));
	}

	private Symbol getSymbol(IConstructor symbol) {
		switch (symbol.getName()) {
			case "char-class":
				return getCharacterClass(symbol);
	
			case "lit":
				return Nonterminal.withName(SymbolAdapter.toString(symbol, true));
	
			case "label":
				return getSymbol(getSymbolCons(symbol));
	
			case "conditional":
				 Symbol cond = getSymbol(getSymbolCons(symbol)); 
				 cond.getConditions().addAll(getConditions(symbol));
				 return cond;
				 
			default:
				return Nonterminal.withName(SymbolAdapter.toString(symbol, true));
		}
	}
	
	private RegularExpression getRegularExpression(IConstructor symbol) {
		
		RegularExpression regex = regularExpressionsCache.get(symbol);
		
		if(regex != null) {
			return regex;
		}
		
		switch (symbol.getName()) {
		
			case "keywords":
			case "sort":
			case "layouts":
			case "lex":
				regex = regularExpressionsMap.get(((IString)symbol.get("name")).getValue());
				break;
				 
			case "conditional":
				regex = getRegularExpression(getSymbolCons(symbol));
				regex.getConditions().addAll(getConditions(symbol));
				break;
			
			case "label":
				regex = getRegularExpression(getSymbolCons(symbol));
				break;
				
			case "char-class":
				regex = getCharacterClass(symbol);
				break;
	
			case "iter":
				regex = RegexPlus.from(getRegularExpression(getSymbolCons(symbol)));
				break;
	
			case "iter-seps":
				regex = RegexPlus.from(getRegularExpression(getSymbolCons(symbol)));
				break;
	
			case "iter-star":
				regex = RegexStar.from(getRegularExpression(getSymbolCons(symbol)));
				break;
	
			case "iter-star-seps":
				regex = RegexStar.from(getRegularExpression(getSymbolCons(symbol)));
				break;
	
			case "opt":
				regex = RegexOpt.from(getRegularExpression(getSymbolCons(symbol)));
				break;
	
			case "alt":
				regex = RegexAlt.from(getRegularExpressionList((ISet) symbol.get("alternatives")));
				break;
	
			case "seq":
				regex = Sequence.from(getRegularExpressionList((IList) symbol.get("symbols")));
				break;
				
			default:
				throw new IllegalStateException("Should not reach here. " + symbol);
			}
		
		if(regex == null) return null;

		// Initialize the automaton field
		regex.getAutomaton().determinize();
		regularExpressionsCache.put(symbol, regex);
		return regex;
	}

	private CharacterClass getCharacterClass(IConstructor symbol) {
		return CharacterClass.from(buildRanges(symbol));
	}

	private Set<Condition> getConditions(IConstructor symbol) {
		ISet conditions = (ISet) symbol.get("conditions");
		Set<Condition> set = new HashSet<>();
		
		List<IConstructor> deleteList = new ArrayList<>();

		for (IValue condition : conditions) {
			switch (((IConstructor) condition).getName()) {
			
				case "not-follow":
					IConstructor notFollow = getSymbolCons((IConstructor) condition);
					set.add(getNotFollow(notFollow));
					break;
	
				case "follow":
					IConstructor follow = getSymbolCons((IConstructor) condition);
					set.add(getFollow(follow));
					break;
	
				case "delete":
					// delete sets are expanded, so here we encounter them one by one
					deleteList.add(getSymbolCons((IConstructor) condition));
					break;
	
				case "not-precede":
					IConstructor notPrecede = getSymbolCons((IConstructor) condition);
					set.add(getNotPrecede(notPrecede));
					break;
	
				case "end-of-line":
					set.add(new PositionalCondition(ConditionType.END_OF_LINE));
					break;
	
				case "start-of-line":
					set.add(new PositionalCondition(ConditionType.START_OF_LINE));
					break;
	
				case "precede":
					IConstructor precede = getSymbolCons((IConstructor) condition);
					set.add(getPrecede(precede));
					break;
	
				case "except":
					break;
	
				default:
					throw new RuntimeException("Unsupported conditional " + symbol);
				}
		}

		if(!deleteList.isEmpty()) {
			
			RegularExpression regex = deleteSetCache.get(deleteList);
			
			if(regex == null) {
				List<Keyword> keywords = new ArrayList<>();
				for(IConstructor c : deleteList) {
					keywords.add(getKeyword(c));
				}
				regex = RegexAlt.from(keywords);
				
				deleteSetCache.put(deleteList, regex);
			}
			
			set.add(RegularExpressionCondition.notMatch(regex));
		}
		
		return set;
	}

	private IConstructor getSymbolCons(IConstructor symbol) {
		return (IConstructor) symbol.get("symbol");
	}

	private boolean isEBNF(IConstructor value) {
		return isEBNFList(value) 
				|| SymbolAdapter.isAlt(value)
				|| SymbolAdapter.isSeq(value) 
				|| SymbolAdapter.isOpt(value) 
				|| SymbolAdapter.isEmpty(value);
	}

	private boolean isEBNFList(IConstructor value) {
		return SymbolAdapter.isIterStarSeps(value)
				|| SymbolAdapter.isIterStar(value)
				|| SymbolAdapter.isIterPlus(value)
				|| SymbolAdapter.isIterPlusSeps(value);
	}
	

	private IConstructor getRegularDefinition(ISet alts) {
		IConstructor value = null;
		for (IValue alt : alts) {
			IConstructor prod = (IConstructor) alt;
			if (prod.getName().equals("regular")) {
				value = prod;
			}
		}
		return value;
	}
	
	private static boolean isAllRegularExpression(List<Symbol> list) {
		for(Symbol s : list) {
			if(!(s instanceof RegularExpression)) {
				return false;
			}
		}
		return true;
	}
	
	private static List<RegularExpression> convert(List<Symbol> symbols) {
		List<RegularExpression> regularExpressions = new ArrayList<>();
		for(Symbol s : symbols) {
			regularExpressions.add((RegularExpression) s);
		}
		return regularExpressions;
	}
	
}