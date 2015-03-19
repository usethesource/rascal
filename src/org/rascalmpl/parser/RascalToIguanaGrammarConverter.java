/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Ali Afroozeh - Ali.Afroozeh@cwi.nl - CWI
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Anastasia Izmaylova - Anastasia.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;

import static org.jgll.datadependent.ast.AST.*;

import org.jgll.datadependent.ast.AbstractAST;
import org.jgll.grammar.Grammar;
import org.jgll.grammar.condition.Condition;
import org.jgll.grammar.condition.ConditionType;
import org.jgll.grammar.condition.PositionalCondition;
import org.jgll.grammar.condition.RegularExpressionCondition;
import org.jgll.grammar.patterns.ExceptPattern;
import org.jgll.grammar.patterns.PrecedencePattern;
import org.jgll.grammar.symbol.Align;
import org.jgll.grammar.symbol.Associativity;
import org.jgll.grammar.symbol.AssociativityGroup;
import org.jgll.grammar.symbol.Block;
import org.jgll.grammar.symbol.Character;
import org.jgll.grammar.symbol.CharacterRange;
import org.jgll.grammar.symbol.Code;
import org.jgll.grammar.symbol.Conditional;
import org.jgll.grammar.symbol.EOF;
import org.jgll.grammar.symbol.Epsilon;
import org.jgll.grammar.symbol.IfThen;
import org.jgll.grammar.symbol.IfThenElse;
import org.jgll.grammar.symbol.LayoutStrategy;
import org.jgll.grammar.symbol.Nonterminal;
import org.jgll.grammar.symbol.Offside;
import org.jgll.grammar.symbol.PrecedenceLevel;
import org.jgll.grammar.symbol.Recursion;
import org.jgll.grammar.symbol.Rule;
import org.jgll.grammar.symbol.Symbol;
import org.jgll.grammar.symbol.Terminal;
import org.jgll.grammar.symbol.While;
import org.jgll.regex.Alt;
import org.jgll.regex.Opt;
import org.jgll.regex.Plus;
import org.jgll.regex.RegularExpression;
import org.jgll.regex.Sequence;
import org.jgll.regex.Star;
import org.jgll.traversal.ISymbolVisitor;
import org.jgll.util.CollectionsUtil;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Expression.LessThanOrEq;
import org.rascalmpl.ast.IntegerLiteral;
import org.rascalmpl.ast.Literal;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.BooleanLiteral.Lexical;
import org.rascalmpl.ast.Expression.CallOrTree;
import org.rascalmpl.ast.Expression.Equals;
import org.rascalmpl.ast.Expression.FieldAccess;
import org.rascalmpl.ast.Expression.GreaterThan;
import org.rascalmpl.ast.Expression.GreaterThanOrEq;
import org.rascalmpl.ast.Expression.LessThan;
import org.rascalmpl.ast.Expression.Or;
import org.rascalmpl.ast.Expression.QualifiedName;
import org.rascalmpl.ast.Literal.Integer;
import org.rascalmpl.ast.LocalVariableDeclaration.Default;
import org.rascalmpl.ast.Statement.Assignment;
import org.rascalmpl.ast.Statement.VariableDeclaration;
import org.rascalmpl.ast.StringLiteral.NonInterpolated;
import org.rascalmpl.ast.Variable.Initialized;
import org.rascalmpl.ast.Variable.UnInitialized;
import org.rascalmpl.ast.Variable;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class RascalToIguanaGrammarConverter {
	
	private Map<IValue, Rule> rulesMap;
	
	private Nonterminal layout;

	public Grammar convert(String name, IConstructor rascalGrammar) {

		Grammar.Builder builder = new Grammar.Builder();
		
		IMap definitions = (IMap) rascalGrammar.get("rules");
		
		rulesMap = new HashMap<>();
		
		layout = getLayoutNonterminal(rascalGrammar);
		
		for (IValue nonterminal : definitions) {

			IConstructor constructor = (IConstructor) nonterminal;
			
			switch (constructor.getName()) {
				case "empty":
					break;

				case "start":	
					builder.addRules(getAlternatives(getSymbolCons(constructor), definitions, LayoutStrategy.INHERITED));
					break;
					
				case "layouts":
				case "lex":
				case "token":
				case "keywords":
					builder.addRules(getAlternatives(nonterminal, definitions, LayoutStrategy.NO_LAYOUT));
					break;
					
				default:
					builder.addRules(getAlternatives(nonterminal, definitions, LayoutStrategy.INHERITED));
					break;
 			}
		}
		
		List<PrecedencePattern> precedencePatterns = getPrecedencePatterns((IMap) rascalGrammar.asWithKeywordParameters().getParameter("notAllowed"));
		List<ExceptPattern> exceptPatterns = getExceptPatterns((IMap) rascalGrammar.asWithKeywordParameters().getParameter("excepts"));
	
		List<List<PrecedencePattern>> split = CollectionsUtil.split(precedencePatterns, 300);
		
		System.out.println("public static List<PrecedencePattern> precedencePatterns() {");
		System.out.println("   List<PrecedencePattern> list = new ArrayList<>();");
		for (int j = 0; j < split.size(); j++) {
			System.out.println("   list.addAll(precedencePatterns" + (j + 1) + "());");
		}
		System.out.println("   return list;");
		System.out.println("}");
		
		int i = 0;
		for (List<PrecedencePattern> l : split) {
			System.out.println("private static List<PrecedencePattern> precedencePatterns" + ++i + "() {");
			System.out.println("  return Arrays.asList(");
			l.forEach(p -> {
				System.out.println("  // " + p);
				System.out.println("     " + p.getConstructorCode() + ", ");	
			});
			System.out.println(");");
			System.out.println("}");
		}
		
		System.out.println("public static List<ExceptPattern> exceptPatterns() {");
		System.out.println("   return Arrays.asList(");
		exceptPatterns.forEach(p -> {
			System.out.println("   // " + p);
			System.out.println("   " + p.getConstructorCode() + ", ");
		});
		System.out.println(");");
		System.out.println("}");

		
		return builder.setLayout(layout).build();
	}
	
	public Nonterminal getLayoutNonterminal(IConstructor rascalGrammar) {
		IMap definitions = (IMap) rascalGrammar.get("rules");
		
		List<String> layoutNonterminals = new ArrayList<>();
		
		for (IValue nonterminal : definitions) {
			IConstructor constructor = (IConstructor) nonterminal;
			if (constructor.getName().equals("layouts")) {
				layoutNonterminals.add(getName(constructor));
			}
		}
		
		List<String> layouts = layoutNonterminals.stream().filter(s -> !s.equals("$default$")).collect(Collectors.toList());
		
		return layouts.isEmpty() ? null : Nonterminal.withName(layouts.get(0)); 
	}
	
	private PrecedenceLevel level;
	
	private List<Rule> getAlternatives(IValue nonterminal, IMap definitions, LayoutStrategy strategy) {
		
		List<Rule> rules = new ArrayList<>();
		
		Nonterminal head = Nonterminal.withName(getName((IConstructor) nonterminal));
		
		IConstructor choice = (IConstructor) definitions.get(nonterminal);
		assert choice.getName().equals("choice");
		
		level = PrecedenceLevel.getFirst();
		getAlternatives(head, choice, strategy, rules, true);
		
		return rules;
	}
	
	private void getAlternatives(Nonterminal head, IConstructor production, LayoutStrategy strategy, List<Rule> rules, boolean isRoot) {
		
		switch (production.getName()) {
			
			case "priority":
				IList choices = (IList) production.get("choices");
			
				for (IValue choice : choices.reverse()) {
					
					IConstructor alt = (IConstructor) choice;
					
					switch(alt.getName()) {
					
						case "choice":
							getAlternatives(head, alt, strategy, rules, false);
							level.setUndefinedIfNeeded();
							break;
							
						case "associativity":
							ISet alternatives = (ISet) alt.get("alternatives");			
							associativity2Rules(head, alternatives, getAssociativity((IConstructor) alt.get("assoc")), strategy, rules);
							break;
							
						case "prod":
							Rule rule = prod2Rule(head, alt, strategy);
							int precedence = level.getPrecedence(rule);
							
							if (precedence != -1)
								rule = rule.copyBuilder().setPrecedence(precedence).setPrecedenceLevel(level).build();
							
							rulesMap.put(alt, rule);
							rules.add(rule);
							
							break;
							
						default: throw new RuntimeException("Unexpected type of a production: " + alt.getName());
					}
					
					level = level.getNext();
				}
				
				break;
				
			case "choice":
				ISet alternatives = (ISet) production.get("alternatives");
				
				for (IValue alternative : alternatives) {
					
					IConstructor alt = (IConstructor) alternative;
					
					switch(alt.getName()) {
					
						case "priority": // Should only happen at the root
							assert isRoot;
							getAlternatives(head, alt, strategy, rules, false);
							level.setUndefinedIfNeeded();
							break;
						
						case "associativity":
							ISet alts = (ISet) alt.get("alternatives");
							associativity2Rules(head, alts, getAssociativity((IConstructor) alt.get("assoc")), strategy, rules);
							break;
							
						case "prod":
							Rule rule = prod2Rule(head, alt, strategy);
							int precedence = level.getPrecedence(rule);
							
							if (precedence != -1)
								rule = rule.copyBuilder().setPrecedence(precedence).setPrecedenceLevel(level).build();
							
							rulesMap.put(alt, rule);
							rules.add(rule);
							break;
							
						default: throw new RuntimeException("Unexpected type of a production: " + alt.getName());
					}
					
				}
				
				if (isRoot) level.done();
				
				break;
				
			default: throw new RuntimeException("Unexpected type of a production: " + production.getName());
			
		}
	}
	
	private void associativity2Rules(Nonterminal head, ISet alternatives, Associativity associativity, LayoutStrategy strategy, List<Rule> rules) {
		
		AssociativityGroup assocGroup = new AssociativityGroup(associativity, level);
		
		for (IValue alternative : alternatives) {
			
			IConstructor alt = (IConstructor) alternative;
			
			Rule rule = prod2Rule(head, alt, strategy);
			int precedence = assocGroup.getPrecedence(rule);
			
			if (precedence != -1)
				rule = rule.copyBuilder().setPrecedence(precedence).setPrecedenceLevel(level).setAssociativityGroup(assocGroup).build();
			
			rulesMap.put(alt, rule);
			rules.add(rule);
			
		}
		
		assocGroup.done();
		level.containsAssociativityGroup(assocGroup.getLhs(), assocGroup.getRhs());
				
	}
	
	private Rule prod2Rule(Nonterminal head, IConstructor production, LayoutStrategy strategy) {
		assert production.getName().equals("prod");
		
		SerializableValue object = null;
		
		IList rhs = (IList) production.get("symbols");
		ISet attributes = (ISet) production.get("attributes");
		
		Associativity associativity = getAssociativity(attributes);
		
		List<Symbol> body = getSymbolList(rhs);
		
		boolean isLeft = body.size() == 0? false : body.get(0).accept(new IsRecursive(head, Recursion.LEFT_REC));
		boolean isRight = body.size() == 0? false : body.get(body.size() - 1).accept(new IsRecursive(head, Recursion.RIGHT_REC));
		
		Recursion recursion = Recursion.NON_REC;
		int precedence = -1;
		
		if (isLeft && isRight)
			recursion = Recursion.LEFT_RIGHT_REC;
		else if (isLeft)
			recursion = Recursion.LEFT_REC;
		else if (isRight)
			recursion = Recursion.RIGHT_REC;
		
		if (recursion == Recursion.NON_REC)
				associativity = Associativity.UNDEFINED;
		
		if ((recursion == Recursion.LEFT_REC || recursion == Recursion.RIGHT_REC)
						&& associativity != Associativity.NON_ASSOC) 
				associativity = Associativity.UNDEFINED;
			
		return Rule.withHead(head).addSymbols(body).setObject(object).setLayoutStrategy(strategy)
									.setRecursion(recursion)
									.setAssociativity(associativity)
									.setPrecedence(precedence)
									.setPrecedenceLevel(level)
									.setLabel(addLabel(production)).build();
	}

	private List<PrecedencePattern> getPrecedencePatterns(IMap notAllowed) {
		
		List<PrecedencePattern> precedencePatterns = new ArrayList<>();

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
				precedencePatterns.add(PrecedencePattern.from(rule, position, rulesMap.get(iterator.next())));
			}
		}
		
		return precedencePatterns;
	}	
	
	private List<ExceptPattern> getExceptPatterns(IMap map) {

		List<ExceptPattern> exceptPatterns = new ArrayList<>();
		
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
				exceptPatterns.add(ExceptPattern.from(rule, position, rulesMap.get(iterator.next())));
			}
		}
		
		return exceptPatterns;
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
				Alt<CharacterRange> charClass = getCharacterClass(symbol);
				return charClass.isSingleChar() ? charClass.asSingleChar() : charClass;
				
			case "lit":
				Sequence<Character> keyword = Sequence.from(getString(symbol));
				return keyword.isSingleChar() ? keyword.asSingleChar() : Terminal.from(keyword);
	
			case "label":
				return getSymbol(getSymbolCons(symbol)).copyBuilder().setLabel(getLabel(symbol)).build();
	
			case "iter":
				return Plus.from(getSymbol(getSymbolCons(symbol)));
	
			case "iter-seps":
				return Plus.builder(getSymbol(getSymbolCons(symbol))).addSeparators(getSymbolList(getSeparators(symbol))).build();
	
			case "iter-star":
				return Star.from(getSymbol(getSymbolCons(symbol)));
	
			case "iter-star-seps":
				return Star.builder(getSymbol(getSymbolCons(symbol))).addSeparators(getSymbolList(getSeparators(symbol))).build();
	
			case "opt":
				return Opt.from(getSymbol(getSymbolCons(symbol)));
	
			case "alt":
				return Alt.from(getSymbolList(getAlternatives(symbol)));
	
			case "seq":
				return Sequence.from(getSymbolList(getSymbols(symbol)));
	
			case "start":
				return Nonterminal.withName("start[" + SymbolAdapter.toString(getSymbolCons(symbol), true) + "]");
	
			case "conditional":
				Symbol sym = getSymbol(getSymbolCons(symbol));
				if (sym instanceof Nonterminal)
					return ((Nonterminal) sym).copyBuilder().addExcepts(getExcepts(symbol))
												.addPreConditions(getPreConditions(symbol))
												.addPostConditions(getPostConditions(symbol))
												.build();
				
				return sym.copyBuilder()
							.addPreConditions(getPreConditions(symbol))
							.addPostConditions(getPostConditions(symbol))
							.build();
				
			case "empty":
				return Epsilon.getInstance();
				
			case "token":
				return Nonterminal.withName(getName(symbol));

			case "layouts":
				String name = getName(symbol);
				if (name.equals(layout.getName()))
					return layout;
				else 
					return null;
				
			// DD part:
				
			case "scope":
				return Block.block(getSymbolList((IList)symbol.get("symbols")).stream().toArray(Symbol[]::new));
				
			case "if":
				Expression condition = buildExpression(getCondition(symbol));
				return IfThen.ifThen((org.jgll.datadependent.ast.Expression) condition.accept(new Visitor()), getSymbol(getSymbolCons(symbol)));
				
			case "ifElse":
				condition = buildExpression(getCondition(symbol));
				return IfThenElse.ifThenElse((org.jgll.datadependent.ast.Expression) condition.accept(new Visitor()), getSymbol(getThenPart(symbol)), getSymbol(getElsePart(symbol)));
				
			case "when":
				condition = buildExpression(getCondition(symbol));
				return Conditional.when(getSymbol(getSymbolCons(symbol)), (org.jgll.datadependent.ast.Expression) condition.accept(new Visitor()));
			
			case "do":
				Statement block = buildStatement(getBlock(symbol));
				return Code.code(getSymbol(getSymbolCons(symbol)), (org.jgll.datadependent.ast.Statement) block.accept(new Visitor()));
			
			case "while":
				condition = buildExpression(getCondition(symbol));
				return While.whileLoop((org.jgll.datadependent.ast.Expression) condition.accept(new Visitor()), getSymbol(getSymbolCons(symbol)));
				
			case "align":
				return Align.align(getSymbol(getSymbolCons(symbol)));
				
			case "offside":
				return Offside.offside(getSymbol(getSymbolCons(symbol)));
								
			default:
				throw new UnsupportedOperationException(symbol.toString());
		}
	}

	private Alt<CharacterRange> getCharacterClass(IConstructor symbol) {
		return Alt.builder(buildRanges(symbol)).build();
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
					
                case "far-not-follow":
                    set.add(RegularExpressionCondition.notFollowIgnoreLayout((RegularExpression) getSymbol(getSymbolCons((IConstructor) condition))));
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
					set.add(RegularExpressionCondition.notPrecede((RegularExpression) getSymbol(notPrecede)));
					break;
	
				case "start-of-line":
					set.add(new PositionalCondition(ConditionType.START_OF_LINE));
					break;
	
				case "precede":
					IConstructor precede = getSymbolCons((IConstructor) condition);
					set.add(RegularExpressionCondition.precede((RegularExpression) getSymbol(precede)));
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
		return ((IString) symbol.get("name")).getValue();
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
	
	public IList getSeparators(IConstructor symbol) {
		return (IList) symbol.get("separators");
	}
	
	private IConstructor getBlock(IConstructor symbol) {
		return (IConstructor) symbol.get("block");
	}
	
	private IConstructor getCondition(IConstructor symbol) {
		return (IConstructor) symbol.get("condition");
	}
	
	private IConstructor getThenPart(IConstructor symbol) {
		return (IConstructor) symbol.get("ifSymbol");
	}
	
	private IConstructor getElsePart(IConstructor symbol) {
		return (IConstructor) symbol.get("thenSymbol");
	}

	@SuppressWarnings("unused")
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
	
	private static Expression buildExpression(IConstructor condition) {
		if (TreeAdapter.isAppl(condition)) {
			String sortName = TreeAdapter.getSortName(condition);
			if (sortName.equals("Expression")) {
				return (Expression) new ASTBuilder().buildValue(condition);
			}
		} 
		else if (TreeAdapter.isAmb(condition)) {
		    throw new Ambiguous(condition);
		}
		
		throw new ImplementationError("This is not a " + "Expression" +  ": " + condition);
	}
	
	private static Statement buildStatement(IConstructor statement) {
		if (TreeAdapter.isAppl(statement)) {
			String sortName = TreeAdapter.getSortName(statement);
			if (sortName.equals("Statement")) {
				return (Statement) new ASTBuilder().buildValue(statement);
			}
		} 
		else if (TreeAdapter.isAmb(statement)) {
		    throw new Ambiguous(statement);
		}
		
		throw new ImplementationError("This is not a " + "Statement" +  ": " + statement);
	}
	
	private static Associativity getAssociativity(ISet attributes) {
		for (IValue attribute : attributes) {
			if (((IConstructor) attribute).getName().equals("assoc")) {
				return getAssociativity((IConstructor)((IConstructor) attribute).get("assoc"));
			}
		}
		return Associativity.UNDEFINED;
	}
	
	private static Associativity getAssociativity(IConstructor assoc) {
		switch(assoc.getName()) {	
			case "left":
				return Associativity.LEFT;
			
			case "right":
				return Associativity.RIGHT;
			
			case "assoc":
				return Associativity.UNDEFINED;
			
			case "non-assoc":
				return Associativity.NON_ASSOC;
			
			default:
				return Associativity.UNDEFINED;
		}
	}
	
	private static String addLabel(IConstructor production) {
		IConstructor symbol = (IConstructor) production.get("def");
		switch(symbol.getName()) {
			case "label":
				return ((IString) symbol.get("name")).getValue();
			default:
		}
		return null;
	}
	
	private static Set<String> getExcepts(IConstructor symbol) {
		ISet conditions = (ISet) symbol.get("conditions");
		
		Set<String> conds = new HashSet<>();
		
		for (IValue condition : conditions) {
			IConstructor cond = (IConstructor) condition;
			
			switch(cond.getName()) {
				case "except":
					String label = ((IString) cond.get("label")).getValue();
					conds.add(label); 
					break;
				default:
			}
		}
		return conds;
	}
	
	public static class Visitor extends NullASTVisitor<AbstractAST> {
		
		@Override
		public AbstractAST visitStatementAssignment(Assignment x) {
			return stat(assign(x.getAssignable().accept(this).toString(), (org.jgll.datadependent.ast.Expression) x.getStatement().getExpression().accept(this)));
		}
		
		@Override
		public AbstractAST visitAssignableVariable(org.rascalmpl.ast.Assignable.Variable x) {
			return var(Names.name(Names.lastName(x.getQualifiedName())));
		}
		
		@Override
		public AbstractAST visitStatementVariableDeclaration(VariableDeclaration x) {
			return varDeclStat((org.jgll.datadependent.ast.VariableDeclaration) x.getDeclaration().accept(this));
		}
		
		@Override
		public AbstractAST visitLocalVariableDeclarationDefault(Default x) {
			return x.getDeclarator().accept(this);
		}
		
		@Override
		public AbstractAST visitDeclaratorDefault(org.rascalmpl.ast.Declarator.Default x) {
			List<Variable> variables = x.getVariables();
			if (variables.size() != 1) {
				throw new RuntimeException("Declarator with multiple variables is not supported!");
			}
			return variables.get(0).accept(this);
		}
		
		@Override
		public AbstractAST visitVariableInitialized(Initialized x) {
			return varDecl(Names.name(x.getName()), (org.jgll.datadependent.ast.Expression) x.getInitial().accept(this));
		}
		
		@Override
		public AbstractAST visitVariableUnInitialized(UnInitialized x) {
			return varDecl(Names.name(x.getName()));
		}
		
		@Override
		public AbstractAST visitStatementExpression(org.rascalmpl.ast.Statement.Expression x) {
			return stat((org.jgll.datadependent.ast.Expression) x.getExpression().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionCallOrTree(CallOrTree x) {
			
			AbstractAST fun = x.getExpression().accept(this);
			
			if (!(fun instanceof org.jgll.datadependent.ast.Expression.Name)) {
				throw new RuntimeException("Unsupported Rascal expression: " + fun);
			}
			String id = ((org.jgll.datadependent.ast.Expression.Name) fun).getName();
			
			if (!(id.equals("indent") || 
				  id.equals("println") || 
				  id.equals("ppLookup") ||
				  id.equals("endOfInput") ||
				  id.equals("startsWith") ||
				  id.equals("endsWith"))) {
				throw new RuntimeException("Unsupported function: " + id);
			}
			
			List<Expression> arguments = x.getArguments();
			org.jgll.datadependent.ast.Expression[] args = new org.jgll.datadependent.ast.Expression[arguments.size()];
			
			int j = 0;
			for (Expression argument : arguments) {
				args[j] = (org.jgll.datadependent.ast.Expression) argument.accept(this);
				j++;
			}
			
			if (id.equals("indent")) 
				return indent(args[0]);
			else if (id.equals("ppLookup"))
				return ppLookup(args[0]);
			else if (id.equals("endOfInput"))
				return endOfFile(args[0]);
			else if (id.equals("startsWith"))
				return startsWith(args[0], args[1]);
			else if (id.equals("endsWith")) 
				return endsWith(args[0], args[1]);
			else 
				return println(args);
		}
		
		@Override
		public AbstractAST visitExpressionOr(Or x) {
			return or((org.jgll.datadependent.ast.Expression) x.getLhs().accept(this), 
					  (org.jgll.datadependent.ast.Expression) x.getRhs().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionLessThan(LessThan x) {
			return less((org.jgll.datadependent.ast.Expression) x.getLhs().accept(this),
				        (org.jgll.datadependent.ast.Expression) x.getRhs().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionLessThanOrEq(LessThanOrEq x) {
			return lessEq((org.jgll.datadependent.ast.Expression) x.getLhs().accept(this),
				          (org.jgll.datadependent.ast.Expression) x.getRhs().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionGreaterThan(GreaterThan x) {
			return greater((org.jgll.datadependent.ast.Expression) x.getLhs().accept(this),
				           (org.jgll.datadependent.ast.Expression) x.getRhs().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
			return greaterEq((org.jgll.datadependent.ast.Expression) x.getLhs().accept(this),
				             (org.jgll.datadependent.ast.Expression) x.getRhs().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionEquals(Equals x) {
			return equal((org.jgll.datadependent.ast.Expression) x.getLhs().accept(this),
					     (org.jgll.datadependent.ast.Expression) x.getRhs().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionFieldAccess(FieldAccess x) {
			String name = Names.name(x.getField());
			
			Expression expression = x.getExpression();
			
			if (!(expression instanceof QualifiedName))
				throw new RuntimeException("Unsupported expression: " + this);
			
			QualifiedName qname = (QualifiedName) expression;
			
			if (name.equals("lExt"))
				return lExt(Names.name(Names.lastName(qname.getQualifiedName())));
			else if (name.equals("rExt"))
				return rExt(Names.name(Names.lastName(qname.getQualifiedName())));
			else
				throw new RuntimeException("Unsupported expression: " + this);
		}
		
		@Override
		public AbstractAST visitExpressionQualifiedName(QualifiedName x) {
			return var(Names.name(Names.lastName(x.getQualifiedName())));
		}
		
		@Override
		public AbstractAST visitExpressionLiteral(org.rascalmpl.ast.Expression.Literal x) {
			return x.getLiteral().accept(this);
		}
		
		@Override
		public AbstractAST visitLiteralBoolean(Literal.Boolean x) {
		    return x.getBooleanLiteral().accept(this); 
		}
		
		@Override
		public AbstractAST visitLiteralInteger(Integer x) {
			return x.getIntegerLiteral().accept(this);
		}
		
		@Override
		public AbstractAST visitLiteralString(org.rascalmpl.ast.Literal.String x) {
			return x.getStringLiteral().accept(this);
		}
		
		@Override
		public AbstractAST visitBooleanLiteralLexical(Lexical x) {
			return x.getString().equals("true")? TRUE : FALSE;
		}
		
		@Override
		public AbstractAST visitIntegerLiteralDecimalIntegerLiteral(IntegerLiteral.DecimalIntegerLiteral x) {
			return x.getDecimal().accept(this);
		}
		
		@Override
		public AbstractAST visitDecimalIntegerLiteralLexical(org.rascalmpl.ast.DecimalIntegerLiteral.Lexical x) {
			return integer(java.lang.Integer.valueOf(x.getString()));
		}
		
		@Override
		public AbstractAST visitStringLiteralNonInterpolated(NonInterpolated x) {
			return x.getConstant().accept(this);
		}
		
		@Override
		public AbstractAST visitStringConstantLexical(org.rascalmpl.ast.StringConstant.Lexical x) {
			return string(x.getString());
		}
		
	}
	
	private static class IsRecursive implements ISymbolVisitor<Boolean> {
		
		private final Recursion recursion;
		private final Nonterminal head;
		
		public IsRecursive(Nonterminal head, Recursion recursion) {
			this.recursion = recursion;
			this.head = head;
		}

		@Override
		public Boolean visit(Align symbol) {
			return symbol.getSymbol().accept(this);
		}

		@Override
		public Boolean visit(Block symbol) {
			Symbol[] symbols = symbol.getSymbols();
			if (recursion == Recursion.LEFT_REC)
				return symbols[0].accept(this);
			else
				return symbols[symbols.length - 1].accept(this);
		}

		@Override
		public Boolean visit(Character symbol) {
			return false;
		}

		@Override
		public Boolean visit(CharacterRange symbol) {
			return false;
		}

		@Override
		public Boolean visit(Code symbol) {
			return symbol.getSymbol().accept(this);
		}

		@Override
		public Boolean visit(Conditional symbol) {
			return symbol.getSymbol().accept(this);
		}

		@Override
		public Boolean visit(EOF symbol) {
			return false;
		}

		@Override
		public Boolean visit(Epsilon symbol) {
			return false;
		}

		@Override
		public Boolean visit(IfThen symbol) {
			return symbol.getThenPart().accept(this);
		}

		@Override
		public Boolean visit(IfThenElse symbol) {
			return symbol.getThenPart().accept(this)
					|| symbol.getElsePart().accept(this);
		}

		@Override
		public Boolean visit(Nonterminal symbol) {
			return symbol.getName().equals(head.getName())
					&& ((head.getParameters() == null && symbol.getArguments() == null)
							|| (head.getParameters().length == symbol.getArguments().length));
		}

		@Override
		public Boolean visit(Offside symbol) {
			return symbol.getSymbol().accept(this);
		}

		@Override
		public Boolean visit(Terminal symbol) {
			return false;
		}

		@Override
		public Boolean visit(While symbol) {
			return symbol.getBody().accept(this);
		}

		@Override
		public <E extends Symbol> Boolean visit(Alt<E> symbol) {
			return false;
		}

		@Override
		public Boolean visit(Opt symbol) {
			return false;
		}

		@Override
		public Boolean visit(Plus symbol) {
			return false;
		}

		@Override
		public <E extends Symbol> Boolean visit(Sequence<E> symbol) {
			return false;
		}

		@Override
		public Boolean visit(Star symbol) {
			return false;
		}
		
	}
	
}