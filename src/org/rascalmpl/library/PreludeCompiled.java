package org.rascalmpl.library;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;

/*
 * This class overrides methods from Prelude that need to be handled differenty in compiled code.
 * In most (all?) cases this will be library function with a @reflect{...} tag that makes them dependent on
 * IEvaluatorContext, the context of the Rascal interpreter.
 */
public class PreludeCompiled extends Prelude {

	public PreludeCompiled(IValueFactory values) {
		super(values);
	}
	
	@Override
	public void println(IValue v, IEvaluatorContext ctx){
		System.err.println("OVERRIDE PRINTLN");
		super.println(v, ctx);
	}
	
	@Override
	// public java &T<:Tree parse(type[&T<:Tree] begin, str input);
	public IValue parse(IValue start, ISourceLocation input, IEvaluatorContext ctx) {
		return RascalPrimitive.getParsingTools().parse(super.values.string("XXX"), start, input);
	}
	@Override
	// public java &T<:Tree parse(type[&T<:Tree] begin, str input, loc origin);
	public IValue parse(IValue start, IString input, IEvaluatorContext ctx) {
		return RascalPrimitive.getParsingTools().parse(super.values.string("XXX"), start, input);
	}
}
