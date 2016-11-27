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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.library.experiments.Compiler.Commands.Rascal;
import org.rascalmpl.library.experiments.Compiler.Commands.RascalC;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.TestExecutor;
import org.rascalmpl.library.lang.rascal.boot.Kernel;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * A JUnit test runner for compiled Rascal tests.
 * 
 * The  approach is as follows:
 *  - The modules to be tested are compiled and linked.
 *  - Meta-data in the compiled modules is used to determine the number of tests
 *    and the ignored tests.
 *  - Next the the tests are executed per compiled module
 */
public class RascalJUnitCompiledTestRunner extends Runner {
    private static Kernel kernel;
    private static RascalExecutionContext rex;
    private static IValueFactory vf;

    private static PathConfig pcfg;
	private Description desc;
	private String prefix;
	private HashMap<String, Integer> testsPerModule; 				// number of tests to be executed
	private HashMap<String, List<Description>> ignoredPerModule;	// tests to be ignored
	int totalTests = 0;

	static {
	  vf = ValueFactoryFactory.getValueFactory();
	  try {
	    pcfg = new PathConfig();
	  } catch (URISyntaxException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	  }
	  rex = RascalExecutionContextBuilder.normalContext(vf, pcfg.getBoot())
	      .setTrace(false)
	      .setProfile(false)
	      .setVerbose(true)
	      .build();
	  try {
	    kernel = new Kernel(vf, rex, pcfg.getBoot());
	  } catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	  } catch (NoSuchRascalFunction e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	  } catch (URISyntaxException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	  }
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
			  System.err.println("*** IRascalJUnitTestSetup ***");
				((IRascalJUnitTestSetup) instance).setup(null);
			}
			else {
				//evaluator.addRascalSearchPath(URIUtil.rootLocation("tmp"));
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
		totalTests = 0;
	}
	
	public static String computeTestName(String name, ISourceLocation loc) {
	    String base = name;
	    int slash = name.indexOf("/");
	    if(slash > 0){
	      base = name.substring(name.indexOf("/")+1, name.indexOf("(")); // Resembles Function.getPrintableName
	    }
	    return base + ": <" + loc.getOffset() +"," + loc.getLength() +">";
	}

	static protected List<String> getRecursiveModuleList(ISourceLocation root) throws IOException {
		List<String> result = new ArrayList<>();
		Queue<ISourceLocation> todo = new LinkedList<>();
		todo.add(root);
		while (!todo.isEmpty()) {
			ISourceLocation currentDir = todo.poll();
			String prefix = currentDir.getPath().replaceFirst(root.getPath(), "").replaceFirst("/", "").replaceAll("/", "::");
			for (ISourceLocation ent : URIResolverRegistry.getInstance().list(currentDir)) {
				if (ent.getPath().endsWith(".rsc")) {
					if (prefix.isEmpty()) {
						result.add(URIUtil.getLocationName(ent).replace(".rsc", ""));
					}
					else {
						result.add(prefix + "::" + URIUtil.getLocationName(ent).replace(".rsc", ""));
					}
				}
				else {
					ISourceLocation possibleDir = URIUtil.getChildLocation(currentDir, currentDir.getPath() + "/" + ent);
					if (URIResolverRegistry.getInstance().isDirectory(possibleDir)) {
						todo.add(possibleDir);
					}
				}
			}
		}
		return result;
		
	}
	
	@Override
	public Description getDescription() {			
		if(desc != null)
			return desc;
		Description desc = Description.createSuiteDescription(prefix);
		this.desc = desc;

		try {
			List<String> modules = getRecursiveModuleList(vf.sourceLocation("std", "", "/" + prefix.replaceAll("::", "/")));
			
			IListWriter w = vf.listWriter();

			for(String module : modules){
			  w.append(vf.string(prefix + "::" + module));
			}

			IList imodules = w.done();
			IList programs = kernel.compileAndLink(
			    imodules,
			    pcfg.getSrcs(),
			    pcfg.getLibs(),
			    pcfg.getBoot(),
			    pcfg.getBin(),
			    vf.sourceLocation("noreloc", "", ""),
			    new HashMap<String, IValue>());
			
			boolean ok = RascalC.handleMessages(programs);
			if(!ok){
			  System.exit(1);
			}
			
			for (String module : modules) {
			  
				String name = prefix + "::" + module;
				
		        ISourceLocation binary = Rascal.findBinary(pcfg.getBin(), name);
		        RVMExecutable executable = RVMExecutable.read(binary);
				Description modDesc = Description.createSuiteDescription(name);
				desc.addChild(modDesc);
				int ntests = 0;
				LinkedList<Description> module_ignored = new LinkedList<Description>();
				
				for(Function f : executable.getTests()){
				  String test_name = f.computeTestName();
                  Description d = Description.createTestDescription(getClass(), test_name);
                  modDesc.addChild(d);
                  ntests++;
                  
                  if(f.isIgnored()){
                    module_ignored.add(d);
                  }
				}
				
				testsPerModule.put(name,  ntests);
				ignoredPerModule.put(name, module_ignored);
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
			
		  ISourceLocation binary = Rascal.findBinary(pcfg.getBin(), mod.getDisplayName());
          RVMCore rvmCore = null;
          try {
              rvmCore = ExecutionTools.initializedRVM(binary, rex);
          } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
          
			Listener listener = new Listener(notifier, mod);
			TestExecutor runner = new TestExecutor(rvmCore, listener, rex);
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

		private Description getDescription(String testName, ISourceLocation loc) {

			for (Description child : module.getChildren()) {
				if (child.getMethodName().equals(testName)) {
					return child;
				}
			}

			throw new IllegalArgumentException(testName + " test was never registered");
		}

		@Override
		public void ignored(String test, ISourceLocation loc) {
		    notifier.fireTestIgnored(getDescription(test, loc));
		}
		
		@Override
		public void start(String context, int count) {
//			  System.out.println("RascalJunitCompiledTestRunner.start: " + context + ", " + count);
			notifier.fireTestRunStarted(module);
		}

		@Override
		public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable t) {
			
//			System.err.println("RascalJunitCompiledTestRunner.report: " + successful + ", test = " + test + ", at " + loc + ", message = " + message);
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
