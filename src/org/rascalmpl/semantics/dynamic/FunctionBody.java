package org.rascalmpl.semantics.dynamic;

import java.util.List;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class FunctionBody extends org.rascalmpl.ast.FunctionBody {

	static public class Default extends org.rascalmpl.ast.FunctionBody.Default {

		public Default(ISourceLocation __param1, List<Statement> __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			Result<IValue> result = org.rascalmpl.interpreter.result.ResultFactory
					.nothing();

			for (Statement statement : this.getStatements()) {
				__eval.setCurrentAST(statement);
				result = statement.interpret(__eval);
			}

			return result;

		}

	}

	public FunctionBody(ISourceLocation __param1) {
		super(__param1);
	}

}
