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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.library.experiments.Compiler.Commands.Rascal;
import org.rascalmpl.library.experiments.Compiler.Commands.RascalC;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.TestExecutor;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.Java2Rascal;
import org.rascalmpl.library.lang.rascal.boot.IKernel;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * A JUnit test runner for compiled Rascal tests.
 * 
 * The  approach is as follows:
 *  - The modules to be tested are compiled and linked.
 *  - Meta-data in the compiled modules is used to determine the number of tests and the ignored tests.
 *  - The tests are executed per compiled module
 *  
 * The file IGNORED.config may contain (parts of) module names that will be ignored (using substring comparison)
 */
public class RascalJUnitCompiledTestRunner extends Runner {
    private static final String IGNORED = "test/org/rascalmpl/test_compiled/TESTS.ignored";
    private static IKernel kernel;
    private static IValueFactory vf;

    private static PathConfig pcfg;
	private Description desc;
	private String prefix;
	private HashMap<String, Integer> testsPerModule; 				// number of tests to be executed
	private HashMap<String, List<Description>> ignoredPerModule;	// tests to be ignored
	int totalTests = 0;
	int totalTestsWithRandom = 0;
	static String[] IGNORED_DIRECTORIES;

	static {   
	  vf = ValueFactoryFactory.getValueFactory();
	  try {
	    pcfg = new PathConfig();
	    pcfg.addSourceLoc(URIUtil.rootLocation("tmp"));
	  } catch (URISyntaxException e1) {
	    System.err.println("Could not create tmp as root location");
	    e1.printStackTrace();
	    System.exit(-1);
	  } 

	  System.err.println(pcfg);
	  try {
	    kernel = Java2Rascal.Builder.bridge(vf, pcfg, IKernel.class)
	              .trace(false)
	              .profile(false)
	              .verbose(false)
	              .build();
	  } catch (IOException e) {
	    System.err.println("Unable to load Rascal Kernel");
	    e.printStackTrace();
	    System.exit(-1);
	  }
	}  
	
	public RascalJUnitCompiledTestRunner(Class<?> clazz) {
	  this(clazz.getAnnotation(RascalJUnitTestPrefix.class).value());
	  desc = null;
	  testsPerModule = new HashMap<String, Integer>();
	  ignoredPerModule = new HashMap<String, List<Description>>();
	  totalTests = 0;
	  try (InputStream ignoredStream = new FileInputStream(Paths.get(".").toAbsolutePath().normalize().resolve(IGNORED).toString());
	      Scanner ignoredScanner = new Scanner(ignoredStream, "UTF-8")){

	    // TODO: It is probably better to replace this by a call to a JSON reader
	    // See org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.Settings
	    
	    String text = ignoredScanner.useDelimiter("\\A").next();
	    
	    IGNORED_DIRECTORIES = text.split("\\n");
	    int emptyLines = 0;
	    for(int i = 0; i < IGNORED_DIRECTORIES.length; i++){   // Strip comments
	      String ignore = IGNORED_DIRECTORIES[i];
	      int comment = ignore.indexOf("//");
	      if(comment >= 0){
	        ignore = ignore.substring(0, comment);
	      }
	      IGNORED_DIRECTORIES[i] =  ignore.replaceAll("/",  "::").trim();
	      if(IGNORED_DIRECTORIES[i].isEmpty()){
	        emptyLines++;
	      }
	    }
	    if(emptyLines > 0){                                    // remove empty lines
	      String[] tmp = new String[IGNORED_DIRECTORIES.length - emptyLines];
	      int k = 0;
	      for(int i = 0; i < IGNORED_DIRECTORIES.length; i++){
	        if(!IGNORED_DIRECTORIES[i].isEmpty()){
	          tmp[k++] = IGNORED_DIRECTORIES[i];
	        }
	      }
	      IGNORED_DIRECTORIES = tmp;
	    }
	  } catch (IOException e1) {
	    System.err.println(IGNORED + " not found; no ignored directories");
	    IGNORED_DIRECTORIES = new String[0];
	  } 
	}
	
