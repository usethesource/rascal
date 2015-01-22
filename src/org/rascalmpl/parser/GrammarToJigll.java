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
import org.jgll.grammar.condition.PositionalCondition;
import org.jgll.grammar.condition.RegularExpressionCondition;
import org.jgll.grammar.precedence.OperatorPrecedence;
import org.jgll.grammar.symbol.Character;
import org.jgll.grammar.symbol.CharacterClass;
import org.jgll.grammar.symbol.CharacterRange;
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
import org.jgll.traversal.ModelBuilderVisitor;
import org.jgll.util.Configuration;
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

	private Grammar grammar;

	private GLLParser parser;

	private String startSymbol;

	private Input input;
	
	private boolean saveObjects = false;

	private IMap definitions;

	public GrammarToJigll(IValueFactory vf) {
		this.vf = vf;
	}

	public IConstructor jparse(IConstructor symbol, IString str, ISourceLocation loc) {
		
		if (grammar == null) {
			return null;
		}
		
		Configuration config = Configuration.DEFAULT;

		input = Input.fromString(str.getValue(), loc.getURI());
		parser = ParserFactory.getParser(config, input, grammar);

		startSymbol = SymbolAdapter.toString(symbol, true);

		GrammarGraph grammarGraph = grammar.toGrammarGraph(input, config);
		grammarGraph.reset(input);
		ParseResult result = parser.parse(input, grammarGraph, Nonterminal.withName(startSymbol));

		if (result.isParseSuccess()) {
			long start = System.nanoTime();
			SPPFNode sppf = result.asParseSuccess().getRoot();
			sppf.accept(new ModelBuilderVisitor<>(input, new ParsetreeBuilder(), grammarGraph));
			long end = System.nanoTime();
			log.info("Flattening: %d ms ", (end - start) / 1000_000);

//			return ((Result<IConstructor>) sppf.getObject()).getObject();
			return null;
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
		
		for (IValue nonterminal : definitions) {

			IConstructor constructor = (IConstructor) nonterminal;
			
			boolean ebnf = isEBNF(constructor);

			Nonterminal head = Nonterminal.withName(SymbolAdapter.toString(constructor, true));
			
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
					
					Rule rule = Rule.withHead(head).addSymbols(body).setObject(object).build();
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
				return getCharacterClass(symbol);
				
			case "lit":
				Sequence<Character> keyword = Sequence.from(getString(symbol));
				return keyword.isSingleChar() ? keyword.asSingleChar() : keyword;
	
			case "label":
				return getSymbol(getSymbolCons(symbol)).copyBuilder().setLabel(getLabel(symbol)).build();
	
			case "iter":
				return Plus.from(getSymbol(getSymbolCons(symbol)));
	
			case "iter-seps":
				return new Nonterminal.Builder(SymbolAdapter.toString(symbol, true)).setEbnfList(true).build();
	
			case "iter-star":
				return Star.from(getSymbol(getSymbolCons(symbol)));
	
			case "iter-star-seps":
				return new Nonterminal.Builder(SymbolAdapter.toString(symbol, true)).setEbnfList(true).build();
	
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
				
			case "token":
	
			default:
				throw new UnsupportedOperationException(symbol.toString());
		}
	}

	private CharacterClass getCharacterClass(IConstructor symbol) {
		List<CharacterRange> targetRanges = buildRanges(symbol);
		return new CharacterClass.Builder(targetRanges).build();
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
					set.add(RegularExpressionCondition.notPrecede((RegularExpression) notPrecede));
					break;
	
				case "start-of-line":
					set.add(new PositionalCondition(ConditionType.START_OF_LINE));
					break;
	
				case "precede":
					IConstructor precede = getSymbolCons((IConstructor) condition);
					set.add(RegularExpressionCondition.precede((RegularExpression) precede));
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
		return ((IString) symbol.get("label")).getValue();
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