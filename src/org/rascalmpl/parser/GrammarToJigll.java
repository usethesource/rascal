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
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
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

public class GrammarToJigll {
	
	private IMap definitions;
	private final IValueFactory vf;
	private IMap notAllowed;
	private Map<IValue, Nonterminal> nonterminals;
	
	private Map<IValue, GrammarSlot> slotsMap;
	
	// A map from a prod to its alternate index
	private Map<IValue, Integer> alternatesMap;

	public GrammarToJigll(IValueFactory vf) {
		this.vf = vf;
	}

	public IConstructor jparse(IValue type, IConstructor symbol, IConstructor grammar, IString str) {
	  Grammar g = convert("inmemory", grammar);
	  
	  g = applyRestrictions(g);
	  
	  
	  // TODO calculate the actual lenght of longest terminals
	  g.setLongestTerminalChain(1);
	  
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
		slotsMap = new HashMap<>();
		definitions = (IMap) grammar.get("rules");
		notAllowed = (IMap) ((IMap) grammar.get("about")).get(vf.string("notAllowed"));
		nonterminals = new HashMap<>();
		alternatesMap = new HashMap<>();
		List<BodyGrammarSlot> slots = new ArrayList<>();

		for (IValue nonterminal : definitions) {
			nonterminals.put(nonterminal, new Nonterminal(nonterminals.size(), nonterminal.toString(), false));
		}

		for (IValue nonterminal : definitions) {
			IConstructor choice = (IConstructor) definitions.get(nonterminal);
			convertNonterminal(nonterminals, slots, nonterminal, choice);
		}
		
		Set<Nonterminal> startSymbols = new HashSet<>();
		for (IValue st : (ISet) grammar.get("starts")) {
			startSymbols.add(nonterminals.get(st));
		}

		return new Grammar(name, new ArrayList<>(nonterminals.values()), slots, startSymbols);
	}

	private void convertNonterminal(Map<IValue, Nonterminal> nonterminals, List<BodyGrammarSlot> slots, IValue nonterminal, IConstructor choice) {
		assert choice.getName().equals("choice");
		Nonterminal head = nonterminals.get(nonterminal);
		ISet alts = (ISet) choice.get("alternatives");

		for (IValue alt : alts) {
			IConstructor prod = (IConstructor) alt;
			convertProduction(nonterminals, slots, head, prod);
		}
	}

	private void convertProduction(Map<IValue, Nonterminal> nonterminals, List<BodyGrammarSlot> slots, Nonterminal head, IConstructor prod) {
		assert prod.getName().equals("prod");
		BodyGrammarSlot slot = null;

		IList rhs = (IList) prod.get("symbols");
		List<Symbol> body = getSymbolList(rhs, nonterminals);
		Rule rule = new Rule(head, body);

		if (rhs.length() == 0) { // epsilon
			convertEpsilonProduction(nonterminals, slots, rule, slot, prod);
		} else {
			convertNonEpsilonProduction(nonterminals, slots, rule, slot, prod);
		}
	}

	private void convertNonEpsilonProduction(Map<IValue, Nonterminal> nonterminals, List<BodyGrammarSlot> slots, Rule rule, BodyGrammarSlot slot, IConstructor prod) {
		int index = 0;
		for (Symbol s : rule.getBody()) {
			if(s instanceof Terminal) {
				slot = new TerminalGrammarSlot(rule, slots.size() + nonterminals.size(), index, slot, (Terminal) s);
			} else {
				// TODO: plug the actual test set here.
				slot = new NonterminalGrammarSlot(rule, slots.size() + nonterminals.size(), index, slot, (Nonterminal) s, new HashSet<Terminal>());
			}
			slotsMap.put(vf.tuple(prod, vf.integer(index)), slot);
			slots.add(slot);

			if (index == 0) {
				alternatesMap.put(prod, rule.getHead().getAlternates().size());
				rule.getHead().addAlternate(slot);
			}
			index++;
		}
		slots.add(new LastGrammarSlot(rule, slots.size() + nonterminals.size(), rule.getBody().size(), slot, prod));
		slotsMap.put(vf.tuple(prod, vf.integer(rule.getBody().size())), slot);
	}

	private void convertEpsilonProduction(Map<IValue, Nonterminal> nonterminals, List<BodyGrammarSlot> slots, Rule rule, BodyGrammarSlot slot, IConstructor prod) {
		slot = new EpsilonGrammarSlot(rule, slots.size() + nonterminals.size(), 0, slot, new HashSet<Terminal>(), prod);
		slots.add(slot);
		rule.getHead().addAlternate(slot);
	}
	
	private Grammar applyRestrictions(Grammar grammar) {
		Map<ISet, Nonterminal> restrictedNonterminals = new HashMap<ISet, Nonterminal>();
		
		Iterator<Entry<IValue, IValue>> it = notAllowed.entryIterator();
		while(it.hasNext()) {
			Entry<IValue, IValue> next = it.next();
			ITuple key = (ITuple) next.getKey();
			ISet set = (ISet) next.getValue();
			NonterminalGrammarSlot grammarSlot = (NonterminalGrammarSlot) slotsMap.get(key);
			
			Nonterminal restrictedNonterminal = restrictedNonterminals.get(set);
			if(restrictedNonterminal == null) {
				restrictedNonterminal = new Nonterminal(nonterminals.size() + restrictedNonterminals.size(), "dummy" + Integer.toString(0), false);
				List<BodyGrammarSlot> alternates = grammarSlot.getNonterminal().getAlternates();
				Iterator<IValue> iterator = set.iterator();
				while(iterator.hasNext()) {
					Integer i = alternatesMap.get(iterator.next());
					alternates.remove(i.intValue());
				}
				for(BodyGrammarSlot alt : alternates) {
					restrictedNonterminal.addAlternate(alt);
				}
				restrictedNonterminals.put(set, restrictedNonterminal);	
			}
			
			grammarSlot.setNonterminal(restrictedNonterminal);
		}
		
		List<Nonterminal> list = new ArrayList<>();
		list.addAll(nonterminals.values());
		list.addAll(restrictedNonterminals.values());
		return new Grammar(grammar.getName(), list, grammar.getGrammarSlots(), grammar.getStartSymbols());
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
