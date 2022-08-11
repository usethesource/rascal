/*******************************************************************************
 * Copyright (c) 2011-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Michael Steindorfer  - michael.steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.rascalmpl.ast.Case;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.PatternWithAction;
import org.rascalmpl.ast.Replacement;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.StringConstant;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.Insert;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.semantics.dynamic.QualifiedName;
import org.rascalmpl.semantics.dynamic.Tree;
import org.rascalmpl.types.NonTerminalType;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.TreeAdapter;

public class Cases  {
	public static final List<String> IUPTR_NAMES = Arrays.asList("appl", "cycle", "amb", "char", "Tree::appl", "Tree::cycle", "Tree::char", "Tree::amb");
	
	private static final TypeFactory TF = TypeFactory.getInstance();
	
	public static List<CaseBlock> precompute(List<Case> cases, boolean allowReplacement) {
		ArrayList<CaseBlock> blocks = new ArrayList<CaseBlock>(cases.size());
		
		for (int i = 0; i < cases.size(); i++) {
			Case c = cases.get(i);
			
			if (!allowReplacement && c.hasPatternWithAction() && c.getPatternWithAction().isReplacing()) {
			    throw new SyntaxError("=> not allowed in switch", c.getLocation());
			}
			
			if (isConcreteSyntaxPattern(c)) {
				ConcreteBlock b = new ConcreteBlock();
				b.add(c);
				for (int j = i + 1; j < cases.size(); j++) {
					Case d = cases.get(j);
					if (isConcreteSyntaxPattern(d) && !isIUPTRPattern(d)) {
						b.add(d);
						i++;
					} else {
						break;
					}
				}
				blocks.add(b);
			} else if (isIUPTRPattern(c)) {
				blocks.add(new DefaultBlock(c));
			} else if (isConstantTreePattern(c)) {
				NodeCaseBlock b = new NodeCaseBlock();
				b.add(c);
				for (int j = i + 1; j < cases.size(); j++) {
					Case d = cases.get(j);
					if (isConstantTreePattern(d) && !isIUPTRPattern(d)) {
						b.add(d);
						i++;
					} else {
						break;
					}
				}
				blocks.add(b);
			} else {
				blocks.add(new DefaultBlock(c));
			}
		}
		
		return blocks;
	}
	
	private static boolean isConcreteSyntaxPattern(Case d) {
		if (d.isDefault()) {
			return false;
		}

		org.rascalmpl.ast.Expression pattern = d.getPatternWithAction()
				.getPattern();
		
		if (pattern.isVariableBecomes() || pattern.isTypedVariableBecomes()) {
			pattern = pattern.getPattern();
		}
		
		if (pattern.getConcreteSyntaxType() != null && pattern.getConcreteSyntaxType() instanceof NonTerminalType) {
			return true;
		}

		return false;
	}
	
	private static boolean isIUPTRPattern(Case d) {
		if (d.isDefault()) {
			return false;
		}

		org.rascalmpl.ast.Expression pattern = d.getPatternWithAction().getPattern();
		
		if (pattern.isVariableBecomes() || pattern.isTypedVariableBecomes()) {
			pattern = pattern.getPattern();
		}
		
		if (pattern.isCallOrTree()) {
			Expression func =  pattern.getExpression();
			if (func.isQualifiedName() && IUPTR_NAMES.contains(Names.fullName(func.getQualifiedName()))) {
				return true;
			}
		}

		return false;

	}


	private static boolean isConstantTreePattern(Case c) {
		if (c.isDefault()) {
			return false;
		}
		
		
		org.rascalmpl.ast.Expression pattern = c.getPatternWithAction()
				.getPattern();
		if (pattern.isVariableBecomes() || pattern.isTypedVariableBecomes()) {
			pattern = pattern.getPattern();
		}
		
		if (pattern.isCallOrTree()) {
			if (pattern.getExpression().isQualifiedName()) {
				return true;
			}
			if (pattern.getExpression().isLiteral()) {
				return true;
			}
		}

		return false;
	}
	
	private static boolean isNonTerminalType(Type t) {
		return t instanceof NonTerminalType;
	}
	
	public static abstract class CaseBlock {
		public boolean hasRegExp = false;
		public boolean allConcrete = false;
		public abstract boolean matchAndEval(IEvaluator<Result<IValue>> eval, Result<IValue> subject);
		
		protected void computePredicates(Case c) {
			if (c.hasPatternWithAction()) {
				Expression pattern = c.getPatternWithAction().getPattern();
				hasRegExp |= pattern.isLiteral() && pattern.getLiteral().isRegExp();

				Type type = pattern.getConcreteSyntaxType();
				allConcrete &= isNonTerminalType(type);
			}
		}
	}

	public static class ConcreteBlock extends CaseBlock {
		private final Hashtable<IConstructor, List<DefaultBlock>> table = new Hashtable<IConstructor, List<DefaultBlock>>();

		public ConcreteBlock() {
			allConcrete = true;
			hasRegExp = false;
		}
		
		void add(Case c) {
			Expression pattern = c.getPatternWithAction().getPattern();
			if (pattern.isVariableBecomes() || pattern.isTypedVariableBecomes()) {
				pattern = pattern.getPattern();
			}

			IConstructor key = ((Tree.Appl) pattern).getProduction();
			List<DefaultBlock> same = table.get(key);
			if (same == null) {
				same = new LinkedList<DefaultBlock>();
				table.put(key, same);
			}
			same.add(new DefaultBlock(c));
		}

		@Override
		public boolean matchAndEval(IEvaluator<Result<IValue>> eval, Result<IValue> subject) {
			IValue value = subject.getValue();
			io.usethesource.vallang.type.Type subjectType = value.getType();

			if (subjectType.isSubtypeOf(RascalValueFactory.Tree) && TreeAdapter.isAppl((org.rascalmpl.values.parsetrees.ITree) value)) {
				List<DefaultBlock> alts = table.get(TreeAdapter.getProduction((org.rascalmpl.values.parsetrees.ITree) value));
				if (alts != null) {
					for (CaseBlock c : alts) {
						if (c.matchAndEval(eval, subject)) {
							return true;
						}
					}
				}
			}

			return false;
		}
	}

	private static class DefaultBlock extends CaseBlock {
		private final Case theCase;
		private final PatternWithAction pattern;
		private IMatchingResult matcher; 
		private final Replacement replacement;
		private final List<Expression> conditions;
		private final Expression insert;

		public DefaultBlock(Case c) {
			this.theCase = c;
			this.pattern = c.hasPatternWithAction() ? c.getPatternWithAction() : null;
			this.replacement = pattern != null && pattern.hasReplacement() ? pattern.getReplacement() : null;
			this.conditions = replacement != null && replacement.hasConditions() ? replacement.getConditions() : Collections.<Expression>emptyList();
			this.insert = replacement != null ? replacement.getReplacementExpression() : null;
			
			computePredicates(c);
		}

		@Override
		public boolean matchAndEval(IEvaluator<Result<IValue>> eval, Result<IValue> subject) {
			if (theCase.isDefault()) {
				theCase.getStatement().interpret(eval);
				return true;
			}
			
			if (matcher == null) {
			    matcher = pattern.getPattern().buildMatcher(eval, false);
			}
			
			if (pattern.hasStatement()) {
				return Cases.matchAndEval(subject, matcher, pattern.getStatement(), eval);
			}
			else {
				return matchEvalAndReplace(subject, matcher, conditions, insert, eval);
			}
		}
		 
	  public static boolean matchEvalAndReplace(Result<IValue> subject, IMatchingResult mp, List<Expression> conditions, Expression replacementExpr, IEvaluator<Result<IValue>> eval) {
	    Environment old = eval.getCurrentEnvt();
	    try {
	      mp.initMatch(subject);

	      while (mp.hasNext()) {
	        if (eval.isInterrupted())
	          throw new InterruptException(eval.getStackTrace(), eval.getCurrentAST().getLocation());
	        if (mp.next()) {
	          int size = conditions.size();
	          
	          if (size == 0) {
	             throw new Insert(replacementExpr.interpret(eval), mp, mp.getType(eval.getCurrentEnvt(), null));
	          }
	          
	          IBooleanResult[] gens = new IBooleanResult[size];
	          Environment[] olds = new Environment[size];
	          Environment old2 = eval.getCurrentEnvt();

	          int i = 0;
	          try {
	            olds[0] = eval.getCurrentEnvt();
	            eval.pushEnv();
	            gens[0] = conditions.get(0).getBacktracker(eval);
	            gens[0].init();

	            while (i >= 0 && i < size) {

	              if (eval.isInterrupted()) {
	                throw new InterruptException(eval.getStackTrace(), eval.getCurrentAST().getLocation());
	              }
	              if (gens[i].hasNext() && gens[i].next()) {
	                if (i == size - 1) {
	                  // in IfThen the body is executed, here we insert the expression
	                  // NB: replaceMentExpr sees the latest bindings of the when clause 
	                  throw new Insert(replacementExpr.interpret(eval), mp, mp.getType(eval.getCurrentEnvt(), null));
	                }

	                i++;
	                gens[i] = conditions.get(i).getBacktracker(eval);
	                gens[i].init();
	                olds[i] = eval.getCurrentEnvt();
	                eval.pushEnv();
	              } else {
	                eval.unwind(olds[i]);
	                eval.pushEnv();
	                i--;
	              }
	            }
	          } finally {
	            eval.unwind(old2);
	          }
	        }
	      }
	    } finally {
	      eval.unwind(old);
	    }
	    return false;
	  }
	}
	

	private static class NodeCaseBlock extends CaseBlock {
		private final HashMap<String, List<DefaultBlock>> table = new HashMap<String, List<DefaultBlock>>();

		public NodeCaseBlock() {
			hasRegExp = false;
			allConcrete = false;
		}

		void add(Case c) {
			Expression pattern = c.getPatternWithAction().getPattern();
			org.rascalmpl.ast.Expression name;
			if (pattern.isVariableBecomes() || pattern.isTypedVariableBecomes()) {
				name = pattern.getPattern().getExpression();
			}
			else {
				name = pattern.getExpression();
			}
			String key = null;

			if (name.isQualifiedName()) {
				key = ((QualifiedName.Default) name.getQualifiedName()).lastName();
			} else if (name.isLiteral()) {
				StringConstant constant = name.getLiteral().getStringLiteral().getConstant();
				key = StringUtils.unescapeBase(StringUtils.unquote(((StringConstant.Lexical) constant).getString()));
			}

			List<DefaultBlock> same = table.get(key);
			if (same == null) {
				same = new LinkedList<DefaultBlock>();
				table.put(key, same);
			}
			same.add(new DefaultBlock(c));
		}

		@Override
		public boolean matchAndEval(IEvaluator<Result<IValue>> eval, Result<IValue> subject) {
			IValue value = subject.getValue();
			io.usethesource.vallang.type.Type subjectType = value.getType();

			if (subjectType.isSubtypeOf(TF.nodeType())) {
				boolean isTree = subjectType.isSubtypeOf(RascalValueFactory.Tree) 
				    && ((org.rascalmpl.values.parsetrees.ITree) subject.getValue()).isAppl();

				if (isTree) { // matching abstract with concrete
					TreeAsNode wrap = new TreeAsNode((org.rascalmpl.values.parsetrees.ITree) subject.getValue());
					Result<IValue> asTree = ResultFactory.makeResult(TF.nodeType(), wrap, eval);

					if (tryCases(eval, asTree)) {
						return true;
					}
				}
				else if (tryCases(eval, subject)) {
					return true;
				}
			}
			return false;
		}

		protected boolean tryCases(IEvaluator<Result<IValue>> eval, Result<IValue> subject) {
			List<DefaultBlock> alts = table.get(((INode) subject.getValue()).getName());

			if (alts != null) {
				for (DefaultBlock c : alts) {
					if (c.matchAndEval(eval, subject)) {
						return true;
					}
				}
			}

			return false;
		}
	}


  public static boolean matchAndEval(Result<IValue> subject, IMatchingResult mp, Statement stat, IEvaluator<Result<IValue>> eval) {
    boolean debug = false;
    Environment old = eval.getCurrentEnvt();
    eval.pushEnv();
  
    try {
      mp.initMatch(subject);
  
      while (mp.hasNext()) {
        eval.pushEnv();
        
        if (eval.isInterrupted()) {
          throw new InterruptException(eval.getStackTrace(), eval.getCurrentAST().getLocation());
        }
  
        if (mp.next()) {
          try {
            try {
              stat.interpret(eval);
            } catch (Insert e) {
              // Make sure that the match pattern is set
              if (e.getMatchPattern() == null) {
                e.setMatchPattern(mp);
              }
              e.setStaticType(mp.getType(eval.getCurrentEnvt(), null));
              throw e;
            }
            return true;
          } catch (Failure e) {
            // unwind(old); // can not clean up because you don't
            // know how far to roll back
          }
        }
      }
    } finally {
      if (debug) {
        System.err.println("Unwind to old env");
      }
      eval.unwind(old);
    }
    return false;
  }
}
