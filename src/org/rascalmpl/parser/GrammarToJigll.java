package org.rascalmpl.parser;

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
import org.jgll.grammar.Nonterminal;
import org.jgll.grammar.Range;
import org.jgll.grammar.Rule;
import org.jgll.grammar.Symbol;
import org.jgll.parser.GLLParser;
import org.jgll.parser.LevelSynchronizedGrammarInterpretter;
import org.jgll.parser.ParseError;
import org.jgll.sppf.NonterminalSymbolNode;
import org.jgll.traversal.ModelBuilderVisitor;
import org.jgll.traversal.Result;
import org.jgll.util.Input;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class GrammarToJigll {
	
	private final IValueFactory vf;
	
	private Map<IValue, Rule> rulesMap;
	
	private IConstructor previousGrammar;
	private Grammar grammar;
	private GLLParser parser;

	private CharacterClass notFollow;
	
	public GrammarToJigll(IValueFactory vf) {
		this.vf = vf;
	}

	@SuppressWarnings("unchecked")
	public IConstructor jparse(IValue type, IConstructor symbol, IConstructor grammar, IString str) {
		
	  if(previousGrammar == null || !grammar.isEqual(previousGrammar)) {
		  this.previousGrammar = grammar;
		  this.grammar = generate("inmemory", grammar);
//		  System.out.println(grammar);
//		  System.out.println(this.grammar);
	  }
	  
	  parser = new LevelSynchronizedGrammarInterpretter();
	  Input input = Input.fromString(str.getValue());

	  System.out.println("Jigll started.");
	  
	  
	  NonterminalSymbolNode sppf = null;
	  
	  try {
		  sppf = parser.parse(input, this.grammar, getSymbolName(symbol));
		  
//		ToDot toDot = new ToDotWithoutIntermeidateAndLists();
//		sppf.accept(toDot);
//		GraphVizUtil.generateGraph(toDot.getString(), "/Users/ali/output", "graph");

	  } catch(ParseError e) {
		  System.out.println(e);
		  return null;
	  }

	  long start = System.nanoTime();
	  sppf.accept(new ModelBuilderVisitor<>(input, new ParsetreeBuilder()));
	  long end = System.nanoTime();
	  System.out.println("Flattening: " + (end - start) / 1000_000);
	  
	  return ((Result<IConstructor>)sppf.getObject()).getObject();
	}
	
	private Grammar generate(String name, IConstructor grammar) {
		  GrammarBuilder builder = convert(name, grammar);
		  IMap notAllowed = (IMap) ((IMap) grammar.get("about")).get(vf.string("notAllowed"));
		  applyRestrictions(builder, notAllowed);
		  builder.filter();
		  return builder.build();
	}
	
	public void generate(IString name, IConstructor grammar) {

	}

	public GrammarBuilder convert(String name, IConstructor grammar) {
		
		GrammarBuilder builder = new GrammarBuilder(name);
		
		IMap definitions = (IMap) grammar.get("rules");
		rulesMap = new HashMap<>();

		for (IValue nonterminal : definitions) {
			boolean ebnf = isEBNF((IConstructor) nonterminal);
			
			Nonterminal head = (Nonterminal) getSymbol((IConstructor) nonterminal);
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
				notFollow = null;
				
				if(!prod.getName().equals("regular")) {
					IList rhs = (IList) prod.get("symbols");
					List<Symbol> body = getSymbolList(rhs);
					Rule rule = new Rule(head, body, object);
					rulesMap.put(prod, rule);
					if(notFollow != null) {
						builder.addRule(rule, notFollow);						
					} else {
						builder.addRule(rule);
					}
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
				builder.addFilter(rule.getHead().getName(), rule.getBody(), position, rulesMap.get(iterator.next()).getBody());
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
	
	private Symbol getSymbol(IConstructor symbol) {
		
		switch (symbol.getName()) {
		
		case "char-class":
			List<Range> targetRanges = buildRanges(symbol);
			return new CharacterClass(targetRanges);
			
		case "sort":
			return new Nonterminal(getSymbolName(symbol));
			
		case "lex":
			return new Nonterminal(getSymbolName(symbol));
		
		case "keywords":
			return new Nonterminal(getSymbolName(symbol));
		
		case "layouts":
			return new Nonterminal("layout(" + getSymbolName(symbol) + ")");
			
		case "lit":
			return new Nonterminal("\"" + ((IString)symbol.get("string")).getValue() + "\"");
			
		case "iter":
			return new Nonterminal(getSymbol(getSymbolCons(symbol)) + "+", true);
			
		case "iter-seps":
			return new Nonterminal(getIteratorName(symbol) + "+", true);
			
		case "iter-star":
			return new Nonterminal(getSymbol(getSymbolCons(symbol)) + "*", true);
			
		case "iter-star-seps":
			return new Nonterminal(getIteratorName(symbol) + "*", true);
			
		case "seq":
			return new Nonterminal(getSymbolList((IList)symbol.get("sequence")).toString());
			
		case "opt":
			return new Nonterminal(getSymbol(getSymbolCons(symbol)) + "?");
			
		case "alt":
			return new Nonterminal(getAlt(symbol));
			
		case "conditional":			
			ISet conditions = (ISet) symbol.get("conditions");
			for(IValue condition : conditions) {
				if(((IConstructor)condition).getName().equals("not-follow")) {
					notFollow = (CharacterClass) getSymbol(getSymbolCons((IConstructor) condition));
				}
			}
			return getSymbol(getSymbolCons(symbol));
			
		}
		
		System.out.println(symbol);
		return null;
	}
	
	private IConstructor getSymbolCons(IConstructor symbol) {
		return (IConstructor) symbol.get("symbol");
	}
	
	private String getSymbolName(IConstructor symbol) {
		return ((IString)symbol.get("name")).getValue();
	}
	
	private boolean isEBNF(IConstructor value) {
		// TODO: sequence, alternative, optional, etc.
		return SymbolAdapter.isIterStarSeps(value) ||
			   SymbolAdapter.isIterStar(value) ||
			   SymbolAdapter.isIterPlus(value) ||
			   SymbolAdapter.isIterPlusSeps(value) ||
			   SymbolAdapter.isAlt(value) ||
			   SymbolAdapter.isSeq(value) ||
			   SymbolAdapter.isOpt(value);
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
	
	public String getAlt(IConstructor cons) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		
		ISet alternatives = (ISet) cons.get("alternatives");
		for(IValue alt : alternatives) {
			sb.append(getSymbol((IConstructor) alt)).append(" | ");
		}
		sb.delete(sb.length() - 3, sb.length());
		sb.append(")");
		return sb.toString();
	}
	
	public String getIteratorName(IConstructor iter) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("(");
		
		IConstructor symbol = (IConstructor)iter.get("symbol");
		sb.append(getSymbol(symbol).toString());
		
		IList separators = (IList) iter.get("separators");
		for(IValue separator : separators) {
			// See if adding separators matters for the uniqueness of the names
		}
		
		sb.append(")");
		
		return sb.toString();
	}

}
