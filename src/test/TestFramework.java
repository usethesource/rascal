package test;

import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;
import org.meta_environment.rascal.interpreter.errors.RunTimeError;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public class TestFramework  {
	private Parser parser = Parser.getInstance();
	private ASTFactory factory = new ASTFactory();
	private ASTBuilder builder = new ASTBuilder(factory);
	private Evaluator evaluator;
	
	public TestFramework () {
		evaluator = new Evaluator(ValueFactory.getInstance(), factory,
				new PrintWriter(System.err), new ModuleEnvironment("***test***"));
	}
	
	public TestFramework (String  command){
		try {
			prepare(command);
		} catch (Exception e){
			throw new RunTimeError("Exception while creating TestFramework", e);
		}
	}
	
	public boolean runTest(String command) {
		try {
		evaluator = new Evaluator(ValueFactory.getInstance(), factory,
				new PrintWriter(System.err), new ModuleEnvironment("***test***"));
		return execute(command);
		} catch (IOException e){
			throw new RunTimeError("Exception while running test", e);
		}
	}
	
	public boolean runTestInSameEvaluator(String command) {
		try {
			return execute(command);
		} catch (IOException e){
			throw new RunTimeError("Exception while running test", e);
		}
	}

	public boolean runTest(String command1, String command2) {
		try {
			evaluator = new Evaluator(ValueFactory.getInstance(), factory,
					        new PrintWriter(System.err), new ModuleEnvironment("***test***"));
			execute(command1);
			return execute(command2);
		} catch (IOException e){
			throw new RunTimeError("Exception while running test", e);
		}
	}
	
	public TestFramework prepare(String command){
		try{
			evaluator = new Evaluator(ValueFactory.getInstance(), factory,
					new PrintWriter(System.err), new ModuleEnvironment("***test***"));
			execute(command);
			
		} catch (Exception e){
			System.err.println("Unhandled exception while preparing test: " + e);
		}
		return this;
	}
	
	public TestFramework prepareMore(String command) {
		try{
			execute(command);
			
		} catch (IOException e){
			System.err.println("Unhandled exception while preparing test: " + e);
		}
		return this;
	}
	
	public boolean prepareModule(String module) {
		try {
			IConstructor tree = parser.parseFromString(module);
			if (tree.getType() == Factory.ParseTree_Summary) {
				System.err.println(tree);
				return false;
			} else {
				evaluator = new Evaluator(ValueFactory.getInstance(), factory,
						new PrintWriter(System.err), new ModuleEnvironment("***test***"));
				Module mod = builder.buildModule(tree);
				mod.accept(evaluator);
				return true;
			}
		} catch (IOException e){
			System.err.println("Unhandled exception while preparing test: " + e);
		}
		return false;
	}

	private boolean execute(String command) throws IOException {
		IConstructor tree = parser.parseFromString(command);

		if (tree.getConstructorType() == Factory.ParseTree_Summary) {
			System.err.println(tree);
			return false;
		} else {
			Command cmd = builder.buildCommand(tree);
			if (cmd.isStatement()) {
				IValue value = evaluator.eval(cmd.getStatement());
				if (value == null || !value.getType().isBoolType())
					return false;
				return value.isEqual(ValueFactory.getInstance().bool(true)) ? true
						: false;
			} else if (cmd.isImport()) {
				evaluator.eval(cmd.getImported());
				return true;
			} else if (cmd.isDeclaration()) {
				evaluator.eval(cmd.getDeclaration());
				return true;
			} else {
				throw new ImplementationError("Unexpected case in eval: " + cmd, cmd);
			}
		}
	}
}
