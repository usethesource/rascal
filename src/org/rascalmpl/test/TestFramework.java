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
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.IRascalSearchPathContributor;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.uri.IURIInputStreamResolver;
import org.rascalmpl.uri.JarURIResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;


public class TestFramework {
	private final static Evaluator evaluator;
	private final static GlobalEnvironment heap;
	private final static ModuleEnvironment root;
	private final static TestModuleResolver modules;
	
	private final static PrintWriter stderr;
	private final static PrintWriter stdout;

	/**
	 * This class allows us to load modules from string values.
	 */
	private static class TestModuleResolver implements IURIInputStreamResolver {
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
		
		public void reset(){
			modules = new HashMap<String,String>();
		}

		public String scheme() {
			return "test-modules";
		}

		public boolean isDirectory(URI uri) {
			return false;
		}

		public boolean isFile(URI uri) {
			return false;
		}

		public long lastModified(URI uri) {
			return 0;
		}

		public String[] listEntries(URI uri) {
			return null;
		}
	}
	
	static{
		heap = new GlobalEnvironment();
		root = heap.addModule(new ModuleEnvironment("***test***", heap));
		modules = new TestModuleResolver();
		
		stderr = new PrintWriter(System.err);
		stdout = new PrintWriter(System.out);
		evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout,  root, heap);
		URIResolverRegistry resolverRegistry = evaluator.getResolverRegistry();
		
		//resolverRegistry.registerInput(new ClassResourceInputOutput(resolverRegistry, "rascal-test", TestFramework.class, "/"));
		resolverRegistry.registerInput(new JarURIResolver(TestFramework.class));
		resolverRegistry.registerInput(modules);
		
		evaluator.addRascalSearchPath(URI.create("test-modules:///"));
		
		// to load modules from benchmarks
		evaluator.addRascalSearchPathContributor(new IRascalSearchPathContributor() {
			public void contributePaths(List<URI> path) {
				path.add(URI.create("rascal-test:///org/rascalmpl/benchmark"));
				path.add(URI.create("rascal-test:///org/rascalmpl/test/data"));
			}
			
			@Override
			public String toString() {
				return "[test library]";
			}
		});
	}
	
	public TestFramework() {
		super();
	}
	
	private void reset() {
		heap.clear();
		root.reset();
		
		modules.reset();
	}

	public boolean runTest(String command) {
		reset();
		return execute(command);
	}
	
	public boolean runRascalTests(String command) {
		try {
			reset();
			execute(command);
			return evaluator.runTests(evaluator.getMonitor());
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
			System.err.println("Unhandled exception while preparing test: " + e);
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
			System.err.println("Unhandled exception while preparing test: " + e);
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
		Result<IValue> result = evaluator.eval(null, command, URI.create("stdin:///"));

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
