package org.rascalmpl.parser;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.jgll.grammar.BodyGrammarSlot;
import org.jgll.grammar.CharacterClass;
import org.jgll.grammar.EpsilonGrammarSlot;
import org.jgll.grammar.Grammar;
import org.jgll.grammar.GrammarSlot;
import org.jgll.grammar.LastGrammarSlot;
import org.jgll.grammar.Nonterminal;
import org.jgll.grammar.NonterminalGrammarSlot;
import org.jgll.grammar.Range;
import org.jgll.grammar.Rule;
import org.jgll.grammar.Symbol;
import org.jgll.grammar.Terminal;
import org.jgll.grammar.TerminalGrammarSlot;
import org.jgll.parser.GrammarInterpreter;
import org.jgll.sppf.NonterminalSymbolNode;
import org.jgll.traversal.ModelBuilderVisitor;
import org.rascalmpl.values.uptr.ProductionAdapter;

public class GrammarToJigll {
	private final IValueFactory vf;
  private IMap definitions;
  private IMap notAllowed;
  private Map<ISet,Nonterminal> restrictedNonterminals;
  private Map<IValue, Nonterminal> nonterminals;
  private List<BodyGrammarSlot> slots;


  public GrammarToJigll(IValueFactory vf) {
	  this.vf = vf;
	}

	public IConstructor jparse(IValue type, IConstructor symbol, IConstructor grammar, IString str) {
	  
	  Grammar g = convert("inmemory", grammar);
	  GrammarInterpreter parser = new GrammarInterpreter();
	
	  System.out.println("Jigll started.");
	  NonterminalSymbolNode parse = parser.parse(str.getValue(), g, symbol.toString());
	  long start = System.nanoTime();
	  parse.accept(new ModelBuilderVisitor<>(new ParsetreeBuilder()));
	  long end = System.nanoTime();
	  System.out.println("Flattening: " + (end - start) / 1000000);
	  return parse.<IConstructor>getResult().getObject();
	}
	
