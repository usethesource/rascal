package org.rascalmpl.library.experiments.Compiler.RascalExtraction;

import java.io.IOException;
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.util.PathConfig;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class RascalExtraction {
	IValueFactory vf;
	private OverloadedFunction extractDoc;
	
	private RVMCore rvm;
	
	public RascalExtraction(IValueFactory vf, PathConfig pcfg) throws IOException{
	    
	}
	
	/**
	 * Extract concepts from a "remote" Rascal files, i.e. outside a documentation hierarchy
	 * @param moduleLoc	Location of the Rascal source file
	 * @param kwArgs	Keyword arguments
	 * @return A tuple consisting of 1. string with extracted documentation, 2. a list of extracted declaration info
	 */
	public ITuple extractDoc(IString parent, ISourceLocation moduleLoc, Map<String,IValue> kwArgs){
		try {
			return (ITuple) rvm.executeRVMFunction(extractDoc, new IValue[] { parent, moduleLoc }, kwArgs);
		} catch (Exception e){
			e.printStackTrace(System.err);
		}
		return vf.tuple(vf.string(""), vf.list());
	}
}
