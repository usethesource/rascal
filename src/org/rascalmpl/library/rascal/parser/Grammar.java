package org.rascalmpl.library.rascal.parser;

import java.io.IOException;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

/**
 * Temporary class for bootstrapping and debugging purposes
 */
public class Grammar {
	private final IValueFactory factory;

	public Grammar(IValueFactory factory) {
		this.factory = factory;
	}
	
	public IValue getGrammar(IString modName, IEvaluatorContext ctx) {
		IValue g = ctx.getEvaluator().getGrammar(modName.getValue());
		System.err.println("getGrammar(" + modName + ") = " + g);
		return g;
	}
	
	public IValue parseModule(ISourceLocation loc, IEvaluatorContext ctx) {
		try {
			return ctx.getEvaluator().parseModuleExperimental(ctx.getResolverRegistry().getInputStream(loc.getURI()), loc.getURI());
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(factory.string(e.getMessage()), ctx.getCurrentAST(), ctx.getStackTrace());
		}
	}
}
