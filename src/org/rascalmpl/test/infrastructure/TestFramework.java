/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Emilie Balland - (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Davy Landman - Davy.Landman@cwi.nl
*******************************************************************************/
package org.rascalmpl.test.infrastructure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.uri.ClassResourceInputOutput;
import org.rascalmpl.uri.IURIInputStreamResolver;
import org.rascalmpl.uri.JarURIResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
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

		public boolean supportsHost() {
			return false;
		}
	}
	
	static{
		heap = new GlobalEnvironment();
		root = heap.addModule(new ModuleEnvironment("___test___", heap));
		modules = new TestModuleResolver();
		
		stderr = new PrintWriter(System.err);
		stdout = new PrintWriter(System.out);
		evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout,  root, heap);
		URIResolverRegistry resolverRegistry = evaluator.getResolverRegistry();
		
		resolverRegistry.registerInput(new JarURIResolver(TestFramework.class));
		
		evaluator.addRascalSearchPath(URIUtil.rootScheme("test-modules"));
		resolverRegistry.registerInput(modules);
		
		evaluator.addRascalSearchPath(URIUtil.rootScheme("benchmarks"));
		resolverRegistry.registerInput(new ClassResourceInputOutput(resolverRegistry, "benchmarks", Evaluator.class, "/org/rascalmpl/benchmark"));
	}
	
	public TestFramework() {
		super();
	}
	
	private void reset() {
		heap.clear();
		root.reset();
		
		modules.reset();
		
		evaluator.getAccumulators().clear();
	}
	
	@After
	public void assureEvaluatorIsSane() {
		assertTrue(evaluator.getCurrentEnvt().isRootScope());
		assertTrue(evaluator.getCurrentEnvt().isRootStackFrame());
		assertTrue("When we are at the root scope and stack frame, the accumulators should be empty as well", evaluator.getAccumulators().empty());
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
		Result<IValue> result = evaluator.eval(null, command, URIUtil.rootScheme("stdin"));

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
