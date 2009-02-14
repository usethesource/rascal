/**
 * 
 */
package org.meta_environment.rascal.interpreter;

import java.io.IOException;

import jline.ConsoleReader;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Command.Declaration;
import org.meta_environment.rascal.ast.Command.Import;
import org.meta_environment.rascal.ast.Command.Shell;
import org.meta_environment.rascal.ast.ShellCommand.Edit;
import org.meta_environment.rascal.ast.ShellCommand.History;
import org.meta_environment.rascal.ast.ShellCommand.Quit;
import org.meta_environment.rascal.interpreter.control_exceptions.FailureControlException;
import org.meta_environment.rascal.interpreter.env.Result;
import org.meta_environment.rascal.interpreter.errors.RascalTypeException;

/*package*/ class CommandEvaluator extends NullASTVisitor<IValue> {
	private final ConsoleReader console;
	private final Evaluator evaluator;

	CommandEvaluator(Evaluator evaluator, ConsoleReader console) {
		this.console = console;
		this.evaluator = evaluator;
	}
	
	public CommandEvaluator(Evaluator evaluator) {
		this.evaluator = evaluator;
		this.console = null;
	}

	public IValue eval(Command command) {
		return command.accept(this);
	}

	@Override
	public IValue visitCommandShell(Shell x) {
		return x.getCommand().accept(this);
	}

	@Override
	public IValue visitCommandDeclaration(Declaration x) {
		return evaluator.eval(x.getDeclaration());
	}

	@Override
	public IValue visitCommandStatement(
			org.meta_environment.rascal.ast.Command.Statement x) {
		return evaluator.eval(x.getStatement());
	}
	
	@Override
	public IValue visitCommandImport(Import x) {
		Result r = x.getImported().accept(evaluator);
		return r.value;
	}

	@Override
	public IValue visitShellCommandQuit(Quit x) {
		throw new FailureControlException();
	}

	@Override
	public IValue visitShellCommandEdit(Edit x) {
		// TODO implement this properly
		String editor = System.getenv("EDITOR");
		
		if (editor == null) {
			throw new RascalTypeException("EDITOR environment variable is not set.");
		}
		
		String file = x.getName().toString();
		
		if (!file.endsWith(Evaluator.RASCAL_FILE_EXT)) {
			file = file + Evaluator.RASCAL_FILE_EXT;
		}
		
		try {
			Process p = Runtime.getRuntime().exec(editor + " " + file);
			p.waitFor();
		} catch (IOException e) {
			throw new RascalTypeException("Editing failed: ", e);
		} catch (InterruptedException e) {
			// might happen, don't know a sensible thing to do
		}
		
		return null;
	}

	@Override
	public IValue visitShellCommandHistory(History x) {
		try {
			console.printString(console.getHistory().toString());
		} catch (IOException e) {
			// should not happen
		}
		
		return null;
	}
}