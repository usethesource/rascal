/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Paul Klint, Jurgen Vinju
 */

package org.rascalmpl.test.infrastructure;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.TestEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.uri.ClassResourceInput;
import org.rascalmpl.uri.IURIInputStreamResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

public class RascalJUnitTestRunner extends Runner {
	private static Evaluator evaluator;
	private static GlobalEnvironment heap;
	private static ModuleEnvironment root;
	private static PrintWriter stderr;
	private static PrintWriter stdout;
	private Description desc;
	private String prefix;

	static {
		heap = new GlobalEnvironment();
		root = heap.addModule(new ModuleEnvironment("___junit_test___", heap));
		
		stderr = new PrintWriter(System.err);
		stdout = new PrintWriter(System.out);
		evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout,  root, heap);
		evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
		evaluator.getConfiguration().setErrors(true);
	}  
	
	public RascalJUnitTestRunner(Class<?> clazz) {
		this(clazz.getAnnotation(RascalJUnitTestPrefix.class).value());
		try {
      Object instance = clazz.newInstance();
      if (instance instanceof IRascalJUnitTestSetup) {
        ((IRascalJUnitTestSetup) instance).setup(evaluator);
      }
      else {
        IURIInputStreamResolver resolver = new ClassResourceInput(evaluator.getResolverRegistry(), "junit", clazz, "/");
        evaluator.getResolverRegistry().registerInput(resolver);
        evaluator.addRascalSearchPath(URIUtil.rootScheme("junit"));
        evaluator.addRascalSearchPath(URIUtil.rootScheme("tmp"));
      }
    } catch (InstantiationException e) {
      throw new ImplementationError("could not setup tests for: " + clazz.getCanonicalName(), e);
    } catch (IllegalAccessException e) {
      throw new ImplementationError("could not setup tests for: " + clazz.getCanonicalName(), e);
    }
	}
	
	public RascalJUnitTestRunner(String prefix) {
	  // remove all the escapes (for example in 'lang::rascal::\syntax')
		this.prefix = prefix;
	}
	
	static protected String computeTestName(String name, ISourceLocation loc) {
		return name + ": <" + loc.getOffset() +"," + loc.getLength() +">";
	}
	
	static protected List<String> getRecursiveModuleList(URI root, URIResolverRegistry reg) throws IOException {
		List<String> result = new ArrayList<>();
		Queue<URI> todo = new LinkedList<>();
		todo.add(root);
		while (!todo.isEmpty()) {
			URI currentDir = todo.poll();
			String prefix = currentDir.getPath().replaceFirst(root.getPath(), "").replaceFirst("/", "").replaceAll("/", "::");
			for (String ent : reg.listEntries(currentDir)) {
				if (ent.endsWith(".rsc")) {
					if (prefix.isEmpty()) {
						result.add(ent.replace(".rsc", ""));
					}
					else {
						result.add(prefix + "::" + ent.replace(".rsc", ""));
					}
				}
				else {
					URI possibleDir;
					try {
						possibleDir = URIUtil.changePath(currentDir, currentDir.getPath() + "/" + ent);
						if (reg.isDirectory(possibleDir)) {
							todo.add(possibleDir);
						}
					} catch (URISyntaxException e) {
					}
				}
			}
		}
		return result;
		
	}
	
	@Override
	public Description getDescription() {		
		Description desc = Description.createSuiteDescription(prefix);
		this.desc = desc;
		
		try {
			List<String> modules = getRecursiveModuleList(URIUtil.create("std", "", "/" + prefix.replaceAll("::", "/")), evaluator.getResolverRegistry());
			
			for (String module : modules) {
				String name = prefix + "::" + module;
				
				try {
					evaluator.doImport(new NullRascalMonitor(), name);
				}
				catch (Throwable e) {
					throw new RuntimeException("Could not import " + name + " for testing...", e);
				}
				
				Description modDesc = Description.createSuiteDescription(name);
				desc.addChild(modDesc);
				
				for (AbstractFunction f : heap.getModule(name.replaceAll("\\\\","")).getTests()) {
				  if (!(f.hasTag("ignore") || f.hasTag("Ignore") || f.hasTag("ignoreInterpreter") || f.hasTag("IgnoreInterpreter"))) {
				    modDesc.addChild(Description.createTestDescription(getClass(), computeTestName(f.getName(), f.getAst().getLocation())));
				  }
				}
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

		for (Description mod : desc.getChildren()) {
			TestEvaluator runner = new TestEvaluator(evaluator, new Listener(notifier, mod));
			runner.test(mod.getDisplayName());
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
			notifier.fireTestRunStarted(module);
		}
	
		@Override
		public void report(boolean successful, String test, ISourceLocation loc,	String message, Throwable t) {
			Description desc = getDescription(test, loc);
			notifier.fireTestStarted(desc);
			
			if (!successful) {
				notifier.fireTestFailure(new Failure(desc, t != null ? t : new Exception(message != null ? message : "no message")));
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
