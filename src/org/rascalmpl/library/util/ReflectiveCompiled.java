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

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.PreludeCompiled;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalRuntimeException;
import org.rascalmpl.uri.URIUtil;

public class ReflectiveCompiled extends Reflective {
	
	private final PreludeCompiled preludeCompiled;

	public ReflectiveCompiled(IValueFactory values){
		super(values);
		preludeCompiled = new PreludeCompiled(values);
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
		ISourceLocation uri = rex.getRascalResolver().resolveModule(modulePath.getValue());
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
			ISourceLocation uri = rex.getRascalResolver().resolvePath(value);
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
	
	private String diff(IValue oldVal, IValue newVal){
		
		if(oldVal.getType().isString()){
			IString ov = (IString) oldVal;
			IString nv = (IString) newVal;
			String ldiff = (ov.length() == nv.length()) ? "" : ("length " + ov.length() + " vs " +  nv.length() + "; ");
			for(int i = 0; i < ov.length() && i < nv.length(); i++){
				if(ov.charAt(i) != nv.charAt(i)){
					return ldiff + "diff at index " + i + ": " + ov.charAt(i) + " + vs + " + nv.charAt(i);
				}
			}
		}
		if(oldVal.getType().isList()){
			IList ov = (IList) oldVal;
			IList nv = (IList) newVal;
			String ldiff = (ov.length() == nv.length()) ? "" : ("size " + ov.length() + " vs " +  nv.length() + "; ");
			for(int i = 0; i < ov.length() && i < nv.length(); i++){
				if(!ov.get(i).equals(nv.get(i))){
					return ldiff + "diff at index " + i + ": " + ov.get(i) + " + vs + " + nv.get(i);
				}
			}
		}
		if(oldVal.getType().isTuple()){
			ITuple ov = (ITuple) oldVal;
			ITuple nv = (ITuple) newVal;
			for(int i = 0; i < ov.arity(); i++){
				if(!ov.get(i).equals(nv.get(i))){
					return "diff at index " + i + ": " + ov.get(i) + " + vs + " + nv.get(i);
				}
			}
		}
		if(oldVal.getType().isSet()){
			ISet ov = (ISet) oldVal;
			ISet nv = (ISet) newVal;
			String ldiff = (ov.size() == nv.size()) ? "" : ("size " + ov.size() + " vs " +  nv.size() + "; ");
			
			ISet diff1 = ov.subtract(nv);
			String msg1 = diff1.size() == 0 ? "" : "only in old: " + diff1 + "; ";
			ISet diff2 = nv.subtract(ov);
			String msg2 = diff2.size() == 0 ? "" : "only in new: " + diff2;
			return ldiff + msg1 + msg2;
		}
		
		if(oldVal.getType().isMap()){
			IMap ov = (IMap) oldVal;
			IMap nv = (IMap) newVal;
			String ldiff = (ov.size() == nv.size()) ? "" : ("size " + ov.size() + " vs " +  nv.size() + "; ");
			
			IMap all = ov.join(nv);
			
			String onlyInOld = "";
			String onlyInNew = "";
			String diffVal = "";
			for(IValue key : all){
				if(!nv.containsKey(key)){
					onlyInOld += " " + key;
					continue;
				}
				if(!ov.containsKey(key)){
					onlyInNew += " " + key;
					continue;
				}
				if(!ov.get(key).equals(nv.get(key))){
					diffVal += " key " + key + ": [" + diff(ov.get(key), nv.get(key)) + "]";
				}
			}
				
			String msg1 = onlyInOld.length() == 0 ? "" : "keys only in old:" + onlyInOld + "; ";
			String msg2 = onlyInNew.length() == 0 ? "" : "keys only in new:" + onlyInNew + "; ";
			String msg3 = diffVal.length() == 0 ? "" : "diffs at" + diffVal + "; ";
			return ldiff + msg1 + msg2 + msg3;
		}
		String sOld = oldVal.toString();
		if(sOld.length() > 20){
			sOld = sOld.substring(0, 20) + "...";
		}
		String sNew = newVal.toString();
		if(sNew.length() > 20){		
			sNew = sNew.substring(0, 20) + "...";
		}
		return "was " + sOld + ", now " + sNew;
		
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
			throw RuntimeExceptionFactory.assertionFailed(values.string("Watchpoint " + name1 + ": " + diff(oldVal, newVal)), null, null);
		}
	}

}
