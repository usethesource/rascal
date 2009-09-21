/**
 * 
 */
package org.meta_environment.rascal.interpreter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.ast.Command.Declaration;
import org.meta_environment.rascal.ast.Command.Expression;
import org.meta_environment.rascal.ast.Command.Import;
import org.meta_environment.rascal.ast.Command.Shell;
import org.meta_environment.rascal.ast.Import.Default;
import org.meta_environment.rascal.ast.ShellCommand.Edit;
import org.meta_environment.rascal.ast.ShellCommand.Help;
import org.meta_environment.rascal.ast.ShellCommand.ListDeclarations;
import org.meta_environment.rascal.ast.ShellCommand.Quit;
import org.meta_environment.rascal.ast.ShellCommand.Test;
import org.meta_environment.rascal.ast.ShellCommand.Unimport;
import org.meta_environment.rascal.interpreter.control_exceptions.FailedTestError;
import org.meta_environment.rascal.interpreter.control_exceptions.QuitException;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.env.RewriteRule;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.parser.ConsoleParser;
import org.meta_environment.uri.FileURIResolver;

public class CommandEvaluator extends Evaluator {
	private ConsoleParser parser;

	
	public CommandEvaluator(IValueFactory f, OutputStream errorStream,
			ModuleEnvironment scope, GlobalEnvironment heap) {
		this(f, errorStream, scope, heap, new ConsoleParser(scope));
	}
	
	public CommandEvaluator(IValueFactory vf, OutputStream errorStream,
			ModuleEnvironment root, GlobalEnvironment heap,
			ConsoleParser consoleParser) {
		super(vf, errorStream, root, heap, consoleParser);
		this.parser = consoleParser;
	}
	
	public IConstructor parseCommand(String command) throws IOException {
		return parser.parseCommand(command);
	}
	
	public Result<IValue> eval(Command command) {
		return command.accept(this);
	}

	@Override
	public Result<IValue> visitCommandShell(Shell x) {
		return x.getCommand().accept(this);
	}

	@Override
	public Result<IValue> visitCommandDeclaration(Declaration x) {
		return x.getDeclaration().accept(this);
	}

	@Override
	public Result<IValue> visitCommandStatement(
			org.meta_environment.rascal.ast.Command.Statement x) {
		setCurrentAST(x.getStatement());
		return x.getStatement().accept(this);
	}
	
	@Override
	public Result<IValue> visitCommandExpression(Expression x) {
		setCurrentAST(x.getExpression());
		return x.getExpression().accept(this);
	}
	
	@Override
	public Result<IValue> visitCommandImport(Import x) {
		return x.getImported().accept(this);
	}
	
	@Override
	public Result<IValue> visitShellCommandHelp(Help x) {
		printHelpMessage(System.out);
		return ResultFactory.nothing();
	}
	
	@Override
	public Result<IValue> visitShellCommandUnimport(Unimport x) {
		((ModuleEnvironment) getCurrentEnvt().getRoot()).unImport(x.getName().toString());
		return ResultFactory.nothing();
	}

	protected void printHelpMessage(PrintStream out) {
		out.println("Welcome to the Rascal command shell.");
		out.println();
		out.println("Shell commands:");
		out.println(":help                      Prints this message");
		out.println(":quit or EOF               Quits the shell");
		out.println(":list                      Lists all visible rules, functions and variables");
		out.println(":set <option> <expression> Sets an option");
		out.println(":edit <modulename>         Opens an editor for that module");
		out.println(":modules                   Lists all imported modules");
		out.println(":test                      Runs all unit tests currently loaded");
		out.println(":unimport <modulename>     Undo an import");
		out.println(":undeclare <name>          Undeclares a variable or function introduced in the shell");
		out.println(":history                   Print the command history");
		out.println();
		out.println("Example rascal statements and declarations:");
		out.println("1 + 1;                     Expressions simply print their output and (static) type");
		out.println("int a;                     Declarations allocate a name in the current scope");
		out.println("a = 1;                     Assignments store a value in a (optionally previously declared) variable");
		out.println("int a = 1;                 Declaration with initialization");
		out.println("import IO;                 Importing a module makes its public members available");
		out.println("println(\"Hello World\")     Function calling");
		out.println();
		out.println("Please read the manual for further information");
	}

	@Override
	public Result<IValue> visitShellCommandQuit(Quit x) {
		throw new QuitException();
	}

	@Override
	public Result<IValue> visitShellCommandEdit(Edit x) {
		return null;
	}

	@Override
	public Result<IValue> visitShellCommandTest(Test x) {
		List<FailedTestError> report = runTests();
		return ResultFactory.makeResult(tf.stringType(), vf.string(report(report)), this);
	}

	@Override
	public Result<IValue> visitShellCommandListDeclarations(ListDeclarations x) {
		PrintStream out = System.out;
		printVisibleDeclaredObjects(out);
		return ResultFactory.nothing();
	}

	protected void printVisibleDeclaredObjects(PrintStream out) {
		List<Entry<String, OverloadedFunctionResult>> functions = getCurrentEnvt().getAllFunctions();
		if (functions.size() != 0) {
			out.println("Functions:");

			for (Entry<String, OverloadedFunctionResult> cand : functions) {
				for (AbstractFunction func : cand.getValue().iterable()) {
					out.print('\t');
					out.println(func.getHeader());
				}
			}
		}
		

		List<RewriteRule> rules = getHeap().getRules();
		if (rules.size() != 0) {
			out.println("Rules:");
			for (RewriteRule rule : rules) {
				out.print('\t');
				out.println(rule.getRule().getPattern().toString());
			}
		}
		
		Map<String, Result<IValue>> variables = getCurrentEnvt().getVariables();
		if (variables.size() != 0) {
			out.println("Variables:");
			for (String name : variables.keySet()) {
				out.print('\t');
				Result<IValue> value = variables.get(name);
				out.println(value.getType() + " " + name + " = " + value.getValue());
			}
		}
	}
	
	@Override
	protected void evalSDFModule(Default x) {
		if (currentEnvt == rootScope) {
			parser.addSdfImportForImportDefault(x);
		}
		super.evalSDFModule(x);
	}
	
	public IConstructor parseModule(String module, ModuleEnvironment env) throws IOException {
		return loader.parseModule(FileURIResolver.STDIN_URI, module, env);
	}


}