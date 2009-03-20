package test;

import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.load.FromResourceLoader;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;


public class TestFramework {
	private Parser parser = Parser.getInstance();
	private ASTFactory factory = new ASTFactory();
	private ASTBuilder builder = new ASTBuilder(factory);
	private Evaluator evaluator;

	public TestFramework() {
		reset();
	}

	protected Evaluator getTestEvaluator() {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment(
				"***test***"));
		Evaluator eval = new Evaluator(ValueFactoryFactory.getValueFactory(),
				factory, new PrintWriter(System.err), root, heap);

		// to load modules from benchmarks and demo's
		eval.addModuleLoader(new FromResourceLoader(getClass()));

		// to load modules from the test directory without qualification
		eval.addModuleLoader(new FromResourceLoader(getClass(), "test"));

		eval.setImportResetsInterpreter(false);
		return eval;
	}

	private void reset() {
		evaluator = getTestEvaluator();
	}

	public TestFramework(String command) {
		try {
			prepare(command);
		} catch (Exception e) {
			throw new ImplementationError(
					"Exception while creating TestFramework", e);
		}
	}

	public boolean runTest(String command) {
		try {
			reset();
			return execute(command);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ImplementationError("Exception while running test", e);
		}
	}

	public boolean runTestInSameEvaluator(String command) {
		try {
			return execute(command);
		} catch (IOException e) {
			throw new ImplementationError("Exception while running test", e);
		}
	}

	public boolean runTest(String command1, String command2) {
		try {
			reset();
			execute(command1);
			return execute(command2);
		} catch (IOException e) {
			throw new ImplementationError("Exception while running test", e);
		}
	}

	public TestFramework prepare(String command) {
		try {
			reset();
			execute(command);

		} catch (Exception e) {
			System.err
					.println("Unhandled exception while preparing test: " + e);
			e.printStackTrace();
		}
		return this;
	}

	public TestFramework prepareMore(String command) {
		try {
			execute(command);

		} catch (Exception e) {
			System.err
					.println("Unhandled exception while preparing test: " + e);
		}
		return this;
	}

	public boolean prepareModule(String module) throws FactTypeUseException {
		try {
			IConstructor tree = parser.parseFromString(module, "-");
			if (tree.getType() == Factory.ParseTree_Summary) {
				System.err.println(tree);
				return false;
			} else {
				reset();
				Module mod = builder.buildModule(tree);
				mod.accept(evaluator);
				return true;
			}
		} catch (IOException e) {
			System.err.println("IOException during preparation:" + e);
			return false;
		}
	}

	private boolean execute(String command) throws IOException {
		IConstructor tree = parser.parseFromString(command, "-");

		if (tree.getConstructorType() == Factory.ParseTree_Summary) {
			System.err.println(tree);
			return false;
		} else {
			Command cmd = builder.buildCommand(tree);
			if (cmd.isStatement()) {
				IValue value = evaluator.eval(cmd.getStatement());
				if (value == null || !value.getType().isBoolType())
					return false;
				return value.isEqual(ValueFactoryFactory.getValueFactory()
						.bool(true)) ? true : false;
			} else if (cmd.isImport()) {
				evaluator.eval(cmd.getImported());
				return true;
			} else if (cmd.isDeclaration()) {
				evaluator.eval(cmd.getDeclaration());
				return true;
			} else {
				throw new ImplementationError("Unexpected case in eval: "
						+ cmd);
			}
		}
	}
}
