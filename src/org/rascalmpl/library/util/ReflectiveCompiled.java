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
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.PreludeCompiled;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalRuntimeException;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.io.InputConverter;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

public class ReflectiveCompiled extends Reflective {
	
	private final PreludeCompiled preludeCompiled;

	public ReflectiveCompiled(IValueFactory values){
		super(values);
		preludeCompiled = new PreludeCompiled(values);
	}
	
	private char[] getResourceContent(ISourceLocation location) throws IOException{
		char[] data;
		Reader textStream = null;
		
		URIResolverRegistry resolverRegistry = URIResolverRegistry.getInstance();
		try {
			textStream = resolverRegistry.getCharacterReader(location);
			data = InputConverter.toChar(textStream);
		}
		finally{
			if(textStream != null){
				textStream.close();
			}
		}
		
		return data;
	}
	
	public IValue parseCommand(IString str, ISourceLocation loc,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("parseCommand", null, null);
	}

	public IValue parseCommands(IString str, ISourceLocation loc,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("parseCommands", null, null);
	}
	
	public IValue parseModule(ISourceLocation loc,  RascalExecutionContext rex) {
		 IActionExecutor<IConstructor> actions = new NoActionExecutor();	
		
	     try {
			return new RascalParser().parse(Parser.START_MODULE, loc.getURI(), getResourceContent(rex.resolveSourceLocation(loc)), actions, new DefaultNodeFlattener<IConstructor, IConstructor, ISourceLocation>(), new UPTRNodeFactory());
		} catch (IOException e) {
			throw RascalRuntimeException.io(values.string(e.getMessage()), null);
		}
	}
	
	public IValue parseModule(IString str, ISourceLocation loc,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("parseModule", null, null);
	}
	
	public IValue parseModule(ISourceLocation loc, final IList searchPath,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("parseModule", null, null);
    }
	
	public IValue getModuleLocation(IString modulePath,  RascalExecutionContext rex) {
		ISourceLocation uri = rex.getRascalSearchPath().resolveModule(modulePath.getValue());
		if (uri == null) {
		  throw RascalRuntimeException.io(modulePath, null);
		}
		return uri;
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
			ISourceLocation uri = rex.getRascalSearchPath().resolvePath(value);
			if (uri == null) {
				URI parent = URIUtil.getParentURI(URIUtil.createFile(value));

				if (parent == null) {
					// if the parent does not exist we are at the root and we look up the first path contributor:
					parent = URIUtil.createFile("/"); 
				}

				// here we recurse on the parent to see if it might exist
				ISourceLocation result = getSearchPathLocation(values.string(parent.getPath()), rex);

				if (result != null) {
					String child = URIUtil.getURIName(URIUtil.createFile(value));
					return URIUtil.getChildLocation(result, child);
				}

				throw RuntimeExceptionFactory.io(values.string("File not found in search path: " + path), null, null);
			}

			return uri;
		} catch (URISyntaxException e) {
			throw  RuntimeExceptionFactory.malformedURI(value, null, null);
		}
	}
	
	public IBool inCompiledMode() { return values.bool(true); }

	
	public IValue watch(IValue tp, IValue val, IString name, RascalExecutionContext rex){
		return watch(tp, val, name, values.string(""), rex);
	}

	public IValue watch(IValue tp, IValue newVal, IString name, IValue suffixVal, RascalExecutionContext rex){
		ISourceLocation watchLoc;
		String suffix = stripQuotes(suffixVal);
		String name1 = stripQuotes(name);

		String path = "watchpoints/" + (suffix.length() == 0 ? name1 : (name1 + "-" + suffix)) + ".txt";
		try {
			watchLoc = values.sourceLocation("home", null, path, null, null);
		} catch (URISyntaxException e) {
			throw RuntimeExceptionFactory.io(values.string("Cannot create |home:///" + path + "|"), null, null);
		}
		IValue oldVal = preludeCompiled.readTextValueFile(tp, watchLoc);
		if(oldVal.equals(newVal)){
			return newVal;
		} else {
			String sOld = oldVal.toString();
			if(sOld.length() > 20){
				sOld = sOld.substring(0, 20) + "...";
			}
			String sNew = newVal.toString();
			if(sNew.length() > 20){		
				sNew = sNew.substring(0, 20) + "...";
			}
			throw RuntimeExceptionFactory.assertionFailed(values.string("Watchpoint " + name1 + ": " + idiff("", oldVal, newVal)), null, null);
		}
	}

}
