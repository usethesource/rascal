/**
 * 
 */
package org.meta_environment.rascal.interpreter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import jline.ConsoleReader;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.ast.Command.Declaration;
import org.meta_environment.rascal.ast.Command.Import;
import org.meta_environment.rascal.ast.Command.Shell;
import org.meta_environment.rascal.ast.ShellCommand.Edit;
import org.meta_environment.rascal.ast.ShellCommand.History;
import org.meta_environment.rascal.ast.ShellCommand.Quit;
import org.meta_environment.rascal.interpreter.control_exceptions.QuitException;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.parser.CommandParser;

public class CommandEvaluator extends Evaluator {
	private final ConsoleReader console;
	private CommandParser parser;

	CommandEvaluator(IValueFactory f, ASTFactory astFactory, Writer errorWriter,
			ModuleEnvironment scope, GlobalEnvironment heap, ConsoleReader console) {
		super(f, astFactory, errorWriter, scope, heap);
		this.parser = new CommandParser(loader);
		this.console = console;
	}
	
	
	public CommandEvaluator(IValueFactory valueFactory, ASTFactory factory,
			PrintWriter printWriter, ModuleEnvironment root,
			GlobalEnvironment heap) {
		this(valueFactory, factory, printWriter, root, heap, null);
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

	@Override
	public Result<IValue> visitShellCommandHistory(History x) {
		try {
			console.printString(console.getHistory().toString());
		} catch (IOException e) {
			// should not happen
		}
		
		return null;
	}
	
	@Override
	public Result<IValue> visitImportDefault(
			org.meta_environment.rascal.ast.Import.Default x) {
		// TODO support for full complexity of import declarations
		// TODO: refactor to have less duplication with Evaluator
		String name = x.getModule().getName().toString();
		if (name.startsWith("\\")) {
			name = name.substring(1);
		}

		if (!heap.existsModule(name) && !loader.isSdfModule(name)) {
			evalModule(x, name);
		}
		else if (loader.isSdfModule(name)) {
			parser.addSdfImportForImportDefault(x);
			String parseTreeModName = "ParseTree";
			if (!heap.existsModule(parseTreeModName)) {
				evalModule(x, parseTreeModName);
			}
			scopeStack.peek().addImport(parseTreeModName, heap.getModule(parseTreeModName, x));
			return ResultFactory.nothing(); 
		}
		else {
			if (importResetsInterpreter && scopeStack.size() == 1 && currentEnvt.getParent() == null) {
				reloadAll(x);
			}
		}

		scopeStack.peek().addImport(name, heap.getModule(name, x));
		return ResultFactory.nothing();
	}


	public IConstructor parseModule(String module) throws IOException {
		return loader.parseModule("-", "-", module);
	}


}