package org.rascalmpl.semantics.dynamic;

import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Replacement;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.PatternEvaluator;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.NodePattern;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.values.uptr.Factory;

public abstract class PatternWithAction extends org.rascalmpl.ast.PatternWithAction {

	public PatternWithAction(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.PatternWithAction.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.PatternWithAction> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Arbitrary extends org.rascalmpl.ast.PatternWithAction.Arbitrary {

		public Arbitrary(INode __param1, Expression __param2, Statement __param3) {
			super(__param1, __param2, __param3);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			IMatchingResult pv = this.getPattern().buildMatcher((PatternEvaluator) __eval.__getPatternEvaluator());

			Type pt = pv.getType(__eval.getCurrentEnvt());

			if (pv instanceof NodePattern) {
				pt = ((NodePattern) pv).getConstructorType(__eval.getCurrentEnvt());
			}

			// TODO store rules for concrete syntax on production rule and
			// create Lambda's for production rules to speed up matching and
			// rewrite rule look up
			if (pt instanceof NonTerminalType) {
				pt = Factory.Tree_Appl;
			}

			if (!(pt.isAbstractDataType() || pt.isConstructorType() || pt.isNodeType()))
				throw new UnexpectedTypeError(org.rascalmpl.interpreter.Evaluator.__getTf().nodeType(), pt, this);

			__eval.__getHeap().storeRule(pt, this, __eval.getCurrentModuleEnvironment());
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	static public class Replacing extends org.rascalmpl.ast.PatternWithAction.Replacing {

		public Replacing(INode __param1, Expression __param2, Replacement __param3) {
			super(__param1, __param2, __param3);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			IMatchingResult pv = this.getPattern().buildMatcher((PatternEvaluator) __eval.__getPatternEvaluator());
			Type pt = pv.getType(__eval.getCurrentEnvt());

			if (pv instanceof NodePattern) {
				pt = ((NodePattern) pv).getConstructorType(__eval.getCurrentEnvt());
			}

			if (pt instanceof NonTerminalType) {
				pt = Factory.Tree_Appl;
			}

			if (!(pt.isAbstractDataType() || pt.isConstructorType() || pt.isNodeType()))
				throw new UnexpectedTypeError(org.rascalmpl.interpreter.Evaluator.__getTf().nodeType(), pt, this);

			__eval.__getHeap().storeRule(pt, this, __eval.getCurrentModuleEnvironment());
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}
}
