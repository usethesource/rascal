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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalRuntimeException;
import org.rascalmpl.uri.URIUtil;

public class ReflectiveCompiled extends Reflective {
	
	public ReflectiveCompiled(IValueFactory values){
		super(values);
	}

	public IConstructor getModuleGrammar(ISourceLocation loc,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("getModuleGrammar", null, null);
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
}
