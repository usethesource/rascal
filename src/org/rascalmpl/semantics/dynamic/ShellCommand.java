package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;

public abstract class ShellCommand extends org.rascalmpl.ast.ShellCommand {

	public ShellCommand(INode __param1) {
		super(__param1);
	}

	static public class History extends org.rascalmpl.ast.ShellCommand.History {

		public History(INode __param1) {
			super(__param1);
		}


	}

	static public class SetOption extends org.rascalmpl.ast.ShellCommand.SetOption {

		public SetOption(INode __param1, QualifiedName __param2, Expression __param3) {
			super(__param1, __param2, __param3);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			String name = "rascal.config." + this.getName().toString();
			String value = this.getExpression().interpret(__eval).getValue().toString();

			java.lang.System.setProperty(name, value);

			__eval.updateProperties();

			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	static public class ListDeclarations extends org.rascalmpl.ast.ShellCommand.ListDeclarations {

		public ListDeclarations(INode __param1) {
			super(__param1);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}

	}

	static public class Help extends org.rascalmpl.ast.ShellCommand.Help {

		public Help(INode __param1) {
			super(__param1);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			__eval.setCurrentAST(this);
			__eval.printHelpMessage(__eval.getStdOut());
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}


	}

	static public class Undeclare extends org.rascalmpl.ast.ShellCommand.Undeclare {

		public Undeclare(INode __param1, QualifiedName __param2) {
			super(__param1, __param2);
		}


	}

	static public class Test extends org.rascalmpl.ast.ShellCommand.Test {

		public Test(INode __param1) {
			super(__param1);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return org.rascalmpl.interpreter.result.ResultFactory.bool(__eval.runTests(), __eval);

		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.ShellCommand.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.ShellCommand> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Quit extends org.rascalmpl.ast.ShellCommand.Quit {

		public Quit(INode __param1) {
			super(__param1);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			throw new QuitException();

		}

	}

	static public class Unimport extends org.rascalmpl.ast.ShellCommand.Unimport {

		public Unimport(INode __param1, QualifiedName __param2) {
			super(__param1, __param2);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			((ModuleEnvironment) __eval.getCurrentEnvt().getRoot()).unImport(this.getName().toString());
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	static public class ListModules extends org.rascalmpl.ast.ShellCommand.ListModules {

		public ListModules(INode __param1) {
			super(__param1);
		}


	}

	static public class Edit extends org.rascalmpl.ast.ShellCommand.Edit {

		public Edit(INode __param1, QualifiedName __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}


	}
}
