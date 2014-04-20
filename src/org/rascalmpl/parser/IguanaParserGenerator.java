/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.jgll.grammar.GrammarBuilder;
import org.jgll.grammar.condition.Condition;
import org.jgll.grammar.condition.ConditionType;
import org.jgll.grammar.condition.ContextFreeCondition;
import org.jgll.grammar.condition.PositionalCondition;
import org.jgll.grammar.condition.RegularExpressionCondition;
import org.jgll.grammar.slot.factory.GrammarSlotFactoryImpl;
import org.jgll.grammar.slot.factory.GrammarSlotFactory;
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
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class IguanaParserGenerator {
	
	public static final int CHARACTER_LEVEL = 0;
	public static final int TOKEN_BASED = 1;
	
	private Map<IValue, Rule> rulesMap;

	private Map<String, Keyword> keywordsMap;
	
	private Map<String, RegularExpression> regularExpressionsMap;

	private int mode = CHARACTER_LEVEL;
	
	private final Evaluator evaluator;
	private final IValueFactory vf;

	public IguanaParserGenerator(IRascalMonitor monitor, PrintWriter out, List<ClassLoader> loaders, IValueFactory factory, Configuration config) {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment scope = new ModuleEnvironment("___parsergenerator___", heap);
		this.evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), out, out, scope,heap);
		this.evaluator.getConfiguration().setRascalJavaClassPathProperty(config.getRascalJavaClassPathProperty());
		evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());		
		this.evaluator.setBootstrapperProperty(true);
		this.vf = factory;
		
		monitor.startJob("Loading parser generator", 100, 139);
		try {
			evaluator.doImport(monitor, "lang::rascal::grammar::ConcreteSyntax");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Modules");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Priorities");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Regular");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Keywords");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Literals");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Parameters");
			evaluator.doImport(monitor, "lang::rascal::grammar::definition::Symbols");
			evaluator.doImport(monitor, "lang::rascal::grammar::IguanaParserGenerator");
			evaluator.doImport(monitor, "Ambiguity");
		}
		finally {
			monitor.endJob(true);
		}
	}
	
	public String getNonterminalName(IConstructor symbolTree) {
		return SymbolAdapter.toString((IConstructor) evaluator.call(new NullRascalMonitor(), "sym2symbol", symbolTree));
	}
	
	private IConstructor getPreprocessedGrammar(IRascalMonitor monitor, String main, IMap definition) {
		IConstructor gr = (IConstructor) evaluator.call(monitor, "modules2grammar", vf.string(main), definition);
		gr = (IConstructor) evaluator.call(monitor, "preprocess", gr);
		return gr;
	}
	
	public Grammar generateGrammar(IRascalMonitor monitor, String main, IMap definitions) {
		IConstructor gr = getPreprocessedGrammar(monitor, main, definitions);
	
		GrammarBuilder builder = convert("inmemory", gr);
		IMap notAllowed = (IMap) ((IMap) gr.get("about")).get(vf.string("notAllowed"));
		IMap except = (IMap) ((IMap) gr.get("about")).get(vf.string("excepts"));

		assert notAllowed != null;
		assert except != null;

		addExceptPatterns(builder, except);
		addPrecedencePatterns(builder, notAllowed);

		return builder.build();
	}
	
	public GrammarBuilder convert(String name, IConstructor rascalGrammar) {

		GrammarSlotFactory factory = new GrammarSlotFactoryImpl();
		GrammarBuilder builder = new GrammarBuilder(name, factory);
		
		IMap definitions = (IMap) rascalGrammar.get("rules");
		rulesMap = new HashMap<>();
		keywordsMap = new HashMap<>();
		regularExpressionsMap = new HashMap<>();
		
		if(mode == TOKEN_BASED) {
			IMap regularExpressions = (IMap) ((IMap) rascalGrammar.get("about")).get(vf.string("regularExpressions"));
			createRegularExpressions(regularExpressions);
		}

		for (IValue nonterminal : definitions) {

			IConstructor constructor = (IConstructor) nonterminal;
			
			boolean ebnf = isEBNF(constructor);

			Nonterminal head = getHead(constructor);
			
			if(mode == TOKEN_BASED) {
				// Don't create a rule body for regular expression heads.
				if(regularExpressionsMap.containsKey(head.getName())) {
					continue;
				}
	
				if(isKeyword(constructor)) {
					continue;
				}
				
				if(isRegularExpression(constructor)) {
					continue;
				}
			}

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

				if (!prod.getName().equals("regular")) {

					IList rhs = (IList) prod.get("symbols");

					List<Symbol> body = getSymbolList(rhs, definitions);
					
					Rule rule = new Rule(head, body, new SerializableValue(object));
					rulesMap.put(prod, rule);
					builder.addRule(rule);
				}
			}
		}
		
		return builder;
	}
	
	private Condition getNotFollow(IConstructor symbol, IMap definitions) {

		switch (symbol.getName()) {

			case "char-class":
				List<Range> targetRanges = buildRanges(symbol);
				return RegularExpressionCondition.notFollow(new CharacterClass(targetRanges));
	
			case "lit":
				return RegularExpressionCondition.notFollow(getKeyword(symbol, definitions));
	
			case "seq":
				IList list = (IList) symbol.get("symbols");
				List<Symbol> symbols = new ArrayList<>();
				for (IValue v : list) {
					symbols.add(getSymbol((IConstructor) v, definitions));
				}
				symbols.remove(1);
				if(isAllRegularExpression(symbols)) {
					return RegularExpressionCondition.notFollow(new Sequence<RegularExpression>(convert(symbols)));				
				} else {
					return ContextFreeCondition.notFollow(symbols);
				}
	
			default:
				throw new IllegalStateException("Should not be here!");
		}
	}
	
	private Condition getFollow(IConstructor symbol, IMap definitions) {

		switch (symbol.getName()) {

			case "char-class":
				List<Range> targetRanges = buildRanges(symbol);
				return RegularExpressionCondition.follow(new CharacterClass(targetRanges));
	
			case "lit":
				return RegularExpressionCondition.follow(getKeyword(symbol, definitions));
	
			case "seq":
				IList list = (IList) symbol.get("symbols");
				List<Symbol> symbols = new ArrayList<>();
				for (IValue v : list) {
					symbols.add(getSymbol((IConstructor) v, definitions));
				}
				symbols.remove(1);
				if(isAllRegularExpression(symbols)) {
					return RegularExpressionCondition.follow(new Sequence<RegularExpression>(convert(symbols)));				
				} else {
					return ContextFreeCondition.follow(symbols);
				}
	
			default:
				throw new IllegalStateException("Should not be here!");
		}
	}

	private Condition getNotPrecede(IConstructor symbol, IMap definitions) {
		switch (symbol.getName()) {
			
			case "char-class":
				List<Range> targetRanges = buildRanges(symbol);
				return RegularExpressionCondition.notPrecede(new CharacterClass(targetRanges));
	
			case "lit":
				return RegularExpressionCondition.notPrecede(getKeyword(symbol, definitions));
	
			default:
				throw new IllegalStateException("Should not be here!");
		}
	}
	
	private Condition getPrecede(IConstructor symbol, IMap definitions) {
		switch (symbol.getName()) {
		
			case "char-class":
				List<Range> targetRanges = buildRanges(symbol);
				return RegularExpressionCondition.precede(new CharacterClass(targetRanges));
	
			case "lit":
				return RegularExpressionCondition.precede(getKeyword(symbol, definitions));
	
			default:
				throw new IllegalStateException("Should not be here!");
		}
	}

	private void addPrecedencePatterns(GrammarBuilder builder, IMap notAllowed) {

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
				builder.addPrecedencePattern(rule.getHead(), rule, position, rulesMap.get(iterator.next()));
			}
		}
	}

	private void createRegularExpressions(IMap regularExpressions) {
		
		Iterator<Entry<IValue, IValue>> it = regularExpressions.entryIterator();

		while (it.hasNext()) {
			Entry<IValue, IValue> regularExpression = it.next();

			Nonterminal head = getHead((IConstructor) regularExpression.getKey());
			IValue prod = regularExpression.getValue();
			IList rhs = (IList) ((IConstructor) prod).get("symbols");

//			System.out.println(new RegularExpression(head.getName(), body));
			regularExpressionsMap.put(head.getName(), new Sequence<>(getRegularExpressionList(rhs)));
		}
	}
	
	private void addExceptPatterns(GrammarBuilder builder, IMap map) {

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
				builder.addExceptPattern(rule.getHead(), rule, position, rulesMap.get(iterator.next()));
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
			targetRanges.add(new Range(begin, end));
		}
		return targetRanges;
	}
	
	private List<Symbol> getSymbolList(IList rhs, IMap definitions) {
		
		List<Symbol> result = new ArrayList<>();
		
		for(int i = 0; i < rhs.length(); i++) {
			IConstructor current = (IConstructor) rhs.get(i);
			
			Symbol symbol = getSymbol(current, definitions);				
			
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
			
			if(!(regex instanceof CharacterClass)) {
				System.out.println("WTF?");
			}
			
			if (regex != null) {
				result.add(regex);
			}
		}
		
		return result;
	}

	private Keyword getKeyword(IConstructor symbol, IMap definitions) {

		String name = SymbolAdapter.toString(symbol, true);
		Keyword keyword = keywordsMap.get(name);

		if (keyword == null) {

			IConstructor choice = (IConstructor) definitions.get(symbol);
			
			ISet alts = null;
			try {
				alts = (ISet) choice.get("alternatives");
			} catch(Exception e) {
				System.out.println(symbol);
			}

			assert alts.size() == 1;

			int[] chars = null;
			for (IValue alt : alts) {
				IConstructor prod = (IConstructor) alt;

				IList rhs = null;
				try {
					rhs = (IList) prod.get("symbols");
				} catch(Exception e) {
					System.out.println(symbol);
				}

				chars = new int[rhs.length()];

				int i = 0;
				for (IValue s : rhs) {

					IList ranges = null;
					try {
						ranges = (IList) ((IConstructor) s).get("ranges");
					} catch(Exception e) {
						System.out.println(symbol);
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
			}

			assert chars != null;

			keyword = new Keyword(name, chars);
			keywordsMap.put(name,  keyword);
		}

		return keyword;
	}
	
	private boolean isKeyword(IConstructor symbol) {
		return symbol.getName().equals("lit");
	}
	
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
		switch (symbol.getName()) {

			case "lit":
				return new Nonterminal(SymbolAdapter.toString(symbol, true));
	
			case "iter":
				return new Nonterminal(SymbolAdapter.toString(symbol, true), true);
	
			case "iter-seps":
				return new Nonterminal(SymbolAdapter.toString(symbol, true), true);
	
			case "iter-star":
				return new Nonterminal(SymbolAdapter.toString(symbol, true), true);
	
			case "iter-star-seps":
				return new Nonterminal(SymbolAdapter.toString(symbol, true), true);
	
			default:
				return new Nonterminal(SymbolAdapter.toString(symbol, true));
		}
	}

	private Symbol getSymbol(IConstructor symbol, IMap definitions) {
		
		if(mode == TOKEN_BASED) {
			RegularExpression regexp = regularExpressionsMap.get(symbol.getName());
			if(regexp != null) {
				return regexp;
			}
			
			if(isRegularExpression(symbol)) {
				return getRegularExpression(symbol);
			}
			
			if(isKeyword(symbol)) {
				return getKeyword(symbol, definitions);
			}
		}
		
		switch (symbol.getName()) {

			case "char-class":
				return getCharacterClass(symbol);
	
			case "lit":
				return new Nonterminal(SymbolAdapter.toString(symbol, true), true);
	
			case "label":
				return getSymbol(getSymbolCons(symbol), definitions);
	
			case "iter":
				return new Nonterminal(SymbolAdapter.toString(symbol, true), true);
	
			case "iter-seps":
				return new Nonterminal(SymbolAdapter.toString(symbol, true), true);
	
			case "iter-star":
				return new Nonterminal(SymbolAdapter.toString(symbol, true), true);
	
			case "iter-star-seps":
				return new Nonterminal(SymbolAdapter.toString(symbol, true), true);
	
			case "opt":
				return new Nonterminal(SymbolAdapter.toString(symbol, true));
	
			case "alt":
				return new Nonterminal(SymbolAdapter.toString(symbol, true));
	
			case "seq":
				return new Nonterminal(SymbolAdapter.toString(symbol, true));
	
			case "start":
				return new Nonterminal("start[" + SymbolAdapter.toString(getSymbolCons(symbol), true) + "]");
	
			case "conditional":
//				return getSymbol(getSymbolCons(symbol), definitions).withConditions(getConditions(symbol, definitions));
	
			default:
				return new Nonterminal(SymbolAdapter.toString(symbol, true));
		}
	}
	
	private RegularExpression getRegularExpression(IConstructor symbol) {
		
		switch (symbol.getName()) {
		
		case "keywords":
		case "sort":
		case "layouts":
		case "lex":
			 return regularExpressionsMap.get(((IString)symbol.get("name")).getValue());
			 
		case "conditional":
//			return getRegularExpression(getSymbolCons(symbol)).addConditions(getConditions(symbol));
			return getRegularExpression(getSymbolCons(symbol));
		
		case "label":
			return getRegularExpression(getSymbolCons(symbol));
			
		case "char-class":
			return getCharacterClass(symbol);	

		case "iter":
			return new RegexPlus(getRegularExpression(getSymbolCons(symbol)));

		case "iter-seps":
			return new RegexPlus(getRegularExpression(getSymbolCons(symbol)));

		case "iter-star":
			return new RegexStar(getRegularExpression(getSymbolCons(symbol)));

		case "iter-star-seps":
			return new RegexStar(getRegularExpression(getSymbolCons(symbol)));

		case "opt":
			return new RegexOpt(getRegularExpression(getSymbolCons(symbol)));

		case "alt":
			return new RegexAlt<>(getRegularExpressionList((ISet) symbol.get("alternatives")));

		case "seq":
			return new Sequence<>(getRegularExpressionList((IList) symbol.get("symbols")));
			
		default:
			throw new IllegalStateException("Should not reach here. " + symbol);
		}
	}

	private CharacterClass getCharacterClass(IConstructor symbol) {
		List<Range> targetRanges = buildRanges(symbol);
		return new CharacterClass(targetRanges);
	}

	private List<Condition> getConditions(IConstructor symbol, IMap definitions) {
		ISet conditions = (ISet) symbol.get("conditions");
		List<Keyword> keywords = new ArrayList<>();
		List<Condition> list = new ArrayList<>();

		for (IValue condition : conditions) {
			switch (((IConstructor) condition).getName()) {
			
				case "not-follow":
					IConstructor notFollow = getSymbolCons((IConstructor) condition);
					list.add(getNotFollow(notFollow, definitions));
					break;
	
				case "follow":
					IConstructor follow = getSymbolCons((IConstructor) condition);
					list.add(getFollow(follow, definitions));
					break;
	
				case "delete":
					// delete sets are expanded, so here we encounter them one by one
					keywords.add(getKeyword(getSymbolCons((IConstructor) condition), definitions));			
					break;
	
				case "not-precede":
					IConstructor notPrecede = getSymbolCons((IConstructor) condition);
					list.add(getNotPrecede(notPrecede, definitions));
					break;
	
				case "end-of-line":
					list.add(new PositionalCondition(ConditionType.END_OF_LINE));
					break;
	
				case "start-of-line":
					list.add(new PositionalCondition(ConditionType.START_OF_LINE));
					break;
	
				case "precede":
					IConstructor precede = getSymbolCons((IConstructor) condition);
					list.add(getPrecede(precede, definitions));
					break;
	
				case "except":
					break;
	
				default:
					throw new RuntimeException("Unsupported conditional " + symbol);
				}
		}

		if(!keywords.isEmpty()) {
			list.add(RegularExpressionCondition.notMatch(new RegexAlt<>(keywords)));
		}
		return list;
	}

	private IConstructor getSymbolCons(IConstructor symbol) {
		return (IConstructor) symbol.get("symbol");
	}

	private boolean isEBNF(IConstructor value) {
		return isEBNFList(value) || SymbolAdapter.isAlt(value)
				|| SymbolAdapter.isSeq(value) || SymbolAdapter.isOpt(value) || SymbolAdapter.isEmpty(value);
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


	
	public IValue diagnoseAmbiguity(IConstructor parseForest) {
		return evaluator.call("diagnose", parseForest);
	}
	
	
	
	public IConstructor getGrammar(IRascalMonitor monitor, String main, IMap definition) {
		return (IConstructor) evaluator.call(monitor, "modules2grammar", vf.string(main), definition);
	}
	
	public IConstructor getExpandedGrammar(IRascalMonitor monitor, String main, IMap definition) {
		IConstructor g = getGrammar(monitor, main, definition);
		
		monitor.event("Expanding keywords", 10);
		g = (IConstructor) evaluator.call(monitor, "expandKeywords", g);
		monitor.event("Adding regular productions",10);
		g = (IConstructor) evaluator.call(monitor, "makeRegularStubs", g);
		monitor.event("Expanding regulars", 10);
		g = (IConstructor) evaluator.call(monitor, "expandRegularSymbols", g);
		monitor.event("Expanding parametrized symbols");
		g = (IConstructor) evaluator.call(monitor, "expandParameterizedSymbols", g);
		monitor.event("Defining literals");
		g = (IConstructor) evaluator.call(monitor, "literals", g);
		return g;
	}

	public ISet getNestingRestrictions(IRascalMonitor monitor,
			IConstructor g) {
		return (ISet) evaluator.call(monitor, "doNotNest", g);
	}
	
	/**
	 * Converts the parse tree of a symbol to a UPTR symbol
	 */
	public IConstructor symbolTreeToSymbol(IConstructor symbol) {
	  return (IConstructor) evaluator.call((IRascalMonitor) null,"sym2symbol", symbol);
	}
	

  public String createHole(IConstructor part, int size) {
    return ((IString) evaluator.call("createHole", part, vf.integer(size))).getValue();
  }
}
