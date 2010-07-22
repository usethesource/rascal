package org.rascalmpl.library.rascal.parser;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;

/**
 * Temporary class for bootstrapping and debugging purposes
 */
public class Grammar {
	public Grammar(IValueFactory factory) {
		// do nothing
	}
	
	public IValue getGrammar(IString modName, IEvaluatorContext ctx) {
		return ctx.getEvaluator().getGrammar(modName.getValue());
	}
}
