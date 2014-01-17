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

	private Grammar grammar;

	private GLLParser parser;

	private String startSymbol;

	private Input input;

	private IConstructor rascalGrammar;
	
	public GrammarToJigll(IValueFactory vf) {
		this.vf = vf;
	}

	@SuppressWarnings("unchecked")
	public IConstructor jparse(IConstructor symbol, IString str) {
		if (grammar == null) {
			return null;
		}

		parser = ParserFactory.createRecursiveDescentParser(grammar);

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
		parser = ParserFactory.createRecursiveDescentParser(grammar);

		NonterminalSymbolNode sppf;
		try {
			sppf = parser.parse(input, this.grammar, startSymbol);
		} catch (ParseError e) {
			throw RuntimeExceptionFactory.parseError(vf.sourceLocation(URI.create("nothing:///"), 0, 1), null, null);
		}

		Visualization.generateSPPFGraph(path.getValue(), sppf, input);
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
			if(isAllRegularExpression(symbols)) {
				return RegularExpressionCondition.notMatch(new Sequence<RegularExpression>(conver(symbols)));				
			} else {
				return ContextFreeCondition.notMatch(symbols);
			}

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
			return RegularExpressionCondition.notMatch(keywords.toArray(new Keyword[] {}));

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
				return RegularExpressionCondition.notMatch(new Sequence<RegularExpression>(conver(symbols)));				
			} else {
				return ContextFreeCondition.notMatch(symbols);
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

	public GrammarBuilder convert(String name, IConstructor rascalGrammar) {

		GrammarBuilder builder = new GrammarBuilder(name);
		
		IMap definitions = (IMap) rascalGrammar.get("rules");
		rulesMap = new HashMap<>();
		keywordsMap = new HashMap<>();
		regularExpressionsMap = new HashMap<>();
		
		IMap regularExpressions = (IMap) ((IMap) rascalGrammar.get("about")).get(vf.string("regularExpressions"));
		createRegularExpressions(regularExpressions);

		for (IValue nonterminal : definitions) {

			IConstructor constructor = (IConstructor) nonterminal;
			
			boolean ebnf = isEBNF(constructor);

			Nonterminal head = getHead(constructor);
			
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
					builder.addRule(rule);
				}
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

	private Symbol getSymbol(IConstructor symbol) {
		
		//TODO: do the same for keywords
		RegularExpression regexp = regularExpressionsMap.get(symbol.getName());
		if(regexp != null) {
			return regexp;
		}
		
		if(isRegularExpression(symbol)) {
			return getRegularExpression(symbol);
		}
		
		switch (symbol.getName()) {

			case "lex":
				// TODO: check if this is always the case
				return regularExpressionsMap.get(((IString)symbol.get("name")).getValue());
			
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
	
	private RegularExpression getRegularExpression(IConstructor symbol) {
		
		switch (symbol.getName()) {
			
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
					list.add(new PositionalCondition(ConditionType.END_OF_LINE));
	
				case "start-of-line":
					list.add(new PositionalCondition(ConditionType.START_OF_LINE));
	
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