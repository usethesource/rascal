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

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalRuntimeException;
import org.rascalmpl.uri.URIUtil;

public class ReflectiveCompiled extends Reflective {
	
	public ReflectiveCompiled(IValueFactory values){
		super(values);
	}
	
	public IValue parseCommand(IString str, ISourceLocation loc,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("parseCommand", null, null);
	}

	public IValue parseCommands(IString str, ISourceLocation loc,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("parseCommands", null, null);
	}
	
	public IValue parseModule(ISourceLocation loc,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("parseModule", null, null);
	}
	
	public IValue parseModule(IString str, ISourceLocation loc,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("parseModule", null, null);
	}
	
	public IValue parseModule(ISourceLocation loc, final IList searchPath,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("parseModule", null, null);
    }
	
	public IValue getModuleLocation(IString modulePath,  RascalExecutionContext rex) {
		URI uri = rex.getRascalResolver().resolve(URIUtil.createRascalModule(modulePath.getValue()));
		if (uri == null) {
		  throw RascalRuntimeException.io(modulePath, null);
		}
		return values.sourceLocation(uri);
	}
	
	public ISourceLocation getSearchPathLocation(IString path, RascalExecutionContext rex) {
		String value = path.getValue();
		
		if (path.length() == 0) {
			throw RuntimeExceptionFactory.io(values.string("File not found in search path: [" + path + "]"), null, null);
		}
		
		if (!value.startsWith("/")) {
			value = "/" + value;
		}
		
		try {
			URI uri = rex.getRascalResolver().resolve(URIUtil.create("rascal", "", value));
			if (uri == null) {
				URI parent = URIUtil.getParentURI(URIUtil.createFile(value));
				
				if (parent == null) {
					// if the parent does not exist we are at the root and we look up the first path contributor:
					parent = rex.getRascalResolver().resolve(URIUtil.create("rascal", "", "/")); 
				}
				
				// here we recurse on the parent to see if it might exist
				ISourceLocation result = getSearchPathLocation(values.string(parent.getPath()), rex);
				
				if (result != null) {
					String child = URIUtil.getURIName(URIUtil.createFile(value));
					return values.sourceLocation(URIUtil.getChildURI(result.getURI(), child));
				}
				
				throw RuntimeExceptionFactory.io(values.string("File not found in search path: " + path), null, null);
			}

			return values.sourceLocation(uri);
		} catch (URISyntaxException e) {
			throw  RuntimeExceptionFactory.malformedURI(value, null, null);
		}
	}
}