	public RascalJUnitCompiledTestRunner(String prefix) {
	  this.prefix = prefix.replaceAll("\\\\", ""); // remove all the escapes (for example in 'lang::rascal::\syntax')
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
	
	static boolean isAcceptable(String rootModule, String candidate){
	   if(!rootModule.isEmpty()){
	     candidate = rootModule + "::" + candidate;
	   }
	  for(String ignore : IGNORED_DIRECTORIES){
	    if(candidate.contains(ignore)){
	      System.err.println("Ignoring: " + candidate);
	      return false;
	    }
	  }
	  return true;
	}

	static protected List<String> getRecursiveModuleList(ISourceLocation root) throws IOException {
	  List<String> result = new ArrayList<>();
	  Queue<ISourceLocation> todo = new LinkedList<>();
	  String rootPath = root.getPath().replaceFirst("/", "").replaceAll("/", "::");
	  todo.add(root);
	  while (!todo.isEmpty()) {
	    ISourceLocation currentDir = todo.poll();
	    String prefix = currentDir.getPath().replaceFirst(root.getPath(), "").replaceFirst("/", "").replaceAll("/", "::");
	    for (ISourceLocation ent : URIResolverRegistry.getInstance().list(currentDir)) {
	      if (ent.getPath().endsWith(".rsc")) {	
	        String candidate = (prefix.isEmpty() ? "" : (prefix + "::")) + URIUtil.getLocationName(ent).replace(".rsc", "");
	        if(isAcceptable(rootPath, candidate)){
	          result.add(candidate);
	        }
	      } else {
	        if (URIResolverRegistry.getInstance().isDirectory(ent) && !todo.contains(ent)){
	          todo.add(ent);
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

	  URIResolverRegistry resolver = URIResolverRegistry.getInstance();
	  try {
	    List<String> modules = getRecursiveModuleList(vf.sourceLocation("std", "", "/" + prefix.replaceAll("::", "/")));

	    for (String module : modules) {
	      String qualifiedName = (prefix.isEmpty() ? "" : prefix + "::") + module;
	      RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(pcfg).build();
	      ISourceLocation binary = Rascal.findBinary(pcfg.getBin(), qualifiedName);
	      ISourceLocation source =  rex.getPathConfig().resolveModule(qualifiedName);

	      //  Do a sufficient but not complete check on the binary; changes to imports will go unnoticed!
	      if(!resolver.exists(binary) || resolver.lastModified(source) > resolver.lastModified(binary)){
	        System.err.println("Compiling: " + qualifiedName);
//	        HashMap<String, IValue> kwparams = new HashMap<>();
//	        kwparams.put("enableAsserts", vf.bool(true));
	        IList programs = kernel.compileAndLink(
	            vf.list(vf.string(qualifiedName)),
//	            pcfg.getSrcs(),
//	            pcfg.getLibs(),
//	            pcfg.getBoot(),
//	            pcfg.getBin(),
	            pcfg.asConstructor(kernel),
	            kernel.kw_compileAndLink().enableAsserts(true).reloc(vf.sourceLocation("noreloc", "", "")));
	        boolean ok = RascalC.handleMessages(programs);
	        if(!ok){
	          System.exit(1);
	        }
	      }

	      RVMExecutable executable = RVMExecutable.newRead(binary, rex.getTypeStore());

	      if(executable.getTests().size() > 0){
	        Description modDesc = Description.createSuiteDescription(qualifiedName);
	        desc.addChild(modDesc);
	        int ntests = 0;
	        int ntests_with_random = 0;
	        LinkedList<Description> module_ignored = new LinkedList<Description>();

	        for(Function f : executable.getTests()){
	          String test_name = f.computeTestName();
	          Description d = Description.createTestDescription(getClass(), test_name);
	          modDesc.addChild(d);
	          ntests++;
	          ntests_with_random += f.getTries();

	          if(f.isIgnored()){
	            module_ignored.add(d);
	          }
	        }

	        testsPerModule.put(qualifiedName,  ntests);
	        ignoredPerModule.put(qualifiedName, module_ignored);
	        totalTests += ntests;
	        totalTestsWithRandom += ntests_with_random;
	      }
	    }
	    int totalIgnored = 0;
	    for(String name : testsPerModule.keySet()){
	      int tests = testsPerModule.get(name);
	      if(tests > 0){
	        int ignored = ignoredPerModule.get(name).size();
	        totalIgnored += ignored;
//	        System.err.println(name + ": " + testsPerModule.get(name) + (ignored == 0 ? "" : " (ignored: " + ignored + ")"));
	      }
	    }
	    System.err.println(prefix + ":");
	    System.err.println("\ttests: " + totalTests);
	    System.err.println("\tignored: " + totalIgnored);
	    System.err.println("\tto be executed (including random arguments): " + (totalTestsWithRandom - totalIgnored));
	    
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
		  RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(pcfg).build();
		  ISourceLocation binary = Rascal.findBinary(pcfg.getBin(), mod.getDisplayName());
          RVMCore rvmCore = null;
          try {
              rvmCore = ExecutionTools.initializedRVM(binary, rex);
          } catch (IOException e1) {
            notifier.fireTestFailure(new Failure(mod, e1));
          }
          
			Listener listener = new Listener(notifier, mod);
			TestExecutor runner = new TestExecutor(rvmCore, listener, rex);
			try {
				runner.test(mod.getDisplayName(), testsPerModule.get(mod.getClassName())); 
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
//		  System.out.println("RascalJunitCompiledTestRunner.start: " + context + ", " + count);
		  notifier.fireTestRunStarted(module);
		}

		@Override
		public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable t) {
			
//			System.err.println("RascalJunitCompiledTestRunner.report: " + successful + ", test = " + test + ", at " + loc + ", message = " + message);
			Description desc = getDescription(test, loc);
			notifier.fireTestStarted(desc);

			if (!successful) {
//			    System.err.println("RascalJunitCompiledTestRunner.report: " + successful + ", test = " + test + ", at " + loc + ", message = " + message);
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
