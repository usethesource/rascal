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
import org.jgll.grammar.symbol.Nonterminal;
import org.jgll.grammar.symbol.Range;
import org.jgll.grammar.symbol.Rule;
import org.jgll.grammar.symbol.Symbol;
import org.jgll.parser.GLLParser;
import org.jgll.parser.ParseError;
import org.jgll.parser.ParseResult;
import org.jgll.parser.ParserFactory;
import org.jgll.regex.RegexAlt;
import org.jgll.regex.RegexOpt;
import org.jgll.regex.RegexPlus;
import org.jgll.regex.RegexStar;
import org.jgll.regex.RegularExpression;
import org.jgll.regex.Sequence;
import org.jgll.sppf.SPPFNode;
import org.jgll.traversal.ModelBuilderVisitor;
import org.jgll.traversal.Result;
import org.jgll.util.GrammarUtil;
import org.jgll.util.Input;
import org.jgll.util.Visualization;
import org.jgll.util.logging.LoggerWrapper;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class GrammarToJigll {

	private static final LoggerWrapper log = LoggerWrapper.getLogger(GrammarToJigll.class);

	private final IValueFactory vf;

	private Map<IValue, Rule> rulesMap;

	private Map<String, RegularExpression> regularExpressionsMap;
	
	private Map<IConstructor, RegularExpression> regularExpressionsCache;
	
	private Map<List<IConstructor>, RegularExpression> deleteSetCache;

	private Grammar grammar;

	private GLLParser parser;

	private String startSymbol;

	private Input input;
	
	private boolean saveObjects = false;

	private IMap definitions;

	public GrammarToJigll(IValueFactory vf) {
		this.vf = vf;
		deleteSetCache = new HashMap<>();
		regularExpressionsCache = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	public IConstructor jparse(IConstructor symbol, IString str, ISourceLocation loc) {
		
		if (grammar == null) {
			return null;
		}

		input = Input.fromString(str.getValue(), loc.getURI());
		parser = ParserFactory.newParser(grammar, input);

		startSymbol = SymbolAdapter.toString(symbol, true);

		GrammarGraph grammarGraph = grammar.toGrammarGraph();
		grammarGraph.reset();
		ParseResult result = parser.parse(input, grammarGraph, startSymbol);

		if (result.isParseSuccess()) {
			long start = System.nanoTime();
			SPPFNode sppf = result.asParseSuccess().getRoot();
			sppf.accept(new ModelBuilderVisitor<>(input, new ParsetreeBuilder(), grammarGraph));
			long end = System.nanoTime();
			log.info("Flattening: %d ms ", (end - start) / 1000_000);

			return ((Result<IConstructor>) sppf.getObject()).getObject();			
		} else {
			ParseError e = result.asParseError();
			throw RuntimeExceptionFactory.parseError(
					   vf.sourceLocation(loc, 
					   e.getInputIndex(), 
					   1,
					   input.getLineNumber(e.getInputIndex()),
					   input.getLineNumber(e.getInputIndex()),
					   input.getColumnNumber(e.getInputIndex()) - 1,
					   input.getColumnNumber(e.getInputIndex()) - 1), null, null);			
		}
		
	}

	public void generateGrammar(IConstructor rascalGrammar) {
		System.out.println("Iguana started.");
		grammar = convert("inmemory", rascalGrammar);
		IMap notAllowed = (IMap) ((IMap) rascalGrammar.get("about")).get(vf.string("notAllowed"));
		IMap except = (IMap) ((IMap) rascalGrammar.get("about")).get(vf.string("excepts"));

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
		GrammarUtil.save(grammar, new File(path.getValue()).toURI());
	}

	public void generateGraph(IString path, ISourceLocation loc) {
		parser = ParserFactory.newParser(grammar, input);

		GrammarGraph grammarGraph = grammar.toGrammarGraph();
		ParseResult result = parser.parse(input, grammarGraph, startSymbol);
		if (result.isParseSuccess()) {
			SPPFNode sppf = result.asParseSuccess().getRoot();
			Visualization.generateSPPFGraph(path.getValue(), sppf, grammarGraph, input);
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

	private Condition getNotFollow(IConstructor symbol) {
		
		switch (symbol.getName()) {

			case "char-class":
				List<Range> targetRanges = buildRanges(symbol);
				return RegularExpressionCondition.notFollow(CharacterClass.from(targetRanges));
	
			case "lit":
				return RegularExpressionCondition.notFollow(getRegularExpression(symbol));
				
			case "seq":
				IList list = (IList) symbol.get("symbols");
				if (list.length() > 0 && ((IConstructor)list.get(0)).getName().equals("lex")) {
					return RegularExpressionCondition.notFollow(createRegularExpression((IConstructor) list.get(0), definitions));
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
				return RegularExpressionCondition.follow(getRegularExpression(symbol));
	
			case "seq":
				IList list = (IList) symbol.get("symbols");
				List<Symbol> symbols = new ArrayList<>();
				for (IValue v : list) {
					symbols.add(getSymbol((IConstructor) v));
				}
				symbols.remove(1);
				if(isAllRegularExpression(symbols)) {
					return RegularExpressionCondition.follow(Sequence.from(conver(symbols)));				
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
				return RegularExpressionCondition.notPrecede(getRegularExpression(symbol));
	
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
				return RegularExpressionCondition.precede(getRegularExpression(symbol));
	
			default:
				throw new IllegalStateException("Should not be here!");
		}
	}

	public Grammar convert(String name, IConstructor rascalGrammar) {

		Grammar.Builder builder = new Grammar.Builder();
		
		definitions = (IMap) rascalGrammar.get("rules");
		
		rulesMap = new HashMap<>();
		regularExpressionsMap = new HashMap<>();
		regularExpressionsCache = new HashMap<>();
		deleteSetCache = new HashMap<>();
		
		createRegularExpressions(definitions);

		for (IValue nonterminal : definitions) {

			IConstructor constructor = (IConstructor) nonterminal;
			
			boolean ebnf = isEBNF(constructor);

			Nonterminal head = getHead(constructor);
			
			if(regularExpressionsMap.containsKey(head.getName())) {
				continue;
			}
			
			IConstructor choice = (IConstructor) definitions.get(nonterminal);
			assert choice.getName().equals("choice");
			ISet alts = (ISet) choice.get("alternatives");

			for (IValue alt : alts) {

				IConstructor prod = (IConstructor) alt;

				SerializableValue object = null;

				if (saveObjects) {
					if (ebnf) {
						object = new SerializableValue(getRegularDefinition(alts));
					} else {
						object = new SerializableValue((IConstructor) alt);
					}					
				} 

				if (!prod.getName().equals("regular")) {

					IList rhs = (IList) prod.get("symbols");

					List<Symbol> body = getSymbolList(rhs);
					
					Rule rule = new Rule(head, body, object);
					rulesMap.put(prod, rule);
					builder.addRule(rule);
				}
			}
		}
		
		return builder.build();
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
			
			if (SymbolAdapter.isToken(nont) || SymbolAdapter.isLiteral(nont) || SymbolAdapter.isCILiteral(nont) ) {
				
				IConstructor prod = (IConstructor) regularExpression.getValue();
				
				if (prod.getName().equals("choice")) {
					ISet alts = (ISet) prod.get("alternatives");
					assert alts.size() == 1;
					prod = (IConstructor) alts.iterator().next();
				}
				
				IList rhs = (IList) ((IConstructor) prod).get("symbols");

				List<RegularExpression> body = getRegularExpressionList(rhs);
				
//				if(body.size() == 1) {
//					regularExpressionsMap.put(head.getName(), body.get(0));				
//				} else {
				SerializableValue object = null;
				if (saveObjects) {
					object = new SerializableValue(prod);
				}
				
				regularExpressionsMap.put(head.getName(), new Sequence.Builder<RegularExpression>(body).setLabel(head.getName()).setObject(object).build());
//				}
			}
		}
	}
	
	private RegularExpression createRegularExpression(IConstructor regex, IMap definitions) {
		
		IConstructor prod = (IConstructor) definitions.get(regex);
		
		if (prod.getName().equals("choice")) {
			ISet alts = (ISet) prod.get("alternatives");
			assert alts.size() == 1;
			prod = (IConstructor) alts.iterator().next();
		}
		
		IList rhs = (IList) ((IConstructor) prod).get("symbols");

		List<RegularExpression> body = getRegularExpressionList(rhs);
		
		return new Sequence.Builder<RegularExpression>(body).build();
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
	
	private Nonterminal getHead(IConstructor symbol) {
		switch (symbol.getName()) {

			case "lit":
				return Nonterminal.withName(SymbolAdapter.toString(symbol, true));
				
			case "token":
				return Nonterminal.withName(SymbolAdapter.toString(symbol, true));
	
			case "iter":
				return new Nonterminal.Builder(SymbolAdapter.toString(symbol, true)).setEbnfList(true).build();
	
			case "iter-seps":
				return new Nonterminal.Builder(SymbolAdapter.toString(symbol, true)).setEbnfList(true).build();
	
			case "iter-star":
				return new Nonterminal.Builder(SymbolAdapter.toString(symbol, true)).setEbnfList(true).build();
	
			case "iter-star-seps":
				return new Nonterminal.Builder(SymbolAdapter.toString(symbol, true)).setEbnfList(true).build();
	
			default:
				return Nonterminal.withName(SymbolAdapter.toString(symbol, true));
		}
	}

	private Symbol getSymbol(IConstructor symbol) {
		
		RegularExpression regularExpression = regularExpressionsMap.get(SymbolAdapter.toString(symbol, true));
		if(regularExpression != null) {
			return regularExpression;
		}

		switch (symbol.getName()) {

			case "char-class":
				return getCharacterClass(symbol);
				
			case "lit":
				return Nonterminal.withName(SymbolAdapter.toString(symbol, true));
	
			case "label":
				return getSymbol(getSymbolCons(symbol));
	
			case "iter":
				return new Nonterminal.Builder(SymbolAdapter.toString(symbol, true)).setEbnfList(true).build();
	
			case "iter-seps":
				return new Nonterminal.Builder(SymbolAdapter.toString(symbol, true)).setEbnfList(true).build();
	
			case "iter-star":
				return new Nonterminal.Builder(SymbolAdapter.toString(symbol, true)).setEbnfList(true).build();
	
			case "iter-star-seps":
				return new Nonterminal.Builder(SymbolAdapter.toString(symbol, true)).setEbnfList(true).build();
	
			case "opt":
				return Nonterminal.withName(SymbolAdapter.toString(symbol, true));
	
			case "alt":
				return Nonterminal.withName(SymbolAdapter.toString(symbol, true));
	
			case "seq":
				return Nonterminal.withName(SymbolAdapter.toString(symbol, true));
	
			case "start":
				return Nonterminal.withName("start[" + SymbolAdapter.toString(getSymbolCons(symbol), true) + "]");
	
			case "conditional":
				return getSymbol(getSymbolCons(symbol)).builder().addConditions(getConditions(symbol)).build();
				
			case "token":
				return getRegularExpression(symbol);
	
			default:
				return Nonterminal.withName(SymbolAdapter.toString(symbol, true));
		}
	}
	
	private RegularExpression getRegularExpression(IConstructor symbol) {
		
		RegularExpression regex = regularExpressionsCache.get(symbol);
		
		if(regex != null) {
			return regex;
		}
		
		SerializableValue object = null;
		if (saveObjects) {
			object = new SerializableValue(symbol);
		}
		
		switch (symbol.getName()) {
		
			case "keywords":
			case "sort":
			case "layouts":
			case "lex":
			case "token":
				regex = regularExpressionsMap.get(((IString)symbol.get("name")).getValue());
				break;
				
			case "lit":
				regex = regularExpressionsMap.get(SymbolAdapter.toString(symbol, true));
				break;
				
			case "conditional":
				regex = (RegularExpression) getRegularExpression(getSymbolCons(symbol)).builder().addConditions(getConditions(symbol)).build();
				break;
			
			case "label":
				regex = getRegularExpression(getSymbolCons(symbol));
				break;
				
			case "char-class":
				regex = getCharacterClass(symbol);
				break;
	
			case "iter":
				regex = new RegexPlus.Builder(getRegularExpression(getSymbolCons(symbol))).setObject(object).build();
				break;
	
			case "iter-seps":
				regex = new RegexPlus.Builder(getRegularExpression(getSymbolCons(symbol))).setObject(object).build();
				break;
	
			case "iter-star":
				regex = new RegexStar.Builder(getRegularExpression(getSymbolCons(symbol))).setObject(object).build();
				break;
	
			case "iter-star-seps":
				regex = new RegexStar.Builder(getRegularExpression(getSymbolCons(symbol))).setObject(object).build();
				break;
	
			case "opt":
				regex = new RegexOpt.Builder(getRegularExpression(getSymbolCons(symbol))).build();
				break;
	
			case "alt":
				regex = new RegexAlt.Builder<>(getRegularExpressionList((ISet) symbol.get("alternatives"))).setObject(object).build();
				break;
	
			case "seq":
				regex = new Sequence.Builder<>(getRegularExpressionList((IList) symbol.get("symbols"))).setObject(object).build();
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
		return new CharacterClass.Builder(targetRanges).setLabel(SymbolAdapter.toString(symbol, true)).build();
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
				regex = RegexAlt.from(list);
				
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
				|| SymbolAdapter.isSeq(value) || SymbolAdapter.isOpt(value);
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