/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
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
import java.net.URISyntaxException;

import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.PreludeCompiled;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalRuntimeException;
import org.rascalmpl.library.lang.rascal.boot.IKernel;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.values.uptr.ITree;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class ReflectiveCompiled extends Reflective {
	
	private final PreludeCompiled preludeCompiled;	

	public ReflectiveCompiled(IValueFactory values){
		super(values);
		preludeCompiled = new PreludeCompiled(values);
	}
	
    public IValue getCurrentPathConfig(RascalExecutionContext rex) {
        return rex.getPathConfig().asConstructor(rex.getRVM().asInterface(IKernel.class));
    }

	public IValue parseCommand(IString str, ISourceLocation loc,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("parseCommand", null, null);
	}

	public IValue parseCommands(IString str, ISourceLocation loc,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("parseCommands", null, null);
	}
	
	public IValue parseNamedModuleWithSpaces(IString modulePath,  RascalExecutionContext rex){
	    ISourceLocation moduleLoc = rex.getPathConfig().resolveModule(modulePath.getValue());
	    if(moduleLoc == null){
	        throw RascalRuntimeException.io(values.string("Module " + modulePath.getValue() + " not found"), null);
	    }
	    return parseModuleWithSpaces(moduleLoc);
	}
	
	public IValue parseModuleWithSpaces(ISourceLocation loc) {
        IActionExecutor<ITree> actions = new NoActionExecutor();    
        try {
            return new RascalParser().parse(Parser.START_MODULE, loc.getURI(), getResourceContent(loc), actions, new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(true));
        } catch (IOException e) {
            throw RascalRuntimeException.io(values.string(e.getMessage()), null);
        }
    }
	
	public IValue parseModuleAndFragments(ISourceLocation loc,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("parseModule", null, null);
	}
	
	public IValue parseModuleAndFragments(IString str, ISourceLocation loc,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("parseModule", null, null);
	}
	
	public IValue parseModuleAndFragments(ISourceLocation loc, final IList searchPath,  RascalExecutionContext rex) {
		throw RascalRuntimeException.notImplemented("parseModule", null, null);
    }
	
	public IValue getModuleLocation(IString modulePath,  RascalExecutionContext rex) {
	    ISourceLocation uri;
	    try {
            uri = rex.getPathConfig().getModuleLoc(modulePath.getValue());
            if (uri == null) {
                throw RascalRuntimeException.io(modulePath, null);
              }
            return uri;
        }
        catch (IOException e) {           
            throw RascalRuntimeException.io(modulePath, null);
        }
	}
	
	public IBool inCompiledMode() { return values.bool(true); }

	public void throwNullPointerException() {
	    throw new NullPointerException();
	}
	
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
