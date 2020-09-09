package org.rascalmpl.core.library;

import java.io.PrintWriter;

import org.rascalmpl.library.Prelude;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

/*
 * This class overrides methods from Prelude that need to be handled differenty in compiled code.
 * In most (all?) cases this will be library function with a @reflect{...} tag that makes them dependent on
 * IEvaluatorContext, the context of the Rascal interpreter.
 */
public class PreludeCompiled extends Prelude {

	public PreludeCompiled(IValueFactory values, IRascalValueFactory rascalValues, PrintWriter out, PrintWriter err) {
		super(values, rascalValues, out,  err);
	}
	
	@Override
	public IFunction parser(IValue start, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity) {
	    throw new UnsupportedOperationException("have to implement parser function for compiled context");
	}
	
	@Override
	public IFunction parsers(IValue start, IBool allowAmbiguity, IBool hasSideEffects, IBool firstAmbiguity) {
	    throw new UnsupportedOperationException("have to implement parsers function for compiled context");
	}
}
	
