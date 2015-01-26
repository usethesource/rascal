/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Emilie Balland - (CWI)
 *   * Anya Helene Bagge - (UiB)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Davy Landman - Davy.Landman@cwi.nl
 *******************************************************************************/
package org.rascalmpl.test.infrastructure;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.junit.After;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.uri.ClassResourceInput;
import org.rascalmpl.uri.ISourceLocationInput;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;


public class ConcurrentTestFramework {
	private final static int N = 12;
	private final static Evaluator evaluator;
	private Evaluator[] evaluators = null;
	private final static TestModuleResolver modules;

	private final static PrintWriter stderr;
	private final static PrintWriter stdout;

	/**
	 * This class allows us to load modules from string values.
	 */
	private static class TestModuleResolver implements ISourceLocationInput {
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

		@Override
		public boolean exists(ISourceLocation uri) {
			return modules.containsKey(uri.getPath());
		}

		@Override
		public InputStream getInputStream(ISourceLocation uri) throws IOException {
			String contents = modules.get(uri.getPath());
			if (contents != null) {
				return new ByteArrayInputStream(contents.getBytes());
			}
			return null;
		}

		public void reset(){
			modules = new HashMap<String,String>();
		}

		@Override
		public String scheme() {
			return "test-modules";
		}

		@Override
		public boolean isDirectory(ISourceLocation uri) {
			return false;
		}

		@Override
		public boolean isFile(ISourceLocation uri) {
			return false;
		}

		@Override
		public long lastModified(ISourceLocation uri) {
			return 0;
		}

		@Override
		public ISourceLocation[] list(ISourceLocation uri) {
			return null;
		}

		@Override
		public boolean supportsHost() {
			return false;
		}

		@Override
		public Charset getCharset(ISourceLocation uri) throws IOException {
			return null;
		}
	}

	static{
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("___test___", heap));
		modules = new TestModuleResolver();

		stderr = new PrintWriter(System.err);
		stdout = new PrintWriter(System.out);
		evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout,  root, heap);
		URIResolverRegistry resolverRegistry = URIResolverRegistry.getInstance();

		evaluator.addRascalSearchPath(URIUtil.rootLocation("test-modules"));
		resolverRegistry.registerInput(modules);

		evaluator.addRascalSearchPath(URIUtil.rootLocation("benchmarks"));
		resolverRegistry.registerInput(new ClassResourceInput("benchmarks", Evaluator.class, "/org/rascalmpl/benchmark"));

	}

	public ConcurrentTestFramework() {
		super();
	}

	private void reset() {
		evaluator.getHeap().clear();
		evaluator.__getRootScope().reset();
		modules.reset();
		evaluators = null; 
		evaluator.getAccumulators().clear();
	}

	private void forkEvaluators() {
		evaluator.freeze();
		evaluators = new Evaluator[N];
		synchronized(evaluators) {
			evaluators[0] = evaluator;
			for(int i = 1; i < evaluators.length; i++) {
				evaluators[i]= (Evaluator) evaluator.fork();
			}
		}
	}

	@After
	public void assureEvaluatorIsSane() {
		assertTrue(evaluator.getCurrentEnvt().isRootScope());
		assertTrue(evaluator.getCurrentEnvt().isRootStackFrame());
		assertTrue("When we are at the root scope and stack frame, the accumulators should be empty as well", evaluator.getAccumulators().empty());
		for(int i = 0; i < evaluators.length; i++) {
			assertTrue(evaluators[i].getCurrentEnvt().isRootScope());
			assertTrue(evaluators[i].getCurrentEnvt().isRootStackFrame());
			assertTrue("When we are at the root scope and stack frame, the accumulators should be empty as well", evaluators[i].getAccumulators().empty());
		}
	}

	public boolean runTest(String command) {
		reset();
		forkEvaluators();
		return executeInThreads(command);
	}

	public boolean runRascalTests(final String command) {
		try {
			reset();
			forkEvaluators();
			return runConcurrently(new ForkedRunnable() {
				@Override
				public boolean run(Evaluator eval) {
					execute(command, eval);
					return eval.runTests(eval.getMonitor());
				}
			});
		}
		finally {
			stderr.flush();
			stdout.flush();
		}
	}

	public boolean runTestInSameEvaluator(String command) {
		if(evaluators == null) {
			forkEvaluators();
		}
		return executeInThreads(command);
	}

	public boolean runTest(String command1, String command2) {
		reset();
		execute(command1, evaluator);
		return executeInThreads(command2);
	}

	public ConcurrentTestFramework prepare(String command) {
		try {
			reset();
			execute(command, evaluator);
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

	public ConcurrentTestFramework prepareMore(String command) {
		try {
			execute(command, evaluator);

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

	private boolean execute(String command, Evaluator eval){
		Result<IValue> result = eval.eval(null, command, URIUtil.rootLocation("stdin"));

		if (result.getType().isBottom()) {
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

	private boolean executeInThreads(final String command){
		return runConcurrently(new ForkedRunnable() {
			@Override
			public boolean run(Evaluator eval) {
				Result<IValue> result = eval.eval(null, command, URIUtil.rootLocation("stdin"));

				if (result.getType().isBottom()) {
					return true;

				}
				if (result.getValue() == null) {
					return false;
				}

				if (result.getType() == TypeFactory.getInstance().boolType()) {
					return ((IBool) result.getValue()).getValue();
				}
				return false;
			}});
	}

	boolean runConcurrently(final ForkedRunnable runner) {
		final RuntimeException failure = new RuntimeException();
		final RuntimeException[] results = new RuntimeException[N];
		final Thread[] threads = new Thread[N];

		for(int i = 0; i < N; i++) {
			final int threadNumber = i;
			threads[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					Evaluator eval;
					synchronized(evaluators) {
						eval = evaluators[threadNumber];
					}
					try {
						boolean result = runner.run(eval);
						synchronized(results) {
							results[threadNumber] = result ? null : failure;
						}
					}
					catch(RuntimeException e) {
						synchronized(results) {
							results[threadNumber] = e;
						}
					}
					// System.err.println(threadNumber + ": " + System.identityHashCode(eval.hashCode()) + ", " + System.identityHashCode(eval.getAccumulators()));
				}			
			});
			threads[i].start();
		}
		for(int i = 0; i < N; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				throw new RuntimeException("Thread interrupted", e);
			}
		}
		synchronized(results) {
			for(int i = 0; i < N; i++) {
				if(results[i] == failure) {
					return false;
				}
				else if(results[i] != null) {
					throw results[i];
				}
			}
		}
		return true;

	}

	interface ForkedRunnable {
		boolean run(Evaluator eval);
	}
}