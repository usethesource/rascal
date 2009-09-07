package org.meta_environment.rascal.interpreter.result;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration.Abstract;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.load.ModuleLoader;
import org.meta_environment.rascal.interpreter.staticErrors.ArityError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.types.FunctionType;
import org.meta_environment.rascal.interpreter.utils.Names;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.rascal.parser.ModuleParser;
import org.meta_environment.rascal.parser.StringParser;
import org.meta_environment.uptr.ParsetreeAdapter;
import org.meta_environment.uptr.TreeAdapter;

public class ParserFunction extends NamedFunction {

	protected ModuleLoader loader;
	protected ModuleParser parser;

	public ParserFunction(Evaluator eval, Abstract func,
			Environment env, ModuleLoader loader) {
		this(eval, func, env, loader, new StringParser());
	}
	
	protected ParserFunction(Evaluator eval, Abstract func,
			Environment env, ModuleLoader loader, ModuleParser parser) {
		super(func, eval,
				(FunctionType) TE.eval(func.getSignature(),env), Names.name(func.getSignature().getName()), 
				false, env);
		this.loader = loader;
		this.parser = parser;
	}

	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals,
			IEvaluatorContext ctx) {
		if (callTracing) {
			printStartTrace();
		}

		checkParameters(actuals, actualTypes);
		
		String source = ((IString)(actuals[0])).getValue();
		
		List<String> sdfSearchPath = loader.getSdfSearchPath();
		Set<String> sdfImports = ((ModuleEnvironment)this.getEnv()).getSDFImports();
		
		
		try {
			Environment env = ctx.getCurrentEnvt();
			IConstructor ptree = ((StringParser)parser).parseString(sdfSearchPath, sdfImports, source);
			ptree = ParsetreeAdapter.addPositionInformation(ptree, source);
			IConstructor tree = (IConstructor) TreeAdapter.getArgs(ParsetreeAdapter.getTop(ptree)).get(1);
			Type resultType = getReturnType().instantiate(env.getStore(), env.getTypeBindings());
			
			return ResultFactory.makeResult(resultType, tree, eval);
		}
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), getAst(), eval.getStackTrace());
		}
		catch (SyntaxError e) {
			throw RuntimeExceptionFactory.parseError(e.getLocation(), getAst(), eval.getStackTrace());
		}
		finally {
			if (callTracing) {
				printEndTrace();
			}
		}
	}
	
	@Override
	public String toString() {
		return getHeader() + " @stringParser";
	}

	protected void checkParameters(IValue[] actuals, Type[] actualTypes) {
		if (actuals.length != 1) {
			throw new ArityError(1, actuals.length, getAst()); 
		}
		
		if (!actualTypes[0].isStringType()) {
			throw new UnexpectedTypeError(TF.stringType(), actualTypes[0], getAst());
		}
	}

	
}
