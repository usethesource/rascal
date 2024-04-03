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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.ConsoleRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.repl.TerminalProgressBarMonitor;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.TypeFactory;
import jline.TerminalFactory;

import org.rascalmpl.values.ValueFactoryFactory;


public class TestFramework {
	private final static IRascalMonitor monitor = System.console() != null 
		? new TerminalProgressBarMonitor(System.out, TerminalFactory.get())
		: new NullRascalMonitor();

	private final static Evaluator evaluator;
	private final static GlobalEnvironment heap;
	private final static ModuleEnvironment root;
	
	private final static PrintWriter stderr;
	private final static PrintWriter stdout;
	
	static{
		heap = new GlobalEnvironment();
		root = heap.addModule(new ModuleEnvironment("___test___", heap));
		
		stderr = new PrintWriter(System.err, true);
		stdout = System.console() != null ? new PrintWriter((TerminalProgressBarMonitor) monitor) : new PrintWriter(System.out, true);
		evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), System.in, System.err, System.console() != null ? (TerminalProgressBarMonitor) monitor : System.out ,  root, heap);
		evaluator.setMonitor(monitor);

		evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
		RascalJUnitTestRunner.configureProjectEvaluator(evaluator, RascalJUnitTestRunner.inferProjectRoot(TestFramework.class));
		
		try {
			assert (false);
			throw new RuntimeException("Make sure you enable the assert statement in your run configuration ( add -ea )");
		}
		catch (AssertionError e) {
			
		}
	}
	
	private Set<ISourceLocation> generatedModules = new HashSet<>();
	
	public TestFramework() {
		super();
	}
	
	private void reset() {
		heap.clear();
		root.reset();

		for (ISourceLocation mod : generatedModules) {
		    try {
                URIResolverRegistry.getInstance().remove(mod, true);
            }
            catch (IOException e) {
            }
		}
		generatedModules.clear();	
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
        try {
            ISourceLocation moduleLoc = ValueFactoryFactory.getValueFactory().sourceLocation("memory", "test-modules", "/" + name.replace("::", "/") + ".rsc");
            generatedModules.add(moduleLoc);
            try (OutputStream target = URIResolverRegistry.getInstance().getOutputStream(moduleLoc, false)) {
                target.write(module.getBytes(StandardCharsets.UTF_8));
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        catch (URISyntaxException e1) {
            e1.printStackTrace();
            return false;
        }
		return true;
	}

	private boolean execute(String command){
		Result<IValue> result = evaluator.eval(null, command, URIUtil.rootLocation("stdin"));

		if (result.getStaticType().isBottom()) {
			return true;
			
		}
		if (result.getValue() == null) {
			return false;
		}
		
		if (result.getStaticType() == TypeFactory.getInstance().boolType()) {
			return ((IBool) result.getValue()).getValue();
		}
		
		return false;
	}
}
