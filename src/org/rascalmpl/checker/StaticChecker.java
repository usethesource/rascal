/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.checker;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;

public class StaticChecker {
	private final Evaluator eval;
	public static final String TYPECHECKER = "typecheckTree";
	private boolean checkerEnabled; 
	private boolean initialized;
	private boolean loaded;
    private Type pathConfigConstructor = null;
	
	public StaticChecker(PrintWriter stderr, PrintWriter stdout) {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("$staticchecker$", heap));
		eval = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout, root, heap);
		eval.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
		checkerEnabled = false;
		initialized = false;
		loaded = false;
	}
	
	private IValue eval(IRascalMonitor monitor, String cmd) {
		try {
			return eval.eval(monitor, cmd, URIUtil.rootLocation("checker")).getValue();
		} catch (ParseError pe) {
			throw new ImplementationError("syntax error in static checker modules", pe);
		}
	}

	public synchronized void load(IRascalMonitor monitor) {
		eval(monitor, "import lang::rascal::types::CheckTypes;");
		eval(monitor, "import util::Reflective;");
	    TypeStore ts = eval.getHeap().getModule("util::Reflective").getStore();
	    pathConfigConstructor = ts.lookupConstructor(ts.lookupAbstractDataType("PathConfig"), "pathConfig").iterator().next();
		loaded = true;
	}

	public void init() {
		initialized = true;
	}
	
	public Configuration getConfiguration() {
	  return eval.getConfiguration();
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public synchronized ITree checkModule(IRascalMonitor monitor, ISourceLocation module) {
		if (checkerEnabled) {
			return (ITree) eval.call(monitor, "check", module, getPathConfig());
		}
		return null;
	}

	private IValue getPathConfig() {
	    assert pathConfigConstructor != null;
	    IValueFactory vf = ValueFactoryFactory.getValueFactory();
	    Map<String, IValue> kwArgs = new HashMap<>();
	    kwArgs.put("srcPath", vf.list(eval.getRascalResolver().collect().toArray(new IValue[0])));
	    // default args for the rest
	    return vf.constructor(pathConfigConstructor, new IValue[0], kwArgs);
    }

    public synchronized void disableChecker() {
		checkerEnabled = false;
	}

	public synchronized void enableChecker(IRascalMonitor monitor) {
		if (!loaded) {
			load(monitor);
		}
		checkerEnabled = true;
	}
	
	public boolean isCheckerEnabled() {
		return checkerEnabled;
	}

	public void addRascalSearchPath(ISourceLocation uri) {
		eval.addRascalSearchPath(uri);
	}
	
	public void addClassLoader(ClassLoader classLoader) {
		eval.addClassLoader(classLoader);
	}

  public Evaluator getEvaluator() {
    return eval;
  }
}
