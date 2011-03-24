package org.rascalmpl.library;

import java.io.IOException;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.parser.ParserGenerator;
import org.rascalmpl.values.uptr.TreeAdapter;

public class Reflective {

	private final IValueFactory values;
	
	public Reflective(IValueFactory values){
		super();
		
		this.values = values;
	}

	public IValue getModuleParseTree(IString modulePath, IEvaluatorContext ctx) {
		try {
			IConstructor tree = null;
			URI uri = ctx.getEvaluator().getRascalResolver().resolve(URI.create("rascal:///" + modulePath.getValue()));
			tree = ctx.getEvaluator().parseModule(uri, new ModuleEnvironment("***TYPECHECKING***", ctx.getHeap()));
			return TreeAdapter.getArgs(tree).get(1);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.moduleNotFound(modulePath, null, null);
		}
	}
	
	public IConstructor getModuleGrammar(ISourceLocation loc, IEvaluatorContext ctx) {
		ParserGenerator pgen = ctx.getEvaluator().getParserGenerator();
		URI uri = loc.getURI();
		ModuleEnvironment env = ctx.getHeap().getModule(uri.getAuthority());
		return pgen.getGrammar(env.getProductions());
	}
	
	public IValue parseCommand(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		return ctx.getEvaluator().parseCommand(str.getValue(), loc.getURI());
	}

	public IValue parseCommands(IString str, ISourceLocation loc, IEvaluatorContext ctx) {
		return ctx.getEvaluator().parseCommands(str.getValue(), loc.getURI());
	}
	
}
