package org.rascalmpl.interpreter.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.Case;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.PatternWithAction;
import org.rascalmpl.ast.Replacement;
import org.rascalmpl.ast.StringConstant;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.semantics.dynamic.QualifiedName;
import org.rascalmpl.semantics.dynamic.Tree;
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
		
		if (pattern.isVariableBecomes() || pattern.isTypedVariableBecomes()) {
			pattern = pattern.getPattern();
		}
		
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
	
	public static abstract class CaseBlock {
		public boolean hasRegExp = false;
		public boolean allConcrete = false;
		public abstract boolean matchAndEval(IEvaluator<Result<IValue>> eval, Result<IValue> subject);
		
		protected void computePredicates(Case c) {
			if (c.hasPatternWithAction()) {
				Expression pattern = c.getPatternWithAction().getPattern();
				hasRegExp |= pattern.isLiteral() && pattern.getLiteral().isRegExp();

				Type type = pattern._getType();
				allConcrete &= type instanceof NonTerminalType;
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
			org.eclipse.imp.pdb.facts.type.Type subjectType = value
					.getType();

			if (subjectType.isSubtypeOf(Factory.Tree) && TreeAdapter.isAppl((IConstructor) value)) {
				List<DefaultBlock> alts = table.get(TreeAdapter.getProduction((IConstructor) value));
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
		public boolean matchAndEval(IEvaluator<Result<IValue>> __eval, Result<IValue> subject) {
			if (theCase.isDefault()) {
				theCase.getStatement().interpret(__eval);
				return true;
			}
			
			PatternWithAction rule = theCase.getPatternWithAction();
			
			if (rule.hasStatement()) {
				return __eval.matchAndEval(subject, rule.getPattern(), pattern.getStatement());
			}
			else {
				return __eval.matchEvalAndReplace(subject, rule.getPattern(), conditions, insert);
			}
		}
	}

	private static class NodeCaseBlock extends CaseBlock {
		private final Hashtable<String, List<DefaultBlock>> table = new Hashtable<String, List<DefaultBlock>>();

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
				key = ((StringConstant.Lexical) name.getLiteral().getStringLiteral().getConstant()).getString();
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
			org.eclipse.imp.pdb.facts.type.Type subjectType = value
					.getType();

			if (subjectType.isSubtypeOf(TF.nodeType())) {
				List<DefaultBlock> alts = table.get(((INode) value).getName());
				if (alts != null) {
					for (DefaultBlock c : alts) {
						if (c.matchAndEval(eval, subject)) {
							return true;
						}
					}
				}
			}
			return false;
		}
	}
}
