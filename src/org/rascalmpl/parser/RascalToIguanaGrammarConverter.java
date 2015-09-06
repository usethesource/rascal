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

import static org.iguana.datadependent.ast.AST.*;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.iguana.datadependent.ast.AbstractAST;
import org.iguana.grammar.Grammar;
import org.iguana.grammar.condition.Condition;
import org.iguana.grammar.condition.ConditionType;
import org.iguana.grammar.condition.PositionalCondition;
import org.iguana.grammar.condition.RegularExpressionCondition;
import org.iguana.grammar.patterns.ExceptPattern;
import org.iguana.grammar.patterns.PrecedencePattern;
import org.iguana.grammar.symbol.Align;
import org.iguana.grammar.symbol.Associativity;
import org.iguana.grammar.symbol.AssociativityGroup;
import org.iguana.grammar.symbol.Block;
import org.iguana.grammar.symbol.Character;
import org.iguana.grammar.symbol.CharacterRange;
import org.iguana.grammar.symbol.Code;
import org.iguana.grammar.symbol.Conditional;
import org.iguana.grammar.symbol.EOF;
import org.iguana.grammar.symbol.Epsilon;
import org.iguana.grammar.symbol.IfThen;
import org.iguana.grammar.symbol.IfThenElse;
import org.iguana.grammar.symbol.Ignore;
import org.iguana.grammar.symbol.LayoutStrategy;
import org.iguana.grammar.symbol.Nonterminal;
import org.iguana.grammar.symbol.Nonterminal.Builder;
import org.iguana.grammar.symbol.Offside;
import org.iguana.grammar.symbol.PrecedenceLevel;
import org.iguana.grammar.symbol.Recursion;
import org.iguana.grammar.symbol.Return;
import org.iguana.grammar.symbol.Rule;
import org.iguana.grammar.symbol.Symbol;
import org.iguana.grammar.symbol.Terminal;
import org.iguana.grammar.symbol.While;
import org.iguana.grammar.transformation.EBNFToBNF;
import org.iguana.regex.Alt;
import org.iguana.regex.Opt;
import org.iguana.regex.Plus;
import org.iguana.regex.RegularExpression;
import org.iguana.regex.Sequence;
import org.iguana.regex.Star;
import org.iguana.traversal.ISymbolVisitor;
import org.rascalmpl.ast.BooleanLiteral.Lexical;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Expression.And;
import org.rascalmpl.ast.Expression.CallOrTree;
import org.rascalmpl.ast.Expression.Equals;
import org.rascalmpl.ast.Expression.FieldAccess;
import org.rascalmpl.ast.Expression.GreaterThan;
import org.rascalmpl.ast.Expression.GreaterThanOrEq;
import org.rascalmpl.ast.Expression.LessThan;
import org.rascalmpl.ast.Expression.LessThanOrEq;
import org.rascalmpl.ast.Expression.Negation;
import org.rascalmpl.ast.Expression.Or;
import org.rascalmpl.ast.Expression.QualifiedName;
import org.rascalmpl.ast.IntegerLiteral;
import org.rascalmpl.ast.Literal;
import org.rascalmpl.ast.Literal.Integer;
import org.rascalmpl.ast.LocalVariableDeclaration.Default;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.Statement.Assignment;
import org.rascalmpl.ast.Statement.VariableDeclaration;
import org.rascalmpl.ast.StringLiteral.NonInterpolated;
import org.rascalmpl.ast.Variable;
import org.rascalmpl.ast.Variable.Initialized;
import org.rascalmpl.ast.Variable.UnInitialized;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class RascalToIguanaGrammarConverter {
	
	private Map<IValue, Rule> rulesMap;
	
	private Map<IConstructor, Terminal> tokens = new HashMap<>();
	
	private IMap definitions;
	
	private Symbol layout;
	
	private Map<String, Set<String>> leftEnds = new HashMap<>();
	private Map<String, Set<String>> rightEnds = new HashMap<>();
	
	private Set<String> ebnfs = new HashSet<>();

	public Grammar convert(String name, IConstructor rascalGrammar) {

		Grammar.Builder builder = new Grammar.Builder();
		
		definitions = (IMap) rascalGrammar.get("rules");
		
		rulesMap = new HashMap<>();
		
		layout = getLayoutNonterminal(rascalGrammar);
		
		for (IValue nonterminal : definitions) {
			
			IConstructor constructor = (IConstructor) nonterminal;
			
			switch (constructor.getName()) {
				case "empty": 
					break;
				case "start": computeEnds(nonterminal, definitions); 
					break;
				case "token":
				case "layouts":
				case "lex":
				case "keywords":
					break;
				case "sort": computeEnds(nonterminal, definitions);
					break;
				default:
					System.out.println("Unkonwn construct: " + constructor);
					break;
 			}
		}
		
		boolean changed = true;
		while (changed) {
			changed = false;
			for (String head : leftEnds.keySet()) {
				Set<String> ends = leftEnds.get(head);
				int size = ends.size();
				Set<String> delta = new HashSet<>();
				for (String end : ends) {
					Set<String> lefts = leftEnds.get(end);
					if (lefts != null) {
						for (String left : lefts) {
							if (!left.equals(head))
								delta.add(left);
						}
					}
				}
				ends.addAll(delta);
				if (ends.size() != size)
					changed = true;
			}
		}
		
		changed = true;
		while (changed) {
			changed = false;
			for (String head : rightEnds.keySet()) {
				Set<String> ends = rightEnds.get(head);
				int size = ends.size();
				Set<String> delta = new HashSet<>();
				for (String end : ends) {
					Set<String> rights = rightEnds.get(end);
					if (rights != null) {
						for (String right : rights) {
							if (!right.equals(head))
								delta.add(right);
						}
					}
				}
				ends.addAll(delta);
				if (ends.size() != size)
					changed = true;
			}
		}
		
		for (IValue nonterminal : definitions) {

			IConstructor constructor = (IConstructor) nonterminal;
			
			switch (constructor.getName()) {
				case "empty":
					break;

				case "start":	
					builder.addRules(getAlternatives(getSymbolCons(constructor), definitions, LayoutStrategy.INHERITED));
					break;

				case "token":
					tokens.computeIfAbsent(constructor, c -> getTokenDefinition(c, definitions));
					break;
					
				case "layouts":
				case "lex":
				case "keywords":
					builder.addRules(getAlternatives(nonterminal, definitions, LayoutStrategy.NO_LAYOUT));
					break;
					
				case "sort":
					builder.addRules(getAlternatives(nonterminal, definitions, LayoutStrategy.INHERITED));
					break;
				
				default:
					System.out.println("Unkonwn construct: " + constructor);
					break;
 			}
		}
		
		// List<PrecedencePattern> precedencePatterns = getPrecedencePatterns((IMap) rascalGrammar.asWithKeywordParameters().getParameter("notAllowed"));
		// List<ExceptPattern> exceptPatterns = getExceptPatterns((IMap) rascalGrammar.asWithKeywordParameters().getParameter("excepts"));
		List<PrecedencePattern> precedencePatterns = new ArrayList<>();
	    List<ExceptPattern> exceptPatterns = new ArrayList<>();
		builder.addPrecedencePatterns(precedencePatterns);
		builder.addExceptPatterns(exceptPatterns);
		
		Map<String, Set<String>> ebnfLefts = new HashMap<>();
		Map<String, Set<String>> ebnfRights = new HashMap<>();
		
		for (String ebnf : ebnfs) {
			Set<String> set = leftEnds.get(ebnf);
			if (set != null)
				ebnfLefts.put(ebnf, set);
			
			set = rightEnds.get(ebnf);
			if (set != null)
				ebnfRights.put(ebnf, set);
		}
		
		builder.addEBNFl(ebnfLefts);
		builder.addEBNFr(ebnfRights);
		
		return builder.setLayout(layout).build();
	}
	
	public Symbol getLayoutNonterminal(IConstructor rascalGrammar) {
		IMap definitions = (IMap) rascalGrammar.get("rules");
		
		List<Symbol> layouts = new ArrayList<>();
		
		for (IValue nonterminal : definitions) {
			IConstructor constructor = (IConstructor) nonterminal;
			
			switch (constructor.getName()) {
			
				case "layouts":
					if (!getName(constructor).equals("$default$")) 
						layouts.add(Nonterminal.withName(getName(constructor)));	
					break;
	
				case "sort":
				case "lex":
					if (getName(constructor).equals("Layout"))
						layouts.add(Nonterminal.withName(getName(constructor)));
					break;
					
				case "token":
					if (getName(constructor).equals("Layout"))
						layouts.add(getTokenDefinition(constructor, definitions));
					break;
			}
		}
		
		return layouts.isEmpty() ? null : layouts.get(0); 
	}
	
	private PrecedenceLevel level;
	
	private List<Rule> getAlternatives(IValue nonterminal, IMap definitions, LayoutStrategy strategy) {
		
		List<Rule> rules = new ArrayList<>();
		
		Nonterminal head = getHead(nonterminal);
		
		IConstructor choice = (IConstructor) definitions.get(nonterminal);
		assert choice.getName().equals("choice");
		
		level = PrecedenceLevel.getFirst();
		getAlternatives(head, choice, strategy, rules, true);
		
		return rules;
	}
	
	private Nonterminal getHead(IValue nonterminal) {
		
		Nonterminal head = Nonterminal.withName(getName((IConstructor) nonterminal));
		IList formals = (IList) nonterminal.asWithKeywordParameters().getParameters().get("formals");
		
		if (formals != null) {
			Builder builder = Nonterminal.builder(head);
			String[] parameters = new String[formals.length()];
			int i = 0;
			for (IValue formal : formals) {
				if (formal instanceof IConstructor) {
					IConstructor symbol = (IConstructor) formal;
					if (symbol.getName() == "label")
						parameters[i++] = getLabel(symbol);
				} else {
					throw new RuntimeException("Unexpected type of a formal parameter: " + formal);
				}
			}
			head = builder.addParameters(parameters).build();
		}
		
		return head;
	}
	
	private Terminal getTokenDefinition(IConstructor constructor, IMap definitions) {
		IConstructor choice = (IConstructor) definitions.get(constructor);
		ISet alts = (ISet) choice.get("alternatives");
		IConstructor alt = (IConstructor) alts.iterator().next();
		List<Symbol> symbols = getSymbolList((IList) alt.get("symbols"));

		if (symbols.size() == 1)
			return Terminal.builder((RegularExpression) symbols.get(0)).setName(getName(constructor)).build();
		else 
			return Terminal.builder(Sequence.from(symbols)).setName(getName(constructor)).build();
	}
	
	private List<Rule> computeEnds(IValue nonterminal, IMap definitions) {
		
		List<Rule> rules = new ArrayList<>();
		
		Nonterminal head = getHead(nonterminal);
		
		IConstructor choice = (IConstructor) definitions.get(nonterminal);
		assert choice.getName().equals("choice");
		
		computeEnds(head, choice);
		
		return rules;
	}
	
	private void computeEnds(Nonterminal head, IConstructor production) {
		
		switch (production.getName()) {
			
			case "priority":
				IList choices = (IList) production.get("choices");
			
				for (IValue choice : choices.reverse()) {
					
					IConstructor alt = (IConstructor) choice;
					
					switch(alt.getName()) {
					
						case "choice":
							computeEnds(head, alt);
							break;
							
						case "associativity":
							ISet alternatives = (ISet) alt.get("alternatives");
							for (IValue alternative : alternatives)
								computeEnds(head, (IConstructor) alternative);
							break;
							
						case "prod":
							computeEnds(head, (IList) alt.get("symbols"));
							break;
							
						default: throw new RuntimeException("Unexpected type of a production: " + alt.getName());
					}
				}
				
				break;
				
			case "choice":
				ISet alternatives = (ISet) production.get("alternatives");
				
				for (IValue alternative : alternatives) {
					
					IConstructor alt = (IConstructor) alternative;
					
					switch(alt.getName()) {
					
						case "priority": // Should only happen at the root
							computeEnds(head, alt); break;
						
						case "associativity":
							ISet alts = (ISet) alt.get("alternatives");
							for (IValue a : alts)
								computeEnds(head, (IConstructor) a);
							break;
							
						case "prod":
							computeEnds(head, (IList) alt.get("symbols"));
							break;
							
						default: throw new RuntimeException("Unexpected type of a production: " + alt.getName());
					}
					
				}
				
				break;
				
			case "prod": 
				computeEnds(head, (IList) production.get("symbols"));
				break;
				
			default: throw new RuntimeException("Unexpected type of a production: " + production.getName());
			
		}
	}
	
	private void computeEnds(Nonterminal head, IList rhs) {
		if (rhs.length() >= 1) {
			Symbol first = getSymbol((IConstructor) rhs.get(0));
			Symbol last = getSymbol((IConstructor) rhs.get(rhs.length() - 1));
			
			IsRecursive isLeft = new IsRecursive(head, Recursion.LEFT_REC, ebnfs);
			
			if(!first.accept(isLeft) && !isLeft.getEnd().isEmpty()) {
				Set<String> ends = leftEnds.get(head.getName());
				if (ends == null) {
					ends = new HashSet<>();
					leftEnds.put(head.getName(), ends);
				}
				ends.add(isLeft.getEnd());
				if (!isLeft.ends.isEmpty()) // EBNF related
					leftEnds.putAll(isLeft.ends);
			}
			
			IsRecursive isRight = new IsRecursive(head, Recursion.RIGHT_REC, ebnfs);
			
			if(!last.accept(isRight) && !isRight.getEnd().isEmpty()) {
				Set<String> ends = rightEnds.get(head.getName());
				if (ends == null) {
					ends = new HashSet<>();
					rightEnds.put(head.getName(), ends);
				}
				ends.add(isRight.getEnd());
				if (!isRight.ends.isEmpty()) // EBNF related
					rightEnds.putAll(isRight.ends);
			}
		}
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
		
		if (!body.isEmpty()) {
			Symbol last = body.get(body.size() - 1);
			if (last instanceof Code) {
				Code code = (Code) last;
				org.iguana.datadependent.ast.Statement lastStmt = code.getStatements()[code.getStatements().length - 1];
				if (lastStmt instanceof org.iguana.datadependent.ast.Statement.Expression
						&& !(((org.iguana.datadependent.ast.Statement.Expression) lastStmt).getExpression() instanceof org.iguana.datadependent.ast.Expression.Assignment)) {
					Return ret = Return.builder(((org.iguana.datadependent.ast.Statement.Expression) lastStmt).getExpression()).build();
					body.remove(body.size() - 1);
					if (code.getStatements().length == 1) {
						body.add(code.getSymbol());
					} else {
						code = Code.code(code.getSymbol(), Arrays.copyOf(code.getStatements(), code.getStatements().length - 1));
						body.add(code);
					}
					body.add(ret);
				}
			}
		}
		
		boolean isLeft = body.size() == 0? false : body.get(0).accept(new IsRecursive(head, Recursion.LEFT_REC, ebnfs));
		boolean isRight = body.size() == 0? false : body.get(body.size() - 1).accept(new IsRecursive(head, Recursion.RIGHT_REC, ebnfs));
		
		IsRecursive visitor = new IsRecursive(head, Recursion.iLEFT_REC, leftEnds, ebnfs, layout);
		
		boolean isiLeft = body.size() == 0? false : body.get(0).accept(visitor);
		String leftEnd = visitor.getEnd();
		
		visitor = new IsRecursive(head, Recursion.iRIGHT_REC, rightEnds, ebnfs, layout);
		boolean isiRight = body.size() == 0? false : body.get(body.size() - 1).accept(visitor);
		String rightEnd = visitor.getEnd();
				
		Recursion recursion = Recursion.NON_REC;
		Recursion irecursion = Recursion.NON_REC;
		int precedence = -1;
		
		if (isLeft && isRight)
			recursion = Recursion.LEFT_RIGHT_REC;
		else if (isLeft)
			recursion = Recursion.LEFT_REC;
		else if (isRight)
			recursion = Recursion.RIGHT_REC;
		
		if (isiLeft && isiRight)
			irecursion = Recursion.iLEFT_RIGHT_REC;
		else if (isiLeft)
			irecursion = Recursion.iLEFT_REC;
		else if (isiRight)
			irecursion = Recursion.iRIGHT_REC;
		
		if (recursion == Recursion.NON_REC && irecursion == Recursion.NON_REC)
			associativity = Associativity.UNDEFINED;
		
		// Mixed cases
		boolean isPrefixOrCanBePrefix = (irecursion != Recursion.iLEFT_REC && recursion == Recursion.RIGHT_REC)
										  || (recursion != Recursion.LEFT_REC && irecursion == Recursion.iRIGHT_REC);
		boolean isPostfixOrCanBePostfix = (recursion == Recursion.LEFT_REC && irecursion != Recursion.iRIGHT_REC)
											|| (irecursion == Recursion.iLEFT_REC && recursion != Recursion.RIGHT_REC);
		
		if ((isPrefixOrCanBePrefix || isPostfixOrCanBePostfix) && associativity != Associativity.NON_ASSOC) 
			associativity = Associativity.UNDEFINED;
			
		return Rule.withHead(head).addSymbols(body).setObject(object).setLayoutStrategy(strategy)
									.setRecursion(recursion)
									.setiRecursion(irecursion)
									.setLeftEnd(leftEnd)
									.setRightEnd(rightEnd)
									.setLeftEnds(leftEnds.get(head.getName()))
									.setRightEnds(rightEnds.get(head.getName()))
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
				Nonterminal nonterminal = Nonterminal.withName(getName(symbol));
				IList actuals = (IList) symbol.asWithKeywordParameters().getParameters().get("actuals");
				if (actuals != null) {
					Builder builder = Nonterminal.builder(nonterminal);
					org.iguana.datadependent.ast.Expression[] arguments = new org.iguana.datadependent.ast.Expression[actuals.length()];
					int i = 0;
					for (IValue actual : actuals) {
						if (actual instanceof IString)
							arguments[i++] = (org.iguana.datadependent.ast.Expression) buildExpression(parseRascal("Expression", ((IString) actual).getValue())).accept(new Visitor());
					}
					nonterminal = builder.apply(arguments).build();
				}
				return nonterminal;

			case "char-class":
				return getCharacterClass(symbol);
				
			case "lit":
				return Terminal.from(Sequence.from(getString(symbol)));
	
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
				return tokens.computeIfAbsent(symbol, c -> getTokenDefinition(c, definitions));

			case "layouts":
//				if (layout != null) {
//					String name = getName(symbol);
//					if (name.equals(layout.getName()))
//						return layout;
//				} 
				return null;
				
			// DD part:
				
			case "scope":
				return Block.block(getSymbolList((IList)symbol.get("symbols")).stream().toArray(Symbol[]::new));
				
			case "if":
				Expression condition = buildExpression(getCondition(symbol));
				return IfThen.ifThen((org.iguana.datadependent.ast.Expression) condition.accept(new Visitor()), getSymbol(getSymbolCons(symbol)));
				
			case "ifElse":
				condition = buildExpression(getCondition(symbol));
				return IfThenElse.ifThenElse((org.iguana.datadependent.ast.Expression) condition.accept(new Visitor()), getSymbol(getThenPart(symbol)), getSymbol(getElsePart(symbol)));
				
			case "when":
				condition = buildExpression(getCondition(symbol));
				return Conditional.when(getSymbol(getSymbolCons(symbol)), (org.iguana.datadependent.ast.Expression) condition.accept(new Visitor()));
			
			case "do":
				Statement block = buildStatement(getBlock(symbol));
				return Code.code(getSymbol(getSymbolCons(symbol)), (org.iguana.datadependent.ast.Statement) block.accept(new Visitor()));
			
			case "while":
				condition = buildExpression(getCondition(symbol));
				return While.whileLoop((org.iguana.datadependent.ast.Expression) condition.accept(new Visitor()), getSymbol(getSymbolCons(symbol)));
				
			case "align":
				return Align.align(getSymbol(getSymbolCons(symbol)));
				
			case "offside":
				return Offside.offside(getSymbol(getSymbolCons(symbol)));
				
			case "ignore":
				return Ignore.ignore(getSymbol(getSymbolCons(symbol)));
								
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
                    
                case "far-follow":
                    set.add(RegularExpressionCondition.followIgnoreLayout((RegularExpression) getSymbol(getSymbolCons((IConstructor) condition))));
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
					
				case "end-of-file":
					set.add(new PositionalCondition(ConditionType.END_OF_FILE));
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
		IString block = (IString) symbol.get("block");
		return parseRascal("Statement", block.getValue());
	}
	
	private IConstructor getCondition(IConstructor symbol) {
		 IString condition = (IString) symbol.get("condition");
		 return  parseRascal("Expression", condition.getValue());
	}
	
	private IConstructor parseRascal(String nt, String input) {
		 return new RascalParser().parse(nt, URIUtil.rootScheme("datadep"), input.toCharArray(), new DefaultNodeFlattener<IConstructor, IConstructor, ISourceLocation>(), new UPTRNodeFactory());
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
			return stat(assign(x.getAssignable().accept(this).toString(), (org.iguana.datadependent.ast.Expression) x.getStatement().getExpression().accept(this)));
		}
		
		@Override
		public AbstractAST visitAssignableVariable(org.rascalmpl.ast.Assignable.Variable x) {
			return var(Names.name(Names.lastName(x.getQualifiedName())));
		}
		
		@Override
		public AbstractAST visitStatementVariableDeclaration(VariableDeclaration x) {
			return varDeclStat((org.iguana.datadependent.ast.VariableDeclaration) x.getDeclaration().accept(this));
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
			return varDecl(Names.name(x.getName()), (org.iguana.datadependent.ast.Expression) x.getInitial().accept(this));
		}
		
		@Override
		public AbstractAST visitVariableUnInitialized(UnInitialized x) {
			return varDecl(Names.name(x.getName()));
		}
		
		@Override
		public AbstractAST visitStatementExpression(org.rascalmpl.ast.Statement.Expression x) {
			return stat((org.iguana.datadependent.ast.Expression) x.getExpression().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionCallOrTree(CallOrTree x) {
			
			AbstractAST fun = x.getExpression().accept(this);
			
			if (!(fun instanceof org.iguana.datadependent.ast.Expression.Name)) {
				throw new RuntimeException("Unsupported Rascal expression: " + fun);
			}
			String id = ((org.iguana.datadependent.ast.Expression.Name) fun).getName();
			
			if (!(id.equals("indent") ||
				  id.equals("len") ||
				  id.equals("println") ||
				  id.equals("ppDeclare") ||
				  id.equals("ppLookup") ||
				  id.equals("endOfFile") ||
				  id.equals("startsWith") ||
				  id.equals("endsWith") ||
				  id.equals("put") ||
				  id.equals("contains") ||
				  id.equals("push") ||
				  id.equals("pop") ||
				  id.equals("top") ||
				  id.equals("find"))) {
				throw new RuntimeException("Unsupported function: " + id);
			}
			
			List<Expression> arguments = x.getArguments();
			org.iguana.datadependent.ast.Expression[] args = new org.iguana.datadependent.ast.Expression[arguments.size()];
			
			int j = 0;
			for (Expression argument : arguments) {
				args[j] = (org.iguana.datadependent.ast.Expression) argument.accept(this);
				j++;
			}
			
			if (id.equals("indent")) 
				return indent(args[0]);
			else if (id.equals("len"))
				return len(args[0]);
			else if (id.equals("ppDeclare"))
				return ppDeclare(args[0], args[1]);
			else if (id.equals("ppLookup"))
				return ppLookup(args[0]);
			else if (id.equals("endOfFile"))
				return endOfFile(args[0]);
			else if (id.equals("startsWith"))
				return startsWith(args);
			else if (id.equals("endsWith")) 
				return endsWith(args[0], args[1]);
			else if (id.equals("put")) {
				if (args.length == 2)
					return put(args[0], args[1]);
				else
					return put(args[0], args[1], args[2]);
			} else if (id.equals("contains"))
				return contains(args[0], args[1]);
			else if (id.equals("push"))
				return push(args[0], args[1]);
			else if (id.equals("pop"))
				return pop(args[0]);
			else if (id.equals("top"))
				return top(args[0]);
			else if (id.equals("find"))
				return find(args[0], args[1]);
			else 
				return println(args);
		}
		
		@Override
		public AbstractAST visitExpressionNegation(Negation x) {
			return not((org.iguana.datadependent.ast.Expression) x.getArgument().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionOr(Or x) {
			return or((org.iguana.datadependent.ast.Expression) x.getLhs().accept(this), 
					  (org.iguana.datadependent.ast.Expression) x.getRhs().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionAnd(And x) {
			return and((org.iguana.datadependent.ast.Expression) x.getLhs().accept(this), 
					  (org.iguana.datadependent.ast.Expression) x.getRhs().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionLessThan(LessThan x) {
			return less((org.iguana.datadependent.ast.Expression) x.getLhs().accept(this),
				        (org.iguana.datadependent.ast.Expression) x.getRhs().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionLessThanOrEq(LessThanOrEq x) {
			return lessEq((org.iguana.datadependent.ast.Expression) x.getLhs().accept(this),
				          (org.iguana.datadependent.ast.Expression) x.getRhs().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionGreaterThan(GreaterThan x) {
			return greater((org.iguana.datadependent.ast.Expression) x.getLhs().accept(this),
				           (org.iguana.datadependent.ast.Expression) x.getRhs().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
			return greaterEq((org.iguana.datadependent.ast.Expression) x.getLhs().accept(this),
				             (org.iguana.datadependent.ast.Expression) x.getRhs().accept(this));
		}
		
		@Override
		public AbstractAST visitExpressionEquals(Equals x) {
			return equal((org.iguana.datadependent.ast.Expression) x.getLhs().accept(this),
					     (org.iguana.datadependent.ast.Expression) x.getRhs().accept(this));
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
			else if (name.equals("yield"))
				return yield(Names.name(Names.lastName(qname.getQualifiedName())));
			else if (name.equals("val"))
				return val(Names.name(Names.lastName(qname.getQualifiedName())));
			else
				throw new RuntimeException("Unsupported expression: " + this);
		}
		
		@Override
		public AbstractAST visitExpressionMap(org.rascalmpl.ast.Expression.Map x) {
			if (!x.getMappings().isEmpty())
				throw new RuntimeException("Unsupported expression: " + this);
			
			return map();
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
			String substring = x.getString().substring(1, x.getString().length() - 1);
			return string(substring);
		}
		
	}
	
	private static class IsRecursive implements ISymbolVisitor<Boolean> {
		
		private final Recursion recursion;
		private final Nonterminal head;
		private final Symbol layout;
		
		private final Map<String, Set<String>> ends;
		private final Set<String> ebnfs;
		
		private String end = "";
		
		public IsRecursive(Nonterminal head, Recursion recursion, Set<String> ebnfs) {
			this(head, recursion, new HashMap<>(), ebnfs, null);
		}
		
		public IsRecursive(Nonterminal head, Recursion recursion, Map<String, Set<String>> ends, Set<String> ebnfs, Symbol layout) {
			this.recursion = recursion;
			this.head = head;
			this.layout = layout;
			this.ends = ends;
			this.ebnfs = ebnfs;
		}
		
		private String getEnd() {
			return end;
		}

		@Override
		public Boolean visit(Align symbol) {
			return symbol.getSymbol().accept(this);
		}

		@Override
		public Boolean visit(Block symbol) {
			Symbol[] symbols = symbol.getSymbols();
			if (recursion == Recursion.LEFT_REC || recursion == Recursion.iLEFT_REC)
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
		public Boolean visit(Ignore symbol) {
			return symbol.getSymbol().accept(this);
		}

		@Override
		public Boolean visit(Nonterminal symbol) {
			
			end = symbol.getName();
			
			if (recursion == Recursion.LEFT_REC || recursion == Recursion.RIGHT_REC) {
				if (symbol.getName().equals(head.getName())
						&& ((head.getParameters() == null && symbol.getArguments() == null)
								|| (head.getParameters().length == symbol.getArguments().length)))
					return true;
				
			} else {
				Set<String> set = ends.get(symbol.getName());
				if (set != null && set.contains(head.getName()))
					return true;
				else
					end = "NREC";
			}
			
			return false;
		}

		@Override
		public Boolean visit(Offside symbol) {
			return symbol.getSymbol().accept(this);
		}

		@Override
		public Boolean visit(Terminal symbol) {
			end = "$" + head.getName();
			return false;
		}

		@Override
		public Boolean visit(While symbol) {
			return symbol.getBody().accept(this);
		}
		
		@Override
		public Boolean visit(Return symbol) {
			return false;
		}

		@Override
		public <E extends Symbol> Boolean visit(Alt<E> symbol) {
			System.out.println("Warning: indirect recursion isn't yet supported for (.|.).");
			return false;
		}

		@Override
		public Boolean visit(Opt symbol) {
			System.out.println("Warning: indirect recursion isn't yet supported for options.");
			return false;
		}

		@Override
		public Boolean visit(Plus symbol) { // TODO: For the general case, should be done differently
			String name = EBNFToBNF.getName(symbol.getSymbol(), symbol.getSeparators(), null) + "+";
			end = name;
			
			if (recursion == Recursion.LEFT_REC || recursion == Recursion.RIGHT_REC) {
				ends.put(name, new HashSet<String>(Arrays.asList(symbol.getSymbol().getName())));
				ebnfs.add(name);
				return false;
			} else {
				Set<String> set = ends.get(name);
				if (set != null && set.contains(head.getName()))
					return true;
				else
					end = "NREC";
			}
			return false;
		}

		@Override
		public <E extends Symbol> Boolean visit(Sequence<E> symbol) {
			System.out.println("Warning: indirect recursion isn't yet supported for (. .).");
			return false;
		}

		@Override
		public Boolean visit(Star symbol) { // TODO: For the general case, should be done differently
			
			String base = EBNFToBNF.getName(symbol.getSymbol(), symbol.getSeparators(), null);
			String name = base + "*";
			end = name;
			
			if (recursion == Recursion.LEFT_REC || recursion == Recursion.RIGHT_REC) {
				// TODO: not good, there should be also left and right ends
				ends.put(name, new HashSet<String>(Arrays.asList(base + "+")));
				ends.put(base + "+", new HashSet<String>(Arrays.asList(symbol.getSymbol().getName())));
				ebnfs.add(name);
				ebnfs.add(base + "+");
				return false;
			} else {
				Set<String> set = ends.get(name);
				if (set != null && set.contains(head.getName()))
					return true;
				else
					end = "NREC";
			}
			return false;
		}
		
	}
	
}
