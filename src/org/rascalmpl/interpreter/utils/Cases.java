package org.rascalmpl.interpreter.utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.Case;
import org.rascalmpl.ast.PatternWithAction;
import org.rascalmpl.ast.StringConstant;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class Cases  {
	private static final TypeFactory TF = TypeFactory.getInstance();
	
	public static List<CaseBlock> precompute(List<Case> cases) {
		ArrayList<CaseBlock> blocks = new ArrayList<CaseBlock>(cases.size());
		
		for (int i = 0; i < cases.size(); i++) {
			Case c = cases.get(i);
			if (isConcreteSyntaxPattern(c)) {
				ConcreteBlock b = new ConcreteBlock();
				b.add(c);
				for (int j = i + 1; j < cases.size(); j++) {
					Case d = cases.get(j);
					if (isConcreteSyntaxPattern(d)) {
						b.add(d);
						i++;
					} else {
						break;
					}
				}
				blocks.add(b);
			} else if (isConstantTreePattern(c)) {
				NodeCaseBlock b = new NodeCaseBlock();
				b.add(c);
				for (int j = i + 1; j < cases.size(); j++) {
					Case d = cases.get(j);
					if (isConstantTreePattern(d)) {
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
		if (pattern._getType() != null
				&& pattern._getType() instanceof NonTerminalType) {
			return true;
		}

		return false;
	}


	private static boolean isConstantTreePattern(Case c) {
		if (c.isDefault()) {
			return false;
		}
		org.rascalmpl.ast.Expression pattern = c.getPatternWithAction()
				.getPattern();
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
	public static abstract class CaseBlock {
		public abstract boolean matchAndEval(Evaluator eval,
				Result<IValue> subject);
	}

	public static class ConcreteBlock extends CaseBlock {
		private final Hashtable<IConstructor, List<Case>> table = new Hashtable<IConstructor, List<Case>>();

		void add(Case c) {
			IConstructor key = TreeAdapter.getProduction(c
					.getPatternWithAction().getPattern().getTree());
			List<Case> same = table.get(key);
			if (same == null) {
				same = new LinkedList<Case>();
				table.put(key, same);
			}
			same.add(c);
		}

		@Override
		public boolean matchAndEval(Evaluator eval, Result<IValue> subject) {
			IValue value = subject.getValue();
			org.eclipse.imp.pdb.facts.type.Type subjectType = value
					.getType();

			if (subjectType.isSubtypeOf(Factory.Tree)) {
				List<Case> alts = table.get(TreeAdapter
						.getProduction((IConstructor) value));
				if (alts != null) {
					for (Case c : alts) {
						PatternWithAction rule = c.getPatternWithAction();
						if (eval.matchAndEval(subject, rule.getPattern(),
								rule.getStatement())) {
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

		public DefaultBlock(Case c) {
			this.theCase = c;
		}

		@Override
		public boolean matchAndEval(Evaluator __eval, Result<IValue> subject) {
			if (theCase.isDefault()) {
				// TODO: what if the default statement uses a fail
				// statement?
				theCase.getStatement().interpret(__eval);
				return true;
			}
			
			PatternWithAction rule = theCase.getPatternWithAction();
			return __eval.matchAndEval(subject, rule.getPattern(), rule.getStatement());
		}
	}

	private static class NodeCaseBlock extends CaseBlock {
		private final Hashtable<String, List<Case>> table = new Hashtable<String, List<Case>>();

		void add(Case c) {
			org.rascalmpl.ast.Expression name = c.getPatternWithAction()
					.getPattern().getExpression();
			String key = null;

			if (name.isQualifiedName()) {
				key = Names.name(Names.lastName(name.getQualifiedName()));
			} else if (name.isLiteral()) {
				key = ((StringConstant.Lexical) name.getLiteral()
						.getStringLiteral().getConstant()).getString();
			}

			List<Case> same = table.get(key);
			if (same == null) {
				same = new LinkedList<Case>();
				table.put(key, same);
			}
			same.add(c);
		}

		@Override
		public boolean matchAndEval(Evaluator eval, Result<IValue> subject) {
			IValue value = subject.getValue();
			org.eclipse.imp.pdb.facts.type.Type subjectType = value
					.getType();

			if (subjectType.isSubtypeOf(TF.nodeType())) {
				List<Case> alts = table.get(((INode) value).getName());
				if (alts != null) {
					for (Case c : alts) {
						PatternWithAction rule = c.getPatternWithAction();
						if (eval.matchAndEval(subject, rule.getPattern(),
								rule.getStatement())) {
							return true;
						}
					}
				}
			}

			return false;
		}
	}

	

}
