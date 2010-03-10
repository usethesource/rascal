package org.rascalmpl.checker;

import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.ast.ASTFactory;
import org.rascalmpl.ast.Command;
import org.rascalmpl.interpreter.CommandEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.values.ValueFactoryFactory;

public class StaticChecker {
	private final CommandEvaluator eval;
	private final ASTBuilder astBuilder;
	
	private static final class InstanceKeeper {
		public static final StaticChecker sInstance = new StaticChecker();
	}

	private StaticChecker() {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("***static-checker***"));
		PrintWriter stderr = new PrintWriter(System.err);
		PrintWriter stdout = new PrintWriter(System.out);

		this.eval = new CommandEvaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout,  root, heap);
		this.astBuilder = new ASTBuilder(new ASTFactory());
		
		eval("import rascal::checker::Check;");
	}
	
	public static StaticChecker getInstance() {
		return InstanceKeeper.sInstance;
	}

	private void eval(String cmd) {
		try {
			Command cmdAst = astBuilder.buildCommand(eval.parseCommand(cmd));
			eval.eval(cmdAst);
		} catch (IOException e) {
			throw new ImplementationError("static checker failed to execute command: " + cmd, e);
		}
		
	}
	
	public IConstructor checkModule(IConstructor moduleParseTree) {
		return (IConstructor) eval.call("checkTree" , moduleParseTree);
	}
}
