/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Jurgen Vinju, Paul Klint, Davy Landman
 */

package org.rascalmpl.test.infrastructure;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.TestExecutor;
import org.rascalmpl.uri.ClassResourceInput;
import org.rascalmpl.uri.IURIInputStreamResolver;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * A JUnit test runner for compiled Rascal tests.
 * 
 * The current approach is hybrid:
 *  - The Rascal source files are parsed to extract the tests.
 *  - Next the the compiled version of the tests is executed.
 *  - If needed, tests are recompiled.
 *  
 *  To achieve this the Rascal sources of the compiler are loaded in the interpreter.
 *  
 *  In a future version we could skip the parsing phase and extract test info from meta-infor
 *  in the generated code.
 *
 */
public class RascalJUnitCompiledTestRunner extends Runner {
	private static Evaluator evaluator;
	private static GlobalEnvironment heap;
	private static ModuleEnvironment root;
	private static PrintWriter stderr;
	private static PrintWriter stdout;
	private Description desc;
	private String prefix;
	private HashMap<String, Integer> testsPerModule; 				// number of tests to be executed
	private HashMap<String, List<Description>> ignoredPerModule;	// tests to be ignored
	int totalTests = 0;

	static {
		heap = new GlobalEnvironment();
		root = heap.addModule(new ModuleEnvironment("___junit_test___", heap));

		stderr = new PrintWriter(System.err);
		stdout = new PrintWriter(System.out);
		evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout,  root, heap);
		evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
		evaluator.getConfiguration().setErrors(true);

		// Import the compiler's Execute module
		NullRascalMonitor monitor = new NullRascalMonitor();
		
		System.err.println("*********************************************");
		System.err.println("**** Loading Compiler (takes a minute!)  ****");
		System.err.println("**** Needs JVM arguments: -Xms64m -Xmx1G ****");
		evaluator.doImport(monitor, "experiments::Compiler::Execute");		
		System.err.println("****          Compiler Loaded            ****");
		System.err.println("*********************************************");
	}  
	
	public RascalJUnitCompiledTestRunner(Class<?> clazz) {
		this(clazz.getAnnotation(RascalJUnitTestPrefix.class).value());
		desc = null;
		testsPerModule = new HashMap<String, Integer>();
		ignoredPerModule = new HashMap<String, List<Description>>();
		totalTests = 0;
		try {
			Object instance = clazz.newInstance();
			if (instance instanceof IRascalJUnitTestSetup) {
				((IRascalJUnitTestSetup) instance).setup(evaluator);
			}
			else {
				IURIInputStreamResolver resolver = new ClassResourceInput(evaluator.getResolverRegistry(), "junit", clazz, "/");
				evaluator.getResolverRegistry().registerInput(resolver);
				evaluator.addRascalSearchPath(URIUtil.rootScheme("junit"));
			}
		} catch (InstantiationException e) {
			throw new ImplementationError("Could not setup tests for: " + clazz.getCanonicalName(), e);
		} catch (IllegalAccessException e) {
			throw new ImplementationError("Could not setup tests for: " + clazz.getCanonicalName(), e);
		}
	}
	
	public RascalJUnitCompiledTestRunner(String prefix) {
	  // remove all the escapes (for example in 'lang::rascal::\syntax')
		this.prefix = prefix;
	}
	
	@Override
	public int testCount(){
		getDescription();
		System.err.println("testCount: " + totalTests);
		return totalTests;
	}
	
	private void cleanUp(){
		desc = null;
		testsPerModule = null;
		ignoredPerModule = null;
	}
	
	static protected String computeTestName(String name, ISourceLocation loc) {
		return name + ": <" + loc.getOffset() +"," + loc.getLength() +">";
	}
	
	@Override
	public Description getDescription() {			
		if(desc != null)
			return desc;
		Description desc = Description.createSuiteDescription(prefix);
		this.desc = desc;

		try {
			String[] modules = evaluator.getResolverRegistry().listEntries(URIUtil.create("rascal", "", "/" + prefix.replaceAll("::", "/")));

			for (String module : modules) {
				if (!module.endsWith(".rsc")) {
					continue;
				}
				String name = prefix + "::" + module.replaceFirst(".rsc", "");
				
				evaluator.doImport(new NullRascalMonitor(), name);
				Description modDesc = Description.createSuiteDescription(name);
				desc.addChild(modDesc);
				int ntests = 0;
				LinkedList<Description> module_ignored = new LinkedList<Description>();
				
				for (AbstractFunction f : heap.getModule(name.replaceAll("\\\\","")).getTests()) {
						String test_name = computeTestName(f.getName(), f.getAst().getLocation());
						Description d = Description.createTestDescription(getClass(), test_name);
						modDesc.addChild(d);
						ntests++;
						
						if ((f.hasTag("ignore") || f.hasTag("Ignore") || f.hasTag("ignoreCompiler") || f.hasTag("IgnoreCompiler"))) {
							module_ignored.add(d);
						}
				}
				testsPerModule.put(name,  ntests);
				ignoredPerModule.put(name, module_ignored);
				System.err.println("Added " + ntests + " tests for module: " + name);
				totalTests += ntests;
			}
			System.err.println("Total number of tests: " + totalTests);
			return desc;
		} catch (IOException e) {
			throw new RuntimeException("could not create test suite", e);
		} catch (URISyntaxException e) {
			throw new RuntimeException("could not create test suite", e);
		}
	}

	@Override
	public void run(final RunNotifier notifier) {
		if (desc == null) {
			desc = getDescription();
		}
		notifier.fireTestRunStarted(desc);

		for (Description mod : desc.getChildren()) {
			
			Listener listener = new Listener(notifier, mod);
			TestExecutor runner = new TestExecutor(evaluator, listener);
			try {
				runner.test(mod.getDisplayName(), testsPerModule.get(mod.getClassName())); 
				for(Description d : ignoredPerModule.get(mod.getClassName())){
					notifier.fireTestIgnored(d);
				}
				listener.done();
			} catch (Exception e) {
				// Something went totally wrong while running the compiled tests, force all tests in this suite to fail.
				System.err.println("RascalJunitCompiledTestrunner.run: " + mod.getMethodName() + " unexpected exception: " + e.getMessage());
				e.printStackTrace(System.err);
				notifier.fireTestFailure(new Failure(mod, e));
			}
		}

		notifier.fireTestRunFinished(new Result());
		cleanUp();
	}

	private final class Listener implements ITestResultListener {
		private final RunNotifier notifier;
		private final Description module;

		private Listener(RunNotifier notifier, Description module) {
			this.notifier = notifier;
			this.module = module;
		}

		private Description getDescription(String name, ISourceLocation loc) {
			String testName = computeTestName(name, loc);

			for (Description child : module.getChildren()) {
				if (child.getMethodName().equals(testName)) {
					return child;
				}
			}

			throw new IllegalArgumentException(name + " test was never registered");
		}

		@Override
		public void start(int count) {
			//System.out.println("RascalJunitCompiledTestRunner.start: " + count);
			notifier.fireTestRunStarted(module);
		}

		@Override
		public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable t) {
			
			//System.err.println("RascalJunitCompiledTestRunner.report: " + successful + ", test = " + test + ", at " + loc + ", message = " + message);
			Description desc = getDescription(test, loc);
			notifier.fireTestStarted(desc);

			if (!successful) {
				notifier.fireTestFailure(new Failure(desc, t != null ? t : new AssertionError(message == null ? "test failed" : message)));
			}
			else {
				notifier.fireTestFinished(desc);
			}
		}

		@Override
		public void done() {
			notifier.fireTestRunFinished(new Result());
		}
	}
}
