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
import org.jgll.grammar.CharacterClass;
import org.jgll.grammar.Grammar;
import org.jgll.grammar.GrammarBuilder;
import org.jgll.grammar.Keyword;
import org.jgll.grammar.Nonterminal;
import org.jgll.grammar.Range;
import org.jgll.grammar.Rule;
import org.jgll.grammar.Symbol;
import org.jgll.grammar.condition.Condition;
import org.jgll.grammar.condition.ConditionFactory;
import org.jgll.parser.GLLParser;
import org.jgll.parser.ParseError;
import org.jgll.parser.ParserFactory;
import org.jgll.sppf.NonterminalSymbolNode;
import org.jgll.traversal.ModelBuilderVisitor;
import org.jgll.traversal.Result;
import org.jgll.util.Input;
import org.jgll.util.dot.GraphVizUtil;
import org.jgll.util.dot.SPPFToDot;
import org.jgll.util.dot.ToDotWithoutIntermeidateAndLists;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class GrammarToJigll {
	
	private final IValueFactory vf;
	
	private Map<IValue, Rule> rulesMap;
	
	private Map<String, Keyword> keywordsMap;
	
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
	  if(grammar == null) {
		  return null;
	  }
	  
	  parser = ParserFactory.levelParser(grammar);

	  System.out.println("Jigll started.");
	  
	  NonterminalSymbolNode sppf = null;

	  input = Input.fromString(str.getValue());
	  startSymbol = SymbolAdapter.toString(symbol);
	  
	  try {
		sppf = parser.parse(input, this.grammar, startSymbol);
	  } catch(ParseError e) {
		  System.out.println(e);
		  throw RuntimeExceptionFactory.parseError(vf.sourceLocation(URI.create("nothing:///"), 0, 1), null, null);
	  }

	  long start = System.nanoTime();
	  sppf.accept(new ModelBuilderVisitor<>(input, new ParsetreeBuilder()));
	  long end = System.nanoTime();
	  System.out.println("Flattening: " + (end - start) / 1000_000);
	  
	  return ((Result<IConstructor>)sppf.getObject()).getObject();
	}
	
	public void generateGrammar(IConstructor rascalGrammar) {
		  this.rascalGrammar = rascalGrammar;
		  GrammarBuilder builder = convert("inmemory", rascalGrammar);
		  IMap notAllowed = (IMap) ((IMap) rascalGrammar.get("about")).get(vf.string("notAllowed"));
		  applyRestrictions(builder, notAllowed);
		  builder.filter();
		  
		  grammar = builder.build();
	}
	
	public void printGrammar() {
		System.out.println(grammar);
	}
	
	public void save(IString path) throws FileNotFoundException, IOException {
		File file = new File(path.getValue());
		if(!file.exists()) {
			file.createNewFile();
		}
		ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		out.writeObject(grammar);
		out.close();
	}
	
	public void generateGraph(IString path) {
		parser = ParserFactory.levelParser(grammar);
		
		NonterminalSymbolNode sppf;
		try {
			sppf = parser.parse(input, this.grammar, startSymbol);			
		} catch(ParseError e) {
			throw RuntimeExceptionFactory.parseError(vf.sourceLocation(URI.create("nothing:///"), 0, 1), null, null);
		}
		
		SPPFToDot toDot = new ToDotWithoutIntermeidateAndLists();
		sppf.accept(toDot);
		GraphVizUtil.generateGraph(toDot.getString(), path.getValue(), "graph");
	}
	
	private Condition getDeleteSet(IConstructor symbol) {
		
		switch(symbol.getName()) {

			case "seq":
				IList list = (IList) symbol.get("sequence");
				List<Symbol> symbols = new ArrayList<>();
				for(IValue v : list) {
					symbols.add(getSymbol((IConstructor) v));
				}
				symbols.remove(1);
				return ConditionFactory.notMatch(symbols.toArray(new Symbol[] {}));
			
		  default:
				  return ConditionFactory.notMatch(new Symbol[] { getSymbol(symbol) });

		  // TODO: optimize for the case where symbol defines a finite set of strings
			// TODO: keywords will dissappear from Rascal
/*
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
	*/		
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
				IList list = (IList) symbol.get("sequence");
				List<Symbol> symbols = new ArrayList<>();
				for(IValue v : list) {
					symbols.add(getSymbol((IConstructor) v));
				}
				symbols.remove(1);
				return ConditionFactory.notFollow(symbols);
				
			default:
				throw new IllegalStateException("Should not be here!");
		}
	}
	
	private Condition getNotPrecede(IConstructor symbol) {
		switch(symbol.getName()) {
		
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
		
		GrammarBuilder builder = new GrammarBuilder(name);
		
		IMap definitions = (IMap) rascalGrammar.get("rules");
		rulesMap = new HashMap<>();
		keywordsMap = new HashMap<>();

		for (IValue nonterminal : definitions) {
			boolean ebnf = isEBNF((IConstructor) nonterminal);
			
			Nonterminal head = getHead((IConstructor) nonterminal);
			
			if(head == null) {
				continue;
			}
			 
			IConstructor choice = (IConstructor) definitions.get(nonterminal);
			assert choice.getName().equals("choice");
			ISet alts = (ISet) choice.get("alternatives");
			
			for (IValue alt : alts) {
				
				IConstructor prod = (IConstructor) alt;
				Object object;
				if(ebnf) {
					object = getRegularDefinition(alts);
				} else {
					object = alt;
				}
				
				if(!prod.getName().equals("regular")) {
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
	
	private void applyRestrictions(GrammarBuilder builder, IMap notAllowed) {
		Iterator<Entry<IValue, IValue>> it = notAllowed.entryIterator();
		while(it.hasNext()) {
			Entry<IValue, IValue> next = it.next();
			
			// Tuple(prod, position)
			ITuple key = (ITuple) next.getKey();
			ISet set = (ISet) next.getValue();
			
			Rule rule = (Rule) rulesMap.get(key.get(0));
			int position = ((IInteger) key.get(1)).intValue();
				
			Iterator<IValue> iterator = set.iterator();
			while(iterator.hasNext()) {
				// Create a new filter for each filtered nonterminal
				builder.addFilter(rule.getHead(), rule, position, rulesMap.get(iterator.next()));
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
		for (IValue elem : rhs) {
			IConstructor cons = (IConstructor) elem;
			Symbol symbol = getSymbol(cons);
			if(symbol != null) {
				result.add(symbol);
			}
		}
		return result;
	}
	
	private Keyword getKeyword(IConstructor symbol) {
		
		String name = SymbolAdapter.toString(symbol);
		Keyword keyword = keywordsMap.get(name);
		
		if(keyword == null) {
		
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
				for(IValue s : rhs) {
					
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
	
			keyword =  new Keyword(name, chars);
		}
		
		return keyword;
	}
	
	private Nonterminal getHead(IConstructor symbol) {
		switch (symbol.getName()) {

			case "lit":
				return new Nonterminal(SymbolAdapter.toString(symbol));
				
			case "iter":
				return new Nonterminal(SymbolAdapter.toString(symbol), true);
				
			case "iter-seps":
				return new Nonterminal(SymbolAdapter.toString(symbol), true);
				
			case "iter-star":
				return new Nonterminal(SymbolAdapter.toString(symbol), true);
				
			case "iter-star-seps":
				return new Nonterminal(SymbolAdapter.toString(symbol), true);
				
			default:
				return new Nonterminal(SymbolAdapter.toString(symbol));
		}

	}
		
	private Symbol getSymbol(IConstructor symbol) {
		
		switch (symbol.getName()) {
		
			case "char-class":
				List<Range> targetRanges = buildRanges(symbol);
				return new CharacterClass(targetRanges);
				
			case "lit":
				return getKeyword(symbol);
				
			case "label":
				return getSymbol(getSymbolCons(symbol));
								
			case "iter":
				return new Nonterminal(SymbolAdapter.toString(symbol), true);
				
			case "iter-seps":
				return new Nonterminal(SymbolAdapter.toString(symbol), true);
				
			case "iter-star":
				return new Nonterminal(SymbolAdapter.toString(symbol), true);
				
			case "iter-star-seps":
				return new Nonterminal(SymbolAdapter.toString(symbol), true);
								
			case "opt":
				return new Nonterminal(SymbolAdapter.toString(symbol));
				
			case "alt":
				return new Nonterminal(SymbolAdapter.toString(symbol));
				
			case "seq":
				return new Nonterminal(SymbolAdapter.toString(symbol));	
				
			case "start":
				return new Nonterminal("start[" + SymbolAdapter.toString(getSymbolCons(symbol)) + "]");
				
			case "conditional":
				return getSymbol(getSymbolCons(symbol)).addConditions(getConditions(symbol));
				
			default:
				return new Nonterminal(SymbolAdapter.toString(symbol));
			}
	}
	
	private List<Condition> getConditions(IConstructor symbol) {
		ISet conditions = (ISet) symbol.get("conditions");
		
		List<Condition> list = new ArrayList<>();
		
		for(IValue condition : conditions) {
			
			switch(((IConstructor)condition).getName()) {
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
		return isEBNFList(value) ||
			   SymbolAdapter.isAlt(value) ||
			   SymbolAdapter.isSeq(value) ||
			   SymbolAdapter.isOpt(value);
	}
	
	private boolean isEBNFList(IConstructor value) {
		return SymbolAdapter.isIterStarSeps(value) ||
				   SymbolAdapter.isIterStar(value) ||
				   SymbolAdapter.isIterPlus(value) ||
				   SymbolAdapter.isIterPlusSeps(value);
	}
	
	private IConstructor getRegularDefinition(ISet alts) {
		IConstructor value = null;
		for (IValue alt : alts) {
			IConstructor prod = (IConstructor) alt;
			if(prod.getName().equals("regular")) {
				value = prod;
			}
		}
		return value;
	}
}
