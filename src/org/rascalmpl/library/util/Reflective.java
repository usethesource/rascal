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
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.library.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.SourceLocationListContributor;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIUtil;

public class Reflective {
	protected final IValueFactory values;
	private Evaluator cachedEvaluator;
	private int robin = 0;
	private static final int maxCacheRounds = 500;

	public Reflective(IValueFactory values){
		super();
		this.values = values;
	}
	
	// REFLECT -- copy in ReflectiveCompiled
	public IValue parseCommand(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		IEvaluator<?> evaluator = ctx.getEvaluator();
		return evaluator.parseCommand(evaluator.getMonitor(), str.getValue(), loc.getURI());
	}

	// REFLECT -- copy in ReflectiveCompiled
	public IValue parseCommands(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		IEvaluator<?> evaluator = ctx.getEvaluator();
		return evaluator.parseCommands(evaluator.getMonitor(), str.getValue(), loc.getURI());
	}
	
	// REFLECT -- copy in ReflectiveCompiled
	public IValue parseModule(ISourceLocation loc, IEvaluatorContext ctx) {
		try {
			Evaluator ownEvaluator = getPrivateEvaluator(ctx);
			return ownEvaluator.parseModule(ownEvaluator.getMonitor(), loc.getURI());
		}
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
		catch (Throwable e) {
		  throw RuntimeExceptionFactory.javaException(e, null, null);
		}
	}

	private Evaluator getPrivateEvaluator(IEvaluatorContext ctx) {
		if (cachedEvaluator == null || robin++ > maxCacheRounds) {
			robin = 0;
			IEvaluator<?> callingEval = ctx.getEvaluator();
			
			
			GlobalEnvironment heap = new GlobalEnvironment();
			ModuleEnvironment root = heap.addModule(new ModuleEnvironment("___full_module_parser___", heap));
			cachedEvaluator = new Evaluator(callingEval.getValueFactory(), callingEval.getStdErr(), callingEval.getStdOut(), root, heap);
			
			// Update the classpath so it is the same as in the context interpreter.
			cachedEvaluator.getConfiguration().setRascalJavaClassPathProperty(ctx.getConfiguration().getRascalJavaClassPathProperty());
		  // clone the classloaders
	    for (ClassLoader loader : ctx.getEvaluator().getClassLoaders()) {
	      cachedEvaluator.addClassLoader(loader);
	    }
		}
		
		return cachedEvaluator;
	}
	
	// REFLECT -- copy in ReflectiveCompiled
	public IValue parseModule(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		Evaluator ownEvaluator = getPrivateEvaluator(ctx);
		return ownEvaluator.parseModule(ownEvaluator.getMonitor(), str.getValue().toCharArray(), loc.getURI());
	}
	
	// REFLECT -- copy in ReflectiveCompiled
	public IValue parseModule(ISourceLocation loc, final IList searchPath, IEvaluatorContext ctx) {
    final Evaluator ownEvaluator = getPrivateEvaluator(ctx);

    // add the given locations to the search path
    SourceLocationListContributor contrib = new SourceLocationListContributor("reflective", searchPath);
    ownEvaluator.addRascalSearchPathContributor(contrib);
    
    try { 
      return ownEvaluator.parseModule(ownEvaluator.getMonitor(), loc.getURI());
    } catch (IOException e) {
      throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
    }
    catch (Throwable e) {
      throw RuntimeExceptionFactory.javaException(e, null, null);
    }
    finally {
      ownEvaluator.removeSearchPathContributor(contrib);
    }
  }
	
	// REFLECT -- copy in ReflectiveCompiled
	public IValue getModuleLocation(IString modulePath, IEvaluatorContext ctx) {
		URI uri = ctx.getEvaluator().getRascalResolver().resolveModule(modulePath.getValue());
		if (uri == null) {
		  throw RuntimeExceptionFactory.moduleNotFound(modulePath, ctx.getCurrentAST(), null);
		}
		return ctx.getValueFactory().sourceLocation(uri);
	}
	
	// REFLECT -- copy in ReflectiveCompiled
	public ISourceLocation getSearchPathLocation(IString path, IEvaluatorContext ctx) {
		String value = path.getValue();
		
		if (path.length() == 0) {
			throw RuntimeExceptionFactory.io(values.string("File not found in search path: [" + path + "]"), null, null);
		}
		
		if (!value.startsWith("/")) {
			value = "/" + value;
		}
		
		try {
			URI uri = ctx.getEvaluator().getRascalResolver().resolvePath(value);
			if (uri == null) {
				URI parent = URIUtil.getParentURI(URIUtil.createFile(value));
				
				if (parent == null) {
					// if the parent does not exist we are at the root and we look up the first path contributor:
					parent = URIUtil.createFile("/"); 
				}
				
				// here we recurse on the parent to see if it might exist
				ISourceLocation result = getSearchPathLocation(values.string(parent.getPath()), ctx);
				
				if (result != null) {
					String child = URIUtil.getURIName(URIUtil.createFile(value));
					return values.sourceLocation(URIUtil.getChildURI(result.getURI(), child));
				}
				
				throw RuntimeExceptionFactory.io(values.string("File not found in search path: " + path), null, null);
			}

			return ctx.getValueFactory().sourceLocation(uri);
		} catch (URISyntaxException e) {
			throw  RuntimeExceptionFactory.malformedURI(value, null, null);
		}
	}

}
