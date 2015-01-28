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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.jgll.grammar.Grammar;
import org.jgll.grammar.GrammarGraph;
import org.jgll.grammar.condition.Condition;
import org.jgll.grammar.condition.ConditionType;
import org.jgll.grammar.condition.PositionalCondition;
import org.jgll.grammar.condition.RegularExpressionCondition;
import org.jgll.grammar.precedence.OperatorPrecedence;
import org.jgll.grammar.symbol.Character;
import org.jgll.grammar.symbol.CharacterRange;
import org.jgll.grammar.symbol.Epsilon;
import org.jgll.grammar.symbol.Nonterminal;
import org.jgll.grammar.symbol.Rule;
import org.jgll.grammar.symbol.Symbol;
import org.jgll.parser.GLLParser;
import org.jgll.parser.ParseError;
import org.jgll.parser.ParseResult;
import org.jgll.parser.ParserFactory;
import org.jgll.regex.Alt;
import org.jgll.regex.Opt;
import org.jgll.regex.Plus;
import org.jgll.regex.RegularExpression;
import org.jgll.regex.Sequence;
import org.jgll.regex.Star;
import org.jgll.sppf.SPPFNode;
import org.jgll.util.Configuration;
import org.jgll.util.GrammarUtil;
import org.jgll.util.Input;
import org.jgll.util.Visualization;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class RascalToIguanaGrammarConverter {
	
	private final IValueFactory vf;

	private Map<IValue, Rule> rulesMap;

	private Grammar grammar;

	private GLLParser parser;

	private String startSymbol;

	private Input input;
	
	private IMap definitions;

	public RascalToIguanaGrammarConverter(IValueFactory vf) {
		this.vf = vf;
	}

	public void generateGrammar(IConstructor rascalGrammar) {
		System.out.println("Iguana started.");
		grammar = convert("inmemory", rascalGrammar);
		IMap notAllowed = (IMap) ((IMap) rascalGrammar.asWithKeywordParameters().getParameter("notAllowed"));
		IMap except = (IMap) ((IMap) rascalGrammar.asWithKeywordParameters().getParameter("excepts"));

		assert notAllowed != null;
		assert except != null;

		OperatorPrecedence op = new OperatorPrecedence();
		addExceptPatterns(op, except);
		addPrecedencePatterns(op, notAllowed);
		grammar = op.transform(grammar);
	}

	public void printGrammar() {
		System.out.println(grammar);
	}

	public void save(IString path) throws FileNotFoundException, IOException {
		System.out.println(grammar.getConstructorCode());
		GrammarUtil.save(grammar, new File(path.getValue()).toURI());
	}

	public void generateGraph(IString path, ISourceLocation loc) {
		Configuration config = Configuration.DEFAULT;
		parser = ParserFactory.getParser(config, input, grammar);

		GrammarGraph grammarGraph = grammar.toGrammarGraph(input, config);
		ParseResult result = parser.parse(input, grammarGraph, Nonterminal.withName(startSymbol));
		if (result.isParseSuccess()) {
			SPPFNode sppf = result.asParseSuccess().getRoot();
			Visualization.generateSPPFGraph(path.getValue(), sppf, grammarGraph.getRegistry(), input);
		} else {
			ParseError e = result.asParseError();
			throw RuntimeExceptionFactory.parseError(vf.sourceLocation(loc, 
					   e.getInputIndex(), 
					   1,
					   input.getLineNumber(e.getInputIndex()),
					   input.getLineNumber(e.getInputIndex()),
					   input.getColumnNumber(e.getInputIndex()) - 1,
					   input.getColumnNumber(e.getInputIndex()) - 1), null, null);
		}
	}

	public Grammar convert(String name, IConstructor rascalGrammar) {

		Grammar.Builder builder = new Grammar.Builder();
		
		definitions = (IMap) rascalGrammar.get("rules");
		
		rulesMap = new HashMap<>();
		
		Nonterminal layout = getLayoutNonterminal(rascalGrammar);
		
		for (IValue nonterminal : definitions) {

			IConstructor constructor = (IConstructor) nonterminal;
			
			if (constructor.getName().equals("layouts") || 
				constructor.getName().equals("lex") ||
				constructor.getName().equals("token")) {
				
				builder.addRules(getAlternatives(nonterminal, null));
				
			} else {
				builder.addRules(getAlternatives(nonterminal, layout));				
			}
			
		}
		
		return builder.build();
	}
	
	public Nonterminal getLayoutNonterminal(IConstructor rascalGrammar) {
		definitions = (IMap) rascalGrammar.get("rules");
		
		List<String> layoutNonterminals = new ArrayList<>();
		
		for (IValue nonterminal : definitions) {
			IConstructor constructor = (IConstructor) nonterminal;
			if (constructor.getName().equals("layouts")) {
				layoutNonterminals.add(((IString)constructor.get("name")).getValue());
			}
		}
		
		Nonterminal layout  = Nonterminal.withName(layoutNonterminals.stream().filter(s -> !s.equals("$default$")).collect(Collectors.toList()).get(0));
		
		return layout;
	}
	
	private List<Rule> getAlternatives(IValue nonterminal, Nonterminal layout) {
		
		List<Rule> rules = new ArrayList<>();
		
		Nonterminal head = Nonterminal.withName(SymbolAdapter.toString((IConstructor) nonterminal, false));
		
		IConstructor choice = (IConstructor) definitions.get(nonterminal);
		assert choice.getName().equals("choice");
		ISet alts = (ISet) choice.get("alternatives");

		for (IValue alt : alts) {

			IConstructor prod = (IConstructor) alt;

			SerializableValue object = null;

			if (!prod.getName().equals("regular")) {

				IList rhs = (IList) prod.get("symbols");

				List<Symbol> body = getSymbolList(rhs);
				
				Rule rule = Rule.withHead(head).addSymbols(body).setObject(object).setLayout(layout).build();
				rulesMap.put(prod, rule);
				rules.add(rule);
			}
		}
		
		return rules;
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

	private static List<CharacterRange> buildRanges(IConstructor symbol) {
		List<CharacterRange> targetRanges = new LinkedList<>();
		IList ranges = (IList) symbol.get("ranges");
		for (IValue r : ranges) {
			IConstructor range = (IConstructor) r;
			int begin = ((IInteger) range.get("begin")).intValue();
			int end = ((IInteger) range.get("end")).intValue();
			targetRanges.add(CharacterRange.in(begin, end));
		}
		return targetRanges;
	}
	
	
	private List<Symbol> getSymbolList(ISet rhs) {
		List<Symbol> result = new ArrayList<>();

		Iterator<IValue> it = rhs.iterator();
		while (it.hasNext()) {
			IConstructor current = (IConstructor) it.next();
			Symbol symbol = getSymbol(current);
			
			if (symbol != null) {
				result.add(symbol);
			}
		}
		
		return result;
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

	private Symbol getSymbol(IConstructor symbol) {
		
		switch (symbol.getName()) {
		
			case "sort":
			case "lex":
				return Nonterminal.withName(getName(symbol));

			case "char-class":
				Alt<CharacterRange> charClass = getCharacterClass(symbol);
				return charClass.isSingleChar() ? charClass.asSingleChar() : charClass;
				
			case "lit":
				Sequence<Character> keyword = Sequence.from(getString(symbol));
				return keyword.isSingleChar() ? keyword.asSingleChar() : keyword;
	
			case "label":
				return getSymbol(getSymbolCons(symbol)).copyBuilder().setLabel(getLabel(symbol)).build();
	
			case "iter":
				return Plus.from(getSymbol(getSymbolCons(symbol)));
	
			case "iter-seps":
				return Plus.builder(getSymbol(getSymbolCons(symbol))).addSeparators(getSymbolList(getSeparators(symbol))).build();
	
			case "iter-star":
				return Star.from(getSymbol(getSymbolCons(symbol)));
	
			case "iter-star-seps":
				return Star.builder(getSymbol(getSymbolCons(symbol))).addSeparators(getSymbolList(getSeparators(symbol))).build();
	
			case "opt":
				return Opt.from(getSymbol(getSymbolCons(symbol)));
	
			case "alt":
				return Alt.from(getSymbolList(getAlternatives(symbol)));
	
			case "seq":
				return Sequence.from(getSymbolList(getSymbols(symbol)));
	
			case "start":
				return Nonterminal.withName("start[" + SymbolAdapter.toString(getSymbolCons(symbol), true) + "]");
	
			case "conditional":
				return getSymbol(getSymbolCons(symbol)).copyBuilder()
							.addPreConditions(getPreConditions(symbol))
							.addPostConditions(getPostConditions(symbol))
							.build();
				
			case "empty":
				return Epsilon.getInstance();
				
			case "token":
				return Nonterminal.withName(getName(symbol));

			case "layouts":
				return null;
				
			default:
				throw new UnsupportedOperationException(symbol.toString());
		}
	}

	private Alt<CharacterRange> getCharacterClass(IConstructor symbol) {
		return Alt.builder(buildRanges(symbol)).build();
	}
	
	private Set<Condition> getPostConditions(IConstructor symbol) {
		ISet conditions = (ISet) symbol.get("conditions");
		Set<Condition> set = new HashSet<>();
		
		List<IConstructor> deleteList = new ArrayList<>();

		for (IValue condition : conditions) {
			switch (((IConstructor) condition).getName()) {
			
				case "not-follow":
					IConstructor notFollow = getSymbolCons((IConstructor) condition);
					set.add(RegularExpressionCondition.notFollow((RegularExpression) getSymbol(notFollow)));
					break;
	
				case "follow":
					IConstructor follow = getSymbolCons((IConstructor) condition);
					set.add(RegularExpressionCondition.follow((RegularExpression) getSymbol(follow)));
					break;
	
				case "delete":
					// delete sets are expanded, so here we encounter them one by one
					deleteList.add(getSymbolCons((IConstructor) condition));
					break;
	
				case "end-of-line":
					set.add(new PositionalCondition(ConditionType.END_OF_LINE));
					break;
	
				case "except":
					break;
				}
		}

		if (!deleteList.isEmpty()) {
			
			List<RegularExpression> list = new ArrayList<>();
			for(IConstructor c : deleteList) {
				list.add((RegularExpression) getSymbol(c));
			}
			
			RegularExpression regex = Alt.from(list);
			
			set.add(RegularExpressionCondition.notMatch(regex));
		}
		
		return set;
	}

	private Set<Condition> getPreConditions(IConstructor symbol) {
		
		ISet conditions = (ISet) symbol.get("conditions");
		Set<Condition> set = new HashSet<>();
		
		for (IValue condition : conditions) {
			switch (((IConstructor) condition).getName()) {
	
				case "not-precede":
					IConstructor notPrecede = getSymbolCons((IConstructor) condition);
					set.add(RegularExpressionCondition.notPrecede((RegularExpression) getSymbol(notPrecede)));
					break;
	
				case "start-of-line":
					set.add(new PositionalCondition(ConditionType.START_OF_LINE));
					break;
	
				case "precede":
					IConstructor precede = getSymbolCons((IConstructor) condition);
					set.add(RegularExpressionCondition.precede((RegularExpression) getSymbol(precede)));
					break;
				}
		}
		
		return set;
	}
	
	private String getName(IConstructor symbol) {
		return ((IString) symbol.get("name")).getValue();
	}
	
	private String getString(IConstructor symbol) {
		return ((IString) symbol.get("string")).getValue();
	}
	
	private String getLabel(IConstructor symbol) {
		return ((IString) symbol.get("name")).getValue();
	}

	private IConstructor getSymbolCons(IConstructor symbol) {
		return (IConstructor) symbol.get("symbol");
	}
	
	private ISet getAlternatives(IConstructor symbol) {
		return (ISet) symbol.get("alternatives");
	}
	
	private IList getSymbols(IConstructor symbol) {
		return (IList) symbol.get("symbols");
	}
	
	public IList getSeparators(IConstructor symbol) {
		return (IList) symbol.get("separators");
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
	
}