package org.rascalmpl.library;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.ModuleLoader;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.parser.ConcreteObjectParser;
import org.rascalmpl.uri.FileURIResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.uptr.ParsetreeAdapter;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class ParseTree {
	private final IValueFactory values;
	private final ConcreteObjectParser parser;
	
	public ParseTree(IValueFactory values){
		super();
		
		this.values = values;
		parser = new ConcreteObjectParser();
	}

	public IValue parse(IConstructor start, ISourceLocation input, IEvaluatorContext ctx) {
		Type reified = start.getType();
		
		IConstructor startSort = checkPreconditions(start, reified);
		
		Evaluator evaluator = ctx.getEvaluator();
		ModuleLoader loader = evaluator.getModuleLoader();

		// ((ModuleEnvironment)this.getEnv()).getSDFImports()
		IConstructor ptree;
		try {
			InputStream stream = URIResolverRegistry.getInstance().getInputStream(input.getURI());
			ptree = parser.parseStream(loader.getSdfSearchPath(), ((ModuleEnvironment) ctx.getCurrentEnvt().getRoot()).getSDFImports(), stream);
			ptree = ParsetreeAdapter.addPositionInformation(ptree, input.getURI());
			IConstructor tree = (IConstructor) TreeAdapter.getArgs(ParsetreeAdapter.getTop(ptree)).get(1);

			IConstructor prod = TreeAdapter.getProduction(tree);
			IConstructor rhs = ProductionAdapter.getRhs(prod);

			if (!rhs.isEqual(startSort)) {
				throw RuntimeExceptionFactory.parseError(TreeAdapter.getLocation(tree), null, null);
			}
			
			return tree;
		}
		catch (SyntaxError e) {
			ISourceLocation loc = e.getLocation();
			loc = values.sourceLocation(input.getURI(), loc.getOffset(), loc.getLength(), loc.getBeginLine(),
					loc.getEndLine(), loc.getBeginColumn(), loc.getEndColumn());
			throw RuntimeExceptionFactory.parseError(loc, null, null);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
		
	}
	
	public IValue parse(IConstructor start, IString input, IEvaluatorContext ctx) {
		Type reified = start.getType();
		
		IConstructor startSort = checkPreconditions(start, reified);
		
		Evaluator evaluator = ctx.getEvaluator();
		ModuleLoader loader = evaluator.getModuleLoader();

		// ((ModuleEnvironment)this.getEnv()).getSDFImports()
		IConstructor ptree;
		try {
			ptree = parser.parseString(loader.getSdfSearchPath(), ((ModuleEnvironment) ctx.getCurrentEnvt().getRoot()).getSDFImports(), input.getValue());
			ptree = ParsetreeAdapter.addPositionInformation(ptree, FileURIResolver.STDIN_URI);
			IConstructor tree = (IConstructor) TreeAdapter.getArgs(ParsetreeAdapter.getTop(ptree)).get(1);

			IConstructor prod = TreeAdapter.getProduction(tree);
			IConstructor rhs = ProductionAdapter.getRhs(prod);

			if (!rhs.isEqual(startSort)) {
				throw RuntimeExceptionFactory.parseError(TreeAdapter.getLocation(tree), null, null);
			}
			
			return tree;
		}
		catch (SyntaxError e) {
			throw RuntimeExceptionFactory.parseError(e.getLocation(), null, null);
		}
		catch(IOException ioex){
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
		}
	}
	
	public IString unparse(IConstructor tree) {
		return values.string(TreeAdapter.yield(tree));
	}
	
	private static IConstructor checkPreconditions(IConstructor start, Type reified) {
		if (!(reified instanceof ReifiedType)) {
		   throw RuntimeExceptionFactory.illegalArgument(start, null, null);
		}
		
		Type nt = reified.getTypeParameters().getFieldType(0);
		
		if (!(nt instanceof NonTerminalType)) {
			throw RuntimeExceptionFactory.illegalArgument(start, null, null);
		}
		
		IConstructor symbol = ((NonTerminalType) nt).getSymbol();
		
		return symbol;
	}
}
