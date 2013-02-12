package org.rascalmpl.parser;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	
	public GrammarToJigll(IValueFactory vf) {
	}

	public IConstructor jparse(IConstructor symbol, IConstructor grammar, IString str) {
	  Grammar g = convert("inmemory", grammar);
	  GrammarInterpreter parser = new GrammarInterpreter();

	  NonterminalSymbolNode parse = parser.parse(str.getValue(), g, symbol.toString());
	  parse.accept(new ModelBuilderVisitor<>(new ParsetreeBuilder()));
	  System.out.println((IConstructor) parse.getObject());
	  return (IConstructor) parse.getObject();
	}
	
	public void generate(IString name, IConstructor grammar) {

		Grammar g = convert(name.getValue(), grammar);

		try (StringWriter out = new StringWriter()) {
			g.code(out, "test");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static public Grammar convert(String name, IConstructor grammar) {
		IMap definitions = (IMap) grammar.get("rules");
		Map<IValue, Nonterminal> nonterminals = new HashMap<IValue, Nonterminal>();
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

	static private void convertNonterminal(Map<IValue, Nonterminal> nonterminals, List<BodyGrammarSlot> slots, IValue nonterminal, IConstructor choice) {
		assert choice.getName().equals("choice");
		Nonterminal head = nonterminals.get(nonterminal);
		ISet alts = (ISet) choice.get("alternatives");

		for (IValue alt : alts) {
			IConstructor prod = (IConstructor) alt;
			convertProduction(nonterminals, slots, head, prod);
		}
	}

	static private void convertProduction(Map<IValue, Nonterminal> nonterminals, List<BodyGrammarSlot> slots, Nonterminal head, IConstructor prod) {
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

	static private void convertNonEpsilonProduction(Map<IValue, Nonterminal> nonterminals, List<BodyGrammarSlot> slots, Rule rule, BodyGrammarSlot slot, IConstructor prod) {
		int index = 0;
		for (Symbol s : rule.getBody()) {
			if(s instanceof Terminal) {
				slot = new TerminalGrammarSlot(rule, slots.size() + nonterminals.size(), index, slot, (Terminal) s);
			} else {
				// TODO: plug the actual test set here.
				slot = new NonterminalGrammarSlot(rule, slots.size() + nonterminals.size(), index, slot, (Nonterminal) s, new HashSet<Terminal>());
			}
			slots.add(slot);			

			if (index == 0) {
				rule.getHead().addAlternate(slot);
			}
			index++;
		}
		slots.add(new LastGrammarSlot(rule, slots.size() + nonterminals.size(), rule.getBody().size(), slot, prod));
	}

	static private void convertEpsilonProduction(Map<IValue, Nonterminal> nonterminals, List<BodyGrammarSlot> slots, Rule rule, BodyGrammarSlot slot, IConstructor prod) {
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
