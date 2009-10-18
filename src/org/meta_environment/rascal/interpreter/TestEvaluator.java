package org.meta_environment.rascal.interpreter;

import java.util.List;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Test;
import org.meta_environment.rascal.ast.Test.Labeled;
import org.meta_environment.rascal.ast.Test.Unlabeled;
import org.meta_environment.rascal.interpreter.control_exceptions.Throw;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;

public class TestEvaluator {
	private final Evaluator eval;
	private final ITestResultListener testResultListener;
	
	public TestEvaluator(Evaluator eval, ITestResultListener testResultListener){
		super();
		
		this.eval = eval;
		this.testResultListener = testResultListener;
	}

	public void test(String moduleName) {
		Environment old = eval.getCurrentEnvt();
		
		try {
			ModuleEnvironment module = eval.getHeap().getModule(moduleName);
			if (module == null) {
				throw new IllegalArgumentException();
			}
			eval.setCurrentEnvt(module);
			
			test();
		}
		finally {
			if (old != null) {
				eval.setCurrentEnvt(old);
			}
		}
	}
	
	public void test() {
		ModuleEnvironment topModule = (ModuleEnvironment) eval.getCurrentEnvt().getRoot();
		
		runTests(topModule, topModule.getTests());
		
		for (String i : topModule.getImports()) {
			ModuleEnvironment mod = topModule.getImport(i);
			runTests(mod, mod.getTests());
		}
	}
	
	private void runTests(ModuleEnvironment env, List<Test> tests){
		Environment old = eval.getCurrentEnvt();
		try {
			eval.setCurrentEnvt(env);
			Visitor visitor = new Visitor();
			for(int i = tests.size() - 1; i >= 0; i--){
				tests.get(i).accept(visitor);
			}
		}
		finally {
			if (old != null) {
				eval.setCurrentEnvt(old);
			}
		}
	}
	
	private class Visitor extends NullASTVisitor<Result<IBool>>{
		
		public Visitor(){
			super();
		}
		
		public Result<IBool> visitTestLabeled(Labeled x){
			Result<IValue> result = ResultFactory.bool(true, eval);
//			System.err.println("visitTestLabeled: " + x);
			
			try{
				result = x.getExpression().accept(eval);
			}catch(Throw e){
				testResultListener.report(false, x.toString(), e);
			}catch(Throwable e){
				testResultListener.report(false, x.toString(), e);
			}
			
			testResultListener.report(result.isTrue(), x.toString());
			
			return ResultFactory.bool(result.isTrue(), eval);
		}
		
		public Result<IBool> visitTestUnlabeled(Unlabeled x){
			Result<IValue> result = ResultFactory.bool(true, eval);
//			System.err.println("visitTestUnlabeled: " + x);
			
			try{
				result = x.getExpression().accept(eval);
			}catch(Throw e){
				testResultListener.report(false, x.toString(), e);
			}catch(Throwable e){
				testResultListener.report(false, x.toString(), e);
			}
			
			testResultListener.report(result.isTrue(), x.toString());
			
			return ResultFactory.bool(result.isTrue(), eval);
		}
	}
}
