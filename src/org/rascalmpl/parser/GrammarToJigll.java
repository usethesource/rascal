package org.rascalmpl.parser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.jgll.grammar.Grammar;
import org.jgll.grammar.GrammarGraph;
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
import org.jgll.parser.GLLParser;
import org.jgll.parser.ParseError;
import org.jgll.parser.ParserFactory;
import org.jgll.regex.RegexAlt;
import org.jgll.regex.RegexOpt;
import org.jgll.regex.RegexPlus;
import org.jgll.regex.RegexStar;
import org.jgll.regex.RegularExpression;
import org.jgll.regex.Sequence;
import org.jgll.regex.automaton.RunnableAutomaton;
import org.jgll.sppf.NonterminalSymbolNode;
import org.jgll.traversal.ModelBuilderVisitor;
import org.jgll.traversal.Result;
import org.jgll.util.Input;
import org.jgll.util.Visualization;
import org.jgll.util.logging.LoggerWrapper;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class GrammarToJigll {

	private static final LoggerWrapper log = LoggerWrapper.getLogger(GrammarToJigll.class);

	private final IValueFactory vf;

	private Map<IValue, Rule> rulesMap;

	private Map<String, Keyword> keywordsMap;
	
	private Map<String, RegularExpression> regularExpressionsMap;
	
	private Map<IConstructor, RegularExpression> regularExpressionsCache;
	
	private Map<List<IConstructor>, RegularExpression> deleteSetCache;

	private GrammarGraph grammarGraph;

	private GLLParser parser;

	private String startSymbol;

	private Input input;

	private IConstructor rascalGrammar;
	
	public GrammarToJigll(IValueFactory vf) {
		this.vf = vf;
		deleteSetCache = new HashMap<>();
		regularExpressionsCache = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	public IConstructor jparse(IConstructor symbol, IString str, ISourceLocation loc) {
		
		if (grammarGraph == null) {
			return null;
		}

		input = Input.fromString(str.getValue(), loc.getURI());
		parser = ParserFactory.newParser(grammarGraph, input);

		log.info("Iguana started.");

		NonterminalSymbolNode sppf = null;

		startSymbol = SymbolAdapter.toString(symbol, true);

		try {
			sppf = parser.parse(input, this.grammarGraph, startSymbol);
		} catch (ParseError e) {
			throw RuntimeExceptionFactory.parseError(vf.sourceLocation(loc, 
																	   e.getInputIndex(), 
																	   1,
																	   input.getLineNumber(e.getInputIndex()),
																	   input.getLineNumber(e.getInputIndex()),
																	   input.getColumnNumber(e.getInputIndex()) - 1,
																	   input.getColumnNumber(e.getInputIndex()) - 1), null, null);
		}

		long start = System.nanoTime();
		sppf.accept(new ModelBuilderVisitor<>(input, new ParsetreeBuilder(), grammarGraph));
		long end = System.nanoTime();
		log.info("Flattening: %d ms ", (end - start) / 1000_000);

		return ((Result<IConstructor>) sppf.getObject()).getObject();
	}

	public void generateGrammar(IConstructor rascalGrammar) {
		this.rascalGrammar = rascalGrammar;
		
		Grammar grammar = convert("inmemory", rascalGrammar);
		IMap notAllowed = (IMap) ((IMap) rascalGrammar.get("about")).get(vf.string("notAllowed"));
		IMap except = (IMap) ((IMap) rascalGrammar.get("about")).get(vf.string("excepts"));

		assert notAllowed != null;
		assert except != null;

		OperatorPrecedence op = new OperatorPrecedence();
		addExceptPatterns(op, except);
		addPrecedencePatterns(op, notAllowed);
//		grammar = op.rewrite(grammar);
		System.out.println(grammar);

		grammarGraph = grammar.toGrammarGraph();
	}

	public void printGrammar() {
		System.out.println(grammarGraph);
	}

	public void save(IString path) throws FileNotFoundException, IOException {
		File file = new File(path.getValue());
		if (!file.exists()) {
			file.createNewFile();
		}
		ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		out.writeObject(grammarGraph);
		out.close();
	}

	public void generateGraph(IString path, ISourceLocation loc) {
		parser = ParserFactory.newParser(grammarGraph, input);

		NonterminalSymbolNode sppf;
		try {
			sppf = parser.parse(input, this.grammarGraph, startSymbol);
		} catch (ParseError e) {
			throw RuntimeExceptionFactory.parseError(vf.sourceLocation(loc, 
					   e.getInputIndex(), 
					   1,
					   input.getLineNumber(e.getInputIndex()),
					   input.getLineNumber(e.getInputIndex()),
					   input.getColumnNumber(e.getInputIndex()) - 1,
					   input.getColumnNumber(e.getInputIndex()) - 1), null, null);
		}

		Visualization.generateSPPFGraph(path.getValue(), sppf, grammarGraph, input);
	}

	private Condition getNotFollow(IConstructor symbol) {

		switch (symbol.getName()) {

			case "char-class":
				List<Range> targetRanges = buildRanges(symbol);
				return RegularExpressionCondition.notFollow(new CharacterClass(targetRanges));
	
			case "lit":
				return RegularExpressionCondition.notFollow(getKeyword(symbol));
	
			case "seq":
				IList list = (IList) symbol.get("symbols");
				List<Symbol> symbols = new ArrayList<>();
				for (IValue v : list) {
					symbols.add(getSymbol((IConstructor) v));
				}
				symbols.remove(1);
				if(isAllRegularExpression(symbols)) {
					return RegularExpressionCondition.notFollow(new Sequence<RegularExpression>(conver(symbols)));				
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
				return RegularExpressionCondition.follow(new CharacterClass(targetRanges));
	
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
					return RegularExpressionCondition.follow(new Sequence<RegularExpression>(conver(symbols)));				
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
				return RegularExpressionCondition.notPrecede(new CharacterClass(targetRanges));
	
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
				return RegularExpressionCondition.precede(new CharacterClass(targetRanges));
	
			case "lit":
				return RegularExpressionCondition.precede(getKeyword(symbol));
	
			default:
				throw new IllegalStateException("Should not be here!");
		}
	}

	public Grammar convert(String name, IConstructor rascalGrammar) {

		Grammar grammar = new Grammar();
		
		IMap definitions = (IMap) rascalGrammar.get("rules");
		
		rulesMap = new HashMap<>();
		keywordsMap = new HashMap<>();
		regularExpressionsMap = new HashMap<>();
		regularExpressionsCache = new HashMap<>();
		deleteSetCache = new HashMap<>();
		
		createRegularExpressions(definitions);

				
		for (IValue nonterminal : definitions) {

			System.out.println(nonterminal);
			IConstructor constructor = (IConstructor) nonterminal;
			
			boolean ebnf = isEBNF(constructor);

			Nonterminal head = getHead(constructor);
			
//			if(mode == TOKEN_BASED) {
//				// Don't create a rule body for regular expression heads.
//				if(regularExpressionsMap.containsKey(head.getName())) {
//					continue;
//				}
//	
//				if(isKeyword(constructor)) {
//					continue;
//				}
//				
////				if(isRegularExpression(constructor)) {
////					continue;
////				}
//			}

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

					List<Symbol> body = getSymbolList(rhs);
					
					Rule rule = new Rule(head, body, new SerializableValue(object));
					rulesMap.put(prod, rule);
					grammar.addRule(rule);
				}
			}
		}
		
		return grammar;
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

	private void createRegularExpressions(IMap definitions) {
		
		Iterator<Entry<IValue, IValue>> it = definitions.entryIterator();

		while (it.hasNext()) {
			Entry<IValue, IValue> regularExpression = it.next();
			IConstructor nont = (IConstructor) regularExpression.getKey();
			
			Nonterminal head = getHead(nont);
			
			if (SymbolAdapter.isToken(nont) || SymbolAdapter.isKeyword(nont) || SymbolAdapter.isLiteral(nont) || SymbolAdapter.isCILiteral(nont) ) {
				IConstructor prod = (IConstructor) regularExpression.getValue();
				
				if (prod.getName().equals("choice")) {
					ISet alts = (ISet) prod.get("alternatives");
					assert alts.size() == 1;
					prod = (IConstructor) alts.iterator().next();
				}
				
				IList rhs = (IList) ((IConstructor) prod).get("symbols");

				List<RegularExpression> body = getRegularExpressionList(rhs);
				if(body.size() == 1) {
					regularExpressionsMap.put(head.getName(), body.get(0));				
				} else {
					regularExpressionsMap.put(head.getName(), new Sequence<>(body));
				}
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
			targetRanges.add(new Range(begin, end));
		}
		return targetRanges;
	}
	
	CharacterClass c = new CharacterClass(Range.in('a', 'z'), Range.in('A', 'Z'), Range.in('_', '_'));
	RunnableAutomaton test = new RegexPlus(c).getAutomaton().getRunnableAutomaton();
	
	private List<Symbol> getSymbolList(IList rhs) {
		
		List<Symbol> result = new ArrayList<>();
		
		for(int i = 0; i < rhs.length(); i++) {
			IConstructor current = (IConstructor) rhs.get(i);
			
			Symbol symbol = getSymbol(current);				
			
			// TODO: get a list of keywords later from the grammar instead of this hack.
			if(symbol instanceof Keyword) {
				// Keywords cannot be followed by [a-z A-Z _] by default.
				if(test.match(Input.fromString(symbol.getName().substring(1, symbol.getName().length() - 1)))) {
					symbol = symbol.withCondition(RegularExpressionCondition.notFollow(c));
				}
			}
			
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
					keyword = new Keyword(name, getChars((IList) symbol.get("symbols")));
				} else if (symbol.getName().equals("char-class")) {
						keyword = new Keyword(name, getChars(symbol));	
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
					
					keyword = new Keyword(name, getChars(rhs));
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
				
			case "token":
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

	private Symbol getSymbol(IConstructor symbol) {
		
//
//		if(mode == TOKEN_BASED) {
//			
//			if(symbol.getName().equals("lex")) {
//				RegularExpression regularExpression = regularExpressionsMap.get(SymbolAdapter.toString(symbol, true));
//				if(regularExpression != null) {
//					return regularExpression;
//				}
//			}
//			
//			// TODO: can be used later when I add the translation of {A sep}* to regular expressions.
////			if(isRegularExpression(symbol)) {
////				return getRegularExpression(symbol);
////			}
//			
//			if(isKeyword(symbol)) {
//				return getKeyword(symbol);
//			}
//		}
		
		switch (symbol.getName()) {

			case "char-class":
				return getCharacterClass(symbol);
				
			case "lit":
				return new Nonterminal(SymbolAdapter.toString(symbol, true), true);
	
			case "label":
				return getSymbol(getSymbolCons(symbol));
	
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
				return getSymbol(getSymbolCons(symbol)).withConditions(getConditions(symbol));
				
			case "token":
				return getRegularExpression(symbol);
	
			default:
				return new Nonterminal(SymbolAdapter.toString(symbol, true));
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
			case "token":
				regex = regularExpressionsMap.get(((IString)symbol.get("name")).getValue());
				break;
				
			case "conditional":
				regex = getRegularExpression(getSymbolCons(symbol)).withConditions(getConditions(symbol));
				break;
			
			case "label":
				regex = getRegularExpression(getSymbolCons(symbol));
				break;
				
			case "char-class":
				regex = getCharacterClass(symbol);
				break;
	
			case "iter":
				regex = new RegexPlus(getRegularExpression(getSymbolCons(symbol)));
				break;
	
			case "iter-seps":
				regex = new RegexPlus(getRegularExpression(getSymbolCons(symbol)));
				break;
	
			case "iter-star":
				regex = new RegexStar(getRegularExpression(getSymbolCons(symbol)));
				break;
	
			case "iter-star-seps":
				regex = new RegexStar(getRegularExpression(getSymbolCons(symbol)));
				break;
	
			case "opt":
				regex = new RegexOpt(getRegularExpression(getSymbolCons(symbol)));
				break;
	
			case "alt":
				regex = new RegexAlt<>(getRegularExpressionList((ISet) symbol.get("alternatives")));
				break;
	
			case "seq":
				regex = new Sequence<>(getRegularExpressionList((IList) symbol.get("symbols")));
				break;
				
			default:
				throw new IllegalStateException("Should not reach here. " + symbol);
			}
		
		if(regex == null) {
			throw new RuntimeException("Regex cannot be null.");
		}

		// Initialize the automaton field
		regex.getAutomaton().determinize();
		regularExpressionsCache.put(symbol, regex);
		return regex;
	}

	private CharacterClass getCharacterClass(IConstructor symbol) {
		List<Range> targetRanges = buildRanges(symbol);
		return new CharacterClass(targetRanges);
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
				List<RegularExpression> list = new ArrayList<>();
				for(IConstructor c : deleteList) {
					list.add(getRegularExpression(c));
				}
				regex = new RegexAlt<>(list);
				
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
	
	private static List<RegularExpression> conver(List<Symbol> symbols) {
		List<RegularExpression> regularExpressions = new ArrayList<>();
		for(Symbol s : symbols) {
			regularExpressions.add((RegularExpression) s);
		}
		return regularExpressions;
	}
	
}