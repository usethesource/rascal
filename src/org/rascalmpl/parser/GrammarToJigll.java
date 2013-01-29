package org.rascalmpl.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.jgll.grammar.Character;
import org.jgll.grammar.CharacterClass;
import org.jgll.grammar.Grammar;
import org.jgll.grammar.GrammarSlot;
import org.jgll.grammar.LastGrammarSlot;
import org.jgll.grammar.Nonterminal;
import org.jgll.grammar.NonterminalGrammarSlot;
import org.jgll.grammar.Range;
import org.jgll.grammar.TerminalGrammarSlot;

public class GrammarToJigll {
  public GrammarToJigll(IValueFactory vf) {
  }
  
  public void generate(IString name, IConstructor grammar) {
    IConstructor start = null;
    for (IValue st : (ISet) grammar.get("starts")) {
      start = (IConstructor) st;
    }
    
    Grammar g = convert(name.getValue(), start, grammar);
    
    for (Nonterminal n : g.getNonterminals()) {
      System.err.println(n.code());
    }
  }
  
  static public Grammar convert(String name, IConstructor start, IConstructor grammar) {
    IMap definitions = (IMap) grammar.get("rules");
    Map<IValue, Nonterminal> nonterminals = new HashMap<IValue, Nonterminal>();
    List<GrammarSlot> slots = new ArrayList<>();

    int id = 0;
    for (IValue nonterminal : definitions) {
      nonterminals.put(nonterminal, new Nonterminal(id, nonterminal.toString(), false));
    }

    for (IValue nonterminal : definitions) {
      IConstructor choice = (IConstructor) definitions.get(nonterminal);
      convertNonterminal(nonterminals, slots, nonterminal, choice);
    }

    return new Grammar(name, new ArrayList<>(nonterminals.values()), slots, nonterminals.get(start));
  }

  static private void convertNonterminal(Map<IValue, Nonterminal> nonterminals, List<GrammarSlot> slots, IValue nonterminal,
      IConstructor choice) {
    assert choice.getName().equals("choice");
    Nonterminal head = nonterminals.get(nonterminal);
    ISet alts = (ISet) choice.get("alternatives");

    for (IValue alt : alts) {
      IConstructor prod = (IConstructor) alt;
      convertProduction(nonterminals, slots, head, prod);
    }
  }

  static private void convertProduction(Map<IValue, Nonterminal> nonterminals, List<GrammarSlot> slots, Nonterminal head,
      IConstructor prod) {
    assert prod.getName().equals("prod");
    GrammarSlot slot = null;

    IList rhs = (IList) prod.get("symbols");

    if (rhs.length() == 0) { // epsilon
      convertEpsilonProduction(nonterminals, slots, head, prod, slot);
    } else {
      convertNonEpsilonProduction(nonterminals, slots, head, prod, slot, rhs);
    }
  }

  static  private void convertNonEpsilonProduction(Map<IValue, Nonterminal> nonterminals, List<GrammarSlot> slots,
      Nonterminal head, IConstructor prod, GrammarSlot slot, IList rhs) {
    int symId = 0;
    for (IValue elem : rhs) {
      IConstructor symbol = (IConstructor) elem;
      switch (symbol.getName()) {
      case "char-class":
        List<Range> targetRanges = buildRanges(symbol);
        slot = new TerminalGrammarSlot(slots.size() + nonterminals.size(), prod.toString(), symId++, slot,
            new CharacterClass(targetRanges));
      default:
        slot = new NonterminalGrammarSlot(slots.size() + nonterminals.size(), prod.toString(), symId++, slot,
            nonterminals.get(symbol), null);
      }

      if (symId == 0) {
        slots.add(slot);
        head.addAlternate(slot);
      }

      slots.add(new LastGrammarSlot(slots.size() + nonterminals.size(), prod.toString(), rhs.length(), head, slot));
    }
  }

  static private void convertEpsilonProduction(Map<IValue, Nonterminal> nonterminals, List<GrammarSlot> slots,
      Nonterminal head, IConstructor prod, GrammarSlot slot) {
    slot = new TerminalGrammarSlot(slots.size() + nonterminals.size(), prod.toString(), 0, slot, new Character(
        (char) -1));
    slots.add(slot);
    head.addAlternate(slot);
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

}
