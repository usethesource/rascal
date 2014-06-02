/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Jurgen Vinju
 */

package org.rascalmpl.test.infrastructure;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.HashMap;

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

public class RascalJUnitCompiledTestRunner extends Runner {
	private static Evaluator evaluator;
	private static GlobalEnvironment heap;
	private static ModuleEnvironment root;
	private static PrintWriter stderr;
	private static PrintWriter stdout;
	private Description desc;
	private String prefix;
	private HashMap<String, Integer> testCount;

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
		
		System.err.println("**** Loading Compiler (takes a minute!) ***");
		evaluator.doImport(monitor, "experiments::Compiler::Execute");		
		System.err.println("**** Compiler Loaded ***");
	}  
	
	public RascalJUnitCompiledTestRunner(Class<?> clazz) {
		this(clazz.getAnnotation(RascalJUnitTestPrefix.class).value());
		desc = null;
		testCount = new HashMap<String, Integer>();
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
			throw new ImplementationError("could not setup tests for: " + clazz.getCanonicalName(), e);
		} catch (IllegalAccessException e) {
			throw new ImplementationError("could not setup tests for: " + clazz.getCanonicalName(), e);
		}
	}
	
	public RascalJUnitCompiledTestRunner(String prefix) {
	  // remove all the escapes (for example in 'lang::rascal::\syntax')
		this.prefix = prefix;
	}
	
	static protected String computeTestName(String name, ISourceLocation loc) {
		String res = name + ":" + loc.getEndLine();
		//System.err.println("computeTestName: " + res);
		return res;
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
				System.err.println("Adding tests for module: " + name);
				evaluator.doImport(new NullRascalMonitor(), name);
				Description modDesc = Description.createSuiteDescription(name);
				desc.addChild(modDesc);
				int count = 0;
				for (AbstractFunction f : heap.getModule(name.replaceAll("\\\\","")).getTests()) {
					if (!(f.hasTag("ignore") || f.hasTag("Ignore") || f.hasTag("ignoreCompiler") || f.hasTag("IgnoreCompiler"))) {
						modDesc.addChild(Description.createTestDescription(getClass(), computeTestName(f.getName(), f.getAst().getLocation())));
						count++;
					}
				}
				testCount.put(name,  count);
				System.err.println(module + ": " + count + " tests");
			}
			
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
		
		System.err.println("run: testCount = " + desc.testCount());

		for (Description mod : desc.getChildren()) {
			
			Listener listener = new Listener(notifier, mod);
			TestExecutor runner = new TestExecutor(evaluator, listener);
			try {
				runner.test(mod.getDisplayName(), testCount.get(mod.getClassName())); 
				listener.done();
			} catch (Exception e) {
				System.err.println("RascalJunitCompiledTestrunner.run: " + mod.getMethodName() + " unexpected exception: " + e.getMessage());
				e.printStackTrace(System.err);
				
			}
			notifier.fireTestStarted(mod);	// Make sure that the test count is decremented for the test suite itself
			notifier.fireTestFinished(mod);
		}

		notifier.fireTestRunFinished(new Result());
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
			System.out.println("RascalJunitCompiledTestRunner.start: " + count);
			notifier.fireTestRunStarted(module);
		}

		@Override
		public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable t) {
			
			//System.err.println("RascalJunitCompiledTestRunner.report: " + successful + ", test = " + test + ", at " + loc + ", message = " + message);
			Description desc = getDescription(test, loc);
			notifier.fireTestStarted(desc);

			if (!successful) {
				notifier.fireTestFailure(new Failure(desc, null)); //t != null ? t : new Exception(message != null ? message : "no message")));
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
