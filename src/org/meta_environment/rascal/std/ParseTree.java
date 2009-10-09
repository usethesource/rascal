package org.meta_environment.rascal.std;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.load.ModuleLoader;
import org.meta_environment.rascal.interpreter.types.NonTerminalType;
import org.meta_environment.rascal.interpreter.types.ReifiedType;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.rascal.parser.ConcreteObjectParser;
import org.meta_environment.uptr.ParsetreeAdapter;
import org.meta_environment.uptr.ProductionAdapter;
import org.meta_environment.uptr.TreeAdapter;
import org.meta_environment.uri.FileURIResolver;
import org.meta_environment.uri.URIResolverRegistry;
import org.meta_environment.values.ValueFactoryFactory;

public class ParseTree {
	private final static IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final ConcreteObjectParser parser = new ConcreteObjectParser();

	static public IValue parse(IConstructor start, ISourceLocation input, IEvaluatorContext ctx) {
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
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
		
	}
	
	static public IValue parse(IConstructor start, IString input, IEvaluatorContext ctx) {
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
		}catch(IOException ioex){
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
		}
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
