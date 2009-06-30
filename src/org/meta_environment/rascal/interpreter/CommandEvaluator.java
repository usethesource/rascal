/**
 * 
 */
package org.meta_environment.rascal.interpreter;

import java.io.IOException;
import java.io.Writer;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.ast.Command.Declaration;
import org.meta_environment.rascal.ast.Command.Import;
import org.meta_environment.rascal.ast.Command.Shell;
import org.meta_environment.rascal.ast.Import.Default;
import org.meta_environment.rascal.ast.ShellCommand.Edit;
import org.meta_environment.rascal.ast.ShellCommand.Quit;
import org.meta_environment.rascal.interpreter.control_exceptions.QuitException;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.parser.ConsoleParser;

public class CommandEvaluator extends Evaluator {
//	private final ConsoleReader console;
	private ConsoleParser parser;

	
	public CommandEvaluator(IValueFactory f, Writer errorWriter,
			ModuleEnvironment scope, GlobalEnvironment heap) {
		this(f, errorWriter, scope, heap, new ConsoleParser());
//		this.console = console;
	}
	
	
	public CommandEvaluator(IValueFactory vf, Writer errorWriter,
			ModuleEnvironment root, GlobalEnvironment heap,
			ConsoleParser consoleParser) {
		super(vf, errorWriter, root, heap, consoleParser);
		this.parser = consoleParser;
//		console = null;
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
		return x.getStatement().accept(this);
	}
	
	@Override
	public Result<IValue> visitCommandImport(Import x) {
		return x.getImported().accept(this);
	}

	@Override
	public Result<IValue> visitShellCommandQuit(Quit x) {
		throw new QuitException();
	}

	@Override
	public Result<IValue> visitShellCommandEdit(Edit x) {
		return null;
	}

//	@Override
//	public Result<IValue> visitShellCommandHistory(History x) {
//		try {
//			console.printString(console.getHistory().toString());
//		} catch (IOException e) {
//			// should not happen
//		}
//		
//		return null;
//	}
	
	@Override
	protected void handleSDFModule(Default x) {
		if (currentEnvt == rootScope) {
			parser.addSdfImportForImportDefault(x);
		}
		super.handleSDFModule(x);
	}
	
	public IConstructor parseModule(String module) throws IOException {
		return loader.parseModule("-", "-", module);
	}


}