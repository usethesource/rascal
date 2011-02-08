package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.ShellCommand;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

public abstract class Command extends org.rascalmpl.ast.Command {

	public Command(INode __param1) {
		super(__param1);
	}

	static public class Import extends org.rascalmpl.ast.Command.Import {

		public Import(INode __param1, org.rascalmpl.ast.Import __param2) {
			super(__param1, __param2);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			__eval.setCurrentAST(this);
			Result<IValue> res = this.getImported().interpret(__eval);
			
			// If we import a module from the command line, notify any expressions caching
			// results that could be invalidated by a module load that we have loaded.
			__eval.notifyGenericLoadListeners();
			
			return res;

		}

	}

	static public class Shell extends org.rascalmpl.ast.Command.Shell {

		public Shell(INode __param1, ShellCommand __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			__eval.setCurrentAST(this);
			return this.getCommand().interpret(__eval);

		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Command.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Command> __param2) {
			super(__param1, __param2);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			throw new Ambiguous((IConstructor) this.getTree());

		}

	}

	static public class Statement extends org.rascalmpl.ast.Command.Statement {

		public Statement(INode __param1, org.rascalmpl.ast.Statement __param2) {
			super(__param1, __param2);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			__eval.setCurrentAST(this.getStatement());
			return __eval.eval(this.getStatement());

		}

	}

	static public class Expression extends org.rascalmpl.ast.Command.Expression {

		public Expression(INode __param1, org.rascalmpl.ast.Expression __param2) {
			super(__param1, __param2);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			Environment old = __eval.getCurrentEnvt();

			try {
				__eval.pushEnv();
				__eval.setCurrentAST(this.getExpression());
				return this.getExpression().interpret(__eval);
			} finally {
				__eval.unwind(old);
			}

		}

	}

	static public class Declaration extends org.rascalmpl.ast.Command.Declaration {

		public Declaration(INode __param1, org.rascalmpl.ast.Declaration __param2) {
			super(__param1, __param2);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			__eval.setCurrentAST(this);
			return this.getDeclaration().interpret(__eval);

		}

	}
}
