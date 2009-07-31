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
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.uptr.ParsetreeAdapter;

public class FileParserFunction extends ParserFunction {

	private Environment env;


	public FileParserFunction(Evaluator eval, Abstract func, Environment env, ModuleLoader loader) {
		super(eval, func, env, loader, loader.getParser());
		this.env = env;
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
			IConstructor ptree = parser.parseObjectLanguageFile(sdfSearchPath, sdfImports, source); 
			IConstructor tree = (IConstructor) new ParsetreeAdapter(ptree).getTop().getArgs().get(1);
			// TODO parse error handling!
			Type resultType = returnType.instantiate(env.getStore(), env.getTypeBindings());
			
			return ResultFactory.makeResult(resultType, tree, eval);
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
