package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.StringLiteral;
import org.rascalmpl.ast.Tags;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class Test extends org.rascalmpl.ast.Test {

	static public class Labeled extends org.rascalmpl.ast.Test.Labeled {

		public Labeled(INode __param1, Tags __param2, Expression __param3,
				StringLiteral __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			__eval.getCurrentModuleEnvironment().addTest(this);
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	static public class Unlabeled extends org.rascalmpl.ast.Test.Unlabeled {

		public Unlabeled(INode __param1, Tags __param2, Expression __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			__eval.getCurrentModuleEnvironment().addTest(this);
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	public Test(INode __param1) {
		super(__param1);
	}
}
