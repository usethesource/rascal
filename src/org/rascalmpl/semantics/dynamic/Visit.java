package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Case;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Strategy;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.TraversalEvaluator;
import org.rascalmpl.interpreter.TraversalEvaluator.DIRECTION;
import org.rascalmpl.interpreter.TraversalEvaluator.FIXEDPOINT;
import org.rascalmpl.interpreter.TraversalEvaluator.PROGRESS;
import org.rascalmpl.interpreter.TraverseResult;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.result.Result;

public abstract class Visit extends org.rascalmpl.ast.Visit {

	public Visit(INode __param1) {
		super(__param1);
	}

	static public class DefaultStrategy extends org.rascalmpl.ast.Visit.DefaultStrategy {

		public DefaultStrategy(INode __param1, Expression __param2, List<Case> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			Result<IValue> subject = this.getSubject().interpret(__eval);
			List<Case> cases = this.getCases();
			TraversalEvaluator te = new TraversalEvaluator(__eval);

			TraverseResult tr = te.traverse(subject.getValue(), te.new CasesOrRules(cases), DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No);
			Type t = tr.value.getType();
			IValue val = tr.value;
			org.rascalmpl.interpreter.TraverseResultFactory.freeTraverseResult(tr);
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(t, val, __eval);

		}

	}

	static public class GivenStrategy extends org.rascalmpl.ast.Visit.GivenStrategy {

		public GivenStrategy(INode __param1, Strategy __param2, Expression __param3, List<Case> __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			Result<IValue> subject = this.getSubject().interpret(__eval);

			// TODO: warning switched to static type here, but not sure if
			// that's correct...
			Type subjectType = subject.getType();

			if (subjectType.isConstructorType()) {
				subjectType = subjectType.getAbstractDataType();
			}

			List<Case> cases = this.getCases();
			Strategy s = this.getStrategy();

			DIRECTION direction = DIRECTION.BottomUp;
			PROGRESS progress = PROGRESS.Continuing;
			FIXEDPOINT fixedpoint = FIXEDPOINT.No;

			if (s.isBottomUp()) {
				direction = DIRECTION.BottomUp;
			} else if (s.isBottomUpBreak()) {
				direction = DIRECTION.BottomUp;
				progress = PROGRESS.Breaking;
			} else if (s.isInnermost()) {
				direction = DIRECTION.BottomUp;
				fixedpoint = FIXEDPOINT.Yes;
			} else if (s.isTopDown()) {
				direction = DIRECTION.TopDown;
			} else if (s.isTopDownBreak()) {
				direction = DIRECTION.TopDown;
				progress = PROGRESS.Breaking;
			} else if (s.isOutermost()) {
				direction = DIRECTION.TopDown;
				fixedpoint = FIXEDPOINT.Yes;
			} else {
				throw new ImplementationError("Unknown strategy " + s);
			}

			TraversalEvaluator te = new TraversalEvaluator(__eval);
			TraverseResult tr = te.traverse(subject.getValue(), te.new CasesOrRules(cases), direction, progress, fixedpoint);
			Type t = tr.value.getType();
			IValue val = tr.value;
			org.rascalmpl.interpreter.TraverseResultFactory.freeTraverseResult(tr);
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(t, val, __eval);

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.Visit.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Visit> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}