package org.meta_environment.rascal.interpreter.env;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration.Abstract;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.RuntimeExceptionFactory;
import org.meta_environment.rascal.interpreter.load.ModuleLoader;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.parser.StringParser;
import org.meta_environment.uptr.ParsetreeAdapter;

public class FileParserFunction extends ParserFunction {

	public FileParserFunction(Evaluator eval, Abstract func, Environment env, ModuleLoader loader) {
		super(eval, func, env, loader, loader.getParser());
	}

	
	@Override
	public Result<IValue> call(IValue[] actuals, Type actualTypes, Environment env) {
		if (callTracing) {
			printStartTrace();
		}

		checkParameters(actuals, actualTypes);
		
		String source = ((IString)(actuals[0])).getValue();
		
		List<String> sdfSearchPath = loader.getSdfSearchPath();
		Set<String> sdfImports = ((ModuleEnvironment)this.getEnv()).getSDFImports();
		
		
		try {
			IConstructor ptree = parser.parseObjectLanguageFile(sdfSearchPath, sdfImports, source); 
			IConstructor tree = (IConstructor) new ParsetreeAdapter(ptree).getTop().getArgs().get(1);
			Type resultType = returnType.instantiate(env.getStore(), env.getTypeBindings());
			
			return ResultFactory.makeResult(resultType, tree, new EvaluatorContext(eval, ast));
		}
		catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.io(VF.string(e.getMessage()), getAst(),
					eval.getStackTrace());
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
		return getHeader() + " @fileParser";
	}

}