	public void generate(IString name, IConstructor grammar) {

		Grammar g = convert(name.getValue(), grammar);

		try (StringWriter out = new StringWriter()) {
			g.code(out, "test");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

  public Grammar convert(String name, IConstructor grammar) {
	  notAllowed = (IMap) ((IMap) grammar.get("about")).get(vf.string("notAllowed"));
		definitions = (IMap) grammar.get("rules");
		nonterminals = new HashMap<IValue, Nonterminal>();
		restrictedNonterminals = new HashMap<ISet, Nonterminal>();
		slots = new ArrayList<>();

		// pre-allocated a nonterminal object for every non-terminal
		for (IValue nonterminal : definitions) {
			nonterminals.put(nonterminal, new Nonterminal(nonterminals.size(), nonterminal.toString(), false));
		}
		
		// reserve a non-terminal object for all the grammar positions restricted by priority/associativity/except
		Iterator<IValue> it = notAllowed.valueIterator();
		int restrictedIndex = 0;
		while (it.hasNext()) {
		  ISet set = (ISet) it.next();
      restrictedNonterminals.put(set, new Nonterminal(nonterminals.size() + restrictedNonterminals.size(), 
          "dummy" + Integer.toString(++restrictedIndex), false));
		}

		for (IValue nonterminal : definitions) {
			IConstructor choice = (IConstructor) definitions.get(nonterminal);
			convertNonterminal(nonterminal, choice, vf.set());
		}
		
		Set<Nonterminal> startSymbols = new HashSet<>();
		for (IValue st : (ISet) grammar.get("starts")) {
			startSymbols.add(nonterminals.get(st));
		}

		return new Grammar(name, new ArrayList<>(nonterminals.values()), slots, startSymbols);
	}

	private void convertNonterminal(IValue nonterminal, IConstructor choice, ISet ignoreThese) {
		assert choice.getName().equals("choice");
		Nonterminal head = nonterminals.get(nonterminal);
		generateSlotsForHead(choice, ignoreThese, head);
	}

  protected void generateSlotsForHead(IConstructor choice, ISet ignoreThese, Nonterminal head) {
    ISet alts = (ISet) choice.get("alternatives");

		for (IValue alt : alts) {
			IConstructor prod = (IConstructor) alt;
			
			if (!ignoreThese.contains(prod)) {
			  convertProduction(head, prod);
			}
		}
  }

	private void convertProduction(Nonterminal head, IConstructor prod) {
		assert prod.getName().equals("prod");
		BodyGrammarSlot slot = null;

		IList rhs = (IList) prod.get("symbols");
		List<Symbol> body = getSymbolList(rhs, nonterminals);
		Rule rule = new Rule(head, body);

		if (rhs.length() == 0) { // epsilon
			convertEpsilonProduction(rule, slot, prod);
		} else {
			convertNonEpsilonProduction(rule, slot, prod);
		}
	}

	private void convertNonEpsilonProduction(Rule rule, BodyGrammarSlot slot, IConstructor prod) {
		int index = 0;
		for (Symbol s : rule.getBody()) {
			if(s instanceof Terminal) {
				slot = new TerminalGrammarSlot(rule, slots.size() + nonterminals.size(), index, slot, (Terminal) s);
			} else {
				// TODO: plug the actual test set here.
			  ISet notAllowedSet = (ISet) notAllowed.get(vf.tuple(prod, vf.integer(index)));
			  
			  if (notAllowedSet == null) {
			    slot = new NonterminalGrammarSlot(rule, slots.size() + nonterminals.size(), index, slot, (Nonterminal) s, new HashSet<Terminal>());
			  }
			  else {
			    // TODO: watch out for these casts..
			    slot = (BodyGrammarSlot) generateRestrictedNonterminal(prod, index, notAllowedSet, rule, (BodyGrammarSlot) slot);
			   
			  }
			}
			
			slots.add(slot);			

			if (index == 0) {
				rule.getHead().addAlternate(slot);
			}
			index++;
		}
		slots.add(new LastGrammarSlot(rule, slots.size() + nonterminals.size(), rule.getBody().size(), slot, prod));
	}

	private GrammarSlot generateRestrictedNonterminal(IConstructor prod, int index, ISet notAllowedSet, Rule rule, BodyGrammarSlot previous) {
	  // make sure not to generate again
	  Nonterminal cached = restrictedNonterminals.get(notAllowedSet);
    if (cached != null) {
	    return cached;
	  }
	  
    IConstructor sym = (IConstructor) ProductionAdapter.getSymbols(prod).get(index);
    Nonterminal restrictedNt = new Nonterminal(nonterminals.size() + restrictedNonterminals.size(), "dummy" + Integer.toString(restrictedNonterminals.size()), false);
    restrictedNonterminals.put(notAllowedSet, restrictedNt);
    
    generateSlotsForHead((IConstructor) definitions.get(sym), notAllowedSet, restrictedNt);
    return new NonterminalGrammarSlot(rule, slots.size() + nonterminals.size(), index, previous, restrictedNt, new HashSet<Terminal>());
  }

  private void convertEpsilonProduction(Rule rule, BodyGrammarSlot slot, IConstructor prod) {
		slot = new EpsilonGrammarSlot(rule, slots.size() + nonterminals.size(), 0, slot, new HashSet<Terminal>(), prod);
		slots.add(slot);
		rule.getHead().addAlternate(slot);
	}

	static private List<Range> buildRanges(IConstructor symbol) {
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
	
	
	private static List<Symbol> getSymbolList(IList rhs, Map<IValue, Nonterminal> nonterminals) {
		List<Symbol> result = new ArrayList<>();
		for (IValue elem : rhs) {
			IConstructor symbol = (IConstructor) elem;
			switch (symbol.getName()) {			
				case "char-class":
					List<Range> targetRanges = buildRanges(symbol);
					result.add(new CharacterClass(targetRanges));
					break;
					
				default:
					result.add(nonterminals.get(symbol));
			}
		}
		return result;
	}

}
