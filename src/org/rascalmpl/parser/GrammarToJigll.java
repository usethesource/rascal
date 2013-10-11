package org.rascalmpl.parser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.jgll.grammar.condition.ConditionFactory;
import org.jgll.grammar.condition.ConditionType;
import org.jgll.grammar.condition.TerminalCondition;
import org.jgll.grammar.symbol.CharacterClass;
import org.jgll.grammar.symbol.Keyword;
import org.jgll.grammar.symbol.Nonterminal;
import org.jgll.grammar.symbol.Plus;
import org.jgll.grammar.symbol.Range;
import org.jgll.grammar.symbol.RegularExpression;
import org.jgll.grammar.symbol.RegularList;
import org.jgll.grammar.symbol.Rule;
import org.jgll.grammar.symbol.Star;
import org.jgll.grammar.symbol.Symbol;
import org.jgll.grammar.symbol.Terminal;
import org.jgll.parser.GLLParser;
import org.jgll.parser.ParseError;
import org.jgll.parser.ParserFactory;
import org.jgll.sppf.NonterminalSymbolNode;
import org.jgll.traversal.ModelBuilderVisitor;
import org.jgll.traversal.Result;
import org.jgll.util.CollectionsUtil;
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

	private Grammar grammar;

	private GLLParser parser;

	private String startSymbol;

	private Input input;

	private IConstructor rascalGrammar;

	private Set<Rule> regularListRules;
	
	public GrammarToJigll(IValueFactory vf) {
		this.vf = vf;
	}

	@SuppressWarnings("unchecked")
	public IConstructor jparse(IConstructor symbol, IString str) {
		if (grammar == null) {
			return null;
		}

		parser = ParserFactory.levelParser(grammar, 30);

		log.info("Iguana started.");

		NonterminalSymbolNode sppf = null;

		input = Input.fromString(str.getValue());
		startSymbol = SymbolAdapter.toString(symbol, true);

		try {
			sppf = parser.parse(input, this.grammar, startSymbol);
		} catch (ParseError e) {
			System.out.println(e);
			throw RuntimeExceptionFactory.parseError(vf.sourceLocation(URI.create("nothing:///"), 0, 1), null, null);
		}

		long start = System.nanoTime();
		sppf.accept(new ModelBuilderVisitor<>(input, new ParsetreeBuilder()));
		long end = System.nanoTime();
		log.info("Flattening: %d ms ", (end - start) / 1000_000);

		return ((Result<IConstructor>) sppf.getObject()).getObject();
	}

	public void generateGrammar(IConstructor rascalGrammar) {
		this.rascalGrammar = rascalGrammar;
		GrammarBuilder builder = convert("inmemory", rascalGrammar);
		IMap notAllowed = (IMap) ((IMap) rascalGrammar.get("about")).get(vf.string("notAllowed"));
		IMap except = (IMap) ((IMap) rascalGrammar.get("about")).get(vf.string("excepts"));

		assert notAllowed != null;
		assert except != null;

		addExceptPatterns(builder, except);
		addPrecedencePatterns(builder, notAllowed);

		builder.rewritePatterns();

		builder.leftFactorize();

		grammar = builder.build();
	}

	public void printGrammar() {
		System.out.println(grammar);
	}

	public void save(IString path) throws FileNotFoundException, IOException {
		File file = new File(path.getValue());
		if (!file.exists()) {
			file.createNewFile();
		}
		ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		out.writeObject(grammar);
		out.close();
	}

	public void generateGraph(IString path) {
		parser = ParserFactory.levelParser(grammar, 30);

		NonterminalSymbolNode sppf;
		try {
			sppf = parser.parse(input, this.grammar, startSymbol);
		} catch (ParseError e) {
			throw RuntimeExceptionFactory.parseError(vf.sourceLocation(URI.create("nothing:///"), 0, 1), null, null);
		}

		Visualization.generateSPPFGraph(path.getValue(), sppf);
	}

	private Condition getDeleteSet(IConstructor symbol) {

		switch (symbol.getName()) {

		case "seq":
			IList list = (IList) symbol.get("sequence");
			List<Symbol> symbols = new ArrayList<>();
			for (IValue v : list) {
				symbols.add(getSymbol((IConstructor) v));
			}
			symbols.remove(1);
			return ConditionFactory.notMatch(symbols.toArray(new Symbol[] {}));

		case "keywords":
			List<Keyword> keywords = new ArrayList<>();
			IMap definitions = (IMap) rascalGrammar.get("rules");
			IConstructor choice = (IConstructor) definitions.get(symbol);
			ISet alts = (ISet) choice.get("alternatives");
			for (IValue alt : alts) {
				IConstructor prod = (IConstructor) alt;
				IList rhs = (IList) prod.get("symbols");
				IConstructor s = (IConstructor) rhs.get(0);
				keywords.add(getKeyword(s));
			}
			return ConditionFactory.notMatch(keywords.toArray(new Keyword[] {}));

		default:
			throw new RuntimeException(symbol.getName() + " is not a supported in delete set.");

			// default:
			// return ConditionFactory.notMatch(new Symbol[] { getSymbol(symbol)
			// });

			// TODO: optimize for the case where symbol defines a finite set of
			// strings
			// TODO: keywords will disappear from Rascal
		}
	}

	private Condition getFollowRestriction(IConstructor symbol) {

		switch (symbol.getName()) {

		case "char-class":
			List<Range> targetRanges = buildRanges(symbol);
			return ConditionFactory.notFollow(new CharacterClass(targetRanges));

		case "lit":
			return ConditionFactory.notFollow(getKeyword(symbol));

		case "seq":
			IList list = (IList) symbol.get("symbols");
			List<Symbol> symbols = new ArrayList<>();
			for (IValue v : list) {
				symbols.add(getSymbol((IConstructor) v));
			}
			symbols.remove(1);
			return ConditionFactory.notFollow(symbols);

		default:
			throw new IllegalStateException("Should not be here!");
		}
	}

	private Condition getNotPrecede(IConstructor symbol) {
		switch (symbol.getName()) {

		case "char-class":
			List<Range> targetRanges = buildRanges(symbol);
			return ConditionFactory.notPrecede(new CharacterClass(targetRanges));

		case "lit":
			return ConditionFactory.notPrecede(getKeyword(symbol));

		default:
			throw new IllegalStateException("Should not be here!");
		}
	}

	public GrammarBuilder convert(String name, IConstructor rascalGrammar) {
		IMap regularExpressions = (IMap) ((IMap) rascalGrammar.get("about")).get(vf.string("regularExpressions"));
		
		Map<Nonterminal, Rule> regularExpressionRules = createRegularExpressionRules(regularExpressions);

		GrammarBuilder builder = new GrammarBuilder(name);
		
		IMap definitions = (IMap) rascalGrammar.get("rules");
		rulesMap = new HashMap<>();
		keywordsMap = new HashMap<>();
		regularListRules = new HashSet<>();
		
		Set<Rule> rules = new HashSet<>();

		for (IValue nonterminal : definitions) {

			boolean ebnf = isEBNF((IConstructor) nonterminal);

			Nonterminal head = getHead((IConstructor) nonterminal);

			if (head == null) {
				continue;
			}

			IConstructor choice = (IConstructor) definitions.get(nonterminal);
			assert choice.getName().equals("choice");
			ISet alts = (ISet) choice.get("alternatives");

			for (IValue alt : alts) {

				IConstructor prod = (IConstructor) alt;

				Object object;

				if (ebnf) {
					object = getRegularDefinition(alts);
				} else {
					object = alt;
				}

				if (!prod.getName().equals("regular")) {

					IList rhs = (IList) prod.get("symbols");

					List<Symbol> body = getSymbolList(rhs);

					Rule rule = new Rule(head, body, object);
					rulesMap.put(prod, rule);
					rules.add(rule);
				}
			}
		}

		for (Rule rule : regularExpressionRules.values()) {
			builder.addRule(rule);
		}
		
		for (Rule rule : rules) {
			if(!regularExpressionRules.containsKey(rule.getHead())) {
				builder.addRule(rule);
			}
		}
		
		return builder;
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
	
	private Map<Nonterminal, Rule> createRegularExpressionRules(IMap regularExpressions) {
		
		Map<Nonterminal, Rule> rules = new HashMap<>();
		
		Iterator<Entry<IValue, IValue>> it = regularExpressions.entryIterator();

		while (it.hasNext()) {
			Entry<IValue, IValue> regularExpression = it.next();

			Nonterminal head = getHead((IConstructor) regularExpression.getKey());
			IValue prod = regularExpression.getValue();
			IList rhs = (IList) ((IConstructor) prod).get("symbols");

			List<Symbol> body = getRegularExpressionList(rhs);
			
			Rule rule = new Rule(head, CollectionsUtil.list(new RegularExpression(body)), prod);
			rules.put(head, rule);
		}
		
		return rules;
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
	
	private List<Symbol> getSymbolList(IList rhs) {
		
		List<Symbol> result = new ArrayList<>();
		
		for(int i = 0; i < rhs.length(); i++) {
			IConstructor current = (IConstructor) rhs.get(i);
			IConstructor next = i + 1 < rhs.length() ? (IConstructor) rhs.get(i + 1) : null;
			Symbol symbol = null; //createRegularList(current, next);
			
			if(symbol == null) {
				symbol = getSymbol(current);				
			}
			
			if (symbol != null) {
				result.add(symbol);
			}
		}
		
		return result;
	}
	
	private List<Symbol> getRegularExpressionList(IList rhs) {
		
		List<Symbol> result = new ArrayList<>();
		
		for(int i = 0; i < rhs.length(); i++) {
			IConstructor current = (IConstructor) rhs.get(i);
			Symbol symbol = getRegularExpression(current);				
			
			if (symbol != null) {
				result.add(symbol);
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
			ISet alts = (ISet) choice.get("alternatives");

			assert alts.size() == 1;

			int[] chars = null;
			for (IValue alt : alts) {
				IConstructor prod = (IConstructor) alt;
				IList rhs = (IList) prod.get("symbols");
				chars = new int[rhs.length()];

				int i = 0;
				for (IValue s : rhs) {

					IList ranges = (IList) ((IConstructor) s).get("ranges");
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
		}

		return keyword;
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

	private Symbol getSymbol(IConstructor symbol) {

		switch (symbol.getName()) {

		case "char-class":
			return getCharacterClass(symbol);

		case "lit":
			return getKeyword(symbol);

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
			return getSymbol(getSymbolCons(symbol)).addConditions(getConditions(symbol));

		default:
			return new Nonterminal(SymbolAdapter.toString(symbol, true));
		}
	}
	
	private Symbol getRegularExpression(IConstructor symbol) {
		
		switch (symbol.getName()) {
		
		case "label":
			return getRegularExpression(getSymbolCons(symbol));
			
		case "char-class":
			return getCharacterClass(symbol);	

		case "iter":
			return new Plus(getCharacterClass(getSymbolCons(symbol)));

		case "iter-seps":
			return new Plus(getCharacterClass(getSymbolCons(symbol)));

		case "iter-star":
			return new Star(getCharacterClass(getSymbolCons(symbol)));

		case "iter-star-seps":
			return new Star(getCharacterClass(getSymbolCons(symbol)));

//		case "opt":
//			return new Nonterminal(SymbolAdapter.toString(symbol, true));
//
//		case "alt":
//			return new Nonterminal(SymbolAdapter.toString(symbol, true));
//
//		case "seq":
//			return new Nonterminal(SymbolAdapter.toString(symbol, true));
			
		default:
			throw new IllegalStateException("Should not reach here." + symbol);
		}
	}

	private CharacterClass getCharacterClass(IConstructor symbol) {
		List<Range> targetRanges = buildRanges(symbol);
		return new CharacterClass(targetRanges);
	}

	@SuppressWarnings("unused")
	private Nonterminal createRegularList(IConstructor currentSymbol, IConstructor nextSymbol) {
		
		if(SymbolAdapter.isConditional(currentSymbol)) {
			RegularList regularList = createRegularListFromConditionals(currentSymbol);
			if(regularList != null) {
				String head = SymbolAdapter.toString(getSymbolCons(currentSymbol), true);
				createRugularListRules(head, regularList);
				return new Nonterminal("Regular_" + head);
			}
		}
		
		if(!isCharacterClassList(currentSymbol) || nextSymbol == null) {
			return null;
		}
		
		CharacterClass thisCharacterClass = getCharacterClassList(currentSymbol);
		CharacterClass otherCharacterClass = getCharacterClassList(nextSymbol);
		
		if (thisCharacterClass != null && otherCharacterClass != null) {
			if(!otherCharacterClass.contains(thisCharacterClass)) {
				String head = SymbolAdapter.toString(currentSymbol, true);
				if (SymbolAdapter.isIterStar(currentSymbol)) {
					RegularList regularList = RegularList.star(head, thisCharacterClass);
					createRugularListRules(head, regularList);
					return new Nonterminal("Regular_" + head);
				} 
				else if (SymbolAdapter.isIterPlus(currentSymbol)) {
					RegularList regularList = RegularList.plus(head, thisCharacterClass);
					createRugularListRules(head, regularList);
					return new Nonterminal("Regular_" + head);
				}
			}
		}
		
		return null;
	}
	
	private void createRugularListRules(String head, RegularList regularList) {
		Rule rule = new Rule(new Nonterminal("Regular_" + head), regularList);
		regularListRules.add(rule);
	}
	
	/**
	 *  Returns the character class if the symbol is of the form [a-z]+, [a-z]* or [a-z].
	 *  
	 *  @return null if the given symbol is not of the above mentioned types.
	 */
	private CharacterClass getCharacterClassList(IConstructor symbol) {
		
		if (SymbolAdapter.isIterStar(symbol) && SymbolAdapter.isCharClass(getSymbolCons(symbol))) {
			return getCharacterClass(getSymbolCons(symbol));
		}
		
		if (SymbolAdapter.isIterPlus(symbol) && SymbolAdapter.isCharClass(getSymbolCons(symbol))) {
			return getCharacterClass(getSymbolCons(symbol));
		}
		
		if(SymbolAdapter.isCharClass(symbol)) {
			return getCharacterClass(symbol);
		}
		
		return null;
	}
	
	private boolean isCharacterClassList(IConstructor symbol) {
		return (SymbolAdapter.isIterStar(symbol) && SymbolAdapter.isCharClass(getSymbolCons(symbol))) ||
			   (SymbolAdapter.isIterPlus(symbol) && SymbolAdapter.isCharClass(getSymbolCons(symbol)));
	}
	
	
	private RegularList createRegularListFromConditionals(IConstructor conditional) {
		
		IConstructor symbol = getSymbolCons(conditional);

		if (SymbolAdapter.isIterStar(symbol)) {
			if (SymbolAdapter.isCharClass(getSymbolCons(symbol))) {
				List<Condition> conditions = getConditions(conditional);
				CharacterClass characterClass = getCharacterClass(getSymbolCons(symbol));
				if (isRegularList(characterClass, conditions)) {
					RegularList star = RegularList.star(SymbolAdapter.toString(symbol, true), characterClass);
					return star.addConditions(conditions);
				}
			}
		}

		if (SymbolAdapter.isIterPlus(symbol)) {
			if (SymbolAdapter.isCharClass(getSymbolCons(symbol))) {
				List<Condition> conditions = getConditions(conditional);
				CharacterClass characterClass = getCharacterClass(getSymbolCons(symbol));
				if (isRegularList(characterClass, conditions)) {
					RegularList plus = RegularList.plus(SymbolAdapter.toString(symbol, true), characterClass);
					return plus.addConditions(conditions);
				}
			}
		}

		return null;
	}

	private boolean isRegularList(CharacterClass characterClass, List<Condition> conditions) {

		for (Condition condition : conditions) {
			if (condition.getType() == ConditionType.NOT_FOLLOW && condition instanceof TerminalCondition) {
				TerminalCondition terminalCondition = (TerminalCondition) condition;

				Terminal terminal = terminalCondition.getTerminal();

				if (!(terminal instanceof CharacterClass)) {
					return false;
				}

				CharacterClass other = (CharacterClass) terminal;
				if (other.contains(characterClass)) {
					return true;
				}
			}
		}

		return false;
	}

	private List<Condition> getConditions(IConstructor symbol) {
		ISet conditions = (ISet) symbol.get("conditions");

		List<Condition> list = new ArrayList<>();

		for (IValue condition : conditions) {
			switch (((IConstructor) condition).getName()) {
				case "not-follow":
					IConstructor follow = getSymbolCons((IConstructor) condition);
					list.add(getFollowRestriction(follow));
					break;
	
				case "follow":
					break;
	
				case "delete":
					IConstructor delete = getSymbolCons((IConstructor) condition);
					list.add(getDeleteSet(delete));
					break;
	
				case "not-precede":
					IConstructor precede = getSymbolCons((IConstructor) condition);
					list.add(getNotPrecede(precede));
					break;
	
				case "end-of-line":
					list.add(ConditionFactory.endOfLine());
	
				case "start-of-line":
	
				case "precede":
					break;
	
				case "except":
					break;
	
				default:
					throw new RuntimeException("Unsupported conditional " + symbol);
				}
		}

		return list;
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
}
