package org.rascalmpl.core.library;

import java.io.PrintWriter;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeStore;

/*
 * This class overrides methods from Prelude that need to be handled differenty in compiled code.
 * In most (all?) cases this will be library function with a @reflect{...} tag that makes them dependent on
 * IEvaluatorContext, the context of the Rascal interpreter.
 */
public class PreludeCompiled extends Prelude {

	public PreludeCompiled(IValueFactory values, IRascalValueFactory rascalValues, PrintWriter out, TypeStore store, IRascalMonitor monitor) {
		super(values, rascalValues, out, store, monitor);
	}
	
}
	
