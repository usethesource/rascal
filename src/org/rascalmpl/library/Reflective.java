package org.rascalmpl.library;

import java.io.IOException;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class Reflective {

	public Reflective(IValueFactory values){
		super();
	}

	public IValue getModuleParseTree(IString modulePath, IEvaluatorContext ctx) {
		try {
			IConstructor tree = null;
			URI uri = ctx.getEvaluator().getRascalResolver().resolve(URI.create("rascal:///" + modulePath.getValue()));
			tree = ctx.getEvaluator().parseModule(ctx.getEvaluator(), uri, new ModuleEnvironment("***getModuleParseTree***", ctx.getHeap()));
			return TreeAdapter.getArgs(tree).get(1);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.moduleNotFound(modulePath, null, null);
		}
	}
	
	public IConstructor getModuleGrammar(ISourceLocation loc, IEvaluatorContext ctx) {
		URI uri = loc.getURI();
		Evaluator evaluator = ctx.getEvaluator();
		return evaluator.getGrammar(evaluator.getMonitor(), uri);
	}
	
	public IValue parseCommand(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		Evaluator evaluator = ctx.getEvaluator();
		return evaluator.parseCommand(evaluator.getMonitor(), str.getValue(), loc.getURI());
	}

	public IValue parseCommands(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		Evaluator evaluator = ctx.getEvaluator();
		return evaluator.parseCommands(evaluator.getMonitor(), str.getValue(), loc.getURI());
	}
	
}
