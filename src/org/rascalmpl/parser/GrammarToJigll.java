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
import org.jgll.grammar.CharacterClass;
import org.jgll.grammar.Grammar;
import org.jgll.grammar.GrammarInterpreter;
import org.jgll.grammar.LevelSynchronizedGrammarInterpretter;
import org.jgll.grammar.Nonterminal;
import org.jgll.grammar.Range;
import org.jgll.grammar.Rule;
import org.jgll.grammar.Symbol;
import org.jgll.sppf.NonterminalSymbolNode;
import org.jgll.traversal.ModelBuilderVisitor;

public class GrammarToJigll {
	
	private final IValueFactory vf;
	
	private Map<IValue, Rule> rulesMap;
	
	public GrammarToJigll(IValueFactory vf) {
		this.vf = vf;
	}

	public IConstructor jparse(IValue type, IConstructor symbol, IConstructor grammar, IString str) {
	  Grammar g = convert("inmemory", grammar);
	  
	  IMap notAllowed = (IMap) ((IMap) grammar.get("about")).get(vf.string("notAllowed"));
	  applyRestrictions(g, notAllowed);
	  
	  GrammarInterpreter parser = new LevelSynchronizedGrammarInterpretter();
	
	  System.out.println("Jigll started.");
	  NonterminalSymbolNode parse = parser.parse(str.getValue(), g, symbol.toString());
	  long start = System.nanoTime();
	  parse.accept(new ModelBuilderVisitor<>(new ParsetreeBuilder()), null);
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
		IMap definitions = (IMap) grammar.get("rules");
		List<Rule> rules = new ArrayList<>();
		rulesMap = new HashMap<>();

		for (IValue nonterminal : definitions) {
			Nonterminal head = new Nonterminal(nonterminal.toString());
			IConstructor choice = (IConstructor) definitions.get(nonterminal);
			assert choice.getName().equals("choice");
			ISet alts = (ISet) choice.get("alternatives");
			
			for (IValue alt : alts) {
				IConstructor prod = (IConstructor) alt;
				IList rhs = (IList) prod.get("symbols");
				List<Symbol> body = getSymbolList(rhs);
				Rule rule = new Rule(head, body, prod);
				rulesMap.put(prod, rule);
				rules.add(rule);
			}
		}

		return Grammar.fromRules(name, rules);
	}
	
	private void applyRestrictions(Grammar grammar, IMap notAllowed) {
		Iterator<Entry<IValue, IValue>> it = notAllowed.entryIterator();
		while(it.hasNext()) {
			Entry<IValue, IValue> next = it.next();
			
			// Tuple(prod, position)
			ITuple key = (ITuple) next.getKey();
			ISet set = (ISet) next.getValue();
			
			Rule rule = (Rule) rulesMap.get(key.get(0));
			int position = ((IInteger) key.get(1)).intValue();
				
			Set<Rule> filterList = new HashSet<>();
			Iterator<IValue> iterator = set.iterator();
			while(iterator.hasNext()) {
				filterList.add(rulesMap.get(iterator.next()));
			}
			grammar.filter(rule, position, filterList);
		}		
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
	
	
	private static List<Symbol> getSymbolList(IList rhs) {
		List<Symbol> result = new ArrayList<>();
		for (IValue elem : rhs) {
			IConstructor symbol = (IConstructor) elem;
			switch (symbol.getName()) {			
				case "char-class":
					List<Range> targetRanges = buildRanges(symbol);
					result.add(new CharacterClass(targetRanges));
					break;
					
				default:
					result.add(new Nonterminal(symbol.toString()));
			}
		}
		return result;
	}

}
