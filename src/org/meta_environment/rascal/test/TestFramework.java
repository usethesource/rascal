package org.meta_environment.rascal.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.interpreter.CommandEvaluator;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.load.FromResourceLoader;
import org.meta_environment.rascal.interpreter.load.ISdfSearchPathContributor;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.uptr.Factory;
import org.meta_environment.values.ValueFactoryFactory;


public class TestFramework {
	private CommandEvaluator evaluator;
	private PrintWriter stderr;
	private PrintWriter stdout;

	public TestFramework() {
		reset();
	}

	protected CommandEvaluator getTestEvaluator() {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("***test***"));
		stderr = new PrintWriter(System.err);
		stdout = new PrintWriter(System.out);
		CommandEvaluator eval = new CommandEvaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout,  root, heap);

		// to load modules from benchmarks
		eval.addModuleLoader(new FromResourceLoader(getClass(), "org/meta_environment/rascal/benchmark"));

		// to load modules from the test directory
		eval.addModuleLoader(new FromResourceLoader(getClass(), "org/meta_environment/rascal/test/data"));

		// to find sdf modules in the test directory
		eval.addSdfSearchPathContributor(new ISdfSearchPathContributor() {
			public List<String> contributePaths() {
				List<String> result = new LinkedList<String>();
				File srcDir = new File(System.getProperty("user.dir"), "src/org/meta_environment/rascal/test/data");
				result.add(srcDir.getAbsolutePath());
				return result;
			}
		});

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
	
	public boolean runRascalTests(String command) {
		try {
			reset();
			execute(command);
			return evaluator.runTests();
		} 
		catch (IOException e) {
			e.printStackTrace();
			throw new ImplementationError("Exception while running test", e);
		}
		finally {
			stderr.flush();
			stdout.flush();
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
			throw new AssertionError(e.getMessage());
		}
		return this;
	}

	public TestFramework prepareMore(String command) {
		try {
			execute(command);

		} catch (Exception e) {
			System.err
					.println("Unhandled exception while preparing test: " + e);
			throw new AssertionError(e.getMessage());
		}
		return this;
	}

	public boolean prepareModule(String name, String module) throws FactTypeUseException {
		try {
			reset();
			ModuleEnvironment env = new ModuleEnvironment(name);
			evaluator.getHeap().addModule(env);
			IConstructor tree = evaluator.parseModule(module, env);
			if (tree.getType() == Factory.ParseTree_Summary) {
				System.err.println(tree);
				return false;
			}
			
			Module mod = new ASTBuilder(new ASTFactory()).buildModule(tree);
			mod.accept(evaluator);
			return true;
		} catch (IOException e) {
			System.err.println("IOException during preparation:" + e);
			throw new AssertionError(e.getMessage());
		}
	}

	private boolean execute(String command) throws IOException {
		IConstructor tree = evaluator.parseCommand(command);

		if (tree.getConstructorType() == Factory.ParseTree_Summary) {
			System.err.println(tree);
			return false;
		}
		
		Command cmd = new ASTBuilder(new ASTFactory()).buildCommand(tree);
		if (cmd.isStatement()) {
			IValue value = evaluator.eval(cmd).getValue();
			if (value == null || !value.getType().isBoolType())
				return false;
			return value.isEqual(evaluator.getValueFactory()
					.bool(true)) ? true : false;
		} else if (cmd.isImport()) {
			evaluator.eval(cmd.getImported());
			return true;
		} else if (cmd.isDeclaration()) {
			evaluator.eval(cmd.getDeclaration());
			return true;
		} else {
			IValue value = evaluator.eval(cmd).getValue();
			if (value == null || !value.getType().isBoolType())
				return false;
			return value.isEqual(evaluator.getValueFactory()
					.bool(true)) ? true : false;
			
			//throw new ImplementationError("Unexpected case in eval: " + cmd);
		}
	}
}
