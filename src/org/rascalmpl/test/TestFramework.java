package org.rascalmpl.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.IRascalSearchPathContributor;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.uri.ClassResourceInputOutput;
import org.rascalmpl.uri.IURIInputStreamResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;


public class TestFramework {
	private Evaluator evaluator;
	private PrintWriter stderr;
	private PrintWriter stdout;
	private TestModuleResolver modules;

	/**
	 * This class allows us to load modules from string values.
	 */
	private class TestModuleResolver implements IURIInputStreamResolver {
		private Map<String,String> modules = new HashMap<String,String>();

		public void addModule(String name, String contents) {
			name = name.replaceAll("::", "/");
			if (!name.startsWith("/")) {
				name = "/" + name;
			}
			if (!name.endsWith(".rsc")) {
				name = name + ".rsc";
			}
			modules.put(name, contents);
		}
		
		public boolean exists(URI uri) {
			return modules.containsKey(uri.getPath());
		}

		public InputStream getInputStream(URI uri) throws IOException {
			String contents = modules.get(uri.getPath());
			if (contents != null) {
				return new ByteArrayInputStream(contents.getBytes());
			}
			return null;
		}

		public String scheme() {
			return "test-modules";
		}
		
		public String absolutePath(URI uri) throws IOException{
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isDirectory(URI uri) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isFile(URI uri) {
			// TODO Auto-generated method stub
			return false;
		}

		public long lastModified(URI uri) {
			// TODO Auto-generated method stub
			return 0;
		}

		public String[] listEntries(URI uri) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public TestFramework() {
		reset();
	}

	protected Evaluator getTestEvaluator() {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("***test***"));
		stderr = new PrintWriter(System.err);
		stdout = new PrintWriter(System.out);
		Evaluator eval = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout,  root, heap);
		URIResolverRegistry resolverRegistry = eval.getResolverRegistry();
		
		resolverRegistry.registerInput(new ClassResourceInputOutput(resolverRegistry, "rascal-test", getClass(), "/"));
		
		// to load modules from benchmarks
		eval.addRascalSearchPathContributor(new IRascalSearchPathContributor() {
			public void contributePaths(List<URI> path) {
				path.add(URI.create("rascal-test:///org/rascalmpl/benchmark"));
				path.add(URI.create("rascal-test:///org/rascalmpl/test/data"));
			}
			
			@Override
			public String toString() {
				return "[test library]";
			}
		});

		return eval;
	}

	private void reset() {
		evaluator = getTestEvaluator();
		this.modules = new TestModuleResolver();
		evaluator.getResolverRegistry().registerInput(this.modules);
		evaluator.addRascalSearchPath(URI.create("test-modules:///"));
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
		reset();
		return execute(command);
	}
	
	public boolean runRascalTests(String command) {
		try {
			reset();
			execute(command);
			return evaluator.runTests();
		}
		finally {
			stderr.flush();
			stdout.flush();
		}
	}

	public boolean runTestInSameEvaluator(String command) {
		return execute(command);
	}

	public boolean runTest(String command1, String command2) {
		reset();
		execute(command1);
		return execute(command2);
	}

	public TestFramework prepare(String command) {
		try {
			reset();
			execute(command);

		}
		catch (StaticError e) {
			throw e;
		}
		catch (Exception e) {
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

		}
		catch (StaticError e) {
			throw e;
		}
		catch (Exception e) {
			System.err
					.println("Unhandled exception while preparing test: " + e);
			throw new AssertionError(e.getMessage());
		}
		return this;
	}

	public boolean prepareModule(String name, String module) throws FactTypeUseException {
		reset();
		modules.addModule(name, module);
		return true;
	}

	private boolean execute(String command){
		Result<IValue> result = evaluator.eval(command, URI.create("stdin:///"));

		if (result.getType().isVoidType()) {
			return true;
			
		}
		if (result.getValue() == null) {
			return false;
		}
		
		if (result.getType() == TypeFactory.getInstance().boolType()) {
			return ((IBool) result.getValue()).getValue();
		}
		
		return false;
	}
}
