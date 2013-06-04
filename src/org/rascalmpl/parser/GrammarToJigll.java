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
import org.jgll.util.GraphVizUtil;
import org.jgll.util.Input;
import org.jgll.util.SPPFToDot;
import org.jgll.util.ToDotWithoutIntermeidateAndLists;
import org.rascalmpl.values.uptr.SymbolAdapter;

public class GrammarToJigll {
	
	private final IValueFactory vf;
	
	private Map<IValue, Rule> rulesMap;
	
	private Grammar grammar;
	
	private GLLParser parser;

	private String startSymbol;
	
	private Input input;
	
	private List<String> deleteSet;
	
	private List<List<List<CharacterClass>>> followRestrictions;
	
	List<List<CharacterClass>> followRestriction;
	
	private IConstructor rascalGrammar;

	public GrammarToJigll(IValueFactory vf) {
		this.vf = vf;
	}

	@SuppressWarnings("unchecked")
	public IConstructor jparse(IConstructor symbol, IString str) {
	  if(grammar == null) {
		  return null;
	  }
	  
	  parser = new LevelSynchronizedGrammarInterpretter();

	  System.out.println("Jigll started.");
	  
	  NonterminalSymbolNode sppf = null;

	  input = Input.fromString(str.getValue());
	  startSymbol = SymbolAdapter.toString(symbol);
	  
	  try {
		sppf = parser.parse(input, this.grammar, startSymbol);
		  
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
	
	public void generateGrammar(IConstructor rascalGrammar) {
		  this.rascalGrammar = rascalGrammar;
		  GrammarBuilder builder = convert("inmemory", rascalGrammar);
		  IMap notAllowed = (IMap) ((IMap) rascalGrammar.get("about")).get(vf.string("notAllowed"));
		  applyRestrictions(builder, notAllowed);
		  builder.filter();
		  
		  grammar = builder.build();
		  
//		  System.out.println(grammar);
	}
	
	public void generateGraph() {
		parser = new LevelSynchronizedGrammarInterpretter();
		NonterminalSymbolNode sppf = parser.parse(input, this.grammar, startSymbol);
		if(sppf == null) {
			return;
		}
		SPPFToDot toDot = new ToDotWithoutIntermeidateAndLists();
		sppf.accept(toDot);
		GraphVizUtil.generateGraph(toDot.getString(), "/Users/ali/output", "graph");
	}
	
	private void getDeleteSet(IConstructor nonterminal) {
		
		deleteSet = new ArrayList<>();
		
		IMap definitions = (IMap) rascalGrammar.get("rules");
		IConstructor choice = (IConstructor) definitions.get(nonterminal);
		ISet alts = (ISet) choice.get("alternatives");
		for (IValue alt : alts) {
			IConstructor prod = (IConstructor) alt;
			IList rhs = (IList) prod.get("symbols");
			IConstructor symbol = (IConstructor) rhs.get(0);
			deleteSet.add(((IString)symbol.get("string")).getValue());
		}
	}
	
	private List<CharacterClass> getFollowRestriction(IConstructor nonterminal) {
		List<CharacterClass> list = new ArrayList<>();
		
		switch (nonterminal.getName()) {
		
			case "char-class":
				List<Range> targetRanges = buildRanges(nonterminal);
				list.add(new CharacterClass(targetRanges));
				break;
				
			case "lit":
				IMap definitions = (IMap) rascalGrammar.get("rules");
				IConstructor choice = (IConstructor) definitions.get(nonterminal);
				ISet alts = (ISet) choice.get("alternatives");
				for (IValue alt : alts) {
					IConstructor prod = (IConstructor) alt;
					IList rhs = (IList) prod.get("symbols");
					for (IValue elem : rhs) {
						IConstructor cons = (IConstructor) elem;
						Symbol symbol = getSymbol(cons);
						if(symbol != null) {
							list.add((CharacterClass)symbol);
						}
					}
				}
			 	break;
	
			default:
				throw new IllegalStateException("Should not be here!");
		}
		
		return list;
	}
	
	public GrammarBuilder convert(String name, IConstructor rascalGrammar) {
		
		GrammarBuilder builder = new GrammarBuilder(name);
		
		IMap definitions = (IMap) rascalGrammar.get("rules");
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
				
				followRestrictions = new ArrayList<>();
				deleteSet = new ArrayList<>();
				
				if(!prod.getName().equals("regular")) {
					IList rhs = (IList) prod.get("symbols");
					
					followRestrictions.clear();
					deleteSet.clear();
					List<Symbol> body = getSymbolList(rhs);
										
					Rule rule = new Rule(head, body, object);
					rulesMap.put(prod, rule);
					builder.addRule(rule, followRestrictions, deleteSet);						
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
			followRestriction = new ArrayList<>();
			Symbol symbol = getSymbol(cons);
			if(symbol != null) {
				result.add(symbol);
				followRestrictions.add(followRestriction);
			}
		}
		return result;
	}
	
	
	private Symbol getSymbol(IConstructor symbol) {
		
		switch (symbol.getName()) {
		
			case "char-class":
				List<Range> targetRanges = buildRanges(symbol);
				return new CharacterClass(targetRanges);
				
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
				return new Nonterminal(SymbolAdapter.toString(symbol), true);
				
			case "alt":
				return new Nonterminal(SymbolAdapter.toString(symbol), true);
				
			case "start":
				return new Nonterminal("start[" + SymbolAdapter.toString(getSymbolCons(symbol)) + "]");
				
			case "conditional":			
				ISet conditions = (ISet) symbol.get("conditions");
				for(IValue condition : conditions) {
					if(((IConstructor)condition).getName().equals("not-follow")) {
						IConstructor follow = getSymbolCons((IConstructor) condition);
						followRestriction.add(getFollowRestriction(follow));
					} 
					else if(((IConstructor)condition).getName().equals("delete")) { 
						IConstructor delete = getSymbolCons((IConstructor) condition);
						getDeleteSet(delete);
					}
				}
				return getSymbol(getSymbolCons(symbol));
				
			default:
				return new Nonterminal(SymbolAdapter.toString(symbol));
			}
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
