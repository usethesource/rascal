package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;

public abstract class ShellCommand extends org.rascalmpl.ast.ShellCommand {

	static public class Edit extends org.rascalmpl.ast.ShellCommand.Edit {
		public Edit(ISourceLocation __param1, QualifiedName __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}
	}

	static public class Help extends org.rascalmpl.ast.ShellCommand.Help {

		public Help(ISourceLocation __param1) {
			super(__param1);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			__eval.setCurrentAST(this);
			__eval.printHelpMessage(__eval.getStdOut());
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	static public class History extends org.rascalmpl.ast.ShellCommand.History {

		public History(ISourceLocation __param1) {
			super(__param1);
		}

	}

	static public class ListDeclarations extends
			org.rascalmpl.ast.ShellCommand.ListDeclarations {

		public ListDeclarations(ISourceLocation __param1) {
			super(__param1);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}

	}

	static public class Quit extends org.rascalmpl.ast.ShellCommand.Quit {

		public Quit(ISourceLocation __param1) {
			super(__param1);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			throw new QuitException();
		}

	}

	static public class SetOption extends
			org.rascalmpl.ast.ShellCommand.SetOption {

		public SetOption(ISourceLocation __param1, QualifiedName __param2,
				Expression __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			String name = "rascal.config." + this.getName().toString();
			String value = this.getExpression().interpret(__eval).getValue()
					.toString();

			java.lang.System.setProperty(name, value);

			__eval.updateProperties();

			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	static public class Test extends org.rascalmpl.ast.ShellCommand.Test {

		public Test(ISourceLocation __param1) {
			super(__param1);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return org.rascalmpl.interpreter.result.ResultFactory.bool(__eval
					.runTests(), __eval);
		}

	}

	static public class Unimport extends
			org.rascalmpl.ast.ShellCommand.Unimport {

		public Unimport(ISourceLocation __param1, QualifiedName __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			((ModuleEnvironment) __eval.getCurrentEnvt().getRoot())
					.unImport(this.getName().toString());
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	public ShellCommand(ISourceLocation __param1) {
		super(__param1);
	}
}
