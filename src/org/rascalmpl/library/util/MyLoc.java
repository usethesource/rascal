package org.rascalmpl.library.util;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class MyLoc {

	public MyLoc(IValueFactory values){
	}
	
	public ISourceLocation myLoc(IEvaluatorContext ctx) {
		return ctx.getCurrentAST().getLocation();
	}

}
